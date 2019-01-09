package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2017/12/13.
 */

public class ScoreStarBean {

    /**
     * status : {"login":1,"errono":0,"errostr":""}
     * data : {"matchlist":[{"match":{"betId":"1405283","timezoneoffset":"1513173600","leagueId":"110","leagueName":"%E5%A1%9E%E5%B0%94%E8%B6%85","round":"22","season":"2017","hostTeamName":"%E8%8B%8F%E6%9D%9C%E5%88%A9%E5%AF%9F","hostTeamId":"21205","awayTeamName":"%E7%91%9E%E5%BE%B7%E5%B0%BC%E5%9F%BA","awayTeamId":"895","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"4","status":"NO_START","cup":"0","color":"#996600","tape":"-7500","logoH":0,"logoG":"895","collection_status":1}},{"match":{"betId":"1405282","timezoneoffset":"1513173600","leagueId":"110","leagueName":"%E5%A1%9E%E5%B0%94%E8%B6%85","round":"22","season":"2017","hostTeamName":"%E8%90%A8%E5%8D%9A%E8%BF%AA%E5%8D%A1","hostTeamId":"9796","awayTeamName":"%E6%B2%83%E4%BC%8A%E6%B2%83%E8%BF%AA%E7%BA%B3","awayTeamId":"896","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"7","status":"NO_START","cup":"0","color":"#996600","tape":"5000","logoH":0,"logoG":"896","collection_status":1}},{"match":{"betId":"1483295","timezoneoffset":"1512901800","leagueId":"772","leagueName":"%E4%BC%8A%E6%9C%97%E7%94%B2","round":"17","season":"2017","hostTeamName":"%E5%B7%B4%E6%A0%BC","hostTeamId":"2859","awayTeamName":"%E5%BA%93%E5%86%85%E5%B7%B4%E5%8D%9A%E5%8B%92","awayTeamId":"25315","hostScore":"2","awayScore":"2","hostHalfScore":"2","awayHalfScore":"0","hostRank":"3","awayRank":"4","status":"COMPLETE","cup":"0","color":"#4999bb","tape":"2500","logoH":0,"logoG":0,"collection_status":1}},{"match":{"betId":"1419323","timezoneoffset":"1512900000","leagueId":"111","leagueName":"%E5%9C%9F%E7%94%B2","round":"15","season":"2017","hostTeamName":"%E4%BB%A3%E5%B0%BC%E5%85%B9","hostTeamId":"516","awayTeamName":"%E8%90%A8%E5%A7%86%E6%9D%BE","awayTeamId":"1346","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"17","awayRank":"16","status":"COMPLETE","cup":"0","color":"#B5A152","tape":"5000","logoH":0,"logoG":0,"collection_status":1}},{"match":{"betId":"1416112","timezoneoffset":"1512896400","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E7%BD%97%E6%96%AF%E6%9D%9C%E5%A4%AB%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5082","awayTeamName":"%E4%B9%8C%E6%B3%95%E9%9D%92%E5%B9%B4%E9%98%9F","awayTeamId":"24147","hostScore":"3","awayScore":"0","hostHalfScore":"2","awayHalfScore":"0","hostRank":"9","awayRank":"5","status":"COMPLETE","cup":"0","color":"#666699","tape":"2500","logoH":"1478227478","logoG":0,"collection_status":1}},{"match":{"betId":"1416110","timezoneoffset":"1512885600","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E5%AE%89%E5%8D%A1%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5069","awayTeamName":"%E5%85%8B%E6%8B%89%E6%96%AF%E8%AF%BA%E8%BE%BE%E5%B0%94%E5%90%8E%E5%A4%87%E9%98%9F","awayTeamId":"15142","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","hostRank":"11","awayRank":"1","status":"COMPLETE","cup":"0","color":"#666699","tape":"-12500","logoH":"1478227699","logoG":"1478227680","collection_status":1}},{"match":{"betId":"1483298","timezoneoffset":"1512815400","leagueId":"772","leagueName":"%E4%BC%8A%E6%9C%97%E7%94%B2","round":"17","season":"2017","hostTeamName":"%E5%A1%94%E6%AF%94%E4%B9%8B","hostTeamId":"11185","awayTeamName":"%E9%A9%AC%E6%8B%89%E4%BA%91","awayTeamId":"2881","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"16","awayRank":"9","status":"COMPLETE","cup":"0","color":"#4999bb","tape":"-2500","logoH":0,"logoG":"2881","collection_status":1}},{"match":{"betId":"1481973","timezoneoffset":"1512814500","leagueId":"55","leagueName":"%E4%B8%9C%E4%BA%9A%E6%9D%AF","round":"决赛","season":"2016-2017","hostTeamName":"%E6%97%A5%E6%9C%AC","hostTeamId":"825","awayTeamName":"%E6%9C%9D%E9%B2%9C","awayTeamId":"799","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"44","awayRank":"132","status":"COMPLETE","cup":"1","color":"#ff6666","tape":"12500","logoH":"825","logoG":"799","collection_status":1}},{"match":{"betId":"1416114","timezoneoffset":"1512813600","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E8%8E%AB%E6%96%AF%E7%A7%91%E6%96%AF%E5%B7%B4%E8%BE%BE%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5083","awayTeamName":"%E8%8E%AB%E6%96%AF%E7%A7%91%E4%B8%AD%E5%A4%AE%E9%99%86%E5%86%9B%E5%90%8E%E5%A4%87%E9%98%9F","awayTeamId":"5087","hostScore":"1","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","hostRank":"8","awayRank":"2","status":"COMPLETE","cup":"0","color":"#666699","tape":"-2500","logoH":"1478227292","logoG":"1478227593","collection_status":1}},{"match":{"betId":"1419330","timezoneoffset":"1512813600","leagueId":"111","leagueName":"%E5%9C%9F%E7%94%B2","round":"15","season":"2017","hostTeamName":"%E5%B0%A4%E5%A7%86%E5%B0%BC%E8%80%B6","hostTeamId":"16331","awayTeamName":"%E5%B7%B4%E9%87%8C%E7%A7%91","awayTeamId":"6092","hostScore":"2","awayScore":"2","hostHalfScore":"1","awayHalfScore":"0","hostRank":"2","awayRank":"5","status":"COMPLETE","cup":"0","color":"#B5A152","tape":"5000","logoH":0,"logoG":"6092","collection_status":1}}],"total":10}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 1
         * errono : 0
         * errostr :
         */

