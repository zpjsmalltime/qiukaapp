package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2017/12/12.
 */

public class SelectProcessBean {

    /**
     * status : {"login":0}
     * data : {"leagueFilter":[["110","塞尔超",1,"0",0,"2","0"],["265","埃及超",1,"0",0,"3","0"],["18","波兰超",1,"0",0,"2","0"],["20","德甲",1,"0",0,"7","1"],["28","荷甲",1,"0",0,"2","0"],["268","南非超",1,"0",0,"3","0"],["43","英超",1,"0",0,"7","0"],["36","苏超",1,"0",0,"2","0"],["66","葡杯",1,"0",0,"2","1"],["78","意杯",1,"0",0,"7","0"],["57","法联杯",1,"0",0,"7",null],["227","南球杯",0,"0",0,"4",null]]}
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
