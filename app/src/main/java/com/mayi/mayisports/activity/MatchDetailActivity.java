package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.support.test.espresso.AmbiguousViewMatcherException;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewFinder;
import android.support.test.espresso.base.ViewFinderImpl;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.adapter.MatchAnlyIitemSetData;
import com.mayisports.qca.bean.AnlyBean;
import com.mayisports.qca.bean.AnlySecondBean;
import com.mayisports.qca.bean.LiveSelectBean;
import com.mayisports.qca.bean.MatchDetailBean;
import com.mayisports.qca.bean.MatchInformationBean;
import com.mayisports.qca.bean.MatchOddsBean;
import com.mayisports.qca.bean.MathesInfoBean;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.StarStutasBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.utils.BindViewUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.RefreshSocreUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.LiveSelectPopuWindow;
import com.mayisports.qca.view.MyLinearLayout;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.ta.utdid2.android.utils.NetworkUtils;
import com.tencent.open.utils.Util;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 比赛详情界面
 */
public class MatchDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, RefreshSocreUtils.OnRefreshCallBack, MyLinearLayout.OnTouchCall {




    public static void start(Activity activity,String betId){
        Intent intent = new Intent(activity,MatchDetailActivity.class);
        intent.putExtra("betId",betId);
        activity.startActivity(intent);
    }

    public static void start(Activity activity,String betId,int type){
        Intent intent = new Intent(activity,MatchDetailActivity.class);
        intent.putExtra("betId",betId);
        intent.putExtra("type",type);
        activity.startActivity(intent);
    }



    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "match_detail_action";
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

    @Override
    public void onReload() {

    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra("position",getIntent().getIntExtra("position",-1));
        if(iv_right_title_match_detail.getTag() != null) {
            int tag = (int) iv_right_title_match_detail.getTag();
            intent.putExtra("collection_status", tag);
        }
        setResult(2,intent);
        super.finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        refreshHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onRefreshOk(Object object) {
        this.matchDetailBean = (MatchDetailBean) object;
        updateView();
    }

    private int topPre = -1;

    @Override
    public boolean onCall(int deY) {

        if(topPre == -1) {
           topPre = ll_top_title_match_detail.getBottom();
        }
        int i = topPre + deY;
        ViewGroup.LayoutParams layoutParams = ll_top_title_match_detail.getLayoutParams();
        int height = layoutParams.height;
        height += deY;
        if(height>(topPre - ll_icon.getHeight()) && height<topPre+1) {
            int paddingTop = ll_top_title_match_detail.getPaddingTop();
            ll_top_title_match_detail.setPadding(0,paddingTop += deY,0,0);
            layoutParams.height = height;
            ll_top_title_match_detail.setLayoutParams(layoutParams);
            return true;
        }

        return  false;
    }

    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();
//            initData();
//            ToastUtils.toast("回调刷新");
        }
    }



    private TextView tv_weather_match_detail;
    private TextView tv_title_match_detail;

    private TextView tv_groom_match_detail;
    private TextView tv_analyes_match_detail;
    private TextView tv_price_match_detail;
    private TextView tv_outs_match_detail;
    private TextView tv_qingbao_match_detail;
    private View iv_groom_match_detail;
    private View iv_analyes_match_detail;
    private View iv_price_match_detail;
    private View iv_outs_match_detail;
    private View iv_qingbao_match_detail;


   // private LinearLayout ll_groom_match_detail;


    private ImageView iv_left_title_match_detail;
    private ImageView iv_right_title_match_detail;
    private ImageView iv_right1_title_match_detail;

    private TextView tv_host_rank_match_detail;
    private TextView tv_away_rank_match_detail;
    private TextView tv_host_name_match_detail;
    private  TextView tv_away_name_match_detail;
    private ImageView iv_host_icon_match_detail;
    private ImageView iv_away_icon_match_detail;
    private TextView tv_center_top_match_detail;
    private TextView tv_cent_bottom_match_detail;

    private ListView lv_match_detail;

   // private WebView wv_match_detail;
    private LinearLayout ll_center_container;
  //  private LinearLayout ll_no_data;
    private FrameLayout fl_webview_container;

    private RelativeLayout rl_load_layout;

  //  private RelativeLayout load_webview;
    private TextView tv_live_btn;
    private LinearLayout ll_top_title_match_detail;
    private MyLinearLayout my_lv;


    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 2){//更新
                rl_load_layout = null;
                RefreshSocreUtils.getDataForMatchDetail(matchDetailBean,MatchDetailActivity.this);
                refreshHandler.sendEmptyMessageDelayed(2,1000*15);
            }

        }
    };


    private boolean isSend;


    //R.layout.activity_match_detail_test_one;
    @Override
    protected int setViewForContent() {
        return R.layout.activity_match_detail_new;
    }


    private LinearLayout ll_icon;

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;


    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);

        initReceiver();


        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        });



//        my_lv = findViewById(R.id.my_lv);
//        my_lv.setOnTouchCall(this);
//        ll_top_title_match_detail = findViewById(R.id.ll_top_title_match_detail);
        ll_icon = findViewById(R.id.ll_icon);
//        ll_bottom_match_detail = findViewById(R.id.ll_bottom_match_detail);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        //load_webview = findViewById(R.id.rl_load_webview_layout);

        tv_title_match_detail = findViewById(R.id.tv_title_match_detail);
        tv_weather_match_detail = findViewById(R.id.tv_weather_match_detail);

        fl_webview_container = findViewById(R.id.fl_webview_container);

//        View viewHeader = View.inflate(getBaseContext(),R.layout.header_match_detail_layout,null);
        //ll_no_data = findViewById(R.id.ll_no_data);
        ll_center_container = findViewById(R.id.ll_center_container);
        tv_live_btn = findViewById(R.id.tv_live_btn);
        tv_groom_match_detail =  findViewById(R.id.tv_groom_match_detail);
        tv_analyes_match_detail =  findViewById(R.id.tv_analyes_match_detail);
        tv_price_match_detail =  findViewById(R.id.tv_price_match_detail);
        tv_outs_match_detail =  findViewById(R.id.tv_outs_match_detail);
        tv_qingbao_match_detail = findViewById(R.id.tv_qingbao_match_detail);
        iv_groom_match_detail =  findViewById(R.id.iv_groom_match_detail);
        iv_analyes_match_detail =  findViewById(R.id.iv_analyes_match_detail);
        iv_price_match_detail =  findViewById(R.id.iv_price_match_detail);
        iv_outs_match_detail =  findViewById(R.id.iv_outs_match_detail);
        iv_qingbao_match_detail = findViewById(R.id.iv_qingbao_match_detail);
        //ll_groom_match_detail =  findViewById(R.id.ll_groom_match_detail);
        this.iv_left_title_match_detail = findViewById(R.id.iv_left_title_match_detail);
        tv_title = findViewById(R.id.tv_title);
        iv_right_title_match_detail = findViewById(R.id.iv_right_title_match_detail);
        iv_right1_title_match_detail = findViewById(R.id.iv_right1_title_match_detail);
        iv_host_icon_match_detail =  findViewById(R.id.iv_host_icon_match_detail);
        iv_away_icon_match_detail =  findViewById(R.id.iv_away_icon_match_detail);
        tv_center_top_match_detail =  findViewById(R.id.tv_center_top_match_detail);
        tv_cent_bottom_match_detail =  findViewById(R.id.tv_cent_bottom_match_detail);


        lv_match_detail = findViewById(R.id.lv_match_detail);
