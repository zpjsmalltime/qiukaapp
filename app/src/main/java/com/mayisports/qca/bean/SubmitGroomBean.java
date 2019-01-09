package com.mayisports.qca.bean;

/**
 * 发布推荐成功bean
 * Created by Zpj on 2018/1/26.
 */

public class SubmitGroomBean {

    /**
     * status : {"login":0,"id":"71640"}
     * data : {"result":1,"text":"成功"}
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
         * result : 1
         * text : 成功
         */

        public int result;
        public String text;
    }
}
