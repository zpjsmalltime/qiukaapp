package com.mayisports.qca.bean;

import java.util.List;

/**
 * 发布推荐联赛数据bean
 * Created by Zpj on 2018/1/24.
 */

public class PublishGroomMatchBean {

    /**
     * status : {"login":0}
     * data : {"leagueFilter":[["17","比甲",1,"5",502,"2","0"],["41","意甲",1,"8",285,"7","1"],["77","西杯",1,"3",223,"7","0"],["21","德乙",1,"5",184,"7","0"],["28","荷甲",1,"7",78,"2","0"],["56","法国杯",1,"8",74,"7","0"],["79","英联杯",1,"1",37,"7","0"],["36","苏超",1,"10",27,"2","0"],["666","印度甲",1,"1",16,"1","0"],["265","埃及超",1,"3",14,"3","0"],["1265","印度超",1,"1",10,"1","0"],["155","巴圣锦标",1,"4",9,null,"0"],["441","葡联杯",1,"1",8,"2","1"],["150","墨西哥杯",1,"4",4,"4","0"],["14","阿甲",1,"8",1,"4","0"]]}
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
