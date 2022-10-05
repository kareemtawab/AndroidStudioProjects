package com.kareem.martzilla.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.model.products_db.ProductsDB;
import com.kareem.martzilla.model.user_data.SharedPreferences;
import com.kareem.martzilla.view.ProductItemTapInterface;
import com.kareem.martzilla.view.ProductsAdapterCart;
import com.kareem.martzilla.view.ProductsAdapterSaves;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment implements ProductItemTapInterface {

    RecyclerView rv;
    List<Products> productsList = new ArrayList<>();
    List<Products> itemsInSaves = new ArrayList<>();
    ProductsAdapterSaves productsAdapterSaves;
    SharedPreferences sharedPreferences;
    ProductsDB productsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.favoriteRecyclerView);

    }

    @Override
    public void onResume() {
        super.onResume();
        readDB();
    }

    @Override
    public void imageTap(Products products) {

    }

    @Override
    public void titleTap(Products products) {
        Intent in = new Intent(getActivity(), SingleProductActivity.class);
        in.putExtra("product_id", products.getId());
        in.putExtra("product_image", products.getImage());
        in.putExtra("product_title", products.getTitle());
        in.putExtra("product_category", products.getCategory());
        in.putExtra("product_description", products.getDescription());
        in.putExtra("product_price", products.getPrice());
        in.putExtra("product_rate", String.valueOf(products.getRating().getRate()));
        startActivity(in);
    }

    @Override
    public void cartTap(Products products) {
    }

    @Override
    public void favTap(Products products) {

        ProductsDB productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList= productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID);
                boolean productInQuestionOldStatus = productInQuestion.isSaved();
                productsDB.getProductDao().setSaveStatusForSingleProduct(productInQuestionID, !productInQuestionOldStatus);
            }
        }).start();
    }

    private void readDB() {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                productsDB = ProductsDB.getInstance(getActivity());
                itemsInSaves = productsDB.getProductDao().getAllProductsInCart();
                productsAdapterSaves = new ProductsAdapterSaves(itemsInSaves, FavoriteFragment.this);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rv.setAdapter(productsAdapterSaves);
                    }
                });
            }
        }).start();
    }
}