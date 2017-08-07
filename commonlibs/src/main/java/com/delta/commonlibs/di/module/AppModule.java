package com.delta.commonlibs.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.delta.commonlibs.manager.IRepositoryManager;
import com.delta.commonlibs.manager.RepositoryManager;
import com.delta.commonlibs.utils.DeviceUuidFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author :  V.Wenju.Tian
 * @description :提供Application以及Gson对象
 * @date : 2016/12/5 14:34
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }
    @Singleton
    @Provides
    public Context provideContext() {
        return mApplication;
    }
    @Singleton
    @Provides
    public Gson provideGson(Application application, @Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration != null)
            configuration.configGson(application, builder);
        return builder.create();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }

    public interface GsonConfiguration {
        void configGson(Context context, GsonBuilder builder);
    }

    @Singleton
    @Provides
    public DeviceUuidFactory provideDeviceUuid(Application context) {
        return new DeviceUuidFactory(context);
    }
}
