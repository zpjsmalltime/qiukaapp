package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.TopicDetailBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.GridViewUtils;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NetWorkUtil;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.RadiusBackgroundSpan;
import com.mayisports.qca.view.SelectPicPopupWindow;
import com.mayisports.qca.view.SharePopuwind;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 话题详情页
 */
public class TopicDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {

    public static void start(Activity activity,String topicId){
        Intent intent  = new Intent(activity,TopicDetailActivity.class);
        intent.putExtra("topicId",topicId);
        activity.startActivity(intent);
    }


    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "TopicDetailActivity";
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
            int result = intent.getIntExtra(LoginActivity.RESULT, 0);
            if(result == 66){//发布页刷新数据回调
                initDatas();
            }
        }
    }




    @Override
    protected int setViewForContent() {
        return R.layout.activity_topic_detail;
    }

    private XRefreshView xfv_topic_detail_activity;
    private ListView lv_topic_detail_activity;
    private FrameLayout fl_btn_topic_item;
    private MyAdapter myAdapter = new MyAdapter();
    private RelativeLayout rl_load;
    private LinearLayout ll_no_data;

    private JCVideoPlayerStandard player_video;

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;

    @Override
    protected void initView() {
        super.initView();
        initReceiver();
        setTitleShow(true);

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });


        tv_ritht_title.setVisibility(View.INVISIBLE);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText("话题");

        iv_right_title.setVisibility(View.VISIBLE);
        iv_right_title.setImageDrawable(getResources().getDrawable(R.drawable.dot_more));
        iv_right_title.setOnClickListener(this);

        xfv_topic_detail_activity = findViewById(R.id.xfv_topic_detail_activity);
        lv_topic_detail_activity = findViewById(R.id.lv_topic_detail_activity);

        fl_btn_topic_item = findViewById(R.id.fl_btn_topic_item);
        fl_btn_topic_item.setOnClickListener(this);
        rl_load = findViewById(R.id.rl_load_layout);

        xfv_topic_detail_activity.setPullRefreshEnable(true);
        xfv_topic_detail_activity.setXRefreshViewListener(new MyXListener());

        addHeader();

//        lv_topic_detail_activity.setAdapter(myAdapter);
        lv_topic_detail_activity.setOnItemClickListener(this);
