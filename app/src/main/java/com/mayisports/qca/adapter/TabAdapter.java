package com.mayisports.qca.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 暂时废弃不用  性能问题
 * Created by Zpj on 2018/1/25.
 */

public class TabAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments;
        private List<String> tabTitle;

        public TabAdapter(FragmentManager fm, List<Fragment> fragments,List<String> tabTitle) {
            super(fm);
            this.fragments = fragments;
            this.tabTitle = tabTitle;
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        //设置tablayout标题
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitle.get(position);

        }

}
