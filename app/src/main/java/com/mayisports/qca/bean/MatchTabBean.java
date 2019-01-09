package com.mayisports.qca.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.TextureView;
import android.widget.TextView;

import com.mayisports.qca.utils.Utils;

import java.util.Date;
import java.util.List;

/**
 * 比赛头部联赛bean
 * Created by Zpj on 2018/1/25.
 */

public class MatchTabBean {

    /**%B5%A6%E7%94%B2","round":"21","season":"2017","hostTeamName":"%E8
     */

    public StatusBean status;
    public DataBean data;
    public String tagConvert;
    public List<Integer> positions;

    public static class StatusBean {
        /**
         * login : 0
         * id : 0
         */

        public int login;
        public String id;
    }

    public static class DataBean {
        public List<List<String>> tagList;
        public List<MatchBean> match;
        public String tag;

        public static class MatchBean{
            /**
             * betId : 1446340
             * timezoneoffset : 1516640400
             * leagueId : 137
             * leagueName : %E5%A1%9E%E6%B5%A6%E7%94%B2
             * round : 21
             * season : 2017
             * hostTeamName : %E8%89%BE%E7%B1%B3%E6%96%AF
             * hostTeamId : 5782
             * awayTeamName : %E5%A5%A5%E6%9E%97%E6%AF%94%E5%85%8B
             * awayTeamId : 1384
             * hostScore : 4
             * awayScore : 0
             * hostHalfScore : 4
             * awayHalfScore : 0
             * hostRank : 11
             * awayRank : 12
             * status : COMPLETE
             * cup : 0
             * color : #cc9900
             * tape : 7500
             * logoH : 0
             * logoG : 0
             * starttime : 1516644440
             * hostRedCard : 0
             * awayRedCard : 1
             * hostYellowCard : 2
             * awayYellowCard : 2
             * hostCorner : 10
             * awayCorner : 4
             * collection_status : 0
             * type : 1
             * date : 20180123 1:00
             * match_information_count : 7
             * tvlink : 1
             * tvlinkList : ["Firstrow直播","Feed2all直播"]
             */

            public String tag;


            public String betId;
            public String timezoneoffset;
            public String leagueId;
            public String leagueName;
            public String round;
            public String season;
            public String hostTeamName;
            public String hostTeamId;
            public String awayTeamName;
            public String awayTeamId;
            public String hostScore;
            public String awayScore;
            public String hostHalfScore;
            public String awayHalfScore;
            public String hostRank;
            public String awayRank;
            public String status;
            public String cup;
            public String color;
            public String tape;
            public int logoH;
            public int logoG;
            public long starttime;
            public String hostRedCard;
            public String awayRedCard;
            public String hostYellowCard;
            public String awayYellowCard;
            public String hostCorner;
            public String awayCorner;
            public int collection_status;
            public int type;
            public String date;
            public String match_information_count;
            public int tvlink;
            public List<String> tvlinkList;
            public String text1;
            public boolean isShowTitle;

            public String strTitle;

            public boolean first;
            public int playerList;

            public String getStrTitle(){
                if(strTitle != null){
                    return strTitle;
                }
                Long aLong = Long.valueOf(timezoneoffset + "000");
                String weekOfDate = Utils.getWeekOfDate(new Date(aLong));
                String dayTommowYesterdy = Utils.getDayTommowYesterdy(aLong);

                String format = Utils.simpleDateFormatSplitLine.format(aLong);
                if(TextUtils.isEmpty(dayTommowYesterdy)){
                    weekOfDate = format +"\t"+weekOfDate;
                }else{
                    weekOfDate = format +"\t"+dayTommowYesterdy;
                }
                strTitle = weekOfDate;
                return strTitle;
            }


        }
    }
}
