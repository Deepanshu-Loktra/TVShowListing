package com.loktra.tvshowapp.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class BaseActivity extends AppCompatActivity {

//    private static final String TAG = BaseActivity.class.getSimpleName();

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Log.d(getLocalClassName(),"OnCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(getLocalClassName() ,"OnStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(getLocalClassName(),"OnResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(getLocalClassName(),"OnPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(getLocalClassName(),"OnStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(getLocalClassName(),"OnDestroy");
    }

    public void showProgressDialog(){

    }

    public void hideProgressDialog(){

    }
}
