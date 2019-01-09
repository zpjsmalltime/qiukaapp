package com.mayisports.qca.bean;

import java.util.List;

/**
 * 基础配置 bean
 * Created by zhangpengju on 2018/5/7.
 */

public class MainMetaBean {

    /**
     * meta : {"match_thread_interval":15,"sys_msg_interval":60,"banner_interval":6,"display_category_selection":1,"tag":{"ico":"https://app.mayisports.com/mobileFiles/midIcon.png","url":"https://app.mayisports.com/?source=ios&version=5.1/#/game"}}
     */

    public MetaBean meta;

    public static class MetaBean {
        /**
         * match_thread_interval : 15
         * sys_msg_interval : 60
         * banner_interval : 6
         * display_category_selection : 1
         * tag : {"ico":"https://app.mayisports.com/mobileFiles/midIcon.png","url":"https://app.mayisports.com/?source=ios&version=5.1/#/game"}
         */

        public int match_thread_interval;
        public int sys_msg_interval;
        public int banner_interval;
        public int display_category_selection;
        public TagBean tag;
        public String token;

        public static class TagBean {
            /**
             * ico : https://app.mayisports.com/mobileFiles/midIcon.png
             * url : https://app.mayisports.com/?source=ios&version=5.1/#/game
             */

            public String ico;
            public String url;
        }


        /**
         * 开屏广告
         */
        public AD screen_ad;

        /**
         * 主界面广告
         */
        public AD ad;

        /**
         * 控制评论是否选中同步动态
         */

        public AD reply;



    }

    public static class AD{
        public String img_url;
        public String link;


        /**
         * 1选中    其他不选
         */
        public int share;
    }

    public List<BannerBean> bannerList;
    public static class BannerBean{
        public String img;
        public String url;
        public int type;
        public String topic_id;
    }
}
