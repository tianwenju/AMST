package com.delta.module_warning_service.di.app;

import android.app.Application;

import com.delta.commonlibs.base.mvp.BaseCommonApplication;
import com.delta.commonlibs.common.ApplicationAsLibrary;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/7 10:31
 */


public class WarningServiceApplication extends BaseCommonApplication implements ApplicationAsLibrary {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected String getBaseUrl() {
        return null;
    }

    @Override
    public void onCreateAsLibrary(Application application) {



    }
}
