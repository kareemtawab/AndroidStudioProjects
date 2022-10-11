package com.kareem.martzilla.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.user_data.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottom_nav;
    HomeFragment homeFragment = new HomeFragment();
    FavoriteFragment favoritesFragment = new FavoriteFragment();
    CartFragment cartFragment = new CartFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    ImageView logoutBtn;
    TextView loactionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        loactionTV = findViewById(R.id.location);
        SharedPreferences sharedPreferences = new SharedPreferences();
        sharedPreferences.getInstance(this);
        loactionTV.setText(sharedPreferences.getAddress());

        logoutBtn = findViewById(R.id.logoutbutton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this).setTitle("Confirmation").setMessage("Are you sure you to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();

                        Intent in = new Intent(MainActivity.this, GetStartedActivity.class);
                        startActivity(in);
                        finishAffinity();
                    }
                }).setNegativeButton("No", null).show();
            }
        });

        try {
            String frag = getIntent().getExtras().getString("frag");
            switch(frag){
                case "profile":
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, profileFragment).commit();
                    break;
            }
        }
        catch (Exception e){

        }

        bottom_nav = findViewById(R.id.bottomNav);
        bottom_nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int i = item.getItemId();
                switch (i){
                    case R.id.home_bottomnav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, homeFragment).commit();
                        return true;
                    case R.id.favorite_bottomnav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, favoritesFragment).commit();
                        return true;
                    case R.id.cart_bottomnav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, cartFragment).commit();
                        return true;
                    case R.id.profile_bottomnav:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = new SharedPreferences();
        sharedPreferences.getInstance(this);
        loactionTV.setText(sharedPreferences.getAddress());
    }
}