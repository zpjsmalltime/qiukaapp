package com.mayisports.qca.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.BaseActivity;
import com.mayi.mayisports.activity.LoginActivity;
import com.mayi.mayisports.activity.MatchDetailActivity;
import com.mayisports.qca.bean.GuessCompetitionBean;
import com.mayisports.qca.bean.MatchTabBean;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.ScoreListBean;
import com.mayisports.qca.bean.StarStutasBean;
import com.mayisports.qca.bean.TeamRankListBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.MatchTabLayoutBinder;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.RefreshSocreUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.MyTitleListView;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 赛事fragment
 * Created by Zpj on 2018/1/25.
 */

public class TabLayoutFragment extends Fragment  implements  RefreshSocreUtils.OnRefreshCallBack, AdapterView.OnItemClickListener, View.OnClickListener, RequestHttpCallBack.ReLoadListener {

    private LocalBroadcastManager localBroadcastManager;
    private Rec rec;
    public static final String ACTION = "TabLayoutFragment";
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



    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getIntExtra(LoginActivity.RESULT,0) == 99 && isShow) {//首页双击刷新

                /**
                 * 模拟点击刷新键
                 */
                if(mtlv_tablayout_fg != null && mtlv_tablayout_fg.isShown()) {
                    iv_refresh_tablayout_fg.performClick();
                }
            }else if(intent.getIntExtra(LoginActivity.RESULT,0) != 99){

                initDatas();
            }
        }
    }

    private boolean isSelect = true;
    private boolean isShow;
    private boolean isSlientRefresh;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);
         isShow = isVisibleToUser;
        if (isVisibleToUser) {
            if(ll_no_data != null) {
                ll_no_data.setVisibility(View.GONE);
            }
                //可见时执行的操作
            refreshHandler.removeCallbacksAndMessages(null);
            refreshHandler.sendEmptyMessageDelayed(2,10);
            if("-1".equals(tagId)){
             if(mtlv_tablayout_fg != null){
                mtlv_tablayout_fg.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        initDatas();
                    }
                   },500);
                }
            }else{

                myAdapter.notifyDataSetChanged();

            }

        } else {
                //不可见时执行的操作
            refreshHandler.removeCallbacksAndMessages(null);
            RequestNetWorkUtils.kjHttp.cancleAll();

        }

    }


    @Override
    public void onResume() {
        super.onResume();
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshOk(Object object) {
        matchTabBean = (MatchTabBean) object;
        myAdapter.notifyDataSetChanged();
    }

    @SuppressLint("HandlerLeak")
    private Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 2){//更新
                RefreshSocreUtils.getData(matchTabBean,TabLayoutFragment.this,tagId);
                int anInt = SPUtils.getInt(QCaApplication.getContext(), Constant.Match_Thread_Interval);
                if(anInt == 0)anInt = 20;
                refreshHandler.sendEmptyMessageDelayed(2,1000*anInt);
            }

        }
    };




    public static String TABLAYOUT_FRAGMENT = "tab_fragment";
    private TextView txt;
    private String tagId;

    /**
     * 二级菜单0没有   1有
     */
    private int secondTitle;

    public static TabLayoutFragment newInstance(String tagId) {
        TabLayoutFragment fragment = new TabLayoutFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(TABLAYOUT_FRAGMENT, tagId);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String serializable = (String) getArguments().getSerializable(TABLAYOUT_FRAGMENT);
            String[] split = serializable.split(",");
            tagId = split[0];
            secondTitle = Integer.valueOf(split[1]);
        }

    }


    private View viewRoot;
    private LinearLayout ll_no_data;
    private LinearLayout ll_second_title;

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(viewRoot == null) {
            viewRoot = inflater.inflate(R.layout.fragment_tablayout, container, false);
        }
        viewRoot.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        },1000);
        return viewRoot;
    }

    private boolean isFirst = true;

    public void setData(){
        if(isFirst && viewRoot != null) {
            isFirst = false;
//        initDatas();
            initReceiver();
            initView();
            initDatas();
        }
    }

    private ListView mtlv_tablayout_fg;
    private MyAdapter myAdapter = new MyAdapter();
    private RelativeLayout rl_load_layout;
    private ImageView iv_refresh_tablayout_fg;
    private TextView tv_login_dy;

    private TextView tv_no_data;

    private static HashMap<String,Integer> followId = new HashMap<>() ;


    private RelativeLayout rl_europe_edit_groom;
    private LinearLayout ll_europe_edit_groom;
    private TextView tv_left_europe_edit_groom;
    private TextView tv_m_europe_edit_groom;
    private TextView tv_right_europe_edit_groom;



    private LinearLayout  ll_second;
    private LinearLayout  ll_scoreboard;

    private TextView tv_player_rank;
    private TextView tv_team_rank;


    private ListView lv_score_borad;//榜单list


    private LinearLayout ll_rank_list;
    private ListView lv_left_rank_list;
    private TextView tv_left_title;
    private TextView tv_right_title;
    private ListView lv_right_rank_list;


    private LinearLayout ll_no_rank;
    private LinearLayout ll_no_score;

    protected void initView() {


        ll_net_error = viewRoot.findViewById(R.id.ll_net_error);
        tv_refresh_net = viewRoot.findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clickTopType == 0){
                    initDatas();
                }else if(clickTopType == 1){
                    requestScoreNetData();
                }else if(clickTopType == 2){
                    requestRankListData();
                }

            }
        });


        ll_second_title = viewRoot.findViewById(R.id.ll_second_title);
        if(secondTitle == 1){
            ll_second_title.setVisibility(View.VISIBLE);
        }else{
            ll_second_title.setVisibility(View.GONE);
        }

        rl_europe_edit_groom = viewRoot.findViewById(R.id.rl_europe_edit_groom);
        ll_europe_edit_groom = viewRoot.findViewById(R.id.ll_europe_edit_groom);
        tv_left_europe_edit_groom = viewRoot.findViewById(R.id.tv_left_europe_edit_groom);
        tv_m_europe_edit_groom = viewRoot.findViewById(R.id.tv_m_europe_edit_groom);
        tv_right_europe_edit_groom = viewRoot.findViewById(R.id.tv_right_europe_edit_groom);
        tv_left_europe_edit_groom.setOnClickListener(this);
        tv_m_europe_edit_groom.setOnClickListener(this);
        tv_right_europe_edit_groom.setOnClickListener(this);
        ll_second = viewRoot.findViewById(R.id.ll_second);
        ll_scoreboard = viewRoot.findViewById(R.id.ll_scoreboard);


        tv_player_rank = viewRoot.findViewById(R.id.tv_player_rank);
        tv_player_rank.setOnClickListener(this);
        tv_team_rank = viewRoot.findViewById(R.id.tv_team_rank);
        tv_team_rank.setOnClickListener(this);

        lv_score_borad = viewRoot.findViewById(R.id.lv_score_borad);

        ll_rank_list = viewRoot.findViewById(R.id.ll_rank_list);
        lv_left_rank_list = viewRoot.findViewById(R.id.lv_left_rank_list);
        tv_left_title = viewRoot.findViewById(R.id.tv_left_title);
        tv_right_title = viewRoot.findViewById(R.id.tv_right_title);
        lv_right_rank_list = viewRoot.findViewById(R.id.lv_right_rank_list);




        txt = viewRoot.findViewById(R.id.tab_txt);
        mtlv_tablayout_fg = viewRoot.findViewById(R.id.mtlv_tablayout_fg);
        rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);
        iv_refresh_tablayout_fg = viewRoot.findViewById(R.id.iv_refresh_tablayout_fg);
        ll_no_data = viewRoot.findViewById(R.id.ll_no_data);
        tv_no_data = viewRoot.findViewById(R.id.tv_no_data);

        mtlv_tablayout_fg.setOnScrollListener(onScroll);
     //   mtlv_tablayout_fg.setAdapter(myAdapter);
        mtlv_tablayout_fg.setOnItemClickListener(this);
        txt.setText(tagId);

        iv_refresh_tablayout_fg.setOnClickListener(this);

        tv_login_dy = viewRoot.findViewById(R.id.tv_login_dy);
        tv_login_dy.setOnClickListener(this);


        tv_left_europe_edit_groom.performClick();


        ll_no_rank = viewRoot.findViewById(R.id.ll_no_rank);
        ll_no_score = viewRoot.findViewById(R.id.ll_no_score);

