package com.loktra.tvshowapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.loktra.tvshowapp.base.BaseApplication;
import com.loktra.tvshowapp.repository.TVShowRepository;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;
import java.util.List;

public class TVShowViewModel extends ViewModel {

    private static final String TAG = TVShowViewModel.class.getSimpleName();

    private List<TVShowResponse> responseList;

    private TVShowRepository tvShowRepository;

    public TVShowViewModel() {
        // Called by Android itself.
        tvShowRepository = new TVShowRepository();
    }

    // Fetching Data From Api
    public LiveData<Resource<ArrayList<TVShowResponse>>> fetchTVShowDataFromApi() {
        return tvShowRepository.getServerTVShowResponse();
    }

    // Fetching Data From Room
    public LiveData<List<TVShowResponse>> fetchTVShowDataFromLocal() {
        return tvShowRepository.getLocalTVShowResponse();
    }

    // Both DB and API
    public LiveData<List<TVShowResponse>> fetch() {
        return tvShowRepository.getTvShowsMerged();
    }

    public void setResponseList(ArrayList<TVShowResponse> responseList) {
        setResponse(responseList, false);
    }

    // Saving Response From Api To Shared Preference and Setting Response To ResponseList
    public void setResponse(List<TVShowResponse> responseList, boolean saveResponse) {

        if (saveResponse) {
            tvShowRepository.saveTVShowResponse(responseList);
        }

        this.responseList = responseList;
    }

    public List<TVShowResponse> getResponseList() {
        return responseList;
    }
}
