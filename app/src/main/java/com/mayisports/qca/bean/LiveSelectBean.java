package com.mayisports.qca.bean;

import java.util.List;

/**
 * Created by Zpj on 2018/1/11.
 */

public class LiveSelectBean {

    /**
     * data : {"betId":"1487301","tvlink":[{"title":"PPTV高清","id":"2149"}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * betId : 1487301
         * tvlink : [{"title":"PPTV高清","id":"2149"}]
         */

        public String betId;
        public List<TvlinkBean> tvlink;

        public static class TvlinkBean {
            /**
             * title : PPTV高清
             * id : 2149
             */

            public String title;
            public String id;
            public String link;
        }
    }
}
