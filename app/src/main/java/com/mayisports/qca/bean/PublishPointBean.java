package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2018/1/19.
 */

public class PublishPointBean {

    /**
     * status : {"login":1,"errno":0,"id":"200327"}
     */

    public StatusBean status;

    public static class StatusBean {
        /**
         * login : 1
         * errno : 0
         * id : 200327
         */

        public int login;
        public int errno;
        public String id;
        public String errstr;
    }
}
