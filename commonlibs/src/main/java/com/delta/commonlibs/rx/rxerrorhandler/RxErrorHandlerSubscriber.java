package com.delta.commonlibs.rx.rxerrorhandler;

import android.widget.Toast;

import com.delta.commonlibs.utils.NetworkUtil;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @description :通过继承该观察者，实现错误交给RxErrorHandler进行处理。
 * @autor :  V.Wenju.Tian
 * @date : 2016/12/5 15:08
 */
public abstract class RxErrorHandlerSubscriber<T> implements Subscriber<T> {
    private RxErrorHandler rxErrorHandler;

    public RxErrorHandlerSubscriber(RxErrorHandler rxErrorHandler) {
        this.rxErrorHandler = rxErrorHandler;
    }

    @Override
    public void onSubscribe(Subscription s) {
        if (!NetworkUtil.isNetworkConnected(rxErrorHandler.getContext())) {

            Toast.makeText(rxErrorHandler.getContext(), "当前网络不可用，请检查网络情况", Toast.LENGTH_SHORT).show();
            // **一定要主动调用下面这一句**

        }
    }

    @Override
    public void onNext(T mT) {

    }


    @Override
    public void onError(Throwable e) {
        rxErrorHandler.handleError(e);
    }
}
