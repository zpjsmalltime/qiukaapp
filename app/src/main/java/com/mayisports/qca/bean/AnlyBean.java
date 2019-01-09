package com.mayisports.qca.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 分析bean
 * Created by zhangpengju on 2018/6/12.
 */

public class AnlyBean {


    /**
     * status : {"login":1}
     * data : {"group_ranking":[{"score":15,"3":5,"id":"476","logoId":"476","name":"全北现代","goal":22,"fumble":9,"0":1},{"score":14,"3":4,"id":"1705","logoId":"1705","name":"阿赫利","goal":9,"fumble":4,"1":2},{"score":13,"3":4,"id":"7768","logoId":"7768","name":"天津松江","goal":15,"fumble":11,"1":1,"0":1},{"score":8,"3":2,"id":"3119","logoId":"3119","name":"加拉法","goal":12,"fumble":9,"0":2,"1":2},{"score":8,"0":2,"id":"1735","logoId":"1735","name":"亚吉拉","goal":9,"fumble":9,"3":2,"1":2},{"score":4,"0":4,"id":"232","logoId":"232","name":"柏太阳神","goal":6,"fumble":10,"1":1,"3":1},{"score":3,"0":5,"id":"3057","logoId":"3057","name":"杰志","goal":1,"fumble":14,"3":1},{"score":2,"0":4,"id":"9963","logoId":"9963","name":"特拉克托","goal":2,"fumble":10,"1":2}],"match":{"betId":"1499019","time":"1524049200","timezoneoffset":"1524049200","timeStr":"19:00","leagueId":"169","leagueName":"亚冠杯","round":"分组赛","hostTeamName":"天津权健","hostTeamId":"7768","awayTeamName":"柏太阳神","awayTeamId":"232","hostScore":"3","awayScore":"2","hostHalfScore":"2","awayHalfScore":"1","hostRank":"中超10","awayRank":"日职联7","stadium":"","temperature":"","weather":"","status":"COMPLETE","collection_status":"1","tvlink":"1","cup":1,"logoH":"7768","logoG":"232"},"status":0,"matchList":{"host":[{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"67","round":"11","hostRank":"10","awayRank":"7","betId":"1511708","match_time":"1526810400","hostTeamName":"天津松江","awayTeamName":"上海申花","leagueName":"中超","hostScore":"2","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1526810400"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"526","awayTeamId":"7768","round":"十六强","hostRank":"中超5","awayRank":"中超9","betId":"1536255","match_time":"1526385600","hostTeamName":"广州恒大","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"2","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"7500","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1526385600"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"68","round":"10","hostRank":"11","awayRank":"2","betId":"1511698","match_time":"1526124900","hostTeamName":"天津松江","awayTeamName":"山东鲁能","leagueName":"中超","hostScore":"1","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"0","scoreTape":"25000","scoreResult":3,"asiaResult":2,"timezoneoffset":"1526124900"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"526","round":"十六强","hostRank":"中超11","awayRank":"中超4","betId":"1536247","match_time":"1525779000","hostTeamName":"天津松江","awayTeamName":"广州恒大","leagueName":"亚冠杯","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-5000","scoreTape":"27500","scoreResult":3,"asiaResult":1,"timezoneoffset":"1525779000"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"110","awayTeamId":"7768","round":"9","hostRank":"","awayRank":"","betId":"1511692","match_time":"1525520100","hostTeamName":"重庆力帆","awayTeamName":"天津松江","leagueName":"中超","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"0","scoreTape":"25000","scoreResult":3,"asiaResult":1,"timezoneoffset":"1525520100"},{"leagueId":"82","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"533","round":"半准决赛","hostRank":"中超10","awayRank":"中超4","betId":"1537524","match_time":"1525174500","hostTeamName":"天津松江","awayTeamName":"江苏苏宁","leagueName":"中协杯","hostScore":"2","awayScore":"2","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"25000","scoreResult":1,"asiaResult":3,"timezoneoffset":"1525174500"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"15450","round":"8","hostRank":"","awayRank":"","betId":"1511681","match_time":"1524915300","hostTeamName":"天津松江","awayTeamName":"河北华夏","leagueName":"中超","hostScore":"0","awayScore":"3","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"27500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1524915300"},{"leagueId":"82","season":"2018","status":"COMPLETE","hostTeamId":"30395","awayTeamId":"7768","round":"第四轮","hostRank":"中乙北10","awayRank":"中超10","betId":"1534867","match_time":"1524555000","hostTeamName":"大连博阳","awayTeamName":"天津松江","leagueName":"中协杯","hostScore":"0","awayScore":"3","hostHalfScore":"0","awayHalfScore":"2","asiaTape":"-15000","scoreTape":"32500","scoreResult":3,"asiaResult":1,"timezoneoffset":"1524555000"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"12633","awayTeamId":"7768","round":"7","hostRank":"16","awayRank":"13","betId":"1511675","match_time":"1524382200","hostTeamName":"大连阿尔滨","awayTeamName":"天津松江","leagueName":"中超","hostScore":"1","awayScore":"1","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"-2500","scoreTape":"25000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1524382200"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"533","round":"6","hostRank":"10","awayRank":"6","betId":"1511664","match_time":"1523619300","hostTeamName":"天津松江","awayTeamName":"江苏苏宁","leagueName":"中超","hostScore":"1","awayScore":"1","hostHalfScore":"1","awayHalfScore":"0","asiaTape":"2500","scoreTape":"27500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1523619300"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"3057","awayTeamId":"7768","round":"分组赛","hostRank":"香港超1","awayRank":"中超13","betId":"1499017","match_time":"1522843200","hostTeamName":"杰志","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-20000","scoreTape":"35000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1522843200"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"526","round":"4","hostRank":"11","awayRank":"3","betId":"1511649","match_time":"1522409700","hostTeamName":"天津松江","awayTeamName":"广州恒大","leagueName":"中超","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-2500","scoreTape":"35000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1522409700"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"476","round":"分组赛","hostRank":"中超6","awayRank":"韩K联4","betId":"1499015","match_time":"1521028800","hostTeamName":"天津松江","awayTeamName":"全北现代","leagueName":"亚冠杯","hostScore":"4","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"-2500","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1521028800"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"3409","round":"2","hostRank":"3","awayRank":"12","betId":"1511632","match_time":"1520667000","hostTeamName":"天津松江","awayTeamName":"贵州人和","leagueName":"中超","hostScore":"1","awayScore":"2","hostHalfScore":"0","awayHalfScore":"1","asiaTape":"12500","scoreTape":"27500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1520667000"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"476","awayTeamId":"7768","round":"分组赛","hostRank":"韩K联3","awayRank":"中超2","betId":"1499013","match_time":"1520330400","hostTeamName":"全北现代","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"6","awayScore":"3","hostHalfScore":"2","awayHalfScore":"1","asiaTape":"7500","scoreTape":"27500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1520330400"}],"away":[{"leagueId":"140","season":"2018-2019","status":"COMPLETE","hostTeamId":"232","awayTeamId":"28401","round":"第二圈","hostRank":"日职联9","awayRank":"","betId":"1548425","match_time":"1528279200","hostTeamName":"柏太阳神","awayTeamName":"VONDS市原","leagueName":"日皇杯","hostScore":"6","awayScore":"0","hostHalfScore":"2","awayHalfScore":"0","asiaTape":"27500","scoreTape":"42500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1528279200"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"204","awayTeamId":"232","round":"15","hostRank":"18","awayRank":"13","betId":"1496735","match_time":"1526792400","hostTeamName":"名古屋","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"2","awayScore":"3","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"-5000","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1526792400"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"1679","round":"14","hostRank":"9","awayRank":"5","betId":"1496726","match_time":"1526104800","hostTeamName":"柏太阳神","awayTeamName":"川崎前锋","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"1","awayHalfScore":"0","asiaTape":"-2500","scoreTape":"25000","scoreResult":1,"asiaResult":3,"timezoneoffset":"1526104800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"221","round":"13","hostRank":"","awayRank":"","betId":"1496714","match_time":"1525496400","hostTeamName":"柏太阳神","awayTeamName":"磐田喜悦","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"5000","scoreTape":"22500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1525496400"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"1394","awayTeamId":"232","round":"12","hostRank":"","awayRank":"","betId":"1496708","match_time":"1525255200","hostTeamName":"湘南海洋","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"0","awayHalfScore":"2","asiaTape":"-5000","scoreTape":"22500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1525255200"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"227","awayTeamId":"232","round":"11","hostRank":"","awayRank":"","betId":"1496696","match_time":"1524891600","hostTeamName":"清水心跳","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"2","awayScore":"1","hostHalfScore":"2","awayHalfScore":"1","asiaTape":"-2500","scoreTape":"22500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1524891600"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"211","round":"10","hostRank":"","awayRank":"","betId":"1496691","match_time":"1524650400","hostTeamName":"柏太阳神","awayTeamName":"浦和红钻","leagueName":"日职联","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"25000","scoreResult":3,"asiaResult":1,"timezoneoffset":"1524650400"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"2705","round":"8","hostRank":"7","awayRank":"6","betId":"1496669","match_time":"1523682000","hostTeamName":"柏太阳神","awayTeamName":"札幌冈萨","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"5000","scoreTape":"22500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1523682000"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"229","round":"6","hostRank":"6","awayRank":"1","betId":"1496657","match_time":"1523167200","hostTeamName":"柏太阳神","awayTeamName":"广岛三箭","leagueName":"日职联","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","asiaTape":"2500","scoreTape":"22500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1523167200"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"476","round":"分组赛","hostRank":"日职联5","awayRank":"韩K联3","betId":"1499016","match_time":"1522837800","hostTeamName":"柏太阳神","awayTeamName":"全北现代","leagueName":"亚冠杯","hostScore":"0","awayScore":"2","hostHalfScore":"0","awayHalfScore":"1","asiaTape":"2500","scoreTape":"30000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1522837800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"226","round":"5","hostRank":"7","awayRank":"8","betId":"1496641","match_time":"1522405800","hostTeamName":"柏太阳神","awayTeamName":"神户胜利","leagueName":"日职联","hostScore":"2","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"5000","scoreTape":"22500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1522405800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"230","awayTeamId":"232","round":"4","hostRank":"18","awayRank":"8","betId":"1496635","match_time":"1521352800","hostTeamName":"大阪钢巴","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"2","awayScore":"2","hostHalfScore":"1","awayHalfScore":"2","asiaTape":"-2500","scoreTape":"25000","scoreResult":1,"asiaResult":3,"timezoneoffset":"1521352800"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"3057","awayTeamId":"232","round":"分组赛","hostRank":"香港超1","awayRank":"日职联8","betId":"1499014","match_time":"1521028800","hostTeamName":"杰志","awayTeamName":"柏太阳神","leagueName":"亚冠杯","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-25000","scoreTape":"37500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1521028800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"216","round":"3","hostRank":"8","awayRank":"9","betId":"1496629","match_time":"1520661600","hostTeamName":"柏太阳神","awayTeamName":"大阪樱花","leagueName":"日职联","hostScore":"1","awayScore":"1","hostHalfScore":"1","awayHalfScore":"0","asiaTape":"2500","scoreTape":"22500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1520661600"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"3057","round":"分组赛","hostRank":"日职联8","awayRank":"香港超1","betId":"1499012","match_time":"1520332200","hostTeamName":"柏太阳神","awayTeamName":"杰志","leagueName":"亚冠杯","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"30000","scoreTape":"42500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1520332200"}],"history":[{"status":"COMPLETE","hostTeamId":"232","awayTeamId":"7768","round":"分组赛","hostRank":"日职联4","awayRank":"中超3","betId":"1499010","match_time":"1519122600","hostTeamName":"柏太阳神","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"1","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","timezoneoffset":"1519122600"}]},"future3":{"home":[{"hostTeamName":"大连阿尔滨","awayTeamName":"天津松江","leagueName":"中超","match_time":"1524382200"},{"hostTeamName":"大连博阳","awayTeamName":"天津松江","leagueName":"中协杯","match_time":"1524555000"},{"hostTeamName":"天津松江","awayTeamName":"河北华夏","leagueName":"中超","match_time":"1524915300"}],"guest":[{"hostTeamName":"长崎航海","awayTeamName":"柏太阳神","leagueName":"日职联","match_time":"1524380400"},{"hostTeamName":"柏太阳神","awayTeamName":"浦和红钻","leagueName":"日职联","match_time":"1524650400"},{"hostTeamName":"清水心跳","awayTeamName":"柏太阳神","leagueName":"日职联","match_time":"1524891600"}]},"ranking":{"home":{"leagueId":60,"g":{"3":3,"score":10,"goal":9,"fumble":4,"0":1,"1":1,"rank":4},"a":{"count":11},"h":{"0":3,"score":5,"goal":5,"fumble":9,"1":2,"3":1,"rank":15},"rank":9},"guest":{"leagueId":33,"g":{"0":3,"score":10,"goal":10,"fumble":10,"1":1,"3":3,"rank":8},"a":{"count":15},"h":{"3":3,"score":10,"goal":9,"fumble":9,"1":1,"0":4,"rank":12},"rank":9}},"analysis":{"home":{"statics":[],"continuity":null,"asia":null,"match":[]},"guest":{"statics":[],"continuity":null,"asia":null,"match":[]}}}
     */

