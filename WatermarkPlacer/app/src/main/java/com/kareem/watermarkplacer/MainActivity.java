package com.kareem.watermarkplacer;

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
import com.arthenica.ffmpegkit.FFmpegSessionCompleteCallback;
import com.arthenica.ffmpegkit.LogCallback;
import com.arthenica.ffmpegkit.ReturnCode;
import com.arthenica.ffmpegkit.SessionState;
import com.arthenica.ffmpegkit.Statistics;
import com.arthenica.ffmpegkit.StatisticsCallback;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    private int timeInSecsBetweenWatermarkShifts = 5;
    private double visibleWatermarkShiftCycles = 2.5;
    private long rawVideoDuration;
    private int endingVideoWidth, endingVideoHeight, rawVideoWidth, rawVideoHeight, outVideoWidth, outVideoHeight;

    private final static int defaultTimeInSecsBetweenWatermarkShifts = 5;
    private final static int CAPTURE_CODE = 19;
    private final static int PICK_PHOTO_CODE = 20;
    private final static int PICK_VIDEO_CODE = 21;
    private final static int OUT_VIDEO_HEIGHT = 640;
    private int mediaType = 0;
    private int renderProgress = 0;
    private boolean cancelAllRenders = false;

    LinearLayout noMediaLayout;
    ImageView imageView;
    Button captureBtn, loadvideoBtn, loadphotoBtn, saveBtn;
    VideoView videoView;
    ProgressBar loadingCircle;
    Dialog waitDialog;
    TextView progress;

    Bitmap imageFromCamera, imageWithWatermarkHW;
    Uri videoRawUri;
    Uri videoEndingFromResourcesUri;
    Uri videoEndingReadyForRenderUri;
    Uri videoWithWatermarkUri;
    Uri videoFinalUri;
    Uri watermarkUri;
    File filesDir;

    FFmpegSession endingVideoRender, mainVideoRender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        // wake-lock
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // create app data folder
        filesDir = new File(String.valueOf(getFilesDir()));
        filesDir.mkdir();

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
                    // start default camera
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult (cameraIntent, CAPTURE_CODE);

                }
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
                    saveBtn.setEnabled(false);
                    saveBtn.setText("Saving. Please Wait!");
                    loadingCircle.setVisibility(View.VISIBLE);
                    switch (mediaType){
                        case 0: // no media
                            Toast.makeText(MainActivity.this, "No media loaded. Please load media!", Toast.LENGTH_LONG).show();
                            break;
                         case 1: // photo
                             new Thread(new Runnable() {
                                 @Override
                                 public void run() {
                                     try {
                                         FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsolutePath() + "/WP_" + System.currentTimeMillis() + ".jpg");
                                         imageWithWatermarkHW.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
                                         fileOutputStream.flush();
                                         fileOutputStream.close();
                                     } catch (IOException e) {
                                         e.printStackTrace();
                                     }
                                     runOnUiThread(new Runnable() {
                                         @Override
                                         public void run() {
                                             Toast.makeText(MainActivity.this, "Photo Saved to Gallery!", Toast.LENGTH_LONG).show();
                                             saveBtn.setEnabled(true);
                                             saveBtn.setText("Save to Internal Storage");
                                             loadingCircle.setVisibility(View.INVISIBLE);
                                         }
                                     });
                                 }
                             }).start();
                            break;
                         case 2: // video
                             new Thread(new Runnable() {
                                 Path src = null;
                                 Path dest = null;
                                 @Override
                                 public void run() {
                                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                         src = Paths.get(videoFinalUri.getPath());
                                         dest = Paths.get(file.getAbsolutePath() + "/WP_" + System.currentTimeMillis() + ".mp4");
                                         try {
                                             Files.copy(src, dest);
                                         } catch (IOException e) {
                                             e.printStackTrace();
                                         }
                                         runOnUiThread(new Runnable() {
                                             @Override
                                             public void run() {
                                                 Toast.makeText(MainActivity.this, "Video Saved to Gallery!", Toast.LENGTH_LONG).show();
                                                 saveBtn.setEnabled(true);
                                                 saveBtn.setText("Save to Internal Storage");
                                                 loadingCircle.setVisibility(View.INVISIBLE);
                                             }
                                         });
                                     }
                                 }
                             }).start();
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

            imageFromCamera = (Bitmap) data.getExtras().get("data");

            imageWithWatermarkHW = imageFromCamera.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap imageHW = imageFromCamera.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(imageWithWatermarkHW);
            canvas.drawBitmap(imageHW, 0, 0, null);

            Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.tiktok_watermark);
            if (imageFromCamera.getWidth() >= imageFromCamera.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, imageFromCamera.getHeight() / 4, imageFromCamera.getHeight() / 4, false);
            }
            if (imageFromCamera.getWidth() < imageFromCamera.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, imageFromCamera.getWidth() / 4, imageFromCamera.getWidth() / 4, false);
            }
            int startX = (canvas.getWidth() - waterMark.getWidth()) - canvas.getWidth() / 50;
            int startY = (canvas.getHeight() - waterMark.getHeight()) - canvas.getHeight() / 50;
            canvas.drawBitmap(waterMark,startX,startY,null);

            noMediaLayout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imageWithWatermarkHW);

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

            Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.tiktok_watermark);
            if (image.getWidth() >= image.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, image.getHeight() / 4, image.getHeight() / 4, false);
            }
            if (image.getWidth() < image.getHeight()){
                waterMark = Bitmap.createScaledBitmap(waterMark, image.getWidth() / 4, image.getWidth() / 4, false);
            }

            imageWithWatermarkHW = image.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap imageHW = image.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(imageWithWatermarkHW);
            canvas.drawBitmap(imageHW, 0, 0, null);

            int startX = (canvas.getWidth() - waterMark.getWidth()) - canvas.getWidth() / 50;
            int startY = (canvas.getHeight() - waterMark.getHeight()) - canvas.getHeight() / 50;
            canvas.drawBitmap(waterMark,startX,startY,null);

            noMediaLayout.setVisibility(View.INVISIBLE);
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.INVISIBLE);
            imageView.setImageBitmap(imageWithWatermarkHW);
            mediaType = 1;
        }
        if ((data != null) && requestCode == PICK_VIDEO_CODE) {
            waitDialog = new Dialog(MainActivity.this);
            waitDialog.setContentView(R.layout.custom_dialog_wait);
            waitDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            waitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            waitDialog.show();
            progress = waitDialog.findViewById(R.id.progressstats);
            Button stopExec = waitDialog.findViewById(R.id.abort);
            stopExec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelAllRenders = true;
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
                    // create empty watermark file stream
                    FileOutputStream fileOutputStream = null;

                    // get videos' uri
                    videoRawUri = data.getData();

                    extractRawResource(R.raw.ending, filesDir.getAbsolutePath(), "endingRaw.mp4");
                    videoEndingFromResourcesUri = Uri.parse(filesDir.getAbsolutePath() + "/endingRaw.mp4");
                    videoEndingReadyForRenderUri = Uri.parse(filesDir.getAbsolutePath() + "/endingFinal.mp4");

                    videoWithWatermarkUri = Uri.parse(filesDir.getAbsolutePath() + "/temp.mp4");

                    videoFinalUri = Uri.parse(filesDir.getAbsolutePath() + "/final.mp4");

                    // get watermark size with respect to the raw video aspect ratio and save it as png file in app data folder and get uri
                    Bitmap waterMark = BitmapFactory.decodeResource(getResources(), R.drawable.tiktok_watermark);

                    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                    metaRetriever.setDataSource(MainActivity.this, videoRawUri);

                    rawVideoDuration = Long.parseLong(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
                    rawVideoHeight = metaRetriever.getFrameAtTime(0).getHeight();
                    rawVideoWidth = metaRetriever.getFrameAtTime(0).getWidth();
                    Log.e(TAG, "Raw video dimensions: " + rawVideoWidth + "x" + rawVideoHeight);

                    metaRetriever.setDataSource(MainActivity.this, videoEndingFromResourcesUri);

                    endingVideoHeight = metaRetriever.getFrameAtTime(0).getHeight();
                    endingVideoWidth = metaRetriever.getFrameAtTime(0).getWidth();
                    Log.e(TAG, "Ending video dimensions: " + endingVideoWidth + "x" + endingVideoHeight);

                    outVideoWidth = (int)((float)rawVideoWidth / (float)rawVideoHeight * (float)OUT_VIDEO_HEIGHT);
                    outVideoHeight = OUT_VIDEO_HEIGHT;

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

                    //calculate non-default watermark shift cycles time
                    if (rawVideoDuration < timeInSecsBetweenWatermarkShifts * visibleWatermarkShiftCycles){
                        timeInSecsBetweenWatermarkShifts = (int)((double)rawVideoDuration / visibleWatermarkShiftCycles);
                    }
                    else {
                        timeInSecsBetweenWatermarkShifts = defaultTimeInSecsBetweenWatermarkShifts;
                    }
                    // render process starts here
                    renderEndingVideoFun();
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

    void renderEndingVideoFun(){

        // get ffmpeg argumented command for preparing ending video
        String ffmpegCommandForEndingVideo = "-y"
                + " -i " + videoEndingFromResourcesUri.getPath() + " -r 30"
                + " -vf \""
                + "scale=w=" + outVideoWidth + ":h=" + outVideoHeight + ":force_original_aspect_ratio=1, "
                + "pad="+ outVideoWidth + ":" + outVideoHeight + ":" + (outVideoWidth - (endingVideoWidth * outVideoWidth / outVideoHeight))/2 + ":" + outVideoHeight/2 + ":0x0E0E1A, "
                + "drawtext=fontfile=/system/fonts/Roboto-Italic.ttf:text='@3dpguru':fontcolor=white:fontsize=18:box=1:boxcolor=black@0.1:boxborderw=5:x=(w-text_w)/2-30:y=(h-text_h)/2+46"
                + "\""
                + " -framerate 30 -b:v 512k -b:a 128k -vsync 2 "
                + "-s " + outVideoWidth + "x" + outVideoHeight + " "
                + filesDir.getAbsolutePath() + "/endingFinal.mp4";
        Log.e(TAG, "ffmpeg 1/3 command: " + ffmpegCommandForEndingVideo);

        // run ffmpeg command for ending video
        endingVideoRender = FFmpegKit.executeAsync(ffmpegCommandForEndingVideo, new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                // CALLED WHEN SESSION IS EXECUTED
                SessionState state = session.getState();
                ReturnCode returnCode = session.getReturnCode();
                Log.e(TAG, String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.getFailStackTrace()));
            }
        }, new LogCallback() {

            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) {
                // CALLED WHEN SESSION PRINTS LOGS
                Log.i(TAG, "ffmpeg 1/3 log: " + log.getMessage());
            }
        }, new StatisticsCallback() {
            @Override
            public void apply(Statistics statistics) {
                // CALLED WHEN SESSION GENERATES STATISTICS
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(MainActivity.this, videoEndingFromResourcesUri);
                renderProgress = 100 * statistics.getVideoFrameNumber() / Integer.parseInt(metaRetriever.extractMetadata(METADATA_KEY_VIDEO_FRAME_COUNT));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setText(String.format("Task 1/3, Progress: %d%%", renderProgress));
                    }
                });
            }
        });

        FFmpegKitConfig.enableFFmpegSessionCompleteCallback(new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                if (!cancelAllRenders){
                    renderMainVideoFun();
                }
                else{
                    cancelAllRenders = false;
                }
            }
        });
    }

    void renderMainVideoFun(){

        // get ffmpeg argumented command for preparing main video
        String ffmpegCommand = "-y"
                + " -i " + FFmpegKitConfig.getSafParameterForRead(MainActivity.this, videoRawUri) + " -r 30"
                + " -i " + watermarkUri.getPath() + " -r 30"
//                            + " -filter_complex \"[0:v]scale=-1:" + outVideoHeight + "[bg];[bg][1:v]overlay=0:0\""
//                            + " -filter_complex \"[0:v]scale=-1:" + outVideoHeight + "[bg];[bg][1:v]overlay=" + (outVideoWidth - 10 - waterMark.getWidth()) + ":" + (outVideoHeight - 10 - waterMark.getHeight()) + "\""
                + " -filter_complex \""
                + "[0:v]scale=" + outVideoWidth + ":" + outVideoHeight + "[v0]; "
                + "[v0][1:v]overlay=x='if(lt(mod(t,10)," + timeInSecsBetweenWatermarkShifts + "),10,W-w-W*2/100)':y='if(lt(mod(t,10)," + timeInSecsBetweenWatermarkShifts + "),10,H-h-H*2/100)'"
                + "\""
                + " -framerate 30 -b:v 512k -b:a 128k -vsync 2 "
                + "-s " + outVideoWidth + "x" + outVideoHeight + " "
                + filesDir.getAbsolutePath() + "/temp.mp4";
        Log.e(TAG, "ffmpeg 2/3 command: " + ffmpegCommand);

        // run ffmpeg command for main video
        mainVideoRender = FFmpegKit.executeAsync(ffmpegCommand, new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                // CALLED WHEN SESSION IS EXECUTED
                SessionState state = session.getState();
                ReturnCode returnCode = session.getReturnCode();
                Log.e(TAG, String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.getFailStackTrace()));
            }
        }, new LogCallback() {

            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) {
                // CALLED WHEN SESSION PRINTS LOGS
                Log.i(TAG, "ffmpeg 2/3 log: " + log.getMessage());
            }
        }, new StatisticsCallback() {

            @Override
            public void apply(Statistics statistics) {
                // CALLED WHEN SESSION GENERATES STATISTICS
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(MainActivity.this, videoRawUri);
                renderProgress = 100 * statistics.getVideoFrameNumber() / Integer.parseInt(metaRetriever.extractMetadata(METADATA_KEY_VIDEO_FRAME_COUNT));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setText(String.format("Task 2/3, Progress: %d%%", renderProgress));
                    }
                });
            }
        });

        FFmpegKitConfig.enableFFmpegSessionCompleteCallback(new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                if (!cancelAllRenders){
                    concatVideosFun();
                }
                else{
                    cancelAllRenders = false;
                }
            }
        });
    }

    void concatVideosFun(){

        // get ffmpeg argumented command for preparing final video
        String ffmpegConcatCommand = "-y"
                + " -i " + videoWithWatermarkUri.getPath() + " -r 30"
                + " -i " + videoEndingReadyForRenderUri.getPath() + " -r 30"
                + " -filter_complex concat=n=2:v=1:a=1"
                + " -framerate 30 -b:v 512k -b:a 128k -vsync 2 "
                + "-s " + outVideoWidth + "x" + outVideoHeight + " "
                + filesDir.getAbsolutePath() + "/final.mp4";
        Log.e(TAG, "ffmpeg 3/3 command: " + ffmpegConcatCommand);

        // run ffmpeg command for main video
        mainVideoRender = FFmpegKit.executeAsync(ffmpegConcatCommand, new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                // CALLED WHEN SESSION IS EXECUTED
                SessionState state = session.getState();
                ReturnCode returnCode = session.getReturnCode();
                Log.e(TAG, String.format("FFmpeg process exited with state %s and rc %s.%s", state, returnCode, session.getFailStackTrace()));
            }
        }, new LogCallback() {

            @Override
            public void apply(com.arthenica.ffmpegkit.Log log) {
                // CALLED WHEN SESSION PRINTS LOGS
                Log.i(TAG, "ffmpeg 3/3 log: " + log.getMessage());
            }
        }, new StatisticsCallback() {

            @Override
            public void apply(Statistics statistics) {
                // CALLED WHEN SESSION GENERATES STATISTICS
                MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
                metaRetriever.setDataSource(MainActivity.this, videoWithWatermarkUri);
                renderProgress = Integer.parseInt(metaRetriever.extractMetadata(METADATA_KEY_VIDEO_FRAME_COUNT));
                metaRetriever.setDataSource(MainActivity.this, videoEndingReadyForRenderUri);
                renderProgress += Integer.parseInt(metaRetriever.extractMetadata(METADATA_KEY_VIDEO_FRAME_COUNT));
                renderProgress = 100 * statistics.getVideoFrameNumber() / renderProgress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress.setText(String.format("Task 3/3, Progress: %d%%", renderProgress));
                    }
                });
            }
        });

        FFmpegKitConfig.enableFFmpegSessionCompleteCallback(new FFmpegSessionCompleteCallback() {
            @Override
            public void apply(FFmpegSession session) {
                waitDialog.cancel();
                cancelAllRenders = false;
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
                                videoView.setVideoURI(videoFinalUri);
                                videoView.start();
                            }
                        });
                        videoView.setVideoURI(videoFinalUri);
                        videoView.start();
                    }
                });
                mediaType = 2;
            }
        });
    }

    private void extractRawResource(int resourceId, String path, String resourceName){
        String pathFinal = path + "/" + resourceName;
        try{
            InputStream in = getResources().openRawResource(resourceId);
            FileOutputStream out = null;
            out = new FileOutputStream(pathFinal);
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}