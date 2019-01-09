package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2017/12/13.
 */

public class StarStutasBean {

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
