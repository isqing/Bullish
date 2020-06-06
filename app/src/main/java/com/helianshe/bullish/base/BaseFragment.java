package com.helianshe.bullish.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

public class BaseFragment extends Fragment {
    boolean isLastVisible = false;
    private boolean hidden = false;
    private boolean isFirst = true;
    private boolean isResuming = false;
    private boolean isViewDestroyed = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isLastVisible = false;
        hidden = false;
        isFirst = true;
        isViewDestroyed = false;
    }

    /**
     * Fragment 是否在前台。
     */
    private boolean isResuming() {
        return isResuming;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewDestroyed = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResuming = true;
        tryToChangeVisibility(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        isResuming = false;
        tryToChangeVisibility(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        onHiddenChangedClient(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        setUserVisibleHintClient(isVisibleToUser);
    }

    private void setUserVisibleHintClient(boolean isVisibleToUser) {
        tryToChangeVisibility(isVisibleToUser);
        if (isAdded()) {
            // 当Fragment不可见时，其子Fragment也是不可见的。因此要通知子Fragment当前可见状态改变了。
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).setUserVisibleHintClient(isVisibleToUser);
                    }
                }
            }
        }
    }

    void onHiddenChangedClient(boolean hidden) {
        this.hidden = hidden;
        tryToChangeVisibility(!hidden);
        if (isAdded()) {
            List<Fragment> fragments = getChildFragmentManager().getFragments();
            if (fragments != null && fragments.size() > 0) {
                for (Fragment fragment : fragments) {
                    if (fragment instanceof BaseFragment) {
                        ((BaseFragment) fragment).onHiddenChangedClient(hidden);
                    }
                }
            }
        }
    }

    private void tryToChangeVisibility(boolean tryToShow) {
//        Log.i("qing==", "tryToChangeVisibility: "+tryToShow+","+isLastVisible+","+isFragmentVisible());
        // 上次可见
        if (isLastVisible) {
            if (tryToShow) {
                return;
            }
            if (!isFragmentVisible()) {
                Log.i("qing==", "tryToChangeVisibility1: onFragmentPause");

                onFragmentPause();
                isLastVisible = false;
            }
            // 上次不可见
        } else {
            boolean tryToHide = !tryToShow;
            if (tryToHide) {
                return;
            }
            if (isFragmentVisible()) {

                onFragmentResume(isFirst, isViewDestroyed);
                isLastVisible = true;
                isFirst = false;
            }
        }
    }

    /**
     * Fragment是否可见
     */
    boolean isFragmentVisible() {
//        Log.i("qing==", "isFragmentVisible: "+","+isResuming()+","+getUserVisibleHint()+","+!hidden);
        return isResuming()
                && getUserVisibleHint()
                && !hidden;

    }

    /**
     * Fragment 可见时回调
     *
     * @param isFirst         是否是第一次显示
     * @param isViewDestroyed Fragment 的 View 被回收，但是Fragment实例仍在。
     */
    public void onFragmentResume(boolean isFirst, boolean isViewDestroyed) {
        Log.d("qing==onFragmentResume", "onFragmentResume: ");

    }

    /**
     * Fragment 不可见时回调
     */
    public void onFragmentPause() {
        Log.d("qing==onFragmentPause", "onFragmentPause: ");

    }
}



