package com.mayisports.qca.utils;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.MatchTabBean;
import com.mayisports.qca.fragment.TabLayoutFragment;

import java.net.URLDecoder;
import java.util.List;

import static com.mayisports.qca.utils.BindViewUtils.alphaAnimation;

/**
 * 首页赛事比分填充
 * Created by Zpj on 2018/1/25.
 */

public class MatchTabLayoutBinder {

    public static void bindData(MatchTabBean.DataBean.MatchBean matchBean, TabLayoutFragment.MyHolder lvHolder, int position,String tag){


        lvHolder.tv_title.setText(matchBean.getStrTitle());

        lvHolder.tv_team_name_layout.setText(URLDecoder.decode(matchBean.hostTeamName));
        lvHolder.tv_team_name_away.setText(URLDecoder.decode(matchBean.awayTeamName));


        if(matchBean.logoH != 0){
            String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ matchBean.logoH + ".png";
            PictureUtils.show(hostUrl,lvHolder.iv_host_icon_match_detail,R.drawable.host_icon);
        }else{
            PictureUtils.show(R.drawable.host_icon,lvHolder.iv_host_icon_match_detail);
        }
        if(matchBean.logoG != 0){
            String hostUrl = "http://dldemo-img.stor.sinaapp.com/logo_"+ matchBean.logoG + ".png";
            PictureUtils.show(hostUrl,lvHolder.iv_away_icon_match_detail,R.drawable.away_icon);
        }else{
            PictureUtils.show(R.drawable.away_icon,lvHolder.iv_away_icon_match_detail);
        }


        //联赛名称 时间

        Long aLong = Long.valueOf(matchBean.timezoneoffset + "000");
        String format = Utils.simpleDateFormatHHMM.format(aLong);
        String matchName = URLDecoder.decode(matchBean.leagueName);
        String roundStr = "";
        try{
            Integer integer = Integer.valueOf(matchBean.round);
            roundStr = "\t"+"第"+integer+"轮";
        }catch (Exception e){
            roundStr = "\t"+matchBean.round;
        }

        if(roundStr.length()>5){
            roundStr = "";
        }

        lvHolder.tv_left_top.setText(matchName+roundStr+"\t"+format);


        if(matchBean.collection_status == 0){//未关注
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_gray));
        }else{
            lvHolder.iv_star.setImageDrawable(QCaApplication.getContext().getResources().getDrawable(R.drawable.star_yellow));
        }


        /**
         * 竞猜
         */
        if("-3".equals(tag)){
            lvHolder.tv_left.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(matchBean.text1)){
                String substring1 = matchBean.text1.substring(0, matchBean.text1.length() - 3);
                String substring = matchBean.text1.substring(matchBean.text1.length() - 3);
                lvHolder.tv_left.setText(substring1 +"\n"+substring);
            }
        }else{
            lvHolder.tv_left.setVisibility(View.GONE);
        }

        lvHolder.iv_rigth1.setVisibility(View.GONE);
        lvHolder.iv_rigth2.setVisibility(View.GONE);

        String status = matchBean.status;
        lvHolder.tv_layerlist.setVisibility(View.GONE);
        switch (status){
            case "NO_START":
            case "DELAY":
                lvHolder.iv_star.setVisibility(View.VISIBLE);
                lvHolder.ll_score_match_fg.setVisibility(View.GONE);
                lvHolder.tv_center_top.setVisibility(View.GONE);
                lvHolder.tv_time_pos.setVisibility(View.GONE);

                lvHolder.tv_left_top.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));


                if(matchBean.tvlink == 1){
                    lvHolder.iv_rigth1.setVisibility(View.VISIBLE);
                }

                if(!TextUtils.isEmpty(matchBean.match_information_count)&&Integer.valueOf(matchBean.match_information_count)>0){
                    lvHolder.iv_rigth2.setVisibility(View.VISIBLE);
                }


                bindTVLink(lvHolder.tv_live_url_bottom,matchBean.tvlinkList);
                hideCard(lvHolder);

                if(matchBean.playerList != 0){
                    lvHolder.tv_layerlist.setVisibility(View.VISIBLE);
                }


                break;
            case "FIRST_HALF":
            case "SECOND_HALF":
                lvHolder.iv_rigth.setVisibility(View.GONE);
                lvHolder.iv_star.setVisibility(View.GONE);
                lvHolder.ll_score_match_fg.setVisibility(View.VISIBLE);

                lvHolder.tv_center_top.setVisibility(View.VISIBLE);
                lvHolder.tv_center_top.setTextColor(Color.parseColor("#3EA53B"));
                lvHolder.tv_time_pos.setVisibility(View.VISIBLE);
                if(matchBean.starttime != 0){
                    String minStart = Utils.getMinStart(Long.valueOf(matchBean.starttime + "000"));
                    if("SECOND_HALF".equals(matchBean.status)) {
                        minStart = Integer.valueOf(minStart)+45 + "";
                    }
                    lvHolder.tv_center_top.setText(minStart);
                }else {
                    String minStart = Utils.getMinStart(Long.valueOf(matchBean.timezoneoffset + "000"));
                    lvHolder.tv_center_top.setText(minStart);
                }

                lvHolder.tv_time_pos.startAnimation(alphaAnimation);
                lvHolder.tv_center_top.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,27);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#3EA53B"));
                SpannableString spannableString = new SpannableString(matchBean.hostScore + " : " + matchBean.awayScore);
