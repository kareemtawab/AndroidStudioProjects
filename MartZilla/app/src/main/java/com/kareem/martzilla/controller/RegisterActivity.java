package com.kareem.martzilla.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.kareem.martzilla.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUserName, edtEmail, edtPassword, edtConfirmPassword;
    private Button btnRegister;
    private TextView tvLogin;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        edtUserName = findViewById(R.id.editText_username);
        edtEmail = findViewById(R.id.editText_emailaddress);
        edtPassword = findViewById(R.id.editText_password);
        edtConfirmPassword = findViewById(R.id.editText_confirmpassword);
        btnRegister = findViewById(R.id.registerbutton);
        tvLogin = findViewById(R.id.loginbutton);
        progressBar = findViewById(R.id.progressbar);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(in);
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtUserName.getText().toString().isEmpty() && !edtEmail.getText().toString().isEmpty() && !edtPassword.getText().toString().isEmpty()) {
                    if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString()) && edtConfirmPassword.getText().toString().equals(edtPassword.getText().toString())){
                        progressBar.setVisibility(View.VISIBLE);
                        btnRegister.setEnabled(false);

                        firebaseAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(),
                                        edtPassword.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(RegisterActivity.this, "Successfully registered! Welcome to MartZilla!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(RegisterActivity.this, MoreUserDataActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this,
                                                    task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                            progressBar.setVisibility(View.INVISIBLE);
                                            btnRegister.setEnabled(true);
                                        }
                                    }
                                });
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Fields cannot be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}