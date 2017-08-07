package com.delta.commonlibs.base.mvp;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.SupportActivity;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @description :不带Presenter的Activity
 * @autHor :  V.Wenju.Tian
 * @date : 2016/12/14 11:24
 */


public abstract class BaseCommonActivity extends SupportActivity {
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private String TAG = getClass().getSimpleName();
    private Unbinder bind;
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (UseEventBus()) {
            EventBus.getDefault().register(this);
        }
        initWindow();
        int resId = getLayoutId(savedInstanceState);

        if (resId != 0) {

            setContentView(resId);
        }
        bind = ButterKnife.bind(this);
        initCData(savedInstanceState);
        initCView(savedInstanceState);
    }

    protected abstract int getLayoutId(Bundle mSavedInstanceState);

    protected void initWindow() {
    }

    public boolean UseEventBus() {
        return false;
    }

    protected abstract void initCView(Bundle mSavedInstanceState);

    protected abstract void initCData(Bundle mSavedInstanceState);


    @Override
    protected void onDestroy() {
        if (bind != Unbinder.EMPTY) {
            bind.unbind();
        }
        if (UseEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public void FullScreencall() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
