package com.helianshe.bullish.utils;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.helianshe.bullish.base.BaseWebViewActivity;
import com.myhayo.dsp.listener.RewardAdListener;
import com.myhayo.dsp.view.RewardVideoAd;
import com.tbruyelle.rxpermissions2.BuildConfig;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WebviewUtils {
    private static RewardVideoAd rewardVideoView = null;
    private static String TAG = "WebviewUtils";
    // 权限列表
    private static String[] permissions = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void initWebView(BridgeWebView webView, final ProgressBar progressBar) {
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

    public static void onDestroy(BridgeWebView mWebView) {
        if (mWebView != null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }

    public static void requestRewardVideoView(final FragmentActivity activity, final String adCode) {

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
                        Log.i(TAG, "onNext: "+permission.granted);
                        if (permission.granted) {
                            rewardVideoView(activity, adCode);
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

    public static void rewardVideoView(Context context, final String adCode) {
        if (rewardVideoView != null) {
            rewardVideoView.loadRewardVideo(adCode);
            return;
        }
        rewardVideoView = new RewardVideoAd(context, new RewardAdListener() {

            private String tag;

            @Override
            public void onAdVideoComplete() {
                Log.d(TAG, "onAdVideoComplete: ");
            }

            @Override
            public void onAdReady() {
                Log.d(TAG, "onAdReady: ");
            }

            @Override
            public void onAdFailed(String s) {
                Log.d(TAG, "onAdFailed: "+s);
            }

            @Override
            public void onAdClick() {
                Log.d(TAG, "onAdClick: ");
            }

            @Override
            public void onAdClose() {
                Log.d(TAG, "onAdClose: ");
            }

            @Override
            public void onAdShow() {
                Log.d(TAG, "onAdShow: ");
            }
        });
        rewardVideoView.loadRewardVideo("B0A5C2AF49D2");

    }

    public static void jsRegister(final BridgeWebView webView, final FragmentActivity activity) {
        gotoWebview(webView,activity);
        requestRewardVideoViewJs(webView, activity);
    }

    //加载url
    private static void gotoWebview(BridgeWebView webView, final FragmentActivity activity) {
        webView.registerHandler("gotoWebview", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject != null) {
                        String url = jsonObject.getString("url");
                        Intent intent = new Intent(activity, BaseWebViewActivity.class);
                        intent.putExtra("url", url);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        activity.startActivity(intent);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void requestRewardVideoViewJs(final BridgeWebView webView, final FragmentActivity activity) {
        webView.registerHandler("requestRewardVideoViewJs", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject != null) {
                        String adCode = jsonObject.getString("adCode");
                        requestRewardVideoView(activity, adCode);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void callOnResume(BridgeWebView webView) {
        webView.callHandler("viewOnResume", null, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });

    }

    public static void callOnPause(BridgeWebView webView) {
        webView.callHandler("viewOnPause", null, new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });
    }
}
