package com.mayisports.qca.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mayisports.qca.bean.ImmediateScoreBean;
import com.mayisports.qca.bean.RefreshBean;

import java.util.ArrayList;

/**
 *  解析工具类
 */
public class JsonParseUtils {



    public static  <T> T parseJsonClass(String json, Class<T> beanClass){
        try {
            return new Gson().fromJson(json, beanClass);
        }catch (Exception e){
            return null;
        }
    }

    public static ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> parseJsonRefreshList(String json, Class<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> object){
        try {

            return new Gson().fromJson(json, new TypeToken<ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean>>() {
            }.getType());
        }catch (Exception e){
            return null;
        }
    }

}
