package com.mayisports.qca.bean;

import java.util.List;

/**
 * 我的粉丝数据
 * Created by Zpj on 2018/2/8.
 */

public class FansListBean {

    /**
     * data : {"followers_count":"14","list":[{"user_id":"198159","nickname":"喵喵的大小球","headurl":"http://dldemo-img.stor.sinaapp.com/3361514694487.png","verify_type":"3","follow_status":1},{"user_id":"194425","nickname":"财神哥","headurl":"http://dldemo-img.stor.sinaapp.com/9141511870902.png","verify_type":"3","follow_status":1},{"user_id":"183748","nickname":"YOU先生足球推荐","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511841530.png","verify_type":"3","follow_status":1},{"user_id":"196890","nickname":"华仔の江湖","headurl":"http://dldemo-img.stor.sinaapp.com/7181512359754.png","verify_type":"3","follow_status":1},{"user_id":"196954","nickname":"司马爱彩","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511151699.png","verify_type":"3","follow_status":1},{"user_id":"200451","nickname":"华夫饼","headurl":"http://dldemo-img.stor.sinaapp.com/1161513324035.png","verify_type":"0","follow_status":0},{"user_id":"197556","nickname":"155****7692","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_8.png","verify_type":"0","follow_status":0},{"user_id":"198355","nickname":"芭蕉足球","headurl":"http://dldemo-img.stor.sinaapp.com/1241507470311.png","verify_type":"3","follow_status":1},{"user_id":"188252","nickname":"四月一号","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_5.png","verify_type":"0","follow_status":1},{"user_id":"186518","nickname":"烟雨平生","headurl":"http://dldemo-img.stor.sinaapp.com/9151500830482.png","verify_type":"0","follow_status":0}]}
     */

    public DataBean data;

    public static class DataBean {
        /**
         * followers_count : 14
         * list : [{"user_id":"198159","nickname":"喵喵的大小球","headurl":"http://dldemo-img.stor.sinaapp.com/3361514694487.png","verify_type":"3","follow_status":1},{"user_id":"194425","nickname":"财神哥","headurl":"http://dldemo-img.stor.sinaapp.com/9141511870902.png","verify_type":"3","follow_status":1},{"user_id":"183748","nickname":"YOU先生足球推荐","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511841530.png","verify_type":"3","follow_status":1},{"user_id":"196890","nickname":"华仔の江湖","headurl":"http://dldemo-img.stor.sinaapp.com/7181512359754.png","verify_type":"3","follow_status":1},{"user_id":"196954","nickname":"司马爱彩","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511151699.png","verify_type":"3","follow_status":1},{"user_id":"200451","nickname":"华夫饼","headurl":"http://dldemo-img.stor.sinaapp.com/1161513324035.png","verify_type":"0","follow_status":0},{"user_id":"197556","nickname":"155****7692","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_8.png","verify_type":"0","follow_status":0},{"user_id":"198355","nickname":"芭蕉足球","headurl":"http://dldemo-img.stor.sinaapp.com/1241507470311.png","verify_type":"3","follow_status":1},{"user_id":"188252","nickname":"四月一号","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_5.png","verify_type":"0","follow_status":1},{"user_id":"186518","nickname":"烟雨平生","headurl":"http://dldemo-img.stor.sinaapp.com/9151500830482.png","verify_type":"0","follow_status":0}]
         */

        public String followers_count;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * user_id : 198159
             * nickname : 喵喵的大小球
             * headurl : http://dldemo-img.stor.sinaapp.com/3361514694487.png
             * verify_type : 3
             * follow_status : 1
             */

            public String user_id;
            public String nickname;
            public String headurl;
            public String verify_type;
            public int follow_status;
        }
    }
}
