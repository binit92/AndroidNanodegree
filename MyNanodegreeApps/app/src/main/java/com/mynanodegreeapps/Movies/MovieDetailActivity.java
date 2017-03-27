package com.mynanodegreeapps.Movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.mynanodegreeapps.R;
import com.squareup.picasso.Picasso;

/**
 * Created by binit92 on 1/14/2017.
 */
public class MovieDetailActivity extends AppCompatActivity{

    ImageView moviePoster;
    TextView movieTitle;
    TextView moviePlot;
    TextView movieRelease;
    TextView movieVote;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviesdetails);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String title = intent.getStringExtra("title");
        String plot= intent.getStringExtra("plot");
        String release_date = intent.getStringExtra("releaseDate");
        String vote = intent.getStringExtra("voteAvg");
        String id = intent.getStringExtra("id");

        Log.d("url",url);

        moviePoster = (ImageView) findViewById(R.id.movie_Image);
        movieTitle = (TextView) findViewById(R.id.movie_Title);
        moviePlot = (TextView) findViewById(R.id.movie_Plot);
        movieRelease =(TextView) findViewById(R.id.movie_ReleaseDate);
        movieVote = (TextView) findViewById(R.id.movie_Votes);

        Picasso.with(getApplicationContext()).load(url).resize(600,800).into(moviePoster);
        movieTitle.setText(title);
        moviePlot.setText(plot);
        movieRelease.setText(release_date);
        movieVote.setText(vote);

    }
}
