package com.mayisports.qca.utils.request_net_utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mayi.mayisports.BuildConfig;
import com.mayi.mayisports.QCaApplication;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.PhoneInfo;
import com.mayisports.qca.utils.SPUtils;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.DownloadTaskQueue;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.http.HttpConnectStack;
import org.kymjs.kjframe.http.HttpParams;
import org.kymjs.kjframe.http.HttpStack;
import org.kymjs.kjframe.http.Network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;


import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 *联网请求工具类 待封装
 *
 */
public class RequestNetWorkUtils {

    public static String getDeviceId() {
        TelephonyManager telephonyMgr = (TelephonyManager) QCaApplication.getContext().getSystemService(TELEPHONY_SERVICE);
        if (telephonyMgr != null) {
            return  telephonyMgr.getDeviceId();
        } else {
            return "未知DID";
        }
    }

    private static HttpParams setHeaders(HttpParams params) {
        /**
         * BuildConfig.URL_HOST+"?v=235732&source=android&version=4.2&network="
         + PhoneInfo.getInstance(QCaApplication.getContext()).getNetworkType()
         + "&uuid=" + PhoneInfo.getInstance(QCaApplication.getContext()).getMyUUID()
         + "&sysversion=" + Build.VERSION.RELEASE
         + "&platform=MMY&appstore="+platform+"/";
         */


//        params.put("v","235732");
//        if(!Constant.isSee) {
            params.put("source", "android");
            params.put("version", BuildConfig.VERSION_NAME);
            params.put("network", PhoneInfo.getInstance(QCaApplication.getContext()).getNetworkType());
            params.put("uuid", PhoneInfo.getInstance(QCaApplication.getContext()).getMyUUID());
            params.put("sysversion", Build.VERSION.RELEASE);
            params.put("appstore", BuildConfig.APPSTORE);
//        }




        //设置cookie

        String userId = SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID);
        String token = SPUtils.getString(QCaApplication.getContext(), Constant.TOKEN);
//        String sessid = SPUtils.getString(QCaApplication.getContext(), Constant.SESSID);
//        String cookieId = SPUtils.getString(QCaApplication.getContext(), Constant.COOKIE_ID);

        String header = SPUtils.getString(QCaApplication.getContext(), Constant.HEADER);
//        params.putHeaders("cookie","token="+token+";id="+userId+";"+header+"");
        params.putHeaders("cookie","token="+token+";id="+userId);


        String cookie = params.getHeaders().get("cookie");
        Log.e("cookie",cookie+"");


        try {
            params.putHeaders("appVersion", "QCa/"+getVersionCode());
        } catch (Exception e) {
            e.printStackTrace();
        }


//        params.putHeaders("deviceToken", UmengRegistrar.getRegistrationId(QCaApplication.getContext()));

        return params;
    }

    // 获取当前程序的版本号
    public static String getVersionCode() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = QCaApplication.getContext().getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(QCaApplication.getContext().getPackageName(), 0);
        return packInfo.versionName;
    }

//    public static void getRequest(String url, HttpParams params, HttpCallBack httpCallBack) {
//        if (params == null) {
//            params = new HttpParams();
//        }
//        params = setHeaders(params);
//        new KJHttp().get(url, params, false, httpCallBack);
//    }


    public static KJHttp kjHttp = new KJHttp();
    public static void getRequest(String url, HttpParams params, final HttpCallBack httpCallBack) {
        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
        url = url.substring(0,url.indexOf("//")+2) + replace;
        if (params == null) {
            params = new HttpParams();
        }
        StringBuilder urlParams = params.getUrlParams();
        Log.i("urlParams",urlParams.toString());
        params = setHeaders(params);


//        new KJHttp().get(url, params, false, httpCallBack);

        /**
         * 十秒联网超时
         */
        HttpConfig.TIMEOUT = 1000*10;
        kjHttp.get(url, params, false, httpCallBack);


    }

    /**
     * 上传文件方法
     * @param url
     * @param params
     * @param httpCallBack
     */
    public static void postFileRequest(String url, HttpParams params, final HttpCallBack httpCallBack) {
        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
        url = url.substring(0,url.indexOf("//")+2) + replace;
        if (params == null) {
            params = new HttpParams();
        }
        params = setHeaders(params);
//        new KJHttp().get(url, params, false, httpCallBack);
        kjHttp.post(url, params, false, httpCallBack);


    }



    public static void postRequest(String url, HttpParams params, HttpCallBack httpCallBack) {
//        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
//        url = url.substring(0,url.indexOf("//")+2) + replace;
//        if (params == null) {
//            params = new HttpParams();
//        }
//        params.put("timestamp", System.currentTimeMillis()+"");
//        try {
//            params.put("sign", Utils.md5ForParams(paramsSort(params)));
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        params = setHeaders(params);
//        StringBuilder urlParams = params.getUrlParams();
//        HttpConfig.TIMEOUT = 1000 * 5;

        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
        url = url.substring(0,url.indexOf("//")+2) + replace;
        if (params == null) {
            params = new HttpParams();
        }
        params = setHeaders(params);

        /**
         * 十秒联网超时
         */
        HttpConfig.TIMEOUT = 1000*10;
        new KJHttp().post(url, params, false, httpCallBack);
    }

    public static void postRequestOfLongTimeout(String url, HttpParams params, HttpCallBack httpCallBack) {
        String replace = url.substring(url.indexOf("//") + 2).replace("//", "/");
        url = url.substring(0,url.indexOf("//")+2) + replace;
        if (params == null) {
            params = new HttpParams();
        }
        params.put("timestamp", System.currentTimeMillis()+"");
        params = setHeaders(params);
        StringBuilder urlParams = params.getUrlParams();
        HttpConfig.TIMEOUT = 1000*500;
        new KJHttp().post(url, params, false, httpCallBack);
    }

    public static void getRequestWithCache(String url, HttpParams params, HttpCallBack httpCallBack) {
        params = setHeaders(params);
        new KJHttp().get(url, params, true, httpCallBack);
    }

    public static void postRequestWithCache(String url, HttpParams params, HttpCallBack httpCallBack) {
        if (params == null) {
            params = new HttpParams();
        }
        params = setHeaders(params);
        new KJHttp().post(url, params, true, httpCallBack);
    }


    private static String paramsSort(HttpParams httpParams) {
        StringBuilder stringBuilder = httpParams.getUrlParams().replace(0, 1, "");
        String[] split = stringBuilder.toString().split("&");
        Arrays.sort(split);
        stringBuilder = new StringBuilder("");
        for(int x = 0;x<split.length;x++){
            if(x == split.length -1){
                stringBuilder.append(split[x]);
            }else{
                stringBuilder.append(split[x]+"&");
            }
        }
        return stringBuilder.toString();
    }


    public static void downloadFile(String storeFilePath,String url, HttpCallBack httpCallBack){
        DownloadTaskQueue download = new KJHttp().download(storeFilePath, url, httpCallBack);

    }
}
