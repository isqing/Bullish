package com.helianshe.bullish.status;


import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.helianshe.bullish.R;


/**
 * Created by wangpeng on 2018/5/22.
 * Android5.1以上对StatusBar的操作
 * 对状态栏的支持{@link #setStatusBarColor(Activity, int)}
 * 对透明状态栏的支持{@link #setTranslucentStatusBar(Activity,boolean,boolean)}
 * 对LightMode状态栏的支持{@link #setStatusBarLightMode(Activity, int)} }
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
class KitKatStatusBar {

    /**
     * 设置状态栏颜色
     * 由于不支持直接设置颜色，所以实现方式是通过全屏，在顶部添加一个和statusBar高度一致的View做填充
     *
     * @param activity
     * @param statusColor
     */
    static void setStatusBarColor(Activity activity, int statusColor) {
        Window window = activity.getWindow();

        ViewGroup mContentView = window.findViewById(Window.ID_ANDROID_CONTENT);
        //获取父布局
        View mContentChild = mContentView.getChildAt(0);
        //获取状态栏高度
        int statusBarHeight = StatusBar.getStatusBarHeight(activity);

        //如果已经存在假状态栏则移除，防止重复添加
//        StatusBar.removeFakeStatusBarViewIfExist(activity);
        StatusBar.removeSelfStatus(activity, mContentChild);
        //添加一个View来作为状态栏的填充
        StatusBar.addFakeStatusBarView(activity, statusColor, statusBarHeight);

        //设置Window为全透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (StatusBar.isFullScreen(window)) {
            //之前是全屏才进行,一进页面就设置会有问题，待解决
            //设置子控件到状态栏的间距
//            StatusBar.addMarginTopToContentChild(mContentChild, statusBarHeight);
//        }
        if (mContentChild != null) {
            //子View需适配状态栏高度
            mContentChild.setFitsSystemWindows(true);
        }
        //如果在Activity中使用了ActionBar则需要再将布局与状态栏的高度跳高一个ActionBar的高度，否则内容会被ActionBar遮挡
        int action_bar_id = activity.getResources().getIdentifier("action_bar", "id", activity.getPackageName());
        View view = activity.findViewById(action_bar_id);
        if (view != null) {
            TypedValue typedValue = new TypedValue();
            if (activity.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)) {
                int actionBarHeight = TypedValue.complexToDimensionPixelSize(typedValue.data, activity.getResources().getDisplayMetrics());
                StatusBar.setContentTopPadding(activity, actionBarHeight);
            }
        }
    }

    /**
     * 设置状态栏颜色
     * 实现方式是通过全屏
     *
     * @param activity
     */
    static void setTranslucentStatusBar(Activity activity, boolean hideStatusBarBackground, boolean isLightTheme) {
        Window window = activity.getWindow();
        //设置Window为透明
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        ViewGroup mContentView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        View mContentChild = mContentView.getChildAt(0);

        if (mContentChild != null) {

            //获取状态栏高度
            int statusBarHeight = StatusBar.getStatusBarHeight(activity);
            //如果已经存在假状态栏则移除，防止重复添加
            StatusBar.removeSelfStatus(activity, mContentChild);
            //添加一个View来作为状态栏的填充，防止透明导致状态栏看不见
            StatusBar.addFakeStatusBarView(activity, StatusBar.DEFAULT_WHITE, statusBarHeight);

            //fitsSystemWindow 为 false, 不预留系统栏位置.
            mContentChild.setFitsSystemWindows(false);
        }
    }

    /**
     * 设置LightModel的状态栏颜色
     * 由于不支持直接设置，所以实现模式是通过在RGB的机制上加了些透明度
     * 小米和魅族手机可以用api设置
     *
     * @param activity
     * @param color
     */
    static void setStatusBarLightMode(Activity activity, int color) {
        //判断是否为小米或魅族手机，如果是则将状态栏文字改为黑色
        if (PlatformStatusHelper.MIUISetStatusBarLightMode(activity, true) || PlatformStatusHelper.FlymeSetStatusBarLightMode(activity, true)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LollipopStatusBar.setStatusBarColor(activity, color);
            } else {
                setStatusBarColor(activity, color);
            }
        } else {
            //将颜色坐下转换处理
            int newColor;
            if (color == Color.WHITE) {
                //白色默认实现为灰色
                newColor = StatusBar.DEFAULT_WHITE;
            } else {
                //非白色加一层透明
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);
                newColor = Color.argb(155, red, green, blue);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                LollipopStatusBar.setStatusBarColor(activity, newColor);
            } else {
                setStatusBarColor(activity, newColor);
            }
        }
    }

}
