package com.mayisports.qca.adapter;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.MatchDetailActivity;
import com.mayisports.qca.bean.AnlyBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.tencent.open.utils.Util;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

/**
 *
 * 比赛详情，分析模块数据适配器
 * Created by zhangpengju on 2018/6/12.
 */

public class MatchAnlyIitemSetData {

    private View convertView;
    private AnlyBean anlyBean;

    public MatchAnlyIitemSetData(View convertView,AnlyBean anlyBean){
        this.convertView = convertView;
        this.anlyBean = anlyBean;

        initView();
        setData();

    }



    private LinearLayout ll_group_rank;
    private LinearLayout ll_group_rank_container;

    private LinearLayout ll_match_rank;
    private LinearLayout ll_match_rank_container;

    private LinearLayout ll_history_vs;
    private LinearLayout ll_history_vs_container;

    private LinearLayout ll_near_vs;
    private LinearLayout ll_near_vs_container;

    private LinearLayout ll_futuer_three;
    private LinearLayout ll_futuer_three_container;

    private void initView() {
        convertView = View.inflate(QCaApplication.getContext(), R.layout.anly_item,null);

        ll_group_rank = convertView.findViewById(R.id.ll_group_rank);
        ll_group_rank_container = convertView.findViewById(R.id.ll_group_rank_container);

        ll_match_rank = convertView.findViewById(R.id.ll_match_rank);
        ll_match_rank_container = convertView.findViewById(R.id.ll_match_rank_container);

        ll_history_vs = convertView.findViewById(R.id.ll_history_vs);
        ll_history_vs_container = convertView.findViewById(R.id.ll_history_vs_container);

        ll_near_vs = convertView.findViewById(R.id.ll_near_vs);
        ll_near_vs_container = convertView.findViewById(R.id.ll_near_vs_container);

        ll_futuer_three = convertView.findViewById(R.id.ll_futuer_three);
        ll_futuer_three_container = convertView.findViewById(R.id.lll_futuer_three_container);




    }


