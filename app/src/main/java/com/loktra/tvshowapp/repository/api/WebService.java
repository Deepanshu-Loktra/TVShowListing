package com.loktra.tvshowapp.repository.api;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.loktra.tvshowapp.base.ThreadSafeLiveData;
import com.loktra.tvshowapp.repository.responses.GenericResponse;
import com.loktra.tvshowapp.repository.responses.TVShowResponse;
import com.loktra.tvshowapp.utils.GsonHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WebService {
    private static final String TAG = WebService.class.getSimpleName();

//    private static final MediaType JSON = MediaType.parse("application/json");

    private okhttp3.OkHttpClient client;
    private static WebService webService;

    private WebService() {
        client = new OkHttpClient();
    }

    public static synchronized void init() {
        if (webService == null) {
            webService = new WebService();
        }
    }

    public static synchronized WebService getInstance() {
        if (webService == null) {
            webService = new WebService();
        }
        return webService;
    }

    @NonNull
    public OkHttpClient getClient() {
        return client;
    }


    public <U> LiveData<Resource<U>> genericGetApiCall(@NonNull String apiUrl,
                                                       final @NonNull Type type) {
        return genericGetApiCall(apiUrl, type, false);
    }

    public <U> LiveData<Resource<U>> genericGetApiCall(@NonNull String apiUrl,
                                                       final @NonNull Type type,
                                                       final boolean avoidCrash) {

        final ThreadSafeLiveData<Resource<U>> resource = new ThreadSafeLiveData<>();
        final Request request = new Request.Builder().url(apiUrl).get().build();

        // Show loading url
        final Resource<U> loading = Resource.loading(null);
        resource.setValue(loading);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "onFailure for request: " + call.request() + ", request could not be completed due to connectivity issue");
                Resource<U> res = Resource.error("Error occurred", null);
                resource.setValue(res);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
//                    String jsonData = response.body().string();
                    /*Log.v(TAG, jsonData);
                    try {
                        getCurrentDetails(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/

                    String responseJsonString = response.body().string();

                    Log.d(TAG, "Response arrived for url : " + call.request() + " response : " + response.code());

                    ArrayList<TVShowResponse> serverResponseObject;
                    Resource<ArrayList<TVShowResponse>> res;

                    if (avoidCrash) {
                        try {
                            serverResponseObject = GsonHelper.getInstance().fromJson(responseJsonString, type);
                            res = Resource.success(serverResponseObject);
                        } catch (JsonSyntaxException ex) {
                            res = Resource.error(404, "Invalid server response", null);
                        }
                    } else {
                        serverResponseObject = GsonHelper.getInstance()
                                .fromJson(responseJsonString, type);
//                        serverResponseObject = GsonHelper.getInstance()
//                                .fromJson(responseJsonString, type);
                        res = Resource.success(serverResponseObject);

                    }
                    resource.setValue((Resource<U>) res);
                } else {
                    Log.d(TAG, "Response arrived for url : " + call.request()
                            + " response code : " + response.code() + " ");
                    Resource<U> res = Resource.error("Error occurred", null);
                    resource.setValue(res);
                }
            }
        });

        return resource;
    }

    private void getCurrentDetails(String jsonData) throws JSONException {

//        Log.v(TAG, "From JSON : " + test);
    }
}
