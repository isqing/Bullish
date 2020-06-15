package com.helianshe.bullish;

import android.app.Application;
import android.content.Context;

import com.liyaqing.download.App;
import com.myhayo.madsdk.util.DeviceUtil;

public class AppApplication extends App {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        DeviceUtil.setOaid("237819312302");

    }
    public static Context getInstance() {
        return context;
    }
}
