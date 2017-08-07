package com.delta.commonlibs.rx.rxerrorhandler;

import android.content.Context;

/**
 * @description :
 * @autor :  V.Wenju.Tian
 * @date : 2016/12/5 14:44
 */


public interface ResponseErrorListener {
    void handlerResponseError(Context context, Exception e);

    ResponseErrorListener EMPTY = new ResponseErrorListener() {
        @Override
        public void handlerResponseError(Context context, Exception e) {

        }
    };

}
