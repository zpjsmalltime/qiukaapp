package com.mayisports.qca.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;

import com.mayi.mayisports.QCaApplication;

import java.util.List;

/**
 * 像素转换工具类
 */
public class DisplayUtil {

    private static int screenWidth;

    /**
     * 将px转换成dip/dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue/scale+0.5f);
    }

    /**
     * 将dip/dp转换成px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context , float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue*scale+0.5f);
    }

    /**
     * 将px值转换成sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(pxValue/fontScale+0.5f);
    }

    /**
     * 将sp值转换成px值
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue){
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue*fontScale+0.5f);
    }

    //使用系统提供的类TypedValue来进行单位的换算

    /**
     * dp2px
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Fragment context, int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }

    /**
     * sp2px
     * @param context
     * @param sp
     * @return
     */
    public static int sp2px(Context context, int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,sp,context.getResources().getDisplayMetrics());
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            String className1 = cpn.getClassName();
            if (className.equals(className1)) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }


    public static int getScreenWidth(Activity activity) {
        if(activity == null){
            return 100;
        }
       return activity.getWindowManager().getDefaultDisplay().getWidth();
    }
    public static int getScreenHeigth(Activity activity) {
        if(activity == null){
            return  100;
        }
        return activity.getWindowManager().getDefaultDisplay().getHeight();
    }
}
