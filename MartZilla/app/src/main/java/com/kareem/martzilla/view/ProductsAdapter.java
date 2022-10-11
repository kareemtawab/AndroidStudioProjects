package com.kareem.martzilla.view;

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
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.model.user_data.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Products> productsList = new ArrayList<>();
    private ProductItemTapInterface productItemTapInterface;

    public ProductsAdapter(List<Products> productsList, ProductItemTapInterface productItemTapInterface) {
        this.productsList = (ArrayList<Products>) productsList;
        this.productItemTapInterface = productItemTapInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productCategory;
        LinearLayout productData;
        ImageView cartIcon;
        ImageView favIcon;
        TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productimage);
            productName = itemView.findViewById(R.id.productname);
            productCategory = itemView.findViewById(R.id.productcategory);
            productData = itemView.findViewById(R.id.productdata);
            cartIcon = itemView.findViewById(R.id.addtocartbutton);
            favIcon = itemView.findViewById(R.id.favoritebutton);
            price = itemView.findViewById(R.id.productprice);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products currentProduct = productsList.get(position);

        holder.productName.setText(currentProduct.getTitle());
        holder.productCategory.setText(currentProduct.getCategory());
        Glide.with(holder.productImage.getContext())
                .load(productsList.get(position).getImage())
                .into(holder.productImage);
        holder.price.setText(String.format("$%.2f", currentProduct.getPrice()));

        if (currentProduct.isSaved()){
            holder.favIcon.setImageResource(R.drawable.ic_baseline_bookmark_24);
        }
        else {
            holder.favIcon.setImageResource(R.drawable.ic_baseline_bookmark_border_24);
        }

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInterface.imageTap(currentProduct);
            }
        });
        holder.productData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInterface.titleTap(currentProduct);
            }
        });
        holder.cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInterface.cartTap(currentProduct);
            }
        });
        holder.favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInterface.favTap(currentProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
