package com.mayisports.qca.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.MatchDetailActivity;
import com.mayi.mayisports.activity.SelectScoreActivity;
import com.mayisports.qca.bean.ImmediateScoreBean;
import com.mayisports.qca.bean.StarStutasBean;
import com.mayisports.qca.utils.BindViewUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.RefreshSocreUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 即时比分改版赛事模块  旧版
 * Created by Zpj on 2017/12/5.
 */

public class MatchFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener, RefreshSocreUtils.OnRefreshCallBack, RequestHttpCallBack.ReLoadListener {


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

    @Override
    public void onReload() {
//        ToastUtils.toast("reload");
    }

    private String[] sp;
    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();
//            initData();

            int select = intent.getIntExtra("select", 0);
            if(select == 1){
                rl_load_clone = rl_load_layout;
                page = 0;
                 sp = intent.getStringExtra("sp").split("-");
                 if(sp[2].equals("0")){//即时比分
                     reqeustDatas(type,page,tag);
                 }else{
                     reqeustDatas(type,page,sp[1]);
                 }

            }
        }
    }



    private  View viewRoot;

    private XRefreshView xfv_score_fg;
    private ListView lv_score_fg;
    private RecyclerView rv_score_fg;
    private View score_top_week_fg;


    private MyLvAdapter myLvAdapter = new MyLvAdapter();
    private MyRvAdapter myRvAdapter = new MyRvAdapter();



    private TextView tv_immediate_score_fg;
    private TextView tv_result_score_fg;
    private TextView tv_process_score_fg;
    private TextView tv_focus_score_fg;

    private View iv_immediate_score_fg;
    private View iv_result_score_fg;
    private View iv_process_score_fg;
    private View iv_focus_score_fg;

    private TextView tv_ritht_title;
    private ImageView iv_left_title;

    private RelativeLayout rl_load_layout;
    private RelativeLayout rl_load_clone;

    private LinearLayout ll_no_data;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(viewRoot == null){
            viewRoot = View.inflate(getContext(), R.layout.score_fg,null);
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
        lv_score_fg.setAdapter(myLvAdapter);
        rv_score_fg.setAdapter(myRvAdapter);
    }

    private long timeOf13 ;
    private void initView() {

        timeOf13  = Utils.getDayLongTime();
        initWeekBefor();

        rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);
        rl_load_clone = rl_load_layout;
        ll_no_data = viewRoot.findViewById(R.id.ll_no_data);

        initReceiver();

        iv_left_title = viewRoot.findViewById(R.id.iv_left_title);
        iv_left_title.setVisibility(View.INVISIBLE);
        tv_ritht_title = viewRoot.findViewById(R.id.tv_ritht_title);
        xfv_score_fg = viewRoot.findViewById(R.id.xfv_score_fg);
        initXFV();


        lv_score_fg = viewRoot.findViewById(R.id.lv_score_fg);
        lv_score_fg.setOnItemClickListener(this);

        rv_score_fg = viewRoot.findViewById(R.id.rv_score_fg);
        initRV();

        score_top_week_fg = viewRoot.findViewById(R.id.score_top_week_fg);
