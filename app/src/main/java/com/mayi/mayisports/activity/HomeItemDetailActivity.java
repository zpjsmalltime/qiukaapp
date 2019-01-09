package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.HomeItemBean;
import com.mayisports.qca.bean.MatchThumbupBean;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.RewardBean;
import com.mayisports.qca.bean.TopicDetailBean;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.BindViewUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.RefreshSocreUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.RewardPopuwind;
import com.mayisports.qca.view.SelectPicPopupWindow;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastMsgPopuWindow;
import com.mayisports.qca.view.ToastPricePopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 推荐详情界面
 */
public class HomeItemDetailActivity extends BaseActivity implements  RewardPopuwind.CoinCheckListener, RequestHttpCallBack.ReLoadListener, RefreshSocreUtils.OnRefreshCallBack, PlatformActionListener {



    public static void start(Activity activity,String userId,String betId){
        Intent intent = new Intent(activity,HomeItemDetailActivity.class);
        intent.putExtra("userId",userId);
        intent.putExtra("betId",betId);
        activity.startActivity(intent);
    }

    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2){//更新
                rl_load_layout = null;
                RefreshSocreUtils.getDataForHomeItemDetail(homeItemBean,HomeItemDetailActivity.this);
                refreshHandler.sendEmptyMessageDelayed(2,1000*20);
            }

        }
    };

    private ImageView iv_header_home_item_detail;
    private ImageView iv_vip_header;
    private View ll_name_home_item_detail;
    private TextView tv_name_home_detail;
    private ImageView iv_host_icon_home_detail;
    private ImageView iv_away_icon_home_detail;
    private TextView tv_host_name_home_detail;
    private TextView tv_away_name_home_detail;
    private TextView tv_cent_top_home_detail;
    private TextView tv_cent_bottom_home_detail;
    private LinearLayout ll_pan_container_home_detail;
    private TextView tv_content_home_detail;
    private TextView tv_thumbup_home_detail;
    private TextView tv_price_home_detail;
    private TextView tv_publish_home_detail;
    private FrameLayout fl_thumbup_home_detail;
    private FrameLayout fl_price_home_detail;
    private FrameLayout fl_publish_home_detail;

    private TextView tv_follow_home_detail;
    private LinearLayout ll_tag_container_home_detail;
    private LinearLayout ll_center_top_home_detail;

    private RelativeLayout rl_load_layout;
    private WebView wv_content_home_detail;

    private TextView tv_create_home_detail;
    private WebView webview_content_home_detail;
    private TextView tv_title_home_detail;


    @Override
    protected int setViewForContent() {
        return R.layout.activity_home_item_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_title.setText("球咖");
        iv_right_title.setVisibility(View.VISIBLE);
        iv_right_title.setImageDrawable(getResources().getDrawable(R.drawable.dot_more));
        iv_right_title.setOnClickListener(this);
        tv_ritht_title.setVisibility(View.GONE);

        rl_load_layout = findViewById(R.id.rl_load_layout);
        webview_content_home_detail = findViewById(R.id.webview_content_home_detail);
        webview_content_home_detail.setClickable(false);
        webview_content_home_detail.setEnabled(false);
        tv_title_home_detail = findViewById(R.id.tv_title_home_detail);



        WebSettings settings = webview_content_home_detail.getSettings();
//        settings.setSupportZoom(true);
        settings.setJavaScriptEnabled(false);
//        settings.setBuiltInZoomControls(true);
//        settings.setUseWideViewPort(true);
//        settings.setLoadWithOverviewMode(true);
//        settings.setDisplayZoomControls(true);
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setUseWideViewPort(true);
        webview_content_home_detail.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                return null;
            }
        });

        tv_create_home_detail = findViewById(R.id.tv_create_home_detail);

        tv_follow_home_detail = findViewById(R.id.tv_follow_home_detail);
        tv_follow_home_detail.setOnClickListener(this);

        iv_header_home_item_detail = findViewById(R.id.iv_header_home_item_detail);
        iv_vip_header = findViewById(R.id.iv_vip_header);
        ll_tag_container_home_detail = findViewById(R.id.ll_tag_container_home_detail);
        iv_header_home_item_detail.setOnClickListener(this);
        ll_name_home_item_detail = findViewById(R.id.ll_name_home_item_detail);
        ll_name_home_item_detail.setOnClickListener(this);
        iv_left_title = findViewById(R.id.iv_left_title);
        iv_left_title.setOnClickListener(this);

        tv_name_home_detail = findViewById(R.id.tv_name_home_detail);

        iv_host_icon_home_detail = findViewById(R.id.iv_host_icon_home_detail);
        iv_away_icon_home_detail = findViewById(R.id.iv_away_icon_home_detail);
        tv_host_name_home_detail = findViewById(R.id.tv_host_name_home_detail);
        tv_away_name_home_detail = findViewById(R.id.tv_away_name_home_detail);
        tv_cent_top_home_detail = findViewById(R.id.tv_cent_top_home_detail);
        ll_center_top_home_detail = findViewById(R.id.ll_center_top_home_detail);
        tv_cent_bottom_home_detail = findViewById(R.id.tv_cent_bottom_home_detail);
        ll_pan_container_home_detail = findViewById(R.id.ll_pan_container_home_detail);
        tv_content_home_detail = findViewById(R.id.tv_content_home_detail);
        wv_content_home_detail = findViewById(R.id.wv_content_home_detail);
        tv_thumbup_home_detail = findViewById(R.id.tv_thumbup_home_detail);
        tv_thumbup_home_detail.setOnClickListener(this);
        tv_price_home_detail = findViewById(R.id.tv_price_home_detail);
        tv_price_home_detail.setOnClickListener(this);
        tv_publish_home_detail = findViewById(R.id.tv_publish_home_detail);
        tv_publish_home_detail.setOnClickListener(this);
        fl_thumbup_home_detail = findViewById(R.id.fl_thumbup_home_detail);
        fl_price_home_detail = findViewById(R.id.fl_price_home_detail);
        fl_publish_home_detail = findViewById(R.id.fl_publish_home_detail);

