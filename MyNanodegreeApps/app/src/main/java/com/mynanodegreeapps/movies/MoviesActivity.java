package com.mynanodegreeapps.movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mynanodegreeapps.R;

/**
 * Created by binit92 on 12/4/2016.
 */
public class MoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies_activity);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if(isTablet){
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movieRecyclerFragment, new MovieFragment())
                        .add(R.id.movieDetailFragment, new MovieDetailFragment())
                        .commit();
            }
        }else{
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.popularmoviecontainer, new MovieFragment())
                        .commit();
            }
        }
    }
}
