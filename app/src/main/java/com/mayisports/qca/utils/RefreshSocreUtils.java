package com.mayisports.qca.utils;

import android.util.Log;

import com.mayisports.qca.bean.HomeItemBean;
import com.mayisports.qca.bean.ImmediateScoreBean;
import com.mayisports.qca.bean.MatchDetailBean;
import com.mayisports.qca.bean.MatchTabBean;
import com.mayisports.qca.bean.RefreshBean;
import com.mayisports.qca.bean.ScoreStarBean;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 时时更新工具类
 * Created by Zpj on 2017/12/20.
 */

public class RefreshSocreUtils {

    public interface OnRefreshCallBack{
        void onRefreshOk(Object object);
    }

    public static void getData(final ImmediateScoreBean immediateScoreBean, final OnRefreshCallBack onRefreshCallBack){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListStatus");
        String ids = parseBean(immediateScoreBean);
        params.put("value",ids);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans = JsonParseUtils.parseJsonRefreshList(string, ImmediateScoreBean.DataBean.MatchlistBean.MatchBean.class);
                delData(immediateScoreBean,refreshBeans,onRefreshCallBack);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                 ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private static void delData(ImmediateScoreBean immediateScoreBean, ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans, OnRefreshCallBack onRefreshCallBack) {
          for(int i = 0;i<positions.size();i++){
              ImmediateScoreBean.DataBean.MatchlistBean.MatchBean matchBean = refreshBeans.get(i);
              Integer position = positions.get(i);
              ImmediateScoreBean.DataBean.MatchlistBean matchlistBean1 = immediateScoreBean.data.matchlist.get(position);
              /**
               * awayCorner: "4", // 客队角球
               awayHalfScore: "0", // 客队上半场比分
               awayRed: "0", // 客队红牌
               awayScore: "0", // 客队比分
               awayYellow: "1", // 客队黄牌
               betId: "1406341", // 比赛 id
               hostCorner: "0",  // 主队角球
               hostHalfScore: "0", // 主队上半场比分
               hostRed: "0",  // 主队红牌
               hostScore: "1", // 主队比分
               hostYellow: "0", // 主队 黄牌
               playerList: 0, // 是否有阵容,  赛前显示
               starttime: "1510304180", // 上半场开始时间 or 下半场开始时间
               status: "FIRST_HALF", // 比赛状态
               timezoneoffset: "1510303800" // 比赛开始时间
               */
              matchlistBean1.match.awayCorner = matchBean.awayCorner;
              matchlistBean1.match.awayHalfScore = matchBean.awayHalfScore;
              matchlistBean1.match.awayRedCard = matchBean.awayRed;
              matchlistBean1.match.awayScore = matchBean.awayScore;
              matchlistBean1.match.awayYellowCard = matchBean.awayYellow;
              matchlistBean1.match.betId = matchBean.betId;
              matchlistBean1.match.hostCorner = matchBean.hostCorner;
              matchlistBean1.match.hostHalfScore = matchBean.hostHalfScore;
              matchlistBean1.match.hostRedCard = matchBean.hostRed;
              matchlistBean1.match.hostScore = matchBean.hostScore;

              matchlistBean1.match.hostYellowCard = matchBean.hostYellow;
              matchlistBean1.match.playerList = matchBean.playerList;
              matchlistBean1.match.starttime = matchBean.starttime;
              matchlistBean1.match.status = matchBean.status;
              matchlistBean1.match.timezoneoffset = matchBean.timezoneoffset;

          }
          //回调
          onRefreshCallBack.onRefreshOk(immediateScoreBean);
    }

    public static void getDataForMatchDetail(final MatchDetailBean matchDetailBean, final OnRefreshCallBack onRefreshCallBack){
        if(matchDetailBean == null||matchDetailBean.data == null)return;
        if(matchDetailBean.data.match.status.equals("NO_START")){
            Long time = Long.valueOf(matchDetailBean.data.match.timezoneoffset + "000");
            Integer integer = Integer.valueOf(Utils.getMinStart(time));
            if(integer <0) {
                Integer delt = Math.abs(integer);
                if (delt >= 30) return;
            }
        }
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListStatus");
//        String ids = parseBean(immediateScoreBean);
        params.put("value",matchDetailBean.data.match.betId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans = JsonParseUtils.parseJsonRefreshList(string, ImmediateScoreBean.DataBean.MatchlistBean.MatchBean.class);
                delDataForMatchDetail(matchDetailBean,refreshBeans,onRefreshCallBack);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private static void delDataForMatchDetail(MatchDetailBean matchDetailBean, ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans, OnRefreshCallBack onRefreshCallBack) {
        for(int i = 0;i<refreshBeans.size();i++){
            ImmediateScoreBean.DataBean.MatchlistBean.MatchBean matchBean = refreshBeans.get(i);
            MatchDetailBean.DataBean.MatchBean match = matchDetailBean.data.match;
            /**
             * awayCorner: "4", // 客队角球
             awayHalfScore: "0", // 客队上半场比分
             awayRed: "0", // 客队红牌
             awayScore: "0", // 客队比分
             awayYellow: "1", // 客队黄牌
             betId: "1406341", // 比赛 id
             hostCorner: "0",  // 主队角球
             hostHalfScore: "0", // 主队上半场比分
             hostRed: "0",  // 主队红牌
             hostScore: "1", // 主队比分
             hostYellow: "0", // 主队 黄牌
             playerList: 0, // 是否有阵容,  赛前显示
             starttime: "1510304180", // 上半场开始时间 or 下半场开始时间
             status: "FIRST_HALF", // 比赛状态
             timezoneoffset: "1510303800" // 比赛开始时间
             */
//            match.awayCorner = matchBean.awayCorner+"";
            match.awayHalfScore = matchBean.awayHalfScore;
//            match.awayRedCard = matchBean.awayRed;
            match.awayScore = matchBean.awayScore;
//            match.awayYellowCard = matchBean.awayYellow;
            match.betId = matchBean.betId;
//            match.hostCorner = matchBean.hostCorner;
            match.hostHalfScore = matchBean.hostHalfScore;
//            match.hostRedCard = matchBean.hostRed;
            match.hostScore = matchBean.hostScore;

//            match.hostYellowCard = matchBean.hostYellow;
//            match.playerList = matchBean.playerList;
            match.starttime = matchBean.starttime;
            match.status = matchBean.status;
            match.timezoneoffset = matchBean.timezoneoffset;

        }
        //回调
        onRefreshCallBack.onRefreshOk(matchDetailBean);
    }


    private static List<Integer> positions = new ArrayList<>();
    private static String parseBean(ImmediateScoreBean immediateScoreBean) {
        positions.clear();
        String ids = "";
        if(immediateScoreBean != null && immediateScoreBean.data != null &&immediateScoreBean.data.matchlist != null){

           for(int i = 0;i<immediateScoreBean.data.matchlist.size();i++){
               ImmediateScoreBean.DataBean.MatchlistBean matchlistBean = immediateScoreBean.data.matchlist.get(i);
               if(matchlistBean.match.status.equals("COMPLETE"))continue;

               if(matchlistBean.match.status.equals("NO_START")){
                   Long time = Long.valueOf(matchlistBean.match.timezoneoffset + "000");
                   Integer integer = Integer.valueOf(Utils.getMinStart(time));
                   if(integer <0) {
                       Integer delt = Math.abs(integer);
                       if (delt >= 30) continue;
                   }
                   ids += matchlistBean.match.betId+",";
                   positions.add(i);
                   continue;
               }

               ids += matchlistBean.match.betId+",";
               positions.add(i);

           }

           if(ids.length() >0)ids = ids.substring(0,ids.length()-1);

        }
        return ids;
    }

    private static  synchronized  String parseBean(MatchTabBean matchTabBean,String tagId) {
        positions.clear();
        matchTabBean.positions = new ArrayList<>();
        String ids = "";
        if(matchTabBean != null && matchTabBean.data != null &&matchTabBean.data.match != null){
            for(int i = 0;i<matchTabBean.data.match.size();i++){
                MatchTabBean.DataBean.MatchBean matchlistBean = matchTabBean.data.match.get(i);
                if(matchlistBean.status.equals("COMPLETE"))continue;

                if(matchlistBean.status.equals("NO_START")){
                    Long time = Long.valueOf(matchlistBean.timezoneoffset + "000");
                    Integer integer = Integer.valueOf(Utils.getMinStart(time));
                    if(integer <0) {
                        Integer delt = Math.abs(integer);
                        if (delt >= 30) continue;
                    }
                    ids += matchlistBean.betId+",";
                    positions.add(i);
                    matchTabBean.positions.add(i);
                    continue;
                }

                ids += matchlistBean.betId+",";
                positions.add(i);
                matchTabBean.positions.add(i);

            }

            if(ids.length() >0)ids = ids.substring(0,ids.length()-1);

        }
        return ids;
    }
    public static  void getData(final MatchTabBean matchTabBean , final OnRefreshCallBack onRefreshCallBack, final String tagId){
        if(matchTabBean == null)return;
        String url = Constant.BASE_URL + "/php/api.php";
        if(matchTabBean.tagConvert == null){
            matchTabBean.tagConvert = tagId;
        }


        HttpParams params = new HttpParams();
        params.put("action","matchListStatus");
        String ids = parseBean(matchTabBean,tagId);
        //http:///php/api.php?action=matchListStatus&value=1424258,1424256,1424260,1490885,1490886,1490887,1490192
        params.put("value",ids);

        Log.e("refresh",System.currentTimeMillis()+"-->"+tagId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                    ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans = JsonParseUtils.parseJsonRefreshList(string, ImmediateScoreBean.DataBean.MatchlistBean.MatchBean.class);
                    delData(matchTabBean, refreshBeans, onRefreshCallBack, tagId);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private static synchronized void delData( MatchTabBean matchTabBean, ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans, OnRefreshCallBack onRefreshCallBack,String tagId) {
        try {
            for (int i = 0; i < matchTabBean.positions.size(); i++) {
                ImmediateScoreBean.DataBean.MatchlistBean.MatchBean matchBean = refreshBeans.get(i);
                Integer position = matchTabBean.positions.get(i);
                MatchTabBean.DataBean.MatchBean matchlistBean1 = matchTabBean.data.match.get(position);
                /**
                 * awayCorner: "4", // 客队角球
                 awayHalfScore: "0", // 客队上半场比分
                 awayRed: "0", // 客队红牌
                 awayScore: "0", // 客队比分
                 awayYellow: "1", // 客队黄牌
                 betId: "1406341", // 比赛 id
                 hostCorner: "0",  // 主队角球
                 hostHalfScore: "0", // 主队上半场比分
                 hostRed: "0",  // 主队红牌
                 hostScore: "1", // 主队比分
                 hostYellow: "0", // 主队 黄牌
                 playerList: 0, // 是否有阵容,  赛前显示
                 starttime: "1510304180", // 上半场开始时间 or 下半场开始时间
                 status: "FIRST_HALF", // 比赛状态
                 timezoneoffset: "1510303800" // 比赛开始时间
                 */
                matchlistBean1.awayCorner = matchBean.awayCorner;
                matchlistBean1.awayHalfScore = matchBean.awayHalfScore;
                matchlistBean1.awayRedCard = matchBean.awayRed;
                matchlistBean1.awayScore = matchBean.awayScore;
                matchlistBean1.awayYellowCard = matchBean.awayYellow;
                matchlistBean1.betId = matchBean.betId;
                matchlistBean1.hostCorner = matchBean.hostCorner;
                matchlistBean1.hostHalfScore = matchBean.hostHalfScore;
                matchlistBean1.hostRedCard = matchBean.hostRed;
                matchlistBean1.hostScore = matchBean.hostScore;
                matchlistBean1.hostYellowCard = matchBean.hostYellow;
                matchlistBean1.playerList = matchBean.playerList;
                matchlistBean1.starttime = matchBean.starttime;
                matchlistBean1.status = matchBean.status;
                matchlistBean1.timezoneoffset = matchBean.timezoneoffset;

            }
            //回调
            onRefreshCallBack.onRefreshOk(matchTabBean);
        }catch (Exception e){

        }
    }


    public static void getDataForHomeItemDetail(final HomeItemBean matchDetailBean, final OnRefreshCallBack onRefreshCallBack){
        if(matchDetailBean == null||matchDetailBean.data == null)return;
        if(matchDetailBean.data.match.status.equals("NO_START")){
            Long time = Long.valueOf(matchDetailBean.data.match.timezoneoffset + "000");
            Integer integer = Integer.valueOf(Utils.getMinStart(time));
            if(integer <0) {
                Integer delt = Math.abs(integer);
                if (delt >= 30) return;
            }
        }
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListStatus");
//        String ids = parseBean(immediateScoreBean);
        params.put("value",matchDetailBean.data.match.betId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans = JsonParseUtils.parseJsonRefreshList(string, ImmediateScoreBean.DataBean.MatchlistBean.MatchBean.class);
                delDataForHomeItemDetail(matchDetailBean,refreshBeans,onRefreshCallBack);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }
    private static void delDataForHomeItemDetail(HomeItemBean matchDetailBean, ArrayList<ImmediateScoreBean.DataBean.MatchlistBean.MatchBean> refreshBeans, OnRefreshCallBack onRefreshCallBack) {
        for(int i = 0;i<refreshBeans.size();i++){
            ImmediateScoreBean.DataBean.MatchlistBean.MatchBean matchBean = refreshBeans.get(i);
            HomeItemBean.DataBean.MatchBean match = matchDetailBean.data.match;
            /**
             * awayCorner: "4", // 客队角球
             awayHalfScore: "0", // 客队上半场比分
             awayRed: "0", // 客队红牌
             awayScore: "0", // 客队比分
             awayYellow: "1", // 客队黄牌
             betId: "1406341", // 比赛 id
             hostCorner: "0",  // 主队角球
             hostHalfScore: "0", // 主队上半场比分
             hostRed: "0",  // 主队红牌
             hostScore: "1", // 主队比分
             hostYellow: "0", // 主队 黄牌
             playerList: 0, // 是否有阵容,  赛前显示
             starttime: "1510304180", // 上半场开始时间 or 下半场开始时间
             status: "FIRST_HALF", // 比赛状态
             timezoneoffset: "1510303800" // 比赛开始时间
             */
//            match.awayCorner = matchBean.awayCorner+"";
            match.awayHalfScore = matchBean.awayHalfScore;
//            match.awayRedCard = matchBean.awayRed;
            match.awayScore = matchBean.awayScore;
//            match.awayYellowCard = matchBean.awayYellow;
            match.betId = matchBean.betId;
//            match.hostCorner = matchBean.hostCorner;
            match.hostHalfScore = matchBean.hostHalfScore;
//            match.hostRedCard = matchBean.hostRed;
            match.hostScore = matchBean.hostScore;

//            match.hostYellowCard = matchBean.hostYellow;
//            match.playerList = matchBean.playerList;
//            match.timezoneoffset = matchBean.starttime;
            match.status = matchBean.status;
            match.timezoneoffset = matchBean.starttime+"";

        }
        //回调
        onRefreshCallBack.onRefreshOk(matchDetailBean);
    }

}
