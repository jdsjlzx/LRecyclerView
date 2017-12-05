package com.lzx.demo;

import android.app.Application;


public class AppContext extends Application {
    private static AppContext instance ;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static AppContext getInstance() {
        return instance;
    }
}
