package com.kareem.martzilla.model.products_rf;

import com.kareem.martzilla.model.products_db.Products;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ProductsAPIInterface {

    @GET("products")
    Call<List<Products>> getProducts();

}
