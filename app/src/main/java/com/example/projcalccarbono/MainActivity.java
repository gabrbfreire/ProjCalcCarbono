package com.example.projcalccarbono;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.warkiz.widget.IndicatorSeekBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IndicatorSeekBar seekBar = findViewById(R.id.seekBar3);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);

        bottomNavigationView.setSelectedItemId(R.id.calcMensal);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.calcDiario:
                        openActivityCalcDiario();
                        break;
                    case R.id.atitudes:
                        openActivityAtitudes();
                        break;
                }
                return false;
            }
        });

        seekBar.setIndicatorTextFormat("${PROGRESS} %");
    }

    public void openActivityCalcDiario(){
        Intent intent = new Intent(this, CalcDiario.class);
        startActivity(intent);
    }

    public void openActivityAtitudes(){
        Intent intent = new Intent(this, Atitudes.class);
        startActivity(intent);
    }


}