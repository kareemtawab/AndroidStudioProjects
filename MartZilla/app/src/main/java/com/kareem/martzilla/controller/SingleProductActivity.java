package com.kareem.martzilla.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.model.products_db.ProductsDB;
import com.kareem.martzilla.view.ProductsAdapterCart;

public class SingleProductActivity extends AppCompatActivity {

    ImageView productImage, plus, minus, save;
    TextView productTitle, productCategory, productDescription, productPrice, rateTV;
    Button addToCartBtn;
    ImageView backBtn;
    EditText countET;
    ProductsDB productsDB;
    int productInQuestionNewCartCount;
    float subtotal;
    float vat = 1.14f;
    TextView subtotalTV;
    LinearLayout subtotlalayout;
    int id;

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
        plus = findViewById(R.id.plus);
        minus = findViewById(R.id.minus);
        save = findViewById(R.id.save);
        rateTV = findViewById(R.id.rating);
        countET = findViewById(R.id.itemcount);

        id = getIntent().getIntExtra("product_id", 1);
        countET.setText("1");
        Glide.with(this)
                .load(getIntent().getStringExtra("product_image"))
                .into(productImage);
        productTitle.setText(getIntent().getStringExtra("product_title"));
        productCategory.setText(getIntent().getStringExtra("product_category"));
        productDescription.setText(getIntent().getStringExtra("product_description"));
        productPrice.setText(String.format("$%.2f", getIntent().getFloatExtra("product_price", 0.0f)));
        rateTV.setText(getIntent().getStringExtra("product_rate"));

        if (getIntent().getBooleanExtra("product_save", false)) {
            save.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
        }
        else {
            save.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = Integer.parseInt(countET.getText().toString());
                productsDB = ProductsDB.getInstance(SingleProductActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (id < 1) id = 1;
                        int cartCount = productsDB.getProductDao().getAllProducts().get(id - 1).getInCartCount();
                        productInQuestionNewCartCount = cartCount + count;
                        productsDB.getProductDao().setCartCountForSingleProduct(id, productInQuestionNewCartCount);
                        productInQuestionNewCartCount = 1;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getBaseContext(), "Added", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                }).start();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                countET.setText(String.valueOf(Integer.parseInt(countET.getText().toString()) + 1));;
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Integer.parseInt(countET.getText().toString()) != 1){
                    countET.setText(String.valueOf(Integer.parseInt(countET.getText().toString()) - 1));;
                }
            }
        });
    }
}