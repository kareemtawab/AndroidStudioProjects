package com.example.e6;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class Screen1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);
        Button backB1 = findViewById(R.id.backtohome1);

        String name = getIntent().getStringExtra("name");
        TextView nameF = findViewById(R.id.nametext);
        nameF.setText(name);

        Snackbar.make(findViewById(R.id.backtohome1), "Dismiss Me First!     >>>>", Snackbar.LENGTH_INDEFINITE).setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        }).show();

        backB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in1 = new Intent(Screen1.this, MainActivity.class);
                startActivity(in1);
            }
        });
    }
}