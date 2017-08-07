package com.delta.commonlibs.base.mvp;

import android.app.Application;

import com.delta.commonlibs.base.delegate.AppDelegate;
import com.delta.commonlibs.base.delegate.IApp;
import com.delta.commonlibs.di.component.BaseAppComponent;

/**
 * 本项目由
 * mvp
 * +dagger2
 * +retrofit
 * +rxjava
 * +butterknife组成
 */
public abstract class BaseCommonApplication extends Application implements IApp {


    private AppDelegate mAppDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mAppDelegate == null) {

            mAppDelegate = new AppDelegate(this);
        }
        this.mAppDelegate.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppDelegate != null) {

            mAppDelegate.onTerminate();
        }
    }

    @Override
    public BaseAppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
