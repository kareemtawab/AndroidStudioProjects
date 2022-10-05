package com.kareem.martzilla.model.products_db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.kareem.martzilla.model.categories.Categories;

import java.util.List;

@Dao
public interface ProductsDAO {

    // insert products
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addProduct(Products p);

    // set save status for single product by id
    @Query("update products_table set saved = :s where id = :id")
    void setSaveStatusForSingleProduct(int id, boolean s);

    // set cart count for single product by id
    @Query("update products_table set inCartCount = :c where id = :id")
    void setCartCountForSingleProduct(int id, int c);



    // get all products
    @Query("select * from products_table")
    List<Products> getAllProducts();

    // get all categories
    @Query("select distinct category from products_table")
    List<Categories> getAllCategories();

    // get products in a category
    @Query("select * from products_table where category like :cat")
    List<Products> getAllProductsInCategory(String cat);

    // get items that are saved
    @Query("select * from products_table where saved")
    List<Products> getAllProductsInSaves();

    // get products that are in cart
    @Query("select * from products_table where inCartCount > 0")
    List<Products> getAllProductsInCart();

}