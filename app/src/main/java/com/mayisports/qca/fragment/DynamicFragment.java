package com.mayisports.qca.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mayi.mayisports.MainActivity;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.CoinDetailActivity;
import com.mayi.mayisports.activity.CollectionActivity;
import com.mayi.mayisports.activity.ComentsDetailsActivity;
import com.mayi.mayisports.activity.GroomPersonalActivity;
import com.mayi.mayisports.activity.GuessingCompetitionActivity;
import com.mayi.mayisports.activity.HomeItemDetailActivity;
import com.mayi.mayisports.activity.InforPostActivity;
import com.mayi.mayisports.activity.IntelligentBallSelectionActivity;
import com.mayi.mayisports.activity.MyFollowerListActivity;
import com.mayi.mayisports.activity.MyGroomListActivity;
import com.mayi.mayisports.activity.PersonalDetailActivity;
import com.mayi.mayisports.activity.PicShowActivity;
import com.mayi.mayisports.activity.PublishGroomActivity;
import com.mayi.mayisports.activity.PublishPointActivity;
import com.mayi.mayisports.activity.ReplyDetailsActivity;
import com.mayi.mayisports.activity.ShareComentsDetailsActivity;
import com.mayi.mayisports.activity.TopicDetailActivity;
import com.mayi.mayisports.activity.VideoPlayerActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.adapter.ShareReplyUtils;
import com.mayisports.qca.bean.ComentsDetailBean;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.HomeDataNewBean;
import com.mayisports.qca.bean.MainMetaBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.GridViewUtils;
import com.mayisports.qca.utils.ImageLoaderType;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.CustomLinkMovementMethod;
import com.mayisports.qca.view.JCaoStrandPlayer;
import com.mayisports.qca.view.JieCaoPlayer;
import com.mayisports.qca.view.NoLineClickSpan;
import com.mayisports.qca.view.SeeAllTextViewUtils;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.ta.utdid2.android.utils.NetworkUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.kymjs.kjframe.http.HttpParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 动态模块 球咖，关注模块，主要内容fragment
 * Created by Zpj on 2017/12/5.
 */

public class DynamicFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {


    /**
     * 发送到关注界面，刷新数据
     */
    public static void sendFoucsData(){
        Intent intent1 = new Intent(DynamicFragment.ACTION);
        intent1.putExtra(RESULT, 88);//发送到关注 球咖关注
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent1);
    }

    private boolean isShow = true;
    private int updateCount;



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
    }

    public static String DY_FRAGMENT = "dy_fragment";

    public static DynamicFragment newInstance(String tagId) {
        DynamicFragment fragment = new DynamicFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(DY_FRAGMENT, tagId);
        fragment.setArguments(bundle);
        return fragment;
    }

    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "dynamic_fragment_action";
    private Rec rec;
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }

    private void destroyReceiver(){
        if(localBroadcastManager != null && rec != null) {
            localBroadcastManager.unregisterReceiver(rec);
        }
    }

    @Override
    public void onReload() {
//        ToastUtils.toast("重载");
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


    /**
     * 获取下拉更新数量
     * @return
     */
    public int getUpdateCount() {
        if(dynamicBean.list != null){
            int i = 0;
            for(;i<dynamicBean.list.size();i++){
                String create_time = dynamicBean.list.get(i).create_time;
                if (!TextUtils.isEmpty(create_time)) {
                    String createTime = Utils.getCreateTime(Long.valueOf(create_time + "000"));
                    if(createTime.equals(preCreateTime)){
                        break;
                    }
                }
            }

           return i;

        }

        return 0;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }


    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();
            //result 3  发布观点回调
            if(intent.getIntExtra(RESULT,0) == 99 && isShow) {//首页双击刷新
                if(xfv_dynamic_fg != null){
                    xfv_dynamic_fg.startRefresh();
                }
            }else if(intent.getIntExtra(RESULT,0) ==88){//关注刷新
                if("-3".equals(tagId)){
                    initData();
                }

            }else if(intent.getIntExtra(RESULT,0) == 1){
                rl_load_clone = rl_load_layout;
                initData();
            }else if(intent.getIntExtra(RESULT,0) == 100){//个人中心刷新
                int follow_status = intent.getIntExtra("follow_status",0);
                String user_id = intent.getStringExtra("user_id");

                delRvFollow(follow_status,user_id);
                delFollow(follow_status,user_id);

                myAdapter.notifyDataSetChanged();
            }else if(intent.getIntExtra(RESULT,0) != 99 && isShow){
                rl_load_clone = rl_load_layout;
                initData();
                lv_dynamic_fg.setSelection(0);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }



    private  View viewRoot;
    private ImageView iv_right_title_dynamic;
    public XRefreshView xfv_dynamic_fg;
    private ListView lv_dynamic_fg;
    private FrameLayout fl_publish_point_dy;
    private FrameLayout fl_publish_gro_dy;

    private RelativeLayout rl_load_layout;
    private RelativeLayout rl_load_clone;
    private View ll_no_data;
    private TextView tv_no_data;

    private TextView tv_login_dy;

    private ImageView iv_publish_point_dy;
    private TextView tv_update_count_dy;

    private TextView tv_follow_dy;




    private String tagId = "";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tagId = (String) getArguments().getSerializable(DY_FRAGMENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(QCaApplication.getContext(), R.layout.live_fg,null);
        }
        if("0".equals(tagId)){
            setData();
        }else {
            viewRoot.postDelayed(new Runnable() {
                @Override
                public void run() {
                    setData();
                }
            }, 950);
        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // initView();
//        delCache();
      //  initData();
    }

    private boolean isFirst = true;

    public void setData(){
        if(isFirst && viewRoot != null) {
            isFirst = false;
//        initDatas();
            initReceiver();
            initView();

//           delCache();

            if("0".equals(tagId)){
                delCache();
            }else{

                initData();
            }
        }
    }

    /**
     * 处理缓存
     */
    private void delCache(){
        String string = SPUtils.getString(this.getActivity(), Constant.DYNAMIC_PAGE_CACHE+tagId);
        if(!TextUtils.isEmpty(string)){
            if("-3".equals(tagId)){
                MainActivity.sendCanleHomePoint();
                    updatePersonal();
            }



            preDynamicBean = dynamicBean;
            tv_follow_dy.setVisibility(View.GONE);
            dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
            DynamicBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

            if(dynamicBean!= null&&dynamicBean.data != null&&dynamicBean.list == null){
                if(dynamicBean.data.list != null){
                    dynamicBean.list = dynamicBean.data.list;
                    dynamicBean1.list = dynamicBean1.data.list;
                }
            }
            delData(dynamicBean,dynamicBean1);


            if(dynamicBean != null && dynamicBean.list != null){
                lv_dynamic_fg.setAdapter(myAdapter);
                xfv_dynamic_fg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xfv_dynamic_fg.startRefresh();
                    }
                },1000);

            }

        }else{
           initData();
        }
    }



    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;


    private void initView() {

        ll_net_error = viewRoot.findViewById(R.id.ll_net_error);
        tv_refresh_net = viewRoot.findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });


        tv_follow_dy = viewRoot.findViewById(R.id.tv_follow_dy);
        tv_follow_dy.setOnClickListener(this);

        ll_no_data = viewRoot.findViewById(R.id.ll_no_data);
        tv_no_data = viewRoot.findViewById(R.id.tv_no_data);

        if("-3".equals(tagId)){//关注
            ImageView iv_no_data = viewRoot.findViewById(R.id.iv_no_data);

            if(LoginUtils.isLogin()){
                tv_no_data.setText("你关注的人没有动态");
                iv_no_data.setVisibility(View.VISIBLE);
            }else {


                iv_no_data.setVisibility(View.GONE);
                tv_no_data.setText("关注的人的动态\n在这里显示");
            }
        }else{
            tv_no_data.setText("暂无信息");
        }

        tv_login_dy = viewRoot.findViewById(R.id.tv_login_dy);
        iv_publish_point_dy = viewRoot.findViewById(R.id.iv_publish_point_dy);
        iv_publish_point_dy.setOnClickListener(this);

        tv_login_dy.setOnClickListener(this);
        tv_login_dy.setOnClickListener(this);
        rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);
        rl_load_clone = rl_load_layout;

        initReceiver();

        xfv_dynamic_fg  = viewRoot.findViewById(R.id.xfv_dynamic_fg);

        lv_dynamic_fg = viewRoot.findViewById(R.id.lv_dynamic_fg);
        if(true || tagId.equals("-2")) {
            initListener();
        }

        fl_publish_point_dy = viewRoot.findViewById(R.id.fl_publish_point_dy);
        fl_publish_point_dy.setOnClickListener(this);
        fl_publish_gro_dy = viewRoot.findViewById(R.id.fl_publish_gro_dy);
        fl_publish_gro_dy.setOnClickListener(this);


        iv_right_title_dynamic = viewRoot.findViewById(R.id.iv_right_title_dynamic);
        iv_right_title_dynamic.setVisibility(View.INVISIBLE);
//        iv_right_title_dynamic.setOnClickListener(this);

        xfv_dynamic_fg.setPullRefreshEnable(true);//设置允许下拉刷新
//        xfv_dynamic_fg.setPullLoadEnable(true);//设置允许上拉加载
        //刷新动画，需要自定义CustomGifHeader，不需要修改动画的会默认头布局
//        CustomGifHeader header = new CustomGifHeader(ct);
//        xfv_home_fg.setCustomHeaderView(header);
        xfv_dynamic_fg.setMoveForHorizontal(true);
//        xfv_dynamic_fg.setSilenceLoadMore(true);
        xfv_dynamic_fg.setXRefreshViewListener(new MyListener());
        int headerViewsCount = lv_dynamic_fg.getHeaderViewsCount();
        if(headerViewsCount == 0) {
            if("3".equals(tagId)){
                View view = View.inflate(QCaApplication.getContext(),R.layout.jingcai_header,null);
                initJingCaiHeader(view);
                lv_dynamic_fg.addHeaderView(view);
            }else if("0".equals(tagId)){
                View view = View.inflate(QCaApplication.getContext(),R.layout.banner_header,null);
                initBannerHeader(view);
                lv_dynamic_fg.addHeaderView(view);
            }else if("-3".equals(tagId)){
                View view = View.inflate(QCaApplication.getContext(),R.layout.personal_header,null);
               // initBannerHeader(view);
                lv_dynamic_fg.setTag(view);
                initPersoanlHeader(view);
                lv_dynamic_fg.addHeaderView(view);
            } else {
                lv_dynamic_fg.addHeaderView(new View(QCaApplication.getContext()));
            }
        }
        lv_dynamic_fg.setAdapter(myAdapter);
        lv_dynamic_fg.setOnItemClickListener(this);

        tv_update_count_dy = viewRoot.findViewById(R.id.tv_update_count_dy);


        initAnimation();

        if(!tagId.equals("-2")){
            setAudio();
        }

    }


    /**
     * 初始化关注头
     * @param view
     */
    private LinearLayout ll_my_personal;
    private TextView tv_more_p;
    private RecyclerView rv_personal_header;
    private void initPersoanlHeader(View view) {
        tv_more_p = view.findViewById(R.id.tv_more_p);
        tv_more_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyFollowerListActivity.start(DynamicFragment.this.getActivity());
            }
        });

        rv_personal_header = view.findViewById(R.id.rv_personal_header);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_personal_header.setLayoutManager(linearLayoutManager);
        rv_personal_header.setHasFixedSize(true);
        ll_my_personal = view.findViewById(R.id.ll_my_personal);

    }


    /**
     * 初始化推荐 头部banner
     * @param view
     */
    private Banner banner;
    private ImageView iv_bg_banner;
    private void initBannerHeader(View view) {
        banner = view.findViewById(R.id.banner);
        banner.setImageLoader(new ImageLoaderType());
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);



        iv_bg_banner = view.findViewById(R.id.iv_bg_banner);

        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = (int) (DisplayUtil.getScreenWidth(this.getActivity())/375.0*152);
        banner.setLayoutParams(layoutParams);
        int anInt = SPUtils.getInt(this.getContext(), Constant.Banner_Interval);
        if(anInt == 0)anInt = 4;
        banner.setDelayTime(1000*anInt);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                try {
                    String topic_id = strings.get(position).split(",")[1];
//                WebViewActivtiy.start(HomeNewFragment.this.getActivity(),Constant.BASE_URL+"#/topic/"+topic_id,"话题");
                    String type = strings.get(position).split(",")[2];

                    if ("1".equals(type)) {
                        TopicDetailActivity.start(DynamicFragment.this.getActivity(), topic_id);
                    } else if ("2".equals(type)) {
                        WebViewActivtiy.start(DynamicFragment.this.getActivity(), topic_id, "球咖", true);
                    } else if ("3".equals(type)) {//登录
                        if (LoginUtils.isUnLogin()) {
                            LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(), "");
                            return;
                        }

                        WebViewActivtiy.start(DynamicFragment.this.getActivity(), topic_id, "球咖", true);
                    }
                }catch (Exception e){

                }

            }
        });

        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            int pos;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pos = position;
            }

            @Override
            public void onPageSelected(int position) {
                try {
                    ImageView view1 = (ImageView) banner.imageViews.get(position);
                    String url = strings.get(pos).split(",")[0];
                    view1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    PictureUtils.show(url, view1);
                }catch (Exception e){

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }


    private List<String> strings = new ArrayList<>();
    private boolean isSetBanner;
    private void updateBanner() {
        if(isSetBanner == false || page == 0) {
            if (banner == null) {
                isSetBanner = false;
                return;
            }

            isSetBanner = true;

            banner.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (dynamicBean != null && dynamicBean.data != null && dynamicBean.data.bannerList != null && dynamicBean.data.bannerList.size() > 0) {
                        banner.setVisibility(View.VISIBLE);
                        int size = dynamicBean.data.bannerList.size();
                        //            id_banner.setBannerAdapter(new MyBannerAdapter(homeDataBean.data.bannerList));
                        banner.setVisibility(View.VISIBLE);
                        iv_bg_banner.setVisibility(View.VISIBLE);
                        strings.clear();
                        List<String> titles = new ArrayList<>();
                        for (int i = 0; i < dynamicBean.data.bannerList.size(); i++) {

                            int type = dynamicBean.data.bannerList.get(i).type;
                            if (type == 1) {
                                strings.add(dynamicBean.data.bannerList.get(i).banner + "," + dynamicBean.data.bannerList.get(i).topic_id + "," + dynamicBean.data.bannerList.get(i).type);
                                titles.add(dynamicBean.data.bannerList.get(i).title);
                            } else if (type == 2 || type == 3) {
                                strings.add(dynamicBean.data.bannerList.get(i).banner + "," + dynamicBean.data.bannerList.get(i).url + "," + dynamicBean.data.bannerList.get(i).type);
                                titles.add(dynamicBean.data.bannerList.get(i).title);
                            }


                        }

                        banner.setImages(strings);
                        if (titles.size() > 0) {
                            banner.setBannerTitles(titles);
                        }
                        banner.start();
                    } else {
                        banner.setVisibility(View.GONE);
                        iv_bg_banner.setVisibility(View.GONE);
                    }


                }
            }, 550);

        }
    }

    /**
     * 更新关注推荐人横向列表
     */
    private void updatePersonal() {
               rv_personal_header.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(dynamicBean != null && dynamicBean.data!= null && dynamicBean.data.friends != null && dynamicBean.data.friends.size()>0) {

                        rv_personal_header.setAdapter(new RyAdapter());
                        ll_my_personal.setVisibility(View.VISIBLE);
                    }else{
                        ll_my_personal.setVisibility(View.GONE);
                    }
                }
            },200);
    }



    class RyAdapter extends RecyclerView.Adapter<RvHolder> {


        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RvHolder rvHolder = new RvHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.type5_cy_item_layout_new, parent,
                    false));
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(final RvHolder holder, final int position) {
            final DynamicBean.DataBean.UserBean userBean = dynamicBean.data.friends.get(position);

            PictureUtils.showCircle(userBean.headurl,holder.iv_header_item);
            holder.tv_name_item.setText(userBean.nickname);
            holder.tv_tag_item.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(userBean.verify_type)&&Integer.valueOf(userBean.verify_type)>0){
                holder.iv_vip_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_vip_item.setVisibility(View.GONE);
            }

            holder.tv_follow_item.setVisibility(View.GONE);

            holder.rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(userBean);
                    PersonalDetailActivity.start(DynamicFragment.this,userBean.user_id,position,2,json);