//        lv_match_detail.addHeaderView(viewHeader);
        lv_match_detail.setOnItemClickListener(this);

        tv_host_rank_match_detail =  findViewById(R.id.tv_host_rank_match_detail);
        tv_away_rank_match_detail =  findViewById(R.id.tv_away_rank_match_detail);
        tv_host_name_match_detail =  findViewById(R.id.tv_host_name_match_detail);
        tv_away_name_match_detail =  findViewById(R.id.tv_away_name_match_detail);
        sv = findViewById(R.id.sv);

        tv_groom_match_detail.setOnClickListener(this);
        tv_analyes_match_detail.setOnClickListener(this);
        tv_price_match_detail.setOnClickListener(this);
        tv_outs_match_detail.setOnClickListener(this);
        tv_qingbao_match_detail.setOnClickListener(this);
        iv_left_title_match_detail.setOnClickListener(this);
        iv_right_title_match_detail.setOnClickListener(this);
        iv_right1_title_match_detail.setOnClickListener(this);

        tv_live_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastLive();
            }
        });

        initWebView();

    }

    private LiveSelectBean liveSelectBean;


    /**
     * 查看直播
     */
    private void toastLive() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match");
        params.put("type","tvlink");
        params.put("betId",betId+"");
        rl_load_layout = findViewById(R.id.rl_load_layout);
        tv_live_btn.setEnabled(false);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                 liveSelectBean = JsonParseUtils.parseJsonClass(string,LiveSelectBean.class);
                 if(liveSelectBean.data.tvlink != null){
                     List<String> datas = new ArrayList<>();
                     for(int i = 0;i<liveSelectBean.data.tvlink.size();i++){
                         if(i>5)break;
                         LiveSelectBean.DataBean.TvlinkBean tvlinkBean = liveSelectBean.data.tvlink.get(i);
                           if(TextUtils.isEmpty(tvlinkBean.link)){
                             tvlinkBean.link = "0";
                         }
                         String str = tvlinkBean.title+"-"+tvlinkBean.link;
                         datas.add(str);
                     }

                     try {
                         final LiveSelectPopuWindow liveSelectPopuWindow = new LiveSelectPopuWindow(MatchDetailActivity.this, datas);
                         liveSelectPopuWindow.showAtLocation(tv_title_match_detail, Gravity.CENTER, 0, 0);
                     }catch (Exception e){

                     }


                 }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
                tv_live_btn.setEnabled(true);
            }
        });
    }


    private boolean isFinish;
    private void initWebView() {
    }




    /**
     * 赔率
     * @param url  /oddsDetail?oddsType=euro&betId=1424232&companyId=21
     */
    @JavascriptInterface
    public void openOddDetailView(String url){
        WebViewActivtiy.start(this,Constant.BASE_URL + "#"+url,"赔率详情");

    }

    /**
     * 积分
     * @param url  /pointRanklist?leagueId=242&leagueName=伊朗超&hostTeamId=2880&awayTeamId=16810
     */
    @JavascriptInterface
    public void openNewView(String url){
      WebViewActivtiy.start(this,Constant.BASE_URL +  "#"+url,"积分榜");
    }
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
//                wv_match_detail.loadUrl("javascript:App.resize(document.body.getBoundingClientRect().height)");
                handler.sendEmptyMessageDelayed(1,3000);
             }
        }
    };

    /**
     * 添加cookie
     * @param context
     * @param url
     * @param cookie
     */
    public void synCookies(Context context, String url, String cookie) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setCookie(url, cookie);//cookies是在HttpClient中获得的cookie
        CookieSyncManager.getInstance().sync();
    }


    private String betId;
    private int type_home;//2 首页情报跳转
    @Override
    protected void initDatas() {
        super.initDatas();
        betId =  getIntent().getStringExtra("betId");
        type_home = getIntent().getIntExtra("type",0);
        requestNetDatas();

    }

    private MatchDetailBean matchDetailBean;
    private void requestNetDatas() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match_recommendation1");
        params.put("betId",betId+"");
        params.put("type",1);
        params.put("start",0);
        params.put("from",1);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                  matchDetailBean = JsonParseUtils.parseJsonClass(string,MatchDetailBean.class);
                  setDatas();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);

                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                }else{
                    if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * 设置list
     */
    private ScrollView sv;
    private MyAdapter myAdapter = new MyAdapter();
    private void setDatas() {
//           myAdapter.notifyDataSetChanged();
           updateView();
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");

    private int clickPosition;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        MatchDetailBean.DataBean.ListBean bean = matchDetailBean.data.list.get(i);
        String price = bean.recommendation.price+"";
        String nickname = bean.user.nickname;
        if(LoginUtils.isUnLogin()){
           LoginUtils.goLoginActivity(this,ACTION);
            return;
        }


        if(bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0)|| "COMPLETE".equals(matchDetailBean.data.match.status)|| SPUtils.getString(this,Constant.USER_ID).equals(bean.user.user_id)){//已购买 //或者免费
//            Intent intent = new Intent(this,PersonalDetailActivity.class);
//            String user_id = matchDetailBean.data.list.get(i).user.user_id;
//            intent.putExtra("id",user_id);

            goDetail(bean);
//            startActivity(intent);
        }else{
              clickPosition = i;
              requestToast(bean.user.user_id,matchDetailBean.data.match.betId);
        }

    }

    /**
     * 请求是否购买
     */
    private int type;//1确定，2充值
    private ToastPricePopuWindow toastPricePopuWindow;
    private void requestToast(String userId,String betId) {
        rl_load_layout = null;
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","recommendation_Detail");
        params.put("type","req");
        params.put("betId",betId);
        params.put("user_id",userId);
        params.put("from",3);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                ToastPriceBean toastPriceBean = JsonParseUtils.parseJsonClass(string,ToastPriceBean.class);
                String title = "";
                title = toastPriceBean.text;
                if("req".equals(toastPriceBean.type)){
                    type = 1;
                }else if("nomoney".equals(toastPriceBean.type)){
                    type = 2;
                }
                toastPricePopuWindow =   new ToastPricePopuWindow(MatchDetailActivity.this,MatchDetailActivity.this,title,type);
                toastPricePopuWindow.showAtLocation(lv_match_detail, Gravity.CENTER,0,0);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });
    }



    private boolean isEmpty;
    class MyAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            if(matchDetailBean != null && matchDetailBean.data!= null && matchDetailBean.data.list!= null){
                if(matchDetailBean.data.list.size()>0) {
                 //   ll_no_data.setVisibility(View.GONE);
                    isEmpty = false;
                }else{
                 //   ll_no_data.setVisibility(View.VISIBLE);
                    isEmpty = true;
                    return 1;
                }
                return matchDetailBean.data.list.size();
            }
            isEmpty = true;
           // ll_no_data.setVisibility(View.VISIBLE);
            return 1;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(isEmpty){
                view = View.inflate(QCaApplication.getContext(),R.layout.empty,null);
                view.setTag(null);
                return view;
            }
            Holder holder;
            if(view == null || view.getTag() == null){
                holder = new Holder();
                view = View.inflate(getBaseContext(),R.layout.item_match_detail,null);
                holder.iv_head_match_item = view.findViewById(R.id.iv_head_match_item);
                holder.iv_vip_header = view.findViewById(R.id.iv_vip_header);

                holder.tv_name_match_item = view.findViewById(R.id.tv_name_match_item);

                holder.ll_tag_container_match_detail = view.findViewById(R.id.ll_tag_container_match_detail);
                holder.ll_top_click_match_detail = view.findViewById(R.id.ll_top_click_match_detail);

                holder.tv_time_match_item = view.findViewById(R.id.tv_time_match_item);
                holder.tv_team_name_match_item = view.findViewById(R.id.tv_team_name_match_item);
                holder.tv_match_name_match_item = view.findViewById(R.id.tv_match_name_match_item);
                holder.tv_type_match_item = view.findViewById(R.id.tv_type_match_item);
                holder.tv_price_match_item = view.findViewById(R.id.tv_price_match_item);
                holder.tv_ritht_title_match_item = view.findViewById(R.id.tv_ritht_title_match_item);
                holder.tv_rigth_value_match_item = view.findViewById(R.id.tv_rigth_value_match_item);
                holder.tv_right_item = view.findViewById(R.id.tv_right_item);

                holder.ll_price = view.findViewById(R.id.ll_price);
                holder.iv_result_personal = view.findViewById(R.id.iv_result_personal);
                holder.tv_buy = view.findViewById(R.id.tv_buy);


                view.setTag(holder);
            }else{
                holder = (Holder) view.getTag();
            }

            final MatchDetailBean.DataBean.ListBean listBean = matchDetailBean.data.list.get(i);

            holder.ll_top_click_match_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MatchDetailActivity.this, PersonalDetailActivity.class);
                    intent.putExtra("id",listBean.user.user_id+"");
                    startActivity(intent);
                }
            });

            PictureUtils.showCircle(listBean.user.headurl,holder.iv_head_match_item);
            holder.tv_name_match_item.setText(listBean.user.nickname+"");

            if(!TextUtils.isEmpty(listBean.user.verify_type)&&Integer.valueOf(listBean.user.verify_type)>0){
               holder.iv_vip_header.setVisibility(View.VISIBLE);
            }else{
                holder.iv_vip_header.setVisibility(View.GONE);
            }




            holder.ll_price.setVisibility(View.VISIBLE);
            holder.tv_right_item.setVisibility(View.GONE);
            holder.iv_result_personal.setVisibility(View.GONE);




            ViewStatusUtils.addTags(holder.ll_tag_container_match_detail,listBean.user.tag,listBean.user.tag1);

            Long aLong = Long.valueOf(listBean.recommendation.create_time + "000");
            String createTime = Utils.getCreateTime(aLong);
            holder.tv_time_match_item.setText(createTime);

            holder.tv_team_name_match_item.setText(matchDetailBean.data.match.hostTeamName+"\t\tvs\t\t"+matchDetailBean.data.match.awayTeamName);
//            String format = BindViewUtils.simpleDateFormat.format(Long.valueOf(matchDetailBean.data.match.time+"000"));
            String matchStartTime = Utils.getMatchStartTime(Long.valueOf(matchDetailBean.data.match.time + "000"));

            String re = "";
            if("1".equals(listBean.recommendation.return_if_wrong)){
                re = " · "+"不中退款";
            }


            holder.tv_match_name_match_item.setText(matchDetailBean.data.match.leagueName+" · "+matchStartTime+re);


