package com.kareem.martzilla.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.user_data.SharedPreferences;

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPass;
    private Button btnLogin;
    private TextView tvRegister;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        firebaseAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.editText_emailaddress);
        edtPass = findViewById(R.id.editText_password);
        btnLogin = findViewById(R.id.loginbutton);
        tvRegister = findViewById(R.id.registerbutton);
        progressBar = findViewById(R.id.progressbar);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(in);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtPass.getText().toString().trim().isEmpty() && !edtEmail.getText().toString().trim().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    btnLogin.setEnabled(false);
                    firebaseAuth.signInWithEmailAndPassword(edtEmail.getText().toString().trim(),
                                    edtPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                        if (currentUser != null) {

                                            SharedPreferences sharedPreferences = new SharedPreferences();
                                            sharedPreferences.getInstance(LoginActivity.this);
                                            sharedPreferences.saveEmail(currentUser.getEmail());
                                            sharedPreferences.saveName(currentUser.getDisplayName());
                                            Toast.makeText(LoginActivity.this, "Logged in!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this,
                                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnLogin.setEnabled(true);
                                        }

                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        progressBar.setVisibility(View.INVISIBLE);
                                        btnLogin.setEnabled(true);
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}