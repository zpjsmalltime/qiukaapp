package com.mayisports.qca.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.ShootVideoFragment;
import com.mayisports.qca.fragment.ShootVideoNewFragment;
import com.mayisports.qca.fragment.TabLayoutFragment;

import java.util.List;

/**
 * 发现短视频界面 adapter
 * Created by zhangpengju on 2018/6/13.
 */
public class ShootVideoVpAdapter extends FragmentPagerAdapter {

//    private List<Fragment> fragments;
    private List<String> tabTitle;

    public ShootVideoVpAdapter(FragmentManager fm, List<String> tabTitle) {
        super(fm);
        this.tabTitle = tabTitle;
    }

    @Override
    public Fragment getItem(int position) {
        String[] split = tabTitle.get(position).split(",");
        String titleId = split[1];
        String is = split[2];//是否有榜单
     //   ShootVideoFragment shootVideoFragment = ShootVideoFragment.newInstance(titleId);
        DynamicFragment dynamicFragment = DynamicFragment.newInstance("-3");
        return  dynamicFragment;

      //  return shootVideoFragment;
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
