package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Zpj on 2018/1/10.
 */

public class NewWebView extends WebView {

    public NewWebView(Context context) {
        super(context);
    }

    public NewWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private OnScrollChangeListener mOnScrollChangeListener;



    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // webview的高度
        float webcontent = getContentHeight() * getScale();
        // 当前webview的高度
        float webnow = getHeight() + getScrollY();
        if (Math.abs(webcontent - webnow) < 1) {
            //处于底端
            mOnScrollChangeListener.onPageEnd(l, t, oldl, oldt);
        } else if (getScrollY() == 0) {
            //处于顶端
            mOnScrollChangeListener.onPageTop(l, t, oldl, oldt);
        } else {
            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {

        public void onPageEnd(int l, int t, int oldl, int oldt);

        public void onPageTop(int l, int t, int oldl, int oldt);

        public void onScrollChanged(int l, int t, int oldl, int oldt);

    }
}
