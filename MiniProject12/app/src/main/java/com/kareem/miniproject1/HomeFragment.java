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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HomeFragment extends Fragment implements NoteInterface{

    NoteDB n;
    List<Notes> homeFragList;
    private RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rv = view.findViewById(R.id.recycle1);

        FloatingActionButton floatBtn = view.findViewById(R.id.floatingaddbutton);
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), EditNoteActivity.class);
                startActivity(in);
            }
        });
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
        if(!a.isFav()){
            Toast.makeText(getActivity(), "Hearted", Toast.LENGTH_SHORT).show();
            updateNoteFav(a, true);
        }
        else{
            Toast.makeText(getActivity(), "Un-Hearted", Toast.LENGTH_SHORT).show();
            updateNoteFav(a, false);
        }

    }

    private void readDB() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                n = NoteDB.getInstance(getActivity());
                homeFragList = n.getNoteDao().getAllNotes();
                NoteAdapter na = new NoteAdapter(homeFragList, HomeFragment.this);

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