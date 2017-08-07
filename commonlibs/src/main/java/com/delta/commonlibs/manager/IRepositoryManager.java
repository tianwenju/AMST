package com.delta.commonlibs.manager;

public interface IRepositoryManager {


    /**
     * 根据传入的Class获取对应的Retrift service，如果没有的话就创建一个新的对象
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T obtainRetrofitService(Class<T> service);


}
