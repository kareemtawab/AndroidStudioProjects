package com.kareem.martzilla.controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.model.products_db.ProductsDB;
import com.kareem.martzilla.model.user_data.SharedPreferences;
import com.kareem.martzilla.view.ProductItemTapInCartInterface;
import com.kareem.martzilla.view.ProductItemTapInterface;
import com.kareem.martzilla.view.ProductsAdapter;
import com.kareem.martzilla.view.ProductsAdapterCart;
import com.kareem.martzilla.view.ProductsAdapterSaves;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements ProductItemTapInCartInterface {

    RecyclerView rv;
    Button checkoutBtn;
    List<Products> productsList = new ArrayList<>();
    List<Products> itemsinCartList = new ArrayList<>();
    ProductsAdapterCart productsAdapterCart;
    ProductsDB productsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.cartRecyclerView);

        checkoutBtn = getActivity().findViewById(R.id.checkoutbutton);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog_checkout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button backBtn = dialog.findViewById(R.id.backbutton);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                Button okBtn = dialog.findViewById(R.id.okbutton);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Order Accepted", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(getActivity(), OrderAcceptedActivity.class);
                        startActivity(in);
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        readDB();
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
    public void deleteTap(Products products) {

    }

    @Override
    public void plusTap(Products products) {

    }

    @Override
    public void minusTap(Products products) {

    }

    @Override
    public void countChange(Products products) {

    }

    private void readDB() {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                productsDB = ProductsDB.getInstance(getActivity());
                itemsinCartList = productsDB.getProductDao().getAllProductsInCart();
                productsAdapterCart = new ProductsAdapterCart(itemsinCartList, CartFragment.this);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rv.setAdapter(productsAdapterCart);
                    }
                });
            }
        }).start();
    }
}