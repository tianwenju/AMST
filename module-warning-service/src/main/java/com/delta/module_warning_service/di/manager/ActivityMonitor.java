package com.delta.module_warning_service.di.manager;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @description :
 * @autHor :  V.Wenju.Tian
 * @date : 2016/12/27 10:20
 */


public class ActivityMonitor {
    private static final String TAG = "ActivityMonitor";
    private static final ActivityState[] ACTIVE_ORDER = {ActivityState.DESTROYED, ActivityState.STOPPED, ActivityState.CREATED, ActivityState.STARTED, ActivityState.PAUSED, ActivityState.RESUMED};
    private static final ActivityState[] STRICT_ACTIVE_ORDER = {ActivityState.DESTROYED, ActivityState.STOPPED, ActivityState.PAUSED, ActivityState.CREATED, ActivityState.STARTED, ActivityState.RESUMED};
    private static boolean sDebug = true;
    private static boolean sStrictForeground = true;
    private List<ActivityEntry> mActivityEntries = new ArrayList<>();
    private boolean mAppForeground;
    private List<OnAppStateChangeListener> mOnAppStateChangeListeners = new CopyOnWriteArrayList<>();
    private List<OnActivityTopListener> onActivityTopListeners = new CopyOnWriteArrayList<>();
    private Activity monitorActivity;

    public void setMonitorActivity(Activity monitorActivity) {
        this.monitorActivity = monitorActivity;
    }

    public Activity getMonitorActivity() {
        return monitorActivity;
    }

    private ActivityMonitor() {
    }

    public static ActivityMonitor getInstance() {
        return SingletonHolder.sInstance;
    }

    public static void setDebug(boolean debug) {
        sDebug = debug;
    }

    /**
     * 开启严格的前后台判断，只要有Activity处于created或started状态，就认为App在前台
     *
     * @param strictForeground
     */
    public static void setStrictForeground(boolean strictForeground) {
        sStrictForeground = strictForeground;
    }

    private static int compare(ActivityState left, ActivityState right) {
        ActivityState[] order = sStrictForeground ? STRICT_ACTIVE_ORDER : ACTIVE_ORDER;
        return getOrder(order, left) - getOrder(order, right);
    }

