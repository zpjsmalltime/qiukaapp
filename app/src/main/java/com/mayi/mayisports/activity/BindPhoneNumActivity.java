package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 *绑定手机号界面
 * ceshi test
 */
public class BindPhoneNumActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,BindPhoneNumActivity.class);
        activity.startActivity(intent);

    }


    public static void start(Activity activity, int sortId, String id,  String token,  String weiboName, String weiboIcon){
        Intent intent = new Intent(activity,BindPhoneNumActivity.class);
        intent.putExtra("sortId",sortId);
        intent.putExtra("id",id+"");
        intent.putExtra("token",token+"");
        intent.putExtra("weiboName",weiboName+"");
        intent.putExtra("weiboIcon",weiboIcon+"");
        activity.startActivity(intent);

    }



    private EditText et_number_login;
    private EditText et_code_login;
    private TextView tv_send_code_login;
    private TextView tv_login_login;
    private TextView tv_unlogin_login;

    private String action;//成功广播
    public static final String ACTION = "";
    private ImageView iv_left_title_;

    private ImageView tv_delete_num_login;




    private RelativeLayout rl_load_layout;

    private TextView tv_finish;

    @Override
     protected int setViewForContent() {
        return R.layout.activity_bind_num;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);
        iv_left_title_ = findViewById(R.id.iv_left_title_);
        iv_left_title_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_number_login,BindPhoneNumActivity.this);
                iv_left_title_.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },300);

            }
        });
        action = getIntent().getStringExtra(ACTION);

        rl_load_layout = findViewById(R.id.rl_load_layout);


        tv_finish = findViewById(R.id.tv_finish);



        et_number_login = findViewById(R.id.et_number_login);
        et_code_login = findViewById(R.id.et_code_login);
        tv_send_code_login = findViewById(R.id.tv_send_code_login);
        tv_send_code_login.setOnClickListener(this);
        tv_login_login = findViewById(R.id.tv_login_login);
        tv_login_login.setOnClickListener(this);
        tv_unlogin_login = findViewById(R.id.tv_unlogin_login);
        tv_unlogin_login.setOnClickListener(this);



        et_number_login.addTextChangedListener(new MyWatcher());

        tv_delete_num_login = findViewById(R.id.tv_delete_num_login);
        tv_delete_num_login.setOnClickListener(this);



        et_number_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    KeyBoardUtils.openKeybord(et_number_login,BindPhoneNumActivity.this);
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
                    KeyBoardUtils.openKeybord(et_number_login,BindPhoneNumActivity.this);
                }
            }
        });




    }



    private int sortId;
    private String id;
    private String token;
    private String weiboName;
    private String weiboIcon;
    @Override
    protected void initDatas() {
        super.initDatas();
        /**
         * intent.putExtra("sortId",sortId+"");
         intent.putExtra("id",id+"");
         intent.putExtra("token",token+"");
         intent.putExtra("weiboName",weiboName+"");
         intent.putExtra("weiboIcon",weiboIcon+"");
         */
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        tv_finish.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(id)){
            sortId = intent.getIntExtra("sortId",-1);
            this.id = id;
            token = intent.getStringExtra("token");
            weiboName = intent.getStringExtra("weiboName");
            weiboIcon = intent.getStringExtra("weiboIcon");
            tv_finish.setVisibility(View.VISIBLE);
            tv_finish.setOnClickListener(this);
        }



    }

    private void submitWeiboDatas(final int sortId,final String id, final String token, final String weiboName,final String weiboIcon){
        //api.php?action=user&type=authinfo&id=&value=  //api.php?action=user&type=authinfo&id={user_id}&value={token}
        String url = Constant.BASE_URL + "/php/api.php";
        //action=user&type=auth_unlogin&platform={weixin,qq,weibo}
        //api.php?action=user&type=auth_login&platform=(weixin,weibo,qq)&nickname=&headurl=
        HttpParams params = new HttpParams();
        params.put("action","user");
        if(TextUtils.isEmpty(token) && TextUtils.isEmpty(id)){
            params.put("type", "auth_unlogin");
        }else {
            params.put("type", "auth_login");
            params.put("id",id);
            params.put("value",token);
            params.put("nickname",weiboName);
            params.put("headurl",weiboIcon);
        }

        String platformName = Utils.getPlatformNameBySortId(sortId);

        params.put("platform",platformName);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string, RewardBean.class);
                //&&rewardBean.status.first_auth == 1
                if(rewardBean != null && rewardBean.status != null && rewardBean.status.errno == 0){

                    BindPhoneNumActivity.this.id = null;

                    if(LoginUtils.isUnLogin()) {
                        LoginUtils.saveLoginTags(rewardBean.status.user_id, "");
                        LoginActivity.loginOk(MineFragment.MINE_FG_ACTION);
                        ToastUtils.toast("登录成功");
                    }else{
                       ToastUtils.toast("绑定成功");
                    }

                    finish();
                }else{
                    ToastUtils.toast(rewardBean.status.errstr+"");


                    if(LoginUtils.isUnLogin()) {
//                        ToastUtils.toast("登录成功");
                    }else{
                        tv_login_login.setText("绑定");
                        ToastUtils.toast("关联失败，可用当前手机号登录");
                        finish();
                    }

                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                if(LoginUtils.isUnLogin()){
                    ToastUtils.toast("登录失败，请重试");
                    BindPhoneNumActivity.this.id = null;
                    LoginUtils.goLoginActivity(BindPhoneNumActivity.this,MineFragment.MINE_FG_ACTION);
                    finish();
                }else{
                    ToastUtils.toast("关联失败，可用当前手机号登录");
                    tv_login_login.setText("绑定");
                    finish();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                BindPhoneNumActivity.this.id = null;
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
               // requestCode();
                requestCheckNum();
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
        }else if(view.getId() == R.id.tv_finish){
            finish();
        }
    }



    /**
     * 验证手机号
     * {"status":{"errno":1,"errstr":"\u9519\u8bef\u7684\u624b\u673a\u53f7"}}
     */
    private void requestCheckNum() {
///php/api.php?action=user&date=&type=assertcode_new&mobile=13290505500&source=other&network=other&appstore=other
//        String url = "http://blog.csdn.net/kontrol/article/details/7767983";
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","get_mobile_bind_status");
        String number = et_number_login.getText().toString().trim();
        params.put("mobile",number);


        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                LoginBean loginBean = JsonParseUtils.parseJsonClass(string, LoginBean.class);
                if(loginBean != null && loginBean.data != null ) {
                    if(loginBean.data.bind == 0){
                        requestCode();
                    }else {
                        ToastUtils.toast("该账号已注册,不能绑定");
                        countHanlder.sendEmptyMessage(stop);
                    }
                }else{
                    if(loginBean != null && loginBean.status != null && loginBean.status.errstr != null){
                        ToastUtils.toast(loginBean.status.errstr);
                    }
                    countHanlder.sendEmptyMessage(stop);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                countHanlder.sendEmptyMessage(stop);
                ToastUtils.toast(Constant.NET_FAIL_TOAST+"");
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

        String string = SPUtils.getString(QCaApplication.getContext(), Constant.MSG_TOKEN);
        String s = Utils.MD5(number + string);
        String s1 = Utils.MD5(s);
        params.put("token",s1);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                LoginBean loginBean = JsonParseUtils.parseJsonClass(string, LoginBean.class);
                if(loginBean != null && loginBean.status != null && loginBean.status.errstr != null) {
                   // ToastUtils.toast(loginBean.status.errstr);
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
        final String number = et_number_login.getText().toString();
        params.put("mobile",number);
        params.put("code",et_code_login.getText().toString());
        params.put("source","android");

        tv_login_login.setText("绑定中...");
        rl_load_layout.setVisibility(View.VISIBLE);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                UserInfoBean userInfoBean = JsonParseUtils.parseJsonClass(string, UserInfoBean.class);
                if(TextUtils.isEmpty(userInfoBean.id)){
                    ToastUtils.toast(userInfoBean.status);
                    rl_load_layout.setVisibility(View.GONE);
                    tv_login_login.setText("绑定");
                    return;
                }


                if(BindPhoneNumActivity.this.id != null){
                    LoginUtils.saveLoginTags(userInfoBean.id,userInfoBean.token);
                    LoginActivity.loginOk(MineFragment.MINE_FG_ACTION);
                    ToastUtils.toast("绑定成功，关联第三方账号");
                    submitWeiboDatas(sortId,id,token,weiboName,weiboIcon);
                }else{
                    SPUtils.putString(QCaApplication.getContext(),Constant.MOBLIE,number);
                    finish();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(Constant.NET_FAIL_TOAST);
                  rl_load_layout.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(et_code_login,this);
        if(id != null) {
            submitWeiboDatas(sortId,id,token,weiboName,weiboIcon);
            return;
        }


        super.finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        action = getIntent().getStringExtra(ACTION);
    }

    public static final String RESULT = "result";


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
