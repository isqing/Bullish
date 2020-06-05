package com.helianshe.bullish.status;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

public class StatusBar {
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "statusBarView";
    private static final String TAG_MARGIN_ADDED = "marginAdded";
    public static final int DEFAULT_WHITE = Color.rgb(191, 191, 192);

    public StatusBar() {
    }

    public static void setStatusBarColor(Activity activity, int colorResId) {
        int color = ContextCompat.getColor(activity, colorResId);
        if (Build.VERSION.SDK_INT >= 21) {
            LollipopStatusBar.setStatusBarColor(activity, color);
        } else if (Build.VERSION.SDK_INT >= 19) {
            KitKatStatusBar.setStatusBarColor(activity, color);
        }

    }

    public static void setTranslucentStatusBar(Activity activity) {
        setTranslucentStatusBar(activity, false, false);
    }

    public static void setTranslucentStatusBar(Activity activity, boolean hideStatusBarBackground, boolean isLightTheme) {
        if (Build.VERSION.SDK_INT >= 23) {
            MStatusBar.setTranslucentStatusBar(activity, hideStatusBarBackground, isLightTheme);
        } else if (Build.VERSION.SDK_INT >= 21) {
            LollipopStatusBar.setTranslucentStatusBar(activity, hideStatusBarBackground, isLightTheme);
        } else if (Build.VERSION.SDK_INT >= 19) {
            KitKatStatusBar.setTranslucentStatusBar(activity, hideStatusBarBackground, isLightTheme);
        }

    }

    public static void setStatusBarLightMode(Activity activity, int colorResId) {
        int color = ContextCompat.getColor(activity, colorResId);
        if (Build.VERSION.SDK_INT >= 23) {
            MStatusBar.setStatusBarLightMode(activity, color);
        } else if (Build.VERSION.SDK_INT >= 19) {
            KitKatStatusBar.setStatusBarLightMode(activity, color);
        }

    }

    public static void setFullscreen(Activity activity) {
        activity.requestWindowFeature(1);
        activity.getWindow().setFlags(1024, 1024);
    }

    static void setContentTopPadding(Activity activity, int padding) {
        ViewGroup mContentView = (ViewGroup)activity.getWindow().findViewById(16908290);
        mContentView.setPadding(0, padding, 0, 0);
    }

    static int getPxFromDp(Context context, float dp) {
        return (int) TypedValue.applyDimension(1, dp, context.getResources().getDisplayMetrics());
    }

    static boolean removeFakeStatusBarViewIfExist(Activity activity) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup)window.getDecorView();
        View fakeView = mDecorView.findViewWithTag("statusBarView");
        if (fakeView != null) {
            mDecorView.removeView(fakeView);
            return true;
        } else {
            return false;
        }
    }

    static View addFakeStatusBarView(Activity activity, int statusBarColor, int statusBarHeight) {
        Window window = activity.getWindow();
        ViewGroup mDecorView = (ViewGroup)window.getDecorView();
        View mStatusBarView = new View(activity);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, statusBarHeight);
        layoutParams.gravity = 48;
        mStatusBarView.setLayoutParams(layoutParams);
        mStatusBarView.setBackgroundColor(statusBarColor);
        mStatusBarView.setTag("statusBarView");
        mDecorView.addView(mStatusBarView);
        return mStatusBarView;
    }

    static void addMarginTopToContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild != null) {
            if (!"marginAdded".equals(mContentChild.getTag())) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mContentChild.getLayoutParams();
                lp.topMargin += statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag("marginAdded");
            }

        }
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            result = context.getResources().getDimensionPixelOffset(resId);
        }

        return result;
    }

    static void removeMarginTopOfContentChild(View mContentChild, int statusBarHeight) {
        if (mContentChild != null) {
            if ("marginAdded".equals(mContentChild.getTag())) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mContentChild.getLayoutParams();
                lp.topMargin -= statusBarHeight;
                mContentChild.setLayoutParams(lp);
                mContentChild.setTag((Object)null);
            }

        }
    }

    static void removeSelfStatus(Activity activity, View mContentChild) {
        boolean hasStatus = removeFakeStatusBarViewIfExist(activity);
        if (hasStatus && mContentChild != null) {
            int statusBarHeight = getStatusBarHeight(activity);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)mContentChild.getLayoutParams();
            int topMargin = lp.topMargin;
            topMargin -= statusBarHeight;
            lp.topMargin = Math.max(topMargin, 0);
            mContentChild.setLayoutParams(lp);
            mContentChild.setTag((Object)null);
        }

    }

    static boolean isFullScreen(Window window) {
        return (window.getAttributes().flags & 67108864) == 67108864;
    }
}
