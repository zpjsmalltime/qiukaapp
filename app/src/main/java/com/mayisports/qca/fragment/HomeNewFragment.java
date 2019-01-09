package com.mayisports.qca.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.CoinDetailActivity;
import com.mayi.mayisports.activity.ComentsDetailsActivity;
import com.mayi.mayisports.activity.GroomOfHomeActivity;
import com.mayi.mayisports.activity.GroomPersonalActivity;
import com.mayi.mayisports.activity.GuessingCompetitionActivity;
import com.mayi.mayisports.activity.HomeItemDetailActivity;
import com.mayi.mayisports.activity.InforPostActivity;
import com.mayi.mayisports.activity.IntelligentBallSelectionActivity;
import com.mayi.mayisports.activity.LoginActivity;
import com.mayi.mayisports.activity.MatchDetailActivity;
import com.mayi.mayisports.activity.PersonalDetailActivity;
import com.mayi.mayisports.activity.PicShowActivity;
import com.mayi.mayisports.activity.PublishGroomActivity;
import com.mayi.mayisports.activity.ReplayCommentsActivity;
import com.mayi.mayisports.activity.SearchInfosActivity;
import com.mayi.mayisports.activity.SystemMsgActivity;
import com.mayi.mayisports.activity.TopicDetailActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.HomeDataNewBean;
import com.mayisports.qca.bean.MessageBean;
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
import com.mayisports.qca.view.MyPopuWindow;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 首页模块 123
 * Created by Zpj on 2017/12/5.
 */

public class HomeNewFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RadioGroup.OnCheckedChangeListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {


    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "home_fragment_action";
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
//       ToastUtils.toast("重载");
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


    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int result = intent.getIntExtra(LoginActivity.RESULT, 0);
            if(result == 99){
                String userId = intent.getStringExtra("userId")+"";
                int follow_status = intent.getIntExtra("follow_status", 0);
                delFollow(follow_status,userId);
                delRvFollow(follow_status,userId);
                myAdapter.notifyDataSetChanged();
            }else {
                initData();
                lv_home_fg.setSelection(0);
                Log.e("home", "home_initdata");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }

    private  View viewRoot;

    public XRefreshView xfv_home_fg;
    private ListView lv_home_fg;
    private ImageView iv_left_title_home;
    private ImageView iv_right_title_home;

    private RelativeLayout rl_load_layout;
    private RelativeLayout rl_load_clone ;

    private TextView rb_0_home_fg,rb_1_home_fg,rb_3_home_fg,rb_4_home_fg;

    private TextView et_search_home_fg;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(getContext(), R.layout.home_fg,null);
        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        flag = 1;

        delCache();