//        lv_topic_detail_activity.setAdapter(myAdapter);

    }

    private ImageView iv_header_topic_header;
    private TextView tv_title_topic_header;
    private WebView wv_topic_header;
    private ImageView iv_host_icon_home_detail;
    private TextView tv_host_name_home_detail;
    private TextView tv_cent_top_home_detail;
    private  ImageView iv_away_icon_home_detail;
    private TextView tv_away_name_home_detail;
    private View ll_match_header;
    private TextView tv_count_header;
    private TextView tv_view_count_topic_detail;

    private boolean isPause;


    @Override
    protected void onPause() {
        super.onPause();
        if (player_video.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING ) {
            isPause = true;
            player_video.startButton.performClick();
        }
    }

    private ImageView iv_back,iv_dot_more;

    private TextView tv_default_sort;
    private ImageView iv_default_sort;
    private TextView tv_latest_sort;
    private ImageView iv_latest_sort;


    private View v_vote_header;
    private TextView tv_host_vote_count;
    private TextView tv_host_name;
    private TextView tv_away_name;
    private TextView tv_away_vote_count;
    private SeekBar sb_vote_topic;
    private TextView tv_curdown_time;

    private LinearLayout ll_count_header;
    private View v_line_gray;


    private void addHeader() {
        View view = View.inflate(this, R.layout.topic_detail_header,null);

        ll_count_header = view.findViewById(R.id.ll_count_header);
        v_line_gray = view.findViewById(R.id.v_line_gray);

        v_vote_header = view.findViewById(R.id.v_vote_header);
        tv_host_vote_count = view.findViewById(R.id.tv_host_vote_count);
        tv_host_name = view.findViewById(R.id.tv_host_name);
        tv_away_vote_count = view.findViewById(R.id.tv_away_vote_count);
        tv_away_name = view.findViewById(R.id.tv_away_name);

        sb_vote_topic = view.findViewById(R.id.sb_vote_topic);
        sb_vote_topic.setEnabled(false);

        tv_curdown_time = view.findViewById(R.id.tv_curdown_time);



        tv_default_sort = view.findViewById(R.id.tv_default_sort);
        iv_default_sort = view.findViewById(R.id.iv_default_sort);
        tv_latest_sort = view.findViewById(R.id.tv_latest_sort);
        iv_latest_sort = view.findViewById(R.id.iv_latest_sort);

        tv_default_sort.setOnClickListener(this);
        tv_latest_sort.setOnClickListener(this);


        iv_header_topic_header = view.findViewById(R.id.iv_header_topic_header);

        ViewGroup.LayoutParams layoutParams = iv_header_topic_header.getLayoutParams();
        layoutParams.height = (int) (DisplayUtil.getScreenWidth(this) / 750.0 * 304);
        iv_header_topic_header.setLayoutParams(layoutParams);

        player_video = findViewById(R.id.player_video);
        int heightVideo = (int) (DisplayUtil.getScreenWidth(this) / 16.0 * 9);
        player_video.getLayoutParams().height = heightVideo;

        tv_title_topic_header = view.findViewById(R.id.tv_title_topic_header);
        wv_topic_header = view.findViewById(R.id.wv_topic_header);
        wv_topic_header.setLayerType(View.LAYER_TYPE_NONE, null);
        ll_no_data = view.findViewById(R.id.ll_no_data);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_dot_more =findViewById(R.id.iv_dot_more);
        iv_dot_more.setOnClickListener(this);


        iv_host_icon_home_detail = view.findViewById(R.id.iv_host_icon_home_detail);
        tv_host_name_home_detail = view.findViewById(R.id.tv_host_name_home_detail);
        tv_cent_top_home_detail = view.findViewById(R.id.tv_cent_top_home_detail);
        iv_away_icon_home_detail = view.findViewById(R.id.iv_away_icon_home_detail);
        tv_away_name_home_detail = view.findViewById(R.id.tv_away_name_home_detail);
        tv_count_header = view.findViewById(R.id.tv_count_header);
        tv_view_count_topic_detail = view.findViewById(R.id.tv_view_count_topic_detail);

        ll_match_header = view.findViewById(R.id.ll_match_header);
        ll_match_header.setOnClickListener(this);


        lv_topic_detail_activity.addHeaderView(view);
    }

    private SelectPicPopupWindow selectPicPopupWindow;

    /**
     * 处理点击
     * @param view
     */
    @Override
    public void onClick(View view) {
        super.onClick(view);


        if(topicDetailBean == null || topicDetailBean.data == null)return;

        switch (view.getId()){
            case R.id.ll_match_header:
                Intent intent = new Intent(this, MatchDetailActivity.class);
                intent.putExtra("betId",topicDetailBean.data.topic.match.betId);
                startActivity(intent);
                break;
            case R.id.fl_btn_topic_item:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
                    return;
                }
//                PublishPointActivity.start(this,topicId);


                boolean isShow ;
                String close_time = topicDetailBean.data.close_time;

                if(!TextUtils.isEmpty(close_time)){

                    if("0".equals(close_time)){
                        isShow = true;
                    }else {
                        Long aLong = Long.valueOf(close_time + "000");
                        if (System.currentTimeMillis() < aLong) {
                            isShow = true;
                        } else {
                            isShow = false;
                        }
                    }

                }else{
                    isShow = true;
                }


                PublishPointActivity.start(this,topicId,isShow);
                break;

            case R.id.iv_dot_more:
            case R.id.iv_right_title://分享

                if(topicDetailBean == null || topicDetailBean.data == null || topicDetailBean.data.topic == null)return;
//                showShare();
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
                                        LoginUtils.goLoginActivity(TopicDetailActivity.this, HomeNewFragment.ACTION);
                                        return;
                                    }
                                    requestCollect();
                                    break;
                                case R.id.btn_pick_photo://举报
