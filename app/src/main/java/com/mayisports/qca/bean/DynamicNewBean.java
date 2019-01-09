package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2018/1/12.
 */

public class DynamicNewBean {


    /**
     * status : {"login":0,"id":"200327"}
     * list : [{"type":3,"id":"90945","comment":{"comment_id":"38396","comment":"123498563","praise_count":"0","reward_count":"0","reply_count":"0","praise_status":0},"user":{"nickname":"规律","headurl":"http://dldemo-img.stor.sinaapp.com/761515661319.png","user_id":"200327","verify_type":"0","tag":[""],"follow_status":0},"create_time":"1515732138"},{"type":1,"id":"90728","user":{"nickname":"李玟娥呀","headurl":"http://dldemo-img.stor.sinaapp.com/7631515639337.png","user_id":"200331","verify_type":"0","tag":[""],"follow_status":1,"tag1":[]},"create_time":"1515723491","recommendation":{"type":"亚盘","title":"","buy":0,"price":"18","reason_len":0,"return_if_wrong":"0"},"match":{"betId":"1487284","hostTeamName":"中国U23","awayTeamName":"乌兹别克U23","leagueName":"亚青U23","match_time":"1515744000","status":"NO_START"},"count":"2"}]
     * data : {"list":[{"type":3,"id":"90945","comment":{"comment_id":"38396","comment":"123498563","praise_count":"0","reward_count":"0","reply_count":"0","praise_status":0},"user":{"nickname":"规律","headurl":"http://dldemo-img.stor.sinaapp.com/761515661319.png","user_id":"200327","verify_type":"0","tag":[""],"follow_status":0},"create_time":"1515732138"},{"type":1,"id":"90728","user":{"nickname":"李玟娥呀","headurl":"http://dldemo-img.stor.sinaapp.com/7631515639337.png","user_id":"200331","verify_type":"0","tag":[""],"follow_status":1,"tag1":[]},"create_time":"1515723491","recommendation":{"type":"亚盘","title":"","buy":0,"price":"18","reason_len":0,"return_if_wrong":"0"},"match":{"betId":"1487284","hostTeamName":"中国U23","awayTeamName":"乌兹别克U23","leagueName":"亚青U23","match_time":"1515744000","status":"NO_START"},"count":"2"}]}
     */

    public StatusBean status;
    public DataBean data;
    public List<ListBeanX> list;

    public static class StatusBean {
        /**
         * login : 0
         * id : 200327
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        public List<ListBean> list;

        public static class ListBean {
            /**
             * type : 3
             * id : 90945
             * comment : {"comment_id":"38396","comment":"123498563","praise_count":"0","reward_count":"0","reply_count":"0","praise_status":0}
             * user : {"nickname":"规律","headurl":"http://dldemo-img.stor.sinaapp.com/761515661319.png","user_id":"200327","verify_type":"0","tag":[""],"follow_status":0}
             * create_time : 1515732138
             * recommendation : {"type":"亚盘","title":"","buy":0,"price":"18","reason_len":0,"return_if_wrong":"0"}
             * match : {"betId":"1487284","hostTeamName":"中国U23","awayTeamName":"乌兹别克U23","leagueName":"亚青U23","match_time":"1515744000","status":"NO_START"}
             * count : 2
             */

            public int type;
            public String id;
            public CommentBean comment;
            public UserBean user;
            public String create_time;
            public RecommendationBean recommendation;
            public MatchBean match;
            public String count;

            public static class CommentBean {
                /**
                 * comment_id : 38396
                 * comment : 123498563
                 * praise_count : 0
                 * reward_count : 0
                 * reply_count : 0
                 * praise_status : 0
                 */

                public String comment_id;
                public String comment;
                public String praise_count;
                public String reward_count;
                public String reply_count;
                public int praise_status;
            }

            public static class UserBean {
                /**
                 * nickname : 规律
                 * headurl : http://dldemo-img.stor.sinaapp.com/761515661319.png
                 * user_id : 200327
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

            public static class RecommendationBean {
                /**
                 * type : 亚盘
                 * title :
                 * buy : 0
                 * price : 18
                 * reason_len : 0
                 * return_if_wrong : 0
                 */

                public String type;
                public String title;
                public int buy;
                public String price;
                public int reason_len;
                public String return_if_wrong;
            }

            public static class MatchBean {
                /**
                 * betId : 1487284
                 * hostTeamName : 中国U23
                 * awayTeamName : 乌兹别克U23
                 * leagueName : 亚青U23
                 * match_time : 1515744000
                 * status : NO_START
                 */

                public String betId;
                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public String match_time;
                public String status;
            }
        }
    }

    public static class ListBeanX {
        /**
         * type : 3
         * id : 90945
         * comment : {"comment_id":"38396","comment":"123498563","praise_count":"0","reward_count":"0","reply_count":"0","praise_status":0}
         * user : {"nickname":"规律","headurl":"http://dldemo-img.stor.sinaapp.com/761515661319.png","user_id":"200327","verify_type":"0","tag":[""],"follow_status":0}
         * create_time : 1515732138
         * recommendation : {"type":"亚盘","title":"","buy":0,"price":"18","reason_len":0,"return_if_wrong":"0"}
         * match : {"betId":"1487284","hostTeamName":"中国U23","awayTeamName":"乌兹别克U23","leagueName":"亚青U23","match_time":"1515744000","status":"NO_START"}
         * count : 2
         */

        public int type;
        public String id;
        public CommentBeanX comment;
        public UserBeanX user;
        public String create_time;
        public RecommendationBeanX recommendation;
        public MatchBeanX match;
        public String count;

        public static class CommentBeanX {
            /**
             * comment_id : 38396
             * comment : 123498563
             * praise_count : 0
             * reward_count : 0
             * reply_count : 0
             * praise_status : 0
             */

            public String comment_id;
            public String comment;
            public String praise_count;
            public String reward_count;
            public String reply_count;
            public int praise_status;
        }

        public static class UserBeanX {
            /**
             * nickname : 规律
             * headurl : http://dldemo-img.stor.sinaapp.com/761515661319.png
             * user_id : 200327
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

        public static class RecommendationBeanX {
            /**
             * type : 亚盘
             * title :
             * buy : 0
             * price : 18
             * reason_len : 0
             * return_if_wrong : 0
             */

            public String type;
            public String title;
            public int buy;
            public String price;
            public int reason_len;
            public String return_if_wrong;
        }

        public static class MatchBeanX {
            /**
             * betId : 1487284
             * hostTeamName : 中国U23
             * awayTeamName : 乌兹别克U23
             * leagueName : 亚青U23
             * match_time : 1515744000
             * status : NO_START
             */

            public String betId;
            public String hostTeamName;
            public String awayTeamName;
            public String leagueName;
            public String match_time;
            public String status;
        }
    }
}