        requestNetData();
    }

    private void delCache() {
        String json = SPUtils.getString(this.getActivity(), Constant.HOME_PAGE_CACHE);
        if(!TextUtils.isEmpty(json)){
            rl_load_clone = null;
            if(flag == 1) {
                homeDataBean = JsonParseUtils.parseJsonClass(json, HomeDataNewBean.class);
                if(homeDataBean.meta != null){
                    saveTimeCount(homeDataBean.meta);
                }
                HomeDataNewBean newBean = JsonParseUtils.parseJsonClass(json, HomeDataNewBean.class);
                delData(homeDataBean,newBean);
                updateView();
            }
            myAdapter.notifyDataSetChanged();
        }

    }

    private HomeDataNewBean homeDataBean;
    private MyAdapter myAdapter = new MyAdapter();
    private int flag = 1;//1
    private int pageIndex = 1;
    private  int pre_page = 1;
    private void requestNetData() {
        //htp:/i.com/php/api.php?action=user_recommendation&type=recommendation_list&start=1
//        htp://20180109.dldemo.applinzi.com/php/api.php?action=index_page&loadMore=0&source=&network=&appstore=&uuid=
        String  url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        if(flag == 1){
            params.put("action","index_page");
            //下拉刷新
            params.put("loadmore",0+"");

            pageIndex = 1;//恢复加载更多页数
        }else{ //action=user_recommendation&type=recommendation_list&start=2
//           params.put("action","user_recommendation");
//           params.put("type","recommendation_list");
            params.put("action","index_page");
            //下拉刷新
           params.put("start",pageIndex++);
        }

        StringBuilder urlParams = params.getUrlParams();
        Log.e("url_home",urlParams.toString());
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {
                if(xfv_home_fg != null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }
                  if(flag == 1) {
                      homeDataBean = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                      SPUtils.putString(QCaApplication.getContext(), Constant.HOME_PAGE_CACHE,string);
                      if(homeDataBean.meta != null){
                          saveTimeCount(homeDataBean.meta);
                      }
                      HomeDataNewBean newBean = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                      delData(homeDataBean,newBean);
                      updateView();
                      pre_page = 1;
                  }else{
                    try{
                        HomeDataNewBean homeDataBeanNew = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                        HomeDataNewBean newBean = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                        delData(homeDataBeanNew,newBean);
                        homeDataBean.data.recommendation.addAll(homeDataBeanNew.data.recommendation);
                    }catch (Exception e){
//                        ToastUtils.toast("暂无更多数据");
                        pre_page = 0;
                    }
                  }
                  myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                if(xfv_home_fg !=  null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_clone = rl_load_layout;
            }
        });
    }

    /**
     * 保存更新时间
     * @param meta
     */
    private void saveTimeCount(HomeDataNewBean.MetaBean meta) {
        SPUtils.putInt(this.getContext(),Constant.Match_Thread_Interval,meta.match_thread_interval);
        SPUtils.putInt(this.getContext(),Constant.Banner_Interval,meta.banner_interval);
        SPUtils.putInt(this.getContext(),Constant.Sys_Msg_Interval,meta.sys_msg_interval);

        int anInt = SPUtils.getInt(this.getContext(), Constant.Banner_Interval);
        if(anInt == 0)anInt = 4;
        if(banner != null) banner.setDelayTime(1000*anInt);
    }

    /**
     * 处理数据  type 1 显示
     * @param homeDataBean
     * @param newBean
     */
    private void delData(HomeDataNewBean homeDataBean, HomeDataNewBean newBean) {
        if(homeDataBean.data != null && homeDataBean.data.recommendation != null){
             homeDataBean.data.recommendation.clear();
             for(int i = 0;i<newBean.data.recommendation.size();i++){

                 HomeDataNewBean.DataBeanX.RecommendationBeanX beanX = newBean.data.recommendation.get(i);
                 if(beanX.type == 1 || beanX.type == 3 ||beanX.type == 2 || beanX.type == 4 ||beanX.type == 5) {//1推荐单  3 话题  2情报站
                     if(beanX.type == 5){
                         mlist = beanX.list;
                     }
                     homeDataBean.data.recommendation.add(beanX);
                 }
             }
        }
    }

    private List<String> strings = new ArrayList<>();
    private void updateView() {

       if(homeDataBean != null && homeDataBean.meta != null) {
           String default_text = homeDataBean.meta.default_text;
           if (TextUtils.isEmpty(default_text)) {
               default_text = "搜索";
           }

           et_search_home_fg.setHint(default_text);
       }else{
           et_search_home_fg.setHint("搜索");
       }

        if(homeDataBean != null && homeDataBean.data!= null && homeDataBean.data.bannerList != null){
            banner.setVisibility(View.VISIBLE);
            strings.clear();
            for(int i = 0;i<homeDataBean.data.bannerList.size();i++){
                strings.add(homeDataBean.data.bannerList.get(i).banner+","+homeDataBean.data.bannerList.get(i).topic_id+","+homeDataBean.data.bannerList.get(i).type);
            }

            if(homeDataBean.meta.toolbar_display == 1){
                ll_bar_home_header.setVisibility(View.VISIBLE);
            }else{
                ll_bar_home_header.setVisibility(View.GONE);
            }

            banner.setImages(strings);
            banner.start();

            PictureUtils.show(homeDataBean.data.bannerList.get(0).banner,id_banner);
        }else{
            banner.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
          switch (i){
              case R.id.rb_0_home_fg:
                  WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/quizContest","球咖");
                  break;
              case R.id.rb_1_home_fg:
                  WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/peopleFocused","战绩红人");
                  break;
              case R.id.rb_2_home_fg:
//                  WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/quizContest","球咖");
                  return;
              case R.id.rb_3_home_fg:
                  WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/matchLeida","智能选球");
                  break;
          }

    }


    private MessageBean messageBean;
    private void requestMessage() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","new_msg");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                messageBean = JsonParseUtils.parseJsonClass(string,MessageBean.class);
                if(messageBean != null && messageBean.data != null){
                    int count = messageBean.data.msg.count;
                    if(count <=0){
                        tv_msg_count_home.setVisibility(View.INVISIBLE);
                    }else if(count <=9){
                        tv_msg_count_home.setVisibility(View.VISIBLE);
                        tv_msg_count_home.setText(count+"");
                    }else{
                        tv_msg_count_home.setVisibility(View.VISIBLE);
                        tv_msg_count_home.setText("9+");
                    }
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    MyPopuWindow myPopuWindow ;
    private void initView() {

       rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);
       rl_load_clone = rl_load_layout;

        initReceiver();

        myPopuWindow = new MyPopuWindow(this.getActivity(),this,0,0);
        tv_msg_count_home = viewRoot.findViewById(R.id.tv_msg_count_home);
        xfv_home_fg = viewRoot.findViewById(R.id.xfv_home_fg);
        lv_home_fg = viewRoot.findViewById(R.id.lv_home_fg);

        iv_left_title_home = viewRoot.findViewById(R.id.iv_left_title_home);
        iv_right_title_home = viewRoot.findViewById(R.id.iv_right_title_home);
        iv_left_title_home.setOnClickListener(this);
        iv_right_title_home.setOnClickListener(this);

        xfv_home_fg.setPullRefreshEnable(true);//设置允许下拉刷新
        xfv_home_fg.setMoveForHorizontal(true);


        xfv_home_fg.setXRefreshViewListener(new MyListener());

        addHeaderForListView();

        lv_home_fg.setAdapter(myAdapter);
        lv_home_fg.setOnItemClickListener(this);

        et_search_home_fg = viewRoot.findViewById(R.id.et_search_home_fg);
//        et_search_home_fg.setEnabled(false);

        et_search_home_fg.setOnClickListener(this);

    }

    private View header;
//    private Banner id_banner;
    private ImageView id_banner;
    private TextView tv_more_count_home;
    private LinearLayout ll_top_container_home;
    private RelativeLayout ll_more_count;
    public TextView tv_msg_count_home;
//    private RadioGroup rg_home_fg;
    private LinearLayout ll_bar_home_header;


    public com.youth.banner.Banner banner;

    private boolean isHideFirst = false;

    private void addHeaderForListView() {
         header = View.inflate(getContext(),R.layout.home_header_fg,null);
         id_banner = header.findViewById(R.id.id_banner);
        ll_bar_home_header = header.findViewById(R.id.ll_bar_home_header);

        banner = header.findViewById(R.id.banner);
        banner.setImageLoader(new ImageLoaderType());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = (int) (DisplayUtil.getScreenWidth(this.getActivity())*0.32);
        banner.setLayoutParams(layoutParams);

        int anInt = SPUtils.getInt(this.getContext(), Constant.Banner_Interval);
        if(anInt == 0)anInt = 4;
        banner.setDelayTime(1000*anInt);


        Banner banner = this.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String topic_id = strings.get(position).split(",")[1];
//                WebViewActivtiy.start(HomeNewFragment.this.getActivity(),Constant.BASE_URL+"#/topic/"+topic_id,"话题");
                String type = strings.get(position).split(",")[2];

                if("2".equals(type)){
                    String title = homeDataBean.data.bannerList.get(position).title;
                    WebViewActivtiy.start(HomeNewFragment.this.getActivity(),title,"球咖",true,false);
                }else if("1".equals(type)){
                    TopicDetailActivity.start(HomeNewFragment.this.getActivity(), topic_id);
                }

            }
        });


        rb_0_home_fg = header.findViewById(R.id.rb_0_home_fg);
        rb_1_home_fg = header.findViewById(R.id.rb_1_home_fg);
        rb_3_home_fg = header.findViewById(R.id.rb_3_home_fg);
        rb_4_home_fg = header.findViewById(R.id.rb_4_home_fg);
        rb_0_home_fg.setOnClickListener(this);
        rb_1_home_fg.setOnClickListener(this);
        rb_3_home_fg.setOnClickListener(this);
        rb_4_home_fg.setOnClickListener(this);

        tv_more_count_home = header.findViewById(R.id.tv_more_count_home);
        ll_top_container_home = header.findViewById(R.id.ll_top_container_home);
        ll_more_count = header.findViewById(R.id.ll_more_count);
        ll_more_count.setOnClickListener(this);

