package com.udacity.popularmovies2.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.udacity.popularmovies2";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_URI + "/" + TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_URI + "/" + TABLE_NAME;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }

}
