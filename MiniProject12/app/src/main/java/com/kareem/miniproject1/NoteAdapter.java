package com.kareem.miniproject1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private final List<Notes> notesList;
    private final NoteInterface noteInterface;

    public NoteAdapter(List<Notes> notesList, NoteInterface noteInterface) {
        this.notesList = notesList;
        this.noteInterface = noteInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rlist1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Notes currentNote = notesList.get(position);
        holder.notetitle.setText(currentNote.getNotetitle());
        holder.notebody.setText(currentNote.getNotebody());

        if(currentNote.isFav()) {
            holder.isFav.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            holder.isFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        holder.isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentNote.isFav()) {
                    holder.isFav.setImageResource(R.drawable.ic_baseline_favorite_24);
                    currentNote.setFav(false);
                } else {
                    holder.isFav.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    currentNote.setFav(true);
                }
                noteInterface.onFavClick(currentNote);
            }
        });

        holder.singleItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteInterface.onMainClick(currentNote);
            }
        });

    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notetitle;
        TextView notebody;
        ImageView isFav;
        RelativeLayout singleItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            singleItem = itemView.findViewById(R.id.recycleritem);
            notetitle = itemView.findViewById(R.id.NoteTopTXT);
            notebody = itemView.findViewById(R.id.NoteBottomTXT);
            isFav = itemView.findViewById(R.id.isfavicon);
        }
    }
}