    /**
     * 填充数据
     */
    private void setData() {

        /**
         * 杯赛排行数据
         */
        if(anlyBean.data.group_ranking != null && anlyBean.data.group_ranking.size()>0){
            ll_group_rank.setVisibility(View.VISIBLE);
            List<AnlyBean.DataBean.GroupRankingBean> group_ranking = anlyBean.data.group_ranking;

            for(int i = 0;i<group_ranking.size();i++){
                View item = View.inflate(QCaApplication.getContext(),R.layout.anly_child_item,null);

                AnlyBean.DataBean.GroupRankingBean groupRankingBean = group_ranking.get(i);

                TextView tv0 = item.findViewById(R.id.tv0);
                tv0.setText((i+1)+"");

                TextView tv1 = item.findViewById(R.id.tv1);
                tv1.setText(groupRankingBean.name);

//                TextView tv2 = item.findViewById(R.id.tv2);
//                tv2.setText();
                TextView tv3 = item.findViewById(R.id.tv3);
                int count = groupRankingBean._$0 + groupRankingBean._$1 + groupRankingBean._$3;
                tv3.setText(count+"");

                TextView tv4 = item.findViewById(R.id.tv4);
                tv4.setText(groupRankingBean._$3+"");

                TextView tv5 = item.findViewById(R.id.tv5);
                tv5.setText(groupRankingBean._$1+"");

                TextView tv6 = item.findViewById(R.id.tv6);
                tv6.setText(groupRankingBean._$0+"");

                TextView tv7 = item.findViewById(R.id.tv7);
                tv7.setText(groupRankingBean.goal+"/"+groupRankingBean.fumble);

                TextView tv8 = item.findViewById(R.id.tv8);
                tv8.setText((groupRankingBean.goal-groupRankingBean.fumble)+"");

                TextView tv9 = item.findViewById(R.id.tv9);
                tv9.setText(groupRankingBean.score+"");


                String hostTeamId = anlyBean.data.match.hostTeamId;
                String awayTeamId = anlyBean.data.match.awayTeamId;
                String id = groupRankingBean.id;
                if(id.equals(hostTeamId) || id.equals(awayTeamId)){
                    item.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.yellow_small));
                }


                ll_group_rank_container.addView(item);

            }




        }else{
            ll_group_rank.setVisibility(View.GONE);
        }


        /**
         * 联赛数据排名
         */

        setMatchRank();


        /**
         * 历史交锋
         */

        setHistoryVS();



        /**
         *近期战绩
         */
         setNearVS();


        /**
         * 未来三场
         */

        if(anlyBean.data.future3 != null && (anlyBean.data.future3.home != null || anlyBean.data.future3.guest != null)){
            ll_futuer_three.setVisibility(View.VISIBLE);

            AnlyBean.DataBean.Future3Bean future3 = anlyBean.data.future3;


            String hostName = URLDecoder.decode(anlyBean.data.match.hostTeamName);
            String awayName = URLDecoder.decode(anlyBean.data.match.awayTeamName);


            if(future3.home != null){

                View item = View.inflate(QCaApplication.getContext(),R.layout.future_item,null);

                TextView tv_name = item.findViewById(R.id.tv_name);
                tv_name.setText( URLDecoder.decode(anlyBean.data.match.hostTeamName));

                LinearLayout ll_future_home_container = item.findViewById(R.id.ll_future_home_container);
                for(int i = 0;i<future3.home.size();i++){

                    AnlyBean.DataBean.Future3Bean.HomeBean homeBean = future3.home.get(i);

                    View childItem = View.inflate(QCaApplication.getContext(),R.layout.future_child_item,null);

                    TextView tv0 = childItem.findViewById(R.id.tv0);
                    String time = Utils.simpleDateSplit.format(new Date(Long.valueOf(homeBean.match_time + "000")));
                    tv0.setText(time);


                    TextView tv1 = childItem.findViewById(R.id.tv1);
                    tv1.setText(homeBean.leagueName);


                    TextView tv2 = childItem.findViewById(R.id.tv2);
                    tv2.setText(homeBean.hostTeamName);
                    if(homeBean.hostTeamName.equals(hostName)){
                        tv2.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                    }


                    TextView tv3 = childItem.findViewById(R.id.tv3);
                    tv3.setText(homeBean.awayTeamName);
                    if(homeBean.awayTeamName.equals(hostName)){
                        tv3.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                    }

                    TextView tv4 = childItem.findViewById(R.id.tv4);


                    try {
                        String s = anlyBean.data.match.timezoneoffset + "000";
                        Long aLong = Long.valueOf(s);
                        int dayString = Utils.getDayString(new Date(aLong),new Date(Long.valueOf(homeBean.match_time + "000")));
                        tv4.setText(dayString + "");
                    }catch (Exception e){
                        tv4.setText("");
                    }



                    ll_future_home_container.addView(childItem);


                }


               ll_futuer_three_container.addView(item);
            }




            if(future3.guest != null){
                View item = View.inflate(QCaApplication.getContext(),R.layout.future_item,null);

                TextView tv_name = item.findViewById(R.id.tv_name);
                tv_name.setText( URLDecoder.decode(anlyBean.data.match.awayTeamName));

                LinearLayout ll_future_home_container = item.findViewById(R.id.ll_future_home_container);
                for(int i = 0;i<future3.guest.size();i++){

                    AnlyBean.DataBean.Future3Bean.GuestBean guestBean = future3.guest.get(i);

                    View childItem = View.inflate(QCaApplication.getContext(),R.layout.future_child_item,null);

                    TextView tv0 = childItem.findViewById(R.id.tv0);
                    String time = Utils.simpleDateSplit.format(new Date(Long.valueOf(guestBean.match_time + "000")));
                    tv0.setText(time);


                    TextView tv1 = childItem.findViewById(R.id.tv1);
                    tv1.setText(guestBean.leagueName);


                    TextView tv2 = childItem.findViewById(R.id.tv2);
                    tv2.setText(guestBean.hostTeamName);
                    if(guestBean.hostTeamName.equals(awayName)){
                        tv2.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                    }


                    TextView tv3 = childItem.findViewById(R.id.tv3);
                    tv3.setText(guestBean.awayTeamName);
                    if(guestBean.awayTeamName.equals(awayName)){
                        tv3.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                    }

                    TextView tv4 = childItem.findViewById(R.id.tv4);
                    try {
                        String s = anlyBean.data.match.timezoneoffset + "000";
                        Long aLong = Long.valueOf(s);
                        int dayString = Utils.getDayString(new Date(aLong),new Date(Long.valueOf(guestBean.match_time + "000")));
                        tv4.setText(dayString + "");
                    }catch (Exception e){
                        tv4.setText("");
                    }



                    ll_future_home_container.addView(childItem);


                }



                ll_futuer_three_container.addView(item);
            }




        }else{
            ll_futuer_three.setVisibility(View.GONE);
        }







    }


    /**
     * 设置近期战绩数据
     */
    private void setNearVS() {
        ll_near_vs.setVisibility(View.VISIBLE);
        ll_near_vs_container.setVisibility(View.VISIBLE);

        if(anlyBean.data.matchList != null && (anlyBean.data.matchList.host != null || anlyBean.data.matchList.away != null) && (anlyBean.data.matchList.host.size() != 0 || anlyBean.data.matchList.away.size() != 0)){
            ll_near_vs.setVisibility(View.VISIBLE);

            if(anlyBean.data.matchList.host != null){
                View item = View.inflate(QCaApplication.getContext(),R.layout.near_vs_item,null);

                TextView tv_name = item.findViewById(R.id.tv_name);
                tv_name.setText( URLDecoder.decode(anlyBean.data.match.hostTeamName));
                tv_name.setTag(anlyBean.data.match.hostTeamId);

                ImageView iv_logo = item.findViewById(R.id.iv_logo);
                if(anlyBean.data.match.logoH != null){
                    String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ anlyBean.data.match.logoH + ".png";
                    PictureUtils.show(hostUrl,iv_logo);
                }


                LinearLayout ll_child_item_container = item.findViewById(R.id.ll_child_item_container);
                List<AnlyBean.DataBean.MatchListBean.HostBean> host = anlyBean.data.matchList.host;


                int windCount = 0;
                int drawCount = 0;
                int loseCount = 0;


                for(int i = 0;i<host.size();i++){

                    View itemChild = View.inflate(QCaApplication.getContext(),R.layout.history_child_item,null);


                    AnlyBean.DataBean.MatchListBean.HostBean hostBean = host.get(i);
                    TextView tv0 = itemChild.findViewById(R.id.tv0);
                    tv0.setText(hostBean.leagueName);

                    TextView tv1 = itemChild.findViewById(R.id.tv1);
                    String time = Utils.simpleDateSplit.format(new Date(Long.valueOf(hostBean.match_time + "000")));
                    tv1.setText(time);


                    TextView tv2 = itemChild.findViewById(R.id.tv2);
                    tv2.setText(URLDecoder.decode(hostBean.hostTeamName));
                    tv2.setTag(hostBean.hostTeamId);

                    TextView tv3 = itemChild.findViewById(R.id.tv3);
                    String score = hostBean.hostScore+":"+hostBean.awayScore;
                    tv3.setText(score);



                    TextView tv4 = itemChild.findViewById(R.id.tv4);
                    String halfScore = hostBean.hostHalfScore+":"+hostBean.awayHalfScore;
                    tv4.setText(halfScore);

                    TextView tv5 = itemChild.findViewById(R.id.tv5);
                    tv5.setText(URLDecoder.decode(hostBean.awayTeamName));



                    TextView tvColor = null;
                    boolean isHost = true;
                    String titleTeamId = (String) tv_name.getTag();
                    String currentHostId = (String) tv2.getTag();
                    if(titleTeamId.equals(currentHostId)){
                        tvColor = tv2;
                        isHost = true;
                    }else{
                        tvColor = tv5;
                        isHost = false;
                    }


                    if(hostBean.hostScore == hostBean.awayScore){
                        drawCount ++;
                        tvColor.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue));
                    }else if(! (hostBean.hostScore>hostBean.awayScore ^ isHost)){
                        windCount ++;
                        tvColor.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                    }else {
                        loseCount ++;
                        tvColor.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));
                    }



                    ll_child_item_container.addView(itemChild);


                }

                View sumItem = View.inflate(QCaApplication.getContext(),R.layout.sum_vs_item,null);
                TextView tv_content = sumItem.findViewById(R.id.tv_content);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                spannableStringBuilder.append("共"+(windCount+drawCount+loseCount)+"场，");

                SpannableString winStr = new SpannableString("胜"+windCount);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.red_));
                winStr.setSpan(foregroundColorSpan,1,winStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.append(winStr);


                SpannableString drawStr = new SpannableString("平"+drawCount);
                ForegroundColorSpan foregroundColorSpanD = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.blue));
                drawStr.setSpan(foregroundColorSpanD,1,drawStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.append(drawStr);


                SpannableString loseStr = new SpannableString("负"+loseCount);
                ForegroundColorSpan foregroundColorSpanL = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.green));
                loseStr.setSpan(foregroundColorSpanL,1,loseStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.append(loseStr);

                tv_content.setText(spannableStringBuilder);

                ll_child_item_container.addView(sumItem);


                ll_near_vs_container.addView(item);

            }


            if(anlyBean.data.matchList.away != null){



                View item = View.inflate(QCaApplication.getContext(),R.layout.near_vs_item,null);

                TextView tv_name = item.findViewById(R.id.tv_name);
                tv_name.setText( URLDecoder.decode(anlyBean.data.match.awayTeamName));
                tv_name.setTag(anlyBean.data.match.awayTeamId);

                ImageView iv_logo = item.findViewById(R.id.iv_logo);
                if(anlyBean.data.match.logoG != null){
                    String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ anlyBean.data.match.logoG + ".png";
                    PictureUtils.show(hostUrl,iv_logo);
                }



                int windCount = 0;
                int drawCount = 0;
                int loseCount = 0;


                LinearLayout ll_child_item_container = item.findViewById(R.id.ll_child_item_container);
                List<AnlyBean.DataBean.MatchListBean.AwayBean> away = anlyBean.data.matchList.away;
                for(int i = 0;i<away.size();i++){

                    View itemChild = View.inflate(QCaApplication.getContext(),R.layout.history_child_item,null);


                    AnlyBean.DataBean.MatchListBean.AwayBean awayBean = away.get(i);
                    TextView tv0 = itemChild.findViewById(R.id.tv0);
                    tv0.setText(awayBean.leagueName);

                    TextView tv1 = itemChild.findViewById(R.id.tv1);
                    String time = Utils.simpleDateSplit.format(new Date(Long.valueOf(awayBean.match_time + "000")));
                    tv1.setText(time);


                    TextView tv2 = itemChild.findViewById(R.id.tv2);
                    tv2.setText(URLDecoder.decode(awayBean.hostTeamName));
                    tv2.setTag(awayBean.hostTeamId);

                    TextView tv3 = itemChild.findViewById(R.id.tv3);
                    String score = awayBean.hostScore+":"+awayBean.awayScore;
                    tv3.setText(score);



                    TextView tv4 = itemChild.findViewById(R.id.tv4);
                    String halfScore = awayBean.hostHalfScore+":"+awayBean.awayHalfScore;
                    tv4.setText(halfScore);

                    TextView tv5 = itemChild.findViewById(R.id.tv5);
                    tv5.setText(URLDecoder.decode(awayBean.awayTeamName));



                    TextView tvColor = null;
                    boolean isHost = true;
                    String titleTeamId = (String) tv_name.getTag();
                    String currentHostId = (String) tv2.getTag();
                    if(titleTeamId.equals(currentHostId)){
                        tvColor = tv2;
                        isHost = true;
                    }else{
                        tvColor = tv5;
                        isHost = false;
                    }


                    if(awayBean.hostScore == awayBean.awayScore){
                        tvColor.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue));
                        drawCount++;
                    }else if( !(awayBean.hostScore>awayBean.awayScore ^ isHost)){
                        tvColor.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                        windCount++;
                    }else {
                        tvColor.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));
                        loseCount++;
                    }



                    ll_child_item_container.addView(itemChild);


                }




                View sumItem = View.inflate(QCaApplication.getContext(),R.layout.sum_vs_item,null);
                TextView tv_content = sumItem.findViewById(R.id.tv_content);

                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                spannableStringBuilder.append("共"+(windCount+drawCount+loseCount)+"场，");

                SpannableString winStr = new SpannableString("胜"+windCount);
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.red_));
                winStr.setSpan(foregroundColorSpan,1,winStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.append(winStr);


                SpannableString drawStr = new SpannableString("平"+drawCount);
                ForegroundColorSpan foregroundColorSpanD = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.blue));
                drawStr.setSpan(foregroundColorSpanD,1,drawStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.append(drawStr);


                SpannableString loseStr = new SpannableString("负"+loseCount);
                ForegroundColorSpan foregroundColorSpanL = new ForegroundColorSpan(QCaApplication.getContext().getResources().getColor(R.color.green));
                loseStr.setSpan(foregroundColorSpanL,1,loseStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                spannableStringBuilder.append(loseStr);

                tv_content.setText(spannableStringBuilder);

                ll_child_item_container.addView(sumItem);




                ll_near_vs_container.addView(item);





            }




        }else{
            ll_near_vs.setVisibility(View.GONE);
        }



    }


    /**
     * 设置历史交锋
     */
    private void setHistoryVS() {

        ll_history_vs.setVisibility(View.VISIBLE);
        ll_history_vs_container.setVisibility(View.VISIBLE);

        if(anlyBean.data.matchList != null && anlyBean.data.matchList.history != null && anlyBean.data.matchList.history.size()>0){
            ll_history_vs.setVisibility(View.VISIBLE);
            List<AnlyBean.DataBean.MatchListBean.HistoryBean> history = anlyBean.data.matchList.history;

            View item = View.inflate(QCaApplication.getContext(),R.layout.history_vs_item,null);


            String hostName = URLDecoder.decode(anlyBean.data.match.hostTeamName);
            String awayName = URLDecoder.decode(anlyBean.data.match.awayTeamName);



            TextView tv_title = item.findViewById(R.id.tv_title);
            String title = "近期"+(history.size())+"场交锋";
            tv_title.setText(title);

            TextView tv_host_name = item.findViewById(R.id.tv_host_name);
            tv_host_name.setText(hostName);



            TextView tv_away_name = item.findViewById(R.id.tv_away_name);
            tv_away_name.setText(awayName);


            SeekBar sb_vs = item.findViewById(R.id.sb_vs_ping_item);
            sb_vs.setEnabled(false);




            int hostWinCount = 0;
            int drawCount = 0;
            int awayWinCount = 0;

            LinearLayout ll_child_item_container = item.findViewById(R.id.ll_child_item_container);

            for(int i= 0;i<history.size();i++){
                AnlyBean.DataBean.MatchListBean.HistoryBean historyBean = history.get(i);

                View itemChild = View.inflate(QCaApplication.getContext(),R.layout.history_child_item,null);


                TextView tv0 = itemChild.findViewById(R.id.tv0);
                tv0.setText(historyBean.leagueName);

                TextView tv1 = itemChild.findViewById(R.id.tv1);
                String time = Utils.simpleDateSplit.format(new Date(Long.valueOf(historyBean.match_time + "000")));
                tv1.setText(time);


                TextView tv2 = itemChild.findViewById(R.id.tv2);
                tv2.setText(hostName);

                TextView tv3 = itemChild.findViewById(R.id.tv3);
                String score = historyBean.hostScore+":"+historyBean.awayScore;
                tv3.setText(score);

                if(historyBean.hostScore>historyBean.awayScore){
                    hostWinCount ++;
                }else if(historyBean.hostScore == historyBean.awayScore){
                    drawCount ++;
                }else if(historyBean.hostScore < historyBean.awayScore){
                    awayWinCount ++;
                }



                TextView tv4 = itemChild.findViewById(R.id.tv4);
                String halfScore = historyBean.hostHalfScore+":"+historyBean.awayHalfScore;
                tv4.setText(halfScore);

                TextView tv5 = itemChild.findViewById(R.id.tv5);
                tv5.setText(awayName);


                ll_child_item_container.addView(itemChild);
            }



            TextView tv_host_win_count = item.findViewById(R.id.tv_host_win_count);
            tv_host_win_count.setText(hostWinCount+"胜");

            TextView tv_draw_count = item.findViewById(R.id.tv_draw_count);
            tv_draw_count.setText(drawCount+"平");

            TextView tv_away_win_count = item.findViewById(R.id.tv_away_win_count);
            tv_away_win_count.setText(awayWinCount+"胜");




            int sum = hostWinCount+awayWinCount+drawCount;

            int hostIndex = (int) (hostWinCount/1.0/sum*100);
            if(hostIndex<2){
                sb_vs.setSecondaryProgress(2);
            }else

            if(hostIndex>98){
               sb_vs.setSecondaryProgress(98);
            }

            else {
                sb_vs.setSecondaryProgress(hostIndex);
            }

            int awayIndex = (int) (awayWinCount/1.0/sum*100);
            if(awayIndex<2){
                sb_vs.setProgress(98);
            }else if(awayIndex>98){
                sb_vs.setProgress(2);
            } else {
                sb_vs.setProgress(100 - awayIndex);
            }






            ll_history_vs_container.addView(item);
        }else{
            ll_history_vs.setVisibility(View.GONE);
        }

    }


    public View getConvertView() {
        return convertView;
    }


    /**
     * 设置联赛
     */
    private void setMatchRank(){
        if(anlyBean.data.ranking != null &&(anlyBean.data.ranking.home != null || anlyBean.data.ranking.guest != null)){
            ll_match_rank.setVisibility(View.VISIBLE);

            AnlyBean.DataBean.RankingBean ranking = anlyBean.data.ranking;

            if(ranking.home != null && ranking.home.h != null && ranking.home.g != null){
                View item = View.inflate(QCaApplication.getContext(),R.layout.match_rank_item,null);

                ImageView iv_logo = item.findViewById(R.id.iv_logo);
                TextView tv_name = item.findViewById(R.id.tv_name);
                if(anlyBean.data.match.logoH != null){
                    String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ anlyBean.data.match.logoH + ".png";
                    PictureUtils.show(hostUrl,iv_logo);
                }

                tv_name.setText( URLDecoder.decode(anlyBean.data.match.hostTeamName));

                LinearLayout ll_host = item.findViewById(R.id.ll_host);
                ll_host.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.yellow_small));


                AnlyBean.DataBean.RankingBean.HomeBeanX home = ranking.home;

                AnlyBean.DataBean.RankingBean.HomeBeanX.HBean h = home.h;
                AnlyBean.DataBean.RankingBean.HomeBeanX.GBean g = home.g;


                if(h == null || g == null)return;

                /**
                 * 总队
                 */


                TextView tv1_0 = item.findViewById(R.id.tv1_0);
                int count1 = h._$0 + h._$1 + h._$3 + g._$0 + g._$1 + g._$3;
                tv1_0.setText(count1+"");

                TextView tv3_0 = item.findViewById(R.id.tv3_0);
                tv3_0.setText(h._$3+g._$3+"");

                TextView tv4_0 = item.findViewById(R.id.tv4_0);
                tv4_0.setText(h._$1+g._$1+"");

                TextView tv5_0 = item.findViewById(R.id.tv5_0);
                tv5_0.setText(h._$0+g._$0+"");

                TextView tv6_0 = item.findViewById(R.id.tv6_0);
                tv6_0.setText(h.goal+g.goal+"/"+(h.fumble+g.fumble));

                TextView tv7_0 = item.findViewById(R.id.tv7_0);
                tv7_0.setText(h.goal+g.goal-h.fumble-g.fumble +"");

                TextView tv8_0 = item.findViewById(R.id.tv8_0);
                tv8_0.setText(h.score+g.score+"");


                TextView tv9_0 = item.findViewById(R.id.tv9_0);
                tv9_0.setText(home.rank+"");

                String percent = Utils.parsePercent(h._$3+g._$3, count1, 1);
                TextView tv10_0 = item.findViewById(R.id.tv10_0);
                tv10_0.setText(percent);




                /**
                 * 主队
                 */


                TextView tv1_1 = item.findViewById(R.id.tv1_1);
                int count = h._$0 + h._$1 + h._$3;
                tv1_1.setText(count+"");

                TextView tv3_1 = item.findViewById(R.id.tv3_1);
                tv3_1.setText(h._$3+"");

                TextView tv4_1 = item.findViewById(R.id.tv4_1);
                tv4_1.setText(h._$1+"");

                TextView tv5_1 = item.findViewById(R.id.tv5_1);
                tv5_1.setText(h._$0+"");

                TextView tv6_1 = item.findViewById(R.id.tv6_1);
                tv6_1.setText(h.goal+"/"+h.fumble);

                TextView tv7_1 = item.findViewById(R.id.tv7_1);
                tv7_1.setText(h.goal-h.fumble +"");

                TextView tv8_1 = item.findViewById(R.id.tv8_1);
                tv8_1.setText(h.score+"");


                TextView tv9_1 = item.findViewById(R.id.tv9_1);
                tv9_1.setText(h.rank+"");

                String percent1 = Utils.parsePercent(h._$3, count, 1);
                TextView tv10_1 = item.findViewById(R.id.tv10_1);
                tv10_1.setText(percent1);

                /**
                 * 客队
                 */
                TextView tv1_2 = item.findViewById(R.id.tv1_2);
                int count2 = g._$0 + g._$1 + g._$3;
                tv1_2.setText(count2+"");

                TextView tv3_2 = item.findViewById(R.id.tv3_2);
                tv3_2.setText(g._$3+"");

                TextView tv4_2 = item.findViewById(R.id.tv4_2);
                tv4_2.setText(g._$1+"");

                TextView tv5_2 = item.findViewById(R.id.tv5_2);
                tv5_2.setText(g._$0+"");

                TextView tv6_2 = item.findViewById(R.id.tv6_2);
                tv6_2.setText(g.goal+"/"+g.fumble);

                TextView tv7_2 = item.findViewById(R.id.tv7_2);
                tv7_2.setText(g.goal-g.fumble +"");

                TextView tv8_2 = item.findViewById(R.id.tv8_2);
                tv8_2.setText(g.score+"");


                TextView tv9_2 = item.findViewById(R.id.tv9_2);
                tv9_2.setText(g.rank+"");

                String percent2 = Utils.parsePercent(g._$3, count2, 1);
                TextView tv10_2 = item.findViewById(R.id.tv10_2);
                tv10_2.setText(percent2);


                ll_match_rank_container.addView(item);
            }


            if(ranking.guest != null && ranking.guest.h != null && ranking.guest.g != null){

                View item = View.inflate(QCaApplication.getContext(),R.layout.match_rank_item,null);

                ImageView iv_logo = item.findViewById(R.id.iv_logo);
                TextView tv_name = item.findViewById(R.id.tv_name);
                if(anlyBean.data.match.logoG != null){
                    String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ anlyBean.data.match.logoG + ".png";
                    PictureUtils.show(hostUrl,iv_logo);
                }
                tv_name.setText( URLDecoder.decode(anlyBean.data.match.awayTeamName));

                LinearLayout ll_away = item.findViewById(R.id.ll_away);
                ll_away.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.yellow_small));


                AnlyBean.DataBean.RankingBean.GuestBeanX guest = ranking.guest;

                AnlyBean.DataBean.RankingBean.GuestBeanX.HBeanX h = guest.h;
                AnlyBean.DataBean.RankingBean.GuestBeanX.GBeanX g = guest.g;


                /**
                 * 总队
                 */

                TextView tv1_0 = item.findViewById(R.id.tv1_0);
                int count1 = h._$0 + h._$1 + h._$3 + g._$0 + g._$1 + g._$3;
                tv1_0.setText(count1+"");

                TextView tv3_0 = item.findViewById(R.id.tv3_0);
                tv3_0.setText(h._$3+g._$3+"");

                TextView tv4_0 = item.findViewById(R.id.tv4_0);
                tv4_0.setText(h._$1+g._$1+"");

                TextView tv5_0 = item.findViewById(R.id.tv5_0);
                tv5_0.setText(h._$0+g._$0+"");

                TextView tv6_0 = item.findViewById(R.id.tv6_0);
                tv6_0.setText(h.goal+g.goal+"/"+(h.fumble+g.fumble));

                TextView tv7_0 = item.findViewById(R.id.tv7_0);
                tv7_0.setText(h.goal+g.goal-h.fumble-g.fumble +"");

                TextView tv8_0 = item.findViewById(R.id.tv8_0);
                tv8_0.setText(h.score+g.score+"");


                TextView tv9_0 = item.findViewById(R.id.tv9_0);
                tv9_0.setText(guest.rank+"");

                String percent = Utils.parsePercent(h._$3+g._$3, count1, 1);
                TextView tv10_0 = item.findViewById(R.id.tv10_0);
                tv10_0.setText(percent);




                /**
                 * 主队
                 */


                TextView tv1_1 = item.findViewById(R.id.tv1_1);
                int count = h._$0 + h._$1 + h._$3;
                tv1_1.setText(count+"");

                TextView tv3_1 = item.findViewById(R.id.tv3_1);
                tv3_1.setText(h._$3+"");

                TextView tv4_1 = item.findViewById(R.id.tv4_1);
                tv4_1.setText(h._$1+"");

                TextView tv5_1 = item.findViewById(R.id.tv5_1);
                tv5_1.setText(h._$0+"");

                TextView tv6_1 = item.findViewById(R.id.tv6_1);
                tv6_1.setText(h.goal+"/"+h.fumble);

                TextView tv7_1 = item.findViewById(R.id.tv7_1);
                tv7_1.setText(h.goal-h.fumble +"");

                TextView tv8_1 = item.findViewById(R.id.tv8_1);
                tv8_1.setText(h.score+"");


                TextView tv9_1 = item.findViewById(R.id.tv9_1);
                tv9_1.setText(h.rank+"");

                String percent1 = Utils.parsePercent(h._$3, count, 1);
                TextView tv10_1 = item.findViewById(R.id.tv10_1);
                tv10_1.setText(percent1);

                /**
                 * 客队
                 */
                TextView tv1_2 = item.findViewById(R.id.tv1_2);
                int count2 = g._$0 + g._$1 + g._$3;
                tv1_2.setText(count2+"");

                TextView tv3_2 = item.findViewById(R.id.tv3_2);
                tv3_2.setText(g._$3+"");

                TextView tv4_2 = item.findViewById(R.id.tv4_2);
                tv4_2.setText(g._$1+"");

                TextView tv5_2 = item.findViewById(R.id.tv5_2);
                tv5_2.setText(g._$0+"");

                TextView tv6_2 = item.findViewById(R.id.tv6_2);
                tv6_2.setText(g.goal+"/"+g.fumble);

                TextView tv7_2 = item.findViewById(R.id.tv7_2);
                tv7_2.setText(g.goal-g.fumble +"");

                TextView tv8_2 = item.findViewById(R.id.tv8_2);
                tv8_2.setText(g.score+"");


                TextView tv9_2 = item.findViewById(R.id.tv9_2);
                tv9_2.setText(g.rank+"");

                String percent2 = Utils.parsePercent(g._$3, count2, 1);
                TextView tv10_2 = item.findViewById(R.id.tv10_2);
                tv10_2.setText(percent2);




                ll_match_rank_container.addView(item);
            }


        }else{
            ll_match_rank.setVisibility(View.GONE);
        }
    }
}
