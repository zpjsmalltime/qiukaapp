package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.adapter.ShareReplyUtils;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.HomeDataNewBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.PublishPointBean;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.GridViewUtils;
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
import com.mayisports.qca.view.CustomLinkMovementMethod;
import com.mayisports.qca.view.NoLineClickSpan;
import com.mayisports.qca.view.SelectPicPopupWindow;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import static com.mayi.mayisports.activity.PersonalDetailActivity.ACTION;


/**
 * 我的收藏界面
 */
public class CollectionActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,CollectionActivity.class);
        activity.startActivity(intent);
    }
    @Override
    protected int setViewForContent() {
        return R.layout.activity_collection;
    }


    public XRefreshView xfv_dynamic_fg;
    private ListView lv_dynamic_fg;
      private View ll_no_data;



    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;

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

        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("收藏");

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               initDatas();
            }
        });


        rl_load_layout = findViewById(R.id.rl_load_layout);


        ll_no_data = findViewById(R.id.ll_no_data);
        xfv_dynamic_fg  = findViewById(R.id.xfv_dynamic_fg);
        lv_dynamic_fg = findViewById(R.id.lv_dynamic_fg);
        xfv_dynamic_fg.setPullRefreshEnable(true);//设置允许下拉刷新
//        xfv_dynamic_fg.setPullLoadEnable(true);//设置允许上拉加载
        //刷新动画，需要自定义CustomGifHeader，不需要修改动画的会默认头布局
//        CustomGifHeader header = new CustomGifHeader(ct);
//        xfv_home_fg.setCustomHeaderView(header);
        xfv_dynamic_fg.setMoveForHorizontal(true);
//        xfv_dynamic_fg.setSilenceLoadMore(true);
        xfv_dynamic_fg.setXRefreshViewListener(new MyListener());

