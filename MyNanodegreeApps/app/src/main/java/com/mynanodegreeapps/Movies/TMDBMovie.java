package com.mynanodegreeapps.movies;

/**
 * Created by binit92 on 12/4/2016.
 */
public class TMDBMovie {

    String movieName;
    String moviePosterPath;
    String movieReleaseDate;
    String movieVoteAverage;
    String moviePlotSynopsis;
    String movieId;

    byte[] movieImageBlob;


    TMDBMovie(String movieName, String moviePosterPath,String movieReleaseDate, String movieVoteAverage, String moviePlotSynopsis,String movieId){
        this.movieName = movieName;
        this.moviePosterPath= moviePosterPath;
        this.movieReleaseDate = movieReleaseDate;
        this.movieVoteAverage = movieVoteAverage;
        this.moviePlotSynopsis = moviePlotSynopsis;
        this.movieId = movieId;
    }

    // Todo : don't do multiple constructor as told in "Effective Java"
    TMDBMovie(String movieName, byte[] movieImageBlob,String movieReleaseDate,String movieVoteAverage,String moviePlotSynopsis,String movieId){
        this.movieName = movieName;
        this.movieImageBlob = movieImageBlob;
        this.movieReleaseDate = movieReleaseDate;
        this.movieVoteAverage = movieVoteAverage;
        this.moviePlotSynopsis = moviePlotSynopsis;
        this.movieId = movieId;
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

    String getMovieId() {return movieId;}

    byte[] getMovieImageBlob() { return movieImageBlob; }
}
