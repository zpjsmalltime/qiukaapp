package com.mayisports.qca.alipay;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * 支付宝参数bean
 */
public class PaySignBean implements Serializable {
    public String msg;
    public Boolean success;
    public Obj obj;

    public class Obj implements Serializable {

        public String subject;
        public String _input_charset;
        public String sign;
        public String it_b_pay;
        public String body;
        public String notify_url;
        public String payment_type;
        public String out_trade_no;
        public String partner;
        public String service;
        public String total_fee;
        public String sign_type;
        public String seller_id;
        public String show_url;

        @Override
        public String toString() {
            String s = "";
            Field[] arr = this.getClass().getFields();
            for (Field f : getClass().getFields()) {
                try {
                    s += f.getName() + "=" + f.get(this) + "\n,";
                } catch (Exception e) {
                }
            }
            return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
        }
    }

    @Override
    public String toString() {
        String s = "";
        Field[] arr = this.getClass().getFields();
        for (Field f : getClass().getFields()) {
            try {
                s += f.getName() + "=" + f.get(this) + "\n,";
            } catch (Exception e) {
            }
        }
        return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
    }
}
