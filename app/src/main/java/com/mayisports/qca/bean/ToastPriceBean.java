package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2017/12/14.
 */

public class ToastPriceBean {


    /**
     * status : {"login":1,"reqtype":"req","user_id":"71640"}
     * data : {"match":{"betId":"1395633","timezoneoffset":"1513272600","leagueId":"28","leagueName":"%E8%8D%B7%E7%94%B2","round":"16","season":"2017","hostTeamName":"%E8%B5%AB%E6%8B%89%E5%85%8B","hostTeamId":"289","awayTeamName":"%E9%B9%BF%E7%89%B9%E5%B7%B4%E8%BE%BE","awayTeamId":"284","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"17","status":"NO_START","cup":"0","color":"#FF6699","tape":"2500","logoH":"289","logoG":"284","fav_status":0},"user":{"verify_type":"3","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511157327.png","nickname":"竞猜310","user_type":"2","user_id":"188310","tag":["实战高手"],"tag1":[],"reward_count_list":[1,8,18,28,88,188],"view_count":"641","followers_count":"1","praise_status":0,"coin":"0.5","praise_count":"0","follow_status":0},"revenueStr":"","recommendation":{"create_time":"1513227565","bet":{"awayOdds":"8500","tape":"2500"},"title":"赫拉克vs鹿特巴达：一场荷兰甲","status":1,"return_if_wrong":0},"odds":{"asia":{"hostOdds":"1.09","awayOdds":0.85,"tape":"2500"}},"reason":"竞猜001荷甲赫拉克莱斯vs鹿特丹斯巴达。<br/><br/>今年欧冠赛场荷兰军团再次全军覆没，导致荷兰欧战积分以降再降，明年欧冠正赛资格已经没有，荷兰这种全攻全守打法已经不适合现代足球，目前主流打法仍然是传控和防守反击，未来个人认为这两种打法依然不会被时代淘汰，荷兰崇尚全攻全守，这就需要个个位置都是全能性球员，这点太难了，没落是迟早的，就怕来的太晚。<br/><br/>主队近期2场全部大败，不过其主场已经4连胜，主强客弱，客队鹿特丹属于荷兰传统强队，近况发挥欠佳，目前已经3轮不胜，本赛季发挥糟糕同样是客场表现太差，目前客场已经10场不胜。<br/>历史交锋旗鼓相当，本赛季首次交锋鹿特丹主场3:1赢球，盘口方面，主队已经连续3个盘口不敌鹿特丹，同样连续3场面对鹿特丹非平即负。<br/><br/>本场亚盘多以半球高水，历史盘口同样是半球平半居多，这就说明本场依然沿用历史盘口，个人认为尊重历史充分体现了2大题材。<br/><br/>1:主队主场4连胜，这点有拉力，大家都能看到。<br/><br/>2:客队连续3年不败对手，同样具备拉力。<br/><br/>目前大家都能看到这两个拉力，这样就剩下盘口解析。<br/><br/>我们充分去尊重庄家，来看澳门盘口。<br/><br/>初盘半球上盘高水，退盘平半上盘仍然高水。<br/><br/>初盘高水是对于主队主场4连胜还是历史交锋不信任？后续退盘平半按照常规操作下盘理应高水，目前反过来，持续打压上盘，合解？个人认为是历史交锋题材并没有发挥作用，以至于澳门持续打压上盘，过渡说明历史因素，殊不知主队主场4连胜导致上盘投注较大，不得不退盘求其次。<br/><br/>简单说:<br/>闲家已经忽略历史交锋题材，着重看好主场战绩，以至于上盘持续高水，如果澳门选择升盘或者过渡看好上盘势必更加过热，不利于平衡投注量。<br/><br/>荷甲本轮整体只出现2个上盘，其中亚盘有一个赢半的，那么本轮还有2场，下面那场超级深盘，所以本场显的就非常重要。<br/>尔在第十五轮次同样上盘较少，连续2轮上盘不出。<br/><br/>结论:<br/><br/>看似各种操作都在对付下盘玩家，实则本场主队主场战绩虽好但克星终究是克星。<br/><br/>推荐:<br/><br/>鹿特丹不败。受平半赢。"}
     * type : nomoney
     * text : 您的金币余额不足！
     */

