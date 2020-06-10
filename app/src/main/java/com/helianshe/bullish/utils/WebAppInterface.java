package com.helianshe.bullish.utils;

import android.Manifest;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.helianshe.bullish.base.BaseWebViewActivity;
import com.tbruyelle.rxpermissions2.BuildConfig;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    @JavascriptInterface
    public void requestStoragePermission() {
        Log.d("qing==", "requestStorageProgre: " );
        if (activity == null) {
            return;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RxPermissions rxPermissions = new RxPermissions(activity);
                rxPermissions.setLogging(BuildConfig.DEBUG);
                rxPermissions
                        .requestEachCombined(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Observer<Permission>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Permission permission) {
                                int grant=0;
                                if (permission.granted) {
                                    grant=1;
                                }else {
                                    grant=0;
                                }
                                WebviewUtils.getInstance().callBackStoragePermission(webView,grant);

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }
        });
    }

}
