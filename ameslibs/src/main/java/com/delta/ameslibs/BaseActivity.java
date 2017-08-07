package com.delta.ameslibs;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.delta.ameslibs.di.component.AppComponent;
import com.delta.ameslibs.di.component.DaggerAppComponent;
import com.delta.ameslibs.di.module.MangerModule;
import com.delta.commonlibs.base.mvp.BaseCommonActivityWithPresenter;
import com.delta.commonlibs.base.mvp.BaseCommonPresenter;
import com.delta.commonlibs.di.component.BaseAppComponent;
import com.delta.demacia.barcode.BarCodeIpml;
import com.delta.demacia.barcode.Barcode;
import com.delta.demacia.barcode.BarcodeFactory;
import com.delta.demacia.barcode.exception.DevicePairedNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * @description :baseActivity
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/5 15:15
 */


public abstract class BaseActivity<p extends BaseCommonPresenter> extends BaseCommonActivityWithPresenter<p> implements BarCodeIpml.OnScanSuccessListener {


    private Barcode mBarCodeIpm;

    private List<OnBarCodeSuccess> events = new ArrayList<>();

    public void addOnBarCodeSuccess(OnBarCodeSuccess onBarCodeSuccess) {
        if (onBarCodeSuccess != null) {
            events.add(onBarCodeSuccess);
        }
    }

    public void removeOnBarCodeSuccess(OnBarCodeSuccess onBarCodeSuccess) {
        if (onBarCodeSuccess != null) {
            events.remove(onBarCodeSuccess);
        }
    }

    @Override
    protected void initCData(Bundle mSavedInstanceState) {
        mBarCodeIpm = BarcodeFactory.getBarcode(this);
        mBarCodeIpm.setOnGunKeyPressListener(this);

        }


    @Override
    protected void componentInject(BaseAppComponent mBaseAppComponent) {
        AppComponent mAppComponent = DaggerAppComponent.builder().baseAppComponent(mBaseAppComponent).mangerModule(new MangerModule()).build();
        componentInject(mAppComponent);

    }

    protected abstract void componentInject(AppComponent appComponent);

    protected abstract void initData();

    protected abstract void initView();

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if (mBarCodeIpm.isEventFromBarCode(event)) {
            mBarCodeIpm.analysisKeyEvent(event);
            return true;
        }

        return super.dispatchKeyEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mBarCodeIpm.hasConnectBarcode();
        } catch (DevicePairedNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {

        if (mBarCodeIpm != null) {
            mBarCodeIpm.onComplete();
        }
        super.onDestroy();
    }


    @Override
    public void onScanSuccess(String barcode) {

        Log.e(TAG, "onScanSuccess: " + barcode);
        if (events.size() != 0) {
            Log.e(TAG, "onScanSuccess: " + events.size());
            for (OnBarCodeSuccess event : events) {
                event.onScanSuccess(barcode);
            }
        }
    }

    public interface OnBarCodeSuccess {
        void onScanSuccess(String barcode);
    }
}
