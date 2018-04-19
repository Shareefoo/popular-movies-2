package com.udacity.popularmovies2.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies2.BuildConfig;
import com.udacity.popularmovies2.R;
import com.udacity.popularmovies2.adapters.ReviewAdapter;
import com.udacity.popularmovies2.adapters.TrailerAdapter;
import com.udacity.popularmovies2.api.ApiClient;
import com.udacity.popularmovies2.api.ApiInterface;
import com.udacity.popularmovies2.app.Utils;
import com.udacity.popularmovies2.data.MovieContract;
import com.udacity.popularmovies2.data.MovieDBHelper;
import com.udacity.popularmovies2.models.Movie;
import com.udacity.popularmovies2.models.ReviewResult;
import com.udacity.popularmovies2.models.ReviewsResponse;
import com.udacity.popularmovies2.models.TrailerResult;
import com.udacity.popularmovies2.models.TrailersResponse;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by shareefoo
 */

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    @BindView(R.id.imageView_poster)
    ImageView imageViewPoster;

    @BindView(R.id.textView_title)
    TextView textViewTitle;

    @BindView(R.id.textView_date)
    TextView textViewDate;

    @BindView(R.id.textView_vote)
    TextView textViewVote;

    @BindView(R.id.textView_overview)
    TextView textViewOverview;

    @BindView(R.id.recyclerView_trailers)
    RecyclerView recyclerViewTrailers;

    @BindView(R.id.recyclerView_reviews)
    RecyclerView recyclerViewReviews;

    private AlertDialog progressDialog;
    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private Movie movie;

    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        String posterUrl = "http://image.tmdb.org/t/p/w185/" + movie.posterPath;
        String releaseDate = "(" + movie.releaseDate + ")";

        Picasso.get().load(posterUrl).into(imageViewPoster);

        textViewTitle.setText(movie.title);
        textViewDate.setText(releaseDate);
        textViewVote.setText(String.valueOf(movie.voteAverage));
        textViewOverview.setText(movie.overview);

        getMovieTrailers(movie.id, BuildConfig.MOVIE_DB_API_KEY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            addFavorite();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addFavorite() {
        MovieDBHelper dbHelper = new MovieDBHelper(this);
        mDb = dbHelper.getWritableDatabase();

        Cursor cursor = mDb.rawQuery("SELECT * FROM movieTable WHERE _id = ?", new String[]{String.valueOf(movie.id)});

        // if favorite
        if (cursor != null && cursor.moveToFirst()) {
            long result = mDb.delete(MovieContract.MovieEntry.TABLE_NAME, MovieContract.MovieEntry._ID + " = ?", new String[]{String.valueOf(movie.id)});
            if (result > 0) {
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }
        // if not favorite
        else {
            ContentValues values = new ContentValues();
            values.put(MovieContract.MovieEntry._ID, movie.id);
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, movie.title);
            values.put(MovieContract.MovieEntry.COLUMN_POSTER, movie.posterPath);
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate);
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, movie.voteAverage);
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.overview);
            long result = mDb.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
            if (result > 0) {
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getMovieTrailers(int id, String apiKey) {
        if (Utils.isNetworkConnected(MovieDetailsActivity.this)) {
            progressDialog = new SpotsDialog(MovieDetailsActivity.this, R.style.AlertDialogTheme);
            progressDialog.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<TrailersResponse> trailersResponseCall = apiService.getMovieTrailers(id, apiKey);
            trailersResponseCall.enqueue(new Callback<TrailersResponse>() {
                @Override
                public void onResponse(@NonNull Call<TrailersResponse> call, @NonNull Response<TrailersResponse> response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {

                        TrailersResponse trailersResponse = response.body();

                        if (trailersResponse != null) {
                            List<TrailerResult> trailers = trailersResponse.results;
                            trailerAdapter = new TrailerAdapter(MovieDetailsActivity.this, trailers);
                            recyclerViewTrailers.setAdapter(trailerAdapter);
                            recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this));

                            //
                            getMovieReviews(movie.id, BuildConfig.MOVIE_DB_API_KEY);
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
                public void onFailure(@NonNull Call<TrailersResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_LONG).show();
        }
    }

    private void getMovieReviews(int id, String apiKey) {
        if (Utils.isNetworkConnected(MovieDetailsActivity.this)) {
            progressDialog = new SpotsDialog(MovieDetailsActivity.this, R.style.AlertDialogTheme);
            progressDialog.show();
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ReviewsResponse> trailersResponseCall = apiService.getMovieReviews(id, apiKey);
            trailersResponseCall.enqueue(new Callback<ReviewsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ReviewsResponse> call, @NonNull Response<ReviewsResponse> response) {
                    Log.d(TAG, "onResponse: " + response.toString());
                    progressDialog.dismiss();

                    if (response.isSuccessful()) {

                        ReviewsResponse reviewsResponse = response.body();

                        if (reviewsResponse != null) {
                            List<ReviewResult> reviews = reviewsResponse.results;
                            reviewAdapter = new ReviewAdapter(MovieDetailsActivity.this, reviews);
                            recyclerViewReviews.setAdapter(reviewAdapter);
                            recyclerViewReviews.setLayoutManager(new LinearLayoutManager(MovieDetailsActivity.this));
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
                public void onFailure(@NonNull Call<ReviewsResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, "No Internet Connection !", Toast.LENGTH_LONG).show();
        }
    }

}
