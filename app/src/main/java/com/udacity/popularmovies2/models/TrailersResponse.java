package com.udacity.popularmovies2.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shareefoo
 */

public class TrailersResponse {

    @SerializedName("id")
    public Integer id;

    @SerializedName("results")
    public List<TrailerResult> results = null;

}
