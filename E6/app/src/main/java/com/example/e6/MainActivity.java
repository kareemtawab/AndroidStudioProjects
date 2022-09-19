package com.example.e6;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    String name = "UNDEFINED!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int x;
        int z = 8;
        z = x + 5;

        TextView nameF = findViewById(R.id.namefield);
        Button TopB = findViewById(R.id.screen1_button);
        Button BotB = findViewById(R.id.screen2_button);

        Toast.makeText(MainActivity.this, "WELCOME!", Toast.LENGTH_LONG).show();


        nameF.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().equals("")){
                    name = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        TopB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in1 = new Intent(MainActivity.this, Screen1.class);
                in1.putExtra("name", name);
                startActivity(in1);
            }
        });

        BotB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in2 = new Intent(MainActivity.this, Screen2.class);
                startActivity(in2);
            }
        });
    }

}