package com.mayisports.qca.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 即时比分数据
 * Created by Zpj on 2017/12/10.
 */

public class ImmediateScoreBean {


    /**
     * status : {"login":1,"errono":0,"errostr":""}
     * data : {"tags":{"全部":0,"竞彩(43)":-2,"热门推荐":-1,"一级":-3,"直播":-4,"北单":-5},"matchlist":[{"match":{"betId":"1416772","timezoneoffset":"1512864000","leagueId":"357","leagueName":"%E6%99%BA%E5%88%A9%E7%94%B2","round":"15","season":"2017-2018","hostTeamName":"%E5%9F%BA%E7%BA%A6%E5%A1%94","hostTeamId":"6745","awayTeamName":"%E5%A5%A5%E8%BE%BE%E7%A7%91%E6%96%AF","awayTeamId":"913","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"9","awayRank":"5","status":"HALF_TIME","cup":"1","color":"#003900","tape":"0","logoH":0,"logoG":0,"starttime":1512863960,"hostRedCard":"0","awayRedCard":"0","hostYellowCard":"1","awayYellowCard":"0","hostCorner":"1","awayCorner":"2","index":"周六081","collection_status":0}},{"match":{"betId":"1444585","timezoneoffset":"1512865800","leagueId":"14","leagueName":"%E9%98%BF%E7%94%B2","round":"12","season":"2017","hostTeamName":"%E8%90%A8%E5%85%B0%E5%85%B5%E5%B7%A5","hostTeamId":"304","awayTeamName":"%E9%98%BF%E6%A0%B9%E5%BB%B7%E7%8B%AC%E7%AB%8B","awayTeamId":"302","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","hostRank":"28","awayRank":"7","status":"FIRST_HALF","cup":"0","color":"#00CCCC","tape":"-2500","logoH":"304","logoG":"302","starttime":1512865820,"hostRedCard":"0","awayRedCard":"0","hostYellowCard":"0","awayYellowCard":"0","index":"周六082","collection_status":0}},{"match":{"betId":"1416110","timezoneoffset":"1512885600","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E5%AE%89%E5%8D%A1%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5069","awayTeamName":"%E5%85%8B%E6%8B%89%E6%96%AF%E8%AF%BA%E8%BE%BE%E5%B0%94%E5%90%8E%E5%A4%87%E9%98%9F","awayTeamId":"15142","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"11","awayRank":"1","status":"NO_START","cup":"0","color":"#666699","tape":"-12500","logoH":"1478227699","logoG":"1478227680","index":null,"collection_status":0}},{"match":{"betId":"1448450","timezoneoffset":"1512887400","leagueId":"271","leagueName":"%E9%A6%99%E6%B8%AF%E8%B6%85","round":"8","season":"2017","hostTeamName":"%E6%A0%87%E5%87%86%E6%B5%81%E6%B5%AA","hostTeamId":"7884","awayTeamName":"%E6%9D%B0%E5%BF%97","awayTeamId":"3057","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"1","status":"NO_START","cup":"0","color":"#E910A0","tape":"-30000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1483477","timezoneoffset":"1512887400","leagueId":"271","leagueName":"%E9%A6%99%E6%B8%AF%E8%B6%85","round":"8","season":"2017","hostTeamName":"%E5%92%8C%E5%AF%8C%E5%A4%A7%E5%9F%94","hostTeamId":"3963","awayTeamName":"%E5%A4%A9%E8%A1%8C%E5%85%83%E6%9C%97","awayTeamId":"21695","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"8","status":"NO_START","cup":"0","color":"#E910A0","tape":"10000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1406357","timezoneoffset":"1512892800","leagueId":"237","leagueName":"%E6%BE%B3%E6%B4%B2%E7%94%B2","round":"10","season":"2017","hostTeamName":"%E5%A2%A8%E5%B0%94%E6%9C%AC%E5%9F%8E","hostTeamId":"13186","awayTeamName":"%E4%B8%AD%E5%B2%B8%E6%B0%B4%E6%89%8B","awayTeamId":"2471","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"5","status":"NO_START","cup":"0","color":"#FF7000","tape":"5000","logoH":"13186","logoG":"2471","index":"周日001","collection_status":0}},{"match":{"betId":"1416112","timezoneoffset":"1512896400","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E7%BD%97%E6%96%AF%E6%9D%9C%E5%A4%AB%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5082","awayTeamName":"%E4%B9%8C%E6%B3%95%E9%9D%92%E5%B9%B4%E9%98%9F","awayTeamId":"24147","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"9","awayRank":"5","status":"NO_START","cup":"0","color":"#666699","tape":"2500","logoH":"1478227478","logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1448452","timezoneoffset":"1512898200","leagueId":"271","leagueName":"%E9%A6%99%E6%B8%AF%E8%B6%85","round":"8","season":"2017","hostTeamName":"%E7%90%86%E6%96%87","hostTeamId":"46205","awayTeamName":"%E5%8D%97%E5%8C%BA","awayTeamId":"18719","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"9","awayRank":"5","status":"NO_START","cup":"0","color":"#E910A0","tape":"-5000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1419323","timezoneoffset":"1512900000","leagueId":"111","leagueName":"%E5%9C%9F%E7%94%B2","round":"15","season":"2017","hostTeamName":"%E4%BB%A3%E5%B0%BC%E5%85%B9","hostTeamId":"516","awayTeamName":"%E8%90%A8%E5%A7%86%E6%9D%BE","awayTeamId":"1346","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"17","awayRank":"16","status":"NO_START","cup":"0","color":"#B5A152","tape":"5000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1407478","timezoneoffset":"1512903600","leagueId":"112","leagueName":"%E4%BF%9D%E8%B6%85","round":"20","season":"2017","hostTeamName":"%E5%A4%9A%E7%91%99","hostTeamId":"4229","awayTeamName":"%E6%99%AE%E7%BD%97%E5%A4%AB%E7%81%AB%E8%BD%A6","awayTeamId":"993","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"13","awayRank":"10","status":"NO_START","cup":"0","color":"#6C6C00","tape":"2500","logoH":"1512020701","logoG":"993","index":null,"collection_status":0}}]}
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
         * tags : {"全部":0,"竞彩(43)":-2,"热门推荐":-1,"一级":-3,"直播":-4,"北单":-5}
         * matchlist : [{"match":{"betId":"1416772","timezoneoffset":"1512864000","leagueId":"357","leagueName":"%E6%99%BA%E5%88%A9%E7%94%B2","round":"15","season":"2017-2018","hostTeamName":"%E5%9F%BA%E7%BA%A6%E5%A1%94","hostTeamId":"6745","awayTeamName":"%E5%A5%A5%E8%BE%BE%E7%A7%91%E6%96%AF","awayTeamId":"913","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"9","awayRank":"5","status":"HALF_TIME","cup":"1","color":"#003900","tape":"0","logoH":0,"logoG":0,"starttime":1512863960,"hostRedCard":"0","awayRedCard":"0","hostYellowCard":"1","awayYellowCard":"0","hostCorner":"1","awayCorner":"2","index":"周六081","collection_status":0}},{"match":{"betId":"1444585","timezoneoffset":"1512865800","leagueId":"14","leagueName":"%E9%98%BF%E7%94%B2","round":"12","season":"2017","hostTeamName":"%E8%90%A8%E5%85%B0%E5%85%B5%E5%B7%A5","hostTeamId":"304","awayTeamName":"%E9%98%BF%E6%A0%B9%E5%BB%B7%E7%8B%AC%E7%AB%8B","awayTeamId":"302","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","hostRank":"28","awayRank":"7","status":"FIRST_HALF","cup":"0","color":"#00CCCC","tape":"-2500","logoH":"304","logoG":"302","starttime":1512865820,"hostRedCard":"0","awayRedCard":"0","hostYellowCard":"0","awayYellowCard":"0","index":"周六082","collection_status":0}},{"match":{"betId":"1416110","timezoneoffset":"1512885600","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E5%AE%89%E5%8D%A1%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5069","awayTeamName":"%E5%85%8B%E6%8B%89%E6%96%AF%E8%AF%BA%E8%BE%BE%E5%B0%94%E5%90%8E%E5%A4%87%E9%98%9F","awayTeamId":"15142","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"11","awayRank":"1","status":"NO_START","cup":"0","color":"#666699","tape":"-12500","logoH":"1478227699","logoG":"1478227680","index":null,"collection_status":0}},{"match":{"betId":"1448450","timezoneoffset":"1512887400","leagueId":"271","leagueName":"%E9%A6%99%E6%B8%AF%E8%B6%85","round":"8","season":"2017","hostTeamName":"%E6%A0%87%E5%87%86%E6%B5%81%E6%B5%AA","hostTeamId":"7884","awayTeamName":"%E6%9D%B0%E5%BF%97","awayTeamId":"3057","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"1","status":"NO_START","cup":"0","color":"#E910A0","tape":"-30000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1483477","timezoneoffset":"1512887400","leagueId":"271","leagueName":"%E9%A6%99%E6%B8%AF%E8%B6%85","round":"8","season":"2017","hostTeamName":"%E5%92%8C%E5%AF%8C%E5%A4%A7%E5%9F%94","hostTeamId":"3963","awayTeamName":"%E5%A4%A9%E8%A1%8C%E5%85%83%E6%9C%97","awayTeamId":"21695","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"8","status":"NO_START","cup":"0","color":"#E910A0","tape":"10000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1406357","timezoneoffset":"1512892800","leagueId":"237","leagueName":"%E6%BE%B3%E6%B4%B2%E7%94%B2","round":"10","season":"2017","hostTeamName":"%E5%A2%A8%E5%B0%94%E6%9C%AC%E5%9F%8E","hostTeamId":"13186","awayTeamName":"%E4%B8%AD%E5%B2%B8%E6%B0%B4%E6%89%8B","awayTeamId":"2471","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"3","awayRank":"5","status":"NO_START","cup":"0","color":"#FF7000","tape":"5000","logoH":"13186","logoG":"2471","index":"周日001","collection_status":0}},{"match":{"betId":"1416112","timezoneoffset":"1512896400","leagueId":"414","leagueName":"%E4%BF%84%E9%9D%92%E8%81%94","round":"20","season":"2017","hostTeamName":"%E7%BD%97%E6%96%AF%E6%9D%9C%E5%A4%AB%E5%90%8E%E5%A4%87%E9%98%9F","hostTeamId":"5082","awayTeamName":"%E4%B9%8C%E6%B3%95%E9%9D%92%E5%B9%B4%E9%98%9F","awayTeamId":"24147","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"9","awayRank":"5","status":"NO_START","cup":"0","color":"#666699","tape":"2500","logoH":"1478227478","logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1448452","timezoneoffset":"1512898200","leagueId":"271","leagueName":"%E9%A6%99%E6%B8%AF%E8%B6%85","round":"8","season":"2017","hostTeamName":"%E7%90%86%E6%96%87","hostTeamId":"46205","awayTeamName":"%E5%8D%97%E5%8C%BA","awayTeamId":"18719","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"9","awayRank":"5","status":"NO_START","cup":"0","color":"#E910A0","tape":"-5000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1419323","timezoneoffset":"1512900000","leagueId":"111","leagueName":"%E5%9C%9F%E7%94%B2","round":"15","season":"2017","hostTeamName":"%E4%BB%A3%E5%B0%BC%E5%85%B9","hostTeamId":"516","awayTeamName":"%E8%90%A8%E5%A7%86%E6%9D%BE","awayTeamId":"1346","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"17","awayRank":"16","status":"NO_START","cup":"0","color":"#B5A152","tape":"5000","logoH":0,"logoG":0,"index":null,"collection_status":0}},{"match":{"betId":"1407478","timezoneoffset":"1512903600","leagueId":"112","leagueName":"%E4%BF%9D%E8%B6%85","round":"20","season":"2017","hostTeamName":"%E5%A4%9A%E7%91%99","hostTeamId":"4229","awayTeamName":"%E6%99%AE%E7%BD%97%E5%A4%AB%E7%81%AB%E8%BD%A6","awayTeamId":"993","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"13","awayRank":"10","status":"NO_START","cup":"0","color":"#6C6C00","tape":"2500","logoH":"1512020701","logoG":"993","index":null,"collection_status":0}}]
         */

