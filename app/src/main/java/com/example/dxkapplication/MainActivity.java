package com.example.dxkapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button cities = findViewById(R.id.cities);
        Button parks = findViewById(R.id.parks);
        Button music = findViewById(R.id.music);
        Button movies = findViewById(R.id.movies);
        Button traffic = findViewById(R.id.traffic);
        Button food = findViewById(R.id.food);

        cities.setOnClickListener(this);
        parks.setOnClickListener(this);
        music.setOnClickListener(this);
        movies.setOnClickListener(this);
        traffic.setOnClickListener(this);
        food.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cities:
                Toast.makeText(this, "Cities", Toast.LENGTH_SHORT).show();
                break;
            case R.id.parks:
                Toast.makeText(this, "Parks", Toast.LENGTH_SHORT).show();
                break;
            case R.id.music:
                Toast.makeText(this, "Music", Toast.LENGTH_SHORT).show();
                break;
            case R.id.movies:
                Toast.makeText(this, "Movies", Toast.LENGTH_SHORT).show();
                break;
            case R.id.traffic:
                Toast.makeText(this, "Traffic", Toast.LENGTH_SHORT).show();
                break;
            case R.id.food:
                Toast.makeText(this, "Food", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}