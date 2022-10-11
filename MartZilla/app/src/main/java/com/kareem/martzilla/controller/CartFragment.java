package com.kareem.martzilla.controller;

import static android.content.ContentValues.TAG;

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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
    int productInQuestionNewCartCount;
    float subtotal;
    float vat = 1.14f;
    TextView subtotalTV;
    LinearLayout subtotlalayout;
    LinearLayout cartisempty;

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
        subtotlalayout = view.findViewById(R.id.subtotal);
        checkoutBtn = getActivity().findViewById(R.id.checkoutbutton);
        subtotalTV = getActivity().findViewById(R.id.subtotalprice);
        cartisempty = getActivity().findViewById(R.id.cartisempty);
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog_checkout);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                TextView subtotalcheckout = dialog.findViewById(R.id.subtotalprice);
                TextView totalcheckout = dialog.findViewById(R.id.totalprice);
                subtotalcheckout.setText(String.format("$%.2f", getSubtotal()));
                totalcheckout.setText(String.format("$%.2f", getSubtotal() * vat));

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
                        SharedPreferences sharedPreferences = new SharedPreferences();
                        sharedPreferences.getInstance(getActivity());
                        if (!sharedPreferences.getAddress().equals("undefined") || !sharedPreferences.getPhoneNumber().equals("undefined")){
                            productsDB = ProductsDB.getInstance(getActivity());
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    productsDB.getProductDao().setCartCountAtZeroForAllProducts();
                                }
                            }).start();
                            Toast.makeText(getActivity(), "Order Accepted", Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getActivity(), OrderAcceptedActivity.class);
                            startActivity(in);
                        }
                        else{
                            Toast.makeText(getActivity(), "Error: Please update your location\n" +
                                    "and phone number first in profile page\n" +
                                    "before submitting new orders.", Toast.LENGTH_LONG).show();
                        }
                        dialog.cancel();
                    }
                });
                dialog.show();
            }
        });
        readDB();
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
    public void deleteTap(Products products) {
        Toast.makeText(getActivity(), "Deleted from Cart", Toast.LENGTH_SHORT).show();
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList = productsDB.getProductDao().getAllProducts();
                productsDB.getProductDao().setCartCountForSingleProduct(productInQuestionID, 0);
                readDB();
            }
        }).start();
    }

    @Override
    public void plusTap(Products products) {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList = productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID - 1);
                productInQuestionNewCartCount = productInQuestion.getInCartCount() + 1;
                productsDB.getProductDao().setCartCountForSingleProduct(productInQuestionID, productInQuestionNewCartCount);
                productInQuestionNewCartCount = 0;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Added", Toast.LENGTH_SHORT).show();
                    }
                });
                readDB();
            }
        }).start();
    }

    @Override
    public void minusTap(Products products) {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList = productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID - 1);
                productInQuestionNewCartCount = productInQuestion.getInCartCount() - 1;
                if (productInQuestionNewCartCount < 1) productInQuestionNewCartCount = 1;
                productsDB.getProductDao().setCartCountForSingleProduct(productInQuestionID, productInQuestionNewCartCount);
                productInQuestionNewCartCount = 0;
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Deducted", Toast.LENGTH_SHORT).show();
                    }
                });
                readDB();
            }
        }).start();
    }

    @Override
    public float getSubtotal() {
        Log.e(TAG, String.valueOf(subtotal));
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                subtotal = productsDB.getProductDao().getSubtotalInCart();
            }
        }).start();
        return subtotal;
    }

    private void readDB() {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                itemsinCartList = productsDB.getProductDao().getAllProductsInCart();
                subtotal = productsDB.getProductDao().getSubtotalInCart();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getSubtotal() == 0.00f){
                            checkoutBtn.setVisibility(View.INVISIBLE);
                            subtotlalayout.setVisibility(View.INVISIBLE);
                            cartisempty.setVisibility(View.VISIBLE);
                        }
                        else {
                            checkoutBtn.setVisibility(View.VISIBLE);
                            subtotlalayout.setVisibility(View.VISIBLE);
                            cartisempty.setVisibility(View.INVISIBLE);
                            subtotalTV.setText(String.format("$%.2f", getSubtotal()));
                        }
                        productsAdapterCart = new ProductsAdapterCart(itemsinCartList, CartFragment.this);
                        rv.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                        rv.setAdapter(productsAdapterCart);
                    }
                });
            }
        }).start();
    }
}