    public StatusBean status;
    public DataBean data;

    public static class StatusBean {
        /**
         * login : 1
         */

        public int login;
    }

    public static class DataBean {
        /**
         * group_ranking : [{"score":15,"3":5,"id":"476","logoId":"476","name":"全北现代","goal":22,"fumble":9,"0":1},{"score":14,"3":4,"id":"1705","logoId":"1705","name":"阿赫利","goal":9,"fumble":4,"1":2},{"score":13,"3":4,"id":"7768","logoId":"7768","name":"天津松江","goal":15,"fumble":11,"1":1,"0":1},{"score":8,"3":2,"id":"3119","logoId":"3119","name":"加拉法","goal":12,"fumble":9,"0":2,"1":2},{"score":8,"0":2,"id":"1735","logoId":"1735","name":"亚吉拉","goal":9,"fumble":9,"3":2,"1":2},{"score":4,"0":4,"id":"232","logoId":"232","name":"柏太阳神","goal":6,"fumble":10,"1":1,"3":1},{"score":3,"0":5,"id":"3057","logoId":"3057","name":"杰志","goal":1,"fumble":14,"3":1},{"score":2,"0":4,"id":"9963","logoId":"9963","name":"特拉克托","goal":2,"fumble":10,"1":2}]
         * match : {"betId":"1499019","time":"1524049200","timezoneoffset":"1524049200","timeStr":"19:00","leagueId":"169","leagueName":"亚冠杯","round":"分组赛","hostTeamName":"天津权健","hostTeamId":"7768","awayTeamName":"柏太阳神","awayTeamId":"232","hostScore":"3","awayScore":"2","hostHalfScore":"2","awayHalfScore":"1","hostRank":"中超10","awayRank":"日职联7","stadium":"","temperature":"","weather":"","status":"COMPLETE","collection_status":"1","tvlink":"1","cup":1,"logoH":"7768","logoG":"232"}
         * status : 0
         * matchList : {"host":[{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"67","round":"11","hostRank":"10","awayRank":"7","betId":"1511708","match_time":"1526810400","hostTeamName":"天津松江","awayTeamName":"上海申花","leagueName":"中超","hostScore":"2","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1526810400"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"526","awayTeamId":"7768","round":"十六强","hostRank":"中超5","awayRank":"中超9","betId":"1536255","match_time":"1526385600","hostTeamName":"广州恒大","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"2","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"7500","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1526385600"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"68","round":"10","hostRank":"11","awayRank":"2","betId":"1511698","match_time":"1526124900","hostTeamName":"天津松江","awayTeamName":"山东鲁能","leagueName":"中超","hostScore":"1","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"0","scoreTape":"25000","scoreResult":3,"asiaResult":2,"timezoneoffset":"1526124900"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"526","round":"十六强","hostRank":"中超11","awayRank":"中超4","betId":"1536247","match_time":"1525779000","hostTeamName":"天津松江","awayTeamName":"广州恒大","leagueName":"亚冠杯","hostScore":"0","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-5000","scoreTape":"27500","scoreResult":3,"asiaResult":1,"timezoneoffset":"1525779000"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"110","awayTeamId":"7768","round":"9","hostRank":"","awayRank":"","betId":"1511692","match_time":"1525520100","hostTeamName":"重庆力帆","awayTeamName":"天津松江","leagueName":"中超","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"0","scoreTape":"25000","scoreResult":3,"asiaResult":1,"timezoneoffset":"1525520100"},{"leagueId":"82","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"533","round":"半准决赛","hostRank":"中超10","awayRank":"中超4","betId":"1537524","match_time":"1525174500","hostTeamName":"天津松江","awayTeamName":"江苏苏宁","leagueName":"中协杯","hostScore":"2","awayScore":"2","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"25000","scoreResult":1,"asiaResult":3,"timezoneoffset":"1525174500"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"15450","round":"8","hostRank":"","awayRank":"","betId":"1511681","match_time":"1524915300","hostTeamName":"天津松江","awayTeamName":"河北华夏","leagueName":"中超","hostScore":"0","awayScore":"3","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"27500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1524915300"},{"leagueId":"82","season":"2018","status":"COMPLETE","hostTeamId":"30395","awayTeamId":"7768","round":"第四轮","hostRank":"中乙北10","awayRank":"中超10","betId":"1534867","match_time":"1524555000","hostTeamName":"大连博阳","awayTeamName":"天津松江","leagueName":"中协杯","hostScore":"0","awayScore":"3","hostHalfScore":"0","awayHalfScore":"2","asiaTape":"-15000","scoreTape":"32500","scoreResult":3,"asiaResult":1,"timezoneoffset":"1524555000"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"12633","awayTeamId":"7768","round":"7","hostRank":"16","awayRank":"13","betId":"1511675","match_time":"1524382200","hostTeamName":"大连阿尔滨","awayTeamName":"天津松江","leagueName":"中超","hostScore":"1","awayScore":"1","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"-2500","scoreTape":"25000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1524382200"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"533","round":"6","hostRank":"10","awayRank":"6","betId":"1511664","match_time":"1523619300","hostTeamName":"天津松江","awayTeamName":"江苏苏宁","leagueName":"中超","hostScore":"1","awayScore":"1","hostHalfScore":"1","awayHalfScore":"0","asiaTape":"2500","scoreTape":"27500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1523619300"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"3057","awayTeamId":"7768","round":"分组赛","hostRank":"香港超1","awayRank":"中超13","betId":"1499017","match_time":"1522843200","hostTeamName":"杰志","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-20000","scoreTape":"35000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1522843200"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"526","round":"4","hostRank":"11","awayRank":"3","betId":"1511649","match_time":"1522409700","hostTeamName":"天津松江","awayTeamName":"广州恒大","leagueName":"中超","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-2500","scoreTape":"35000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1522409700"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"476","round":"分组赛","hostRank":"中超6","awayRank":"韩K联4","betId":"1499015","match_time":"1521028800","hostTeamName":"天津松江","awayTeamName":"全北现代","leagueName":"亚冠杯","hostScore":"4","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"-2500","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1521028800"},{"leagueId":"60","season":"2018","status":"COMPLETE","hostTeamId":"7768","awayTeamId":"3409","round":"2","hostRank":"3","awayRank":"12","betId":"1511632","match_time":"1520667000","hostTeamName":"天津松江","awayTeamName":"贵州人和","leagueName":"中超","hostScore":"1","awayScore":"2","hostHalfScore":"0","awayHalfScore":"1","asiaTape":"12500","scoreTape":"27500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1520667000"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"476","awayTeamId":"7768","round":"分组赛","hostRank":"韩K联3","awayRank":"中超2","betId":"1499013","match_time":"1520330400","hostTeamName":"全北现代","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"6","awayScore":"3","hostHalfScore":"2","awayHalfScore":"1","asiaTape":"7500","scoreTape":"27500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1520330400"}],"away":[{"leagueId":"140","season":"2018-2019","status":"COMPLETE","hostTeamId":"232","awayTeamId":"28401","round":"第二圈","hostRank":"日职联9","awayRank":"","betId":"1548425","match_time":"1528279200","hostTeamName":"柏太阳神","awayTeamName":"VONDS市原","leagueName":"日皇杯","hostScore":"6","awayScore":"0","hostHalfScore":"2","awayHalfScore":"0","asiaTape":"27500","scoreTape":"42500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1528279200"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"204","awayTeamId":"232","round":"15","hostRank":"18","awayRank":"13","betId":"1496735","match_time":"1526792400","hostTeamName":"名古屋","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"2","awayScore":"3","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"-5000","scoreTape":"27500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1526792400"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"1679","round":"14","hostRank":"9","awayRank":"5","betId":"1496726","match_time":"1526104800","hostTeamName":"柏太阳神","awayTeamName":"川崎前锋","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"1","awayHalfScore":"0","asiaTape":"-2500","scoreTape":"25000","scoreResult":1,"asiaResult":3,"timezoneoffset":"1526104800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"221","round":"13","hostRank":"","awayRank":"","betId":"1496714","match_time":"1525496400","hostTeamName":"柏太阳神","awayTeamName":"磐田喜悦","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"5000","scoreTape":"22500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1525496400"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"1394","awayTeamId":"232","round":"12","hostRank":"","awayRank":"","betId":"1496708","match_time":"1525255200","hostTeamName":"湘南海洋","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"0","awayHalfScore":"2","asiaTape":"-5000","scoreTape":"22500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1525255200"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"227","awayTeamId":"232","round":"11","hostRank":"","awayRank":"","betId":"1496696","match_time":"1524891600","hostTeamName":"清水心跳","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"2","awayScore":"1","hostHalfScore":"2","awayHalfScore":"1","asiaTape":"-2500","scoreTape":"22500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1524891600"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"211","round":"10","hostRank":"","awayRank":"","betId":"1496691","match_time":"1524650400","hostTeamName":"柏太阳神","awayTeamName":"浦和红钻","leagueName":"日职联","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","scoreTape":"25000","scoreResult":3,"asiaResult":1,"timezoneoffset":"1524650400"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"2705","round":"8","hostRank":"7","awayRank":"6","betId":"1496669","match_time":"1523682000","hostTeamName":"柏太阳神","awayTeamName":"札幌冈萨","leagueName":"日职联","hostScore":"1","awayScore":"2","hostHalfScore":"1","awayHalfScore":"1","asiaTape":"5000","scoreTape":"22500","scoreResult":1,"asiaResult":3,"timezoneoffset":"1523682000"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"229","round":"6","hostRank":"6","awayRank":"1","betId":"1496657","match_time":"1523167200","hostTeamName":"柏太阳神","awayTeamName":"广岛三箭","leagueName":"日职联","hostScore":"0","awayScore":"1","hostHalfScore":"0","awayHalfScore":"1","asiaTape":"2500","scoreTape":"22500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1523167200"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"476","round":"分组赛","hostRank":"日职联5","awayRank":"韩K联3","betId":"1499016","match_time":"1522837800","hostTeamName":"柏太阳神","awayTeamName":"全北现代","leagueName":"亚冠杯","hostScore":"0","awayScore":"2","hostHalfScore":"0","awayHalfScore":"1","asiaTape":"2500","scoreTape":"30000","scoreResult":3,"asiaResult":3,"timezoneoffset":"1522837800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"226","round":"5","hostRank":"7","awayRank":"8","betId":"1496641","match_time":"1522405800","hostTeamName":"柏太阳神","awayTeamName":"神户胜利","leagueName":"日职联","hostScore":"2","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"5000","scoreTape":"22500","scoreResult":1,"asiaResult":1,"timezoneoffset":"1522405800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"230","awayTeamId":"232","round":"4","hostRank":"18","awayRank":"8","betId":"1496635","match_time":"1521352800","hostTeamName":"大阪钢巴","awayTeamName":"柏太阳神","leagueName":"日职联","hostScore":"2","awayScore":"2","hostHalfScore":"1","awayHalfScore":"2","asiaTape":"-2500","scoreTape":"25000","scoreResult":1,"asiaResult":3,"timezoneoffset":"1521352800"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"3057","awayTeamId":"232","round":"分组赛","hostRank":"香港超1","awayRank":"日职联8","betId":"1499014","match_time":"1521028800","hostTeamName":"杰志","awayTeamName":"柏太阳神","leagueName":"亚冠杯","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"-25000","scoreTape":"37500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1521028800"},{"leagueId":"33","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"216","round":"3","hostRank":"8","awayRank":"9","betId":"1496629","match_time":"1520661600","hostTeamName":"柏太阳神","awayTeamName":"大阪樱花","leagueName":"日职联","hostScore":"1","awayScore":"1","hostHalfScore":"1","awayHalfScore":"0","asiaTape":"2500","scoreTape":"22500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1520661600"},{"leagueId":"169","season":"2018","status":"COMPLETE","hostTeamId":"232","awayTeamId":"3057","round":"分组赛","hostRank":"日职联8","awayRank":"香港超1","betId":"1499012","match_time":"1520332200","hostTeamName":"柏太阳神","awayTeamName":"杰志","leagueName":"亚冠杯","hostScore":"1","awayScore":"0","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"30000","scoreTape":"42500","scoreResult":3,"asiaResult":3,"timezoneoffset":"1520332200"}],"history":[{"status":"COMPLETE","hostTeamId":"232","awayTeamId":"7768","round":"分组赛","hostRank":"日职联4","awayRank":"中超3","betId":"1499010","match_time":"1519122600","hostTeamName":"柏太阳神","awayTeamName":"天津松江","leagueName":"亚冠杯","hostScore":"1","awayScore":"1","hostHalfScore":"0","awayHalfScore":"0","asiaTape":"2500","timezoneoffset":"1519122600"}]}
         * future3 : {"home":[{"hostTeamName":"大连阿尔滨","awayTeamName":"天津松江","leagueName":"中超","match_time":"1524382200"},{"hostTeamName":"大连博阳","awayTeamName":"天津松江","leagueName":"中协杯","match_time":"1524555000"},{"hostTeamName":"天津松江","awayTeamName":"河北华夏","leagueName":"中超","match_time":"1524915300"}],"guest":[{"hostTeamName":"长崎航海","awayTeamName":"柏太阳神","leagueName":"日职联","match_time":"1524380400"},{"hostTeamName":"柏太阳神","awayTeamName":"浦和红钻","leagueName":"日职联","match_time":"1524650400"},{"hostTeamName":"清水心跳","awayTeamName":"柏太阳神","leagueName":"日职联","match_time":"1524891600"}]}
         * ranking : {"home":{"leagueId":60,"g":{"3":3,"score":10,"goal":9,"fumble":4,"0":1,"1":1,"rank":4},"a":{"count":11},"h":{"0":3,"score":5,"goal":5,"fumble":9,"1":2,"3":1,"rank":15},"rank":9},"guest":{"leagueId":33,"g":{"0":3,"score":10,"goal":10,"fumble":10,"1":1,"3":3,"rank":8},"a":{"count":15},"h":{"3":3,"score":10,"goal":9,"fumble":9,"1":1,"0":4,"rank":12},"rank":9}}
         * analysis : {"home":{"statics":[],"continuity":null,"asia":null,"match":[]},"guest":{"statics":[],"continuity":null,"asia":null,"match":[]}}
         */

