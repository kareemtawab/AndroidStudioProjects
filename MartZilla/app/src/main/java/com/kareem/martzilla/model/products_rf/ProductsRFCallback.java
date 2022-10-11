package com.kareem.martzilla.model.products_rf;

import android.util.Log;

import com.kareem.martzilla.model.products_db.Products;

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

    public void connectToAPI() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        ProductsAPIInterface productsAPIInterface = retrofit.create(ProductsAPIInterface.class);
        Call<List<Products>> productsList = productsAPIInterface.getProducts();
        productsList.enqueue(new Callback<List<Products>>(){
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                Log.e("Retrofit", "API connection success");
                productsRFInterface.getProductsList(response.body());
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.e("Retrofit", "API connection failed");
            }
        });
    }
}
