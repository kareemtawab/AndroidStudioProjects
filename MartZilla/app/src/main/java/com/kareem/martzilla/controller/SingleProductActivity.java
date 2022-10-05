package com.kareem.martzilla.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kareem.martzilla.R;

public class SingleProductActivity extends AppCompatActivity {

    ImageView productImage;
    TextView productTitle, productCategory, productDescription, productPrice, rateTV;
    Button addToCartBtn;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_single_product);

        productImage = findViewById(R.id.productpic);
        productTitle = findViewById(R.id.producttitle);
        productCategory = findViewById(R.id.productcategory);
        productDescription = findViewById(R.id.productdescription);
        productPrice = findViewById(R.id.productprice);
        addToCartBtn = findViewById(R.id.addtocartbutton);
        backBtn = findViewById(R.id.backbutton);
        rateTV = findViewById(R.id.rating);

        Glide.with(this)
                .load(getIntent().getStringExtra("product_image"))
                .into(productImage);
        productTitle.setText(getIntent().getStringExtra("product_title"));
        productCategory.setText(getIntent().getStringExtra("product_category"));
        productDescription.setText(getIntent().getStringExtra("product_description"));
        productPrice.setText(String.format("$%.2f", getIntent().getFloatExtra("product_price", 0.0f)));
        rateTV.setText(getIntent().getStringExtra("product_rate"));
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}