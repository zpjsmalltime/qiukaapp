package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.CellIdentityWcdma;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.wxapi.WechatPayHelper;
import com.mayisports.qca.bean.GameShareBean;

import com.mayisports.qca.bean.WePayBean;
import com.mayisports.qca.bean.WebViewShareBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsInteration;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.MyFrameLayout;
import com.mayisports.qca.view.SharePopuwind;
import com.mayi.mayisports.wxapi.PayWechatBean;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.json.JSONArray;
import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * webview  各种类型前端界面容器
 */
public class WebViewActivtiy extends BaseActivity implements MyFrameLayout.CallBackListener, View.OnLayoutChangeListener, PlatformActionListener {


    public static Activity activity;
    /**
     * Android 5.0以下版本的文件选择回调
     */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /**
     * Android 5.0及以上版本的文件选择回调
     */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;

    protected static final int REQUEST_CODE_FILE_PICKER = 51426;


    protected String mUploadableFileTypes = "image/*";

    public static void start(Activity activity,String url ,String title){
        start(activity,url,false,title);
    }

    public static void start(Activity activity, String url, boolean isShare, String title) {
        Intent intent =  new Intent(activity, WebViewActivtiy.class);

            if (url.contains("?")) {
                url += Constant.getParams();
            } else {
                url += Constant.getUrl();
            }

        intent.putExtra("url",url);
        intent.putExtra("isShare",isShare);
        intent.putExtra("title",title);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, String url, String title,boolean isLive) {
        Intent intent =  new Intent(activity, WebViewActivtiy.class);

        if (url.contains("?") && url.indexOf("/#/", url.indexOf("?")) == -1) {
            url += Constant.getParams();
        }else {
            url += Constant.getUrl();
        }

        intent.putExtra("url",url);
        intent.putExtra("isShare",false);
        intent.putExtra("title",title);
        intent.putExtra("isLive",isLive);
        activity.startActivity(intent);
    }

    public static void start(Activity activity, String url, String title,boolean isLive,boolean isShare) {
        Intent intent =  new Intent(activity, WebViewActivtiy.class);

        if(url.contains("?")){
            url += Constant.getParams();
        }else {
            url += Constant.getUrl();
        }

        intent.putExtra("url",url);
        intent.putExtra("isShare",isShare);
        intent.putExtra("title",title);
        intent.putExtra("isLive",isLive);
        activity.startActivity(intent);
    }


    public static WebView cwv_webview_actvtivity;
    private View title_top;
    private ImageView iv_left_title_webview;
    private TextView tv_title_webview;
    private boolean isBackground;
    private JsInteration jsInteration;
    private RelativeLayout rl_load_layout;
    private MyFrameLayout view_root;

    private TextView tv_right_webview;
    private ImageView iv_right_webview;


    /**
     * 分享框 右上角功能键是否显示
     */
    private  int showType = -1;
    private WebViewShareBean webViewShareBean;