    public StatusBean status;
    public DataBean data;
    public String type;
    public String text;

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
         * match : {"betId":"1395633","timezoneoffset":"1513272600","leagueId":"28","leagueName":"%E8%8D%B7%E7%94%B2","round":"16","season":"2017","hostTeamName":"%E8%B5%AB%E6%8B%89%E5%85%8B","hostTeamId":"289","awayTeamName":"%E9%B9%BF%E7%89%B9%E5%B7%B4%E8%BE%BE","awayTeamId":"284","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","hostRank":"10","awayRank":"17","status":"NO_START","cup":"0","color":"#FF6699","tape":"2500","logoH":"289","logoG":"284","fav_status":0}
         * user : {"verify_type":"3","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511157327.png","nickname":"竞猜310","user_type":"2","user_id":"188310","tag":["实战高手"],"tag1":[],"reward_count_list":[1,8,18,28,88,188],"view_count":"641","followers_count":"1","praise_status":0,"coin":"0.5","praise_count":"0","follow_status":0}
         * revenueStr :
         * recommendation : {"create_time":"1513227565","bet":{"awayOdds":"8500","tape":"2500"},"title":"赫拉克vs鹿特巴达：一场荷兰甲","status":1,"return_if_wrong":0}
         * odds : {"asia":{"hostOdds":"1.09","awayOdds":0.85,"tape":"2500"}}
         * reason : 竞猜001荷甲赫拉克莱斯vs鹿特丹斯巴达。<br/><br/>今年欧冠赛场荷兰军团再次全军覆没，导致荷兰欧战积分以降再降，明年欧冠正赛资格已经没有，荷兰这种全攻全守打法已经不适合现代足球，目前主流打法仍然是传控和防守反击，未来个人认为这两种打法依然不会被时代淘汰，荷兰崇尚全攻全守，这就需要个个位置都是全能性球员，这点太难了，没落是迟早的，就怕来的太晚。<br/><br/>主队近期2场全部大败，不过其主场已经4连胜，主强客弱，客队鹿特丹属于荷兰传统强队，近况发挥欠佳，目前已经3轮不胜，本赛季发挥糟糕同样是客场表现太差，目前客场已经10场不胜。<br/>历史交锋旗鼓相当，本赛季首次交锋鹿特丹主场3:1赢球，盘口方面，主队已经连续3个盘口不敌鹿特丹，同样连续3场面对鹿特丹非平即负。<br/><br/>本场亚盘多以半球高水，历史盘口同样是半球平半居多，这就说明本场依然沿用历史盘口，个人认为尊重历史充分体现了2大题材。<br/><br/>1:主队主场4连胜，这点有拉力，大家都能看到。<br/><br/>2:客队连续3年不败对手，同样具备拉力。<br/><br/>目前大家都能看到这两个拉力，这样就剩下盘口解析。<br/><br/>我们充分去尊重庄家，来看澳门盘口。<br/><br/>初盘半球上盘高水，退盘平半上盘仍然高水。<br/><br/>初盘高水是对于主队主场4连胜还是历史交锋不信任？后续退盘平半按照常规操作下盘理应高水，目前反过来，持续打压上盘，合解？个人认为是历史交锋题材并没有发挥作用，以至于澳门持续打压上盘，过渡说明历史因素，殊不知主队主场4连胜导致上盘投注较大，不得不退盘求其次。<br/><br/>简单说:<br/>闲家已经忽略历史交锋题材，着重看好主场战绩，以至于上盘持续高水，如果澳门选择升盘或者过渡看好上盘势必更加过热，不利于平衡投注量。<br/><br/>荷甲本轮整体只出现2个上盘，其中亚盘有一个赢半的，那么本轮还有2场，下面那场超级深盘，所以本场显的就非常重要。<br/>尔在第十五轮次同样上盘较少，连续2轮上盘不出。<br/><br/>结论:<br/><br/>看似各种操作都在对付下盘玩家，实则本场主队主场战绩虽好但克星终究是克星。<br/><br/>推荐:<br/><br/>鹿特丹不败。受平半赢。
         */

        public MatchBean match;
        public UserBean user;
        public String revenueStr;
        public RecommendationBean recommendation;
        public OddsBean odds;
        public String reason;

        public static class MatchBean {
            /**
             * betId : 1395633
             * timezoneoffset : 1513272600
             * leagueId : 28
             * leagueName : %E8%8D%B7%E7%94%B2
             * round : 16
             * season : 2017
             * hostTeamName : %E8%B5%AB%E6%8B%89%E5%85%8B
             * hostTeamId : 289
             * awayTeamName : %E9%B9%BF%E7%89%B9%E5%B7%B4%E8%BE%BE
             * awayTeamId : 284
             * hostScore : 0
             * awayScore : 0
             * hostHalfScore : 0
             * awayHalfScore : 0
             * hostRank : 10
             * awayRank : 17
             * status : NO_START
             * cup : 0
             * color : #FF6699
             * tape : 2500
             * logoH : 289
             * logoG : 284
             * fav_status : 0
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
            public int fav_status;
        }

        public static class UserBean {
            /**
             * verify_type : 3
             * headurl : http://dldemo-img.stor.sinaapp.com/header_1511157327.png
             * nickname : 竞猜310
             * user_type : 2
             * user_id : 188310
             * tag : ["实战高手"]
             * tag1 : []
             * reward_count_list : [1,8,18,28,88,188]
             * view_count : 641
             * followers_count : 1
             * praise_status : 0
             * coin : 0.5
             * praise_count : 0
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
            public List<?> tag1;
            public List<Integer> reward_count_list;
        }

        public static class RecommendationBean {
            /**
             * create_time : 1513227565
             * bet : {"awayOdds":"8500","tape":"2500"}
             * title : 赫拉克vs鹿特巴达：一场荷兰甲
             * status : 1
             * return_if_wrong : 0
             */

            public String create_time;
            public BetBean bet;
            public String title;
            public int status;
            public int return_if_wrong;

            public static class BetBean {
                /**
                 * awayOdds : 8500
                 * tape : 2500
                 */

                public String awayOdds;
                public String tape;
            }
        }

        public static class OddsBean {
            /**
             * asia : {"hostOdds":"1.09","awayOdds":0.85,"tape":"2500"}
             */

            public AsiaBean asia;

            public static class AsiaBean {
                /**
                 * hostOdds : 1.09
                 * awayOdds : 0.85
                 * tape : 2500
                 */

                public String hostOdds;
                public double awayOdds;
                public String tape;
            }
        }
    }
}
