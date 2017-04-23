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
        setContentView(R.layout.activity_movies);

        // Find if twoPane Layout for tablets
        if(findViewById(R.id.movieDetailFragment) != null){
            System.out.println("--> This is tablet ");

            // Todo: Change the screen orientation
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.movieRecyclerFragment, new MovieFragment())
                        .add(R.id.movieDetailFragment, new MovieDetailFragment())
                        .commit();
            }
        }else{
            System.out.println("--> This is phone ");
            if(savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.popularmoviecontainer, new MovieFragment())
                        .commit();
            }
        }
    }
}
