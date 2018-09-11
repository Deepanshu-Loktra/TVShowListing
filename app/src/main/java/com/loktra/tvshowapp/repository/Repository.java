package com.loktra.tvshowapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loktra.tvshowapp.repository.api.ApiConstants;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.api.WebService;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.repository.shared_preference.SharedPrefConstants;
import com.loktra.tvshowapp.repository.shared_preference.SharedPrefUtil;
import com.loktra.tvshowapp.utils.GsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Repository {

    private static final String TAG = Repository.class.getSimpleName();

    // Getting Local Tv Show Data From Shared Preference
    public static LiveData<ArrayList<TVShowResponse>> getLocalTVShowResponse(){
        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();
        String json = SharedPrefUtil.getStringFromPreferences(SharedPrefConstants.TVSHOW_RESPONSE);
        ArrayList<TVShowResponse> tvShowResponse;
        MutableLiveData<ArrayList<TVShowResponse>> tvShowResponseLD = new MutableLiveData<>();
        if(json != null){
            tvShowResponse = GsonHelper.getInstance().fromJson(json,type);
            tvShowResponseLD.setValue(tvShowResponse);
        }

        Log.d(TAG,"Fetching Response From TVShow Local Database");

        return tvShowResponseLD;
    }

    // Fetching Tv Show Data From Api
    public static LiveData<Resource<ArrayList<TVShowResponse>>> getServerTVShowResponse(){
        Log.d(TAG,"Making TVShow APi Call");

        Uri builtUri = Uri.parse(ApiConstants.FETCH_TV_SHOW);

        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();

        return WebService.getInstance().genericGetApiCall(builtUri.toString(), type);
    }

    // Saving Data Fetched From Api To Shared Preference
    @WorkerThread
    public static void saveTVShowResponse(@NonNull ArrayList<TVShowResponse> tvShowResponseList) {

        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();
        SharedPrefUtil.saveStringToPreferences(SharedPrefConstants.TVSHOW_RESPONSE, GsonHelper.getInstance().toJson(tvShowResponseList, type));
        Log.d(TAG, "Saving Tv Show response to db");
    }
}
