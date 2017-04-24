package com.mynanodegreeapps.movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mynanodegreeapps.BuildConfig;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 *  Created by binit92 on 1/14/2017. *
 *  notes on recyclerview: https://guides.codepath.com/android/using-the-recyclerview#create-the-recyclerview-within-layout
 */
public class MovieFragment extends Fragment implements IMoviesConstants, IImageAdapterCallback {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private final String TAG_POPULAR = "popular";
    private final String TAG_TOPRATED = "top_rated";
    private final String TAG_FAVORITE = "favorite";
    private String CURRENT_TAG = TAG_POPULAR;

    ImageAdapter imageAdapter;
    View rootview;
    RecyclerView movieView;

    StringRequest movieListRequest;
    RequestQueue movieListRequestQueue ;

    public MovieFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_movies, menu);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.fragment_movies,container,false);

        if(rootview!= null) {
            //movieGrid = (GridView) rootview.findViewById(R.id.movieGrid);
            //movieGrid.setClickable(true);
            movieView = (RecyclerView) rootview.findViewById(R.id.movieGrid1);
            movieView.setLayoutManager(new GridLayoutManager(getContext(),2));
            movieView.setClickable(true);
        }
        movieListRequestQueue =  Volley.newRequestQueue(this.getContext());

        if (savedInstanceState != null ) {
            CURRENT_TAG =  savedInstanceState.getString(getString(R.string.moviemenu));
        }

        return rootview;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                 CURRENT_TAG = TAG_POPULAR;
                 updateMoviesList(TAG_POPULAR);
                 break;

            case R.id.action_toprated:
                 CURRENT_TAG = TAG_TOPRATED;
                 updateMoviesList(TAG_TOPRATED);
                 break;

            case R.id.action_favorite:
                CURRENT_TAG = TAG_FAVORITE;
                 fetchFromDb();
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
        updateMovies(CURRENT_TAG);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // When tablets rotate, the currently selected list item needs to be saved.
        // When no item is selected, mPosition will be set to Listview.INVALID_POSITION,
        // so check for that before storing.

        outState.putString(getString(R.string.moviemenu),CURRENT_TAG);
        super.onSaveInstanceState(outState);
    }

    private void updateMovies(String tag_movie){
        if(CURRENT_TAG == TAG_FAVORITE){
            fetchFromDb();
        }else{
            updateMoviesList(tag_movie);
        }
    }

    private void updateMoviesList(String tag_movie){

        // clear the arraylist for now
        Uri requestUri = Uri.parse(MOVIEDB_BASE_URL+tag_movie).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANUGAGE,"en-us")
                .build();

        // Request a string response from the provided URL
        movieListRequest =  new StringRequest(Request.Method.GET, requestUri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ArrayList<TMDBMovie> movieArrayList = new ArrayList<>();
                        try {

                            JSONObject movieJSON = new JSONObject(response);
                            JSONArray movieArray = movieJSON.getJSONArray(MDB_RESULTS);

                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                String movieName = movieObject.getString(MDB_MOVIENAME);
                                String moviePosterPath = movieObject.getString(MDB_POSTERPATH);
                                String movieReleaseDate = movieObject.getString(MDB_RELEASEDATE);
                                String movieVoteAverage = movieObject.getString(MDB_VOTEAVERAGE);
                                String moviePlotSynopsis = movieObject.getString(MDB_PLOTSYNOPSIS);
                                String movieID = movieObject.getString(MDB_ID);

                                TMDBMovie tmdbMovie = new TMDBMovie(movieName, moviePosterPath, movieReleaseDate, movieVoteAverage, moviePlotSynopsis, movieID);
                                movieArrayList.add(tmdbMovie);

                            }

                            // Set Adapter
                            imageAdapter = new ImageAdapter(getContext(),movieArrayList,ImageAdapter.SOURCE_NETWORK,MovieFragment.this);
                            movieView.setAdapter(imageAdapter);

                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

        });

        // set the TAG
        movieListRequest.setTag(LOG_TAG);

        // Add the request to the RequestQueue
        movieListRequestQueue.add(movieListRequest);
    }

    private void fetchFromDb() {

        // select all the movies that exists in db
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{"*"},
                null,
                null,
                null);

        ArrayList<TMDBMovie> movieArrayList = new ArrayList<>();
        try{
            while (movieCursor.moveToNext()) {

                String movieID = movieCursor.getString(0);
                String movieName = movieCursor.getString(1);
                byte imageArray[] = movieCursor.getBlob(2);
                String movieReleaseDate = movieCursor.getString(3);
                String moviePlotSynopsis = movieCursor.getString(4);
                String movieVoteAverage = movieCursor.getString(5);

                TMDBMovie tmdbMovie = new TMDBMovie(movieName, imageArray, movieReleaseDate, movieVoteAverage, moviePlotSynopsis, movieID);
                movieArrayList.add(tmdbMovie);

            }
        }finally {
            movieCursor.close();
        }
       // reusing the same image adapter
        imageAdapter = new ImageAdapter(getContext(),movieArrayList,ImageAdapter.SOURCE_DB,this);
        movieView.setAdapter(imageAdapter);

    }

    @Override
    public void onMovieSelect(Bundle bundle) {
        // for tablets
        if(getActivity().findViewById(R.id.favorite) != null){

            MovieDetailFragment mdf = new MovieDetailFragment();
            mdf.setArguments(bundle);
            getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.movieDetailFragment, mdf)
                        .commit();
        }else {

            Intent intent = new Intent(getContext(), MovieDetailActivity.class);
            intent.putExtra("movieBundle", bundle);
            startActivity(intent);
        }
    }

}
