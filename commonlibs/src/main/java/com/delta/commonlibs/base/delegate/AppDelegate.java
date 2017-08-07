package com.delta.commonlibs.base.delegate;

import android.app.Application;

import com.delta.commonlibs.BuildConfig;
import com.delta.commonlibs.base.mvp.BaseCommonApplication;
import com.delta.commonlibs.common.ConfigModule;
import com.delta.commonlibs.di.component.BaseAppComponent;
import com.delta.commonlibs.di.component.DaggerBaseAppComponent;
import com.delta.commonlibs.di.module.AppModule;
import com.delta.commonlibs.di.module.ClientModule;
import com.delta.commonlibs.di.module.GlobalConfigModule;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * @description :
 * 1.  管理 app生命周期
 * 优点：我们在组件化的时候 通过 configModule 通过重写injectAppLifecycle（）方法来
 * 来管理我们各个组件的Lifecycle（）;
 * 2. 管理 activity生命周期 同上面
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/9 14:58
 */


public class AppDelegate implements IApp {
    private BaseCommonApplication mApplication;
    protected final String TAG = this.getClass().getSimpleName();
    private BaseAppComponent mAppComponent;
    private final List<ConfigModule> mModules;
    @Inject
    ActivityLifeCycle mActivityLifeCycle;
    //application的生命周期回调
    private List<Lifecycle> mAppLifeCycles = new ArrayList<>();
    //activity的回调
    private List<Application.ActivityLifecycleCallbacks> mActivityLifeCycles = new ArrayList<>();

    public AppDelegate(BaseCommonApplication mApplication) {
        this.mApplication = mApplication;
        this.mModules = new ManifestParser(mApplication).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(mApplication, mAppLifeCycles);
            module.injectActivityLifecycle(mApplication, mActivityLifeCycles);
        }
    }

    public void setAppComponent(BaseAppComponent mAppComponent) {
        this.mAppComponent = mAppComponent;
    }



    public void onCreate() {

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //Stetho.initializeWithDefaults(this);
        }
        AutoLayoutConifg.getInstance().useDeviceSize();

        mAppComponent = DaggerBaseAppComponent
                .builder()
                .appModule(new AppModule(mApplication))//提供application
                .clientModule(new ClientModule())//用于提供okhttp和retrofit的单例
                .globalConfigModule(getGlobalConfigModule(mApplication, mModules))//全局配置
                .build();
        mApplication.registerActivityLifecycleCallbacks(mActivityLifeCycle);
        for (Application.ActivityLifecycleCallbacks mActivityLifecycle : mActivityLifeCycles) {
            mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        }

        // lifeCycle生命周期 oncreate回调

        for (Lifecycle mAppLifecycle : mAppLifeCycles) {
            mAppLifecycle.onCreate(mApplication);
        }

    }



    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Application context, List<ConfigModule> modules) {

        GlobalConfigModule.Builder builder = GlobalConfigModule
                .builder();

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }

    @Override
    public BaseAppComponent getAppComponent() {
        return mAppComponent;
    }


    public void onTerminate() {
        if (mActivityLifeCycles != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifeCycle);
        }
        if (mActivityLifeCycles != null && mActivityLifeCycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifeCycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        if (mAppLifeCycles != null && mAppLifeCycles.size() > 0) {
            for (Lifecycle lifecycle : mAppLifeCycles) {
                lifecycle.onTerminate(mApplication);
            }
        }
        this.mAppComponent = null;
        this.mActivityLifeCycle = null;
        this.mActivityLifeCycles = null;
        this.mAppLifeCycles = null;
        this.mApplication = null;
    }


    public interface Lifecycle {
        void onCreate(Application application);

        void onTerminate(Application application);
    }

}
