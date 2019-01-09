package com.mayisports.qca.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mayisports.qca.fragment.TabLayoutFragment;

import java.util.List;

/**
 * 比赛模块，tab  底部内容fragment  适配器
 * Created by zhangpengju on 2018/6/6.
 */

public class VpAdapter extends FragmentPagerAdapter {

//    private List<Fragment> fragments;
    private List<String> tabTitle;

    public VpAdapter(FragmentManager fm,List<String> tabTitle) {
        super(fm);
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        String[] split = tabTitle.get(position).split(",");
        String titleId = split[1];
        String is = split[2];//是否有榜单
        TabLayoutFragment tabLayoutFragment = TabLayoutFragment.newInstance(titleId + "," + is);

        return tabLayoutFragment;
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
