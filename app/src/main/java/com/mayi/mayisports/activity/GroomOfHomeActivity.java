package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.HomeDataNewBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.MyPopuWindow;
import com.mayisports.qca.view.ToastPricePopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 专家推荐
 */
public class GroomOfHomeActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,GroomOfHomeActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_groom_of_home;
    }

    private XRefreshView xfv_home_fg;
    private ListView lv_home_fg;

    private RelativeLayout rl_load_layout;
    private RelativeLayout rl_load_clone ;
    MyPopuWindow myPopuWindow ;
    private LinearLayout ll_no_data;

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("专家推荐");
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rl_load_layout = findViewById(R.id.rl_load_layout);
        rl_load_clone = rl_load_layout;

        myPopuWindow = new MyPopuWindow(this,this,0,0);
        xfv_home_fg = findViewById(R.id.xfv_home_fg);
        lv_home_fg = findViewById(R.id.lv_home_fg);
        ll_no_data = findViewById(R.id.ll_no_data);

        xfv_home_fg.setPullRefreshEnable(true);//设置允许下拉刷新
        xfv_home_fg.setMoveForHorizontal(true);
        xfv_home_fg.setXRefreshViewListener(new MyListener());


        lv_home_fg.setAdapter(myAdapter);
        lv_home_fg.setOnItemClickListener(this);


    }

    @Override
    protected void initDatas() {
        super.initDatas();
        flag = 1;
        requestNetData();
    }


    private HomeDataNewBean homeDataBean;
    private MyAdapter myAdapter = new MyAdapter();
    private int flag = 1;//1
    private int pageIndex = 1;
    private  int pre_page = 1;
    private void requestNetData() {
        String  url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        if(flag == 1){
            params.put("action","user_recommendation");
            params.put("type","recommendation_list");
            //下拉刷新
            pageIndex = 0;
            params.put("start",pageIndex);

            //恢复加载更多页数
        }else{ //action=user_recommendation&type=recommendation_list&start=2
            params.put("action","user_recommendation");
            params.put("type","recommendation_list");

            params.put("start",++pageIndex);
        }

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {
                if(xfv_home_fg != null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }
                if(flag == 1) {
                    homeDataBean = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                    if(homeDataBean.meta != null){
                        saveTimeCount(homeDataBean.meta);
                    }
                    HomeDataNewBean newBean = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                    delData(homeDataBean,newBean);
                    pre_page = 1;
                }else{
                    try{
                        HomeDataNewBean homeDataBeanNew = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                        HomeDataNewBean newBean = JsonParseUtils.parseJsonClass(string, HomeDataNewBean.class);
                        delData(homeDataBeanNew,newBean);
                        homeDataBean.data.recommendation.addAll(homeDataBeanNew.data.recommendation);
                    }catch (Exception e){
                        pre_page = 0;
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                if(xfv_home_fg !=  null) {
                    xfv_home_fg.stopRefresh();
                    xfv_home_fg.stopLoadMore();
                }
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_clone = rl_load_layout;
            }
        });
    }

    /**
     * 保存更新时间
     * @param meta
     */
    private void saveTimeCount(HomeDataNewBean.MetaBean meta) {
//        SPUtils.putInt(this.getContext(),Constant.Match_Thread_Interval,meta.match_thread_interval);
//        SPUtils.putInt(this.getContext(),Constant.Banner_Interval,meta.banner_interval);
//        SPUtils.putInt(this.getContext(),Constant.Sys_Msg_Interval,meta.sys_msg_interval);
//
//        int anInt = SPUtils.getInt(this.getContext(), Constant.Banner_Interval);
//        if(anInt == 0)anInt = 4;
//        if(banner != null) banner.setDelayTime(1000*anInt);
    }

    /**
     * 处理数据  type 1 显示
     * @param homeDataBean
     * @param newBean
     */
    private void delData(HomeDataNewBean homeDataBean, HomeDataNewBean newBean) {
        if(homeDataBean.data != null && homeDataBean.data.recommendation != null){
            homeDataBean.data.recommendation.clear();
            for(int i = 0;i<newBean.data.recommendation.size();i++){

                HomeDataNewBean.DataBeanX.RecommendationBeanX beanX = newBean.data.recommendation.get(i);
                if(beanX.type == 1) {//1推荐单  3 话题
                    homeDataBean.data.recommendation.add(beanX);
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_cancle_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                break;
            case R.id.tv_go_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                if(type == 1){//确定
                    HomeDataNewBean.DataBeanX.RecommendationBeanX beanX = homeDataBean.data.recommendation.get(clickPosition);
                    beanX.recommendation.buy = 1;
                    myAdapter.notifyDataSetChanged();
                    goDetail(beanX);
                }else{//充值
                    Intent intent = new Intent(GroomOfHomeActivity.this, CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;

        }
    }



    private int clickPosition;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        HomeDataNewBean.DataBeanX.RecommendationBeanX bean = homeDataBean.data.recommendation.get(i);

        if(bean.type == 1){//推荐单
            delType2Click(bean,i);
        }else if(bean.type == 3){//话题
            if(bean.topic != null){
                String topic_id = bean.topic.topic_id;
//                WebViewActivtiy.start(this, Constant.BASE_URL+"#/topic/"+topic_id,"话题");
                TopicDetailActivity.start(this,topic_id);
            }
            myAdapter.notifyDataSetChanged();
        }
    }


    private void delType2Click(HomeDataNewBean.DataBeanX.RecommendationBeanX bean, int i) {
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
            intent.putExtra("betId",bean.recommendation.betId);
            startActivity(intent);
        }else{
            clickPosition = i;
            requestToast(bean.user.user_id,bean.recommendation.betId);
        }
    }

    private void goDetail(HomeDataNewBean.DataBeanX.RecommendationBeanX bean){
        Intent intent = new Intent(this, HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
        intent.putExtra("betId",bean.recommendation.betId);
        startActivity(intent);
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
                toastPricePopuWindow =   new ToastPricePopuWindow(GroomOfHomeActivity.this,GroomOfHomeActivity.this,title,type);
                toastPricePopuWindow.showAtLocation(lv_home_fg, Gravity.CENTER,0,0);
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


    /**
     * 加载刷新监听
     */
    class MyListener extends XRefreshView.SimpleXRefreshListener {
        @Override
        public void onRefresh(boolean isPullDown) {
            rl_load_clone = null;
            flag = 1;
            requestNetData();
        }
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(homeDataBean != null && homeDataBean.data!= null &&homeDataBean.data.recommendation!= null) {
                int size = homeDataBean.data.recommendation.size();
                if(size == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                }else{
                    ll_no_data.setVisibility(View.GONE);
                }
                return size;
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            final MyHolder myHolder;
            if(view == null){
                myHolder = new MyHolder();
                view = View.inflate(QCaApplication.getContext(),R.layout.dynamic_item,null);
                myHolder.ll_top_click = view.findViewById(R.id.ll_top_click);
                myHolder.tv_buy = view.findViewById(R.id.tv_buy);
                myHolder.iv_head_dynamic_item = view.findViewById(R.id.iv_head_dynamic_item);
                myHolder.tv_name_dynamic_item = view.findViewById(R.id.tv_name_dynamic_item);
                myHolder.iv_red_point_header = view.findViewById(R.id.iv_red_point_header);
                myHolder.ll_top_tag_container = view.findViewById(R.id.ll_top_tag_container);
                myHolder.tv_time_dynamic_item = view.findViewById(R.id.tv_time_dynamic_item);
                myHolder.tv_content_dynamic_item = view.findViewById(R.id.tv_content_dynamic_item);
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
                view.setTag(myHolder);
            }else{
                myHolder = (MyHolder) view.getTag();
            }


            final   HomeDataNewBean.DataBeanX.RecommendationBeanX bean = homeDataBean.data.recommendation.get(i);


            myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GroomOfHomeActivity.this, PersonalDetailActivity.class);
                    intent.putExtra("id", bean.user.user_id + "");
                    intent.putExtra("position", i);
                    String json = new Gson().toJson(bean);
                    PersonalDetailActivity.start(GroomOfHomeActivity.this,bean.user.user_id,i,1,json);
//                        startActivityForResult(intent, 1);
                }
            });


            PictureUtils.showCircle(bean.user.headurl+"",myHolder.iv_head_dynamic_item);
            myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
            ViewStatusUtils.addTags(myHolder.ll_top_tag_container,bean.user.tag,bean.user.tag1);

            myHolder.iv_red_point_header.setVisibility(View.GONE);


            if(!TextUtils.isEmpty(bean.user.verify_type)&&Integer.valueOf(bean.user.verify_type)>0){
                myHolder.iv_vip_header.setVisibility(View.VISIBLE);
            }else {
                myHolder.iv_vip_header.setVisibility(View.GONE);
            }

            if(false&&bean.user.follow_status == 0&&!(SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID).equals(bean.user.user_id))){//系统推荐，显示关注

                myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        requestFollows(i);
                    }
                });


                if(bean.user.follow_status == 1){
                    myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                    myHolder.tv_follow_dynamic_item.setText("已关注");
                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                }else{
                    myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                    myHolder.tv_follow_dynamic_item.setText("+关注");
                }


            }else{
                myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);
                if(bean.recommendation != null && !TextUtils.isEmpty(bean.recommendation.create_time)) {
                    String createTime = Utils.getCreateTime(Long.valueOf(bean.recommendation.create_time + "000"));
                    myHolder.tv_time_dynamic_item.setText(createTime);
                }else if(!TextUtils.isEmpty(bean.create_time)){
                    String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                    myHolder.tv_time_dynamic_item.setText(createTime);
                }else{
                    myHolder.tv_time_dynamic_item.setText("");
                }
            }




            if(bean.type == 1){//推荐单
                myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                myHolder.ll_bottom_dynamic_item.setVisibility(View.GONE);

                bindType2(bean,myHolder);

            }else if(bean.type == 3){//话题
                myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);

                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);


                bindType1(bean,myHolder);
            }

            if(i == getCount() - 3 ||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_clone = null;
                    flag = 0;
                    requestNetData();
                }
            }

            return view;
        }
    }
    static class MyHolder{
        public ImageView iv_head_home_item;
        public TextView tv_name_home_item;
        public TextView tv_time_home_item;
        public TextView tv_content_home_item;
        public TextView tv_bottom_home_item;

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
                HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user = homeDataBean.data.recommendation.get(position).user;
                user.follow_status = follow_status;
                delFollow(follow_status,user.user_id);
                myAdapter.notifyDataSetChanged();
            }

        }
    }

    /**
     * 请求关注
     * @param i
     */
    public void requestFollows(int i){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        final HomeDataNewBean.DataBeanX.RecommendationBeanX bean = homeDataBean.data.recommendation.get(i);
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";
        if(bean.user.follow_status == 1){
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
                        delFollow(1,bean.user.user_id);
                        NotificationsUtils.checkNotificationAndStartSetting(GroomOfHomeActivity.this,lv_home_fg);
                    }else{
                        bean.user.follow_status = 0;
                        delFollow(0,bean.user.user_id);
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
     * 处理关注状态
     * @param type
     * @param userId
     */
    private void delFollow(int type,String userId) {
        for(int i = 0;i<homeDataBean.data.recommendation.size();i++){
            HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user = homeDataBean.data.recommendation.get(i).user;
            String user_id = user.user_id;
            if(user_id.equals(userId)){
                user.follow_status = type;
            }
        }
    }


    /**
     * 话题
     * @param bean
     * @param myHolder
     */
    private void bindType1(final HomeDataNewBean.DataBeanX.RecommendationBeanX bean, MyHolder myHolder) {

        if(!TextUtils.isEmpty(bean.comment.comment)){
            myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
            myHolder.tv_content_dynamic_item.setText(bean.comment.comment);
        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
        }


        myHolder.tv_content_dynamic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {  //#/topicViewDetail/36979
                ComentsDetailsActivity.start(GroomOfHomeActivity.this,bean.comment.comment_id);
            }
        });





        if(bean.comment.imglist != null){ //http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif
            myHolder.ll_img_container_dynamic_item.setVisibility(View.VISIBLE);
            for(int j = 0;j<myHolder.ll_img_container_dynamic_item.getChildCount();j++){
                myHolder.ll_img_container_dynamic_item.getChildAt(j).setVisibility(View.INVISIBLE);
            }
            String urls = "";
            for(int i = 0;i<bean.comment.imglist.size();i++){
                if(i>2)break;
                String imgUrl = bean.comment.imglist.get(i);
                if(TextUtils.isEmpty(imgUrl)){
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    break;
                }
                urls+=imgUrl+",";
            }
            for(int i = 0;i<bean.comment.imglist.size();i++){
                if(i>2)break;
                String imgUrl = bean.comment.imglist.get(i);
                if(TextUtils.isEmpty(imgUrl)){
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    break;
                }
                ImageView imageView = (ImageView) myHolder.ll_img_container_dynamic_item.getChildAt(i);
                final int finalI = i;
                final String finalUrls = urls;
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(finalUrls.length()>0){
                            String substring = finalUrls.substring(0, finalUrls.length() - 1);
                            Intent intent = new Intent(GroomOfHomeActivity.this,PicShowActivity.class);
                            intent.putExtra("urls",substring);
                            intent.putExtra("position", finalI);
                            startActivity(intent);
                        }
                    }
                });
                imageView.setVisibility(View.VISIBLE);

                PictureUtils.show(imgUrl, imageView);
            }

