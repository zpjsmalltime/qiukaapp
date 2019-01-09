package com.mayisports.qca.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Zpj on 2018/1/10.
 */

public class MyLinearLayout extends LinearLayout {

    private OnTouchCall onTouchCall;

    public void setOnTouchCall(OnTouchCall onTouchCall){
        this.onTouchCall = onTouchCall;
    }

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float rawY = ev.getRawY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                preY = (int) rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                int deY = (int) (rawY - preY);

                if(Math.abs(deY)>1){
                        if(onTouchCall != null){

//                            int height = findViewById(R.id.ll_top_title_match_detail).getHeight();
//                            int height1 = findViewById(R.id.ll_icon).getHeight();
//                            Log.e("can",b+"");
//                           int newY  = height +deY;
//                            if(height >0 && ){
//                                onTouchCall.onCall(deY);
//                                preY = (int) rawY;
//                                return false;
//                            }

//                            if(deY<0 || deY<0){
//                            if(newY>(height - height1) && newY<height+10) {
//                            boolean b = onTouchCall.onCall(deY);

//                                preY = (int) rawY;
//                                return true;
//                            }
//                                return super.dispatchTouchEvent(ev);
//                            }

//                            if(deY<0&&height>3){
//                                onTouchCall.onCall(deY);
//
//                                return true;
//                            }


                        //    preY = (int) rawY;
//                            if(b){
//                                return false;
//                            }
                        }
                    preY = (int) rawY;

                }


//                preY = (int) rawY;

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int preY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean bo =  super.onInterceptTouchEvent(ev);
        return bo;
    }

    public interface OnTouchCall{
        boolean onCall(int deY);
    }

}
