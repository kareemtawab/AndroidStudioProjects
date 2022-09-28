package com.kareem.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    int count = 4;
    String text1;
    String text2;
    String username = "koko";
    String password = "wawa";
    long timeout = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.button);
        EditText e1 = findViewById(R.id.username);
        EditText e2 = findViewById(R.id.password);
        TextView err = findViewById(R.id.err);
        TextView timer = findViewById(R.id.time);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e1.getText().toString().trim().equalsIgnoreCase(username) && e2.getText().toString().equals(password)){
                    Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(count != 0){
                        count--;
                        b.setEnabled(true);
                    }
                    text2 = String.format(Locale.ENGLISH, "Invalid credentials. You have %d attempts left!", count);
                    err.setText(text2);
                    if (count == 0){

                        new CountDownTimer(timeout, 1000){
                            @Override
                            public void onTick(long l) {
                                text2 = String.format(Locale.ENGLISH,
                                        "Login disabled. Please try again in %02d:%02d minutes",
                                        TimeUnit.MILLISECONDS.toMinutes(l),
                                        TimeUnit.MILLISECONDS.toSeconds(l) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                                if (count == 0){
                                    timer.setText(text2);
                                }
                                b.setEnabled(false);
                            }
                            @Override
                            public void onFinish() {
                                count = 4;
                                err.setText("");
                                timer.setText("");
                                b.setEnabled(true);
                                timeout *= 2;
                            }
                        }.start();
                    }
                }
            }
        });
    }
}