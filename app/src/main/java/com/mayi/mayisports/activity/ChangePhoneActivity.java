package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayi.mayisports.R;
import com.mayisports.qca.utils.KeyBoardUtils;

/**
 * 绑定手机号界面
 */
public class ChangePhoneActivity extends BaseActivity {



    private EditText et_code_login;
    private TextView tv_send_code_login;
    private TextView tv_login_login;
    private TextView tv_unlogin_login;

    private String action;//成功广播
    public static final String ACTION = "";
    private ImageView iv_left_title_;

    private ImageView tv_delete_num_login;


    private ImageView iv_weixin_login;
    private ImageView iv_weibo_login;
    private ImageView iv_qq_login;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_login3;
    }


    @SuppressLint("ClickableViewAccessibility")
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
                KeyBoardUtils.closeKeybord(et_code_login,ChangePhoneActivity.this);
                iv_left_title_.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },300);

            }
        });
        action = getIntent().getStringExtra(ACTION);

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



        tv_delete_num_login = findViewById(R.id.tv_delete_num_login);
        tv_delete_num_login.setOnClickListener(this);

    }



}
