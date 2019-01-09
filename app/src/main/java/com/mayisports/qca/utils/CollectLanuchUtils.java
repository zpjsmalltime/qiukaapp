package com.mayisports.qca.utils;

import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

/**
 * Created by zhangpengju on 2018/5/28.
 */

public class CollectLanuchUtils {

    public static void onResume(){
        String url = Constant.BASE_URL + "php/api.php";

        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","duration");
        String starttime = (System.currentTimeMillis()+"").substring(0,10);
        params.put("starttime",starttime);
        params.put("endtime","");

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });


    }

    public static void onPause(){
        String url = Constant.BASE_URL + "php/api.php";

        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","duration");
        params.put("starttime","");
        String endtime = (System.currentTimeMillis()+"").substring(0,10);
        params.put("endtime",endtime);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });


    }
}
