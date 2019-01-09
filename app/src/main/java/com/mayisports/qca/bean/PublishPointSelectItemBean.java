package com.mayisports.qca.bean;

import java.util.List;

/**
 *
 * 发布观点  初始化加载比赛选项bean
 * Created by Zpj on 2018/1/23.
 */

public class PublishPointSelectItemBean {

    /**
     * data : {"topic":{"selection":[{"id":"4570","title":"阿森纳vs切尔西，赛果如何？","items":[{"id":"4571","title":"阿森纳赢1球以上"},{"id":"4572","title":"阿森纳赢1球"},{"id":"4573","title":"平局"},{"id":"4574","title":"切尔西赢"}]},{"id":"4575","title":"全场总进球数如何？","items":[{"id":"4576","title":"大于三球"},{"id":"4577","title":"三球"},{"id":"4578","title":"两球"},{"id":"4579","title":"小于两球"}]}]}}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * topic : {"selection":[{"id":"4570","title":"阿森纳vs切尔西，赛果如何？","items":[{"id":"4571","title":"阿森纳赢1球以上"},{"id":"4572","title":"阿森纳赢1球"},{"id":"4573","title":"平局"},{"id":"4574","title":"切尔西赢"}]},{"id":"4575","title":"全场总进球数如何？","items":[{"id":"4576","title":"大于三球"},{"id":"4577","title":"三球"},{"id":"4578","title":"两球"},{"id":"4579","title":"小于两球"}]}]}
         */

        public TopicBean topic;

        public static class TopicBean {
            public List<SelectionBean> selection;

            public static class SelectionBean {
                /**
                 * id : 4570
                 * title : 阿森纳vs切尔西，赛果如何？
                 * items : [{"id":"4571","title":"阿森纳赢1球以上"},{"id":"4572","title":"阿森纳赢1球"},{"id":"4573","title":"平局"},{"id":"4574","title":"切尔西赢"}]
                 */

                public String id;
                public String title;
                public List<ItemsBean> items;

                public static class ItemsBean {
                    /**
                     * id : 4571
                     * title : 阿森纳赢1球以上
                     */

                    public String id;
                    public String title;
                }
            }
        }
    }
}
