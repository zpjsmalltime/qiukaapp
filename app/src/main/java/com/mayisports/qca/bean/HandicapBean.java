package com.mayisports.qca.bean;

import java.util.List;

/**
 * 盘口数据bean
 * Created by Zpj on 2017/12/12.
 */

public class HandicapBean {

    /**
     * status : {"login":0}
     * data : {"asiaTapeFilter":[["0","0",1,7],["2500","2500",1,6],["-2500","-2500",1,5],["-5000","-5000",1,2],["5000","5000",1,2],["-7500","-7500",1,4],["7500","7500",1,3],["12500","12500",1,2],["-12500","-12500",1,1],["15000","15000",1,1],["17500","17500",1,1],["22500","22500",1,1]]}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 0
         */

        public int login;
    }

    public static class DataBean {
        public List<List<String>> asiaTapeFilter;
    }
}
