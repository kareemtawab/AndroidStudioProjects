package com.kareem.watermarkplacer;

import static android.media.MediaMetadataRetriever.METADATA_KEY_DURATION;
import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.arthenica.ffmpegkit.FFmpegKit;
import com.arthenica.ffmpegkit.FFmpegKitConfig;
import com.arthenica.ffmpegkit.FFmpegSession;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.Statistics;
import com.arthenica.ffmpegkit.StatisticsCallback;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private final static int CAPTURE_CODE = 19;
    private final static int PICK_PHOTO_CODE = 20;
    private final static int PICK_VIDEO_CODE = 21;
    private final static int OUT_VIDEO_HEIGHT = 640;
    private int mediaType = 0;
    private int renderProgress = 0;

    LinearLayout noMediaLayout;
    ImageView imageView;
    Button captureBtn, loadvideoBtn, loadphotoBtn, saveBtn;
    VideoView videoView;
    ProgressBar loadingCircle;
    Dialog waitDialog;

    Bitmap imagewithwatermarkHW;
    Uri rawVideoUri;
    Uri videoWithWatermarkUri;
    Uri watermarkUri;
    File filesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // wake-lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        noMediaLayout = findViewById(R.id.nomedia);
        imageView = findViewById(R.id.photopreview);
        videoView = findViewById(R.id.videopreview);
        captureBtn = findViewById(R.id.capturebutton);
        loadvideoBtn = findViewById(R.id.loadvideobutton);
        loadphotoBtn = findViewById(R.id.loadphotobutton);
        saveBtn = findViewById(R.id.savemediabutton);
        loadingCircle = findViewById(R.id.loading);

        imageView.setVisibility(View.INVISIBLE);
        videoView.setVisibility(View.INVISIBLE);

        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noMediaLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                mediaType = 0;
                if (checkCameraPermissions()){
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult (cameraIntent, CAPTURE_CODE);
                };
            }
        });

        loadphotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noMediaLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                mediaType = 0;
                if(checkReadStoragePermissions()){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, PICK_PHOTO_CODE);
                    }
                };
            }
        });

        loadvideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noMediaLayout.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                videoView.setVisibility(View.INVISIBLE);
                mediaType = 0;
                if(checkReadStoragePermissions()){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, PICK_VIDEO_CODE);
                    }
                };
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/WP");
                file.mkdir();
                if (checkWriteStoragePermissions()){
                    switch (mediaType){
                        case 0: // no media
                            Toast.makeText(MainActivity.this, "No media loaded. Please load media!", Toast.LENGTH_LONG).show();
                            break;
                         case 1: // photo
                             saveBtn.setEnabled(false);
                             loadingCircle.setVisibility(View.VISIBLE);
                             new Thread(new Runnable() {
                                 @Override
                                 public void run() {
                                     try {
                                         FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath() + "/WP_" + System.currentTimeMillis() + ".jpg");
                                         imagewithwatermarkHW.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                         fileOutputStream.flush();
                                         fileOutputStream.close();
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 Toast.makeText(MainActivity.this, "Photo saved!", Toast.LENGTH_LONG).show();
                                                 saveBtn.setEnabled(true);
                                                 loadingCircle.setVisibility(View.INVISIBLE);
                                             }
                                         });
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 Toast.makeText(MainActivity.this, "Error saving file!", Toast.LENGTH_LONG).show();
                                             }
                                         });
                                     }
                                 }
                             }).start();
                            break;
                         case 2: // video
                             saveBtn.setEnabled(false);
                             loadingCircle.setVisibility(View.VISIBLE);
                             Path src = null;
                             Path dest = null;
                             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                 src = Paths.get(videoWithWatermarkUri.getPath());
                                 dest = Paths.get(file.getAbsolutePath() + "/WP_" + System.currentTimeMillis() + ".mp4");
                                 try {
                                     Files.copy(src, dest);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }}
                             Toast.makeText(MainActivity.this, "Video saved!", Toast.LENGTH_LONG).show();
                             saveBtn.setEnabled(true);
                             loadingCircle.setVisibility(View.INVISIBLE);
                             break;
                    }
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
                }
                else {
                    Toast.makeText(MainActivity.this, "Permission Error!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((data != null) && requestCode == CAPTURE_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            imagewithwatermarkHW = image.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap imageHW = image.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(imagewithwatermarkHW);
            canvas.drawBitmap(imageHW, 0, 0, null);

            Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.guru_watermark);
            if (image.getWidth() >= image.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, image.getHeight() / 4, image.getHeight() / 4, false);
            }
            if (image.getWidth() < image.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, image.getWidth() / 4, image.getWidth() / 4, false);
            }
            int startX = (canvas.getWidth() - waterMark.getWidth()) - 50;
            int startY = (canvas.getHeight() - waterMark.getHeight()) - 50;
            canvas.drawBitmap(waterMark,startX,startY,null);

            noMediaLayout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imagewithwatermarkHW);
            mediaType = 1;
        }
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            Bitmap image = null;
            try {
                // check version of Android on device
                if(Build.VERSION.SDK_INT > 27){
                    // on newer versions of Android, use the new decodeBitmap method
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), photoUri);
                    image = ImageDecoder.decodeBitmap(source);
                } else {
                    // support older versions of Android by using getBitmap
                    image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.guru_watermark);
            if (image.getWidth() >= image.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, image.getHeight() / 4, image.getHeight() / 4, false);
            }
            if (image.getWidth() < image.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, image.getWidth() / 4, image.getWidth() / 4, false);
            }

            imagewithwatermarkHW = image.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap imageHW = image.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(imagewithwatermarkHW);
            canvas.drawBitmap(imageHW, 0, 0, null);

            int startX = (canvas.getWidth() - waterMark.getWidth()) - 50;
            int startY = (canvas.getHeight() - waterMark.getHeight()) - 50;
            canvas.drawBitmap(waterMark,startX,startY,null);

            noMediaLayout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imagewithwatermarkHW);
            mediaType = 1;
        }
        if ((data != null) && requestCode == PICK_VIDEO_CODE) {
            waitDialog = new Dialog(MainActivity.this);
            waitDialog.setContentView(R.layout.custom_dialog_wait);
            waitDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            waitDialog.show();
            TextView progress = waitDialog.findViewById(R.id.progressstats);
            Button stopExec = waitDialog.findViewById(R.id.abort);
            stopExec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FFmpegKit.cancel();
                    waitDialog.cancel();
                    renderProgress = 0;
                    noMediaLayout.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    videoView.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this, "Aborted by user!", Toast.LENGTH_LONG).show();
                }
            });
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // create app data directory then create empty watermark file stream
                    filesDir = new File(String.valueOf(getFilesDir()));
                    filesDir.mkdir();
                    FileOutputStream fileOutputStream = null;

                    // get videos' uri
                    rawVideoUri = data.getData();
                    videoWithWatermarkUri = Uri.parse(filesDir.getAbsolutePath() + "/temp.mp4");

                    // get watermark size with respect to the raw video aspect ratio and save it as png file in app data folder and get uri
                    Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.guru_watermark);

                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                    metaRetriever.setDataSource(MainActivity.this, rawVideoUri);

                    Bitmap bmp = metaRetriever.getFrameAtTime(0);
                    int rawVideoWidth = bmp.getWidth();
                    int rawVideoHeight = bmp.getHeight();
                    Log.e(TAG, "Raw video dimensions: " + rawVideoWidth + "x" + rawVideoHeight);

                    int outVideoWidth = (int)((float)rawVideoWidth / (float)rawVideoHeight * (float)OUT_VIDEO_HEIGHT);
                    int outVideoHeight = OUT_VIDEO_HEIGHT;
                    if (rawVideoWidth >= rawVideoHeight){
                        waterMark = Bitmap.createScaledBitmap(waterMark, outVideoHeight / 4, outVideoHeight / 4, false);
                    }
                    if (rawVideoWidth < rawVideoHeight){
                        waterMark = Bitmap.createScaledBitmap(waterMark, outVideoWidth / 4, outVideoWidth / 4, false);
                    }

                    // save watermark as png in app data folder
                    try {
                        fileOutputStream = new FileOutputStream(filesDir.getAbsolutePath() + "/watermark.png");
                        waterMark.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        watermarkUri = Uri.parse(filesDir.getAbsolutePath() + "/watermark.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // get render FFmpeg progress
                    FFmpegKitConfig.enableStatisticsCallback(new StatisticsCallback() {
                        @Override
                        public void apply(final Statistics newStatistics) {
                            renderProgress = 100 * newStatistics.getVideoFrameNumber() / Integer.parseInt(metaRetriever.extractMetadata(METADATA_KEY_VIDEO_FRAME_COUNT));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setText(String.format("Progress: %d%%", renderProgress));
                            }
                        });
                        }
                    });

                    // get ffmpeg argumented command
                    String ffmpegCommand = "-y"
                            + " -i " + FFmpegKitConfig.getSafParameterForRead(MainActivity.this, rawVideoUri)
                            + " -i " + watermarkUri.getPath()