//        lv_dynamic_fg.setAdapter(myAdapter);
        lv_dynamic_fg.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_cancle_toast_price:
                if (toastPricePopuWindow != null) {
                    toastPricePopuWindow.dismiss();
                }
                break;
            case R.id.tv_go_toast_price:
                if (toastPricePopuWindow != null) {
                    toastPricePopuWindow.dismiss();
                }
                if (type == 1) {//确定
                    DynamicBean.ListBean bean = dynamicBean.list.get(clickPosition);
                    bean.recommendation.buy = 1;
                    myAdapter.notifyDataSetChanged();
                    goDetail(bean,clickPosition);
                } else {//充值

                    Intent intent = new Intent(this, CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }

    /**
     * 同步关注状态
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1&&resultCode == 1){//个人详情

            int follow_status = data.getIntExtra("follow_status", 0);
            int position = data.getIntExtra("position", -1);
            if(position != -1) {
                myAdapter.notifyDataSetChanged();
            }

        }else if(requestCode == 2&&resultCode == 2){//话题详情
            int position = data.getIntExtra("position", -1);
            String praise_count = data.getStringExtra("praise_count");

            if(position != -1) {
                DynamicBean.ListBean bean = dynamicBean.list.get(position);


                int  follow_status  = Integer.valueOf(data.getStringExtra("follow_status"));
                String collection_status = data.getStringExtra("collection_status");


                boolean coll = false;
                if(!"1".equals(collection_status)){
                    coll = true;
                }


                if(bean.type == 4){
                    int praise_status = data.getIntExtra("praise_status", 0);
                    bean.reply.praise_count = praise_count;
                    bean.reply.praise_status = praise_status;
                    bean.reply.collection_status = collection_status;
                }else {
                    int praise_status = data.getIntExtra("praise_status", 0);
                    bean.comment.praise_count = praise_count;
                    bean.comment.praise_status = praise_status;
                    bean.comment.collection_status = collection_status;
                }

                boolean delete = data.getBooleanExtra("delete", false);
                if(delete || coll){
                    dynamicBean.list.remove(position);
                }

                myAdapter.notifyDataSetChanged();
            }

        }else if(requestCode == 22&& resultCode == 22){//推荐单详情回调
            int position = data.getIntExtra("position", -1);

            if(position != -1) {
                String collection_status = data.getStringExtra("collection_status");
                boolean coll = false;
                if(!"1".equals(collection_status)){
                    coll = true;
                }

                if(coll){
                    dynamicBean.list.remove(position);
                    myAdapter.notifyDataSetChanged();
                }
            }
            }
    }

    /**
     * 跳转推荐单详情
     * @param bean
     * @param position
     */
    private void goDetail(DynamicBean.ListBean bean,int position){
        Intent intent = new Intent(this, HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
        intent.putExtra("betId",bean.recommendation.betId);
        intent.putExtra("position",position);
        startActivityForResult(intent,22);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        initData();
    }


    public void initData() {

        page = 0;
        requestNetData();
    }

    /**
     * 加载刷新监听
     */
    private int page;
    private int pre_page = 1;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DynamicBean.ListBean bean = dynamicBean.list.get(i);
        if(bean.type == 1){//推荐单
            delType2Click(bean,i);
        }else if(bean.type == 3){//话题
//            if(bean.topic != null){
//                String topic_id = bean.topic.topic_id;
////                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/topic/"+topic_id,"话题");
//                TopicDetailActivity.start(this.getActivity(),topic_id);
//                return;
//            }


            if(bean.type == 3 && isShootVideo(bean)){
                toShootVideoPlayer(bean,i);
            }else {

                ComentsDetailsActivity.startForResult(2, this, bean.comment.comment_id, i);
            }
        }else if(bean.type == 6){
            TopicDetailActivity.start(this,bean.topic.id);
        }else if(bean.type == 4){//评论类型 或者转发
            ShareComentsDetailsActivity.startForResult(2, CollectionActivity.this, ComentsDetailsActivity.SHARE_TYPE, bean.comment.comment_id, bean, i);
        }


    }


    /**
     * 去短视频页
     * @param bean
     * @param position
     */
    private void toShootVideoPlayer(DynamicBean.ListBean bean, int position) {

        DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.list = new ArrayList<>();
        dynamicBean.list.add(bean);
        Constant.dynamicBean = dynamicBean;
        VideoPlayerActivity.start(this,-1,0,bean.subtype,22);

    }
    private boolean isShootVideo(DynamicBean.ListBean bean){
        boolean b = !TextUtils.isEmpty(bean.subtype) && Integer.valueOf(bean.subtype) == 7;
        return b;
    }

    /**
     * 处理item点击事件
     */
    private int clickPosition;
    private void delType2Click(DynamicBean.ListBean bean,int i) {
        String price = bean.recommendation.price;
        String nickname = bean.user.nickname;
        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        myAdapter.notifyDataSetChanged();
        if(bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0) || "COMPLETE".equals(bean.match.status)|| SPUtils.getString(this,Constant.USER_ID).equals(bean.user.user_id)){//已购买 //或者免费
            Intent intent = new Intent(this, HomeItemDetailActivity.class);
            intent.putExtra("userId",bean.user.user_id);

                intent.putExtra("betId", bean.recommendation.betId);
              goDetail(bean,i);
        }else{
            clickPosition = i;
            requestToast(bean.user.user_id,bean.recommendation.betId);
        }
    }

    /**
     * 请求是否购买
     */
    private int type;//1确定，2充值
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
                    type = 1;
                }else if("nomoney".equals(toastPriceBean.type)){
                    type = 2;
                }
                if(!CollectionActivity.this.isFinishing()) {
                    toastPricePopuWindow = new ToastPricePopuWindow(CollectionActivity.this, CollectionActivity.this, title, type);
                    toastPricePopuWindow.showAtLocation(lv_dynamic_fg, Gravity.CENTER, 0, 0);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    @Override
    public void onReload() {

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


    /**
     * 下拉刷新监听
     */
    class MyListener extends XRefreshView.SimpleXRefreshListener {


        @Override
        public void onRefresh(boolean isPullDown) {
            page = 0;
            requestNetData();
        }

    }


    /**
     * 初始化请求数据
     */
    private DynamicBean dynamicBean = new DynamicBean();
    private void requestNetData() {
        //http://20180207.dldemo.applinzi.com/php/api.php?action=user&type=user_collection_list&start=0
           String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","user_collection_list");
        params.put("start",page);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                if(xfv_dynamic_fg != null) {
                    xfv_dynamic_fg.stopRefresh();
                    xfv_dynamic_fg.stopLoadMore();
                }

                if(page == 0) {

                    DynamicBean.DyBean dyBean = JsonParseUtils.parseJsonClass(string, DynamicBean.DyBean.class);
                    dynamicBean.list = dyBean.data;

                    DynamicBean.DyBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.DyBean.class);
                    DynamicBean dynamicBean2 = new DynamicBean();
                    dynamicBean2.list = dynamicBean1.data;

                    delData(dynamicBean,dynamicBean2);



//                    SPUtils.putString(QCaApplication.getContext(), Constant.DYNAMIC_PAGE_CACHE,string);
                    if(dynamicBean != null && dynamicBean.list != null){
                        if(dynamicBean.list.size() == 0){
                            if(ll_no_data != null) xfv_dynamic_fg.setPullLoadEnable(false);
                            if(ll_no_data != null) ll_no_data.setVisibility(View.VISIBLE);
                        }else{

                            if(ll_no_data != null)ll_no_data.setVisibility(View.GONE);
                        }
                    }else{
                        if(ll_no_data != null)xfv_dynamic_fg.setPullLoadEnable(false);
                        if(ll_no_data != null)ll_no_data.setVisibility(View.VISIBLE);

                    }
                    pre_page = 1;
                     lv_dynamic_fg.setAdapter(myAdapter);
                }else{
                    try {
                        DynamicBean newDy = new DynamicBean();
                        DynamicBean newDy1 = new DynamicBean();
                        DynamicBean.DyBean newBean = JsonParseUtils.parseJsonClass(string, DynamicBean.DyBean.class);
                        DynamicBean.DyBean newBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.DyBean.class);

                        newDy.list = newBean.data;
                        newDy1.list = newBean1.data;


                        delData(newDy,newDy1);
                        dynamicBean.list.addAll(newDy.list);
                    }catch (Exception e){
//                        ToastUtils.toast("没有更多了。。。");
                        pre_page = 0;
                    }
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }


            @Override
            public void onFinish() {
                super.onFinish();
                if(page == 0){
                    if(NetworkUtils.isConnected(QCaApplication.getContext())){
                        if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                    }else{
                        if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                    }
                }

                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });
    }


    /**
     * 处理数据  type 1 显示  过滤数据
     * @param homeDataBean
     * @param newBean
     */
    private void delData(DynamicBean homeDataBean, DynamicBean newBean) {
        if(homeDataBean.list != null&&newBean.list != null){
            homeDataBean.list.clear();
            for(int i = 0;i<newBean.list.size();i++){

                DynamicBean.ListBean beanX = newBean.list.get(i);
                if(beanX.type == 1 || beanX.type == 3  ||beanX.type == 6 ||beanX.type == 5 || (beanX.type==0 && !TextUtils.isEmpty(beanX.subtype)) || beanX.type == 4) {//1推荐单  3 话题 6话题  5可能感兴趣的人 4转发评论动态
                    homeDataBean.list.add(beanX);
                }
            }
        }
    }


    /**
     * 主适配器
     */
    private MyAdapter myAdapter = new MyAdapter();
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(dynamicBean != null && dynamicBean.list!= null){
                ll_no_data.setVisibility(View.GONE);
                if(dynamicBean.list.size() == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                }
                return dynamicBean.list.size();
            }
            ll_no_data.setVisibility(View.VISIBLE);
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final DynamicBean.ListBean bean = dynamicBean.list.get(i);

            if(bean.type == 0 && !TextUtils.isEmpty(bean.subtype)){
                view = View.inflate(QCaApplication.getContext(),R.layout.delete_collect_item,null);
                view.setTag(null);
                TextView tv = view.findViewById(R.id.tv_delete);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteCollected(bean,i);
                    }
                });

                LinearLayout ll_click_to_personal = view.findViewById(R.id.ll_click_to_personal);
                ll_click_to_personal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        PersonalDetailActivity.start(CollectionActivity.this,bean.user_id,"{}");
                    }
                });


            }else {
                final MyHolder myHolder;
                if (view == null || view.getTag() == null) {
                    myHolder = new MyHolder();
                    view = View.inflate(CollectionActivity.this, R.layout.dynamic_item, null);
                    myHolder.ll_top_click = view.findViewById(R.id.ll_top_click);
                    myHolder.tv_buy = view.findViewById(R.id.tv_buy);
                    myHolder.iv_head_dynamic_item = view.findViewById(R.id.iv_head_dynamic_item);
                    myHolder.tv_name_dynamic_item = view.findViewById(R.id.tv_name_dynamic_item);
                    myHolder.iv_red_point_header = view.findViewById(R.id.iv_red_point_header);
                    myHolder.ll_top_tag_container = view.findViewById(R.id.ll_top_tag_container);
                    myHolder.tv_time_dynamic_item = view.findViewById(R.id.tv_time_dynamic_item);
                    myHolder.tv_content_dynamic_item = view.findViewById(R.id.tv_content_dynamic_item);

                    myHolder.tv_content_dynamic_item_copy = view.findViewById(R.id.tv_content_dynamic_item_copy);

                    myHolder.ll_groom_dynamic_item = view.findViewById(R.id.ll_groom_dynamic_item);
                    myHolder.tv_team_name_dynamic_item = view.findViewById(R.id.tv_team_name_dynamic_item);
                    myHolder.tv_bottom_dynamic_item = view.findViewById(R.id.tv_bottom_dynamic_item);
                    myHolder.tv_type_dynamic_item = view.findViewById(R.id.tv_type_dynamic_item);
                    myHolder.tv_price_dynamic_item = view.findViewById(R.id.tv_price_dynamic_item);
                    myHolder.ll_img_container_dynamic_item = view.findViewById(R.id.ll_img_container_dynamic_item);
                    myHolder.rl_topic_dynamic_item = view.findViewById(R.id.rl_topic_dynamic_item);
                    myHolder.iv_img_left = view.findViewById(R.id.iv_img_left);
                    myHolder.tv_bottom_title_topic_item = view.findViewById(R.id.tv_bottom_title_topic_item);
                    myHolder.tv_view_count_topic_item = view.findViewById(R.id.tv_view_count_topic_item);
                    myHolder.ll_bottom_dynamic_item = view.findViewById(R.id.ll_bottom_dynamic_item);
                    myHolder.tv_thumbup_topic_item = view.findViewById(R.id.tv_thumbup_topic_item);
                    myHolder.tv_comments_topic_item = view.findViewById(R.id.tv_comments_topic_item);
                    myHolder.ll_price = view.findViewById(R.id.ll_price);
                    myHolder.tv_follow_dynamic_item = view.findViewById(R.id.tv_follow_dynamic_item);
                    myHolder.iv_vip_header = view.findViewById(R.id.iv_vip_header);
                    myHolder.tv_right_item = view.findViewById(R.id.tv_right_item);
                    myHolder.rl_video_item = view.findViewById(R.id.rl_video_item);
                    myHolder.iv_video_item = view.findViewById(R.id.iv_video_item);
                    myHolder.rl_bg_content_item = view.findViewById(R.id.rl_bg_content_item);
                    myHolder.tv_content_bg_dynamic_item = view.findViewById(R.id.tv_content_bg_dynamic_item);
                    myHolder.iv_content_bg_item = view.findViewById(R.id.iv_content_bg_item);
                    myHolder.gv_dynamic_item = view.findViewById(R.id.gv_dynamic_item);
                    myHolder.rl_bg_long_content_item = view.findViewById(R.id.rl_bg_long_content_item);
                    myHolder.tv_content_long_content = view.findViewById(R.id.tv_content_long_content);
                    myHolder.tv_title_long_content = view.findViewById(R.id.tv_title_long_content);
                    myHolder.tv_share_topic_item = view.findViewById(R.id.tv_share_topic_item);

                    myHolder.fl_topic_img_item = view.findViewById(R.id.fl_topic_img_item);
                    myHolder.iv_topic_img = view.findViewById(R.id.iv_topic_img);
                    myHolder.tv_title_topic_img = view.findViewById(R.id.tv_title_topic_img);
                    myHolder.tv_viewcount_topic_img = view.findViewById(R.id.tv_viewcount_topic_img);
                    myHolder.ll_bottom_item = view.findViewById(R.id.ll_bottom_item);

                    myHolder.tv_content_bottom = view.findViewById(R.id.tv_content_bottom);

                    ViewGroup.LayoutParams layoutParams = myHolder.fl_topic_img_item.getLayoutParams();
                    layoutParams.height = (int) (DisplayUtil.getScreenWidth(CollectionActivity.this) * 0.32);
                    myHolder.fl_topic_img_item.setLayoutParams(layoutParams);

                    myHolder.iv_dot_more_item = view.findViewById(R.id.iv_dot_more_item);
                    myHolder.iv_dot_more_item.setVisibility(View.VISIBLE);


                    myHolder.ll_comment_type = view.findViewById(R.id.ll_comment_type);
                    myHolder.tv_content_comment_type_item = view.findViewById(R.id.tv_content_comment_type_item);
                    myHolder.gv_comment_type_item = view.findViewById(R.id.gv_comment_type_item);
                    myHolder.tv_detail_title = view.findViewById(R.id.tv_detail_title);


                    myHolder.rl_video_hot_reply_item = view.findViewById(R.id.rl_video_hot_reply_item);
                    myHolder.iv_video_hot_reply_item = view.findViewById(R.id.iv_video_hot_reply_item);

                    view.setTag(myHolder);
                } else {
                    myHolder = (MyHolder) view.getTag();
                }




                myHolder.iv_dot_more_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToastDotMore(i, bean);
                    }
                });
                myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(CollectionActivity.this, PersonalDetailActivity.class);
                        intent.putExtra("id", bean.user.user_id + "");
                        intent.putExtra("position", i);
                        startActivityForResult(intent, 1);
                    }
                });


                if (bean.user != null) {
                    PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item);
                    myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
                    ViewStatusUtils.addTags(myHolder.ll_top_tag_container, bean.user.tag, bean.user.tag1);

                    if (!TextUtils.isEmpty(bean.count) && Integer.valueOf(bean.count) >= 2) {
                        myHolder.iv_red_point_header.setVisibility(View.VISIBLE);
                    } else {
                        myHolder.iv_red_point_header.setVisibility(View.GONE);
                    }


                    if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                        myHolder.iv_vip_header.setVisibility(View.VISIBLE);
                    } else {
                        myHolder.iv_vip_header.setVisibility(View.GONE);
                    }

                    myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            requestFollows(i);
                        }
                    });


                    if (false && bean.user.follow_status == 0 && !(SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(bean.user.user_id))) {//系统推荐，显示关注

                        myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
                        myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                        myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                requestFollows(i);
                            }
                        });


                        if (bean.user.follow_status == 1) {
                            myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                            myHolder.tv_follow_dynamic_item.setText("已关注");
                            myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                        } else {
                            myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                            myHolder.tv_follow_dynamic_item.setText("+关注");
                        }


                    } else {
                        myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                        myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);

                        if (bean.recommendation != null && !TextUtils.isEmpty(bean.create_time)) {
                            String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                            myHolder.tv_time_dynamic_item.setText(createTime);
                        } else if (!TextUtils.isEmpty(bean.create_time)) {
                            String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                            myHolder.tv_time_dynamic_item.setText(createTime);
                        } else {
                            myHolder.tv_time_dynamic_item.setText("");
                        }


                    }
                    myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                    myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);
                }



                myHolder.tv_content_dynamic_item.setMaxLines(5);

                myHolder.tv_content_dynamic_item_copy.setVisibility(View.GONE);
                myHolder.ll_comment_type.setVisibility(View.GONE);

                if (bean.type == 6) {
                    myHolder.ll_bottom_item.setVisibility(View.GONE);
                    myHolder.fl_topic_img_item.setVisibility(View.VISIBLE);

                    PictureUtils.show(bean.topic.background_img, myHolder.iv_topic_img);

                    myHolder.tv_title_topic_img.setText("#" + bean.topic.title);

                    if (TextUtils.isEmpty(bean.topic.view_count)) {
                        bean.topic.view_count = "0";
                    }
                    String count = Utils.parseIntToK(Integer.valueOf(bean.topic.view_count));
                    String str = count + " 阅读·" + bean.topic.user_count + " 参与";
                    myHolder.tv_viewcount_topic_img.setText(str);


                } else if (bean.type == 1) {//推荐单
                    myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                    myHolder.fl_topic_img_item.setVisibility(View.GONE);

                    myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                    myHolder.ll_bottom_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                    bindType2(i, bean, myHolder);

                } else if (bean.type == 3) {//话题
                    myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                    myHolder.fl_topic_img_item.setVisibility(View.GONE);

                    myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);


                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);


                    bindType1(i, bean, myHolder);
                }else if(bean.type == 4){
                    myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                    myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);


                    bindtype4(i,bean,myHolder);
                }

            }

                if (i == getCount() - 3 || (getCount() < 3)) {
                    if (pre_page == 1) {
                        page++;
                        rl_load_layout = null;
                        requestNetData();
                    }
                }

            return view;
        }
    }


    /**
     * 进行操作
     * @param bean
     */
    private SelectPicPopupWindow selectPicPopupWindow;
    private boolean isDelete;


    /**
     * 右上角点弹出框
     * @param position
     * @param bean
     */
    private void showToastDotMore(final int position, final DynamicBean.ListBean bean) {
            selectPicPopupWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()){
                        case R.id.btn_share_photo://分享
                            if(selectPicPopupWindow.rg_share_fg.isShown()){
                                selectPicPopupWindow.rg_share_fg.setVisibility(View.GONE);
                                selectPicPopupWindow.btn_share_photo.setText("分享");
                            }else {
                                selectPicPopupWindow.btn_share_photo.setText("取消分享");
                                selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                            }
                            break;
                        case R.id.btn_take_photo://收藏
                            selectPicPopupWindow.dismiss();
                            requestCollect(bean,position);
                            break;
                        case R.id.btn_pick_photo://举报
                            selectPicPopupWindow.dismiss();
                            if(isDelete){
                                toastDelete(bean.comment.comment_id);
                            }else {
//                                ReportActivity.start(CollectionActivity.this, comentsDetailBean.data.comment.user.user_id, comentsDetailBean.data.comment.user.nickname, comentsDetailBean.data.comment.create_time);
                                switch (bean.type){
                                    case 1: //推荐单
                                        ReportActivity.start(CollectionActivity.this, bean.user.user_id, bean.user.nickname, bean.create_time);
                                        break;

                                    case 3://动态
                                        ReportActivity.start(CollectionActivity.this, bean.user.user_id, bean.user.nickname, bean.create_time);
                                        break;

//                                    case 6://话题
//                                        ReportActivity.start(CollectionActivity.this, bean.user.user_id, bean.user.nickname, bean.create_time);
//                                        break;
                                    case 4://分享转发单
                                        ReportActivity.start(CollectionActivity.this, bean.user.user_id, bean.user.nickname, bean.create_time);
                                        break;
                                }
                            }
                            break;
                    }
                }
            }, new SharePopuwind.ShareTypeClickListener() {
                @Override
                public void onTypeClick(int type) {
                    DynamicBean.ListBean beanX = dynamicBean.list.get(position);

                    if(beanX.type == 4){


                        List<String> urls = new ArrayList<>();


                        int typeOfBean;
                        if(TextUtils.isEmpty(beanX.title)){
                            typeOfBean = 1;
                            List<DynamicBean.ListBean.ImageList> imgListEx = beanX.comment.imgListEx;
                            if(imgListEx != null) {
                                for (int i = 0;i<imgListEx.size();i++) {
                                    urls.add(imgListEx.get(i).url);
                                }
                            }
                        }else{
                            typeOfBean = 2;
                            if(!TextUtils.isEmpty(beanX.background_img)){
                                urls.add(beanX.background_img);
                            }

                            else if(beanX.comment.backgroundList != null){
                                List<DynamicBean.ListBean.ImageList> backgroundList = beanX.comment.backgroundList;
                                for(int i =0;i<backgroundList.size();i++){
                                    urls.add(backgroundList.get(i).url);
                                }
                            }

                        }


                        if(TextUtils.isEmpty(beanX.comment.comment)){


                                String imglist = beanX.reply.imglist;
                                if(!TextUtils.isEmpty(imglist)){
                                    urls.add(imglist);
                                }


                        }

                        ShareUtils.shareMsgForShareContent(type, beanX.comment.comment_id,beanX.reply.id,beanX.comment.comment,beanX.reply.reply,beanX.user.nickname,urls,typeOfBean,beanX.title, CollectionActivity.this);

                    }else if (beanX.topic == null) {

                        String videoUrl = "";
                        if(beanX.comment.imgListEx != null) {//http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif

                            if (beanX.comment.imgListEx.size() > 0) {
                                DynamicBean.ListBean.ImageList imageList = bean.comment.imgListEx.get(0);
                                if ("video".equals(imageList.type)) {
                                    videoUrl = imageList.url;
                                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname, videoUrl,CollectionActivity.this);

                                }else{


                                    List<String> urls = new ArrayList<>();


                                    int typeOfBean;
                                    if(TextUtils.isEmpty(beanX.comment.title)){
                                        typeOfBean = 1;
                                        List<DynamicBean.ListBean.ImageList> imgListEx = beanX.comment.imgListEx;
                                        if(imgListEx != null) {
                                            for (int i = 0;i<imgListEx.size();i++) {
                                                urls.add(imgListEx.get(i).url);
                                            }
                                        }
                                    }else{
                                        typeOfBean = 2;
                                        if(!TextUtils.isEmpty(beanX.comment.background_img)){
                                            urls.add(beanX.comment.background_img);
                                        }else if(beanX.comment.backgroundList != null){
                                            List<DynamicBean.ListBean.ImageList> backgroundList = beanX.comment.backgroundList;
                                            for(int i =0;i<backgroundList.size();i++){
                                                urls.add(backgroundList.get(i).url);
                                            }
                                        }

                                    }

                                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname,urls,typeOfBean,beanX.comment.title, CollectionActivity.this);

                                }
                            }
                        }else{

                            List<String> urls = new ArrayList<>();
                            int typeOfBean;
                            if(TextUtils.isEmpty(beanX.comment.title)){
                                typeOfBean = 1;
                            }else{
                                typeOfBean = 2;
                                if(!TextUtils.isEmpty(beanX.comment.background_img)){
                                    urls.add(beanX.comment.background_img);
                                }else if(beanX.comment.backgroundList != null){
                                    List<DynamicBean.ListBean.ImageList> backgroundList = beanX.comment.backgroundList;
                                    for(int i =0;i<backgroundList.size();i++){
                                        urls.add(backgroundList.get(i).url);
                                    }
                                }

                            }
                            ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname,urls,typeOfBean,beanX.comment.title, CollectionActivity.this);

                        }

                    } else {
                        ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname,CollectionActivity.this);
                    }
                }
            });
