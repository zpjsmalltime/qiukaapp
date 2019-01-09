package com.mayisports.qca.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.activity.HomeItemDetailActivity;
import com.mob.MobSDK;
import com.mob.commons.SHARESDK;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;


import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WXMiniProgramObject;

/**
 * 分享工具类
 * Created by Zpj on 2017/12/21.
 */

public class ShareUtils {



    public static void shareMsg(int platform, String title, String text, String url, String imageUrl, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());


        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
             //   weibo_sp.setTitle(title);
                weibo_sp.setText(text);
            //    weibo_sp.setUrl(url);
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                qzone_sp.setTitle(title);
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                wechat_sp.setTitle(title);
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
            case 6: //小程序

                Wechat.ShareParams wechat_s = new Wechat.ShareParams();
                wechat_s.setShareType(Platform.SHARE_WXMINIPROGRAM);
                wechat_s.setTitle(title);
                wechat_s.setText(text);
                wechat_s.setUrl("http://www.qq.com");
                wechat_s.setImageUrl(imageUrl);
                wechat_s.setWxUserName("gh_aa1f18c3b1e7");
                wechat_s.setWxPath(url);
                Platform wechat1 = ShareSDK.getPlatform(Wechat.NAME);
                wechat1.setPlatformActionListener(platformActionListener);
                wechat1.share(wechat_s);


