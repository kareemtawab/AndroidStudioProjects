package com.kareem.martzilla.model.products_db;

public class ProductsRating {

    private float rate;
    private int count;

    public ProductsRating(float rate, int count) {
        this.rate = rate;
        this.count = count;
    }

    public ProductsRating() {
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
