package com.mayisports.qca.bean;

/**
 * 发布推荐成功bean
 * Created by Zpj on 2018/1/30.
 */

public class PublishSuccessBean {


    /**
     * status : {"login":1,"sql":"select betDetail from `user_recommendation` where user_id=71640 and create_time>1517202000"}
     * data : {"cur_user_id":"71640","cur_user_type":"0","match":{"hostTeamId":"225","round":"21","isCup":"0","awayTeamId":"278","betId":"1430120","hostTeamName":"朗斯","awayTeamName":"索肖","timezoneoffset":"1517335200","leagueName":"法乙","HLogoId":0,"ALogoId":0},"odds":{"asia":{"hostOdds":"1.09","awayOdds":"0.84","tape":"5000"},"score":{"hostOdds":"10000","awayOdds":"9000","tape":"25000"}},"type":"complete","recommendation":{"price":"0","reason":"","betDetail":{"winOdds":"20000","tape":""},"user_id":"71640"},"standard_price":0}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 1
         * sql : select betDetail from `user_recommendation` where user_id=71640 and create_time>1517202000
         */

        public int login;
        public String sql;
    }

    public static class DataBean {
        /**
         * cur_user_id : 71640
         * cur_user_type : 0
         * match : {"hostTeamId":"225","round":"21","isCup":"0","awayTeamId":"278","betId":"1430120","hostTeamName":"朗斯","awayTeamName":"索肖","timezoneoffset":"1517335200","leagueName":"法乙","HLogoId":0,"ALogoId":0}
         * odds : {"asia":{"hostOdds":"1.09","awayOdds":"0.84","tape":"5000"},"score":{"hostOdds":"10000","awayOdds":"9000","tape":"25000"}}
         * type : complete
         * recommendation : {"price":"0","reason":"","betDetail":{"winOdds":"20000","tape":""},"user_id":"71640"}
         * standard_price : 0
         */

        public String cur_user_id;
        public String cur_user_type;
        public MatchBean match;
        public OddsBean odds;
        public String type;
        public RecommendationBean recommendation;
        public int standard_price;

        public static class MatchBean {
            /**
             * hostTeamId : 225
             * round : 21
             * isCup : 0
             * awayTeamId : 278
             * betId : 1430120
             * hostTeamName : 朗斯
             * awayTeamName : 索肖
             * timezoneoffset : 1517335200
             * leagueName : 法乙
             * HLogoId : 0
             * ALogoId : 0
             */

            public String hostTeamId;
            public String round;
            public String isCup;
            public String awayTeamId;
            public String betId;
            public String hostTeamName;
            public String awayTeamName;
            public String timezoneoffset;
            public String leagueName;
            public int HLogoId;
            public int ALogoId;
        }

        public static class OddsBean {
            /**
             * asia : {"hostOdds":"1.09","awayOdds":"0.84","tape":"5000"}
             * score : {"hostOdds":"10000","awayOdds":"9000","tape":"25000"}
             */

            public AsiaBean asia;
            public ScoreBean score;

            public static class AsiaBean {
                /**
                 * hostOdds : 1.09
                 * awayOdds : 0.84
                 * tape : 5000
                 */

                public String hostOdds;
                public String awayOdds;
                public String tape;
            }

            public static class ScoreBean {
                /**
                 * hostOdds : 10000
                 * awayOdds : 9000
                 * tape : 25000
                 */

                public String hostOdds;
                public String awayOdds;
                public String tape;
            }
        }

        public static class RecommendationBean {
            /**
             * price : 0
             * reason :
             * betDetail : {"winOdds":"20000","tape":""}
             * user_id : 71640
             */

            public String price;
            public String reason;
            public BetDetailBean betDetail;
            public String user_id;

            public static class BetDetailBean {
                /**
                 * winOdds : 20000
                 * tape :
                 */

                public String winOdds;
                public String tape;
                ////'hostOdds' 'awayOdds' 'winOdds' 'drowOdds' 'loseOdds' 'smallScoreOdds' 'bigScoreOdds'
                public String hostOdds;
                public String awayOdds;
                public String drowOdds;
                public String loseOdds;
                public String smallScoreOdds;
                public String bigScoreOdds;
            }


        }
    }
}
