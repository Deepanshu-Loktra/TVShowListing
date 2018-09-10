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

    public LiveData<Resource<ArrayList<TVShowResponse>>> fetchTVShowDataFromApi() {
        return Repository.getServerTVShowResponse();
    }
//    private void test(){
//        fetchTVShowDataFromApi().
//    }

    public LiveData<ArrayList<TVShowResponse>> fetchTVShowDataFromLocal() {
        return Repository.getLocalTVShowResponse();
    }

    public void setResponseList(ArrayList<TVShowResponse> responseList) {
        setResponse(responseList, false);
    }

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
