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
 * 提示消息通用弹窗
 * Created by Zpj on 2018/3/23.
 */

public class ToastMsgPopuWindow extends PopupWindow {

    private TextView tv_content_toast_msg;
    private TextView tv_ok_toast_msg;


    public ToastMsgPopuWindow(View.OnClickListener paramOnClickListener, String title){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_msg, null);
        tv_content_toast_msg = view.findViewById(R.id.tv_content_toast_msg);
//        tv_content_toast_msg.setOnClickListener(paramOnClickListener);
        tv_ok_toast_msg = view.findViewById(R.id.tv_ok_toast_msg);
        tv_ok_toast_msg.setOnClickListener(paramOnClickListener);


        tv_content_toast_msg.setText(title);
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

    public ToastMsgPopuWindow(View.OnClickListener paramOnClickListener, String title,String btnText){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_msg, null);
        tv_content_toast_msg = view.findViewById(R.id.tv_content_toast_msg);
//        tv_content_toast_msg.setOnClickListener(paramOnClickListener);
        tv_ok_toast_msg = view.findViewById(R.id.tv_ok_toast_msg);
        tv_ok_toast_msg.setOnClickListener(paramOnClickListener);
        tv_ok_toast_msg.setText(btnText);


        tv_content_toast_msg.setText(title);
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
