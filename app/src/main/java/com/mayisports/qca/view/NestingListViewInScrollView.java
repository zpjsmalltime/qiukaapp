package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * listview  在 ScrollView 中使用
 */
public class NestingListViewInScrollView extends ListView {

	public NestingListViewInScrollView(Context context) {
		super(context);
	}

	public NestingListViewInScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NestingListViewInScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, mExpandSpec);
	}

}
