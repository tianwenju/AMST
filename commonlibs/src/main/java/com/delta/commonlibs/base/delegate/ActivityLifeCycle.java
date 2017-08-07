package com.delta.commonlibs.base.delegate;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.delta.commonlibs.manager.AppManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @description :单一职责原理 这个负责 整个activity生命周期的控制管控
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/13 10:10
 */

@Singleton
public class ActivityLifeCycle implements Application.ActivityLifecycleCallbacks {


    private AppManager mAppManager;


    @Inject
    public ActivityLifeCycle(AppManager mAppManager) {
        this.mAppManager = mAppManager;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        boolean isNotAdd = false;
        if (activity.getIntent() != null)
            isNotAdd = activity.getIntent().getBooleanExtra(AppManager.IS_NOT_ADD_ACTIVITY_LIST, false);

        if (!isNotAdd)
            mAppManager.addActivity(activity);


    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
        mAppManager.setCurrentActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mAppManager.getCurrentActivity() == activity) {
            mAppManager.setCurrentActivity(null);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mAppManager.removeActivity(activity);
    }
}
