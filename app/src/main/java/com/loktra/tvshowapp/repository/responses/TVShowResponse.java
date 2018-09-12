package com.loktra.tvshowapp.repository.responses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.loktra.tvshowapp.repository.database.dao.data_convertor.DataConvertor;

import java.util.Arrays;
import java.util.List;

@Entity(tableName = "TvResponse_Table")
public class TVShowResponse {

    @PrimaryKey(autoGenerate = true)
    public int id;


    @ColumnInfo(name = "TvShow_Title")
    @NonNull
    @SerializedName("name")
    @Expose
    public String tvShowName;

    @ColumnInfo(name = "Language")
    @SerializedName("language")
    @Expose
    public String language;

    @ColumnInfo(name = "Genres")
    @TypeConverters(DataConvertor.class)
    @SerializedName("genres")
    @Expose
    public List<String> genres;

    @Embedded
    @SerializedName("rating")
    @Expose
    public Rating rating;

    @Embedded
    @SerializedName("image")
    @Expose
    public Image posterImage;

    @ColumnInfo(name = "Summary")
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
    public static class Rating {

        @ColumnInfo(name = "Rating")
        @SerializedName("average")
        @Expose
        public float average;
    }

    // Tv Poster Image
    public static class Image {

        @ColumnInfo(name = "Image_Url")
        @SerializedName("medium")
        @Expose
        public String medium;

        @ColumnInfo(name = "Image_Poster")
        @SerializedName("original")
        @Expose
        public String original;

    }
}