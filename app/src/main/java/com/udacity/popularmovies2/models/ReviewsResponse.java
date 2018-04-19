package com.udacity.popularmovies2.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shareefoo
 */

public class ReviewsResponse {

    @SerializedName("id")
    public Integer id;

    @SerializedName("page")
    public Integer page;

    @SerializedName("results")
    public List<ReviewResult> results = null;

    @SerializedName("total_pages")
    public Integer totalPages;

    @SerializedName("total_results")
    public Integer totalResults;

}
