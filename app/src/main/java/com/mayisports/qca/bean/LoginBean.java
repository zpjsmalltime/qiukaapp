package com.mayisports.qca.bean;

/**
 * Created by Zpj on 2017/12/8.
 */

public class LoginBean {


    /**
     * status : {"errno":3,"errstr":"请求成功"}
     */

    public StatusBean status;
    public Data data;

    public static class StatusBean {
        /**
         * errno : 3
         * errstr : 请求成功
         */

        public int errno;
        public String errstr;
    }

    public static class Data{
       public int bind;
    }
}
