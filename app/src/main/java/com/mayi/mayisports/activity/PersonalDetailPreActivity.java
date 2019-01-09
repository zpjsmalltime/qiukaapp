package com.mayi.mayisports.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.PersoanalGroomBean;
import com.mayisports.qca.bean.PersonalTopicBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import static com.mayi.mayisports.QCaApplication.getContext;


/**
 * 旧版个人中心界面，暂不用
 */
public class PersonalDetailPreActivity extends BaseActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {



    private LocalBroadcastManager localBroadcastManager;
    public static final String ACTION = "personal_detail_action";
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

    @Override
    public void onReload() {

    }


    /**
     * 分享回调
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.toastNoStates("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.toast("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.toastNoStates("分享取消");

    }


    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            initView();
//            initData();
//            ToastUtils.toast("回调刷新");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }

    private XRefreshView xfv_personal_detail;
    private ListView lv_personal_detail;

    private TextView tv_groom_personal_detial;
    private TextView tv_topic_personal_detial;
    private View iv_groom_personal_detial;
    private View iv_topice_personal_detial;
    private TextView tv_count_persoanal_detail;

    private MyLvAdapter myLvAdapter = new MyLvAdapter();

    private RelativeLayout rl_load_layout;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_personal_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        initReceiver();
        tv_title.setText("球咖");
        iv_right_title.setVisibility(View.VISIBLE);
        iv_right_title.setOnClickListener(this);
        iv_right_title.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
        tv_ritht_title.setVisibility(View.GONE);

        rl_load_layout = findViewById(R.id.rl_load_layout);
        xfv_personal_detail = findViewById(R.id.xfv_personal_detail);
        xfv_personal_detail.setPullRefreshEnable(false);//设置允许下拉刷新
        xfv_personal_detail.setPullLoadEnable(true);//设置允许上拉加载
        //刷新动画，需要自定义CustomGifHeader，不需要修改动画的会默认头布局
//        CustomGifHeader header = new CustomGifHeader(ct);
//        xfv_home_fg.setCustomHeaderView(header);
        xfv_personal_detail.setMoveForHorizontal(true);
        xfv_personal_detail.setXRefreshViewListener(new MyListener());

        lv_personal_detail = findViewById(R.id.lv_personal_detail);
        addHeaderForListView();
        lv_personal_detail.setAdapter(myLvAdapter);
        lv_personal_detail.setOnItemClickListener(this);




    }

    private String id;
    @Override
    protected void initDatas() {
        super.initDatas();
        id = getIntent().getStringExtra("id");
        int type = getIntent().getIntExtra("type", 0);
        if(type == 1){
            onClick(tv_topic_personal_detial);
        }else {
            onClick(tv_groom_personal_detial);
        }
    }

    /**
     * 请求推荐数据
     */
    private PersoanalGroomBean persoanalGroomBean;
    private void requestLeftData(final int pageLeft) {

        //htt://20180109.dldemo.applinzi.com/php/api.php?action=followers&type=friends_feed_list&start=0&user_id=198159
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","homepage");
        params.put("user_id",id+"");
        params.put("start",pageLeft);
        params.put("from",6);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                xfv_personal_detail.stopLoadMore();
                type = 1;

                if(pageLeft == 0) {
                    persoanalGroomBean = JsonParseUtils.parseJsonClass(string,PersoanalGroomBean.class);
                    if (persoanalGroomBean != null && persoanalGroomBean.data != null && persoanalGroomBean.data.recommendationlist != null) {
                        lv_personal_detail.setAdapter(myLvAdapter);
                        ll_no_topic.setVisibility(View.GONE);
                    }else{
                        ll_no_topic.setVisibility(View.VISIBLE);
                        tv_count_persoanal_detail.setVisibility(View.GONE);
                    }
                    if(persoanalGroomBean != null && persoanalGroomBean.data != null&&persoanalGroomBean.data.user != null){
                        updateHeader();
                    }
                }else{
                    try {
                        PersoanalGroomBean newBean = JsonParseUtils.parseJsonClass(string, PersoanalGroomBean.class);
                        persoanalGroomBean.data.recommendationlist.addAll(newBean.data.recommendationlist);
                        myLvAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        ToastUtils.toast("没有更多了");
                    }

                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                xfv_personal_detail.stopLoadMore();
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private boolean isSelf;
    /**
     * 刷新头部数据
     *   大数据推荐id 53719
     */
    private void updateHeader() {
        if(type == 2){
            PersonalTopicBean.DataBean.UserBean user = personalTopicBean.data.user;
            if(personalTopicBean.data.list !=null) {
                tv_count_persoanal_detail.setVisibility(View.VISIBLE);
            }
            int comment_count = user.comment_count;
            String praise_count = user.praise_count;
            if(TextUtils.isEmpty(praise_count)){
                praise_count = "0";
            }
            tv_count_persoanal_detail.setText("共" + comment_count + "条评论，获赞"+praise_count+"个");
//            tv_count_persoanal_detail.setText("共推荐" + user.all_bet_count + "场");
            PictureUtils.showCircle(user.headurl+"",iv_header_personal);

            if(!TextUtils.isEmpty(user.verify_type)&&Integer.valueOf(user.verify_type)>0){
                iv_vip_header.setVisibility(View.VISIBLE);
            }else{
                iv_vip_header.setVisibility(View.GONE);
            }

            ViewStatusUtils.addTagsPersonal(ll_tag_container_personal_detail,personalTopicBean.data.user.tag,personalTopicBean.data.user.tag1);

            tv_name_personal.setText(user.nickname+"");
//        tv_fenxishi_personal.setText(user.verify_type);
            if(!id.equals(SPUtils.getString(this,Constant.USER_ID))){
                isSelf = false;
                if(user.follow_status == 1){
                    tv_follow_personal.setTag(1);
//            cb.setChecked(true);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                    tv_follow_personal.setTextColor(Color.parseColor("#e8e8e8"));
                    tv_follow_personal.setText("已关注");
                }else{
                    tv_follow_personal.setTag(0);
//            cb.setChecked(false);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                    tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                    tv_follow_personal.setText(" 关注 ");
                }
            }else {
                tv_follow_personal.setVisibility(View.GONE);
                isSelf = true;
            }


            if(!"1".equals(user.type)){
                if(user.follow_status == 1){
                    tv_follow_personal.setTag(1);
//            cb.setChecked(true);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                    tv_follow_personal.setTextColor(Color.parseColor("#e8e8e8"));
                    tv_follow_personal.setText("已关注");
                }else{
                    tv_follow_personal.setTag(0);
//            cb.setChecked(false);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                    tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                    tv_follow_personal.setText(" 关注 ");
                }
            }else {
                tv_follow_personal.setVisibility(View.GONE);
//                user.headurl = "http://dldemo-img.stor.sinaapp.com/model_head.png";
                PictureUtils.show(R.drawable.big_data_header,iv_header_personal);
            }




            if(!TextUtils.isEmpty(user.verify_reason)){
                tv_jieshao_personal.setText(user.verify_reason);
            }else{
                tv_jieshao_personal.setText("");
                tv_jieshao_personal.setVisibility(View.INVISIBLE);
            }

            tv_follow_personal.setOnClickListener(this);
            return;
        }
        PersoanalGroomBean.DataBean.UserBean user = persoanalGroomBean.data.user;
        if(persoanalGroomBean.data.recommendationlist !=null) {
            tv_count_persoanal_detail.setVisibility(View.VISIBLE);
        }
        tv_count_persoanal_detail.setText("共推荐" + user.all_bet_count + "场");
        PictureUtils.showCircle(user.headurl+"",iv_header_personal);

        if(!TextUtils.isEmpty(user.verify_type)&&Integer.valueOf(user.verify_type)>0){
            iv_vip_header.setVisibility(View.VISIBLE);
        }else{
            iv_vip_header.setVisibility(View.GONE);
        }

        ViewStatusUtils.addTagsPersonal(ll_tag_container_personal_detail,persoanalGroomBean.data.user.tag,persoanalGroomBean.data.user.tag1);

        tv_name_personal.setText(user.nickname+"");
//        tv_fenxishi_personal.setText(user.verify_type);
        if(!id.equals(SPUtils.getString(this,Constant.USER_ID))){
            isSelf = false;
            if(user.follow_status == 1){
                tv_follow_personal.setTag(1);
//            cb.setChecked(true);
                tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                tv_follow_personal.setTextColor(Color.parseColor("#e8e8e8"));
                tv_follow_personal.setText("已关注");
            }else{
                tv_follow_personal.setTag(0);
//            cb.setChecked(false);
                tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                tv_follow_personal.setText(" 关注 ");
            }
        }else {
            tv_follow_personal.setVisibility(View.GONE);
            isSelf = true;
        }

        if(!"1".equals(user.type)){//http://dldemo-img.stor.sinaapp.com/model_head.png
            if(user.follow_status == 1){
                tv_follow_personal.setTag(1);
//            cb.setChecked(true);
                tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                tv_follow_personal.setTextColor(Color.parseColor("#e8e8e8"));
                tv_follow_personal.setText("已关注");
            }else{
                tv_follow_personal.setTag(0);
//            cb.setChecked(false);
                tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                tv_follow_personal.setText(" 关注 ");
            }
        }else {
            tv_follow_personal.setVisibility(View.GONE);
//            user.headurl = "http://dldemo-img.stor.sinaapp.com/model_head.png";
            PictureUtils.show(R.drawable.big_data_header,iv_header_personal);
        }

        if(!TextUtils.isEmpty(user.verify_reason)){
            tv_jieshao_personal.setText(user.verify_reason);
        }else{
            tv_jieshao_personal.setText("");
            tv_jieshao_personal.setVisibility(View.INVISIBLE);
        }

        tv_follow_personal.setOnClickListener(this);
    }

    private View headerView;
    private ImageView iv_header_personal;
    private TextView tv_name_personal;
    private TextView tv_fenxishi_personal;
//    private CheckBox cb;
    private TextView tv_follow_personal;
    private TextView tv_jieshao_personal;
    private LinearLayout ll_no_topic;
    private LinearLayout ll_tag_container_personal_detail;
    private ImageView iv_vip_header;
    private void addHeaderForListView() {
        headerView = View.inflate(getContext(),R.layout.personal_detail_header_fg,null);
        tv_groom_personal_detial = headerView.findViewById(R.id.tv_groom_personal_detial);
        iv_vip_header = headerView.findViewById(R.id.iv_vip_header);
        tv_topic_personal_detial = headerView.findViewById(R.id.tv_topic_personal_detial);
        iv_groom_personal_detial = headerView.findViewById(R.id.iv_groom_personal_detial);
        iv_topice_personal_detial = headerView.findViewById(R.id.iv_topice_personal_detial);
        tv_count_persoanal_detail = headerView.findViewById(R.id.tv_count_persoanal_detail);
        iv_header_personal = headerView.findViewById(R.id.iv_header_personal);
        iv_header_personal.setOnClickListener(this);
        ll_tag_container_personal_detail = headerView.findViewById(R.id.ll_tag_container_personal_detail);
        tv_name_personal = headerView.findViewById(R.id.tv_name_personal);
//        tv_fenxishi_personal = headerView.findViewById(R.id.tv_fenxishi_personal);
        ll_no_topic = headerView.findViewById(R.id.ll_no_topic);
//        cb = headerView.findViewById(R.id.cb);

        tv_follow_personal = headerView.findViewById(R.id.tv_follow_personal);
        tv_follow_personal.setOnClickListener(this);
        tv_jieshao_personal = headerView.findViewById(R.id.tv_jieshao_personal);

        tv_groom_personal_detial.setOnClickListener(this);
        tv_topic_personal_detial.setOnClickListener(this);

        lv_personal_detail.addHeaderView(headerView);
        iv_left_title.setOnClickListener(this);
    }

    /**
     * 1，推,2，话题
     */
    private int type;
    private int page;
    private int pageLeft;
    private int clickType;
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_groom_personal_detial:
                selectOne(0);
                count = 30;
                tv_count_persoanal_detail.setVisibility(View.VISIBLE);
//                myLvAdapter.notifyDataSetChanged();
                ll_no_topic.setVisibility(View.GONE);
                clickType = 0;
                pageLeft = 0;
                rl_load_layout = findViewById(R.id.rl_load_layout);
                requestLeftData(pageLeft);
                break;
            case R.id.tv_topic_personal_detial:
                selectOne(1);
                count = 0;
                tv_count_persoanal_detail.setVisibility(View.VISIBLE);
//                myLvAdapter.notifyDataSetChanged();
                clickType = 1;
                page = 0;
                rl_load_layout = findViewById(R.id.rl_load_layout);
                requestRightData(page);
                break;
            case R.id.iv_left_title:
                finish();
                break;
            case R.id.tv_cancle_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                break;
            case R.id.tv_go_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                if(type_ == 1){//确定
                    PersoanalGroomBean.DataBean.RecommendationlistBean bean = persoanalGroomBean.data.recommendationlist.get(clickPosition-1);
                    bean.recommendation.buy = 1;
                    myLvAdapter.notifyDataSetChanged();
                    goDetail(bean);
                }else{//充值

                    Intent intent = new Intent(this, CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_right_title:

                if(LoginUtils.isUnLogin()){
                LoginUtils.goLoginActivity(this,ACTION);
                return;
                }
                SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
                    @Override
                    public void onTypeClick(int count) {
                        shareTo(count);
                    }
                },new SharePopuwind.ShareInfoBean());

                sharePopuwind.showAtLocation(lv_personal_detail, Gravity.CENTER,0,0);

                break;
            case R.id.tv_follow_personal:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this,ACTION);
                    return;
                }

