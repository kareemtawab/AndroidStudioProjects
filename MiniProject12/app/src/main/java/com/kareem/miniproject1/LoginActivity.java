package com.kareem.miniproject1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout usernameTF;
    TextInputLayout passwordTF;
    TextInputLayout phoneTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button RegisterBtn = findViewById(R.id.registerbutton);
        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameTF = findViewById(R.id.usernamefield);
                passwordTF = findViewById(R.id.passwordfield);
                phoneTF = findViewById(R.id.phonefield);
                String u = usernameTF.getEditText().getText().toString().trim();
                String p = passwordTF.getEditText().getText().toString().trim();
                String pn = phoneTF.getEditText().getText().toString().trim();

                if (u.equalsIgnoreCase("")) {
                    usernameTF.setError("This field can not be blank");
                }
                if (p.equalsIgnoreCase("")) {
                    passwordTF.setError("This field can not be blank");
                }
                if (pn.equalsIgnoreCase("")) {
                    phoneTF.setError("This field can not be blank");
                }
                if (!u.equalsIgnoreCase("") && !p.equalsIgnoreCase("") && !pn.equalsIgnoreCase("")){
                    saveToShared(u, p, pn);
                    Intent in = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }
            }
        });
    }

    private void saveToShared(String username, String password, String phone) {
        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sp-username", username);
        editor.putString("sp-password", password);
        editor.putString("sp-phone", phone);
        editor.apply();

        Toast.makeText(this, "Account Updated", Toast.LENGTH_SHORT).show();

        usernameTF.getEditText().setText("");
        passwordTF.getEditText().setText("");
        phoneTF.getEditText().setText("");
    }

}