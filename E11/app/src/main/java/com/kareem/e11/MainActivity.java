package com.kareem.e11;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final int locationRequestID = 4;
    TextView latlngTV;
    TextView addressTV;
    MapView mapView;
    GoogleMap map;
    double lat;
    double lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissionAndProvider();
        latlngTV = findViewById(R.id.latlng);
        addressTV = findViewById(R.id.address);
        mapView = findViewById(R.id.gmap);


        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 5));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String getAddress(double lat, double lng) {
        String a = null;
        try {
            Geocoder geocoder = new Geocoder(MainActivity.this);
            Address address = geocoder.getFromLocation(lat, lng, 1).get(0);
            a = address.getAdminArea() + ", " + address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == locationRequestID) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
                Toast.makeText(this, "GOT LOCATION RESULT!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "DIDN'T GET LOCATION RESULT!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void checkPermissionAndProvider() {
        if (isLocationPermissionsEnabled()) {
            if (isGPSProviderEnabled()) {
                Toast.makeText(this, "PROVIDER ENABLED!", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
        } else {
            Toast.makeText(this, "REQUESTING LOCATION PERMISSIONS!", Toast.LENGTH_SHORT).show();
            enableLocationPermissions();
        }
    }

    private void enableLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                locationRequestID);
    }

    private boolean isLocationPermissionsEnabled() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isGPSProviderEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }

    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        com.google.android.gms.location.LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(3000);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Location currentLocation = locationResult.getLastLocation();
            lat = currentLocation.getLatitude();
            lng = currentLocation.getLongitude();
            latlngTV.setText(String.valueOf(lat) + " E       " + String.valueOf(lng) + " N");
            addressTV.setText(getAddress(lat, lng));
        }

    };
}