package com.kareem.e12;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText username = findViewById(R.id.usernamefield);
        EditText email = findViewById(R.id.emailfield);
        EditText job = findViewById(R.id.jobfield);
        EditText office = findViewById(R.id.officefield);
        EditText score = findViewById(R.id.scorefield);
        Button addBTN = findViewById(R.id.addnew);
        RecyclerView rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();
        databaseReference.setValue("HelloWorld");

        addBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i;
                try{
                    i = Integer.parseInt(score.getText().toString().trim());
                }
                catch (NumberFormatException ex){
                    i = 0;
                }
                User u = new User(username.getText().toString(),
                        email.getText().toString(),
                        job.getText().toString(),
                        office.getText().toString(),
                        i
                );
                databaseReference.child("users").push().setValue(u);
                Toast.makeText(MainActivity.this, "New user added!", Toast.LENGTH_SHORT).show();
            }
        });

        List<User> users = new ArrayList<>();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    users.add(currentUser);
                }
                UserAdapter ua = new UserAdapter(users);
                rv.setAdapter(ua);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}