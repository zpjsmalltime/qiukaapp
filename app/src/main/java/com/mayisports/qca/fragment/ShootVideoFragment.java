package com.mayisports.qca.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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

import com.andview.refreshview.XRefreshView;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.LoginActivity;
import com.mayi.mayisports.activity.PublishPointActivity;
import com.mayi.mayisports.activity.SearchInfosActivity;
import com.mayi.mayisports.activity.TopicDetailActivity;
import com.mayi.mayisports.activity.VideoPlayerActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.adapter.TabAdapter;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.ImageLoaderType;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.HeaderGridView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.kymjs.kjframe.http.HttpParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 短视频界面 主界面对应发现  视频模块子界面
 * Created by Zpj on 2018/3/14.
 */

public class ShootVideoFragment extends Fragment implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener {




    public static ShootVideoFragment newInstance(String subType) {
        ShootVideoFragment fragment = new ShootVideoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("subType", subType);
        fragment.setArguments(bundle);
        return fragment;
    }



    private boolean isShow;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
    }


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

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 22 && resultCode == 22 ){//回调刷新数据
//            int position = data.getIntExtra("position", -1);
//            String praise_count = data.getStringExtra("praise_count");
//
//            if(position != -1) {
//                DynamicBean.ListBean bean = dynamicBean.list.get(position);
//                int praise_status = data.getIntExtra("praise_status", 0);
//                bean.comment.praise_count = praise_count;
//                bean.comment.praise_status = praise_status;
//
//                boolean delete = data.getBooleanExtra("delete", false);
//                if(delete){
//                    dynamicBean.list.remove(position);
//                }

                myAdapter.notifyDataSetChanged();
//            }
        }
    }

    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();

//            ToastUtils.toast("回调刷新");

            int result = intent.getIntExtra(LoginActivity.RESULT, 0);
            if(result == 99){

            }else if(result == 66){//点击底tab刷新
                if(xfv_home_fg != null){
                    xfv_home_fg.startRefresh();
                }

            } else {
                initData();
                lv_home_fg.setSelection(0);

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }


    private View viewRoot;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String serializable = (String) getArguments().getSerializable("subType");
            String[] split = serializable.split(",");
            subType = split[0];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(getContext(), R.layout.shoot_video_fg,null);
        }
        return viewRoot;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initReceiver();
        initView();
       // initTab();
        initData();
    }

    private String subType = "";
    private void initTab() {
        page = 0;
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();

        params.put("action","sys_feed");
        params.put("type",tagId);
        params.put("start",page);

//        if(page>0){
//            params.put("loadMore",0);
//        }else{
//            params.put("loadMore",1);
//        }


        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {



                if(xfv_home_fg != null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }
                dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
                updateTabs();
                updateBanner();

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);

            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_clone = rl_load_layout;
                if(xfv_home_fg != null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }

            }
        });
    }

    private List<String> strings = new ArrayList<>();
    private void updateBanner() {
        if(dynamicBean != null && dynamicBean.data!= null && dynamicBean.data.bannerList != null){
            int size = dynamicBean.data.bannerList.size();
//            id_banner.setBannerAdapter(new MyBannerAdapter(homeDataBean.data.bannerList));
            banner.setVisibility(View.VISIBLE);
            strings.clear();
            for(int i = 0;i<dynamicBean.data.bannerList.size();i++){

                int type = dynamicBean.data.bannerList.get(i).type;
                if(type == 1) {
                    strings.add(dynamicBean.data.bannerList.get(i).banner + "," + dynamicBean.data.bannerList.get(i).topic_id + "," + dynamicBean.data.bannerList.get(i).type);
                }else if(type == 2 || type == 3){
                    strings.add(dynamicBean.data.bannerList.get(i).banner + "," + dynamicBean.data.bannerList.get(i).title + "," + dynamicBean.data.bannerList.get(i).type);
                }
            }

            banner.setImages(strings);
            banner.start();
        }else{
            banner.setVisibility(View.GONE);
        }

    }


    public XRefreshView xfv_home_fg;
    private HeaderGridView lv_home_fg;
    private LinearLayout ll_no_data;
    private RelativeLayout rl_load_layout;
    private TextView tv_title;
    private ImageView iv_right_title;
    private TabLayout tl_top_bar;

    private View v;
    private int currentTabSelect = -1;
    private int  scrollYs;

    private RelativeLayout ll_title;


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

        ll_title = viewRoot.findViewById(R.id.rl_title);

        ll_title.setVisibility(View.GONE);


        xfv_home_fg = viewRoot.findViewById(R.id.xfv_home_fg);
        lv_home_fg = viewRoot.findViewById(R.id.lv_home_fg);
        tl_top_bar = viewRoot.findViewById(R.id.tl_top_bar);
        xfv_home_fg.setPullRefreshEnable(true);//设置允许下拉刷新
        xfv_home_fg.setMoveForHorizontal(true);
        xfv_home_fg.setXRefreshViewListener(new MyListener());

        v = viewRoot.findViewById(R.id.v);

        ll_no_data = viewRoot.findViewById(R.id.ll_no_data);
        rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);

        viewRoot.findViewById(R.id.iv_left_title).setVisibility(View.INVISIBLE);
        tv_title = viewRoot.findViewById(R.id.tv_title);
        tv_title.setText("球咖");
        viewRoot.findViewById(R.id.tv_ritht_title).setVisibility(View.INVISIBLE);
        iv_right_title =  viewRoot.findViewById(R.id.iv_right_title);
        iv_right_title.setVisibility(View.VISIBLE);
        iv_right_title.setImageResource(R.drawable.search_white);
        iv_right_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchHistory = "";
                if(dynamicBean.meta != null && dynamicBean.meta.default_text != null){
                    searchHistory = dynamicBean.meta.default_text;
                }
                SearchInfosActivity.start(ShootVideoFragment.this.getActivity(),searchHistory);
            }
        });

       int headerViewCount = lv_home_fg.getHeaderViewCount();
        if(headerViewCount == 0) {
            View header = View.inflate(getContext(),R.layout.find_header,null);
            initheader(header);
          //  lv_home_fg.addHeaderView(header);
        }
