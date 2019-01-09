package com.mayisports.qca.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zpj on 2018/1/23.
 */

public class ComentsDetailBean {

    /**
     * status : {"login":1,"errno":0,"check_mode":1,"id":"71640"}
     * data : {"comment":{"topic_id":"1203","topic_title":"伦敦德比战，一触即发","background_img":null,"comment_id":"39180","comment":"保温杯，至少不败吧，这种比赛再拿不了玩个球啊。。。","create_time":"1516601357","praise_count":"1","reply_count":"2","view_count":"3428","title":null,"imglist":["http://sinacloud.net/topicimg/qkt_184956_1516601337157335.webp"],"score":"","praise_status":0,"reward_user_count":"0","reward_count":"0","user":{"nickname":"大希解球","headurl":"http://dldemo-img.stor.sinaapp.com/header_1512629882.png","user_id":"184956","verify_type":"3","follow_status":"1"}},"list":[{"reply_id":"4091","nickname":"邓玮","headurl":"http://dldemo-img.stor.sinaapp.com/4461515669195.png","user_id":"1","verify_type":"0","create_time":"1516660923","reply":"一大块空白区域啊","praise_count":"0","parent":{"nickname":"邓玮","reply":"你到底发了图片吗？"},"praise_status":0},{"reply_id":"4090","nickname":"邓玮","headurl":"http://dldemo-img.stor.sinaapp.com/4461515669195.png","user_id":"1","verify_type":"0","create_time":"1516660901","reply":"你到底发了图片吗？","praise_count":"0","praise_status":0}],"praise_list":[{"user_id":"198147","headurl":"http://dldemo-img.stor.sinaapp.com/header_1506591125.png"}],"reward_count_list":[5,10,15,20,50,88]}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 1
         * errno : 0
         * check_mode : 1
         * id : 71640
         */

        public int login;
        public int errno;
        public int check_mode;
        public String id;
    }

    public static class DataBean {
        /**
         * comment : {"topic_id":"1203","topic_title":"伦敦德比战，一触即发","background_img":null,"comment_id":"39180","comment":"保温杯，至少不败吧，这种比赛再拿不了玩个球啊。。。","create_time":"1516601357","praise_count":"1","reply_count":"2","view_count":"3428","title":null,"imglist":["http://sinacloud.net/topicimg/qkt_184956_1516601337157335.webp"],"score":"","praise_status":0,"reward_user_count":"0","reward_count":"0","user":{"nickname":"大希解球","headurl":"http://dldemo-img.stor.sinaapp.com/header_1512629882.png","user_id":"184956","verify_type":"3","follow_status":"1"}}
         * list : [{"reply_id":"4091","nickname":"邓玮","headurl":"http://dldemo-img.stor.sinaapp.com/4461515669195.png","user_id":"1","verify_type":"0","create_time":"1516660923","reply":"一大块空白区域啊","praise_count":"0","parent":{"nickname":"邓玮","reply":"你到底发了图片吗？"},"praise_status":0},{"reply_id":"4090","nickname":"邓玮","headurl":"http://dldemo-img.stor.sinaapp.com/4461515669195.png","user_id":"1","verify_type":"0","create_time":"1516660901","reply":"你到底发了图片吗？","praise_count":"0","praise_status":0}]
         * praise_list : [{"user_id":"198147","headurl":"http://dldemo-img.stor.sinaapp.com/header_1506591125.png"}]
         * reward_count_list : [5,10,15,20,50,88]
         */

        public CommentBean comment;
        public List<ListBean> list;
        public List<PraiseListBean> praise_list;
        public List<Integer> reward_count_list;

        public static class CommentBean {
            /**
             * topic_id : 1203
             * topic_title : 伦敦德比战，一触即发
             * background_img : null
             * comment_id : 39180
             * comment : 保温杯，至少不败吧，这种比赛再拿不了玩个球啊。。。
             * create_time : 1516601357
             * praise_count : 1
             * reply_count : 2
             * view_count : 3428
             * title : null
             * imglist : ["http://sinacloud.net/topicimg/qkt_184956_1516601337157335.webp"]
             * score :
             * praise_status : 0
             * reward_user_count : 0
             * reward_count : 0
             * user : {"nickname":"大希解球","headurl":"http://dldemo-img.stor.sinaapp.com/header_1512629882.png","user_id":"184956","verify_type":"3","follow_status":"1"}
             */

            public String topic_id;
            public String topic_title;
            public String background_img;
            public String comment_id;
            public String comment;
            public String create_time;
            public String praise_count;
            public String reply_count;
            public String view_count;
            public String title;
            public String score;
            public int praise_status;
            public String reward_user_count;
            public String reward_count;
            public UserBean user;
            public List<String> imglist;
            public List<SelectionList>  selectionList;
            public List<DynamicBean.ListBean.ImageList> imgListEx;

            public String collection_status;
            public List<DynamicBean.ListBean.ImageList> backgroundList;

            public static class SelectionList{
                public String title;
                public String value;

            }
            public static class UserBean {
                /**
                 * nickname : 大希解球
                 * headurl : http://dldemo-img.stor.sinaapp.com/header_1512629882.png
                 * user_id : 184956
                 * verify_type : 3
                 * follow_status : 1
                 */

                public String nickname;
                public String headurl;
                public String user_id;
                public String verify_type;
                public String follow_status;
            }
        }

        public static class ListBean implements Serializable{
            /**
             * reply_id : 4091
             * nickname : 邓玮
             * headurl : http://dldemo-img.stor.sinaapp.com/4461515669195.png
             * user_id : 1
             * verify_type : 0
             * create_time : 1516660923
             * reply : 一大块空白区域啊
             * praise_count : 0
             * parent : {"nickname":"邓玮","reply":"你到底发了图片吗？"}
             * praise_status : 0
             */

            public String reply_id;
            public String nickname;
            public String headurl;
            public String user_id;
            public String verify_type;
            public String create_time;
            public String reply;
            public String praise_count;

            public String reply_count;//回复评论数

            public ParentBean parent;
            public int praise_status;
            public List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx;

            public static class ParentBean implements Serializable{
                /**
                 * nickname : 邓玮
                 * reply : 你到底发了图片吗？
                 */

                public String nickname;
                public String reply;
                public String reply_id;
                public String user_id;
                public String imglist;
            }
        }

        public static class PraiseListBean {
            /**
             * user_id : 198147
             * headurl : http://dldemo-img.stor.sinaapp.com/header_1506591125.png
             */

            public String user_id;
            public String headurl;
        }
    }
}