//        rg_home_fg = header.findViewById(R.id.rg_home_fg);
//        rg_home_fg.setOnCheckedChangeListener(this);


        if(isHideFirst){
            rb_0_home_fg.setVisibility(View.GONE);
        }else{
            rb_0_home_fg.setVisibility(View.VISIBLE);
        }

         lv_home_fg.addHeaderView(header);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.id_banner:
                String topic_id = homeDataBean.data.bannerList.get(0).topic_id;
//                 WebViewActivtiy.start(HomeNewFragment.this.getActivity(),Constant.BASE_URL+"#/topic/"+topic_id,"话题");
                TopicDetailActivity.start(this.getActivity(),topic_id);
                break;
            case R.id.iv_left_title_home:
//                ToastUtils.toast("系统消息");
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    if(myPopuWindow !=null)myPopuWindow.dismiss();
                    return;
                }
                if(tv_msg_count_home.isShown() || Utils.isLogin()){
//                  WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/sysMessage","系统消息");
                    tv_msg_count_home.setVisibility(View.GONE);
                    SystemMsgActivity.start(this.getActivity());
                }
                //#/sysMessage
                break;
            case R.id.iv_right_title_home:
                int px = DisplayUtil.dp2px(this, 10);
                myPopuWindow.showAsDropDown(iv_right_title_home,px,0);
                break;
            case R.id.ll_publish_home:
                if(myPopuWindow !=null)myPopuWindow.dismiss();
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }

//               WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/selectMatch/1","发布推荐");
                PublishGroomActivity.start(this.getActivity());
                break;
            case R.id.ll_my_publish_home:
                if(myPopuWindow !=null)myPopuWindow.dismiss();
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);

                    return;
                }
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/myRecommendation","我的推荐");
                break;
            case R.id.ll_search_home:
                if(myPopuWindow !=null)myPopuWindow.dismiss();
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/searchQiuka","搜索球咖");
                break;
            case R.id.ll_vip_home://申请认证  #/applyV
                if(myPopuWindow !=null)myPopuWindow.dismiss();
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);

                    return;
                }
                //http://app.mayisports.com/applyV/applyV.html?hideDownloadBar=1
