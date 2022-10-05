package com.kareem.martzilla.view;

import com.kareem.martzilla.model.products_db.Products;

public interface ProductItemTapInCartInterface {

    void titleTap(Products products);
    void deleteTap(Products products);
    void plusTap(Products products);
    void minusTap(Products products);
    void countChange(Products products);

}
