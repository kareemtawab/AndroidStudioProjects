package com.kareem.miniproject1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileFragment extends Fragment {

    String u;
    String p;
    String pn;
    TextView usernameTF;
    TextView passwordTF;
    TextView phoneTF;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameTF = view.findViewById(R.id.usernamefield);
        passwordTF = view.findViewById(R.id.passwordfield);
        phoneTF = view.findViewById(R.id.phonefield);

        SharedPreferences preferences = getActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE);
        usernameTF.setText(preferences.getString("sp-username", "data not found"));
        passwordTF.setText(preferences.getString("sp-password", "data not found"));
        phoneTF.setText(preferences.getString("sp-phone", "data not found"));

        Button editBtn = view.findViewById(R.id.editbutton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(view.getContext(), EditProfileActivity.class);
                startActivity(in);
            }
        });

    }
}