//                    PersonalDetailActivity.start(HomeNewFragment.this.getActivity(),listBean.user_id);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(dynamicBean != null && dynamicBean.data != null && dynamicBean.data.friends != null){
                int size = dynamicBean.data.friends.size();
                return size;
            }
            return 0;
        }
    }


    /**
     * 初始化竞猜头部
     * @param view
     */

    private FrameLayout fl_0_jingcai_header;
    private FrameLayout fl_1_jingcai_header;
    private FrameLayout fl_2_jingcai_header;
    private FrameLayout fl_3_jingcai_header;
    private FrameLayout fl_4_jingcai_header;
    private FrameLayout fl_5_jingcai_header;
    private ImageView iv_top_header;
    private void initJingCaiHeader(View view) {
        fl_0_jingcai_header = view.findViewById(R.id.fl_0_jingcai_header);
        fl_1_jingcai_header = view.findViewById(R.id.fl_1_jingcai_header);
        fl_2_jingcai_header = view.findViewById(R.id.fl_2_jingcai_header);
        fl_3_jingcai_header = view.findViewById(R.id.fl_3_jingcai_header);
        fl_4_jingcai_header = view.findViewById(R.id.fl_4_jingcai_header);
        fl_5_jingcai_header = view.findViewById(R.id.fl_5_jingcai_header);

        fl_0_jingcai_header.setOnClickListener(this);
        fl_1_jingcai_header.setOnClickListener(this);
        fl_2_jingcai_header.setOnClickListener(this);
        fl_3_jingcai_header.setOnClickListener(this);
        fl_4_jingcai_header.setOnClickListener(this);
        fl_5_jingcai_header.setOnClickListener(this);


        iv_top_header = view.findViewById(R.id.iv_top_header);//底部新增图片入口
        ViewGroup.LayoutParams layoutParams = iv_top_header.getLayoutParams();
        layoutParams.height = (int) (DisplayUtil.getScreenWidth(this.getActivity())*0.32);
        iv_top_header.setLayoutParams(layoutParams);

        iv_top_header.setOnClickListener(this);



        String string = SPUtils.getString(QCaApplication.getContext(), Constant.BANNER_IMG);
        if (TextUtils.isEmpty(string)) {
            iv_top_header.setVisibility(View.GONE);
            requestMeta();
        } else {
            PictureUtils.showRounded(string, iv_top_header);
            iv_top_header.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 请求初始化数据
     */
    private  MainMetaBean mainMetaBean;
    private void requestMeta() {

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action", "meta");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                mainMetaBean = JsonParseUtils.parseJsonClass(string, MainMetaBean.class);
                if (mainMetaBean != null) {

                    if (mainMetaBean.bannerList != null && mainMetaBean.bannerList.size() > 0) {
                        MainMetaBean.BannerBean bannerBean = mainMetaBean.bannerList.get(0);
                        SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_IMG, bannerBean.img);
                        SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_URL, bannerBean.url);
                        if(iv_top_header != null) {
                            iv_top_header.setVisibility(View.VISIBLE);
                            PictureUtils.showRounded(bannerBean.img, iv_top_header);
                        }
                    } else {

                        SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_IMG, "");
                        SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_URL, "");
                        if(iv_top_header != null) {
                            iv_top_header.setVisibility(View.GONE);
                        }
                    }

                } else {
                    SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_IMG, "");
                    SPUtils.putString(QCaApplication.getContext(), Constant.BANNER_URL, "");
                    if(iv_top_header != null) {
                        iv_top_header.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });
    }
    /**
     * 初始化动画
     */
    private ObjectAnimator animator1;


    private int dY = DisplayUtil.dip2px(QCaApplication.getContext(),25.5f);
    private void initAnimation() {


         animator1=ObjectAnimator.ofFloat(tv_update_count_dy, "translationY", -dY,0,0,0,-dY);

         animator1.setDuration(2000);

    }


    /**
     * 设置顶部黄色提示框显示内容
     */
    private void setTopTitleShow() {

        if(!isShow)return;

        boolean connected = NetworkUtils.isConnected(QCaApplication.getContext());
        if(!connected){
            tv_update_count_dy.setText("网络状态不佳");
        }else {
            if ("-3".equals(tagId)) {
                int count = getUpdateCount();

                if (count == 0) {
                    tv_update_count_dy.setText("暂无更新,看看推荐吧");
                } else {
                    tv_update_count_dy.setText("球咖为你更新" + count + "条动态");
                }

            } else {
                if (refreshCount != 0) {
                    if (dynamicBean.list != null) {
                        tv_update_count_dy.setText("球咖为你更新" + dynamicBean.list.size() + "条动态");
                    }
                } else {
                    if ("0".equals(tagId)) {
                        tv_update_count_dy.setText("暂无更新");
                    } else {
                        tv_update_count_dy.setText("暂无更新,看看推荐吧");
                    }
                }
            }
        }
        tv_update_count_dy.postDelayed(new Runnable() {
            @Override
            public void run() {
                animator1.start();
            }
        }, 1000);
    }


    public void initData() {

         rl_load_clone = rl_load_layout;
         page = 0;
         loadMore = 0;
         requestNetData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_right_title_dynamic://#/mySubcription
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/mySubcription","我的订阅");
                break;
            case R.id.fl_publish_point_dy://发布观点 //#/topicViewRelease/0,false,false,%E4%B8%BB%E9%98%9F,%E5%AE%A2%E9%98%9F
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                PublishPointActivity.start(this.getActivity(),"0");
//                String uId = SPUtils.getString(this.getContext(), Constant.USER_ID);
//                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/topicViewRelease/0,false,false,主队,客队","发布观点");
                break;
            case R.id.fl_publish_gro_dy://发布推荐
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
//                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/selectMatch/1","发布推荐");
                PublishGroomActivity.start(this.getActivity());
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
                    DynamicBean.ListBean bean = dynamicBean.list.get(clickPosition);
                    bean.recommendation.buy = 1;
                    myAdapter.notifyDataSetChanged();
                    goDetail(bean);
                }else{//充值

                    Intent intent = new Intent(DynamicFragment.this.getActivity(), CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.tv_login_dy:
                if(LoginUtils.isUnLogin()){
                    SPUtils.putInt(QCaApplication.getContext(),Constant.HOME_SELECT,-1);
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                break;
            case R.id.iv_publish_point_dy:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                PublishPointActivity.start(this.getActivity(),"0");
                break;

            case R.id.fl_0_jingcai_header:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                PublishGroomActivity.start(this.getActivity());
                break;
            case R.id.fl_1_jingcai_header:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                MyGroomListActivity.start(this.getActivity());
                break;
            case R.id.fl_2_jingcai_header:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }


                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/consumeList","已购推荐");
                break;
            case R.id.fl_3_jingcai_header:
                InforPostActivity.start(this.getActivity());
                break;
            case R.id.fl_4_jingcai_header:
                IntelligentBallSelectionActivity.start(this.getActivity());
                break;
            case R.id.fl_5_jingcai_header://红人榜
//                GroomOfHomeActivity.start(this.getActivity());
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/rank","红人榜");
                break;
            case R.id.iv_top_header:
//                GuessingCompetitionActivity.start(this.getActivity());
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                WebViewActivtiy.start(getActivity(),SPUtils.getString(QCaApplication.getContext(),Constant.BANNER_URL),"球咖",true);
                break;
            case R.id.tv_follow_dy://一键关注

                NotificationsUtils.checkNotificationAndStartSetting(this.getActivity(),lv_dynamic_fg);
                submitFollow();
                break;

        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(iv_top_header != null) {
            String string = SPUtils.getString(QCaApplication.getContext(), Constant.BANNER_IMG);
            if (TextUtils.isEmpty(string)) {
                iv_top_header.setVisibility(View.GONE);
            } else {
                PictureUtils.showRounded(string, iv_top_header);
                iv_top_header.setVisibility(View.VISIBLE);
            }
        }


    }

    /**
     * 提交一键关注数据
     */
    private int submitCount;
    private Set<String> checkId = new HashSet<>();
    private void submitFollow() {
       //https://app.mayisports.com/php/api.php?action=followers&type=add&user_id=189187
        for(int i = 0;i<dynamicBean.recommendation_accountList.size();i++){
            DynamicBean.DataBean.UserBean userBean = dynamicBean.recommendation_accountList.get(i);
            if(userBean.isCheck){
                  submitCount ++;
                  requestFollowById(userBean.user_id);
            }
        }

        if(submitCount == 0){
            ToastUtils.toast("请至少选择一位");
        }
    }


    /**
     * 去往推荐详情页
     */
    private int clickPosition;
    private void goDetail(DynamicBean.ListBean bean){
        Intent intent = new Intent(this.getActivity(), HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
        intent.putExtra("betId",bean.match.betId);
        startActivity(intent);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i == 0)return;
        i = i-1;

        if(dynamicBean.recommendation_accountList != null){
            DynamicBean.DataBean.UserBean userBean = dynamicBean.recommendation_accountList.get(i);
            if (LoginUtils.isUnLogin()) {
                LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(), HomeNewFragment.ACTION);
                return;
            }

            if (userBean.isCheck) {
                userBean.isCheck = false;
                checkId.remove(userBean.user_id);
            } else {
                userBean.isCheck = true;
                checkId.add(userBean.user_id);
            }
            myAdapterGroom.notifyDataSetChanged();
            if(checkId.size() == 0){
                tv_follow_dy.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_66));
                tv_follow_dy.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.select_person_uncheck));
            }else{
                tv_follow_dy.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));
                tv_follow_dy.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.select_score_bottom));

            }

        }else {

            DynamicBean.ListBean bean = dynamicBean.list.get(i);
            if (bean.type == 1) {//推荐单
                delType2Click(bean, i);
            } else if (bean.type == 3 || bean.type1 == 8 || bean.type == 7) {//话题
                if(bean.comment == null)return;
                if(bean.type == 3 && isShootVideo(bean)){
                    toShootVideoPlayer(bean,i,false);
                }else {
                    toJsonCommentBean(bean);
                    ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, i);
                }
            } else if (bean.type == 6) {
                TopicDetailActivity.start(DynamicFragment.this.getActivity(), bean.topic.id);
            }else if(bean.type == 4){//评论&回复类型
                ShareComentsDetailsActivity.startForResult(2,DynamicFragment.this,ComentsDetailsActivity.SHARE_TYPE,bean.comment.comment_id,bean,i);
            }

        }
    }


    private void toDetail(int i){
        DynamicBean.ListBean bean = dynamicBean.list.get(i);
        if (bean.type == 1) {//推荐单
            delType2Click(bean, i);
        } else if (bean.type == 3 || bean.type1 == 8 || bean.type == 7) {//话题

            if(bean.comment == null)return;
            if(bean.type == 3 && isShootVideo(bean)){
                toShootVideoPlayer(bean,i,false);
            }else {
                toJsonCommentBean(bean);
                ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, i);
            }
        } else if (bean.type == 6) {
            TopicDetailActivity.start(DynamicFragment.this.getActivity(), bean.topic.id);
        }
    }


    /**
     * 处理推荐单类型 点击事件
     * @param bean
     * @param i
     */
    private void delType2Click(DynamicBean.ListBean bean,int i) {
        String price = bean.recommendation.price;
        String nickname = bean.user.nickname;
        if(!Constant.isSee && LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

//        bean.count = "0";
        myAdapter.notifyDataSetChanged();
        if(Constant.isSee || bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0) || "COMPLETE".equals(bean.match.status)|| SPUtils.getString(this.getContext(),Constant.USER_ID).equals(bean.user.user_id)){//已购买 //或者免费
            Intent intent = new Intent(this.getActivity(), HomeItemDetailActivity.class);
            intent.putExtra("userId",bean.user.user_id);
            if("-3".equals(tagId)){
                intent.putExtra("betId", bean.match.betId);
            }else {
                intent.putExtra("betId", bean.recommendation.betId);
            }
            startActivity(intent);
        }else{

//            new ToastPricePopuWindow(this.getActivity(),this,)
            clickPosition = i;
            if("-3".equals(tagId)){
                requestToast(bean.user.user_id,bean.match.betId);
            }else {
                requestToast(bean.user.user_id,bean.recommendation.betId);
            }

        }
    }

    /**
     * 请求是否购买
     */
    private int type;//1确定，2充值
    private ToastPricePopuWindow toastPricePopuWindow;
    private void requestToast(String userId,String betId) {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","recommendation_Detail");
        params.put("type","req");
        params.put("betId",betId);
        params.put("user_id",userId);
        params.put("from",3);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
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
                if(!DynamicFragment.this.getActivity().isFinishing()) {
                    toastPricePopuWindow = new ToastPricePopuWindow(DynamicFragment.this.getActivity(), DynamicFragment.this, title, type);
                    toastPricePopuWindow.showAtLocation(lv_dynamic_fg, Gravity.CENTER, 0, 0);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    /**
     * 加载刷新监听
     */
    private int page;
    class MyListener extends XRefreshView.SimpleXRefreshListener {


        @Override
        public void onRefresh(boolean isPullDown) {
            rl_load_clone = null;
            page = 0;
            loadMore ++;
            requestNetData();
            JCVideoPlayer.releaseAllVideos();
        }


    }

    private DynamicBean dynamicBean;
    private int pre_page = 1;
    private String preCreateTime;
    private DynamicBean preDynamicBean;
    private int loadMore = 0;
    private void requestNetData() {
        // //http://20180207.dldemo.applinzi.com/php/api.php?action=sys_feed&start=0&type=0

        //http://app.mayisports.com/php/api.php?action=followers&type=friends_feed_list&start=0
        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        if("-3".equals(tagId)){//关注

            //action=followers&type=friends_feed_list&start=0&loadMore=0
            params.put("action","followers");
            params.put("type","friends_feed_list");
            params.put("start",page);

        }else{
            params.put("action","sys_feed");
            params.put("type",tagId);
            params.put("start",page);
        }
        if(page>0){
            params.put("loadMore",0);
        }else{
            params.put("loadMore",loadMore);
        }


        if(ll_net_error != null)  ll_net_error.setVisibility(View.GONE);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {
                if(xfv_dynamic_fg != null) {
                    xfv_dynamic_fg.stopRefresh();
                    xfv_dynamic_fg.stopLoadMore();
                }

                if(ll_net_error != null)  ll_net_error.setVisibility(View.GONE);

                updateBanner();
                if(page == 0) {





                    if("-3".equals(tagId)){
                        MainActivity.sendCanleHomePoint();
                        updatePersonal();
                    }

                    if("0".equals(tagId)){
                  //      updateBanner();
                    }







                    preDynamicBean = dynamicBean;
                    tv_follow_dy.setVisibility(View.GONE);
                    dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
                    DynamicBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                    if(dynamicBean!= null&&dynamicBean.data != null&&dynamicBean.list == null){
                         if(dynamicBean.data.list != null){
                             dynamicBean.list = dynamicBean.data.list;
                             dynamicBean1.list = dynamicBean1.data.list;
                         }
                    }

//                    dynamicBean.list = null;
//                    dynamicBean1.list = null;


                    delData(dynamicBean,dynamicBean1);



                    if("0".equals(tagId) || "-3".equals(tagId)) {
                        SPUtils.putString(QCaApplication.getContext(), Constant.DYNAMIC_PAGE_CACHE + tagId, string);
                    }
                    if(dynamicBean != null && dynamicBean.list != null){
                        if(dynamicBean.list.size() == 0){
                            if(ll_no_data != null) xfv_dynamic_fg.setPullLoadEnable(false);
                            if(ll_no_data != null) ll_no_data.setVisibility(View.VISIBLE);
                            preCreateTime = "";
                        }else{
                            if(("-3".equals(tagId) && LoginUtils.isLogin())) {
                                DynamicBean.ListBean bean = dynamicBean.list.get(0);
                                if (!TextUtils.isEmpty(bean.create_time)) {
                                    String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                                     preCreateTime = createTime;
                                } else {
                                     preCreateTime = "";
                                }
                            }else{
                                delRefresh(dynamicBean,preDynamicBean);
                            }
//                            xfv_dynamic_fg.setPullLoadEnable(true);
                            if(ll_no_data != null)ll_no_data.setVisibility(View.GONE);
                        }

                        pre_page = 1;
                        setTopTitleShow();
                        lv_dynamic_fg.setAdapter(myAdapter);

                    }else{
                        if(ll_no_data != null)xfv_dynamic_fg.setPullLoadEnable(false);
                        if(ll_no_data != null)ll_no_data.setVisibility(View.VISIBLE);
                        preCreateTime = "";

                        if("-3".equals(tagId) && LoginUtils.isLogin()) {
                            showGroomPeople();
                        }else{
                            pre_page = 1;
                            setTopTitleShow();
                            lv_dynamic_fg.setAdapter(myAdapter);
                        }

                    }


                }else{
                    try {
                        DynamicBean newBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
                        DynamicBean newBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                        if(newBean!= null&&newBean.data != null&&newBean.list == null){
                            if(newBean.data.list != null){
                                newBean.list = newBean.data.list;
                                newBean1.list = newBean1.data.list;
                            }
                        }
                        delData(newBean,newBean1);
                        if(newBean.list.size()>0) {
                            dynamicBean.list.addAll(newBean.list);
                        }else{
                            pre_page = 0;
                        }

                    }catch (Exception e){
//                        ToastUtils.toast("没有更多了。。。");
                        pre_page = 0;
                    }
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

                if(xfv_dynamic_fg != null) {
                    xfv_dynamic_fg.stopRefresh();
                    xfv_dynamic_fg.stopLoadMore();
                }
                if( "0".equals(tagId) || "-3".equals(tagId)){
                    if(page == 0){

                        if(dynamicBean != null && dynamicBean.data != null){
                            if(!NetworkUtils.isConnected(QCaApplication.getContext())){
                                setTopTitleShow();
                            }else{
                                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                            }
                        }else {
                            if (ll_net_error != null) ll_net_error.setVisibility(View.VISIBLE);
                            return;
                        }
                    }

                }else{
                    if(page == 0){
                        if(dynamicBean != null && dynamicBean.data != null){
                            if(!NetworkUtils.isConnected(QCaApplication.getContext())){
                                setTopTitleShow();
//                                if("-3".equals(tagId)){
//                                    if(ll_my_personal != null)ll_my_personal.setVisibility(View.GONE);
//                                }
                            }else{
                                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                            }
                        }else {
                            if (ll_net_error != null) ll_net_error.setVisibility(View.VISIBLE);

                            if("-3".equals(tagId)){
                                if(ll_my_personal != null)ll_my_personal.setVisibility(View.GONE);
                            }
                            return;
                        }
                    }

                }


//                if(page == 0){
//                    if(!NetworkUtils.isConnected(QCaApplication.getContext())){
//                        setTopTitleShow();
//                    }
//                }else{
//                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
//                }


            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_clone = rl_load_layout;
                if(LoginUtils.isUnLogin()){
                    if(tv_login_dy != null)tv_login_dy.setVisibility(View.VISIBLE);
                }else{
                    if(tv_login_dy != null)tv_login_dy.setVisibility(View.GONE);
                }

            }
        });
    }


    /**
     * 更新刷新数量
     * @param dynamicBean
     * @param preDynamicBean
     */
    private int refreshCount;
    private void delRefresh(DynamicBean dynamicBean, DynamicBean preDynamicBean) {
        try {
            if (preDynamicBean == null || "0".equals(tagId)) {
                refreshCount = dynamicBean.list.size();
            } else {

                DynamicBean.ListBean.CommentBean comment = dynamicBean.list.get(0).comment;
                DynamicBean.ListBean.CommentBean comment1 = preDynamicBean.list.get(0).comment;
                DynamicBean.ListBean.RecommendationBean recommendation = dynamicBean.list.get(0).recommendation;
                DynamicBean.ListBean.RecommendationBean recommendation1 = preDynamicBean.list.get(0).recommendation;
                DynamicBean.ListBean.TopicBean topic = dynamicBean.list.get(0).topic;
                DynamicBean.ListBean.TopicBean topic1 = preDynamicBean.list.get(0).topic;

                if (comment != null && comment1 != null) {
                    if (comment.comment_id.equals(comment1.comment_id)) {
                        refreshCount = 0;
                    }else{
                        refreshCount = dynamicBean.list.size();
                    }
                } else if(recommendation != null && recommendation1 != null){
                    if (recommendation.id.equals(recommendation1.id)) {
                        refreshCount = 0;
                    }else{
                        refreshCount = dynamicBean.list.size();
                    }
                }else if(topic != null && topic1 != null){
                    if (topic.id.equals(topic1.id)) {
                        refreshCount = 0;
                    }else{
                        refreshCount = dynamicBean.list.size();
                    }
                } else {
                    refreshCount = dynamicBean.list.size();
                }


            }
        }catch (Exception e){
            refreshCount = 0;
        }
    }


    /**
     * 显示推荐人
     */
    private MyAdapterGroom myAdapterGroom;
    private void showGroomPeople() {
          if(dynamicBean != null && dynamicBean.recommendation_accountList != null){
             tv_follow_dy.setVisibility(View.VISIBLE);
              ll_no_data.setVisibility(View.GONE);
              myAdapterGroom = new MyAdapterGroom();
              if(tv_follow_dy != null) {
                  tv_follow_dy.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));
                  tv_follow_dy.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.select_score_bottom));
              }
              lv_dynamic_fg.setAdapter(myAdapterGroom);
          }
    }


    class MyAdapterGroom extends BaseAdapter{

        @Override
        public int getCount() {
            if(dynamicBean != null && dynamicBean.recommendation_accountList != null){
                return dynamicBean.recommendation_accountList.size()+1;
            }
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

            if(position == dynamicBean.recommendation_accountList.size()){

                convertView = View.inflate(QCaApplication.getContext(), R.layout.groom_personal_empty, null);
                convertView.setTag(null);

            }else {

                final MyHolderGroom myHolder;
                if (convertView == null || convertView.getTag() == null) {
                    myHolder = new MyHolderGroom();
                    convertView = View.inflate(QCaApplication.getContext(), R.layout.groom_personal_item_tag, null);
                    myHolder.iv_header_item = convertView.findViewById(R.id.iv_header_item);
                    myHolder.iv_vip_item = convertView.findViewById(R.id.iv_vip_item);
                    myHolder.tv_name_item = convertView.findViewById(R.id.tv_name_item);
                    myHolder.tv_reason_item = convertView.findViewById(R.id.tv_reason_item);
                    myHolder.iv_follow_item = convertView.findViewById(R.id.iv_follow_item);
                    myHolder.v_top_line = convertView.findViewById(R.id.v_top_line);
                    convertView.setTag(myHolder);
                } else {
                    myHolder = (MyHolderGroom) convertView.getTag();
                }




                final DynamicBean.DataBean.UserBean dataBean = dynamicBean.recommendation_accountList.get(position);

                PictureUtils.showCircle(dataBean.headurl, myHolder.iv_header_item);

                if (!TextUtils.isEmpty(dataBean.verify_type) && Integer.valueOf(dataBean.verify_type) > 0) {
                    myHolder.iv_vip_item.setVisibility(View.VISIBLE);
                } else {
                    myHolder.iv_vip_item.setVisibility(View.GONE);
                }


                if(position == 0){
                    myHolder.v_top_line.setVisibility(View.GONE);
                }else{
                    myHolder.v_top_line.setVisibility(View.VISIBLE);
                }


                if (dataBean.isCheck) {//关注
                    myHolder.iv_follow_item.setTag(1);
                    myHolder.iv_follow_item.setImageResource(R.drawable.choose_check);
//                myHolder.iv_follow_item.setTextColor(Color.parseColor("#e8e8e8"));
//                myHolder.iv_follow_item.setText("已关注");
                    checkId.add(dataBean.user_id);
                } else {
                    myHolder.iv_follow_item.setTag(0);
                    myHolder.iv_follow_item.setImageResource(R.drawable.choose_uncheck);
//                myHolder.iv_follow_item.setTextColor(Color.parseColor("#282828"));
//                myHolder.iv_follow_item.setText(" 关注 ");
                    checkId.remove(dataBean.user_id);
                }

                myHolder.tv_name_item.setText(dataBean.nickname);
                if (TextUtils.isEmpty(dataBean.verify_reason)) {
                    myHolder.tv_reason_item.setText("");
                } else {
                    myHolder.tv_reason_item.setText(dataBean.verify_reason);
                }



                myHolder.iv_header_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dataBean.verify_reason="";
                        String json = new Gson().toJson(dataBean);
                        PersonalDetailActivity.start(DynamicFragment.this.getActivity(),dataBean.user_id,json);

                    }
                });

                myHolder.tv_name_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dataBean.verify_reason="";
                        String json = new Gson().toJson(dataBean);
                        PersonalDetailActivity.start(DynamicFragment.this.getActivity(),dataBean.user_id,json);

                    }
                });

            }
            return convertView;
        }
    }

    static class MyHolderGroom {

        public ImageView iv_header_item;
        public ImageView iv_vip_item;
        public TextView tv_name_item;
        public TextView tv_reason_item;
        public ImageView iv_follow_item;
        public View v_top_line;

    }


    /**
     * 处理数据  type 1 显示
     * @param homeDataBean
     * @param newBean
     */
    private void delData(DynamicBean homeDataBean, DynamicBean newBean) {
        if(homeDataBean != null && newBean != null) {
            if (homeDataBean.list != null && newBean.list != null) {
                homeDataBean.list.clear();
                for (int i = 0; i < newBean.list.size(); i++) {

                    DynamicBean.ListBean beanX = newBean.list.get(i);
                    if (beanX.type == 1 || beanX.type == 3 || beanX.type == 6 || beanX.type == 5 || beanX.type1 == 8 || beanX.type == 7 ||beanX.type == 4) {//1推荐单  3 话题 6话题  5可能感兴趣的人   8头条   4评论&转发类型
//                        if(beanX.type == 4){
//
//                            if(beanX.comment != null){
//                                homeDataBean.list.add(beanX);
//                            }
//                        }else

                            if (!(beanX.type == 3 && isHeadline(beanX))) {
                            homeDataBean.list.add(beanX);
                        } else if (beanX.comment != null) {
                            homeDataBean.list.add(beanX);
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否是头条类型
     * @param bean
     * @return
     */
    private boolean isHeadline(DynamicBean.ListBean bean){
        boolean b = !TextUtils.isEmpty(bean.subtype) && Integer.valueOf(bean.subtype) == 8;
        return b;
    }

    /**
     * 是否为短视频类型
     * @param bean
     * @return
     */
    private boolean isShootVideo(DynamicBean.ListBean bean){
        boolean b = !TextUtils.isEmpty(bean.subtype) && Integer.valueOf(bean.subtype) == 7;
        return b;
    }

    private MyAdapter myAdapter = new MyAdapter();
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(dynamicBean != null && dynamicBean.list!= null){
                return dynamicBean.list.size()+1;
            }
            return 0;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {

            if(dynamicBean == null || dynamicBean.list == null)return view;

            if(i == dynamicBean.list.size()){
                view = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = view.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                return view;
            }


            final DynamicBean.ListBean bean = dynamicBean.list.get(i);

             MyHolder myHolder = null;//默认类型

                if (bean.type == 5) {
                    view = View.inflate(QCaApplication.getContext(), R.layout.recy_type5_layout_item, null);
                    view.setTag(null);

                    RecyclerView rv_item = view.findViewById(R.id.rv_item);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(QCaApplication.getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                    rv_item.setLayoutManager(linearLayoutManager);
                    rv_item.setHasFixedSize(true);

                    LinearLayout ll_click_ry_item = view.findViewById(R.id.ll_click_ry_item);
                    ll_click_ry_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            GroomPersonalActivity.start(DynamicFragment.this.getActivity());
                        }
                    });

                    rv_item.setAdapter(new MyRvType5Adapter(bean.list));
                } else {

                    if (view == null || view.getTag() == null) {
                        myHolder = new MyHolder();
                        view = View.inflate(DynamicFragment.this.getActivity(), R.layout.dynamic_item, null);
                        myHolder.ll_top_click = view.findViewById(R.id.ll_top_click);
                        myHolder.tv_buy = view.findViewById(R.id.tv_buy);
                        myHolder.iv_head_dynamic_item = view.findViewById(R.id.iv_head_dynamic_item);
                        myHolder.tv_name_dynamic_item = view.findViewById(R.id.tv_name_dynamic_item);
                        myHolder.iv_red_point_header = view.findViewById(R.id.iv_red_point_header);
                        myHolder.ll_top_tag_container = view.findViewById(R.id.ll_top_tag_container);
                        myHolder.tv_time_dynamic_item = view.findViewById(R.id.tv_time_dynamic_item);
                        myHolder.tv_content_dynamic_item = view.findViewById(R.id.tv_content_dynamic_item);
                        myHolder.ll_groom_dynamic_item = view.findViewById(R.id.ll_groom_dynamic_item);
                        myHolder.tv_team_name_dynamic_item = view.findViewById(R.id.tv_team_name_dynamic_item);
                        myHolder.tv_bottom_dynamic_item = view.findViewById(R.id.tv_bottom_dynamic_item);
                        myHolder.tv_type_dynamic_item = view.findViewById(R.id.tv_type_dynamic_item);
                        myHolder.tv_price_dynamic_item = view.findViewById(R.id.tv_price_dynamic_item);
                        myHolder.ll_img_container_dynamic_item = view.findViewById(R.id.ll_img_container_dynamic_item);
                        myHolder.rl_topic_dynamic_item = view.findViewById(R.id.rl_topic_dynamic_item);
                        myHolder.iv_img_left = view.findViewById(R.id.iv_img_left);
                        myHolder.tv_bottom_title_topic_item = view.findViewById(R.id.tv_bottom_title_topic_item);
                        myHolder.tv_view_count_topic_item = view.findViewById(R.id.tv_view_count_topic_item);
                        myHolder.ll_bottom_dynamic_item = view.findViewById(R.id.ll_bottom_dynamic_item);
                        myHolder.tv_thumbup_topic_item = view.findViewById(R.id.tv_thumbup_topic_item);
                        myHolder.tv_comments_topic_item = view.findViewById(R.id.tv_comments_topic_item);
                        myHolder.ll_price = view.findViewById(R.id.ll_price);
                        myHolder.tv_follow_dynamic_item = view.findViewById(R.id.tv_follow_dynamic_item);
                        myHolder.iv_vip_header = view.findViewById(R.id.iv_vip_header);
                        myHolder.tv_right_item = view.findViewById(R.id.tv_right_item);
                        myHolder.rl_video_item = view.findViewById(R.id.rl_video_item);
                        myHolder.iv_video_item = view.findViewById(R.id.iv_video_item);
                        myHolder.rl_bg_content_item = view.findViewById(R.id.rl_bg_content_item);
                        myHolder.tv_content_bg_dynamic_item = view.findViewById(R.id.tv_content_bg_dynamic_item);
                        myHolder.iv_content_bg_item = view.findViewById(R.id.iv_content_bg_item);
                        myHolder.gv_dynamic_item = view.findViewById(R.id.gv_dynamic_item);
                        myHolder.rl_bg_long_content_item = view.findViewById(R.id.rl_bg_long_content_item);
                        myHolder.tv_content_long_content = view.findViewById(R.id.tv_content_long_content);
                        myHolder.tv_title_long_content = view.findViewById(R.id.tv_title_long_content);
                        myHolder.tv_share_topic_item = view.findViewById(R.id.tv_share_topic_item);

                        myHolder.fl_topic_img_item = view.findViewById(R.id.fl_topic_img_item);
                        myHolder.iv_topic_img = view.findViewById(R.id.iv_topic_img);
                        myHolder.tv_title_topic_img = view.findViewById(R.id.tv_title_topic_img);
                        myHolder.tv_viewcount_topic_img = view.findViewById(R.id.tv_viewcount_topic_img);
                        myHolder.ll_bottom_item = view.findViewById(R.id.ll_bottom_item);

                        myHolder.tv_content_bottom = view.findViewById(R.id.tv_content_bottom);

                        myHolder.ll_no_pan = view.findViewById(R.id.ll_no_pan);
                        myHolder.tv_bottom_home_item = view.findViewById(R.id.tv_bottom_home_item);
                        myHolder.iv_buy = view.findViewById(R.id.iv_buy);

//                        ViewGroup.LayoutParams layoutParams = myHolder.fl_topic_img_item.getLayoutParams();
//                        layoutParams.height = (int) (DisplayUtil.getScreenWidth(DynamicFragment.this.getActivity()) * 0.32);
//                        myHolder.fl_topic_img_item.setLayoutParams(layoutParams);

                        setVideoHolder(myHolder, view);

                        setHeadLineHolder(myHolder,view);

                        myHolder.v_top_line_big = view.findViewById(R.id.v_top_line_big);
                        myHolder.v_top_line_small = view.findViewById(R.id.v_top_line_small);

                        view.setTag(myHolder);



                        myHolder.jc_type_item = view.findViewById(R.id.jc_type_item);
                        myHolder.jc_type_item.delTouch = true;
                        int heightVideo = (int) (DisplayUtil.getScreenWidth(DynamicFragment.this.getActivity()) / 16.0 * 8.7);
//                    myHolder.iv_video_item.getLayoutParams().height = heightVideo;




                        myHolder.jc_type_item.backButton.setVisibility(View.GONE);
                        myHolder.jc_type_item.tinyBackImageView.setVisibility(View.GONE);
                        myHolder.jc_type_item.getLayoutParams().height = heightVideo;



                        myHolder.jc_type_item.titleTextView.setTextSize(16);
                        myHolder.jc_type_item.titleTextView.setLines(2);

                        myHolder.tv_detail_title = view.findViewById(R.id.tv_detail_title);
                        myHolder.ll_hot_comment = view.findViewById(R.id.ll_hot_comment);
                        myHolder.tv_thumup_hot_comment = view.findViewById(R.id.tv_thumup_hot_comment);
                        myHolder.tv_content_hot_item = view.findViewById(R.id.tv_content_hot_item);
                        myHolder.gv_hot_comment_item = view.findViewById(R.id.gv_hot_comment_item);

                        myHolder.ll_hot_comment_head = view.findViewById(R.id.ll_hot_comment_head);


                        myHolder.ll_reward_comment = view.findViewById(R.id.ll_reward_comment);
                        myHolder.tv_content_reward_item = view.findViewById(R.id.tv_content_reward_item);

                        myHolder.gv_reward_comment_item = view.findViewById(R.id.gv_reward_comment_item);



                        myHolder.ll_comment_type = view.findViewById(R.id.ll_comment_type);
                        myHolder.tv_content_comment_type_item = view.findViewById(R.id.tv_content_comment_type_item);
                        myHolder.gv_comment_type_item = view.findViewById(R.id.gv_comment_type_item);


                        myHolder.rl_video_hot_reply_item = view.findViewById(R.id.rl_video_hot_reply_item);
                        myHolder.iv_video_hot_reply_item = view.findViewById(R.id.iv_video_hot_reply_item);

                        myHolder.tv_content_dynamic_item_copy = view.findViewById(R.id.tv_content_dynamic_item_copy);

                    } else {
                        myHolder = (MyHolder) view.getTag();
                    }


                    /**
                     * 填充数据
                     */


                    myHolder.tv_content_dynamic_item.setMaxLines(5);
                    myHolder.tv_content_dynamic_item_copy.setVisibility(View.GONE);
//                    myHolder.tv_content_dynamic_item.setOnClickListener(null);


                    ctrlLine(myHolder,dynamicBean,i);


                    myHolder.ll_video_dy_item.setVisibility(View.GONE);
                    myHolder.ll_headline_dy_item.setVisibility(View.GONE);
                    myHolder.ll_bottom_item.setVisibility(View.GONE);
                    myHolder.fl_topic_img_item.setVisibility(View.GONE);
                    myHolder.ll_comment_type.setVisibility(View.GONE);

                    if ("-2".equals(tagId)) {
                        myHolder.ll_bottom_item.setVisibility(View.GONE);
                        myHolder.fl_topic_img_item.setVisibility(View.GONE);
                        myHolder.ll_video_dy_item.setVisibility(View.VISIBLE);
                        setVideoDatas(myHolder, bean, i);


                    } else {
                        myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DynamicFragment.this.getActivity(), PersonalDetailActivity.class);
                                intent.putExtra("id", bean.user.user_id + "");
                                intent.putExtra("position", i);
                                String json = new Gson().toJson(bean.user);
                                PersonalDetailActivity.start(DynamicFragment.this, bean.user.user_id, i, 1, json);
                                //                        startActivityForResult(intent, 1);
                            }
                        });


                        if (bean.user != null) {
                            PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item);
                            myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
                            ViewStatusUtils.addTags(myHolder.ll_top_tag_container, bean.user.tag, bean.user.tag1);

                            if (!TextUtils.isEmpty(bean.count) && Integer.valueOf(bean.count) >= 2) {
                                myHolder.iv_red_point_header.setVisibility(View.VISIBLE);
                            } else {
                                myHolder.iv_red_point_header.setVisibility(View.GONE);
                            }


                            if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                                myHolder.iv_vip_header.setVisibility(View.VISIBLE);
                            } else {
                                myHolder.iv_vip_header.setVisibility(View.GONE);
                            }

                            myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    requestFollows(i);
                                }
                            });


                            //放开关注功能