    @JavascriptInterface
    public void showShareToast(final String msg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(TextUtils.isEmpty(msg)){
                    iv_right_webview.setVisibility(View.GONE);
                    showType = -1;
                }else{
//                    ToastUtils.toast(msg);

                    webViewShareBean = JsonParseUtils.parseJsonClass(msg,WebViewShareBean.class);

                    iv_right_webview.setVisibility(View.VISIBLE);
                    iv_right_webview.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
                    iv_right_webview.setOnClickListener(WebViewActivtiy.this);
                    showType = 1;

                }


            }
        });


    }


    /**
     * 直接弹出分享框
     * 弹出类型   appShare 自己吊起分享
     */
    private String toastType = "";

    @JavascriptInterface
    public void goShare(final String msg){




        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toastType = "";
                if(TextUtils.isEmpty(msg)){
                    return;
                }else{
//                    ToastUtils.toast(msg);
                   goShareToast(msg);
                }


            }
        });


    }



    /**
     * 打开充值界面
     *
     */

    @JavascriptInterface
    public void goTopupActivity(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CoinDetailActivity.start(WebViewActivtiy.this);
            }
        });
    }


    /**
     * 打开微信支付
     *
     */

    @JavascriptInterface
    public void goWeChatPay(final String payInfoJson){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!WechatPayHelper.getInstance().isWXAppInstalled()) {
                    ToastUtils.toast("您尚未安装微信");
                    return;
                }

                WePayBean wePayBean = JsonParseUtils.parseJsonClass(payInfoJson, WePayBean.class);
                if(wePayBean.result != null) {
                    PayWechatBean payWechatBean = wePayBean.result;
                    WechatPayHelper.getInstance().onWechatPay(payWechatBean);
                }
            }
        });



    }


    /**
     * 打开竞猜大赛页面
     *
     */

    @JavascriptInterface
    public void goGuessActivity(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GuessingCompetitionActivity.start(WebViewActivtiy.this);
            }
        });


    }



    /**
     * 打开加载框
     */
    @JavascriptInterface
    public void openloading(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(rl_load_layout!= null) {
                    rl_load_layout.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    /**
     * 关闭加载提示UI
     */
    @JavascriptInterface
    public void closeloding(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(rl_load_layout != null && !TextUtils.isEmpty(currenrUrl)) {
                    rl_load_layout.setVisibility(View.GONE);
                }
            }
        });
    }


    /**
     * 请求成功
     */
    @JavascriptInterface
    public void releaseSuccess(){
        if(url.equals(currenrUrl)){

            if(url.contains("#/topicViewRelease")){
                Intent intent = new Intent(DynamicFragment.ACTION);
                intent.putExtra(LoginActivity.RESULT,3);
                LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);
            }

            finish();
        }else{
            if(cwv_webview_actvtivity == null)return;
            cwv_webview_actvtivity.goBack();
        }

    }


    /**
     * 关闭当前界面
     */
    @JavascriptInterface
    public void closeWebView(){
        finish();
    }

    @JavascriptInterface
    public void closeActivity(){
        finish();
    }


    /**
     * 删除成功提示
     */
    @JavascriptInterface
    public void deleteTopicSuccess(){
        Intent intent = new Intent(DynamicFragment.ACTION);
        intent.putExtra(LoginActivity.RESULT,3);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);
        ToastUtils.toast("删除成功");
        finish();
    }

    @JavascriptInterface
    public void loginAction(String userId, String token){
        LoginUtils.saveLoginTags(userId,token);
    }

    @JavascriptInterface
    public void finishApp(){
        LoginUtils.removeLoginTags();
    }

    

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        setPortarait(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_web_view_activtiy;
    }

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;


    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    if(cwv_webview_actvtivity != null)cwv_webview_actvtivity.reload();
                    ll_net_error.setVisibility(View.GONE);
                }else{

                }


            }
        });


        if(NetworkUtils.isConnected(QCaApplication.getContext())){
            ll_net_error.setVisibility(View.GONE);
        }else{
            ll_net_error.setVisibility(View.VISIBLE);
        }


        jsInteration = new JsInteration(this);
        tv_right_webview = findViewById(R.id.tv_right_webview);
        view_root = findViewById(R.id.view_root);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        cwv_webview_actvtivity = findViewById(R.id.cwv_webview_actvtivity);
        title_top = findViewById(R.id.title_top);
        iv_left_title_webview = findViewById(R.id.iv_left_title_webview);
        iv_left_title_webview.setOnClickListener(this);
        tv_title_webview = findViewById(R.id.tv_title_webview);
        iv_right_webview = findViewById(R.id.iv_right_webview);
        initWebView();


        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.iv_left_title_webview){
            KeyBoardUtils.closeKeybord(tv_title_webview,this);

            if(cwv_webview_actvtivity != null && cwv_webview_actvtivity.canGoBack()){
                cwv_webview_actvtivity.goBack();
            }else{
                finish();
            }


       //     finish();
        }else if(view.getId() == R.id.iv_right_webview){
//            ToastUtils.toast("分享");


            toastType = "appShare";

            if(showType == 1){
                showShareTitleToast();
            }else {
                showShare();
            }
        }
    }

    /**
     * 右上角点击弹出
     */
    private void showShareTitleToast() {
        if(cwv_webview_actvtivity == null || webViewShareBean == null)return;
        final SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
               try {
                   if(type == 6){

                       WebViewShareBean.ShareEntry beanByType = webViewShareBean.getBeanByType(type);
                       ShareUtils.shareMsg(type, beanByType.title, beanByType.desc+"", beanByType.path, beanByType.imgUrl, WebViewActivtiy.this);


                   }else {
                       WebViewShareBean.ShareEntry beanByType = webViewShareBean.getBeanByType(type);
                       ShareUtils.shareMsg(type, beanByType.title, beanByType.desc, beanByType.link, beanByType.imgUrl, WebViewActivtiy.this);
                   }
               }catch (Exception e){

               }


            }
        }, new SharePopuwind.ShareInfoBean());
        sharePopuwind.setShowType(webViewShareBean.jsApiList);
        sharePopuwind.showAtLocation(cwv_webview_actvtivity, Gravity.CENTER, 0, 0);
    }


    /**
     * 直接弹出
     */
    private void goShareToast(final String msg) {
        if(TextUtils.isEmpty(msg))return;
        final WebViewShareBean webViewShareBean = JsonParseUtils.parseJsonClass(msg, WebViewShareBean.class);
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                try {

                    if(type == 6){//小程序
                        WebViewShareBean.ShareEntry beanByType = webViewShareBean.getBeanByType(type);
                        ShareUtils.shareMsg(type, beanByType.title, beanByType.desc+"", beanByType.path, beanByType.imgUrl, WebViewActivtiy.this);

                    }else {
                        WebViewShareBean.ShareEntry beanByType = webViewShareBean.getBeanByType(type);
                        ShareUtils.shareMsg(type, beanByType.title, beanByType.desc, beanByType.link, beanByType.imgUrl, WebViewActivtiy.this);
                    }
                }catch (Exception e){

                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.setShowType(webViewShareBean.jsApiList);
        sharePopuwind.showAtLocation(cwv_webview_actvtivity, Gravity.CENTER, 0, 0);
    }



    private void showShare() {
        if(cwv_webview_actvtivity == null)return;
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                if(url.contains("/applyV")){//申请认证
                     shareApplyV(type);
                }else if(isJsShow){
                    /**
                     * 获取分享有值
                     */
                    jsShare(type);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(cwv_webview_actvtivity, Gravity.CENTER, 0, 0);
    }


    /**
     * js  调用分享
     * @param type
     */
    private void jsShare(int type) {
        isToShare = true;
        switch (type) {
            case 1://weibo
                JsMethod("weibo-"+type);
                break;
            case 2:     //qq
            case 3:     //qzone
            case 4:     //wechat
            case 5:     //circle
                JsMethod("defult-"+type);
                break;
        }
    }

    /**
     * 申请认证 分享
     * @param type
     */
    private void shareApplyV(int type) {
        ShareUtils.shareMsgForApplyV(type,this);
    }

    /**
     * 设置Cookie
     *
     * @param context
     * @param url
     * @param cookie  格式：uid=21233 如需设置多个，需要多次调用
     */
    public void synCookies(Context context, String url, String cookie) {

        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }

    String url ;
    String url_half;
    private boolean titleTag = true;
    private String temapUrl;

    private boolean isShare;
    private boolean isLive;
    @Override
    protected void initDatas() {//?source=android
        super.initDatas();
        url = getIntent().getStringExtra("url");

     // url = "http://192.168.1.6:3030/#/game";
        //url = "http://20180525.dldemo.applinzi.com/#/game";

        isShare = getIntent().getBooleanExtra("isShare",false);
        isLive = getIntent().getBooleanExtra("isLive",false);


        if(isLive){
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) cwv_webview_actvtivity.getLayoutParams();
            int i = DisplayUtil.dip2px(QCaApplication.getContext(), 41);
            layoutParams.topMargin = i;
            cwv_webview_actvtivity.setLayoutParams(layoutParams);
        }
        if(!TextUtils.isEmpty(url)){

            if(isShare){
                iv_right_webview.setVisibility(View.VISIBLE);
                iv_right_webview.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
                iv_right_webview.setOnClickListener(this);
                title_top.setClickable(true);
            }else{
//                title_top.setClickable(true);
            }

            if(url.contains("#/topic/")){
                iv_right_webview.setVisibility(View.VISIBLE);
                iv_right_webview.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
            }


            if(url.contains("#/searchQiuka")){
                titleTag =false;
                view_root.setCallBackListener(this);
            }

            if(url.contains("#/pointRanklist")){
                view_root.setCallBackListener(this);
            }

            if(url.contains("#/topicViewRelease/")){
                tv_right_webview.setVisibility(View.VISIBLE);
            }


            int i = url.indexOf("#");
            if(i != -1) {
                url_half = url.substring(0,url.length()-1);
            }else{
                url_half = url;
            }
            String title = getIntent().getStringExtra("title");
            if(title != null){
                tv_title_webview.setText(title);
            }



                CookieSyncManager.createInstance(QCaApplication.getContext());
                CookieManager.getInstance().removeAllCookie();

                String idCookie = "id=" + SPUtils.getString(this, Constant.USER_ID);
                synCookies(QCaApplication.getContext(), url, idCookie);
                String token = SPUtils.getString(this, Constant.TOKEN);
                String tokenCookie = "token=" + token;
                synCookies(QCaApplication.getContext(), url, tokenCookie);

                if(false&&"tu".equals(title)){
                    String html = "<img src=\""+url+"\"   height=\"auto\" width=\"100%\"/>";
                    cwv_webview_actvtivity.loadDataWithBaseURL(null, html, "text/html" , "utf-8", null);
                }else {
                    cwv_webview_actvtivity.loadUrl(url);
                }
        }
    }


    private String currenrUrl = "";
    private void initWebView() {

        cwv_webview_actvtivity.addJavascriptInterface(jsInteration, "control");
        cwv_webview_actvtivity.addJavascriptInterface(this,"qk");
        final WebSettings webSettings = cwv_webview_actvtivity.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //支持js交互
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);

        //使用插件
        webSettings.setPluginState(WebSettings.PluginState.ON);

        webSettings.setAllowFileAccess(true);
        webSettings.setSavePassword(true);

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);



        webSettings.setBlockNetworkImage(true);
