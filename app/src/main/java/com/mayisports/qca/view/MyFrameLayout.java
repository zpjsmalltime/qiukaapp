package com.mayisports.qca.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.mayi.mayisports.R;

/**
 * Created by Zpj on 2017/12/29.
 */

public class MyFrameLayout extends FrameLayout {

    private CallBackListener callBackListener;

    public void setCallBackListener(CallBackListener callBackListener){
        this.callBackListener = callBackListener;
    }

    public MyFrameLayout(@NonNull Context context) {
        super(context);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        ImageView imageView = findViewById(R.id.iv_left_title_webview);
        int rawX = (int) ev.getRawX();
        int rawY = (int) ev.getRawY();
        boolean touchPointInView = isTouchPointInView(imageView, rawX, rawY);
        if(touchPointInView && callBackListener!=null){
            callBackListener.callBack();
            return false;
        }
        return false;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        if (view.isClickable() && y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    public interface CallBackListener{
        void callBack();
    }
}
