package com.kareem.martzilla.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kareem.martzilla.R;
import com.kareem.martzilla.model.carousel.Carousel;

import java.util.ArrayList;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {

    private ArrayList<Carousel> BannerList;

    public CarouselAdapter(ArrayList<Carousel> bannerList) {
        this.BannerList = bannerList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bannerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            bannerImage = itemView.findViewById(R.id.bannerimage);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carousel_rv, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carousel currentBanner = BannerList.get(position);
        holder.bannerImage.setImageResource(currentBanner.getImgNumber());
    }

    @Override
    public int getItemCount() {
        return BannerList.size();
    }
}
