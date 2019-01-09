package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2018/1/16.
 */

public class GuessToastItemBean {

    public List<DataBean> data;


    public static class DataBean {
        /**
         * match_index : 201803
         * date : 01.15-01.22
         */

        public String match_index;
        public String date;
        public boolean isCheck;
    }
}