//            listBean.recommendation.buy = 1;

            if(listBean.recommendation.buy == 1){
                holder.tv_buy.setVisibility(View.VISIBLE);
                holder.ll_price.setVisibility(View.GONE);
                holder.tv_right_item.setVisibility(View.VISIBLE);
            }else{
                holder.tv_buy.setVisibility(View.GONE);
                holder.ll_price.setVisibility(View.VISIBLE);
                holder.tv_right_item.setVisibility(View.GONE);
            }


            holder.tv_type_match_item.setText(listBean.recommendation.type+"");
            if(listBean.recommendation.price == 0){
                holder.tv_price_match_item.setText("免费");
                holder.tv_buy.setVisibility(View.GONE);
            }else {
                holder.tv_price_match_item.setText(listBean.recommendation.price + "金币");
            }


            holder.tv_ritht_title_match_item.setText(listBean.statics.value);
            holder.tv_rigth_value_match_item.setText(listBean.statics.title);



            switch (matchDetailBean.data.match.status){
                case "COMPLETE":
                    holder.tv_buy.setVisibility(View.GONE);
                    holder.ll_price.setVisibility(View.GONE);
                    holder.iv_result_personal.setVisibility(View.VISIBLE);
                    ViewStatusUtils.parseStatusForImg(listBean.recommendation.revenueStr,holder.iv_result_personal);
                    break;
            }


            return view;
        }
    }

    class Holder{
        public LinearLayout ll_top_click_match_detail;
        public ImageView iv_head_match_item;
        public ImageView iv_vip_header;
        public TextView tv_name_match_item;
        public LinearLayout ll_tag_container_match_detail;
        public TextView tv_time_match_item;
        public TextView tv_team_name_match_item;
        public TextView tv_match_name_match_item;
        public TextView tv_type_match_item;
        public TextView tv_price_match_item;
        public TextView tv_ritht_title_match_item;
        public TextView tv_rigth_value_match_item;
        public TextView tv_right_item;
        public LinearLayout ll_price;
        public ImageView iv_result_personal;
        public TextView tv_buy;


    }

    MatchDetailBean.DataBean.MatchBean matchlistBean;
    private boolean isFrist = true;
    private void updateView() {


        if(matchDetailBean == null || matchDetailBean.data == null || matchDetailBean.data.match == null)return;
         matchlistBean = matchDetailBean.data.match;

//        matchlistBean = Constant.matchlistBean;
        if(matchlistBean == null){
            ToastUtils.toast("数据异常");
            return;
        }

        String leagueName = URLDecoder.decode(matchDetailBean.data.match.leagueName);
        String round = matchDetailBean.data.match.round;
        String roundName = "";
        try{
            Integer integer = Integer.valueOf(round);
            roundName = "第"+integer+"轮";
        }catch (Exception e){
            roundName = round;
        }
        tv_title_match_detail.setText(leagueName+"-"+roundName);

        String weather = "";
        if(!TextUtils.isEmpty(matchDetailBean.data.match.weather)){
            tv_weather_match_detail.setVisibility(View.VISIBLE);
            weather = matchDetailBean.data.match.weather;
        }

        if(!TextUtils.isEmpty(matchDetailBean.data.match.temperature)){
            tv_weather_match_detail.setVisibility(View.VISIBLE);
            weather +=(" "+matchDetailBean.data.match.temperature);
        }
        if(!TextUtils.isEmpty(weather)){
            tv_title_match_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
            tv_weather_match_detail.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            tv_weather_match_detail.setText(weather);
        }






        if(matchlistBean.collection_status == 0){//未关注
            iv_right_title_match_detail.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
            iv_right_title_match_detail.setTag(0);
        }else{
            iv_right_title_match_detail.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
            iv_right_title_match_detail.setTag(1);
        }

        if(!TextUtils.isEmpty(matchlistBean.hostRank)&&!"0".equals(matchlistBean.hostRank)){
            tv_host_rank_match_detail.setText(matchlistBean.hostRank);
        }else{
            tv_host_rank_match_detail.setVisibility(View.INVISIBLE);
        }

        if(!TextUtils.isEmpty(matchlistBean.awayRank)&&!"0".equals(matchlistBean.awayRank)){
            tv_away_rank_match_detail.setText(matchlistBean.awayRank);
        }else{
            tv_away_rank_match_detail.setVisibility(View.INVISIBLE);
        }


        tv_host_name_match_detail.setText(URLDecoder.decode(matchlistBean.hostTeamName));
        tv_away_name_match_detail.setText(URLDecoder.decode(matchlistBean.awayTeamName));

        if(matchlistBean.logoH != 0){
            String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ matchlistBean.logoH + ".png";
            PictureUtils.show(hostUrl,iv_host_icon_match_detail);
        }

        if(matchlistBean.logoG != 0){
            String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ matchlistBean.logoG + ".png";
            PictureUtils.show(hostUrl,iv_away_icon_match_detail);
        }

        iv_right1_title_match_detail.setVisibility(View.GONE);
        if(type_home == 2){

            switch (matchlistBean.status) {
                case "NO_START":
                case "DELAY":
                    String format = BindViewUtils.simpleDateFormat.format(Long.valueOf(matchlistBean.timezoneoffset + "000"));
//                    String matchStartTime = Utils.getMatchStartTime(Long.valueOf(matchlistBean.timezoneoffset + "000"));
                    tv_center_top_match_detail.setText(format);
                    tv_cent_bottom_match_detail.setText(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(matchlistBean.timezoneoffset + "000")));
                    iv_right1_title_match_detail.setVisibility(View.VISIBLE);

                    if(!isSend) {
                        isSend = true;
                        refreshHandler.removeCallbacksAndMessages(null);
                        refreshHandler.sendEmptyMessageDelayed(2, 500);
                    }
                    break;
                case "FIRST_HALF":
                case "SECOND_HALF":

                    setProcess();

                    break;
                case "HALF_TIME":

                    setProcess();
                    break;

                case "COMPLETE":
                    setComplete();
                    break;
            }

            if(isFrist) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isFrist = false;

                        onClick(tv_qingbao_match_detail);

                    }
                });

            }
        }else if(type_home == 3){//分析界面


            switch (matchlistBean.status) {
                case "NO_START":
                case "DELAY":
                    String format = BindViewUtils.simpleDateFormat.format(Long.valueOf(matchlistBean.timezoneoffset + "000"));
//                    String matchStartTime = Utils.getMatchStartTime(Long.valueOf(matchlistBean.timezoneoffset + "000"));
                    tv_center_top_match_detail.setText(format);
                    tv_cent_bottom_match_detail.setText(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(matchlistBean.timezoneoffset + "000")));
                    iv_right1_title_match_detail.setVisibility(View.VISIBLE);
                    break;
                case "FIRST_HALF":
                case "SECOND_HALF":

                    setProcess();

                    break;
                case "HALF_TIME":

                    setProcess();
                    break;

                case "COMPLETE":
                    setComplete();
                    break;
            }

            if(isFrist) {
                isFrist = false;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onClick(tv_analyes_match_detail);
                        myAdapter.notifyDataSetChanged();
                    }
                });

            }


        }else if(type_home == 4){//赛况

            switch (matchlistBean.status) {
                case "NO_START":
                case "DELAY":
                    String format = BindViewUtils.simpleDateFormat.format(Long.valueOf(matchlistBean.timezoneoffset + "000"));
//                    String matchStartTime = Utils.getMatchStartTime(Long.valueOf(matchlistBean.timezoneoffset + "000"));
                    tv_center_top_match_detail.setText(format);
                    tv_cent_bottom_match_detail.setText(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(matchlistBean.timezoneoffset + "000")));
                    iv_right1_title_match_detail.setVisibility(View.VISIBLE);
                    if(!isSend) {
                        isSend = true;
                        refreshHandler.removeCallbacksAndMessages(null);
                        refreshHandler.sendEmptyMessageDelayed(2, 500);
                    }
                    break;
                case "FIRST_HALF":
                case "SECOND_HALF":

                    setProcess();

                    break;
                case "HALF_TIME":

                    setProcess();
                    break;

                case "COMPLETE":
                    setComplete();
                    break;
            }

            if(isFrist) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isFrist = false;
                        onClick(tv_outs_match_detail);

                    }
                });

            }
        }else  {
            switch (matchlistBean.status) {
                case "NO_START":
                case "DELAY":
                    String format = BindViewUtils.simpleDateFormat.format(Long.valueOf(matchlistBean.timezoneoffset + "000"));
                    tv_center_top_match_detail.setText(format);
                    tv_cent_bottom_match_detail.setText(new SimpleDateFormat("yyyy-MM-dd").format(Long.valueOf(matchlistBean.timezoneoffset + "000")));
                    iv_right1_title_match_detail.setVisibility(View.VISIBLE);
                    if (isFrist) {
                        isFrist = false;
//                        onClick(tv_groom_match_detail);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onClick(tv_analyes_match_detail);
                                myAdapter.notifyDataSetChanged();
                            }
                        });

                    }

                    if(!isSend) {
                        isSend = true;
                        refreshHandler.removeCallbacksAndMessages(null);
                        refreshHandler.sendEmptyMessageDelayed(2, 500);
                    }
                    break;
                case "FIRST_HALF":
                case "SECOND_HALF":

                    setProcess();

                    break;
                case "HALF_TIME":

                    setProcess();
                    break;

                case "COMPLETE":
                    setComplete();


                    if (isFrist) {
                        isFrist = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                onClick(tv_outs_match_detail);
                                myAdapter.notifyDataSetChanged();
                            }
                        });

                    }

                    break;
            }
        }
        if(!"0".equals(matchlistBean.tvlink)&&!"COMPLETE".equals(matchlistBean.status)){
            tv_live_btn.setVisibility(View.VISIBLE);
        }else{
            tv_live_btn.setVisibility(View.GONE);
        }

    }


    /**
     * 比赛进行中
     */
    private void setProcess() {

        if(!isSend) {
            isSend = true;
            refreshHandler.removeCallbacksAndMessages(null);
            refreshHandler.sendEmptyMessageDelayed(2, 500);
        }

        if(isFrist && type_home != 2) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isFrist = false;
                    onClick(tv_outs_match_detail);
                }
            });

        }

        ll_center_container.removeAllViews();
        ViewGroup dayView = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
        TextView tv = (TextView) dayView.getChildAt(0);
        Long timeLong = Long.valueOf(matchlistBean.timezoneoffset + "000");
        tv.setText(Utils.simpleDateFormatYYMMDD.format(timeLong));
