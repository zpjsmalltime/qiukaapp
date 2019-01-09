package com.mayisports.qca.bean;

import com.google.gson.annotations.SerializedName;
import com.mayi.mayisports.wxapi.PayWechatBean;

/**
 * Created by Zpj on 2018/1/2.
 */

public class WePayBean {

    /**
     * status : 1
     * result : {"appid":"wx44083f44d242cceb","partnerid":"1341899901","prepayid":"wx20180102172332ff65fe37250804801235","package":"Sign=WXPay","noncestr":"zoeb3vnq6rwked9s72t2f92u7e7pr4oe","timestamp":"1514885012","sign":"680A9DF3D3DD38074C04C6329E9010B5"}
     */

    public int status;
    public PayWechatBean result;

}
