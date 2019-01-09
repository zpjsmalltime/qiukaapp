package com.mayisports.qca.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


/**
 * Created by zhangpengju on 2018/8/23.
 */

public class AutoLineTextView extends TextView {
    public AutoLineTextView(Context context) {
        super(context);
    }

    public AutoLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        TextPaint paint = getPaint();
        float v = paint.measureText(getText().toString());

        String s = getText().toString();
        Rect rect = new Rect();
        paint.getTextBounds(s,0,s.length(),rect);

        Paint.FontMetrics fontMetrics = paint.getFontMetrics();


        float v1 = v / getWidth();


        Log.e("width",fontMetrics.top+"--"+fontMetrics.bottom+"--"+ v1);


    }
}
