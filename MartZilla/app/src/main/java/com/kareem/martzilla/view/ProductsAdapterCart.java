package com.kareem.martzilla.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.Products;
import com.kareem.martzilla.model.user_data.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsAdapterCart extends RecyclerView.Adapter<ProductsAdapterCart.ViewHolder> {

    private ArrayList<Products> productsList;
    private ProductItemTapInCartInterface productItemTapInCartInterface;

    public ProductsAdapterCart(List<Products> productsList, ProductItemTapInCartInterface productItemTapInCartInterface) {
        this.productsList = (ArrayList<Products>) productsList;
        this.productItemTapInCartInterface = productItemTapInCartInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView productImage;
        TextView productName;
        TextView productCategory;
        TextView productPrice;
        LinearLayout productData;
        ImageView deleteBtn;
        ImageView minusIV;
        ImageView plusIV;
        EditText itemCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productimage);
            productName = itemView.findViewById(R.id.productname);
            productCategory = itemView.findViewById(R.id.productcategory);
            productPrice = itemView.findViewById(R.id.productprice);
            productData = itemView.findViewById(R.id.productdata);
            deleteBtn = itemView.findViewById(R.id.deletebutton);
            minusIV = itemView.findViewById(R.id.minus);
            plusIV = itemView.findViewById(R.id.plus);
            itemCount = itemView.findViewById(R.id.itemcount);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products currentProduct = productsList.get(position);

        holder.productName.setText(currentProduct.getTitle());
        holder.productCategory.setText(currentProduct.getCategory());
        holder.productPrice.setText(String.format("$%.2f", currentProduct.getPrice()));
        holder.itemCount.setText(String.valueOf(currentProduct.getInCartCount()));
        Glide.with(holder.productImage.getContext())
                .load(productsList.get(position).getImage())
                .into(holder.productImage);

        holder.productData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInCartInterface.titleTap(currentProduct);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInCartInterface.deleteTap(currentProduct);
            }
        });
        holder.minusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInCartInterface.minusTap(currentProduct);
            }
        });
        holder.plusIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productItemTapInCartInterface.plusTap(currentProduct);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}
