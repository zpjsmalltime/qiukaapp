package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2017/12/14.
 */

public class HomeItemBean {

    /**
     * status : {"login":1,"reqtype":"req","user_id":"71640"}
     * data : {"match":{"betId":"1395245","timezoneoffset":"1513194300","leagueId":"43","leagueName":"%E8%8B%B1%E8%B6%85","round":"17","season":"2017","hostTeamName":"%E7%BA%BD%E5%8D%A1%E6%96%AF%E5%B0%94","hostTeamId":"56","awayTeamName":"%E5%9F%83%E5%BC%97%E9%A1%BF","awayTeamId":"59","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"16","awayRank":"10","status":"COMPLETE","cup":"0","color":"#FF3333","tape":"2500","logoH":"56","logoG":"59","fav_status":"1"},"user":{"verify_type":"3","headurl":"http://dldemo-img.stor.sinaapp.com/1241507470311.png","nickname":"芭蕉足球","user_type":"2","user_id":"198355","tag":["分析师"],"tag1":["近20中15"],"reward_count_list":[1,8,18,28,88,188],"view_count":"236","followers_count":"4","praise_status":0,"coin":"0.5","praise_count":"1","follow_status":0},"revenueStr":"输","recommendation":{"create_time":"1513181066","bet":{"hostOdds":"10100","tape":"2500"},"title":"纽卡斯尔vs埃弗顿：娱乐即可","status":0,"return_if_wrong":0,"buy":0},"odds":{"asia":{"hostOdds":1.01,"awayOdds":"1.08","tape":"2500"}},"reason":"今晚没有找到比较适合投资的比赛。我选择这场下注一天饭钱就OK了，买个盼头，做个好梦。"}
     * type : result
     */

    public StatusBean status;
    public DataBean data;
    public String type;

    public static class StatusBean {
        /**
         * login : 1
         * reqtype : req
         * user_id : 71640
         */

        public int login;
        public String reqtype;
        public String user_id;
    }

    public static class DataBean {
        /**
         * match : {"betId":"1395245","timezoneoffset":"1513194300","leagueId":"43","leagueName":"%E8%8B%B1%E8%B6%85","round":"17","season":"2017","hostTeamName":"%E7%BA%BD%E5%8D%A1%E6%96%AF%E5%B0%94","hostTeamId":"56","awayTeamName":"%E5%9F%83%E5%BC%97%E9%A1%BF","awayTeamId":"59","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"16","awayRank":"10","status":"COMPLETE","cup":"0","color":"#FF3333","tape":"2500","logoH":"56","logoG":"59","fav_status":"1"}
         * user : {"verify_type":"3","headurl":"http://dldemo-img.stor.sinaapp.com/1241507470311.png","nickname":"芭蕉足球","user_type":"2","user_id":"198355","tag":["分析师"],"tag1":["近20中15"],"reward_count_list":[1,8,18,28,88,188],"view_count":"236","followers_count":"4","praise_status":0,"coin":"0.5","praise_count":"1","follow_status":0}
         * revenueStr : 输
         * recommendation : {"create_time":"1513181066","bet":{"hostOdds":"10100","tape":"2500"},"title":"纽卡斯尔vs埃弗顿：娱乐即可","status":0,"return_if_wrong":0,"buy":0}
         * odds : {"asia":{"hostOdds":1.01,"awayOdds":"1.08","tape":"2500"}}
         * reason : 今晚没有找到比较适合投资的比赛。我选择这场下注一天饭钱就OK了，买个盼头，做个好梦。
         */

        public MatchBean match;
        public UserBean user;
        public String revenueStr;
        public RecommendationBean recommendation;
        public OddsBean odds;
        public String reason;

        public static class MatchBean {
            /**
             * betId : 1395245
             * timezoneoffset : 1513194300
             * leagueId : 43
             * leagueName : %E8%8B%B1%E8%B6%85
             * round : 17
             * season : 2017
             * hostTeamName : %E7%BA%BD%E5%8D%A1%E6%96%AF%E5%B0%94
             * hostTeamId : 56
             * awayTeamName : %E5%9F%83%E5%BC%97%E9%A1%BF
             * awayTeamId : 59
             * hostScore : 0
             * awayScore : 1
             * hostHalfScore : 0
             * awayHalfScore : 1
             * hostRank : 16
             * awayRank : 10
             * status : COMPLETE
             * cup : 0
             * color : #FF3333
             * tape : 2500
             * logoH : 56
             * logoG : 59
             * fav_status : 1
             */

            public String betId;
            public String timezoneoffset;
            public String leagueId;
            public String leagueName;
            public String round;
            public String season;
            public String hostTeamName;
            public String hostTeamId;
            public String awayTeamName;
            public String awayTeamId;
            public String hostScore;
            public String awayScore;
            public String hostHalfScore;
            public String awayHalfScore;
            public String hostRank;
            public String awayRank;
            public String status;
            public String cup;
            public String color;
            public String tape;
            public String logoH;
            public String logoG;
            public String fav_status;
        }

        public static class UserBean {
            /**
             * verify_type : 3
             * headurl : http://dldemo-img.stor.sinaapp.com/1241507470311.png
             * nickname : 芭蕉足球
             * user_type : 2
             * user_id : 198355
             * tag : ["分析师"]
             * tag1 : ["近20中15"]
             * reward_count_list : [1,8,18,28,88,188]
             * view_count : 236
             * followers_count : 4
             * praise_status : 0
             * coin : 0.5
             * praise_count : 1
             * follow_status : 0
             */

            public String verify_type;
            public String headurl;
            public String nickname;
            public String user_type;
            public String user_id;
            public String view_count;
            public String followers_count;
            public int praise_status;
            public String coin;
            public String praise_count;
            public int follow_status;
            public List<String> tag;
            public List<String> tag1;
            public List<Integer> reward_count_list;
            public String type;
        }

        public static class RecommendationBean {
            /**
             * create_time : 1513181066
             * bet : {"hostOdds":"10100","tape":"2500"}
             * title : 纽卡斯尔vs埃弗顿：娱乐即可
             * status : 0
             * return_if_wrong : 0
             * buy : 0
             */

            public String create_time;
            public BetBean bet;
            public String title;
            public int status;
            public int return_if_wrong;
            public int buy;

            public String id;
            public String collection_status;

            public static class BetBean {
                /**
                 * hostOdds : 10100
                 * tape : 2500
                 */

                public String hostOdds;
                public String tape;
                public String awayOdds;
                public String winOdds;
                public String drowOdds;
                public String loseOdds;
                public String smallScoreOdds;
                public String bigScoreOdds;
            }
        }

        public static class OddsBean {
            /**
             * asia : {"hostOdds":1.01,"awayOdds":"1.08","tape":"2500"}
             */

            public AsiaBean asia;

            public static class AsiaBean {
                /**
                 * hostOdds : 1.01
                 * awayOdds : 1.08
                 * tape : 2500
                 */

                public double hostOdds;
                public String awayOdds;
                public String tape;
            }
            public ScoreBean score;

            public static class ScoreBean {
                /**
                 * hostOdds : 1.01
                 * awayOdds : 1.08
                 * tape : 2500
                 */

                public String hostOdds;
                public String awayOdds;
                public double bigScoreOdds;
                public String tape;
            }

            public EuropeOddsBean europeOdds;

            public static class EuropeOddsBean {
                /**
                 * hostOdds : 1.01
                 * awayOdds : 1.08
                 * tape : 2500
                 */

                public int drowOdds;
                public String loseOdds;
                public int winOdds;
            }
        }
    }
}