//        xfv_home_fg.setOnAbsListViewScrollListener(new AbsListView.OnScrollListener() {
//                                                       @Override
//                                                       public void onScrollStateChanged(AbsListView view, int scrollState) {
//                                                       }
//
//                                                       @Override
//                                                       public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                                                           scrollYs = getScrollYs(view, firstVisibleItem);
//                                                           Log.e("srcoll",scrollYs+"");
//                                                           cuLoaction(v);
//
//                                                           if (firstVisibleItem < 4) {
//                                                               boolean shown = tl_top_bar.isShown();
//
//                                                               if (compareLocation(tl_match_home)) {
//                                                                   if(!shown) {
//                                                                       refreshTabs(tl_top_bar,currentTabSelect);
//                                                                       tl_top_bar.setVisibility(View.VISIBLE);
//                                                                   }
//                                                               } else {
//                                                                   if(shown) {
//                                                                       refreshTabs(tl_match_home,currentTabSelect);
//                                                                       tl_top_bar.setVisibility(View.GONE);
//
//                                                                   }
//                                                               }
//                                                           }
//                                                       }
//
//                                                   }
//            );


        lv_home_fg.setAdapter(myAdapter);
        lv_home_fg.setOnItemClickListener(this);

    }



    private HashMap<Integer,Integer> itemHeights = new HashMap<>();

    public int getScrollYs(AbsListView v,int fir) {
        View c = v.getChildAt(0);
        if (c == null) {return 0;}

        int firstVisiblePosition = fir;
        if (!itemHeights.containsKey(firstVisiblePosition)){
            itemHeights.put(firstVisiblePosition,itemHeight);}
        int top = c.getTop();
        int height = 0;
        Set<HashMap.Entry<Integer,Integer>> entry = itemHeights.entrySet();
        for (HashMap.Entry<Integer,Integer> entrys: entry){
            if (entrys.getKey()<firstVisiblePosition && firstVisiblePosition <dynamicBean.list.size()){
                height += entrys.getValue();
            }
        }


            return -top + height ;
    }





    private int listTabY = -1;
    private void cuLoaction(View view){
        if(listTabY == -1) {
            int[] location = new int[2];
            view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标，含toolBar
            listTabY = location[1];
        }
//        view.getLocationOnScreen(location);
    }

    private boolean compareLocation(View view){
        int[] location = new int[2];
        view.getLocationInWindow(location); //获取在当前窗口内的绝对坐标，含toolBar
        if(location[1]<=listTabY){
            return true;
        }

        return false;
    }


    /**
     * 初始化头部
     * @param header
     */
    private Banner banner;
    private TabLayout tl_match_home;
    private int bannerHeight;
    private void initheader(View header) {
        banner = header.findViewById(R.id.banner);
        banner.setImageLoader(new ImageLoaderType());
        banner.setIndicatorGravity(BannerConfig.CENTER);
        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();
        layoutParams.height = (int) (DisplayUtil.getScreenWidth(this.getActivity())*0.32);
        bannerHeight = layoutParams.height;
        banner.setLayoutParams(layoutParams);
        int anInt = SPUtils.getInt(this.getContext(), Constant.Banner_Interval);
        if(anInt == 0)anInt = 4;
        banner.setDelayTime(1000*anInt);

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                String topic_id = strings.get(position).split(",")[1];
//                WebViewActivtiy.start(HomeNewFragment.this.getActivity(),Constant.BASE_URL+"#/topic/"+topic_id,"话题");
                String type = strings.get(position).split(",")[2];

              if("1".equals(type)){
                    TopicDetailActivity.start(ShootVideoFragment.this.getActivity(), topic_id);
              }else if("2".equals(type)){
                   WebViewActivtiy.start(ShootVideoFragment.this.getActivity(),topic_id,"球咖",true);
              }else if("3".equals(type)){//登录
                  if(LoginUtils.isUnLogin()){
                      LoginUtils.goLoginActivity(ShootVideoFragment.this.getActivity(),"");
                      return;
                  }

                  WebViewActivtiy.start(ShootVideoFragment.this.getActivity(),topic_id,"球咖",true);
              }

            }
        });




        tl_match_home = header.findViewById(R.id.tl_match_home);
