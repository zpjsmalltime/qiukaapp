package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2017/12/20.
 */

public class RewardBean {

    /**
     * status : {"login":1,"id":"71640","errno":3,"errstr":"您的余额不够，请充值后再打赏吧"}
     */

    public StatusBean status;

    public static class StatusBean {
        /**
         * login : 1
         * id : 71640
         * errno : 3
         * "user_id": "200327",
         "login": 0,
         "first_auth": 1
         * errstr : 您的余额不够，请充值后再打赏吧
         */

        public int login;
        public String id;
        public int errno;
        public String errstr;

        public int first_auth;
        public String user_id;

        public int result;
    }
}
