package com.kareem.e10;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class QuoteAdapter extends RecyclerView.Adapter<QuoteAdapter.ViewHolder> {

    private List<Quote> QuoteList;

    public QuoteAdapter(List<Quote> q) {
        this.QuoteList = q;
    }

    @NonNull
    @Override
    public QuoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuoteAdapter.ViewHolder holder, int position) {
        Quote currentQ = QuoteList.get(position);
        holder.textTv.setText(currentQ.getName());
        holder.bodyTV.setText(currentQ.getCity());
    }

    @Override
    public int getItemCount() {
        return QuoteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTv;
        TextView bodyTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTv = itemView.findViewById(R.id.titleTV);
            bodyTV = itemView.findViewById(R.id.bodyTV);
        }
    }
}
