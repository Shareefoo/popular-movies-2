package com.udacity.popularmovies2.models;

import com.google.gson.annotations.SerializedName;

public class TrailerResult {

    @SerializedName("id")
    public String id;

    @SerializedName("iso_639_1")
    public String iso6391;

    @SerializedName("iso_3166_1")
    public String iso31661;

    @SerializedName("key")
    public String key;

    @SerializedName("name")
    public String name;

    @SerializedName("site")
    public String site;

    @SerializedName("size")
    public Integer size;

    @SerializedName("type")
    public String type;

}
