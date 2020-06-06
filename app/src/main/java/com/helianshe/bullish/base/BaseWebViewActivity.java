package com.helianshe.bullish.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.helianshe.bullish.R;
import com.helianshe.bullish.utils.WebviewUtils;

public  abstract class BaseWebViewActivity extends BaseActivity {
    private BridgeWebView webView;
    private ProgressBar progressBar;
    private String url;
    private boolean isFirst=true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentLayoutId());
        webView=findViewById(R.id.webview);
        progressBar=findViewById(R.id.pb_web_progress);
        url=getIntent().getStringExtra("url");
        WebviewUtils.initWebView(webView,progressBar);
        WebviewUtils.jsRegister(webView,this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst=false;
            load();
        }else {
            WebviewUtils.callOnResume(webView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        WebviewUtils.callOnPause(webView);
    }

    public WebView getWebView() {
        return webView;
    }
    public void load(){
        webView.loadUrl(url);
    }

    public int getContentLayoutId(){
        return R.layout.fragment_home;
    }
}



