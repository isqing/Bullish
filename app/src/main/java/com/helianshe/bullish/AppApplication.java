package com.helianshe.bullish;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    public static Context getInstance() {
        return context;
    }
}
