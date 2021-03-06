package com.dxk.dxkapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class MoviesDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_activity_details);

        TextView titleDetail = findViewById(R.id.titleDetail);
        TextView yearDetail = findViewById(R.id.yearDetail);
        ImageView movieImageDetail = findViewById(R.id.movieImageDetail);
        TextView movieDetail = findViewById(R.id.movieDetail);

    Bundle bundle = this.getIntent().getExtras();
    String[] movieDetails = bundle.getStringArray("detail");
    titleDetail.setText(movieDetails[0]);
    yearDetail.setText(movieDetails[1]);
    Picasso instance = Picasso.get() ;
    instance.setIndicatorsEnabled(true);
    instance.load(movieDetails[3]).into(movieImageDetail);
    movieDetail.setText(movieDetails[4]);
    }
}