        public MatchBean match;
        public int status;
        public MatchListBean matchList;
        public Future3Bean future3;
        public RankingBean ranking;
      //  public AnalysisBean analysis;
        public List<GroupRankingBean> group_ranking;

        public static class MatchBean {
            /**
             * betId : 1499019
             * time : 1524049200
             * timezoneoffset : 1524049200
             * timeStr : 19:00
             * leagueId : 169
             * leagueName : 亚冠杯
             * round : 分组赛
             * hostTeamName : 天津权健
             * hostTeamId : 7768
             * awayTeamName : 柏太阳神
             * awayTeamId : 232
             * hostScore : 3
             * awayScore : 2
             * hostHalfScore : 2
             * awayHalfScore : 1
             * hostRank : 中超10
             * awayRank : 日职联7
             * stadium :
             * temperature :
             * weather :
             * status : COMPLETE
             * collection_status : 1
             * tvlink : 1
             * cup : 1
             * logoH : 7768
             * logoG : 232
             */

            public String betId;
            public String time;
            public String timezoneoffset;
            public String timeStr;
            public String leagueId;
            public String leagueName;
            public String round;
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
            public String stadium;
            public String temperature;
            public String weather;
            public String status;
            public String collection_status;
            public String tvlink;
            public int cup;
            public String logoH;
            public String logoG;
        }

