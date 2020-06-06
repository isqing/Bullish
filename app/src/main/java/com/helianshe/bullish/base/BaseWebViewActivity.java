package com.helianshe.bullish.base;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.helianshe.bullish.R;
import com.helianshe.bullish.utils.WebAppInterface;
import com.helianshe.bullish.utils.WebviewUtils;

public  class BaseWebViewActivity extends BaseActivity {
    private WebView webView;
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
//        webView.setDefaultHandler(new DefaultHandler());

//        webView.addJavascriptInterface(new WebViewJavascriptBridge(webView.getCallbacks(), webView), "android");
        WebviewUtils.getInstance().initWebView(webView,progressBar);
        WebAppInterface object = new WebAppInterface(this,webView);
        webView.addJavascriptInterface(object, "Android");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isFirst) {
            isFirst=false;
            load();
        }else {
            WebviewUtils.getInstance().callOnResume(webView);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        WebviewUtils.getInstance().callOnPause(webView);
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

    @Override
    protected void onDestroy() {
        Log.i("qing==", "onDestroy: ");
        super.onDestroy();
        WebviewUtils.getInstance().onDestroy(webView);
    }
}



