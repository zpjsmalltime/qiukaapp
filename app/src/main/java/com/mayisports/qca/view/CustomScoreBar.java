package com.mayisports.qca.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.mayi.mayisports.R;

/**
 * Created by zhangpengju on 2018/4/10.
 */

public class CustomScoreBar extends View {
    private Context context;
    private TypedValue typedValue;
    private static final int DEGREE =10;
    private int mColorLeft, mColorRight;
    private int mScoreLeft, mScoreRight;
    //各种画笔
    private Paint paintBar =new Paint();
    private Paint paintText=new Paint();
    public CustomScoreBar(Context context) {
        super(context);
        this.context=context;
        init();
    }

    public CustomScoreBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
    }

    public CustomScoreBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        /*初始化数据，默认属性*/
        mColorLeft = Color.rgb(95, 112, 72);
        mColorRight = Color.rgb(69, 91, 136);
        mScoreLeft =5;
        mScoreRight =8;
        typedValue=new TypedValue();
//        context.getTheme().resolveAttribute(R.attr.maintextclor,typedValue,true);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width=getWidth();
        float height=getHeight();
        /*文字画笔设置*/
        paintText.reset();
        paintText.setAntiAlias(true);
        /*文字的大小取控件宽度的十分之一和高度的二分之一的最小值*/
        paintText.setTextSize(Math.min(width / DEGREE, height) / 2);
        paintText.setColor(getResources().getColor(R.color.black_28));
        /*Paint.Align.CENTER：The text is drawn centered horizontally on the x,y origin*/
        paintText.setTextAlign(Paint.Align.CENTER);
        /*绘制中间的横线的画笔*/
        paintBar.reset();
        paintBar.setAntiAlias(true);
        paintBar.setStyle(Paint.Style.STROKE);
        /*画笔的高度为控件高度的十分之一*/
        paintBar.setStrokeWidth(height/10);
        /*测量字体*/
        Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
        float textBaseLineOffset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        /*绘制左边的比分文字*/
        /*把控件宽度分为10份，第一份和第十份分别绘制左边和右边的文字*/
        /*中间的8份宽度绘制比分条*/
        canvas.drawText("" + mScoreLeft, width / DEGREE / 2, height / 2 + textBaseLineOffset, paintText);
        paintBar.setColor(mColorLeft);
        /* drawLine(float startX, float startY, float stopX, float stopY,Paint paint)*/
        /*按照比例绘制左边比分对应长度的比分条*/
        canvas.drawLine(width / DEGREE, height / 2, width / DEGREE + width * (DEGREE - 2) / DEGREE * mScoreLeft / (mScoreLeft + mScoreRight), height / 2, paintBar);
        /*测量右边的比分文字*/
        fontMetrics = paintText.getFontMetrics();
        textBaseLineOffset = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        /*在控件宽度的最后十分之一绘制右边的比分数字*/
        canvas.drawText("" + mScoreRight, width-width / DEGREE / 2, height / 2 + textBaseLineOffset,paintText);
        paintBar.setColor(mColorRight);
        /*绘制右边的比分对应长度的比分条*/
        canvas.drawLine(width/ DEGREE +width*(DEGREE -2)/ DEGREE * mScoreLeft /(mScoreLeft + mScoreRight),height/2,width*(DEGREE -1)/ DEGREE,height/2, paintBar);
    }

    public void setScores(int score1, int score2) {
        this.mScoreLeft =score1;
        this.mScoreRight =score2;
        invalidate();
    }
}
