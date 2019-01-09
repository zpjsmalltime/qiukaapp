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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.adapter.ShootVideoVpAdapter;
import com.mayisports.qca.adapter.TabAdapter;
import com.mayisports.qca.adapter.VpAdapter;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.MatchTabBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.NetWorkUtil;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 *
 * 主界面发现模块，  新版短视频界面
 * Created by Zpj
 */

public class ShootVideoNewFragment extends Fragment implements RequestHttpCallBack.ReLoadListener {


    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "ShootVideoNewFragment";
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

    @Override
    public void onReload() {

    }

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

    private TextView tv_top_title;

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
        tv_ritht_title = viewRoot.findViewById(R.id.tv_ritht_title);
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tl_match_home = viewRoot.findViewById(R.id.tl_match_home);
        vp_match_home = viewRoot.findViewById(R.id.vp_match_home);

        tv_top_title = viewRoot.findViewById(R.id.tv_top_title);

    }

    private void initData() {
       // requestTabDatas();
        initTab();
    }

    private MatchTabBean matchTabBean;


    private String tagId = "-4";
    private DynamicBean dynamicBean;
    private void initTab() {

        updateTabs();
        if(true){
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();

        params.put("action","sys_feed");
        params.put("type",tagId);
        params.put("start",0);

        if(ll_net_error != null)   ll_net_error.setVisibility(View.GONE);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                if(ll_net_error != null)     ll_net_error.setVisibility(View.GONE);


                dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
          //      updateTabs();

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);

                if(NetWorkUtil.isConnected(QCaApplication.getContext())){
                    initTab();
                    if(ll_net_error != null)       ll_net_error.setVisibility(View.GONE);
                }else{

                    if(ll_net_error != null)     ll_net_error.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }


    List<String> titles = new ArrayList<>();

    private void updateTabs() {
        if(!this.isAdded())return;
        titles.clear();
        List<String> titlesNew = new ArrayList<>();
        if(false && dynamicBean != null && dynamicBean.data != null && dynamicBean.data.tag != null && dynamicBean.data.tag.size()>0){
            for (int i = 0; i < dynamicBean.data.tag.size(); i++) {
                List<String> strings = dynamicBean.data.tag.get(i);
                String title = strings.get(0);
                titles.add(title);
                String titleId = strings.get(1);
                TabLayout.Tab tab = tl_match_home.newTab();
                tab.setText(title);
                tab.setCustomView(getTabView(i));
                tl_match_home.addTab(tab);
                String newTitleStr = title + "," +titleId+","+"0";
                titlesNew.add(newTitleStr);
            }


            if(titles.size() > 1){
                tl_match_home.setVisibility(View.VISIBLE);
            }else{
                tl_match_home.setVisibility(View.GONE);
            }

        }else{
            titles.add("0");
            TabLayout.Tab tab = tl_match_home.newTab();
            tab.setText("");
            tab.setCustomView(getTabView(0));
            tl_match_home.addTab(tab);
            String newTitleStr =  "1," +"0,"+"0";
            titlesNew.add(newTitleStr);
            tl_match_home.setVisibility(View.GONE);
        }



        if(tl_match_home.isShown()){
            tv_top_title.setVisibility(View.GONE);
        }else{
            tv_top_title.setVisibility(View.VISIBLE);
        }


        final ShootVideoVpAdapter vpAdapter = new ShootVideoVpAdapter(getChildFragmentManager(),titlesNew);

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
        vp_match_home.setCurrentItem(0);

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

}
