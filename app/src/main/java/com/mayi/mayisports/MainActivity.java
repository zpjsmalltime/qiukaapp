package com.mayi.mayisports;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mayi.mayisports.activity.SelectScoreActivity;
import com.mayi.mayisports.activity.BaseActivity;
import com.mayi.mayisports.activity.SetPrefrencesActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.bean.MainMetaBean;
import com.mayisports.qca.bean.NewMessageBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.FindFragment;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.fragment.MainQcaFragment;
import com.mayisports.qca.fragment.MatchHomeFragment;
import com.mayisports.qca.fragment.MineFragment;
import com.mayisports.qca.fragment.ShootVideoFragment;
import com.mayisports.qca.fragment.ShootVideoNewFragment;
import com.mayisports.qca.fragment.TabLayoutFragment;
import com.mayisports.qca.utils.CollectLanuchUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.HomePagePopuWindow;
import com.mayisports.qca.view.MyRadioGroup;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 *  主界面
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, MyRadioGroup.OnClickButton {


    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "MainActivity";
    private Rec rec;
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }
    private void destroyReceiver(){
        localBroadcastManager.unregisterReceiver(rec);
    }

    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
             updateHome(0,true);
             updateDynamic(0,false);
        }
    }


    private void isFirstToast(){
        if(!SPUtils.getBoolean(this, Constant.IS_FIRST)){//第一次
              SPUtils.putBoolean(this,Constant.IS_FIRST,true);
              SetPrefrencesActivity.start(this);
              ToastUtils.toast("请选择您的偏好");
         }else{

         }
    }


    /**
     * 开启推送
     */
    private void startPushSDK() {
        PushAgent mPushAgent = PushAgent.getInstance(getApplicationContext());
        mPushAgent.enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String s) {
                String device_token = UmengRegistrar.getRegistrationId(getApplication());//注册dev_token
                Log.e("device_token",device_token+""+s);
                SPUtils.putString(QCaApplication.getContext(),Constant.DEV_PUSH_ID,device_token);
            }
        });

        String device_token = UmengRegistrar.getRegistrationId(getApplication());//注册dev_token
        Log.e("device_token",device_token+"");
        SPUtils.putString(QCaApplication.getContext(),Constant.DEV_PUSH_ID,device_token);
    }

    /**
     * 清理缓存
     */
    @Override
    protected void onDestroy() {
        SPUtils.clearCacheSelectSP();
        destroyReceiver();
        super.onDestroy();
    }

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            if(msg.what == 2){//更新
                requestNetDatas();
                requestMeta();
                int anInt = SPUtils.getInt(QCaApplication.getContext(), Constant.Sys_Msg_Interval);if(anInt == 0)anInt = 20;
                refreshHandler.sendEmptyMessageDelayed(2,1000*anInt);
            }

        }
    };


    /**
     * 请求初始化数据
     */
    private void requestNetDatas() {

        if(LoginUtils.isUnLogin())return;

        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams  params = new HttpParams();
        params.put("action","user");
        params.put("type","new_msg");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                try {
                    NewMessageBean newMessageBean = JsonParseUtils.parseJsonClass(string, NewMessageBean.class);
                    if (newMessageBean.data != null) {
                        if ("number".equals(newMessageBean.data.feedlist.show_type)) {
                            updateDynamic(newMessageBean.data.feedlist.count, false);
                        } else {
                            updateDynamic(newMessageBean.data.feedlist.count, true);
                        }

                        if ("number".equals(newMessageBean.data.msg.show_type)) {
                            updateHome(newMessageBean.data.msg.count, false);
                        } else {
                            updateHome(newMessageBean.data.msg.count, true);
                        }

                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                updateDynamic(0,false);
                updateHome(0,false);
            }
        });
    }


    /**
     * 发送广播到我的 更新消息
     */
    private void updateHome(int count,boolean isPoint) {

        Intent intent5 = new Intent(MineFragment.MINE_FG_ACTION);
        intent5.putExtra(RESULT, 66);
        intent5.putExtra("count",count);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent5);



        if(vp_main_ac.getCurrentItem()==3)return;
        if(isPoint && count>1){
            tv_dynamic_home.setText(" ");
            return;
        }

        tv_dynamic_home.setVisibility(View.VISIBLE);
        tv_dynamic_home.setText(" ");
        if(count<1) {
            tv_dynamic_home.setVisibility(View.GONE);
        } else if(count <2){
            tv_dynamic_home.setText("1");
        }else if(count<100){
            tv_dynamic_home.setText(count+"");
        }else{
            tv_dynamic_home.setText("99+");
        }

    }


    /**
     * 更新消息，关注模块。已取消
     * @param count
     * @param isPoint
     */
    private void updateDynamic(int count,boolean isPoint) {

        if(count>0){
            Intent intent1 = new Intent(MainQcaFragment.ACTION);
            intent1.putExtra(RESULT, 44);//发送到关注，控制红点
            intent1.putExtra("msg",true);
            LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);

        }else{
            Intent intent1 = new Intent(MainQcaFragment.ACTION);
            intent1.putExtra(RESULT, 44);//发送到关注，控制红点
            intent1.putExtra("msg",false);
            LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);

        }


    }



    public static void sendCanleHomePoint(){
        Intent intent1 = new Intent(MainQcaFragment.ACTION);
        intent1.putExtra(RESULT, 44);//发送到关注，控制红点
        intent1.putExtra("msg",false);
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent1);
    }


    private ViewPager vp_main_ac;
    private MyRadioGroup rg_main_ac;

    private List<Fragment> fragments = new ArrayList<>();
    private TextView tv_dynamic_home;

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(getBaseContext());
        refreshHandler.sendEmptyMessageDelayed(2,0);
        JCVideoPlayer.releaseAllVideos();