//                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/applyV","申请认证");
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"applyV/applyV.html?hideDownloadBar=1",true,"申请认证");
                break;
            case R.id.ll_more_count:
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/information","情报站");
                InforPostActivity.start(getActivity());
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
                    HomeDataNewBean.DataBeanX.RecommendationBeanX beanX = homeDataBean.data.recommendation.get(clickPosition - 1);
                    beanX.recommendation.buy = 1;
                    myAdapter.notifyDataSetChanged();
                    goDetail(beanX);
                }else{//充值
                    Intent intent = new Intent(HomeNewFragment.this.getActivity(), CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rb_0_home_fg://竞猜大赛
//                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/quizContest","球咖");
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }
                GuessingCompetitionActivity.start(this.getActivity());
                break;
            case R.id.rb_1_home_fg://情报站
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/information","情报站");
//                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/peopleFocused","战绩红人");
                InforPostActivity.start(getActivity());
                break;
            case R.id.rb_2_home_fg:
//                  WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/quizContest","球咖");
                return;
            case R.id.rb_3_home_fg:
//                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL + "#/matchLeida","智能选球");
                IntelligentBallSelectionActivity.start(getActivity());
                break;

            case R.id.rb_4_home_fg://专家推荐
                getActivity().startActivity(new Intent(getActivity(), GroomOfHomeActivity.class));
                break;
            case R.id.et_search_home_fg://搜索界面
                SearchInfosActivity.start(this.getActivity(),et_search_home_fg.getHint()+"");
                break;
        }
    }


    public static void goWebViewActivity(Activity activity,String url ,String title){

    }

    private int clickPosition;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i == 0)return;
        HomeDataNewBean.DataBeanX.RecommendationBeanX bean = homeDataBean.data.recommendation.get(i - 1);

        if(bean.type == 1){//推荐单
            delType2Click(bean,i);
        }else if(bean.type == 3){//话题
            ComentsDetailsActivity.startForResult(2,HomeNewFragment.this, bean.comment.comment_id,i-1);
        }else if(bean.type == 2){//情报站
            HomeDataNewBean.DataBeanX.RecommendationBeanX.DataBean dataBean = bean.data.get(0);
            MatchDetailActivity.start(this.getActivity(),dataBean.match.betId,2);
        }

    }


    private void delType2Click(HomeDataNewBean.DataBeanX.RecommendationBeanX bean, int i) {
        String price = bean.recommendation.price;
        String nickname = bean.user.nickname;
        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

//        bean.count = "0";
        myAdapter.notifyDataSetChanged();
        if(bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0) || "COMPLETE".equals(bean.match.status)|| SPUtils.getString(this.getContext(),Constant.USER_ID).equals(bean.user.user_id)){//已购买 //或者免费
            Intent intent = new Intent(this.getActivity(), HomeItemDetailActivity.class);
            intent.putExtra("userId",bean.user.user_id);
            intent.putExtra("betId",bean.recommendation.betId);
            startActivity(intent);
        }else{

//            new ToastPricePopuWindow(this.getActivity(),this,)
            clickPosition = i;
            requestToast(bean.user.user_id,bean.recommendation.betId);
        }
    }

    private void goDetail(HomeDataNewBean.DataBeanX.RecommendationBeanX bean){
        Intent intent = new Intent(this.getActivity(), HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
        intent.putExtra("betId",bean.recommendation.betId);
        startActivity(intent);
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
              toastPricePopuWindow =   new ToastPricePopuWindow(HomeNewFragment.this.getActivity(),HomeNewFragment.this,title,type);
                toastPricePopuWindow.showAtLocation(lv_home_fg, Gravity.CENTER,0,0);
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
    class MyListener extends XRefreshView.SimpleXRefreshListener {


        @Override
        public void onRefresh(boolean isPullDown) {
                rl_load_clone = null;
                flag = 1;
                requestNetData();
        }
    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(homeDataBean != null && homeDataBean.data!= null &&homeDataBean.data.recommendation!= null)return homeDataBean.data.recommendation.size();
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
            final   HomeDataNewBean.DataBeanX.RecommendationBeanX bean = homeDataBean.data.recommendation.get(i);

            if(bean.type == 5){
                view = View.inflate(QCaApplication.getContext(),R.layout.recy_type5_layout_item,null);
                view.setTag(null);

                RecyclerView rv_item = view.findViewById(R.id.rv_item);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_item.setLayoutManager(linearLayoutManager);
                rv_item.setHasFixedSize(true);

                LinearLayout ll_click_ry_item = view.findViewById(R.id.ll_click_ry_item);
                ll_click_ry_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GroomPersonalActivity.start(HomeNewFragment.this.getActivity());
                    }
                });

                rv_item.setAdapter(new MyRvType5Adapter(bean.list));
            }else if(bean.type == 4){
                view = View.inflate(QCaApplication.getContext(),R.layout.recy_layout_item,null);
                view.setTag(null);

                RecyclerView rv_item = view.findViewById(R.id.rv_item);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rv_item.setLayoutManager(linearLayoutManager);
                rv_item.setHasFixedSize(true);

                rv_item.setAdapter(new MyRvAdapter(bean.list));

            }else if(bean.type == 2){//情报站
                view = View.inflate(QCaApplication.getContext(),R.layout.infor_home_item_layout,null);
                TextView tv_more_count_home = view.findViewById(R.id.tv_more_count_home);
                ImageView iv_host_icon_home_detail = view.findViewById(R.id.iv_host_icon_home_detail);
                TextView tv_host_name_home_detail = view.findViewById(R.id.tv_host_name_home_detail);
                TextView tv_cent_top_home_detail = view.findViewById(R.id.tv_cent_top_home_detail);
                TextView tv_cent_bottom_home_detail = view.findViewById(R.id.tv_cent_bottom_home_detail);
                ImageView iv_away_icon_home_detail = view.findViewById(R.id.iv_away_icon_home_detail);
                TextView tv_away_name_home_detail = view.findViewById(R.id.tv_away_name_home_detail);
                LinearLayout ll_tag_container = view.findViewById(R.id.ll_tag_container);
                TextView tv_tag_1 = view.findViewById(R.id.tv_tag_1);
                TextView tv_tag_2 = view.findViewById(R.id.tv_tag_2);
                LinearLayout ll_to_more = view.findViewById(R.id.ll_to_more);

                view.setTag(null);
                final HomeDataNewBean.DataBeanX.RecommendationBeanX.DataBean dataBean = bean.data.get(0);

                if(bean.data !=null&&bean.data.size()>0) {
                    int i1 = bean.information_more - 1;
                    if(i1 <1){
                        tv_more_count_home.setText("");
                    }else{
                        tv_more_count_home.setText("更多"+i1+"场");
                        ll_to_more.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                InforPostActivity.start(HomeNewFragment.this.getActivity());
                            }
                        });
                    }


                    if (!TextUtils.isEmpty(dataBean.match.logoH)&&!"0".equals(dataBean.match.logoH)) {
                        String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_" + dataBean.match.logoH + ".png";
                        PictureUtils.show(hostUrl, iv_host_icon_home_detail);
                    }

                    if (!TextUtils.isEmpty(dataBean.match.logoG)&&!"0".equals(dataBean.match.logoG)) {
                        String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_" + dataBean.match.logoG + ".png";
                        PictureUtils.show(hostUrl, iv_away_icon_home_detail);
                    }


                    tv_host_name_home_detail.setText(URLDecoder.decode(dataBean.match.hostTeamName));
                    tv_away_name_home_detail.setText(URLDecoder.decode(dataBean.match.awayTeamName));

                    tv_cent_top_home_detail.setText(URLDecoder.decode(dataBean.match.leagueName));

                    String s = dataBean.match.match_time + "000";
                    Long time = Long.valueOf(s);
                    String matchStartTime = Utils.getMatchStartTime(time);
                    tv_cent_bottom_home_detail.setText(matchStartTime);


                    tv_tag_1.setVisibility(View.GONE);
                    tv_tag_2.setVisibility(View.GONE);
                    if(dataBean.infoList != null){
                        try {
                            for (int j = 0; j < dataBean.infoList.size(); j++) {
                                if (j > 1) break;
                                String title = dataBean.infoList.get(j).title;
                                int i3 = title.indexOf("【");
                                int i2 = title.indexOf("】");
                                String substring = title.substring(i3 + 1, i2);
                                if(j == 0) {
                                    tv_tag_1.setText(substring);
                                    tv_tag_1.setVisibility(View.VISIBLE);
                                }

                                if(j == 1) {
                                    tv_tag_2.setText(substring);
                                    tv_tag_2.setVisibility(View.VISIBLE);
                                }
                            }
                        }catch (Exception e){

                        }
                    }



                }

            }else {
                final MyHolder myHolder;
                if (view == null || view.getTag() == null) {
                    myHolder = new MyHolder();
                    view = View.inflate(QCaApplication.getContext(), R.layout.dynamic_item, null);
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
                    myHolder.tv_title_long_content = view.findViewById(R.id.tv_title_long_content);
                    myHolder.tv_content_long_content = view.findViewById(R.id.tv_content_long_content);
                    myHolder.tv_share_topic_item = view.findViewById(R.id.tv_share_topic_item);
                    myHolder.tv_content_bottom = view.findViewById(R.id.tv_content_bottom);
                    view.setTag(myHolder);
                } else {
                    myHolder = (MyHolder) view.getTag();
                }


//                myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(HomeNewFragment.this.getActivity(), PersonalDetailActivity.class);
//                        intent.putExtra("id", bean.user.user_id + "");
//                        intent.putExtra("position", i);
//                        startActivityForResult(intent, 1);
//                    }
//                });
                myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(HomeNewFragment.this.getActivity(), PersonalDetailActivity.class);
                        intent.putExtra("id", bean.user.user_id + "");
                        intent.putExtra("position", i);
                        String json = new Gson().toJson(bean.user);
                        PersonalDetailActivity.start(HomeNewFragment.this,bean.user.user_id,i,1,json);
//                        startActivityForResult(intent, 1);
                    }
                });


                PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item);
                myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
                ViewStatusUtils.addTags(myHolder.ll_top_tag_container, bean.user.tag, bean.user.tag1);

