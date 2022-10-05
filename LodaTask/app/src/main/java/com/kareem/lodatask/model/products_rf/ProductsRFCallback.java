package com.kareem.lodatask.model.products_rf;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;
import android.widget.Toast;

import com.kareem.lodatask.controller.MainActivity;
import com.kareem.lodatask.model.Products;
import com.kareem.lodatask.model.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductsRFCallback {

    private ProductsRFInterface productsRFInterface;

    public ProductsRFCallback(ProductsRFInterface productsRFInterface) {
        this.productsRFInterface = productsRFInterface;
    }

    public void getAllProducts() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://dummyjson.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ProductsAPIInterface productsAPIInterface = retrofit.create(ProductsAPIInterface.class);
        Call<Root> root = productsAPIInterface.getProducts();
        root.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                Log.e(TAG, "onResponse: API OK");
                productsRFInterface.getProductsList(response.body().getProducts());
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                Log.e(TAG, "onResponse: API BAD");
            }
        });
    }
}
