package com.helianshe.bullish.base;

public interface IPresenter<V> {
    void register(V view);

    void unRegister();
}