                break;
        }

    }


    /**
     * vip 分享
     * @param platform
     * @param platformActionListener
     */
    public static void shareMsgForApplyV(int platform, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
        String url = "http://app.mayisports.com/applyV/applyV.html";
        String title = "";
        String text = "";
        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
             //   weibo_sp.setTitle(title);
                text = "#球咖#足彩分析师召集，百万现金诚邀足彩高手！点击查看详细分析"+ url;
                weibo_sp.setText(text);
            //    weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "百万现金诚邀足彩高手";
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = "#足彩分析师#来球咖分享你对足球赛事的观点，为彩民指路，还能赚钱";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                qzone_sp.setTitle(title);
                text = "#足彩分析师#百万现金诚邀足彩高手";
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);


                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "百万现金诚邀足彩高手";

                wechat_sp.setTitle(title);
                text = "#足彩分析师#来球咖分享你对足球赛事的观点，为彩民指路，还能赚钱";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "百万现金诚邀足彩高手";
                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }


    /**
     * 话题分享
     * @param platform
     * @param topicId
     * @param imgUrl
     * @param topicTitle
     * @param topicDes
     * @param platformActionListener
     *
     *
     *
     * let topicUrl = '/topic/' + this.topicId + '_' + userId;
      url = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7cfd1bd726cb4a01&
      redirect_uri=' + encodeURIComponent('https://app.mayisports.com')
     + '&response_type=code&scope=snsapi_userinfo&state=' + encodeURIComponent(topicUrl) + '#wechat_redirect'
     *
     *
     *
     */
    public static void shareMsgForTopic(int platform,String topicId,String imgUrl,String topicTitle,String topicDes, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
        if(imgUrl != null){
            imageUrl = imgUrl;
        }
        //Constant.BASE_URL+"#/topic/"+topic_id
        String url = "http://app.mayisports.com/#/topic/"+topicId+"_"+getUserId();
//        String url = "http://app.mayisports.com/#/share/topic?id="+topicId+"&shareUserId="+getUserId()+"&action=topic";

        String mayiUrl = "https://app.mayisports.com";
        mayiUrl = URLEncoder.encode(mayiUrl);

        String paramsUrl ="/topic/"+topicId+"_"+getUserId();
        paramsUrl = URLEncoder.encode(paramsUrl);




        String title = "";
        String text = "";
        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "#球咖#"+topicTitle+url;

                text = "球咖,足球界的段子手都在这儿";
                text = title;
             //   weibo_sp.setTitle(title);
                weibo_sp.setText(text);
             //   weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
                if(imgUrl != null){
                    imageUrl = imgUrl;
                }
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = topicDes;
                text = "球咖,足球界的段子手都在这儿";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;
                text = "球咖,足球界的段子手都在这儿";
                qzone_sp.setTitle(title);
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat

                url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7cfd1bd726cb4a01&redirect_uri="+mayiUrl
                        +"&response_type=code&scope=snsapi_userinfo&state=" + paramsUrl
                        +"#wechat_redirect";

                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;

                wechat_sp.setTitle(title);
                text = topicDes;
//                text =  text.replaceAll("现金","").replaceAll("提现币","").replaceAll("可提现","").replaceAll("红包","");
                text = "球咖,足球界的段子手都在这儿";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle

                url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx7cfd1bd726cb4a01&redirect_uri="+mayiUrl
                        +"&response_type=code&scope=snsapi_userinfo&state=" + paramsUrl
                        +"#wechat_redirect";


                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;
                text = "球咖,足球界的段子手都在这儿";
                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }


    public static String getUserId(){
        return SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID);
    }

    /**
     * 分享观点无话题
     * @param platform
     * @param platformActionListener
     *
     *视频
     */
    public static void shareMsgForPointNoTopic(int platform,String commentId,String point,String nickName,String videoImg, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
//        String url = "http://app.mayisports.com/#/topicViewDetail/"+commentId+"_"+getUserId();
        String url = "http://app.mayisports.com/#/share/topic?id="+commentId+"&shareUserId="+getUserId();

        String title = "";
        String text = "";
        point = point.replaceAll("<[^>]*>|\\n|&nbsp","");
        if(point.length()>30){
            point = point.substring(0,30)+"...";
        }

        //<[^>]*>|\n|&nbsp

        String videoImgUrl = "";
        if(!TextUtils.isEmpty(videoImg)){

//            String urlHost = "http://topicimg.appchizi.com/w_300,h_300,p_dG9waWNpbWcvcWt0XzIwMDA0OTE1MjQwMzk4OTEucG5n-150-center-0-0/yda/topicimg/";
            String urlHost = "http://topicimg.appchizi.com/p_dG9waWNpbWcvcWt0XzIwMDA0OTE1MjQ2MjkyNDkucG5n-0-center-0-0/yda/topicimg/";
            int las = videoImg.lastIndexOf("/");
            String videoName = videoImg.substring(las+1);
            int i = videoName.lastIndexOf(".");
            videoName = videoName.substring(0,i)+".png";
            videoImgUrl = urlHost+ videoName;

//            String imgUrl = videoImg.substring(0, videoImg.lastIndexOf(".")) + ".png";
            imageUrl = videoImgUrl;
        }
        switch (platform) {
            case 1://weibo   #球咖#昵称 发布了新动态：观点（超出30个字符，则截取并加…）   点击查看详细分析
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);


                title = "#球咖#"+point+url;

                text = title;
          //      weibo_sp.setTitle(title);
                weibo_sp.setText(text);
           //     weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";

                if(!TextUtils.isEmpty(videoImg)){
                    String urlHost = "http://topicimg.appchizi.com/p_dG9waWNpbWcvcWt0XzIwMDA0OTE1MjQ2MjkyNDkucG5n-0-center-0-0/yda/topicimg/";
                    int las = videoImg.lastIndexOf("/");
                    String videoName = videoImg.substring(las+1);
                    int i = videoName.lastIndexOf(".");
                    videoName = videoName.substring(0,i)+".png";
                    videoImgUrl = urlHost+ videoName;

//            String imgUrl = videoImg.substring(0, videoImg.lastIndexOf(".")) + ".png";
                    imageUrl = videoImgUrl;
                }

                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "来自"+nickName+"的球咖动态";

                title = "【视频】"+point;
                text = "球咖,足球界的段子手都在这儿";

                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);

                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;

                title = "【视频】"+point;
                text = "球咖,足球界的段子手都在这儿";

                qzone_sp.setTitle(title);

                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "来自"+nickName+"的球咖动态";


                title = "【视频】"+point;
                text = "球咖,足球界的段子手都在这儿";
                wechat_sp.setTitle(title);

                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;


                title = "【视频】"+point;
                text = "球咖,足球界的段子手都在这儿";

                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }

    /**
     * 分享观点无话题
     * @param platform
     * @param platformActionListener
     *
     * type 1 普通动态  2，长文章
     */
    public static void shareMsgForPointNoTopic(int platform, String commentId, String point, String nickName, List<String> imgUrls,int type,String titleLong, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
//        String url = "http://app.mayisports.com/#/topicViewDetail/"+commentId+"_"+getUserId();

        String url = "http://app.mayisports.com/#/share/topic?id="+commentId+"&shareUserId="+getUserId();

        String title = "";
        String text = "";
        point = point.replaceAll("<[^>]*>|\\n|&nbsp","");


        //<[^>]*>|\n|&nbsp


        if(imgUrls.size()>0){
            imageUrl = imgUrls.get(0);
        }

        switch (platform) {
            case 1://weibo   #球咖#昵称 发布了新动态：观点（超出30个字符，则截取并加…）   点击查看详细分析

                if(point.length()>100){
                    point = point.substring(0,100)+"...";
                }
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);


                title = "#球咖#"+point+url;

                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = "#球咖#"+titleLong+url;

                }
                text = title;
         //       weibo_sp.setTitle(title);
                weibo_sp.setText(text);
          //      weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
                if(imgUrls.size()>0){
                    String[] strings = new String[imgUrls.size()];
                    String[] strings1 = imgUrls.toArray(strings);

                    if(type == 1) {
//                        weibo_sp.setImageArray(strings1);
                        weibo_sp.setImageUrl(imgUrls.get(0));
                    }else if(type == 2){
                        weibo_sp.setImageUrl(imgUrls.get(0));
                    }

                }else{
                    weibo_sp.setImageUrl(imageUrl);
                }


                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "来自"+nickName+"的球咖动态";


                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }


                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);

                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }

                qzone_sp.setTitle(title);
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "来自"+nickName+"的球咖动态";

                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }


                wechat_sp.setTitle(title);

                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }

                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }



    /**
     * 分享转发回复
     * @param platform
     * @param platformActionListener
     *
     *
     * http://localhost:3030/#/share/topic?id=108197&shareUserId=200034
     */
    public static void shareMsgForShareContent(int platform, String commentId,String reply_id, String point,String replyStr, String nickName, List<String> imgUrls,int type,String titleLong, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
        String url = "http://app.mayisports.com/#/share/topic?id="+commentId+"&shareUserId="+getUserId()+"&reply_id="+reply_id;
        String title = "";
        String text = "";



        //<[^>]*>|\n|&nbsp

        point = delPoint(point,replyStr);


        if(imgUrls.size()>0){
            imageUrl = imgUrls.get(0);

            if(imageUrl.endsWith(".mp4")) {
                imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf(".")) + ".png";
            }
        }

        switch (platform) {
            case 1://weibo   #球咖#昵称 发布了新动态：观点（超出30个字符，则截取并加…）   点击查看详细分析


                if(!TextUtils.isEmpty(point)) {
                    point = point.replaceAll("<[^>]*>|\\n|&nbsp", "");
                }else{
                    point = replyStr;
                }


                if(point.length()>100){
                    point = point.substring(0,100)+"...";
                }
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);


                title = "#球咖#"+point+url;

                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = "#球咖#"+titleLong+url;

                }
                text = title;
                //       weibo_sp.setTitle(title);
                weibo_sp.setText(text);
                //      weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";

                if(imgUrls.size()>0){
                    imageUrl = imgUrls.get(0);
                    if(imageUrl.endsWith(".mp4")) {
                        imageUrl = imageUrl.substring(0, imageUrl.lastIndexOf(".")) + ".png";
                    }
                }