//        updateTabs();

    }


private void refreshTabs(TabLayout tabLayout,int currentTabSelect){

    for (int i = 0; i <tabLayout.getTabCount(); i++) {

        if(i == currentTabSelect) {
            tabLayout.getTabAt(i).select();
            break;
        }

    }

//    for (int i = 0; i < tabLayout.getTabCount(); i++) {
//        TabLayout.Tab tab = tabLayout.getTabAt(i);
//        if (tab != null) {
//            tab.setCustomView(getTabView(i));
//        }
//    }

//        tl_match_home.setScrollPosition(deaulftSelect,0,true);
    updateTabTextViewRefresh(tabLayout.getTabAt(currentTabSelect), true);


}


    private int deaulftSelect = 1;
    List<String> titles = new ArrayList<>();
    private  void updateTabs() {

        if(!this.isAdded())return;
        titles.clear();
        tl_match_home.removeAllTabs();
        tl_top_bar.removeAllTabs();
//        List<Fragment> fragments = new ArrayList<>();

        for (int i = 0; i <dynamicBean.data.tag.size(); i++) {
            List<String> strings = dynamicBean.data.tag.get(i);
            String title = strings.get(0);
            titles.add(title);
            String titleId = strings.get(1);

            TabLayout.Tab tab = tl_match_home.newTab();
            tab.setText(title);
            tab.setCustomView(getTabView(i));

            TabLayout.Tab tab1 = tl_top_bar.newTab();
            tab1.setText(title);
            tab1.setCustomView(getTabView(i));

            if("1".equals(strings.get(2))){
                deaulftSelect = i;

                tl_match_home.addTab(tab,true);

                tl_top_bar.addTab(tab1,true);
            }else {
                tl_match_home.addTab(tab);
                tl_top_bar.addTab(tab1);
            }
//            fragments.add(DynamicFragment.newInstance(titleId));

        }

//        for (int i = 0; i < tl_match_home.getTabCount(); i++) {
//            TabLayout.Tab tab = tl_match_home.getTabAt(i);
//            TabLayout.Tab tabAt = tl_top_bar.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(getTabView(i));
//                tabAt.setCustomView(getTabView(i));
//            }
//        }

//        tl_match_home.setScrollPosition(deaulftSelect,0,true);


        tl_match_home.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                vp_match_home.setCurrentItem(tab.getPosition());
                updateTabTextView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        //设置可以滑动
        tl_match_home.setTabMode(TabLayout.MODE_SCROLLABLE);

        tl_top_bar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                vp_match_home.setCurrentItem(tab.getPosition());
                updateTabTextView(tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                updateTabTextView(tab, false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        //设置可以滑动
        tl_top_bar.setTabMode(TabLayout.MODE_SCROLLABLE);


        updateTabTextView(tl_match_home.getTabAt(deaulftSelect), true);
        updateTabTextView(tl_top_bar.getTabAt(deaulftSelect),true);

    }


    private String preSubType = "";
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {

        if (isSelect) {
            //选中加粗
            TextView tabSelect = tab.getCustomView().findViewById(R.id.tv_tab_item);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black));
            tabSelect.setText(tab.getText());

            int tag = (int) tabSelect.getTag();
            subType = dynamicBean.data.tag.get(tag).get(1);
            if(!subType.equals(preSubType)) {
                preSubType = subType;
                initData();
            }
            currentTabSelect = tag;

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.VISIBLE);

        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black_99));
            tabUnSelect.setText(tab.getText());

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.INVISIBLE);
        }
    }

    private void updateTabTextViewRefresh(TabLayout.Tab tab, boolean isSelect) {

        if (isSelect) {
            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
            tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black));
            tabSelect.setText(tab.getText());

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.VISIBLE);

            int tag = (int) tabSelect.getTag();
            subType = dynamicBean.data.tag.get(tag).get(1);
