package com.mayisports.qca.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 赛事模块，积分榜数据
 * Created by zhangpengju on 2018/5/2.
 */

public class ScoreListBean

{
    public List<DataBean> data;

    public static class DataBean {
        /**
         * score : 93
         * 3 : 30
         * id : 54
         * name : 曼城
         * goal : 102
         * fumble : 26
         * 1 : 3
         * 0 : 2
         */

        //队标
        public String logoId;

        public int score;
        @SerializedName("3")
        public int _$3;
        public String id;
        public String name;
        public int goal;
        public int fumble;
        @SerializedName("1")
        public int _$1;
        @SerializedName("0")
        public int _$0;
    }
}
