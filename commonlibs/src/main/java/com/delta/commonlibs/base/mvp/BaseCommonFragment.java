package com.delta.commonlibs.base.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2017/6/5 14:59
 */


public abstract class BaseCommonFragment extends Fragment {

    private View rootView;
    private Unbinder bind;
    public String TAG = getClass().getSimpleName();
    Bundle savedState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        rootView = inflater.inflate(getContentViewId(), container, false);
        bind = ButterKnife.bind(this, rootView);
        initCData();
        initCView();
        return rootView;
    }

    protected abstract void initCView();

    protected abstract void initCData();


    //默认不使用
    protected boolean UseEventBus() {
        return false;
    }


    protected abstract int getContentViewId();

    @Override
    public void onDestroy() {
        if (bind != Unbinder.EMPTY) {
            bind.unbind();

        }
        if (UseEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Restore State Here
        if (UseEventBus()) {
            EventBus.getDefault().register(this);
        }
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
    }

    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            b.putBundle("data", savedState);
        }
    }


    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {

            savedState = b.getBundle("data");
        }
        if (savedState != null) {
            restoreState();
            return true;
        }
        return false;
    }

    /////////////////////////////////
    // Restore Instance State Here
    /////////////////////////////////

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString(text));
            onRestoreState(savedState);
        }
    }

    protected void onRestoreState(Bundle savedInstanceState) {

    }

    private Bundle saveState() {
        Bundle state = new Bundle();
        // For Example
        //state.putString(text, tv1.getText().toString());
        onSaveState(state);
        return state;
    }

    protected void onSaveState(Bundle outState) {

    }
}
