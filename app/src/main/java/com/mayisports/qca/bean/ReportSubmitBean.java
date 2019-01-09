package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2018/3/1.
 */

public class ReportSubmitBean {

    /**
     * status : {"login":1,"result":1}
     */

    public StatusBean status;

    public static class StatusBean {
        /**
         * login : 1
         * result : 1
         */

        public int login;
        public int result;
    }
}
