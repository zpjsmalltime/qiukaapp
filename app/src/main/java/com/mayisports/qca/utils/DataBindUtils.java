package com.mayisports.qca.utils;

import android.text.TextUtils;
import android.widget.TextView;

/**
 * 处理特殊逻辑显示 工具类
 * Created by Zpj on 2018/2/7.
 */

public class DataBindUtils {

    /**
     *
     *
     * 处理列表中点赞数量逻辑
     * @param tv
     * @param praiseCount
     */
    public static void setPraiseCount(TextView tv,int praiseCount){
        if(praiseCount == 0){
            tv.setText("赞");
        }else{
            String s = parseIntToK(Integer.valueOf(praiseCount));
            tv.setText(s+"");
        }
    }
    public static void setPraiseCount(TextView tv,String praiseCount){
        if(TextUtils.isEmpty(praiseCount) || Integer.valueOf(praiseCount) == 0){
            tv.setText("赞");
        }else{


            String s = parseIntToK(Integer.valueOf(praiseCount));
            tv.setText(s+"");
        }
    }


    public static void setPraiseCount(TextView tv,String praiseCount,boolean isAutoData){
        if(TextUtils.isEmpty(praiseCount) || Integer.valueOf(praiseCount) == 0){
            tv.setText("");
        }else{


            String s = parseIntToK(Integer.valueOf(praiseCount));
            tv.setText(s+"");
        }
    }

    public static String parseIntToK(int view_count) {
        try {
            if(view_count <1000)return view_count+"";
            int k = view_count / 1000;
            int b = view_count % 1000 / 100;
            return k+"."+b+"k";
        }catch ( Exception e){
            return "0.0";
        }

    }
    /**
     * 处理底部点赞 字样  显示数量逻辑
     * @param tv
     * @param praiseCount
     */
    public static void setBottomPraise(TextView tv, String praiseCount) {
        if(TextUtils.isEmpty(praiseCount) || Integer.valueOf(praiseCount) == 0){
            tv.setText("赞");
        }else{
            String s = parseIntToK(Integer.valueOf(praiseCount));
//            tv.setText("赞·"+s);
            tv.setText(""+s);
        }
    }

    /**
     * 处理评论显示逻辑
     * @param tv
     * @param reply_count
     */
    public static void setComentCount(TextView tv, String reply_count) {
        if(TextUtils.isEmpty(reply_count) || Integer.valueOf(reply_count) == 0){
            tv.setText("评论");
        }else{
            tv.setText(reply_count+"");
        }
    }

    public static void setPraiseCountShoot(TextView tv,String praiseCount) {
        if(TextUtils.isEmpty(praiseCount) || Integer.valueOf(praiseCount) == 0){
            tv.setText("");
        }else{


            String s = parseIntToK(Integer.valueOf(praiseCount));
            tv.setText(s+"");
        }
    }

    public static void setPraiseCountVideo(TextView tv, String praiseCount) {
        if(TextUtils.isEmpty(praiseCount) || Integer.valueOf(praiseCount) == 0){
            tv.setText(" ");
        }else{
            String s = parseIntToK(Integer.valueOf(praiseCount));
            tv.setText(s+"");
        }
    }

    public static void setComentCountVideo(TextView tv, String reply_count) {
        if(TextUtils.isEmpty(reply_count) || Integer.valueOf(reply_count) == 0){
            tv.setText(" ");
        }else{
            tv.setText(reply_count+"");
        }
    }
}