//        fl_thumbup_home_detail.setOnClickListener(this);
//        fl_price_home_detail.setOnClickListener(this);
//        fl_publish_home_detail.setOnClickListener(this);
    }

    private String userId;
    private String betId;
    private RewardPopuwind rewardPopuwind;
    @Override
    protected void initDatas() {
        super.initDatas();
        userId = getIntent().getStringExtra("userId");
        betId = getIntent().getStringExtra("betId");

        requestNetData();

    }

    private HomeItemBean homeItemBean;
    private void requestNetData() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","recommendation_Detail");
        params.put("type","result");
        params.put("betId",betId+"");
        params.put("user_id",userId+"");
        params.put("from",3);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                 homeItemBean = JsonParseUtils.parseJsonClass(string,HomeItemBean.class);
                refreshHandler.sendEmptyMessageDelayed(2,1300);
                 if(homeItemBean != null) updateView();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");
    private DecimalFormat df = new DecimalFormat("#.00");

    private void updateView() {


        if(homeItemBean == null || homeItemBean.data == null)return;

        try {
            SPUtils.putString(this, Constant.COIN, homeItemBean.data.user.coin);

        }catch (Exception e){

        }


        if(homeItemBean.data.user != null) {
            PictureUtils.showCircle(homeItemBean.data.user.headurl + "", iv_header_home_item_detail);

            if (!TextUtils.isEmpty(homeItemBean.data.user.verify_type) && Integer.valueOf(homeItemBean.data.user.verify_type) > 0) {
                iv_vip_header.setVisibility(View.VISIBLE);
            } else {
                iv_vip_header.setVisibility(View.GONE);
            }

            tv_name_home_detail.setText(homeItemBean.data.user.nickname + "");

            ViewStatusUtils.addTagsPersonal(ll_tag_container_home_detail, homeItemBean.data.user.tag, homeItemBean.data.user.tag1);

            if (!"1".equals(homeItemBean.data.user.user_type)) {

                if (homeItemBean.data.user.follow_status == 1) {//关注
                    tv_follow_home_detail.setTag(1);
                    tv_follow_home_detail.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                    tv_follow_home_detail.setTextColor(Color.parseColor("#e8e8e8"));
                    tv_follow_home_detail.setText("已关注");
                    tv_follow_home_detail.setVisibility(View.GONE);
                } else {
                    tv_follow_home_detail.setTag(0);
                    tv_follow_home_detail.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                    tv_follow_home_detail.setTextColor(Color.parseColor("#282828"));
                    tv_follow_home_detail.setText(" 关注 ");
                    tv_follow_home_detail.setVisibility(View.VISIBLE);
                }
            } else {
                tv_follow_home_detail.setVisibility(View.GONE);
                PictureUtils.show(R.drawable.big_data_header, iv_header_home_item_detail);
            }

        }

        if(SPUtils.getString(this,Constant.USER_ID).equals(userId)){
            tv_follow_home_detail.setVisibility(View.GONE);
            tv_follow_home_detail.setVisibility(View.GONE);
        }
        HomeItemBean.DataBean.MatchBean match = homeItemBean.data.match;

        if(match != null) {

            if (!TextUtils.isEmpty(homeItemBean.data.match.logoH) && !"0".equals(homeItemBean.data.match.logoH)) {
                String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_" + homeItemBean.data.match.logoH + ".png";
                PictureUtils.show(hostUrl, iv_host_icon_home_detail);
            }

            if (!TextUtils.isEmpty(homeItemBean.data.match.logoG) && !"0".equals(homeItemBean.data.match.logoG)) {
                String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_" + homeItemBean.data.match.logoG + ".png";
                PictureUtils.show(hostUrl, iv_away_icon_home_detail);
            }


            tv_host_name_home_detail.setText(URLDecoder.decode(match.hostTeamName));
            tv_away_name_home_detail.setText(URLDecoder.decode(match.awayTeamName));


            if ("COMPLETE".equals(match.status)) {
                tv_cent_top_home_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                String s = match.timezoneoffset + "000";
                Long time = Long.valueOf(s);
                tv_cent_top_home_detail.setText(URLDecoder.decode(match.leagueName) + "  " + sdf.format(time));

                tv_cent_bottom_home_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                tv_cent_bottom_home_detail.setTextColor(getResources().getColor(R.color.red_));
                String bottom = match.hostScore + " : " + match.awayScore;
                tv_cent_bottom_home_detail.setText(bottom);

                tv_publish_home_detail.setEnabled(false);
                fl_publish_home_detail.setEnabled(false);

                refreshHandler.removeCallbacksAndMessages(null);

            } else if ("FIRST_HALF".equals(match.status)) {

                tv_cent_top_home_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                setProcess(ll_center_top_home_detail);


                tv_cent_bottom_home_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                tv_cent_bottom_home_detail.setTextColor(Color.parseColor("#3EA53B"));
                String bottom = match.hostScore + " : " + match.awayScore;
                tv_cent_bottom_home_detail.setText(bottom);
            } else if ("SECOND_HALF".equals(match.status)) {

                setProcess(ll_center_top_home_detail);


                tv_cent_bottom_home_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                tv_cent_bottom_home_detail.setTextColor(Color.parseColor("#3EA53B"));
                String bottom = match.hostScore + " : " + match.awayScore;
                tv_cent_bottom_home_detail.setText(bottom);
            } else if ("HALF_TIME".equals(match.status)) {

                setProcess(ll_center_top_home_detail);


                tv_cent_bottom_home_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                tv_cent_bottom_home_detail.setTextColor(Color.parseColor("#3EA53B"));
                String bottom = match.hostScore + " : " + match.awayScore;
                tv_cent_bottom_home_detail.setText(bottom);


                /**
                 *  ViewGroup scoreView = (ViewGroup) View.inflate(getBaseContext(),R.layout.score_match_detail_layout,null);
                 TextView scoreTV = (TextView) scoreView.getChildAt(0);
                 scoreTV.setTextColor(Color.parseColor("#3EA53B"));
                 scoreTV.setText(homeItemBean.data.match.hostScore+" : "+homeItemBean.data.match.awayScore);
                 if("HALF_TIME".equals(homeItemBean.data.match.status)){
                 tv.setText("中场");
                 tv.setVisibility(View.GONE);
                 scoreTV.setTextColor(Color.parseColor("#62AFEF"));
                 }else {
                 tv_time_pos.setVisibility(View.VISIBLE);
                 tv_time_pos.startAnimation(BindViewUtils.alphaAnimation);
                 }


                 ll_center_container.addView(scoreView);

                 ViewGroup smallScoreView = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
                 TextView smallTV = (TextView) smallScoreView.getChildAt(0);
                 if("HALF_TIME".equals(homeItemBean.data.match.status)){
                 smallTV.setText("中场");
                 }else{
                 smallTV.setText(homeItemBean.data.match.hostHalfScore+" : "+homeItemBean.data.match.awayHalfScore);
                 }


                 ll_center_container.addView(smallScoreView);
                 */
            } else {
                tv_cent_top_home_detail.setText(URLDecoder.decode(match.leagueName));

                String s = match.timezoneoffset + "000";
                Long time = Long.valueOf(s);
                tv_cent_bottom_home_detail.setText(sdf.format(time));
            }


        }


        HomeItemBean.DataBean.OddsBean odds = homeItemBean.data.odds;
        if(odds != null) {
            ll_pan_container_home_detail.removeAllViews();
               View view = View.inflate(getApplicationContext(),R.layout.home_detail_pan_layout,null);
               TextView tv_type_pan = view.findViewById(R.id.tv_type_pan);
               TextView tv_title_pan = view.findViewById(R.id.tv_title_pan);
               int type = 0;
               if(odds.asia != null){//亚盘
                   type = 1;
                   tv_type_pan.setText("亚盘");
               }else if(odds.score != null){//大小球
                   type = 2;
                    tv_type_pan.setText("大小球");
               }else if(odds.europeOdds != null){//欧盘
                   type = 3;
                    tv_type_pan.setText("欧盘");
               }
               tv_title_pan.setText("猜两队90分钟内(含补时)的让球结果");

            HomeItemBean.DataBean.RecommendationBean recommendation = homeItemBean.data.recommendation;
            ImageView iv_result_pan = view.findViewById(R.id.iv_result_pan);
            if("COMPLETE".equals(match.status)) {
                 iv_result_pan.setVisibility(View.VISIBLE);
                 ViewStatusUtils.parseStatusForImg(homeItemBean.data.revenueStr,iv_result_pan);
            }else{
                iv_result_pan.setVisibility(View.GONE);
            }


            LinearLayout ll_result_pan = view.findViewById(R.id.ll_result_pan);
            TextView tv_left = view.findViewById(R.id.tv_left);
            TextView tv_m = view.findViewById(R.id.tv_m);
            TextView tv_right = view.findViewById(R.id.tv_right);

            /**
             * "hostOdds": "8600",
             "awayOdds": "9600",
             "tape": "32500",
             "smallScoreOdds": 0.78
             */
            if(recommendation != null){
                try {
                        if(recommendation.bet.getClass().getField("hostOdds").get(recommendation.bet) != null&&type == 1){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_2_left));
                            String s0 = Utils.parseOddOfHandicap(odds.asia.tape) + "   "+df.format(odds.asia.hostOdds + 1);
                            tv_left.setText(s0);
                            String a = "";
                            if(odds.asia.tape.contains("-")){
                                a = odds.asia.tape;
                                a = a.replace("-", "");
                            }else{
                                a = "-"+odds.asia.tape;
                            }
                            String s1 =""+ Utils.parseOddOfHandicap(a) + "   "+df.format(Double.valueOf(odds.asia.awayOdds) + 1);
                            tv_right.setText(s1);

                        }else if(recommendation.bet.getClass().getField("awayOdds").get(recommendation.bet) != null&&type == 1){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_2_rig));
                            String s0 = Utils.parseOddOfHandicap(odds.asia.tape) + "   "+df.format(odds.asia.hostOdds + 1);
                            tv_left.setText(s0);
                            String a = "";
                            if(odds.asia.tape.contains("-")){
                                a = odds.asia.tape;
                                a = a.replace("-", "");
                            }else{
                                a = "-"+odds.asia.tape;
                            }
                            String s1 =""+ Utils.parseOddOfHandicap(a) + "   "+df.format(Double.valueOf(odds.asia.awayOdds) + 1);
                            tv_right.setText(s1);

                        }else if(recommendation.bet.getClass().getField("winOdds").get(recommendation.bet) != null && type == 3){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_3_left));
                            tv_m.setVisibility(View.VISIBLE);
                            double v = odds.europeOdds.winOdds / 10000.0;
                            tv_left.setText("主胜   "+df.format(v));
                            double v1 = odds.europeOdds.drowOdds / 10000.0;
                            tv_m.setText("平局   "+df.format(v1));
                            double v2 = Integer.valueOf(odds.europeOdds.loseOdds) / 10000.0;
                            tv_right.setText("客胜   "+df.format(v2));

                        }else if(recommendation.bet.getClass().getField("drowOdds").get(recommendation.bet) != null&&type == 3){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_3_m));
                            tv_m.setVisibility(View.VISIBLE);
                            double v = odds.europeOdds.winOdds / 10000.0;
                            tv_left.setText("主胜   "+df.format(v));
                            double v1 = odds.europeOdds.drowOdds / 10000.0;
                            tv_m.setText("平局   "+df.format(v1));
                            double v2 = Integer.valueOf(odds.europeOdds.loseOdds) / 10000.0;
                            tv_right.setText("客胜   "+df.format(v2));

                        }else  if(recommendation.bet.getClass().getField("loseOdds").get(recommendation.bet) != null && type == 3){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_3_righ));
                            tv_m.setVisibility(View.VISIBLE);
                            double v = odds.europeOdds.winOdds / 10000.0;
                            tv_left.setText("主胜   "+df.format(v));
                            double v1 = odds.europeOdds.drowOdds / 10000.0;
                            tv_m.setText("平局   "+df.format(v1));
                            double v2 = Integer.valueOf(odds.europeOdds.loseOdds) / 10000.0;
                            tv_right.setText("客胜   "+df.format(v2));

                        }else  if(recommendation.bet.getClass().getField("bigScoreOdds").get(recommendation.bet) != null&&type == 2){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_2_left));
                            String s0 = "大于" + Utils.parseOddOfHandicap(odds.score.tape) + "   "+df.format(Double.valueOf(odds.score.hostOdds)/10000 + 1);
                            tv_left.setText(s0);
                            String s1 = "小于" + Utils.parseOddOfHandicap(odds.score.tape) + "   "+df.format(Double.valueOf(odds.score.awayOdds)/10000 + 1);
                            tv_right.setText(s1);

                            //smallScoreOdds
                        }else if(recommendation.bet.getClass().getField("smallScoreOdds").get(recommendation.bet) != null&&type == 2){
                            ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_2_rig));
                            String s0 = "大于" + Utils.parseOddOfHandicap(odds.score.tape) + "   "+df.format(Double.valueOf(odds.score.hostOdds)/10000 + 1);
                            tv_left.setText(s0);
                            String s1 = "小于" + Utils.parseOddOfHandicap(odds.score.tape) + "   "+df.format(Double.valueOf(odds.score.awayOdds)/10000 + 1);
                            tv_right.setText(s1);
                        }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ll_pan_container_home_detail.addView(view);
        }



        if(homeItemBean.data.recommendation != null && !TextUtils.isEmpty(homeItemBean.data.recommendation.title)){
            tv_title_home_detail.setText(homeItemBean.data.recommendation.title);
            tv_title_home_detail.setVisibility(View.VISIBLE);
        }else{
            tv_title_home_detail.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(homeItemBean.data.reason)) {
            homeItemBean.data.reason = homeItemBean.data.reason.replaceAll("\\n","<br/>");
            webview_content_home_detail.loadDataWithBaseURL(null, homeItemBean.data.reason+"", "text/html" , "utf-8", null);
            tv_content_home_detail.setText(homeItemBean.data.reason + "");
//            wv_content_home_detail.loadDataWithBaseURL(null, homeItemBean.data.reason + "", "text/html" , "utf-8", null);
            Long aLong = Long.valueOf(homeItemBean.data.recommendation.create_time + "000");
            String format = Utils.simpleDateFormatYYMMDD.format(aLong);
            if("今天".equals(Utils.getDayTommowYesterdy(aLong))){
                format = Utils.simpleDateFormatHHMM.format(aLong);
            }
            tv_create_home_detail.setText(format+"发布");
        }else{
            webview_content_home_detail.setVisibility(View.GONE);
            tv_content_home_detail.setVisibility(View.GONE);
            tv_create_home_detail.setVisibility(View.GONE);
        }




