package com.kareem.martzilla.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kareem.martzilla.R;
import com.kareem.martzilla.model.carousel.Carousel;
import com.kareem.martzilla.model.categories.Categories;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Categories> CategoriesList;
    private CategoryItemTapInterface categoryItemTapInterface;

    public CategoriesAdapter(List<Categories> CategoriesList, CategoryItemTapInterface categoryItemTapInterface) {
        this.CategoriesList = CategoriesList;
        this.categoryItemTapInterface = categoryItemTapInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.category);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categories currentCategory = CategoriesList.get(position);
        holder.categoryTV.setText(currentCategory.getCategory());
        holder.categoryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryItemTapInterface.categoryTap(holder.categoryTV.getText().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return CategoriesList.size();
    }
}
