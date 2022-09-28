package com.kareem.e13.model;

import com.kareem.e13.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface QuoteInterface {

    @GET("d4867d8b-b5d5-4a48-a4ab-79131b5809b8")
    Call<List<Quote>> getAllQuotes();
}
