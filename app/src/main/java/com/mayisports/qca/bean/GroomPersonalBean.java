package com.mayisports.qca.bean;

import java.util.List;

/**
 * 推荐可能感兴趣的人 数据bean
 * Created by Zpj on 2018/1/30.
 */

public class GroomPersonalBean {

    /**
     * status : {"login":0}
     * data : [{"nickname":"小脸说足彩","headurl":"http://dldemo-img.stor.sinaapp.com/7201510029392.png","user_id":"194254","verify_type":"3","verify_reason":"对各个联赛，各个球队都有了解，能够结合球队基本面与盘面进行深入分析，对数据分析有自己的方法，擅长亚盘，欧盘，大小球等玩法，本女子也是一位曼联球迷。"},{"nickname":"尧舜解盘","headurl":"http://dldemo-img.stor.sinaapp.com/1971513584723.png","user_id":"196273","verify_type":"3","verify_reason":"多家知名平台受邀足彩专家，撰稿过大量赛前全面详细分析，某平台万人主播。团队包含回放比赛录像、总结球队打法、盘口大师和前操盘手，综合题材背景，基本面，盘口，受注心理等做最全面的详细分析推荐。"},{"nickname":"稳准赢","headurl":"http://dldemo-img.stor.sinaapp.com/header_1515570206.png","user_id":"187423","verify_type":"3","verify_reason":"稳是关键，盈利是目的，有的赚才是硬道理，期望你的支持和关注，长期跟单定盈利，足球是圆的，月有阴晴圆缺，欢迎跟踪验证，祝君常红"},{"nickname":"红单直播间","headurl":"http://dldemo-img.stor.sinaapp.com/2631511448136.png","user_id":"197565","verify_type":"3","verify_reason":"研究足球赛事5年以上，专注足球单场数据，对足球内部消息比较熟悉，对五大联赛相当熟悉，擅长乙级联赛，构建独到的购彩数学模型，科学投注，稳定收益！"},{"nickname":"司马爱彩","headurl":"http://dldemo-img.stor.sinaapp.com/header_1511151699.png","user_id":"196954","verify_type":"3","verify_reason":"足球自媒体荐彩专家，8年足彩经验，精研亚盘欧赔，掌握欧亚足坛博彩市场内第一手资料。每天精选心水推荐，助你解析盘赔背后的玄机"},{"nickname":"凯利波经","headurl":"http://dldemo-img.stor.sinaapp.com/3671515238417.png","user_id":"196919","verify_type":"3","verify_reason":"专业研究各大足球赛事，比赛结果预测(胜平负、进球数)，赛事情报推送，利用盘口走势、基本面和凯利指数看盘。"},{"nickname":"囍湿傅","headurl":"http://wx.qlogo.cn/mmopen/oNM054zmBSebmuEgpZBxLOiaN5sVcGQjUYKIQtDlITd9w5WSHjyEmCB5x8CHgv5o1D8mqqt8rhyl0MalYDcLr0E1aj3WHmsic0/0","user_id":"183888","verify_type":"3","verify_reason":"国内首家音乐综合类体育自媒体创始人之一。提倡理性投注，娱乐为主，巭孬嫑夯BET。微信公众号：V波体育资讯。"},{"nickname":"财神哥","headurl":"http://dldemo-img.stor.sinaapp.com/9141511870902.png","user_id":"194425","verify_type":"3","verify_reason":"专业玩球数年，专博大奶子，一起发财！每天有免费推荐，精准专业！微信公众号：足彩资深分析师财哥\r\n"},{"nickname":"南京第一球咖","headurl":"http://dldemo-img.stor.sinaapp.com/3681511222922.png","user_id":"185469","verify_type":"3","verify_reason":"看球20年，擅长分析欧洲五大联赛，欧冠，世界杯，欧洲杯等国家队赛事。对当今足坛各大豪门球队，各家球星也是了解甚多。善于从各个方面分析比赛。"},{"nickname":"红旗说球","headurl":"http://dldemo-img.stor.sinaapp.com/4281508060072.png","user_id":"198530","verify_type":"3","verify_reason":"你刚好需要，我刚好专业，漫漫竞彩路你我相遇相知则相交。多年竞彩经验，独有的分析数据模型，精通亚盘和欧赔，在数据中摸清菠菜公司思路，希望未来竞彩日子里，一路飘红。"},{"nickname":"足彩界的皇帝","headurl":"http://dldemo-img.stor.sinaapp.com/header_1516330079.png","user_id":"201680","verify_type":"3","verify_reason":"精通五大联赛巴西甲，日本联赛各队基本面，对盘口欧赔亚盘受注阻诱有些独到见解。人称浅盘小王子。熟知各大博彩公司对应的联赛等等，能最综合的分析比赛。"},{"nickname":"华仔の江湖","headurl":"http://dldemo-img.stor.sinaapp.com/7181512359754.png","user_id":"196890","verify_type":"3","verify_reason":"玩球15年，专业的比赛分析推荐，长期保持盈利率，擅长五大联赛，对亚盘欧盘结合，立博威廉欧盘盘口以及大小球盘口有着深入的掌握分析能力。"}]
     */

    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        /**
         * login : 0
         */

        public int login;
    }

    public static class DataBean {
        /**
         * nickname : 小脸说足彩
         * headurl : http://dldemo-img.stor.sinaapp.com/7201510029392.png
         * user_id : 194254
         * verify_type : 3
         *
         *
         * verify_reason : 对各个联赛，各个球队都有了解，能够结合球队基本面与盘面进行深入分析，对数据分析有自己的方法，擅长亚盘，欧盘，大小球等玩法，本女子也是一位曼联球迷。
         */

        public String nickname;
        public String headurl;
        public String user_id;
        public String verify_type;
        public String verify_reason;
        //手动创建
        public int follow_status;

        public String weibo_headurl;
        public String id;
    }
}