//                StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
//                spannableString.setSpan(styleSpan,0,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableString);

                lvHolder.tv_left_top.setText(matchName);
                lvHolder.tv_left_top.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));
                bindTVLink(lvHolder.tv_live_url_bottom,matchBean.tvlinkList);
                bindCard(lvHolder,matchBean);

                if(matchBean.tvlink == 1){
                    lvHolder.iv_rigth1.setVisibility(View.VISIBLE);
                }

                if(!TextUtils.isEmpty(matchBean.match_information_count)&&Integer.valueOf(matchBean.match_information_count)>0){
                    lvHolder.iv_rigth2.setVisibility(View.VISIBLE);
                }
                break;
            case "HALF_TIME":
                lvHolder.iv_rigth.setVisibility(View.GONE);
                lvHolder.iv_star.setVisibility(View.GONE);
                lvHolder.ll_score_match_fg.setVisibility(View.VISIBLE);


                lvHolder.tv_center_top.setVisibility(View.VISIBLE);
                lvHolder.tv_center_top.setTextColor(Color.parseColor("#62AFEF"));
                lvHolder.tv_center_top.setText("中场");
                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,27);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#3EA53B"));
                SpannableString spannableString1 = new SpannableString(matchBean.hostScore + " : " + matchBean.awayScore);
//                StyleSpan styleSpan1 = new StyleSpan(Typeface.BOLD);
//                spannableString1.setSpan(styleSpan1,0,spannableString1.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableString1);
                lvHolder.tv_center_bottom.setTextColor(Color.parseColor("#62AFEF"));
                lvHolder.tv_left_top.setText(matchName);
                lvHolder.tv_left_top.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));

                bindTVLink(lvHolder.tv_live_url_bottom,matchBean.tvlinkList);//直播

                bindCard(lvHolder,matchBean);


                if(matchBean.tvlink == 1){
                    lvHolder.iv_rigth1.setVisibility(View.VISIBLE);
                }

                if(!TextUtils.isEmpty(matchBean.match_information_count)&&Integer.valueOf(matchBean.match_information_count)>0){
                    lvHolder.iv_rigth2.setVisibility(View.VISIBLE);
                }
                break;

            case "COMPLETE":
                lvHolder.tv_time_pos.setVisibility(View.GONE);
                lvHolder.tv_center_top.setVisibility(View.GONE);
                lvHolder.iv_rigth.setVisibility(View.GONE);
                lvHolder.iv_star.setVisibility(View.GONE);
                lvHolder.ll_score_match_fg.setVisibility(View.VISIBLE);
                lvHolder.tv_left_top.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black_99));

                lvHolder.tv_center_bottom.setTextSize(TypedValue.COMPLEX_UNIT_SP,27);
                lvHolder.tv_center_bottom.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                SpannableString spannableStringx = new SpannableString(matchBean.hostScore + " : " + matchBean.awayScore);
