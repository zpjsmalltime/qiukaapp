package com.mayisports.qca.utils.share;

import android.app.Activity;

import com.mayi.mayisports.R;
import com.mayisports.qca.utils.Constant;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

/**
 * 友盟分享
 * 微信/朋友圈
 //设置分享内容跳转URL
 weixinContent.setTargetUrl("你的URL链接");
 circleMedia.setTargetUrl("你的URL链接");
 详情参考：[url=http://dev.umeng.com/social/android/detail-share#2]http://dev.umeng.com/social/android/detail-share#2[/url]

 QQ/Qzone
 //设置点击分享内容的跳转链接
 qqShareContent.setTargetUrl("你的URL链接");
 //设置点击消息的跳转URL
 qzone.setTargetUrl("你的URL链接");
 详情请参考：[url=http://dev.umeng.com/social/android/detail-share#3_4]http://dev.umeng.com/social/android/detail-share#3_4[/url]

 新浪微博、腾讯微博及豆瓣的跳转链接只能设置在分享文字之中，以http形式传递
 // 设置分享内容
 mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，[url=http://www.umeng.com/social]http://www.umeng.com/social[/url]");
 详情请参考：[url=http://dev.umeng.com/social/android/detail-share#5]http://dev.umeng.com/social/android/detail-share#5[/url]
 */

public class ShareUtils {

    public static final int wxCircle = 1; //微信朋友圈
    public static final int wx = 2; //微信
    public static final int qq = 3;
    public static final int sina = 4;
    public static final int qzne = 5;


    private final UMSocialService mController;
    private Activity activity;

    private final UMQQSsoHandler qqSsoHandler;
    private final QZoneSsoHandler qZoneSsoHandler;
    private final UMWXHandler wxHandler;
    private final UMWXHandler wxCircleHandler;
    private boolean isShareSuccess;

    public ShareUtils(final Activity activity) {
        this.activity = activity;

        //微信
        wxHandler = new UMWXHandler(activity, "wx38310ce7f5865da9",
                "f1f32ca0b9b3b59c5117d43655b7f465");

        //微信朋友圈
        wxCircleHandler = new UMWXHandler(activity,"wx38310ce7f5865da9","f1f32ca0b9b3b59c5117d43655b7f465");
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        qqSsoHandler = new UMQQSsoHandler(activity,
                "1105039297", "mJaTpORx2glgTHs1");
        // 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        qZoneSsoHandler = new QZoneSsoHandler(
                activity, "1105039297",
                "mJaTpORx2glgTHs1");
        qZoneSsoHandler.addToSocialSDK();
        qqSsoHandler.addToSocialSDK();
        //添加微信分享
        wxHandler.addToSocialSDK();

        // 首先在您的Activity中添加如下成员变量
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.TENCENT, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,
                SHARE_MEDIA.QZONE);

    }

    /**
     * 分享平台
     * @param shareUrl
     * @param shareTitle
     * @param content
     * @param picUrl
     */
    public void share(String shareUrl, String shareTitle, String content, String picUrl, int shareMedia){

        //微信和微信朋友圈
        wxHandler.setTargetUrl(shareUrl);
        wxCircleHandler.setTargetUrl(shareUrl);
        wxHandler.setTitle(shareTitle);
        wxCircleHandler.setTitle(shareTitle);

        //设置点击分享内容的跳转链接
        qqSsoHandler.setTargetUrl(shareUrl);
        qqSsoHandler.setTitle(shareTitle);
        //设置点击消息的跳转URL
        qZoneSsoHandler.setTargetUrl(shareUrl);
        // 设置分享内容
//        mController.setShareContent(content + ",[url=" + shareUrl + "[/url]");
        mController.setShareContent(content);

        // 设置分享图片, 参数2为图片的url地址
        if(picUrl == null){
            mController.setShareMedia(new UMImage(activity, R.drawable.pic_default_loading));
        }else{
            mController.setShareMedia(new UMImage(activity, picUrl));
        }

        switch (shareMedia){
            case wxCircle:
                mController.postShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE,new QCaSnsPostListener());
                break;
            case wx:
                mController.postShare(activity, SHARE_MEDIA.WEIXIN,new QCaSnsPostListener());
                break;
            case qq:
                mController.postShare(activity, SHARE_MEDIA.QQ,new QCaSnsPostListener());
                break;
            case sina:
                mController.postShare(activity, SHARE_MEDIA.SINA,new QCaSnsPostListener());
                break;
            case qzne://qq空间的分享
                mController.postShare(activity, SHARE_MEDIA.QZONE,new QCaSnsPostListener());
                break;
        }

    }

//    class YZTSnsPostListener implements SocializeListeners.SnsPostListener {
//        @Override
//        public void onStart() {
//            Toast.makeText(YZTApplication.getContext(), "开始分享.", Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
//            if (eCode == 200) {
//                Toast.makeText(YZTApplication.getContext(), "分享成功.", Toast.LENGTH_SHORT).show();
//            } else {
//                String eMsg = "";
//                if (eCode == -101) {
//                    eMsg = "没有授权";
//                }
//                Toast.makeText(YZTApplication.getContext(), "分享失败[" + eCode + "] " +
//                        eMsg, Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    /**
     * 分享带回调
     * @param shareUrl
     * @param shareTitle
     * @param content
     * @param picUrl
     * @param shareMedia
     */
    public void shareWithCallBack(String shareUrl, String shareTitle, String content, String picUrl, int shareMedia, SocializeListeners.SnsPostListener shareCallBack){

        //微信和微信朋友圈
        wxHandler.setTargetUrl(shareUrl);
        wxCircleHandler.setTargetUrl(shareUrl);
        wxHandler.setTitle(shareTitle);
        wxCircleHandler.setTitle(shareTitle);

        //设置点击分享内容的跳转链接
        qqSsoHandler.setTargetUrl(shareUrl);
        qqSsoHandler.setTitle(shareTitle);
        //设置点击消息的跳转URL
        qZoneSsoHandler.setTargetUrl(shareUrl);
        // 设置分享内容
//        mController.setShareContent(content + ",[url=" + shareUrl + "[/url]");
        mController.setShareContent(content);

        // 设置分享图片, 参数2为图片的url地址
        if(picUrl == null){
            mController.setShareMedia(new UMImage(activity, R.drawable.pic_default_loading));
        }else{
            mController.setShareMedia(new UMImage(activity, picUrl));
        }

        switch (shareMedia){
            case wxCircle:
                mController.postShare(activity, SHARE_MEDIA.WEIXIN_CIRCLE,shareCallBack);
                break;
            case wx:
                mController.postShare(activity, SHARE_MEDIA.WEIXIN,shareCallBack);
                break;
            case qq:
                mController.postShare(activity, SHARE_MEDIA.QQ,shareCallBack);
                break;
            case sina:
                mController.postShare(activity, SHARE_MEDIA.SINA,shareCallBack);
                break;
            case qzne:
                mController.postShare(activity, SHARE_MEDIA.QZONE,shareCallBack);
                break;
        }
    }


}
