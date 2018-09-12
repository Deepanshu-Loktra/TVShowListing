package com.loktra.tvshowapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loktra.tvshowapp.base.ThreadSafeLiveData;
import com.loktra.tvshowapp.repository.api.ApiConstants;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.api.WebService;
import com.loktra.tvshowapp.repository.database.dao.InsertAsynkTask;
import com.loktra.tvshowapp.repository.database.dao.TVShowDao;
import com.loktra.tvshowapp.repository.database.dao.TvShowDatabase;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.repository.shared_preference.SharedPrefConstants;
import com.loktra.tvshowapp.repository.shared_preference.SharedPrefUtil;
import com.loktra.tvshowapp.utils.GsonHelper;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TVShowRepository {

    private static final String TAG = TVShowRepository.class.getSimpleName();

    private TVShowDao mTVShowDao;
    private WebService webService;

    private LiveData<List<TVShowResponse>> mAllTVShow;

    private MediatorLiveData<List<TVShowResponse>> mergedData = new MediatorLiveData<>();

    public TVShowRepository() {

        webService = WebService.getInstance();
        // This should happen after login/app start up depending on use case.
        TvShowDatabase db = TvShowDatabase.getTvShowDatabase();

        mTVShowDao = db.tvShowDao();
//        Log.d("tv_show",mTVShowDao.toString());
        mAllTVShow = mTVShowDao.loadTvShowResponse();
        Log.d("tv_show", "" + mAllTVShow.getValue());
    }

//    // Getting Local Tv Show Data From Shared Preference
//    public LiveData<ArrayList<TVShowResponse>> getLocalTVShowResponse(){
//        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();
//        String json = SharedPrefUtil.getStringFromPreferences(SharedPrefConstants.TVSHOW_RESPONSE);
//        ArrayList<TVShowResponse> tvShowResponse;
//        MutableLiveData<ArrayList<TVShowResponse>> tvShowResponseLD = new MutableLiveData<>();
//        if(json != null){
//            tvShowResponse = GsonHelper.getInstance().fromJson(json,type);
//            tvShowResponseLD.setValue(tvShowResponse);
//        }
//
//        Log.d(TAG,"Fetching Response From TVShow Local Database");
//
//        return tvShowResponseLD;
//    }

    // Fetching Tv Show Data From Api
    public LiveData<Resource<ArrayList<TVShowResponse>>> getServerTVShowResponse() {
        Log.d(TAG, "Making TVShow APi Call");

        Uri builtUri = Uri.parse(ApiConstants.FETCH_TV_SHOW);

        Type type = new TypeToken<ArrayList<TVShowResponse>>() {
        }.getType();

        return WebService.getInstance().genericGetApiCall(builtUri.toString(), type);
    }

    // Getting Local Tv Show Data From Shared Preference
    public LiveData<List<TVShowResponse>> getLocalTVShowResponse() {

        Log.d(TAG, "Fetching Response From TVShow Local Database");
        Log.d("datassssstt", "" + mAllTVShow.getValue() + " livedata : " + mAllTVShow);
        return mAllTVShow;
    }

//    // Saving Data Fetched From Api To Shared Preference
//    @WorkerThread
//    public static void saveTVShowResponse(@NonNull ArrayList<TVShowResponse> tvShowResponseList) {
//
//        Type type = new TypeToken<ArrayList<TVShowResponse>>(){}.getType();
//        SharedPrefUtil.saveStringToPreferences(SharedPrefConstants.TVSHOW_RESPONSE, GsonHelper.getInstance().toJson(tvShowResponseList, type));
//        Log.d(TAG, "Saving Tv Show response to db");
//    }

    /**
     * Saving Data Fetched From Api To Room Database
     * @param tvShowResponseList
     */
    public void saveTVShowResponse(@NonNull final List<TVShowResponse> tvShowResponseList) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Inserting tv list in db");
                mTVShowDao.insertAll(tvShowResponseList);
            }
        };
        new Thread(runnable).start();

        Log.d(TAG, "" + tvShowResponseList);

//        new InsertAsynkTask(mTVShowDao).execute(tvShowResponseList);
        Log.d(TAG, "Saving Tv Show response to db");
    }

    public LiveData<List<TVShowResponse>> getTvShowsMerged() {

        // Add source 1
        mergedData.addSource(getLocalTVShowResponse(), new Observer<List<TVShowResponse>>() {
            @Override
            public void onChanged(@Nullable List<TVShowResponse> tvShowResponseList) {
                Log.d(TAG, "got from db, setting to merger");
                mergedData.setValue(tvShowResponseList);
                Log.d("data",tvShowResponseList.toString());
            }
        });


        // Add source 2
        mergedData.addSource(getServerTVShowResponse(), new Observer<Resource<ArrayList<TVShowResponse>>>() {
            @Override
            public void onChanged(@Nullable Resource<ArrayList<TVShowResponse>> arrayListResource) {

                Log.d(TAG, arrayListResource.message);

                switch (arrayListResource.status) {
                    case SUCCESS:
                        // Api sends to activity, actually DB should
                        List<TVShowResponse> list = arrayListResource.data;
                        saveTVShowResponse(list);
                        Log.d(TAG, "got from api, setting to merger " + list);
                        mergedData.setValue(list);

                        break;
                }


            }
        });

        return mergedData;
    }

}
