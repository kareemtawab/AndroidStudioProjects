package com.example.e6.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.e6.MainActivity;
import com.example.e6.R;

public class Screen2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen2);
        Button backB2 = findViewById(R.id.backtohome2);

        backB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Screen2.this).setTitle("Confirmation").setMessage("Do you want to go to the main screen?").setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent in2 = new Intent(Screen2.this, MainActivity.class);
                        startActivity(in2);
                    }
                }).setNegativeButton("Deny", null).show();
            }
        });
    }
}