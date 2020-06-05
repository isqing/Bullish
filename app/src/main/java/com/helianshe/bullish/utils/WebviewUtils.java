package com.helianshe.bullish.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebviewUtils {
    public static void initWebView(WebView webView, final ProgressBar progressBar){
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

    public static  void onDestroy(WebView mWebView) {
        if( mWebView!=null) {
            mWebView.setVisibility(View.GONE);
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }
}