//        homeItemBean.data.user



//        if(homeItemBean.data.user.praise_status == 1){//点赞
//            Drawable drawable= getResources().getDrawable(R.drawable.thumbup_check);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tv_thumbup_home_detail.setCompoundDrawables(drawable,null,null,null);
//            tv_thumbup_home_detail.setTag(true);
//        }else {
//            Drawable drawable= getResources().getDrawable(R.drawable.thumbup_gray);
//            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tv_thumbup_home_detail.setCompoundDrawables(drawable,null,null,null);
//            tv_thumbup_home_detail.setTag(false);
//        }
//
//        tv_thumbup_home_detail.setText("点赞·"+homeItemBean.data.user.praise_count);
        updateThumbup();


        String string = SPUtils.getString(QCaApplication.getContext(), Constant.PRE_TOAST_TIME);
        if(!TextUtils.isEmpty(string)) {
            int dayCount = Utils.getDayString(new Date(Long.valueOf(string)));
            if(dayCount>0){
                //弹出

                showPriceToast();
                SPUtils.putString(QCaApplication.getContext(),Constant.PRE_TOAST_TIME,System.currentTimeMillis()+"");

            }
        }else{
            //弹出
            showPriceToast();
            SPUtils.putString(QCaApplication.getContext(),Constant.PRE_TOAST_TIME,System.currentTimeMillis()+"");

        }

    }

    private void setProcess(LinearLayout ll_center_container) {
        ll_center_container.removeAllViews();

        ViewGroup vg1 = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
        TextView textView = (TextView) vg1.getChildAt(0);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        textView.setText(URLDecoder.decode(homeItemBean.data.match.leagueName)+"  ");
        ll_center_container.addView(vg1);


        ViewGroup dayView = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
        TextView tv = (TextView) dayView.getChildAt(0);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        Long timeLong = Long.valueOf(homeItemBean.data.match.timezoneoffset + "000");
        tv.setText(Utils.simpleDateFormatYYMMDD.format(timeLong));

        if("SECOND_HALF".equals(homeItemBean.data.match.status)){
            String minStart = Utils.getMinStart(Long.valueOf(homeItemBean.data.match.timezoneoffset + "000"));
            int i = Integer.valueOf(minStart) + 45;
            tv.setText(i+"");
        }else{
            String minStart = Utils.getMinStart(Long.valueOf(homeItemBean.data.match.timezoneoffset + "000"));
            tv.setText(minStart);
        }

        tv.setTextColor(Color.parseColor("#3EA53B"));
        TextView tv_time_pos = dayView.findViewById(R.id.tv_time_pos);
        tv_time_pos.setVisibility(View.VISIBLE);
        tv_time_pos.startAnimation(BindViewUtils.alphaAnimation);

        if("HALF_TIME".equals(homeItemBean.data.match.status)){
            tv.setText("中场");
            tv_time_pos.setVisibility(View.GONE);
            tv.setTextColor(Color.parseColor("#62AFEF"));
            ll_center_container.removeViewAt(0);
        }


        ll_center_container.addView(dayView);


    }

    private void updateThumbup(){
        try {
            if (homeItemBean.data.user.praise_status == 1) {//点赞
                Drawable drawable = getResources().getDrawable(R.drawable.thumbup_check);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_thumbup_home_detail.setCompoundDrawables(drawable, null, null, null);
                tv_thumbup_home_detail.setTag(true);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.thumbup_gray);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_thumbup_home_detail.setCompoundDrawables(drawable, null, null, null);
                tv_thumbup_home_detail.setTag(false);
            }
            DataBindUtils.setBottomPraise(tv_thumbup_home_detail, homeItemBean.data.user.praise_count);
        }catch (Exception e){

        }
    }


    private SelectPicPopupWindow selectPicPopupWindow;
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_header_home_item_detail:
            case R.id.ll_name_home_item_detail:

                Intent intent = new Intent(this,PersonalDetailActivity.class);
                intent.putExtra("id",userId);
                HomeItemDetailActivity.this.startActivity(intent);

                break;

            case R.id.iv_left_title:
                finish();
                break;
            case R.id.tv_thumbup_home_detail:
                   if(tv_thumbup_home_detail.getTag() != null) {
                       boolean b = (boolean) tv_thumbup_home_detail.getTag();
                       requestThumbup(!b);
                   }
                break;
            case R.id.tv_price_home_detail:
                try {
                    rewardPopuwind = new RewardPopuwind(this, this, homeItemBean.data.user.coin);
                    rewardPopuwind.showAtLocation(iv_header_home_item_detail, Gravity.CENTER, 0, 0);
                }catch (Exception e){

                }
                break;
            case R.id.tv_publish_home_detail:
                if(homeItemBean.data.recommendation.status == 1){//已发布，调到发布成功页
                    PublishSuccessGroomActivity.start(this,betId);
                }else{
                    EditGroomActivity.start(this,betId);
                }
                break;
            case R.id.tv_follow_home_detail:
                if(tv_follow_home_detail.getTag() == null)break;
                int b1 = (int) tv_follow_home_detail.getTag();
                if(b1 == 1){//取消关注
                    ToastUtils.toast("已关注");
//                    requestFollows("delete");
                }else{//关注、
                    requestFollows("add");
                }
                break;

            case R.id.tv_cancle_toast_price:
                if(toastPricePopuWindow != null){
                    toastPricePopuWindow.dismiss();
                }
                break;
            case R.id.tv_go_toast_price:
                if(toastType == 2){
                    if(toastPricePopuWindow != null){
                        toastPricePopuWindow.dismiss();
                    }
                    Intent intent1 = new Intent(this,CoinDetailActivity.class);
                    startActivity(intent1);
                    return;
                }
                break;
            case R.id.iv_right_title:

                if(homeItemBean == null || homeItemBean.data == null || homeItemBean.data.user == null)return;
                toastDotMore();
                break;

        }
    }



    /**
     * 提示分享
     */
    private ToastMsgPopuWindow priceToast;
    private void showPriceToast() {

        try {
            if (priceToast != null || isFinishing()) return;
            priceToast = new ToastMsgPopuWindow(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    priceToast.dismiss();
                }
            }, "分享出去,好友可以免费看！", "我知道了");

            priceToast.showAtLocation(tv_away_name_home_detail, Gravity.CENTER, 0, 0);
        }catch (Exception e){

        }

    }



    private void toastDotMore(){
        if (true || selectPicPopupWindow == null){
            selectPicPopupWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){

                        case R.id.btn_share_photo://分享
//                                    selectPicPopupWindow.dismiss();
//                                    showShare();
                            if(selectPicPopupWindow.rg_share_fg.isShown()){
                                selectPicPopupWindow.rg_share_fg.setVisibility(View.GONE);
                                selectPicPopupWindow.btn_share_photo.setText("分享");
                            }else {
                                selectPicPopupWindow.btn_share_photo.setText("取消分享");
                                selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                            }
                            break;

                        case R.id.btn_take_photo://收藏
                            selectPicPopupWindow.dismiss();
                            if (LoginUtils.isUnLogin()) {
                                LoginUtils.goLoginActivity(HomeItemDetailActivity.this, HomeNewFragment.ACTION);
                                return;
                            }
                            requestCollect();
                            break;
                        case R.id.btn_pick_photo://举报
                            selectPicPopupWindow.dismiss();
                            ReportActivity.start(HomeItemDetailActivity.this,homeItemBean.data.user.user_id,homeItemBean.data.user.nickname,homeItemBean.data.recommendation.create_time);

                            break;
                    }
                }
            }, new SharePopuwind.ShareTypeClickListener() {
                @Override
                public void onTypeClick(int type) {

                    HomeItemBean.DataBean data = homeItemBean.data;
                    String title = "";
                    if(data.recommendation != null){
                        title = data.recommendation.title;
                    }

                    String vs = URLDecoder.decode(data.match.hostTeamName)+"vs"+URLDecoder.decode(data.match.awayTeamName);
                    String url = "https://app.mayisports.com/#/recommendSheet/"+data.user.user_id+","+data.match.betId+"?share_userid="+ SPUtils.getString(HomeItemDetailActivity.this,Constant.USER_ID);

                    ShareUtils.shareMsgForGroom(type,url,title,data.user.nickname,vs,HomeItemDetailActivity.this);
                }
            });
        }
        if("1".equals(homeItemBean.data.recommendation.collection_status)){
            selectPicPopupWindow.btn_take_photo.setText("取消收藏");
        }else{
            selectPicPopupWindow.btn_take_photo.setText("收藏");
        }


        selectPicPopupWindow.btn_share_photo.setVisibility(View.GONE);
        selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
        selectPicPopupWindow.tv_share_title.setVisibility(View.VISIBLE);

        if(SPUtils.getString(this,Constant.USER_ID).equals(userId)){
            selectPicPopupWindow.btn_pick_photo.setVisibility(View.GONE);
        }else{
            selectPicPopupWindow.btn_pick_photo.setVisibility(View.VISIBLE);
        }

        selectPicPopupWindow.showAtLocation(tv_title_home_detail, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    /**
     * 收藏功能
     */
    private void requestCollect(){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","collection");
        /**
         * match:1 comment:2 recommendation:5 topic:6
         */
        params.put("subtype",5);
        params.put("id",homeItemBean.data.recommendation.id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
                if(reportSubmitBean.status.result == 1){
                    ToastUtils.toast("收藏成功");
                    homeItemBean.data.recommendation.collection_status = "1";
                }else if(reportSubmitBean.status.result == 0){
                    ToastUtils.toast("取消收藏");
                    homeItemBean.data.recommendation.collection_status = "0";
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    @Override
    public void finish() {
        if(homeItemBean != null && homeItemBean.data != null && homeItemBean.data.recommendation != null) {
            Intent intent = getIntent().putExtra("collection_status", homeItemBean.data.recommendation.collection_status + "");
            setResult(22,intent);
        }


        super.finish();
    }

    public void requestFollows(String type){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        params.put("type",type);
        params.put("user_id",userId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        tv_follow_home_detail.setTag(1);
                        tv_follow_home_detail.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                        tv_follow_home_detail.setTextColor(Color.parseColor("#e8e8e8"));
                        tv_follow_home_detail.setText("已关注");
                        NotificationsUtils.checkNotificationAndStartSetting(HomeItemDetailActivity.this,tv_follow_home_detail);
                    }else{
                        tv_follow_home_detail.setTag(0);
                        tv_follow_home_detail.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                        tv_follow_home_detail.setTextColor(Color.parseColor("#282828"));
                        tv_follow_home_detail.setText(" 关注 ");
                    }
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    ToastPricePopuWindow toastPricePopuWindow;
    private int toastType = 1;


    /**
     * 打赏金额回调
     * @param count
     */
    @Override
    public void onCoinCheck(int count) {
        if(rewardPopuwind != null){
            rewardPopuwind.dismiss();
        }
        if(SPUtils.getString(this,Constant.USER_ID).equals(userId)){
            ToastUtils.toast("不能自己打赏自己哦~");
            return;
        }

        if(count>Double.valueOf(homeItemBean.data.user.coin)){
            toastType = 2;
            toastPricePopuWindow = new ToastPricePopuWindow(this,this,"您的金币余额不足",toastType);
            toastPricePopuWindow.showAtLocation(iv_header_home_item_detail,Gravity.CENTER,0,0);
            return;
        }
         requestPrice(count);
    }


    /**
     * 请求打赏
     */
    private void requestPrice(int coin) {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","reward");
        params.put("user_id",userId+"");
        params.put("betId",betId+"");
        params.put("count",coin);//da'shang打赏金币数
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string,RewardBean.class);
                if(rewardBean != null){
                    if(rewardBean.status.errno == 0){
                        ToastUtils.toast("打赏成功");
                    }else{
                        ToastUtils.toast(rewardBean.status.errstr);
                    }
                }
                requestNetData();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    /**
     * 请求是否点赞
     * @param isThumbup
     */
    private void requestThumbup(boolean isThumbup) {
         String url = Constant.BASE_URL + "/php/api.php";
         HttpParams params = new HttpParams();
         params.put("action","user");
         params.put("type","praise");
         params.put("user_id",userId+"");
         params.put("betId",betId+"");
         RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
             @Override
             public void onSucces(String string) {
                 MatchThumbupBean matchThumbupBean = JsonParseUtils.parseJsonClass(string,MatchThumbupBean.class);
                 if(matchThumbupBean.data != null){
                     homeItemBean.data.user.praise_status = matchThumbupBean.data.praise_status;
                     homeItemBean.data.user.praise_count = matchThumbupBean.data.praise_count;
                     updateThumbup();
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

    @Override
    public void onRefreshOk(Object object) {
        this.homeItemBean = (HomeItemBean) object;
        updateView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        refreshHandler.removeCallbacksAndMessages(null);

    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.toastNoStates("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.toastNoStates("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.toastNoStates("分享取消");

    }
}
