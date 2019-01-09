package com.mayisports.qca.bean;

/**
 * 编辑推荐bean
 * Created by Zpj on 2018/1/24.
 */

public class EditGroomBean {

    /**
     * status : {"login":1,"sql":"select betDetail from `user_recommendation` where user_id=200327 and create_time>1516597200"}
     * data : {"cur_user_id":"200327","cur_user_type":"0","match":{"hostTeamId":"202","round":"3","isCup":"0","awayTeamId":"197","betId":"1432784","hostTeamName":"桑普利亚","awayTeamName":"罗马","timezoneoffset":"1516823100","leagueName":"意甲","HLogoId":0,"ALogoId":0},"odds":{"asia":{"hostOdds":"0.84","awayOdds":"1.12","tape":"-5000"},"europeOdds":{"winOdds":33000,"drowOdds":36000,"loseOdds":20500},"score":{"hostOdds":"9100","awayOdds":"9900","tape":"27500"}},"type":"edit","return_if_wrong":1,"standard_price":18}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 1
         * sql : select betDetail from `user_recommendation` where user_id=200327 and create_time>1516597200
         */

        public int login;
        public String sql;
    }

    public static class DataBean {
        /**
         * cur_user_id : 200327
         * cur_user_type : 0
         * match : {"hostTeamId":"202","round":"3","isCup":"0","awayTeamId":"197","betId":"1432784","hostTeamName":"桑普利亚","awayTeamName":"罗马","timezoneoffset":"1516823100","leagueName":"意甲","HLogoId":0,"ALogoId":0}
         * odds : {"asia":{"hostOdds":"0.84","awayOdds":"1.12","tape":"-5000"},"europeOdds":{"winOdds":33000,"drowOdds":36000,"loseOdds":20500},"score":{"hostOdds":"9100","awayOdds":"9900","tape":"27500"}}
         * type : edit
         * return_if_wrong : 1
         * standard_price : 18
         */

        public String cur_user_id;
        public String cur_user_type;
        public MatchBean match;
        public OddsBean odds;
        public String type;
        public int return_if_wrong;
        public int standard_price;

        public static class MatchBean {
            /**
             * hostTeamId : 202
             * round : 3
             * isCup : 0
             * awayTeamId : 197
             * betId : 1432784
             * hostTeamName : 桑普利亚
             * awayTeamName : 罗马
             * timezoneoffset : 1516823100
             * leagueName : 意甲
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
             * asia : {"hostOdds":"0.84","awayOdds":"1.12","tape":"-5000"}
             * europeOdds : {"winOdds":33000,"drowOdds":36000,"loseOdds":20500}
             * score : {"hostOdds":"9100","awayOdds":"9900","tape":"27500"}
             */

            public AsiaBean asia;
            public EuropeOddsBean europeOdds;
            public ScoreBean score;

            public static class AsiaBean {
                /**
                 * hostOdds : 0.84
                 * awayOdds : 1.12
                 * tape : -5000
                 */

                public double hostOdds;
                public double awayOdds;
                public String tape;
            }

            public static class EuropeOddsBean {
                /**
                 * winOdds : 33000
                 * drowOdds : 36000
                 * loseOdds : 20500
                 */

                public int winOdds;
                public int drowOdds;
                public int loseOdds;
            }

            public static class ScoreBean {
                /**
                 * hostOdds : 9100
                 * awayOdds : 9900
                 * tape : 27500
                 */

                public String hostOdds;
                public String awayOdds;
                public String tape;
            }
        }
    }
}
