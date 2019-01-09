package com.mayisports.qca.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 智能选球数据bean
 * Created by Zpj on 2018/1/15.
 */

public class IntelBallSelectBean {

    public List<PacklistBean> packlist;
    public List<DataBean> data;

    public static class PacklistBean {
        /**
         * title : 名人大咖推荐套餐
         * desc_value : 110.1%
         * desc_text : 近10场回报率
         * sort_value : 110.1
         * pack_id : 11
         * cpw : 117
         * view : 10768
         * price : 98
         * new : 7
         */

        public String title;
        public String desc_value;
        public String desc_text;
        public String sort_value;
        public int pack_id;
        public int cpw;
        public String view;
        public int price;
        @SerializedName("new")
        public int newX;
    }

    public static class DataBean implements Cloneable{
        /**
         * statics : {"g":[["连续不胜",8]],"sv":8}
         * match : {"timezoneoffset":"1516042800","leagueName":"葡超","hostTeamId":"839","awayTeamId":"438","hostTeamName":"摩里伦斯","awayTeamName":"塞图巴尔","betId":"1415172","asiaTape":"2500","date":"20180116 3:00"}
         */

        public StaticsBean statics;
        public MatchBean match;

        @Override
        public DataBean clone() {
            DataBean dataBean = null;
            try {
                dataBean = (DataBean) super.clone();
            } catch (CloneNotSupportedException e) {
            }
            dataBean.statics = statics.clone();
            return dataBean;
        }

        public static class StaticsBean implements Cloneable {
            @Override
            protected StaticsBean clone() {
                StaticsBean starStutasBean = null;
                try {
                    starStutasBean  = (StaticsBean) super.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return starStutasBean;
            }

            /**
             * g : [["连续不胜",8]]
             * sv : 8
             */



            public int sv;
            public List<List<String>> g;
            public List<List<String>> h;
            /**
             * type 1  主队
             * type 2 客队
             */
            public int type;
        }

        public static class MatchBean {
            /**
             * timezoneoffset : 1516042800
             * leagueName : 葡超
             * hostTeamId : 839
             * awayTeamId : 438
             * hostTeamName : 摩里伦斯
             * awayTeamName : 塞图巴尔
             * betId : 1415172
             * asiaTape : 2500
             * date : 20180116 3:00
             */

            public String timezoneoffset;
            public String leagueName;
            public String hostTeamId;
            public String awayTeamId;
            public String hostTeamName;
            public String awayTeamName;
            public String betId;
            public String asiaTape;
            public String date;
        }
    }
}