//                                    selectPicPopupWindow.dismiss();
//                                    if(isDelete){
//                                        ComentsDetailsActivity.this.onClick(iv_delect_header);
//                                    }else {
//                                        ReportActivity.start(ComentsDetailsActivity.this, comentsDetailBean.data.comment.user.user_id, comentsDetailBean.data.comment.user.nickname, comentsDetailBean.data.comment.create_time);
//                                    }
                                    break;
                            }
                        }
                    }, new SharePopuwind.ShareTypeClickListener() {
                        @Override
                        public void onTypeClick(int type) {
                            TopicDetailBean.DataBean.TopicBean topic = topicDetailBean.data.topic;
                            String imgUrl = null;
                            if(!TextUtils.isEmpty(topicDetailBean.data.topic.background_img)){
                                imgUrl = topicDetailBean.data.topic.background_img;
                            }
                            ShareUtils.shareMsgForTopic(type,topic.topic_id,imgUrl,topic.title,topic.desc_text,TopicDetailActivity.this);
                        }
                    });
                }

                selectPicPopupWindow.btn_share_photo.setVisibility(View.GONE);
                selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                selectPicPopupWindow.tv_share_title.setVisibility(View.VISIBLE);
                selectPicPopupWindow.btn_pick_photo.setVisibility(View.GONE);


                if("1".equals(topicDetailBean.data.topic.collection_status)){
                    selectPicPopupWindow.btn_take_photo.setText("取消收藏");
                }else{
                    selectPicPopupWindow.btn_take_photo.setText("收藏");
                }

                selectPicPopupWindow.showAtLocation(lv_topic_detail_activity, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);



                break;

            case R.id.tv_default_sort://评论 默认排序
                if(sortType == 0)break;
                sortType = 0;
                initData();
                break;
            case R.id.tv_latest_sort://评论 最新
                if(sortType == 1)break;
                sortType = 1;
                initData();
                break;

        }
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
        params.put("subtype",6);
        params.put("id",topicId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
                if(reportSubmitBean.status.result == 1){
                    ToastUtils.toast("收藏成功");
                    topicDetailBean.data.topic.collection_status = "1";
                }else if(reportSubmitBean.status.result == 0){
                    ToastUtils.toast("取消收藏");
                    topicDetailBean.data.topic.collection_status = "0";
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }



    private String topicId;

    @Override
    protected void initDatas() {
        super.initDatas();
        topicId = getIntent().getStringExtra("topicId");
//        topicId = "1196";
        initData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyReceiver();
        JCVideoPlayer.releaseAllVideos();
    }

    private void initData(){
        page = 0;
        requestNetDatas();
    }

    private int page;
    private int pre_page;
    private TopicDetailBean topicDetailBean;


    private Handler netErrorHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(rl_load != null && ll_net_error != null) {
                rl_load.setVisibility(View.GONE);
                RequestNetWorkUtils.kjHttp.cancelAll();
                ll_net_error.setVisibility(View.VISIBLE);
            }
        }
    };


    private void requestNetDatas() {
        //http:/\action=topic&type=get_comment_list&time=1516246328976&id=1196&start=0
        String url = Constant.BASE_URL + "php/api.php";

        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","get_comment_list");
        String time = System.currentTimeMillis() + "";
        time = time.substring(0,time.length()-3);
        params.put("time",time);
        params.put("id",topicId);
        params.put("start",page);
        params.put("orderBy",sortType+1);

        ll_net_error.setVisibility(View.GONE);

        netErrorHandler.removeCallbacksAndMessages(null);
        netErrorHandler.sendEmptyMessageDelayed(0,1000*10);



        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load,this) {
            @Override
            public void onSucces(String string) {
                if (page == 0){
                    topicDetailBean = JsonParseUtils.parseJsonClass(string, TopicDetailBean.class);

                    if (topicDetailBean != null) {
//                        if(!isNotify) {
                            updateHeader();
//                        }
                        pre_page = 1;
                        ListAdapter adapter = lv_topic_detail_activity.getAdapter();
                        if(adapter == null) {
                            lv_topic_detail_activity.setAdapter(myAdapter);
                        }else {
//                            if(isNotify){
//
//                            }
                             myAdapter.notifyDataSetChanged();
                        }
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        ll_count_header.setVisibility(View.GONE);
                        v_line_gray.setVisibility(View.GONE);
                    }
                 }else{
                    TopicDetailBean newBean = JsonParseUtils.parseJsonClass(string, TopicDetailBean.class);
                    try{
                        if(newBean.data.list.timeline.size()>0) {
                            topicDetailBean.data.list.timeline.addAll(newBean.data.list.timeline);
                        }else{
                            pre_page = 0;
                        }
                    }catch (Exception e ){
                         pre_page = 0;
                    }
                    myAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
//                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                netErrorHandler.removeCallbacksAndMessages(null);
                ll_net_error.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                netErrorHandler.removeCallbacksAndMessages(null);
                rl_load = findViewById(R.id.rl_load_layout);
                if(xfv_topic_detail_activity != null){
                    xfv_topic_detail_activity.stopRefresh();
                }
            }
        });

    }
    private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd  HH:mm");


    private   boolean setUp;

    /**
     * 排序类型 0默认  1最新
     */
    private int sortType;
    private void updateHeader() {
        if(topicDetailBean.data == null || topicDetailBean.data.topic == null)return;
        TopicDetailBean.DataBean.TopicBean topic = topicDetailBean.data.topic;

        if(!TextUtils.isEmpty(topic.video_url)) {
            setTitleShow(false);
            iv_header_topic_header.setVisibility(View.GONE);
            player_video.setVisibility(View.VISIBLE);
            iv_back.setVisibility(View.VISIBLE);
            iv_dot_more.setVisibility(View.VISIBLE);
            String videoUrl = topic.video_url;
            if (!setUp){
                setUp =player_video.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
                if (setUp) {
                    String imgUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".png";
                    PictureUtils.show(imgUrl, player_video.thumbImageView);
                }
                //模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
                boolean isAutoStart = getIntent().getBooleanExtra("isAutoStart", false);
                if (isAutoStart) {
                    player_video.startButton.performClick();
                }
            }else{
                if (isPause && player_video.currentState == JCVideoPlayer.CURRENT_STATE_PAUSE ) {
                    isPause = false;
                    player_video.startButton.performClick();
                }
            }
        }else{
            setTitleShow(true);
//            xfv_coments_detail.setPullRefreshEnable(true);
            player_video.setVisibility(View.GONE);
            iv_back.setVisibility(View.GONE);
            iv_dot_more.setVisibility(View.GONE);
            String background_img = topic.background_img;
            PictureUtils.show(background_img,iv_header_topic_header);
            iv_header_topic_header.setVisibility(View.VISIBLE);
        }





        SpannableString spannableString = new SpannableString("#"+topic.title);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#5EAEF2"));
        spannableString.setSpan(foregroundColorSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(topic.title);

        tv_title_topic_header.setText(spannableString);


        /**
         * 处理样式  <div class=\"description topic-description\">
         */
        String imgPath = "file:///android_asset/";




        String css = ".topic-description .git-wrap {" +
                "  position: relative;" +
                "}" +
                ".topic-description .git-wrap:after {" +
                "  content: '';" +
                "  position: absolute;" +
                "  top: 0;" +
                "  left: 0;" +
                "  bottom: 0;" +
                "  right: 0;" +
                "  width: 106px;" +
                "  height: 39px;" +
                "  margin: auto;" +
                "  background: no-repeat center center;" +
                "  background-size: 106px 39px;" +
                "}" +
                ".topic-description .git-wrap.status0:after {" +
                "  background-image: url('"+imgPath+"gifStatus0.png');" +
                "}" +
                ".topic-description .git-wrap.status1:after {" +
                "  background-image: url('"+imgPath+"gifStatus1.gif');" +
                "}.topic-description tr, .topic-description td {" +
                "  border: none;" +
                "}";

        String js = "";
        String jsNew = "<script type=\"text/javascript\" src=\"file:///android_asset/comment.js\" /></script>";
        String jsNewWifi = "<script type=\"text/javascript\" src=\"file:///android_asset/comment_wifi.js\" /></script>";

        if(NetWorkUtil.isWifi(QCaApplication.getContext())){
            js = jsNewWifi;
        }else{
            js = jsNew;
        }




//        String cssUrl = "<style>br {line-height: 2px;}div, p, span, ul, li, img {-webkit-touch-callout: none;-webkit-user-select: none; -khtml-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;-webkit-overflow-scrolling: touch;}.description, .description * {font-size: 16px;color: #282828; line-height: 19px;letter-spacing: 1px!important;margin-top: 5px!important;margin-bottom: 5px!important;}.description table {width: 100%%!important;}.description table tr:first-child {width: 100%%!important;}.description table tr:nth-child(2) td {width: 50%%!important;}.topic-description p{font-size: 16px;color: #282828; line-height: 19px;font-weight: 400; }.topic-description p img{display: block;visibility: visible!important;width:100%!important;height: auto!important; background: transparent!important; }</style><div class=\"description topic-description\">"+topic.desc+"</div>";

        String cssUrl = "<style>"+css+"br {line-height: 2px;}div, p, span, ul, li, img {-webkit-touch-callout: none;-webkit-user-select: none; -khtml-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;-webkit-overflow-scrolling: touch;}.description {width: 100%;overflow: hidden;}.description {color: #282828;}.description, .description * {font-size: 16px; line-height: 19px;letter-spacing: 1px!important;margin-top: 5px!important;margin-bottom: 5px!important;}.description table {width: 100%%!important;}.description table tr:first-child {width: 100%%!important;}.description table tr:nth-child(2) td {width: 50%%!important;}.topic-description, .topic-description * {font-size: 17px;line-height: 1.41;letter-spacing: 0.34px;}.topic-description p{line-height: 1.41;margin: 25px 0!important;font-weight: 400; }.topic-description img{display: block;visibility: visible!important;width:100%!important;height: auto!important;margin: 25px 0!important; background: transparent!important; }</style><div class=\"description topic-description\">"+topic.desc+"</div>"+js;

        String local = "file:///android_asset";

        wv_topic_header.loadDataWithBaseURL(local,cssUrl, "text/html" , "utf-8", null);

        if(topic.match !=null) {
            ll_match_header.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(topic.match.logoH)&&!"0".equals(topic.match.logoH)) {
                String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_" + topic.match.logoH + ".png";
                PictureUtils.show(hostUrl, iv_host_icon_home_detail);
            }

            if (!TextUtils.isEmpty(topic.match.logoG)&&!"0".equals(topic.match.logoG)) {
                String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_" + topic.match.logoG + ".png";
                PictureUtils.show(hostUrl, iv_away_icon_home_detail);
            }


            tv_host_name_home_detail.setText(URLDecoder.decode(topic.match.hostTeamName));
            tv_away_name_home_detail.setText(URLDecoder.decode(topic.match.awayTeamName));

            String s = topic.match.timezoneoffset + "000";
            Long time = Long.valueOf(s);
            tv_cent_top_home_detail.setText(URLDecoder.decode(topic.match.leagueName) + "  " + sdf.format(time));
        }else{
            ll_match_header.setVisibility(View.GONE);
        }

        String viewCount = "阅读 " + topicDetailBean.data.topic.view_count + "·参与 "+topicDetailBean.data.topic.user_count+"";
        tv_view_count_topic_detail.setText(viewCount);


        if(!TextUtils.isEmpty(topicDetailBean.data.topic.comment_count)) {

            int integer = Integer.valueOf(topicDetailBean.data.topic.comment_count);
            if (integer>0) {
                if(sortType == 0){
                    tv_count_header.setText("最新观点 " + integer + "");
                }else{
                    tv_count_header.setText("最新观点 " + integer + "");
                }

                tv_count_header.setVisibility(View.VISIBLE);

            } else {
                if(sortType == 0){
                    tv_count_header.setText("最新观点");
                }else {
                    tv_count_header.setText("最新观点");
                }

                tv_count_header.setVisibility(View.GONE);
            }

        }else{
            if(sortType == 0){
                tv_count_header.setText("最新观点");
            }else{
                tv_count_header.setText("最新观点");
            }

            tv_count_header.setVisibility(View.GONE);
        }

        switchSortType(sortType);



        if(topicDetailBean.data.topic.vote != null){
            List<TopicDetailBean.DataBean.TopicBean.VoteBean> vote = topicDetailBean.data.topic.vote;
            v_vote_header.setVisibility(View.VISIBLE);

            int total = vote.get(0).value+vote.get(1).value;
            String percent = Utils.parsePercent(vote.get(0).value, total, 0).replace("%","").trim();
            Integer percentInt = Integer.valueOf(percent);
            int awayPercent = 100-percentInt;

            if(awayPercent<3){
                awayPercent = 3;
                percentInt = 97;
            }else if(awayPercent>97){
                awayPercent = 97;
                percentInt=3;
            }

            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.6f);

            SpannableString hostPercent = new SpannableString(percentInt+"%");
            hostPercent.setSpan(relativeSizeSpan,hostPercent.length()-1,hostPercent.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_host_vote_count.setText(hostPercent);
            tv_host_name.setText(vote.get(0).title+"");

            SpannableString awayPercentstr = new SpannableString(awayPercent+"%");
            awayPercentstr.setSpan(relativeSizeSpan,awayPercentstr.length()-1,awayPercentstr.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tv_away_vote_count.setText(awayPercentstr);
            tv_away_name.setText(vote.get(1).title+"");


            sb_vote_topic.setProgress(percentInt);





        }else{
            v_vote_header.setVisibility(View.GONE);
        }


        String close_time = topicDetailBean.data.close_time;
        if(TextUtils.isEmpty(close_time)){
            close_time = "0";
        }

        Long aLong = Long.valueOf(close_time + "000");
        long l =  aLong - System.currentTimeMillis()/1000 ;
        if(l>=60){
            String revertCount = Utils.getRevertCount(l);

            String[] split = revertCount.split("-");
            String day = split[0];//天
            String hour = split[1];//小时
            String min = split[2];//分钟

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append("距截止");

            SpannableString spannableStringDay = new SpannableString(day);
//            spannableStringDay.setSpan();


            int radius = DisplayUtil.dip2px(QCaApplication.getContext(), 2);

            RadiusBackgroundSpan radiusBackgroundSpan0 = new RadiusBackgroundSpan(QCaApplication.getContext().getResources().getColor(R.color.yellow),radius);
            RadiusBackgroundSpan radiusBackgroundSpan1 = new RadiusBackgroundSpan(QCaApplication.getContext().getResources().getColor(R.color.yellow),radius);
            RadiusBackgroundSpan radiusBackgroundSpan2 = new RadiusBackgroundSpan(QCaApplication.getContext().getResources().getColor(R.color.yellow),radius);
            StyleSpan styleSpan0 = new StyleSpan(Typeface.BOLD);
            StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
            StyleSpan styleSpan2 = new StyleSpan(Typeface.BOLD);



            spannableStringDay.setSpan(radiusBackgroundSpan0,0,spannableStringDay.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringDay.setSpan(styleSpan0,0,spannableStringDay.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(spannableStringDay);
            spannableStringBuilder.append(" 天 ");

            SpannableString spannableStringHour = new SpannableString(hour);
            spannableStringHour.setSpan(radiusBackgroundSpan1,0,spannableStringHour.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringHour.setSpan(styleSpan1,0,spannableStringHour.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            spannableStringBuilder.append(spannableStringHour);
            spannableStringBuilder.append(" 时 ");

            SpannableString spannableStringMin = new SpannableString(min);
            spannableStringMin.setSpan(radiusBackgroundSpan2,0,spannableStringMin.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringMin.setSpan(styleSpan2,0,spannableStringMin.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

            spannableStringBuilder.append(spannableStringMin);
            spannableStringBuilder.append(" 分");

            tv_curdown_time.setText(spannableStringBuilder);
            tv_curdown_time.setVisibility(View.VISIBLE);
        }else{
            tv_curdown_time.setVisibility(View.GONE);
        }



    }

    private void switchSortType(int sortType){
        switch (sortType){
            case 0:
                tv_default_sort.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));
                tv_latest_sort.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_bf));
                iv_default_sort.setVisibility(View.VISIBLE);
                iv_latest_sort.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tv_default_sort.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_bf));
                tv_latest_sort.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));
                iv_default_sort.setVisibility(View.INVISIBLE);
                iv_latest_sort.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)return;
        TopicDetailBean.DataBean.ListBean.TimelineBean bean = topicDetailBean.data.list.timeline.get(position - 1);
        ComentsDetailsActivity.startForResult(66, this, bean.comment_id, position-1);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(topicDetailBean == null || topicDetailBean.data == null)return;
        if(requestCode == 66&&resultCode == 2){//动态详情
            int position = data.getIntExtra("position", -1);
            String praise_count = data.getStringExtra("praise_count");

            if(position != -1) {
                TopicDetailBean.DataBean.ListBean.TimelineBean bean = topicDetailBean.data.list.timeline.get(position);


                int  follow_status  = Integer.valueOf(data.getStringExtra("follow_status"));

                int praise_status = data.getIntExtra("praise_status", 0);
                bean.praise_count = praise_count;
                bean.praise_status = praise_status;
                bean.reply_count = data.getStringExtra("reply_count");

                boolean delete = data.getBooleanExtra("delete", false);
                if(delete){
                    topicDetailBean.data.list.timeline.remove(position);
                }

                myAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onReload() {

    }

    /**
     * 分享成功
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","share");
        //1 2 3
        params.put("type",2);
        params.put("id",topicId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });
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

    class MyXListener extends XRefreshView.SimpleXRefreshListener{

        @Override
        public void onRefresh(boolean isPullDown) {
                page = 0;
                rl_load = null;
                requestNetDatas();
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(topicDetailBean != null && topicDetailBean.data != null && topicDetailBean.data.list != null && topicDetailBean.data.list.timeline != null){
                int size = topicDetailBean.data.list.timeline.size();
                if(size == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                    ll_count_header.setVisibility(View.GONE);
                    v_line_gray.setVisibility(View.GONE);
                }else{
                    ll_no_data.setVisibility(View.GONE);
                    ll_count_header.setVisibility(View.VISIBLE);
                    v_line_gray.setVisibility(View.VISIBLE);
                    size ++;
                }
                return size;
            }
            ll_no_data.setVisibility(View.VISIBLE);
            ll_count_header.setVisibility(View.GONE);
            v_line_gray.setVisibility(View.GONE);
            return 0;
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(position == topicDetailBean.data.list.timeline.size()){
                convertView = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = convertView.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                return convertView;
            }

            MyHolder myHolder;
            if(convertView == null || convertView.getTag() == null ){
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(), R.layout.topic_detail_item,null);
                myHolder.iv_head_dynamic_item = convertView.findViewById(R.id.iv_head_dynamic_item);
                myHolder.vip_coin = convertView.findViewById(R.id.iv_vip_header);
                myHolder.iv_red_point_header = convertView.findViewById(R.id.iv_red_point_header);
                myHolder.tv_name_item = convertView.findViewById(R.id.tv_name_item);
                myHolder.tv_create_time_topic_item = convertView.findViewById(R.id.tv_create_time_topic_item);
                myHolder.iv_jinghua_item = convertView.findViewById(R.id.iv_jinghua_item);
                myHolder.tv_content_topic_detail_item = convertView.findViewById(R.id.tv_content_topic_detail_item);
                myHolder.ll_img_container_dynamic_item = convertView.findViewById(R.id.ll_img_container_dynamic_item);
                myHolder.ll_tag_container_item = convertView.findViewById(R.id.ll_tag_container_item);
                myHolder.tv_thumbup_topic_item = convertView.findViewById(R.id.tv_thumbup_topic_item);
                myHolder.tv_comments_topic_item = convertView.findViewById(R.id.tv_comments_topic_item);
                myHolder.rl_top = convertView.findViewById(R.id.rl_top);
                myHolder.tv_view_count_topic_item = convertView.findViewById(R.id.tv_view_count_topic_item);

                myHolder.gv_dynamic_item = convertView.findViewById(R.id.gv_dynamic_item);
                myHolder.rl_video_item = convertView.findViewById(R.id.rl_video_item);
                myHolder.iv_video_item = convertView.findViewById(R.id.iv_video_item);
                myHolder.tv_share_topic_item = convertView.findViewById(R.id.tv_share_topic_item);
                myHolder.v_bottom_line = convertView.findViewById(R.id.v_bottom_line);
                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }


            myHolder.v_bottom_line.setVisibility(View.VISIBLE);

            myHolder.gv_dynamic_item.setVisibility(View.GONE);
            myHolder.rl_video_item.setVisibility(View.GONE);


            final TopicDetailBean.DataBean.ListBean.TimelineBean bean = topicDetailBean.data.list.timeline.get(position);

            PictureUtils.showCircle(bean.headurl,myHolder.iv_head_dynamic_item);


            try {
                if (!TextUtils.isEmpty(bean.verify_type) && Integer.valueOf(bean.verify_type) > 0) {
                    myHolder.vip_coin.setVisibility(View.VISIBLE);
                } else {
                    myHolder.vip_coin.setVisibility(View.GONE);
                }
            }catch (Exception e){
                myHolder.vip_coin.setVisibility(View.GONE);
            }

            myHolder.tv_name_item.setText(bean.nickname);
            Long aLong = Long.valueOf(bean.create_time + "000");
            String createTime = Utils.getCreateTime(aLong);
            myHolder.tv_create_time_topic_item.setText(createTime);

            myHolder.rl_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(bean);
                    PersonalDetailActivity.start(TopicDetailActivity.this,bean.user_id,json);
                }
            });


            /**
             * 精华未知字段
             */
            myHolder.iv_jinghua_item.setVisibility(View.GONE);

            bean.comment = bean.comment.replaceAll("<br>","").replaceAll("<br","").replaceAll("<b","");
            myHolder.tv_content_topic_detail_item.setText(bean.comment+"");
//            myHolder.tv_content_topic_detail_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    ComentsDetailsActivity.start(TopicDetailActivity.this,bean.comment_id);
//                }
//            });

            if(bean.selectionList != null){
                myHolder.ll_tag_container_item.setVisibility(View.VISIBLE);
                addTags(myHolder.ll_tag_container_item,bean.selectionList);
            }else{
                myHolder.ll_tag_container_item.setVisibility(View.GONE);
            }

            if(bean.imgListEx != null){//http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif

                if(bean.imgListEx.size()>0){
                    if("video".equals(bean.imgListEx.get(0).type)){
                        myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                        myHolder.gv_dynamic_item.setVisibility(View.GONE);
                        myHolder.rl_video_item.setVisibility(View.VISIBLE);
                        TopicDetailBean.DataBean.ListBean.ImageList imageList = bean.imgListEx.get(0);
                        String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                        PictureUtils.show(imgUrl,myHolder.iv_video_item);
                        myHolder.rl_video_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                            ComentsDetailsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);
                                ComentsDetailsActivity.startForResult(2,TopicDetailActivity.this,bean.comment_id,position);
                            }
                        });


                    }else{
                        myHolder.rl_video_item.setVisibility(View.GONE);
                        myHolder.gv_dynamic_item.setVisibility(View.VISIBLE);
                        myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);

                        MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_dynamic_item.getAdapter();
                        if (adapter != null) {
                            adapter.setData(bean.imgListEx);
                            adapter.notifyDataSetChanged();
                        }else {
                            List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx = bean.imgListEx;
                            myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.imgListEx));
                        }




                        if(bean.imgListEx.size() == 4 || bean.imgListEx.size() == 2){
                            GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,2);
                        }else if(bean.imgListEx.size() == 1){
                            GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,1);
                        }else{
                            GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,3);
                        }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                        myHolder.gv_dynamic_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String urls = "";
                                for(int i = 0;i<bean.imgListEx.size();i++){
                                    String url = bean.imgListEx.get(i).url;
                                    String delUrl = Utils.delUrl(url);
                                    urls +=  delUrl+ ",";
                                }

                                String substring = urls.substring(0, urls.length() - 1);
                                Intent intent = new Intent(TopicDetailActivity.this,PicShowActivity.class);
                                intent.putExtra("urls",substring);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        });



                    }
                }else{
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                }


            }
            myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
