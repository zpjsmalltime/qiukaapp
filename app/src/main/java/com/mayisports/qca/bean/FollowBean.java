package com.mayisports.qca.bean;

/**
 * 关注响应数据
 * Created by Zpj on 2017/12/15.
 */

public class FollowBean {

    /**
     * status : {"login":1,"result":"1"}
     */

    public StatusBean status;

    public static class StatusBean {
        /**
         * login : 1
         * result : 1
         */

        public int login;
        public String result;
    }
}
