package com.mayisports.qca.view;

/**
 * Created by Zpj on 2018/2/28.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mayi.mayisports.R;

public class SelectPicPopupWindow extends PopupWindow implements RadioGroup.OnCheckedChangeListener {



    public TextView btn_take_photo, btn_pick_photo, btn_cancel,btn_share_photo;
    private View mMenuView;
    public RadioGroup rg_share_fg;
    public TextView tv_share_title;

    private SharePopuwind.ShareTypeClickListener shareTypeClickListener;

    public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_dialog, null);
        tv_share_title = mMenuView.findViewById(R.id.tv_share_title__);
        rg_share_fg = mMenuView.findViewById(R.id.rg_share_fg);
        rg_share_fg.setOnCheckedChangeListener(this);


        btn_take_photo =  mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo =  mMenuView.findViewById(R.id.btn_pick_photo);
        btn_share_photo = mMenuView.findViewById(R.id.btn_share_photo);
        btn_cancel =  mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
        btn_share_photo.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick, SharePopuwind.ShareTypeClickListener shareTypeClickListener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.alert_dialog, null);

        rg_share_fg = mMenuView.findViewById(R.id.rg_share_fg);
        rg_share_fg.setOnCheckedChangeListener(this);
        tv_share_title = mMenuView.findViewById(R.id.tv_share_title__);


        this.shareTypeClickListener = shareTypeClickListener;

        btn_take_photo =  mMenuView.findViewById(R.id.btn_take_photo);
        btn_pick_photo =  mMenuView.findViewById(R.id.btn_pick_photo);
        btn_share_photo = mMenuView.findViewById(R.id.btn_share_photo);
        btn_cancel =  mMenuView.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        btn_pick_photo.setOnClickListener(itemsOnClick);
        btn_take_photo.setOnClickListener(itemsOnClick);
        btn_share_photo.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int type = -1;
        switch (checkedId){
            case R.id.rb_0_share_fg:
                type = 1;
                break;
            case R.id.rb_1_share_fg:
                type = 2;
                break;
            case R.id.rb_2_share_fg:
                type = 3;
                break;
            case R.id.rb_3_share_fg:
                type = 4;
                break;
            case R.id.rb_4_share_fg:
                type = 5;
                break;
        }

        if(shareTypeClickListener != null){
            shareTypeClickListener.onTypeClick(type);
        }
        dismiss();
    }
}