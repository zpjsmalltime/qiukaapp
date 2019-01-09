package com.mayisports.qca.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 竞猜大赛bean
 * Created by Zpj on 2018/1/16.
 */

public class GuessCompetitionBean {

    /**
     * status : {"login":1}
     * data : {"user":{"verify_type":"0","nickname":"球咖小宝贝儿","weibo_headurl":"http://dldemo-img.stor.sinaapp.com/header_1514952789.png","user_id":"71640"},"bet_match":{"cur_bonus":7166.16175,"match_index":"201803","ranking_date_range":"01.15-01.22","close_time":"01.22 12:00"},"ranklist":[{"new":"5","user_id":"196182","nickname":"我总是中奖","verify_type":"0","rank":"1","pre_rank":"15","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_7.png","bonus":"221","bet_count":"5","score":"5.99","revenue":"219.8%","accuracy_text":"5中5"},{"new":"0","user_id":"184615","nickname":"绿茵精灵","verify_type":"0","rank":"2","pre_rank":"18","headurl":"http://dldemo-img.stor.sinaapp.com/5821494594285.png","bonus":"118","bet_count":"5","score":"5.70","revenue":"214.0%","accuracy_text":"5中5"},{"new":"6","user_id":"192264","nickname":"随心","verify_type":"0","rank":"3","pre_rank":"11","headurl":"http://dldemo-img.stor.sinaapp.com/2371494044182.png","bonus":"69","bet_count":"5","score":"5.38","revenue":"207.6%","accuracy_text":"5中4"},{"new":"6","user_id":"197522","nickname":"138****3562","verify_type":"0","rank":"4","pre_rank":"1","headurl":"http://dldemo-img.stor.sinaapp.com/6071507128991.png","bonus":"37","bet_count":"5","score":"4.30","revenue":"186.0%","accuracy_text":"5中4"},{"new":"5","user_id":"199162","nickname":"五零五","verify_type":"0","rank":"5","pre_rank":"21","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_4.png","bonus":"35","bet_count":"5","score":"3.91","revenue":"178.2%","accuracy_text":"5中4"},{"new":"1","user_id":"189832","nickname":"刘大红","verify_type":"0","rank":"6","pre_rank":"25","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_6.png","bonus":"34","bet_count":"5","score":"3.61","revenue":"172.2%","accuracy_text":"5中4"},{"new":"1","user_id":"185467","nickname":"138****9686","verify_type":"0","rank":"7","pre_rank":"40","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_4.png","bonus":"32","bet_count":"5","score":"3.38","revenue":"167.6%","accuracy_text":"5中4"},{"new":"0","user_id":"195538","nickname":"看球就困","verify_type":"0","rank":"8","pre_rank":"58","headurl":"http://dldemo-img.stor.sinaapp.com/9411498834993.png","bonus":"30","bet_count":"5","score":"2.31","revenue":"146.3%","accuracy_text":"5中4"},{"new":"1","user_id":"185760","nickname":"老顽童","verify_type":"0","rank":"9","pre_rank":"27","headurl":"http://dldemo-img.stor.sinaapp.com/801494579675.png","bonus":"28","bet_count":"5","score":"2.27","revenue":"145.4%","accuracy_text":"5中3"},{"new":"0","user_id":"195513","nickname":"重走青春路","verify_type":"0","rank":"10","pre_rank":"61","headurl":"http://dldemo-img.stor.sinaapp.com/header_1510215271.png","bonus":"26","bet_count":"5","score":"2.21","revenue":"144.2%","accuracy_text":"5中4"},{"new":"1","user_id":"191574","nickname":"天才少年","verify_type":"0","rank":"11","pre_rank":"12","headurl":"http://dldemo-img.stor.sinaapp.com/6671512748996.png","bonus":"25","bet_count":"6","score":"2.50","revenue":"141.8%","accuracy_text":"6中4"},{"new":"1","user_id":"183888","nickname":"囍湿傅","verify_type":"3","rank":"12","pre_rank":"13","headurl":"http://wx.qlogo.cn/mmopen/oNM054zmBSebmuEgpZBxLOiaN5sVcGQjUYKIQtDlITd9w5WSHjyEmCB5x8CHgv5o1D8mqqt8rhyl0MalYDcLr0E1aj3WHmsic0/0","bonus":"23","bet_count":"5","score":"1.91","revenue":"138.2%","accuracy_text":"5中4"},{"new":"0","user_id":"194190","nickname":"d\u2006d","verify_type":"0","rank":"13","pre_rank":"14","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_3.png","bonus":"21","bet_count":"5","score":"1.90","revenue":"138.0%","accuracy_text":"5中3"},{"new":"6","user_id":"200297","nickname":"八戒爱mm","verify_type":"0","rank":"14","pre_rank":"15","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_7.png","bonus":"19","bet_count":"5","score":"1.78","revenue":"135.6%","accuracy_text":"5中3"},{"new":"1","user_id":"190707","nickname":"我爱你","verify_type":"0","rank":"15","pre_rank":"16","headurl":"http://dldemo-img.stor.sinaapp.com/3311510653565.png","bonus":"17","bet_count":"5","score":"1.70","revenue":"134.0%","accuracy_text":"5中4"},{"new":"1","user_id":"200422","nickname":"136****7385","verify_type":"0","rank":"16","pre_rank":"17","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_3.png","bonus":"16","bet_count":"5","score":"1.59","revenue":"131.8%","accuracy_text":"5中4"},{"new":"5","user_id":"197179","nickname":"老马博彩","verify_type":"0","rank":"17","pre_rank":"18","headurl":"http://dldemo-img.stor.sinaapp.com/1631503105542.png","bonus":"14","bet_count":"5","score":"1.01","revenue":"120.2%","accuracy_text":"5中3"},{"new":"0","user_id":"184962","nickname":"123木头人","verify_type":"0","rank":"18","pre_rank":"21","headurl":"http://dldemo-img.stor.sinaapp.com/1061492732316.png","bonus":"12","bet_count":"7","score":"-0.09","revenue":"98.7%","accuracy_text":"7中4"},{"new":"5","user_id":"200437","nickname":"175****7045","verify_type":"0","rank":"19","pre_rank":"22","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_2.png","bonus":"10","bet_count":"5","score":"-0.12","revenue":"97.6%","accuracy_text":"5中3"},{"new":"2","user_id":"184670","nickname":"cl","verify_type":"0","rank":"20","pre_rank":"23","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_7.png","bonus":"8.3","bet_count":"5","score":"-0.21","revenue":"95.7%","accuracy_text":"5中3"}]}
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
         * user : {"verify_type":"0","nickname":"球咖小宝贝儿","weibo_headurl":"http://dldemo-img.stor.sinaapp.com/header_1514952789.png","user_id":"71640"}
         * bet_match : {"cur_bonus":7166.16175,"match_index":"201803","ranking_date_range":"01.15-01.22","close_time":"01.22 12:00"}
         * ranklist : [{"new":"5","user_id":"196182","nickname":"我总是中奖","verify_type":"0","rank":"1","pre_rank":"15","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_7.png","bonus":"221","bet_count":"5","score":"5.99","revenue":"219.8%","accuracy_text":"5中5"},{"new":"0","user_id":"184615","nickname":"绿茵精灵","verify_type":"0","rank":"2","pre_rank":"18","headurl":"http://dldemo-img.stor.sinaapp.com/5821494594285.png","bonus":"118","bet_count":"5","score":"5.70","revenue":"214.0%","accuracy_text":"5中5"},{"new":"6","user_id":"192264","nickname":"随心","verify_type":"0","rank":"3","pre_rank":"11","headurl":"http://dldemo-img.stor.sinaapp.com/2371494044182.png","bonus":"69","bet_count":"5","score":"5.38","revenue":"207.6%","accuracy_text":"5中4"},{"new":"6","user_id":"197522","nickname":"138****3562","verify_type":"0","rank":"4","pre_rank":"1","headurl":"http://dldemo-img.stor.sinaapp.com/6071507128991.png","bonus":"37","bet_count":"5","score":"4.30","revenue":"186.0%","accuracy_text":"5中4"},{"new":"5","user_id":"199162","nickname":"五零五","verify_type":"0","rank":"5","pre_rank":"21","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_4.png","bonus":"35","bet_count":"5","score":"3.91","revenue":"178.2%","accuracy_text":"5中4"},{"new":"1","user_id":"189832","nickname":"刘大红","verify_type":"0","rank":"6","pre_rank":"25","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_6.png","bonus":"34","bet_count":"5","score":"3.61","revenue":"172.2%","accuracy_text":"5中4"},{"new":"1","user_id":"185467","nickname":"138****9686","verify_type":"0","rank":"7","pre_rank":"40","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_4.png","bonus":"32","bet_count":"5","score":"3.38","revenue":"167.6%","accuracy_text":"5中4"},{"new":"0","user_id":"195538","nickname":"看球就困","verify_type":"0","rank":"8","pre_rank":"58","headurl":"http://dldemo-img.stor.sinaapp.com/9411498834993.png","bonus":"30","bet_count":"5","score":"2.31","revenue":"146.3%","accuracy_text":"5中4"},{"new":"1","user_id":"185760","nickname":"老顽童","verify_type":"0","rank":"9","pre_rank":"27","headurl":"http://dldemo-img.stor.sinaapp.com/801494579675.png","bonus":"28","bet_count":"5","score":"2.27","revenue":"145.4%","accuracy_text":"5中3"},{"new":"0","user_id":"195513","nickname":"重走青春路","verify_type":"0","rank":"10","pre_rank":"61","headurl":"http://dldemo-img.stor.sinaapp.com/header_1510215271.png","bonus":"26","bet_count":"5","score":"2.21","revenue":"144.2%","accuracy_text":"5中4"},{"new":"1","user_id":"191574","nickname":"天才少年","verify_type":"0","rank":"11","pre_rank":"12","headurl":"http://dldemo-img.stor.sinaapp.com/6671512748996.png","bonus":"25","bet_count":"6","score":"2.50","revenue":"141.8%","accuracy_text":"6中4"},{"new":"1","user_id":"183888","nickname":"囍湿傅","verify_type":"3","rank":"12","pre_rank":"13","headurl":"http://wx.qlogo.cn/mmopen/oNM054zmBSebmuEgpZBxLOiaN5sVcGQjUYKIQtDlITd9w5WSHjyEmCB5x8CHgv5o1D8mqqt8rhyl0MalYDcLr0E1aj3WHmsic0/0","bonus":"23","bet_count":"5","score":"1.91","revenue":"138.2%","accuracy_text":"5中4"},{"new":"0","user_id":"194190","nickname":"d\u2006d","verify_type":"0","rank":"13","pre_rank":"14","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_3.png","bonus":"21","bet_count":"5","score":"1.90","revenue":"138.0%","accuracy_text":"5中3"},{"new":"6","user_id":"200297","nickname":"八戒爱mm","verify_type":"0","rank":"14","pre_rank":"15","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_7.png","bonus":"19","bet_count":"5","score":"1.78","revenue":"135.6%","accuracy_text":"5中3"},{"new":"1","user_id":"190707","nickname":"我爱你","verify_type":"0","rank":"15","pre_rank":"16","headurl":"http://dldemo-img.stor.sinaapp.com/3311510653565.png","bonus":"17","bet_count":"5","score":"1.70","revenue":"134.0%","accuracy_text":"5中4"},{"new":"1","user_id":"200422","nickname":"136****7385","verify_type":"0","rank":"16","pre_rank":"17","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_3.png","bonus":"16","bet_count":"5","score":"1.59","revenue":"131.8%","accuracy_text":"5中4"},{"new":"5","user_id":"197179","nickname":"老马博彩","verify_type":"0","rank":"17","pre_rank":"18","headurl":"http://dldemo-img.stor.sinaapp.com/1631503105542.png","bonus":"14","bet_count":"5","score":"1.01","revenue":"120.2%","accuracy_text":"5中3"},{"new":"0","user_id":"184962","nickname":"123木头人","verify_type":"0","rank":"18","pre_rank":"21","headurl":"http://dldemo-img.stor.sinaapp.com/1061492732316.png","bonus":"12","bet_count":"7","score":"-0.09","revenue":"98.7%","accuracy_text":"7中4"},{"new":"5","user_id":"200437","nickname":"175****7045","verify_type":"0","rank":"19","pre_rank":"22","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_2.png","bonus":"10","bet_count":"5","score":"-0.12","revenue":"97.6%","accuracy_text":"5中3"},{"new":"2","user_id":"184670","nickname":"cl","verify_type":"0","rank":"20","pre_rank":"23","headurl":"http://dldemo-img.stor.sinaapp.com/default_head_7.png","bonus":"8.3","bet_count":"5","score":"-0.21","revenue":"95.7%","accuracy_text":"5中3"}]
         */

