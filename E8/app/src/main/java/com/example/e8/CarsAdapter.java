package com.example.e8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private ArrayList<Cars> CarsList;

    public CarsAdapter(ArrayList<Cars> carsList) {
        this.CarsList = carsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgName;
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.CarTXT);
            imgName = itemView.findViewById(R.id.CarIMG);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cars currentCar = CarsList.get(position);
        holder.txtName.setText(currentCar.getImgName());
        holder.imgName.setImageResource(currentCar.getImgNumber());
    }

    @Override
    public int getItemCount() {
        return CarsList.size();
    }
}
