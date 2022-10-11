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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.products_db.ProductsDB;
import com.kareem.martzilla.model.user_data.SharedPreferences;
import com.kareem.martzilla.view.CarouselAdapter;
import com.kareem.martzilla.view.CategoriesAdapter;
import com.kareem.martzilla.view.ProductsAdapter;

public class ProfileFragment extends Fragment {

    Button logoutBtn;
    LinearLayout editBtn, resetPassword, deleteAccount;
    TextView nameTV, emailTV, phoneTV;
    SharedPreferences sharedPreferences = new SharedPreferences();
    ProductsDB productsDB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences.getInstance(getActivity());


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences.getInstance(getActivity());

        nameTV = getActivity().findViewById(R.id.name);
        nameTV.setText(sharedPreferences.getName());
        emailTV = getActivity().findViewById(R.id.emailaddress);
        emailTV.setText(sharedPreferences.getEmail());
        phoneTV = getActivity().findViewById(R.id.phonenumber);
        phoneTV.setText(sharedPreferences.getPhoneNumber());

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

                TextView emailTV = dialog.findViewById(R.id.emailaddress);
                SharedPreferences sharedPreferences = new SharedPreferences();
                sharedPreferences.getInstance(getActivity());
                emailTV.setText(sharedPreferences.getEmail());

                EditText nameET = dialog.findViewById(R.id.editText_name);
                nameET.setText(sharedPreferences.getName());

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
                        String name = nameET.getText().toString().trim();

                        sharedPreferences.saveName(name);
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);

                        nameTV.setText(sharedPreferences.getName());
                        emailTV.setText(sharedPreferences.getEmail());
                        phoneTV.setText(sharedPreferences.getPhoneNumber());

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
        resetPassword = getActivity().findViewById(R.id.resetpassword);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Password Reset")
                        .setMessage("Are you sure you want to reset the account password?\n" +
                        "This will log you out.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        FirebaseAuth.getInstance().signOut();
                        Intent in = new Intent(getActivity(), GetStartedActivity.class);
                        startActivity(in);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", null).show();
            }
        });

        deleteAccount = getActivity().findViewById(R.id.deleteaccount);
        deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Account Deletion")
                        .setMessage("Are you sure you want to delete your account?\n" +
                        "This action can not be undone and will:\n" +
                        "1. Delete products in the \"Saved\" and the \"Cart\" sections\n" +
                        "2. Reset your account data\n" +
                        "3. Log you out.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        productsDB = ProductsDB.getInstance(getActivity());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                productsDB.getProductDao().setUnsavedForAllProducts();
                                productsDB.getProductDao().setCartCountAtZeroForAllProducts();
                            }
                        }).start();

                        SharedPreferences sharedPreferences = new SharedPreferences();
                        sharedPreferences.getInstance(getActivity());
                        sharedPreferences.clearPreferences();

                        FirebaseAuth.getInstance().getCurrentUser().delete();
                        FirebaseAuth.getInstance().signOut();

                        Intent in = new Intent(getActivity(), GetStartedActivity.class);
                        startActivity(in);
                        getActivity().finish();
                    }
                }).setNegativeButton("No", null).show();
            }
        });
    }
}