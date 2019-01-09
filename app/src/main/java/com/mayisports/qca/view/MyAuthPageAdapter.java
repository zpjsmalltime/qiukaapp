package com.mayisports.qca.view;

import android.view.View;

import com.mayi.mayisports.R;

import cn.sharesdk.framework.TitleLayout;

/**
 * Created by zhangpengju on 2018/4/12.
 */

public class MyAuthPageAdapter extends cn.sharesdk.framework.authorize.AuthorizeAdapter {

    public void onCreate() {
        TitleLayout titleLayout = getTitleLayout();
        titleLayout.getTvTitle().setText("球咖");
        titleLayout.getTvTitle().setVisibility(View.VISIBLE);
        titleLayout.getBtnBack().setImageResource(R.drawable.back_left);
        titleLayout.getBtnBack().setVisibility(View.VISIBLE);
    }
}