//            bean.imglist = null;
//            if(bean.imglist != null) { //http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif
//                myHolder.ll_img_container_dynamic_item.setVisibility(View.VISIBLE);
//                for (int j = 0; j < myHolder.ll_img_container_dynamic_item.getChildCount(); j++) {
//                    myHolder.ll_img_container_dynamic_item.getChildAt(j).setVisibility(View.INVISIBLE);
//                }
//                String urls = "";
//                for (int i = 0; i < bean.imglist.size(); i++) {
//                    if (i > 2) break;
//                    String imgUrl = bean.imglist.get(i);
//                    if (TextUtils.isEmpty(imgUrl)) {
//                        myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
//                        break;
//                    }
//                    urls += imgUrl + ",";
//                }
//                for (int i = 0; i < bean.imglist.size(); i++) {
//                    if (i > 2) break;
//                    String imgUrl = bean.imglist.get(i);
//                    if (TextUtils.isEmpty(imgUrl)) {
//                        myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
//                        break;
//                    }
//                    ImageView imageView = (ImageView) myHolder.ll_img_container_dynamic_item.getChildAt(i);
//                    final int finalI = i;
//                    final String finalUrls = urls;
//                    imageView.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (finalUrls.length() > 0) {
//                                String substring = finalUrls.substring(0, finalUrls.length() - 1);
//                                Intent intent = new Intent(TopicDetailActivity.this, PicShowActivity.class);
//                                intent.putExtra("urls", substring);
//                                intent.putExtra("position", finalI);
//                                startActivity(intent);
//                            }
//                        }
//                    });
//                    imageView.setVisibility(View.VISIBLE);
//
//                    PictureUtils.show(imgUrl, imageView);
//                }
//
//
//            }


            myHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestThumbup(bean);
                }
            });

