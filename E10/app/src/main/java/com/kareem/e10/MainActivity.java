package com.kareem.e10;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    String QuotesURL = "https://mocki.io/v1/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        Retrofit rf = new Retrofit.Builder().baseUrl(QuotesURL).addConverterFactory(GsonConverterFactory.create()).build();
        QuoteInterface qi = rf.create(QuoteInterface.class);
        Call<List<Quote>> q = qi.getAllQuotes();
        q.enqueue(new Callback<List<Quote>>() {
            @Override
            public void onResponse(Call<List<Quote>> call, Response<List<Quote>> response) {
                QuoteAdapter adapter = new QuoteAdapter(response.body());
                rv.setAdapter(adapter);
                Toast.makeText(MainActivity.this, "Got Response", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Quote>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Did not get Response", Toast.LENGTH_SHORT).show();
            }
        });
    }
}