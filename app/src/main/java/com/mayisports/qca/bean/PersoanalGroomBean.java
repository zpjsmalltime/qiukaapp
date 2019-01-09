package com.mayisports.qca.bean;

import java.util.List;

/**
 * 推荐数据bean
 * Created by Zpj on 2017/12/12.
 */

public class PersoanalGroomBean {


    /**
     * status : {"login":0}
     * data : {"user":{"headurl":"http://tva3.sinaimg.cn/crop.0.0.180.180.50/754a27e9jw1e8qgp5bmzyj2050050aa8.jpg","verify_reason":"超过十万个体育博彩预测模型，基于海量大数据和机器学习算法从多个维度进行分析和预测","verify_type":"0","type":"1","user_id":"170208","nickname":"足彩大数据模型","user_rank":0,"praise_count":null,"view_count":"7","tag":[null],"tag1":["11连红"],"follow_status":0,"all_bet_count":"89"},"statics":{"all_bet_count":"88","all_bet_accuracy":"62","all_revenue":"85.61","week_bet_count":"1","week_bet_accuracy":"1","week_revenue":"1.5","month_bet_count":"2","month_bet_accuracy":"2","month_revenue":"2.9","quarter_bet_count":"6","quarter_bet_accuracy":"6","quarter_revenue":"8.34","recent10_accuracy":"10","recent20_accuracy":"16","continueMatch":"11","item1":{"title":"10场","count":10,"accuracy":"10","revenue":"13.9"},"item2":{"title":"50场","count":50,"accuracy":"34","revenue":"46.46"},"item3":{"title":"88场","count":"88","accuracy":"62","revenue":"85.61"}},"recommendationlist":[{"match":{"betId":"1482729","hostScore":"0","awayScore":"0","leagueId":"55","leagueName":"东亚杯","hostTeamName":"日本","awayTeamName":"中国","status":"HALF_TIME","timezoneoffset":"1513074000"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":""}},{"match":{"betId":"1432916","hostScore":"2","awayScore":"0","leagueId":"41","leagueName":"意甲","hostTeamName":"乌迪内斯","awayTeamName":"贝内文托","status":"COMPLETE","timezoneoffset":"1512914400"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":15000}","revenueStr":"赢"}},{"match":{"betId":"1401936","hostScore":"3","awayScore":"0","leagueId":"29","leagueName":"荷乙","hostTeamName":"阿贾克斯B","awayTeamName":"多德勒支","status":"COMPLETE","timezoneoffset":"1511809200"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":14000}","revenueStr":"赢"}},{"match":{"betId":"1337065","hostScore":"3","awayScore":"1","leagueId":"103","leagueName":"瑞典甲","hostTeamName":"法尔肯","awayTeamName":"弗雷杰","status":"COMPLETE","timezoneoffset":"1509807600"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1402996","hostScore":"2","awayScore":"1","leagueId":"117","leagueName":"捷甲","hostTeamName":"博雷斯拉","awayTeamName":"伊赫拉瓦","status":"COMPLETE","timezoneoffset":"1509285600"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1472073","hostScore":"7","awayScore":"0","leagueId":"59","leagueName":"荷兰杯","hostTeamName":"瓦尔韦克","awayTeamName":"赫鲁斯","status":"COMPLETE","timezoneoffset":"1508867100"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1400447","hostScore":"1","awayScore":"0","leagueId":"45","leagueName":"英甲","hostTeamName":"维冈竞技","awayTeamName":"普利茅斯","status":"COMPLETE","timezoneoffset":"1506451500"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1403213","hostScore":"1","awayScore":"0","leagueId":"120","leagueName":"墨西联","hostTeamName":"蒙特雷","awayTeamName":"内卡萨","status":"COMPLETE","timezoneoffset":"1505001600"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":14400}","revenueStr":"赢"}},{"match":{"betId":"1359764","hostScore":"4","awayScore":"2","leagueId":"1131","leagueName":"韩挑K联","hostTeamName":"釜山偶像","awayTeamName":"大田市民","status":"COMPLETE","timezoneoffset":"1504432800"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1353679","hostScore":"2","awayScore":"0","leagueId":"143","leagueName":"冰岛超","hostTeamName":"瓦路尔","awayTeamName":"格林达维克","status":"COMPLETE","timezoneoffset":"1503342900"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}}]}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 0
         */

