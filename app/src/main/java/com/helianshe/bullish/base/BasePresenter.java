package com.helianshe.bullish.base;

import java.lang.ref.WeakReference;

public class BasePresenter<V> implements IPresenter<V> {
    public static final String TAG = "BasePresenter";

    protected WeakReference<V> iView;

    @Override
    public void register(V view) {
//        Log.i(TAG, "BasePresenter register: ");
        iView = new WeakReference<V>(view);
    }

    @Override
    public void unRegister() {
//        Log.i(TAG, "BasePresenter unRegister: ");
        iView.clear();
    }
}