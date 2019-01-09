package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by zhangpengju on 2018/5/15.
 */

public class CoinListBean {


    /**
     * status : {"login":0,"id":"200034"}
     * data : [{"id":"60525","create_time":"1526366389","coin":"0.08","title":"答题彩蛋奖金"},{"id":"60522","create_time":"1526366171","coin":"0.18","title":"答题彩蛋奖金"},{"id":"60517","create_time":"1526366050","coin":"0.09","title":"答题彩蛋奖金"},{"id":"60516","create_time":"1526366008","coin":"0.06","title":"答题彩蛋奖金"},{"id":"60515","create_time":"1526365912","coin":"0.18","title":"答题彩蛋奖金"},{"id":"60495","create_time":"1526352290","coin":"2.1","title":"答题奖金"},{"id":"59832","create_time":"1526269343","coin":"2.1","title":"答题奖金"},{"id":"59827","create_time":"1526266731","coin":"-20","title":"金币提现\u2014\u2014支付宝"},{"id":"59713","create_time":"1526008609","coin":"0.08","title":"答题彩蛋奖金"},{"id":"59711","create_time":"1526008043","coin":"1","title":"答题奖金"}]
     */

    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        /**
         * login : 0
         * id : 200034
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        /**
         * id : 60525
         * create_time : 1526366389
         * coin : 0.08
         * title : 答题彩蛋奖金
         */

        public String id;
        public String create_time;
        public String coin;
        public String title;
    }
}
