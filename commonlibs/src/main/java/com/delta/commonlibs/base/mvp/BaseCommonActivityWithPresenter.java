package com.delta.commonlibs.base.mvp;

import android.app.Application;
import android.os.Bundle;

import com.delta.commonlibs.di.component.BaseAppComponent;

import javax.inject.Inject;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/1 10:15
 */


public abstract class BaseCommonActivityWithPresenter<p extends BaseCommonPresenter> extends BaseCommonActivity {
    public final String TAG = getClass().getSimpleName();
    @Inject
    protected p mPresenter;

    @Override
    protected void initCData(Bundle mSavedInstanceState) {
        Application mApplication = BaseCommonActivityWithPresenter.this.getApplication();

        if (mApplication instanceof BaseCommonApplication) {
            BaseAppComponent mBaseAppComponent = ((BaseCommonApplication) mApplication).getAppComponent();
            componentInject(mBaseAppComponent);
        }else {
            throw new ClassCastException("Your application must extend BaseCommonApplication ");
        }
        initData();
    }

    protected abstract void initData();

    protected abstract void componentInject(BaseAppComponent mBaseAppComponent);

    @Override
    protected void initCView(Bundle mSavedInstanceState) {

        initView();
    }

    protected abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.ondestory();//释放资源
        this.mPresenter = null;
    }

    public p getPresenter() {
        return mPresenter;
    }
}
