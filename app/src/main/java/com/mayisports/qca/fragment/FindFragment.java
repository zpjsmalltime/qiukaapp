package com.mayisports.qca.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mayi.mayisports.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现模块 fragment
 * Created by Zpj on 2018/3/14.
 */

public class FindFragment extends Fragment implements View.OnClickListener {

    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "FindFragment";
    private Rec rec;
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }

    private void destroyReceiver(){
        if(localBroadcastManager != null && rec != null) {
            localBroadcastManager.unregisterReceiver(rec);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_groom_match_detail:
                vp_find_home.setCurrentItem(0);
                break;
            case R.id.tv_analyes_match_detail:
                vp_find_home.setCurrentItem(1);
                break;
        }
    }


    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }


    private View viewRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(getContext(), R.layout.main_find_fg,null);
        }
        return viewRoot;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initReceiver();
        initView();
        initData();
    }

    private ViewPager vp_find_home;
    private TextView tv_groom_match_detail;
    private TextView tv_analyes_match_detail;
    private View iv_groom_match_detail;
    private View iv_analyes_match_detail;

    private void initView() {
        vp_find_home = viewRoot.findViewById(R.id.vp_find_home);
        tv_groom_match_detail = viewRoot.findViewById(R.id.tv_groom_match_detail);
        tv_analyes_match_detail = viewRoot.findViewById(R.id.tv_analyes_match_detail);
        iv_groom_match_detail = viewRoot.findViewById(R.id.iv_groom_match_detail);
        iv_analyes_match_detail = viewRoot.findViewById(R.id.iv_analyes_match_detail);
        tv_groom_match_detail.setOnClickListener(this);
        tv_analyes_match_detail.setOnClickListener(this);


    }

    private List<Fragment> fragments = new ArrayList<>();
    private void initData() {
        fragments.clear();
        fragments.add(new HomeNewFragment());
        fragments.add(new ShootVideoFragment());


        vp_find_home.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        vp_find_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                   selectOne(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        vp_find_home.setCurrentItem(0);
//        vp_find_home.setOffscreenPageLimit(2);
    }

    /**
     * 刷新顶部tab状态
     * @param postion
     */
    private void selectOne(int postion){

        tv_groom_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_groom_match_detail.setVisibility(View.INVISIBLE);
        tv_groom_match_detail.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

        tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_analyes_match_detail.setVisibility(View.INVISIBLE);
        tv_analyes_match_detail.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));


        switch (postion){
            case 0:
                tv_groom_match_detail.setTextColor(getResources().getColor(R.color.white_));
                tv_groom_match_detail.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                iv_groom_match_detail.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.white));
                tv_analyes_match_detail.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                iv_analyes_match_detail.setVisibility(View.VISIBLE);
                break;

        }
    }
}
