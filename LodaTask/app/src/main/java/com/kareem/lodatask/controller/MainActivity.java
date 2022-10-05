package com.kareem.lodatask.controller;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import com.kareem.lodatask.R;
import com.kareem.lodatask.model.Products;
import com.kareem.lodatask.model.products_rf.ProductsRFCallback;
import com.kareem.lodatask.model.products_rf.ProductsRFInterface;
import com.kareem.lodatask.view.ProductsAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ProductsRFInterface {

    RecyclerView rv1;
    ProductsAdapter productsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        rv1 = findViewById(R.id.rv1);
        rv1.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));

        ProductsRFCallback callback = new ProductsRFCallback(this);
        callback.getAllProducts();
    }

    @Override
    public void getProductsList(List<Products> productsList) {
        productsAdapter = new ProductsAdapter(productsList);
        rv1.setAdapter(productsAdapter);
    }
}