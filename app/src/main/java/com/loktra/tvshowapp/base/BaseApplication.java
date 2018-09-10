package com.loktra.tvshowapp.base;

import android.app.Application;

import com.loktra.tvshowapp.repository.api.WebService;
import com.loktra.tvshowapp.repository.shared_preference.SharedPrefUtil;
import com.loktra.tvshowapp.utils.GsonHelper;

public class BaseApplication extends Application {

    public static final String TAG = BaseApplication.class.getSimpleName();

    public static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        initGlobalAppComponents();
    }

    private void initGlobalAppComponents() {
        WebService.init();
        GsonHelper.getInstance();
        SharedPrefUtil.init(this);
        BaseApplication.application = this;
    }
}
