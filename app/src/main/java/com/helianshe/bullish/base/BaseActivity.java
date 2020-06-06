package com.helianshe.bullish.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.helianshe.bullish.R;
import com.helianshe.bullish.status.StatusBar;

public abstract class BaseActivity<P extends BasePresenter>  extends AppCompatActivity implements IView{

    protected P iPresenter;
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        //为了能拿到子View，适配状态栏
        initStatusBar();
    }

    /**
     * 初始化状态栏设置
     */
    protected void initStatusBar() {
        StatusBar.setStatusBarLightMode(this, R.color.green);
    }
    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        bindView();

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();

        UnbindView();

    }

    @Override

    public void bindView() {

        iPresenter = createPresenter();
        if (iPresenter!=null) {
            iPresenter.register(this);
        }

    }
    @Override

    public void UnbindView() {
        if (iPresenter!=null) {
            iPresenter.unRegister();
        }

    }

    protected abstract P createPresenter();

}