        public static class MatchListBean {
            public List<HostBean> host;
            public List<AwayBean> away;
            public List<HistoryBean> history;

            public static class HostBean {
                /**
                 * leagueId : 60
                 * season : 2018
                 * status : COMPLETE
                 * hostTeamId : 7768
                 * awayTeamId : 67
                 * round : 11
                 * hostRank : 10
                 * awayRank : 7
                 * betId : 1511708
                 * match_time : 1526810400
                 * hostTeamName : 天津松江
                 * awayTeamName : 上海申花
                 * leagueName : 中超
                 * hostScore : 2
                 * awayScore : 1
                 * hostHalfScore : 0
                 * awayHalfScore : 0
                 * asiaTape : 2500
                 * scoreTape : 27500
                 * scoreResult : 1
                 * asiaResult : 1
                 * timezoneoffset : 1526810400
                 */

                public String leagueId;
                public String season;
                public String status;
                public String hostTeamId;
                public String awayTeamId;
                public String round;
                public String hostRank;
                public String awayRank;
                public String betId;
                public String match_time;
                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public int hostScore;
                public int awayScore;
                public int hostHalfScore;
                public int awayHalfScore;
                public String asiaTape;
                public String scoreTape;
                public int scoreResult;
                public int asiaResult;
                public String timezoneoffset;
            }

