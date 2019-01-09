package com.mayisports.qca.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.ShootVideoFragment;
import com.mayisports.qca.fragment.TabLayoutFragment;

import java.util.List;

/**
 * 主界面adapter
 * Created by zhangpengju on 2018/6/6.
 */

public class VpMainAdapter extends FragmentPagerAdapter {

//    private List<Fragment> fragments;
    private List<String> tabTitle;

    public VpMainAdapter(FragmentManager fm, List<String> tabTitle) {
        super(fm);
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        String[] split = tabTitle.get(position).split(",");
        String titleId = split[1];
        if("-2".equals(titleId)){
            ShootVideoFragment shootVideoFragment = ShootVideoFragment.newInstance("0");
            return shootVideoFragment;
        }else {
            DynamicFragment dynamicFragment = DynamicFragment.newInstance(titleId);
            return dynamicFragment;
        }

    }

    @Override
    public int getCount() {
        return tabTitle.size();
    }

    //设置tablayout标题
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle.get(position).split(",")[0];

    }
}