        public UserBean user;
        public BetMatchBean bet_match;
        public List<RanklistBean> ranklist;
        public List<BonusList> bonuslist;

        public static class BonusList{
            public String match_index;
            public String rank;
            public String bonus;
        }

        public static class UserBean {
            /**
             * verify_type : 0
             * nickname : 球咖小宝贝儿
             * weibo_headurl : http://dldemo-img.stor.sinaapp.com/header_1514952789.png
             * user_id : 71640
             *
             accuracy_text
             :
             "1中0"
             bet_count
             :
             "1"
             bonus
             :
             "1.1"
             nickname
             :
             "迪巴拉别哭"
             pre_rank
             :
             "835"
             rank
             :
             "820"
             score
             :
             "-1.00"
             user_id
             :
             "200034"
             value
             :
             "0.00"
             verify_type
             :
             null
             weibo_headurl
             :

             *
             */

            public String weibo_headurl;

            public String user_id;
            public String nickname;
            public String verify_type;
            public String rank;
            public String pre_rank;
            public String bonus;
            public String bet_count;
            public String score;
            public String value;
            public String accuracy_text;
        }

        public static class BetMatchBean {
            /**
             * cur_bonus : 7166.16175
             * match_index : 201803
             * ranking_date_range : 01.15-01.22
             * close_time : 01.22 12:00
             */

            public double cur_bonus;
            public String match_index;
            public String ranking_date_range;
            public String close_time;
        }

        public static class RanklistBean {
            /**
             * new : 5
             * user_id : 196182
             * nickname : 我总是中奖
             * verify_type : 0
             * rank : 1
             * pre_rank : 15
             * headurl : http://dldemo-img.stor.sinaapp.com/default_head_7.png
             * bonus : 221
             * bet_count : 5
             * score : 5.99
             * revenue : 219.8%
             * accuracy_text : 5中5
             */

            @SerializedName("new")
            public String newX;
            public String user_id;
            public String nickname;
            public String verify_type;
            public String rank;
            public String pre_rank;
            public String headurl;
            public String bonus;
            public String bet_count;
            public String score;
            public String revenue;
            public String accuracy_text;
        }
    }
}