//            initData();
            currentTabSelect = tag;
            deaulftSelect = tag;

        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
            tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black_99));
            tabUnSelect.setText(tab.getText());

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.INVISIBLE);
        }
    }

    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.tab_item_and_bottom, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_tab_item);
        textView.setText(titles.get(currentPosition));
        textView.setTag(currentPosition);
        return view;
    }
    private int page;
    private int pre_page = 1;
    private RelativeLayout rl_load_clone;
    private void initData() {
        page = 0;
        requestNetData();
    }

    /**
     * 短视频
     */
    private String tagId = "-4";
    private DynamicBean dynamicBean;
    private  void requestNetData() {

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();

        params.put("action","sys_feed");
        params.put("type",tagId);
        params.put("subtype",subType);
        params.put("start",page);

//        if(page>0){
//            params.put("loadMore",0);
//        }else{
//            params.put("loadMore",1);
//        }

        if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {



                if(xfv_home_fg != null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }

                if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);

                if(page == 0) {

                    updateBanner();
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
                        if(dynamicBean.list.size() == 0){
                            if(ll_no_data != null) xfv_home_fg.setPullLoadEnable(false);
                            if(ll_no_data != null) ll_no_data.setVisibility(View.VISIBLE);
//                            preCreateTime = "";
                        }else{
//                            xfv_dynamic_fg.setPullLoadEnable(true);
                            if(ll_no_data != null)ll_no_data.setVisibility(View.GONE);
                        }
                    }else{
                        if(ll_no_data != null)xfv_home_fg.setPullLoadEnable(false);
                        if(ll_no_data != null)ll_no_data.setVisibility(View.VISIBLE);
//                        preCreateTime = "";
                    }
                    pre_page = 1;
                    lv_home_fg.setAdapter(myAdapter);
//                    myAdapter.notifyDataSetChanged();
//                    lv_home_fg.setSelection(0);
//                    lv_home_fg.smoothScrollBy();


                    if(tl_top_bar.isShown()){
                        int i = DisplayUtil.dip2px(QCaApplication.getContext(), 30);
                        int dua = (-scrollYs) - (listTabY -i);
                        int height = lv_home_fg.getHeight()-bannerHeight-i;
                        Log.e("dua",dua+"---"+height);

//                        lv_home_fg.smoothScrollBy(dua,0);


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
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                if(page == 0){

                    if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_clone = rl_load_layout;
                if(xfv_home_fg != null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }

            }
        });
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
                    if (beanX.type == 3 || beanX.type == 7) {// 3 话题  7 短视频
                        homeDataBean.list.add(beanX);
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //  if(position == 0 || position == 1)return;
        position = position ;
        String url = dynamicBean.list.get(position).comment.imgListEx.get(0).url;
        Constant.dynamicBean = dynamicBean;
        VideoPlayerActivity.start(this,position,page,subType,22);
    }

    /**
     * 加载刷新监听
     */
    class MyListener extends XRefreshView.SimpleXRefreshListener {


        @Override
        public void onRefresh(boolean isPullDown) {
            rl_load_clone = null;
            page = 0;
            requestNetData();
        }

    }

    private int itemHeight = -1;

    private MyAdapter myAdapter = new MyAdapter();
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(dynamicBean != null && dynamicBean.list!= null){
                return dynamicBean.list.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {

            MyHolder myHolder;
            if(convertView == null){
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.shoot_vide_item,null);
                myHolder.rl_video_item = convertView.findViewById(R.id.rl_video_item);
                myHolder.iv_video_item = convertView.findViewById(R.id.iv_video_item);
                myHolder.tv_video_title_item = convertView.findViewById(R.id.tv_video_title_item);
                myHolder.iv_head_dynamic_item_video = convertView.findViewById(R.id.iv_head_dynamic_item_video);
                myHolder.iv_vip_header_video = convertView.findViewById(R.id.iv_vip_header_video);
                myHolder.tv_name_dynamic_item_video = convertView.findViewById(R.id.tv_name_dynamic_item_video);
                myHolder.tv_thumbup_topic_item_video = convertView.findViewById(R.id.tv_thumbup_topic_item_video);

                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) myHolder.rl_video_item.getLayoutParams();
                int screenWidth = DisplayUtil.getScreenWidth(ShootVideoFragment.this.getActivity());
                double v = screenWidth / 1.0 / 6 * 4;
                layoutParams.height = (int) v;
                if(itemHeight == -1)itemHeight = layoutParams.height;
                myHolder.rl_video_item.setLayoutParams(layoutParams);

                convertView.setTag(myHolder);
            }else {
                myHolder = (MyHolder) convertView.getTag();
            }

            final DynamicBean.ListBean bean = dynamicBean.list.get(position);

            if (bean.user != null) {
                PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item_video);
                myHolder.tv_name_dynamic_item_video.setText(bean.user.nickname);

                try {

                    if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                        myHolder.iv_vip_header_video.setVisibility(View.VISIBLE);
                    } else {
                        myHolder.iv_vip_header_video.setVisibility(View.GONE);
                    }
                }catch (Exception e){
                    myHolder.iv_vip_header_video.setVisibility(View.GONE);
                }
            }

            myHolder.tv_video_title_item.setText(bean.comment.comment);

            if(bean.comment.imgListEx != null) {
                DynamicBean.ListBean.ImageList imageList = bean.comment.imgListEx.get(0);
                String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                PictureUtils.show(imgUrl, myHolder.iv_video_item);
            }


//            myHolder.tv_thumbup_topic_item_video.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                    requestThumbup(bean);
//                }
//            });


            if(bean.comment.praise_status == 1){//点赞
//                Drawable drawable= getResources().getDrawable(R.drawable.video_thumbup_ok);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                myHolder.tv_thumbup_topic_item_video.setCompoundDrawables(drawable,null,null,null);
            }else {
                Drawable drawable= getResources().getDrawable(R.drawable.video_thumbup_no);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                myHolder.tv_thumbup_topic_item_video.setCompoundDrawables(drawable,null,null,null);
            }

            DataBindUtils.setPraiseCountShoot(myHolder.tv_thumbup_topic_item_video,bean.comment.praise_count);


            if (position == getCount() - 3 || (getCount() < 3)) {
                if (pre_page == 1) {
                    rl_load_clone = null;
                    page++;
                    requestNetData();
                }
            }
            return convertView;
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
        public RelativeLayout rl_video_item;
        public ImageView iv_video_item;
        public TextView tv_video_title_item;
        public ImageView iv_head_dynamic_item_video;
        public ImageView iv_vip_header_video;
        public TextView tv_name_dynamic_item_video;
        public TextView tv_thumbup_topic_item_video;

    }
}
