package com.kareem.martzilla.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.model.products_db.ProductsDB;
import com.kareem.martzilla.model.products_rf.ProductsRFCallback;
import com.kareem.martzilla.model.products_rf.ProductsAPIInterface;
import com.kareem.martzilla.model.products_rf.ProductsRFInterface;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements ProductsRFInterface {

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth firebaseAuth;
    boolean isUserSigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    isUserSigned = true;
                }
            }
        };

        ProductsRFCallback callback = new ProductsRFCallback(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.getAllProducts();
                if(isUserSigned) {
                    Toast.makeText(SplashActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, GetStartedActivity.class));
                }
                finish();
            }
        }, 1000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void getProductsList(List<Products> productsList) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ProductsDB productsDB = ProductsDB.getInstance(getApplicationContext());
                for (Products i : productsList){
                    productsDB.getProductDao().addProduct(new Products(
                            i.getId(),
                            i.getTitle(),
                            i.getDescription(),
                            i.getPrice(),
                            i.getCategory(),
                            i.getImage(),
                            i.getRating()));
                }
            }
        }).start();
    }
}