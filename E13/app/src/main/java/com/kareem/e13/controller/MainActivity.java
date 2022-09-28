package com.kareem.e13.controller;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kareem.e13.model.QuoteCallback;
import com.kareem.e13.model.QuoteSendAfterGETResponse;
import com.kareem.e13.view.QuoteAdapter;
import com.kareem.e13.R;
import com.kareem.e13.model.Quote;

import java.util.List;

public class MainActivity extends AppCompatActivity implements QuoteSendAfterGETResponse {

    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        QuoteCallback quoteCallback = new QuoteCallback(this);
        quoteCallback.getQuotes();
    }

    @Override
    public void getQuotesAfterGET(List<Quote> q) {
        QuoteAdapter adapter = new QuoteAdapter(q);
        rv.setAdapter(adapter);
    }
}