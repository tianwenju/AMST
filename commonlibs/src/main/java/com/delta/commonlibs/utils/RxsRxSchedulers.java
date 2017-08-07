package com.delta.commonlibs.utils;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author :  V.Wenju.Tian
 * @description :统一流转换操作，当创建Observable流的时候，compose()会立即执行，犹如已经提前写好了一个操作符一样
 * @date : 2016/12/5 15:34
 */

public class RxsRxSchedulers {
    public static <T> ObservableTransformer<T, T> io_main() {


        ObservableTransformer<T, T> transformer = new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }


        };
        return transformer;
    }
}
