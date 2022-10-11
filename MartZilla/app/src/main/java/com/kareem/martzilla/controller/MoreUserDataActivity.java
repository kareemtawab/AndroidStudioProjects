package com.kareem.martzilla.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.kareem.martzilla.R;
import com.kareem.martzilla.model.location.GMaps;
import com.kareem.martzilla.model.user_data.SharedPreferences;

public class MoreUserDataActivity extends AppCompatActivity {

    double lat, lng;
    TextView latTV, lngTV, addressTV;
    EditText addressET, phoneET;
    Button saveBtn;
    MapView mapView;
    String currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null)
            this.getSupportActionBar().hide();
        setContentView(R.layout.activity_more_user_data);

        SharedPreferences sharedPreferences = new SharedPreferences();
        sharedPreferences.getInstance(MoreUserDataActivity.this);

        latTV = findViewById(R.id.latitude);
        lngTV = findViewById(R.id.longitude);
        addressTV = findViewById(R.id.address);
        mapView = findViewById(R.id.mapview);
        addressET = findViewById(R.id.editText_customaddress);
        phoneET = findViewById(R.id.editText_phonenumber);

        addressET.setText(sharedPreferences.getAddress());
        phoneET.setText(sharedPreferences.getPhoneNumber());

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location currentLocation = locationResult.getLastLocation();
                lat = currentLocation.getLatitude();
                lng = currentLocation.getLongitude();
            }
        };

        GMaps gMaps = new GMaps(2, this, locationCallback);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.0444, 31.2357), 7));
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull LatLng latLng) {
                        googleMap.clear();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                        currentAddress = gMaps.getAddress(latLng.latitude, latLng.longitude, MoreUserDataActivity.this);
                        addressTV.setText(currentAddress);
                        latTV.setText(String.valueOf(lat) + " E");
                        lngTV.setText(String.valueOf(lng) + " N");
                        addressET.setText(currentAddress);
                    }
                });
            }
        });

        gMaps.checkPermissionAndProvider(this, MoreUserDataActivity.this, locationCallback);

        saveBtn = findViewById(R.id.savebutton);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addressET.getText().toString().isEmpty() && !phoneET.getText().toString().isEmpty()){
                    sharedPreferences.saveAddress(addressET.getText().toString());
                    sharedPreferences.savePhoneNumber(phoneET.getText().toString());
                    Toast.makeText(MoreUserDataActivity.this, "Data Saved!", Toast.LENGTH_SHORT);
                    finish();
                }
                else {
                    Toast.makeText(MoreUserDataActivity.this, "Fields con not be empty!", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
}