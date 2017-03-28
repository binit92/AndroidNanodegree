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

        Bundle extras = intent.getExtras();
        if(extras != null){

            String url = extras.getString("url");
            String title = extras.getString("title");
            String plot= extras.getString("plot");
            String release_date = extras.getString("releaseDate");
            String vote = extras.getString("voteAvg");
            String id = extras.getString("id");

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
}
