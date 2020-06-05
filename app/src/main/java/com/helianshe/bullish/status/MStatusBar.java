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
 * Android6.0以上对StatusBar的操作
 * 添加了对LightModel主题的支持{@link #setStatusBarLightMode(Activity, int)}
 */
@TargetApi(Build.VERSION_CODES.M)
public class MStatusBar {

    /**
     * 设置LightModel的状态栏
     * 6.0以上可通过API直接设置
     * 对应Theme中的 <item name="android:windowLightStatusBar">true</item>
     *
     * @param activity
     * @param color
     */
    public static void setStatusBarLightMode(Activity activity, int color) {
        try {
            Window window = activity.getWindow();
            //取消状态栏透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //如果是6.0以上将状态栏文字改为黑色，并设置状态栏颜色
//        activity.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(color);


            //fitsSystemWindow 为 false, 不预留系统栏位置.
            ViewGroup mContentView = activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
            View mChildView = mContentView.getChildAt(0);
            if (mChildView != null) {
                StatusBar.removeSelfStatus(activity, mChildView);
                mChildView.setFitsSystemWindows(false);
                ViewCompat.requestApplyInsets(mChildView);
            }
        } catch (Throwable e) {
            //
        }
    }

    static void setTranslucentStatusBar(Activity activity, boolean hideStatusBarBackground, boolean isLightTheme) {
        try {

            Window window = activity.getWindow();
            //添加Flag把状态栏设为可绘制模式
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (hideStatusBarBackground) {
                //如果为全透明模式，取消设置Window半透明的Flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置状态栏为透明
                window.setStatusBarColor(Color.TRANSPARENT);
                if (isLightTheme) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    //设置window的状态栏不可见
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                }
            } else {
                //如果为半透明模式，添加设置Window半透明的Flag
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //设置系统状态栏处于可见状态
                if (isLightTheme) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                }
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

        } catch (Throwable e) {
            //
        }
    }
}
