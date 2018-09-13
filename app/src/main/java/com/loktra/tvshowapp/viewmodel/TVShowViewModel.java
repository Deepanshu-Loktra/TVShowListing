package com.loktra.tvshowapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.v7.widget.SearchView;

import com.loktra.tvshowapp.base.ThreadSafeLiveData;
import com.loktra.tvshowapp.repository.TVShowRepository;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.List;

public class TVShowViewModel extends ViewModel implements SearchView.OnQueryTextListener{

    private static final String TAG = TVShowViewModel.class.getSimpleName();

    private List<TVShowResponse> responseList;

    private TVShowRepository tvShowRepository;
    private ThreadSafeLiveData<String> searchQuery = new ThreadSafeLiveData<>();

    public TVShowViewModel() {
        // Called by Android itself.
        tvShowRepository = new TVShowRepository();
    }

//    // Fetching Data From Api
//    public LiveData<Resource<ArrayList<TVShowResponse>>> fetchTVShowDataFromApi() {
//        return tvShowRepository.getServerTVShowResponse();
//    }
//
//    // Fetching Data From Room
//    public LiveData<Resource<List<TVShowResponse>>> fetchTVShowDataFromLocal() {
//        return tvShowRepository.getLocalTVShowResponse();
//    }

    // Both DB and API
    public LiveData<Resource<List<TVShowResponse>>> fetch() {
        return tvShowRepository.getTvShowsMerged();
    }

//    public void setResponseList(List<TVShowResponse> responseList) {
//        setResponse(responseList, false);
//    }

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    public LiveData<String> getSearchQuery() {
        return searchQuery;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        searchQuery.setValue(newText);
        return true;
    }
}
