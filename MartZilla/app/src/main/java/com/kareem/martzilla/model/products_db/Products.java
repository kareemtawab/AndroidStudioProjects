package com.kareem.martzilla.model.products_db;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products_table")
public class Products {

    @PrimaryKey(autoGenerate = false)
    private int id;
    private String title;
    private String description;
    private float price;
    private String category;
    private String image;
    @Embedded
    private ProductsRating rating;

    private boolean saved;
    private int inCartCount;


    public Products(int id, String title, String description, float price, String category, String image, ProductsRating rating, boolean saved, int inCartCount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.rating = rating;
        this.saved = saved;
        this.inCartCount = inCartCount;
    }

    public Products(int id, String title, String description, float price, String category, String image, ProductsRating rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.rating = rating;
    }

    public Products() {
    }

    public ProductsRating getRating() {
        return rating;
    }

    public void setRating(ProductsRating rating) {
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public int getInCartCount() {
        return inCartCount;
    }

    public void setInCartCount(int inCartCount) {
        this.inCartCount = inCartCount;
    }
}
