package com.helianshe.bullish;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.helianshe.bullish.base.BaseActivity;
import com.helianshe.bullish.base.BaseWebViewFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainBottomHelper {
    private RadioGroup mNovelMainTabRadioGroup;
    private Context context;
    private List<Fragment> fragmentList;
    private FragmentManager mFragmentManager;
    private List<StartConfigBean.BottomMenuBean> bottomMenuBeans;
    private String[] tabText = {"首页", "提现","福利", "我的"};
    private int position;
    private double mCurrentFocusIndex;

    public MainBottomHelper(RadioGroup mNovelMainTabRadioGroup, BaseActivity context) {
        this.mNovelMainTabRadioGroup = mNovelMainTabRadioGroup;
        this.context = context;
        fragmentList=new ArrayList<>();
        this.mFragmentManager = context.getSupportFragmentManager();
        setOnClick();

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private void setOnClick(){
        if (mNovelMainTabRadioGroup==null){
            return;
        }
        mNovelMainTabRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.indexOfChild(group.findViewById(checkedId));
                Fragment fragment = setCurrentFragment(index);
                //todo
//                if (fragment instanceof WithDrawFragment){
//                    ((WithDrawFragment)fragment).refreshWebView();
//                }

            }
        });
    }
    /**
     * 设置当前fragment
     *
     * @param pos 当前位置
     */
    private Fragment setCurrentFragment(int pos) {
        position = pos;
        if (fragmentList == null || fragmentList.isEmpty()) {
            return null;
        }
        if (position > fragmentList.size()) {
            position = 0;
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        hideAllFragment(fragmentTransaction, fragmentList, pos);
        Fragment fragment = fragmentList.get(pos);
        mFragmentManager.executePendingTransactions();
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.container, fragment);
        }
        fragmentTransaction.show(fragment);

        fragmentTransaction.commitAllowingStateLoss();
        return fragment;
    }
    public Fragment getCurrentFragment(){
        if (position > fragmentList.size()) {
            position = 0;
        }
        return fragmentList.get(position);
    }
    public AppCompatRadioButton bottomBarItem(String tabName, int tabTextColor, int bottomIconId) {
        AppCompatRadioButton radioButton = new AppCompatRadioButton(context);
        radioButton.setText(tabName);
        Drawable tabDrawable = ContextCompat.getDrawable(context, bottomIconId);
        tabDrawable.setBounds(0, 0, tabDrawable.getIntrinsicWidth(), tabDrawable.getIntrinsicHeight());
        radioButton.setCompoundDrawables(null, tabDrawable, null, null);
        radioButton.setTextColor(ContextCompat.getColorStateList(context, tabTextColor));
        radioButton.setTextSize(10);
        radioButton.setGravity(Gravity.CENTER_HORIZONTAL);
        radioButton.setButtonDrawable(new ColorDrawable());
        radioButton.setBackground(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            radioButton.setForeground(null);
        }
//            int padding = ScreenUtils.dip2px(this, 6);
//            radioButton.setPadding(0, padding, 0, padding);
        RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(0, RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.weight = 1;
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        mNovelMainTabRadioGroup.addView(radioButton, layoutParams);
        return radioButton;
    }
    public void selectTabFromPosition(int mTabPosition) {
        if (mTabPosition < 0 || mTabPosition >= mNovelMainTabRadioGroup.getChildCount()) {
            mTabPosition = 0;
        }
        RadioButton radioButton = (RadioButton) mNovelMainTabRadioGroup.getChildAt(mTabPosition);
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
    }
    public void refreshNavigationBar(List<StartConfigBean.BottomMenuBean> bottomMenuBeans) {
        if (bottomMenuBeans == null || bottomMenuBeans.isEmpty()) {
            return;
        }
        this.bottomMenuBeans = bottomMenuBeans;
        mNovelMainTabRadioGroup.removeAllViews();
        removeAllFragment();
        for (int i = 0; i < bottomMenuBeans.size(); i++) {
            StartConfigBean.BottomMenuBean bottomMenuBean = bottomMenuBeans.get(i);
            if (Constants.TAB_HOME.equals(bottomMenuBean.getId())) {
                AppCompatRadioButton homeBottomBar=bottomBarItem(bottomMenuBean.getName(), R.drawable.app_bottom_tab_text_selector, R.drawable.app_bottom_bar_home_selector);
                BaseWebViewFragment home = BaseWebViewFragment.newInstance( bottomMenuBean.getTarget());
                fragmentList.add(home);
                Constants.TAB_MAIN_INDEX = i;
                homeBottomBar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCurrentFocusIndex == position){
                            //todo
//                            home.showRefresh();
                        }
                        mCurrentFocusIndex = position;
                    }
                });
            } else if (Constants.TAB_WITHDRAW.equals(bottomMenuBean.getId())) {
                AppCompatRadioButton radioButton = bottomBarItem(bottomMenuBean.getName(), R.drawable.app_bottom_tab_text_selector, R.drawable.app_bottom_bar_home_selector);
                fragmentList.add(BaseWebViewFragment.newInstance( bottomMenuBean.getTarget()));
                Constants.TAB_WITHDRAW_INDEX = i;
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentFocusIndex = position;
                    }
                });

            } else if (Constants.TAB_WELFARE.equals(bottomMenuBean.getId())) {
                AppCompatRadioButton radioButton = bottomBarItem(bottomMenuBean.getName(), R.drawable.app_bottom_tab_text_selector, R.drawable.app_bottom_bar_home_selector);
                fragmentList.add(BaseWebViewFragment.newInstance(bottomMenuBean.getTarget()));
                Constants.TAB_WELFARE_INDEX = i;
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentFocusIndex = position;
                    }
                });

            } else if (Constants.TAB_MINE.equals(bottomMenuBean.getId())) {
                AppCompatRadioButton radioButton = bottomBarItem(bottomMenuBean.getName(), R.drawable.app_bottom_tab_text_selector, R.drawable.app_bottom_bar_home_selector);
                fragmentList.add(BaseWebViewFragment.newInstance(bottomMenuBean.getTarget()));
                Constants.TAB_MY_INDEX = i;
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentFocusIndex = position;
                    }
                });

            }
        }

        if (position > fragmentList.size()) {
            position = 0;
        }
        selectTabFromPosition(position);
        setCurrentFragment(position);

    }


    private void hideAllFragment(FragmentTransaction transaction, List<Fragment> fragmentList, int excludePosition) {
        if (fragmentList == null) {
            return;
        }
        int tabCount = fragmentList.size();
        for (int i = 0; i < tabCount; i++) {
            Fragment fragment = fragmentList.get(i);
            if (fragment != null && excludePosition != i) {
                transaction.hide(fragment);
            }
        }
    }
    private void removeAllFragment() {
        if (fragmentList == null || fragmentList.isEmpty()) {
            return;
        }
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : fragmentList) {
            fragmentTransaction.remove(fragment);
        }
        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentList.clear();

    }
}