//        lv_score_borad.setAdapter(new MyScoreListAdapter());
    }


    /**
     * 积分榜adapter
     */
    class MyScoreListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(scoreListBean == null || scoreListBean.data == null){
                ll_no_score.setVisibility(View.VISIBLE);
                return 0;
            }
            ll_no_score.setVisibility(View.GONE);
            return scoreListBean.data.size()+1;
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

            if(position == scoreListBean.data.size()){
                convertView = View.inflate(QCaApplication.getContext(),R.layout.empty_height,null);
                convertView.setTag(null);
                return  convertView;
            }

            MyScoreListHolder myScoreListHolder;

            if(convertView == null || convertView.getTag() == null){
                myScoreListHolder = new MyScoreListHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.score_rank_item,null);

                myScoreListHolder.tv_number = convertView.findViewById(R.id.tv_number);
                myScoreListHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                myScoreListHolder.tv_name = convertView.findViewById(R.id.tv_name);
                myScoreListHolder.tv1 = convertView.findViewById(R.id.tv1);
                myScoreListHolder.tv2 = convertView.findViewById(R.id.tv2);
                myScoreListHolder.tv3 = convertView.findViewById(R.id.tv3);
                myScoreListHolder.tv4 = convertView.findViewById(R.id.tv4);
                myScoreListHolder.tv5 = convertView.findViewById(R.id.tv5);
                myScoreListHolder.tv6 = convertView.findViewById(R.id.tv6);
                convertView.setTag(myScoreListHolder);
            }else{
                myScoreListHolder = (MyScoreListHolder) convertView.getTag();
            }

            ScoreListBean.DataBean dataBean = scoreListBean.data.get(position);


            myScoreListHolder.tv_number.setText(position+1+"");

            String imgUrl =   "http://dldemo-img.stor.sinaapp.com/logo_"+ dataBean.logoId + ".png";
            PictureUtils.show(imgUrl,myScoreListHolder.iv_icon,R.drawable.team_logo);

            myScoreListHolder.tv_name.setText(URLDecoder.decode(dataBean.name)+"");

            myScoreListHolder.tv1.setText(dataBean._$0+dataBean._$1+dataBean._$3+"");
            myScoreListHolder.tv2.setText(dataBean._$3+"");
            myScoreListHolder.tv3.setText(dataBean._$1+"");
            myScoreListHolder.tv4.setText(dataBean._$0+"");

            myScoreListHolder.tv5.setText(dataBean.goal+"/"+dataBean.fumble);
            myScoreListHolder.tv6.setText(dataBean.score+"");





            return convertView;
        }
    }

    static class MyScoreListHolder{
        public TextView tv_number;
        public ImageView iv_icon;
        public TextView tv_name;
        public TextView tv1,tv2,tv3,tv4,tv5,tv6;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
        try {
            MatchTabBean.DataBean.MatchBean matchBean = matchTabBean.data.match.get(position);
            switch (matchBean.type) {
                case 1:
                    intent.putExtra("betId", matchBean.betId);
                    intent.putExtra("position", position);
                    String status = matchBean.status;
                    if((Constant.MATCH_NO_START.equals(status)|| Constant.MATCH_DELAY.equals(status))&&matchBean.playerList != 0){
                        intent.putExtra("type",4);//赛况
                    }
                    startActivityForResult(intent, 2);
                    break;
            }
        }catch (Exception e){

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(matchTabBean == null || matchTabBean.data == null)return;
        if(requestCode == 2&&resultCode == 2){
            int position = data.getIntExtra("position", -1);
            if(position != -1){
                int collection_status = data.getIntExtra("collection_status", 0);
                if(matchTabBean.data.match != null){
                    MatchTabBean.DataBean.MatchBean matchBean = matchTabBean.data.match.get(position);
                    matchBean.collection_status = collection_status;
                    if(collection_status == 1){
                        followId.put(matchBean.betId,matchBean.collection_status);
                    }else{
                        followId.remove(matchBean.betId);
                    }
                }
                if("-1".equals(tagId)&&collection_status == 0){
                    matchTabBean.data.match.remove(position);
                }

                myAdapter.notifyDataSetChanged();
            }
        }
    }

    private  RotateAnimation rotateAnimation;

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_refresh_tablayout_fg:
                if(rotateAnimation == null) {
                    rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                    rotateAnimation.setInterpolator(new LinearInterpolator());
                    rotateAnimation.setDuration(1000*1);
                }else{
                    iv_refresh_tablayout_fg.clearAnimation();
                }

                iv_refresh_tablayout_fg.startAnimation(rotateAnimation);

                initDatas();
                break;
            case R.id.tv_login_dy:

                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(),ACTION);
                }
                break;
            case R.id.tv_left_europe_edit_groom:
            case R.id.tv_m_europe_edit_groom:
            case R.id.tv_right_europe_edit_groom:
                updatePan(v.getId());
                break;
            case R.id.tv_player_rank:
            case R.id.tv_team_rank:
                updateRank(v.getId());
                break;
        }
    }

    private int clickRank = -1;
    private MyRankLeftAdapter myRankLeftAdapter = new MyRankLeftAdapter();
    private MyRankRightAdapter myRankRightAdapter = new MyRankRightAdapter();
    private void updateRank(int id) {
        if(id == R.id.tv_player_rank){//球员榜
            if(clickRank == 0)return;
            clickRank = 0;
            playerClickPosition = 0;



            Drawable drawable = QCaApplication.getContext().getResources().getDrawable(R.drawable.player_checked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_player_rank.setCompoundDrawables(drawable, null, null, null);


            Drawable drawable1 = QCaApplication.getContext().getResources().getDrawable(R.drawable.team_unchecked);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tv_team_rank.setCompoundDrawables(drawable1, null, null, null);

            tv_left_title.setText("球员");

            myRankLeftAdapter.setData(clickRank);
            lv_left_rank_list.setAdapter(myRankLeftAdapter);

            AdapterView.OnItemClickListener onItemClickListener = lv_left_rank_list.getOnItemClickListener();
//            if(onItemClickListener == null){
                lv_left_rank_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(playerClickPosition == position)return;
                        playerClickPosition = position;
                        List<List<String>> list = teamRankListBean.data.player.get(position).list;
                        myRankRightAdapter.setData(list,clickRank);
                        lv_right_rank_list.setAdapter(myRankRightAdapter);
                        myRankLeftAdapter.notifyDataSetChanged();

                    }
                });
