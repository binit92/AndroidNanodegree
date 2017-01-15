package com.mynanodegreeapps.Movies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mynanodegreeapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by binit92 on 1/14/2017.
 */
public class ImageAdapter extends BaseAdapter {

    private Context c ;
    ArrayList<TMDBMovie> movieList;
    private LayoutInflater inflater;
    ImageView posterImage;

    public ImageAdapter(Context context, ArrayList<TMDBMovie> movieList) {
        this.c = context;
        this.movieList = movieList;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public TMDBMovie getItem(int i) {
        return movieList.get(i);
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View gridView = inflater.inflate(R.layout.fragment_movies,null);

        posterImage = (ImageView) gridView.findViewById(R.id.moviePoster);

        TMDBMovie selectedMovie = getItem(i);
        final String baseImageUrl = "http://image.tmdb.org/t/p/w185";
        String url = baseImageUrl+ selectedMovie.getMoviePosterPath();

        //Automatically creates bacground thread and loads image
        Picasso.with(c).load(url).into(posterImage);


        posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               String title = movieList.get(i).getMovieName();
               String url = baseImageUrl+movieList.get(i).getMoviePosterPath();
               String releaseDate =  movieList.get(i).getMovieReleaseDate();
               String voteAvg = movieList.get(i).getMovieVoteAverage();
               String plot = movieList.get(i).getMoviePlotSynopsis();

               Intent intent = new Intent(c,MovieDetailActivity.class);
               intent.putExtra("title",title);
               intent.putExtra("url",url);
               intent.putExtra("releaseDate",releaseDate);
               intent.putExtra("voteAvg",voteAvg);
               intent.putExtra("plot",plot);

               c.startActivity(intent);
            }
        });

        return gridView;
    }


}