//                if(imgUrls.size()>0){
//
//
//                    if(type == 1) {
////                        weibo_sp.setImageArray(strings1);
//                        weibo_sp.setImageUrl(imgUrls.get(0));
//                    }else if(type == 2){
//                        weibo_sp.setImageUrl(imgUrls.get(0));
//                    }
//
//
//
//
//                }else{
                    weibo_sp.setImageUrl(imageUrl);
//                }


                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq





                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "来自"+nickName+"的球咖动态";


                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }


                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);

                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone






                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }

                qzone_sp.setTitle(title);
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat




                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "来自"+nickName+"的球咖动态";

                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }


                wechat_sp.setTitle(title);

                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle


                if(point.length()>30){
                    point = point.substring(0,30)+"...";
                }
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = point;
                text = "球咖,足球界的段子手都在这儿";
                if(type == 2){//长文章
                    title = titleLong;

                }

                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }


    /**
     * 处理转发原动态被删除文案
     * @param point
     * @return
     */
    private static String delPoint(String point,String replyStr){
        if(!TextUtils.isEmpty(point)) {
            point = point.replaceAll("<[^>]*>|\\n|&nbsp", "");
        }else{
            point = replyStr;
        }
        return point;
    }





    /**
     * 分享观点有话题
     * @param platform
     * @param platformActionListener
     */
    public static void shareMsgForPointHasTopic(int platform,String commentId,String topicTitle,String nickName, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
//        String url = "http://app.mayisports.com/#/topicViewDetail/"+commentId+"_"+getUserId();
        String url = "http://app.mayisports.com/#/share/topic?id="+commentId+"&shareUserId="+getUserId();

        String title = "";
        String text = "";
        switch (platform) {
            case 1://weibo



                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);

                title = "#球咖#"+topicTitle+ ""+url;
                text = title;
            //    weibo_sp.setTitle(title);
                weibo_sp.setText(text);
           //     weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = "来自"+nickName+"的球咖动态";
                text = "球咖,足球界的段子手都在这儿";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;
                text = "来自"+nickName+"的球咖动态";
                text = "球咖,足球界的段子手都在这儿";
                qzone_sp.setTitle(title);
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;

                wechat_sp.setTitle(title);
                text = "来自"+nickName+"的球咖动态";
                text = "球咖,足球界的段子手都在这儿";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = topicTitle;
                text = "来自"+nickName+"的球咖动态";
                text = "球咖,足球界的段子手都在这儿";
                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }


    /**
     * 个人中心分享
     * @param platform
     * @param id
     * @param nickname
     * @param platformActionListener
     */
    public static void shareMsgForPersonalDetails(int platform,String id, String nickname,String headUrl,PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/static/img/logo.png";
        String url = "http://app.mayisports.com/#/personHomepage/"+id+",6?mid="+getUserId();
        String title = "";
        String text = "";
        if(!TextUtils.isEmpty(headUrl)){
            imageUrl = headUrl;
        }
        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
            //    weibo_sp.setTitle(title);
                text = "#球咖# "+ nickname + ":我在球咖app里聊球,快来关注我!"+ url;
                weibo_sp.setText(text);
            //    weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
                if(!TextUtils.isEmpty(headUrl)){
                    imageUrl = headUrl;
                }
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = nickname + ":我在球咖app里聊球,快来关注我!";
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = "球咖,足球界的段子手都在这儿";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = nickname + ":我在球咖app里聊球,快来关注我!";
                qzone_sp.setTitle(title);
                text = "球咖,足球界的段子手都在这儿";
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title =  nickname + ":我在球咖app里聊球,快来关注我!";

                wechat_sp.setTitle(title);
                text = "球咖,足球界的段子手都在这儿";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title =  nickname + ":我在球咖app里聊球,快来关注我!";
                circle_sp.setTitle(title);
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }




    /**
     * 分享短视频
     * @param platform
     * @param platformActionListener
     */
    public static void shareMsgForShootVideo(int platform,String shareUserId,String commentId,String videoTitle,String imgUrl,String headUrl,String nickName, PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = imgUrl;

        /**
         * sn: 分享人名称,  sAvatar: 分享人头像地址,  id: comment_id
         *
         *
         */

        if(LoginUtils.isLogin()){
            headUrl = SPUtils.getString(QCaApplication.getContext(),Constant.HEADER_URl);
            nickName = SPUtils.getString(QCaApplication.getContext(),Constant.NICK_NAME);
            shareUserId = getUserId();
        }


        if(!TextUtils.isEmpty(headUrl) && headUrl.contains(".com/")){
            headUrl = headUrl.split(".com/")[1];
        }

        if(!TextUtils.isEmpty(headUrl)){
            headUrl = URLEncoder.encode(headUrl);
        }else{
            headUrl = "";
        }


        String encode = URLEncoder.encode(nickName);

        String url = "http://app.mayisports.com/share/video?id="+commentId+"&sn="+encode+"&sAvatar="+headUrl+"&utm_source="+platform+"&mid="+shareUserId+"&p=video";
        String title = "";
        String text = "";
//        point = point.replaceAll("<[^>]*>|\\n|&nbsp","");
//        Log.e("正则",point);
//        if(point.length()>30){
//            point = point.substring(0,30)+"...";
//        }

        //<[^>]*>|\n|&nbsp


        switch (platform) {
            case 1://weibo   #球咖#昵称 发布了新动态：观点（超出30个字符，则截取并加…）   点击查看详细分析
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);

                // #球咖#短视频内容 点击查看
                title = "#球咖#"+videoTitle+""+url;

                text = "球咖,给你跟多有趣的体育内容";
                text = title;
           //     weibo_sp.setTitle(title);
                weibo_sp.setText(text);
           //     weibo_sp.setUrl(url);
                imageUrl = imgUrl;
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq

                /**
                 * 短视频内容

                 b)     球咖，优质体育内容社区|资讯|短视频|话题讨论|段子|美女|搞笑
                 */

                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = videoTitle;
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = "球咖,足球界的段子手都在这儿";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = videoTitle;
                qzone_sp.setTitle(title);
                text = "球咖,足球界的段子手都在这儿";
                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                /**
                 *
                 */
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = videoTitle;

                wechat_sp.setTitle(title);
                text = "球咖,足球界的段子手都在这儿";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle

                /**
                 * a)     短视频内容

                 b)     球咖，优质体育内容社区|资讯|短视频|话题讨论|段子|美女|搞笑
                 */
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = videoTitle;
                circle_sp.setTitle(title);
                text = "球咖,足球界的段子手都在这儿";
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }



    /**
     * 竞猜大赛分享
     * @param platform
     * @param platformActionListener
     */
    public static void shareMsgForGuessing(int platform,String imgUrl,PlatformActionListener platformActionListener) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/mobileFiles/contest.png";
        String url = "https://app.mayisports.com/#/quizContest";
        String title = "";
        String text = "";
