package com.mayisports.qca.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mayi.mayisports.R;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 进入付费项选择提示
 * Created by Zpj on 2017/12/14.
 */

public class ToastPricePopuWindow extends PopupWindow {


    private TextView tv_content_toast_price;
    private TextView tv_cancle_toast_price;
    private TextView tv_go_toast_price;
    public ToastPricePopuWindow(Activity paramActivity, View.OnClickListener paramOnClickListener, String title, int i){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_price, null);
        tv_content_toast_price = view.findViewById(R.id.tv_content_toast_price);
        tv_content_toast_price.setOnClickListener(paramOnClickListener);
        tv_cancle_toast_price = view.findViewById(R.id.tv_cancle_toast_price);
        tv_cancle_toast_price.setOnClickListener(paramOnClickListener);
        tv_go_toast_price = view.findViewById(R.id.tv_go_toast_price);
        tv_go_toast_price.setOnClickListener(paramOnClickListener);

        if(i == 1){//确定
            tv_go_toast_price.setText("确定");
        }else if(i == 2){//充值
            tv_go_toast_price.setText("充值");
        }
        tv_content_toast_price.setText(title);
        setFocusable(true);
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public ToastPricePopuWindow(View.OnClickListener paramOnClickListener, String title, String leftText,String rightText){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_price, null);
        tv_content_toast_price = view.findViewById(R.id.tv_content_toast_price);
        tv_content_toast_price.setOnClickListener(paramOnClickListener);
        tv_cancle_toast_price = view.findViewById(R.id.tv_cancle_toast_price);
        tv_cancle_toast_price.setOnClickListener(paramOnClickListener);
        tv_go_toast_price = view.findViewById(R.id.tv_go_toast_price);
        tv_go_toast_price.setOnClickListener(paramOnClickListener);

        tv_cancle_toast_price.setText(leftText);
        tv_go_toast_price.setText(rightText);
        tv_content_toast_price.setText(title);
        setFocusable(true);
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}
