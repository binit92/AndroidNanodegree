package com.mynanodegreeapps.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mynanodegreeapps.R;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moviedetails_activity);

        Bundle b = getIntent().getBundleExtra("movieBundle");
        if(savedInstanceState == null){
            MovieDetailFragment mdf = new MovieDetailFragment();
            mdf.setArguments(b);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.moviedetailcontainer, mdf)
                    .commit();
        }
    }
}