//                            if(bean.user.follow_status == 0){//未关注
//                                myHolder.tv_follow_dynamic_item.setTag(true);
//                            }else{
//                                myHolder.tv_follow_dynamic_item.setTag(false);
//                            }


                            if ( bean.user.follow_status == 0 && !(SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(bean.user.user_id))) {//系统推荐，显示关注
                                //            if(bean.follow != 1){

                                myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
                                myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                                final MyHolder finalMyHolder = myHolder;
                                myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        requestFollows(i, finalMyHolder);
                                    }
                                });


                                if (bean.user.follow_status == 1) {
                                                        myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
                                    myHolder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                                    myHolder.tv_follow_dynamic_item.setText("已关注");
                                    myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                           toDetail(i);
                                        }
                                    });
                                } else {
                                                        myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_blue_storke_cricle));
                                    //                    myHolder.tv_follow_dynamic_item.setText(" 关注 ");
                                    myHolder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
                                    myHolder.tv_follow_dynamic_item.setText("关注");
                                    //                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                                }


                            } else {
                                myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                                myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);

                                if(!"0".equals(tagId)) {
                                    if (bean.recommendation != null && !TextUtils.isEmpty(bean.create_time)) {
                                        String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                                        myHolder.tv_time_dynamic_item.setText(createTime);
                                    } else if (!TextUtils.isEmpty(bean.create_time)) {
                                        String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                                        myHolder.tv_time_dynamic_item.setText(createTime);
                                    } else {
                                        myHolder.tv_time_dynamic_item.setText("");
                                    }

                                    myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                                }else{
                                    myHolder.tv_time_dynamic_item.setText("");


                                    myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);


                                    myHolder.tv_time_dynamic_item.setVisibility(View.GONE);

                                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke_cricle));

                                    myHolder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                                    myHolder.tv_follow_dynamic_item.setText("已关注");
                                    myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            toDetail(i);
                                        }
                                    });
                                }

                            }
                        }



                        if(bean.type == 3 && isHeadline(bean)){//头条
                            myHolder.ll_headline_dy_item.setVisibility(View.VISIBLE);
                            setHeadLineData(myHolder, bean);
                        }else if (bean.type == 6) {
                            myHolder.ll_bottom_item.setVisibility(View.GONE);
                            myHolder.fl_topic_img_item.setVisibility(View.VISIBLE);

                            PictureUtils.showImg(bean.topic.background_img, myHolder.iv_topic_img);

                            myHolder.tv_title_topic_img.setText("" + bean.topic.title);

                            if (TextUtils.isEmpty(bean.topic.view_count)) {
                                bean.topic.view_count = "0";
                            }
                            String str = bean.topic.user_count + " 参与" ;
                            myHolder.tv_viewcount_topic_img.setText(str);


                        } else if (bean.type == 1) {//推荐单
                            myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                            myHolder.fl_topic_img_item.setVisibility(View.GONE);

                            myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);
                            myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                            myHolder.ll_bottom_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_video_item.setVisibility(View.GONE);
                            myHolder.rl_bg_content_item.setVisibility(View.GONE);
                            myHolder.gv_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                            myHolder.ll_no_pan.setVisibility(View.GONE);
                            myHolder.tv_detail_title.setVisibility(View.GONE);
                            myHolder.ll_hot_comment.setVisibility(View.GONE);
                            myHolder.ll_reward_comment.setVisibility(View.GONE);
                            bindType2(i, bean, myHolder);

                        } else if (bean.type == 3 || bean.type == 7) {//话题
                            myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                            myHolder.fl_topic_img_item.setVisibility(View.GONE);
                            myHolder.ll_no_pan.setVisibility(View.GONE);

                            myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                            myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);


                            myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                            myHolder.gv_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_video_item.setVisibility(View.GONE);
                            myHolder.rl_bg_content_item.setVisibility(View.GONE);
                            myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                            myHolder.tv_detail_title.setVisibility(View.GONE);
                            myHolder.ll_hot_comment.setVisibility(View.GONE);
                            myHolder.ll_reward_comment.setVisibility(View.GONE);


                            bindType1(i, bean, myHolder);
                        }else if(bean.type == 4){
                            myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                            myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                            myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_video_item.setVisibility(View.GONE);
                            myHolder.rl_bg_content_item.setVisibility(View.GONE);
                            myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                            myHolder.gv_dynamic_item.setVisibility(View.GONE);
                            myHolder.rl_bg_long_content_item.setVisibility(View.GONE);


                            bindtype4(i,bean,myHolder);
                        }



                        if("-3".equals(tagId)) {
                            if (!TextUtils.isEmpty(bean.create_time)) {
                                String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                                myHolder.tv_detail_title.setText(createTime);
                            } else {
                                myHolder.tv_detail_title.setText("");
                            }

                            myHolder.tv_detail_title.setVisibility(View.VISIBLE);
                        }

                    }
                }

                if (i == getCount() - 3 || (getCount() < 3)) {
                    if (pre_page == 1) {
                        rl_load_clone = null;
                        page++;
                        requestNetData();
                    }
                }

            return view;
        }
    }


    /**
     * 控制分割线样式  增加type6  话题样式
     * @param myHolder
     * @param dynamicBean
     * @param position
     */
    private void ctrlLine(MyHolder myHolder, DynamicBean dynamicBean, int position) {
        myHolder.v_top_line_small.setVisibility(View.GONE);
        myHolder.v_top_line_big.setVisibility(View.GONE);

        if(position == 0)return;

        DynamicBean.ListBean preListBean = dynamicBean.list.get(position - 1);
        DynamicBean.ListBean currentListBean = dynamicBean.list.get(position);

        boolean headline = isHeadline(preListBean);

        if(headline || preListBean.type == 6){
            boolean headline1 = isHeadline(currentListBean);
            if(headline1 || currentListBean.type == 6){
                myHolder.v_top_line_small.setVisibility(View.VISIBLE);
            }else{
                myHolder.v_top_line_big.setVisibility(View.VISIBLE);
            }


        }else{
            myHolder.v_top_line_big.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 填充头条数据
     * @param myHolder
     * @param bean
     */
    private void setHeadLineData(MyHolder myHolder, DynamicBean.ListBean bean) {
        if(bean.comment == null)return;
        myHolder.ll_one_img_headline.setVisibility(View.GONE);
        myHolder.ll_more_img_headline.setVisibility(View.GONE);
        myHolder.ll_headline_img_container.setVisibility(View.GONE);

        String comments = "";
        if(!TextUtils.isEmpty(bean.comment.reply_count)&&Integer.valueOf(bean.comment.reply_count)>0){
            String s = DataBindUtils.parseIntToK(Integer.valueOf(bean.comment.reply_count));
            comments = s + "评";
        }

        String bottom_text = bean.user.nickname + "   " + comments;

        if(bean.comment.backgroundList == null && TextUtils.isEmpty(bean.comment.background_img)){
            myHolder.ll_more_img_headline.setVisibility(View.VISIBLE);
            myHolder.tv_title_more_img.setText(bean.comment.title);
            myHolder.tv_bottom_more_img.setText(bottom_text);
        }else if(bean.comment.backgroundList != null){
            if(bean.comment.backgroundList.size()>2){
                //多图
                myHolder.ll_more_img_headline.setVisibility(View.VISIBLE);
                myHolder.ll_headline_img_container.setVisibility(View.VISIBLE);
                myHolder.tv_title_more_img.setText(bean.comment.title);
                myHolder.tv_bottom_more_img.setText(bottom_text);

                for(int i = 0;i<3;i++){
                    String url = bean.comment.backgroundList.get(i).url;
                    ImageView iv = (ImageView) myHolder.ll_headline_img_container.getChildAt(i);
                    PictureUtils.showImg(url,iv);
                }


            }else {//单图
                myHolder.ll_one_img_headline.setVisibility(View.VISIBLE);
                myHolder.tv_title_one_img.setText(bean.comment.title);
                myHolder.tv_bottom_one_img.setText(bottom_text);
                if(TextUtils.isEmpty(bean.comment.background_img)) {
                    PictureUtils.showImg(bean.comment.backgroundList.get(0).url, myHolder.iv_right_one_img);
                }else{
                    PictureUtils.showImg(bean.comment.background_img, myHolder.iv_right_one_img);
                }
            }

        }else {//单图
            PictureUtils.showImg(bean.comment.background_img, myHolder.iv_right_one_img);
            myHolder.ll_one_img_headline.setVisibility(View.VISIBLE);
            myHolder.tv_title_one_img.setText(bean.comment.title);
            myHolder.tv_bottom_one_img.setText(bottom_text);
        }


        //屏蔽置顶功能
        if(false && bean.top == 1){
            myHolder.tv_top_tag.setVisibility(View.VISIBLE);
            myHolder.tv_top_tag_1img.setVisibility(View.VISIBLE);
        }else{
            myHolder.tv_top_tag.setVisibility(View.GONE);
            myHolder.tv_top_tag_1img.setVisibility(View.GONE);
        }

    }


    /**
     * 评论类型
     * @param i
     * @param bean
     * @param myHolder
     */
    private void bindtype4(final int i, final DynamicBean.ListBean bean, MyHolder myHolder) {

        if(bean.reply == null || bean.comment == null){
            return;
        }
        myHolder.ll_comment_type.setVisibility(View.VISIBLE);
        myHolder.tv_content_dynamic_item.setVisibility(View.GONE);

        myHolder.ll_comment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, i);

            }
        });

        HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user;

        if(!bean.covert) {

            String time = "";
            time = bean.create_time;
            bean.create_time = bean.reply.create_time;
            bean.reply.create_time = time;

            user = bean.user;
            bean.user = bean.reply.user;
            bean.reply.user = user;



            if(bean.reply.user == null) bean.reply.user = new HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean();

            bean.covert = true;
        }

       setComenHead(i,bean,myHolder);



        myHolder.tv_detail_title.setVisibility(View.VISIBLE);
        if(!TextUtils.isEmpty(bean.user.user_desc)){
            myHolder.tv_detail_title.setText(bean.user.user_desc);
        }else{//显示时间
            if (!TextUtils.isEmpty(bean.create_time)) {
                String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                myHolder.tv_detail_title.setText(createTime);
            } else {
                myHolder.tv_detail_title.setText("");
            }
        }

        myHolder.tv_content_dynamic_item_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareComentsDetailsActivity.startForResult(2, DynamicFragment.this, ComentsDetailsActivity.SHARE_TYPE, bean.comment.comment_id, bean, i);

            }
        });

        myHolder.tv_content_dynamic_item_copy.setVisibility(View.VISIBLE);

        ShareReplyUtils.bindReplyLink(DynamicFragment.this.getActivity(),myHolder.tv_content_dynamic_item_copy,bean.reply,bean.reply.parentList);


        ShareReplyUtils.bindShareOriginal(DynamicFragment.this.getActivity(),myHolder.ll_comment_type,myHolder.tv_content_comment_type_item,bean.reply,bean.comment,i);



        if(bean.comment.imgListEx != null && bean.comment.imgListEx.size()>0) {
            DynamicBean.ListBean.ImageList imageList = bean.comment.imgListEx.get(0);
            String type = imageList.type;
            if ("video".equals(type)) {


                String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                myHolder.rl_video_hot_reply_item.setVisibility(View.VISIBLE);
                myHolder.gv_comment_type_item.setVisibility(View.GONE);
                PictureUtils.show(imgUrl, myHolder.iv_video_hot_reply_item);

                myHolder.rl_video_hot_reply_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ComentsDetailsActivity.startForResult(true, 2, DynamicFragment.this, bean.comment.comment_id, i);

                    }
                });

            } else {


                MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_comment_type_item.getAdapter();

                myHolder.rl_video_hot_reply_item.setVisibility(View.GONE);
                myHolder.gv_comment_type_item.setVisibility(View.VISIBLE);

                if (adapter != null) {
                    adapter.setData(bean.comment.imgListEx);
                    adapter.notifyDataSetChanged();
                } else {
                    myHolder.gv_comment_type_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                }

                if (bean.comment.imgListEx.size() == 4 || bean.comment.imgListEx.size() == 2) {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_comment_type_item, 2);
                } else if (bean.comment.imgListEx.size() == 1) {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_comment_type_item, 1);
                } else {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_comment_type_item, 3);
                }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                myHolder.gv_comment_type_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positi, long id) {

                        if (bean.comment.imgListEx.size() == 7 && positi >= 7) {
                            //      toJsonCommentBean(bean);
                            // ComentsDetailsActivity.startForResult(2, DynamicFragment.this,, position);
//                        ToastUtils.toast("热评详情");
                            return;
                        }

                        String urls = "";
                        for (int i = 0; i < bean.comment.imgListEx.size(); i++) {
                            String url = bean.comment.imgListEx.get(i).url;
                            String delUrl = Utils.delUrl(url);
                            urls += delUrl + ",";
                        }

                        String substring = urls.substring(0, urls.length() - 1);
                        Intent intent = new Intent(DynamicFragment.this.getActivity(), PicShowActivity.class);
                        intent.putExtra("urls", substring);
                        intent.putExtra("position", positi);
                        startActivity(intent);
                    }
                });

            }
            }else {
            myHolder.gv_comment_type_item.setVisibility(View.GONE);
            myHolder.rl_video_hot_reply_item.setVisibility(View.GONE);

        }




        if(!TextUtils.isEmpty(bean.reply.view_count)){
            String s = Utils.parseIntToK(Integer.valueOf(bean.reply.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 "+s);
        }else{
            myHolder.tv_view_count_topic_item.setText("阅读 "+"1");
        }

        myHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestThumbup(bean);
            }
        });

        myHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(),ACTION);
                    return;
                }
                ShareComentsDetailsActivity.startForResult(2,DynamicFragment.this,ComentsDetailsActivity.SHARE_TYPE,bean.comment.comment_id,bean,i);