        public int login;
        public int errono;
        public String errostr;
    }

    public static class DataBean {
        /**
         * matchlist : [{"match":{"betId":"1405283","timezoneoffset":"1513173600","leagueId":"110","leagueName":"%E5%A1%9E%E5%B0%94%E8%B6%85","round":"22","season":"2017","hostTeamName":"%E8%8B%8F%E6%9D%9C%E5%88%A9%E5%AF%9F","hostTeamId":"21205","awayTeamName":"%E7%91%9E%E5%BE%B7%E5%B0%BC%E5%9F%BA","awayTeamId":"895","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"4","status":"NO_START","cup":"0","color":"#996600","tape":"-7500","logoH":0,"logoG":"895","collection_status":1}},{"match":{"betId":"1405282","timezoneoffset":"1513173600","leagueId":"110","leagueName":"%E5%A1%9E%E5%B0%94%E8%B6%85","round":"22","season":"2017","hostTeamName":"%E8%90%A8%E5%8D%9A%E8%BF%AA%E5%8D%A1","hostTeamId":"9796","awayTeamName":"%E6%B2%83%E4%BC%8A%E6%B2%83%E8%BF%AA%E7%BA%B3","awayTeamId":"896","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"7","status":"NO_START","cup":"0","color":"#996600","tape":"5000","logoH":0,"logoG":"896","collection_status":1}},{"match":{"betId":"1483295","timezoneoffset":"1512901800","leagueId":"772","leagueName":"%E4%BC%8A%E6%9C%97%E7%94%B2","round":"17","season":"2017","hostTeamName":"%E5%B7%B4%E6%A0%BC","hostTeamId":"2859","awayTeamName":"%E5%BA%93%E5%86%85%E5%B7%B4%E5%8D%9A%E5%8B%92","awayTeamId":"25315","hostScore":"2","awayScore":"2","hostHalfScore":"2","awayHalfScore":"0","hostRank":"3","awayRank":"4","status":"COMPLETE","cup":"0","color":"#4999bb","tape":"2500","logoH":0,"logoG":0,"collection_status":1}},{"match":{"betId":"1419323","timezoneoffset":"1512900000","leagueId":"111","leagueName":"%E5%9C%9F%E7%94%B2","round":"15","season":"2017","hostTeamName":"%E4%BB%A3%E5%B0%BC%E5%85%B9","hostTeamId":"516","awayTeamName":"%E8%90%A8%E5%A7%86%E6%9D%BE","awayTeamId":"1346","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"17","awayRank":"16","status":"COMPLETE","cup":"0","color":"#B5A152","tape":"5000","logoH":0,"logoG":0,"collection_status":1}},{"match":{"betId":"1416112","timezoneoffset":"1512896400","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E7%BD%97%E6%96%AF%E6%9D%9C%E5%A4%AB%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5082","awayTeamName":"%E4%B9%8C%E6%B3%95%E9%9D%92%E5%B9%B4%E9%98%9F","awayTeamId":"24147","hostScore":"3","awayScore":"0","hostHalfScore":"2","awayHalfScore":"0","hostRank":"9","awayRank":"5","status":"COMPLETE","cup":"0","color":"#666699","tape":"2500","logoH":"1478227478","logoG":0,"collection_status":1}},{"match":{"betId":"1416110","timezoneoffset":"1512885600","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E5%AE%89%E5%8D%A1%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5069","awayTeamName":"%E5%85%8B%E6%8B%89%E6%96%AF%E8%AF%BA%E8%BE%BE%E5%B0%94%E5%90%8E%E5%A4%87%E9%98%9F","awayTeamId":"15142","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","hostRank":"11","awayRank":"1","status":"COMPLETE","cup":"0","color":"#666699","tape":"-12500","logoH":"1478227699","logoG":"1478227680","collection_status":1}},{"match":{"betId":"1483298","timezoneoffset":"1512815400","leagueId":"772","leagueName":"%E4%BC%8A%E6%9C%97%E7%94%B2","round":"17","season":"2017","hostTeamName":"%E5%A1%94%E6%AF%94%E4%B9%8B","hostTeamId":"11185","awayTeamName":"%E9%A9%AC%E6%8B%89%E4%BA%91","awayTeamId":"2881","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"16","awayRank":"9","status":"COMPLETE","cup":"0","color":"#4999bb","tape":"-2500","logoH":0,"logoG":"2881","collection_status":1}},{"match":{"betId":"1481973","timezoneoffset":"1512814500","leagueId":"55","leagueName":"%E4%B8%9C%E4%BA%9A%E6%9D%AF","round":"决赛","season":"2016-2017","hostTeamName":"%E6%97%A5%E6%9C%AC","hostTeamId":"825","awayTeamName":"%E6%9C%9D%E9%B2%9C","awayTeamId":"799","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"44","awayRank":"132","status":"COMPLETE","cup":"1","color":"#ff6666","tape":"12500","logoH":"825","logoG":"799","collection_status":1}},{"match":{"betId":"1416114","timezoneoffset":"1512813600","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E8%8E%AB%E6%96%AF%E7%A7%91%E6%96%AF%E5%B7%B4%E8%BE%BE%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5083","awayTeamName":"%E8%8E%AB%E6%96%AF%E7%A7%91%E4%B8%AD%E5%A4%AE%E9%99%86%E5%86%9B%E5%90%8E%E5%A4%87%E9%98%9F","awayTeamId":"5087","hostScore":"1","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","hostRank":"8","awayRank":"2","status":"COMPLETE","cup":"0","color":"#666699","tape":"-2500","logoH":"1478227292","logoG":"1478227593","collection_status":1}},{"match":{"betId":"1419330","timezoneoffset":"1512813600","leagueId":"111","leagueName":"%E5%9C%9F%E7%94%B2","round":"15","season":"2017","hostTeamName":"%E5%B0%A4%E5%A7%86%E5%B0%BC%E8%80%B6","hostTeamId":"16331","awayTeamName":"%E5%B7%B4%E9%87%8C%E7%A7%91","awayTeamId":"6092","hostScore":"2","awayScore":"2","hostHalfScore":"1","awayHalfScore":"0","hostRank":"2","awayRank":"5","status":"COMPLETE","cup":"0","color":"#B5A152","tape":"5000","logoH":0,"logoG":"6092","collection_status":1}}]
         * total : 10
         */

        public int total;
        public List<MatchlistBean> matchlist;

        public static class MatchlistBean {
            /**
             * match : {"betId":"1405283","timezoneoffset":"1513173600","leagueId":"110","leagueName":"%E5%A1%9E%E5%B0%94%E8%B6%85","round":"22","season":"2017","hostTeamName":"%E8%8B%8F%E6%9D%9C%E5%88%A9%E5%AF%9F","hostTeamId":"21205","awayTeamName":"%E7%91%9E%E5%BE%B7%E5%B0%BC%E5%9F%BA","awayTeamId":"895","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"4","status":"NO_START","cup":"0","color":"#996600","tape":"-7500","logoH":0,"logoG":"895","collection_status":1}
             */

            public MatchBean match;

            public static class MatchBean {
                /**
                 * betId : 1405283
                 * timezoneoffset : 1513173600
                 * leagueId : 110
                 * leagueName : %E5%A1%9E%E5%B0%94%E8%B6%85
                 * round : 22
                 * season : 2017
                 * hostTeamName : %E8%8B%8F%E6%9D%9C%E5%88%A9%E5%AF%9F
                 * hostTeamId : 21205
                 * awayTeamName : %E7%91%9E%E5%BE%B7%E5%B0%BC%E5%9F%BA
                 * awayTeamId : 895
                 * hostScore : 0
                 * awayScore : 0
                 * hostHalfScore : 0
                 * awayHalfScore : 0
                 * hostRank : 10
                 * awayRank : 4
                 * status : NO_START
                 * cup : 0
                 * color : #996600
                 * tape : -7500
                 * logoH : 0
                 * logoG : 895
                 * collection_status : 1
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
                public int logoH;
                public String logoG;
                public int collection_status;
            }
        }
    }
}
