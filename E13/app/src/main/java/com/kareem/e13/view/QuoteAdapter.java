package com.kareem.e13.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kareem.e13.R;
import com.kareem.e13.controller.MainActivity;
import com.kareem.e13.model.Quote;

import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {

    private List<Quote> QuoteList;

    public QuoteAdapter(List<Quote> q) {
        this.QuoteList = q;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quote currentQ = QuoteList.get(position);
        holder.textTv.setText(currentQ.getName());
        holder.bodyTV.setText(currentQ.getCity());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(holder.itemView.getContext());
                dialog.setContentView(R.layout.dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                TextView topTV = dialog.findViewById(R.id.texttop);
                TextView bottomTV = dialog.findViewById(R.id.textbottom);
                topTV.setText(currentQ.getName());
                bottomTV.setText(currentQ.getCity());
                Button okBtn = dialog.findViewById(R.id.ok);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return QuoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTv;
        TextView bodyTV;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTv = itemView.findViewById(R.id.titleTV);
            bodyTV = itemView.findViewById(R.id.bodyTV);
            relativeLayout = itemView.findViewById(R.id.relativeV);
        }
    }
}
