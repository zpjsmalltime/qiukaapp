package com.mayisports.qca.bean;

import java.util.List;

/**
 * 发布推荐请求联赛比赛队bean
 * Created by Zpj on 2018/1/24.
 */

public class PublishGroomTeamBean {

    /**
     * data : {"matchList":[{"match":{"betId":"1395979","timezoneoffset":"1516822200","leagueId":"17","leagueName":"%E6%AF%94%E7%94%B2","round":"23","season":"2017","hostTeamName":"%E5%AE%89%E7%89%B9%E5%8D%AB%E6%99%AE","hostTeamId":"1449","awayTeamName":"%E6%A0%B9%E7%89%B9","awayTeamId":"165","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"5","awayRank":"4","status":"NO_START","cup":"0","color":"#FC9B0A","tape":"-2500","logoH":"1449","logoG":"165","tvlink":1,"tvlinkList":["体育直播"]},"recommendation_status":0},{"match":{"betId":"1395978","timezoneoffset":"1516822200","leagueId":"17","leagueName":"%E6%AF%94%E7%94%B2","round":"23","season":"2017","hostTeamName":"%E6%B4%9B%E5%85%8B%E4%BC%A6","hostTeamId":"169","awayTeamName":"%E4%BA%A8%E5%85%8B","awayTeamId":"171","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"14","awayRank":"10","status":"NO_START","cup":"0","color":"#FC9B0A","tape":"-5000","logoH":"169","logoG":"171","tvlink":1,"tvlinkList":["体育直播"]},"recommendation_status":0},{"match":{"betId":"1395976","timezoneoffset":"1516822200","leagueId":"17","leagueName":"%E6%AF%94%E7%94%B2","round":"23","season":"2017","hostTeamName":"%E6%AC%A7%E6%9C%AC","hostTeamId":"1147","awayTeamName":"%E6%B2%99%E5%8B%92%E7%BD%97%E7%93%A6","awayTeamId":"147","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"16","awayRank":"2","status":"NO_START","cup":"0","color":"#FC9B0A","tape":"-2500","logoH":"1147","logoG":"147","tvlink":1,"tvlinkList":["体育直播"]},"recommendation_status":0},{"match":{"betId":"1395974","timezoneoffset":"1516822200","leagueId":"17","leagueName":"%E6%AF%94%E7%94%B2","round":"23","season":"2017","hostTeamName":"%E5%AE%89%E5%BE%B7%E8%8E%B1%E8%B5%AB","hostTeamId":"177","awayTeamName":"%E8%B4%9D%E5%BC%97%E4%BC%A6","awayTeamId":"1080","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"9","status":"NO_START","cup":"0","color":"#FC9B0A","tape":"12500","logoH":"177","logoG":"1080","tvlink":1,"tvlinkList":["互动图文直播"]},"recommendation_status":0},{"match":{"betId":"1395973","timezoneoffset":"1516908600","leagueId":"17","leagueName":"%E6%AF%94%E7%94%B2","round":"23","season":"2017","hostTeamName":"%E5%B8%83%E9%B2%81%E6%97%A5","hostTeamId":"172","awayTeamName":"%E5%A5%A5%E6%96%AF%E5%9D%A6%E5%BE%B7","awayTeamId":"193","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"1","awayRank":"12","status":"NO_START","cup":"0","color":"#FC9B0A","tape":"12500","logoH":"172","logoG":"193"},"recommendation_status":0}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<MatchListBean> matchList;

        public static class MatchListBean {
            /**
             * match : {"betId":"1395979","timezoneoffset":"1516822200","leagueId":"17","leagueName":"%E6%AF%94%E7%94%B2","round":"23","season":"2017","hostTeamName":"%E5%AE%89%E7%89%B9%E5%8D%AB%E6%99%AE","hostTeamId":"1449","awayTeamName":"%E6%A0%B9%E7%89%B9","awayTeamId":"165","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"5","awayRank":"4","status":"NO_START","cup":"0","color":"#FC9B0A","tape":"-2500","logoH":"1449","logoG":"165","tvlink":1,"tvlinkList":["体育直播"]}
             * recommendation_status : 0
             */

            public MatchBean match;
            public int recommendation_status;

            public static class MatchBean {
                /**
                 * betId : 1395979
                 * timezoneoffset : 1516822200
                 * leagueId : 17
                 * leagueName : %E6%AF%94%E7%94%B2
                 * round : 23
                 * season : 2017
                 * hostTeamName : %E5%AE%89%E7%89%B9%E5%8D%AB%E6%99%AE
                 * hostTeamId : 1449
                 * awayTeamName : %E6%A0%B9%E7%89%B9
                 * awayTeamId : 165
                 * hostScore : 0
                 * awayScore : 0
                 * hostHalfScore : 0
                 * awayHalfScore : 0
                 * hostRank : 5
                 * awayRank : 4
                 * status : NO_START
                 * cup : 0
                 * color : #FC9B0A
                 * tape : -2500
                 * logoH : 1449
                 * logoG : 165
                 * tvlink : 1
                 * tvlinkList : ["体育直播"]
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
                public int tvlink;
                public List<String> tvlinkList;
            }
        }
    }
}