            public static class AwayBean {
                /**
                 * leagueId : 140
                 * season : 2018-2019
                 * status : COMPLETE
                 * hostTeamId : 232
                 * awayTeamId : 28401
                 * round : 第二圈
                 * hostRank : 日职联9
                 * awayRank :
                 * betId : 1548425
                 * match_time : 1528279200
                 * hostTeamName : 柏太阳神
                 * awayTeamName : VONDS市原
                 * leagueName : 日皇杯
                 * hostScore : 6
                 * awayScore : 0
                 * hostHalfScore : 2
                 * awayHalfScore : 0
                 * asiaTape : 27500
                 * scoreTape : 42500
                 * scoreResult : 1
                 * asiaResult : 1
                 * timezoneoffset : 1528279200
                 */

                public String leagueId;
                public String season;
                public String status;
                public String hostTeamId;
                public String awayTeamId;
                public String round;
                public String hostRank;
                public String awayRank;
                public String betId;
                public String match_time;
                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public int hostScore;
                public int awayScore;
                public int hostHalfScore;
                public int awayHalfScore;
                public String asiaTape;
                public String scoreTape;
                public int scoreResult;
                public int asiaResult;
                public String timezoneoffset;
            }

            public static class HistoryBean {
                /**
                 * status : COMPLETE
                 * hostTeamId : 232
                 * awayTeamId : 7768
                 * round : 分组赛
                 * hostRank : 日职联4
                 * awayRank : 中超3
                 * betId : 1499010
                 * match_time : 1519122600
                 * hostTeamName : 柏太阳神
                 * awayTeamName : 天津松江
                 * leagueName : 亚冠杯
                 * hostScore : 1
                 * awayScore : 1
                 * hostHalfScore : 0
                 * awayHalfScore : 0
                 * asiaTape : 2500
                 * timezoneoffset : 1519122600
                 */