//            myHolder.ll_img_container_dynamic_item.setOnClickListener(new View.OnClickListener() {
//               @Override
//               public void onClick(View view) {
////                   if(finalUrls.length()>0){
////                       String substring = finalUrls.substring(0, finalUrls.length() - 1);
////                       Intent intent = new Intent(DynamicFragment.this.getActivity(),PicShowActivity.class);
////                       intent.putExtra("urls",substring);
////                       intent.putExtra("position",)
////                       startActivity(intent);
////                   }
//               }
//           });
        }

        if(bean.topic != null){
            myHolder.rl_topic_dynamic_item.setVisibility(View.VISIBLE);
            PictureUtils.show(bean.topic.background_img,myHolder.iv_img_left);
            myHolder.tv_bottom_title_topic_item.setText(bean.topic.title);
//            myHolder.tv_view_count_topic_item.setText(bean.topic.view_count+" 阅读");
        }

        if(!TextUtils.isEmpty(bean.comment.view_count)){
            String s = Utils.parseIntToK(Integer.valueOf(bean.comment.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 "+s);
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
                    LoginUtils.goLoginActivity(GroomOfHomeActivity.this,HomeNewFragment.ACTION);
                    return;
                }
//                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
//                WebViewActivtiy.start(GroomOfHomeActivity.this,url,"球咖");
                ComentsDetailsActivity.start(GroomOfHomeActivity.this,bean.comment.comment_id);
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

        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        myHolder.tv_comments_topic_item.setText(bean.comment.reply_count+"");

    }

    /**
     * 处理点赞
     * @param bean
     */
    private void requestThumbup(final HomeDataNewBean.DataBeanX.RecommendationBeanX  bean) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","praise");
        params.put("id",bean.comment.comment_id);
        rl_load_clone = rl_load_layout;
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    bean.comment.praise_count = praiseBean.data.praise_count;
                    bean.comment.praise_status = praiseBean.data.praise_status;
                    myAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.toast("点赞失败");
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(strMsg);
            }
        });

    }


    /**
     * 推荐单
     * @param bean
     * @param myHolder
     */
    private void bindType2(final HomeDataNewBean.DataBeanX.RecommendationBeanX bean, MyHolder myHolder) {

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
//            spannableStringBuilder.append(bean.recommendation.type + " · ");
            if (!TextUtils.isEmpty(bean.recommendation.price)&&!"0".equals(bean.recommendation.price) ) {
                Integer integer = Integer.valueOf(bean.recommendation.price);
                String price = integer + "金币";
//                SpannableString spannableString = new SpannableString(price);
//                ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#AE7614"));
//                spannableString.setSpan(span, 0, price.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                spannableStringBuilder.append(spannableString);
                myHolder.tv_price_dynamic_item.setText(price);
            }else{
                myHolder.tv_price_dynamic_item.setText("免费");
            }
        }else{
//            spannableStringBuilder.append(bean.recommendation.type + "");
        }


        myHolder.tv_bottom_dynamic_item.setText(spannableStringBuilder);
        spannableStringBuilder.clear();
    }

}
