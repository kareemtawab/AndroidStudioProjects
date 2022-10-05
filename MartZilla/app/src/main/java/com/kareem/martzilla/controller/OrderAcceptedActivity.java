package com.kareem.martzilla.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.kareem.martzilla.R;

public class OrderAcceptedActivity extends AppCompatActivity {

    Button backHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_order_accepted);

        backHomeBtn = findViewById(R.id.backtohome);
        backHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OrderAcceptedActivity.this, MainActivity.class);
                startActivity(in);
                finish();
            }
        });
    }
}