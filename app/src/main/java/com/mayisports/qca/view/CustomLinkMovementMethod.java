package com.mayisports.qca.view;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

/**
 * Created by zhangpengju on 2018/8/16.
 */

public class CustomLinkMovementMethod extends LinkMovementMethod {

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        boolean b = super.onTouchEvent(widget,buffer,event);
        //解决点击事件冲突问题
        if(!b && event.getAction() == MotionEvent.ACTION_UP){
            ViewParent parent = widget.getParent();//处理widget的父控件点击事件
            if (parent instanceof ViewGroup) {
                return ((ViewGroup) parent).performClick();
            }
        }
        return b;





//        int action = event.getAction();
//
//        if (action == MotionEvent.ACTION_UP ||
//                action == MotionEvent.ACTION_DOWN) {
//            int x = (int) event.getX();
//            int y = (int) event.getY();
//
//            x -= widget.getTotalPaddingLeft();
//            y -= widget.getTotalPaddingTop();
//
//            x += widget.getScrollX();
//            y += widget.getScrollY();
//
//            Layout layout = widget.getLayout();
//            int line = layout.getLineForVertical(y);
//            int off = layout.getOffsetForHorizontal(line, x);
//
//            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
//
//            if (link.length != 0) {
//                if (action == MotionEvent.ACTION_UP) {
//                    //除了点击事件，我们不要其他东西
//                    link[0].onClick(widget);
//                }
//                return true;
//            }
//        }
//        return true;





    }

    public static CustomLinkMovementMethod getInstance() {
        if (sInstance == null)
            sInstance = new CustomLinkMovementMethod();

        return sInstance;
    }


    private static CustomLinkMovementMethod sInstance;
}
