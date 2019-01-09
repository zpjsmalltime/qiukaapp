package com.mayi.mayisports;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.activity.BaseActivity;
import com.mayi.mayisports.activity.CustomBarActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.bean.MainMetaBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎页 启动页
 */
public class SplashActivity extends BaseActivity implements  View.OnClickListener {


    // 初始化定时器
    Timer timer = null;

    private void startTimer(final TextView tv) {

        timer = null;
        timer = new Timer();
     timer.schedule(new

    TimerTask() {

        @Override
        public void run () {
            int s = (int) tv.getTag();
            if(s>=0){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    int  s  = (int) tv.getTag();


                    tv.setText(" 跳过  "+s+"s ");

                    s--;


                    tv.setTag(s);

                    }
                });
            }else{
                stopTimer();
            }
        }
    },0,1000*1);
   }

    // 停止定时器
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
        handler.sendEmptyMessage(1);
    }


    // 停止定时器
    private void stopTimerNow(){
        if(timer != null){
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
        handler.removeCallbacksAndMessages(null);
    }

    private   boolean isStart = false;

        private Handler handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if(msg.what == 1) {
                    if(isStart)return;
                    isStart = true;
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                CustomBarActivity.start(SplashActivity.this);
                    finish();
                }else if(msg.what == 2){//联网失败跳过
                    RequestNetWorkUtils.kjHttp.cancelAll();
                    if(isStart)return;
                    isStart = true;
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));

                    finish();

                }
            }
        };


    @Override
    public void finish() {
        super.finish();
        //   isStart = false;
        handler.removeCallbacksAndMessages(null);
    }

    private ImageView iv_splash;
    private ViewPager vp_splash;
    private int[] imgs = {com.mayi.mayisports.R.drawable.guide_0, com.mayi.mayisports.R.drawable.guide_1, com.mayi.mayisports.R.drawable.guide_2, com.mayi.mayisports.R.drawable.guide_3};
    private ImageView[] imageViews ;


    private LinearLayout ll_splash_banner;
    private ImageView iv_banner;
    private RelativeLayout rl;

    private TextView tv_go;

    @Override
    protected void initView() {
            super.initView();

            if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
                finish();
                return;
            }


            setTitleShow(false);
            iv_splash = findViewById(com.mayi.mayisports.R.id.iv_splash);
            vp_splash = findViewById(com.mayi.mayisports.R.id.vp_splash);

            ll_splash_banner = findViewById(R.id.ll_splash_banner);
            iv_banner = findViewById(R.id.iv_banner);
            rl = findViewById(R.id.rl);

            rl.setOnClickListener(this);
            iv_banner.setOnClickListener(this);

            ll_splash_banner.setVisibility(View.VISIBLE);

            double v = DisplayUtil.getScreenWidth(this) / 200.0 * 288;
            ViewGroup.LayoutParams layoutParams = rl.getLayoutParams();
            layoutParams.height = (int) v;
            rl.setLayoutParams(layoutParams);

            tv_go = findViewById(R.id.tv_go);
            tv_go.setOnClickListener(this);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

    }


    /**
     * 请求初始化数据
     */
    private MainMetaBean mainMetaBean;
    private void requestMeta() {


        if(mainMetaBean != null)return;
        handler.sendEmptyMessageDelayed(2,1000*3);

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","meta");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                mainMetaBean = JsonParseUtils.parseJsonClass(string,MainMetaBean.class);
                if(mainMetaBean != null  ){
                    handler.removeCallbacksAndMessages(null);
                    tv_go.setTag(3);
                    if(mainMetaBean.meta != null && mainMetaBean.meta.screen_ad != null && !TextUtils.isEmpty(mainMetaBean.meta.screen_ad.img_url)){
                        rl.setVisibility(View.VISIBLE);
                        PictureUtils.showImgNoErr(mainMetaBean.meta.screen_ad.img_url,iv_banner);
                        if (!isFinishing()){
                            startTimer(tv_go);
                        }else{
                            stopTimerNow();
                        }
                    }else {
                        rl.setVisibility(View.INVISIBLE);
                        handler.sendEmptyMessageDelayed(2,1000);
                    }


                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                handler.removeCallbacksAndMessages(null);
                handler.sendEmptyMessageDelayed(2,1000);
            }
        });

    }




    @Override
    protected int setViewForContent() {
            return com.mayi.mayisports.R.layout.activity_splash;
        }


    private boolean isSend = true;

    @Override
    protected void onResume() {
        super.onResume();
        requestMeta();

    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_go:
                handler.removeCallbacksAndMessages(null);
                stopTimer();
                break;
            case R.id.iv_banner:
                try {
                    stopTimerNow();
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    WebViewActivtiy.start(this, mainMetaBean.meta.screen_ad.link, "球咖",true);
                    finish();
                }catch (Exception e){
                }
                    break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(handler != null){
            handler.removeCallbacksAndMessages(null);
        }
    }

}