//        updateTopWeek();

        tv_immediate_score_fg = viewRoot.findViewById(R.id.tv_immediate_score_fg);
        tv_result_score_fg = viewRoot.findViewById(R.id.tv_result_score_fg);
        tv_process_score_fg =viewRoot.findViewById(R.id.tv_process_score_fg);
        tv_focus_score_fg = viewRoot.findViewById(R.id.tv_focus_score_fg);
        iv_immediate_score_fg = viewRoot.findViewById(R.id.iv_immediate_score_fg);
        iv_result_score_fg = viewRoot.findViewById(R.id.iv_result_score_fg);
        iv_process_score_fg =viewRoot.findViewById(R.id.iv_process_score_fg);
        iv_focus_score_fg = viewRoot.findViewById(R.id.iv_focus_score_fg);

        tv_immediate_score_fg.setOnClickListener(this);
        tv_result_score_fg.setOnClickListener(this);
        tv_process_score_fg.setOnClickListener(this);
        tv_focus_score_fg.setOnClickListener(this);
        tv_ritht_title.setOnClickListener(this);


        delCache();


        onClick(tv_immediate_score_fg);//默认选中第一个

        updateTopWeek(1);//初始化日期
    }
    private void delCache() {
        type = 0;
        page = 0;
        String json = SPUtils.getString(this.getActivity(), Constant.SCORE_PAGE_CACHE);
        if(!TextUtils.isEmpty(json)){
            rl_load_clone = null;

            ImmediateScoreBean  bean = JsonParseUtils.parseJsonClass(json, ImmediateScoreBean.class);
            if(bean.data != null){
                try {
                    delTags(json);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            immediateScoreBean = bean;
            if(immediateScoreBean != null && immediateScoreBean.data !=null)immediateScoreBean.data.tag = tag;
            myRvAdapter.notifyDataSetChanged();
            lv_score_fg.setAdapter(myLvAdapter);
        }

    }

    /**
     * 初始化日期
     */
    private int checkWeek;
    private int checkWeekProcess;
    private boolean isWeekUpdate;
    List<String> weekOfDate = Utils.getWeeksDaysBefore(false);
    List<String> weekOfDateAfter = Utils.getWeeksDaysAfter();

    /**
     * 更新顶部周几
     * @param type
     */
    private void updateTopWeek(final int type) {

        ViewGroup score_top_week_fg = (ViewGroup) this.score_top_week_fg;

        for(int i = 0;i<score_top_week_fg.getChildCount();i++){

            ViewGroup child = (ViewGroup) score_top_week_fg.getChildAt(i);
            child.setTag(i);

            TextView week = (TextView) child.getChildAt(0);
            TextView day = (TextView) child.getChildAt(1);
             String[] split;
            if(type == 1){//赛果
               split = weekOfDate.get(i).split(",");
            }else{//赛程
               split = weekOfDateAfter.get(i).split(",");
            }

            week.setText(split[0]);
            day.setText(split[1]);

            week.setTextColor(getResources().getColor(R.color.black_66));
            day.setTextColor(getResources().getColor(R.color.black_66));
            child.setBackground(null);


            final String[] finalSplit = split;
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = (int) view.getTag();
                    page = 0;
                    if(type == 1){
                        if(checkWeek == i)return;
                        checkWeek = i;
                        result_tag = finalSplit[2];
                        reqeustDatas(1,page,result_tag);
                    }else{
                        if(checkWeekProcess == i)return;
                        checkWeekProcess = i;
                        process_tag = finalSplit[2];
                        reqeustDatas(2,page,process_tag);
                    }

                    updateTopWeek(type);

                }
            });

            if(type == 1){
                if(i == checkWeek){
                    week.setTextColor(getResources().getColor(R.color.black_28));
                    day.setTextColor(getResources().getColor(R.color.black_28));
                    child.setBackground(getResources().getDrawable(R.drawable.score_top_week_check_bg));
                }
            }else{
                if(i == checkWeekProcess){
                    week.setTextColor(getResources().getColor(R.color.black_28));
                    day.setTextColor(getResources().getColor(R.color.black_28));
                    child.setBackground(getResources().getDrawable(R.drawable.score_top_week_check_bg));
                }
            }



        }
    }

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2){//更新
                switch (type){
                    case 0:
                        RefreshSocreUtils.getData(immediateScoreBean,MatchFragment.this);
                        break;
                    case 1:
                        initWeekBefor();

                        break;
                    case 2:
//                        reqeustDatas(type,page,process_tag);
                        break;
                    case 3:
                        RefreshSocreUtils.getData(immediateScoreBean,MatchFragment.this);
                        break;
                }


                int anInt = SPUtils.getInt(QCaApplication.getContext(), Constant.Match_Thread_Interval);
                if(anInt == 0)anInt = 20;
                refreshHandler.sendEmptyMessageDelayed(2,1000*anInt);
            }

        }
    };


    private void initWeekBefor(){
        if(isWeekUpdate)return;
        String minStart = Utils.getMinStart(timeOf13);
        Integer integer = Integer.valueOf(minStart);
        if(integer >=0&&!isWeekUpdate){
            weekOfDate = Utils.getWeeksDaysBefore(true);
            isWeekUpdate = true;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshHandler.removeCallbacksAndMessages(null);
        refreshHandler.sendEmptyMessageDelayed(2,500);
//        initWeekBefor();
    }

    @Override
    public void onStop() {
        super.onStop();
        refreshHandler.removeCallbacksAndMessages(null);
    }

    private void initRV() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_score_fg.setLayoutManager(linearLayoutManager);
        rv_score_fg.setHasFixedSize(true);
    }

    private void initXFV(){

        xfv_score_fg.setPullRefreshEnable(true);//设置允许下拉刷新
        xfv_score_fg.setPullLoadEnable(false);//设置允许上拉加载
        //刷新动画，需要自定义CustomGifHeader，不需要修改动画的会默认头布局
//        CustomGifHeader header = new CustomGifHeader(ct);
//        xfv_home_fg.setCustomHeaderView(header);
        xfv_score_fg.setMoveForHorizontal(true);
//        xfv_score_fg.setSilenceLoadMore(true);
//        xfv_score_fg.setHideFooterWhenComplete(true);
        xfv_score_fg.setXRefreshViewListener(new MyListener());

    }

    @Override
    public void onClick(View view) {
       switch (view.getId()){
           case R.id.tv_immediate_score_fg:
               selectOne(0);
               updateTopByType(1);
               page = 0;
               xfv_score_fg.setPullLoadEnable(false);
               tv_ritht_title.setVisibility(View.VISIBLE);
               reqeustDatas(0,page,tag);
               break;
           case R.id.tv_result_score_fg:
               selectOne(1);
               updateTopByType(2);
               checkWeek = 6;
               updateTopWeek(1);
               page = 0;
               xfv_score_fg.setPullLoadEnable(false);
               tv_ritht_title.setVisibility(View.VISIBLE);
               result_tag = weekOfDate.get(6).split(",")[2];
               reqeustDatas(1,page,result_tag);

               break;
           case R.id.tv_process_score_fg:
               selectOne(2);
               updateTopByType(2);
               checkWeekProcess = 0;
               updateTopWeek(2);
               page = 0;
               xfv_score_fg.setPullLoadEnable(false);
               tv_ritht_title.setVisibility(View.VISIBLE);
               process_tag = weekOfDateAfter.get(0).split(",")[2];
               reqeustDatas(2,page,process_tag);
               break;
           case R.id.tv_focus_score_fg:

               if(LoginUtils.isUnLogin()){
                   LoginUtils.goLoginActivity(this.getActivity(), MatchFragment.ACTION);
                   return;
               }

               xfv_score_fg.setPullLoadEnable(false);
               selectOne(3);
               updateTopByType(3);
               tv_ritht_title.setVisibility(View.INVISIBLE);
               page = 0;

               reqeustDatas(3,page,process_tag);

               break;

           case R.id.tv_ritht_title:

//               if(LoginUtils.isUnLogin()){
//                   LoginUtils.goLoginActivity(this.getActivity(),ScoreFragment.ACTION);
//                   return;
//               }

               Intent intent = new Intent(this.getActivity(), SelectScoreActivity.class);
               intent.putExtra("flag",type);
               if(type == 1){//赛果
                   intent.putExtra("date",result_tag);
               }else if(type == 2){//赛程
                   intent.putExtra("date",process_tag);
               }

               startActivity(intent);
               break;
       }


    }


    private int type = -1;
    /**
     * 通用bean
     */
    ImmediateScoreBean immediateScoreBean;
    private int page;
    private void reqeustDatas(final int i, final int page, final String tag) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String format = sdf.format(new Date());
        String url = Constant.BASE_URL + "/php/api.php";
        final HttpParams params = new HttpParams();
        HttpParams newParams = new HttpParams();
        switch (i){
            case 0: //即时比分  缺少加载更多限制
                params.put("action","matchListByDate");
                params.put("date",format.substring(2));
                params.put("start",page);
                params.put("count",20);
                params.put("type",1);
                params.put("leagueId",tag);

                String string = SPUtils.getString(this.getContext(), Constant.TYPE + 0 + 1 + "");//赛事
                if(!TextUtils.isEmpty(string)){
                    newParams.put("leagueFilter",string);
                }else{
                    String strings = SPUtils.getString(this.getContext(), Constant.TYPE + 0 + 2 + "");//赛事
                    newParams.put("asiaTapeFilter",strings);
                }

                break;
            case 1://赛果
                params.put("action","matchList");
                params.put("type","matchList");
                String tag1 = tag;
                params.put("date",tag);

                String string_result = SPUtils.getString(this.getContext(), Constant.TYPE + 1 + 1 +tag );//赛事
                if(!TextUtils.isEmpty(string_result)){
                    newParams.put("leagueFilter",string_result);
                }

                break;
            case 2://赛程
                params.put("action","matchList");
                params.put("type","matchList");
                params.put("date",tag);

                String string_process = SPUtils.getString(this.getContext(), Constant.TYPE + 2 + 1 + tag);//赛事
                if(!TextUtils.isEmpty(string_process)){
                    newParams.put("leagueFilter",string_process);
                }
                break;
            case 3://关注
                params.put("action","matchListByDate");
                params.put("start",page);
                params.put("count",20);
                params.put("type",3);
                break;

        }

        String string = url + params.getUrlParams().toString();
        Log.e("url",string);

        RequestNetWorkUtils.postRequest(string, newParams, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {
                if(xfv_score_fg != null) {
                    xfv_score_fg.stopLoadMore();
                    xfv_score_fg.stopRefresh();
                }
                type = i;
                switch (i){
                    case 0: //即时比分
                        if(page == 0) {
                            SPUtils.putString(QCaApplication.getContext(), Constant.SCORE_PAGE_CACHE,string);
                          ImmediateScoreBean  bean = JsonParseUtils.parseJsonClass(string, ImmediateScoreBean.class);
                          if(bean.data != null){
                              try {
                                  delTags(string);
                              } catch (Exception e) {
                                  e.printStackTrace();
                              }
                          }
                           immediateScoreBean = bean;
                            if(immediateScoreBean != null && immediateScoreBean.data !=null){
                                immediateScoreBean.data.tag = tag;
                                if(!"0".equals(tag))immediateScoreBean.data.pre_page = 1;
                            }

                            myRvAdapter.notifyDataSetChanged();
                            lv_score_fg.setAdapter(myLvAdapter);
                        }else{

                            ImmediateScoreBean newBean = JsonParseUtils.parseJsonClass(string, ImmediateScoreBean.class);

                            try {
                                immediateScoreBean.data.matchlist.addAll(newBean.data.matchlist);
                                immediateScoreBean.data.pre_page = newBean.data.pre_page;
                                if(!"0".equals(tag))immediateScoreBean.data.pre_page = 1;
                            }catch (Exception e){
//                                ToastUtils.toast("暂无更多");
                                immediateScoreBean.data.pre_page = 0;
                            }
                        }
                        if(immediateScoreBean != null && immediateScoreBean.data !=null)immediateScoreBean.data.tag = tag;

                        myLvAdapter.notifyDataSetChanged();
                        break;
                    case 1://赛果
                        Log.e("type","onSuccess_1");
                        immediateScoreBean = JsonParseUtils.parseJsonClass(string,ImmediateScoreBean.class);
                        ImmediateScoreBean newBean = JsonParseUtils.parseJsonClass(string, ImmediateScoreBean.class);
                        delResult(MatchFragment.this.immediateScoreBean,newBean);
                        lv_score_fg.setAdapter(myLvAdapter);
//                        myLvAdapter.notifyDataSetChanged();

                        break;
                    case 2:
                        MatchFragment.this.immediateScoreBean = JsonParseUtils.parseJsonClass(string,ImmediateScoreBean.class);
                        ImmediateScoreBean newBean2 = JsonParseUtils.parseJsonClass(string, ImmediateScoreBean.class);
                        delProcess(MatchFragment.this.immediateScoreBean,newBean2);
                        lv_score_fg.setAdapter(myLvAdapter);
//                        myLvAdapter.notifyDataSetChanged();
                        break;
                    case 3://关注

                        if(page == 0) {
//                            SPUtils.putString(QCaApplication.getContext(), Constant.SCORE_PAGE_CACHE,string);
                            ImmediateScoreBean  bean = JsonParseUtils.parseJsonClass(string, ImmediateScoreBean.class);
//                            if(bean.data != null){
//                                try {
//                                    delTags(string);
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
                            immediateScoreBean = bean;
                            immediateScoreBean.data.pre_page = 1;
//                            if(immediateScoreBean != null && immediateScoreBean.data !=null)immediateScoreBean.data.tag = tag;
//                            myRvAdapter.notifyDataSetChanged();
                            lv_score_fg.setAdapter(myLvAdapter);
                        }else{

                            ImmediateScoreBean newBean1 = JsonParseUtils.parseJsonClass(string, ImmediateScoreBean.class);

                            try {
                                immediateScoreBean.data.matchlist.addAll(newBean1.data.matchlist);
                            }catch (Exception e){
//                                ToastUtils.toast("暂无更多");
                                immediateScoreBean.data.pre_page = 0;
                            }
                        }
//                        if(immediateScoreBean != null && immediateScoreBean.data !=null)immediateScoreBean.data.tag = tag;

                        myLvAdapter.notifyDataSetChanged();




//                        ScoreFragment.this.immediateScoreBean = JsonParseUtils.parseJsonClass(string,ImmediateScoreBean.class);
//                        lv_score_fg.setAdapter(myLvAdapter);


//                        myLvAdapter.notifyDataSetChanged();
                        break;

                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                   ToastUtils.toast(Constant.NET_FAIL_TOAST);
                   if(xfv_score_fg != null) {
                       xfv_score_fg.stopLoadMore();
                       xfv_score_fg.stopRefresh();
                   }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_clone = rl_load_layout;
            }
        });
    }

    /**
     *筛选赛果
     * @param immediateScoreBean
     * @param newBean
     */
    private void delResult(ImmediateScoreBean immediateScoreBean, ImmediateScoreBean newBean) {
        if(newBean.data != null&&newBean.data.matchList != null) {
            List<ImmediateScoreBean.DataBean.MatchlistBean> matchList = immediateScoreBean.data.matchList;
            matchList.clear();
            for (int i = 0; i <newBean.data.matchList.size();i++) {
                ImmediateScoreBean.DataBean.MatchlistBean matchlistBean = newBean.data.matchList.get(i);
                if("COMPLETE".equals(matchlistBean.match.status)) {
                    matchList.add(matchlistBean);
                }
            }
        }
    }

    /**
     *筛选赛程
     * @param immediateScoreBean
     * @param newBean
     */
    private void delProcess(ImmediateScoreBean immediateScoreBean, ImmediateScoreBean newBean) {
        if(newBean.data != null&&newBean.data.matchList != null) {
            List<ImmediateScoreBean.DataBean.MatchlistBean> matchList = immediateScoreBean.data.matchList;
            matchList.clear();
            for (int i = 0; i <newBean.data.matchList.size();i++) {
                ImmediateScoreBean.DataBean.MatchlistBean matchlistBean = newBean.data.matchList.get(i);
                if("NO_START".equals(matchlistBean.match.status)) {
                    matchList.add(matchlistBean);
                }
            }
        }
    }


    /**
     * 刷新顶部tab状态
     * @param postion
     */
    private void selectOne(int postion){

        tv_immediate_score_fg.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_immediate_score_fg.setVisibility(View.INVISIBLE);

        tv_result_score_fg.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_result_score_fg.setVisibility(View.INVISIBLE);

        tv_process_score_fg.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_process_score_fg.setVisibility(View.INVISIBLE);

        tv_focus_score_fg.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_focus_score_fg.setVisibility(View.INVISIBLE);


        switch (postion){
            case 0:
                tv_immediate_score_fg.setTextColor(getResources().getColor(R.color.score_black_top_title));
                iv_immediate_score_fg.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_result_score_fg.setTextColor(getResources().getColor(R.color.score_black_top_title));
                iv_result_score_fg.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_process_score_fg.setTextColor(getResources().getColor(R.color.score_black_top_title));
                iv_process_score_fg.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_focus_score_fg.setTextColor(getResources().getColor(R.color.score_black_top_title));
                iv_focus_score_fg.setVisibility(View.VISIBLE);
                break;
        }
    }


    /**
     * 根据 关注，赛程，刷新顶部
     * 1 显示 tag标签
     * 2 显示周几日期
     * 3 全部隐藏
     */
    private void updateTopByType(int type){
        switch (type){
            case 1:
                rv_score_fg.setVisibility(View.VISIBLE);
                score_top_week_fg.setVisibility(View.GONE);
                break;
            case 2:
                rv_score_fg.setVisibility(View.GONE);
                score_top_week_fg.setVisibility(View.VISIBLE);
                break;
            case 3:
                rv_score_fg.setVisibility(View.GONE);
                score_top_week_fg.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
        Log.e("position",i+"");
        if(type == 0 || type == 3){
//           Constant.matchlistBean = immediateScoreBean.data.matchlist.get(i);
            intent.putExtra("betId",immediateScoreBean.data.matchlist.get(i).match.betId);
        }else {
//           Constant.matchlistBean = immediateScoreBean.data.matchList.get(i);
            intent.putExtra("betId",immediateScoreBean.data.matchList.get(i).match.betId);
        }

        intent.putExtra("position",i);
        startActivityForResult(intent,2);
    }

    /**
     * 时时刷新回调
     * @param immediateScoreBean
     */
    @Override
    public void onRefreshOk(Object immediateScoreBean) {
       this.immediateScoreBean = (ImmediateScoreBean) immediateScoreBean;
        myLvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2&&resultCode == 2){
            int position = data.getIntExtra("position", -1);
            if(position != -1){
                int collection_status = data.getIntExtra("collection_status", 0);
                if(immediateScoreBean.data.matchList != null){
                    immediateScoreBean.data.matchList.get(position).match.collection_status = collection_status;
                }else{
                    immediateScoreBean.data.matchlist.get(position).match.collection_status = collection_status;
                }
                myLvAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 加载刷新监听
     */
    class MyListener extends XRefreshView.SimpleXRefreshListener {


        @Override
        public void onRefresh(boolean isPullDown) {
            rl_load_clone = null;
            switch (type){
                case 0:
                    page = 0;
                    reqeustDatas(0,page,tag+"");
                    break;
                case 1:
                    reqeustDatas(1,page,result_tag+"");
                    break;
                case 2:
                    reqeustDatas(2,page,process_tag+"");
                    break;
                case 3:
                    reqeustDatas(3,page,"");
                    break;
            }


        }

        @Override
        public void onLoadMore(boolean isSilence) {
            rl_load_clone = null;
            switch (type){
                case 0:
//                    if(immediateScoreBean != null && immediateScoreBean.data != null){
//                        if("0".equals(tag)&&immediateScoreBean.data.pre_page == 0){
//                            ToastUtils.toast("暂无更多");
//                            xfv_score_fg.stopLoadMore();
//                            return;
//                        }
//                    }
//
//                    page +=10;
//                    reqeustDatas(0,page,tag+"");
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3://关注
//                    page +=10;
//                    reqeustDatas(3,page,tag+"");
                    break;
            }

        }

    }


    class MyLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            switch (type){
                case 0:
                    if(immediateScoreBean != null && immediateScoreBean.data != null && immediateScoreBean.data.matchlist != null) {
                        if(immediateScoreBean.data.matchlist.size()>0){
                            ll_no_data.setVisibility(View.GONE);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                        }

                        return immediateScoreBean.data.matchlist.size();
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        return 0;
                    }
                case 1:
                    if(immediateScoreBean != null && immediateScoreBean.data != null && immediateScoreBean.data.matchList != null) {
                        if(immediateScoreBean.data.matchList.size() >0){
                            ll_no_data.setVisibility(View.GONE);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                        }

                        return immediateScoreBean.data.matchList.size();
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        return 0;
                    }

                case 2:
                    if(immediateScoreBean != null && immediateScoreBean.data != null && immediateScoreBean.data.matchList != null) {
                        if(immediateScoreBean.data.matchList.size()>0) {
                            ll_no_data.setVisibility(View.GONE);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                        }
                        return immediateScoreBean.data.matchList.size();
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        return 0;
                    }
                case 3:
                    if(immediateScoreBean != null && immediateScoreBean.data != null && immediateScoreBean.data.matchlist != null) {
                        if(immediateScoreBean.data.matchlist.size() >0) {
                            ll_no_data.setVisibility(View.GONE);
                        }else{
                            ll_no_data.setVisibility(View.VISIBLE);
                        }
                        return immediateScoreBean.data.matchlist.size();
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                        return 0;
                    }
            }
            ll_no_data.setVisibility(View.VISIBLE);
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

            LvHolder lvHolder;
            if(view == null){
                lvHolder = new LvHolder();
                view = View.inflate(getContext(),R.layout.item_score_fg,null);
                lvHolder.iv_star = view.findViewById(R.id.iv_star);
                lvHolder.tv_left = view.findViewById(R.id.tv_left);
                lvHolder.tv_left_top = view.findViewById(R.id.tv_left_top);
                lvHolder.tv_left_bottom = view.findViewById(R.id.tv_left_bottom);
                lvHolder.tv_center_top = view.findViewById(R.id.tv_center_top);
                lvHolder.tv_center_bottom = view.findViewById(R.id.tv_center_bottom);
                lvHolder.tv_right_top = view.findViewById(R.id.tv_right_top);
                lvHolder.tv_right_bottom = view.findViewById(R.id.tv_right_bottom);
                lvHolder.iv_rigth = view.findViewById(R.id.iv_rigth);
//                lvHolder.tv = view.findViewById(R.id.tv_score_top_title);
                lvHolder.tv_date_title_item = view.findViewById(R.id.tv_date_title_item);
                lvHolder.ll_left_bottom = view.findViewById(R.id.ll_left_bottom);
                lvHolder.ll_right_bottom = view.findViewById(R.id.ll_right_bottom);
                lvHolder.ll_right_top = view.findViewById(R.id.ll_right_top);
                lvHolder.tv_time_pos = view.findViewById(R.id.tv_time_pos);
                lvHolder.iv_rigth1 = view.findViewById(R.id.iv_rigth1);
                lvHolder.iv_rigth2 = view.findViewById(R.id.iv_rigth2);
                lvHolder.tv_yellow_card_layout = view.findViewById(R.id.tv_yellow_card_layout);
                lvHolder.tv_red_card_layout = view.findViewById(R.id.tv_red_card_layout);
                lvHolder.tv_rank_layout = view.findViewById(R.id.tv_rank_layout);
                lvHolder.tv_team_name_layout = view.findViewById(R.id.tv_team_name_layout);
                lvHolder.tv_yellow_card_away = view.findViewById(R.id.tv_yellow_card_away);
                lvHolder.tv_red_card_away = view.findViewById(R.id.tv_red_card_away);
                lvHolder.tv_rank_away = view.findViewById(R.id.tv_rank_away);
                lvHolder.tv_team_name_away = view.findViewById(R.id.tv_team_name_away);
                view.setTag(lvHolder);
            }else{
                lvHolder = (LvHolder) view.getTag();
            }

            lvHolder.tv_time_pos.setVisibility(View.GONE);
            lvHolder.tv_time_pos.clearAnimation();

            switch (type){
                case 0:
                    lvHolder.tv_date_title_item.setVisibility(View.GONE);
                    BindViewUtils.updateImmediate(lvHolder,immediateScoreBean,i);
                    break;
                case 1:
                    lvHolder.tv_date_title_item.setVisibility(View.GONE);
                    BindViewUtils.updateResult(lvHolder,immediateScoreBean,i);
                    break;
                case 2:
                    lvHolder.tv_date_title_item.setVisibility(View.GONE);
                    BindViewUtils.updateProcess(lvHolder,immediateScoreBean,i);
                    break;
                case 3:
                    ImmediateScoreBean.DataBean.MatchlistBean matchlistBean = immediateScoreBean.data.matchlist.get(i);
                    String titleDate = getTitleDate(matchlistBean.match.timezoneoffset + "000");
                    if(i == 0 ){
                        lvHolder.tv_date_title_item.setVisibility(View.VISIBLE);
                        lvHolder.tv_date_title_item.setText(titleDate);
                    }else{
                        ImmediateScoreBean.DataBean.MatchlistBean preBean = immediateScoreBean.data.matchlist.get(i - 1);

                        String preTimezoneoffset = getTitleDate(preBean.match.timezoneoffset+"000");
                        if(!titleDate.equals(preTimezoneoffset)){
                           lvHolder.tv_date_title_item.setVisibility(View.VISIBLE);
                           lvHolder.tv_date_title_item.setText(titleDate);
                        }else{
                            lvHolder.tv_date_title_item.setVisibility(View.GONE);
                        }


                    }
                    BindViewUtils.updateImmediate(lvHolder,immediateScoreBean,i);
                    break;


            }


            lvHolder.iv_star.setOnClickListener(new View.OnClickListener() {//关注
                @Override
                public void onClick(View view) {
                     delStar(immediateScoreBean,i);
                }
            });


            if(i == getCount()-3 ||(getCount()<3)){

                rl_load_clone = null;
                switch (type){
                    case 0:
                        if(immediateScoreBean != null && immediateScoreBean.data != null){
                            if("0".equals(tag)&&immediateScoreBean.data.pre_page == 0){
//                                ToastUtils.toast("暂无更多");
                                xfv_score_fg.stopLoadMore();
                                break;
                            }else if(immediateScoreBean.data.pre_page == 0){
                                break;
                            }
                        }

                        if(page == page+20)break;
                        page +=20;
                        reqeustDatas(0,page,tag+"");
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3://关注
                        if(immediateScoreBean.data.pre_page == 0)break;
                        if(page == page+20)break;
                        page +=20;
                        reqeustDatas(3,page,tag+"");
                        break;
                }


            }


            return view;
        }
    }


    private String  getTitleDate(String time){
        Long aLong = Long.valueOf(time);
        Date date = new Date(aLong);
        String format = Utils.simpleDateFormatSplitLine.format(date);
        String weekOfDate = Utils.getWeekOfDate(date);
        return format+" "+weekOfDate;
    }

    /**
     * 处理关注动作
     * @param immediateScoreBean
     * @param i
     *
     */
    private void delStar(ImmediateScoreBean immediateScoreBean, int i) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(), MatchFragment.ACTION);
            return;
        }

        final ImmediateScoreBean.DataBean.MatchlistBean bean;
        if(type == 0 || type == 3) {
            bean = immediateScoreBean.data.matchlist.get(i);
        }else{
            bean = immediateScoreBean.data.matchList.get(i);
        }

        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","match_collection");
        params.put("betId",bean.match.betId+"");

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                StarStutasBean starStutasBean = JsonParseUtils.parseJsonClass(string, StarStutasBean.class);
                if(starStutasBean.status.result == 1) {//关注成功
                    ToastUtils.toast("关注成功");
                }else{
                    ToastUtils.toast("取消关注");
                }

                bean.match.collection_status = starStutasBean.status.result;
                myLvAdapter.notifyDataSetChanged();
