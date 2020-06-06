package com.helianshe.bullish;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.helianshe.bullish.base.BaseActivity;
import com.helianshe.bullish.base.BasePresenter;
import com.helianshe.bullish.presenter.MainContract;
import com.helianshe.bullish.presenter.MainPresenter;
import com.helianshe.bullish.utils.WebviewUtils;
import com.myhayo.dsp.listener.RewardAdListener;
import com.myhayo.dsp.view.RewardVideoAd;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{
    private MainBottomHelper mainBottomHelper;
    private RadioGroup mNovelMainTabRadioGroup;
    public int position;
    private String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        iPresenter.initBottomData();
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }
    private void initView() {
        mNovelMainTabRadioGroup = findViewById(R.id.novel_main_tab_radio_group);
        mainBottomHelper = new MainBottomHelper(mNovelMainTabRadioGroup, this);
        mainBottomHelper.setPosition(position);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebviewUtils.requestRewardVideoView(MainActivity.this,"SDK21AC28AB7734");
            }
        });
    }

    @Override
    public void showTab(int pos) {
        if (mainBottomHelper != null) {
            mainBottomHelper.setPosition(pos);
            mainBottomHelper.selectTabFromPosition(pos);
        }
    }

    @Override
    public void refreshNavigationBar(List<StartConfigBean.BottomMenuBean> bottomMenuBeans) {
        if (bottomMenuBeans == null || bottomMenuBeans.isEmpty()) {
            return;
        }
        if (mainBottomHelper != null) {
            mainBottomHelper.refreshNavigationBar(bottomMenuBeans);
        }

    }



}
