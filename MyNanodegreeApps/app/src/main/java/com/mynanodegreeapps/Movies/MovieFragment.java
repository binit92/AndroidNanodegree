package com.mynanodegreeapps.Movies;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



/**
 * Created by binit92 on 1/14/2017.
 *
 *  notes on recyclerview: https://guides.codepath.com/android/using-the-recyclerview#create-the-recyclerview-within-layout
 */
public class MovieFragment extends Fragment implements IMoviesConstants {

    private final String LOG_TAG = MovieFragment.class.getSimpleName();
    private final String TAG_POPULAR = "popular";
    private final String TAG_TOPRATED = "top_rated";

    private String CURRENT_TAG = TAG_POPULAR;

    ImageAdapter imageAdapter;
    ArrayList<TMDBMovie> movieArrayList = new ArrayList<>();

    View rootview;
   // GridView movieGrid;
    RecyclerView movieView;

    StringRequest movieListRequest;
    RequestQueue movieListRequestQueue ;

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

        movieListRequestQueue =  Volley.newRequestQueue(this.getContext());

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

    @Override
    public void onStop() {
        super.onStop();
    }

    private void updateMoviesList(String tag_movie){
        Uri requestUri = Uri.parse(MOVIEDB_BASE_URL+tag_movie).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANUGAGE,"en-us")
                .build();

        // Request a string response from the provided URL
        movieListRequest =  new StringRequest(Request.Method.GET, requestUri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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


        // Set Adapter
        imageAdapter = new ImageAdapter(getContext(),movieArrayList);
        movieView.setAdapter(imageAdapter);
    }

}