//            if(!TextUtils.isEmpty(bean.count)&&Integer.valueOf(bean.count) >=2){
//                myHolder.iv_red_point_header.setVisibility(View.VISIBLE);
//            }else{
                myHolder.iv_red_point_header.setVisibility(View.GONE);
//            }


                if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                    myHolder.iv_vip_header.setVisibility(View.VISIBLE);
                } else {
                    myHolder.iv_vip_header.setVisibility(View.GONE);
                }

                if (false&&bean.user.follow_status == 0 && !(SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(bean.user.user_id))) {//系统推荐，显示关注
//            if(bean.follow != 1){

                    myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                    myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            requestFollows(i);
                        }
                    });


                    if (bean.user.follow_status == 1) {
//                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                        myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                        myHolder.tv_follow_dynamic_item.setText("已关注");
                        myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                    } else {
//                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
//                    myHolder.tv_follow_dynamic_item.setText(" 关注 ");
                        myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                        myHolder.tv_follow_dynamic_item.setText("+关注");
//                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                    }


                } else {
                    myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                    myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);

                    if (bean.recommendation != null && !TextUtils.isEmpty(bean.recommendation.create_time)) {
                        String createTime = Utils.getCreateTime(Long.valueOf(bean.recommendation.create_time + "000"));
                        myHolder.tv_time_dynamic_item.setText(createTime);
                    } else if (!TextUtils.isEmpty(bean.create_time)) {
                        String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                        myHolder.tv_time_dynamic_item.setText(createTime);
                    } else {
                        myHolder.tv_time_dynamic_item.setText("");
                    }


                }


                if (bean.type == 1) {//推荐单
                    myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                    myHolder.ll_bottom_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);

                    bindType2(i,bean, myHolder);

                } else if (bean.type == 3) {//话题
                    myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);

                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);


                    bindType1(i,bean, myHolder);
                }

            }

            if (i == getCount() - 3 || (getCount() < 3)) {
                if (pre_page == 1) {
                    rl_load_clone = null;
                    flag = 0;
                    requestNetData();
                }
            }
            return view;
        }
    }
    static class MyHolder{
        public ImageView iv_head_home_item;
        public TextView tv_name_home_item;
        public TextView tv_time_home_item;
        public TextView tv_content_home_item;
        public TextView tv_bottom_home_item;

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
        public TextView tv_title_long_content;
        public TextView tv_content_long_content;

        public View v_click;
        public TextView tv_share_topic_item;

        public TextView tv_content_bottom;
    }

    private List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean> mlist;
    private MyRvType5Adapter myRvType5Adapter;
    class MyRvType5Adapter extends RecyclerView.Adapter<RvHolder> {

        private List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean> list;
        public MyRvType5Adapter(List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean> list) {
            HomeNewFragment.this.mlist = list;
            this.list = list;
            myRvType5Adapter = this;
        }

        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RvHolder rvHolder = new RvHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.type5_cy_item_layout, parent,
                    false));
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(final RvHolder holder, final int position) {
            final HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean listBean = list.get(position);

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
                holder.tv_follow_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                holder.tv_follow_item.setTextColor(Color.parseColor("#e8e8e8"));
                holder.tv_follow_item.setText("已关注");
            } else {
                holder.tv_follow_item.setTag(0);
                holder.tv_follow_item.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                holder.tv_follow_item.setTextColor(Color.parseColor("#282828"));
                holder.tv_follow_item.setText(" 关注 ");
            }

            holder.tv_follow_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(LoginUtils.isUnLogin()){
                        LoginUtils.goLoginActivity(HomeNewFragment.this.getActivity(),ACTION);
                        return;
                    }

                    if(holder.tv_follow_item.getTag() == null)return;
                    int b1 = (int) holder.tv_follow_item.getTag();
                    if(b1 == 1){//取消关注
//                        requestFollows("delete",listBean,MyRvType5Adapter.this);
                        ToastUtils.toast("您已关注该用户");
                    }else{//关注
                        requestFollows("add",listBean,MyRvType5Adapter.this);
                    }
                    return;
                }
            });

            holder.rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(listBean);
                    PersonalDetailActivity.start(HomeNewFragment.this,listBean.user_id,position,3,json);
