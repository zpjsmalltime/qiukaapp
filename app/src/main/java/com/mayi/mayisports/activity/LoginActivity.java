package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.LoginBean;
import com.mayisports.qca.bean.RewardBean;
import com.mayisports.qca.bean.UserInfoBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.fragment.MainQcaFragment;
import com.mayisports.qca.fragment.MineFragment;
import com.mayisports.qca.fragment.TabLayoutFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NetWorkUtil;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mob.tools.utils.UIHandler;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 *登陆界面
 */
public class LoginActivity extends BaseActivity implements PlatformActionListener, Handler.Callback, RequestHttpCallBack.ReLoadListener {


    private EditText et_number_login;
    private EditText et_code_login;
    private TextView tv_send_code_login;
    private TextView tv_login_login;
    private TextView tv_unlogin_login;

    private String action;//成功广播
    public static final String ACTION = "LoginActivity";
    private ImageView iv_left_title_;

    private ImageView tv_delete_num_login;


    private ImageView iv_weixin_login;
    private ImageView iv_weibo_login;
    private ImageView iv_qq_login;

    private RelativeLayout rl_load_layout;

    private FrameLayout fl_login_wechat;
    private ImageView iv_phone_login;


    private LocalBroadcastManager localBroadcastManager;
    private Rec rec;
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }
    private void destroyReceiver(){
        if(localBroadcastManager != null && rec != null) {
            localBroadcastManager.unregisterReceiver(rec);
        }
    }
    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }

    @Override
     protected int setViewForContent() {
        return R.layout.activity_login4;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);

        initReceiver();

        iv_left_title_ = findViewById(R.id.iv_left_title_);
