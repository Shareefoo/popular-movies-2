package com.udacity.popularmovies2.api;

import com.udacity.popularmovies2.models.MoviesResponse;
import com.udacity.popularmovies2.models.ReviewsResponse;
import com.udacity.popularmovies2.models.TrailersResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by shareefoo
 */

public interface ApiInterface {

    @GET("popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailersResponse> getMovieTrailers(@Path("id") int id,
                                            @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewsResponse> getMovieReviews(@Path("id") int id,
                                          @Query("api_key") String apiKey);

}