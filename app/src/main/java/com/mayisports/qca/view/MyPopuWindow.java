package com.mayisports.qca.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.utils.DisplayUtil;

/**
 * Created by Zpj on 2017/12/12.
 */

public class MyPopuWindow extends PopupWindow {
    private View mainView;
    private LinearLayout ll_publish_home, ll_my_publish_hom,ll_search_homee,ll_vip_home;

    public MyPopuWindow(Activity paramActivity, View.OnClickListener paramOnClickListener, int paramInt1, int paramInt2){
        super(paramActivity);
        //窗口布局
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.plus_toast_layout, null);
        ll_publish_home = ((LinearLayout)mainView.findViewById(R.id.ll_publish_home));
        ll_my_publish_hom = (LinearLayout)mainView.findViewById(R.id.ll_my_publish_home);
        ll_search_homee = mainView.findViewById(R.id.ll_search_home);
        ll_vip_home = mainView.findViewById(R.id.ll_vip_home);

        //设置每个子布局的事件监听器
        if (paramOnClickListener != null){
            ll_publish_home.setOnClickListener(paramOnClickListener);
            ll_my_publish_hom.setOnClickListener(paramOnClickListener);
            ll_search_homee.setOnClickListener(paramOnClickListener);
            ll_vip_home.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        int i = DisplayUtil.dip2px(QCaApplication.getContext(), 165);
        setWidth(i);
        int i1 = DisplayUtil.dip2px(QCaApplication.getContext(), 150);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置宽度
//        setWidth(paramInt1);
        //设置高度
//        setHeight(paramInt2);
        //设置显示隐藏动画
//        setAnimationStyle(R.style.AnimTools);
        //设置背景透明
//        setBackgroundDrawable(new ColorDrawable(0));

        setBackgroundDrawable(new BitmapDrawable());
// 设置点击窗口外边窗口消失
        setOutsideTouchable(true);
// 设置此参数获得焦点，否则无法点击
        setFocusable(true);
    }
}
