package com.kareem.martzilla.model.user_data;

import android.content.Context;

public class SharedPreferences {

    private android.content.SharedPreferences preferences;

    public void getInstance(Context c){
        preferences = c.getSharedPreferences("user_data", Context.MODE_PRIVATE);
    }

    public boolean getSavedStatusforProductID(String i){
        return preferences.getBoolean(i, false);
    }

    public void setSavedStatusforProductID(String i, boolean b){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(i, b);
        editor.apply();
    }

    public int getInCartCountforProductID(String i){
        return preferences.getInt(i, 0);
    }
    public void setInCartCountforProductID(String i, int c){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(i, c);
        editor.apply();
    }
}