        public TagsBean tags;
        public List<MatchlistBean> matchlist;
        public List<MatchlistBean> matchList;
        /**
         * 1 有  0 没有下一页
         */
        public int pre_page;

        /**
         * 自定义标记
         */
        public String tag;

        public static class TagsBean {
            /**
             * 全部 : 0
             * 竞彩(43) : -2
             * 热门推荐 : -1
             * 一级 : -3
             * 直播 : -4
             * 北单 : -5
             */

            public int 全部;
            @SerializedName("竞彩(43)")
            public int _$43126; // FIXME check this code
            public int 热门推荐;
            public int 一级;
            public int 直播;
            public int 北单;
        }

        public static class MatchlistBean {
            /**
             * match : {"betId":"1416772","timezoneoffset":"1512864000","leagueId":"357","leagueName":"%E6%99%BA%E5%88%A9%E7%94%B2","round":"15","season":"2017-2018","hostTeamName":"%E5%9F%BA%E7%BA%A6%E5%A1%94","hostTeamId":"6745","awayTeamName":"%E5%A5%A5%E8%BE%BE%E7%A7%91%E6%96%AF","awayTeamId":"913","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","hostRank":"9","awayRank":"5","status":"HALF_TIME","cup":"1","color":"#003900","tape":"0","logoH":0,"logoG":0,"starttime":1512863960,"hostRedCard":"0","awayRedCard":"0","hostYellowCard":"1","awayYellowCard":"0","hostCorner":"1","awayCorner":"2","index":"周六081","collection_status":0}
             */

