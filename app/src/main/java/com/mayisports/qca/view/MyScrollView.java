package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 处理内部webview 滑动冲突
 * Created by Zpj on 2017/12/29.
 */

public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private float preY;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
      return   super.onInterceptTouchEvent(ev);
//        boolean isIntercept = false;
//        float rawY = ev.getRawY();
//        switch (ev.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                preY = rawY;
//                isIntercept = false;
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                if(Math.abs(rawY-preY)>10){
//                    smoothScrollTo(0, (int) (rawY - preY));
//                    isIntercept = true;
//                }else{
//                    isIntercept = false;
//                }
//
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                isIntercept = false;
//                break;
//        }
//
//        return isIntercept;
    }



    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
