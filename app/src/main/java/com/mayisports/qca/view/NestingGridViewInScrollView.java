package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Zpj on 2017/12/12.
 */

public class NestingGridViewInScrollView extends GridView {
    public NestingGridViewInScrollView(Context context) {
        super(context);
    }

    public NestingGridViewInScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NestingGridViewInScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