                if(tv_follow_personal.getTag() == null)break;
               int b = (int) tv_follow_personal.getTag();
                if(b == 1){//取消关注
                    requestFollows("delete");
                }else{//关注
                    requestFollows("add");
                }
                break;
            case R.id.iv_header_personal:
               if(persoanalGroomBean != null){
                   Intent intent = new Intent(PersonalDetailPreActivity.this,PicShowActivity.class);

                   intent.putExtra("urls",persoanalGroomBean.data.user.headurl);
                    startActivity(intent);
                }

                break;
        }
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        if(tv_follow_personal.getTag() != null) {
            int tag = (int) tv_follow_personal.getTag();
            intent.putExtra("position",getIntent().getIntExtra("position", -1));
            intent.putExtra("follow_status",tag);
            setResult(1,intent);
        }
        super.finish();
    }

    /**
     * 分享到第三方
     * @param type
     */
    private void shareTo(int type) {
//        ShareUtils.shareMsgForPersonalDetails(type, id,tv_name_personal.getText().toString(),this);
    }

    private  int clickPosition;
    private void goDetail(PersoanalGroomBean.DataBean.RecommendationlistBean bean){
        Intent intent = new Intent(this, HomeItemDetailActivity.class);
        intent.putExtra("userId",persoanalGroomBean.data.user.user_id);
        intent.putExtra("betId",bean.match.betId);
        startActivity(intent);
    }
    /**
     * 请求话题数据
     */
    private PersonalTopicBean personalTopicBean;
    private void requestRightData(final int page) {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","get_user_comments_list");
        params.put("user_id",id+"");
        params.put("start",page);
        params.put("from",6);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                xfv_personal_detail.stopLoadMore();
                type = 2;


                if(page == 0) {
                    personalTopicBean = JsonParseUtils.parseJsonClass(string,PersonalTopicBean.class);
                    if (personalTopicBean != null && personalTopicBean.data != null &&personalTopicBean.data.list != null) {
                        lv_personal_detail.setAdapter(myLvAdapter);
                        ll_no_topic.setVisibility(View.GONE);
                    } else {
                        ll_no_topic.setVisibility(View.VISIBLE);
                        tv_count_persoanal_detail.setVisibility(View.GONE);
                    }
                    if (personalTopicBean != null && personalTopicBean.data != null&&personalTopicBean.data.user != null ) {
                        updateHeader();
                    }


                }else{
                    try {
                        PersonalTopicBean newBean = JsonParseUtils.parseJsonClass(string, PersonalTopicBean.class);
                        personalTopicBean.data.list.addAll(newBean.data.list);
                        myLvAdapter.notifyDataSetChanged();
                    }catch (Exception e){
                        ToastUtils.toast("没有更多了");
                    }

                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                xfv_personal_detail.stopLoadMore();
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if(i == 0)return;
        if(type == 1) {
            PersoanalGroomBean.DataBean.RecommendationlistBean bean = persoanalGroomBean.data.recommendationlist.get(i-1);
            String price = bean.recommendation.price+"";
            if(LoginUtils.isUnLogin()){
                LoginUtils.goLoginActivity(this,ACTION);
                return;
            }


            if(bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0) || "COMPLETE".equals(bean.match.status) || SPUtils.getString(getContext(),Constant.USER_ID).equals(persoanalGroomBean.data.user.user_id)){//已购买 //或者免费
                Intent intent = new Intent(this, HomeItemDetailActivity.class);
                intent.putExtra("userId",persoanalGroomBean.data.user.user_id);
                intent.putExtra("betId",bean.match.betId);
                startActivity(intent);
            }else{
                clickPosition = i;
                requestToast(persoanalGroomBean.data.user.user_id,bean.match.betId);
            }



        }else if(type == 2){
            try {
                String topic_id = personalTopicBean.data.list.get(i-1).topic.topic_id;
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/topic/"+topic_id,"话题");
                TopicDetailActivity.start(this,topic_id);
            }catch (Exception e){

            }

        }

    }


    /**
     * 请求是否购买
     */
    private int type_;//1确定，2充值
    private ToastPricePopuWindow toastPricePopuWindow;
    private void requestToast(String userId,String betId) {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","recommendation_Detail");
        params.put("type","req");
        params.put("betId",betId);
        params.put("user_id",userId);
        params.put("from",3);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ToastPriceBean toastPriceBean = JsonParseUtils.parseJsonClass(string,ToastPriceBean.class);
                String title = "";
                title = toastPriceBean.text;
                if("req".equals(toastPriceBean.type)){
                    type_ = 1;
                }else if("nomoney".equals(toastPriceBean.type)){
                    type_ = 2;
                }
                toastPricePopuWindow =   new ToastPricePopuWindow(PersonalDetailPreActivity.this,PersonalDetailPreActivity.this,title,type_);
                toastPricePopuWindow.showAtLocation(lv_personal_detail, Gravity.CENTER,0,0);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){//取消关注
             requestFollows("delete");
        }else{//关注
             requestFollows("add");
        }
    }

    public void requestFollows(String type){
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        params.put("type",type);
        params.put("user_id",id+"");
        rl_load_layout = null;
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        tv_follow_personal.setTag(1);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                        tv_follow_personal.setTextColor(Color.parseColor("#e8e8e8"));
                        tv_follow_personal.setText("已关注");
                        NotificationsUtils.checkNotificationAndStartSetting(PersonalDetailPreActivity.this,lv_personal_detail);
                    }else{
                        tv_follow_personal.setTag(0);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                        tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                        tv_follow_personal.setText(" 关注 ");
                    }
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
     * 加载刷新监听
     */
    class MyListener extends XRefreshView.SimpleXRefreshListener {


        @Override
        public void onRefresh(boolean isPullDown) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //填写刷新数据的网络请求，一般page=1，List集合清空操作
                    xfv_personal_detail.stopRefresh();//刷新停止
                }
            }, 2000);//2000是刷新的延时，使得有个动画效果
        }

        @Override
        public void onLoadMore(boolean isSilence) {
            rl_load_layout = null;
            if(type == 1){
                pageLeft ++;
                requestLeftData(pageLeft);
            }else if(type == 2) {
                page ++;
                requestRightData(page);
            }
        }

    }

    /**
     * 刷新顶部tab状态
     * @param postion
     */
    private void selectOne(int postion){

        tv_groom_personal_detial.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_groom_personal_detial.setVisibility(View.INVISIBLE);

        tv_topic_personal_detial.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_topice_personal_detial.setVisibility(View.INVISIBLE);

        switch (postion){
            case 0:
                tv_groom_personal_detial.setTextColor(getResources().getColor(R.color.score_black_top_title));
                iv_groom_personal_detial.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_topic_personal_detial.setTextColor(getResources().getColor(R.color.score_black_top_title));
                iv_topice_personal_detial.setVisibility(View.VISIBLE);
                break;
        }
    }



    int count = 30;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
    class MyLvAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            switch (type){
                case 1:
                    if(persoanalGroomBean != null && persoanalGroomBean.data != null && persoanalGroomBean.data.recommendationlist != null){
                        return  persoanalGroomBean.data.recommendationlist.size();
                    }
                    break;
                case 2:
                    if(personalTopicBean != null && personalTopicBean.data != null && personalTopicBean.data.list !=null){
                        return personalTopicBean.data.list.size();
                    }

                    break;
            }

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
        public View getView(int i, View view, ViewGroup viewGroup) {
            LvHolder lvHolder;
            if(view == null){
                lvHolder = new LvHolder();

                if(type == 1) {
                    view = View.inflate(getContext(), R.layout.item_personal_detial, null);
                    lvHolder.tv_content_personal = view.findViewById(R.id.tv_content_personal);
                    lvHolder.tv_team_name_personal = view.findViewById(R.id.tv_team_name_personal);
                    lvHolder.tv_bottom_persoanal = view.findViewById(R.id.tv_bottom_persoanal);
                    lvHolder.tv_type_personal = view.findViewById(R.id.tv_type_personal);
                    lvHolder.tv_price_match_item = view.findViewById(R.id.tv_price_match_item);
                    lvHolder.iv_result_personal = view.findViewById(R.id.iv_result_personal);
                    lvHolder.ll_price = view.findViewById(R.id.ll_price);
                    lvHolder.tv_buy = view.findViewById(R.id.tv_buy);
                    lvHolder.tv_right_item = view.findViewById(R.id.tv_right_item);
                }else if(type == 2){
                    view = View.inflate(getContext(),R.layout.item_topic_personal_detail,null);
                    lvHolder.ll_top_click = view.findViewById(R.id.ll_top_click);
                    lvHolder.iv_vip_header = view.findViewById(R.id.iv_vip_header);
                    lvHolder.iv_head_topic_item = view.findViewById(R.id.iv_head_topic_item);
                    lvHolder.tv_name_topic_item = view.findViewById(R.id.tv_name_topic_item);
                    lvHolder.tv_time_topic_item = view.findViewById(R.id.tv_time_topic_item);
                    lvHolder.tv_title_topic_item = view.findViewById(R.id.tv_title_topic_item);
                    lvHolder.iv_img_left = view.findViewById(R.id.iv_img_left);
                    lvHolder.tv_bottom_title_topic_item = view.findViewById(R.id.tv_bottom_title_topic_item);
                    lvHolder.tv_view_count_topic_item = view.findViewById(R.id.tv_view_count_topic_item);
                    lvHolder.tv_thumbup_topic_item = view.findViewById(R.id.tv_thumbup_topic_item);
                    lvHolder.tv_comments_topic_item = view.findViewById(R.id.tv_comments_topic_item);
                    lvHolder.rl_topic_item = view.findViewById(R.id.rl_topic_item);
                    lvHolder.ll_tag_container_topic_item = view.findViewById(R.id.ll_tag_container_topic_item);
                    lvHolder.ll_img_container_personal_detail = view.findViewById(R.id.ll_img_container_personal_detail);

                }
                view.setTag(lvHolder);
            }else{
                lvHolder = (LvHolder) view.getTag();
            }
            if(type == 1) {
                PersoanalGroomBean.DataBean.RecommendationlistBean bean = persoanalGroomBean.data.recommendationlist.get(i);

                lvHolder.ll_price.setVisibility(View.VISIBLE);
                lvHolder.iv_result_personal.setVisibility(View.GONE);
                lvHolder.tv_right_item.setVisibility(View.GONE);

                switch (bean.match.status){
                    case "NO_START":
                        break;
                    case "FIRST_HALF":
                        break;
                    case "HALF_TIME":
                        break;
                    case "SECOND_HALF":
                        break;
                    case "COMPLETE":
                        break;
                }

            if(!TextUtils.isEmpty(bean.recommendation.title)){
                lvHolder.tv_content_personal.setVisibility(View.VISIBLE);
                lvHolder.tv_content_personal.setText(bean.recommendation.title);
            }else{
                lvHolder.tv_content_personal.setVisibility(View.GONE);
            }

                lvHolder.tv_team_name_personal.setText(bean.match.hostTeamName + "\t\tvs\t\t" + bean.match.awayTeamName);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                spannableStringBuilder.append(bean.match.leagueName );
                lvHolder.tv_type_personal.setText(bean.recommendation.type+"");
                if (!TextUtils.isEmpty(bean.match.timezoneoffset)) {
                    String matchStartTime = Utils.getMatchStartTime(Long.valueOf(bean.match.timezoneoffset + "000"));
                    spannableStringBuilder.append(" · " +matchStartTime );
                }

                if("1".equals(bean.recommendation.return_if_wrong)){
                    spannableStringBuilder.append(" · "+"不中退款");
                }

                if(!bean.match.status.equals("COMPLETE")) {



                    if (bean.recommendation.price != 0 ) {
                        Integer integer = Integer.valueOf(bean.recommendation.price);
                        String price = " · "+integer + "金币";
                        SpannableString spannableString = new SpannableString(price);
                        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#AE7614"));
                        spannableString.setSpan(span, 0, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

                        if(bean.recommendation.buy == 1){
                            lvHolder.tv_buy.setVisibility(View.VISIBLE);
                            lvHolder.ll_price.setVisibility(View.GONE);
                            lvHolder.tv_right_item.setVisibility(View.VISIBLE);
                            spannableStringBuilder.append(" · "+bean.recommendation.type+"");
                        }else{
                            lvHolder.tv_buy.setVisibility(View.GONE);
                            lvHolder.ll_price.setVisibility(View.VISIBLE);
                            lvHolder.tv_right_item.setVisibility(View.GONE);
                        }


                        lvHolder.tv_price_match_item.setText(integer + "金币");


                    }else{
//                        spannableStringBuilder.append(" · "+ bean.recommendation.type);
//                        lvHolder.ll_price.setVisibility(View.GONE);
                        lvHolder.tv_buy.setVisibility(View.GONE);
                        String price = "免费";
//                        SpannableString spannableString = new SpannableString(price);
//                        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#AE7614"));
//                        spannableString.setSpan(span, 0, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                        spannableStringBuilder.append(spannableString);
                        lvHolder.tv_price_match_item.setText(price);

                    }
                }else{
                    spannableStringBuilder.append(" · "+ bean.recommendation.type + "");
                    lvHolder.ll_price.setVisibility(View.GONE);
                    lvHolder.tv_buy.setVisibility(View.GONE);
                    lvHolder.iv_result_personal.setVisibility(View.VISIBLE);
                    ViewStatusUtils.parseStatusForImg(bean.recommendation.revenueStr,lvHolder.iv_result_personal);
                }


                lvHolder.tv_bottom_persoanal.setText(spannableStringBuilder);
                spannableStringBuilder.clear();


                if(isSelf) {
                    lvHolder.tv_buy.setVisibility(View.GONE);
                }
            }else if(type == 2) {
                final PersonalTopicBean.DataBean.ListBean listBean = personalTopicBean.data.list.get(i);
                PictureUtils.showCircle(listBean.user.headurl,lvHolder.iv_head_topic_item);

                if(!TextUtils.isEmpty(listBean.user.verify_type)&&Integer.valueOf(listBean.user.verify_type)>0){
                    lvHolder.iv_vip_header.setVisibility(View.VISIBLE);
                }else{
                    lvHolder.iv_vip_header.setVisibility(View.GONE);
                }
//                lvHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                });

//                ViewStatusUtils.addTags(lvHolder.ll_tag_container_topic_item,personalTopicBean.data.user.tag,personalTopicBean.data.user.tag1);

                lvHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestThumbup(listBean);
                    }
                });

                lvHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(LoginUtils.isUnLogin()){
                            LoginUtils.goLoginActivity(PersonalDetailPreActivity.this,ACTION);
                            return;
                        }
