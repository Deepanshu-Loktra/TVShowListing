package com.loktra.tvshowapp.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loktra.tvshowapp.base.ThreadSafeLiveData;
import com.loktra.tvshowapp.repository.api.ApiConstants;
import com.loktra.tvshowapp.repository.api.Resource;
import com.loktra.tvshowapp.repository.api.WebService;
import com.loktra.tvshowapp.repository.database.dao.TVShowDao;
import com.loktra.tvshowapp.repository.database.TvShowDatabase;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;

import java.lang.reflect.Type;
import java.util.List;

public class TVShowRepository {

    private static final String TAG = TVShowRepository.class.getSimpleName();

    private TVShowDao mTVShowDao;
    private WebService webService;

    private LiveData<Resource<List<TVShowResponse>>> mAllTVShow;

    private MediatorLiveData<Resource<List<TVShowResponse>>> mergedData = new MediatorLiveData<>();

    public TVShowRepository() {

        webService = WebService.getInstance();
        // This should happen after login/app start up depending on use case.
        TvShowDatabase db = TvShowDatabase.getTvShowDatabase();
        mTVShowDao = db.tvShowDao();
        mAllTVShow = addingResourceToLocalDatabase();
        Log.d("tv_show", "" + mAllTVShow.getValue());
    }

    private LiveData<Resource<List<TVShowResponse>>> addingResourceToLocalDatabase() {
        final ThreadSafeLiveData<Resource<List<TVShowResponse>>> resource = new ThreadSafeLiveData<>();
        mTVShowDao.loadTvShowResponse().observeForever(new Observer<List<TVShowResponse>>() {
            @Override
            public void onChanged(@Nullable List<TVShowResponse> tvShowResponseList) {

                if (tvShowResponseList != null) {
                    Resource<List<TVShowResponse>> successResource = Resource.success(tvShowResponseList);
                    resource.setValue(successResource);
                } else {
                    Resource<List<TVShowResponse>> errorResource = Resource.error("Error Fetching Data From Room Database", null);
                    resource.setValue(errorResource);
                }
                if (mTVShowDao.loadTvShowResponse().hasActiveObservers()) {
                    mTVShowDao.loadTvShowResponse().removeObserver(this);
                }
            }
        });
        return resource;
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
    private LiveData<Resource<List<TVShowResponse>>> getServerTVShowResponse() {

        Log.d(TAG, "Making TVShow APi Call");

        Uri builtUri = Uri.parse(ApiConstants.FETCH_TV_SHOW);

        Type type = new TypeToken<List<TVShowResponse>>() {
        }.getType();

        return webService.genericGetApiCall(builtUri.toString(), type);
    }

    // Getting Local Tv Show Data From Shared Preference
    public LiveData<Resource<List<TVShowResponse>>> getLocalTVShowResponse() {

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
     *
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

        Log.d(TAG, "Saving Tv Show response to db");
    }

    public LiveData<Resource<List<TVShowResponse>>> getTvShowsMerged() {

        // Add source 1
        mergedData.addSource(getLocalTVShowResponse(), new Observer<Resource<List<TVShowResponse>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<TVShowResponse>> tvShowResponseList) {

                if (tvShowResponseList != null) {
                    switch (tvShowResponseList.status) {
                        case SUCCESS:
                            Log.d(TAG, "got from db, setting to merger");
                            mergedData.setValue(tvShowResponseList);
                            break;
                        case ERROR:
                            Log.d(TAG, "database is empty");

                            break;
                        default:
                            break;
                    }
                }

            }
        });


        // Add source 2
        mergedData.addSource(getServerTVShowResponse(), new Observer<Resource<List<TVShowResponse>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<TVShowResponse>> arrayListResource) {


                if (arrayListResource != null) {
                    switch (arrayListResource.status) {
                        case SUCCESS:
                            // Api sends to activity, actually DB should
                            List<TVShowResponse> list = arrayListResource.data;
                            if (list != null) {
                                saveTVShowResponse(list);
                                Log.d(TAG, "got from api, setting to merger " + list);
                            } else {
                                Log.d(TAG, "list is empty");
                            }
                            mergedData.setValue(arrayListResource);
                            break;
                    }
                } else {
                    Log.d(TAG, "resource array list is null ");
                }
            }
        });

        return mergedData;
    }

}