//        }
        selectPicPopupWindow.btn_share_photo.setVisibility(View.GONE);
        selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
        selectPicPopupWindow.tv_share_title.setVisibility(View.VISIBLE);
        if(bean.type != 6) {
            isDelete = SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(bean.user.user_id);

            if (isDelete  ) {//删除键
                if(bean.type != 1) {
                    selectPicPopupWindow.btn_pick_photo.setText("删除");
                    selectPicPopupWindow.btn_pick_photo.setVisibility(View.VISIBLE);
                }else{
                    selectPicPopupWindow.btn_pick_photo.setVisibility(View.GONE);
                }
            } else {//举报键
                selectPicPopupWindow.btn_pick_photo.setText("举报");
            }
        }else{
            selectPicPopupWindow.btn_pick_photo.setVisibility(View.GONE);
        }
        String collection_status = "";
        switch (bean.type){
            case 1: //推荐单
                if(bean.recommendation.collection_status == null){
                    bean.recommendation.collection_status = "1";
                }
             collection_status =   bean.recommendation.collection_status;
                selectPicPopupWindow.btn_share_photo.setVisibility(View.GONE);
                selectPicPopupWindow.rg_share_fg.setVisibility(View.GONE);
                selectPicPopupWindow.tv_share_title.setVisibility(View.GONE);
                break;

            case 3://动态
                if(bean.comment.collection_status == null){
                  bean.comment.collection_status = "1";
                }
                collection_status =     bean.comment.collection_status;
                break;

            case 6://话题
                if(bean.topic.collection_status == null){
                  bean.topic.collection_status = "1";
                }
                collection_status =    bean.topic.collection_status ;
                break;

            case 4://转发评论
                collection_status = bean.reply.collection_status;
                break;
        }
        if("1".equals(collection_status)){
            selectPicPopupWindow.btn_take_photo.setText("取消收藏");
        }else{
            selectPicPopupWindow.btn_take_photo.setText("收藏");
        }

        selectPicPopupWindow.showAtLocation(lv_dynamic_fg, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

    }


    /**
     * 删除提示
     */
    private ToastPricePopuWindow deleteToast;
    private void toastDelete(final String commentId) {
        String str = "确定要删此内容吗?";
        deleteToast = new ToastPricePopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.tv_cancle_toast_price) {//左边
                    deleteToast.dismiss();
                } else if (v.getId() == R.id.tv_go_toast_price) {//右边
                    delComments(commentId, 1,0);
                }


            }
        }, str, "取消", "确认");
        deleteToast.showAtLocation(tv_title, Gravity.CENTER, 0, 0);
    }


    /**
     * 删除评论
     */
    private void delComments(String replayId, final int subtype, final int position) {
        //http/php/api.php?action=topic&type=del&subtype=2&id=4123
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","del");
        params.put("subtype",subtype);
        params.put("id",replayId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                PublishPointBean publishPointBean = JsonParseUtils.parseJsonClass(string,PublishPointBean.class);
                if(publishPointBean != null && publishPointBean.status != null){
                    if(publishPointBean.status.errno == 0){
                          ToastUtils.toast("删除成功");
                          if(dynamicBean != null) {
                              dynamicBean.list.remove(position);
                              myAdapter.notifyDataSetChanged();
                          }

                    }else{
                        ToastUtils.toast("删除失败");
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
                if(deleteToast != null){
                    deleteToast.dismiss();
                }
            }
        });
    }


    private void requestCollect(final DynamicBean.ListBean bean, final int position) {
        String url = Constant.BASE_URL + "/php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","collection");
        /**
         * match:1 comment:2 recommendation:5 topic:6
         */

        switch (bean.type){
            case 1: //推荐单
                params.put("subtype",5);
                params.put("id",bean.recommendation.id);
                break;

            case 3://动态
                params.put("subtype",2);
                params.put("id",bean.comment.comment_id);

                break;

            case 6://话题
                params.put("subtype",6);
                params.put("id",bean.topic.id);
                break;
            case 4://转发评论单
                params.put("subtype",4);
                params.put("id",bean.reply.id);
                break;
        }

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
                if(reportSubmitBean.status.result == 1){
                    ToastUtils.toast("收藏成功");
//                    comentsDetailBean.data.comment.collection_status = "1";
                    switch (bean.type){
                        case 1: //推荐单
                          bean.recommendation.collection_status = "1";
                            break;

                        case 3://动态
                          bean.comment.collection_status = "1";
                            break;

                        case 6://话题
                           bean.topic.collection_status = "1";
                            break;
                        case 4:
                            bean.reply.collection_status = "1";
                            break;
                    }
                }else if(reportSubmitBean.status.result == 0){
                    ToastUtils.toast("取消成功");
                    switch (bean.type){
                        case 1: //推荐单
                            bean.recommendation.collection_status = "0";
                            break;

                        case 3://动态
                            bean.comment.collection_status = "0";
                            break;

                        case 6://话题
                            bean.topic.collection_status = "0";
                            break;
                        case 4:
                            bean.reply.collection_status = "0";
                            break;
                    }
                    dynamicBean.list.remove(position);
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    private void deleteCollected(final DynamicBean.ListBean bean, final int position) {
        String url = Constant.BASE_URL + "/php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","collection");



        params.put("subtype",bean.subtype+"");
        params.put("id", bean.provider_id+"");




        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
               if(reportSubmitBean.status.result == 0){
                   dynamicBean.list.remove(position);
                   myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    public void requestFollows(int i){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        final DynamicBean.ListBean bean = dynamicBean.list.get(i);
        if(bean.user == null){
            return;
        }
        int follow_status = bean.user.follow_status;
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";
        if(follow_status == 1){
            type = "delete";
        }else{
            type = "add";
        }
        params.put("type",type);
        params.put("user_id",bean.user.user_id+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        bean.user.follow_status = 1;
                        NotificationsUtils.checkNotificationAndStartSetting(CollectionActivity.this,lv_dynamic_fg);
                    }else{
                        bean.user.follow_status = 0;
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    /**
     * 评论类型
     * @param i
     * @param bean
     * @param myHolder
     */
    private void bindtype4(final int i, final DynamicBean.ListBean bean, final MyHolder myHolder) {


        if (bean.reply == null ) {
            return;
        }
        myHolder.ll_comment_type.setVisibility(View.VISIBLE);


        myHolder.tv_content_dynamic_item.setVisibility(View.GONE);



        myHolder.ll_comment_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComentsDetailsActivity.startForResult(2, CollectionActivity.this, bean.comment.comment_id, i);

            }
        });

        HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user;

        if (!bean.covert) {

            String time = "";
            time = bean.create_time;
            bean.create_time = bean.reply.create_time;
            bean.reply.create_time = time;

            user = bean.user;
            bean.user = bean.reply.user;
            bean.reply.user = user;

            bean.covert = true;
        }

        setComenHead(i, bean, myHolder);



        myHolder.tv_content_dynamic_item_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareComentsDetailsActivity.startForResult(2, CollectionActivity.this, ComentsDetailsActivity.SHARE_TYPE, bean.comment.comment_id, bean, i);

            }
        });

        if (bean.reply != null) {

            myHolder.tv_content_dynamic_item_copy.setVisibility(View.VISIBLE);
            ShareReplyUtils.bindReplyLink(CollectionActivity.this,myHolder.tv_content_dynamic_item_copy,bean.reply,bean.reply.parentList);



            ShareReplyUtils.bindShareOriginal(CollectionActivity.this,myHolder.ll_comment_type,myHolder.tv_content_comment_type_item,bean.reply,bean.comment,i);


            if (bean.comment != null && bean.comment.imgListEx != null && bean.comment.imgListEx.size() > 0) {


                DynamicBean.ListBean.ImageList imageList = bean.comment.imgListEx.get(0);
                String type = imageList.type;
                if ("video".equals(type)) {


                    String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                    myHolder.rl_video_hot_reply_item.setVisibility(View.VISIBLE);
                    myHolder.gv_comment_type_item.setVisibility(View.GONE);
                    PictureUtils.show(imgUrl, myHolder.iv_video_hot_reply_item);

                    myHolder.rl_video_hot_reply_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ComentsDetailsActivity.startForResult(true, 2, CollectionActivity.this, bean.comment.comment_id, i);

                        }
                    });

                } else {

                    MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_comment_type_item.getAdapter();

                    myHolder.gv_comment_type_item.setVisibility(View.VISIBLE);

                    if (adapter != null) {
                        adapter.setData(bean.comment.imgListEx);
                        adapter.notifyDataSetChanged();
                    } else {
                        myHolder.gv_comment_type_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    }

                    if (bean.comment.imgListEx.size() == 4 || bean.comment.imgListEx.size() == 2) {
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_comment_type_item, 2);
                    } else if (bean.comment.imgListEx.size() == 1) {
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_comment_type_item, 1);
                    } else {
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_comment_type_item, 3);
                    }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    myHolder.gv_comment_type_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positi, long id) {

                            if (bean.comment.imgListEx.size() == 7 && positi >= 7) {
                                //      toJsonCommentBean(bean);
                                // ComentsDetailsActivity.startForResult(2, DynamicFragment.this,, position);
//                            ToastUtils.toast("热评详情");
                                return;
                            }

                            String urls = "";
                            for (int i = 0; i < bean.comment.imgListEx.size(); i++) {
                                String url = bean.comment.imgListEx.get(i).url;
                                String delUrl = Utils.delUrl(url);
                                urls += delUrl + ",";
                            }

                            String substring = urls.substring(0, urls.length() - 1);
                            Intent intent = new Intent(CollectionActivity.this, PicShowActivity.class);
                            intent.putExtra("urls", substring);
                            intent.putExtra("position", positi);
                            startActivity(intent);
                        }
                    });
                }
            } else {
                myHolder.gv_comment_type_item.setVisibility(View.GONE);
            }





        if (!TextUtils.isEmpty(bean.reply.view_count)) {
            String s = Utils.parseIntToK(Integer.valueOf(bean.reply.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 " + s);
        } else {
            myHolder.tv_view_count_topic_item.setText("阅读 " + "1");
        }

        myHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestThumbup(bean);
            }
        });

        myHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(CollectionActivity.this, ACTION);
                    return;
                }

                ShareComentsDetailsActivity.startForResult(2, CollectionActivity.this, ComentsDetailsActivity.SHARE_TYPE, bean.comment.comment_id, bean, i);


            }
        });

        myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
                showShare(i);
            }
        });
        if (bean.reply.praise_status == 1) {//点赞
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable, null, null, null);
        }

        DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item, bean.reply.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item, bean.reply.reply_count);

    }
    }


    /**
     * 设置公共头部  item  昵称头像等
     * @param i
     * @param bean
     * @param myHolder
     */
    private void setComenHead(int i, DynamicBean.ListBean bean, MyHolder myHolder){
        PictureUtils.showCircle(bean.user.headurl+"",myHolder.iv_head_dynamic_item);


        myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
        myHolder.ll_top_tag_container.setVisibility(View.GONE);
        myHolder.iv_red_point_header.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(bean.user.verify_type)&&Integer.valueOf(bean.user.verify_type)>0){
            myHolder.iv_vip_header.setVisibility(View.VISIBLE);
        }else {
            myHolder.iv_vip_header.setVisibility(View.GONE);
        }

        if(false && bean.sys_recommendation == 1 ){//系统推荐，显示关注

            myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
            myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                        requestThumbup(bean);
                }
            });


            if(bean.follow == 1){
                myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                myHolder.tv_follow_dynamic_item.setText("已关注");
                myHolder.tv_follow_dynamic_item.setOnClickListener(null);
            }else{
                myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black));
                myHolder.tv_follow_dynamic_item.setText("+关注");
            }


        }else{
            myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
        }
    }




    /**
     * 话题类型
     * @param bean
     * @param myHolder
     */
    private void bindType1(final int position, final DynamicBean.ListBean bean, MyHolder myHolder) {

        if(!TextUtils.isEmpty(bean.comment.comment)){
            bean.comment.comment =  bean.comment.comment.replaceAll("&nbsp;"," ").replaceAll("<br/>"," ").replaceAll("\n"," ").replaceAll("<[^>]*>|\\n","");

            if(TextUtils.isEmpty(bean.comment.background_img) && bean.comment.backgroundList != null){

                String url = "";
                for(int i = 0;i<bean.comment.backgroundList.size();i++){
                    String url1 = bean.comment.backgroundList.get(i).url;
                    if(!url1.endsWith(".gif")){
                        url = url1;
                        break;
                    }
                }

                bean.comment.background_img = url;
            }


            if(TextUtils.isEmpty(bean.comment.background_img)){
                if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                    myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                    myHolder.tv_title_long_content.setText(bean.comment.title);
                    myHolder.tv_content_long_content.setText(bean.comment.comment);

                }else {//普通评论
                    myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                    myHolder.tv_content_dynamic_item.setText(bean.comment.comment);
                }

            }else{
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.VISIBLE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                myHolder.tv_content_bg_dynamic_item.setText(bean.comment.title);
                myHolder.tv_content_bottom.setText(bean.comment.comment+"");
                PictureUtils.show(bean.comment.background_img,myHolder.iv_content_bg_item);
            }

        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
            myHolder.rl_bg_content_item.setVisibility(View.GONE);
            myHolder.rl_bg_long_content_item.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                myHolder.tv_title_long_content.setText(bean.comment.title);
                myHolder.tv_content_long_content.setText(bean.comment.comment);

            }
        }

        if(!TextUtils.isEmpty(bean.comment.view_count)){
            String s = Utils.parseIntToK(Integer.valueOf(bean.comment.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 "+s);
        }






        if(bean.comment.imgListEx != null){//http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif

            if(bean.comment.imgListEx.size()>0){
                if("video".equals(bean.comment.imgListEx.get(0).type)){
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.VISIBLE);
                    DynamicBean.ListBean.ImageList imageList = bean.comment.imgListEx.get(0);
                    String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                    PictureUtils.show(imgUrl,myHolder.iv_video_item);
                    myHolder.rl_video_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ComentsDetailsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);
                            if(bean.type == 3 && isShootVideo(bean)){
                                toShootVideoPlayer(bean,position);
                            }else {
                                ComentsDetailsActivity.startForResult(true, 2, CollectionActivity.this, bean.comment.comment_id, position);
                            }
                        }
                    });

                    int heightVideo = (int) (DisplayUtil.getScreenWidth(CollectionActivity.this) / 16.0 * 8.7);
                    myHolder.iv_video_item.getLayoutParams().height = heightVideo;
                }else{
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);

                    MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_dynamic_item.getAdapter();



                    if (adapter != null) {
                        adapter.setData(bean.comment.imgListEx);
                        adapter.notifyDataSetChanged();
                    }else {
                        myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    }

                    if(bean.comment.imgListEx.size() == 4 || bean.comment.imgListEx.size() == 2){
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,2);
                    }else if(bean.comment.imgListEx.size() == 1){
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,1);
                    }else{
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,3);
                    }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    myHolder.gv_dynamic_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positi, long id) {

                            if(bean.comment.imgListEx.size() == 7 && positi>=7){
                                ComentsDetailsActivity.startForResult(2, CollectionActivity.this, bean.comment.comment_id, position);
                                return;
                            }

                            String urls = "";
                            for(int i = 0;i<bean.comment.imgListEx.size();i++){
                                String url = bean.comment.imgListEx.get(i).url;
                                String delUrl = Utils.delUrl(url);
                                urls +=  delUrl+ ",";
                            }

                            String substring = urls.substring(0, urls.length() - 1);
                            Intent intent = new Intent(CollectionActivity.this,PicShowActivity.class);
                            intent.putExtra("urls",substring);
                            intent.putExtra("position", positi);
                            startActivity(intent);
                        }
                    });



                }
            }else{
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
            }


        }

        if(bean.topic != null){
            myHolder.rl_topic_dynamic_item.setVisibility(View.VISIBLE);
            PictureUtils.show(bean.topic.background_img,myHolder.iv_img_left);
            myHolder.tv_bottom_title_topic_item.setText(bean.topic.title);
            myHolder.rl_topic_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity.start(CollectionActivity.this,bean.topic.topic_id);
                }
            });
        }

        myHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestThumbup(bean);
            }
        });


        myHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(CollectionActivity.this,HomeNewFragment.ACTION);
                    return;
                }
                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