//                if(LoginUtils.isUnLogin()){
//                    LoginUtils.goLoginActivity(PersonalDetailActivity.this,"");
//                    return;
//                }
//                ReplayCommentsActivity.start(PersonalDetailActivity.this,bean.comment.comment_id);

            }
        });

        myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
                showShare(i);
            }
        });
        if(bean.reply.praise_status == 1){//点赞
            Drawable drawable= getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
        }else {
            Drawable drawable= getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
        }

        DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item,bean.reply.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.reply.reply_count);


    }

    /**
     * 设置公共头部
     * @param i
     * @param bean
     * @param myHolder
     */
    private void setComenHead(final int i, final DynamicBean.ListBean bean, MyHolder myHolder){
        myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DynamicFragment.this.getActivity(), PersonalDetailActivity.class);
                intent.putExtra("id", bean.user.user_id + "");
                intent.putExtra("position", i);
                String json = new Gson().toJson(bean.user);
                PersonalDetailActivity.start(DynamicFragment.this, bean.user.user_id, i, 1, json);
                //                        startActivityForResult(intent, 1);
            }
        });


        if (bean.user != null) {
            PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item);
            myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
            ViewStatusUtils.addTags(myHolder.ll_top_tag_container, bean.user.tag, bean.user.tag1);

            if (!TextUtils.isEmpty(bean.count) && Integer.valueOf(bean.count) >= 2) {
                myHolder.iv_red_point_header.setVisibility(View.VISIBLE);
            } else {
                myHolder.iv_red_point_header.setVisibility(View.GONE);
            }


            if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                myHolder.iv_vip_header.setVisibility(View.VISIBLE);
            } else {
                myHolder.iv_vip_header.setVisibility(View.GONE);
            }

            myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    requestFollows(i);
                }
            });


            //放开关注功能

