package com.mayi.mayisports.activity;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayi.mayisports.R;
import com.mayisports.qca.utils.CollectLanuchUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.HttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestEntry;
import com.mayisports.qca.utils.request_net_utils.RequestNetUtils;
import com.mob.MobSDK;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.message.PushAgent;

import org.kymjs.kjframe.http.HttpParams;

import cn.sharesdk.framework.ShareSDK;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;

/**
 * 界面基类，做一些通用功能封装
 * Created by Zpj on 2017/12/5.
 */

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {


    protected FrameLayout fl_content_base_activity;

    protected int resInt;

    protected View contentViewChild;

    protected LayoutInflater layoutInflater;

    private View main_title;


    protected ImageView iv_left_title;
    protected TextView tv_title;
    protected  ImageView iv_right_title;
    protected  TextView tv_ritht_title;

    protected TextView tv_left_title;


    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        ToastUtils.enableToast();

        if(!Utils.isActive) {
            Utils.isActive = true;
            CollectLanuchUtils.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ToastUtils.cancleToast();
       // CollectLanuchUtils.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(!Utils.isForeground(this))
        {
            Utils.isActive = false;
            CollectLanuchUtils.onPause();
        }
    }

    public void setFullScreen(){

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }

    }

    private boolean is = true;
    public void setPortarait(boolean is){
        this.is = is;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(is) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        setFullScreen();
        setContentView(R.layout.base_layout_activity);

        layoutInflater = LayoutInflater.from(this);

        /**
         * 分享
         */
        MobSDK.init(this);
        PushAgent.getInstance(getApplicationContext()).onAppStart();//启动推送失效统计，与数据统计无关，必须调用

        bindViews();
        initView();
        initDatas();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         * 分享
         */
        MobSDK.init(this);
    }

    protected void bindViews(){

        fl_content_base_activity = findViewById(R.id.content_base_activity);

         main_title = findViewById(R.id.main_title);
        iv_left_title = findViewById(R.id.iv_left_title);
        tv_title = findViewById(R.id.tv_title);
        iv_right_title = findViewById(R.id.iv_right_title);
        tv_ritht_title = findViewById(R.id.tv_ritht_title);

        tv_left_title = findViewById(R.id.tv_left_title);



    }


    protected void initView(){


        fl_content_base_activity.removeAllViews();

        resInt = setViewForContent();
        contentViewChild = layoutInflater.inflate(resInt,null);

        fl_content_base_activity.addView(contentViewChild);

    }

    protected void initDatas(){

        setLeftImageTile(iv_left_title);
        setCenterTextTitle(tv_title);
        setRightImageTitle(iv_right_title);
        setRigthTextTitle(tv_ritht_title);

    }

    protected abstract @LayoutRes int setViewForContent();

    public void setTitleShow(boolean isShow){
        if(main_title == null)return;
        if(isShow){
            main_title.setVisibility(View.VISIBLE);
        }else {
            main_title.setVisibility(View.GONE);
        }
    }

    protected void setLeftImageTile(ImageView iv_left_title){
        this.iv_left_title = iv_left_title;
    }

    protected void setRightImageTitle(ImageView iv_right_title){
        this.iv_right_title = iv_right_title;
    }

    protected void setCenterTextTitle(TextView tv_title){
        this.tv_title = tv_title;
    }

    protected void setRigthTextTitle(TextView tv_ritht_title){
        this.tv_ritht_title = tv_ritht_title;
    }
}
