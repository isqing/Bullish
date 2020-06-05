package com.helianshe.bullish;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.helianshe.bullish.base.BaseActivity;
import com.helianshe.bullish.base.BasePresenter;
import com.helianshe.bullish.presenter.MainContract;
import com.helianshe.bullish.presenter.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View{
    private MainBottomHelper mainBottomHelper;
    private RadioGroup mNovelMainTabRadioGroup;
    public int position;
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

    @TargetApi(Build.VERSION_CODES.M)
    private void checkAndRequestPermission() {
        List<String> lackedPermission = new ArrayList<String>();
        if (!(checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (!(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (!(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            lackedPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        // 如果需要的权限都已经有了，那么直接调用SDK
        if (lackedPermission.size() != 0) {
            // 否则，建议请求所缺少的权限，在onRequestPermissionsResult中再看是否获得权限
            String[] requestPermissions = new String[lackedPermission.size()];
            lackedPermission.toArray(requestPermissions);
            requestPermissions(requestPermissions, 1024);
        } else {
//            findViewById(R.id.get_reward_video_btn).setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    rewardVideoView.loadRewardVideo("B0A5C2AF49D2");
//                }
//            });
        }
    }

    private boolean hasAllPermissionsGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1024 && hasAllPermissionsGranted(grantResults)) {
            Toast.makeText(this, "所需权限已经打开。", Toast.LENGTH_LONG).show();
//            findViewById(R.id.get_reward_video_btn).setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    rewardVideoView.loadRewardVideo("B0A5C2AF49D2");
//                }
//            });
        } else {
            Toast.makeText(this, "应用缺少必要的权限！请点击\"权限\"，打开所需要的权限。", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivity(intent);
            finish();
        }
    }


}
