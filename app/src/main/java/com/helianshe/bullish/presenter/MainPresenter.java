package com.helianshe.bullish.presenter;

import com.helianshe.bullish.Constants;
import com.helianshe.bullish.MainActivity;
import com.helianshe.bullish.StartConfigBean;
import com.helianshe.bullish.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter<MainActivity> implements MainContract.Presenter {


    @Override
    public void initBottomData() {
        if (iView.get() == null) {
            return;
        }
        List<StartConfigBean.BottomMenuBean> bottomMenuBeanList = new ArrayList<>();

        StartConfigBean.BottomMenuBean bottomMenuBean1 = new StartConfigBean.BottomMenuBean();
        bottomMenuBean1.setId(Constants.TAB_HOME);
        bottomMenuBean1.setName("主页");
        bottomMenuBean1.setTarget(Constants.HOME_URL);

        StartConfigBean.BottomMenuBean bottomMenuBean2 = new StartConfigBean.BottomMenuBean();
        bottomMenuBean2.setId(Constants.TAB_WITHDRAW);
        bottomMenuBean2.setName("游戏");
        bottomMenuBean2.setTarget(Constants.WITHDRAW_URL);

        StartConfigBean.BottomMenuBean bottomMenuBean3 = new StartConfigBean.BottomMenuBean();
        bottomMenuBean3.setId(Constants.TAB_WELFARE);
        bottomMenuBean3.setName("财富");
        bottomMenuBean3.setTarget(Constants.WELFARE_URL);

        StartConfigBean.BottomMenuBean bottomMenuBean4 = new StartConfigBean.BottomMenuBean();
        bottomMenuBean4.setId(Constants.TAB_MINE);
        bottomMenuBean4.setName("我的");
        bottomMenuBean4.setTarget(Constants.MINE_URL);

        bottomMenuBeanList.add(bottomMenuBean1);
        bottomMenuBeanList.add(bottomMenuBean2);
        bottomMenuBeanList.add(bottomMenuBean3);
        bottomMenuBeanList.add(bottomMenuBean4);
        iView.get().refreshNavigationBar(bottomMenuBeanList);
    }
}
