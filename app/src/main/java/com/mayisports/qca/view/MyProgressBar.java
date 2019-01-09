package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * 增加进度条监听
 * Created by Zpj on 2018/3/21.
 */

public class MyProgressBar extends ProgressBar {

    private OnValueChangeListener onValueChangeListener;
    public MyProgressBar(Context context) {
        super(context);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener){
        this.onValueChangeListener = onValueChangeListener;
    }

    @Override
    public synchronized void setProgress(int progress) {
        super.setProgress(progress);
        if(onValueChangeListener != null){
            onValueChangeListener.onProgressChange(progress);
        }
    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress);
        if(onValueChangeListener != null){
            onValueChangeListener.onSecondaryProgressChange(secondaryProgress);
        }
    }

    public interface OnValueChangeListener{
        void onProgressChange(int progress);
        void onSecondaryProgressChange(int secondaryProgress);
    }
}