//            }

            if(teamRankListBean != null && teamRankListBean.data != null && teamRankListBean.data.player != null) {
                List<List<String>> list = teamRankListBean.data.player.get(0).list;
                myRankRightAdapter.setData(list, clickRank);
                lv_right_rank_list.setAdapter(myRankRightAdapter);
            }

        }else if(id == R.id.tv_team_rank){//球队榜
            if(clickRank == 1)return;
            clickRank = 1;
            playerClickPosition = 0;
            Drawable drawable = QCaApplication.getContext().getResources().getDrawable(R.drawable.player_unchecked);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_player_rank.setCompoundDrawables(drawable, null, null, null);


            Drawable drawable1 = QCaApplication.getContext().getResources().getDrawable(R.drawable.team_checked);
            drawable1.setBounds(0, 0, drawable1.getMinimumWidth(), drawable1.getMinimumHeight());
            tv_team_rank.setCompoundDrawables(drawable1, null, null, null);

            tv_left_title.setText("球队");

            myRankLeftAdapter.setData(clickRank);
            lv_left_rank_list.setAdapter(myRankLeftAdapter);

            AdapterView.OnItemClickListener onItemClickListener = lv_left_rank_list.getOnItemClickListener();
//            if(onItemClickListener == null){
                lv_left_rank_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if(playerClickPosition == position)return;
                        playerClickPosition = position;
                        List<List<String>> list = teamRankListBean.data.team.get(position).list;
                        myRankRightAdapter.setData(list,clickRank);
                        lv_right_rank_list.setAdapter(myRankRightAdapter);
                        myRankLeftAdapter.notifyDataSetChanged();

                    }
                });