//                            if(bean.user.follow_status == 0){//未关注
//                                myHolder.tv_follow_dynamic_item.setTag(true);
//                            }else{
//                                myHolder.tv_follow_dynamic_item.setTag(false);
//                            }


            if ( bean.user.follow_status == 0 && !(SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(bean.user.user_id))) {//系统推荐，显示关注
                //            if(bean.follow != 1){

                myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                final MyHolder finalMyHolder = myHolder;
                myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        requestFollows(i, finalMyHolder);
                    }
                });


                if (bean.user.follow_status == 1) {
                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
                    myHolder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                    myHolder.tv_follow_dynamic_item.setText("已关注");
                    myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toDetail(i);
                        }
                    });
                } else {
                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_blue_storke_cricle));
                    //                    myHolder.tv_follow_dynamic_item.setText(" 关注 ");
                    myHolder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
                    myHolder.tv_follow_dynamic_item.setText("关注");
                    //                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                }


            } else {
                myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);

                if(!"0".equals(tagId)) {
                    if (bean.recommendation != null && !TextUtils.isEmpty(bean.create_time)) {
                        String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                        myHolder.tv_time_dynamic_item.setText(createTime);
                    } else if (!TextUtils.isEmpty(bean.create_time)) {
                        String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                        myHolder.tv_time_dynamic_item.setText(createTime);
                    } else {
                        myHolder.tv_time_dynamic_item.setText("");
                    }

                    myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                }else{
                    myHolder.tv_time_dynamic_item.setText("");


                    myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);


                    myHolder.tv_time_dynamic_item.setVisibility(View.GONE);

                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke_cricle));

                    myHolder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                    myHolder.tv_follow_dynamic_item.setText("已关注");
                    myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toDetail(i);
                        }
                    });
                }




            }
//                            myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
//                            myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 绑定头条View
     * @param myHolder
     * @param view
     */
    private void setHeadLineHolder(MyHolder myHolder, View view) {
        myHolder.ll_headline_dy_item = view.findViewById(R.id.ll_headline_dy_item);
        myHolder.ll_one_img_headline = view.findViewById(R.id.ll_one_img_headline);
        myHolder.tv_title_one_img = view.findViewById(R.id.tv_title_one_img);
        myHolder.tv_bottom_one_img = view.findViewById(R.id.tv_bottom_one_img);
        myHolder.iv_right_one_img = view.findViewById(R.id.iv_right_one_img);
        myHolder.ll_more_img_headline = view.findViewById(R.id.ll_more_img_headline);
        myHolder.tv_title_more_img = view.findViewById(R.id.tv_title_more_img);
        myHolder.ll_headline_img_container = view.findViewById(R.id.ll_headline_img_container);
        myHolder.tv_bottom_more_img = view.findViewById(R.id.tv_bottom_more_img);
        myHolder.tv_top_tag = view.findViewById(R.id.tv_top_tag);
        myHolder.tv_top_tag_1img = view.findViewById(R.id.tv_top_tag_1img);
    }

    /**
     * 视频特殊type 绑定数据
     * @param myHolder
     * @param bean
     */
    private void setVideoDatas(final MyHolder myHolder, final DynamicBean.ListBean bean, final int position) {
        if(bean.comment == null)return;
        if (bean.user != null) {
            PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item_video);
            myHolder.tv_name_dynamic_item_video.setText(bean.user.nickname);


            if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                myHolder.iv_vip_header_video.setVisibility(View.VISIBLE);
            } else {
                myHolder.iv_vip_header_video.setVisibility(View.GONE);
            }
        }


