package com.mayisports.qca.bean;

import java.util.List;

/**
 * 情报站数据bean
 * Created by Zpj on 2018/1/15.
 */

public class InforPostBean {


    /**
     * data : {"matchInfoList":[{"infoList":[{"title":"【奥斯主场进球最少】奥斯本赛季打入24球，但是主场只打入8球，所有球队中主场进球最少，连续两个主场未能破门"},{"title":"【尼美根大量主力伤疑】主力射手拉伊希（6球），主力后卫约朋（3球）、主力后卫范德帕弗特（2球）主力前锋格隆维尔德（5球）等四人伤缺，另有两名替补感冒伤缺"},{"title":"【奥斯队长停赛】奥斯队长、主力后卫范登赫里克上轮吃到第5张黄牌，此役自动停赛"}],"match":{"hostTeamName":"奥斯","awayTeamName":"尼美根","leagueName":"荷乙","match_time":"1516042800","betId":"1401954","status":"NO_START","logoH":"292","logoG":"251"},"view_count":13981},{"infoList":[{"title":"【阿尔克青近7轮输6场】阿尔克青最近7轮比赛中输了6场，但是唯一的胜利在主场击败了排名第3的阿贾青年"},{"title":"【坎布尔新增两主力受伤】主力前锋丹尼尔斯（11场1球），主力中场马尼斯（7场）以及两名替补球员范德拉恩和罗西均继续伤缺"},{"title":"【阿尔克青一后卫缺阵】后卫范赖恩以及两名小将库普梅纳斯和哈吉迪亚科斯因为表现出色，被提拔一线队征战荷甲，本场无法出战"},{"title":"【坎布尔联赛客场4个月不胜】坎布尔本赛季联赛客场战绩平平，至今仅有2胜2平5负的成绩，位居中下游"}],"match":{"hostTeamName":"阿尔克青","awayTeamName":"坎布尔","leagueName":"荷乙","match_time":"1516042800","betId":"1401959","status":"NO_START","logoH":0,"logoG":"297"},"view_count":12211},{"infoList":[{"title":"【主力1/3进球来自头球】贝蒂斯本赛季联赛共打进30个进球，其中10粒头球高居联赛首位，占总进球数的三分之一"},{"title":"【客队对阵下半区球队不败】莱加内斯本赛季拿分主要力拼中下游球队，在面对西甲10名开外的球队时，战绩为6胜2平保持不败，本场对手皇家贝蒂斯目前暂列西甲第12位"}],"match":{"hostTeamName":"贝蒂斯","awayTeamName":"莱加内斯","leagueName":"西甲","match_time":"1516046400","betId":"1424789","status":"NO_START","logoH":"123","logoG":"859"},"view_count":7859},{"infoList":[{"title":"【主队场均进2.05球】曼联本赛季进攻端火热，22轮联赛过后仅3场交白卷，为英超第二少，目前已打入45个进球，场均进2.05球的进攻效率十分可观"},{"title":"【客队场均丢2.14球】斯托克城本赛季防守漏洞百出，22轮联赛过后仅2场零封，为英超最少，目前已经丢47球，场均丢2.14球的数据也是英超最高的"},{"title":"【客队或5人缺阵】斯托克城主力后卫肖克罗斯（15场1球）缺阵，彼得斯（19场0球）出战成疑。后卫因迪（8场0球）伤情不明朗。后卫约翰逊是长期伤号，前锋J罗（9场1球）因为个人原因也无法出"}],"match":{"hostTeamName":"曼联","awayTeamName":"斯托克城","leagueName":"英超","match_time":"1516046400","betId":"1395304","status":"NO_START","logoH":"55","logoG":"85"},"view_count":7340},{"infoList":[{"title":"【主队主场平局多】摩里伦斯主场有防平必要，本赛季8个主场打出4场平局，50%的平局率为葡超最高"},{"title":"【客队本赛季客场胜率为0】塞图巴尔防守奇差，本赛季17场葡超1场零封，为葡超最少，场均1.88的失球率也是第3高"}],"match":{"hostTeamName":"摩里伦斯","awayTeamName":"塞图巴尔","leagueName":"葡超","match_time":"1516042800","betId":"1415172","status":"NO_START","logoH":"839","logoG":"438"},"view_count":7281},{"infoList":[{"title":"【朗斯赢盘能力差】朗斯本赛季20场联赛只有7场赢盘，排名倒数第3，7次出大球也排名倒数第3"},{"title":"【索肖交锋不败率71%】索肖最近7次对阵朗斯取得4胜1平2负的战绩，本赛季首回合主场3比2战胜对手，心理上占一定优势"}],"match":{"hostTeamName":"朗斯","awayTeamName":"索肖","leagueName":"法乙","match_time":"1516045500","betId":"1430120","status":"NO_START","logoH":"225","logoG":"278"},"view_count":3252}]}
     */

    public DataBean data;

    public static class DataBean {
        public List<MatchInfoListBean> matchInfoList;

        public static class MatchInfoListBean {
            /**
             * infoList : [{"title":"【奥斯主场进球最少】奥斯本赛季打入24球，但是主场只打入8球，所有球队中主场进球最少，连续两个主场未能破门"},{"title":"【尼美根大量主力伤疑】主力射手拉伊希（6球），主力后卫约朋（3球）、主力后卫范德帕弗特（2球）主力前锋格隆维尔德（5球）等四人伤缺，另有两名替补感冒伤缺"},{"title":"【奥斯队长停赛】奥斯队长、主力后卫范登赫里克上轮吃到第5张黄牌，此役自动停赛"}]
             * match : {"hostTeamName":"奥斯","awayTeamName":"尼美根","leagueName":"荷乙","match_time":"1516042800","betId":"1401954","status":"NO_START","logoH":"292","logoG":"251"}
             * view_count : 13981
             */

            public MatchBean match;
            public int view_count;
            public List<InfoListBean> infoList;

            public static class MatchBean {
                /**
                 * hostTeamName : 奥斯
                 * awayTeamName : 尼美根
                 * leagueName : 荷乙
                 * match_time : 1516042800
                 * betId : 1401954
                 * status : NO_START
                 * logoH : 292
                 * logoG : 251
                 */

                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public String match_time;
                public String betId;
                public String status;
                public String logoH;
                public String logoG;
            }

            public static class InfoListBean {
                /**
                 * title : 【奥斯主场进球最少】奥斯本赛季打入24球，但是主场只打入8球，所有球队中主场进球最少，连续两个主场未能破门
                 */

                public String title;
            }
        }
    }
}