                public String status;
                public String hostTeamId;
                public String awayTeamId;
                public String round;
                public String hostRank;
                public String awayRank;
                public String betId;
                public String match_time;
                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public int hostScore;
                public int awayScore;
                public int hostHalfScore;
                public int awayHalfScore;
                public String asiaTape;
                public String timezoneoffset;
            }
        }

        public static class Future3Bean {
            public List<HomeBean> home;
            public List<GuestBean> guest;

            public static class HomeBean {
                /**
                 * hostTeamName : 大连阿尔滨
                 * awayTeamName : 天津松江
                 * leagueName : 中超
                 * match_time : 1524382200
                 */

                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public String match_time;
            }

            public static class GuestBean {
                /**
                 * hostTeamName : 长崎航海
                 * awayTeamName : 柏太阳神
                 * leagueName : 日职联
                 * match_time : 1524380400
                 */

                public String hostTeamName;
                public String awayTeamName;
                public String leagueName;
                public String match_time;
            }
        }

        public static class RankingBean {
            /**
             * home : {"leagueId":60,"g":{"3":3,"score":10,"goal":9,"fumble":4,"0":1,"1":1,"rank":4},"a":{"count":11},"h":{"0":3,"score":5,"goal":5,"fumble":9,"1":2,"3":1,"rank":15},"rank":9}
             * guest : {"leagueId":33,"g":{"0":3,"score":10,"goal":10,"fumble":10,"1":1,"3":3,"rank":8},"a":{"count":15},"h":{"3":3,"score":10,"goal":9,"fumble":9,"1":1,"0":4,"rank":12},"rank":9}
             */