//        myHolder.tv_video_title_item.setText(bean.comment.comment+"");
        myHolder.tv_video_title_item.setVisibility(View.GONE);
        myHolder.tv_video_title_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(myHolder.player_video != null){
            myHolder.player_video.release();
        }
        myHolder.player_video.backButton.setVisibility(View.GONE);
        myHolder.player_video.tinyBackImageView.setVisibility(View.GONE);

//        myHolder.player_video.titleTextView.setText(bean.comment.comment+"");
        myHolder.player_video.titleTextView.setTextSize(16);
        myHolder.player_video.titleTextView.setLines(2);
        myHolder.player_video.titleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ComentsDetailsActivity.startForResult(2,DynamicFragment.this,bean.comment.comment_id,position);
            }
        });

        int heightVideo = (int) (DisplayUtil.getScreenWidth(DynamicFragment.this.getActivity()) / 16.0 * 9);
        myHolder.player_video.getLayoutParams().height = heightVideo;




        if(bean.comment.imgListEx != null) {
            String videoUrl = bean.comment.imgListEx.get(0).url;
            String title = bean.comment.comment.trim().replaceAll("\\n","").replaceAll("\\t","");
            boolean setUp = myHolder.player_video.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, title);


            if (setUp) {
                String imgUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".png";
                PictureUtils.showImg(imgUrl, myHolder.player_video.thumbImageView);
            }

        }
        myHolder.tv_thumbup_topic_item_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestThumbupVideoItem(bean,myHolder.tv_thumbup_topic_item_video);
            }
        });

        myHolder.tv_comments_topic_item_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bean.type == 3 && isShootVideo(bean)){
                    toShootVideoPlayer(bean,position,true);
                }else {
                    toJsonCommentBean(bean);
                    ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, position, true);
                }

            }
        });


        myHolder.tv_share_topic_item_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare(position);
            }
        });

        myHolder.ll_top_click_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PersonalDetailActivity
                PersonalDetailActivity.start(DynamicFragment.this,bean.user.user_id,position,3,"{}");
//
            }
        });

        if(bean.comment.praise_status == 1){//点赞
            Drawable drawable= QCaApplication.getContext().getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item_video.setCompoundDrawables(drawable,null,null,null);
        }else {
            Drawable drawable= QCaApplication.getContext().getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item_video.setCompoundDrawables(drawable,null,null,null);
        }

        DataBindUtils.setPraiseCountVideo(myHolder.tv_thumbup_topic_item_video,bean.comment.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        DataBindUtils.setComentCountVideo(myHolder.tv_comments_topic_item_video,bean.comment.reply_count+"");

    }


    private void toJsonCommentBean(DynamicBean.ListBean listBean){
        listBean.comment.user = listBean.user;
        String s = new Gson().toJson(listBean.comment);
        Constant.commentBeanJson = s;
    }

    /**
     * 去短视频页
     * @param bean
     * @param position
     */
    private void toShootVideoPlayer(DynamicBean.ListBean bean, int position,boolean isSelectFirst) {

        DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.list = new ArrayList<>();
        dynamicBean.list.add(bean);
        Constant.dynamicBean = dynamicBean;
        VideoPlayerActivity.start(this.getActivity(),-1,0,bean.subtype,22,isSelectFirst);

    }


    /**
     * 可能感兴趣的人，推荐流内容适配器
     */
    private List<DynamicBean.ListBean> mlist;
    private MyRvType5Adapter myRvType5Adapter;
    class MyRvType5Adapter extends RecyclerView.Adapter<RvHolder> {

        private List<DynamicBean.ListBean> list;
        public MyRvType5Adapter(List<DynamicBean.ListBean> list) {
            DynamicFragment.this.mlist = list;
            this.list = list;
            myRvType5Adapter = this;
        }

        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RvHolder rvHolder = new RvHolder(LayoutInflater.from(
                    QCaApplication.getContext()).inflate(R.layout.type5_cy_item_layout, parent,
                    false));
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(final RvHolder holder, final int position) {
            final DynamicBean.ListBean listBean = list.get(position);

            PictureUtils.showCircle(listBean.headurl,holder.iv_header_item);
            holder.tv_name_item.setText(listBean.nickname);
            holder.tv_tag_item.setText(listBean.tag);
            if(!TextUtils.isEmpty(listBean.verify_type)&&Integer.valueOf(listBean.verify_type)>0){
                holder.iv_vip_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_vip_item.setVisibility(View.GONE);
            }

            if (listBean.follow_status == 1) {//关注
                holder.tv_follow_item.setTag(1);
                holder.tv_follow_item.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
                holder.tv_follow_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                holder.tv_follow_item.setText("已关注");
            } else {
                holder.tv_follow_item.setTag(0);
                holder.tv_follow_item.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.shape_yellow_cricel));
                holder.tv_follow_item.setTextColor(Color.parseColor("#222222"));
                holder.tv_follow_item.setText(" 关注 ");
            }

            holder.tv_follow_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(LoginUtils.isUnLogin()){
                        LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(),ACTION);
                        return;
                    }
                    if(holder.tv_follow_item.getTag() == null)return;
                    int b1 = (int) holder.tv_follow_item.getTag();
                    if(b1 == 1){//取消关注
                        DynamicBean.DataBean.UserBean userBean = new DynamicBean.DataBean.UserBean();
                        userBean.nickname = listBean.nickname;
                        userBean.headurl = listBean.headurl;
                        String json = new Gson().toJson(userBean);

                        PersonalDetailActivity.start(DynamicFragment.this,listBean.user_id,position,3,json);
                    }else{//关注
                        requestFollows("add",listBean,MyRvType5Adapter.this);
                    }
                    return;
                }
            });

            holder.rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DynamicBean.DataBean.UserBean userBean = new DynamicBean.DataBean.UserBean();
                    userBean.nickname = listBean.nickname;
                    userBean.headurl = listBean.headurl;
                    String json = new Gson().toJson(userBean);
                    PersonalDetailActivity.start(DynamicFragment.this,listBean.user_id,position,3,json);
//                    PersonalDetailActivity.start(HomeNewFragment.this.getActivity(),listBean.user_id);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    static class RvHolder extends RecyclerView.ViewHolder{

        public ImageView iv_img_rv_item;
        public TextView tv_title_rv_item;

        public LinearLayout rl_root;
        public ImageView iv_header_item;
        public ImageView iv_vip_item;
        public TextView tv_name_item;
        public TextView tv_tag_item;
        public TextView tv_follow_item;

        public RvHolder(View itemView) {
            super(itemView);
            iv_img_rv_item = itemView.findViewById(R.id.iv_img_rv_item);
            tv_title_rv_item = itemView.findViewById(R.id.tv_title_rv_item);
            iv_header_item = itemView.findViewById(R.id.iv_header_item);
            iv_vip_item = itemView.findViewById(R.id.iv_vip_item);
            tv_name_item = itemView.findViewById(R.id.tv_name_item);
            tv_tag_item = itemView.findViewById(R.id.tv_tag_item);
            tv_follow_item = itemView.findViewById(R.id.tv_follow_item);
            rl_root = itemView.findViewById(R.id.rl_root);
        }
    }

    /**
     * 可能感兴趣的人 关注方法
     * @param type
     * @param listBean
     * @param myRvType5Adapter
     */
    public void requestFollows(String type, final DynamicBean.ListBean listBean, final MyRvType5Adapter myRvType5Adapter){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        params.put("type",type);
        params.put("user_id",listBean.user_id+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        listBean.follow_status = 1;
                        NotificationsUtils.checkNotificationAndStartSetting(DynamicFragment.this.getActivity(),lv_dynamic_fg);
                    }else{
                        listBean.follow_status = 0;
                    }
                    delFollow(listBean.follow_status,listBean.user_id);
                    myRvType5Adapter.notifyDataSetChanged();
                    myAdapter.notifyDataSetChanged();

                    /**
                     * 刷新我的关注数量
                     */
                    Intent intent5 = new Intent(MineFragment.MINE_FG_ACTION);
                    intent5.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent5);

                    DynamicFragment.sendFoucsData();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    /**
     * 同步关注状态
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1&&resultCode == 1){//个人详情

            int follow_status = 0;

            try {
                follow_status = data.getIntExtra("follow_status", 0);
            }catch (Exception e){

            }

            int position = data.getIntExtra("position", -1);

            if(dynamicBean == null || dynamicBean.list == null)return;
            if(position >= dynamicBean.list.size()){
                return;
            }

            if(position != -1) {
                DynamicBean.ListBean bean = dynamicBean.list.get(position);

                if(!"-3".equals(tagId)){
                    delFollow(follow_status,bean.user.user_id);
                    delRvFollow(follow_status,bean.user.user_id);
                }else {
                    if(follow_status == 0 && !SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID).equals(bean.user.user_id)){
                        delFollowDelete(bean.user.user_id);
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

        }else if(requestCode == 2&&resultCode == 2){//话题详情
            int position = data.getIntExtra("position", -1);
            String praise_count = data.getStringExtra("praise_count");

            if(position != -1) {
                if(dynamicBean == null || dynamicBean.list == null)return;
                if(position >= dynamicBean.list.size()){
                    return;
                }

                DynamicBean.ListBean bean = dynamicBean.list.get(position);


                int  follow_status  = 0;
                try {
                    follow_status = Integer.valueOf(data.getStringExtra("follow_status"));
                }catch (Exception e){

                }

                if(bean.type == 4){
                    int praise_status = data.getIntExtra("praise_status", 0);
                    bean.reply.praise_count = praise_count;
                    bean.reply.praise_status = praise_status;
                    bean.reply.reply_count = data.getStringExtra("reply_count");
                    bean.reply.collection_status = data.getStringExtra("collection_status");
                }else {

                    int praise_status = data.getIntExtra("praise_status", 0);
                    bean.comment.praise_count = praise_count;
                    bean.comment.praise_status = praise_status;
                    bean.comment.reply_count = data.getStringExtra("reply_count");
                    bean.comment.collection_status = data.getStringExtra("collection_status");
                }
                if(!"-3".equals(tagId)){
                     delFollow(follow_status,bean.user.user_id);
                    delRvFollow(follow_status,bean.user.user_id);
                }else {

                    if (follow_status == 0 && !bean.user.user_id.equals(SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID))) {
                        delFollowDelete(bean.user.user_id);
                    }
                }
                boolean delete = data.getBooleanExtra("delete", false);
                if(delete){
                    dynamicBean.list.remove(position);
                }

                myAdapter.notifyDataSetChanged();
            }

        }else if(requestCode == 3 && resultCode == 1){
            int follow_status = 0;

            try {
                follow_status = data.getIntExtra("follow_status", 0);
            }catch (Exception e){

            }
            int position = data.getIntExtra("position", -1);
            if(mlist == null )return;
            if(position >= mlist.size()){
                return;
            }

            if(position != -1) {

                if(mlist != null && myRvType5Adapter != null) {
                    DynamicBean.ListBean listBean = mlist.get(position);
                    if(listBean.follow_status == follow_status)return;
                    listBean.follow_status = follow_status;
                    delFollow(follow_status,listBean.user_id);
                    myRvType5Adapter.notifyDataSetChanged();
                    myAdapter.notifyDataSetChanged();
                }
            }
        }else if(requestCode == 22){//短视频页返回
            myAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 删除关注人方法
     * @param userId
     */
    private void delFollowDelete(String userId) {
            List<DynamicBean.ListBean> backupList = new ArrayList<>();
            for (int i = 0; i < dynamicBean.list.size(); i++) {
                DynamicBean.ListBean list = dynamicBean.list.get(i);
                int type1 = list.type;
                if (type1 == 2 || type1 == 4 || type1 == 5) continue;
                if (list.user == null) continue;
                String user_id = list.user.user_id;
                if (!user_id.equals(userId)) {
                    backupList.add(list);
                }
            }
            dynamicBean.list = backupList;
    }

    /**
     * 处理del 首页推荐用户状态
     * @param follow_status
     * @param user_id
     */
    private void delRvFollow(int follow_status, String user_id) {

        if(mlist == null)return;

        for(int i = 0;i<mlist.size();i++){
            DynamicBean.ListBean bean = mlist.get(i);
            String userId = bean.user_id;
            if(user_id.equals(userId)){
                bean.follow_status = follow_status;
            }
        }

        if(myRvType5Adapter == null)return;
        myRvType5Adapter.notifyDataSetChanged();
    }

    /**
     * 处理首页状态
     * @param type
     * @param userId
     */
    private void delFollow(int type,String userId) {
        if(dynamicBean == null || dynamicBean.list == null)return;
        for(int i = 0;i<dynamicBean.list.size();i++){
            HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user = dynamicBean.list.get(i).user;
            int type1 = dynamicBean.list.get(i).type;
            if(type1 == 2 ||type1 == 4 ||type1 == 5||type1 == 6)continue;
            if(user == null)continue;
            String user_id = user.user_id;
            if(user_id.equals(userId)){
                user.follow_status = type;
            }
        }
    }


    /**
     * 列表点击关注请求方法
     * @param i
     */
    public void requestFollows(int i){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        final DynamicBean.ListBean bean = dynamicBean.list.get(i);
        if(bean.user == null){
            return;
        }
        int follow_status = bean.user.follow_status;
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";
        if(follow_status == 1){
            type = "delete";
        }else{
            type = "add";
        }
        params.put("type",type);
        params.put("user_id",bean.user.user_id+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        bean.user.follow_status = 1;
                        NotificationsUtils.checkNotificationAndStartSetting(DynamicFragment.this.getActivity(),lv_dynamic_fg);
                    }else{
                        bean.user.follow_status = 0;
                    }
                    myAdapter.notifyDataSetChanged();

//                    DynamicFragment.sendFoucsData();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    public void requestFollows(final int i, final MyHolder holder){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        final DynamicBean.ListBean bean = dynamicBean.list.get(i);
        if(bean.user == null){
            return;
        }
        int follow_status = bean.user.follow_status;
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";
        if(follow_status == 1){
            type = "delete";
        }else{
            type = "add";
        }
        params.put("type",type);
        params.put("user_id",bean.user.user_id+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        bean.user.follow_status = 1;
                        NotificationsUtils.checkNotificationAndStartSetting(DynamicFragment.this.getActivity(),lv_dynamic_fg);
                    }else{
                        bean.user.follow_status = 0;
                    }
                  //  myAdapter.notifyDataSetChanged();


                    if (bean.user.follow_status == 1) {
                        holder.tv_follow_dynamic_item.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
                        holder.tv_follow_dynamic_item.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                        holder.tv_follow_dynamic_item.setText("已关注");
                        holder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                toDetail(i);
                            }
                        });

                        delFollow(1,bean.user.user_id+"");

                   //     myAdapter.notifyDataSetChanged();
                    }


