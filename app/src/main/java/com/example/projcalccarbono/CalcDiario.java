package com.example.projcalccarbono;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class CalcDiario extends AppCompatActivity {

    int PERMISSION_ID = 44;
    FusedLocationProviderClient mFusedLocationClient;
    TextView textView1, textView2, textView8;
    ArrayList<Double> latitudes = new ArrayList<Double>();
    ArrayList<Double> longitudes = new ArrayList<Double>();
    LocationManager locationManager;
    LocationListener locationListener;
    Double distTotal = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_diario);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.calcDiario);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calcMensal:
                        openActivityCalcMensal();
                        break;
                    case R.id.atitudes:
                        openActivityAtitudes();
                        break;
                }
                return false;
            }
        });

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView8 = findViewById(R.id.textView8);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                getLastLocation();
//
//                handler.postDelayed(this, 2000);
//            }
//        }, 1);

        final Context context = getApplicationContext();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitudes.add(location.getLatitude());
                longitudes.add(location.getLongitude());

                Double distPontos = calcDist();
                distTotal = distTotal + distPontos;
                textView8.setText(String.format("%.3f", distTotal)+" km");

                CharSequence text = location.getLatitude() + "";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, 10);
            return;
        }

        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener);

        Spinner spinner = findViewById(R.id.spinner);
        //Create an ArrayAdapter using the string array and a default spinner layout;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.vehicles, android.R.layout.simple_spinner_item);
        //Specify the layout to use when the list of choices appears;
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Apply the adapter to the spinner
        spinner.setAdapter(adapter);

    }

    public void openActivityCalcMensal(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openActivityAtitudes(){
        Intent intent = new Intent(this, Atitudes.class);
        startActivity(intent);
    }
//
//    @SuppressLint("MissingPermission")
//    private void getLastLocation(){
//        if (checkPermissions()) {
//            if (isLocationEnabled()) {
//                mFusedLocationClient.getLastLocation().addOnCompleteListener(
//                        new OnCompleteListener<Location>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Location> task) {
//                                Location location = task.getResult();
//                                if (location == null) {
//                                    requestNewLocationData();
//                                } else {
//                                    latitudes.add(location.getLatitude());
//                                    longitudes.add(location.getLongitude());
//
////                                    textView1.setText(location.getLatitude()+"");
////                                    textView2.setText(location.getLongitude()+"");
//
//                                    StringBuffer sb = new StringBuffer();
//
//                                    for (Double s : latitudes) {
//                                        sb.append(s);
//                                        sb.append(" ");
//                                    }
//
//
//                                    Double str = calcDist();
//
//                                    textView8.setText(str+" km");
//
//                                    Context context = getApplicationContext();
//                                    CharSequence text = sb.toString();
//                                    int duration = Toast.LENGTH_SHORT;
//
//                                    Toast toast = Toast.makeText(context, text, duration);
//                                    toast.show();
//                                }
//                            }
//                        }
//                );
//            } else {
//                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        } else {
//            requestPermissions();
//        }
//    }
//
//
//    @SuppressLint("MissingPermission")
//    private void requestNewLocationData(){
//
//        LocationRequest mLocationRequest = new LocationRequest();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(0);
//        mLocationRequest.setFastestInterval(0);
//        mLocationRequest.setNumUpdates(1);
//
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//        );
//
//    }
//
//    private LocationCallback mLocationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            Location mLastLocation = locationResult.getLastLocation();
//            latitudes.add(mLastLocation.getLatitude());
//            longitudes.add(mLastLocation.getLongitude());
//        }
//    };
//
//    private boolean checkPermissions() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return true;
//        }
//        return false;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(
//                this,
//                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
//                PERMISSION_ID
//        );
//    }
//
//    private boolean isLocationEnabled() {
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
//                LocationManager.NETWORK_PROVIDER
//        );
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_ID) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                getLastLocation();
//            }
//        }
//    }
//
//    @Override
//    public void onResume(){
//        super.onResume();
//        if (checkPermissions()) {
//            //getLastLocation();
//        }
//
//    }

    public double calcDist(){
        if(latitudes.size()>3) {
            double d = 0;

            for (int i = 0; i < latitudes.size()-1; i++) {
                double R = 6371e3; // metres
                double φ1 = Math.toRadians(latitudes.get(i));
                double φ2 = Math.toRadians(latitudes.get(i + 1));
                double Δφ = Math.toRadians(latitudes.get(i + 1) - latitudes.get(i));
                double Δλ = Math.toRadians(longitudes.get(i + 1) - longitudes.get(i));

                double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) + Math.cos(φ1) * Math.cos(φ2) * Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
                double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                d = (d + (R * c))/1000;
            }
            return d;
        }else{
            return 0.0;
        }
    }
}