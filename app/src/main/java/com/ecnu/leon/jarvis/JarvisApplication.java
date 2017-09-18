package com.ecnu.leon.jarvis;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.cootek.feedsnews.sdk.FeedsManager;
import com.ecnu.leon.jarvis.news.MockNewsUtil;

public class JarvisApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context sAppCtx;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppCtx = this;
        FeedsManager.getIns().init(new MockNewsUtil());
    }

    public static Context getContext() {
        return sAppCtx;
    }
}