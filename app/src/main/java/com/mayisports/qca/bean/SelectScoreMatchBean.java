package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2017/12/12.
 */

public class SelectScoreMatchBean {

    /**
     * status : {"login":0}
     * data : {"leagueFilter":[["41","意甲",1,"1",180,"7","1"],["20","德甲",1,"4",99,"7","1"],["55","东亚杯",1,"2",65,"1","1"],["24","法乙",1,"1",50,"7","0"],["29","荷乙",1,"1",39,"2","0"],["43","英超",1,"3",30,"7","0"],["128","苏冠",1,"1",12,"2","0"],["18","波兰超",1,"4",12,"2","0"],["28","荷甲",1,"2",6,"2","0"],["266","世俱杯",1,"2",5,"6","0"],["78","意杯",1,"1",4,"7","0"],["57","法联杯",1,"3",3,"7",null],["85","英足总杯",1,"6",1,"7","0"],["36","苏超",1,"2",1,"2","0"],["92","比利时杯",1,"2",0,"2","1"]]}
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
        public List<List<String>> leagueFilter;
    }
}
