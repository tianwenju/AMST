package com.delta.commonlibs.common;

import android.app.Application;
import android.content.Context;

import com.delta.commonlibs.base.delegate.AppDelegate;
import com.delta.commonlibs.base.mvp.BaseCommonApplication;
import com.delta.commonlibs.di.module.GlobalConfigModule;
import com.delta.commonlibs.manager.IRepositoryManager;

import java.util.List;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/9 15:23
 */
public interface ConfigModule {

    /**
     * 使用{@link GlobalConfigModule.Builder}给框架配置一些配置参数
     *
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobalConfigModule.Builder builder);

    /**
     * 使用{@link IRepositoryManager}给框架注入一些网络请求和数据缓存等服务
     *
     * @param context
     * @param repositoryManager
     */
    void registerComponents(Context context, IRepositoryManager repositoryManager);

    void injectAppLifecycle(BaseCommonApplication mApplication, List<AppDelegate.Lifecycle> mAppLifecycles);

    void injectActivityLifecycle(BaseCommonApplication mApplication, List<Application.ActivityLifecycleCallbacks> mActivityLifecycles);
}
