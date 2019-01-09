package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2017/12/27.
 */

public class PraiseBean {

    /**
     * status : {"login":1,"errno":0,"id":"200327"}
     * data : {"praise_status":1,"praise_count":"15"}
     */

    public StatusBean status;
    public DataBean data;

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

    public static class DataBean {
        /**
         * praise_status : 1
         * praise_count : 15
         */

        public int praise_status;
        public String praise_count;
    }
}
