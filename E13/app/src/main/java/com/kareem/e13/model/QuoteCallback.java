package com.kareem.e13.model;

import com.kareem.e13.model.QuoteInterface;
import com.kareem.e13.model.Quote;
import com.kareem.e13.model.QuoteSendAfterGETResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuoteCallback  {

    String QuotesURL = "https://mocki.io/v1/";

    private QuoteSendAfterGETResponse quoteSendAfterGETResponse;

    public QuoteCallback(QuoteSendAfterGETResponse quoteSendAfterGETResponse) {
        this.quoteSendAfterGETResponse = quoteSendAfterGETResponse;
    }

    public void getQuotes() {
        Retrofit rf = new Retrofit.Builder().baseUrl(QuotesURL).addConverterFactory(GsonConverterFactory.create()).build();
        QuoteInterface qi = rf.create(QuoteInterface.class);
        Call<List<Quote>> q = qi.getAllQuotes();
        q.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                quoteSendAfterGETResponse.getQuotesAfterGET(response.body());
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
            }
        });

    }
}