//                            + " -filter_complex \"[0:v]scale=-1:" + OUT_VIDEO_HEIGHT + "[bg];[bg][1:v]overlay=0:0\""
//                            + " -filter_complex \"[0:v]scale=-1:" + OUT_VIDEO_HEIGHT + "[bg];[bg][1:v]overlay=" + (outVideoWidth - 10 - waterMark.getWidth()) + ":" + (outVideoHeight - 10 - waterMark.getHeight()) + "\""
                            + " -filter_complex \"[0:v]scale=-1:" + OUT_VIDEO_HEIGHT + "[bg];[bg][1:v]overlay=x='if(lt(mod(t\\,20)\\,10)\\,W-w-W*2/100\\,W*2/100)':y='if(lt(mod(t+5\\,20)\\,10)\\,H-h-H*2/100\\,H*2/100)'\""
                            + " -preset ultrafast "
                            + filesDir.getAbsolutePath() + "/temp.mp4";
                    Log.e(TAG, "FFMPEG Command: " + ffmpegCommand);

                    // run ffmpeg command
                    FFmpegSession session1 = FFmpegKit.execute(ffmpegCommand);
                    if (ReturnCode.isSuccess(session1.getReturnCode())) {
                        waitDialog.cancel();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Successfully rendered video!", Toast.LENGTH_LONG).show();
                                noMediaLayout.setVisibility(View.INVISIBLE);
                                imageView.setVisibility(View.INVISIBLE);
                                videoView.setVisibility(View.VISIBLE);

                                MediaController mediaController = new MediaController(MainActivity.this);
                                mediaController.setAnchorView(videoView);
                                videoView.setMediaController(mediaController);

                                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mp) {
                                        videoView.stopPlayback();
                                        videoView.setVideoURI(videoWithWatermarkUri);
                                        videoView.start();
                                    }
                                });
                                videoView.setVideoURI(videoWithWatermarkUri);
                                videoView.start();
                            }
                        });
                        mediaType = 2;
                    }
                }
            }).start();
        }
    }

    private boolean checkReadStoragePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
        else {
//            Toast.makeText(this, "EXTERNAL STORAGE PERMISSIONS GRANTED", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private boolean checkWriteStoragePermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return false;
        }
        else {
//            Toast.makeText(this, "EXTERNAL STORAGE PERMISSIONS GRANTED", Toast.LENGTH_LONG).show();
            return true;
        }
    }

    private boolean checkCameraPermissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
            return false;
        }
        else {
//            Toast.makeText(this, "CAMERA PERMISSION GRANTED", Toast.LENGTH_LONG).show();
            return true;
        }
    }
}