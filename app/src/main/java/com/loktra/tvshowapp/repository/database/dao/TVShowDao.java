package com.loktra.tvshowapp.repository.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TVShowDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<TVShowResponse> tvShowResponseList);

    @Query("DELETE FROM tvresponse_table")
    void deleteAll();

    @Query("SELECT * FROM tvresponse_table")
    LiveData<List<TVShowResponse>> loadTvShowResponse();
}
