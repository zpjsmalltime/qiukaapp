package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.PublishGroomMatchBean;
import com.mayisports.qca.bean.PublishGroomTeamBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 发布推荐
 */
public class PublishGroomActivity extends BaseActivity  {


    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "PublishGroomActivity";
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
            if(matchListBean != null){
                matchListBean.recommendation_status = 1;
                myTeamAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }


    public static void start(Activity activity){
        Intent intent = new Intent(activity,PublishGroomActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_publish_groom;
    }

    private ListView lv_match_name_groom;
    private ListView lv_team_name_groom;
    private RelativeLayout rl_load_layout;
    private MyMatchAdapter myMatchAdapter = new MyMatchAdapter();
    private MyTeamAdapter myTeamAdapter = new MyTeamAdapter();

    private LinearLayout ll_no_data;
    @Override
    protected void initView() {
        super.initView();
        initReceiver();
        setTitleShow(true);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("发布推荐");

        lv_match_name_groom = findViewById(R.id.lv_match_name_groom);
        lv_team_name_groom = findViewById(R.id.lv_team_name_groom);
        rl_load_layout = findViewById(R.id.rl_load_layout);

        lv_match_name_groom.setAdapter(myMatchAdapter);
        lv_team_name_groom.setAdapter(myTeamAdapter);
        lv_match_name_groom.setOnItemClickListener(new MyMatchItemClick());
        lv_team_name_groom.setOnItemClickListener(new MyTeamItemClick());

        ll_no_data = findViewById(R.id.ll_no_data);


    }



    @Override
    protected void initDatas() {
        super.initDatas();
        requestNetDatas();
    }

    private PublishGroomMatchBean publishGroomMatchBean;
    private void requestNetDatas() {
        rl_load_layout.setVisibility(View.VISIBLE);
        //http://app.mayisports.com/php/api.php?action=matchListFilter&type=leagueFilter
       String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListFilter");
        params.put("type","leagueFilter");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                publishGroomMatchBean = JsonParseUtils.parseJsonClass(string,PublishGroomMatchBean.class);
                myMatchAdapter.notifyDataSetChanged();
                if(publishGroomMatchBean != null && publishGroomMatchBean.data != null && publishGroomMatchBean.data.leagueFilter != null){
                    if(publishGroomMatchBean.data.leagueFilter.size()>0) {
                        String s = publishGroomMatchBean.data.leagueFilter.get(0).get(0);
                        requestTeamDatas(s);
                    }
                }else{
                    rl_load_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                rl_load_layout.setVisibility(View.GONE);
            }
        });
    }

    class MyMatchItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(matchClickPosition != position) {
                matchClickPosition = position;
                myMatchAdapter.notifyDataSetChanged();
                String matchId = publishGroomMatchBean.data.leagueFilter.get(position).get(0);
                requestTeamDatas(matchId);

            }
        }
    }

    private HashMap<String,PublishGroomTeamBean> teamMap = new HashMap<>();
    /**
     * 填充联赛内比赛
     * @param matchId
     */
    private PublishGroomTeamBean publishGroomTeamBean;
    private void requestTeamDatas(final String matchId) {
        PublishGroomTeamBean bean = teamMap.get(matchId);
        if(bean != null){
            this.publishGroomTeamBean = bean;
             myTeamAdapter.notifyDataSetChanged();
        }else {
            //?action=matchListByDate&date=180124&start=0&count=100&type=5&leagueId=17
            rl_load_layout.setVisibility(View.VISIBLE);
            String url = Constant.BASE_URL + "php/api.php";
            HttpParams params = new HttpParams();
            params.put("action", "matchListByDate");
            String format = Utils.simpleDateFormatYY.format(new Date(System.currentTimeMillis()));
            format = format.substring(2,format.length());
            params.put("date", format);
            params.put("start", 0);
            params.put("count", 100);
            params.put("type", 5);
            params.put("leagueId", matchId);
            RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
                @Override
                public void onSucces(String string) {
                    try {
                        PublishGroomActivity.this.publishGroomTeamBean = JsonParseUtils.parseJsonClass(string, PublishGroomTeamBean.class);
                        if(PublishGroomActivity.this.publishGroomMatchBean != null) {
                            teamMap.put(matchId, PublishGroomActivity.this.publishGroomTeamBean);
                            myTeamAdapter.notifyDataSetChanged();
                        }
                    }catch (Exception e){

                        if(PublishGroomActivity.this.publishGroomTeamBean == null){
                            PublishGroomActivity.this.publishGroomTeamBean = new PublishGroomTeamBean();
                        }
                        PublishGroomActivity.this.publishGroomTeamBean.data = null;
                        teamMap.put(matchId, PublishGroomActivity.this.publishGroomTeamBean);

                        myTeamAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    rl_load_layout.setVisibility(View.GONE);
                }
            });
        }
    }


    private PublishGroomTeamBean.DataBean.MatchListBean matchListBean;
    class MyTeamItemClick implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             matchListBean = publishGroomTeamBean.data.matchList.get(position);
            if(matchListBean.recommendation_status == 1){//已发布
                PublishSuccessGroomActivity.start(PublishGroomActivity.this,matchListBean.match.betId);
            }else {
                String betId = matchListBean.match.betId;
                EditGroomActivity.start(PublishGroomActivity.this, betId);
            }
        }
    }


    private int matchClickPosition;
    class MyMatchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(publishGroomMatchBean != null && publishGroomMatchBean.data != null && publishGroomMatchBean.data.leagueFilter != null){
                return publishGroomMatchBean.data.leagueFilter.size();
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
            MyMatchHolder myMatchHolder;
            if(convertView == null){
                myMatchHolder = new MyMatchHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.groom_match_item,null);
                myMatchHolder.iv_match_groom_item = convertView.findViewById(R.id.iv_match_groom_item);
                myMatchHolder.tv_match_name_groom_item = convertView.findViewById(R.id.tv_match_name_groom_item);
                myMatchHolder.tv_count_groom_item = convertView.findViewById(R.id.tv_count_groom_item);
                convertView.setTag(myMatchHolder);
            }else{
                myMatchHolder = (MyMatchHolder) convertView.getTag();
            }

            List<String> strings = publishGroomMatchBean.data.leagueFilter.get(position);
            myMatchHolder.tv_match_name_groom_item.setText(strings.get(1));
            myMatchHolder.tv_count_groom_item.setText(strings.get(3)+"");


            String hostUrl = "http://dldemo-img.stor.sinaapp.com/league_logo_"+ strings.get(0) + ".png";
            PictureUtils.show(hostUrl,myMatchHolder.iv_match_groom_item);

            if(position == matchClickPosition){
                convertView.setBackgroundColor(getResources().getColor(R.color.white));
            }else{
                convertView.setBackgroundColor(getResources().getColor(R.color.coment_gray_bg));
            }


            return convertView;
        }
    }

    static class MyMatchHolder{
        public ImageView iv_match_groom_item;
        public TextView tv_match_name_groom_item;
        public TextView tv_count_groom_item;
    }

    class MyTeamAdapter extends  BaseAdapter{

        @Override
        public int getCount() {
            if(publishGroomTeamBean != null && publishGroomTeamBean.data != null && publishGroomTeamBean.data.matchList != null){
                ll_no_data.setVisibility(View.GONE);
                return publishGroomTeamBean.data.matchList.size();
            }
            if(publishGroomTeamBean != null){
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
        public View getView(int position, View convertView, ViewGroup parent) {
            MyTeamHolder myTeamHolder;
            if(convertView == null){
                myTeamHolder = new MyTeamHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.groom_team_layout,null);

                myTeamHolder.tv_time_team_item = convertView.findViewById(R.id.tv_time_team_item);
                myTeamHolder.tv_tape_team_item = convertView.findViewById(R.id.tv_tape_team_item);
                myTeamHolder.tv_host_team_name = convertView.findViewById(R.id.tv_host_team_name);
                myTeamHolder.tv_away_team_name = convertView.findViewById(R.id.tv_away_team_name);
                myTeamHolder.tv_rank_host_team_name = convertView.findViewById(R.id.tv_rank_host_team_name);
                myTeamHolder.tv_rank_away_team_name = convertView.findViewById(R.id.tv_rank_away_team_name);
                myTeamHolder.iv_published = convertView.findViewById(R.id.iv_published);

                convertView.setTag(myTeamHolder);
            }else{
                myTeamHolder = (MyTeamHolder) convertView.getTag();
            }

            PublishGroomTeamBean.DataBean.MatchListBean bean = publishGroomTeamBean.data.matchList.get(position);

            Long aLong = Long.valueOf(bean.match.timezoneoffset + "000");
            String matchStartTime = Utils.getMatchStartTime(aLong);
            myTeamHolder.tv_time_team_item.setText(matchStartTime);
            String tape = Utils.parseOddOfHandicap(bean.match.tape);
            myTeamHolder.tv_tape_team_item.setText(tape);

            String host = URLDecoder.decode(bean.match.hostTeamName);
            myTeamHolder.tv_host_team_name.setText(host);
            if(!TextUtils.isEmpty(bean.match.hostRank) && !"0".equals(bean.match.hostRank)){
                myTeamHolder.tv_rank_host_team_name.setText("["+bean.match.hostRank+"]");
            }else{
                myTeamHolder.tv_rank_host_team_name.setText("");
            }

            String away = URLDecoder.decode(bean.match.awayTeamName);
            myTeamHolder.tv_away_team_name.setText(away);
            if(!TextUtils.isEmpty(bean.match.awayRank)&& !"0".equals(bean.match.awayRank)){
                myTeamHolder.tv_rank_away_team_name.setText("["+bean.match.awayRank+"]");
            }else{
                myTeamHolder.tv_rank_away_team_name.setText("");
            }


            if(bean.recommendation_status == 1){//已发布
                myTeamHolder.iv_published.setVisibility(View.VISIBLE);
            }else{
                myTeamHolder.iv_published.setVisibility(View.GONE);
            }


            return convertView;
        }
    }

    static class MyTeamHolder{
        public TextView tv_time_team_item;
        public TextView tv_tape_team_item;
        public TextView tv_host_team_name;
        public TextView tv_rank_host_team_name;
        public TextView tv_away_team_name;
        public TextView tv_rank_away_team_name;
        public ImageView iv_published;

    }

}
