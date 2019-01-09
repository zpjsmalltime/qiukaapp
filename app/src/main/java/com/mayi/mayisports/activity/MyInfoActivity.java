package com.mayi.mayisports.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.MessageBean;
import com.mayisports.qca.bean.MineUserInfoBean;
import com.mayisports.qca.bean.RewardBean;
import com.mayisports.qca.fragment.MineFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastMsgPopuWindow;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.mob.tools.utils.UIHandler;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 我的资料界面
 */
public class MyInfoActivity extends BaseActivity implements PlatformActionListener, Handler.Callback, RequestHttpCallBack.ReLoadListener {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,MyInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_my_info;
    }


    private LinearLayout ll_header_myinfo;
    private ImageView iv_header_myinfo;



    private LinearLayout ll_nickname_myinfo;
    private TextView tv_nickname_myinfo;

    private LinearLayout  ll_sign_myinfo;
    private TextView tv_sign_myinfo;

    private LinearLayout ll_weibo_myinfo;
    private TextView tv_weibo_myinfo;

    private RelativeLayout rl_load_layout;


    private boolean isAuther;

    private LinearLayout ll_phone_myinfo;
    private TextView tv_phone_myinfo;
    private LinearLayout ll_weixin_myinfo;
    private TextView tv_weixin_myinfo;
    private LinearLayout ll_qq_myinfo;
    private TextView tv_qq_myinfo;
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("我的资料");
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ll_phone_myinfo = findViewById(R.id.ll_phone_myinfo);
        ll_phone_myinfo.setOnClickListener(this);
        ll_weixin_myinfo = findViewById(R.id.ll_weixin_myinfo);
        ll_weixin_myinfo.setOnClickListener(this);
        tv_weixin_myinfo = findViewById(R.id.tv_weixin_myinfo);
        ll_qq_myinfo = findViewById(R.id.ll_qq_myinfo);
        ll_qq_myinfo.setOnClickListener(this);
        tv_qq_myinfo = findViewById(R.id.tv_qq_myinfo);


        ll_header_myinfo = findViewById(R.id.ll_header_myinfo);
        ll_header_myinfo.setOnClickListener(this);
        iv_header_myinfo = findViewById(R.id.iv_header_myinfo);
        tv_phone_myinfo = findViewById(R.id.tv_phone_myinfo);
        ll_nickname_myinfo = findViewById(R.id.ll_nickname_myinfo);
        ll_nickname_myinfo.setOnClickListener(this);
        tv_nickname_myinfo = findViewById(R.id.tv_nickname_myinfo);
        ll_sign_myinfo = findViewById(R.id.ll_sign_myinfo);
        ll_sign_myinfo.setOnClickListener(this);
        tv_sign_myinfo = findViewById(R.id.tv_sign_myinfo);
        ll_weibo_myinfo = findViewById(R.id.ll_weibo_myinfo);
        ll_weibo_myinfo.setOnClickListener(this);
        tv_weibo_myinfo = findViewById(R.id.tv_weibo_myinfo);

        rl_load_layout = findViewById(R.id.rl_load_layout);


        updateAuther();

    }


    /**
     * 更新weibo qq weixin
     */
    private int black = QCaApplication.getContext().getResources().getColor(R.color.coment_black);
    private int gray =  QCaApplication.getContext().getResources().getColor(R.color.coment_black_99);
    private void updateAuther(){
        if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.WEOBO_ID))){
            isAuther = false;
            tv_weibo_myinfo.setText("授权的微博账号在个人主页展示");
            Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
            if (sina.isAuthValid()) { //如果授权就删除授权资料
                sina.removeAccount(true);
            }
            tv_weibo_myinfo.setTextColor(gray);
        }else{
            isAuther = true;
            String weiboName = SPUtils.getString(QCaApplication.getContext(), Constant.WEIBO_NAME);
            tv_weibo_myinfo.setText(weiboName);
            if (mineUserInfoBean != null ) {
                if(TextUtils.isEmpty(mineUserInfoBean.user.nickname)) {
                    tv_nickname_myinfo.setText(weiboName);
                }else if(mineUserInfoBean.user.nickname.startsWith("球咖") && mineUserInfoBean.user.nickname.length() == 8){
                    tv_nickname_myinfo.setText(weiboName);
                }
            }
            tv_weibo_myinfo.setTextColor(black);
        }


        if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.MOBLIE).trim())){
            tv_phone_myinfo.setTag(false);
            tv_phone_myinfo.setText("点击绑定");
            tv_phone_myinfo.setTextColor(gray);
        }else{
            tv_phone_myinfo.setTag(true);
            String moblie = SPUtils.getString(QCaApplication.getContext(), Constant.MOBLIE);
            tv_phone_myinfo.setText(moblie);
            tv_phone_myinfo.setTextColor(black);
        }

        if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.WEIXIN_ID).trim())){
            tv_weixin_myinfo.setTag(false);
            tv_weixin_myinfo.setText("点击授权");
            tv_weixin_myinfo.setTextColor(gray);

        }else{
            tv_weixin_myinfo.setTag(true);
            String weiboName = SPUtils.getString(QCaApplication.getContext(), Constant.WEIXIN_NAME);
            tv_weixin_myinfo.setText(weiboName);
            tv_weixin_myinfo.setTextColor(black);
        }

        if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.QQ_ID).trim())){
            tv_qq_myinfo.setTag(false);
            tv_qq_myinfo.setText("点击授权");
            tv_qq_myinfo.setTextColor(gray);

        }else{
            tv_qq_myinfo.setTag(true);
            String weiboName = SPUtils.getString(QCaApplication.getContext(), Constant.QQ_NAME);
            tv_qq_myinfo.setText(weiboName);
            tv_qq_myinfo.setTextColor(black);
        }



    }

    @Override
    protected void initDatas() {
        super.initDatas();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private MineUserInfoBean mineUserInfoBean;
    private void initData() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","userinfo");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {

                if(Utils.isLogin()) {
                    try {
                        mineUserInfoBean = JsonParseUtils.parseJsonClass(string, MineUserInfoBean.class);
//                        SPUtils.putString(getContext(), Constant.MINE_PAGE_CACHE,string);
                        updateView();
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });
    }

    private void updateView() {

        PictureUtils.showCircle(mineUserInfoBean.user.weibo_headurl,iv_header_myinfo);
        SPUtils.putString(this,Constant.HEADER_URl,mineUserInfoBean.user.weibo_headurl);

        try {
            if (TextUtils.isEmpty(mineUserInfoBean.user.nickname)) {
                String mobile = mineUserInfoBean.user.mobile;
                String mo = mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
                mineUserInfoBean.user.nickname = mo;
            }
        }catch (Exception e){

        }

        tv_nickname_myinfo.setText(mineUserInfoBean.user.nickname+"");

//        tv_phone_myinfo.setText(mineUserInfoBean.user.mobile);
        String verify_reason = mineUserInfoBean.user.verify_reason;
        if(!TextUtils.isEmpty(verify_reason)) {
            tv_sign_myinfo.setText(verify_reason+" ");
        }else{
            tv_sign_myinfo.setHint("未设置 ");
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ll_header_myinfo:
                UpdateHeaderActivity.start(this);
                break;
            case R.id.ll_nickname_myinfo:
                EditNicknameActivity.start(this,tv_nickname_myinfo.getText().toString().trim());
                break;
            case R.id.ll_sign_myinfo:
                EditSignActivity.start(this,tv_sign_myinfo.getText().toString().trim());
                break;
            case R.id.ll_weibo_myinfo:

                if(isAuther){
                    requestCancleWeibo(1);
                }else {

                    ll_weibo_myinfo.setEnabled(false);
                    ll_weibo_myinfo.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ll_weibo_myinfo.setEnabled(true);
                        }
                    },2500);

                    Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                    sina.setPlatformActionListener(this);
                    sina.SSOSetting(false);
                    authorize(sina);
                }

                break;
            case R.id.ll_phone_myinfo:
                boolean bo = (boolean) tv_phone_myinfo.getTag();
                if(bo){
                    ChangePhoneNumberActivity.start(this,"");
                }else{
                    BindPhoneNumActivity.start(this);
                }
                break;
            case R.id.ll_weixin_myinfo:
                boolean b = (boolean) tv_weixin_myinfo.getTag();
                if(b){
                    requestCancleWeibo(4);
                }else {
                    bindWeixin();
                }
                break;
            case R.id.ll_qq_myinfo:
                boolean b1 = (boolean) tv_qq_myinfo.getTag();
                if(b1){
                    requestCancleWeibo(7);
                }else {
                    bindQQ();
                }
                break;
        }
    }


    private ToastPricePopuWindow toastPricePopuWindow;
    //取消授权
    private void requestCancleWeibo(final int sortId) {
        String str = "确认要撤销授权吗?";
        toastPricePopuWindow = new ToastPricePopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tv_cancle_toast_price){//左边
                    toastPricePopuWindow.dismiss();
                }else if(v.getId() == R.id.tv_go_toast_price){//右边
                    toastPricePopuWindow.dismiss();
                    boolean b = checkIsCancleAuth();
                    if(!b){
                        ToastUtils.toast("至少关联一项");
                        return;
                    }
                   submitWeiboDatas(sortId,"","","","");

                }



            }
        }, str, "取消", "确认");
        toastPricePopuWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);
    }


    /**
     * 微信绑定
     */
    private void bindWeixin() {

        ll_weixin_myinfo.setEnabled(false);
        ll_weixin_myinfo.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_weixin_myinfo.setEnabled(true);
            }
        },2500);

        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(this);
        wechat.SSOSetting(false);
        authorize(wechat);
    }

    /**
     * qq绑定
     */
    private void bindQQ() {
        ll_qq_myinfo.setEnabled(false);
        ll_qq_myinfo.postDelayed(new Runnable() {
            @Override
            public void run() {
                ll_qq_myinfo.setEnabled(true);
            }
        },2500);

        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.SSOSetting(false);
        authorize(qq);
    }
    //授权
    private void authorize(Platform plat) {

        if (plat.isAuthValid()) { //如果授权就删除授权资料
            plat.removeAccount(true);
        }
        plat.showUser(null);//授权并获取用户信息
    }


    private ProgressDialog progressDialog;
    //显示dialog
    public void showProgressDialog(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    //隐藏dialog
    public void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }


    private static final int MSG_ACTION_CCALLBACK = 0;
    /**
     * 授权
     * @param platform
     * @param i
     */
    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);

    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    //登陆发送的handle消息在这里处理
    @Override
    public boolean handleMessage(Message message) {
        hideProgressDialog();
        switch (message.arg1) {
            case 1:  // 成功


                //获取用户资料
                Platform platform = (Platform) message.obj;
                final int sortId = platform.getSortId();
                String userId = platform.getDb().getUserId();//获取用户账号
                final String token = platform.getDb().getToken();
                final String userName = platform.getDb().getUserName();
                final String userIcon = platform.getDb().getUserIcon();


                if(sortId == 4){//微信
                    userId = platform.getDb().get("unionid");
                    submitWeiboDatas(sortId,userId,token,userName,userIcon);
                } else if(sortId == 7){//qq
                    Utils.getQQUID(token, new Utils.OnQQUIdListener() {
                        @Override
                        public void onOk(String uid, String data) {
                            if(TextUtils.isEmpty(uid)){
                                ToastUtils.toast(uid + "----"+data);
                            }else {
                                submitWeiboDatas(sortId, uid, token, userName, userIcon);
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.toast("请重试");
                            if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                        }
                    });
                    break;
                }else {

                    submitWeiboDatas(sortId,userId,token,userName,userIcon);
                }


//                submitWeiboDatas(sortId,userId,token,userName,userIcon);


                break;
            case 2:  // 失败
                ToastUtils.toast("授权失败");

                break;
            case 3:  // 取消
                ToastUtils.toast("授权取消");
//                submitWeiboDatas("fdsfadsf","fdsfsdfdsfd");
                break;
        }
        return false;
    }


    /**
     * 检查是否可以取消授权
     * @return
     */
    private boolean checkIsCancleAuth(){
        String phone = SPUtils.getString(this,Constant.MOBLIE);
        String qqId = SPUtils.getString(this, Constant.QQ_ID);
        String weixinId = SPUtils.getString(this, Constant.WEIXIN_ID);
        String weiboId = SPUtils.getString(this, Constant.WEOBO_ID);
        int count = 0;

        if(!TextUtils.isEmpty(phone)){
            count ++;
        }

        if(!TextUtils.isEmpty(qqId)){
            count ++;
        }
        if(!TextUtils.isEmpty(weiboId)){
            count ++;
        }
        if(!TextUtils.isEmpty(weixinId)){
            count ++;
        }

        if(count<=1){
            return false;
        }

        return  true;
    }

    /**
     * 提交微博授权成功信息
     */
    private void submitWeiboDatas(final int sortId,final String id, final String token, final String weiboName,final String weiboIcon){
        //api.php?action=user&type=authinfo&id=&value=  //api.php?action=user&type=authinfo&id={user_id}&value={token}
        String url = Constant.BASE_URL + "/php/api.php";
        //action=user&type=auth_unlogin&platform={weixin,qq,weibo}
        //api.php?action=user&type=auth_login&platform=(weixin,weibo,qq)&nickname=&headurl=
        HttpParams params = new HttpParams();
        params.put("action","user");
//        params.put("type","authinfo");
        if(TextUtils.isEmpty(token) && TextUtils.isEmpty(id)){
            params.put("type", "auth_unlogin");
        }else {
            params.put("type", "auth_login");
            params.put("id",id);
            params.put("value",token);
            String encode = URLEncoder.encode(weiboName);
            params.put("nickname",encode);
            params.put("headurl",weiboIcon);
        }


        String platformName = Utils.getPlatformNameBySortId(sortId);
        params.put("platform",platformName);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
//                ToastUtils.toast(string);
                //{"status":{"login":1,"errno":0,"id":"200327"}}
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string, RewardBean.class);
                if(rewardBean.status.errno == 0){
                    ToastUtils.toast("操作成功");
                    Utils.dispatchData(sortId,id,token,weiboName);
                    updateAuther();
                }else{
                    if(TextUtils.isEmpty(rewardBean.status.errstr)){
                        ToastUtils.toast("绑定失败,请重试~");
                    }else{
                        ToastUtils.toast(rewardBean.status.errstr+"");
                    }

                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                 ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    @Override
    public void onReload() {

    }

}
