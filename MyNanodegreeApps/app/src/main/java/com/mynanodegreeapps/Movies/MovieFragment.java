package com.mynanodegreeapps.Movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.GridView;
import com.mynanodegreeapps.R;
import java.util.ArrayList;


/**
 * Created by binit92 on 1/14/2017.
 */
public class MovieFragment extends Fragment implements FetchMoviesResponse {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private final String TAG_POPULAR = "popular";
    private final String TAG_TOPRATED = "top_rated";


    ArrayList<TMDBMovie> movies;

    View rootview;
    GridView movieGrid;

    public MovieFragment(){}

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_movies,container,false);
        setHasOptionsMenu(true);

        if(rootview!= null) {
            movieGrid = (GridView) rootview.findViewById(R.id.movieGrid);
            movieGrid.setClickable(true);
        }
        return rootview;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                 updateMoviesList(TAG_POPULAR);
                 break;

            case R.id.action_toprated:
                 updateMoviesList(TAG_TOPRATED);
                 break;

            default:
                 updateMoviesList(TAG_POPULAR);
                 break;
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMoviesList(TAG_POPULAR);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMoviesList(TAG_POPULAR);
    }

    private void updateMoviesList(String tag_movie){
        FetchMoviesTask FPM = new FetchMoviesTask();
        FPM.delegate=this;
        FPM.execute(tag_movie);
    }

    @Override
    public void processFinish(ArrayList<TMDBMovie> movieList) {
        this.movies=movieList;
        movieGrid.setAdapter(new ImageAdapter(getContext(),movieList));

    }


}