//    String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.87 Safari/537.36";
//     webSettings.setUserAgentString(agent);

        cwv_webview_actvtivity.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(rl_load_layout!=null){
                            rl_load_layout.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(rl_load_layout!=null){
                            rl_load_layout.setVisibility(View.GONE);
                        }
                    }
                });

                title_top.setVisibility(View.INVISIBLE);
               currenrUrl =  URLDecoder.decode(url);

                webSettings.setBlockNetworkImage(false);



                //http://app.mayisports.com/#/personHomepage/200291,8
//                    if(url.contains("#/personHomepage/")) {
//                        int i = url.lastIndexOf("/");
//                        String substring = url.substring(i+1, url.length());
//                        String[] split = substring.split(",");
//                        cwv_webview_actvtivity.goBack();
//                        startPersonalDetailActivity(split[0], split[1]);
//                    }else if(url.contains("#/matchDetail/")){
//                        int i = url.lastIndexOf("/");
//                        String substring = url.substring(i + 1, url.length());
//                        int end = substring.indexOf("?");
//                        if(end == -1)end = substring.lastIndexOf(",");
//                        if(end != -1) {
//                            String betId = substring.substring(0, end);
//                            cwv_webview_actvtivity.goBack();
//                            if (substring.contains("navTabType=info")) {
//                                startMatchDetailActivity(betId + "", 2);
//                            } else if(substring.contains("navTabType=analyse")){
//                                startMatchDetailActivity(betId+"",3);
//                            }else  {
//                                startMatchDetailActivity(betId + "", 1);
//                            }
//                        }
//                    }else if(url.contains("#/myCoin")){//http://app.mayisports.com/#/myCoin
//                        startCoinDetailActivity();
//                        cwv_webview_actvtivity.goBack();
//                    }


                if(!titleTag){
                    return;
                }

                if(url.contains(url_half)){
                   title_top.setVisibility(View.VISIBLE);
                }else{
                    title_top.setVisibility(View.GONE);
                }
                if(isLive){
                    title_top.setVisibility(View.VISIBLE);
                }


                 JsMethod(null);

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }
        });
        cwv_webview_actvtivity.setWebChromeClient(new OpenFileChromeClient());
    }


    private boolean isJsShow;
    private boolean isToShare;

    /**
     * 调用js 封装工具类
     * @param typeName
     */
    private void JsMethod(final String typeName){
        if(cwv_webview_actvtivity == null)return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//sdk>19才有用
            String params = "";
            if(TextUtils.isEmpty(typeName)){
                params = "weixin";
            }else{
                params = typeName.split("-")[0];
            }
            String script = "getShareText('"+params+"')";
            cwv_webview_actvtivity.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(final String responseJson) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                    if(!TextUtils.isEmpty(responseJson)&&!"null".equals(responseJson)){
                        iv_right_webview.setVisibility(View.VISIBLE);
                        iv_right_webview.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
                        iv_right_webview.setOnClickListener(WebViewActivtiy.this);
                        title_top.setClickable(true);
                        isJsShow = true;
                        Log.e("json",responseJson);
                        if(isToShare){
                            String s = typeName.split("-")[1];
                            GameShareBean gameShareBean = JsonParseUtils.parseJsonClass(responseJson, GameShareBean.class);
                            ShareUtils.shareMsg(Integer.valueOf(s),gameShareBean.title,gameShareBean.desc,url,gameShareBean.imageUrl,null);
                            isToShare = false;
                        }

                    }else{
                        isJsShow = false;
                    }
                        }
                    });
                }
            });
        } else {//sdk<19后,通过prompt来获取
//            String[] paths = moduleUrl.split("/", 3);
//            promptMap.put(paths[2], paths);
//            webView.loadUrl("javascript:mandaobridge.getParams('!" + paths[2] + "')");
//            LogUtils.d("Prompt请求:" + "mandaobridge.getParams('!" + paths[2] + "')");
        }
    }


    /**
     *
     * @param method  getShareStatus('0');
     */
    private void invokeJsMethod(final String method){
        if(cwv_webview_actvtivity == null)return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//sdk>19才有用
            String script = method;
            cwv_webview_actvtivity.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(final String responseJson) {

                }
            });
        }
    }

    /**
     *开启新的界面 充值
     */
    private void startCoinDetailActivity() {
        Intent intent = new Intent(this,CoinDetailActivity.class);
        startActivity(intent);
    }

    /**
     * 推荐单详情
     * @param s
     * @param s1
     */
    private void startHomeItemDetail(String s, String s1) {

    }

    /**
     * 跳转到比赛详情，情报页
     * @param betId
     * @param  type 1 推荐   2  情报
     */
    private void startMatchDetailActivity(String betId,int type) {
        Intent intent = new Intent(this, MatchDetailActivity.class);
        intent.putExtra("betId",betId);
        //
        intent.putExtra("type",type);
        startActivity(intent);
    }

    /**
     * 跳转个人详情页
     * @param id
     * @param s1
     */
    private void startPersonalDetailActivity(String id, String s1) {
        Intent intent = new Intent(this,PersonalDetailActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //添加layout大小发生改变监听器
        view_root.addOnLayoutChangeListener(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        invokeJsMethod("onShow();");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cwv_webview_actvtivity == null)return;
     //   cwv_webview_actvtivity.clearCache(true);
        try {
            cwv_webview_actvtivity.clearFormData();
            cwv_webview_actvtivity.clearHistory();
            cwv_webview_actvtivity.clearMatches();
            cwv_webview_actvtivity.destroy();
        }catch (Exception e){

        }
       // cwv_webview_actvtivity = null;
    }




    //返回键,网页返回前一个页面
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && cwv_webview_actvtivity != null && cwv_webview_actvtivity.canGoBack()) {
            cwv_webview_actvtivity.goBack();       //返回前一个页面
//            String jsFunc = "applicationDidReturn()";
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                //系统版本大于等于4.4
//                webView.evaluateJavascript(jsFunc, null);
//            } else {
//                //系统版本小于4.4
//                String call = "javascript:" + jsFunc;
//                webView.loadUrl(call);
//            }
            return true;
        }
        isBackground = false;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void callBack() {
        if(!titleTag){
            finish();
        }
        if(url.contains("#/pointRanklist")){
            finish();
        }

    }

    @Override
    public void finish() {
//        cwv_webview_actvtivity.clearCache(true);
//        cwv_webview_actvtivity.clearFormData();
//        cwv_webview_actvtivity.clearHistory();
//        cwv_webview_actvtivity.clearMatches();
//        cwv_webview_actvtivity.destroy();
        super.finish();

    }

    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;


    /**
     * 监听键盘
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

//          ToastUtils.toast("键盘弹起");



//          cwv_webview_actvtivity.scrollTo(0,(bottom));

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

//            ToastUtils.toast("键盘关闭");

        }



    }


    /**
     * 分享完成
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","share");
        //1 2 3
        params.put("type",1);
        params.put("id","");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });

        invokeJsMethod("getShareResult('1','"+toastType+"');");
        ToastUtils.toastNoStates("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, final Throwable throwable) {

        invokeJsMethod("getShareResult('3','"+toastType+"');");
        ToastUtils.toastNoStates("分享失败");

    }

    @Override
    public void onCancel(Platform platform, int i) {

        invokeJsMethod("getShareResult('2','"+toastType+"');");
        ToastUtils.toastNoStates("分享取消");

    }


    private class OpenFileChromeClient extends WebChromeClient {

        //  Android 2.2 (API level 8)到Android 2.3 (API level 10)版本选择文件时会触发该隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // Android 3.0 (API level 11)到 Android 4.0 (API level 15))版本选择文件时会触发，该方法为隐藏方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // Android 4.1 (API level 16) -- Android 4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileInput(uploadMsg, null, false);
        }

        // Android 5.0 (API level 21)以上版本会触发该方法，该方法为公开方法
        @SuppressWarnings("all")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选


                openFileInput(null, filePathCallback, allowMultiple);

                return true;
            } else {
                return false;
            }
        }
    }

    @SuppressLint("NewApi")
    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
        //Android 5.0以下版本
        if (mFileUploadCallbackFirst != null) {
            mFileUploadCallbackFirst.onReceiveValue(null);
        }
        mFileUploadCallbackFirst = fileUploadCallbackFirst;

        //Android 5.0及以上版本
        if (mFileUploadCallbackSecond != null) {
            mFileUploadCallbackSecond.onReceiveValue(null);
        }
        mFileUploadCallbackSecond = fileUploadCallbackSecond;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= 18) {
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }

        i.setType(mUploadableFileTypes);

        startActivityForResult(Intent.createChooser(i, "选择文件"), REQUEST_CODE_FILE_PICKER);

    }

    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    //Android 5.0以下版本
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
                        mFileUploadCallbackFirst = null;
                    } else if (mFileUploadCallbackSecond != null) {//Android 5.0及以上版本
                        Uri[] dataUris = null;

                        try {
                            if (intent.getDataString() != null) {
                                dataUris = new Uri[]{Uri.parse(intent.getDataString())};
                            } else {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    if (intent.getClipData() != null) {
                                        final int numSelectedFiles = intent.getClipData().getItemCount();

                                        dataUris = new Uri[numSelectedFiles];

                                        for (int i = 0; i < numSelectedFiles; i++) {
                                            dataUris[i] = intent.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        } catch (Exception ignored) {
                        }
                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            } else {
                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst.onReceiveValue(null);
                    mFileUploadCallbackFirst = null;
                } else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }
        }
    }




}
