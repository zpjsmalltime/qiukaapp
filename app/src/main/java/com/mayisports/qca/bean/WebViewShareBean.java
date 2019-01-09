package com.mayisports.qca.bean;

import android.support.test.runner.permission.ShellCommand;

import java.util.List;

/**
 *
 * 提示bean
 * Created by zhangpengju on 2018/5/9.
 */

public class WebViewShareBean {


    //分享按钮显示列表
    public List<String> jsApiList;


    public ShareEntry onMenuShareTimeline;
    public ShareEntry onMenuShareAppMessage;
    public ShareEntry onMenuShareQQ;
    public ShareEntry onMenuShareWeibo;
    public ShareEntry onMenuShareQZone;
    public ShareEntry miniProgram;

    public static class ShareEntry{
        public String title;  // 分享标题
        public String desc;     // 分享描述
        public String link;     // 分享链接
        public String imgUrl;   // 分享图标

        public String path; //分享路径
    }

    public  ShareEntry getBeanByType(int type){
        ShareEntry shareEntry = null;
        switch (type) {
            case 1://weibo
                shareEntry = onMenuShareWeibo;
                break;

            case 2:     //qq
                shareEntry = onMenuShareQQ;
                break;
            case 3:     //qzone
                shareEntry = onMenuShareQZone;
                break;
            case 4:     //wechat
                shareEntry = onMenuShareAppMessage;
                break;
            case 5:     //circle
                shareEntry = onMenuShareTimeline;
                break;
            case 6:     //小程序
                shareEntry = miniProgram;
                break;
        }
        return shareEntry;
    }
}