//            myHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    if(LoginUtils.isUnLogin()){
////                        LoginUtils.goLoginActivity(TopicDetailActivity.this, HomeNewFragment.ACTION);
////                        return;
////                    }
////                    String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment_id;
////                    WebViewActivtiy.start(TopicDetailActivity.this,url,"球咖");
//                    ComentsDetailsActivity.start(TopicDetailActivity.this,bean.comment_id);
//                }
//            });

            myHolder.tv_share_topic_item.setVisibility(View.GONE);
            myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ToastUtils.toast("分享");
                    showShare(position);
                }
            });
            if(bean.praise_status == 1){//点赞
                Drawable drawable= getResources().getDrawable(R.drawable.thumb_checked_small);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
            }else {
                Drawable drawable= getResources().getDrawable(R.drawable.thumb_small);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
            }

            DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item,bean.praise_count,false);
//            myHolder.tv_thumbup_topic_item.setText(bean.praise_count+"");
            DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.reply_count+"");
            if(bean.reply_count != null && Integer.valueOf(bean.reply_count)>0){
                myHolder.tv_comments_topic_item.setText(bean.reply_count+"");
            }else{
                myHolder.tv_comments_topic_item.setText("评论");
            }

            if(TextUtils.isEmpty(bean.view_count)){
                bean.view_count = "0";
            }
            myHolder.tv_view_count_topic_item.setText("阅读 "+bean.view_count+"");


            if(position == getCount()-3 || getCount()<3){
                if(pre_page == 1) {
                    rl_load = null;
                    page++;
                    requestNetDatas();
                }
            }

            return convertView;
        }
    }

    private void showShare(final int position) {
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                TopicDetailBean.DataBean.ListBean.TimelineBean timelineBean = topicDetailBean.data.list.timeline.get(position);
                ShareUtils.shareMsgForPointHasTopic(type,timelineBean.comment_id,topicDetailBean.data.topic.title,timelineBean.nickname,TopicDetailActivity.this);
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_topic_detail_activity, Gravity.CENTER, 0, 0);
    }

    class MyGvAdapter extends BaseAdapter{


        private List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx;


        public MyGvAdapter(List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }

        @Override
        public int getCount() {
            return imgListEx.size();
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

            TopicDetailBean.DataBean.ListBean.ImageList imageList = imgListEx.get(position);

            View view = View.inflate(QCaApplication.getContext(),R.layout.img_gif_layout,null);
            ImageView imageView = view.findViewById(R.id.iv);
            TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
            Utils.showType(imageList.url,imageView,tv_gif_icon, imageList.size);

            if(getCount() == 1){
                Utils.scaleImg(TopicDetailActivity.this,view,imageView,imageList.size);
            }
            return view;
        }

        public void setData(List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }
    }

    /**
     * 增加评论标记
     * @param selectionList
     */
    private void addTags(LinearLayout layout,List<String> selectionList) {
        layout.removeAllViews();
        for(int i = 0;i<selectionList.size();i++){
            String tag = selectionList.get(i);
            View view = View.inflate(QCaApplication.getContext(), R.layout.tag_topic_detail,null);
            TextView textView = view.findViewById(R.id.tv);
            textView.setText(tag);
            layout.addView(view);
        }
    }

    /**
     * 处理点赞
     * @param bean
     */
    private void requestThumbup(final TopicDetailBean.DataBean.ListBean.TimelineBean bean) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","praise");
        params.put("id",bean.comment_id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    bean.praise_count = praiseBean.data.praise_count;
                    bean.praise_status = praiseBean.data.praise_status;
                    myAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.toast(praiseBean.status.errstr);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });

    }

    static class MyHolder {
        public ImageView iv_head_dynamic_item;
        public ImageView vip_coin;
        public TextView iv_red_point_header;
        public TextView tv_name_item;
        public TextView tv_create_time_topic_item;
        public ImageView iv_jinghua_item;
        public TextView tv_content_topic_detail_item;
        public LinearLayout ll_tag_container_item;
        public LinearLayout ll_img_container_dynamic_item;
        public TextView tv_thumbup_topic_item;
        public TextView tv_comments_topic_item;
        public RelativeLayout rl_top;
        public TextView tv_view_count_topic_item;

        public GridView gv_dynamic_item;

        public RelativeLayout rl_video_item;
        public ImageView iv_video_item;
        public TextView tv_share_topic_item;
        public View v_bottom_line;
    }
}
