package com.example.twanist.project_movie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class SqliteMovies extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Movies";
    private static final int VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + MoviesContract.MoviesContractEntry.TABALE_NAME + " (" +
                    MoviesContract.MoviesContractEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    MoviesContract.MoviesContractEntry.BACKDROP_PATH + " TEXT," +
                    MoviesContract.MoviesContractEntry.POSTER_PATH + " TEXT," +
                    MoviesContract.MoviesContractEntry.TITLE + " TEXT," +
                    MoviesContract.MoviesContractEntry.OVERVIEW + " TEXT," +
                    MoviesContract.MoviesContractEntry.VOTE_AVERAGE + " TEXT," +
                    MoviesContract.MoviesContractEntry.RELEASE_DATE + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MoviesContract.MoviesContractEntry.TABALE_NAME;

    public SqliteMovies(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
