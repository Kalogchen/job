package com.intermediary.job.utlis;

import android.app.Application;
import android.content.Context;

/**
 * 提供上下文对象
 * Created by kalogchen on 2016/12/28.
 */

public class BaseApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