//                reqeustDatas(type,,tag);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });

    }

    public static class LvHolder {
        public TextView tv;
        public ImageView iv_star;
        public TextView tv_left;
        public TextView tv_left_top;
        public TextView tv_left_bottom;
        public TextView tv_center_top;
        public TextView tv_center_bottom;
        public TextView tv_right_top;
        public TextView tv_right_bottom;
        public ImageView iv_rigth;
        public TextView tv_date_title_item;
        public LinearLayout ll_left_bottom;
        public LinearLayout ll_right_bottom;
        public LinearLayout ll_right_top;
        public TextView tv_time_pos;
        public View iv_rigth1;
        public View iv_rigth2;
        public TextView tv_yellow_card_layout;

        public TextView tv_red_card_layout;
        public TextView tv_rank_layout;
        public TextView tv_team_name_layout;
        public TextView tv_team_name_away;
        public TextView tv_rank_away;
        public TextView tv_red_card_away;
        public TextView tv_yellow_card_away;
    }

    private int checkPostion;
    private String tag = "0";
    private String result_tag;
    private String process_tag;
    class MyRvAdapter extends RecyclerView.Adapter<RvHolder>{

        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RvHolder rvHolder = new RvHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.item_text_view, parent,
                    false));
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(RvHolder holder, final int position) {

            final String[] split = tagsList.get(position).split(":");


            String s = split[0].replace("\"","");
            if(s.contains("(")){
                String substring = s.substring(s.indexOf("("));
                s = s.substring(0,s.indexOf("("));
                s = Utils.decodeUnicode(s)+substring;
            }else{
                s = Utils.decodeUnicode(s);
            }

            holder.tv_score_top_title.setText(s);



            holder.tv_score_top_title.setTextColor(getResources().getColor(R.color.socre_top_uncheck_item));
            holder.tv_score_top_title.setBackground(getResources().getDrawable(R.drawable.score_top_title_uncheck_bg));
            if(position == checkPostion){
                holder.tv_score_top_title.setTextColor(getResources().getColor(R.color.socre_top_item));
                holder.tv_score_top_title.setBackground(getResources().getDrawable(R.drawable.score_top_title_bg));
            }
            holder.tv_score_top_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                      checkPostion = position;


//                    try {
//                        String declaredField = immediateScoreBean.data.tags.getClass().getField(name).getName();
//                        tag = declaredField;
                        tag = split[1];
                        page = 0;
                        reqeustDatas(0,page,tag);
                        myRvAdapter.notifyDataSetChanged();
//                    } catch (NoSuchFieldException e) {
//                        e.printStackTrace();
//                    }

                }
            });
        }

        @Override
        public int getItemCount() {
//            int length = immediateScoreBean.data.tags.getClass().getSuperclass().getFields().length;
//            int le =  immediateScoreBean.data.tags.getClass().getFields().length - length;
            if(type == 0)return  tagsList.size();
            return 0;
        }
    }

    private List<String> tagsList = new ArrayList<>();
    private void   delTags(String string) throws Exception{

        tagsList = new ArrayList<>();

        int tagIndex = string.indexOf("tags");
        if(tagIndex == -1)return;
        int i1 = string.indexOf("{", tagIndex);

        int i2 = string.indexOf("}", tagIndex);
        String substring = string.substring(i1 + 1, i2);
        String[] split = substring.split(",");
        for(int i = 0;i<split.length;i++){
            tagsList.add(split[i]);
        }

    }


    static class RvHolder extends RecyclerView.ViewHolder{

        public TextView tv_score_top_title;
        public RvHolder(View itemView) {
            super(itemView);
            tv_score_top_title = itemView.findViewById(R.id.tv_score_top_title);
        }
    }

}
