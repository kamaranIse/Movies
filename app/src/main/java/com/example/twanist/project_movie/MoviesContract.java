package com.example.twanist.project_movie;

import android.provider.BaseColumns;


public class MoviesContract {
    private MoviesContract() {
    }


    public static class MoviesContractEntry implements BaseColumns {
        public static final String TABALE_NAME = "Movies";
        public static final String POSTER_PATH = "poster_path";
        public static final String BACKDROP_PATH = "backdrop_path";
        public static final String TITLE = "Title";
        public static final String OVERVIEW = "Overview";
        public static final String RELEASE_DATE = "Release_date";
        public static final String VOTE_AVERAGE = "Vote_Average";


    }

}
