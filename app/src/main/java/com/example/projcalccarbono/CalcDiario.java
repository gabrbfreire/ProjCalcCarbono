package com.example.projcalccarbono;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;

public class CalcDiario extends AppCompatActivity{

    int PERMISSION_ID = 44;

    private ActivityRecognitionClient activityRecognitionClient;
    private PendingIntent pIntent;
    private BroadcastReceiver receiver;

    FusedLocationProviderClient mFusedLocationClient;
    TextView textView1, textView2, textView8, textView;
    ArrayList<Double> latitudes = new ArrayList<Double>();
    ArrayList<Double> longitudes = new ArrayList<Double>();
    LocationManager locationManager;
    LocationListener locationListener;
    Double distTotal = 0.0;
    Double emissaoTotal = 0.0;
    Spinner spinner;

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
                    case R.id.atitudes:
                        openActivityAtitudes();
                        break;
                    case R.id.calcMensal:
                        openActivityCalcMensal();
                        break;
                }
                return false;
            }
        });

        textView1 = findViewById(R.id.textView1);
        textView2 = findViewById(R.id.textView2);
        textView8 = findViewById(R.id.textView8);
        textView = findViewById(R.id.textView);
        spinner = findViewById(R.id.spinner1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

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
                if(spinner.getSelectedItem().equals("Carro")){
                    emissaoTotal = emissaoTotal + distPontos*190;
                    textView.setText(String.format("%.3f", emissaoTotal)+" g CO2");
                }else if(spinner.getSelectedItem().equals("Moto")){
                    emissaoTotal = emissaoTotal + distPontos*70;
                    textView.setText(String.format("%.3f", emissaoTotal)+" g CO2");
                }else{
                    emissaoTotal = emissaoTotal + distPontos*0;
                    textView.setText(String.format("%.3f", emissaoTotal)+" g CO2");
                }

//                CharSequence text = location.getLatitude() + "";
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, text, duration);
//                toast.show();
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

        locationManager.requestLocationUpdates("gps", 5000, 5, locationListener); /////


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