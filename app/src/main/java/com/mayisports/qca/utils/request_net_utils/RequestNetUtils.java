package com.mayisports.qca.utils.request_net_utils;

import com.google.gson.Gson;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpParams;

/**
 * 联网请求工具类
 * Created by Zpj on 2017/12/5.
 */

public class RequestNetUtils {

    public static  <T> void postRequest (String url, HttpParams params, final Class<T> clazz, final HttpCallBack<T> httpCallBack) {
        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
        url = url.substring(0,url.indexOf("//")+2) + replace;
        if (params == null) {
            params = new HttpParams();
        }

        StringBuilder urlParams = params.getUrlParams();
        HttpConfig.TIMEOUT = 1000 * 5;
        new KJHttp().post(url, params, false, new org.kymjs.kjframe.http.HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if(httpCallBack != null){
                    Gson gson = new Gson();

                    T t1 = gson.fromJson(t, clazz);
                    httpCallBack.onSuccess(t1);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                if(httpCallBack != null){
                    httpCallBack.onFailure(errorNo,strMsg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    public static  <T> void postRequest (RequestEntry entry, final Class<T> clazz, final HttpCallBack<T> httpCallBack) {
        String url = entry.url;
        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
        url = url.substring(0,url.indexOf("//")+2) + replace;

        StringBuilder urlParams = entry.getHttpParams().getUrlParams();
        HttpConfig.TIMEOUT = 1000 * 5;



        new KJHttp().post(url, entry.getHttpParams(), false, new org.kymjs.kjframe.http.HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                if(httpCallBack != null){
                    Gson gson = new Gson();

                    T t1 = gson.fromJson(t, clazz);
                    httpCallBack.onSuccess(t1);
                }
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                if(httpCallBack != null){
                    httpCallBack.onFailure(errorNo,strMsg);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

}
