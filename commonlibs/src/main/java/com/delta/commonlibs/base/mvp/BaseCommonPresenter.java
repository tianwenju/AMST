package com.delta.commonlibs.base.mvp;

import java.lang.ref.WeakReference;


public class BaseCommonPresenter<M extends IModel, V extends IView> implements Ipresenter {
    protected final String TAG = this.getClass().getSimpleName();
    protected M mModel;
    protected V mView;
    WeakReference<IView> mIViewWeakReference = new WeakReference<IView>(mView);

    WeakReference<IModel> iModelWeakReference = new WeakReference<IModel>(mModel);

    public BaseCommonPresenter(M model, V mView) {
        this.mModel = model;
        this.mView = mView;
        onStart();
    }

    public M getModel() {
        return mModel;
    }

    public V getView() {
        return mView;
    }

    public BaseCommonPresenter(V rootView) {
        this.mView = rootView;
        onStart();
    }


    public void onStart() {


    }

    public void ondestory() {
        mIViewWeakReference.clear();
        iModelWeakReference.clear();
    }

}
