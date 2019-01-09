package com.mayi.mayisports;

import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;


import com.danikula.videocache.HttpProxyCacheServer;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.SPUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

import org.kymjs.kjframe.http.HttpConfig;

import java.util.HashSet;

import cn.sharesdk.framework.ShareSDK;
import cn.shuzilm.core.Main;

/**
 * base application
 * Created by Zpj on 2017/12/5.
 */

public class QCaApplication extends Application {

    private static Context context;
    public static IWXAPI wxApi;

    public static HashSet<String> idSet = new HashSet<>();


    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        /**
         * 开发设置成true，发布时设置为false。
         */
        CrashReport.initCrashReport(this, Constant.APP_ID_BUGLY, false);

        /**
         * 配置友盟
         */
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE,null);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        /**
         * wechat
         */
        wxApi = WXAPIFactory.createWXAPI(this, null);
        wxApi.registerApp(Constant.APP_ID_WEPAY);


        /**
         * 友盟推送
         */
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String s) {
            }
        });


        /**
         * 可信id  数字联盟
         */

        Context ctx = this.getApplicationContext();

        try {

            String apiKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKbdck+oGEfIb1dApFA06ejO/rgG82Ebf4J8uV+T45qCy18qz6qI1D1hddh/aj7zAIxPKxGPOJ1nrWdICr+Nj0MCAwEAAQ==";

            Main.init(ctx,apiKey);
            Main.setConfig("apiKey", apiKey);
            Main.go(ctx,  BuildConfig.APPSTORE,"");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 视频请求流代理类   实现边播边缓存
     */
    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        QCaApplication app = (QCaApplication) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }


    public static Context getContext(){
        return context;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    /**
     * 程序终止回调
     */
    @Override
    public void onTerminate() {

        SPUtils.clearCacheSelectSP();
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);

        if(level == TRIM_MEMORY_COMPLETE){
//            System.exit(0);
          //  android.os.Process.killProcess(Process.myPid());
        }
    }
}
