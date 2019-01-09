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
 *切换手机号
 */
public class ChangePhoneNumberActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {


    public static void start(Activity activity,String action){
        Intent intent = new Intent(activity,ChangePhoneNumberActivity.class);
        intent.putExtra("action",action+"");
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

    private TextView tv_current_phone;

    @Override
     protected int setViewForContent() {
        return R.layout.activity_change_phone;
    }



    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);
        iv_left_title_ = findViewById(R.id.iv_left_title_);
//        tv_ritht_title.setVisibility(View.INVISIBLE);
//        tv_title.setText("免注册登录");
        iv_left_title_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyBoardUtils.closeKeybord(et_number_login,ChangePhoneNumberActivity.this);
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

        et_number_login = findViewById(R.id.et_number_login);
        et_code_login = findViewById(R.id.et_code_login);
        tv_send_code_login = findViewById(R.id.tv_send_code_login);
        tv_send_code_login.setTextColor(getResources().getColor(R.color.coment_black));
        tv_send_code_login.setOnClickListener(this);
        tv_login_login = findViewById(R.id.tv_login_login);
        tv_login_login.setOnClickListener(this);
        tv_unlogin_login = findViewById(R.id.tv_unlogin_login);
        tv_unlogin_login.setOnClickListener(this);



        tv_current_phone = findViewById(R.id.tv_current_phone);
        String moblie = SPUtils.getString(QCaApplication.getContext(), Constant.MOBLIE);
        tv_current_phone.setText("当前绑定手机号："+moblie);

        et_code_login.addTextChangedListener(new MyWatcher());

        tv_delete_num_login = findViewById(R.id.tv_delete_num_login);
        tv_delete_num_login.setOnClickListener(this);



        et_code_login.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    KeyBoardUtils.openKeybord(et_number_login,ChangePhoneNumberActivity.this);
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
        et_code_login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                String s = et_code_login.getText().toString();
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
                    KeyBoardUtils.openKeybord(et_number_login,ChangePhoneNumberActivity.this);
                }
            }
        });



        et_number_login.setText(moblie);



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
        }
    }


    private boolean isSended;
    @Override
    public void onClick(View view) {
        super.onClick(view);

        if(view.getId() == R.id.tv_send_code_login){

//            et_number_login.requestFocus();
            tv_delete_num_login.setVisibility(View.GONE);
            if(checkDatasNumber()){
                isSended = true;

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
            et_code_login.setText("");
        }
    }


    private boolean checkDatasNumber() {
        String number = et_number_login.getText().toString()+"";
        if(number.length() != 11|| !number.startsWith("1")){
            ToastUtils.toast("手机号填写有误，请重填");
//            et_number_login.requestFocus();
            KeyBoardUtils.openKeybord(et_number_login,this);
            return false;
        }
        return true;
    }


    /**
     * 计时器
     */
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

        tv_login_login.setText("验证中...");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                UserInfoBean userInfoBean = JsonParseUtils.parseJsonClass(string, UserInfoBean.class);
                if(TextUtils.isEmpty(userInfoBean.id)){
                    ToastUtils.toast(userInfoBean.status);
                    return;
                }

                BindPhoneNumActivity.start(ChangePhoneNumberActivity.this);
                finish();


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tv_login_login.setText("验证成功");
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
