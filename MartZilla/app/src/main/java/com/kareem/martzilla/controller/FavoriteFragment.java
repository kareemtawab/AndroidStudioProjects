package com.kareem.martzilla.controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    LinearLayout savedisempty;
    List<Products> productsList = new ArrayList<>();
    List<Products> itemsInSaves = new ArrayList<>();
    ProductsAdapterSaves productsAdapterSaves;
    SharedPreferences sharedPreferences;
    ProductsDB productsDB;
    boolean productInQuestionNewStatus;

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
        savedisempty = view.findViewById(R.id.savedisempty);
        readDB();

    }

    @Override
    public void onResume() {
        super.onResume();
        readDB();
    }

    @Override
    public void imageTap(Products products) {
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
        Toast.makeText(getActivity(), "Added to Cart", Toast.LENGTH_SHORT).show();
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList= productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID - 1);
                int productInQuestionOldInCartCount = productInQuestion.getInCartCount();
                productsDB.getProductDao().setCartCountForSingleProduct(productInQuestionID, productInQuestionOldInCartCount + 1);
            }
        }).start();
    }

    @Override
    public void favTap(Products products) {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList = productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID - 1);
                productInQuestionNewStatus = !productInQuestion.isSaved();
                productsDB.getProductDao().setSaveStatusForSingleProduct(productInQuestionID, productInQuestionNewStatus);
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (productInQuestionNewStatus){
                            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(), "Unsaved", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                readDB();
            }
        }).start();
    }

    private void readDB() {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                itemsInSaves = productsDB.getProductDao().getAllProductsInSaves();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (itemsInSaves.size() == 0){
                            savedisempty.setVisibility(View.VISIBLE);
                        }
                        else {
                            savedisempty.setVisibility(View.INVISIBLE);
                        }
                        productsAdapterSaves = new ProductsAdapterSaves(itemsInSaves, FavoriteFragment.this);
                        rv.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
                        rv.setAdapter(productsAdapterSaves);
                    }
                });
            }
        }).start();
    }
}