package com.mynanodegreeapps.movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mynanodegreeapps.BuildConfig;
import com.mynanodegreeapps.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *  Created by binit92 on 1/14/2017.
 *  Todo: add onclick from butterknife
 *        add an inner class for adapter
 */
public class MovieDetailActivity extends AppCompatActivity implements IMoviesConstants{
    @Bind(R.id.movie_Image)
    ImageView moviePoster;

    @Bind(R.id.favorite)
    ImageView favorite;

    @Bind(R.id.movie_Title)
    TextView movieTitle;

    @Bind(R.id.movie_Plot)
    TextView moviePlot;

    @Bind(R.id.movie_ReleaseDate)
    TextView movieRelease;

    @Bind(R.id.movie_Votes)
    TextView movieVote;


    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    RequestQueue moviesDetailsRequestQueue ;

    private static final int MOVIE_TRAILER_LIST = 0;
    private static final int MOVIE_REVIEW_LIST = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moviesdetails);
        ButterKnife.bind(this);

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

            Picasso.with(getApplicationContext()).load(url).resize(600,800).into(moviePoster);
            movieTitle.setText(title);
            moviePlot.setText(plot);
            movieRelease.setText(release_date);
            movieVote.setText(vote);

            moviesDetailsRequestQueue =  Volley.newRequestQueue(this.getApplicationContext());
            getMovieTrailerList(id);
            getMovieReviewList(id);
        }

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
                        // Construct the arrayList
                        ArrayList<MovieTrailer> movieTrailers = new ArrayList<MovieTrailer>();

                        try {
                        JSONObject movieVideoJSON = new JSONObject(response);
                        JSONArray movieArray = movieVideoJSON.getJSONArray(MDB_RESULTS);

                        for (int i = 0; i < movieArray.length(); i++) {
                            JSONObject movieObject = movieArray.getJSONObject(i);

                            String movieKey = movieObject.getString(MDB_KEY);
                            String movieName = movieObject.getString(MDB_NAME);

                            System.out.println("--> movieName is "+ movieName);
                            System.out.println("--> movieKey is "+ movieKey);

                            MovieTrailer movieTrailer = new MovieTrailer(movieName,movieKey);
                            movieTrailers.add(movieTrailer);
                        }

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.trailerLayout);
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                        for(MovieTrailer obj : movieTrailers){
                            View view = inflater.inflate(R.layout.custom_movietrailer,linearLayout,false);
                            TextView trailerName = (TextView)view.findViewById(R.id.trailer_name);

                            final String key = obj.key;
                            trailerName.setText(obj.name);
                            trailerName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    System.out.println("--> Open Youtube with Key "+ key);
                                    String url = YOUTUBE_URL+key;
                                    Intent i = new Intent(Intent.ACTION_VIEW);
                                    i.setData(Uri.parse(url));
                                    startActivity(i);
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
                        // Construct the arrayList
                        ArrayList<MovieReview> movieReviews = new ArrayList<MovieReview>();

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

                        System.out.println("--> movieReview.size() is " + movieReviews.size());

                        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.reviewLayout);
                        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
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
