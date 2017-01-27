package com.mynanodegreeapps.Movies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mynanodegreeapps.R;
import java.util.ArrayList;


/**
 * Created by binit92 on 1/14/2017.
 *
 *  notes on recyclerview: https://guides.codepath.com/android/using-the-recyclerview#create-the-recyclerview-within-layout
 */
public class MovieFragment extends Fragment implements FetchMoviesResponse {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private final String TAG_POPULAR = "popular";
    private final String TAG_TOPRATED = "top_rated";

    private String CURRENT_TAG = TAG_POPULAR;

    LinearLayoutManager layoutManager;
    ImageAdapter2 imageAdapter2;
    ArrayList<TMDBMovie> movies;

    View rootview;
   // GridView movieGrid;
    RecyclerView movieView;

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
            //movieGrid = (GridView) rootview.findViewById(R.id.movieGrid);
            //movieGrid.setClickable(true);
            movieView = (RecyclerView) rootview.findViewById(R.id.movieGrid1);
            movieView.setLayoutManager(new GridLayoutManager(getContext(),4));
            movieView.setClickable(true);

        }

        return rootview;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                 CURRENT_TAG= TAG_POPULAR;
                 updateMoviesList(TAG_POPULAR);
                 break;

            case R.id.action_toprated:
                 CURRENT_TAG=TAG_TOPRATED;
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
        updateMoviesList(CURRENT_TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMoviesList(CURRENT_TAG);
    }

    private void updateMoviesList(String tag_movie){
        FetchMoviesTask FPM = new FetchMoviesTask();
        FPM.delegate=this;
        FPM.execute(tag_movie);
    }

    @Override
    public void processFinish(ArrayList<TMDBMovie> movieList) {
        this.movies=movieList;

        imageAdapter2 = new ImageAdapter2(getContext(),movies);
        movieView.setAdapter(imageAdapter2);

    }


}
