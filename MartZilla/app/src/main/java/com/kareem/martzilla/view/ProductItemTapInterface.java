package com.kareem.martzilla.view;

import com.kareem.martzilla.model.products_db.Products;

public interface ProductItemTapInterface {

    void imageTap(Products products);
    void titleTap(Products products);
    void cartTap(Products products);
    void favTap(Products products);

}
