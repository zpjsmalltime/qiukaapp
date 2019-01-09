package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2017/12/10.
 */

public class MineUserInfoBean {


    /**
     * user_id : 71640
     * user : {"name":"18032011888","nickname":"球咖小宝贝儿","coin":30.5,"mobile":"18032011888","weibo_headurl":"http://dldemo-img.stor.sinaapp.com/121496298498.png","coin2cash":30.5,"msg_count":0,"followers_count":"10","topic_count":"29","view_count":"1059","user_level":5,"level_score":"383","level_score_start":100,"user_level_helper":217,"verify_reason":"小仙女","my_friends_count":"6"}
     * time : 0.013992
     */

    public String user_id;
    public UserBean user;
    public double time;

    public static class UserBean {


        public List<List<BannerBean>> bannerList;

        /**
         * name : 18032011888
         * nickname : 球咖小宝贝儿
         * coin : 30.5
         * mobile : 18032011888
         * weibo_headurl : http://dldemo-img.stor.sinaapp.com/121496298498.png
         * coin2cash : 30.5
         * msg_count : 0
         * followers_count : 10
         * topic_count : 29
         * view_count : 1059
         * user_level : 5
         * level_score : 383
         * level_score_start : 100
         * user_level_helper : 217
         * verify_reason : 小仙女
         * my_friends_count : 6
         */

        public String name;
        public String nickname;
        public double coin;
        public String mobile;
        public String weibo_headurl;
        public double coin2cash;
        public int msg_count;
        public String followers_count;
        public String topic_count;
        public String view_count;
        public int user_level;
        public String level_score;
        public int level_score_start;
        public int user_level_helper;
        public String verify_reason;
        public String my_friends_count;
        public String verify_type;


        public WeiboBean weibo;
        public WeiboBean weixin;
        public WeiboBean qq;

        /**
         * 是否显示达人
         */
        public String subtype;

        /**
         * 偏好设置
         */
        public String sports_category;

    }


    public static class WeiboBean{
        public String token;
        public String user_id;
        public String nickname;
    }

    public static class BannerBean{
        public String img;
        public String url;
    }
}
