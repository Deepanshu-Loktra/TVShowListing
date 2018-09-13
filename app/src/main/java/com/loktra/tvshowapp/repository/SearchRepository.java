package com.loktra.tvshowapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.loktra.tvshowapp.base.ThreadSafeLiveData;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.api.WebService;
import com.loktra.tvshowapp.repository.database.TvShowDatabase;
import com.loktra.tvshowapp.repository.database.dao.TVShowDao;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchRepository {

    private static final String TAG = SearchRepository.class.getSimpleName();

    private TVShowDao mTVShowDao;

    private LiveData<Resource<List<TVShowResponse>>> mAllTVShow;

    public SearchRepository() {

        TvShowDatabase db = TvShowDatabase.getTvShowDatabase();
        mTVShowDao = db.tvShowDao();
    }

    private LiveData<Resource<List<TVShowResponse>>> addingResourceToLocalDatabase(String query){
        final ThreadSafeLiveData<Resource<List<TVShowResponse>>> resource = new ThreadSafeLiveData<>();
        mTVShowDao.loadTvShowResponse().observeForever(new Observer<List<TVShowResponse>>() {
            @Override
            public void onChanged(@Nullable List<TVShowResponse> tvShowResponseList) {

                if (tvShowResponseList != null) {
                    Resource<List<TVShowResponse>> successResource = Resource.success(tvShowResponseList);
                    resource.setValue(successResource);
                } else {
                    Resource<List<TVShowResponse>> errorResource = Resource.error("Error Fetching Data From Room Database", null);
                    resource.setValue(errorResource);
                }
                if(mTVShowDao.loadTvShowResponse().hasActiveObservers()) {
                    mTVShowDao.loadTvShowResponse().removeObserver(this);
                }
            }
        });

        return resource;
    }

    private List<TVShowResponse> searchedResult(String query, List<TVShowResponse> tvShowResponseList){
        List<TVShowResponse> tvShowSearchResponses = new ArrayList<>();

        for(TVShowResponse item : tvShowResponseList){

        }
        return tvShowSearchResponses;
    }



    // Getting Local Tv Show Data From Shared Preference
    public LiveData<Resource<List<TVShowResponse>>> getLocalTVShowResponse(String query) {

        Log.d(TAG, "Fetching Response From TVShow Local Database");
        return addingResourceToLocalDatabase(query);
    }
}
