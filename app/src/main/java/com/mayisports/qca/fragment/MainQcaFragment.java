package com.mayisports.qca.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayi.mayisports.MainActivity;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.BindPhoneNumActivity;
import com.mayi.mayisports.activity.LoginActivity;
import com.mayi.mayisports.activity.PublishPointActivity;
import com.mayi.mayisports.activity.SearchInfosActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.adapter.TabAdapter;
import com.mayisports.qca.adapter.VpAdapter;
import com.mayisports.qca.adapter.VpMainAdapter;
import com.mayisports.qca.bean.MainQcaTabsBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NetWorkUtil;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.BindPhonePopuWindow;
import com.mayisports.qca.view.HomePagePopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;
import static com.mayisports.qca.fragment.DynamicFragment.ACTION;

/**
 * 首页球咖模块fragment
 * Created by Zpj on 2018/2/27.
 */

public class MainQcaFragment extends Fragment implements View.OnClickListener {


    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "MainQcaFragment";
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



    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();
            if(intent.getIntExtra(RESULT,0) == 11) {//发布动态回调
                int currentItem = vp_match_home.getCurrentItem();
                if(currentItem == 0){
//                    vp_match_home.setCurrentItem(0);
                    Intent intent1 = new Intent(DynamicFragment.ACTION);
//                            LocalBroadcastManager.getInstance(PublishPointActivity.this).sendBroadcast(intent1);
//                    Intent intent1 = new Intent(MainQcaFragment.ACTION);
                    intent1.putExtra(RESULT, 33);
                    LocalBroadcastManager.getInstance(MainQcaFragment.this.getActivity()).sendBroadcast(intent1);

                }

            }else if(intent.getIntExtra(RESULT,0) == 22){//跳转到推荐
                int currentItem = vp_match_home.getCurrentItem();
//                if(currentItem != 0 && LoginUtils.isLogin()) {
//                    vp_match_home.setCurrentItem(1);
//                }

//                if(LoginUtils.isUnLogin() || currentItem != 0){
//                    vp_match_home.setCurrentItem(1);
//                }
                if(SPUtils.getInt(QCaApplication.getContext(),Constant.HOME_SELECT) != -1) {
                    vp_match_home.setCurrentItem(0);
                }

            }else if(intent.getIntExtra(RESULT,0) == 33){//退出登录  设置偏好


                    vp_match_home.setCurrentItem(0);


            }else if( intent.getIntExtra(RESULT,0) == 99){
                if(mainQcaTabsBean == null || mainQcaTabsBean.data == null || mainQcaTabsBean.data.tag == null){
                    initData();
                }
            }

            if( intent.getIntExtra(RESULT,0) == 44){//更新关注红点
                try {
                    View redPoint = tl_match_home.getTabAt(0).getCustomView().findViewById(R.id.tv_red_point);
                    boolean msg = intent.getBooleanExtra("msg", false);
//                    msg = true;
                    if (msg) {
                //        redPoint.setVisibility(View.VISIBLE);
                    } else {
                        redPoint.setVisibility(View.GONE);
                    }
                }catch (Exception e){

                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }
    private View viewRoot;
    public ViewPager vp_match_home;
    private TabLayout tl_match_home;
    private TabAdapter adapter;

    private TextView et_search_home_fg;
    private ImageView iv_left_title_home;


    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(getContext(), R.layout.main_qca_fg,null);
        }
        initReceiver();
        initView();
        delCache();
        return viewRoot;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    private ImageView iv_search;

    private ImageView iv_edit_title_home;
    private void initView() {

        ll_net_error = viewRoot.findViewById(R.id.ll_net_error);
        tv_refresh_net = viewRoot.findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCache();
            }
        });

        iv_search = viewRoot.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);

        tl_match_home = viewRoot.findViewById(R.id.tl_match_home);
        vp_match_home = viewRoot.findViewById(R.id.vp_match_home);

        et_search_home_fg = viewRoot.findViewById(R.id.et_search_home_fg);