//        String minStart = Utils.getMinStart(Long.valueOf(matchDetailBean.data.match.timezoneoffset + "000"));
//        tv.setText(minStart);
        tv.setTextColor(Color.parseColor("#3EA53B"));


        if(matchlistBean.starttime != 0){
            String minStartNew = Utils.getMinStart(Long.valueOf(matchlistBean.starttime+ "000"));
            if("SECOND_HALF".equals(matchlistBean.status)) {
                minStartNew = Integer.valueOf(minStartNew)+45 + "";
            }
            tv.setText(minStartNew);
        }else {
            String minStartNew = Utils.getMinStart(Long.valueOf(matchlistBean.timezoneoffset + "000"));
            tv.setText(minStartNew);
        }



        ll_center_container.addView(dayView);

        ViewGroup scoreView = (ViewGroup) View.inflate(getBaseContext(),R.layout.score_match_detail_layout,null);
        TextView scoreTV = (TextView) scoreView.getChildAt(0);
        scoreTV.setTextColor(Color.parseColor("#3EA53B"));
        scoreTV.setText(matchlistBean.hostScore+" : "+matchlistBean.awayScore);




        if("HALF_TIME".equals(matchlistBean.status)){
            tv.setText("中场");
            tv.setVisibility(View.GONE);
            scoreTV.setTextColor(Color.parseColor("#62AFEF"));
        }else {
            TextView tv_time_pos = dayView.findViewById(R.id.tv_time_pos);
            tv_time_pos.setVisibility(View.VISIBLE);
            tv_time_pos.startAnimation(BindViewUtils.alphaAnimation);
        }


        ll_center_container.addView(scoreView);

        ViewGroup smallScoreView = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
        TextView smallTV = (TextView) smallScoreView.getChildAt(0);
        if("HALF_TIME".equals(matchlistBean.status)){
            smallTV.setText("中场");
        }else{
            smallTV.setText(matchlistBean.hostHalfScore+" : "+matchlistBean.awayHalfScore);
        }


        ll_center_container.addView(smallScoreView);
    }


    /**
     * 已结束
     */
    private void setComplete() {
        ll_center_container.removeAllViews();
        ViewGroup dayView = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
        TextView tv = (TextView) dayView.getChildAt(0);
        Long timeLong = Long.valueOf(matchlistBean.timezoneoffset + "000");
        tv.setText(Utils.simpleDateFormatYYMMDD.format(timeLong));
        ll_center_container.addView(dayView);

        ViewGroup scoreView = (ViewGroup) View.inflate(getBaseContext(),R.layout.score_match_detail_layout,null);
        TextView scoreTV = (TextView) scoreView.getChildAt(0);
        scoreTV.setText(matchlistBean.hostScore+" : "+matchlistBean.awayScore);
        ll_center_container.addView(scoreView);

        ViewGroup smallScoreView = (ViewGroup) View.inflate(getBaseContext(),R.layout.date_match_detial_layout,null);
        TextView smallTV = (TextView) smallScoreView.getChildAt(0);
        smallTV.setText(matchlistBean.hostHalfScore+" : "+matchlistBean.awayHalfScore);
        ll_center_container.addView(smallScoreView);

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){

            case R.id.tv_groom_match_detail:
                selectOne(0);
                 lv_match_detail.setAdapter(myAdapter);
//                lv_match_detail.removeHeaderView(wv_match_detail);
                break;
            case R.id.tv_analyes_match_detail:
                selectOne(1);
                requestAnly();

                break;
            case R.id.tv_price_match_detail:
                selectOne(2);
                requestOdds(0);
                break;
            case R.id.tv_outs_match_detail://   ///matchDetail/betId?navTabType=even
                selectOne(3);
                requestMathes();
                break;
            case R.id.tv_qingbao_match_detail://情报   #/matchDetail/1395253?navTabType=info
                selectOne(4);
                requestInfromaction();
                break;
            case R.id.iv_left_title_match_detail:
                finish();
                break;
            case R.id.iv_right_title_match_detail:
                delStar();
                break;
            case R.id.iv_right1_title_match_detail://#/editRecommend/1395961,1

                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this,ACTION);
                    return;
                }

                /**
                 * 比赛详情页点击右上角发布推荐,  如果 data.status === 1跳转到 '/successPage/recommend,' + betId,
                 * 如果 data.status === 0 跳转到 '/editRecommend/' + betId + ',4'
                 */
                if(matchDetailBean == null||matchDetailBean.data == null)break;
                if(matchDetailBean.data.status == 1) {
//                    WebViewActivtiy.start(this, Constant.BASE_URL + "#/successPage/recommend," + betId, "编辑推荐");
                    PublishSuccessGroomActivity.start(this,betId);
                }else{
//                    WebViewActivtiy.start(this, Constant.BASE_URL + "#/editRecommend/" + betId + ",4", "编辑推荐");
                    EditGroomActivity.start(this,betId);
                }
                break;
            case R.id.tv_cancle_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                break;
            case R.id.tv_go_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                if(type == 1){//确定
                    MatchDetailBean.DataBean.ListBean listBean = matchDetailBean.data.list.get(clickPosition);
                    listBean.recommendation.buy = 1;
                    myAdapter.notifyDataSetChanged();
                    goDetail(listBean);
                }else{//充值

                    Intent intent = new Intent(this, CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }


    /**
     * 请求分析信息
     *
     * https://app.mayisports.com/php/api.php?action=matchDetail&betId=1499019
     */
    private AnlyBean anlyBean;
    private void requestAnly() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchDetail");
        params.put("betId",betId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
                anlyBean = JsonParseUtils.parseJsonClass(string,AnlyBean.class);
                if(isNull()){
                    requestAnlySecond();
                }else{
                    lv_match_detail.setAdapter(new AnlyAdapter());
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    //https://app.mayisports.com/php/api.php?action=match&type=history_match&betId=1517427
    private void requestAnlySecond() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match");
        params.put("type","history_match");
        params.put("betId",betId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
               AnlySecondBean anlyBean1 = JsonParseUtils.parseJsonClass(string,AnlySecondBean.class);

               if(anlyBean1 != null && anlyBean1.matchList != null ){

                   AnlySecondBean.MatchListBean matchList = anlyBean1.matchList;

                   if(anlyBean.data.matchList == null)anlyBean.data.matchList = new AnlyBean.DataBean.MatchListBean();


                   if(matchList.away != null){


                       anlyBean.data.matchList.away = new ArrayList<>();//元数据，被填充


                       for(int i = 0;i<matchList.away.size();i++){
                           AnlyBean.DataBean.MatchListBean.AwayBean bean = new AnlyBean.DataBean.MatchListBean.AwayBean();

                           List<String> strings = matchList.away.get(i);


                           bean.match_time = strings.get(1);
                           bean.timezoneoffset = strings.get(1);

                          bean.leagueId = strings.get(2);

                           bean.leagueName = strings.get(3);

                           bean.hostTeamId = strings.get(4);
                           bean.awayTeamId = strings.get(5);

                           bean.hostTeamName = strings.get(6);
                           bean.awayTeamName = strings.get(7);

                           bean.hostScore = Integer.valueOf(strings.get(8));
                           bean.awayScore = Integer.valueOf(strings.get(9));

                           String[] split = strings.get(10).split("-");

                           bean.hostHalfScore = Integer.valueOf(split[0]);
                           bean.awayHalfScore = Integer.valueOf(split[1]);

                           bean.betId = strings.get(11);

                           anlyBean.data.matchList.away.add(bean);

                       }


                   }


                   if(matchList.host != null){
                       anlyBean.data.matchList.host = new ArrayList<>();//元数据，被填充


                       for(int i = 0;i<matchList.host.size();i++){
                           AnlyBean.DataBean.MatchListBean.HostBean bean = new AnlyBean.DataBean.MatchListBean.HostBean();

                           List<String> strings = matchList.host.get(i);


                           bean.match_time = strings.get(1);
                           bean.timezoneoffset = strings.get(1);

                           bean.leagueId = strings.get(2);

                           bean.leagueName = strings.get(3);

                           bean.hostTeamId = strings.get(4);
                           bean.awayTeamId = strings.get(5);

                           bean.hostTeamName = strings.get(6);
                           bean.awayTeamName = strings.get(7);

                           bean.hostScore = Integer.valueOf(strings.get(8));
                           bean.awayScore = Integer.valueOf(strings.get(9));

                           String[] split = strings.get(10).split("-");

                           bean.hostHalfScore = Integer.valueOf(split[0]);
                           bean.awayHalfScore = Integer.valueOf(split[1]);

                           bean.betId = strings.get(11);





                           anlyBean.data.matchList.host.add(bean);


                       }
                   }


                   if(matchList.history != null){
                       anlyBean.data.matchList.history = new ArrayList<>();//元数据，被填充


                       for(int i = 0;i<matchList.history.size();i++){
                           AnlyBean.DataBean.MatchListBean.HistoryBean bean = new AnlyBean.DataBean.MatchListBean.HistoryBean();

                           List<String> strings = matchList.history.get(i);


                           bean.match_time = strings.get(1);
                           bean.timezoneoffset = strings.get(1);



                           bean.leagueName = strings.get(3);

                           bean.hostTeamId = strings.get(4);
                           bean.awayTeamId = strings.get(5);

                           bean.hostTeamName = strings.get(6);
                           bean.awayTeamName = strings.get(7);

                           bean.hostScore = Integer.valueOf(strings.get(8));
                           bean.awayScore = Integer.valueOf(strings.get(9));

                           String[] split = strings.get(10).split("-");

                           try {
                               bean.hostHalfScore = Integer.valueOf(split[0]);
                               bean.awayHalfScore = Integer.valueOf(split[1]);
                           }catch (Exception e){

                           }
                           bean.betId = strings.get(11);





                           anlyBean.data.matchList.history.add(bean);


                       }
                   }

               }

                lv_match_detail.setAdapter(new AnlyAdapter());
                //ll_groom_match_detail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    class AnlyAdapter extends  BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(isNull()){
                convertView = View.inflate(QCaApplication.getContext(),R.layout.empty,null);
                //       ll_no_data.setVisibility(View.VISIBLE);
                return convertView;
            }

            MatchAnlyIitemSetData matchAnlyIitemSetData = new MatchAnlyIitemSetData(convertView,anlyBean);

            convertView = matchAnlyIitemSetData.getConvertView();



            return convertView;
        }
    }

    private boolean isNull(){
        if(anlyBean == null || anlyBean.data == null )return true;
        AnlyBean.DataBean data = anlyBean.data;
        if(data.future3 != null &&(data.future3.home != null || data.future3.guest != null))return false;
        if(data.group_ranking != null)return false;
        if(data.matchList != null && (data.matchList.host != null || data.matchList.away != null) && (data.matchList.host.size() != 0 || data.matchList.away.size() != 0))return false;
        if(data.ranking != null && (data.ranking.home != null || data.ranking.guest != null))return false;


        return true;
    }


    /**
     * 请求赔率信息
     *
     * https://app.mayisports.com/php/api.php?action=oddsdetail&type=euro&betId=1509873
     *
     * type
     * 0 欧盘   1 亚盘  2 大小球
     */
    private MatchOddsBean matchOddsBean;
    private void requestOdds(final int type) {
        String typeStr = "";

        switch (type){
            case 0:
                typeStr = "euro";
                break;
            case 1:
                typeStr = "asia";
                break;
            case 2:
                typeStr = "score";
                break;
        }

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","oddsdetail");
        params.put("type",typeStr);
        params.put("betId",betId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
                matchOddsBean = JsonParseUtils.parseJsonClass(string,MatchOddsBean.class);

                if(matchOddsBean == null)matchOddsBean = new MatchOddsBean();
                lv_match_detail.setAdapter(new OddsAdapter(type,matchOddsBean.data));
                //ll_groom_match_detail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                }else{
                    if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    class OddsAdapter extends  BaseAdapter{

        private MatchOddsBean.DataBean dataBean;
        private int type;

        private  List<MatchOddsBean.DataBean.OddsBean.EuropeOddsBean> europeOdds;

        private  List<MatchOddsBean.DataBean.OddsBean.AsiaBean> asia;

        private  List<MatchOddsBean.DataBean.OddsBean.ScoreBean> score;

        private boolean isNull;
        public OddsAdapter(int type,MatchOddsBean.DataBean dataBean){
            this.type = type;
            this.dataBean = dataBean;

            if(dataBean == null){
                isNull = true;
                return;
            }

            switch (type){
                case 0:
                    europeOdds = dataBean.odds.europeOdds;
                    isNull = (europeOdds == null);
                    break;
                case 1:
                    asia = dataBean.odds.asia;
                    isNull = (asia == null);
                    break;
                case 2:
                    score = dataBean.odds.score;
                    isNull = (score == null);
                    break;
            }



        }

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            convertView = View.inflate(QCaApplication.getContext(),R.layout.odds_item,null);
            LinearLayout  ll_europe_edit_groom = convertView.findViewById(R.id.ll_europe_edit_groom);
            tv_left_europe_edit_groom = convertView.findViewById(R.id.tv_left_europe_edit_groom);
            tv_m_europe_edit_groom = convertView.findViewById(R.id.tv_m_europe_edit_groom);
            tv_right_europe_edit_groom = convertView.findViewById(R.id.tv_right_europe_edit_groom);
            MyClick myClick = new MyClick();
            tv_left_europe_edit_groom.setOnClickListener(myClick);
            tv_m_europe_edit_groom.setOnClickListener(myClick);
            tv_right_europe_edit_groom.setOnClickListener(myClick);


            TextView tv_type_name = convertView.findViewById(R.id.tv_type_name);

            switch (type){

                case 0:
                    ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_left);
                    tv_type_name.setText("平局");
                    break;
                case 1:
                    ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_m);
                    tv_type_name.setText("盘口");
                    break;
                case 2:
                    ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_righ);
                    tv_type_name.setText("盘口");
                    break;

            }




            FrameLayout fll_empty = convertView.findViewById(R.id.fl_empty);
            LinearLayout ll_item_title = convertView.findViewById(R.id.ll_item_title);


            LinearLayout ll_item_container = convertView.findViewById(R.id.ll_item_container);


            if(dataBean == null || dataBean.odds == null || isNull){
                fll_empty.setVisibility(View.VISIBLE);
                ll_item_container.setVisibility(View.GONE);
                ll_item_title.setVisibility(View.GONE);
                return convertView;
            }



            switch (type){
                case 0:

                    for(int i = 0;i<europeOdds.size();i++){
                        View item = View.inflate(QCaApplication.getContext(),R.layout.odds_child_item,null);


                        TextView tv_name = item.findViewById(R.id.tv_name);
                        TextView tv_top1 = item.findViewById(R.id.tv_top1);
                        TextView tv_bottom1 = item.findViewById(R.id.tv_bottom1);
                        TextView tv_top2 = item.findViewById(R.id.tv_top2);
                        TextView tv_bottom2 = item.findViewById(R.id.tv_bottom2);
                        TextView tv_top3 = item.findViewById(R.id.tv_top3);
                        TextView tv_bottom3 = item.findViewById(R.id.tv_bottom3);

                        MatchOddsBean.DataBean.OddsBean.EuropeOddsBean europeOddsBean = europeOdds.get(i);



                        tv_name.setText(europeOddsBean.title);

                        Utils.setOddsEur(tv_top1,tv_bottom1,europeOddsBean.firstWinOdds,europeOddsBean.winOdds);

                        Utils.setOddsEur(tv_top2,tv_bottom2,europeOddsBean.firstDrowOdds,europeOddsBean.drowOdds);

                        Utils.setOddsEur(tv_top3,tv_bottom3,europeOddsBean.firstLoseOdds,europeOddsBean.loseOdds);




                        if(europeOddsBean.companyId == 0){
                            item.findViewById(R.id.iv_right).setVisibility(View.INVISIBLE);
                        }else {
                            item.setTag(europeOddsBean.companyId + "-" + type);
                            item.setOnClickListener(new MyClick());
                        }

                        ll_item_container.addView(item);
                    }


                    break;
                case 1:

                    for(int i = 0;i<asia.size();i++){

                        View item = View.inflate(QCaApplication.getContext(),R.layout.odds_child_item,null);


                        TextView tv_name = item.findViewById(R.id.tv_name);
                        TextView tv_top1 = item.findViewById(R.id.tv_top1);
                        TextView tv_bottom1 = item.findViewById(R.id.tv_bottom1);
                        TextView tv_top2 = item.findViewById(R.id.tv_top2);
                        TextView tv_bottom2 = item.findViewById(R.id.tv_bottom2);
                        TextView tv_top3 = item.findViewById(R.id.tv_top3);
                        TextView tv_bottom3 = item.findViewById(R.id.tv_bottom3);

                        MatchOddsBean.DataBean.OddsBean.AsiaBean asiaBean = asia.get(i);


                        tv_name.setText(asiaBean.title);


                        double v = Double.valueOf(asiaBean.firstHostOdds) + 1.00;
                        String format = String.format("%.2f", v);
                        tv_top1.setText(format);

                        double v1 = Double.valueOf(asiaBean.hostOdds) + 1.00;
                        String format1 = String.format("%.2f", v1);

                        tv_bottom1.setText(format1);


                        if(v1 >v){
                            tv_bottom1.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                        }else if(v1<v){
                            tv_bottom1.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));

                        }


                        String tape = Utils.parseOddOfHandicap(asiaBean.firstTape);
                        tv_top2.setText(tape);
                        String tape1 = Utils.parseOddOfHandicap(asiaBean.tape);
                        tv_bottom2.setText(tape1);



                        double v2 = Double.valueOf(asiaBean.firstAwayOdds) + 1.00;
                        String format2 = String.format("%.2f", v2);
                        tv_top3.setText(format2);

                        double v3 = Double.valueOf(asiaBean.awayOdds) + 1.00;
                        String format3 = String.format("%.2f", v3);

                        tv_bottom3.setText(format3);


                        if(v3 >v2){
                            tv_bottom3.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                        }else if(v3<v2){
                            tv_bottom3.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));

                        }

                        item.setTag(asiaBean.companyId+"-"+type);
                        item.setOnClickListener(new MyClick());

                        ll_item_container.addView(item);


                    }

                    break;
                case 2:

                    for(int i = 0;i<score.size();i++){
                        View item = View.inflate(QCaApplication.getContext(),R.layout.odds_child_item,null);


                        TextView tv_name = item.findViewById(R.id.tv_name);
                        TextView tv_top1 = item.findViewById(R.id.tv_top1);
                        TextView tv_bottom1 = item.findViewById(R.id.tv_bottom1);
                        TextView tv_top2 = item.findViewById(R.id.tv_top2);
                        TextView tv_bottom2 = item.findViewById(R.id.tv_bottom2);
                        TextView tv_top3 = item.findViewById(R.id.tv_top3);
                        TextView tv_bottom3 = item.findViewById(R.id.tv_bottom3);

                        MatchOddsBean.DataBean.OddsBean.ScoreBean scoreBean = score.get(i);


                        tv_name.setText(scoreBean.title);


                        double v = Double.valueOf(scoreBean.firstHostOdds)/10000 + 1.00;
                        String format = String.format("%.2f", v);
                        tv_top1.setText(format);

                        double v1 = Double.valueOf(scoreBean.hostOdds)/10000 + 1.00;
                        String format1 = String.format("%.2f", v1);

                        tv_bottom1.setText(format1);


                        if(v1 >v){
                            tv_bottom1.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                        }else if(v1<v){
                            tv_bottom1.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));

                        }


                        String tape = Utils.parseOddOfHandicap(scoreBean.firstTape);
                        tv_top2.setText(tape);
                        String tape1 = Utils.parseOddOfHandicap(scoreBean.tape);
                        tv_bottom2.setText(tape1);



                        double v2 = Double.valueOf(scoreBean.firstAwayOdds)/10000 + 1.00;
                        String format2 = String.format("%.2f", v2);
                        tv_top3.setText(format2);

                        double v3 = Double.valueOf(scoreBean.awayOdds)/10000 + 1.00;
                        String format3 = String.format("%.2f", v3);

                        tv_bottom3.setText(format3);


                        if(v3 >v2){
                            tv_bottom3.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                        }else if(v3<v2){
                            tv_bottom3.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));

                        }




                        item.setTag(scoreBean.companyId+"-"+type);
                        item.setOnClickListener(new MyClick());

                        ll_item_container.addView(item);
                    }

                    break;
            }





            return convertView;
        }
    }



    private class MyClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v.getTag() != null){
                String tag = (String) v.getTag();
                String[] split = tag.split("-");

                String companyId = split[0];
                int type = Integer.valueOf(split[1]);

                OddsDetailActivity.start(MatchDetailActivity.this,type,betId,companyId);


            }else{
                updatePan(v.getId());
            }
        }
    }


    private int clickTopType = -1;
   // private LinearLayout ll_europe_edit_groom;
    private TextView tv_left_europe_edit_groom;
    private TextView tv_m_europe_edit_groom;
    private TextView tv_right_europe_edit_groom;
    private void updatePan(int resID){


        switch (resID){

            case R.id.tv_left_europe_edit_groom:

                if(clickTopType == 0)return;
//                ll_europe_edit_groom.setBackgroundResource(R.drawable.tuijiandan_empty_3);
             //   ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_left);
                clickTopType = 0;
                requestOdds(0);
                break;
            case R.id.tv_m_europe_edit_groom:
                if(clickTopType == 1)return;

          //      ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_m);
                clickTopType = 1;
                requestOdds(1);
                break;
            case R.id.tv_right_europe_edit_groom:
                if(clickTopType == 2)return;

          //      ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_righ);
                clickTopType = 2;
                requestOdds(2);
                break;

        }

    }


    /**
     * 获取情报信息
     * https://app.mayisports.com/php/api.php?action=match&type=match_information&betId=1433131
     */
    private void requestInfromaction() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match");
        params.put("type","match_information");
        params.put("betId",betId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
                matchInformationBean = JsonParseUtils.parseJsonClass(string,MatchInformationBean.class);

                if(matchInformationBean != null && matchInformationBean.data != null && matchInformationBean.data.information != null) {
//                    Collections.reverse(matchInformationBean.data.information);
                    List<MatchInformationBean.DataBean.InformationBean> information = matchInformationBean.data.information;



                    informationInfo.clear();
                    informationToast.clear();
                    informationforecast.clear();
                    informationGroom.clear();


                    for(int i = 0;i<information.size();i++){
                        MatchInformationBean.DataBean.InformationBean informationBean = information.get(i);
                        String type = informationBean.type;

                        switch (type){
                            case "1":
                            case "2":
                                informationInfo.add(informationBean);
                                break;
                            case "3":
                                informationToast.add(informationBean);
                                break;
                            case "4":
                                informationforecast.add(informationBean);
                                break;
                            case "5":
                                informationGroom.add(informationBean);
                                break;
                        }
                    }
                }

                lv_match_detail.setAdapter(new MatchQingBaoAdapter());
                //ll_groom_match_detail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                }else{
                    if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private MatchInformationBean matchInformationBean;
    private List<MatchInformationBean.DataBean.InformationBean> informationInfo = new ArrayList<>();
    private List<MatchInformationBean.DataBean.InformationBean> informationToast = new ArrayList<>();
    private List<MatchInformationBean.DataBean.InformationBean> informationforecast = new ArrayList<>();
    private List<MatchInformationBean.DataBean.InformationBean> informationGroom = new ArrayList<>();


    class MatchQingBaoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(matchInformationBean == null || matchInformationBean.data == null || matchInformationBean.data.information == null ||  matchInformationBean.data.information.size() == 0){
                convertView = View.inflate(QCaApplication.getContext(),R.layout.empty,null);
         //       ll_no_data.setVisibility(View.VISIBLE);
                return convertView;
            }


            convertView = View.inflate(QCaApplication.getContext(),R.layout.match_qingbao_item,null);


            LinearLayout ll_info_item = convertView.findViewById(R.id.ll_info_item);
            if(informationInfo.size()>0){
                 ll_info_item.setVisibility(View.VISIBLE);

                 TextView tv_host_info = convertView.findViewById(R.id.tv_host_info);
                 TextView tv_away_info = convertView.findViewById(R.id.tv_away_info);

                MatchInformationBean.DataBean.InformationBean informationBean = informationInfo.get(1);

                SpannableString spannableString = new SpannableString(informationBean.information);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                int i = informationBean.information.indexOf(" ");
                if(i>0) {
                    spannableString.setSpan(styleSpan, 0, i, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                tv_host_info.setText(spannableString);


                MatchInformationBean.DataBean.InformationBean informationBean1 = informationInfo.get(0);

                SpannableString spannableString1 = new SpannableString(informationBean1.information);
                StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
                int i1 = informationBean1.information.indexOf(" ");
                if(i1>0) {
                    spannableString1.setSpan(styleSpan1, 0, i1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                }
                tv_away_info.setText(spannableString1);


            }else{
                ll_info_item.setVisibility(View.GONE);
            }

            //特别提醒
            LinearLayout ll_toast_item = convertView.findViewById(R.id.ll_toast_item);
            if(informationToast.size()>0){
                ll_toast_item.setVisibility(View.VISIBLE);

                LinearLayout ll_toast_container = convertView.findViewById(R.id.ll_toast_container);

                for(int i = 0;i<informationToast.size();i++){

                    MatchInformationBean.DataBean.InformationBean informationBean = informationToast.get(i);

                    View item = View.inflate(QCaApplication.getContext(),R.layout.match_toast,null);
                    ImageView iv_level = item.findViewById(R.id.iv_level);
                    TextView tv_content = item.findViewById(R.id.tv_content);

                    switch (informationBean.level) {
                        case "1"://红色
                            iv_level.setBackgroundResource(R.drawable.shape_red_cricel);
                            break;
                        case "2"://黄色
                            iv_level.setBackgroundResource(R.drawable.shape_yellow_cricel);
                            break;
                        case "3"://灰色
                            iv_level.setBackgroundResource(R.drawable.shape_gray_cricel);
                            break;
                    }


                    SpannableString spannableString = new SpannableString(informationBean.information);
                    StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                    int j = informationBean.information.indexOf("】",1);
                    spannableString.setSpan(styleSpan,0,j+1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    tv_content.setText(spannableString);


                    ll_toast_container.addView(item);


                }

            }else{
                ll_toast_item.setVisibility(View.GONE);
            }


            //媒体预测
            LinearLayout ll_forecast_item = convertView.findViewById(R.id.ll_forecast_item);
            if(informationforecast.size()>0){
                ll_forecast_item.setVisibility(View.VISIBLE);

                LinearLayout ll_forecast_container = convertView.findViewById(R.id.ll_forecast_container);
                for(int i = 0;i<informationforecast.size();i++) {
                    MatchInformationBean.DataBean.InformationBean informationBean = informationforecast.get(i);


                    View item = View.inflate(QCaApplication.getContext(), R.layout.forecast_item, null);
                    TextView tv_name = item.findViewById(R.id.tv_name);
                    TextView tv_content = item.findViewById(R.id.tv_content);

                    String[] split = informationBean.information.split("：");
                    tv_name.setText(split[0]);
                    tv_content.setText(split[1]);

                    ll_forecast_container.addView(item);

                }



            }else{
                ll_forecast_item.setVisibility(View.GONE);
            }

            //大V推荐
            LinearLayout ll_groom_item = convertView.findViewById(R.id.ll_groom_item);
            if(informationGroom.size()>0){
                ll_groom_item.setVisibility(View.VISIBLE);

                LinearLayout ll_groom_container = convertView.findViewById(R.id.ll_groom_container);

                for(int i = 0;i<informationGroom.size();i++){
                    MatchInformationBean.DataBean.InformationBean informationBean = informationGroom.get(i);

                    View item = View.inflate(QCaApplication.getContext(),R.layout.groom_item,null);
                    TextView tv = item.findViewById(R.id.tv);


                    SpannableString spannableString = new SpannableString(informationBean.information);
                    StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                    int j = informationBean.information.indexOf("：",1);
                    if(j != -1) {
                        spannableString.setSpan(styleSpan, 0, j + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
                    int j1 = informationBean.information.indexOf("。推荐",1);
                    if(j1 != -1) {
                        spannableString.setSpan(styleSpan1, j1 + 1, informationBean.information.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }

                    tv.setText(spannableString);
                    ll_groom_container.addView(item);
                }

            }else{
                ll_groom_item.setVisibility(View.GONE);
            }



            return convertView;
        }
    }


    /**
     * 获取赛况数据
     */
    private MathesInfoBean mathesInfoBean;
    private  List<Item> itemData;
    private void requestMathes() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match_event");
        params.put("betId",betId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
                    mathesInfoBean = JsonParseUtils.parseJsonClass(string,MathesInfoBean.class);
                    if(mathesInfoBean != null && mathesInfoBean.data != null && mathesInfoBean.data.eventList != null) {
                        Collections.reverse(mathesInfoBean.data.eventList);
                    }

                    if(mathesInfoBean.data.playerH != null || mathesInfoBean.data.playerG != null ) {
                         itemData = delPlayerDatas(mathesInfoBean.data.playerH, mathesInfoBean.data.playerG);
                    }else{
                        itemData = null;
                    }

                    lv_match_detail.setAdapter(new MatchInfoAdapter());
                  //  ll_groom_match_detail.setVisibility(View.VISIBLE);

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                }else{
                    if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private boolean isMatchInfoNull(){

        if(mathesInfoBean == null || mathesInfoBean.data == null)return true;
        if(mathesInfoBean.data.eventList != null && mathesInfoBean.data.eventList.size()>0)return false;
        if(mathesInfoBean.data.statics != null && mathesInfoBean.data.statics.size()>0)return false;
        if(mathesInfoBean.data.techStatics != null && mathesInfoBean.data.techStatics.size()>0)return false;
        if(mathesInfoBean.data.playerH != null && mathesInfoBean.data.playerH.size()>0)return false;
        if(mathesInfoBean.data.playerG != null && mathesInfoBean.data.playerG.size() >0)return false;

        return true;
    }
    //https://app.mayisports.com/php/api.php?action=match_event&betId=1511709
    class MatchInfoAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {



            if(isMatchInfoNull()){
                convertView = View.inflate(QCaApplication.getContext(),R.layout.empty,null);
             //   ll_no_data.setVisibility(View.VISIBLE);
                return convertView;
            }else{
                convertView = View.inflate(QCaApplication.getContext(),R.layout.one_item_layout,null);
                //比赛事件
                LinearLayout ll_matches_item = convertView.findViewById(R.id.ll_matches_item);
                LinearLayout ll_matches_container = convertView.findViewById(R.id.ll_matches_container);

                if(mathesInfoBean.data.eventList != null){
                    ll_matches_item.setVisibility(View.VISIBLE);
                   // Collections.reverse(mathesInfoBean.data.eventList);
                    List<MathesInfoBean.DataBean.EventListBean> eventList = mathesInfoBean.data.eventList;
                   // ll_matches_container.removeAllViews();
                    for(int i = 0;i<eventList.size();i++){
                        View item = View.inflate(QCaApplication.getContext(),R.layout.match_event_item,null);

                        LinearLayout ll_left = item.findViewById(R.id.ll_left);
                        TextView tv_left_top = item.findViewById(R.id.tv_left_top);
                        TextView tv_left_bottom = item.findViewById(R.id.tv_left_bottom);

                        LinearLayout ll_right = item.findViewById(R.id.ll_right);
                        TextView tv_right_top = item.findViewById(R.id.tv_right_top);
                        TextView tv_right_bottom = item.findViewById(R.id.tv_right_bottom);


                        TextView tv_time = item.findViewById(R.id.tv_time);

                        MathesInfoBean.DataBean.EventListBean eventListBean = eventList.get(i);


                        tv_time.setText(eventListBean.time+"");

                        if(eventListBean.is_home == 1){//主队
                            ll_left.setVisibility(View.VISIBLE);
                            parseTypeAndBindData(eventListBean,ll_left,tv_left_top,tv_left_bottom);
                        }else{
                            ll_right.setVisibility(View.VISIBLE);
                            parseTypeAndBindData(eventListBean,ll_right,tv_right_top,tv_right_bottom);
                        }

                        ll_matches_container.addView(item);
                    }
                }else{
                    ll_matches_item.setVisibility(View.GONE);
                }


                //数据统计
                LinearLayout ll_data_anly_item = convertView.findViewById(R.id.ll_data_anly_item);
                LinearLayout ll_data_anly_container = convertView.findViewById(R.id.ll_data_anly_container);

                if(mathesInfoBean.data.statics != null){
                     ll_data_anly_item.setVisibility(View.VISIBLE);
                    List<List<String>> statics = mathesInfoBean.data.statics;
                    for(int i = 0;i<statics.size();i++){
                        List<String> strings = statics.get(i);
                        View item = View.inflate(QCaApplication.getContext(),R.layout.data_anly_item,null);
                        TextView tv_left_data_anly = item.findViewById(R.id.tv_left_data_anly);
                        TextView tv_mid_data_anly  = item.findViewById(R.id.tv_mid_data_anly);
                        TextView tv_right_data_anly = item.findViewById(R.id.tv_right_data_anly);

                        tv_left_data_anly.setText(strings.get(1));
                        tv_mid_data_anly.setText(strings.get(0));
                        tv_right_data_anly.setText(strings.get(2));

                        ll_data_anly_container.addView(item);
                    }




                }else{
                    ll_data_anly_item.setVisibility(View.GONE);
                }



                //球员阵容
                LinearLayout ll_player_item = convertView.findViewById(R.id.ll_player_item);


                 if(mathesInfoBean.data.playerH != null || mathesInfoBean.data.playerG != null) {

                     LinearLayout ll_player_container = convertView.findViewById(R.id.ll_player_container);

                     TextView tv_host_name = convertView.findViewById(R.id.tv_host_name);
                     TextView tv_away_name = convertView.findViewById(R.id.tv_away_name);

                     tv_host_name.setText(tv_host_name_match_detail.getText());
                     tv_away_name.setText(tv_away_name_match_detail.getText());

                     ll_player_item.setVisibility(View.VISIBLE);
                     List<List<String>> playerH = mathesInfoBean.data.playerH;
                     List<List<String>> playerG = mathesInfoBean.data.playerG;

                     int count = 0;

                     if(playerG1.size()<=playerH1.size()){
                         count = playerH1.size();
                     }else{
                         count = playerG1.size();
                     }
                     for (int i = 0; i <count; i++) {


                        View item = View.inflate(QCaApplication.getContext(),R.layout.player_item,null);
                        if(i == 0){
                            View title = View.inflate(QCaApplication.getContext(),R.layout.player_title_item,null);
                            TextView tv = title.findViewById(R.id.tv);
                            tv.setText("首发阵容");
                            ll_player_container.addView(title);
                        }

//                        else if(!stringsH.get(2).equals(mathesInfoBean.data.playerH.get(i-1).get(2))){
//                            View title = View.inflate(QCaApplication.getContext(),R.layout.player_title_item,null);
//                            TextView tv = title.findViewById(R.id.tv);
//                            tv.setText("替补阵容");
//                            ll_player_container.addView(title);
//                            try {
//                                ll_player_container.getChildAt(i).findViewById(R.id.v).setVisibility(View.GONE);
//                            }catch (Exception e){
//
//                            }
//                        }





                        TextView tv_host_count = item.findViewById(R.id.tv_host_count);
                        TextView tv_type_host = item.findViewById(R.id.tv_type_host);
                        TextView tv_host_pname = item.findViewById(R.id.tv_host_pname);

                        TextView tv_away_pname = item.findViewById(R.id.tv_away_pname);
                        TextView tv_away_type = item.findViewById(R.id.tv_away_type);
                        TextView tv_away_count = item.findViewById(R.id.tv_away_count);

                        if(i< playerH1.size()) {
                            List<String> stringsH = playerH1.get(i);
                            tv_host_count.setText(stringsH.get(0));
                            tv_type_host.setText(stringsH.get(2).substring(0, 1));
                            tv_host_pname.setText(stringsH.get(1));
                        }else{
                            tv_host_count.setVisibility(View.INVISIBLE);
                            tv_type_host.setVisibility(View.INVISIBLE);
                            tv_host_pname.setVisibility(View.INVISIBLE);
                        }

//

                         if(i <playerG1.size()) {
                             List<String> stringsG = playerG1.get(i);

                             tv_away_count.setText(stringsG.get(0));
                             tv_away_type.setText(stringsG.get(2).substring(0, 1));
                             tv_away_pname.setText(stringsG.get(1));
                         }else {
                                tv_away_count.setVisibility(View.INVISIBLE);
                                tv_away_type.setVisibility(View.INVISIBLE);
                                tv_away_pname.setVisibility(View.INVISIBLE);

                         }
                        ll_player_container.addView(item);

                     }




                     if(playerG2.size()<=playerH2.size()){
                         count = playerH2.size();
                     }else{
                         count = playerG2.size();
                     }
                     for (int i = 0; i <count; i++) {


                         View item = View.inflate(QCaApplication.getContext(),R.layout.player_item,null);
                         if(i == 0){
                             View title = View.inflate(QCaApplication.getContext(),R.layout.player_title_item,null);
                             TextView tv = title.findViewById(R.id.tv);
                             tv.setText("替补阵容");
                             ll_player_container.addView(title);
                         }

//                        else if(!stringsH.get(2).equals(mathesInfoBean.data.playerH.get(i-1).get(2))){
//                            View title = View.inflate(QCaApplication.getContext(),R.layout.player_title_item,null);
//                            TextView tv = title.findViewById(R.id.tv);
//                            tv.setText("替补阵容");
//                            ll_player_container.addView(title);
//                            try {
//                                ll_player_container.getChildAt(i).findViewById(R.id.v).setVisibility(View.GONE);
//                            }catch (Exception e){
//
//                            }
//                        }





                         TextView tv_host_count = item.findViewById(R.id.tv_host_count);
                         TextView tv_type_host = item.findViewById(R.id.tv_type_host);
                         TextView tv_host_pname = item.findViewById(R.id.tv_host_pname);

                         TextView tv_away_pname = item.findViewById(R.id.tv_away_pname);
                         TextView tv_away_type = item.findViewById(R.id.tv_away_type);
                         TextView tv_away_count = item.findViewById(R.id.tv_away_count);

                         if(i< playerH2.size()) {
                             List<String> stringsH = playerH2.get(i);
                             tv_host_count.setText(stringsH.get(0));
                             tv_type_host.setText(stringsH.get(2).substring(0, 1));
                             tv_host_pname.setText(stringsH.get(1));
                         }else{
                             tv_host_count.setVisibility(View.INVISIBLE);
                             tv_type_host.setVisibility(View.INVISIBLE);
                             tv_host_pname.setVisibility(View.INVISIBLE);
                         }


                         if(i <playerG2.size()) {
                             List<String> stringsG = playerG2.get(i);

                             tv_away_count.setText(stringsG.get(0));
                             tv_away_type.setText(stringsG.get(2).substring(0, 1));
                             tv_away_pname.setText(stringsG.get(1));
                         }else {
                             tv_away_count.setVisibility(View.INVISIBLE);
                             tv_away_type.setVisibility(View.INVISIBLE);
                             tv_away_pname.setVisibility(View.INVISIBLE);

                         }


                         ll_player_container.addView(item);

                     }



                }else {
                    ll_player_item.setVisibility(View.GONE);
                }

                //技术统计
                LinearLayout ll_jishu_item = convertView.findViewById(R.id.ll_jishu_item);
                if(mathesInfoBean.data.techStatics != null){
                    ll_jishu_item.setVisibility(View.VISIBLE);

                    LinearLayout ll_jishu_container = convertView.findViewById(R.id.ll_jishu_container);

                    List<List<String>> statics = mathesInfoBean.data.techStatics;
                    for(int i = 0;i<statics.size();i++){
                        List<String> strings = statics.get(i);
                        View item = View.inflate(QCaApplication.getContext(),R.layout.data_anly_item,null);
                        TextView tv_left_data_anly = item.findViewById(R.id.tv_left_data_anly);
                        TextView tv_mid_data_anly  = item.findViewById(R.id.tv_mid_data_anly);
                        TextView tv_right_data_anly = item.findViewById(R.id.tv_right_data_anly);

                        tv_left_data_anly.setText(strings.get(1));
                        tv_mid_data_anly.setText(strings.get(0));
                        tv_right_data_anly.setText(strings.get(2));

                        ll_jishu_container.addView(item);
                    }




                }else{
                    ll_jishu_item.setVisibility(View.GONE);
                }

            }




            return convertView;
        }
    }


    /**
     * 处理阵容数据，分类拆分
     * @param playerH
     * @param playerG
     * @return
     */

    private List<List<String>> playerH1 = new ArrayList<>();
    private List<List<String>> playerH2 = new ArrayList<>();

    private List<List<String>> playerG1 = new ArrayList<>();
    private List<List<String>> playerG2 = new ArrayList<>();

    private List<Item> delPlayerDatas(List<List<String>> playerH, List<List<String>> playerG) {
            playerG1.clear();
            playerG2.clear();
            playerH1.clear();
            playerH2.clear();

            if(playerH != null) {

                for (int i = 0; i < playerH.size(); i++) {

                    List<String> strings = playerH.get(i);

                    String s = strings.get(2);
                    if (s.contains("首")) {
                        playerH1.add(strings);
                    } else {
                        playerH2.add(strings);
                    }
                }

            }

            if(playerG != null) {

                for (int i = 0; i < playerG.size(); i++) {

                    List<String> strings = playerG.get(i);

                    String s = strings.get(2);
                    if (s.contains("首")) {
                        playerG1.add(strings);
                    } else {
                        playerG2.add(strings);
                    }
                }

            }

        return null;
    }


    public  class Item{
        public String title;
        public  List<String> stringsH;
        public  List<String> stringsG;
    }
    /**
     * 填充时间内容
     */
    private void parseTypeAndBindData(MathesInfoBean.DataBean.EventListBean eventListBean,LinearLayout ll, TextView tv_top, TextView tv_bottom) {

        try {
            boolean isHome = false;
            if (eventListBean.is_home == 1) isHome = true;
            switch (eventListBean.type) {
                case "player-change"://换人
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);
                    Drawable drawable = getResources().getDrawable(R.drawable.up_player);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawable, null);
                    } else {
                        tv_top.setCompoundDrawables(drawable, null, null, null);
                    }


                    tv_bottom.setVisibility(View.VISIBLE);
                    tv_bottom.setText(eventListBean.player2.name);
                    Drawable drawable1 = getResources().getDrawable(R.drawable.down_player);
                    drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
                    if (isHome) {
                        tv_bottom.setCompoundDrawables(null, null, drawable1, null);
                    } else {
                        tv_bottom.setCompoundDrawables(drawable1, null, null, null);
                    }

                    break;
                case "yellow-card":
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableY = getResources().getDrawable(R.drawable.yellow_card_one);
                    drawableY.setBounds(0, 0, drawableY.getMinimumWidth(), drawableY.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableY, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableY, null, null, null);
                    }
                    break;
                case "two-yellow-card":
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableYT = getResources().getDrawable(R.drawable.yellow_card_two);
                    drawableYT.setBounds(0, 0, drawableYT.getMinimumWidth(), drawableYT.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableYT, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableYT, null, null, null);
                    }
                    break;
                case "red-card":
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableR = getResources().getDrawable(R.drawable.red_card);
                    drawableR.setBounds(0, 0, drawableR.getMinimumWidth(), drawableR.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableR, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableR, null, null, null);
                    }
                    break;
                case "goal"://进球

                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableI = getResources().getDrawable(R.drawable.in_ball);
                    drawableI.setBounds(0, 0, drawableI.getMinimumWidth(), drawableI.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableI, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableI, null, null, null);
                    }


                    break;
                case "goal-own"://乌龙
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableW = getResources().getDrawable(R.drawable.wulong_ball);
                    drawableW.setBounds(0, 0, drawableW.getMinimumWidth(), drawableW.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableW, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableW, null, null, null);
                    }
                    break;
                case "goal-kick"://点球
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableD = getResources().getDrawable(R.drawable.dian_ball);
                    drawableD.setBounds(0, 0, drawableD.getMinimumWidth(), drawableD.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableD, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableD, null, null, null);
                    }
                    break;
                case "goal-kick-fail"://点球未进
                    tv_top.setVisibility(View.VISIBLE);
                    tv_top.setText(eventListBean.player1.name);

                    Drawable drawableDF = getResources().getDrawable(R.drawable.dian_ball_no);
                    drawableDF.setBounds(0, 0, drawableDF.getMinimumWidth(), drawableDF.getMinimumHeight());
                    if (isHome) {
                        tv_top.setCompoundDrawables(null, null, drawableDF, null);
                    } else {
                        tv_top.setCompoundDrawables(drawableDF, null, null, null);
                    }
                    break;
            }

        }catch (Exception e){

        }

    }


    private void goDetail( MatchDetailBean.DataBean.ListBean bean){
        Intent intent = new Intent(this, HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
        intent.putExtra("betId",matchDetailBean.data.match.betId);
        startActivity(intent);
    }


    /**
     * 点击收藏
     */
    private void delStar() {

        if(matchlistBean == null)return;
        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,ACTION);
            return;
        }
        requestCollect();
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
        params.put("subtype",1);
        params.put("id",matchlistBean.betId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
                if(reportSubmitBean.status.result == 1){
//                    ToastUtils.toast("收藏成功");
//                 iv_right_title_match_detail.setTag(1);
                    iv_right_title_match_detail.setTag(1);
                    iv_right_title_match_detail.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));

                    NotificationsUtils.checkNotificationAndStartSetting(MatchDetailActivity.this,lv_match_detail);

                }else if(reportSubmitBean.status.result == 0){
//                    ToastUtils.toast("取消收藏");
                      iv_right_title_match_detail.setTag(0);
                      iv_right_title_match_detail.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });
    }
    /**
     * 刷新顶部tab状态
     * @param postion
     */
    private void selectOne(int postion){

        tv_groom_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_groom_match_detail.setVisibility(View.INVISIBLE);

        tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_analyes_match_detail.setVisibility(View.INVISIBLE);

        tv_price_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_price_match_detail.setVisibility(View.INVISIBLE);

        tv_outs_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_outs_match_detail.setVisibility(View.INVISIBLE);

        tv_qingbao_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_qingbao_match_detail.setVisibility(View.INVISIBLE);

        //ll_no_data.setVisibility(View.GONE);
        switch (postion){
            case 0:
                tv_groom_match_detail.setTextColor(getResources().getColor(R.color.white));
                iv_groom_match_detail.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.white));
                iv_analyes_match_detail.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_price_match_detail.setTextColor(getResources().getColor(R.color.white));
                iv_price_match_detail.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_outs_match_detail.setTextColor(getResources().getColor(R.color.white));
                iv_outs_match_detail.setVisibility(View.VISIBLE);
                break;
            case 4:
                tv_qingbao_match_detail.setTextColor(getResources().getColor(R.color.white));
                iv_qingbao_match_detail.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Constant.matchlistBean = null;
        refreshHandler.removeCallbacksAndMessages(null);
        destroyReceiver();
    }
}
