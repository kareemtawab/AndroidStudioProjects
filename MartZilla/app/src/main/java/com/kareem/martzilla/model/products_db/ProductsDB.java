package com.kareem.martzilla.model.products_db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Products.class}, version = 1, exportSchema = false)
public abstract class ProductsDB extends RoomDatabase {

    private static ProductsDB instance;

    public abstract ProductsDAO getProductDao();

    public static ProductsDB getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ProductsDB.class, "products_table")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}