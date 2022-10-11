package com.kareem.martzilla.model.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;

public class GMaps {

    private int locationRequestID;
    private Context context;
    private LocationCallback locationCallback;

    public GMaps(int locationRequestID, Context context, LocationCallback locationCallback) {
        this.locationRequestID = locationRequestID;
        this.context = context;
        this.locationCallback = locationCallback;
    }

    public void checkPermissionAndProvider(Context context, Activity activity, LocationCallback locationCallback) {
        if (isLocationPermissionsEnabled(context)) {
            if (isGPSProviderEnabled(context)) {
                Toast.makeText(context, "GPS ENABLED!", Toast.LENGTH_SHORT).show();
                getCurrentLocation(context, locationCallback);
            }
        } else {
            Toast.makeText(context, "REQUESTING LOCATION PERMISSIONS!", Toast.LENGTH_SHORT).show();
            enableLocationPermissions(activity);
        }
    }

    private void enableLocationPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                locationRequestID);
    }

    private boolean isLocationPermissionsEnabled(Context context) {
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isGPSProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
    }

    FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingPermission")
    private void getCurrentLocation(Context context, LocationCallback locationCallback) {
        com.google.android.gms.location.LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setFastestInterval(3000);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    public String getAddress(double lat, double lng, Context context) {
        String a = null;
        try {
            Geocoder geocoder = new Geocoder(context);
            Address address = geocoder.getFromLocation(lat, lng, 1).get(0);
            a = address.getAdminArea() + ", " + address.getLocality();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

}
