package com.mynanodegreeapps.Movies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by binit92 on 1/14/2017.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<TMDBMovie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    public FetchMoviesResponse delegate = null;


    protected void onPostExecute(ArrayList<TMDBMovie> movieList) {
           delegate.processFinish(movieList);

    }

    @Override
    protected ArrayList<TMDBMovie> doInBackground(String... query) {

        ArrayList<TMDBMovie> results = new ArrayList<>();

        HttpURLConnection urlConnection = null;


        String movieJsonStr = null;
        String movieFlag = query[0];
        String language = "en-US";


        // details at:- https://developers.themoviedb.org/3/getting-started/authentication

        final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
        final String API_PARAM = "api_key";
        final String LANUGAGE = "language";

        // Todo: Add API KEY under API_PARAM to successfully run !
        try {
            Uri builtUri = Uri.parse(MOVIEDB_BASE_URL + movieFlag).buildUpon()
                    .appendQueryParameter(API_PARAM,"")
                    .appendQueryParameter(LANUGAGE, language)
                    .build();

            URL url = new URL(builtUri.toString());
               Log.v(LOG_TAG, "Built URI: " + builtUri.toString());


            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            StringBuffer stringBuffer = new StringBuffer();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            while (data !=  -1){
                char current = (char) data;
                data = isw.read();
                stringBuffer.append(current);
            }

            movieJsonStr = stringBuffer.toString();

            getImagesUrlsFromJson(movieJsonStr,results);

        } catch (Exception e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }

        return results;
    }

    void getImagesUrlsFromJson(String movieJsonStr,ArrayList<TMDBMovie> movieArrayList) throws JSONException {

        // These are the names of the JSON objects that need to be extracted
        final String MDB_RESULTS="results";
        final String MDB_MOVIENAME="title";
        final String MDB_POSTERPATH="poster_path";
        final String MDB_RELEASEDATE ="release_date";
        final String MDB_VOTEAVERAGE ="vote_average";
        final String MDB_PLOTSYNOPSIS="overview";

        JSONObject movieJSON = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJSON.getJSONArray(MDB_RESULTS);

        for(int i=0; i<movieArray.length();i++){
            JSONObject movieObject= movieArray.getJSONObject(i);

            String movieName = movieObject.getString(MDB_MOVIENAME);
            String moviePosterPath = movieObject.getString(MDB_POSTERPATH);
            String movieReleaseDate = movieObject.getString(MDB_RELEASEDATE);
            String movieVoteAverage = movieObject.getString(MDB_VOTEAVERAGE);
            String moviePlotSynopsis = movieObject.getString(MDB_PLOTSYNOPSIS);

            TMDBMovie tmdbMovie = new TMDBMovie(movieName,moviePosterPath,movieReleaseDate,movieVoteAverage,moviePlotSynopsis);
            movieArrayList.add(tmdbMovie);

        }

    }
}
