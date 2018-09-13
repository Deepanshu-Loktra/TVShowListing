package com.loktra.tvshowapp.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.loktra.tvshowapp.R;
import com.loktra.tvshowapp.adapter.TVShowAdapter;
import com.loktra.tvshowapp.base.BaseActivity;
import com.loktra.tvshowapp.databinding.ActivityMainBinding;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.utils.AppUtils;
import com.loktra.tvshowapp.viewmodel.TVShowViewModel;

import java.util.List;

public class MainActivityWithMerger extends BaseActivity {
    private static final String TAG = MainActivityWithMerger.class.getSimpleName();

    private TVShowViewModel tvShowViewModel;
    private ActivityMainBinding activityMainBinding;
    private TVShowAdapter tvShowAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        setAdapter();
        fetchData();
        fetchSearchData();
    }

    //Instantiated Data Binding and View Model
    private void initBinding() {
        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    //Setting Adapter for TVShow Listing Recyclerview
    private void setAdapter() {
        activityMainBinding.tvRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
//        activityMainBinding.tvRecyclerview.setHasFixedSize(true); // why?? infinite scroll // tv api fixed that is why it is working

        tvShowAdapter = new TVShowAdapter();
        activityMainBinding.tvRecyclerview.setAdapter(tvShowAdapter);
    }


    //Fetching Data
    private void fetchData() {

        tvShowViewModel.fetch().observe(this, new Observer<Resource<List<TVShowResponse>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<TVShowResponse>> tvShowResourceList) {
                if (tvShowResourceList != null) {
                    switch (tvShowResourceList.status) {
                        case LOADING:
                            showProgressbar();
                            break;
                        case ERROR:
                            Log.d(TAG, "Error Response arrived for merger Live data");
                            hideProgressbar();
                            AppUtils.showToast(MainActivityWithMerger.this, "There was problem in Merger Live Data");
                            break;
                        case SUCCESS:
                            hideProgressbar();
                            List<TVShowResponse> tvShowResponse = tvShowResourceList.data;
                            if (tvShowResponse != null) {
                                Log.d(TAG, "Data Fetched Successfully");
                                initiateMain(tvShowResponse);
                            } else {
                                Log.d(TAG, "Error Fetching Data");
                            }
                            break;
                    }
                } else {
                    Log.d(TAG, "Error Fetching Data");
                }
            }
        });
    }

    private void fetchSearchData() {
        tvShowViewModel.getSearchQuery().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvShowAdapter.setsearchFilter(s);
            }
        });
    }

    //Loading Arraylist To Recyclerview Adapter
    private void initiateMain(List<TVShowResponse> tvShowResponseList) {
        Log.d("Init main with list : ", tvShowResponseList.toString());
        tvShowAdapter.setTVShowList(tvShowResponseList);
    }

    @Override
    public ProgressBar getProgressBar() {
        return activityMainBinding.mainProgressbar;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_search, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_tvshow).getActionView();
        searchView.setQueryHint("Search TV Shows");
        searchView.setOnQueryTextListener(tvShowViewModel);
        return true;
    }
}
