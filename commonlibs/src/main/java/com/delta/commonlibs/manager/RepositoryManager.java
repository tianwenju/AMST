package com.delta.commonlibs.manager;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;

@Singleton
public class RepositoryManager implements IRepositoryManager {
    private Retrofit mRetrofit;

    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<>();

    @Inject
    public RepositoryManager(Retrofit retrofit) {
        this.mRetrofit = retrofit;
    }


    /**
     * 根据传入的Class获取对应的Retrift service
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T obtainRetrofitService(Class<T> service) {

        if (mRetrofitServiceCache.containsKey(service.getName())) {
            return (T) mRetrofitServiceCache.get(service.getName());
        } else {
            T mT = mRetrofit.create(service);
            mRetrofitServiceCache.put(service.getName(), mT);
            return mT;
        }
    }
}
