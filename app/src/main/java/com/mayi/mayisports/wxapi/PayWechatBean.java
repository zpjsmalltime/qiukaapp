package com.mayi.mayisports.wxapi;

import com.google.gson.annotations.SerializedName;

/**
 * 支付bean
 */
public class PayWechatBean {
    private String appid;
    private String partnerid;
    @SerializedName("package")
    private String mpackage;
    private String timestamp;
    private String prepayid;
    private String noncestr;
    private String sign;

    public PayWechatBean(String appid, String partnerid, String mpackage, String timestamp, String prepayid, String noncestr, String sign) {
        this.appid = appid;
        this.partnerid = partnerid;
        this.mpackage = mpackage;
        this.timestamp = timestamp;
        this.prepayid = prepayid;
        this.noncestr = noncestr;
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getMpackage() {
        return mpackage;
    }

    public void setMpackage(String mpackage) {
        this.mpackage = mpackage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
