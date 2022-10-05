package com.kareem.lodatask.model;

import java.util.List;

public class Root {

    private List<Products> products;

    public Root() {
    }

    public Root(List<Products> products) {
        this.products = products;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }
}
