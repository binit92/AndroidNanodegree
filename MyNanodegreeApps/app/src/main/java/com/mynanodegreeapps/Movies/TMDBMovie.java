package com.mynanodegreeapps.Movies;

/**
 * Created by binit92 on 12/4/2016.
 */
public class TMDBMovie {

    String movieName= null;
    String moviePosterPath=null;
    String movieReleaseDate=null;
    String movieVoteAverage=null;
    String moviePlotSynopsis=null;


    TMDBMovie(String movieName, String moviePosterPath,String movieReleaseDate, String movieVoteAverage, String moviePlotSynopsis){
        this.movieName = movieName;
        this.moviePosterPath= moviePosterPath;
        this.movieReleaseDate = movieReleaseDate;
        this.movieVoteAverage = movieVoteAverage;
        this.moviePlotSynopsis = moviePlotSynopsis;
    }

    String getMovieName(){
        return movieName;
    }

    String getMoviePosterPath(){
        return moviePosterPath;
    }

    String getMovieReleaseDate() {return movieReleaseDate;}

    String getMovieVoteAverage() {return movieVoteAverage;}

    String getMoviePlotSynopsis() {return moviePlotSynopsis;}
}
