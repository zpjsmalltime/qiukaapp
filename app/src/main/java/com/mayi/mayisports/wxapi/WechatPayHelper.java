package com.mayi.mayisports.wxapi;

import android.util.Log;

import com.mayi.mayisports.QCaApplication;
import com.mayisports.qca.utils.Constant;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @Module :
 * @Comments : 微信支付帮助类
 */
public class WechatPayHelper {
    private static IWXAPI wxApi;
    private static WechatPayHelper helper;

    public static WechatPayHelper getInstance() {
        wxApi = WXAPIFactory.createWXAPI(QCaApplication.getContext(), null);
        wxApi.registerApp(Constant.APP_ID_WEPAY);
        if (helper == null){
            helper = new WechatPayHelper();
        }
        return helper;
    }

    //是否安装了微信客户端
    public boolean isWXAppInstalled(){
        return wxApi.isWXAppInstalled();
    }

    /**
     * 调起微信支付
     *
     * @param bean
     */
    public void onWechatPay(PayWechatBean bean) {
        PayReq req = new PayReq();
        req.appId = bean.getAppid();
        req.partnerId = bean.getPartnerid();
        req.prepayId = bean.getPrepayid();
        req.nonceStr = bean.getNoncestr();
        req.timeStamp = bean.getTimestamp();
        req.packageValue = bean.getMpackage();
        req.sign = bean.getSign();
        req.extData = "app data"; // optional
        boolean b = QCaApplication.wxApi.sendReq(req);
        Log.d("WechatPayHelper", "b:" + b);
    }
}
