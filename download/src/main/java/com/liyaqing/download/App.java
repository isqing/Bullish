package com.liyaqing.download;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static App app;
    private static Application application;

    public void onCreate() {
        super.onCreate();
    }

    public void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        app = this;
    }

    public static Application get() {
        return app;
    }
}
