package com.example.e8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rv = findViewById(R.id.itemList);

        ArrayList<Cars> carsAL = new ArrayList<>();
        carsAL.add(new Cars("Acura", R.drawable.acura));
        carsAL.add(new Cars("Alfa", R.drawable.alfa));
        carsAL.add(new Cars("Aston", R.drawable.aston));
        carsAL.add(new Cars("Audi", R.drawable.audi));
        carsAL.add(new Cars("Bently", R.drawable.bentley));
        carsAL.add(new Cars("Chrysler", R.drawable.chrysler));
        carsAL.add(new Cars("Ferrari", R.drawable.ferrari));
        carsAL.add(new Cars("Ford", R.drawable.ford));
        carsAL.add(new Cars("Honda", R.drawable.honda));
        carsAL.add(new Cars("Hummer", R.drawable.hummer));
        carsAL.add(new Cars("Infiniti", R.drawable.infiniti));
        carsAL.add(new Cars("Jaguar", R.drawable.jaguar));
        carsAL.add(new Cars("Jeep", R.drawable.jeep));
        carsAL.add(new Cars("Koenigsegg", R.drawable.koenigsegg));
        carsAL.add(new Cars("Lamborghini", R.drawable.lamborghini));
        carsAL.add(new Cars("Land Rover", R.drawable.land));
        carsAL.add(new Cars("Lincoln", R.drawable.lincoln));
        carsAL.add(new Cars("Mclaren", R.drawable.mclaren));
        carsAL.add(new Cars("Mercedes", R.drawable.mercedes));
        carsAL.add(new Cars("Mini", R.drawable.mini));
        carsAL.add(new Cars("Porsche", R.drawable.porsche));
        carsAL.add(new Cars("SAAB", R.drawable.saab));
        carsAL.add(new Cars("Tesla", R.drawable.tesla));
        carsAL.add(new Cars("Volvo", R.drawable.volvo));

        CarsAdapter adapter = new CarsAdapter(carsAL);

        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.consolidated_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int i = item.getItemId();
        switch (i){
            case R.id.profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.fav:
                Toast.makeText(this, "Favorites", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.settings:
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}