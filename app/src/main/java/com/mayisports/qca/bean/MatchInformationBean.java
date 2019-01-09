package com.mayisports.qca.bean;

import java.util.List;

/**
 * 比赛情报数据bean
 * Created by zhangpengju on 2018/5/28.
 */

public class MatchInformationBean {


    /**
     * data : {"status":0,"match":{"weather":"雷陣雨","temperature":"18℃～19℃","hostRank":"1","awayRank":"19","leagueId":"41","hostTeamId":"189","awayTeamId":"549","isCup":"0","hostHalfScore":"0","awayHalfScore":"0","status":"COMPLETE","timezoneoffset":"1526734800","hostTeamName":"尤文图斯","awayTeamName":"维罗纳","leagueName":"意甲","round":"38","hostScore":"2","awayScore":"1","logoH":"189","logoG":"549","collection_status":0,"tvlink":"2"},"information":[{"type":"5","subtype":"0","create_time":"1526717306","information":"@ 渣叔聊球：本场开出主让两球半/三球1.74的初盘，尤文作为上盘近10场比赛赢盘6场，赢盘率高达60%，本场还是布冯小将的告别仪式及球队意甲冠军的颁奖仪式。推荐：尤文让两球半/三球","level":"1","index":"0"},{"type":"5","subtype":"0","create_time":"1526717379","information":"@足綵在线研究：主场盘口双双开始历史相对深一个盘2.5的初盘，临场升到2.75.也是间接预示主队存在穿盘的可能性，而且很大。推荐：让球主胜","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526664919","information":"球探网：维罗纳","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526692934","information":"香港马会：大球","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526694326","information":"东方日报：小球","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526699533","information":"中国竞彩网：维罗纳","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526699653","information":"有料体育：尤文图斯","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526699653","information":"雪缘园：尤文图斯","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526717714","information":"苹果日报：维罗纳","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526718562","information":"星岛日报：尤文图斯","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526721571","information":"捷报网：维罗纳","level":"1","index":"0"},{"type":"3","subtype":"0","create_time":"1526716758","information":"【尤文提前1轮夺冠】上轮联赛，尤文图斯客场0比0战平罗马，联赛还剩1轮的情况下，高出第2的那不勒斯4分，提前1轮夺冠。至此，尤文实现意甲7连霸的同时，其联赛夺冠次数也升至34次。","level":"1","index":"0"},{"type":"3","subtype":"0","create_time":"1526716780","information":"【维罗纳提前降入意乙】升班马维罗纳，本赛季表现的一直很挣扎，联赛还剩1轮的情况下，已经落后保级区足足10分，确定降入下赛季的意乙联赛。","level":"1","index":"0"},{"type":"3","subtype":"0","create_time":"1526716924","information":"【尤文5人伤停】主力后卫基耶利尼（疑似），替补后卫赫韦德斯（疑似），主力中场赫迪拉（疑似），轮换中场贝纳尔代斯基，主力中场夸德拉多。","level":"2","index":"0"},{"type":"3","subtype":"0","create_time":"1526716959","information":"【维罗纳阵容完整】维罗纳此役无新增伤停，中场扎卡格尼（6场）继续因伤缺阵，球队阵容接近完整。","level":"2","index":"0"},{"type":"3","subtype":"0","create_time":"1526717127","information":"【尤文冲击联赛零封记录】上轮联赛与罗马0-0是本赛季第22次零封对手，也是意甲单赛季零封场次的纪录，一旦在本轮对战维罗纳的比赛中再次零封对手尤文将创造意甲单赛季零封场次新纪录。","level":"3","index":"0"},{"type":"3","subtype":"0","create_time":"1526717161","information":"【维罗纳主帅下课】维罗纳主帅佩齐亚已经基本确定下课，本赛季在巴里执教的伟大的意大利后卫格罗索如今成为了维罗纳的目标人选。","level":"3","index":"0"},{"type":"2","subtype":null,"create_time":"1526716700","information":"维罗纳 是意甲降级球队，本赛季意甲积25分排名第19，已经提前降级进入意乙，上轮联赛主场0比1不敌中下游球队乌迪内斯。","level":"1","index":"0"},{"type":"1","subtype":null,"create_time":"1526716700","information":"尤文图斯 是意甲班霸，本赛季意甲积92分排名第1，已经提前一轮获得联赛冠军，上轮联赛客场0比0战平实力强大的罗马。","level":"1","index":"0"}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * status : 0
         * match : {"weather":"雷陣雨","temperature":"18℃～19℃","hostRank":"1","awayRank":"19","leagueId":"41","hostTeamId":"189","awayTeamId":"549","isCup":"0","hostHalfScore":"0","awayHalfScore":"0","status":"COMPLETE","timezoneoffset":"1526734800","hostTeamName":"尤文图斯","awayTeamName":"维罗纳","leagueName":"意甲","round":"38","hostScore":"2","awayScore":"1","logoH":"189","logoG":"549","collection_status":0,"tvlink":"2"}
         * information : [{"type":"5","subtype":"0","create_time":"1526717306","information":"@ 渣叔聊球：本场开出主让两球半/三球1.74的初盘，尤文作为上盘近10场比赛赢盘6场，赢盘率高达60%，本场还是布冯小将的告别仪式及球队意甲冠军的颁奖仪式。推荐：尤文让两球半/三球","level":"1","index":"0"},{"type":"5","subtype":"0","create_time":"1526717379","information":"@足綵在线研究：主场盘口双双开始历史相对深一个盘2.5的初盘，临场升到2.75.也是间接预示主队存在穿盘的可能性，而且很大。推荐：让球主胜","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526664919","information":"球探网：维罗纳","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526692934","information":"香港马会：大球","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526694326","information":"东方日报：小球","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526699533","information":"中国竞彩网：维罗纳","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526699653","information":"有料体育：尤文图斯","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526699653","information":"雪缘园：尤文图斯","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526717714","information":"苹果日报：维罗纳","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526718562","information":"星岛日报：尤文图斯","level":"1","index":"0"},{"type":"4","subtype":null,"create_time":"1526721571","information":"捷报网：维罗纳","level":"1","index":"0"},{"type":"3","subtype":"0","create_time":"1526716758","information":"【尤文提前1轮夺冠】上轮联赛，尤文图斯客场0比0战平罗马，联赛还剩1轮的情况下，高出第2的那不勒斯4分，提前1轮夺冠。至此，尤文实现意甲7连霸的同时，其联赛夺冠次数也升至34次。","level":"1","index":"0"},{"type":"3","subtype":"0","create_time":"1526716780","information":"【维罗纳提前降入意乙】升班马维罗纳，本赛季表现的一直很挣扎，联赛还剩1轮的情况下，已经落后保级区足足10分，确定降入下赛季的意乙联赛。","level":"1","index":"0"},{"type":"3","subtype":"0","create_time":"1526716924","information":"【尤文5人伤停】主力后卫基耶利尼（疑似），替补后卫赫韦德斯（疑似），主力中场赫迪拉（疑似），轮换中场贝纳尔代斯基，主力中场夸德拉多。","level":"2","index":"0"},{"type":"3","subtype":"0","create_time":"1526716959","information":"【维罗纳阵容完整】维罗纳此役无新增伤停，中场扎卡格尼（6场）继续因伤缺阵，球队阵容接近完整。","level":"2","index":"0"},{"type":"3","subtype":"0","create_time":"1526717127","information":"【尤文冲击联赛零封记录】上轮联赛与罗马0-0是本赛季第22次零封对手，也是意甲单赛季零封场次的纪录，一旦在本轮对战维罗纳的比赛中再次零封对手尤文将创造意甲单赛季零封场次新纪录。","level":"3","index":"0"},{"type":"3","subtype":"0","create_time":"1526717161","information":"【维罗纳主帅下课】维罗纳主帅佩齐亚已经基本确定下课，本赛季在巴里执教的伟大的意大利后卫格罗索如今成为了维罗纳的目标人选。","level":"3","index":"0"},{"type":"2","subtype":null,"create_time":"1526716700","information":"维罗纳 是意甲降级球队，本赛季意甲积25分排名第19，已经提前降级进入意乙，上轮联赛主场0比1不敌中下游球队乌迪内斯。","level":"1","index":"0"},{"type":"1","subtype":null,"create_time":"1526716700","information":"尤文图斯 是意甲班霸，本赛季意甲积92分排名第1，已经提前一轮获得联赛冠军，上轮联赛客场0比0战平实力强大的罗马。","level":"1","index":"0"}]
         */

