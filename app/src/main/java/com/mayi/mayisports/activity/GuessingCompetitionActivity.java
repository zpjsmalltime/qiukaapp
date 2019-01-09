package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.GuessCompetitionBean;
import com.mayisports.qca.bean.GuessToastItemBean;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.GuessCompetPopuWindow;
import com.mayisports.qca.view.SharePopuwind;

import org.kymjs.kjframe.http.HttpParams;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

/**
 * 竞猜大赛界面
 */

public class GuessingCompetitionActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, GuessCompetPopuWindow.OnItemCheck, PlatformActionListener {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,GuessingCompetitionActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_guessing_competition;
    }

    private ListView lv_guess_compet_activity;
    private ViewGroup rl_load_layout;
    private FrameLayout fl_btn_guess_item;
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("免费竞猜");
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lv_guess_compet_activity = findViewById(R.id.lv_guess_compet_activity);
        rl_load_layout = findViewById(R.id.rl_load_layout);

        iv_right_title.setVisibility(View.VISIBLE);
        iv_right_title.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
        iv_right_title.setOnClickListener(this);

        addHeader();

        lv_guess_compet_activity.setOnItemClickListener(this);
        fl_btn_guess_item = findViewById(R.id.fl_btn_guess_item);
        fl_btn_guess_item.setOnClickListener(this);

    }

    GuessCompetPopuWindow guessCompetPopuWindow;
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.fl_btn_guess_item:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
                    return;
                }
                PublishGroomActivity.start(this);
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/selectMatch/2","发布推荐");
                break;

            case R.id.tv_date_header:
                if(guessCompetPopuWindow == null && guessToastItemBean != null){
                    guessCompetPopuWindow = new GuessCompetPopuWindow(this,guessToastItemBean.data,this);
                }

                if(guessToastItemBean != null) {
                    guessCompetPopuWindow.showAtLocation(lv_guess_compet_activity, Gravity.CENTER, 0, 0);
                }

                break;

            case R.id.fl_0_header://红人榜
                WebViewActivtiy.start(this,Constant.BASE_URL+"#/rank","红人榜");
                break;
            case R.id.fl_1_header://我的推荐
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/myRecommendation","我的推荐");
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(this, "");
                    return;
                }
                MyGroomListActivity.start(this);
                break;
            case R.id.iv_right_title:
                toShare();
                break;
        }
    }


    private void toShare(){
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {

                    ShareUtils.shareMsgForGuessing(type,"",GuessingCompetitionActivity.this);
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_guess_compet_activity, Gravity.CENTER, 0, 0);
    }

    private ImageView iv_header;
    private FrameLayout fl_0_header;
    private FrameLayout fl_1_header;
    private TextView tv_date_header;
    private LinearLayout ll_lingjiang_guess_comp;
    private TextView tv_text_guess_comp;
    private TextView tv_lingjiang_guess_comp;
    private  MyHolder myHolder;
    private View v_mine_item;
    private LinearLayout ll_lingjaing_container;
    private void addHeader() {
        View view = View.inflate(this,R.layout.guess_comp_header_layout,null);
        iv_header = view.findViewById(R.id.iv_header);
        fl_0_header = view.findViewById(R.id.fl_0_header);
        fl_0_header.setOnClickListener(this);
        fl_1_header = view.findViewById(R.id.fl_1_header);
        fl_1_header.setOnClickListener(this);
        tv_date_header = view.findViewById(R.id.tv_date_header);
        tv_date_header.setOnClickListener(this);
        ll_lingjaing_container = view.findViewById(R.id.ll_lingjaing_container);

        ll_lingjiang_guess_comp = view.findViewById(R.id.ll_lingjiang_guess_comp);
        tv_text_guess_comp = view.findViewById(R.id.tv_text_guess_comp);
        tv_lingjiang_guess_comp = view.findViewById(R.id.tv_lingjiang_guess_comp);
        v_mine_item = view.findViewById(R.id.v_mine_item);
        v_mine_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuessingCompetitionActivity.this, PersonalDetailActivity.class);
                intent.putExtra("id",guessCompetitionBean.data.user.user_id+"");
                startActivity(intent);
            }
        });



        myHolder = new MyHolder();
        myHolder.tv_count_guess_item = view.findViewById(R.id.tv_count_guess_item);
        myHolder.iv_head_dynamic_item = view.findViewById(R.id.iv_head_dynamic_item);
        myHolder.iv_vip_header = view.findViewById(R.id.iv_vip_header);
        myHolder.iv_red_point_header = view.findViewById(R.id.iv_red_point_header);
        myHolder.tv_name_guess_item = view.findViewById(R.id.tv_name_guess_item);
        myHolder.tv_tag_guess_item = view.findViewById(R.id.tv_tag_guess_item);
        myHolder.tv_per_guess_item = view.findViewById(R.id.tv_per_guess_item);
        myHolder.tv_price_guess_item = view.findViewById(R.id.tv_price_guess_item);



        lv_guess_compet_activity.addHeaderView(view);
        View view1 = View.inflate(QCaApplication.getContext(),R.layout.text_footer,null);
        lv_guess_compet_activity.addFooterView(view1);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestNetDatas();
    }

    private String value = "";
    private GuessCompetitionBean guessCompetitionBean;
    private String firstIndex;
    private void requestNetDatas() {
        //htp://app.mayisports.com/php/api.php?action=mybet&type=week_ranking_list&value=
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","mybet");
        params.put("type","week_ranking_list");
        params.put("value",value);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                guessCompetitionBean = JsonParseUtils.parseJsonClass(string,GuessCompetitionBean.class);


                if(guessToastItemBean == null) {
                    firstIndex = guessCompetitionBean.data.bet_match.match_index;
                    updateHeader();
                    requestToastItem();
                    lv_guess_compet_activity.setAdapter(myAdapter);
                }else{
                    updateHeader();
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private GuessToastItemBean guessToastItemBean;

    private void requestToastItem() {
        //htt//20180109.dldemo.applinzi.com/php/api.php?action=mybet&type=match_index_list
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","mybet");
        params.put("type","match_index_list");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                guessToastItemBean = JsonParseUtils.parseJsonClass(string,GuessToastItemBean.class);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private void updateHeader() {
        String ranking_date_range = guessCompetitionBean.data.bet_match.ranking_date_range;
        String match_index = guessCompetitionBean.data.bet_match.match_index;
        if(match_index.equals(firstIndex)) {
            String str = "即时榜单(" + ranking_date_range + ")";
            tv_date_header.setText(str);
        }else {
            String str = "第" + match_index + "期(" + ranking_date_range + ")";
            tv_date_header.setText(str);
        }


        GuessCompetitionBean.DataBean.UserBean user = guessCompetitionBean.data.user;
        if(user == null){
            v_mine_item.setVisibility(View.GONE);
        }else{
        GuessCompetitionBean.DataBean.RanklistBean ranklistBean = new GuessCompetitionBean.DataBean.RanklistBean();
        ranklistBean.accuracy_text = user.accuracy_text;
        ranklistBean.bet_count = user.bet_count;
        ranklistBean.bonus = user.bonus;
        ranklistBean.revenue = user.value;
        ranklistBean.nickname = user.nickname;
        ranklistBean.rank = user.rank;
        ranklistBean.pre_rank = user.pre_rank;
        ranklistBean.headurl = user.weibo_headurl;
        ranklistBean.user_id = user.user_id;
        ranklistBean.verify_type = user.verify_type;
            bindView(myHolder, ranklistBean);
        }

        if(guessCompetitionBean.data.bonuslist != null){
            ll_lingjiang_guess_comp.setVisibility(View.VISIBLE);
            ll_lingjaing_container.removeAllViews();
            for(int i = 0;i< guessCompetitionBean.data.bonuslist.size();i++){
                final GuessCompetitionBean.DataBean.BonusList list = guessCompetitionBean.data.bonuslist.get(i);

                View view = View.inflate(QCaApplication.getContext(),R.layout.lingjiang_layout,null);
                TextView tv = view.findViewById(R.id.tv_text_guess_comp);
                String st = "恭喜！您在"+list.match_index+"期大赛中排名第"+ list.rank+"名！\n赢取"+list.bonus+"元现金奖励";
                tv.setText(st);

                TextView tv_lingjiang =  view.findViewById(R.id.tv_lingjiang_guess_comp);
                tv_lingjiang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivtiy.start(GuessingCompetitionActivity.this,Constant.BASE_URL+"#/weekScoreShare/"+list.match_index,"每周球咖战报");
                    }
                });

                ll_lingjaing_container.addView(view);
            }
        }else{
            ll_lingjiang_guess_comp.setVisibility(View.GONE);
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0 || position == guessCompetitionBean.data.ranklist.size()+1)return;
        GuessCompetitionBean.DataBean.RanklistBean bean = guessCompetitionBean.data.ranklist.get(position-1);
        Intent intent = new Intent(this, PersonalDetailActivity.class);
        intent.putExtra("id",bean.user_id+"");
        startActivityForResult(intent,1);
    }

    @Override
    public void onReload() {

    }


    private MyAdapter myAdapter = new MyAdapter();

    @Override
    public void onItemCheck(GuessToastItemBean.DataBean bean) {
        value = bean.match_index;
        requestNetDatas();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.toastNoStates("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.toastNoStates("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.toastNoStates("分享取消");

    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(guessCompetitionBean != null && guessCompetitionBean.data != null&&guessCompetitionBean.data.ranklist != null){
                return guessCompetitionBean.data.ranklist.size();
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
            MyHolder myHolder;
            if(convertView == null){
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.guess_comp_item_layout,null);
                myHolder.tv_count_guess_item = convertView.findViewById(R.id.tv_count_guess_item);
                myHolder.iv_head_dynamic_item = convertView.findViewById(R.id.iv_head_dynamic_item);
                myHolder.iv_vip_header = convertView.findViewById(R.id.iv_vip_header);
                myHolder.iv_red_point_header = convertView.findViewById(R.id.iv_red_point_header);
                myHolder.tv_name_guess_item = convertView.findViewById(R.id.tv_name_guess_item);
                myHolder.tv_tag_guess_item = convertView.findViewById(R.id.tv_tag_guess_item);
                myHolder.tv_per_guess_item = convertView.findViewById(R.id.tv_per_guess_item);
                myHolder.tv_price_guess_item = convertView.findViewById(R.id.tv_price_guess_item);

                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }
            GuessCompetitionBean.DataBean.RanklistBean bean = guessCompetitionBean.data.ranklist.get(position);

             bindView(myHolder,bean);

            return convertView;
        }
    }


    public void bindView(MyHolder myHolder, GuessCompetitionBean.DataBean.RanklistBean bean){

        if(bean.rank != null && bean.pre_rank != null) {
            int rank = Integer.valueOf(bean.rank);
            int pre_rank = Integer.valueOf(bean.pre_rank);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append(bean.rank);
            if (rank < pre_rank) {//升
                SpannableString spannableString = new SpannableString("↑");
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.red_));
                spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(spannableString);
                myHolder.tv_count_guess_item.setText(spannableStringBuilder);
            } else {
                SpannableString spannableString = new SpannableString("↓");
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#5500ff00"));
                spannableString.setSpan(foregroundColorSpan, 0, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                spannableStringBuilder.append(spannableString);
                myHolder.tv_count_guess_item.setText(spannableStringBuilder);
            }
        }else{
            myHolder.tv_count_guess_item.setText("--");
        }

        if(!TextUtils.isEmpty(bean.verify_type)&&Integer.valueOf(bean.verify_type)>0){
            myHolder.iv_vip_header.setVisibility(View.VISIBLE);
        }else{
            myHolder.iv_vip_header.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(bean.newX)&&Integer.valueOf(bean.newX)>0){
            myHolder.iv_red_point_header.setVisibility(View.VISIBLE);
        }else{
            myHolder.iv_red_point_header.setVisibility(View.GONE);
        }

        PictureUtils.showCircle(bean.headurl,myHolder.iv_head_dynamic_item);

        myHolder.tv_name_guess_item.setText(bean.nickname);
        if(bean.accuracy_text != null){
            myHolder.tv_tag_guess_item.setText(bean.accuracy_text+"");
        }else{
            myHolder.tv_tag_guess_item.setText("");
        }

        if(!TextUtils.isEmpty(bean.revenue)) {
            if(!bean.revenue.contains("%"))bean.revenue += "%";
            myHolder.tv_per_guess_item.setText(bean.revenue);
        }else{
            myHolder.tv_per_guess_item.setText("--");
        }

        if(!TextUtils.isEmpty(bean.bonus)) {
            myHolder.tv_price_guess_item.setText("￥"+bean.bonus);
        }else{
            myHolder.tv_price_guess_item.setText("￥0");
        }
    }
    static class MyHolder{
        public TextView tv_count_guess_item;
        public ImageView iv_head_dynamic_item;
        public ImageView iv_vip_header;
        public TextView iv_red_point_header;
        public TextView tv_name_guess_item;
        public TextView tv_tag_guess_item;
        public TextView tv_per_guess_item;
        public TextView tv_price_guess_item;

    }
}
