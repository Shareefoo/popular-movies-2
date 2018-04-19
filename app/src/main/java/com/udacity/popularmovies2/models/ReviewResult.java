package com.udacity.popularmovies2.models;

import com.google.gson.annotations.SerializedName;

public class ReviewResult {

    @SerializedName("author")
    public String author;

    @SerializedName("content")
    public String content;

    @SerializedName("id")
    public String id;

    @SerializedName("url")
    public String url;

}
