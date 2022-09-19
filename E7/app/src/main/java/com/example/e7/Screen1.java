package com.example.e7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class Screen1 extends AppCompatActivity {

    private String userName;
    private String passWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen1);

        TextView f1 = findViewById(R.id.field1);
        TextView f2 = findViewById(R.id.field2);

        //Intent in = new Intent (Screen1.this, LoginActivity.class);
        userName = getIntent().getStringExtra("username");
        passWord = getIntent().getStringExtra("password");

        f1.setText(userName);
        f2.setText(passWord);

    }

}