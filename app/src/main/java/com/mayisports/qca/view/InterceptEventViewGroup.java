package com.mayisports.qca.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by zhangpengju on 2018/8/14.
 */

public class InterceptEventViewGroup extends FrameLayout {
    public InterceptEventViewGroup(@NonNull Context context) {
        super(context);
    }

    public InterceptEventViewGroup(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptEventViewGroup(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private boolean isIntercept;

    public void setIntercept(boolean isIntercept){
        this.isIntercept = isIntercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

         super.onInterceptTouchEvent(ev);
        return  isIntercept;
    }
}
