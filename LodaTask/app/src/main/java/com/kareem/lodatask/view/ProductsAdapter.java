package com.kareem.lodatask.view;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kareem.lodatask.ProductData;
import com.kareem.lodatask.R;
import com.kareem.lodatask.model.Products;

import java.util.ArrayList;
import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Products> productsList = new ArrayList<>();

    public ProductsAdapter(List<Products> productsList) {
        this.productsList = productsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productCategory;
        TextView productPrice;
        CardView cardView;
        TextView results;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        productImage = itemView.findViewById(R.id.productimage);
        productName = itemView.findViewById(R.id.productname);
        productCategory = itemView.findViewById(R.id.productcategory);
        productPrice = itemView.findViewById(R.id.productprice);
        cardView = itemView.findViewById(R.id.card);
        results = itemView.findViewById(R.id.results);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 1) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.results_rv, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_rv, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products currentProduct = productsList.get(position);
        if (position != 0){
            holder.productName.setText(currentProduct.getTitle());
            holder.productCategory.setText(currentProduct.getCategory());
            holder.productPrice.setText(String.format("$%.0f", currentProduct.getPrice()));
            Glide.with(holder.productImage.getContext())
                    .load(productsList.get(position).getImages().get(0))
                    .into(holder.productImage);

            holder.productImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent in = new Intent(holder.itemView.getContext(), ProductData.class);
                    in.putExtra("image", productsList.get(position).getImages().get(0));
                    in.putExtra("title", productsList.get(position).getTitle());
                    in.putExtra("price", String.format("$%.0f", currentProduct.getPrice()));
                    in.putExtra("rate", String.format("%.1f", currentProduct.getRating()));
                    in.putExtra("description", productsList.get(position).getDescription());
                    holder.itemView.getContext().startActivity(in);
                }
            });
        }
        else {
            holder.results.setText(String.format("Found\n%d results!", productsList.size()));
        }

    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return 1;
        }
        else{
            return 2;
        }
    }
}