    private static int getOrder(ActivityState[] order, ActivityState activityState) {
        for (int i = 0, j = order.length; i < j; i++) {
            if (order[i] == activityState) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 将Activity的生命周期回调传递进来，可使用BaseActivity的方式，也可使用lifeCircleListener
     *
     * @param activity      当前生命周期变化的Activity
     * @param activityState Activity当前的生命周期状态
     */
    public void onActivityEvent(Activity activity, ActivityState activityState) {
        updateActivityState(activity, activityState);
    }

    /**
     * 获取当前栈顶的Activity
     */
    public Activity getTopActivity() {
        if (mActivityEntries.isEmpty()) {
            return null;
        }
        return mActivityEntries.get(mActivityEntries.size() - 1).mActivityRef.get();
    }

    /**
     * 获取倒数第二个activity
     * @return
     */
    public Activity getPenultActivity(){
        if (mActivityEntries.isEmpty()) {
            return null;
        }
        return mActivityEntries.get(mActivityEntries.size() - 2).mActivityRef.get();
    }


    /**
     * 判断App是否在前台
     */
    public boolean isAppForeground() {
        if (mActivityEntries.isEmpty()) {
            return false;
        }
        ActivityEntry activityEntry = mActivityEntries.get(mActivityEntries.size() - 1);
        if (!sStrictForeground) {
            return activityEntry.mState == ActivityState.RESUMED ||
                    activityEntry.mState == ActivityState.PAUSED;
        } else {
            return activityEntry.mState == ActivityState.RESUMED ||
                    activityEntry.mState == ActivityState.PAUSED ||
                    activityEntry.mState == ActivityState.STARTED ||
                    activityEntry.mState == ActivityState.CREATED;
        }
    }

    private void updateActivityState(Activity activity, ActivityState activityState) {

        ActivityEntry activityEntry = new ActivityEntry(activity, activityState);
        if (mActivityEntries.contains(activityEntry)) {
            mActivityEntries.remove(activityEntry);
        }
        mActivityEntries.add(activityEntry);

        clearAndSort();


        boolean foreground = isAppForeground();
        if (mAppForeground != foreground) {
            mAppForeground = foreground;
            notifyAppStateChanged();
        }


        for (OnActivityTopListener onActivityTopListener : onActivityTopListeners) {
            if (monitorActivity.getClass().getName().equals(getTopActivity().getClass().getName())) {

                onActivityTopListener.onActivityTop(true);
            } else {
                onActivityTopListener.onActivityTop(false);
            }
        }


        if (sDebug) {
            Activity topActivity = getTopActivity();
            Log.d(TAG, activity.getClass().getSimpleName() + " " + activityEntry.mState + ", top activity is " + (topActivity == null ? "null" : topActivity.getClass().getSimpleName()) + ", foreground = " + isAppForeground() + ", activities = " + mActivityEntries.size());
        }
    }

    /**
     * 根据Activity的状态排序，并移除已销毁Activity的状态，末尾的Activity即是当前用户交互的Activity
     */
    private void clearAndSort() {
        Iterator<ActivityEntry> activityItemIterator = mActivityEntries.iterator();
        while (activityItemIterator.hasNext()) {
            ActivityEntry activityEntry = activityItemIterator.next();
            if (activityEntry.mState == ActivityState.DESTROYED) {
                activityItemIterator.remove();
            }
        }

        Collections.sort(mActivityEntries, new Comparator<ActivityEntry>() {
            @Override
            public int compare(ActivityEntry lhs, ActivityEntry rhs) {
                return ActivityMonitor.compare(lhs.mState, rhs.mState);
            }
        });
    }

    public void registerAppStateChangeListener(OnAppStateChangeListener onAppStateChangeListener) {
        if (!mOnAppStateChangeListeners.contains(onAppStateChangeListener)) {
            mOnAppStateChangeListeners.add(onAppStateChangeListener);
        }
    }

    public void registerActivityTopChangerListenr(OnActivityTopListener onActivityTopListener) {
        if (!(onActivityTopListeners.contains(onActivityTopListener))) {
            onActivityTopListeners.add(onActivityTopListener);
        }
    }

    public void removeAppStateChangeListener(OnAppStateChangeListener onAppStateChangeListener) {
        if (mOnAppStateChangeListeners.contains(onAppStateChangeListener)) {
            mOnAppStateChangeListeners.remove(onAppStateChangeListener);
        }
    }
    public void removeActivityTopChangerListenr(OnActivityTopListener onActivityTopListener) {
        if (onActivityTopListeners.contains(onActivityTopListener)) {
            onActivityTopListeners.remove(onActivityTopListener);
        }
    }
    private void notifyAppStateChanged() {
        for (OnAppStateChangeListener onAppStateChangeListener : mOnAppStateChangeListeners) {
            onAppStateChangeListener.onAppStateChange(mAppForeground);
        }
    }

    /**
     * App前后台状态变化的回调
     */
    public interface OnAppStateChangeListener {
        void onAppStateChange(boolean foreground);
    }

    /**
     * 判断制定的Activity是不是位于栈顶
     */
    public interface OnActivityTopListener {
        void onActivityTop(boolean top);
    }

    private static class SingletonHolder {
        static ActivityMonitor sInstance = new ActivityMonitor();
    }

    private class ActivityEntry {
        WeakReference<Activity> mActivityRef;
        ActivityState mState;

        ActivityEntry(Activity activity, ActivityState activityState) {
            mActivityRef = new WeakReference<>(activity);
            mState = activityState;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof ActivityEntry) {
                ActivityEntry activityEntry = (ActivityEntry) o;
                return mActivityRef == activityEntry.mActivityRef || !(mActivityRef == null || activityEntry.mActivityRef == null) && mActivityRef.get() == activityEntry.mActivityRef.get();
            }
            return false;
        }
    }
}
