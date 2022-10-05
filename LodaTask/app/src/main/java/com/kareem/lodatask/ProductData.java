package com.kareem.lodatask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProductData extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_product_data);

        ImageView pic = findViewById(R.id.productimage);
        CardView back = findViewById(R.id.back);
        TextView title = findViewById(R.id.productname);
        TextView price = findViewById(R.id.productprice);
        TextView rate = findViewById(R.id.rate);
        TextView desc = findViewById(R.id.description);

        title.setText(getIntent().getStringExtra("title"));
        price.setText(getIntent().getStringExtra("price"));
        rate.setText(getIntent().getStringExtra("rate"));
        desc.setText(getIntent().getStringExtra("description"));

        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .into(pic);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}