//                    DynamicFragment.sendFoucsData();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    /**
     * 一键关注 提交器
     */
    private int successCount;
    public void requestFollowById(String userId){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";

        type = "add";

        params.put("type",type);
        params.put("user_id",userId+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                successCount++;
                if(submitCount==successCount){
                    initData();

                    Intent intent5 = new Intent(MineFragment.MINE_FG_ACTION);
                    intent5.putExtra(RESULT, 99);
                    LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent5);
                }
            }
        });
    }

    /**
     * 话题
     * @param bean
     * @param myHolder
     */
    private int playPosition = -1;
    private void bindType1(final int position, final DynamicBean.ListBean bean, final MyHolder myHolder) {

        if(!TextUtils.isEmpty(bean.comment.comment)){

           bean.comment.comment =  bean.comment.comment.replaceAll("&nbsp;"," ").replaceAll("<br/>"," ").replaceAll("\n"," ").replaceAll("<[^>]*>|\\n","");


           if(TextUtils.isEmpty(bean.comment.background_img) && bean.comment.backgroundList != null){

               String url = "";
               for(int i = 0;i<bean.comment.backgroundList.size();i++){
                   String url1 = bean.comment.backgroundList.get(i).url;
                   if(!url1.endsWith(".gif")){
                       url = url1;
                       break;
                   }
               }

               bean.comment.background_img = url;
           }


            if(TextUtils.isEmpty(bean.comment.background_img)){



                if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                    myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                    myHolder.tv_title_long_content.setText(bean.comment.title);
                    myHolder.tv_content_long_content.setText(bean.comment.comment);




                }else {//普通评论
                    myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                    myHolder.tv_content_dynamic_item.setText(bean.comment.comment);


                    SeeAllTextViewUtils.toggleEllipsize(DynamicFragment.this.getContext(),myHolder.tv_content_dynamic_item,5,bean.comment.comment,"全部",R.color.blue_5593E6,false);





                }

            }else{
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.VISIBLE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                myHolder.tv_content_bg_dynamic_item.setText(bean.comment.title);
                myHolder.tv_content_bottom.setText(bean.comment.comment+"");
                PictureUtils.showImg(bean.comment.background_img,myHolder.iv_content_bg_item);

                SeeAllTextViewUtils.toggleEllipsize(DynamicFragment.this.getContext(),myHolder.tv_content_dynamic_item,5,bean.comment.comment,"全部",R.color.blue_5593E6,false);

            }

        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
            myHolder.rl_bg_content_item.setVisibility(View.GONE);
            myHolder.rl_bg_long_content_item.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                myHolder.tv_title_long_content.setText(bean.comment.title);
                myHolder.tv_content_long_content.setText(bean.comment.comment);

            }
        }

        if(!TextUtils.isEmpty(bean.comment.view_count)){
            String s = Utils.parseIntToK(Integer.valueOf(bean.comment.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 "+s);
        }





        if(bean.comment.imgListEx != null){//http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif

            if(bean.comment.imgListEx.size()>0){
                if("video".equals(bean.comment.imgListEx.get(0).type)){
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.VISIBLE);
//                    DynamicBean.ListBean.ImageList imageList = bean.comment.imgListEx.get(0);
//                    String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
//                    PictureUtils.showImg(imgUrl,myHolder.iv_video_item);
//                    myHolder.rl_video_item.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
////                            ComentsDetailsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);
//                            if(bean.type == 3 && isShootVideo(bean)){
//                                toShootVideoPlayer(bean,position);
//                            }else {
//                                toJsonCommentBean(bean);
//                                ComentsDetailsActivity.startForResult(true, 2, DynamicFragment.this, bean.comment.comment_id, position);
//                            }
//                        }
//                    });







                    myHolder.jc_type_item.resetProgressAndTime();
                    myHolder.jc_type_item.destroyDrawingCache();
                    myHolder.jc_type_item.release();

                    myHolder.jc_type_item.setSilencePattern(true);
                    myHolder.jc_type_item.fullscreenButton.setVisibility(View.GONE);


                    myHolder.jc_type_item.setVisibility(View.VISIBLE);





                    myHolder.jc_type_item.setOnClick(new JCaoStrandPlayer.OnClick() {
                        @Override
                        public void onClick() {
                            JCVideoPlayer.releaseAllVideos();
                            if(bean.type == 3 && isShootVideo(bean)){
                                toShootVideoPlayer(bean,position,false);
                            }else {
                                toJsonCommentBean(bean);
                                ComentsDetailsActivity.startForResult(true,2, DynamicFragment.this, bean.comment.comment_id, position);
                            }
                        }
                    });


                    if(bean.comment.imgListEx != null) {


                        myHolder.rl_video_item.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                String videoUrl = bean.comment.imgListEx.get(0).url;
//                                String title = bean.comment.comment.trim().replaceAll("\\n","").replaceAll("\\t","");
                                String title = "";
                                boolean setUp = myHolder.jc_type_item.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, title);
                                if (setUp) {
                                    String img = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".png";
                                    PictureUtils.showImg(img, myHolder.jc_type_item.thumbImageView);
                         //           myHolder.jc_type_item.startButton.performClick();
                                }

                            }
                        },50);
                    }

                    playPosition = position;
                }else{
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);

                    MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_dynamic_item.getAdapter();



                    if (adapter != null) {
                        adapter.setData(bean.comment.imgListEx);
                        adapter.notifyDataSetChanged();
                    }else {
                        myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    }

                    if(bean.comment.imgListEx.size() == 4 || bean.comment.imgListEx.size() == 2){
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,2);
                    }else if(bean.comment.imgListEx.size() == 1){
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,1);
                    }else{
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,3);
                    }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    myHolder.gv_dynamic_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positi, long id) {

                            if(bean.comment.imgListEx.size() == 7 && positi>=7){
                                toJsonCommentBean(bean);
                                ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, position);
                                return;
                            }

                            String urls = "";
                            for(int i = 0;i<bean.comment.imgListEx.size();i++){
                                String url = bean.comment.imgListEx.get(i).url;
                                String delUrl = Utils.delUrl(url);
                                urls +=  delUrl+ ",";
                            }

                            String substring = urls.substring(0, urls.length() - 1);
                            Intent intent = new Intent(DynamicFragment.this.getActivity(),PicShowActivity.class);
                            intent.putExtra("urls",substring);
                            intent.putExtra("position", positi);
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

        if(bean.topic != null){
            myHolder.rl_topic_dynamic_item.setVisibility(View.VISIBLE);
            PictureUtils.showImg(bean.topic.background_img,myHolder.iv_img_left);
            myHolder.tv_bottom_title_topic_item.setText(bean.topic.title);
            myHolder.rl_topic_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity.start(DynamicFragment.this.getActivity(),bean.topic.topic_id);
                }
            });
        }

        myHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 requestThumbup(bean);
            }
        });


        myHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(LoginUtils.isUnLogin()){
//                    LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(),ACTION);
//                    return;
//                }
                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
//                WebViewActivtiy.start(DynamicFragment.this.getActivity(),url,"球咖");
//                ComentsDetailsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);
                if(bean.type == 3 && isShootVideo(bean)){
                    toShootVideoPlayer(bean,position,true);
                }else {
                    toJsonCommentBean(bean);
                    ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, position, true);
                }
//                if(LoginUtils.isUnLogin()){
//                    LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(),DynamicFragment.ACTION);
//                    return;
//                }
//                ReplayCommentsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);

            }
        });


        myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
                showShare(position);
            }
        });

        if(bean.comment.praise_status == 1){//点赞
            Drawable drawable= QCaApplication.getContext().getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
        }else {
            Drawable drawable= QCaApplication.getContext().getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
        }

        DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item,bean.comment.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.comment.reply_count+"");
//        myHolder.tv_comments_topic_item.setText(bean.comment.reply_count+"");



        //处理新增字段  热评
        if(bean.hot_reply != null){
            myHolder.ll_hot_comment.setVisibility(View.VISIBLE);
            myHolder.ll_hot_comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ComentsDetailsActivity.startForResult(2, DynamicFragment.this, bean.comment.comment_id, position, true);

                }
            });


//            bean.hot_reply.imgListEx = bean.comment.imgListEx;
            bindhotReply(myHolder,bean.comment.comment_id,bean.hot_reply,position);



        }else{
            myHolder.ll_hot_comment.setVisibility(View.GONE);
            myHolder.ll_hot_comment.setOnClickListener(null);
        }

//        if(!"-3".equals(tagId)){
            myHolder.tv_detail_title.setVisibility(View.VISIBLE);//标签

            if(!TextUtils.isEmpty(bean.reason)){
                myHolder.tv_detail_title.setText(bean.reason);
            }else{//显示时间
                if (!TextUtils.isEmpty(bean.create_time)) {
                    String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                    myHolder.tv_detail_title.setText(createTime);
                } else {
                    myHolder.tv_detail_title.setText("");
                }
            }