//        tv_ritht_title.setVisibility(View.INVISIBLE);
//        tv_title.setText("免注册登录");
        iv_left_title_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_number_login,LoginActivity.this);
                iv_left_title_.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },300);

            }
        });
        action = getIntent().getStringExtra(ACTION);


        fl_login_wechat = findViewById(R.id.fl_login_wechat);
        fl_login_wechat.setOnClickListener(this);
        iv_phone_login = findViewById(R.id.iv_phone_login);
        iv_phone_login.setOnClickListener(this);

        rl_load_layout = findViewById(R.id.rl_load_layout);

        et_number_login = findViewById(R.id.et_number_login);
        et_code_login = findViewById(R.id.et_code_login);
        tv_send_code_login = findViewById(R.id.tv_send_code_login);
        tv_send_code_login.setOnClickListener(this);
        tv_login_login = findViewById(R.id.tv_login_login);
        tv_login_login.setOnClickListener(this);
        tv_unlogin_login = findViewById(R.id.tv_unlogin_login);
        tv_unlogin_login.setOnClickListener(this);

        iv_weixin_login = findViewById(R.id.iv_weixin_login);
        iv_weixin_login.setOnClickListener(this);
        iv_weibo_login = findViewById(R.id.iv_weibo_login);
        iv_weibo_login.setOnClickListener(this);
        iv_qq_login = findViewById(R.id.iv_qq_login);
        iv_qq_login.setOnClickListener(this);


        et_number_login.addTextChangedListener(new MyWatcher());

        tv_delete_num_login = findViewById(R.id.tv_delete_num_login);
        tv_delete_num_login.setOnClickListener(this);



        et_number_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    KeyBoardUtils.openKeybord(et_number_login,LoginActivity.this);
                    EditText et = (EditText) v;
                    String s = et.getText().toString();
                    if(TextUtils.isEmpty(s.toString())){
                        tv_delete_num_login.setVisibility(View.INVISIBLE);
                        tv_delete_num_login.setEnabled(false);
                    }else{
                        tv_delete_num_login.setVisibility(View.VISIBLE);
                        tv_delete_num_login.setEnabled(true);
                    }
                }else{
//                    KeyBoardUtils.openKeybord(et_number_login,LoginActivity.this);
                    tv_delete_num_login.setVisibility(View.INVISIBLE);
                    tv_delete_num_login.setEnabled(false);
                }


            }
        });
        et_number_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String s = et_number_login.getText().toString();
                if(TextUtils.isEmpty(s.toString())){
                    tv_delete_num_login.setVisibility(View.INVISIBLE);
                    tv_delete_num_login.setEnabled(false);
                }else{
                    tv_delete_num_login.setVisibility(View.VISIBLE);
                    tv_delete_num_login.setEnabled(true);
                }
                return false;
            }
        });

        et_code_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    KeyBoardUtils.openKeybord(et_number_login,LoginActivity.this);
                }
            }
        });




    }

    @Override
    public void onReload() {

    }


    class MyWatcher implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
               if(TextUtils.isEmpty(s.toString())){
                   tv_delete_num_login.setVisibility(View.INVISIBLE);
                   tv_delete_num_login.setEnabled(false);
               }else{
                   tv_delete_num_login.setVisibility(View.VISIBLE);
                   tv_delete_num_login.setEnabled(true);
               }


        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length() == 11){
                tv_send_code_login.setTextColor(getResources().getColor(R.color.coment_black));
            }else {
                    tv_send_code_login.setTextColor(getResources().getColor(R.color.coment_black_99));
            }
        }
    }


    private boolean isSended;
    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view.getId() == R.id.tv_send_code_login){

            et_number_login.requestFocus();
            tv_delete_num_login.setVisibility(View.GONE);
            if(checkDatasNumber()){
                isSended = true;
                tv_send_code_login.setTextColor(getResources().getColor(R.color.coment_black));
                tv_send_code_login.setText("正在发送");
                tv_send_code_login.setEnabled(false);
                tv_delete_num_login.setVisibility(View.GONE);
                et_code_login.requestFocus();
                requestCode();
            }

        }else if(view.getId() == R.id.tv_login_login){
           if(checkDatas()){
               KeyBoardUtils.closeKeybord(et_code_login,this);
               requestLogin();
           }
        }else if(view.getId() == R.id.tv_unlogin_login){
            ToastUtils.toast("游客模式");
            finish();

        }else if(view.getId() == R.id.tv_delete_num_login){
            et_number_login.setText("");
        }else {

            switch (view.getId()) {
                case R.id.iv_weixin_login:
                    iv_weixin_login.setEnabled(false);
                    iv_weixin_login.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_weixin_login.setEnabled(true);
                        }
                    },2000);
                    loginByWeixin();
                    break;
                case R.id.iv_weibo_login:
                    iv_weibo_login.setEnabled(false);
                    iv_weibo_login.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_weibo_login.setEnabled(true);
                        }
                    },2000);
                    loginByWeibo();
                    break;
                case R.id.iv_qq_login:
                    iv_qq_login.setEnabled(false);
                    iv_qq_login.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            iv_qq_login.setEnabled(true);
                        }
                    },2000);
                    loginByQQ();
                    break;
                case R.id.fl_login_wechat:
                    fl_login_wechat.setEnabled(false);
                    fl_login_wechat.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fl_login_wechat.setEnabled(true);
                        }
                    },2000);
                    loginByWeixin();
                    break;
                case R.id.iv_phone_login:
                    PhoneLoginActivity.start(this,action);
                    break;
            }
        }
    }


    /**
     * 微博登陆
     */
    private void loginByWeibo() {
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        sinaWeibo.setPlatformActionListener(this);
        sinaWeibo.SSOSetting(false);
        authorize(sinaWeibo);
    }

    /**
     * 微信登陆
     */
    private void loginByWeixin() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(this);
        wechat.SSOSetting(false);
        authorize(wechat);
    }

    /**
     * qq登陆
     */
    private void loginByQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.SSOSetting(false);
        authorize(qq);
    }


    /**
     * 授权
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {  //如果授权就删除授权资料
            platform.removeAccount(true);
        }

        if(rl_load_layout != null) rl_load_layout.setVisibility(View.VISIBLE);
        platform.showUser(null); //授权并获取用户信息
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
        switch (message.arg1) {
            case 1:  // 成功


                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userId = platform.getDb().getUserId();//获取用户账号
                final String token = platform.getDb().getToken();
                final String userName = platform.getDb().getUserName();
                final String userIcon = platform.getDb().getUserIcon();
                final int sortId = platform.getSortId();

//                submitWeiboDatas(sortId,userId,token,userName,userIcon);
                if(sortId == 4){//微信
                    userId = platform.getDb().get("unionid");
                    getAuthStatus(sortId, userId, token, userName, userIcon);
                 } else if(sortId == 7){//qq
                    Utils.getQQUID(token, new Utils.OnQQUIdListener() {
                        @Override
                        public void onOk(String uid, String data) {
                            getAuthStatus(sortId,uid,token,userName,userIcon);
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.toast("请重试");
                            if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                        }
                    });
                    break;
                }else {

                    getAuthStatus(sortId, userId, token, userName, userIcon);
                }

                break;
            case 2:  // 失败
                ToastUtils.toast("登录失败");
                if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                break;
            case 3:  // 取消
                ToastUtils.toast("登录取消");
                if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
//                submitWeiboDatas("fdsfadsf","fdsfsdfdsfd");
                break;
        }
        return false;
    }




    /**
     * 第三方登录提交
     * @param sortId
     * @param id
     * @param token
     * @param weiboName
     * @param weiboIcon
     *
     *
     *
     * 请求是否要弹出绑定手机号
     *
     * 是   调到手机号  绑定成功  然后提交第三方数据做绑定处理  取消绑定，或手动关闭绑定界面，提交第三方数据做初始化登录处理
     * 否   直接登录，提交第三方做登录处理
     *
     *
     *
     * api.php?action=user&type=get_auth_bind_status&id=&platform=
     *
     *
     *
     *
     */

    private void getAuthStatus(final int sortId,final String id, final String token, final String weiboName,final String weiboIcon){
        String url = Constant.BASE_URL + "/php/api.php";

        HttpParams params = new HttpParams();

        params.put("action","user");
        params.put("type", "get_auth_bind_status");
        params.put("id",id);
        String platformName = Utils.getPlatformNameBySortId(sortId);
        params.put("platform",platformName);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string, RewardBean.class);
                //&&rewardBean.status.first_auth == 1
                if(rewardBean.status.errno == 0){
                    if(rewardBean.status.first_auth == 1){
                       BindPhoneNumActivity.start(LoginActivity.this,sortId,id,token,weiboName,weiboIcon);
                       finish();
                    }else{
                        submitWeiboDatas(sortId,id,token,weiboName,weiboIcon);
                        finish();
                    }
                }else{
                    ToastUtils.toast(rewardBean.status.errstr+"");
                    rl_load_layout.setVisibility(View.GONE);
                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                rl_load_layout.setVisibility(View.GONE);
            }
        });
    }


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
                //&&rewardBean.status.first_auth == 1
                if(rewardBean.status.errno == 0){
                    ToastUtils.toast("登录成功");
                    LoginUtils.saveLoginTags(rewardBean.status.user_id,"");
                    sendLoginOk();
                    finish();
                }else{
                    ToastUtils.toast(rewardBean.status.errstr+"");
                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    private boolean checkDatasNumber() {
        String number = et_number_login.getText().toString()+"";
        if(number.length() != 11|| !number.startsWith("1")){
            ToastUtils.toast("手机号填写有误，请重填");
            et_number_login.requestFocus();
            KeyBoardUtils.openKeybord(et_number_login,this);
            return false;
        }
        return true;
    }


    private int start = 1;
    private int stop = 2;
    private int countTime = 60;
    private Handler countHanlder = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == start){
                if(countTime <1){
                    countHanlder.sendEmptyMessage(stop);
                    return;
                }
                tv_send_code_login.setText(--countTime+"s");
                countHanlder.sendEmptyMessageDelayed(start,1000*1);
            }else if(msg.what == stop){
                countTime=60;
                isSended = false;
                tv_send_code_login.setText("重新发送");
                tv_send_code_login.setEnabled(true);
                countHanlder.removeCallbacksAndMessages(null);
            }
        }
    };

    /**
     * 请求验证码
     */
    private void requestCode() {
///php/api.php?action=user&date=&type=assertcode_new&mobile=13290505500&source=other&network=other&appstore=other
//        String url = "http://blog.csdn.net/kontrol/article/details/7767983";
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("date","");
        params.put("type","assertcode_new1");
        String number = et_number_login.getText().toString();
        params.put("mobile",number);
        params.put("source","android");

        boolean wifi = NetWorkUtil.isWifi(getBaseContext());
        if(wifi){
            params.put("network","wifi");
        }else{
            params.put("network","cellular");
        }

        params.put("appstore","other");

        String string = SPUtils.getString(QCaApplication.getContext(), Constant.MSG_TOKEN);
        String s = Utils.MD5(number + string);
        String s1 = Utils.MD5(s);
        params.put("token",s1);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                LoginBean loginBean = JsonParseUtils.parseJsonClass(string, LoginBean.class);
                if(loginBean != null && loginBean.status != null && loginBean.status.errstr != null) {
                    ToastUtils.toast(loginBean.status.errstr);
                }
                countHanlder.sendEmptyMessage(start);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                 countHanlder.sendEmptyMessage(stop);
                 ToastUtils.toast(Constant.NET_FAIL_TOAST+"");
            }
        });
    }


    /**
     * 去登录
     */
    private void requestLogin() {
       String url = Constant.BASE_URL + "/php/api.php";
       HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","assertCodeLogin_new");
        String number = et_number_login.getText().toString();
        params.put("mobile",number);
        params.put("code",et_code_login.getText().toString());
        params.put("source","android");

        tv_login_login.setText("登录中...");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                UserInfoBean userInfoBean = JsonParseUtils.parseJsonClass(string, UserInfoBean.class);
                if(TextUtils.isEmpty(userInfoBean.id)){
                    ToastUtils.toast(userInfoBean.status);
                    return;
                }

                saveInfo(userInfoBean);
                sendLoginOk();
                finish();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tv_login_login.setText("免注册登录");
            }
        });
    }


    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        action = getIntent().getStringExtra(ACTION);
    }

    private void saveInfo(UserInfoBean userInfoBean) {
//        SPUtils.putString(getBaseContext(),Constant.USER_ID,userInfoBean.id);
//        SPUtils.putString(getBaseContext(),Constant.TOKEN,userInfoBean.token);
        LoginUtils.saveLoginTags(userInfoBean.id,userInfoBean.token);
//        SPUtils.putString(getBaseContext(),Constant.COIN,userInfoBean.coin);
//        sendLoginOk();

    }

    public static final String RESULT = "result";

    /**
     * 登录成功发送，通知对应界面刷新数据
     */
    private void sendLoginOk(){
        Intent intent = new Intent(action+"");
        intent.putExtra(RESULT,1);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);

        if(!action.equals(HomeNewFragment.ACTION)) {
            Intent intent1 = new Intent(HomeNewFragment.ACTION);
            intent1.putExtra(RESULT, 1);
            LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);
        }

        if(!action.equals(DynamicFragment.ACTION)){
            Intent intent1 = new Intent(DynamicFragment.ACTION);
            intent1.putExtra(RESULT, 1);
            LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);
        }
        Intent intent2 = new Intent(TabLayoutFragment.ACTION);
        intent2.putExtra(RESULT, 1);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent2);


        Intent intent5 = new Intent(MainQcaFragment.ACTION);
        intent5.putExtra(RESULT, 22);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent5);


        Intent intent6 = new Intent(MineFragment.MINE_FG_ACTION);
        intent6.putExtra(RESULT, 99);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent6);

    }

    public static void loginOk(String action){

        Intent intent = new Intent(action+"");
        intent.putExtra(RESULT,1);
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent);

        if(!action.equals(HomeNewFragment.ACTION)) {
            Intent intent11 = new Intent(HomeNewFragment.ACTION);
            intent11.putExtra(RESULT, 1);
            LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent11);
        }

        if(!action.equals(DynamicFragment.ACTION)) {
            Intent intent1 = new Intent(DynamicFragment.ACTION);
            intent1.putExtra(RESULT, 1);
            LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent1);
        }

        Intent intent2 = new Intent(TabLayoutFragment.ACTION);
        intent2.putExtra(RESULT, 1);
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent2);


        Intent intent5 = new Intent(MainQcaFragment.ACTION);
        intent5.putExtra(RESULT, 22);
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent5);
    }


    private boolean checkDatas() {
        String number = et_number_login.getText().toString()+"";

        boolean b = number.startsWith("1");
        if(number.length() != 11 || !number.startsWith("1")){
            ToastUtils.toast("手机号填写有误，请重填");
            et_number_login.requestFocus();
            KeyBoardUtils.openKeybord(et_number_login,this);
            return false;
        }
        String code = et_code_login.getText().toString() + "";
        if(code.length() != 4){
            ToastUtils.toast("请填写正确验证码");
            et_code_login.requestFocus();
            KeyBoardUtils.openKeybord(et_code_login,this);
            return false;
        }

        return true;
    }
}
