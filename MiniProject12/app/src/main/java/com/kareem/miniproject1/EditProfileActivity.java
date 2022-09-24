package com.kareem.miniproject1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class EditProfileActivity extends AppCompatActivity {

    String u;
    String p;
    String pn;
    TextInputLayout passwordTF;
    TextInputLayout phoneTF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        passwordTF = findViewById(R.id.newpasswordfield);
        phoneTF = findViewById(R.id.newphonefield);

        SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
        passwordTF.getEditText().setText(preferences.getString("sp-password", "data not found"));
        phoneTF.getEditText().setText(preferences.getString("sp-phone", "data not found"));

        Button confirmBtn = findViewById(R.id.confirmbutton);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u = preferences.getString("sp-username", "data not found");
                p = passwordTF.getEditText().getText().toString().trim();
                pn = phoneTF.getEditText().getText().toString().trim();
                saveToShared(u, p, pn);
                Toast.makeText(EditProfileActivity.this, "Details Saved", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(view.getContext(), MainActivity.class);
                startActivity(in);
                finish();
            }
        });

        Button deleteBtn = findViewById(R.id.deleteaccountbutton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditProfileActivity.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure to delete account data?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SharedPreferences preferences = getSharedPreferences("user_data", MODE_PRIVATE);
                                preferences.edit().clear().apply();
                                passwordTF.getEditText().setText(""); ;
                                phoneTF.getEditText().setText("");
                                Toast.makeText(EditProfileActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(view.getContext(), LoginActivity.class);
                                in.putExtra("input_username", "");
                                in.putExtra("input_password", "");
                                in.putExtra("input_phone", "");
                                startActivity(in);
                                finish();
                            }
                        }).setNegativeButton("No", null)
                        .show();
            }
        });
    }

    private void saveToShared(String username, String password, String phone) {
        SharedPreferences preferences = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("sp-username", username);
        editor.putString("sp-password", password);
        editor.putString("sp-phone", phone);
        editor.apply();
    }

}