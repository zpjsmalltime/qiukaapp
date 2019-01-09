package com.mayisports.qca.bean;

import java.util.List;

/**
 * 系统消息bean
 * Created by Zpj on 2018/1/24.
 */

public class SysMsgBean {

    /**
     * status : {"login":0,"id":"71640"}
     * data : [{"type":"10","read_time":"1516711396","title":"新评论通知","message":"大希解球回复了您在话题《伦敦德比战，一触即发》中的评论：\u201c你好，在呢。。。\u201d","itemId":"39180","create_time":"1516709502"},{"type":"10","read_time":"1516711396","title":"点赞通知","message":"司马爱彩给你在话题《伦敦德比战，一触即发》中的评论点赞啦！","itemId":"39367","create_time":"1516707567"},{"type":"10","read_time":"1516711396","title":"点赞通知","message":"司马爱彩给你在话题《伦敦德比战，一触即发》中的评论点赞啦！","itemId":"39366","create_time":"1516707566"},{"type":"0","read_time":"1516353567","title":"打赏成功","message":"您打赏给作家的1个金币已经到达对方的账户，感谢你的支持！","itemId":"0","create_time":"1516353288"},{"type":"0","read_time":"1516189117","title":"打赏成功","message":"您打赏给作家的1个金币已经到达对方的账户，感谢你的支持！","itemId":"0","create_time":"1516187523"},{"type":"10","read_time":"1516187049","title":"新评论通知","message":"迪巴拉别哭回复了您在话题《国王杯半决赛，西班牙人能否逆袭巴萨？》中的评论：\u201c的\u201d","itemId":"38767","create_time":"1516183371"},{"type":"10","read_time":"1516187049","title":"点赞通知","message":"迪巴拉别哭给你在话题《国王杯半决赛，西班牙人能否逆袭巴萨？》中的评论点赞啦！","itemId":"38767","create_time":"1516183352"},{"type":"10","read_time":"1516187049","title":"新评论通知","message":"迪巴拉别哭回复了您在话题《国王杯半决赛，西班牙人能否逆袭巴萨？》中的评论：\u201c哦\u201d","itemId":"38767","create_time":"1516183331"},{"type":"10","read_time":"1516167260","title":"点赞通知","message":"规律给你在话题《国王杯半决赛，西班牙人能否逆袭巴萨？》中的评论点赞啦！","itemId":"38762","create_time":"1516164432"},{"type":"12","read_time":"1516167260","title":"新粉丝通知","message":"您有新粉丝啦，赶紧点击查看吧！","itemId":"71640","create_time":"1516164405"}]
     */

    public StatusBean status;
    public List<DataBean> data;

    public static class StatusBean {
        /**
         * login : 0
         * id : 71640
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        /**
         * type : 10
         * read_time : 1516711396
         * title : 新评论通知
         * message : 大希解球回复了您在话题《伦敦德比战，一触即发》中的评论：“你好，在呢。。。”
         * itemId : 39180
         * create_time : 1516709502
         */

        public String type;
        public String read_time;
        public String title;
        public String message;
        public String itemId;
        public String create_time;
    }
}
