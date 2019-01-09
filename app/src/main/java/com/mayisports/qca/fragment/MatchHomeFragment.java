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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.adapter.TabAdapter;
import com.mayisports.qca.adapter.VpAdapter;
import com.mayisports.qca.bean.MatchTabBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.NetWorkUtil;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 赛事模块 内容fragment
 * Created by Zpj on 2018/1/25.
 */

public class MatchHomeFragment extends Fragment {



    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "score_fragment_action";
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
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }

    private String[] sp;


    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getIntExtra(RESULT,0) == 99){
                if(matchTabBean == null || matchTabBean.data == null || matchTabBean.data.tagList == null){
                    initData();
                }
            }
        }
    }



    private View viewRoot;
    public ViewPager vp_match_home;
    private TabLayout tl_match_home;
    private TabAdapter adapter;
    private TextView tv_ritht_title;
    private TextView tv_title;

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(getContext(), R.layout.match_home_fg,null);
        }
        return viewRoot;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initReceiver();
        initView();
        initData();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    private void initView() {

        ll_net_error = viewRoot.findViewById(R.id.ll_net_error);
        tv_refresh_net = viewRoot.findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        tv_title = viewRoot.findViewById(R.id.tv_title);
        tv_title.setText("赛事");
        tv_ritht_title = viewRoot.findViewById(R.id.tv_ritht_title);
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tl_match_home = viewRoot.findViewById(R.id.tl_match_home);
        vp_match_home = viewRoot.findViewById(R.id.vp_match_home);

    }

    private void initData() {
        requestTabDatas();
    }

    private MatchTabBean matchTabBean;
    private void requestTabDatas() {
        //http://20180123.dldemo.applinzi.com/php/api.php?action=hotMatch_byDate&type=-1&start=0
        String url = Constant.BASE_URL+"php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","hotMatch_byDate");
        params.put("type","-1");
        params.put("start",0);

        if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {


                if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);

                matchTabBean = JsonParseUtils.parseJsonClass(string,MatchTabBean.class);
                if(matchTabBean != null && matchTabBean.data != null && matchTabBean.data.tagList != null){
                    updateTabs();

                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);

                if(NetWorkUtil.isConnected(QCaApplication.getContext())){
                    initData();
                    if(ll_net_error != null)       ll_net_error.setVisibility(View.GONE);
                }else{

                    if(ll_net_error != null)     ll_net_error.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    List<String> titles = new ArrayList<>();
    private void updateTabs() {
        if(!this.isAdded())return;

        titles.clear();
       // final List<Fragment> fragments = new ArrayList<>();

        List<String> titlesNew = new ArrayList<>();
        for (int i = 0; i < matchTabBean.data.tagList.size(); i++) {
            List<String> strings = matchTabBean.data.tagList.get(i);
            String title = strings.get(0);
            titles.add(title);
            String titleId = strings.get(1);
            TabLayout.Tab tab = tl_match_home.newTab();
            tab.setText(title);
            tab.setCustomView(getTabView(i));
            tl_match_home.addTab(tab);
            String newTitleStr = title + "," +titleId+","+strings.get(2);
            titlesNew.add(newTitleStr);
//            fragments.add(TabLayoutFragment.newInstance(titleId+","+strings.get(2)));

        }

        final VpAdapter vpAdapter = new VpAdapter(getChildFragmentManager(),titlesNew);

        //给ViewPager设置适配器
        vp_match_home.setAdapter(vpAdapter);
        vp_match_home.setOffscreenPageLimit(3);



        //将TabLayout和ViewPager关联起来。
//        tl_match_home.setupWithViewPager(vp_match_home);
        //设置可以滑动
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
        tl_match_home.setTabMode(TabLayout.MODE_SCROLLABLE);
        vp_match_home.setCurrentItem(1);

        updateTabTextView(tl_match_home.getTabAt(tl_match_home.getSelectedTabPosition()), true);

    }

    private void updateTabTextView(TabLayout.Tab tab, boolean isSelect) {

        if (isSelect) {


            //选中加粗
            TextView tabSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
           // tabSelect.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tabSelect.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.white));
            tabSelect.setTextSize(15);
            tabSelect.setText(tab.getText());

            View v_bottom_line = tab.getCustomView().findViewById(R.id.v_bottom_line);
            v_bottom_line.setVisibility(View.VISIBLE);

        } else {
            TextView tabUnSelect = (TextView) tab.getCustomView().findViewById(R.id.tv_tab_item);
         //   tabUnSelect.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
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

}
