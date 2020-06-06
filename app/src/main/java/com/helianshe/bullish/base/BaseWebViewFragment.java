package com.helianshe.bullish.base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.helianshe.bullish.HomeFragment;
import com.helianshe.bullish.R;
import com.helianshe.bullish.utils.WebviewUtils;

import java.util.List;

public class BaseWebViewFragment extends BaseFragment {
    private BridgeWebView webView;
    private ProgressBar progressBar;
    private String url;
    public static BaseWebViewFragment newInstance(String url) {
        BaseWebViewFragment fragment = new BaseWebViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("url");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getContentLayoutId(), container, false);
        webView=view.findViewById(R.id.webview);
        progressBar=view.findViewById(R.id.pb_web_progress);
        WebviewUtils.initWebView(webView,progressBar);
        WebviewUtils.jsRegister(webView,getActivity());
        return view;
    }

    public void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        if (isFirst) {
            load();
        }else {
            WebviewUtils.callOnResume(webView);
        }
    }

    public WebView getWebView() {
        return webView;
    }
    public void load(){
        webView.loadUrl(url);

    }

    /**
     * Fragment 不可见时回调
     */
    public void onFragmentPause() {
        WebviewUtils.callOnPause(webView);
    }
    public int getContentLayoutId(){
        return R.layout.fragment_home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        WebviewUtils.onDestroy(webView);
    }
}



