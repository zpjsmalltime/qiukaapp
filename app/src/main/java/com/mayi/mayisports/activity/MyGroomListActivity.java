package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
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
import com.mayisports.qca.bean.MyGroomType0Bean;
import com.mayisports.qca.bean.MyGroomType1Bean;
import com.mayisports.qca.bean.MyGroomType2Bean;
import com.mayisports.qca.utils.BindViewUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 增加我的推荐页面
 */
public class MyGroomListActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener, AdapterView.OnItemClickListener {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,MyGroomListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_my_groom_list;
    }


    private TextView tv_groom_match_detail;
    private TextView tv_analyes_match_detail;
    private TextView tv_price_match_detail;
    private View iv_groom_match_detail;
    private View iv_analyes_match_detail;
    private View iv_price_match_detail;

    private ListView lv_my_groom_ac;
    private LinearLayout ll_no_data;
    private RelativeLayout rl_load_layout;


    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title.setText("我的推荐");
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_groom_match_detail =  findViewById(R.id.tv_groom_match_detail);
        tv_analyes_match_detail =  findViewById(R.id.tv_analyes_match_detail);
        tv_price_match_detail =  findViewById(R.id.tv_price_match_detail);
        iv_groom_match_detail =  findViewById(R.id.iv_groom_match_detail);
        iv_analyes_match_detail =  findViewById(R.id.iv_analyes_match_detail);
        iv_price_match_detail =  findViewById(R.id.iv_price_match_detail);
        tv_groom_match_detail.setOnClickListener(this);
        tv_analyes_match_detail.setOnClickListener(this);
        tv_price_match_detail.setOnClickListener(this);


        lv_my_groom_ac = findViewById(R.id.lv_my_groom_ac);
        lv_my_groom_ac.setOnItemClickListener(this);
        ll_no_data = findViewById(R.id.ll_no_data);
        rl_load_layout = findViewById(R.id.rl_load_layout);

        initHeader();

    }

    private View typeTop0Header;

    private View typeTop1Header;
    private TextView tv_left_top_header;
    private TextView tv_mid_top_header;
    private TextView tv_right_top_header;
    private LinearLayout ll_report_header;
    private TextView tv_left_header;
    private TextView tv_mid_header;
    private TextView tv_right_header;

    private View typeTop2Header;
    private ImageView iv_header_item;
    private ImageView iv_vip_item;
    private TextView tv_name_item;
    private TextView tv_reason_item;
    private TextView tv_price_header;


    private void initHeader() {
        typeTop0Header = View.inflate(QCaApplication.getContext(),R.layout.type_1_header,null);

        typeTop1Header = View.inflate(QCaApplication.getContext(),R.layout.type_2_header,null);
        tv_left_top_header = typeTop1Header.findViewById(R.id.tv_left_top_header);
        tv_mid_top_header = typeTop1Header.findViewById(R.id.tv_mid_top_header);
        tv_right_top_header = typeTop1Header.findViewById(R.id.tv_right_top_header);
        ll_report_header = typeTop1Header.findViewById(R.id.ll_report_header);
        tv_left_header = typeTop1Header.findViewById(R.id.tv_left_header);
        tv_left_header.setOnClickListener(this);
        tv_mid_header = typeTop1Header.findViewById(R.id.tv_mid_header);
        tv_mid_header.setOnClickListener(this);
        tv_right_header = typeTop1Header.findViewById(R.id.tv_right_header);
        tv_right_header.setOnClickListener(this);

        typeTop2Header = View.inflate(QCaApplication.getContext(),R.layout.type_3_header,null);
        iv_header_item = typeTop2Header.findViewById(R.id.iv_header_item);
        iv_vip_item = typeTop2Header.findViewById(R.id.iv_vip_item);
        tv_name_item = typeTop2Header.findViewById(R.id.tv_name_item);
        tv_reason_item = typeTop2Header.findViewById(R.id.tv_reason_item);
        tv_price_header = typeTop2Header.findViewById(R.id.tv_price_header);

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        onClick(tv_groom_match_detail);
    }

    private int typeTop;
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_groom_match_detail:
                typeTop = 0;
                selectOne(typeTop);
                requestData();
                break;
            case R.id.tv_analyes_match_detail:
                typeTop = 1;
                selectOne(typeTop);
                requestData();
                break;
            case R.id.tv_price_match_detail:
                typeTop = 2;
                selectOne(typeTop);
                requestData();
                break;
            case R.id.tv_left_header:
                ll_report_header.setBackgroundResource(R.drawable.fangandan_3_left);
                myTopType1Adapter.setDataType(0);
                myTopType1Adapter.notifyDataSetChanged();
                break;
            case R.id.tv_mid_header:
                ll_report_header.setBackgroundResource(R.drawable.fangandan_3_m);
                myTopType1Adapter.setDataType(1);
                myTopType1Adapter.notifyDataSetChanged();
                break;
            case R.id.tv_right_header:
                ll_report_header.setBackgroundResource(R.drawable.fangandan_3_righ);
                myTopType1Adapter.setDataType(2);
                myTopType1Adapter.notifyDataSetChanged();
                break;
        }
    }

    private int page;
    private int pre_page;

    private MyGroomType0Bean myGroomType0Bean;
    private MyGroomType1Bean myGroomType1Bean;
    private MyGroomType2Bean myGroomType2Bean;
    private void requestData() {

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        switch (typeTop){
            case 0:
              //  http://app.mayisports.com/php/api.php?action=user_recommendation&type=list&start=0
                params.put("action","user_recommendation");
                params.put("type","list");
                params.put("start",page);

                break;
            case 1:
                page = 0;
                //http://app.mayisports.com/php/api.php?action=user_recommendation&type=statics
                params.put("action","user_recommendation");
                params.put("type","statics");
                break;
            case 2:
                page = 0;
                //http://app.mayisports.com/php/api.php?action=mybet&type=mylist
                params.put("action","mybet");
                params.put("type","mylist");
                break;
        }
        RequestNetWorkUtils.kjHttp.cancleAll();
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                switch (typeTop){
                    case 0:
                        removeHeaders();
                        delTopType0(string);
                        break;
                    case 1:
                         removeHeaders();
                         myGroomType1Bean = JsonParseUtils.parseJsonClass(string,MyGroomType1Bean.class);
                         lv_my_groom_ac.addHeaderView(typeTop1Header);
                         updateTypeTop1Header();
                         lv_my_groom_ac.setAdapter(myTopType1Adapter);
                         onClick(tv_left_header);
                        break;
                    case 2:
                        removeHeaders();
                        myGroomType2Bean = JsonParseUtils.parseJsonClass(string,MyGroomType2Bean.class);
                        lv_my_groom_ac.addHeaderView(typeTop2Header);
                        updateTypeTop2Header();
                        lv_my_groom_ac.setAdapter(myTopType2Adapter);
                        break;
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });

    }



    /**
     * 解析 toptype0 网络数据
     * @param string
     */
    private void delTopType0(String string) {
        if(page == 0){
             myGroomType0Bean = JsonParseUtils.parseJsonClass(string,MyGroomType0Bean.class);
             lv_my_groom_ac.setAdapter(myTopType0Adapter);
             pre_page = 1;
        }else{
            try{
                MyGroomType0Bean beanNew = JsonParseUtils.parseJsonClass(string, MyGroomType0Bean.class);
                myGroomType0Bean.data.addAll(beanNew.data);
            }catch (Exception e){
                pre_page = 0;
            }
           myTopType0Adapter.notifyDataSetChanged();
        }


    }

    private void updateTypeTop1Header() {
        if(myGroomType1Bean == null || myGroomType1Bean.data == null )return;
        if(myGroomType1Bean.data.total != null){
            MyGroomType1Bean.DataBean.TotalBean total = myGroomType1Bean.data.total;
            //总场数
            tv_left_top_header.setText(total.all_bet_count+"");
            //命中率
            String mid = Utils.parsePercent(total.all_bet_accuracy, total.all_bet_count);
            tv_mid_top_header.setText(mid);

            //回报率
            String right = Utils.parsePercent(total.all_bet_revenue, total.all_bet_count);
            tv_right_top_header.setText(right);
        }

    }
    /**
     * 更新type2 头部
     */
    private void updateTypeTop2Header() {
        if(myGroomType2Bean == null || myGroomType2Bean.data == null)return;
        if(myGroomType2Bean.data.user != null){
            MyGroomType2Bean.DataBean.UserBean user = myGroomType2Bean.data.user;
            PictureUtils.showCircle(user.weibo_headurl,iv_header_item);
            tv_name_item.setText(user.nickname);
            if(!TextUtils.isEmpty(user.verify_type)&&Integer.valueOf(user.verify_type)>0){
                iv_vip_item.setVisibility(View.VISIBLE);
            }else {
                iv_vip_item.setVisibility(View.GONE);
            }
        }
        if(myGroomType2Bean.list != null){
            tv_reason_item.setText("参赛"+myGroomType2Bean.list.size()+"期");
        }else{
            tv_reason_item.setText("未参赛");
        }

        if(myGroomType2Bean.statics != null){

                double v = myGroomType2Bean.statics.bonus % 1;
                if(v>0) {
                    tv_price_header.setText(myGroomType2Bean.statics.bonus + "元");
                }else{
                    int intBonus = (int) myGroomType2Bean.statics.bonus;
                    tv_price_header.setText(intBonus + "元");
                }
        }

    }

    private void removeHeaders() {
        lv_my_groom_ac.removeHeaderView(typeTop0Header);
        lv_my_groom_ac.removeHeaderView(typeTop1Header);
        lv_my_groom_ac.removeHeaderView(typeTop2Header);
    }

    /**
     * 刷新顶部tab状态
     * @param postion
     */
    private void selectOne(int postion){

        tv_groom_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_groom_match_detail.setVisibility(View.INVISIBLE);

        tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_analyes_match_detail.setVisibility(View.INVISIBLE);

        tv_price_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_price_match_detail.setVisibility(View.INVISIBLE);


        switch (postion){
            case 0:
                tv_groom_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_groom_match_detail.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_analyes_match_detail.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_price_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_price_match_detail.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onReload() {

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(typeTop == 2){//参赛记录
            if(position == 0)return;
            String match_index = myGroomType2Bean.list.get(position-1).match_index;
            WebViewActivtiy.start(this,Constant.BASE_URL+"#/weekScoreShare/"+match_index,"每周球咖战报");
        }else if(typeTop == 0){
            MyGroomType0Bean.DataBean dataBean = myGroomType0Bean.data.get(position);
            HomeItemDetailActivity.start(this,dataBean.user.user_id,dataBean.match.betId);
        }
    }

    private MyTopType0Adapter myTopType0Adapter = new MyTopType0Adapter();
    class MyTopType0Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(myGroomType0Bean != null && myGroomType0Bean.data != null){
                int size = myGroomType0Bean.data.size();
                if (size == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                }else{
                    ll_no_data.setVisibility(View.GONE);
                }
                return size;
            }
            ll_no_data.setVisibility(View.VISIBLE);
            return 0;
        }

        @Override
        public MyGroomType0Bean.DataBean getItem(int position) {
            return myGroomType0Bean.data.get(position);

        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyTopType0Holder myTopType0Holder;
            if(convertView == null){
                myTopType0Holder = new MyTopType0Holder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.my_groom_type0_item,null);
                myTopType0Holder.tv_match_type_item = convertView.findViewById(R.id.tv_match_type_item);
                myTopType0Holder.tv_day_item = convertView.findViewById(R.id.tv_day_item);
                myTopType0Holder.tv_time_item = convertView.findViewById(R.id.tv_time_item);
                myTopType0Holder.tv_host_name_item = convertView.findViewById(R.id.tv_host_name_item);
                myTopType0Holder.tv_vs_score_item = convertView.findViewById(R.id.tv_vs_score_item);
                myTopType0Holder.tv_away_name_item = convertView.findViewById(R.id.tv_away_name_item);
                myTopType0Holder.tv_handpic_type_item = convertView.findViewById(R.id.tv_handpic_type_item);
                myTopType0Holder.tv_name_tape_item = convertView.findViewById(R.id.tv_name_tape_item);
                myTopType0Holder.tv_tape_item = convertView.findViewById(R.id.tv_tape_item);
                myTopType0Holder.iv_result_item = convertView.findViewById(R.id.iv_result_item);
                myTopType0Holder.tv_price_item = convertView.findViewById(R.id.tv_price_item);
                myTopType0Holder.tv_sale_count_item = convertView.findViewById(R.id.tv_sale_count_item);
                myTopType0Holder.tv_revenue_item = convertView.findViewById(R.id.tv_revenue_item);
                myTopType0Holder.tv_view_count_item = convertView.findViewById(R.id.tv_view_count_item);

                myTopType0Holder.tv_status_item = convertView.findViewById(R.id.tv_status_item);
                convertView.setTag(myTopType0Holder);
            }else{
                myTopType0Holder = (MyTopType0Holder) convertView.getTag();
            }

            MyGroomType0Bean.DataBean item = getItem(position);

            myTopType0Holder.tv_match_type_item.setText(item.match.leagueName);

            Long aLong = Long.valueOf(item.match.timezoneoffset + "000");

            String time = Utils.simpleDateFormatMMDD.format(new Date(aLong));
            myTopType0Holder.tv_day_item.setText(time.split(" ")[0]);
            myTopType0Holder.tv_time_item.setText(time.split(" ")[1]);

            myTopType0Holder.tv_host_name_item.setText(item.match.hostTeamName);

            if(Constant.MATCH_NO_START.equals(item.match.status)) {
                myTopType0Holder.tv_vs_score_item.setText("vs");
                myTopType0Holder.tv_vs_score_item.setTextColor(getResources().getColor(R.color.coment_black_99));

            }else{
                String vsScore = item.match.hostScore + " : " + item.match.awayScore;
                myTopType0Holder.tv_vs_score_item.setText(vsScore);
                myTopType0Holder.tv_vs_score_item.setTextColor(getResources().getColor(R.color.red_));
            }

            myTopType0Holder.tv_away_name_item.setText(item.match.awayTeamName);


           String oddsType =  parseOddsType(item.recommendation.betDetail);
           myTopType0Holder.tv_handpic_type_item.setText(oddsType);

            /**
             * 设置推荐结果
             */
           setOddsNumber(item,myTopType0Holder.tv_name_tape_item,myTopType0Holder.tv_tape_item);


           myTopType0Holder.tv_status_item.setVisibility(View.GONE);
           myTopType0Holder.iv_result_item.setVisibility(View.GONE);
           if(Constant.MATCH_COMPLETE.equals(item.match.status)){
               myTopType0Holder.iv_result_item.setVisibility(View.VISIBLE);
               ViewStatusUtils.parseStatusForImg(item.recommendation.revenueStr,myTopType0Holder.iv_result_item);
           }else{
               myTopType0Holder.tv_status_item.setVisibility(View.VISIBLE);
           }

           myTopType0Holder.tv_price_item.setText("单价:"+item.recommendation.price);
           myTopType0Holder.tv_sale_count_item.setText("销量:"+item.recommendation.sales);
           myTopType0Holder.tv_revenue_item.setText("收益:"+item.recommendation.revenue);
           myTopType0Holder.tv_view_count_item.setText("阅读:"+item.recommendation.display_by_share);



            if(position == getCount()-3||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestData();
                }
            }
            return convertView;
        }
    }


    /**
     * 设置推荐结果
     * @param item
     * @param tv_name_tape_item
     * @param tv_tape_item
     */
    private DecimalFormat df = new DecimalFormat("#.00");
    private void setOddsNumber(MyGroomType0Bean.DataBean item, TextView tv_name_tape_item, TextView tv_tape_item) {
        MyGroomType0Bean.DataBean.RecommendationBean.BetDetailBean betDetail = item.recommendation.betDetail;
        if(betDetail.hostOdds != null){
            String s = Utils.parseOddOfHandicap(betDetail.tape);
            tv_name_tape_item.setText(item.match.hostTeamName+","+s);
            String format = df.format(Double.valueOf(betDetail.hostOdds)/10000 + 1);
            tv_tape_item.setText(format);
        }else if(betDetail.awayOdds != null){
            String a = "";
            if(betDetail.tape.contains("-")){
                a = betDetail.tape;
                a = a.replace("-", "");
            }else{
                a = "-"+betDetail.tape;
            }

            String s = Utils.parseOddOfHandicap(a);
            tv_name_tape_item.setText(item.match.awayTeamName+","+s);
            String format = df.format(Double.valueOf(betDetail.awayOdds)/10000 + 1);
            tv_tape_item.setText(format);
        }else if(betDetail.winOdds != null){
            tv_name_tape_item.setText("主胜");
            tv_tape_item.setText(df.format(Double.valueOf(betDetail.winOdds)/10000));
        }else if(betDetail.drowOdds != null){
            tv_name_tape_item.setText("平局");
            tv_tape_item.setText(df.format(Double.valueOf(betDetail.drowOdds)/10000));
        }else if(betDetail.loseOdds != null){
            tv_name_tape_item.setText("客胜");
            tv_tape_item.setText(df.format(Double.valueOf(betDetail.loseOdds)/10000));
        }else if(betDetail.bigScoreOdds != null){
            tv_name_tape_item.setText("大于"+Utils.parseOddOfHandicap(betDetail.tape));
            tv_tape_item.setText(df.format(Double.valueOf(betDetail.bigScoreOdds)/10000+1));
        }else if(betDetail.smallScoreOdds != null){
            tv_name_tape_item.setText("小于"+Utils.parseOddOfHandicap(betDetail.tape));
            tv_tape_item.setText(df.format(Double.valueOf(betDetail.smallScoreOdds)/10000+1));
        }
    }

    /**
     * 解析盘口类型
     * @param betDetail
     * @return
     */
    private String parseOddsType(MyGroomType0Bean.DataBean.RecommendationBean.BetDetailBean betDetail) {
        if(betDetail.hostOdds != null || betDetail.awayOdds != null){
            return "亚盘";
        }else if(betDetail.winOdds != null || betDetail.drowOdds != null || betDetail.loseOdds != null){
            return "欧盘";
        }else if(betDetail.bigScoreOdds != null || betDetail.smallScoreOdds != null){
            return "大小球";
        }

        return "";
    }

    private static class MyTopType0Holder{
        public TextView tv_match_type_item;
        public TextView tv_day_item;
        public TextView tv_time_item;
        public TextView tv_host_name_item;
        public TextView tv_vs_score_item;
        public TextView tv_away_name_item;
        public TextView tv_handpic_type_item;
        public TextView tv_name_tape_item;
        public TextView tv_tape_item;
        public ImageView iv_result_item;
        public TextView tv_price_item;
        public TextView tv_sale_count_item;
        public TextView tv_revenue_item;
        public TextView tv_view_count_item;

        public TextView tv_status_item;


    }

    private MyTopType1Adapter myTopType1Adapter = new MyTopType1Adapter();
    class MyTopType1Adapter extends BaseAdapter{

        private int dataType;
        public void setDataType(int dataType){
            this.dataType = dataType;
        }
        @Override
        public int getCount() {
            switch (dataType){
                case 0:
                    if(myGroomType1Bean != null && myGroomType1Bean.data != null && myGroomType1Bean.data.day != null){
                        int size = myGroomType1Bean.data.day.size();
                        return size;
                    }
                    break;
                case 1:
                    if(myGroomType1Bean != null && myGroomType1Bean.data != null && myGroomType1Bean.data.week != null){
                        int size = myGroomType1Bean.data.week.size();
                        return size;
                    }
                    break;
                case 2:
                    if(myGroomType1Bean != null && myGroomType1Bean.data != null && myGroomType1Bean.data.month != null){
                        int size = myGroomType1Bean.data.month.size();
                        return size;
                    }
                    break;
            }
            return 0;
        }

        @Override
        public MyGroomType1Bean.DataBean.DayBean getItem(int position) {
            switch (dataType){
                case 0:
                    return myGroomType1Bean.data.day.get(position);
                case 1:
                    return myGroomType1Bean.data.week.get(position);
                case 2:
                    return myGroomType1Bean.data.month.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyType2Holder myType2Holder;
            if(convertView == null){
                myType2Holder = new MyType2Holder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.my_groom_type_item,null);
                myType2Holder.tv0 = convertView.findViewById(R.id.tv0);
                myType2Holder.tv1 = convertView.findViewById(R.id.tv1);
                myType2Holder.tv2 = convertView.findViewById(R.id.tv2);
                myType2Holder.tv3 = convertView.findViewById(R.id.tv3);
                convertView.setTag(myType2Holder);
            }else{
                myType2Holder = (MyType2Holder) convertView.getTag();
            }

            MyGroomType1Bean.DataBean.DayBean item = getItem(position);
            if(item != null){
                if(dataType == 0) {
                    item.date = item.date.replace("年", "/").replace("月", "/").replace("日", "");
                }else{
                    item.date = item.date.replace("年", "/").replace("月", "");

                }
                myType2Holder.tv0.setText(item.date);
                myType2Holder.tv1.setText(item.count+"");
                String tv2 = Utils.parsePercent(item.accuracy, item.count);
                myType2Holder.tv2.setText(tv2);


                if(item.revenue>=item.count){
                    myType2Holder.tv3.setTextColor(getResources().getColor(R.color.red_));
                }else{
                    myType2Holder.tv3.setTextColor(getResources().getColor(R.color.green));
                }

                String tv3 = Utils.parsePercent(item.revenue, item.count);
                myType2Holder.tv3.setText(tv3);


            }
            return convertView;
        }
    }

    static class MyType2Holder{
        public TextView tv0;
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;

        public TextView tv2Top;
        public TextView tv2Bottom;
    }

    private MyTopType2Adapter myTopType2Adapter = new MyTopType2Adapter();
    class MyTopType2Adapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(myGroomType2Bean != null && myGroomType2Bean.list != null){
                int size = myGroomType2Bean.list.size();
                return size;
            }
            return 0;
        }

        @Override
        public MyGroomType2Bean.ListBean getItem(int position) {
            return  myGroomType2Bean.list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyType2Holder myType2Holder;
            if(convertView == null){
                myType2Holder = new MyType2Holder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.my_groom_type2_item,null);
                myType2Holder.tv0 = convertView.findViewById(R.id.tv0);
                myType2Holder.tv1 = convertView.findViewById(R.id.tv1);
                myType2Holder.tv2Top = convertView.findViewById(R.id.tv2Top);
                myType2Holder.tv2Bottom = convertView.findViewById(R.id.tv2Bottom);
                myType2Holder.tv3 = convertView.findViewById(R.id.tv3);
                convertView.setTag(myType2Holder);
            }else{
                myType2Holder = (MyType2Holder) convertView.getTag();
            }

            MyGroomType2Bean.ListBean item = getItem(position);

            myType2Holder.tv0.setText(item.match_index);
            myType2Holder.tv1.setText(item.rank);
            try{
                int aDouble = Integer.valueOf(item.bet_count);
                myType2Holder.tv2Bottom.setText("竞猜"+aDouble+"场");

                String s = Utils.parsePercent(Double.valueOf(item.bet_revenue),aDouble);
                myType2Holder.tv2Top.setText(s);

            }catch (Exception e){
                myType2Holder.tv2Top.setText("---");
                myType2Holder.tv2Bottom.setText("---");
            }

            myType2Holder.tv3.setText(item.bonus);

            return convertView;
        }
    }
}
