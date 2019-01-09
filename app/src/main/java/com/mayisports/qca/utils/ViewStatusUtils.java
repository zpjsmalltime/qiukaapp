package com.mayisports.qca.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayi.mayisports.R;

import java.net.URLDecoder;
import java.util.List;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 更具不同状态更新View
 * Created by Zpj on 2017/12/15.
 */

public class ViewStatusUtils {

    private static Context context = getContext();
    /**
     * 根据状态设置输赢图片
     * @param revenueStr
     * @param iv_result_pan
     */
    public static void parseStatusForImg(String revenueStr, ImageView iv_result_pan) {
        revenueStr = URLDecoder.decode(revenueStr);
        switch (revenueStr){
            case "":
                iv_result_pan.setVisibility(View.GONE);
                break;
            case "赢":
                iv_result_pan.setImageDrawable(context.getResources().getDrawable(R.drawable.ying_reslult));
                break;
            case "输":
                iv_result_pan.setImageDrawable(context.getResources().getDrawable(R.drawable.fail_result));
                break;
            case "无效":
                iv_result_pan.setImageDrawable(context.getResources().getDrawable(R.drawable.zoushui_result));
                break;
            case "1/2赢 1/2无效":
                iv_result_pan.setImageDrawable(context.getResources().getDrawable(R.drawable.yingban_result));
                break;
            case "1/2输 1/2无效":
                iv_result_pan.setImageDrawable(context.getResources().getDrawable(R.drawable.shuban_result));
                break;
        }
    }


    /**
     * 添加标签
     * @param ll_container
     * @param tag
     * @param tag1
     */
    public static void addTags(LinearLayout ll_container, List<String> tag, List<String> tag1) {
        ll_container.removeAllViews();

        if(tag != null && tag.size()>0 && !TextUtils.isEmpty(tag.get(0))){
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(),R.layout.tag_blue_layout,null);
            TextView tv = (TextView) viewGroup.getChildAt(0);
            tv.setText(tag.get(0));
            ll_container.addView(viewGroup);
        }

        if(tag1 != null && tag1.size()>0 && !TextUtils.isEmpty(tag1.get(0))){
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(),R.layout.tag_red_layout,null);
            TextView tv = (TextView) viewGroup.getChildAt(0);
            tv.setText(tag1.get(0));
            ll_container.addView(viewGroup);
        }
    }

    public static void addTagsPersonal(LinearLayout ll_container, List<String> tag, List<String> tag1) {
        ll_container.removeAllViews();

        if(tag != null && tag.size()>0 && !TextUtils.isEmpty(tag.get(0))){
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(),R.layout.tag_white_layout,null);
            TextView tv = (TextView) viewGroup.getChildAt(0);
            if(tag.get(0) != null) {
                tv.setText(tag.get(0));
                ll_container.addView(viewGroup);
            }
        }

        if(tag1 != null && tag1.size()>0 && !TextUtils.isEmpty(tag1.get(0))){
            ViewGroup viewGroup = (ViewGroup) View.inflate(getContext(),R.layout.tag_white_layout,null);
            TextView tv = (TextView) viewGroup.getChildAt(0);
            if(tag1.get(0) != null) {
                tv.setText(tag1.get(0));
                ll_container.addView(viewGroup);
            }
        }
    }
}