//                        String url = Constant.BASE_URL + "#/topicViewDetail/"+listBean.comments.comment_id;
//                        WebViewActivtiy.start(PersonalDetailPreActivity.this,url,"球咖");
                        ComentsDetailsActivity.start(PersonalDetailPreActivity.this,listBean.comments.comment_id);
                    }
                });

                lvHolder.tv_title_topic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {  //#/topicViewDetail/36979
//                        String url = Constant.BASE_URL + "#/topicViewDetail/"+listBean.comments.comment_id;
//                        WebViewActivtiy.start(PersonalDetailPreActivity.this,url,"球咖");
                        ComentsDetailsActivity.start(PersonalDetailPreActivity.this,listBean.comments.comment_id);
                    }
                });


                lvHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(LoginUtils.isUnLogin()){
                            LoginUtils.goLoginActivity(PersonalDetailPreActivity.this,ACTION);
                            return;
                        }
//                        String url = Constant.BASE_URL + "#/topicViewDetail/"+listBean.comment_id;
//                        WebViewActivtiy.start(PersonalDetailPreActivity.this,url,"球咖");
                        ComentsDetailsActivity.start(PersonalDetailPreActivity.this,listBean.comments.comment_id);
                    }
                });

                lvHolder.tv_name_topic_item.setText(listBean.user.nickname+"");
                try {

                    String createTime = Utils.getCreateTime(Long.valueOf(listBean.create_time + "000"));
                    lvHolder.tv_time_topic_item.setText(createTime + "");
                }catch (Exception e){

                }
                lvHolder.tv_title_topic_item.setText(listBean.comment+"");


                if(listBean.comments.imglist != null){
                    lvHolder.ll_img_container_personal_detail.setVisibility(View.VISIBLE);
                    for(int j = 0;j<lvHolder.ll_img_container_personal_detail.getChildCount();j++){
                        lvHolder.ll_img_container_personal_detail.getChildAt(j).setVisibility(View.INVISIBLE);
                    }
                    String urls = "";
                    for(int j = 0;j<listBean.comments.imglist.size();j++){
                        if(j>2)break;
                        String imgUrl = listBean.comments.imglist.get(j);
                        if(TextUtils.isEmpty(imgUrl)){
                            lvHolder.ll_img_container_personal_detail.setVisibility(View.GONE);
                            break;
                        }
                        urls+=imgUrl+",";
                    }
                    for(int j = 0;j<listBean.comments.imglist.size();j++){
                        if(j>2)break;
                        String imgUrl = listBean.comments.imglist.get(j);
                        if(TextUtils.isEmpty(imgUrl)){
                            lvHolder.ll_img_container_personal_detail.setVisibility(View.GONE);
                            break;
                        }
                        final String finalUrls = urls;
                        final int finalI = j;
                        ImageView imageView = (ImageView) lvHolder.ll_img_container_personal_detail.getChildAt(j);
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(finalUrls.length()>0){
                                    String substring = finalUrls.substring(0, finalUrls.length() - 1);
                                    Intent intent = new Intent(PersonalDetailPreActivity.this,PicShowActivity.class);
                                    intent.putExtra("urls",substring);
                                    intent.putExtra("position", finalI);
                                    startActivity(intent);
                                }
                            }
                        });
                        imageView.setVisibility(View.VISIBLE);
                        PictureUtils.show(imgUrl,imageView);
                    }
