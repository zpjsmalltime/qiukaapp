package com.mayisports.qca.view;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by zhangpengju on 2018/7/30.
 */

public class SeeAllTextViewUtils {
    /**
     * 设置textView结尾...后面显示的文字和颜色
     * @param context 上下文
     * @param textView textview
     * @param minLines 最少的行数
     * @param originText 原文本
     * @param endText 结尾文字
     * @param endColorID 结尾文字颜色id
     * @param isExpand 当前是否是展开状态
     */
    public static void toggleEllipsize(final Context context,
                                final TextView textView,
                                final int minLines,
                                final String originText,
                                final String endText,
                                final int endColorID,
                                final boolean isExpand) {



        if (TextUtils.isEmpty(originText) || originText.length()<80) {
            return;
        }
        try {
            textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                    .OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (isExpand) {
                        textView.setText(originText);
                    } else {
                        int paddingLeft = textView.getPaddingLeft();
                        int paddingRight = textView.getPaddingRight();
                        TextPaint paint;
                        paint = textView.getPaint();
                        float moreText = textView.getTextSize() * endText.length();
                        float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) *
                                minLines - moreText;
                        CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint,
                                availableTextWidth, TextUtils.TruncateAt.END);
                        if (ellipsizeStr.length() < originText.length()) {
                            if(ellipsizeStr.length()<6)return;
                            CharSequence temp = ellipsizeStr.subSequence(0, ellipsizeStr.length() - 6) + "..." + endText;
                            SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                            ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor
                                            (endColorID)),
                                    temp.length() - endText.length(), temp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                            textView.setText(ssb);
                        } else {
                            textView.setText(originText);
                        }
                    }
                    if (Build.VERSION.SDK_INT >= 16) {
                        textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });
        }catch (Exception e){

        }
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i< c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }if (c[i]> 65280&& c[i]< 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

}
