package com.mayisports.qca.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.WebView;

/**
 * 自定义webview   用于一些交互处理
 * Created by Zpj on 2017/12/5.
 */

public class CustomWebView extends WebView {
    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        requestLayout();
        super.onDraw(canvas);
        int height = canvas.getHeight();

        int contentHeight = getContentHeight();
        Rect rect = new Rect();
         getDrawingRect(rect);
        int maximumBitmapHeight = canvas.getMaximumBitmapHeight();
        Log.e("canvas","draw..."+height+"content"+contentHeight+"---"+rect.height()+"bit"+maximumBitmapHeight);



    }
}
