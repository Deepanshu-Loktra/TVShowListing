package com.loktra.tvshowapp.ui;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.loktra.tvshowapp.R;
import com.loktra.tvshowapp.adapter.TVShowAdapter;
import com.loktra.tvshowapp.base.BaseActivity;
import com.loktra.tvshowapp.databinding.ActivityMainBinding;
import com.loktra.tvshowapp.databinding.ActivitySearchResultBinding;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.viewmodel.SearchViewModel;
import com.loktra.tvshowapp.viewmodel.TVShowViewModel;

import java.util.List;

public class SearchResultActivity extends BaseActivity {

    private SearchViewModel searchViewModel;
    private ActivitySearchResultBinding activitySearchResultBinding;
    private TVShowAdapter tvShowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initBinding();
        setAdapter();
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            fetchData(query);
        }
    }

    private void fetchData(String query){
        searchViewModel.fetchTVShowDataFromLocal(query).observe(this, new Observer<Resource<List<TVShowResponse>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<TVShowResponse>> listResource) {

            }
        });
    }


    //Instantiated Data Binding and View Model
    private void initBinding() {
        searchViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);
        activitySearchResultBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_result);
    }

    //Setting Adapter for TVShow Listing Recyclerview
    private void setAdapter() {
        activitySearchResultBinding.tvSearchRecyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        tvShowAdapter = new TVShowAdapter();
        activitySearchResultBinding.tvSearchRecyclerview.setAdapter(tvShowAdapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}