//        if(!TextUtils.isEmpty(imgUrl)){
//            imageUrl = imgUrl;
//        }
        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
              //  weibo_sp.setTitle(title);
                text = "#球咖#【足球竞猜大赛】免费参战,100%赢取现金奖励"+ url;
                weibo_sp.setText(text);
              //  weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
//                if(!TextUtils.isEmpty(imgUrl)){
//                    imageUrl = imgUrl;
//                }
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "【足球竞猜大赛】免费参战,100%赢取现金奖励";
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = "球咖竞猜大赛,球迷彩迷谁能更胜一筹?";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone
                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "【足球竞猜大赛】免费参战,100%赢取现金奖励";
                qzone_sp.setTitle(title);
                text = "球咖竞猜大赛,球迷彩迷谁能更胜一筹?";

                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "【足球竞猜大赛】免费参战,100%赢取现金奖励";

                wechat_sp.setTitle(title);
                text = "球咖竞猜大赛,球迷彩迷谁能更胜一筹?";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                title = "【足球竞猜大赛】免费参战,100%赢取现金奖励";
                circle_sp.setTitle(title);
                text = "球咖竞猜大赛,球迷彩迷谁能更胜一筹?";
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }


    /**
     * 分享方案单
     * @param platform
     */
    public static void shareMsgForGroom(int platform,String urlShare, String titleGroom,String nickName,String vs,PlatformActionListener platformActionListener) {
        MobSDK.init(QCaApplication.getContext());
        String imageUrl = "http://app.mayisports.com/mobileFiles/WechatIMG744.png";
        String url = "";
        String title = "";
        String text = "";
        if(!TextUtils.isEmpty(titleGroom)){
            title = "【免费给你看场私单】"+titleGroom;
        }else{
            title = "【免费给你看场私单】"+nickName+":"+vs;
        }
        url = urlShare;
        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
             //   weibo_sp.setTitle(title);
                text = "#球咖#"+ title+url;
                weibo_sp.setText(text);
              //  weibo_sp.setUrl(url);
                imageUrl = "http://app.mayisports.com/static/img/logo500.png";
//                if(!TextUtils.isEmpty(imgUrl)){
//                    imageUrl = imgUrl;
//                }
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(platformActionListener);
                weibo.share(weibo_sp);
                break;

            case 2:     //qq
                QQ.ShareParams qq_sp = new QQ.ShareParams();
                qq_sp.setShareType(Platform.SHARE_WEBPAGE);
                qq_sp.setTitle(title);
                qq_sp.setTitleUrl(url);
                qq_sp.setImageUrl(imageUrl);
                text = "足彩不赚钱,还没用球咖吧?";
                qq_sp.setText(text);
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(platformActionListener);
                qq.share(qq_sp);
                break;
            case 3:     //qzone


                QZone.ShareParams qzone_sp = new QZone.ShareParams();
                qzone_sp.setShareType(Platform.SHARE_WEBPAGE);
                qzone_sp.setTitle(title);
                text = "足彩不赚钱,还没用球咖吧?";

                qzone_sp.setText(text);
                qzone_sp.setImageUrl(imageUrl);
                qzone_sp.setTitleUrl(url);
                Platform qzone = ShareSDK.getPlatform(QZone.NAME);
                qzone.setPlatformActionListener(platformActionListener);
                qzone.share(qzone_sp);
                break;
            case 4:     //wechat
                Wechat.ShareParams wechat_sp = new Wechat.ShareParams();
                wechat_sp.setShareType(Platform.SHARE_WEBPAGE);

                wechat_sp.setTitle(title);
                text = "足彩不赚钱,还没用球咖吧?";
                wechat_sp.setText(text);
                wechat_sp.setUrl(url);
                wechat_sp.setImageUrl(imageUrl);
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(platformActionListener);
                wechat.share(wechat_sp);
                break;
            case 5:     //circle
                WechatMoments.ShareParams circle_sp = new WechatMoments.ShareParams();
                circle_sp.setShareType(Platform.SHARE_WEBPAGE);
                circle_sp.setTitle(title);
                text = "足彩不赚钱,还没用球咖吧?";
                circle_sp.setText(text);
                circle_sp.setUrl(url);
                circle_sp.setImageUrl(imageUrl);
                Platform circle = ShareSDK.getPlatform(WechatMoments.NAME);
                circle.setPlatformActionListener(platformActionListener);
                circle.share(circle_sp);
                break;
        }

    }
}
