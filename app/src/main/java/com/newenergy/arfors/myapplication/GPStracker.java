package com.newenergy.arfors.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPStracker {

    private Context context;
    private LocationManager locationManager;

    public GPStracker(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    public void requestLocationPermissions(Activity activity) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            Log.d("GPS", "NO permission");
        } else {
            Log.d("GPS", "permission");
            getLocation((LocationListener) activity);
        }
    }

    public void onRequestPermissionsResult(int requestCode, int[] grantResults, LocationListener listener) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation(listener);
            } else {
                Toast.makeText(context, "Доступ до місцезнаходження відхилено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getLocation(LocationListener listener) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.d("GPS", "Requesting location updates");
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, listener);
        Log.d("GPS_provider", " " + LocationManager.GPS_PROVIDER);

        // Спроба отримати останнє відоме місцезнаходження як резервний варіант
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            Log.d("GPS", "Last known location: " + lastKnownLocation.getLatitude() + ", " + lastKnownLocation.getLongitude());
            listener.onLocationChanged(lastKnownLocation);
        }
    }

    public void stopLocationUpdates(LocationListener listener) {
        if (locationManager != null) {
            locationManager.removeUpdates(listener);
        }
    }
}
