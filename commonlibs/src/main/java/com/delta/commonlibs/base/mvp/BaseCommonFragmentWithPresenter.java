package com.delta.commonlibs.base.mvp;

import android.app.Application;

import com.delta.commonlibs.di.component.BaseAppComponent;

import javax.inject.Inject;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/5 15:04
 */


public abstract class BaseCommonFragmentWithPresenter<p extends BaseCommonPresenter> extends BaseCommonFragment {

    public final String TAG = getClass().getSimpleName();
    @Inject
    protected p mPresenter;

    @Override
    protected void initCData() {
        Application mApplication = getActivity().getApplication();

        if (mApplication instanceof BaseCommonApplication) {
            BaseAppComponent mBaseAppComponent = ((BaseCommonApplication) mApplication).getAppComponent();
            componentInject(mBaseAppComponent);
        } else {
            throw new ClassCastException("Your application must extend BaseCommonApplication ");
        }


        initData();
    }

    protected abstract void initData();

    protected abstract void componentInject(BaseAppComponent mBaseAppComponent);

    @Override
    protected void initCView() {

        initView();
    }

    protected abstract void initView();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.ondestory();//释放资源
        this.mPresenter = null;
    }

    public p getPresenter() {
        return mPresenter;
    }

}
