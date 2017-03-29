package com.mynanodegreeapps.Movies;

public interface IMoviesConstants {

    public static final String MOVIEDB_BASE_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_PARAM = "api_key";
    public static final String LANUGAGE = "language";
    public static final String REVIEWS = "/reviews";
    public static final String VIDEOS = "/videos";


    // These are the names of the JSON objects that need to be extracted
    public static final String MDB_RESULTS="results";
    public static final String MDB_MOVIENAME="title";
    public static final String MDB_POSTERPATH="poster_path";
    public static final String MDB_RELEASEDATE ="release_date";
    public static final String MDB_VOTEAVERAGE ="vote_average";
    public static final String MDB_PLOTSYNOPSIS="overview";
    public static final String MDB_ID = "id";

    // These are for videos
    public static final String MDB_KEY = "key";
    public static final String MDB_NAME= "name";

    // These are for reviews
    public static final String MDB_CONTENTS="content";
    public static final String MDB_AUTHOR="author";

    // Youtube URL
    public static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
}
