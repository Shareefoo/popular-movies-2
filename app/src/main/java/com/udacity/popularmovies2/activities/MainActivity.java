package com.udacity.popularmovies2.activities;


import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.udacity.popularmovies2.BuildConfig;
import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.adapters.MovieAdapter;
import com.udacity.popularmovies2.api.ApiClient;
import com.udacity.popularmovies2.api.ApiInterface;
import com.udacity.popularmovies2.app.Utils;
import com.udacity.popularmovies2.data.MovieContract;
import com.udacity.popularmovies2.data.MovieDBHelper;
import com.udacity.popularmovies2.models.Movie;
import com.udacity.popularmovies2.models.MoviesResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerView_movies)
    RecyclerView recyclerViewMovies;

    private AlertDialog progressDialog;
    private MovieAdapter movieAdapter;

    private SQLiteDatabase mDb;
    private MovieDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dbHelper = new MovieDBHelper(this);

        //
        getPopularMovies(BuildConfig.MOVIE_DB_API_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {
            getPopularMovies(BuildConfig.MOVIE_DB_API_KEY);
            return true;
        } else if (id == R.id.action_top) {
            getTopRatedMovies(BuildConfig.MOVIE_DB_API_KEY);
            return true;
        } else if (id == R.id.action_favorite) {
            getFavoriteMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPopularMovies(String apiKey) {
        if (Utils.isNetworkConnected(MainActivity.this)) {
            progressDialog = new SpotsDialog(MainActivity.this, R.style.AlertDialogTheme);
            progressDialog.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> popularMoviesResponseCall = apiService.getPopularMovies(apiKey);
            popularMoviesResponseCall.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {

                        MoviesResponse popularMoviesResponse = response.body();

                        if (popularMoviesResponse != null) {
                            List<Movie> movies = popularMoviesResponse.movies;
                            movieAdapter = new MovieAdapter(MainActivity.this, movies);
                            recyclerViewMovies.setAdapter(movieAdapter);
                            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            recyclerViewMovies.setLayoutManager(gridLayoutManager);
                        }

                    } else {
                        try {
                            JSONObject errorObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG, "onResponse: " + errorObject.toString());
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_LONG).show();
        }
    }

    private void getTopRatedMovies(String apiKey) {
        if (Utils.isNetworkConnected(MainActivity.this)) {
            progressDialog = new SpotsDialog(MainActivity.this, R.style.AlertDialogTheme);
            progressDialog.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MoviesResponse> popularMoviesResponseCall = apiService.getTopRatedMovies(apiKey);
            popularMoviesResponseCall.enqueue(new Callback<MoviesResponse>() {
                @Override
                public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {

                        MoviesResponse popularMoviesResponse = response.body();

                        if (popularMoviesResponse != null) {
                            List<Movie> movies = popularMoviesResponse.movies;
                            movieAdapter = new MovieAdapter(MainActivity.this, movies);
                            recyclerViewMovies.setAdapter(movieAdapter);
                            StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                            recyclerViewMovies.setLayoutManager(gridLayoutManager);
                        }

                    } else {
                        try {
                            JSONObject errorObject = new JSONObject(response.errorBody().string());
                            Log.e(TAG, "onResponse: " + errorObject.toString());
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: " + e.getMessage());
                        }
                    }

                }

                @Override
                public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_LONG).show();
        }
    }

    private void getFavoriteMovies() {

        mDb = dbHelper.getWritableDatabase();

        List<Movie> movies = new ArrayList<>();

        Cursor cursor = mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry._ID));
            movie.title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            movie.posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
            movie.releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
            movie.voteAverage = cursor.getDouble(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));
            movie.overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
            movies.add(movie);
        }

        cursor.close();

        movieAdapter = new MovieAdapter(MainActivity.this, movies);
        recyclerViewMovies.setAdapter(movieAdapter);
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerViewMovies.setLayoutManager(gridLayoutManager);
    }

}