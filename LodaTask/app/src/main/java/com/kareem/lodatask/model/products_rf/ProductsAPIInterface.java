package com.kareem.lodatask.model.products_rf;

import com.kareem.lodatask.model.Products;
import com.kareem.lodatask.model.Root;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductsAPIInterface {

    @GET("products")
    Call<Root> getProducts();

}
