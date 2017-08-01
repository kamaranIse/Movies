package com.example.twanist.project_movie;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;



public class MoviesContentProvider extends ContentProvider {
    static final String PROVIDER_NAME = "com.example.twanist.project_movie";
    static final String URL = "content://" + PROVIDER_NAME + "/Movies";
    public static final int MOVIE = 100;
    public static final int MOVIE_WITH_ID = 101;
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final UriMatcher uriMatcher = buildUriMatcher();
    private SqliteMovies sqliteMovies;
    private SQLiteDatabase db;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "Movies", MOVIE);
        uriMatcher.addURI(PROVIDER_NAME, "Movies/#", MOVIE_WITH_ID);
        return uriMatcher;

    }

    @Override
    public boolean onCreate() {
        sqliteMovies = new SqliteMovies(getContext());
        db = sqliteMovies.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {

        int match = uriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case MOVIE:
                cursor = db.query(MoviesContract.MoviesContractEntry.TABALE_NAME,
                        strings, s, strings1, null, null, s1);
                break;
            default:
                throw new SQLiteException("Not Match:" + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        Uri uriReturn;
        switch (match) {
            case MOVIE:
                long id = db.insert(MoviesContract.MoviesContractEntry.TABALE_NAME, "", contentValues);
                if (id > 0) {
                    uriReturn = ContentUris.withAppendedId(CONTENT_URI, id);
                } else {
                    throw new SQLiteException("Not Insert Row:" + uri);
                }
                break;

            default:
                throw new SQLiteException("filed:" + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);

        return uriReturn;

    }

    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        if (null == selection) selection = "1";
        switch (uriMatcher.match(uri)) {
            case MOVIE:
                numRowsDeleted = sqliteMovies.getWritableDatabase().delete(
                        MoviesContract.MoviesContractEntry.TABALE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numRowsDeleted != 0) {

            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numRowsDeleted;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}