//                StyleSpan styleSpanx = new StyleSpan(Typeface.BOLD);
//                spannableStringx.setSpan(styleSpanx,0,spannableStringx.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                lvHolder.tv_center_bottom.setText(spannableStringx);

//                bindCard(lvHolder,matchBean);
                hideCard(lvHolder);
                lvHolder.tv_live_url_bottom.setText("已结束");

                if(!TextUtils.isEmpty(matchBean.match_information_count)&&Integer.valueOf(matchBean.match_information_count)>0){
                    lvHolder.iv_rigth2.setVisibility(View.VISIBLE);
                }
                break;
            case "ABORTION":
                break;

            case "INTERRUPT":
                break;
        }


    }

    private static void hideCard(TabLayoutFragment.MyHolder lvHolder){
        /**
         * 主队
         */
        lvHolder.tv_yellow_card_layout.setVisibility(View.GONE);
        lvHolder.tv_red_card_layout.setVisibility(View.GONE);
        /**
         * 客队
         */
        lvHolder.tv_yellow_card_away.setVisibility(View.GONE);
        lvHolder.tv_red_card_away.setVisibility(View.GONE);
    }
    /**
     * 绑定红黄牌
     * @param lvHolder
     * @param matchBean
     */
    private static void bindCard(TabLayoutFragment.MyHolder lvHolder, MatchTabBean.DataBean.MatchBean matchBean) {

        /**
         * 主队
         */
        lvHolder.tv_yellow_card_layout.setVisibility(View.GONE);
        lvHolder.tv_red_card_layout.setVisibility(View.GONE);
       if(!TextUtils.isEmpty(matchBean.hostYellowCard)&&Integer.valueOf(matchBean.hostYellowCard)>0){
            lvHolder.tv_yellow_card_layout.setText(matchBean.hostYellowCard);
            lvHolder.tv_yellow_card_layout.setVisibility(View.VISIBLE);
        }


        if(!TextUtils.isEmpty(matchBean.hostRedCard)&&Integer.valueOf(matchBean.hostRedCard)>0){
            lvHolder.tv_red_card_layout.setText(matchBean.hostRedCard);
            lvHolder.tv_red_card_layout.setVisibility(View.VISIBLE);
        }


        /**
         * 客队
         */
        lvHolder.tv_yellow_card_away.setVisibility(View.GONE);
        lvHolder.tv_red_card_away.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(matchBean.awayRedCard)&&Integer.valueOf(matchBean.awayRedCard)>0){
            lvHolder.tv_red_card_away.setText(matchBean.awayRedCard);
            lvHolder.tv_red_card_away.setVisibility(View.VISIBLE);
        }
        if(!TextUtils.isEmpty(matchBean.awayYellowCard)&&Integer.valueOf(matchBean.awayYellowCard)>0){
            lvHolder.tv_yellow_card_away.setText(matchBean.awayYellowCard);
            lvHolder.tv_yellow_card_away.setVisibility(View.VISIBLE);
        }


    }

    private static void bindTVLink(TextView tvLink,List<String> list){
        if(list != null) {
            String str = "";
            for(int i = 0;i<list.size();i++) {
                str += list.get(i) +"|";
            }
            if(str.length()>0){
                str = str.substring(0,str.length() - 1);
                tvLink.setText(str);
            }
        }else {
            tvLink.setText("直播链接收集中");
        }

    }

}
