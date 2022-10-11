package com.kareem.martzilla.model.user_data;

import android.content.Context;

public class SharedPreferences {

    private android.content.SharedPreferences preferences;

    public void getInstance(Context c){
        preferences = c.getSharedPreferences("user_data", Context.MODE_PRIVATE);
    }
    /// Deprecated
    public void setSavedStatusforProductID(String i, boolean b){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(i, b);
        editor.apply();
    }

    public boolean getSavedStatusforProductID(String i){
        return preferences.getBoolean(i, false);
    }

    public void setInCartCountforProductID(String i, int c){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(i, c);
        editor.apply();
    }

    public int getInCartCountforProductID(String i){
        return preferences.getInt(i, 0);
    }
    /// Deprecated

    public void saveName(String e){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_name", e);
        editor.apply();
    }

    public String getName(){
        return preferences.getString("user_name", "undefined");
    }

    public void saveEmail(String e){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_email", e);
        editor.apply();
    }

    public String getEmail(){
        return preferences.getString("user_email", "undefined");
    }

    public void savePhoneNumber(String p){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_phone", p);
        editor.apply();
    }

    public String getPhoneNumber(){
        return preferences.getString("user_phone", "undefined");
    }

    public void saveAddress(String a){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_address", a);
        editor.apply();
    }

    public String getAddress(){
        return preferences.getString("user_address", "undefined");
    }

    public void clearPreferences(){
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
