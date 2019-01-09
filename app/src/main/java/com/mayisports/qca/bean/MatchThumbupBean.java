package com.mayisports.qca.bean;

/**
 * 推荐单点赞
 * Created by Zpj on 2017/12/20.
 */

public class MatchThumbupBean {

    /**
     * status : {"login":0,"id":"71640"}
     * data : {"praise_status":0,"praise_count":"0"}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 0
         * id : 71640
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        /**
         * praise_status : 0
         * praise_count : 0
         */

        public int praise_status;
        public String praise_count;
    }
}
