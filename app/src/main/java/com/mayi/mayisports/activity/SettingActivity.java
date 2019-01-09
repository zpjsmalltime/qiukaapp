package com.mayi.mayisports.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayi.mayisports.MainActivity;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.MainQcaFragment;
import com.mayisports.qca.fragment.MineFragment;
import com.mayisports.qca.fragment.TabLayoutFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.view.ToastPricePopuWindow;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {


    private LocalBroadcastManager localBroadcastManager;
    private Rec rec;
    public static final String MINE_FG_ACTION = "SettingActivity";
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(MINE_FG_ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }
    private void destroyReceiver(){
        localBroadcastManager.unregisterReceiver(rec);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }


    @Override
    public void onResume() {
        super.onResume();

        if(LoginUtils.isLogin()){
            tv_logout_setting.setVisibility(View.VISIBLE);

            if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.V_TYPE))){
                ll_apply_vip_mine.setVisibility(View.VISIBLE);
                v_v_type_line.setVisibility(View.VISIBLE);
            }else{
                ll_apply_vip_mine.setVisibility(View.GONE);
                v_v_type_line.setVisibility(View.GONE);
            }

        }else{
            tv_logout_setting.setVisibility(View.GONE);
        }
    }

    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getIntExtra(RESULT,0) == 1){

                finish();

             }
        }
    }


    private LinearLayout ll_replay_setting;
    private LinearLayout ll_about_me_setting;
    private TextView tv_logout_setting;

    private LinearLayout ll_prefrencens_mine;
    private LinearLayout ll_apply_vip_mine;
    private View v_v_type_line;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        super.initView();
        initReceiver();
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("设置");
        iv_left_title.setOnClickListener(this);
        ll_replay_setting = findViewById(R.id.ll_replay_setting);
        ll_about_me_setting = findViewById(R.id.ll_about_me_setting);
        tv_logout_setting = findViewById(R.id.tv_logout_setting);
        v_v_type_line = findViewById(R.id.v_v_type_line);

        ll_replay_setting.setOnClickListener(this);
        ll_about_me_setting.setOnClickListener(this);
        tv_logout_setting.setOnClickListener(this);


        ll_prefrencens_mine = findViewById(R.id.ll_prefrencens_mine);
        ll_prefrencens_mine.setOnClickListener(this);
        ll_apply_vip_mine = findViewById(R.id.ll_apply_vip_mine);
        ll_apply_vip_mine.setOnClickListener(this);

        if(LoginUtils.isLogin()){
            tv_logout_setting.setVisibility(View.VISIBLE);

            if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.V_TYPE))){
                ll_apply_vip_mine.setVisibility(View.VISIBLE);
                v_v_type_line.setVisibility(View.VISIBLE);
            }else{
                ll_apply_vip_mine.setVisibility(View.GONE);
                v_v_type_line.setVisibility(View.GONE);
            }

        }else{
            tv_logout_setting.setVisibility(View.GONE);
        }


    }

    private ToastPricePopuWindow toastPricePopuWindow;


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){

            case R.id.iv_left_title:
                finish();
                break;
            case R.id.ll_replay_setting:
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/feedback","意见反馈");
                Intent intent = new Intent(this,ReplayActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_about_me_setting: //#/aboutUs
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/aboutUs","关于我们");
                Intent intent_ = new Intent(this,AboutMeActivity.class);
                startActivity(intent_);
                break;
            case R.id.tv_logout_setting:


                String str = "确认要退出吗?";
                toastPricePopuWindow = new ToastPricePopuWindow(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(v.getId() == R.id.tv_go_toast_price){//右边
                            toastPricePopuWindow.dismiss();
                        }else if(v.getId() == R.id.tv_cancle_toast_price){//左边
                            LoginUtils.removeLoginTags();
                            Intent intent = new Intent(MineFragment.MINE_FG_ACTION);
                            intent.putExtra(RESULT,4);
                            LocalBroadcastManager.getInstance(SettingActivity.this.getBaseContext()).sendBroadcast(intent);


                            Intent intent2 = new Intent(TabLayoutFragment.ACTION);
                            intent2.putExtra(RESULT, 1);
                            LocalBroadcastManager.getInstance(SettingActivity.this.getBaseContext()).sendBroadcast(intent2);

                            Intent intent3 = new Intent(MainActivity.ACTION);
                            intent3.putExtra(RESULT, 1);
                            LocalBroadcastManager.getInstance(SettingActivity.this.getBaseContext()).sendBroadcast(intent3);

                            Intent intent5 = new Intent(MainQcaFragment.ACTION);
                            intent5.putExtra(RESULT, 22);
                            LocalBroadcastManager.getInstance(SettingActivity.this).sendBroadcast(intent5);


                            finish();
                        }



                    }
                }, str, "确定", "考虑一下");
                toastPricePopuWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);



                break;
            case R.id.ll_prefrencens_mine:
                SetPrefrencesActivity.start(this);
                break;
            case R.id.ll_apply_vip_mine://申请球咖认证
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this, MineFragment.MINE_FG_ACTION);
                    return;
                }
                WebViewActivtiy.start(this,Constant.BASE_URL+"applyV/applyV.html?hideDownloadBar=1","申请认证",true,true);
                break;
        }
    }



}
