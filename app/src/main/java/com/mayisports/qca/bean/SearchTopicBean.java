package com.mayisports.qca.bean;

import java.util.List;

/**
 * 关联话题，搜索话题bean
 * Created by Zpj on 2018/2/6.
 */

public class SearchTopicBean {
    /**
     * status : {"login":1,"id":"71640"}
     * data : {"hotSearchList":["巴萨","皇马","拜仁","巴黎","曼联","曼城","切尔西","阿森纳","米兰","尤文","亚冠"],"hotTopicList":[{"title":"枪手强援来袭，全力迎战埃弗顿","id":"1222"},{"title":"蓝军客战沃特福德，孔蒂能否守住帅位","id":"1224"},{"title":"国王杯半决赛，巴萨主场迎战瓦伦西亚","id":"1221"},{"title":"沙尔克04迎战狼堡，胜负难料","id":"1225"},{"title":"如何看待把校园足球和升学挂钩的政策？","id":"1223"}],"myTopicList":[{"title":"国王杯半决赛，西班牙人能否逆袭巴萨？","id":"1196"},{"title":"斯托克城做客老特拉福德，曼联能否克敌制胜？","id":"1195"},{"title":"拜仁客战药厂，能否全取三分？","id":"1193"}]}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 1
         * id : 71640
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        public List<String> hotSearchList;
        public List<MyTopicListBean> hotTopicList;
        public List<MyTopicListBean> myTopicList;
        public List<MyTopicListBean> topic;

        public static class HotTopicListBean {
            /**
             * title : 枪手强援来袭，全力迎战埃弗顿
             * id : 1222
             */

            public String title;
            public String id;
        }

        public static class MyTopicListBean {
            /**
             * title : 国王杯半决赛，西班牙人能否逆袭巴萨？
             * id : 1196
             */

            public String title;
            public String id;
        }
    }
}