            public HomeBeanX home;
            public GuestBeanX guest;

            public static class HomeBeanX {
                /**
                 * leagueId : 60
                 * g : {"3":3,"score":10,"goal":9,"fumble":4,"0":1,"1":1,"rank":4}
                 * a : {"count":11}
                 * h : {"0":3,"score":5,"goal":5,"fumble":9,"1":2,"3":1,"rank":15}
                 * rank : 9
                 */

                public int leagueId;
                public GBean g;
                public ABean a;
                public HBean h;
                public int rank;

                public static class GBean {
                    /**
                     * 3 : 3
                     * score : 10
                     * goal : 9
                     * fumble : 4
                     * 0 : 1
                     * 1 : 1
                     * rank : 4
                     */

                    @SerializedName("3")
                    public int _$3;
                    public int score;
                    public int goal;
                    public int fumble;
                    @SerializedName("0")
                    public int _$0;
                    @SerializedName("1")
                    public int _$1;
                    public int rank;
                }

                public static class ABean {
                    /**
                     * count : 11
                     */

                    public int count;
                }

                public static class HBean {
                    /**
                     * 0 : 3
                     * score : 5
                     * goal : 5
                     * fumble : 9
                     * 1 : 2
                     * 3 : 1
                     * rank : 15
                     */

