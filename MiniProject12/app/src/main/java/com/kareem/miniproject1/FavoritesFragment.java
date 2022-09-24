package com.kareem.miniproject1;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

public class FavoritesFragment extends Fragment implements NoteInterface {

    NoteDB n;
    List<Notes> favoriteFragList;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.recycle1);
    }

    @Override
    public void onResume() {
        super.onResume();
        readDB();
    }

    @Override
    public void onMainClick(Notes a) {
        Intent in = new Intent(getActivity(), EditNoteActivity.class);
        in.putExtra("title", a.getNotetitle());
        in.putExtra("body", a.getNotebody());
        startActivity(in);
    }

    @Override
    public void onFavClick(Notes a) {
        Toast.makeText(getActivity(), "Un-Hearted", Toast.LENGTH_SHORT).show();
        updateNoteFav(a, false);
        readDB();
    }

    private void readDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                n = NoteDB.getInstance(getActivity());
                favoriteFragList = n.getNoteDao().getOnlyFav();
                NoteAdapter na = new NoteAdapter(favoriteFragList, FavoritesFragment.this);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rv.setAdapter(na);
                    }
                });
            }
        }).start();
    }

    private void updateNoteFav(Notes notes, boolean f) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NoteDB personDatabase = NoteDB.getInstance(getActivity());
                personDatabase.getNoteDao().addNote(new Notes(notes.getId(),notes.getNotetitle(), notes.getNotebody(), f));
            }
        }).start();
    }

}