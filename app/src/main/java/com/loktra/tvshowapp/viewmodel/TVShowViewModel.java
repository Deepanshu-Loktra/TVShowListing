package com.loktra.tvshowapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.loktra.tvshowapp.repository.Repository;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.ArrayList;

public class TVShowViewModel extends ViewModel{

    private static final String TAG = TVShowViewModel.class.getSimpleName();

    private ArrayList<TVShowResponse> responseList;


    // Fetching Data From Api
    public LiveData<Resource<ArrayList<TVShowResponse>>> fetchTVShowDataFromApi() {
        return Repository.getServerTVShowResponse();
    }

    // Fetching Data From Shared Preference
    public LiveData<ArrayList<TVShowResponse>> fetchTVShowDataFromLocal() {
        return Repository.getLocalTVShowResponse();
    }

    public void setResponseList(ArrayList<TVShowResponse> responseList) {
        setResponse(responseList, false);
    }

    // Saving Response From Api To Shared Preference and Setting Response To ResponseList
    public void setResponse(ArrayList<TVShowResponse> responseList, boolean saveResponse) {

        if (saveResponse) {
            Repository.saveTVShowResponse(responseList);
        }

        this.responseList = responseList;
    }

    public ArrayList<TVShowResponse> getResponseList() {
        return responseList;
    }
}
