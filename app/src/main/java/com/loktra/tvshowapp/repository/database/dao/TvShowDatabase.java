package com.loktra.tvshowapp.repository.database.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.loktra.tvshowapp.base.BaseApplication;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

@Database(entities = {TVShowResponse.class}, version = 1)
public abstract class TvShowDatabase extends RoomDatabase {

    private static TvShowDatabase tvShowDatabaseInstance;

    public abstract TVShowDao tvShowDao();

    public static TvShowDatabase getTvShowDatabase() {
        if (tvShowDatabaseInstance == null) {
            synchronized (TvShowDatabase.class) {
                if (tvShowDatabaseInstance == null) {
                    tvShowDatabaseInstance = Room.databaseBuilder(BaseApplication.application.getApplicationContext(), TvShowDatabase.class, "TVShow_Database")
                            .build();


                }
            }
        }
        return tvShowDatabaseInstance;
    }
}
