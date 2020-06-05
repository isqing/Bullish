package com.helianshe.bullish.status;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wangpeng on 2018/5/22.
 * Android5.1以上对StatusBar的操作
 * 对状态栏的支持{@link #setStatusBarColor(Activity, int)}
 * 对透明状态栏的支持{@link #setTranslucentStatusBar(Activity, boolean, boolean)}
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class LollipopStatusBar {

    /**
     * 设置状态栏颜色
     * 有api可以直接设置
     * 需要注意的是可能是从LightModel切换过来的，需移除顶部View
     *
     * @param activity
     * @param statusColor
     */
    static void setStatusBarColor(Activity activity, int statusColor) {

        Window window = activity.getWindow();
        //取消状态栏透明
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //设置状态栏颜色
        window.setStatusBarColor(statusColor);
        //设置系统状态栏处于可见状态
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //让view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //移除已经存在假状态栏则,并且取消它的Margin间距
            StatusBar.removeSelfStatus(activity, mChildView);

            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }

    /**
     * 设置透明状态栏
     * 有api可以直接设置
     *
     * @param activity
     */
    static void setTranslucentStatusBar(Activity activity, boolean hideStatusBarBackground, boolean isLightTheme) {
        Window window = activity.getWindow();
        //添加Flag把状态栏设为可绘制模式
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (hideStatusBarBackground) {
            //如果为全透明模式，取消设置Window半透明的Flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏为透明
            if (!isLightTheme) {
                window.setStatusBarColor(Color.TRANSPARENT);
            } else {
                if (PlatformStatusHelper.MIUISetStatusBarLightMode(activity, true) || PlatformStatusHelper.FlymeSetStatusBarLightMode(activity, true)) {
                    window.setStatusBarColor(Color.TRANSPARENT);
                }else{
                    //设置默认颜色，防止白底导致看不见状态栏
                    window.setStatusBarColor(StatusBar.DEFAULT_WHITE);
                }
            }
            //设置window的状态栏不可见
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            //如果为半透明模式，添加设置Window半透明的Flag
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置系统状态栏处于可见状态
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

        //view不根据系统窗口来调整自己的布局
        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        View mChildView = mContentView.getChildAt(0);
        if (mChildView != null) {
            //移除已经存在假状态栏则,并且取消它的Margin间距
            StatusBar.removeSelfStatus(activity, mChildView);

            mChildView.setFitsSystemWindows(false);
            ViewCompat.requestApplyInsets(mChildView);
        }
    }
}
