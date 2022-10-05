package com.kareem.martzilla.controller;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.kareem.martzilla.R;

public class ProfileFragment extends Fragment {

    Button logoutBtn;
    ImageView editBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        logoutBtn = getActivity().findViewById(R.id.logoutbutton);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity()).setTitle("Confirmation").setMessage("Are you sure you to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().signOut();
                        Intent in = new Intent(getActivity(), GetStartedActivity.class);
                        startActivity(in);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", null).show();
            }
        });
        editBtn = getActivity().findViewById(R.id.editbutton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom_dialog_edit_profile);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                Button backBtn = dialog.findViewById(R.id.backbutton);
                backBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                Button okBtn = dialog.findViewById(R.id.okbutton);
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getActivity(), "Details Saved!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                });
                Button changeLocationBtn = dialog.findViewById(R.id.changelocationbutton);
                changeLocationBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent in = new Intent(getActivity(), MoreUserDataActivity.class);
                        startActivity(in);
                    }
                });
                dialog.show();
            }
        });

    }
}