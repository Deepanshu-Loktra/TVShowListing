package com.loktra.tvshowapp.repository.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;

public class TVShowResponse {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("name")
    @Expose
    public String tvShowName;

    @SerializedName("language")
    @Expose
    public String language;

    @SerializedName("genres")
    @Expose
    public List<String> genres;

    @SerializedName("rating")
    @Expose
    public Rating rating;

    @SerializedName("image")
    @Expose
    public Image posterImage;

    @SerializedName("summary")
    @Expose
    public String summary;


    @Override
    public String toString() {

        return "TVShowResponse{" +
                "id=" + id +
                ", tvShowName=" + tvShowName +
                ", language=" + language +
                ", rating=" + rating.average +
                ", posterImage=" + posterImage.medium +
                '}';
    }

    // Rating Details
    public class Rating {

        @SerializedName("average")
        @Expose
        public float average;
    }

    // Tv Poster Image
    public class Image {

        @SerializedName("medium")
        @Expose
        public String medium;

        @SerializedName("original")
        @Expose
        public String original;

    }
}