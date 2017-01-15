package com.mynanodegreeapps.Movies;

import java.util.ArrayList;

/**
 * Created by binit92 on 1/14/2017.
 */

public interface FetchMoviesResponse {
        void processFinish(ArrayList<TMDBMovie> movieList);
    }