//                    PersonalDetailActivity.start(HomeNewFragment.this.getActivity(),listBean.user_id);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    /**
     * 可能感兴趣的人 关注方法
     * @param type
     * @param listBean
     * @param myRvType5Adapter
     */
    public void requestFollows(String type, final HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean listBean, final MyRvType5Adapter myRvType5Adapter){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        params.put("type",type);
        params.put("user_id",listBean.user_id+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        listBean.follow_status = 1;
                        NotificationsUtils.checkNotificationAndStartSetting(HomeNewFragment.this.getActivity(),lv_home_fg);
                    }else{
                        listBean.follow_status = 0;
                    }
                    delFollow(listBean.follow_status,listBean.user_id);
                    myRvType5Adapter.notifyDataSetChanged();
                    myAdapter.notifyDataSetChanged();

                    DynamicFragment.sendFoucsData();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    class MyRvAdapter extends RecyclerView.Adapter<RvHolder>{

        private List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean> list;
        public MyRvAdapter(List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean> list){
            this.list = list;
        }

        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RvHolder rvHolder = new RvHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.item_img_topic_layout, parent,
                    false));
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(RvHolder holder, final int position) {

            final HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean bean = list.get(position);
            PictureUtils.show(bean.background_img,holder.iv_img_rv_item);
            holder.tv_title_rv_item.setText("#"+bean.title);
            holder.iv_img_rv_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity.start(HomeNewFragment.this.getActivity(),bean.id);
                }
            });


        }

        @Override
        public int getItemCount() {
//            int length = immediateScoreBean.data.tags.getClass().getSuperclass().getFields().length;
//            int le =  immediateScoreBean.data.tags.getClass().getFields().length - length;

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
     * 同步关注状态
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1&&resultCode == 1){//个人详情

            int follow_status = data.getIntExtra("follow_status", 0);
            int position = data.getIntExtra("position", -1);
            if(position != -1) {
                HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user = homeDataBean.data.recommendation.get(position).user;
                if(user.follow_status == follow_status)return;
                user.follow_status = follow_status;
                delFollow(follow_status, user.user_id);
                delRvFollow(follow_status,user.user_id);
                myAdapter.notifyDataSetChanged();
            }

        }else if(requestCode == 2&&resultCode == 2){//话题详情
            int position = data.getIntExtra("position", -1);
            String praise_count = data.getStringExtra("praise_count");

            if(position != -1) {
                HomeDataNewBean.DataBeanX.RecommendationBeanX beanX = homeDataBean.data.recommendation.get(position);
                boolean isOk = false;


                 int  follow_status  = Integer.valueOf(data.getStringExtra("follow_status"));

                if(beanX.comment.praise_count.equals(praise_count) && (beanX.user.follow_status == follow_status))return;
                int praise_status = data.getIntExtra("praise_status", 0);
                beanX.comment.praise_count = praise_count;
                beanX.comment.praise_status = praise_status;
                beanX.user.follow_status = follow_status;
                delFollow(follow_status,beanX.user.user_id);
                delRvFollow(follow_status,beanX.user.user_id);
                myAdapter.notifyDataSetChanged();
            }

        }else if(requestCode == 3 && resultCode == 1){
            int follow_status = data.getIntExtra("follow_status", 0);
            int position = data.getIntExtra("position", -1);
            if(position != -1) {

                if(mlist != null && myRvType5Adapter != null) {
                    HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean listBean = mlist.get(position);
                    if(listBean.follow_status == follow_status)return;
                     listBean.follow_status = follow_status;
                     delFollow(follow_status,listBean.user_id);
                     myRvType5Adapter.notifyDataSetChanged();
                     myAdapter.notifyDataSetChanged();
//                     mlist = null;
//                     myRvType5Adapter = null;
                }
            }
        }


    }

    /**
     * 处理del 首页推荐用户状态
     * @param follow_status
     * @param user_id
     */
    private void delRvFollow(int follow_status, String user_id) {

        if(mlist == null)return;

        for(int i = 0;i<mlist.size();i++){
            HomeDataNewBean.DataBeanX.RecommendationBeanX.ListBean bean = mlist.get(i);
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
        for(int i = 0;i<homeDataBean.data.recommendation.size();i++){
            HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user = homeDataBean.data.recommendation.get(i).user;
            int type1 = homeDataBean.data.recommendation.get(i).type;
            if(type1 == 2 ||type1 == 4 ||type1 == 5)continue;
            String user_id = user.user_id;
            if(user_id.equals(userId)){
                user.follow_status = type;
            }
        }
    }

    public void requestFollows(int i){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(),ACTION);
            return;
        }

        final HomeDataNewBean.DataBeanX.RecommendationBeanX bean = homeDataBean.data.recommendation.get(i);
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";
        if(bean.user.follow_status == 1){
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
//                        myHolder.tv_follow_dynamic_item.setTag(1);
//                        myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
//                        myHolder.tv_follow_dynamic_item.setText("已关注");
                        delFollow(1,bean.user.user_id);
                        delRvFollow(1,bean.user.user_id);
                        NotificationsUtils.checkNotificationAndStartSetting(HomeNewFragment.this.getActivity(),lv_home_fg);
                    }else{
                        bean.user.follow_status = 0;
//                        myHolder.tv_follow_dynamic_item.setTag(0);
//                        myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
//                        myHolder.tv_follow_dynamic_item.setText(" 关注 ");
                        delFollow(0,bean.user.user_id);
                        delFollow(0,bean.user.user_id);
                    }

                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    /**
     * 话题
     *
     * @param bean
     * @param myHolder
     */
    private void bindType1(final int position, final HomeDataNewBean.DataBeanX.RecommendationBeanX bean, MyHolder myHolder) {

        if (!TextUtils.isEmpty(bean.comment.comment)) {

            bean.comment.comment =  bean.comment.comment.replaceAll("&nbsp;"," ").replaceAll("<br/>"," ").replaceAll("\n"," ").replaceAll("<[^>]*>|\\n","");

            if (TextUtils.isEmpty(bean.comment.background_img)) {//文章或者观点

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
                }

            } else {
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.VISIBLE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                myHolder.tv_content_bg_dynamic_item.setText(bean.comment.title);
                myHolder.tv_content_bottom.setText(bean.comment.comment+"");
                PictureUtils.show(bean.comment.background_img, myHolder.iv_content_bg_item);
            }

        } else {
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
            myHolder.rl_bg_content_item.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                myHolder.tv_title_long_content.setText(bean.comment.title);
                myHolder.tv_content_long_content.setText(bean.comment.comment);

            }
        }

        if (!TextUtils.isEmpty(bean.comment.view_count)) {
            String s = Utils.parseIntToK(Integer.valueOf(bean.comment.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 " + s);
        }

//        myHolder.tv_content_dynamic_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {  //#/topicViewDetail/36979
////                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
////                WebViewActivtiy.start(HomeNewFragment.this.getActivity(),url,"球咖");
////                ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
//                ComentsDetailsActivity.startForResult(2,HomeNewFragment.this,bean.comment.comment_id,position);
//            }
//        });
//        myHolder.iv_content_bg_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
//                ComentsDetailsActivity.startForResult(2,HomeNewFragment.this, bean.comment.comment_id,position);
//            }
//        });
//        myHolder.rl_bg_long_content_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
//                ComentsDetailsActivity.startForResult(2,HomeNewFragment.this, bean.comment.comment_id,position);
//            }
//        });


        if (bean.comment.imgListEx != null) {//http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif

            if (bean.comment.imgListEx.size() > 0) {
                if ("video".equals(bean.comment.imgListEx.get(0).type)) {
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.VISIBLE);
                    HomeDataNewBean.DataBeanX.RecommendationBeanX.ImageList imageList = bean.comment.imgListEx.get(0);
                    String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                    PictureUtils.show(imgUrl, myHolder.iv_video_item);
                    myHolder.rl_video_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
                            ComentsDetailsActivity.startForResult(true,2,HomeNewFragment.this, bean.comment.comment_id,position);
                        }
                    });


                } else {
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.VISIBLE);

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

                    myHolder.gv_dynamic_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String urls = "";
                            for (int i = 0; i < bean.comment.imgListEx.size(); i++) {
                                String url = bean.comment.imgListEx.get(i).url;
                                String delUrl = Utils.delUrl(url);
                                urls += delUrl + ",";
                            }

                            String substring = urls.substring(0, urls.length() - 1);
                            Intent intent = new Intent(HomeNewFragment.this.getActivity(), PicShowActivity.class);
                            intent.putExtra("urls", substring);
                            intent.putExtra("position", position);
                            startActivity(intent);
                        }
                    });

                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);

                }
            } else {
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
            }



        }

        if (bean.topic != null) {
            myHolder.rl_topic_dynamic_item.setVisibility(View.VISIBLE);
            PictureUtils.show(bean.topic.background_img, myHolder.iv_img_left);
            myHolder.tv_bottom_title_topic_item.setText(bean.topic.title);
            myHolder.rl_topic_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity.start(HomeNewFragment.this.getActivity(),bean.topic.topic_id);
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
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(HomeNewFragment.this.getActivity(), ACTION);
                    return;
                }
