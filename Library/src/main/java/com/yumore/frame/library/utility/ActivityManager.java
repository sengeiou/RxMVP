package com.yumore.frame.library.utility;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.util.Stack;


public class ActivityManager {

    private static Stack<Activity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    public static synchronized ActivityManager getAppInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    public synchronized void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
        Log.i("TAG", "ActivityManager添加了：" + activity.getClass().getName());
    }

    public Activity currentActivity() {
        return activityStack.lastElement();
    }

    public void finishActivity() {
        finishActivity(activityStack.lastElement());
    }

    public void removeActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            Log.i("TAG", "ActivityManager移除了：" + activity.getClass().getName());
        }
    }

    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            Log.i("TAG", "ActivityManager关闭了：" + activity.getClass().getName());
        }
    }

    public void finishActivity(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
                removeActivity(activityStack.get(i));
                return;
            }
        }
    }

    public void finishAllActivity() {
        for (Activity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }
    
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
