package com.mynanodegreeapps.Movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mynanodegreeapps.R;

/**
 * Created by binit92 on 12/4/2016.
 */
public class MoviesActivity extends AppCompatActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        MovieFragment pmf  = new MovieFragment();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.popularmoviecontainer, new MovieFragment())
                    .commit();
        }

    }

}
