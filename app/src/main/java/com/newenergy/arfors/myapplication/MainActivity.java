package com.newenergy.arfors.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import android.widget.Toast;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private TextView txtvLength, txtvHeight;
    private Button btnSetMarker;
    private Switch swGPS;
    GPStracker gpsTracker;
    Location curLoc, baseLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtvHeight = findViewById(R.id.txtvWidth);
        txtvLength = findViewById(R.id.txtvLength);
        btnSetMarker = findViewById(R.id.btnSetMarker);
        swGPS = findViewById(R.id.swGPS);
        gpsTracker = new GPStracker(this);
        gpsTracker.requestLocationPermissions(this);


        btnSetMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainActivity", "ButtonClicked");
                if (curLoc != null) {
                    double lat = curLoc.getLatitude();  // широта
                    double lon = curLoc.getLongitude(); // довгота
                    Toast.makeText(getApplicationContext(), "Широта: " + lat + "\nДовгота: " + lon, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Location not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        gpsTracker.onRequestPermissionsResult(requestCode, grantResults, this);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            gpsTracker.getLocation(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gpsTracker.stopLocationUpdates(this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        Log.d("MainActivity", "Location changed: " + location.getLatitude() + ", " + location.getLongitude());
        curLoc = location;
        txtvLength.setText(String.valueOf(location.getLatitude()));
        txtvHeight.setText(String.valueOf(location.getLongitude()));

    }

}