                    @SerializedName("0")
                    public int _$0;
                    public int score;
                    public int goal;
                    public int fumble;
                    @SerializedName("1")
                    public int _$1;
                    @SerializedName("3")
                    public int _$3;
                    public int rank;
                }
            }

            public static class GuestBeanX {
                /**
                 * leagueId : 33
                 * g : {"0":3,"score":10,"goal":10,"fumble":10,"1":1,"3":3,"rank":8}
                 * a : {"count":15}
                 * h : {"3":3,"score":10,"goal":9,"fumble":9,"1":1,"0":4,"rank":12}
                 * rank : 9
                 */

                public int leagueId;
                public GBeanX g;
                public ABeanX a;
                public HBeanX h;
                public int rank;

                public static class GBeanX {
                    /**
                     * 0 : 3
                     * score : 10
                     * goal : 10
                     * fumble : 10
                     * 1 : 1
                     * 3 : 3
                     * rank : 8
                     */

                    @SerializedName("0")
                    public int _$0;
                    public int score;
                    public int goal;
                    public int fumble;
                    @SerializedName("1")
                    public int _$1;
                    @SerializedName("3")
                    public int _$3;
                    public int rank;
                }

                public static class ABeanX {
                    /**
                     * count : 15
                     */

                    public int count;
                }

                public static class HBeanX {
                    /**
                     * 3 : 3
                     * score : 10
                     * goal : 9
                     * fumble : 9
                     * 1 : 1
                     * 0 : 4
                     * rank : 12
                     */

                    @SerializedName("3")
                    public int _$3;
                    public int score;
                    public int goal;
                    public int fumble;
                    @SerializedName("1")
                    public int _$1;
                    @SerializedName("0")
                    public int _$0;
                    public int rank;
                }
            }
        }

        public static class AnalysisBean {
            /**
             * home : {"statics":[],"continuity":null,"asia":null,"match":[]}
             * guest : {"statics":[],"continuity":null,"asia":null,"match":[]}
             */

            public HomeBeanXX home;
            public GuestBeanXX guest;

            public static class HomeBeanXX {
                /**
                 * statics : []
                 * continuity : null
                 * asia : null
                 * match : []
                 */

                public Object continuity;
                public Object asia;
                public List<?> statics;
                public List<?> match;
            }

            public static class GuestBeanXX {
                /**
                 * statics : []
                 * continuity : null
                 * asia : null
                 * match : []
                 */

                public Object continuity;
                public Object asia;
                public List<?> statics;
                public List<?> match;
            }
        }

        public static class GroupRankingBean {
            /**
             * score : 15
             * 3 : 5
             * id : 476
             * logoId : 476
             * name : 全北现代
             * goal : 22
             * fumble : 9
             * 0 : 1
             * 1 : 2
             */

            public int score;
            @SerializedName("3")
            public int _$3;
            public String id;
            public String logoId;
            public String name;
            public int goal;
            public int fumble;
            @SerializedName("0")
            public int _$0;
            @SerializedName("1")
            public int _$1;
        }
    }
}