//                    final String finalUrls = urls;
//                    lvHolder.ll_img_container_personal_detail.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            if(finalUrls.length()>0){
//                                String substring = finalUrls.substring(0, finalUrls.length() - 1);
//                                Intent intent = new Intent(PersonalDetailActivity.this,PicShowActivity.class);
//                                intent.putExtra("urls",substring);
//                                startActivity(intent);
//                            }
//                        }
//                    });
                }else{
                    lvHolder.ll_img_container_personal_detail.setVisibility(View.GONE);
                }

                if(listBean.topic != null){
                    PersonalTopicBean.DataBean.ListBean.TopicBean topic = listBean.topic;
                    lvHolder.rl_topic_item.setVisibility(View.VISIBLE);
                    PictureUtils.show(topic.background_img,lvHolder.iv_img_left);
                    lvHolder.tv_bottom_title_topic_item.setText(topic.title);
                    lvHolder.tv_view_count_topic_item.setText(topic.view_count+" 阅读");
                }else{
                    lvHolder.rl_topic_item.setVisibility(View.GONE);
                }

                if(listBean.comments.praise_status == 1){//点赞
                    Drawable drawable= getResources().getDrawable(R.drawable.thumb_checked_small);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    lvHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
                }else {
                    Drawable drawable= getResources().getDrawable(R.drawable.thumb_small);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                    lvHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
                }

                lvHolder.tv_thumbup_topic_item.setText(listBean.comments.praise_count+"");
                lvHolder.tv_comments_topic_item.setText(listBean.comments.reply_count+"");


            }



            return view;
        }
    }

    /**
     * 请求点赞
     * @param listBean
     */
    private void requestThumbup(final PersonalTopicBean.DataBean.ListBean listBean) {

        if(LoginUtils.isUnLogin()){
           LoginUtils.goLoginActivity(this,ACTION);
            return;
        }

        rl_load_layout = findViewById(R.id.rl_load_layout);
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","praise");
        params.put("id",listBean.comment_id);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    listBean.comments.praise_count = praiseBean.data.praise_count;
                    listBean.comments.praise_status = praiseBean.data.praise_status;
                    myLvAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.toast("点赞失败");
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    static class LvHolder {
        public TextView tv_content_personal;
        public TextView tv_team_name_personal;
        public TextView tv_bottom_persoanal;
        public TextView tv_type_personal;
        public TextView tv_price_match_item;
        public ImageView iv_result_personal;
        public LinearLayout ll_price;
        public TextView tv_buy;
        public TextView tv_right_item;


       //话题
       public ImageView iv_head_topic_item;
       public TextView tv_name_topic_item;
       public TextView tv_time_topic_item;
       public TextView tv_title_topic_item;
       public RelativeLayout rl_topic_item;
       public ImageView iv_img_left;
       public TextView tv_bottom_title_topic_item;
       public TextView tv_view_count_topic_item;
       public TextView tv_thumbup_topic_item;
       public TextView tv_comments_topic_item;
       public LinearLayout ll_tag_container_topic_item;
       public LinearLayout ll_img_container_personal_detail;
        public LinearLayout ll_top_click;
        public ImageView iv_vip_header;
    }

}
