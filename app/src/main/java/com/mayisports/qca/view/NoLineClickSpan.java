package com.mayisports.qca.view;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import com.mayisports.qca.utils.ToastUtils;

/**
 * Created by zhangpengju on 2018/8/16.
 */

public class NoLineClickSpan extends ClickableSpan {



    private String tag;

    public NoLineClickSpan(String tag){
        super();
        this.tag = tag;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
//        ds.setColor(ds.linkColor);
        ds.setUnderlineText(false); //去掉下划线

    }


    @Override
    public void onClick(View widget) {
//        ToastUtils.toastNoStates(tag);
        if(onClickListenter != null){
            onClickListenter.onClick(tag);
        }
    }

    private OnClick onClickListenter;

    public void setOnClickListenter(OnClick onClickListenter){
        this.onClickListenter = onClickListenter;
    }

    public interface OnClick{
        void onClick(String tag);
    }


}
