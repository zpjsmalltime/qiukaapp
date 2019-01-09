package com.mayisports.qca.bean;

import java.util.List;

/**
 * 球咖首页顶部tab
 * Created by Zpj on 2018/2/27.
 */

public class MainQcaTabsBean {

    /**
     * status : {"login":0,"id":0}
     * meta : {"default_text":"曼联"}
     * data : {"tag":[["关注",-3,0],["推荐",0,1],["视频",-2,0],["话题",-1,0],["足球",1,0],["竞彩",3,0]],"list":[{"type":3,"comment":{"comment_id":"43837","comment":"就算想走也走不掉的。和拜仁合约到21年。前段时间拜仁刚强调了拜仁会留下最强的球员，当年里贝里都走不了，别说这个了","praise_count":"2","reward_count":"0","reply_count":"0","view_count":"642","title":"","background_img":"","praise_status":0},"user":{"nickname":"中羊纪间伟","headurl":"http://dldemo-img.stor.sinaapp.com/header_1506590890.png","user_id":"198131","verify_type":"0","tag":[""],"follow_status":0},"create_time":"1519706181","topic":{"title":"天空体育：莱万渴望离开拜仁","topic_id":"1252","background_img":"http://dldemo-img.stor.sinaapp.com/topic199381_1519704676.png","user_count":8,"view_count":"905","video_url":""}},{"type":6,"topic":{"title":"天空体育：莱万渴望离开拜仁","id":"1252","background_img":"http://dldemo-img.stor.sinaapp.com/topic199381_1519704676.png","create_time":"1519704677","view_count":"905","user_count":8}},{"type":3,"comment":{"comment_id":"43841","comment":"球员也是人，来来往往很正常。莱万比达尔包括回家的狐媚，有一个算一个，其实加盟拜仁最大的目标还不是欧冠？但这几年确实一直没有拿到，连决赛都没进，自然有想法。","praise_count":"0","reward_count":"0","reply_count":"0","view_count":"484","title":"","background_img":"","praise_status":0},"user":{"nickname":"志山","headurl":"http://dldemo-img.stor.sinaapp.com/header_1506590963.png","user_id":"198134","verify_type":"0","tag":[""],"follow_status":0},"create_time":"1519706526","topic":{"title":"天空体育：莱万渴望离开拜仁","topic_id":"1252","background_img":"http://dldemo-img.stor.sinaapp.com/topic199381_1519704676.png","user_count":8,"view_count":"905","video_url":""}},{"type":3,"comment":{"comment_id":"43846","comment":"拜仁不得不在未来几周缺少科曼，法国人因为韧带受伤成功接受手术。 \u200b","praise_count":"0","reward_count":"0","reply_count":"0","view_count":"53","title":"","background_img":"","praise_status":0},"user":{"nickname":"拜仁大学生联盟","headurl":"http://dldemo-img.stor.sinaapp.com/header_1517906772.png","user_id":"202331","verify_type":"1","tag":[""],"follow_status":0},"create_time":"1519708537"},{"type":3,"comment":{"comment_id":"43786","comment":"【闪电再启动】南非俱乐部马姆罗迪日落队官宣签下博尔特\n#曼联那些事# \u200b","praise_count":"2","reward_count":"0","reply_count":"1","view_count":"7515","title":"","background_img":"","praise_status":0,"imgListEx":[{"url":"http://sinacloud.net/topicimg/sqkt_2023481519689617.jpg","type":"img","size":"733,412"}]},"user":{"nickname":"曼联球迷频道","headurl":"http://dldemo-img.stor.sinaapp.com/2381517996949.png","user_id":"202348","verify_type":"3","tag":[""],"follow_status":0},"create_time":"1519689210"}]}
     */

    public StatusBean status;
    public MetaBean meta;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 0
         * id : 0
         */

        public int login;
        public String id;
    }

    public static class MetaBean {
        /**
         * default_text : 曼联
         */

        public String default_text;
    }

    public static class DataBean {
        public List<List<String>> tag;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * type : 3
             * comment : {"comment_id":"43837","comment":"就算想走也走不掉的。和拜仁合约到21年。前段时间拜仁刚强调了拜仁会留下最强的球员，当年里贝里都走不了，别说这个了","praise_count":"2","reward_count":"0","reply_count":"0","view_count":"642","title":"","background_img":"","praise_status":0}
             * user : {"nickname":"中羊纪间伟","headurl":"http://dldemo-img.stor.sinaapp.com/header_1506590890.png","user_id":"198131","verify_type":"0","tag":[""],"follow_status":0}
             * create_time : 1519706181
             * topic : {"title":"天空体育：莱万渴望离开拜仁","topic_id":"1252","background_img":"http://dldemo-img.stor.sinaapp.com/topic199381_1519704676.png","user_count":8,"view_count":"905","video_url":""}
             */

            public int type;
            public CommentBean comment;
            public UserBean user;
            public String create_time;
            public TopicBean topic;

            public static class CommentBean {
                /**
                 * comment_id : 43837
                 * comment : 就算想走也走不掉的。和拜仁合约到21年。前段时间拜仁刚强调了拜仁会留下最强的球员，当年里贝里都走不了，别说这个了
                 * praise_count : 2
                 * reward_count : 0
                 * reply_count : 0
                 * view_count : 642
                 * title :
                 * background_img :
                 * praise_status : 0
                 */

                public String comment_id;
                public String comment;
                public String praise_count;
                public String reward_count;
                public String reply_count;
                public String view_count;
                public String title;
                public String background_img;
                public int praise_status;
            }

            public static class UserBean {
                /**
                 * nickname : 中羊纪间伟
                 * headurl : http://dldemo-img.stor.sinaapp.com/header_1506590890.png
                 * user_id : 198131
                 * verify_type : 0
                 * tag : [""]
                 * follow_status : 0
                 */

                public String nickname;
                public String headurl;
                public String user_id;
                public String verify_type;
                public int follow_status;
                public List<String> tag;
            }

            public static class TopicBean {
                /**
                 * title : 天空体育：莱万渴望离开拜仁
                 * topic_id : 1252
                 * background_img : http://dldemo-img.stor.sinaapp.com/topic199381_1519704676.png
                 * user_count : 8
                 * view_count : 905
                 * video_url :
                 */

                public String title;
                public String topic_id;
                public String background_img;
                public int user_count;
                public String view_count;
                public String video_url;
            }
        }
    }
}
