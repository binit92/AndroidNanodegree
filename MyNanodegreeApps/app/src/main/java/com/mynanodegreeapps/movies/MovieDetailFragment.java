package com.mynanodegreeapps.movies;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.mynanodegreeapps.BuildConfig;
import com.mynanodegreeapps.R;
import com.mynanodegreeapps.data.MovieContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.OnTouch;

public class MovieDetailFragment extends Fragment implements IMoviesConstants,Target {


    @Bind(R.id.movie_Image)
    ImageView moviePoster;

    @Bind(R.id.favorite)
    MaterialFavoriteButton favorite;

    @Bind(R.id.movie_Title)
    TextView movieTitle;

    @Bind(R.id.movie_Plot)
    TextView moviePlot;

    @Bind(R.id.movie_ReleaseDate)
    TextView movieRelease;

    @Bind(R.id.movie_Votes)
    TextView movieVote;

    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    RequestQueue moviesDetailsRequestQueue ;

    // data
    ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();
    ArrayList<MovieReview> movieReviews = new ArrayList<MovieReview>();
    String url = null;
    String title;
    String plot;
    String release_date;
    String vote;
    String id;

    //source
    int source = ImageAdapter.SOURCE_NETWORK;
    View rootView;
    byte[] imageBlob;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_moviedetails,container,false);

        if(rootView != null) {

            ButterKnife.bind(this,rootView);

            Bundle extras = this.getArguments();
            if(extras != null) {

                title = extras.getString("title");
                plot = extras.getString("plot");
                release_date = extras.getString("releaseDate");
                vote = extras.getString("voteAvg");
                id = extras.getString("id");

                movieTitle.setText(title);
                moviePlot.setText(plot);
                movieRelease.setText(release_date);
                movieVote.setText(vote);

                source = extras.getInt("source");
                if (source == ImageAdapter.SOURCE_DB) {
                    imageBlob = extras.getByteArray("imageBlob");
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
                    moviePoster.setImageBitmap(bmp);

                } else {
                    url = extras.getString("url");
                    Log.d("url", url);
                    Picasso.with(getContext()).load(url).into(this);

                    moviesDetailsRequestQueue = Volley.newRequestQueue(getContext());
                    getMovieTrailerList(id);
                    getMovieReviewList(id);
                }
            }

            if(id !=null && checkIfMovieExists()){
                favorite.setFavorite(true);
            }

            favorite.setOnFavoriteChangeListener(
                    new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override
                        public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if(favorite){
                                // Add everything to database
                                ContentValues movieContent = new ContentValues();
                                movieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                                movieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_NAME, title);
                                movieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER, imageBlob);
                                movieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASEDATE, release_date);
                                movieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_PLOT_SYNOPSIS, plot);
                                movieContent.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTEAVERAGE, vote);

                                if (!checkIfMovieExists() ) {
                                    Uri insertedUri = getContext().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieContent);
                                    // The inserted URI contains the id for the row.
                                    // Extract the movie ID from the Uri
                                    // long insertedid = ContentUris.parseId(insertedUri);
                                    buttonView.setAnimateFavorite(true);


                                }
                            }else{
                                // Todo: Delete from DB
                            }

                        }
                    });
        }
        return rootView;

    }

    boolean checkIfMovieExists(){
        Cursor checkMovie = getContext().getContentResolver()
                .query(MovieContract.MovieEntry.CONTENT_URI
                        , new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID}
                        , MovieContract.MovieEntry.COLUMN_MOVIE_ID+ "=?"
                        , new String[]{id}
                        , null);
        if(checkMovie.moveToFirst()){
            return true;
        }
        return false;
    }

    void getMovieTrailerList(String id){

        Uri requestUri = Uri.parse(MOVIEDB_BASE_URL+id+VIDEOS).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANUGAGE,"en-us")
                .build();

        // Request a string response from the provided URL
        StringRequest movieVideosListRequest  =  new StringRequest(Request.Method.GET, requestUri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject movieVideoJSON = new JSONObject(response);
                            JSONArray movieArray = movieVideoJSON.getJSONArray(MDB_RESULTS);

                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                String movieKey = movieObject.getString(MDB_KEY);
                                String movieName = movieObject.getString(MDB_NAME);


                                MovieTrailer movieTrailer = new MovieTrailer(movieName,movieKey);
                                movieTrailers.add(movieTrailer);
                            }

                            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.trailerLayout);
                            LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
                            for(MovieTrailer obj : movieTrailers){
                                View view = inflater.inflate(R.layout.custom_movietrailer,linearLayout,false);
                                TextView trailerName = (TextView)view.findViewById(R.id.trailer_name);
                                final String key = obj.key;
                                trailerName.setText(obj.name);
                                trailerName.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String url = YOUTUBE_URL+key;
                                        Intent i = new Intent(Intent.ACTION_VIEW);
                                        i.setData(Uri.parse(url));
                                        if(i.resolveActivity(getActivity().getPackageManager())!= null){
                                            startActivity(i);
                                        }

                                    }
                                });

                                linearLayout.addView(view);
                            }

                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        // set the TAG
        movieVideosListRequest.setTag(LOG_TAG);

        // Add the request to the RequestQueue
        moviesDetailsRequestQueue.add(movieVideosListRequest);
        //movieTrailerMap.put("name","value");
    }

    void getMovieReviewList(String id){
        Uri requestUri = Uri.parse(MOVIEDB_BASE_URL+id+REVIEWS).buildUpon()
                .appendQueryParameter(API_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                .appendQueryParameter(LANUGAGE,"en-us")
                .build();

        // Request a string response from the provided URL
        StringRequest movieReviewsListRequest  =  new StringRequest(Request.Method.GET, requestUri.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject movieVideoJSON = new JSONObject(response);
                            JSONArray movieArray = movieVideoJSON.getJSONArray(MDB_RESULTS);

                            for (int i = 0; i < movieArray.length(); i++) {
                                JSONObject movieObject = movieArray.getJSONObject(i);

                                String reviewAuthor = movieObject.getString(MDB_AUTHOR);
                                String reviewContent = movieObject.getString(MDB_CONTENTS);

                                MovieReview movieReview = new MovieReview(reviewAuthor,reviewContent);
                                movieReviews.add(movieReview);
                            }

                            LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.reviewLayout);
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            for(MovieReview obj : movieReviews){
                                View view = inflater.inflate(R.layout.custom_moviereviews,linearLayout,false);
                                TextView reviewAuthor = (TextView)view.findViewById(R.id.review_author);
                                TextView reviewContent = (TextView)view.findViewById(R.id.review_content);

                                reviewAuthor.setText(obj.author);
                                reviewContent.setText(obj.review);

                                linearLayout.addView(view);
                            }

                        /*
                        MovieReviewAdapter movieReviewAdapter = new MovieReviewAdapter(getApplicationContext(),movieReviews);
                        movieReviewList.setAdapter(movieReviewAdapter);
                        */
                        //justifyListViewHeightBasedOnChildren(movieReviewList);

                        }catch (JSONException je){
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }

        });

        // set the TAG
        movieReviewsListRequest.setTag(LOG_TAG);

        // Add the request to the RequestQueue
        moviesDetailsRequestQueue.add(movieReviewsListRequest);

    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        imageBlob= byteArray;
        moviePoster.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    public class MovieTrailer{
        public String name;
        public String key;

        public MovieTrailer(String name, String key){
            this.name = name;
            this.key = key;
        }
    }
    public class MovieReview{
        public String author;
        public String review;

        public MovieReview(String author, String review){
            this.author = author;
            this.review = review;
        }
    }

}
