package com.kareem.martzilla.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kareem.martzilla.R;

public class MoreUserDataActivity extends AppCompatActivity {

    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_more_user_data);

        saveBtn = findViewById(R.id.savebutton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(MoreUserDataActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
}