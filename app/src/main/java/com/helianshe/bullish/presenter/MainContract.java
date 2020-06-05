package com.helianshe.bullish.presenter;

import com.helianshe.bullish.StartConfigBean;

import java.util.List;

public interface MainContract {

    interface View {
        void refreshNavigationBar(List<StartConfigBean.BottomMenuBean> bottomMenuBeans);
        void showTab(int pos);
    }

    interface Presenter {
        void initBottomData();

    }

    interface Model {

//        void demoModel(ModelListener modelListener);
//
//        interface ModelListener {
//
//            void completed(String test);
//
//        }

    }
}