        public int login;
    }

    public static class DataBean {
        /**
         * user : {"headurl":"http://tva3.sinaimg.cn/crop.0.0.180.180.50/754a27e9jw1e8qgp5bmzyj2050050aa8.jpg","verify_reason":"超过十万个体育博彩预测模型，基于海量大数据和机器学习算法从多个维度进行分析和预测","verify_type":"0","type":"1","user_id":"170208","nickname":"足彩大数据模型","user_rank":0,"praise_count":null,"view_count":"7","tag":[null],"tag1":["11连红"],"follow_status":0,"all_bet_count":"89"}
         * statics : {"all_bet_count":"88","all_bet_accuracy":"62","all_revenue":"85.61","week_bet_count":"1","week_bet_accuracy":"1","week_revenue":"1.5","month_bet_count":"2","month_bet_accuracy":"2","month_revenue":"2.9","quarter_bet_count":"6","quarter_bet_accuracy":"6","quarter_revenue":"8.34","recent10_accuracy":"10","recent20_accuracy":"16","continueMatch":"11","item1":{"title":"10场","count":10,"accuracy":"10","revenue":"13.9"},"item2":{"title":"50场","count":50,"accuracy":"34","revenue":"46.46"},"item3":{"title":"88场","count":"88","accuracy":"62","revenue":"85.61"}}
         * recommendationlist : [{"match":{"betId":"1482729","hostScore":"0","awayScore":"0","leagueId":"55","leagueName":"东亚杯","hostTeamName":"日本","awayTeamName":"中国","status":"HALF_TIME","timezoneoffset":"1513074000"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":""}},{"match":{"betId":"1432916","hostScore":"2","awayScore":"0","leagueId":"41","leagueName":"意甲","hostTeamName":"乌迪内斯","awayTeamName":"贝内文托","status":"COMPLETE","timezoneoffset":"1512914400"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":15000}","revenueStr":"赢"}},{"match":{"betId":"1401936","hostScore":"3","awayScore":"0","leagueId":"29","leagueName":"荷乙","hostTeamName":"阿贾克斯B","awayTeamName":"多德勒支","status":"COMPLETE","timezoneoffset":"1511809200"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":14000}","revenueStr":"赢"}},{"match":{"betId":"1337065","hostScore":"3","awayScore":"1","leagueId":"103","leagueName":"瑞典甲","hostTeamName":"法尔肯","awayTeamName":"弗雷杰","status":"COMPLETE","timezoneoffset":"1509807600"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1402996","hostScore":"2","awayScore":"1","leagueId":"117","leagueName":"捷甲","hostTeamName":"博雷斯拉","awayTeamName":"伊赫拉瓦","status":"COMPLETE","timezoneoffset":"1509285600"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1472073","hostScore":"7","awayScore":"0","leagueId":"59","leagueName":"荷兰杯","hostTeamName":"瓦尔韦克","awayTeamName":"赫鲁斯","status":"COMPLETE","timezoneoffset":"1508867100"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1400447","hostScore":"1","awayScore":"0","leagueId":"45","leagueName":"英甲","hostTeamName":"维冈竞技","awayTeamName":"普利茅斯","status":"COMPLETE","timezoneoffset":"1506451500"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1403213","hostScore":"1","awayScore":"0","leagueId":"120","leagueName":"墨西联","hostTeamName":"蒙特雷","awayTeamName":"内卡萨","status":"COMPLETE","timezoneoffset":"1505001600"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":14400}","revenueStr":"赢"}},{"match":{"betId":"1359764","hostScore":"4","awayScore":"2","leagueId":"1131","leagueName":"韩挑K联","hostTeamName":"釜山偶像","awayTeamName":"大田市民","status":"COMPLETE","timezoneoffset":"1504432800"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}},{"match":{"betId":"1353679","hostScore":"2","awayScore":"0","leagueId":"143","leagueName":"冰岛超","hostTeamName":"瓦路尔","awayTeamName":"格林达维克","status":"COMPLETE","timezoneoffset":"1503342900"},"recommendation":{"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":"赢"}}]
         */

        public UserBean user;
        public StaticsBean statics;
        public List<RecommendationlistBean> recommendationlist;

        public static class UserBean {
            /**
             * headurl : http://tva3.sinaimg.cn/crop.0.0.180.180.50/754a27e9jw1e8qgp5bmzyj2050050aa8.jpg
             * verify_reason : 超过十万个体育博彩预测模型，基于海量大数据和机器学习算法从多个维度进行分析和预测
             * verify_type : 0
             * type : 1
             * user_id : 170208
             * nickname : 足彩大数据模型
             * user_rank : 0
             * praise_count : null
             * view_count : 7
             * tag : [null]
             * tag1 : ["11连红"]
             * follow_status : 0
             * all_bet_count : 89
             */

            public String headurl;
            public String verify_reason;
            public String verify_type;
            public String type;
            public String user_id;
            public String nickname;
            public int user_rank;
            public Object praise_count;
            public String view_count;
            public int follow_status;
            public String all_bet_count;
            public List<String> tag;
            public List<String> tag1;
        }

        public static class StaticsBean {
            /**
             * all_bet_count : 88
             * all_bet_accuracy : 62
             * all_revenue : 85.61
             * week_bet_count : 1
             * week_bet_accuracy : 1
             * week_revenue : 1.5
             * month_bet_count : 2
             * month_bet_accuracy : 2
             * month_revenue : 2.9
             * quarter_bet_count : 6
             * quarter_bet_accuracy : 6
             * quarter_revenue : 8.34
             * recent10_accuracy : 10
             * recent20_accuracy : 16
             * continueMatch : 11
             * item1 : {"title":"10场","count":10,"accuracy":"10","revenue":"13.9"}
             * item2 : {"title":"50场","count":50,"accuracy":"34","revenue":"46.46"}
             * item3 : {"title":"88场","count":"88","accuracy":"62","revenue":"85.61"}
             */

            public String all_bet_count;
            public String all_bet_accuracy;
            public String all_revenue;
            public String week_bet_count;
            public String week_bet_accuracy;
            public String week_revenue;
            public String month_bet_count;
            public String month_bet_accuracy;
            public String month_revenue;
            public String quarter_bet_count;
            public String quarter_bet_accuracy;
            public String quarter_revenue;
            public String recent10_accuracy;
            public String recent20_accuracy;
            public String continueMatch;
            public Item1Bean item1;
            public Item2Bean item2;
            public Item3Bean item3;

            public static class Item1Bean {
                /**
                 * title : 10场
                 * count : 10
                 * accuracy : 10
                 * revenue : 13.9
                 */

                public String title;
                public int count;
                public String accuracy;
                public String revenue;
            }

            public static class Item2Bean {
                /**
                 * title : 50场
                 * count : 50
                 * accuracy : 34
                 * revenue : 46.46
                 */

                public String title;
                public int count;
                public String accuracy;
                public String revenue;
            }

            public static class Item3Bean {
                /**
                 * title : 88场
                 * count : 88
                 * accuracy : 62
                 * revenue : 85.61
                 */

                public String title;
                public String count;
                public String accuracy;
                public String revenue;
            }
        }

        public static class RecommendationlistBean {
            /**
             * match : {"betId":"1482729","hostScore":"0","awayScore":"0","leagueId":"55","leagueName":"东亚杯","hostTeamName":"日本","awayTeamName":"中国","status":"HALF_TIME","timezoneoffset":"1513074000"}
             * recommendation : {"price":58,"return_if_wrong":0,"title":null,"type":"欧盘","buy":0,"betDetail":"{\"winOdds\":13600}","revenueStr":""}
             */

            public MatchBean match;
            public RecommendationBean recommendation;

            public static class MatchBean {
                /**
                 * betId : 1482729
                 * hostScore : 0
                 * awayScore : 0
                 * leagueId : 55
                 * leagueName : 东亚杯
                 * hostTeamName : 日本
                 * awayTeamName : 中国
                 * status : HALF_TIME
                 * timezoneoffset : 1513074000
                 */

                public String betId;
                public String hostScore;
                public String awayScore;
                public String leagueId;
                public String leagueName;
                public String hostTeamName;
                public String awayTeamName;
                public String status;
                public String timezoneoffset;
            }

            public static class RecommendationBean {
                /**
                 * price : 58
                 * return_if_wrong : 0
                 * title : null
                 * type : 欧盘
                 * buy : 0
                 * betDetail : {"winOdds":13600}
                 * revenueStr :
                 */

                public int price;
                public int return_if_wrong;
                public String title;
                public String type;
                public int buy;
                public String betDetail;
                public String revenueStr;
            }
        }
    }
}