            public MatchBean match;

            public static class MatchBean {




                /**
                 * betId : 1416772
                 * timezoneoffset : 1512864000
                 * leagueId : 357
                 * leagueName : %E6%99%BA%E5%88%A9%E7%94%B2
                 * round : 15
                 * season : 2017-2018
                 * hostTeamName : %E5%9F%BA%E7%BA%A6%E5%A1%94
                 * hostTeamId : 6745
                 * awayTeamName : %E5%A5%A5%E8%BE%BE%E7%A7%91%E6%96%AF
                 * awayTeamId : 913
                 * hostScore : 0
                 * awayScore : 1
                 * hostHalfScore : 0
                 * awayHalfScore : 1
                 * hostRank : 9
                 * awayRank : 5
                 * status : HALF_TIME
                 * cup : 1
                 * color : #003900
                 * tape : 0
                 * logoH : 0
                 * logoG : 0
                 * starttime : 1512863960
                 * hostRedCard : 0
                 * awayRedCard : 0
                 * hostYellowCard : 1
                 * awayYellowCard : 0
                 * hostCorner : 1
                 * awayCorner : 2
                 * index : 周六081
                 * collection_status : 0
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
                public int logoG;
                public long starttime;
                public String hostRedCard;
                public String awayRedCard;
                public String hostYellowCard;
                public String awayYellowCard;
                public String hostCorner;
                public String awayCorner;
                public String index;
                public int collection_status;

                public int tvlink;//1直播


                public String awayRed;
                public String awayYellow;
                public String hostRed;
                public String hostYellow;
                public int playerList;

                public String match_information_count;
                public int recommendation_count;

            }
        }
    }
}
