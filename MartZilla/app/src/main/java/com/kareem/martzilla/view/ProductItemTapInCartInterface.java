package com.kareem.martzilla.view;

import com.kareem.martzilla.model.products_db.Products;

public interface ProductItemTapInCartInterface {

    void titleTap(Products products);
    void imageTap(Products products);
    void deleteTap(Products products);
    void plusTap(Products products);
    void minusTap(Products products);
    float getSubtotal();
}
