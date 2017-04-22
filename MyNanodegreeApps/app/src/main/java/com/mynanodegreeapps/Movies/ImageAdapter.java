package com.mynanodegreeapps.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mynanodegreeapps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by binit92 on 1/27/2017.
 */

// Create the basic adapter extending from RecyclerView
// Note:- that we specify the custom ViewHolder which gives us access to our views
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{

    private Context c ;
    ArrayList<TMDBMovie> movieList;
    private int source = 1;

    public static final int SOURCE_NETWORK = 1;
    public static final int SOURCE_DB = 2;

    private IImageAdapterCallback iImageAdapterCallback;

    // Pass in the movie array into the constructor
    public ImageAdapter(Context context, ArrayList<TMDBMovie> movieList, int source, IImageAdapterCallback callback) {
        this.c = context;
        this.movieList = movieList;
        this.source = source;
        this.iImageAdapterCallback = callback;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View moviePosterView = inflater.inflate(R.layout.custom_movieposter,parent,false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(moviePosterView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ImageAdapter.ViewHolder viewHolder , final int position) {
        // Get the data model based on position
        TMDBMovie selectedMovie = movieList.get(position);

        // Set item views based on your views and data model
        ImageView imageView = viewHolder.posterImage;

        if(source == SOURCE_DB) {
            byte[] imageArray = movieList.get(position).getMovieImageBlob();
            Bitmap bm = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            imageView.setImageBitmap(bm);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String title = movieList.get(position).getMovieName();
                    byte[] imageArray = movieList.get(position).getMovieImageBlob();
                    String releaseDate = movieList.get(position).getMovieReleaseDate();
                    String voteAvg = movieList.get(position).getMovieVoteAverage();
                    String plot = movieList.get(position).getMoviePlotSynopsis();
                    String id = movieList.get(position).getMovieId();

                    Bundle b = new Bundle();
                    b.putString("title",title);
                    b.putByteArray("imageBlob",imageArray);
                    b.putString("releaseDate", releaseDate);
                    b.putString("voteAvg", voteAvg);
                    b.putString("plot",plot);
                    b.putString("id", id);
                    b.putInt("source", source);

                    /*
                    Intent intent = new Intent(c, MovieDetailActivity.class);
                    intent.setType(Integer.toString(SOURCE_DB));
                    intent.putExtra("title", title);
                    intent.putExtra("imageBlob", imageArray);
                    intent.putExtra("releaseDate", releaseDate);
                    intent.putExtra("voteAvg", voteAvg);
                    intent.putExtra("plot", plot);
                    intent.putExtra("id", id);
                    */
                    iImageAdapterCallback.onMovieSelect(b);

                }
            });

        }else{

            final String baseImageUrl = "http://image.tmdb.org/t/p/w185";
            String url = baseImageUrl + selectedMovie.getMoviePosterPath();

            //Automatically creates bacground thread and loads image
            Picasso.with(c).load(url).into(imageView);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String title = movieList.get(position).getMovieName();
                    String url = baseImageUrl + movieList.get(position).getMoviePosterPath();
                    String releaseDate = movieList.get(position).getMovieReleaseDate();
                    String voteAvg = movieList.get(position).getMovieVoteAverage();
                    String plot = movieList.get(position).getMoviePlotSynopsis();
                    String id = movieList.get(position).getMovieId();

                    Bundle b = new Bundle();
                    b.putString("title",title);
                    b.putString("url",url);
                    b.putString("releaseDate", releaseDate);
                    b.putString("voteAvg", voteAvg);
                    b.putString("plot",plot);
                    b.putString("id", id);
                    b.putInt("source", source);


                    /*
                    Intent intent = new Intent(c, MovieDetailActivity.class);
                    intent.setType(Integer.toString(SOURCE_NETWORK));
                    intent.putExtra("title", title);
                    intent.putExtra("url", url);
                    intent.putExtra("releaseDate", releaseDate);
                    intent.putExtra("voteAvg", voteAvg);
                    intent.putExtra("plot", plot);
                    intent.putExtra("id", id);
                    */


                    iImageAdapterCallback.onMovieSelect(b);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }



    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ImageView posterImage;
        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            posterImage = (ImageView) itemView.findViewById(R.id.moviePoster);
        }
    }


}
