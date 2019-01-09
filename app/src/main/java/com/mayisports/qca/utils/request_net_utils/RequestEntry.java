package com.mayisports.qca.utils.request_net_utils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.LinkedHashMap;

/**
 *
 * 请求网络实体封装类   url  参数 回掉类型
 *
 *
 * Created by Zpj on 2017/12/5.
 */

public class RequestEntry {

    public String url;

    private HttpParams httpParams ;
    public RequestEntry(){
         httpParams = new HttpParams();
    }

    public RequestEntry(String url){
        this();
        this.url = url;
    }

    public void put(String key,String value){
        httpParams.put(key,value);
    }

    public void put(String key,int value){
        httpParams.put(key,value);
    }

    public void put(String json){
        httpParams.putJsonParams(json);
    }

    public HttpParams getHttpParams(){
        return httpParams;
    }



}
