package com.loktra.tvshowapp.repository.database.data_convertor;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class DataConvertor {

    @TypeConverter
    public String fromCountryLangList(List<String> genres) {
        if (genres == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.toJson(genres, type);
    }

    @TypeConverter
    public List<String> toCountryLangList(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(countryLangString, type);
    }
}
