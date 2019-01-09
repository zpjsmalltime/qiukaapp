package com.mayisports.qca.utils;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.ImmediateScoreBean;
import com.mayisports.qca.fragment.MatchFragment;
import com.mayisports.qca.fragment.ScoreFragment;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;

/**
 * 绑定比赛item 工具类
 * Created by Zpj on 2017/12/10.
 */

public class BindViewUtils {

    public static  SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");



    public static  AlphaAnimation alphaAnimation = (AlphaAnimation) AnimationUtils.loadAnimation(QCaApplication.getContext(), R.anim.alpha_ani);
    public static void updateImmediate(ScoreFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int position) {

        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match = immediateScoreBean.data.matchlist.get(position).match;


        lvHolder.iv_rigth.setVisibility(View.VISIBLE);
        lvHolder.iv_rigth1.setVisibility(View.GONE);
        lvHolder.iv_rigth2.setVisibility(View.GONE);
        if(match.tvlink == 1){
            lvHolder.iv_rigth1.setVisibility(View.VISIBLE);
            lvHolder.iv_rigth.setVisibility(View.GONE);
        }
        String match_information_count = match.match_information_count;
        if(!TextUtils.isEmpty(match.match_information_count)&&Integer.valueOf(match.match_information_count)>0){
            lvHolder.iv_rigth2.setVisibility(View.VISIBLE);
            lvHolder.iv_rigth.setVisibility(View.GONE);
        }


        switch (match.status){
            case "NO_START":
                updateProcess(lvHolder,immediateScoreBean,position);
                if("-1".equals(immediateScoreBean.data.tag)){
                    setRightTopTuiJian(match,lvHolder,"-1");
                }
                return;
            case "FIRST_HALF"://上半场
            case "SECOND_HALF"://下半场

                lvHolder.tv_center_top.setVisibility(View.VISIBLE);
                lvHolder.tv_center_top.setTextColor(Color.parseColor("#3EA53B"));
                lvHolder.tv_time_pos.setVisibility(View.VISIBLE);
                if(match.starttime != 0){
                    String minStart = Utils.getMinStart(Long.valueOf(match.starttime + "000"));
                    if("SECOND_HALF".equals(match.status)) {
                        minStart = Integer.valueOf(minStart)+45 + "";
                    }
                    lvHolder.tv_center_top.setText(minStart);
                }else {
                    String minStart = Utils.getMinStart(Long.valueOf(match.timezoneoffset + "000"));
                    lvHolder.tv_center_top.setText(minStart);
                }

                lvHolder.tv_time_pos.startAnimation(alphaAnimation);

                lvHolder.tv_center_top.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
//        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#3EA53B"));

                SpannableString spannableString = new SpannableString(match.hostScore + ":" + match.awayScore);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(styleSpan,0,spannableString.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableString);


//                lvHolder.tv_center_bottom.setText(match.hostScore + ":" + match.awayScore);
                setRightTop(match,lvHolder,"");
                break;

            case "HALF_TIME":

                lvHolder.tv_center_top.setVisibility(View.VISIBLE);
                lvHolder.tv_center_top.setTextColor(Color.parseColor("#62AFEF"));
                lvHolder.tv_center_top.setText("中场");
//        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#3EA53B"));


                SpannableString spannableString1 = new SpannableString(match.hostScore + ":" + match.awayScore);
                StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
                spannableString1.setSpan(styleSpan1,0,spannableString1.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableString1);


                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#62AFEF"));
//                lvHolder.tv_center_top.setVisibility(View.INVISIBLE);
//                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
//                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#BFBFBF"));
//                lvHolder.tv_center_bottom.setText("vs");
                setRightTop(match,lvHolder,"");
                break;

            case "COMPLETE":
                updateResult(lvHolder,immediateScoreBean,position);
                return;
        }


        if(match.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }

        if("-2".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index.substring(match.index.length()-3));
        }else if("-5".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index);
        }else{
            lvHolder.tv_left.setVisibility(View.INVISIBLE);
        }


        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString leagueName = new SpannableString(URLDecoder.decode(match.leagueName));
        if(match .color == null){
            match.color = "#777777";
        }
        ForegroundColorSpan leagueNameColorSpan = new ForegroundColorSpan(Color.parseColor(match.color+""));
        leagueName.setSpan(leagueNameColorSpan,0,leagueName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        leagueName.setSpan(boldSpan_,0,leagueName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(leagueName);
        SpannableString starttime = new SpannableString("\t\t"+simpleDateFormat.format(Long.valueOf(match.timezoneoffset+"000")));
        ForegroundColorSpan starttimeColorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        starttime.setSpan(starttimeColorSpan,0,starttime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append(starttime);
        lvHolder.tv_left_top.setText(spannableStringBuilder);
        spannableStringBuilder.clear();


//
//        if(!("0".equals(match.hostRedCard) || TextUtils.isEmpty(match.hostRedCard))){
//
//            SpannableString hostRedCard = new SpannableString(match.hostRedCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
//            hostRedCard.setSpan(hostRedColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#F96268"));
//            hostRedCard.setSpan(hostRedCardColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostRedCard.setSpan(hostRedSizeSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostRedCard);
//        }
//
////        match.hostYellowCard = "1";
//        if(!("0".equals(match.hostYellowCard) || TextUtils.isEmpty(match.hostYellowCard))){
//
//            SpannableString hostYellowCard = new SpannableString(match.hostYellowCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#7F5709"));
//            hostYellowCard.setSpan(hostRedColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#FDDB45"));
//            hostYellowCard.setSpan(hostRedCardColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostYellowCard.setSpan(hostRedSizeSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostYellowCard);
//        }

        /**
         * 设置主队状态名称
         */
        setHostTeam(match,lvHolder,0);
        setAwayTeam(match,lvHolder,0);

//        //设置比赛队名
//        SpannableString hostRank = new SpannableString("["+match.hostRank+"]");
//        ForegroundColorSpan hostRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        hostRank.setSpan(hostRankColorSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostRankSizeSpan = new RelativeSizeSpan(0.6f);
//        hostRank.setSpan(hostRankSizeSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostRank);
//
//        SpannableString hostTeamName = new SpannableString("\t"+URLDecoder.decode(match.hostTeamName));
//        ForegroundColorSpan hostTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        hostTeamName.setSpan(hostTeamNameColorSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        hostTeamName.setSpan(hostTeamNameSizeSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan  = new StyleSpan(Typeface.BOLD);
//        hostTeamName.setSpan(boldSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostTeamName);
//
//        lvHolder.tv_left_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        //设置比赛状态中间部分





        //设置右边部分



        lvHolder.tv_right_top.setTextColor(Color.parseColor("#666666"));
        try {
            lvHolder.tv_right_top.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        //设置比赛队名


//        SpannableString awayTeamName = new SpannableString(URLDecoder.decode(match.awayTeamName));
//        ForegroundColorSpan awayTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        awayTeamName.setSpan(awayTeamNameColorSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        awayTeamName.setSpan(awayTeamNameSizeSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        awayTeamName.setSpan(boldSpan_,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayTeamName);
//
//
//        SpannableString awayRank = new SpannableString("\t["+match.awayRank+"]");
//        ForegroundColorSpan awayRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        awayRank.setSpan(awayRankColorSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayRankColorSpanSizeSpan = new RelativeSizeSpan(0.6f);
//        awayRank.setSpan(awayRankColorSpanSizeSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayRank);
//
//        lvHolder.tv_right_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();






    }

    public static void updateImmediate(MatchFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int position) {

        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match = immediateScoreBean.data.matchlist.get(position).match;


        lvHolder.iv_rigth.setVisibility(View.VISIBLE);
        lvHolder.iv_rigth1.setVisibility(View.GONE);
        lvHolder.iv_rigth2.setVisibility(View.GONE);
        if(match.tvlink == 1){
            lvHolder.iv_rigth1.setVisibility(View.VISIBLE);
            lvHolder.iv_rigth.setVisibility(View.GONE);
        }
        String match_information_count = match.match_information_count;
        if(!TextUtils.isEmpty(match.match_information_count)&&Integer.valueOf(match.match_information_count)>0){
            lvHolder.iv_rigth2.setVisibility(View.VISIBLE);
            lvHolder.iv_rigth.setVisibility(View.GONE);
        }


        switch (match.status){
            case "NO_START":
                updateProcess(lvHolder,immediateScoreBean,position);
                if("-1".equals(immediateScoreBean.data.tag)){
                    setRightTopTuiJian(match,lvHolder,"-1");
                }
                return;
            case "FIRST_HALF"://上半场
            case "SECOND_HALF"://下半场

                lvHolder.tv_center_top.setVisibility(View.VISIBLE);
                lvHolder.tv_center_top.setTextColor(Color.parseColor("#3EA53B"));
                lvHolder.tv_time_pos.setVisibility(View.VISIBLE);
                if(match.starttime != 0){
                    String minStart = Utils.getMinStart(Long.valueOf(match.starttime + "000"));
                    if("SECOND_HALF".equals(match.status)) {
                        minStart = Integer.valueOf(minStart)+45 + "";
                    }
                    lvHolder.tv_center_top.setText(minStart);
                }else {
                    String minStart = Utils.getMinStart(Long.valueOf(match.timezoneoffset + "000"));
                    lvHolder.tv_center_top.setText(minStart);
                }

                lvHolder.tv_time_pos.startAnimation(alphaAnimation);

                lvHolder.tv_center_top.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
//        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#3EA53B"));

                SpannableString spannableString = new SpannableString(match.hostScore + ":" + match.awayScore);
                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
                spannableString.setSpan(styleSpan,0,spannableString.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableString);


//                lvHolder.tv_center_bottom.setText(match.hostScore + ":" + match.awayScore);
                setRightTop(match,lvHolder,"");
                break;

            case "HALF_TIME":

                lvHolder.tv_center_top.setVisibility(View.VISIBLE);
                lvHolder.tv_center_top.setTextColor(Color.parseColor("#62AFEF"));
                lvHolder.tv_center_top.setText("中场");
//        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#3EA53B"));


                SpannableString spannableString1 = new SpannableString(match.hostScore + ":" + match.awayScore);
                StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
                spannableString1.setSpan(styleSpan1,0,spannableString1.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableString1);


                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#62AFEF"));
//                lvHolder.tv_center_top.setVisibility(View.INVISIBLE);
//                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
//                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#BFBFBF"));
//                lvHolder.tv_center_bottom.setText("vs");
                setRightTop(match,lvHolder,"");
                break;

            case "COMPLETE":
                updateResult(lvHolder,immediateScoreBean,position);
                return;
        }


        if(match.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }

        if("-2".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index.substring(match.index.length()-3));
        }else if("-5".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index);
        }else{
            lvHolder.tv_left.setVisibility(View.INVISIBLE);
        }


        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString leagueName = new SpannableString(URLDecoder.decode(match.leagueName));
        if(match .color == null){
            match.color = "#777777";
        }
        ForegroundColorSpan leagueNameColorSpan = new ForegroundColorSpan(Color.parseColor(match.color+""));
        leagueName.setSpan(leagueNameColorSpan,0,leagueName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        leagueName.setSpan(boldSpan_,0,leagueName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(leagueName);
        SpannableString starttime = new SpannableString("\t\t"+simpleDateFormat.format(Long.valueOf(match.timezoneoffset+"000")));
        ForegroundColorSpan starttimeColorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        starttime.setSpan(starttimeColorSpan,0,starttime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append(starttime);
        lvHolder.tv_left_top.setText(spannableStringBuilder);
        spannableStringBuilder.clear();


//
//        if(!("0".equals(match.hostRedCard) || TextUtils.isEmpty(match.hostRedCard))){
//
//            SpannableString hostRedCard = new SpannableString(match.hostRedCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
//            hostRedCard.setSpan(hostRedColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#F96268"));
//            hostRedCard.setSpan(hostRedCardColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostRedCard.setSpan(hostRedSizeSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostRedCard);
//        }
//
////        match.hostYellowCard = "1";
//        if(!("0".equals(match.hostYellowCard) || TextUtils.isEmpty(match.hostYellowCard))){
//
//            SpannableString hostYellowCard = new SpannableString(match.hostYellowCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#7F5709"));
//            hostYellowCard.setSpan(hostRedColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#FDDB45"));
//            hostYellowCard.setSpan(hostRedCardColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostYellowCard.setSpan(hostRedSizeSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostYellowCard);
//        }

        /**
         * 设置主队状态名称
         */
        setHostTeam(match,lvHolder,0);
        setAwayTeam(match,lvHolder,0);

//        //设置比赛队名
//        SpannableString hostRank = new SpannableString("["+match.hostRank+"]");
//        ForegroundColorSpan hostRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        hostRank.setSpan(hostRankColorSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostRankSizeSpan = new RelativeSizeSpan(0.6f);
//        hostRank.setSpan(hostRankSizeSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostRank);
//
//        SpannableString hostTeamName = new SpannableString("\t"+URLDecoder.decode(match.hostTeamName));
//        ForegroundColorSpan hostTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        hostTeamName.setSpan(hostTeamNameColorSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        hostTeamName.setSpan(hostTeamNameSizeSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan  = new StyleSpan(Typeface.BOLD);
//        hostTeamName.setSpan(boldSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostTeamName);
//
//        lvHolder.tv_left_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        //设置比赛状态中间部分





        //设置右边部分



        lvHolder.tv_right_top.setTextColor(Color.parseColor("#666666"));
        try {
            lvHolder.tv_right_top.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        //设置比赛队名


//        SpannableString awayTeamName = new SpannableString(URLDecoder.decode(match.awayTeamName));
//        ForegroundColorSpan awayTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        awayTeamName.setSpan(awayTeamNameColorSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        awayTeamName.setSpan(awayTeamNameSizeSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        awayTeamName.setSpan(boldSpan_,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayTeamName);
//
//
//        SpannableString awayRank = new SpannableString("\t["+match.awayRank+"]");
//        ForegroundColorSpan awayRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        awayRank.setSpan(awayRankColorSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayRankColorSpanSizeSpan = new RelativeSizeSpan(0.6f);
//        awayRank.setSpan(awayRankColorSpanSizeSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayRank);
//
//        lvHolder.tv_right_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();






    }
    private static void setRightTopTuiJian(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, ScoreFragment.LvHolder lvHolder,String tag) {
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

        ViewGroup tv1 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv1_layout,null);
        TextView tv1Child = (TextView) tv1.getChildAt(0);

            tv1Child.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
            tv1Child.setText("荐"+match.recommendation_count);
            lvHolder.ll_right_top.addView(tv1);

    }

    private static void setRightTop(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, ScoreFragment.LvHolder lvHolder,String tag) {
         lvHolder.ll_right_top.removeAllViews();
         ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
         TextView tvChild = (TextView) tv.getChildAt(0);
         try {
             tvChild.setText(Utils.parseOdd(match.tape + ""));
         }catch (Exception e){

         }

         lvHolder.ll_right_top.addView(tv);

         ViewGroup tv1 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv1_layout,null);
         TextView tv1Child = (TextView) tv1.getChildAt(0);


         if(!match.status.equals("FIRST_HALF")){
             tv1Child.setText("("+match.hostHalfScore+" : " +match.awayHalfScore+")");
         }else{
             tv1Child.setText("");
         }
         lvHolder.ll_right_top.addView(tv1);




        ViewGroup tv2 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv2_layout,null);
        TextView tv2Child = (TextView) tv2.getChildAt(0);

        if((match.hostCorner == null || match.awayCorner == null) || ("0".equals(match.hostCorner)&&"0".equals(match.awayCorner))){
           tv2Child.setText("");
           tv2Child.setVisibility(View.INVISIBLE);
        }else{
            tv2Child.setVisibility(View.VISIBLE);
            tv2Child.setText(match.hostCorner+"-"+match.awayCorner);
        }
        lvHolder.ll_right_top.addView(tv2);

    }
    private static void setRightTopTuiJian(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, MatchFragment.LvHolder lvHolder,String tag) {
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

        ViewGroup tv1 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv1_layout,null);
        TextView tv1Child = (TextView) tv1.getChildAt(0);

        tv1Child.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
        tv1Child.setText("荐"+match.recommendation_count);
        lvHolder.ll_right_top.addView(tv1);

    }

    private static void setRightTop(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, MatchFragment.LvHolder lvHolder,String tag) {
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

        ViewGroup tv1 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv1_layout,null);
        TextView tv1Child = (TextView) tv1.getChildAt(0);


        if(!match.status.equals("FIRST_HALF")){
            tv1Child.setText("("+match.hostHalfScore+" : " +match.awayHalfScore+")");
        }else{
            tv1Child.setText("");
        }
        lvHolder.ll_right_top.addView(tv1);




        ViewGroup tv2 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv2_layout,null);
        TextView tv2Child = (TextView) tv2.getChildAt(0);

        if((match.hostCorner == null || match.awayCorner == null) || ("0".equals(match.hostCorner)&&"0".equals(match.awayCorner))){
            tv2Child.setText("");
            tv2Child.setVisibility(View.INVISIBLE);
        }else{
            tv2Child.setVisibility(View.VISIBLE);
            tv2Child.setText(match.hostCorner+"-"+match.awayCorner);
        }
        lvHolder.ll_right_top.addView(tv2);

    }

    private static void setHostTeam(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, ScoreFragment.LvHolder lvHolder,int type) {
//        lvHolder.ll_left_bottom.removeAllViews();

        lvHolder.tv_yellow_card_layout.setVisibility(View.GONE);
        lvHolder.tv_red_card_layout.setVisibility(View.GONE);
        lvHolder.tv_rank_layout.setVisibility(View.GONE);
        if(type == 2){

        } else if(!TextUtils.isEmpty(match.hostYellowCard)&&Integer.valueOf(match.hostYellowCard)>0){
//            ViewGroup yellowCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.yellow_card_layout,null);
//            TextView textView = (TextView) yellowCard.getChildAt(0);
//            textView.setText(match.hostYellowCard);
//            lvHolder.ll_left_bottom.addView(yellowCard);
            lvHolder.tv_yellow_card_layout.setText(match.hostYellowCard);
            lvHolder.tv_yellow_card_layout.setVisibility(View.VISIBLE);
        }
        if(type == 2){

        } else if(!TextUtils.isEmpty(match.hostRedCard)&&Integer.valueOf(match.hostRedCard)>0){
//            ViewGroup redCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.red_card_layout,null);
//            TextView textView = (TextView) redCard.getChildAt(0);
//            textView.setText(match.hostRedCard);
//            lvHolder.ll_left_bottom.addView(redCard);
            lvHolder.tv_red_card_layout.setText(match.hostRedCard);
            lvHolder.tv_red_card_layout.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(match.hostRank)&&!"0".equals(match.hostRank)){
//            ViewGroup rank = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.rank_layout,null);
//            TextView textView = (TextView) rank.getChildAt(0);
//            textView.setText("["+match.hostRank+"]");
//            lvHolder.ll_left_bottom.addView(rank);
            if(match.hostRank.length()<=4) {
                lvHolder.tv_rank_layout.setText("[" + match.hostRank + "]");
                lvHolder.tv_rank_layout.setVisibility(View.VISIBLE);
            }
        }

//        ViewGroup team = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.team_name_layout,null);
//        TextView tv_team = (TextView) team.getChildAt(0);
//        tv_team.setText(URLDecoder.decode(match.hostTeamName+""));
//        lvHolder.ll_left_bottom.addView(team);
        String decode = URLDecoder.decode(match.hostTeamName + "");
//        if(decode.length()>4)decode = decode.substring(0,4);
        if(decode.length()>5)decode = decode.substring(0,5);
        lvHolder.tv_team_name_layout.setText(decode);

    }


    private static void setHostTeam(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, MatchFragment.LvHolder lvHolder,int type) {
//        lvHolder.ll_left_bottom.removeAllViews();

        lvHolder.tv_yellow_card_layout.setVisibility(View.GONE);
        lvHolder.tv_red_card_layout.setVisibility(View.GONE);
        lvHolder.tv_rank_layout.setVisibility(View.GONE);
        if(type == 2){

        } else if(!TextUtils.isEmpty(match.hostYellowCard)&&Integer.valueOf(match.hostYellowCard)>0){
//            ViewGroup yellowCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.yellow_card_layout,null);
//            TextView textView = (TextView) yellowCard.getChildAt(0);
//            textView.setText(match.hostYellowCard);
//            lvHolder.ll_left_bottom.addView(yellowCard);
            lvHolder.tv_yellow_card_layout.setText(match.hostYellowCard);
            lvHolder.tv_yellow_card_layout.setVisibility(View.VISIBLE);
        }
        if(type == 2){

        } else if(!TextUtils.isEmpty(match.hostRedCard)&&Integer.valueOf(match.hostRedCard)>0){
//            ViewGroup redCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.red_card_layout,null);
//            TextView textView = (TextView) redCard.getChildAt(0);
//            textView.setText(match.hostRedCard);
//            lvHolder.ll_left_bottom.addView(redCard);
            lvHolder.tv_red_card_layout.setText(match.hostRedCard);
            lvHolder.tv_red_card_layout.setVisibility(View.VISIBLE);
        }

        if(!TextUtils.isEmpty(match.hostRank)&&!"0".equals(match.hostRank)){
//            ViewGroup rank = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.rank_layout,null);
//            TextView textView = (TextView) rank.getChildAt(0);
//            textView.setText("["+match.hostRank+"]");
//            lvHolder.ll_left_bottom.addView(rank);
            if(match.hostRank.length()<=4) {
                lvHolder.tv_rank_layout.setText("[" + match.hostRank + "]");
                lvHolder.tv_rank_layout.setVisibility(View.VISIBLE);
            }
        }

//        ViewGroup team = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.team_name_layout,null);
//        TextView tv_team = (TextView) team.getChildAt(0);
//        tv_team.setText(URLDecoder.decode(match.hostTeamName+""));
//        lvHolder.ll_left_bottom.addView(team);
        String decode = URLDecoder.decode(match.hostTeamName + "");
//        if(decode.length()>4)decode = decode.substring(0,4);
        if(decode.length()>5)decode = decode.substring(0,5);
        lvHolder.tv_team_name_layout.setText(decode);

    }


    private static void setAwayTeam(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, ScoreFragment.LvHolder lvHolder,int type) {
//        lvHolder.ll_right_bottom.removeAllViews();

//        ViewGroup team = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.team_name_layout,null);
//        TextView tv_team = (TextView) team.getChildAt(0);
//        tv_team.setText(URLDecoder.decode(match.awayTeamName+""));
//        lvHolder.ll_right_bottom.addView(team);


        lvHolder.tv_rank_away.setVisibility(View.GONE);
        lvHolder.tv_yellow_card_away.setVisibility(View.GONE);
        lvHolder.tv_red_card_away.setVisibility(View.GONE);
        String decode = URLDecoder.decode(match.awayTeamName + "");
//        if(decode.length()>4)decode = decode.substring(0,4);
        if(decode.length()>5)decode = decode.substring(0,5);
        lvHolder.tv_team_name_away.setText(decode);

        if(!TextUtils.isEmpty(match.awayRank)&&!"0".equals(match.awayRank)){
//            ViewGroup rank = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.rank_layout,null);
//            TextView textView = (TextView) rank.getChildAt(0);
//            textView.setText("["+match.awayRank+"]");
//            lvHolder.ll_right_bottom.addView(rank);
            if(match.awayRank.length()<=4) {
                lvHolder.tv_rank_away.setText("[" + match.awayRank + "]");
                lvHolder.tv_rank_away.setVisibility(View.VISIBLE);
            }
        }
        if(type == 2){

        } else if(!TextUtils.isEmpty(match.awayRedCard)&&Integer.valueOf(match.awayRedCard)>0){
//            ViewGroup redCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.red_card_layout,null);
//            TextView textView = (TextView) redCard.getChildAt(0);
//            textView.setText(match.awayRedCard);
//            lvHolder.ll_right_bottom.addView(redCard);
            lvHolder.tv_red_card_away.setText(match.awayRedCard);
            lvHolder.tv_red_card_away.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(match.awayYellowCard)&&Integer.valueOf(match.awayYellowCard)>0){
//            ViewGroup yellowCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.yellow_card_layout,null);
//            TextView textView = (TextView) yellowCard.getChildAt(0);
//            textView.setText(match.awayYellowCard);
//            lvHolder.ll_right_bottom.addView(yellowCard);
            lvHolder.tv_yellow_card_away.setText(match.awayYellowCard);
            lvHolder.tv_yellow_card_away.setVisibility(View.VISIBLE);
        }

    }

    private static void setAwayTeam(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, MatchFragment.LvHolder lvHolder,int type) {
//        lvHolder.ll_right_bottom.removeAllViews();

//        ViewGroup team = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.team_name_layout,null);
//        TextView tv_team = (TextView) team.getChildAt(0);
//        tv_team.setText(URLDecoder.decode(match.awayTeamName+""));
//        lvHolder.ll_right_bottom.addView(team);


        lvHolder.tv_rank_away.setVisibility(View.GONE);
        lvHolder.tv_yellow_card_away.setVisibility(View.GONE);
        lvHolder.tv_red_card_away.setVisibility(View.GONE);
        String decode = URLDecoder.decode(match.awayTeamName + "");
//        if(decode.length()>4)decode = decode.substring(0,4);
        if(decode.length()>5)decode = decode.substring(0,5);
        lvHolder.tv_team_name_away.setText(decode);

        if(!TextUtils.isEmpty(match.awayRank)&&!"0".equals(match.awayRank)){
//            ViewGroup rank = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.rank_layout,null);
//            TextView textView = (TextView) rank.getChildAt(0);
//            textView.setText("["+match.awayRank+"]");
//            lvHolder.ll_right_bottom.addView(rank);
            if(match.awayRank.length()<=4) {
                lvHolder.tv_rank_away.setText("[" + match.awayRank + "]");
                lvHolder.tv_rank_away.setVisibility(View.VISIBLE);
            }
        }
        if(type == 2){

        } else if(!TextUtils.isEmpty(match.awayRedCard)&&Integer.valueOf(match.awayRedCard)>0){
//            ViewGroup redCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.red_card_layout,null);
//            TextView textView = (TextView) redCard.getChildAt(0);
//            textView.setText(match.awayRedCard);
//            lvHolder.ll_right_bottom.addView(redCard);
            lvHolder.tv_red_card_away.setText(match.awayRedCard);
            lvHolder.tv_red_card_away.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(match.awayYellowCard)&&Integer.valueOf(match.awayYellowCard)>0){
//            ViewGroup yellowCard = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.yellow_card_layout,null);
//            TextView textView = (TextView) yellowCard.getChildAt(0);
//            textView.setText(match.awayYellowCard);
//            lvHolder.ll_right_bottom.addView(yellowCard);
            lvHolder.tv_yellow_card_away.setText(match.awayYellowCard);
            lvHolder.tv_yellow_card_away.setVisibility(View.VISIBLE);
        }

    }

    public static void updateResult(ScoreFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int position) {

//        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match = immediateScoreBean.data.matchList.get(position).match;

        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match ;
        try{
            match = immediateScoreBean.data.matchList.get(position).match;;
        }catch (Exception e){
            match = immediateScoreBean.data.matchlist.get(position).match;
        }

        if(match.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }


        lvHolder.tv_left.setVisibility(View.INVISIBLE);



        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString leagueName = new SpannableString(URLDecoder.decode(match.leagueName));
        String color = match.color;
        if(color == null){
            color = "#777777";
        }
        ForegroundColorSpan leagueNameColorSpan = new ForegroundColorSpan(Color.parseColor(color));
        leagueName.setSpan(leagueNameColorSpan,0,leagueName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        leagueName.setSpan(boldSpan_,0,leagueName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(leagueName);

        String timezoneoffset = match.timezoneoffset;
        SpannableString starttime = new SpannableString("\t\t"+simpleDateFormat.format(Long.valueOf(match.timezoneoffset+"000")));
        ForegroundColorSpan starttimeColorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        starttime.setSpan(starttimeColorSpan,0,starttime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append(starttime);

        lvHolder.tv_left_top.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

//        match.hostRedCard = "2";
//        //设置比赛队名
//        if(!("0".equals(match.hostRedCard) || TextUtils.isEmpty(match.hostRedCard))){
//
//            SpannableString hostRedCard = new SpannableString(match.hostRedCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
//            hostRedCard.setSpan(hostRedColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#F96268"));
//            hostRedCard.setSpan(hostRedCardColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostRedCard.setSpan(hostRedSizeSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostRedCard);
//        }
//
////        match.hostYellowCard = "1";
//        if(!("0".equals(match.hostYellowCard) || TextUtils.isEmpty(match.hostYellowCard))){
//
//            SpannableString hostYellowCard = new SpannableString(match.hostYellowCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#7F5709"));
//            hostYellowCard.setSpan(hostRedColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#FDDB45"));
//            hostYellowCard.setSpan(hostRedCardColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostYellowCard.setSpan(hostRedSizeSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostYellowCard);
//        }




//        SpannableString hostRank = new SpannableString("["+match.hostRank+"]");
//        ForegroundColorSpan hostRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        hostRank.setSpan(hostRankColorSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostRankSizeSpan = new RelativeSizeSpan(0.6f);
//        hostRank.setSpan(hostRankSizeSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostRank);
//
//        SpannableString hostTeamName = new SpannableString("\t"+URLDecoder.decode(match.hostTeamName));
//        ForegroundColorSpan hostTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        hostTeamName.setSpan(hostTeamNameColorSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        hostTeamName.setSpan(hostTeamNameSizeSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan  = new StyleSpan(Typeface.BOLD);
//        hostTeamName.setSpan(boldSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostTeamName);
//
//        lvHolder.tv_left_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        //设置比赛状态中间部分

        lvHolder.tv_center_top.setVisibility(View.VISIBLE);
        lvHolder.tv_center_top.setText("完");
        lvHolder.tv_center_top.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
//        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#F96268"));
        String s = match.hostScore + ":" + match.awayScore;
        SpannableString spannableString = new SpannableString(s);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan,0,spannableString.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        lvHolder.tv_center_bottom.setText(spannableString);


        //设置右边部分
        lvHolder.tv_right_top.setTextColor(Color.parseColor("#666666"));
        try {
            lvHolder.tv_right_top.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        //设置比赛队名


//        SpannableString awayTeamName = new SpannableString(URLDecoder.decode(match.awayTeamName));
//        ForegroundColorSpan awayTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        awayTeamName.setSpan(awayTeamNameColorSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        awayTeamName.setSpan(awayTeamNameSizeSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        awayTeamName.setSpan(boldSpan_,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayTeamName);
//
//
//        SpannableString awayRank = new SpannableString("\t["+match.awayRank+"]");
//        ForegroundColorSpan awayRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        awayRank.setSpan(awayRankColorSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayRankColorSpanSizeSpan = new RelativeSizeSpan(0.6f);
//        awayRank.setSpan(awayRankColorSpanSizeSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayRank);
//
//        lvHolder.tv_right_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();


//        if(match.tvlink == 1){
//            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.tvlink));
//        }else{
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.arrow_right));
//        }

        setHostTeam(match,lvHolder,1);
        setAwayTeam(match,lvHolder,1);

        if(match.tvlink == 1){
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.tvlink));
        }else{
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.arrow_right));
        }

        setRightTopComplete(match,lvHolder);

    }

    public static void updateResult(MatchFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int position) {

//        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match = immediateScoreBean.data.matchList.get(position).match;

        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match ;
        try{
            match = immediateScoreBean.data.matchList.get(position).match;;
        }catch (Exception e){
            match = immediateScoreBean.data.matchlist.get(position).match;
        }

        if(match.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }


        lvHolder.tv_left.setVisibility(View.INVISIBLE);



        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString leagueName = new SpannableString(URLDecoder.decode(match.leagueName));
        String color = match.color;
        if(color == null){
            color = "#777777";
        }
        ForegroundColorSpan leagueNameColorSpan = new ForegroundColorSpan(Color.parseColor(color));
        leagueName.setSpan(leagueNameColorSpan,0,leagueName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        leagueName.setSpan(boldSpan_,0,leagueName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(leagueName);

        String timezoneoffset = match.timezoneoffset;
        SpannableString starttime = new SpannableString("\t\t"+simpleDateFormat.format(Long.valueOf(match.timezoneoffset+"000")));
        ForegroundColorSpan starttimeColorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        starttime.setSpan(starttimeColorSpan,0,starttime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append(starttime);

        lvHolder.tv_left_top.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

//        match.hostRedCard = "2";
//        //设置比赛队名
//        if(!("0".equals(match.hostRedCard) || TextUtils.isEmpty(match.hostRedCard))){
//
//            SpannableString hostRedCard = new SpannableString(match.hostRedCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
//            hostRedCard.setSpan(hostRedColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#F96268"));
//            hostRedCard.setSpan(hostRedCardColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostRedCard.setSpan(hostRedSizeSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostRedCard);
//        }
//
////        match.hostYellowCard = "1";
//        if(!("0".equals(match.hostYellowCard) || TextUtils.isEmpty(match.hostYellowCard))){
//
//            SpannableString hostYellowCard = new SpannableString(match.hostYellowCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#7F5709"));
//            hostYellowCard.setSpan(hostRedColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#FDDB45"));
//            hostYellowCard.setSpan(hostRedCardColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostYellowCard.setSpan(hostRedSizeSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostYellowCard);
//        }




//        SpannableString hostRank = new SpannableString("["+match.hostRank+"]");
//        ForegroundColorSpan hostRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        hostRank.setSpan(hostRankColorSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostRankSizeSpan = new RelativeSizeSpan(0.6f);
//        hostRank.setSpan(hostRankSizeSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostRank);
//
//        SpannableString hostTeamName = new SpannableString("\t"+URLDecoder.decode(match.hostTeamName));
//        ForegroundColorSpan hostTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        hostTeamName.setSpan(hostTeamNameColorSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        hostTeamName.setSpan(hostTeamNameSizeSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan  = new StyleSpan(Typeface.BOLD);
//        hostTeamName.setSpan(boldSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostTeamName);
//
//        lvHolder.tv_left_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        //设置比赛状态中间部分

        lvHolder.tv_center_top.setVisibility(View.VISIBLE);
        lvHolder.tv_center_top.setText("完");
        lvHolder.tv_center_top.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
//        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

        lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#F96268"));
        String s = match.hostScore + ":" + match.awayScore;
        SpannableString spannableString = new SpannableString(s);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(styleSpan,0,spannableString.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        lvHolder.tv_center_bottom.setText(spannableString);


        //设置右边部分
        lvHolder.tv_right_top.setTextColor(Color.parseColor("#666666"));
        try {
            lvHolder.tv_right_top.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        //设置比赛队名


//        SpannableString awayTeamName = new SpannableString(URLDecoder.decode(match.awayTeamName));
//        ForegroundColorSpan awayTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        awayTeamName.setSpan(awayTeamNameColorSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        awayTeamName.setSpan(awayTeamNameSizeSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        awayTeamName.setSpan(boldSpan_,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayTeamName);
//
//
//        SpannableString awayRank = new SpannableString("\t["+match.awayRank+"]");
//        ForegroundColorSpan awayRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        awayRank.setSpan(awayRankColorSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayRankColorSpanSizeSpan = new RelativeSizeSpan(0.6f);
//        awayRank.setSpan(awayRankColorSpanSizeSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayRank);
//
//        lvHolder.tv_right_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();


//        if(match.tvlink == 1){
//            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.tvlink));
//        }else{
        lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.arrow_right));
//        }

        setHostTeam(match,lvHolder,1);
        setAwayTeam(match,lvHolder,1);

        if(match.tvlink == 1){
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.tvlink));
        }else{
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.arrow_right));
        }

        setRightTopComplete(match,lvHolder);

    }

    private static void setRightTopComplete(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, ScoreFragment.LvHolder lvHolder) {
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

        ViewGroup tv1 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv1_layout,null);
        TextView tv1Child = (TextView) tv1.getChildAt(0);
        if(!match.status.equals("FIRST_HALF")){
            tv1Child.setText("("+match.hostHalfScore+" : " +match.awayHalfScore+")");
        }else{
            tv1Child.setText("");
        }
        lvHolder.ll_right_top.addView(tv1);
    }

    private static void setRightTopComplete(ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match, MatchFragment.LvHolder lvHolder) {
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

        ViewGroup tv1 = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv1_layout,null);
        TextView tv1Child = (TextView) tv1.getChildAt(0);
        if(!match.status.equals("FIRST_HALF")){
            tv1Child.setText("("+match.hostHalfScore+" : " +match.awayHalfScore+")");
        }else{
            tv1Child.setText("");
        }
        lvHolder.ll_right_top.addView(tv1);
    }

    public static void updateProcess(ScoreFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int i) {
        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match ;
        try{
            match = immediateScoreBean.data.matchList.get(i).match;;
        }catch (Exception e){
            match = immediateScoreBean.data.matchlist.get(i).match;
        }

        if(match.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }

        if("-2".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index.substring(match.index.length()-3));
        }else if("-5".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index);
        }else{
            lvHolder.tv_left.setVisibility(View.INVISIBLE);
        }





        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString leagueName = new SpannableString(URLDecoder.decode(match.leagueName));
        if(match .color == null){
            match.color = "#777777";
        }
        ForegroundColorSpan leagueNameColorSpan = new ForegroundColorSpan(Color.parseColor(match.color+""));
        leagueName.setSpan(leagueNameColorSpan,0,leagueName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        leagueName.setSpan(boldSpan_,0,leagueName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(leagueName);

        SpannableString starttime = new SpannableString("\t\t"+simpleDateFormat.format(Long.valueOf(match.timezoneoffset+"000")));
        ForegroundColorSpan starttimeColorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        starttime.setSpan(starttimeColorSpan,0,starttime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append(starttime);

        lvHolder.tv_left_top.setText(spannableStringBuilder);

        spannableStringBuilder.clear();



//        if(!("0".equals(match.hostRedCard) || TextUtils.isEmpty(match.hostRedCard))){
//
//            SpannableString hostRedCard = new SpannableString(match.hostRedCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
//            hostRedCard.setSpan(hostRedColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#F96268"));
//            hostRedCard.setSpan(hostRedCardColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostRedCard.setSpan(hostRedSizeSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostRedCard);
//        }
//
////        match.hostYellowCard = "1";
//        if(!("0".equals(match.hostYellowCard) || TextUtils.isEmpty(match.hostYellowCard))){
//
//            SpannableString hostYellowCard = new SpannableString(match.hostYellowCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#7F5709"));
//            hostYellowCard.setSpan(hostRedColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#FDDB45"));
//            hostYellowCard.setSpan(hostRedCardColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostYellowCard.setSpan(hostRedSizeSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostYellowCard);
//        }
//
//
//
//        //设置比赛队名
//        SpannableString hostRank = new SpannableString("["+match.hostRank+"]");
//        ForegroundColorSpan hostRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        hostRank.setSpan(hostRankColorSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostRankSizeSpan = new RelativeSizeSpan(0.6f);
//        hostRank.setSpan(hostRankSizeSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostRank);
//
//        SpannableString hostTeamName = new SpannableString("\t"+URLDecoder.decode(match.hostTeamName));
//        ForegroundColorSpan hostTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        hostTeamName.setSpan(hostTeamNameColorSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        hostTeamName.setSpan(hostTeamNameSizeSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan  = new StyleSpan(Typeface.BOLD);
//        hostTeamName.setSpan(boldSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostTeamName);
//
//        lvHolder.tv_left_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        lvHolder.tv_center_top.setVisibility(View.INVISIBLE);
        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#BFBFBF"));
        lvHolder.tv_center_bottom.setText("vs");


        //设置右边部分
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

//        lvHolder.tv_right_top.setTextColor(Color.parseColor("#666666"));
//        try {
//            lvHolder.tv_right_top.setText(Utils.parseOdd(match.tape + ""));
//        }catch (Exception e){
//
//        }

        //设置比赛队名


//        SpannableString awayTeamName = new SpannableString(URLDecoder.decode(match.awayTeamName));
//        ForegroundColorSpan awayTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        awayTeamName.setSpan(awayTeamNameColorSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        awayTeamName.setSpan(awayTeamNameSizeSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        awayTeamName.setSpan(boldSpan_,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayTeamName);
//
//
//        SpannableString awayRank = new SpannableString("\t["+match.awayRank+"]");
//        ForegroundColorSpan awayRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        awayRank.setSpan(awayRankColorSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayRankColorSpanSizeSpan = new RelativeSizeSpan(0.6f);
//        awayRank.setSpan(awayRankColorSpanSizeSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayRank);
//
//        lvHolder.tv_right_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        setHostTeam(match,lvHolder,2);
        setAwayTeam(match,lvHolder,2);

        if(match.tvlink == 1){
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.tvlink));
        }else{
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.arrow_right));
        }



    }

    public static void updateProcess(MatchFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int i) {
        ImmediateScoreBean.DataBean.MatchlistBean.MatchBean match ;
        try{
            match = immediateScoreBean.data.matchList.get(i).match;;
        }catch (Exception e){
            match = immediateScoreBean.data.matchlist.get(i).match;
        }

        if(match.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }

        if("-2".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index.substring(match.index.length()-3));
        }else if("-5".equals(immediateScoreBean.data.tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            lvHolder.tv_left.setText(match.index);
        }else{
            lvHolder.tv_left.setVisibility(View.INVISIBLE);
        }





        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString leagueName = new SpannableString(URLDecoder.decode(match.leagueName));
        if(match .color == null){
            match.color = "#777777";
        }
        ForegroundColorSpan leagueNameColorSpan = new ForegroundColorSpan(Color.parseColor(match.color+""));
        leagueName.setSpan(leagueNameColorSpan,0,leagueName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        leagueName.setSpan(boldSpan_,0,leagueName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(leagueName);

        SpannableString starttime = new SpannableString("\t\t"+simpleDateFormat.format(Long.valueOf(match.timezoneoffset+"000")));
        ForegroundColorSpan starttimeColorSpan = new ForegroundColorSpan(Color.parseColor("#666666"));
        starttime.setSpan(starttimeColorSpan,0,starttime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
        spannableStringBuilder.append(starttime);

        lvHolder.tv_left_top.setText(spannableStringBuilder);

        spannableStringBuilder.clear();



//        if(!("0".equals(match.hostRedCard) || TextUtils.isEmpty(match.hostRedCard))){
//
//            SpannableString hostRedCard = new SpannableString(match.hostRedCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#FFFFFF"));
//            hostRedCard.setSpan(hostRedColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#F96268"));
//            hostRedCard.setSpan(hostRedCardColorSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostRedCard.setSpan(hostRedSizeSpan,0,hostRedCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostRedCard);
//        }
//
////        match.hostYellowCard = "1";
//        if(!("0".equals(match.hostYellowCard) || TextUtils.isEmpty(match.hostYellowCard))){
//
//            SpannableString hostYellowCard = new SpannableString(match.hostYellowCard+"\t");
//            ForegroundColorSpan hostRedColorSpan = new ForegroundColorSpan(Color.parseColor("#7F5709"));
//            hostYellowCard.setSpan(hostRedColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//            BackgroundColorSpan hostRedCardColorSpan = new BackgroundColorSpan(Color.parseColor("#FDDB45"));
//            hostYellowCard.setSpan(hostRedCardColorSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            RelativeSizeSpan hostRedSizeSpan = new RelativeSizeSpan(1.0f);
//            hostYellowCard.setSpan(hostRedSizeSpan,0,hostYellowCard.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//            spannableStringBuilder.append(hostYellowCard);
//        }
//
//
//
//        //设置比赛队名
//        SpannableString hostRank = new SpannableString("["+match.hostRank+"]");
//        ForegroundColorSpan hostRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        hostRank.setSpan(hostRankColorSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostRankSizeSpan = new RelativeSizeSpan(0.6f);
//        hostRank.setSpan(hostRankSizeSpan,0,hostRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostRank);
//
//        SpannableString hostTeamName = new SpannableString("\t"+URLDecoder.decode(match.hostTeamName));
//        ForegroundColorSpan hostTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        hostTeamName.setSpan(hostTeamNameColorSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan hostTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        hostTeamName.setSpan(hostTeamNameSizeSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan  = new StyleSpan(Typeface.BOLD);
//        hostTeamName.setSpan(boldSpan,0,hostTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(hostTeamName);
//
//        lvHolder.tv_left_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        lvHolder.tv_center_top.setVisibility(View.INVISIBLE);
        lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
        lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#BFBFBF"));
        lvHolder.tv_center_bottom.setText("vs");


        //设置右边部分
        lvHolder.ll_right_top.removeAllViews();
        ViewGroup  tv = (ViewGroup) View.inflate(QCaApplication.getContext(),R.layout.right_top_tv_layout,null);
        TextView tvChild = (TextView) tv.getChildAt(0);
        try {
            tvChild.setText(Utils.parseOdd(match.tape + ""));
        }catch (Exception e){

        }

        lvHolder.ll_right_top.addView(tv);

//        lvHolder.tv_right_top.setTextColor(Color.parseColor("#666666"));
//        try {
//            lvHolder.tv_right_top.setText(Utils.parseOdd(match.tape + ""));
//        }catch (Exception e){
//
//        }

        //设置比赛队名


//        SpannableString awayTeamName = new SpannableString(URLDecoder.decode(match.awayTeamName));
//        ForegroundColorSpan awayTeamNameColorSpan = new ForegroundColorSpan(Color.parseColor("#282828"));
//        awayTeamName.setSpan(awayTeamNameColorSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayTeamNameSizeSpan = new RelativeSizeSpan(1.05f);
//        awayTeamName.setSpan(awayTeamNameSizeSpan,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        StyleSpan boldSpan_  = new StyleSpan(Typeface.BOLD);
//        awayTeamName.setSpan(boldSpan_,0,awayTeamName.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayTeamName);
//
//
//        SpannableString awayRank = new SpannableString("\t["+match.awayRank+"]");
//        ForegroundColorSpan awayRankColorSpan = new ForegroundColorSpan(Color.parseColor("#999999"));
//        awayRank.setSpan(awayRankColorSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE );
//        RelativeSizeSpan awayRankColorSpanSizeSpan = new RelativeSizeSpan(0.6f);
//        awayRank.setSpan(awayRankColorSpanSizeSpan,0,awayRank.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(awayRank);
//
//        lvHolder.tv_right_bottom.setText(spannableStringBuilder);

        spannableStringBuilder.clear();

        setHostTeam(match,lvHolder,2);
        setAwayTeam(match,lvHolder,2);

        if(match.tvlink == 1){
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.tvlink));
        }else{
            lvHolder.iv_rigth.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.arrow_right));
        }



    }

    public static void updateFollow(ScoreFragment.LvHolder lvHolder, ImmediateScoreBean immediateScoreBean, int i) {

    }
}
