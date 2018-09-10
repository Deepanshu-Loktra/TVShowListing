package com.loktra.tvshowapp.ui;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;

import com.loktra.tvshowapp.base.BaseActivity;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.utils.AppUtils;
import com.loktra.tvshowapp.viewmodel.TVShowViewModel;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TVShowViewModel tvShowViewModel;
    private ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBinding();
        loadLocalAndMakeApiCall();
    }

    private void initBinding() {
        tvShowViewModel = ViewModelProviders.of(this).get(TVShowViewModel.class);
        activityMainBinding = DataBindingUtil
        activityMainBinding = bind(Acti.class, R.layout.activity_home);
    }

    private void loadLocalAndMakeApiCall() {

        LiveData<ArrayList<TVShowResponse>> tvShowResponse = tvShowViewModel.fetchTVShowDataFromLocal();

        if (tvShowResponse != null) {
            initiateMain(tvShowResponse);
        } else {
            Log.d(TAG, "No previous response, user is logging in for first time.");
        }

        makeTVShowApiCall();
    }

    private void initiateMain(LiveData<ArrayList<TVShowResponse>> tvShowResponse) {
        Log.d("Response",tvShowResponse.toString());
        //TO DO when doing binding
    }

    private void makeTVShowApiCall() {
        tvShowViewModel.fetchTVShowDataFromApi().observe(this, new Observer<Resource<ArrayList<TVShowResponse>>>() {
            @Override
            public void onChanged(@Nullable Resource<ArrayList<TVShowResponse>> tvShowResponseResource) {
                if (tvShowResponseResource == null) {
                    Log.d(TAG, "This is not possible");
                    return;
                }

                switch (tvShowResponseResource.status) {

                    case SUCCESS:
//                        binding.content.swipeRefresh.setRefreshing(false);
//                        hideProgressDialog();

                        if (tvShowResponseResource.data == null) {
                            Log.d(TAG, "Home failed");
                            AppUtils.showToast(MainActivity.this, "Could not fetch");
                            return;
                        }

                        Log.d(TAG, "Success Response arrived for home api");
                        ArrayList<TVShowResponse> data = tvShowResponseResource.data;
                        tvShowViewModel.setResponse(data, true);

                        //LogHelper.logInfo(TAG, " Home " + data);

                        LiveData<ArrayList<TVShowResponse>> tvShowResponse = tvShowViewModel.fetchTVShowDataFromLocal();

                        if (tvShowResponse != null) {
                            initiateMain(tvShowResponse);
                        } else {
                            Log.d(TAG, "No previous response, user is logging in for first time.");
                        }

                        break;

                    case ERROR:
                        Log.d(TAG, "Error Response arrived for home api");
//                        binding.content.swipeRefresh.setRefreshing(false);
//                        hideProgressDialog();
                        //AuAppUtils.showToast(HomeActivity.this, "There was problem in home api");
                        break;

                    case LOADING:
                        // Display loading dialog
//                        if (!AuAppUtils.inHome) {
//                            showProgressDialog();
//                        }
                        //binding.loginProgress.setVisibility(View.VISIBLE);
                        break;
                }

            }
        });
    }

}
