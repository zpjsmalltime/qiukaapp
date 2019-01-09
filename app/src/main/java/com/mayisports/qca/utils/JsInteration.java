package com.mayisports.qca.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.alipay.AlipayHelper;
import com.mayisports.qca.alipay.PayResult;
import com.mayisports.qca.wxapi.PayWechatBean;
import com.mayisports.qca.wxapi.WechatPayHelper;
import com.mob.MobSDK;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * js 调原生接口
 * Created by Zpj on 2017/12/14.
 */
public class JsInteration implements PlatformActionListener {

    private final Activity mActivity;

    public JsInteration(Activity activity) {
        this.mActivity = activity;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AlipayHelper.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.toast("支付成功");
//                        Toast.makeText(MainActivity.mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
//                            Toast.makeText(MainActivity.mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            ToastUtils.toast("支付结果确认中");
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
//                            Toast.makeText(MainActivity.mContext, "支付失败", Toast.LENGTH_SHORT).show();
                            ToastUtils.toast("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    @JavascriptInterface
    public void openNewActivity(String url, String title) {
//        Intent intent = new Intent(mActivity, TitleActivity.class);
//        intent.putExtra("url", url);
//        intent.putExtra("title", title);
//        Log.d("JsInteration", title);
//        Log.d("JsInteration", url);
//        MainActivity.mContext.startActivity(intent);
    }
    //调用支付宝支付
    @JavascriptInterface
    public void getAlipay(String payInfo) {
        AlipayHelper alipayHelper = AlipayHelper.getInstance();
        alipayHelper.onAliPay(payInfo, mHandler, mActivity);
    }

    //调用微信支付
    @JavascriptInterface
    public void getWechatPay(String payInfo) {
        if (!WechatPayHelper.getInstance().isWXAppInstalled()) {
            Toast.makeText(mActivity, "您尚未安装微信", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();
        PayWechatBean payWechatBean = gson.fromJson(payInfo, PayWechatBean.class);
        WechatPayHelper.getInstance().onWechatPay(payWechatBean);
    }



    //分享
    @JavascriptInterface
    public void shareMsg(int platform, String title, String text, String url, String imageUrl) {     //weibo:1  qq:2  qzone:3  wechat:4  cricle:5
        MobSDK.init(mActivity);
        switch (platform) {
            case 1://weibo
                SinaWeibo.ShareParams weibo_sp = new SinaWeibo.ShareParams();
                weibo_sp.setShareType(Platform.SHARE_WEBPAGE);
                weibo_sp.setTitle(title);
                weibo_sp.setText(text);
                weibo_sp.setUrl(url);
                weibo_sp.setImageUrl(imageUrl);
                Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                weibo.setPlatformActionListener(this);
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
                qq.setPlatformActionListener(this);
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
                qzone.setPlatformActionListener(this);
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
                wechat.setPlatformActionListener(this);
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
                circle.setPlatformActionListener(this);
                circle.share(circle_sp);
                break;
        }

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Log.d("JsInteration", "complete");
        if (i == 9) {  //分享
            String jsFunc = "getShareResult(" + 1 + ")";
            JsMethod(jsFunc);
        } else {     //登陆
            Message message = new Message();
            message.what = 1;
            message.obj = platform;
            jsHandler.sendMessage(message);
        }


    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        //授权失败
        if (i == 9) {  //分享
            String jsFunc = "getShareResult(" + 2 + ")";
            JsMethod(jsFunc);
        } else {     //登陆
            Message message = new Message();
            message.what = 2;
            message.obj = platform;
            jsHandler.sendMessage(message);
        }

    }

    @Override
    public void onCancel(Platform platform, int i) {
        //取消授权
        if (i == 9) {  //分享
            String jsFunc = "getShareResult(" + 3 + ")";
            JsMethod(jsFunc);
        } else {     //登陆

        }
    }

    /**
     * 调用js方法
     *
     * @param jsFunc js方法名
     */
    private void JsMethod(String jsFunc) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //系统版本大于等于4.4
            WebViewActivtiy.cwv_webview_actvtivity.evaluateJavascript(jsFunc, null);
        } else {
            //系统版本小于4.4
            String call = "javascript:" + jsFunc;
            WebViewActivtiy.cwv_webview_actvtivity.loadUrl(call);
        }
    }

    Handler jsHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:        //登陆成功
//                    //返回参数
//                    PlatformDb db = ((Platform) msg.obj).getDb();
//                    ThirdLoginUserInfo userInfo = new ThirdLoginUserInfo(db.getPlatformNname(), db.getUserId(), db.getToken(), db.getUserName(), db.getUserGender(), db.getUserIcon());
//                    String json = new Gson().toJson(userInfo);
//                    String jsFunc = "getThirdLoginCallback(" + json + ")";
//                    JsMethod(jsFunc);
                    break;
                case 2:         //登陆失败
                    break;
                case 3:         //全屏调整
//                    boolean fullScreen = (boolean) msg.obj;
//                    if (fullScreen) {
//                        BaseActivity.tintManager.setStatusBarTintColor(Color.TRANSPARENT);
//                        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
//                        lp.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//                        mActivity.getWindow().setAttributes(lp);
//                        mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//                    } else {
//                        BaseActivity.tintManager.setStatusBarTintColor(Color.parseColor(Config.statusColor));
//                        WindowManager.LayoutParams attr = mActivity.getWindow().getAttributes();
//                        attr.flags &= (~WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                        mActivity.getWindow().setAttributes(attr);
//                        mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//                    }
                    break;
            }
        }
    };
}
