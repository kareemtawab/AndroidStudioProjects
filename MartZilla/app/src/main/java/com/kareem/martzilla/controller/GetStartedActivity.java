package com.kareem.martzilla.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kareem.martzilla.R;

public class GetStartedActivity extends AppCompatActivity {

    private TextView martZillaTest;
    private Button btnGetStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_get_started);

        btnGetStarted = findViewById(R.id.getstartedbutton);
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(GetStartedActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

//        martZillaTest = findViewById(R.id.martzillabrand);
//        martZillaTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent in = new Intent(GetStartedActivity.this, MainActivity.class);
//                startActivity(in);
//                finish();
//            }
//        });
    }
}