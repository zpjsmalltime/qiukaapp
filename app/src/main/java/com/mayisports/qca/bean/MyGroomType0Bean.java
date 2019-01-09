package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2018/2/9.
 */

public class MyGroomType0Bean {

    /**
     * status : {"login":0,"id":"71640"}
     * data : [{"match":{"hostTeamName":"比勒费尔","awayTeamName":"柏林联合","leagueName":"德乙","timezoneoffset":"1517859000","hostScore":"1","awayScore":"1","status":"COMPLETE","betId":"1407102"},"recommendation":{"betDetail":{"hostOdds":"9400","tape":"0"},"revenueStr":"无效","create_time":"1517832857","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"2"},"id":"941388","user":{"user_id":"71640"}},{"match":{"hostTeamName":"阿尔克青","awayTeamName":"奥斯","leagueName":"荷乙","timezoneoffset":"1517857200","hostScore":"3","awayScore":"1","status":"COMPLETE","betId":"1402018"},"recommendation":{"betDetail":{"awayOdds":"8500","tape":"5000"},"revenueStr":"输","create_time":"1517832835","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"941386","user":{"user_id":"71640"}},{"match":{"hostTeamName":"阿贾克斯B","awayTeamName":"赫尔蒙特","leagueName":"荷乙","timezoneoffset":"1517857200","hostScore":"2","awayScore":"0","status":"COMPLETE","betId":"1402017"},"recommendation":{"betDetail":{"bigScoreOdds":"8900","tape":"35000"},"revenueStr":"输","create_time":"1517832791","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"941384","user":{"user_id":"71640"}},{"match":{"hostTeamName":"帕德博恩","awayTeamName":"拜仁","leagueName":"德国杯","timezoneoffset":"1517938200","hostScore":"0","awayScore":"6","status":"COMPLETE","betId":"1492027"},"recommendation":{"betDetail":{"hostOdds":"11399.999999999998","tape":"-22500"},"revenueStr":"输","create_time":"1517832717","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"941383","user":{"user_id":"71640"}},{"match":{"hostTeamName":"沃特福德","awayTeamName":"切尔西","leagueName":"英超","timezoneoffset":"1517860800","hostScore":"4","awayScore":"1","status":"COMPLETE","betId":"1395337"},"recommendation":{"betDetail":{"awayOdds":"11000","tape":"-10000"},"revenueStr":"输","create_time":"1517832665","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"941381","user":{"user_id":"71640"}},{"match":{"hostTeamName":"科隆","awayTeamName":"多特蒙德","leagueName":"德甲","timezoneoffset":"1517599800","hostScore":"2","awayScore":"3","status":"COMPLETE","betId":"1406791"},"recommendation":{"betDetail":{"hostOdds":"11200.000000000002","tape":"-5000"},"revenueStr":"输","create_time":"1517562345","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"932453","user":{"user_id":"71640"}},{"match":{"hostTeamName":"美因茨","awayTeamName":"拜仁","leagueName":"德甲","timezoneoffset":"1517668200","hostScore":"0","awayScore":"2","status":"COMPLETE","betId":"1406788"},"recommendation":{"betDetail":{"hostOdds":"8900","tape":"-17500"},"revenueStr":"1/2输 1/2无效","create_time":"1517562264","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"932452","user":{"user_id":"71640"}},{"match":{"hostTeamName":"伯恩利","awayTeamName":"曼城","leagueName":"英超","timezoneoffset":"1517661000","hostScore":"1","awayScore":"1","status":"COMPLETE","betId":"1395332"},"recommendation":{"betDetail":{"hostOdds":"8800","tape":"-17500"},"revenueStr":"赢","create_time":"1517562068","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"932444","user":{"user_id":"71640"}},{"match":{"hostTeamName":"帕尔斯布什尔","awayTeamName":"塞柏","leagueName":"伊朗超","timezoneoffset":"1517579100","hostScore":"0","awayScore":"0","status":"COMPLETE","betId":"1424268"},"recommendation":{"betDetail":{"hostOdds":"10900","tape":"2500"},"revenueStr":"1/2输 1/2无效","create_time":"1517558110","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"932350","user":{"user_id":"71640"}},{"match":{"hostTeamName":"波利斯","awayTeamName":"塞帕汉","leagueName":"伊朗超","timezoneoffset":"1517574000","hostScore":"2","awayScore":"0","status":"COMPLETE","betId":"1424269"},"recommendation":{"betDetail":{"awayOdds":"7000","tape":"10000"},"revenueStr":"输","create_time":"1517555433","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"0"},"id":"932274","user":{"user_id":"71640"}}]
     */

    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        /**
         * login : 0
         * id : 71640
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        /**
         * match : {"hostTeamName":"比勒费尔","awayTeamName":"柏林联合","leagueName":"德乙","timezoneoffset":"1517859000","hostScore":"1","awayScore":"1","status":"COMPLETE","betId":"1407102"}
         * recommendation : {"betDetail":{"hostOdds":"9400","tape":"0"},"revenueStr":"无效","create_time":"1517832857","price":"0","sales":"0","return_if_wrong":"0","revenue":0,"display_by_share":"2"}
         * id : 941388
         * user : {"user_id":"71640"}
         */

        public MatchBean match;
        public RecommendationBean recommendation;
        public String id;
        public UserBean user;

        public static class MatchBean {
            /**
             * hostTeamName : 比勒费尔
             * awayTeamName : 柏林联合
             * leagueName : 德乙
             * timezoneoffset : 1517859000
             * hostScore : 1
             * awayScore : 1
             * status : COMPLETE
             * betId : 1407102
             */

            public String hostTeamName;
            public String awayTeamName;
            public String leagueName;
            public String timezoneoffset;
            public String hostScore;
            public String awayScore;
            public String status;
            public String betId;
        }

        public static class RecommendationBean {
            /**
             * betDetail : {"hostOdds":"9400","tape":"0"}
             * revenueStr : 无效
             * create_time : 1517832857
             * price : 0
             * sales : 0
             * return_if_wrong : 0
             * revenue : 0
             * display_by_share : 2
             */

            public BetDetailBean betDetail;
            public String revenueStr;
            public String create_time;
            public String price;
            public String sales;
            public String return_if_wrong;
            public int revenue;
            public String display_by_share;

            public static class BetDetailBean {
                /**
                 * hostOdds : 9400
                 * tape : 0
                 */



                public String tape;
                ////'hostOdds' 'awayOdds' 'winOdds' 'drowOdds' 'loseOdds' 'smallScoreOdds' 'bigScoreOdds'
                public String hostOdds;
                public String awayOdds;

                public String winOdds;
                public String drowOdds;
                public String loseOdds;

                public String smallScoreOdds;
                public String bigScoreOdds;

            }
        }

        public static class UserBean {
            /**
             * user_id : 71640
             */

            public String user_id;
        }
    }
}
