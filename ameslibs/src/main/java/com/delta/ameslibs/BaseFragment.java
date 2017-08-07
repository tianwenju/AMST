package com.delta.ameslibs;

import com.delta.ameslibs.di.component.AppComponent;
import com.delta.ameslibs.di.component.DaggerAppComponent;
import com.delta.ameslibs.di.module.MangerModule;
import com.delta.commonlibs.base.mvp.BaseCommonFragmentWithPresenter;
import com.delta.commonlibs.base.mvp.BaseCommonPresenter;
import com.delta.commonlibs.di.component.BaseAppComponent;

/**
 * @description :
 * @autHor :  Wenju.Tian
 * @date : 2017/8/2 17:48
 */


public abstract class  BaseFragment<p extends BaseCommonPresenter> extends BaseCommonFragmentWithPresenter<p> {

    @Override
    protected void componentInject(BaseAppComponent mBaseAppComponent) {
        AppComponent mAppComponent = DaggerAppComponent.builder().baseAppComponent(mBaseAppComponent).mangerModule(new MangerModule()).build();
        componentInject(mAppComponent);
            componentInject(mBaseAppComponent);//依赖注入

    }
    abstract void componentInject(AppComponent mAppComponent);


}
