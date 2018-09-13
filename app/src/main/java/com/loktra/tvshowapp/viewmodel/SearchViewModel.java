package com.loktra.tvshowapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.loktra.tvshowapp.repository.SearchRepository;
import com.loktra.tvshowapp.repository.TVShowRepository;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.util.List;

public class SearchViewModel extends ViewModel {

    private static final String TAG = TVShowViewModel.class.getSimpleName();

    private SearchRepository searchRepository;

    public SearchViewModel() {
        // Called by Android itself.
        searchRepository = new SearchRepository();
    }

    // Fetching Data From Room
    public LiveData<Resource<List<TVShowResponse>>> fetchTVShowDataFromLocal(String query) {
        return searchRepository.getLocalTVShowResponse(query);
    }

}