//        et_search_home_fg.setEnabled(false);

        et_search_home_fg.setOnClickListener(this);

        iv_left_title_home = viewRoot.findViewById(R.id.iv_left_title_home);
      //  iv_left_title_home.setOnClickListener(this);
        iv_edit_title_home = viewRoot.findViewById(R.id.iv_edit_title_home);
        iv_edit_title_home.setOnClickListener(this);
    }

    private void initData(){
//         delCache();
        requesetTabs();

    }



    private void delCache() {
        String string = SPUtils.getString(QCaApplication.getContext(), Constant.MAIN_PAGE);
        if(TextUtils.isEmpty(string)){
            requesetTabs();
        }else{
            mainQcaTabsBean = JsonParseUtils.parseJsonClass(string,MainQcaTabsBean.class);
            if(mainQcaTabsBean != null && mainQcaTabsBean.data != null && mainQcaTabsBean.data.tag != null){
//                   SPUtils.putString(QCaApplication.getContext(), Constant.MAIN_QCA_CACHE,string);
                updateTabs();

            }
            if(mainQcaTabsBean != null && mainQcaTabsBean.meta != null){
                String default_text = mainQcaTabsBean.meta.default_text;
                if(TextUtils.isEmpty(default_text)){
                    default_text = "搜索";
                }
                et_search_home_fg.setHint(default_text);
            }

            vp_match_home.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requesetTabs();
                }
            },600);
        }

    }

    private MainQcaTabsBean mainQcaTabsBean;
    private void requesetTabs() {

        if(ll_net_error != null)  ll_net_error.setVisibility(View.GONE);


        //http://20180207.dldemo.applinzi.com/php/api.php?action=sys_feed&start=0&type=0
        String url = Constant.BASE_URL+"php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","sys_feed");
        params.put("start",0);
        params.put("type",0);
        params.put("loadMore",0);
        params.put("msgid",SPUtils.getString(QCaApplication.getContext(),Constant.DEV_PUSH_ID));
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                if(ll_net_error != null)     ll_net_error.setVisibility(View.GONE);

               mainQcaTabsBean = JsonParseUtils.parseJsonClass(string,MainQcaTabsBean.class);
               if(mainQcaTabsBean != null && mainQcaTabsBean.data != null && mainQcaTabsBean.data.tag != null){
//                   SPUtils.putString(QCaApplication.getContext(), Constant.MAIN_QCA_CACHE,string);
                   SPUtils.putString(QCaApplication.getContext(),Constant.MAIN_PAGE,string);
                   updateTabs();

               }
               if(mainQcaTabsBean != null && mainQcaTabsBean.meta != null){
                   String default_text = mainQcaTabsBean.meta.default_text;
                   if(TextUtils.isEmpty(default_text)){
                       default_text = "搜索";
                   }
                   et_search_home_fg.setHint(default_text);
               }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);

                if(NetWorkUtil.isConnected(QCaApplication.getContext())){
                    requesetTabs();
                    if(ll_net_error != null)    ll_net_error.setVisibility(View.GONE);
                }else{
                    String string = SPUtils.getString(QCaApplication.getContext(), Constant.MAIN_PAGE);
                    if(TextUtils.isEmpty(string)){
                        if(ll_net_error != null)  ll_net_error.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    private int deaulftSelect = 0;
    List<String> titles = new ArrayList<>();
    private  void updateTabs() {
        if(!this.isAdded())return;
        titles.clear();
        tl_match_home.removeAllTabs();
    //    final List<Fragment> fragments = new ArrayList<>();
        List<String> titlesNew = new ArrayList<>();


        tl_match_home.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        int anInt = SPUtils.getInt(QCaApplication.getContext(), Constant.HOME_SELECT);

        int j = 0;
        for (int i = 0; i <mainQcaTabsBean.data.tag.size(); i++) {
            List<String> strings = mainQcaTabsBean.data.tag.get(i);

            String titleId = strings.get(1);
            if ("-3".equals(titleId)) {
                j = i;
                break;
            }
        }
        mainQcaTabsBean.data.tag.remove(j);


        for (int i = 0; i <mainQcaTabsBean.data.tag.size(); i++) {
            List<String> strings = mainQcaTabsBean.data.tag.get(i);

            String titleId = strings.get(1);


            String title = strings.get(0);
            titles.add(title);


            TabLayout.Tab tab = tl_match_home.newTab();
            tab.setText(title);
            tab.setCustomView(getTabView(i));


            String newTitleStr = title + "," +titleId;
            titlesNew.add(newTitleStr);

        //    fragments.add(DynamicFragment.newInstance(titleId));
//            if(anInt == 0){
                if("1".equals(strings.get(2))){
                    deaulftSelect = i;
                    tl_match_home.addTab(tab,true);
                }else{
                    tl_match_home.addTab(tab);
                }
//            }else{
//                if(anInt == -1)anInt = 0;
//                if(i == anInt){
//                    deaulftSelect = i;
//                    tl_match_home.addTab(tab,true);
//                }else{
//                    tl_match_home.addTab(tab);
//                }
//
//            }


        }

//        for (int i = 0; i < tl_match_home.getTabCount(); i++) {
//            TabLayout.Tab tab = tl_match_home.getTabAt(i);
//            if (tab != null) {
//                tab.setCustomView(getTabView(i));
//            }
//        }

      //  adapter = new TabAdapter(getChildFragmentManager(), fragments,titles);
        VpMainAdapter vpAdapter = new VpMainAdapter(getChildFragmentManager(),titlesNew);
        //给ViewPager设置适配器
        vp_match_home.setAdapter(vpAdapter);
        vp_match_home.setOffscreenPageLimit(2);
        vp_match_home.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_match_home));
        tl_match_home.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(vp_match_home));

        //将TabLayout和ViewPager关联起来。