//            }
//
            if(teamRankListBean != null && teamRankListBean.data != null && teamRankListBean.data.team != null) {

                List<List<String>> list = teamRankListBean.data.team.get(0).list;
                myRankRightAdapter.setData(list, clickRank);
                lv_right_rank_list.setAdapter(myRankRightAdapter);
            }

        }
    }

    /**
     * 右侧title列表
     */
    class MyRankRightAdapter extends BaseAdapter{
        public List<List<String>> list;

        int type;
        public void setData(List<List<String>> list,int type){
            this.list = list;
            this.type = type;
        }

        @Override
        public int getCount() {
            if(list == null)return 0;
            return list.size()+1;
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


            if(position == list.size()){
                convertView = View.inflate(QCaApplication.getContext(),R.layout.empty_height,null);
                convertView.setTag(null);
                return  convertView;
            }

            ComonHolder comonHolder;
            if(convertView == null || convertView.getTag() == null){
                comonHolder = new ComonHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.right_rank_item,null);
                comonHolder.tv_number = convertView.findViewById(R.id.tv_number);
                comonHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
                comonHolder.tv_1 = convertView.findViewById(R.id.tv_1);
                comonHolder.tv_2 = convertView.findViewById(R.id.tv_2);
                comonHolder.tv_title_value = convertView.findViewById(R.id.tv_title_value);
                convertView.setTag(comonHolder);
            }else{
                comonHolder = (ComonHolder) convertView.getTag();
            }

            List<String> strings = list.get(position);

            comonHolder.tv_number.setText(position+1+"");

            if(type == 0){
                comonHolder.tv_1.setText(strings.get(1));
                comonHolder.tv_2.setText(strings.get(2));
                String imgUrl =   "http://dldemo-img.stor.sinaapp.com/logo_1_"+ strings.get(3) + ".png";

                PictureUtils.show(imgUrl,comonHolder.iv_icon,R.drawable.player_logo);
            }else if(type == 1){
                comonHolder.tv_1.setText(strings.get(1));
                comonHolder.tv_2.setVisibility(View.GONE);
                String imgUrl =   "http://dldemo-img.stor.sinaapp.com/logo_"+ strings.get(2) + ".png";

                PictureUtils.show(imgUrl,comonHolder.iv_icon,R.drawable.team_logo);
            }

            comonHolder.tv_title_value.setText(strings.get(0)+"");
            return convertView;
        }
    }


    private int playerClickPosition;
    class MyRankLeftAdapter extends BaseAdapter{

        private List<TeamRankListBean.DataBean.PlayerBean> player;

        public void setData(int type){
            ll_no_rank.setVisibility(View.GONE);
            if(type == 0){
                if(teamRankListBean == null || teamRankListBean.data == null || teamRankListBean.data.player == null){
                    player = null;
                }else{
                    player = teamRankListBean.data.player;
                }
            }else if(type == 1){
                if(teamRankListBean == null || teamRankListBean.data == null || teamRankListBean.data.team == null){
                    player = null;
                }else{
                    player = teamRankListBean.data.team;
                }
            }else{
                player = null;
            }

            if(player == null){
                ll_no_rank.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getCount() {
            if(player == null)return 0;
            return player.size();
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
            ComonHolder comonHolder;
            if(convertView == null){
                comonHolder = new ComonHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.left_title_item,null);

                comonHolder.ll_root = convertView.findViewById(R.id.ll_root);
                comonHolder.leftView = convertView.findViewById(R.id.leftView);
                comonHolder.tv_left_title = convertView.findViewById(R.id.tv_left_title);

                convertView.setTag(comonHolder);
            }else{
                comonHolder = (ComonHolder) convertView.getTag();
            }
            TeamRankListBean.DataBean.PlayerBean playerBean = player.get(position);


            if(position == playerClickPosition){
                comonHolder.ll_root.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.white_));
                comonHolder.leftView.setVisibility(View.VISIBLE);
                tv_right_title.setText(playerBean.title);
            }else{
                comonHolder.ll_root.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.coment_gray_bg));
                comonHolder.leftView.setVisibility(View.INVISIBLE);
            }

            comonHolder.tv_left_title.setText(playerBean.title);





            return convertView;
        }
    }

    static class ComonHolder{

        public View ll_root;
        public View leftView;
        public TextView tv_left_title;


        public TextView tv_number;
        public ImageView iv_icon;
        public TextView tv_1;
        public TextView tv_2;
        public TextView tv_title_value;
    }


    /**
     * 更新盘口
     */
    private int clickTopType = -1;
    private void updatePan(int resID){

        ll_no_data.setVisibility(View.GONE);
        switch (resID){

            case R.id.tv_left_europe_edit_groom:

                if(clickTopType == 0)return;
//                ll_europe_edit_groom.setBackgroundResource(R.drawable.tuijiandan_empty_3);
                ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_left);
                clickTopType = 0;
                ll_second.setVisibility(View.GONE);
                ll_scoreboard.setVisibility(View.GONE);
                mtlv_tablayout_fg.setVisibility(View.VISIBLE);
                iv_refresh_tablayout_fg.setVisibility(View.VISIBLE);
                lv_score_borad.setVisibility(View.GONE);
                ll_rank_list.setVisibility(View.GONE);
                clickRank = -1;
                break;
            case R.id.tv_m_europe_edit_groom:
                if(clickTopType == 1)return;

                ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_m);
                clickTopType = 1;
                ll_second.setVisibility(View.GONE);
                ll_scoreboard.setVisibility(View.VISIBLE);
                mtlv_tablayout_fg.setVisibility(View.GONE);
                iv_refresh_tablayout_fg.setVisibility(View.GONE);
                lv_score_borad.setVisibility(View.VISIBLE);
                ll_rank_list.setVisibility(View.GONE);
                ll_no_score.setVisibility(View.GONE);
                requestScoreNetData();
                clickRank = -1;
                break;
            case R.id.tv_right_europe_edit_groom:
                if(clickTopType == 2)return;
                ll_no_score.setVisibility(View.GONE);
                ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_righ);
                clickTopType = 2;
                ll_second.setVisibility(View.VISIBLE);
                ll_scoreboard.setVisibility(View.GONE);
                mtlv_tablayout_fg.setVisibility(View.GONE);
                iv_refresh_tablayout_fg.setVisibility(View.GONE);
                tv_player_rank.performClick();
                lv_score_borad.setVisibility(View.GONE);
                ll_rank_list.setVisibility(View.VISIBLE);
                requestRankListData();
                break;

        }

    }

    /**
     * 请求榜单数据
     */
    private TeamRankListBean teamRankListBean;
    private void requestRankListData() {
        if(teamRankListBean != null && teamRankListBean.data != null){
            clickRank = -1;
            tv_player_rank.performClick();
//            return;
        }

        String url = Constant.BASE_URL+"php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","league");
        params.put("type","statics");
        params.put("leagueId",tagId);


        ll_no_rank.setVisibility(View.GONE);
        ll_no_score.setVisibility(View.GONE);
        ll_no_data.setVisibility(View.GONE);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                teamRankListBean = JsonParseUtils.parseJsonClass(string,TeamRankListBean.class);
                if(teamRankListBean != null  && teamRankListBean.data != null){
                    clickRank = -1;
                   tv_player_rank.performClick();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    ll_net_error.setVisibility(View.GONE);
                }else{
                    ll_net_error.setVisibility(View.VISIBLE);
                }
            }
        });


    }


    /**
     * 请求积分榜数据
     */
    private ScoreListBean scoreListBean;
    private void requestScoreNetData() {
        if(scoreListBean != null && scoreListBean.data != null){
            lv_score_borad.setAdapter(new MyScoreListAdapter());
//            return;
        }

        String url = Constant.BASE_URL+"php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","league");
        params.put("type","ranking");
        params.put("leagueId",tagId);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                scoreListBean = JsonParseUtils.parseJsonClass(string,ScoreListBean.class);
                if(scoreListBean != null && scoreListBean.data != null){

                    lv_score_borad.setAdapter(new MyScoreListAdapter());

                }else{
                    ll_no_score.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if(NetworkUtils.isConnected(QCaApplication.getContext())){
                    ll_net_error.setVisibility(View.GONE);
                }else{
                    ll_net_error.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private boolean isInit;
    private void initDatas() {
           page = 0;
//           if(viewRoot != null) rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);
//           if(ll_no_data != null)ll_no_data.setVisibility(View.GONE);
           requestNetDatas();
           isInit = true;
    }

    private MatchTabBean matchTabBean;
    private int page;
    private int pre_page;
    private void requestNetDatas() {
        if(tagId == null)return;

        isSlientRefresh = false;


        //http://20180123.dldemo.applinzi.com/php/api.php?action=hotMatch_byDate&type=-1&start=0
        String url = Constant.BASE_URL+"php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","hotMatch_byDate");
        params.put("type",tagId);
        params.put("start",page);
        Log.e("url",params.getUrlParams().toString());
        ll_no_data.setVisibility(View.GONE);
        iv_refresh_tablayout_fg.setEnabled(false);

        if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {

              if(ll_net_error != null)  ll_net_error.setVisibility(View.GONE);
                if (page == 0){
                    matchTabBean = JsonParseUtils.parseJsonClass(string,MatchTabBean.class);
                    if (matchTabBean != null) {
                        MatchTabBean newBean = JsonParseUtils.parseJsonClass(string, MatchTabBean.class);
                        delData(matchTabBean,newBean);
                        sortDataByDate(TabLayoutFragment.this.matchTabBean.data.match);
                        pre_page = 1;
                        mtlv_tablayout_fg.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
//                        searchMatch();
                        if(isSelect) {
                            selectListView();
                        }

                    }
                }else{
                   try{
                       MatchTabBean newBean = JsonParseUtils.parseJsonClass(string, MatchTabBean.class);
                       MatchTabBean newBeanBean = JsonParseUtils.parseJsonClass(string, MatchTabBean.class);
                       delData(newBean,newBeanBean);
                        if(newBean.data.match.size()>0) {
                            TabLayoutFragment.this.matchTabBean.data.match.addAll(newBean.data.match);
                        }else{
                            pre_page = 0;
                        }
                    }catch (Exception e ){
                        pre_page = 0;
                    }
                    sortDataByDate(TabLayoutFragment.this.matchTabBean.data.match);
                    myAdapter.notifyDataSetChanged();
                }

                iv_refresh_tablayout_fg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_refresh_tablayout_fg.setEnabled(true);
                    }
                },1000);


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                iv_refresh_tablayout_fg.setEnabled(true);


                if(page == 0){
                  if(ll_net_error != null)  ll_net_error.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);

                isSelect = true;
                if(iv_refresh_tablayout_fg.getAnimation() != null){
//                    iv_refresh_tablayout_fg.clearAnimation();
                }

                if("-1".equals(tagId)){
                    tv_no_data.setText("你还没有关注过比赛");
                }else{
                    tv_no_data.setText("暂无信息");
                }

                if(LoginUtils.isUnLogin()){
                    if(tv_login_dy != null)tv_login_dy.setVisibility(View.VISIBLE);
                }else{
                    if(tv_login_dy != null)tv_login_dy.setVisibility(View.GONE);
                }



            }
        });
    }

    /**
     * 处理数据  type 1 显示
     * @param newBean
     */
    private synchronized void delData(MatchTabBean matchTabBean, MatchTabBean newBean) {
        if(matchTabBean.data != null && matchTabBean.data.match != null){
            matchTabBean.data.match.clear();
            for(int i = 0;i<newBean.data.match.size();i++){

                MatchTabBean.DataBean.MatchBean matchBean = newBean.data.match.get(i);
                if(matchBean.type == 1) {//1足球
                    matchTabBean.data.match.add(matchBean);
                    if(matchBean.collection_status == 1){
                        followId.put(matchBean.betId,matchBean.collection_status);
                    }else{
                        followId.remove(matchBean.betId);
                    }
                }
            }
        }
    }

    /**
     * 按时间顺序排列
     * @param match
     */
    private void sortDataByDate(List<MatchTabBean.DataBean.MatchBean> match) {
        if(match == null)return;
        Collections.sort(match, new Comparator<MatchTabBean.DataBean.MatchBean>() {
            @Override
            public int compare(MatchTabBean.DataBean.MatchBean o1, MatchTabBean.DataBean.MatchBean o2) {
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
//                    Date dt1 = format.parse(o1.getBirthday());
//                    Date dt2 = format.parse(o2.getBirthday());
                    if (Utils.dateToStamp(o1.getStrTitle()) > Utils.dateToStamp(o2.getStrTitle())) {
                        return 1;
                    } else if (Utils.dateToStamp(o1.getStrTitle()) < Utils.dateToStamp(o2.getStrTitle())) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    private  AbsListView.OnScrollListener onScroll = new AbsListView.OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(scrollState == SCROLL_STATE_IDLE){//停止滑动
//                PictureUtils.resumeLoad();
                iv_refresh_tablayout_fg.setEnabled(true);
            }else if(scrollState == SCROLL_STATE_TOUCH_SCROLL){//手触滑动
//                PictureUtils.pauseLoad();
                iv_refresh_tablayout_fg.setEnabled(true);
                try {

                        boolean first = matchTabBean.data.match.get(matchPositionFirst - 1).first;
                         if(first){
                             matchTabBean.data.match.get(matchPositionFirst - 1).first = false;
                             myAdapter.notifyDataSetChanged();
                         }
                }catch (Exception e){

                }

            }else{//惯性
//                PictureUtils.pauseLoad();
                iv_refresh_tablayout_fg.setEnabled(false);
            }
        }


        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

            if(matchTabBean == null || matchTabBean.data == null || matchTabBean.data.match == null)return;
            if (matchTabBean.data.match.size() == 1) {
                ((MyTitleListView) view).updateTitle(matchTabBean.data.match.get(0).getStrTitle());
            }

            if(firstVisibleItem >= matchTabBean.data.match.size() || firstVisibleItem+1 >= matchTabBean.data.match.size())return;

            if (matchTabBean.data.match != null && matchTabBean.data.match.size() > 1) {

                // 第一项与第二项标题不同，说明标题需要移动
                if (!TextUtils.equals(matchTabBean.data.match
                                .get(firstVisibleItem)
                                .getStrTitle(), matchTabBean.data.match.get(firstVisibleItem + 1)
                                .getStrTitle())) {
                    ((MyTitleListView) view).moveTitle(matchTabBean.data.match.get(
                            firstVisibleItem).getStrTitle());
                } else {
                    ((MyTitleListView) view).updateTitle(matchTabBean.data.match.get(
                            firstVisibleItem).getStrTitle());
                }
            }
        }
    };

    private HashMap<String,String> dateOfPosition = new HashMap<>();
    private int pos;
    private void setTitleShow(int position, int count, TextView tv,View line, MatchTabBean.DataBean.MatchBean  matchBeans,MatchTabBean.DataBean.MatchBean  preMatchBean){


        if (position == 0) {
            tv.setVisibility(View.VISIBLE);
            String strTitle = matchBeans.getStrTitle();
            dateOfPosition.put(strTitle,position+"");
            matchBeans.isShowTitle = true;
            line.setVisibility(View.GONE);
        } else if (position < count && !TextUtils.equals(matchBeans.getStrTitle(), preMatchBean.getStrTitle())) {
            tv.setVisibility(View.VISIBLE);
            line.setVisibility(View.GONE);
            String strTitle = matchBeans.getStrTitle();
            dateOfPosition.put(strTitle,position+"");
            matchBeans.isShowTitle = true;
        }else{
            tv.setVisibility(View.GONE);
            line.setVisibility(View.VISIBLE);
            matchBeans.isShowTitle = false;
        }
    }

    /**
     * 查找最新比赛
     * 优先级 ; 1，比赛中 2,未开赛 3，今日 4,最底
     */
    private int matchPositionFirst = -1;
    private int matchPositionSecond = -1;
    private void searchMatch(int position,MatchTabBean.DataBean.MatchBean matchBean){

        if(matchPositionFirst == -1 &&(Constant.MATCH_FIRST_HALF.equals(matchBean.status) || Constant.MATCH_HALF_TIME.equals(matchBean.status) || Constant.MATCH_SECOND_HALF.equals(matchBean.status))){
            matchPositionFirst = position;
            Log.e("matchPositionFirst",position+"--->"+tagId);
        }
        if(matchPositionSecond == -1 && Constant.MATCH_NO_START.equals(matchBean.status)){
            matchPositionSecond = position;
            Log.e("matchPositionSecond",position+"--->"+tagId);
        }


    }

    /**
     * 滑动到符合条件item
     */
    public static  int dp30 = DisplayUtil.dip2px(QCaApplication.getContext(), 30);
    private void selectListView(){

        if(myAdapter.getCount()<=1)return;
        for(int i = 0;i<matchTabBean.data.match.size();i++) {
            MatchTabBean.DataBean.MatchBean matchBean = matchTabBean.data.match.get(i);
            switch (matchBean.status){
                case Constant.MATCH_FIRST_HALF:
                case Constant.MATCH_HALF_TIME:
                case Constant.MATCH_SECOND_HALF:
                case Constant.MATCH_NO_START:
                    matchPositionFirst = i;
                    if(matchPositionFirst != -1){

                            try {
                                matchTabBean.data.match.get(i - 1).first = true;
                                mtlv_tablayout_fg.setSelection(matchPositionFirst - 1);
                            } catch (Exception e) {
                                matchTabBean.data.match.get(0).first = true;
                                mtlv_tablayout_fg.setSelection(0);
                            }

                        return;
                    }


                    String today = Utils.simpleDateFormatYY.format(new Date(System.currentTimeMillis()));
                    String position = dateOfPosition.get(today);
                    if(position != null){
                        int i1 = Integer.valueOf(position) - 1;
                        if(i1<0)i1 = 0;
                        mtlv_tablayout_fg.setSelection(i1);
                        return;
                    }

                    /**
                     * 滑到最底
                     */
                    if(myAdapter.getCount()>0) mtlv_tablayout_fg.setSelection(myAdapter.getCount()-1);

                    return;
            }

        }


        if(myAdapter.getCount()>0) mtlv_tablayout_fg.setSelection(myAdapter.getCount()-1);

    }

    @Override
    public void onReload() {

    }


    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {



            if(matchTabBean != null && matchTabBean.data != null && matchTabBean.data.match != null){

                int size = matchTabBean.data.match.size();
                if(size == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                }else{
                    size ++;
                }


                return size;
            }
            if(matchTabBean != null){
                ll_no_data.setVisibility(View.VISIBLE);
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

            if(matchTabBean == null || matchTabBean.data == null || matchTabBean.data.match == null)return convertView;
            if(position == matchTabBean.data.match.size()){
                convertView = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = convertView.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }

                convertView.setTag(null);
                return convertView;
            }


            MyHolder lvHolder;
            if (convertView == null || convertView.getTag() == null) {
                lvHolder = new MyHolder();
                convertView = View.inflate(getContext(), R.layout.match_fg_item, null);
                lvHolder.tv_title = convertView.findViewById(R.id.tv_date_title_item);
                lvHolder.iv_star = convertView.findViewById(R.id.iv_star);
                lvHolder.tv_left = convertView.findViewById(R.id.tv_left);
                lvHolder.tv_left_top = convertView.findViewById(R.id.tv_left_top);
                lvHolder.tv_left_bottom = convertView.findViewById(R.id.tv_left_bottom);
                lvHolder.tv_center_top = convertView.findViewById(R.id.tv_center_top);
                lvHolder.tv_center_bottom = convertView.findViewById(R.id.tv_center_bottom);
                lvHolder.tv_right_top = convertView.findViewById(R.id.tv_right_top);
                lvHolder.tv_right_bottom = convertView.findViewById(R.id.tv_right_bottom);
                lvHolder.iv_rigth = convertView.findViewById(R.id.iv_rigth);
//                lvHolder.tv = view.findViewById(R.id.tv_score_top_title);
                lvHolder.tv_date_title_item = convertView.findViewById(R.id.tv_date_title_item);
                lvHolder.ll_left_bottom = convertView.findViewById(R.id.ll_left_bottom);
                lvHolder.ll_right_bottom = convertView.findViewById(R.id.ll_right_bottom);
                lvHolder.ll_right_top = convertView.findViewById(R.id.ll_right_top);
                lvHolder.tv_time_pos = convertView.findViewById(R.id.tv_time_pos);
                lvHolder.iv_rigth1 = convertView.findViewById(R.id.iv_rigth1);
                lvHolder.iv_rigth2 = convertView.findViewById(R.id.iv_rigth2);
                lvHolder.tv_yellow_card_layout = convertView.findViewById(R.id.tv_yellow_card_layout);
                lvHolder.tv_red_card_layout = convertView.findViewById(R.id.tv_red_card_layout);
                lvHolder.tv_rank_layout = convertView.findViewById(R.id.tv_rank_layout);
                lvHolder.tv_team_name_layout = convertView.findViewById(R.id.tv_team_name_layout);
                lvHolder.tv_yellow_card_away = convertView.findViewById(R.id.tv_yellow_card_away);
                lvHolder.tv_red_card_away = convertView.findViewById(R.id.tv_red_card_away);
                lvHolder.tv_rank_away = convertView.findViewById(R.id.tv_rank_away);
                lvHolder.tv_team_name_away = convertView.findViewById(R.id.tv_team_name_away);
                lvHolder.iv_host_icon_match_detail = convertView.findViewById(R.id.iv_host_icon_match_detail);
                lvHolder.iv_away_icon_match_detail = convertView.findViewById(R.id.iv_away_icon_match_detail);
                lvHolder.ll_score_match_fg = convertView.findViewById(R.id.ll_score_match_fg);
                lvHolder.tv_live_url_bottom = convertView.findViewById(R.id.tv_live_url_bottom);
                lvHolder.v_split_line = convertView.findViewById(R.id.v_split_line);
                lvHolder.v_split_line_bottom = convertView.findViewById(R.id.v_split_line_bottom);
                lvHolder.tv_layerlist = convertView.findViewById(R.id.tv_layerlist);
                convertView.setTag(lvHolder);
            } else {
                lvHolder = (MyHolder) convertView.getTag();
            }


            lvHolder.tv_time_pos.setVisibility(View.GONE);
            lvHolder.tv_time_pos.clearAnimation();

            final MatchTabBean.DataBean.MatchBean matchBean = matchTabBean.data.match.get(position);


            Integer integer = followId.get(matchBean.betId);
            if(integer != null){
                matchBean.collection_status = integer;
            }else{
                matchBean.collection_status = 0;
            }


            MatchTabLayoutBinder.bindData(matchBean,lvHolder,position,tagId+"");

            lvHolder.iv_star.setOnClickListener(new View.OnClickListener() {//关注
                @Override
                public void onClick(View view) {
                    delStar(matchBean,position);
                }
            });



            /**
             * 显示title
             */
            if(position >0){
                MatchTabBean.DataBean.MatchBean matchBean1 = matchTabBean.data.match.get(position - 1);
                setTitleShow(position,getCount(),lvHolder.tv_title,lvHolder.v_split_line,matchBean,matchBean1);
            }else{
                lvHolder.tv_title.setVisibility(View.VISIBLE);
                String strTitle = matchBean.getStrTitle();
                dateOfPosition.put(strTitle,position+"");
            }

            if(matchBean.first){
                lvHolder.tv_title.setVisibility(View.VISIBLE);
            }


            if(position == getCount()-1){
                lvHolder.v_split_line_bottom.setVisibility(View.VISIBLE);
            }else{
                lvHolder.v_split_line_bottom.setVisibility(View.GONE);
            }



            /**
             * 自动加载更多
             */
            if(position == getCount()-3 || getCount()<3){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestNetDatas();
                }
            }

            return convertView;
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
     */
    private void delStar(final MatchTabBean.DataBean.MatchBean matchBean, final int position) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this.getActivity(), MatchFragment.ACTION);
            return;
        }

        requestCollect(matchBean,position);


    }

    /**
     * 收藏功能
     */
    private void requestCollect(final MatchTabBean.DataBean.MatchBean matchBean, final int position){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","collection");
        /** g
         * match:1 comment:2 recommendation:5 topic:6
         */
        params.put("subtype",1);
        params.put("id",matchBean.betId);
        RequestNetWorkUtils.kjHttp.cancel(url);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
                if(reportSubmitBean.status.result == 1){
//                    ToastUtils.toast("收藏成功");
//                    homeItemBean.data.recommendation.collection_status = "1";
                    matchBean.collection_status = 1;

                    followId.put(matchBean.betId,matchBean.collection_status);

                    NotificationsUtils.checkNotificationAndStartSetting(TabLayoutFragment.this.getActivity(),mtlv_tablayout_fg);
                }else if(reportSubmitBean.status.result == 0){
//                    ToastUtils.toast("取消收藏");
//                    homeItemBean.data.recommendation.collection_status = "0";
                    matchBean.collection_status = 0;
                    followId.remove(matchBean.betId);
                    if("-1".equals(tagId)){
                        matchTabBean.data.match.remove(position);
                    }
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    public  static class MyHolder{
        public TextView tv_title;
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

        public ImageView iv_host_icon_match_detail;
        public ImageView iv_away_icon_match_detail;

        public LinearLayout ll_score_match_fg;
        public TextView tv_live_url_bottom;
        public View v_split_line;
        public View v_split_line_bottom;

        public TextView tv_layerlist;
    }
}