//                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
//                WebViewActivtiy.start(HomeNewFragment.this.getActivity(),url,"球咖");
                ComentsDetailsActivity.startForResult(2,HomeNewFragment.this.getActivity(), bean.comment.comment_id,position,true);
//                if(LoginUtils.isUnLogin()){
//                    LoginUtils.goLoginActivity(HomeNewFragment.this.getActivity(),HomeNewFragment.ACTION);
//                    return;
//                }
//                ReplayCommentsActivity.start(HomeNewFragment.this.getActivity(),bean.comment.comment_id);
            }
        });
        if(bean.topic != null){
            myHolder.rl_topic_dynamic_item.setVisibility(View.VISIBLE);
            PictureUtils.show(bean.topic.background_img,myHolder.iv_img_left);
            myHolder.tv_bottom_title_topic_item.setText(bean.topic.title);
            myHolder.rl_topic_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity.start(HomeNewFragment.this.getActivity(),bean.topic.topic_id);
                }
            });
        }

        myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
                showShare(position);
            }
        });

        if (bean.comment.praise_status == 1) {//点赞
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable, null, null, null);
        }

        DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item,bean.comment.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count + "");
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.comment.reply_count+"");
//        myHolder.tv_comments_topic_item.setText(bean.comment.reply_count + "");

    }


    private void showShare(final int position) {
        SharePopuwind sharePopuwind = new SharePopuwind(this.getActivity(), new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                HomeDataNewBean.DataBeanX.RecommendationBeanX beanX = homeDataBean.data.recommendation.get(position);
                if (beanX.topic == null) {
                    List<String> urls = new ArrayList<>();
                    List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ImageList> imgListEx = beanX.comment.imgListEx;
                    if(imgListEx != null) {
                        for (int i = 0;i<imgListEx.size();i++) {
                            urls.add(imgListEx.get(i).url);
                        }
                    }
//                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname,urls, HomeNewFragment.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname,HomeNewFragment.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_home_fg, Gravity.CENTER, 0, 0);

    }


    class MyGvAdapter extends BaseAdapter{


        private List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ImageList> imgListEx;

        public MyGvAdapter(List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ImageList> imgListEx) {
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
            HomeDataNewBean.DataBeanX.RecommendationBeanX.ImageList imglist = imgListEx.get(position);

            View view = View.inflate(QCaApplication.getContext(),R.layout.img_gif_layout,null);
            ImageView imageView = view.findViewById(R.id.iv);
            TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
            Utils.showType(imglist.url,imageView,tv_gif_icon,imglist.size);


            if(getCount() == 1){
               Utils.scaleImg(HomeNewFragment.this.getActivity(),view,imageView,imglist.size);
            }
//            if(getCount() == 4){
//                Utils.scaleImg(HomeNewFragment.this.getActivity(),view,imageView,imglist.size);
//            }
            return view;
        }

        public void setData(List<HomeDataNewBean.DataBeanX.RecommendationBeanX.ImageList> data) {
            imgListEx = data;
        }
    }


    /**
     * 处理点赞
     * @param bean
     */
    private void requestThumbup(final HomeDataNewBean.DataBeanX.RecommendationBeanX  bean) {

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
                    myAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.toast("点赞失败");
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
    private void bindType2(final  int position,final HomeDataNewBean.DataBeanX.RecommendationBeanX bean, MyHolder myHolder) {

        if(!TextUtils.isEmpty(bean.recommendation.title)){
            myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
            myHolder.tv_content_dynamic_item.setText(bean.recommendation.title);
        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
        }


        myHolder.tv_team_name_dynamic_item.setText(bean.match.hostTeamName + "\t\tvs\t\t" + bean.match.awayTeamName);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        spannableStringBuilder.append(bean.match.leagueName);
        myHolder.tv_type_dynamic_item.setText(bean.recommendation.type+"");
        if (!TextUtils.isEmpty(bean.match.match_time)) {
            String matchStartTime = Utils.getMatchStartTime(Long.valueOf(bean.match.match_time + "000"));
            spannableStringBuilder.append(" · "+matchStartTime);
        }

        if(bean.recommendation.reason_len >0){
            spannableStringBuilder.append(" · "+bean.recommendation.reason_len+"字");
        }

        /**
         * 不中退款
         */
        if("1".equals(bean.recommendation.return_if_wrong)){
            spannableStringBuilder.append(" · "+"不中退款");
        }


        if(bean.match.status.equals("COMPLETE")){//一结束     1
            myHolder.ll_price.setVisibility(View.GONE);
            myHolder.tv_right_item.setVisibility(View.VISIBLE);
//            myHolder.iv_result_personal.setVisibility(View.VISIBLE);
            myHolder.tv_buy.setVisibility(View.GONE);
            spannableStringBuilder.append(" · "+bean.recommendation.type);
        }else{
            if(bean.recommendation.buy == 1){
                spannableStringBuilder.append(" · "+bean.recommendation.type);
                myHolder.tv_buy.setVisibility(View.VISIBLE);
                myHolder.ll_price.setVisibility(View.GONE);
                myHolder.tv_right_item.setVisibility(View.VISIBLE);
            }else{
                myHolder.tv_buy.setVisibility(View.GONE);
                myHolder.ll_price.setVisibility(View.VISIBLE);
                myHolder.tv_right_item.setVisibility(View.GONE);
            }
        }

        if(!bean.match.status.equals("COMPLETE")) {
//            spannableStringBuilder.append(bean.recommendation.type + " · ");
            if (!TextUtils.isEmpty(bean.recommendation.price)&&!"0".equals(bean.recommendation.price) ) {
                Integer integer = Integer.valueOf(bean.recommendation.price);
                String price = integer + "金币";
//                SpannableString spannableString = new SpannableString(price);
//                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#AE7614"));
//                spannableString.setSpan(span, 0, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                spannableStringBuilder.append(spannableString);
                myHolder.tv_price_dynamic_item.setText(price);
            }else{
                myHolder.tv_price_dynamic_item.setText("免费");
            }
        }else{
//            spannableStringBuilder.append(bean.recommendation.type + "");
        }


        myHolder.tv_bottom_dynamic_item.setText(spannableStringBuilder);
        spannableStringBuilder.clear();
    }
}
