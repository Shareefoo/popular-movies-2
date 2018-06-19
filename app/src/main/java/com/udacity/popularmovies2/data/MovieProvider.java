package com.udacity.popularmovies2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    private MovieDBHelper mOpenHelper;

    // Use an int for each URI we will run, this represents the different queries
    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    /**
     * Builds a UriMatcher that is used to determine witch database request is being made.
     */
    public static UriMatcher buildUriMatcher() {

        String authority = MovieContract.CONTENT_AUTHORITY;

        // All paths to the UriMatcher have a corresponding code to return
        // when a match is found (the ints above).
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME + "/#", MOVIE_ID);

        return matcher;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;

            case MOVIE_ID:
                String _ID = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{_ID};
                cursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieEntry._ID + " = ?",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id != -1) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return MovieContract.MovieEntry.buildMovieUri(_id);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rows;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                rows = db.delete(MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                if (selection == null || rows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rows;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int rows;

        switch (sUriMatcher.match(uri)) {
            case MOVIE:
                rows = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection, selectionArgs);
                if (rows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return rows;
    }

}
