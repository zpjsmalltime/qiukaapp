package com.mayisports.qca.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.mayi.mayisports.QCaApplication;

/**
 * sp 存储工具类
 */
public class SPUtils {

    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "sp_data";
    public static final String FIRST = "first";

    public static final String SELECT_NAME = "select_activity";//筛选界面存储

    /**
     * 保存int类型的参数
     *
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value) {
        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    /**
     * 得到int类型的参数
     *
     * @param context
     * @param key
     * @return
     */
    public static int getInt(Context context, String key) {
        SharedPreferences sp =QCaApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    /**
     * 保存Stirng类型的参数
     */
    public static void putString(Context context, String key, String value) {

        if(key.contains(Constant.TYPE)){
            SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(SELECT_NAME,
                    Context.MODE_PRIVATE);
            if(value == null){
                value = "";
            }
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, value);
            editor.commit();
            return;
        }

        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(value == null){
            value = "";
        }
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * 得到String类型的参数
     */
    public static String getString(Context context, String key) {

        if(key.contains(Constant.TYPE)){
            SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(SELECT_NAME, Context.MODE_PRIVATE);
            return sp.getString(key, "");
        }


        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
//        if("id".equals(key)){
//            return sp.getString(key, "123456");//userId为空的情况下返回123456
//        }else {
            return sp.getString(key, "");
//        }

    }

    /**
     * 保存boolean值
     * 用于存储是否是第一次进入程序的记录
     */
    public static void putIsFirstBoolean(Context context, String key, boolean value){
        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FIRST,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /**
     * 根据key得到boolean值
     * 默认返回true(表示是第一次进入程序)
     */
    public static boolean getIsFirstBoolean(Context context, String key){
        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FIRST,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, true);
    }
    /**
     * 保存boolean值
     */
    public static void putBoolean(Context context, String key, boolean value){
        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    /**
     * 根据key得到boolean值,默认false
     */
    public static boolean getBoolean(Context context, String key){
        SharedPreferences sp = QCaApplication.getContext().getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    /**
     * 清除即时比分筛选所有缓存
     */
    public static void clearCacheSelectSP(){
        SharedPreferences.Editor editor =QCaApplication.getContext().getSharedPreferences(SELECT_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
