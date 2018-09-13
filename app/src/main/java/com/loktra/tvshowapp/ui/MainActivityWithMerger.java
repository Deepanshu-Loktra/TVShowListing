package com.loktra.tvshowapp.ui;

import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
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
import android.widget.ProgressBar;

import com.loktra.tvshowapp.R;
import com.loktra.tvshowapp.adapter.TVShowAdapter;
import com.loktra.tvshowapp.base.BaseActivity;
import com.loktra.tvshowapp.databinding.ActivityMainBinding;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.utils.AppUtils;
import com.loktra.tvshowapp.viewmodel.TVShowViewModel;

import java.util.ArrayList;
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
        loadLocalAndMakeApiCall();
    }

    //Instantiated Data Binding and View Model
    private void initBinding() {
        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    private void setAdapter() {
        activityMainBinding.tvRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        activityMainBinding.tvRecyclerview.setHasFixedSize(true); // why?? infinite scroll // tv api fixed that is why it is working

        tvShowAdapter = new TVShowAdapter();
        activityMainBinding.tvRecyclerview.setAdapter(tvShowAdapter);
    }
    //Setting Adapter for TVShow Listing Recyclerview


    //Accessing Local Data And Making Api Call For Updated Data
    private void loadLocalAndMakeApiCall() {
        showProgressbar();

        tvShowViewModel.fetch().observe(this, new Observer<List<TVShowResponse>>() {
            @Override
            public void onChanged(@Nullable List<TVShowResponse> tvShowResponseList) {

                if (tvShowResponseList != null) {
                    initiateMain(tvShowResponseList);
                } else {
                    Log.e(TAG, "Null reponse");
                }
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

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_tvshow).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }


}