//        tl_match_home.setupWithViewPager(vp_match_home);
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

//        if(anInt != 0){
//            if(anInt == -1)anInt = 0;
//            deaulftSelect = anInt;
//        }


        vp_match_home.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                updateTabTextView(tl_match_home.getTabAt(position),true);
                if(position == 0){
                    position = -1;
                }
                SPUtils.putInt(QCaApplication.getContext(), Constant.HOME_SELECT,position);
                JCVideoPlayer.releaseAllVideos();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


//        if(deaulftSelect>=tl_match_home.getTabCount()){
//            deaulftSelect = 1;
//        }

        vp_match_home.setCurrentItem(deaulftSelect);
        updateTabTextView(tl_match_home.getTabAt(deaulftSelect), true);
    }


    /**
     * 更新顶部tab 样式
     * @param tab
     * @param isSelect
     */
    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {


        if(tab == null || tab.getCustomView() == null)return;


        if (isSelect) {
            //选中加粗
            if(tl_match_home.getSelectedTabPosition() == 0){
                Intent intent1 = new Intent(MainQcaFragment.ACTION);
                intent1.putExtra(RESULT, 44);//发送到关注，控制红点
                intent1.putExtra("msg",false);
                LocalBroadcastManager.getInstance(MainQcaFragment.this.getActivity()).sendBroadcast(intent1);

            }

            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
        //    tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.white));
            tabSelect.setTextSize(15);
            tabSelect.setText(tab.getText());

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.VISIBLE);


        } else {

            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
          //  tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tabUnSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black_99));
            tabUnSelect.setTextSize(15);
            tabUnSelect.setText(tab.getText());

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.INVISIBLE);
        }
    }

    private View getTabView(int currentPosition) {
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.tab_item, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_tab_item);
        textView.setText(titles.get(currentPosition));
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_left_title_home:
            case R.id.iv_edit_title_home:

                /**
                 * 是否登录
                 */
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                    return;
                }


                /**
                 * 是否绑定手机号
                 */
                if(!Utils.isHavePhoneNum()){
                    showToastPopuWindow();
                    return;
                }


                PublishPointActivity.start(this.getActivity(),"0");
                break;
            case R.id.et_search_home_fg://搜索界面
            case R.id.iv_search:
                SearchInfosActivity.start(this.getActivity(),et_search_home_fg.getHint()+"");
                break;
        }
    }


    /**
     * 绑定手机号弹窗
     */
    private BindPhonePopuWindow homePagePopuWindow;
    private void showToastPopuWindow() {
        if(homePagePopuWindow != null){
            homePagePopuWindow.dismiss();
            homePagePopuWindow = null;
        }

        homePagePopuWindow = new BindPhonePopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tv_to_bind_phone){

                    if(homePagePopuWindow == null )return;
                    BindPhoneNumActivity.start(MainQcaFragment.this.getActivity());
                    homePagePopuWindow.dismiss();



                }else {
                    homePagePopuWindow.dismiss();
                }
            }
        },"","");
        homePagePopuWindow.showAtLocation(vp_match_home, Gravity.CENTER,0,0);


    }
}