//        isFirstToast();//第一次弹窗请求

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onResume(getBaseContext());
        refreshHandler.removeCallbacksAndMessages(null);
        /**
         * 新增  释放视频资源
         */
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void setCenterTextTitle(TextView tv_title) {
        super.setCenterTextTitle(tv_title);
    }

    @Override
    protected int setViewForContent() {
        return com.mayi.mayisports.R.layout.activity_main;
    }


    private RadioButton rb_mid_icon;
    private View v_mid_icon;
    private ImageView iv_mid_icon;

    @Override
    protected void initView() {
        super.initView();
        /**
         *  开启推送
         */
        startPushSDK();
        initReceiver();


        setTitleShow(false);

        rb_mid_icon = findViewById(R.id.rb_mid_icon);
        v_mid_icon = findViewById(R.id.v_mid_icon);
        iv_mid_icon = findViewById(R.id.iv_mid_icon);
        iv_mid_icon.setOnClickListener(this);

        hideMid();

        tv_dynamic_home = findViewById(com.mayi.mayisports.R.id.tv_dynamic_home);
        vp_main_ac = findViewById(com.mayi.mayisports.R.id.vp_main_ac);
        rg_main_ac = findViewById(com.mayi.mayisports.R.id.rg_main_ac);
        vp_main_ac.setOffscreenPageLimit(4);

        rg_main_ac.setOnCheckedChangeListener(this);
        final RadioButton childAt =  findViewById(R.id.rb_square_main_ac);
        childAt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("isCheck", childAt.isChecked() + "");
                    if(childAt.isChecked()){
                        onClickButton(com.mayi.mayisports.R.id.rb_square_main_ac);
                    }
                }
                return false;
            }
        });
        final RadioButton childAt1 = findViewById(R.id.rb_score_main_ac);
        childAt1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("isCheck", childAt1.isChecked() + "");
                    if(childAt1.isChecked()){
                        onClickButton(R.id.rb_score_main_ac);
                    }
                }
                return false;
            }
        });
        final RadioButton childAt0 = findViewById(R.id.rb_live_main_ac);
        childAt0.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("isCheck", childAt0.isChecked() + "");
                    if(childAt0.isChecked()){
                        onClickButton(com.mayi.mayisports.R.id.rb_live_main_ac);
                    }
                }
                return false;
            }
        });

    }


    /**
     * 切换到主界面，球咖模块
     */
    public void setSelectHome() {

        Intent intent1 = new Intent(DynamicFragment.ACTION);
        intent1.putExtra(RESULT, 1);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);

        first = false;
        rg_main_ac.check(R.id.rb_live_main_ac);
        rg_main_ac.postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
            }
        },500);
    }

    public void setSelectHomeWithStutas() {


        if(prePosition == com.mayi.mayisports.R.id.rb_mine_main_ac && LoginUtils.isUnLogin()){
            return;
        }

        Intent intent1 = new Intent(DynamicFragment.ACTION);
        intent1.putExtra(RESULT, 1);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);

        first = false;
        rg_main_ac.check(R.id.rb_live_main_ac);
        rg_main_ac.postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
            }
        },500);
    }

    public void setSelectFirst() {


        Intent intent1 = new Intent(DynamicFragment.ACTION);
        intent1.putExtra(RESULT, 1);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);

        first = false;
        rg_main_ac.check(R.id.rb_live_main_ac);
        rg_main_ac.postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
            }
        },500);
    }


    public  void setSelect() {

        if(prePosition == com.mayi.mayisports.R.id.rb_mine_main_ac && LoginUtils.isUnLogin()){
            Intent intent1 = new Intent(HomeNewFragment.ACTION);
            intent1.putExtra(RESULT, 1);
            LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);
            prePosition = com.mayi.mayisports.R.id.rb_square_main_ac;
        }
        first = false;
        rg_main_ac.check(prePosition);
        rg_main_ac.postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
            }
        },500);
    }

    public void setMine(){
        rg_main_ac.check(com.mayi.mayisports.R.id.rb_mine_main_ac);
        vp_main_ac.setCurrentItem(3);
        rg_main_ac.postDelayed(new Runnable() {
            @Override
            public void run() {
                first = true;
            }
        },500);
    }

    private HomeNewFragment homeNewFragment;
    private DynamicFragment dynamicFragment;

    private MainQcaFragment mainQcaFragment;
    private FindFragment findFragment;


    private ShootVideoNewFragment shootVideoNewFragment;


    @Override
    protected void initDatas() {
        super.initDatas();

        dynamicFragment = new DynamicFragment();
//        fragments.add(dynamicFragment);
        mainQcaFragment = new MainQcaFragment();
        fragments.add(mainQcaFragment);//球咖模块装载


        homeNewFragment = new HomeNewFragment();
//        fragments.add(homeNewFragment);
//        findFragment = new FindFragment();
//        fragments.add(findFragment);
   //     shootVideoFragment = new ShootVideoFragment();
        shootVideoNewFragment = new ShootVideoNewFragment();
        fragments.add(shootVideoNewFragment);//关注模块装载

        fragments.add(new MatchHomeFragment());//比赛模块装载

        fragments.add(new MineFragment());//我的模块装载

        setAdapter();


        Uri uri = getIntent().getData();
        if (uri != null) {
            String name = uri.getQueryParameter("p");
            if ("video".equals(name)) {
                rg_main_ac.check(R.id.rb_square_main_ac);
            } else {
                rg_main_ac.check(R.id.rb_live_main_ac);
            }
        } else {
            rg_main_ac.check(R.id.rb_live_main_ac);
        }

    }


    /**
     * 隐藏中间跳转  球咖答题部分
     */
    private void hideMid(){
        if(v_mid_icon == null)return;
        v_mid_icon.setVisibility(View.GONE);
        iv_mid_icon.setVisibility(View.GONE);
        rb_mid_icon.setVisibility(View.GONE);
    }

    /**
     * 显示中间跳转
     */
    private void showMid(){
        if(v_mid_icon == null)return;
        v_mid_icon.setVisibility(View.VISIBLE);
        iv_mid_icon.setVisibility(View.VISIBLE);
        rb_mid_icon.setVisibility(View.VISIBLE);

        if(mainMetaBean.meta != null && mainMetaBean.meta.ad != null && !TextUtils.isEmpty(mainMetaBean.meta.ad.img_url)){
            showToastPopuWindow();
        }else {

        }
    }


    /**
     * 主界面弹窗
     */
    private   HomePagePopuWindow homePagePopuWindow;
    private void showToastPopuWindow() {
        if(homePagePopuWindow != null){
            homePagePopuWindow.dismiss();
            homePagePopuWindow = null;
        }

        homePagePopuWindow = new HomePagePopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(v.getId() == R.id.iv_ad){

                          if(homePagePopuWindow == null )return;
                            ToastUtils.toastNoStates("前往详情");
                            WebViewActivtiy.start(MainActivity.this,mainMetaBean.meta.ad.link,"球咖",true);
                            homePagePopuWindow.dismiss();



                    }else {
                        homePagePopuWindow.dismiss();
                    }
            }
        },"",mainMetaBean.meta.ad.img_url);
        homePagePopuWindow.showAtLocation(vp_main_ac, Gravity.CENTER,0,0);


    }

    /**
     * 请求初始化数据
     */
    private MainMetaBean mainMetaBean;
    private void requestMeta() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","meta");


        if(iv_mid_icon.isShown())return;
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                mainMetaBean = JsonParseUtils.parseJsonClass(string,MainMetaBean.class);
                if(mainMetaBean != null  ){


                        if (mainMetaBean.meta != null && mainMetaBean.meta.tag != null && !TextUtils.isEmpty(mainMetaBean.meta.tag.ico)) {
                            PictureUtils.show(mainMetaBean.meta.tag.ico, iv_mid_icon);
                            SPUtils.putString(QCaApplication.getContext(),Constant.MID_URL,mainMetaBean.meta.tag.url);
                            showMid();
                        } else {
                            SPUtils.putString(QCaApplication.getContext(),Constant.MID_URL,"");

                            hideMid();
                        }

                    if(mainMetaBean.meta != null && mainMetaBean.meta.token != null){
                        SPUtils.putString(QCaApplication.getContext(),Constant.MSG_TOKEN,mainMetaBean.meta.token);
                    }
                    if(mainMetaBean.bannerList != null && mainMetaBean.bannerList.size()>0) {
                        MainMetaBean.BannerBean bannerBean = mainMetaBean.bannerList.get(0);
                        SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_IMG, bannerBean.img);
                        SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_URL, bannerBean.url);
                    }


                    if(mainMetaBean.meta != null && mainMetaBean.meta.reply != null){
                        if(mainMetaBean.meta.reply.share == 1) {
                            SPUtils.putBoolean(QCaApplication.getContext(), Constant.REPLY_SHARE,true );
                        }else{
                            SPUtils.putBoolean(QCaApplication.getContext(), Constant.REPLY_SHARE, false);
                        }
                    }


                }else{
                    SPUtils.putString(QCaApplication.getContext(),Constant.MID_URL,"");
                    hideMid();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                if(!iv_mid_icon.isShown()) {
                    hideMid();
                }
            }
        });

    }


    private void setAdapter() {
        vp_main_ac.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    @IdRes
    public  int prePosition;

    private boolean first = true;
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
            switch (checkId){
                case com.mayi.mayisports.R.id.rb_score_main_ac:
                    prePosition = com.mayi.mayisports.R.id.rb_score_main_ac;


                    Intent intent3 = new Intent(MatchHomeFragment.ACTION);
                    intent3.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent3);
                    vp_main_ac.setCurrentItem(2);

                    JCVideoPlayer.releaseAllVideos();
                    break;
                case com.mayi.mayisports.R.id.rb_square_main_ac:
                    prePosition = com.mayi.mayisports.R.id.rb_square_main_ac;
                    vp_main_ac.setCurrentItem(1);
                    JCVideoPlayer.releaseAllVideos();
                    break;
                case com.mayi.mayisports.R.id.rb_live_main_ac:
                    prePosition = com.mayi.mayisports.R.id.rb_live_main_ac;
                    Intent intent2 = new Intent(MainQcaFragment.ACTION);
                    intent2.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent2);

                    vp_main_ac.setCurrentItem(0);
                    JCVideoPlayer.releaseAllVideos();
                    break;
                case com.mayi.mayisports.R.id.rb_mine_main_ac:
                    tv_dynamic_home.setVisibility(View.GONE);
                    prePosition = com.mayi.mayisports.R.id.rb_mine_main_ac;
                    vp_main_ac.setCurrentItem(3);
                    JCVideoPlayer.releaseAllVideos();
                    break;

            }
                 upadateTitle(checkId);
    }


    private void upadateTitle(int checkId){

        iv_left_title.setVisibility(View.INVISIBLE);
        iv_right_title.setVisibility(View.GONE);
        tv_ritht_title.setVisibility(View.GONE);
        tv_ritht_title.setOnClickListener(null);
        switch (checkId){
            case com.mayi.mayisports.R.id.rb_score_main_ac:
                tv_title.setText("即时比分");
                tv_ritht_title.setVisibility(View.VISIBLE);
                tv_ritht_title.setVisibility(View.VISIBLE);
                tv_ritht_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, SelectScoreActivity.class);
                        MainActivity.this.startActivity(intent);
                    }
                });

                break;
            case com.mayi.mayisports.R.id.rb_square_main_ac:
                tv_title.setText("首页");
                break;
            case com.mayi.mayisports.R.id.rb_live_main_ac:
                tv_title.setText("直播");
                break;
            case com.mayi.mayisports.R.id.rb_mine_main_ac:
                tv_title.setText("我的");
                break;

        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_mid_icon:

                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this,DynamicFragment.ACTION);
                    return;
                }

                try {
                    String url = mainMetaBean.meta.tag.url;
                     if(url.contains("/#/game")){
//                         url = "https://app.mayisports.com/#/game";
                     }
                   // url = "http://192.168.1.6:3030/#/game";
                    WebViewActivtiy.start(this, url, "球咖",true);
                }catch (Exception e){

                }

                break;
        }
    }

    //退出时的时间
    private long mExitTime;
    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.toast("再按一次退出");
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
//            System.exit(0);
        }
    }


    /**
     * 二次点击加载 监听逻辑处理
     * @param checkId
     */
    @Override
    public void onClickButton(int checkId) {
        switch (checkId){


            case com.mayi.mayisports.R.id.rb_square_main_ac:
                if(prePosition == com.mayi.mayisports.R.id.rb_square_main_ac){
                    Intent intent1 = new Intent(ShootVideoFragment.ACTION);
                    intent1.putExtra(RESULT, 66);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);


                    Intent intent2 = new Intent(DynamicFragment.ACTION);
                    intent2.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent2);

                }
                break;
            case com.mayi.mayisports.R.id.rb_live_main_ac:
                if(prePosition == com.mayi.mayisports.R.id.rb_live_main_ac) {

                    Intent intent1 = new Intent(DynamicFragment.ACTION);
                    intent1.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);


                    Intent intent2 = new Intent(ShootVideoFragment.ACTION);
                    intent2.putExtra(RESULT, 66);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent2);


                }

                break;
            case R.id.rb_score_main_ac:
                if(prePosition == com.mayi.mayisports.R.id.rb_score_main_ac) {

                    Intent intent1 = new Intent(TabLayoutFragment.ACTION);
                    intent1.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);
                }


                break;


        }
    }
}
