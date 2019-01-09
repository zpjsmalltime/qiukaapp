package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by Zpj on 2017/12/7.
 */

public class GridViewInListView extends GridView {
    public GridViewInListView(Context context) {
        super(context);
    }

    public GridViewInListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewInListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
        getLayoutParams().height = getMeasuredHeight();
    }
}