//        }else{
//            myHolder.tv_detail_title.setVisibility(View.GONE);//标签
//
//        }



        //转发逻辑判断
        myHolder.ll_reward_comment.setVisibility(View.GONE);
      //  myHolder.tv_comments_topic_item.setText();



    }


    /**
     * 绑定热评
     * @param myHolder
     * @param commentId
     * @param hot_reply
     */
    private void bindhotReply(MyHolder myHolder, final String commentId, final DynamicBean.ListBean.HotReply hot_reply,final int position) {


        myHolder.tv_content_hot_item.setText(hot_reply.reply);

        myHolder.tv_thumup_hot_comment.setText(hot_reply.praise_count + " 赞");


        if(hot_reply.imgListEx != null && hot_reply.imgListEx.size()>0) {



            DynamicBean.ListBean.ImageList imageList = hot_reply.imgListEx.get(0);
            String type = imageList.type;
            if ("video".equals(type)) {


                String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                myHolder.rl_video_hot_reply_item.setVisibility(View.VISIBLE);
                myHolder.gv_hot_comment_item.setVisibility(View.GONE);
                PictureUtils.show(imgUrl,myHolder.iv_video_hot_reply_item);

                myHolder.rl_video_hot_reply_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ComentsDetailsActivity.startForResult(true,2, DynamicFragment.this, commentId, position);

                    }
                });

            } else {


                MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_hot_comment_item.getAdapter();

                myHolder.rl_video_hot_reply_item.setVisibility(View.GONE);
                myHolder.gv_hot_comment_item.setVisibility(View.VISIBLE);

                if (adapter != null) {
                    adapter.setData(hot_reply.imgListEx);
                    adapter.notifyDataSetChanged();
                } else {
                    myHolder.gv_hot_comment_item.setAdapter(new MyGvAdapter(hot_reply.imgListEx));
                }

                if (hot_reply.imgListEx.size() == 4 || hot_reply.imgListEx.size() == 2) {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_hot_comment_item, 2);
                } else if (hot_reply.imgListEx.size() == 1) {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_hot_comment_item, 1);
                } else {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_hot_comment_item, 3);
                }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                myHolder.gv_hot_comment_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int positi, long id) {

                        if (hot_reply.imgListEx.size() == 7 && positi >= 7) {
                            //      toJsonCommentBean(bean);
                            // ComentsDetailsActivity.startForResult(2, DynamicFragment.this,, position);
//                            ToastUtils.toast("热评详情");
                            return;
                        }

                        String urls = "";
                        for (int i = 0; i < hot_reply.imgListEx.size(); i++) {
                            String url = hot_reply.imgListEx.get(i).url;
                            String delUrl = Utils.delUrl(url);
                            urls += delUrl + ",";
                        }

                        String substring = urls.substring(0, urls.length() - 1);
                        Intent intent = new Intent(DynamicFragment.this.getActivity(), PicShowActivity.class);
                        intent.putExtra("urls", substring);
                        intent.putExtra("position", positi);
                        startActivity(intent);
                    }
                });

            }
        }else{
            myHolder.gv_hot_comment_item.setVisibility(View.GONE);
        }

    }

    private void showShare(final int position) {
        SharePopuwind sharePopuwind = new SharePopuwind(this.getActivity(), new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                DynamicBean.ListBean beanX = dynamicBean.list.get(position);
                if (beanX.topic == null) {

                    String videoUrl = "";
                    if ("-2".equals(tagId)) {
                         videoUrl = beanX.comment.imgListEx.get(0).url;
                    }

                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname,videoUrl, DynamicFragment.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname,DynamicFragment.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_dynamic_fg, Gravity.CENTER, 0, 0);

    }

    class MyGvAdapter extends BaseAdapter{


        private List<DynamicBean.ListBean.ImageList> imgListEx;


        public MyGvAdapter(List<DynamicBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }

        @Override
        public int getCount() {
            if(imgListEx.size() == 7){
                return 9;
            }

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

            try {
                DynamicBean.ListBean.ImageList imageList = imgListEx.get(position);

                View view = View.inflate(QCaApplication.getContext(), R.layout.img_gif_layout, null);
                ImageView imageView = view.findViewById(R.id.iv);
                TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
                Utils.showType(imageList.url, imageView, tv_gif_icon, imageList.size);

                if (getCount() == 1) {
                    Utils.scaleImg(DynamicFragment.this.getActivity(), view, imageView, imageList.size);
                }

                convertView = view;
            }catch (Exception e){
                 convertView = View.inflate(QCaApplication.getContext(), R.layout.img_gif_layout, null);
                ImageView imageView = convertView.findViewById(R.id.iv);
                TextView tv_gif_icon = convertView.findViewById(R.id.tv_gif_icon);
                DynamicBean.ListBean.ImageList imageList = imgListEx.get(0);
                Utils.showType(imageList.url, imageView, tv_gif_icon, imageList.size);

                imageView.setVisibility(View.INVISIBLE);
                tv_gif_icon.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        public void setData(List<DynamicBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }
    }

    /**
     * 处理点赞
     * @param bean
     */
    private void requestThumbup(final DynamicBean.ListBean bean) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","topic");

        if(bean.type == 4){//转发评论点赞
            params.put("type","praise_reply");
            params.put("id",bean.reply.id);
        }else {
            params.put("type", "praise");
            params.put("id", bean.comment.comment_id);
        }
        rl_load_clone = rl_load_layout;
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    if(bean.type == 4){
                        bean.reply.praise_count = praiseBean.data.praise_count;
                        bean.reply.praise_status = praiseBean.data.praise_status;
                    }else {
                        bean.comment.praise_count = praiseBean.data.praise_count;
                        bean.comment.praise_status = praiseBean.data.praise_status;
                    }
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


    /**
     * 处理点赞  视频列表
     * @param bean
     */
    private void requestThumbupVideoItem(final DynamicBean.ListBean bean, final TextView tv) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","praise");
        params.put("id",bean.comment.comment_id);
        rl_load_clone = rl_load_layout;
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    bean.comment.praise_count = praiseBean.data.praise_count;
                    bean.comment.praise_status = praiseBean.data.praise_status;


                    if(bean.comment.praise_status == 1){//点赞
                        Drawable drawable= QCaApplication.getContext().getResources().getDrawable(R.drawable.thumb_checked_small);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv.setCompoundDrawables(drawable,null,null,null);
                    }else {
                        Drawable drawable= QCaApplication.getContext().getResources().getDrawable(R.drawable.thumb_small);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv.setCompoundDrawables(drawable,null,null,null);
                    }

                    DataBindUtils.setPraiseCountVideo(tv,bean.comment.praise_count);



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

    /**
     * 推荐单
     * @param bean
     * @param myHolder
     */
    private void bindType2(final int position, final DynamicBean.ListBean bean, MyHolder myHolder) {

        if("3".equals(tagId)){//竞猜特殊处理

            if(!TextUtils.isEmpty(bean.recommendation.title)){
                myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.tv_content_dynamic_item.setText(bean.match.hostTeamName + "vs" + bean.match.awayTeamName+": "+bean.recommendation.title);
            }else{
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_content_dynamic_item.setText("");
            }


            myHolder.ll_no_pan.setVisibility(View.VISIBLE);
            myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);

            bindType2ForNoPan(bean,myHolder);


        }else {


            if(!TextUtils.isEmpty(bean.recommendation.title)){
                myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.tv_content_dynamic_item.setText(bean.recommendation.title);
            }else{
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_content_dynamic_item.setText("");
            }

         myHolder.ll_no_pan.setVisibility(View.GONE);
         myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);

            myHolder.tv_team_name_dynamic_item.setText(bean.match.hostTeamName + "\t\tvs\t\t" + bean.match.awayTeamName);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

            spannableStringBuilder.append(bean.match.leagueName);
            myHolder.tv_type_dynamic_item.setText(bean.recommendation.type + "");
            if (!TextUtils.isEmpty(bean.match.match_time)) {
                String matchStartTime = Utils.getMatchStartTime(Long.valueOf(bean.match.match_time + "000"));
                spannableStringBuilder.append(" · " + matchStartTime);
            }

            if (bean.recommendation.reason_len > 0) {
                spannableStringBuilder.append(" · " + bean.recommendation.reason_len + "字");
            }

            /**
             * 不中退款
             */
            if ("1".equals(bean.recommendation.return_if_wrong)) {
                spannableStringBuilder.append(" · " + "不中退款");
            }


            if (bean.match.status.equals("COMPLETE")) {//一结束     1
                myHolder.ll_price.setVisibility(View.GONE);
                myHolder.tv_right_item.setVisibility(View.VISIBLE);
//            myHolder.iv_result_personal.setVisibility(View.VISIBLE);
                myHolder.tv_buy.setVisibility(View.GONE);
                spannableStringBuilder.append(" · " + bean.recommendation.type);
            } else {
                if (bean.recommendation.buy == 1) {
                    spannableStringBuilder.append(" · " + bean.recommendation.type);
                    myHolder.tv_buy.setVisibility(View.VISIBLE);
                    myHolder.ll_price.setVisibility(View.GONE);
                    myHolder.tv_right_item.setVisibility(View.VISIBLE);
                } else {
                    myHolder.tv_buy.setVisibility(View.GONE);
                    myHolder.ll_price.setVisibility(View.VISIBLE);
                    myHolder.tv_right_item.setVisibility(View.GONE);
                }
            }

            if (!bean.match.status.equals("COMPLETE")) {
                if (!TextUtils.isEmpty(bean.recommendation.price) && !"0".equals(bean.recommendation.price)) {
                    Integer integer = Integer.valueOf(bean.recommendation.price);
                    String price = integer + "金币";
                    myHolder.tv_price_dynamic_item.setText(price);
                } else {
                    myHolder.tv_price_dynamic_item.setText("免费");
                }
            } else {
//            spannableStringBuilder.append(bean.recommendation.type + "");
            }


            myHolder.tv_bottom_dynamic_item.setText(spannableStringBuilder);
            spannableStringBuilder.clear();
        }
    }


    /**
     * 绑定推荐单没有盘口类型
     * @param bean
     * @param myHolder
     */
    private void bindType2ForNoPan(final DynamicBean.ListBean bean, MyHolder myHolder) {

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(bean.match.leagueName);

        if(!TextUtils.isEmpty(bean.match.match_time)){
            String matchStartTime = Utils.getMatchStartTime(Long.valueOf(bean.match.match_time+"000"));
            spannableStringBuilder.append(" · " +matchStartTime);
        }


        if("1".equals(bean.recommendation.return_if_wrong)){
            spannableStringBuilder.append(" · " +"不中退款");
        }

        myHolder.iv_buy.setVisibility(View.GONE);
        spannableStringBuilder.append(" · " + bean.recommendation.type);
        if(!TextUtils.isEmpty(bean.recommendation.price)&& !"COMPLETE".equals(bean.match.status)){
            Integer integer = Integer.valueOf(bean.recommendation.price);
            String price = "免费";
            if(integer >0) {
                price = integer + "金币";
            }
            SpannableString spannableString = new SpannableString(price);
            ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#AE7614"));
            spannableString.setSpan(span,0,price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(" · ");
            spannableStringBuilder.append(spannableString);

            if(bean.recommendation.buy == 1){
                myHolder.iv_buy.setVisibility(View.VISIBLE);
            }else{
                myHolder.iv_buy.setVisibility(View.GONE);
            }

        }


        myHolder.tv_bottom_home_item.setText(spannableStringBuilder);
        spannableStringBuilder.clear();
    }

    static class MyHolder{
        public LinearLayout ll_top_click;
        public ImageView iv_head_dynamic_item;
        public TextView tv_name_dynamic_item;
        public LinearLayout ll_top_tag_container;
        public TextView tv_time_dynamic_item;
        public TextView tv_content_dynamic_item;
        public LinearLayout ll_groom_dynamic_item;
        public TextView tv_team_name_dynamic_item;
        public TextView tv_bottom_dynamic_item;
        public TextView tv_type_dynamic_item;
        public TextView tv_price_dynamic_item;
        public LinearLayout ll_img_container_dynamic_item;
        public RelativeLayout rl_topic_dynamic_item;
        public ImageView iv_img_left;
        public TextView tv_bottom_title_topic_item;
        public TextView tv_view_count_topic_item;
        public LinearLayout ll_bottom_dynamic_item;
        public TextView tv_thumbup_topic_item;
        public TextView tv_comments_topic_item;
        public LinearLayout ll_price;
        public TextView tv_follow_dynamic_item;
        public ImageView iv_vip_header;
        public TextView iv_red_point_header;
        public TextView tv_buy;
        public TextView tv_right_item;

        public RelativeLayout rl_video_item;
        public ImageView iv_video_item;

        public RelativeLayout rl_bg_content_item;
        public TextView tv_content_bg_dynamic_item;
        public ImageView iv_content_bg_item;
        public GridView gv_dynamic_item;
        public RelativeLayout rl_bg_long_content_item;
        public TextView tv_content_long_content;
        public TextView tv_title_long_content;

        public TextView tv_share_topic_item;



        public JCaoStrandPlayer jc_type_item;


        /**
         * 话题
         */

        public FrameLayout fl_topic_img_item;
        public ImageView iv_topic_img;
        public TextView tv_title_topic_img;
        public TextView tv_viewcount_topic_img;

        public LinearLayout ll_bottom_item;

        /**
         * 话题有图内容
         */
        public TextView tv_content_bottom;

        public LinearLayout ll_no_pan;

        public TextView tv_bottom_home_item;
        public TextView iv_buy;


        /**
         * 视频 模块特殊样式
         */

        public LinearLayout ll_video_dy_item;
        public JCVideoPlayerStandard player_video;
        public TextView tv_video_title_item;
        public LinearLayout ll_top_click_video;
        public ImageView iv_head_dynamic_item_video;
        public ImageView iv_vip_header_video;
        public TextView tv_name_dynamic_item_video;
        public TextView tv_share_topic_item_video;
        public TextView tv_comments_topic_item_video;
        public TextView tv_thumbup_topic_item_video;


        /**
         * 头条样式
         */
        public FrameLayout ll_headline_dy_item;

        public LinearLayout ll_one_img_headline;
        public TextView tv_title_one_img;
        public TextView tv_bottom_one_img;
        public ImageView iv_right_one_img;

        public LinearLayout ll_more_img_headline;
        public TextView tv_title_more_img;
        public LinearLayout ll_headline_img_container;
        public TextView tv_bottom_more_img;

        public TextView tv_top_tag;
        public TextView tv_top_tag_1img;


        /**
         * 头部分割线
         */

        public View v_top_line_big;
        public View v_top_line_small;


        public TextView tv_detail_title;
        public LinearLayout ll_hot_comment;
        public TextView tv_thumup_hot_comment;
        public TextView tv_content_hot_item;
        public GridView gv_hot_comment_item;
        public LinearLayout ll_hot_comment_head;
        public LinearLayout ll_reward_comment;
        public GridView gv_reward_comment_item;
        public TextView tv_content_reward_item;
        public LinearLayout ll_comment_type;
        public GridView gv_comment_type_item;
        public TextView tv_content_comment_type_item;
        public RelativeLayout rl_video_hot_reply_item;
        public ImageView iv_video_hot_reply_item;
        public TextView tv_content_dynamic_item_copy;
    }



    private void setVideoHolder(MyHolder myHolder, View view) {
       myHolder.ll_video_dy_item = view.findViewById(R.id.ll_video_dy_item);
       myHolder.player_video = view.findViewById(R.id.player_video);
       myHolder.tv_video_title_item = view.findViewById(R.id.tv_video_title_item);
       myHolder.ll_top_click_video = view.findViewById(R.id.ll_top_click_video);
       myHolder.iv_head_dynamic_item_video = view.findViewById(R.id.iv_head_dynamic_item_video);
       myHolder.iv_vip_header_video = view.findViewById(R.id.iv_vip_header_video);
       myHolder.tv_name_dynamic_item_video = view.findViewById(R.id.tv_name_dynamic_item_video);
       myHolder.tv_share_topic_item_video = view.findViewById(R.id.tv_share_topic_item_video);
       myHolder.tv_comments_topic_item_video = view.findViewById(R.id.tv_comments_topic_item_video);
       myHolder.tv_thumbup_topic_item_video = view.findViewById(R.id.tv_thumbup_topic_item_video);
    }


    /**
     * 滑动监听
     */
    private AbsListView.OnScrollListener onScrollListener;
    private int firstVisible;//当前第一个可见的item
    private int visibleCount;//当前可见的item个数


    /**
     * 自动滑动播放视频功能
     */
    private void initListener() {
        onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //滑动停止自动播放视频
                        if("-2".equals(tagId)) {
                            autoPlayVideo(view, 0);
                        }else {
                            autoPlayVideoNew(view, 0);
                        }

                        break;

                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        break;
                }
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisible == firstVisibleItem) {
                    return;
                }

                firstVisible = firstVisibleItem;
                visibleCount = visibleItemCount;

            }
        };

        xfv_dynamic_fg.setOnAbsListViewScrollListener(onScrollListener);
    }

    /**
     * 滑动停止自动播放视频
     */
    private JCVideoPlayerStandard currPlayer;
    private void autoPlayVideo(AbsListView view,int upOrDown) {

        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.player_video) != null) {
                currPlayer = view.getChildAt(i).findViewById(R.id.player_video);

                Rect rect = new Rect();
                Rect rect1 = new Rect();
                //获取当前view 的 位置
//                currPlayer.getGlobalVisibleRect(rect);
                view.getChildAt(i).getLocalVisibleRect(rect);
                int videoheight = view.getChildAt(i).getHeight();
                Log.e("top--bottom",rect.top+"--"+rect.bottom+"--"+videoheight);
                //&&  rect.top ==0 && rect.bottom >= videoheight/2
                if ( rect.top <= (videoheight/3.0)   || rect.top == 0) {
                    if (currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {

                     //   JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED = true;
                        if(JCUtils.isWifiConnected(QCaApplication.getContext())) {
                            currPlayer.startButton.performClick();
                        }

                    }

                    return;

                }

            }
        }
        //释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
    }


    /**
     * 滑动停止自动播放视频
     */
    private JCVideoPlayerStandard currPlayerNew;
    private void autoPlayVideoNew(AbsListView view,int upOrDown) {
        Log.e("updown",upOrDown+"");
        for (int i = 0; i < visibleCount; i++) {
            if (view != null && view.getChildAt(i) != null && view.getChildAt(i).findViewById(R.id.jc_type_item) != null) {
                currPlayerNew = view.getChildAt(i).findViewById(R.id.jc_type_item);

                if(!currPlayerNew.isShown())continue;



                Rect rect = new Rect();
                view.getChildAt(i).getLocalVisibleRect(rect);
                int videoheight = view.getChildAt(i).getHeight();

                if ( rect.top <= (videoheight/3.0)   || rect.top == 0) {
                    if (currPlayerNew.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                            || currPlayerNew.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {

                        if(JCUtils.isWifiConnected(QCaApplication.getContext())) {
                            currPlayerNew.startButton.performClick();
                        }
                    }

                    return;

                }

            }
        }
        //释放其他视频资源
        JCVideoPlayer.releaseAllVideos();
    }

    private void setAudio(){
//        JCMediaManager.instance().mediaPlayer.set();

    }


}
