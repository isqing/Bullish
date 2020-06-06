package com.helianshe.bullish.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.myhayo.dsp.listener.RewardAdListener;
import com.myhayo.dsp.view.RewardVideoAd;
import com.tbruyelle.rxpermissions2.BuildConfig;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class WebviewUtils {
    private static RewardVideoAd rewardVideoView = null;
    private static String TAG = "WebviewUtils";
    // 权限列表
    private static String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static WebviewUtils webviewUtils = new WebviewUtils();


    public static WebviewUtils getInstance() {
        return webviewUtils;
    }

    public void initWebView(WebView webView, final ProgressBar progressBar) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 支持通过JS打开新窗口
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);// 支持自动加载图片
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            //请求失败时显示失败的界面
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
//                    webLLayout.setVisibility(View.GONE);
//                    errorTv.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onDestroy(WebView mWebView) {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    public void requestRewardVideoView(final WebView webView, final FragmentActivity activity, final String adCode) {

        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .requestEachCombined(permissions)
                .subscribe(new Observer<Permission>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Permission permission) {
                        Log.i(TAG, "onNext: " + permission.granted);
                        if (permission.granted) {
                            rewardVideoView(activity, webView, adCode);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .subscribe(new Consumer<Boolean>() {
//                    @Override
//                    public void accept(Boolean aBoolean) throws Exception {
//                        Log.i(TAG, "accept: "+aBoolean);
//                        if (aBoolean) {
//                            rewardVideoView(activity,adCode);
//                        }
//                    }
//                });


    }

    public void rewardVideoView(Activity context, final WebView webView, final String adCode) {
        boolean isComplete=false;
        rewardVideoView = new RewardVideoAd(context, adCode, new RewardAdListener() {
            @Override
            public void onAdVideoComplete() {
                Log.d(TAG, "onAdVideoComplete: ");
                isComplete=true;
            }

            @Override
            public void onVideoCached() {
                Log.d(TAG, "onVideoCached: ");
                rewardVideoView.loadRewardVideo();

            }

            @Override
            public void onAdReady() {
                Log.d(TAG, "onAdReady: ");
            }

            @Override
            public void onAdFailed(String s) {
                Log.d(TAG, "onAdFailed: " + s);
                callRewardVideo(webView, 0);
            }

            @Override
            public void onAdClick() {
                Log.d(TAG, "onAdClick: ");
            }

            @Override
            public void onAdEnd() {
                Log.d(TAG, "onAdClose: ");

                callRewardVideo(webView, 0);
                if (isComplete){
                    callRewardVideo(webView, 1);
                }else {
                    callRewardVideo(webView, 0);
                }

            }

            @Override
            public void onAdShow() {
                Log.d(TAG, "onAdShow: ");
            }
        });


    }


    public void callOnResume(WebView webView) {
        if (webView != null) {
            webView.loadUrl("javascript:viewOnResume()");
        }
    }

    public void callOnPause(WebView webView) {
        if (webView != null) {
            webView.loadUrl("javascript:viewOnPause()");
        }
    }

    public void callRewardVideo(WebView webView, int isSucc) {
        if (webView != null) {
            webView.loadUrl("javascript:callRewardVideoBask(" + isSucc + ")");

        }
    }
}
