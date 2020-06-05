package com.helianshe.bullish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.helianshe.bullish.base.BaseFragment;
import com.helianshe.bullish.base.BaseWebViewFragment;


public class MineFragment extends BaseWebViewFragment {

    private String url;


    public MineFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MineFragment newInstance(String url) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString("ulr");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        return view;
    }
}