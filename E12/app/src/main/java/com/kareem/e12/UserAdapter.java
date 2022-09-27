package com.kareem.e12;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User currentUser = userList.get(position);
        holder.usernameTV.setText(currentUser.getUsername());
        holder.emailTV.setText(currentUser.getEmail());
        holder.jobTV.setText(currentUser.getJob());
        holder.officeTV.setText(currentUser.getOffice());
        holder.scoreTV.setText(String.valueOf(currentUser.getScore()));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView usernameTV;
        TextView emailTV;
        TextView jobTV;
        TextView officeTV;
        TextView scoreTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTV = itemView.findViewById(R.id.usernameTV);
            emailTV = itemView.findViewById(R.id.emailTV);
            jobTV = itemView.findViewById(R.id.jobTV);
            officeTV = itemView.findViewById(R.id.officeTV);
            scoreTV = itemView.findViewById(R.id.scoreTV);}
    }
}