        public int status;
        public MatchBean match;
        public List<InformationBean> information;

        public static class MatchBean {
            /**
             * weather : 雷陣雨
             * temperature : 18℃～19℃
             * hostRank : 1
             * awayRank : 19
             * leagueId : 41
             * hostTeamId : 189
             * awayTeamId : 549
             * isCup : 0
             * hostHalfScore : 0
             * awayHalfScore : 0
             * status : COMPLETE
             * timezoneoffset : 1526734800
             * hostTeamName : 尤文图斯
             * awayTeamName : 维罗纳
             * leagueName : 意甲
             * round : 38
             * hostScore : 2
             * awayScore : 1
             * logoH : 189
             * logoG : 549
             * collection_status : 0
             * tvlink : 2
             */

            public String weather;
            public String temperature;
            public String hostRank;
            public String awayRank;
            public String leagueId;
            public String hostTeamId;
            public String awayTeamId;
            public String isCup;
            public String hostHalfScore;
            public String awayHalfScore;
            public String status;
            public String timezoneoffset;
            public String hostTeamName;
            public String awayTeamName;
            public String leagueName;
            public String round;
            public String hostScore;
            public String awayScore;
            public String logoH;
            public String logoG;
            public int collection_status;
            public String tvlink;
        }

        public static class InformationBean {
            /**
             * type : 5
             * subtype : 0
             * create_time : 1526717306
             * information : @ 渣叔聊球：本场开出主让两球半/三球1.74的初盘，尤文作为上盘近10场比赛赢盘6场，赢盘率高达60%，本场还是布冯小将的告别仪式及球队意甲冠军的颁奖仪式。推荐：尤文让两球半/三球
             * level : 1
             * index : 0
             */

            public String type;
            public String subtype;
            public String create_time;
            public String information;
            public String level;
            public String index;
        }
    }
}
