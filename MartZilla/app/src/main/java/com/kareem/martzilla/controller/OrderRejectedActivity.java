package com.kareem.martzilla.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kareem.martzilla.R;

public class OrderRejectedActivity extends AppCompatActivity {

    Button backHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_order_rejected);

        backHomeBtn = findViewById(R.id.backtohome);
        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OrderRejectedActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
}