//                WebViewActivtiy.start(DynamicFragment.this.getActivity(),url,"球咖");
//                ComentsDetailsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);
                if(bean.type == 3 && isShootVideo(bean)){
                    toShootVideoPlayer(bean,position);
                }else {
                    ComentsDetailsActivity.startForResult(2, CollectionActivity.this, bean.comment.comment_id, position, true);
                }
//                if(LoginUtils.isUnLogin()){
//                    LoginUtils.goLoginActivity(DynamicFragment.this.getActivity(),DynamicFragment.ACTION);
//                    return;
//                }
//                ReplayCommentsActivity.start(DynamicFragment.this.getActivity(),bean.comment.comment_id);

            }
        });


        myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
                showShare(position);
            }
        });

        if(bean.comment.praise_status == 1){//点赞
            Drawable drawable= getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
        }else {
            Drawable drawable= getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable,null,null,null);
        }

        DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item,bean.comment.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.comment.reply_count+"");
//        myHolder.tv_comments_topic_item.setText(bean.comment.reply_count+"");

    }

    class MyGvAdapter extends BaseAdapter{


        private List<DynamicBean.ListBean.ImageList> imgListEx;


        public MyGvAdapter(List<DynamicBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }

        @Override
        public int getCount() {

            if(imgListEx.size() == 7){
                return 9;
            }

            return imgListEx.size();
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

            try {
                DynamicBean.ListBean.ImageList imageList = imgListEx.get(position);

                View view = View.inflate(QCaApplication.getContext(), R.layout.img_gif_layout, null);
                ImageView imageView = view.findViewById(R.id.iv);
                TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
                Utils.showType(imageList.url, imageView, tv_gif_icon, imageList.size);

                if (getCount() == 1) {
                    Utils.scaleImg(CollectionActivity.this, view, imageView, imageList.size);
                }

                convertView = view;
            }catch (Exception e){
                convertView = View.inflate(QCaApplication.getContext(), R.layout.img_gif_layout, null);
                ImageView imageView = convertView.findViewById(R.id.iv);
                TextView tv_gif_icon = convertView.findViewById(R.id.tv_gif_icon);
                DynamicBean.ListBean.ImageList imageList = imgListEx.get(0);
                Utils.showType(imageList.url, imageView, tv_gif_icon, imageList.size);

                imageView.setVisibility(View.INVISIBLE);
                tv_gif_icon.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

        public void setData(List<DynamicBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }
    }
    private void showShare(final int position) {
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                DynamicBean.ListBean beanX = dynamicBean.list.get(position);
                if (beanX.topic == null) {
                    List<String> urls = new ArrayList<>();


                    int typeOfBean;
                    if(TextUtils.isEmpty(beanX.comment.title)){
                        typeOfBean = 1;
                        List<DynamicBean.ListBean.ImageList> imgListEx = beanX.comment.imgListEx;
                        if(imgListEx != null) {
                            for (int i = 0;i<imgListEx.size();i++) {
                                urls.add(imgListEx.get(i).url);
                            }
                        }
                    }else{
                        typeOfBean = 2;
                        if(!TextUtils.isEmpty(beanX.comment.background_img)){
                            urls.add(beanX.comment.background_img);
                        }else if(beanX.comment.backgroundList != null){
                            List<DynamicBean.ListBean.ImageList> backgroundList = beanX.comment.backgroundList;
                            for(int i =0;i<backgroundList.size();i++){
                                urls.add(backgroundList.get(i).url);
                            }
                        }

                    }
                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname,urls,typeOfBean,beanX.comment.title,CollectionActivity.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname,CollectionActivity.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_dynamic_fg, Gravity.CENTER, 0, 0);

    }


    /**
     * 处理点赞
     * @param bean
     */
    private void requestThumbup(final DynamicBean.ListBean bean) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","topic");
        if(bean.type == 4){//转发评论点赞
            params.put("type","praise_reply");
            params.put("id",bean.reply.id);
        }else {
            params.put("type", "praise");
            params.put("id", bean.comment.comment_id);
        }

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    if(bean.type == 4){//转发评论点赞
                        bean.reply.praise_count = praiseBean.data.praise_count;
                        bean.reply.praise_status = praiseBean.data.praise_status;
                    }else {
                        bean.comment.praise_count = praiseBean.data.praise_count;
                        bean.comment.praise_status = praiseBean.data.praise_status;
                    }
                    myAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.toast(praiseBean.status.errstr);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });

    }


    /**
     * 推荐单
     * @param bean
     * @param myHolder
     */
    private void bindType2(final int position, final DynamicBean.ListBean bean, MyHolder myHolder) {

        if(!TextUtils.isEmpty(bean.recommendation.title)){
            myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
            myHolder.tv_content_dynamic_item.setText(bean.recommendation.title);
        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
        }

        myHolder.tv_team_name_dynamic_item.setText(bean.match.hostTeamName + "\t\tvs\t\t" + bean.match.awayTeamName);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        spannableStringBuilder.append(bean.match.leagueName);
        myHolder.tv_type_dynamic_item.setText(bean.recommendation.type+"");
        if (!TextUtils.isEmpty(bean.match.match_time)) {
            String matchStartTime = Utils.getMatchStartTime(Long.valueOf(bean.match.match_time + "000"));
            spannableStringBuilder.append(" · "+matchStartTime);
        }

        if(bean.recommendation.reason_len >0){
            spannableStringBuilder.append(" · "+bean.recommendation.reason_len+"字");
        }

        /**
         * 不中退款
         */
        if("1".equals(bean.recommendation.return_if_wrong)){
            spannableStringBuilder.append(" · "+"不中退款");
        }


        if(bean.match.status.equals("COMPLETE")){//一结束     1
            myHolder.ll_price.setVisibility(View.GONE);
            myHolder.tv_right_item.setVisibility(View.VISIBLE);
//            myHolder.iv_result_personal.setVisibility(View.VISIBLE);
            myHolder.tv_buy.setVisibility(View.GONE);
            spannableStringBuilder.append(" · "+bean.recommendation.type);
        }else{
            if(bean.recommendation.buy == 1){
                spannableStringBuilder.append(" · "+bean.recommendation.type);
                myHolder.tv_buy.setVisibility(View.VISIBLE);
                myHolder.ll_price.setVisibility(View.GONE);
                myHolder.tv_right_item.setVisibility(View.VISIBLE);
            }else{
                myHolder.tv_buy.setVisibility(View.GONE);
                myHolder.ll_price.setVisibility(View.VISIBLE);
                myHolder.tv_right_item.setVisibility(View.GONE);
            }
        }

        if(!bean.match.status.equals("COMPLETE")) {
            if (!TextUtils.isEmpty(bean.recommendation.price)&&!"0".equals(bean.recommendation.price) ) {
                Integer integer = Integer.valueOf(bean.recommendation.price);
                String price = integer + "金币";
                myHolder.tv_price_dynamic_item.setText(price);
            }else{
                myHolder.tv_price_dynamic_item.setText("免费");
            }
        }else{
        }


        myHolder.tv_bottom_dynamic_item.setText(spannableStringBuilder);
        spannableStringBuilder.clear();
    }


    static class MyHolder{
        public LinearLayout ll_top_click;
        public ImageView iv_head_dynamic_item;
        public TextView tv_name_dynamic_item;
        public LinearLayout ll_top_tag_container;
        public TextView tv_time_dynamic_item;
        public TextView tv_content_dynamic_item;
        public LinearLayout ll_groom_dynamic_item;
        public TextView tv_team_name_dynamic_item;
        public TextView tv_bottom_dynamic_item;
        public TextView tv_type_dynamic_item;
        public TextView tv_price_dynamic_item;
        public LinearLayout ll_img_container_dynamic_item;
        public RelativeLayout rl_topic_dynamic_item;
        public ImageView iv_img_left;
        public TextView tv_bottom_title_topic_item;
        public TextView tv_view_count_topic_item;
        public LinearLayout ll_bottom_dynamic_item;
        public TextView tv_thumbup_topic_item;
        public TextView tv_comments_topic_item;
        public LinearLayout ll_price;
        public TextView tv_follow_dynamic_item;
        public ImageView iv_vip_header;
        public TextView iv_red_point_header;
        public TextView tv_buy;
        public TextView tv_right_item;

        public RelativeLayout rl_video_item;
        public ImageView iv_video_item;

        public RelativeLayout rl_bg_content_item;
        public TextView tv_content_bg_dynamic_item;
        public ImageView iv_content_bg_item;
        public GridView gv_dynamic_item;
        public RelativeLayout rl_bg_long_content_item;
        public TextView tv_content_long_content;
        public TextView tv_title_long_content;

        public TextView tv_share_topic_item;


        /**
         * 话题
         */

        public FrameLayout fl_topic_img_item;
        public ImageView iv_topic_img;
        public TextView tv_title_topic_img;
        public TextView tv_viewcount_topic_img;

        public LinearLayout ll_bottom_item;


        public ImageView iv_dot_more_item;

        public TextView tv_content_bottom;

        public LinearLayout ll_comment_type;
        public TextView tv_content_comment_type_item;
        public GridView gv_comment_type_item;
        public TextView tv_detail_title;
        public RelativeLayout rl_video_hot_reply_item;
        public ImageView iv_video_hot_reply_item;
        public TextView tv_content_dynamic_item_copy;
    }
}
