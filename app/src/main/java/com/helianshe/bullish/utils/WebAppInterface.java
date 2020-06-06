package com.helianshe.bullish.utils;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.helianshe.bullish.base.BaseWebViewActivity;

public class WebAppInterface {
    FragmentActivity activity;
    WebView webView;

    public WebAppInterface() {
    }

    public WebAppInterface(FragmentActivity c, WebView webView) {
        activity = c;
        this.webView=webView;
    }

    @JavascriptInterface
    public void gotoWebview(final String data) {
        Log.d("qing==", "gotoWebview: " + data);
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(activity, BaseWebViewActivity.class);
                intent.putExtra("url", data);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
    }

    @JavascriptInterface
    public void closeWebview() {
        Log.d("qing==", "closeWebview: "+activity);
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.i("qing==", "closeWebview1: ");
                activity.finish();
            }
        });
    }


    @JavascriptInterface
    public void requestRewardVideoViewJs(final String data) {
        Log.d("qing==", "requestRewardVideoViewJs: " + data);
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WebviewUtils.getInstance().requestRewardVideoView(webView,activity, data);
            }
        });
    }

}
