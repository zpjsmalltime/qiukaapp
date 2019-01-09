package com.mayisports.qca.bean;

/**
 * 提现数据
 * Created by Zpj on 2018/2/24.
 */

public class WithdrawCoinBean {

    /**
     * status : {"login":1,"id":"200327","errno":1,"errstr":"提现金额出错"}
     */

    public StatusBean status;

    public static class StatusBean {
        /**
         * login : 1
         * id : 200327
         * errno : 1
         * errstr : 提现金额出错
         */

        public int login;
        public String id;
        public int errno;
        public String errstr;
    }
}
