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
import android.widget.Toast;

import com.kareem.martzilla.R;
import com.kareem.martzilla.model.carousel.Carousel;
import com.kareem.martzilla.model.categories.Categories;
import com.kareem.martzilla.model.products_db.ProductsDB;
import com.kareem.martzilla.model.user_data.SharedPreferences;
import com.kareem.martzilla.view.CarouselAdapter;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.view.CategoriesAdapter;
import com.kareem.martzilla.view.CategoryItemTapInterface;
import com.kareem.martzilla.view.ProductItemTapInterface;
import com.kareem.martzilla.view.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements ProductItemTapInterface, CategoryItemTapInterface {

    RecyclerView rv1, rv2, rv3;

    List<Carousel> bannerList = new ArrayList<>();
    List<Categories> categoriesList = new ArrayList<>();
    List<Products> productsList = new ArrayList<>();

    List<Products> productsListinCategory = new ArrayList<>();
    ProductsDB productsDB;

    String currentCategoryinDisplay;
    CarouselAdapter carouselAdapter;
    ProductsAdapter productsAdapter;
    CategoriesAdapter categoriesAdapter;

    boolean productInQuestionNewStatus;
    boolean productInQuestionIsSaved;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv1 = view.findViewById(R.id.bannerRecyclerView);
        rv2 = view.findViewById(R.id.topRecyclerView);
        rv3 = view.findViewById(R.id.bottomRecyclerView);

        bannerList.add(new Carousel("banner1", R.drawable.banner1));
        bannerList.add(new Carousel("banner2", R.drawable.banner2));
        bannerList.add(new Carousel("banner3", R.drawable.banner3));
        bannerList.add(new Carousel("banner4", R.drawable.banner4));
        bannerList.add(new Carousel("banner5", R.drawable.banner5));
        bannerList.add(new Carousel("banner6", R.drawable.banner6));
        bannerList.add(new Carousel("banner7", R.drawable.banner7));

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
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList= productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID - 1);
                productInQuestionIsSaved = productInQuestion.isSaved();
            }
        }).start();
        in.putExtra("product_save", productInQuestionIsSaved);
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
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                int productInQuestionID = products.getId();
                productsList= productsDB.getProductDao().getAllProducts();
                Products productInQuestion = productsList.get(productInQuestionID - 1);
                productInQuestionIsSaved = productInQuestion.isSaved();
            }
        }).start();
        in.putExtra("product_save", productInQuestionIsSaved);
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

    @Override
    public void categoryTap(String s) {
        currentCategoryinDisplay = s;
        Toast.makeText(getActivity(), currentCategoryinDisplay.toUpperCase(), Toast.LENGTH_SHORT).show();
    }

    private void readDB() {
        productsDB = ProductsDB.getInstance(getActivity());
        new Thread(new Runnable() {
            @Override
            public void run() {
                categoriesList = productsDB.getProductDao().getAllCategories();
                productsList = productsDB.getProductDao().getAllProducts();
                carouselAdapter = new CarouselAdapter(bannerList);
                categoriesAdapter = new CategoriesAdapter(categoriesList, HomeFragment.this);
                productsAdapter = new ProductsAdapter(productsList, HomeFragment.this);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv1.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                        rv1.setAdapter(carouselAdapter);
                        rv2.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));
                        rv2.setAdapter(categoriesAdapter);
                        rv3.setLayoutManager(new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL));
                        rv3.setAdapter(productsAdapter);
                    }
                });
            }
        }).start();
    }
}

