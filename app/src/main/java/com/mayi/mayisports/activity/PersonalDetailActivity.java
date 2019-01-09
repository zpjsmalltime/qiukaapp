package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
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
import com.mayisports.qca.bean.ComentsDetailBean;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.HomeDataNewBean;
import com.mayisports.qca.bean.PersoanalGroomBean;
import com.mayisports.qca.bean.PersonalTopicBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.fragment.DynamicFragment;
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
import com.mayisports.qca.view.InterceptEventViewGroup;
import com.mayisports.qca.view.MyImageSpan;
import com.mayisports.qca.view.NoLineClickSpan;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.mayi.mayisports.QCaApplication.getContext;
import static com.mayi.mayisports.activity.LoginActivity.RESULT;


/**
 * 个人中心  详情页
 */
public class PersonalDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {


    public static final int CODE = 99;
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
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","share");
        //1个人首页   2 3
        params.put("type",1);
        params.put("id",id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });
        ToastUtils.toastNoStates("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(Platform platform, int i) {

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


    private ImageView iv_left_title1;
    private TextView tv_title1;
    private ImageView iv_right1_title1;
    private ImageView iv_right_title1;



    private TextView tv_go_weibo;


    private RelativeLayout rl_title_container;
    private TextView tv_name_title;


    private InterceptEventViewGroup ievg;

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;

    private RelativeLayout rl_real_title;

    @Override
    protected void initView() {
        super.initView();
        initReceiver();
        setTitleShow(false);


        rl_real_title = findViewById(R.id.rl_real_title);

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        });

        ievg = findViewById(R.id.ievg);
        ievg.setIntercept(true);

        tv_title1 = findViewById(R.id.tv_title1);
        iv_left_title1 = findViewById(R.id.iv_left_title1);
        iv_right1_title1 = findViewById(R.id.iv_right1_title1);
        iv_right1_title1.setOnClickListener(this);
        iv_right_title1 = findViewById(R.id.iv_right_title1);
        tv_title1.setText("球咖");
        iv_right_title1.setVisibility(View.VISIBLE);
        iv_right_title1.setOnClickListener(this);
        iv_right_title1.setImageDrawable(getResources().getDrawable(R.drawable.share_icon));
        rl_load_layout = findViewById(R.id.rl_load_layout);




        xfv_personal_detail = findViewById(R.id.xfv_personal_detail);
        xfv_personal_detail.setPullRefreshEnable(false);//设置允许下拉刷新

       xfv_personal_detail.setTouchDel(false);

        xfv_personal_detail.setMoveForHorizontal(true);
        xfv_personal_detail.setXRefreshViewListener(new MyListener());

        lv_personal_detail = findViewById(R.id.lv_personal_detail);
        addHeaderForListView();
        lv_personal_detail.setAdapter(myLvAdapter);
        ll_no_topic.setVisibility(View.GONE);
        lv_personal_detail.setOnItemClickListener(this);


        rl_title_container = findViewById(R.id.rl_title_container);
        tv_name_title = findViewById(R.id.tv_name_title);



        xfv_personal_detail.setOnAbsListViewScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mCurrentfirstVisibleItem = firstVisibleItem;
                View firstView = view.getChildAt(0);//获取当前最顶部的item
                if (firstView != null) {
                    ItemRecod itemRecord = (ItemRecod) recordSp.get(firstVisibleItem);
                    if (itemRecord == null) {
                        itemRecord = new ItemRecod();
                    }
                    //关于这个height和top到底是多少,怎么变化,我们可以通过日志来看,一目了然
                    itemRecord.height = firstView.getHeight();//获取当前最顶部Item的高度
                    itemRecord.top = firstView.getTop();//获取对应item距离顶部的距离
                    recordSp.append(firstVisibleItem, itemRecord);//添加键值对设置值
                    int scrollY = getScrollY();//这就是滑动的距离,我们可以根据这个距离做很多事
                    Log.e("scrollY",scrollY+"");


                    if(scrollY<=220){

                        float f = (float) (scrollY/200.0);
                        Log.e("scrollY",scrollY+"-"+f);
                        rl_title_container.setAlpha(f);
                        tv_name_title.setAlpha(f);
                    }else{
                        rl_title_container.setAlpha(1.0f);
                        tv_name_title.setAlpha(1.0f);
                    }

                }


            }
        });


    }


    /**
     * 获取滑动距离
     * @return
     */
    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = (ItemRecod) recordSp.get(i);
            if (itemRecod != null) {
                height += itemRecod.height;
            }
        }
        ItemRecod itemRecod = (ItemRecod) recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }

    private class ItemRecod {
        int height = 0;
        int top = 0;
    }



    private int mCurrentfirstVisibleItem;
    private SparseArray recordSp = new SparseArray(10);//设置容器大小，默认是10


    /**
     * 设置顶部全屏显示
     */
    @Override
    public void setFullScreen() {
        super.setFullScreen();

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


    }

    public static void start(Activity activity,String userId,String jsonHeader){
        Intent intent = new Intent(activity,PersonalDetailActivity.class);
        intent.putExtra("id",userId);
        intent.putExtra("jsonHeader",jsonHeader);
        activity.startActivity(intent);
    }

    public static void start(Activity activity,String userId,int position,int code,String jsonHeader){
        Intent intent = new Intent(activity,PersonalDetailActivity.class);
        intent.putExtra("id",userId);
        intent.putExtra("position",position);
        intent.putExtra("jsonHeader",jsonHeader);
        activity.startActivityForResult(intent,code);

    }

    public static void start(Fragment fragment, String userId, int position, int code,String jsonHeader){
        Intent intent = new Intent(fragment.getActivity(),PersonalDetailActivity.class);
        intent.putExtra("id",userId);
        intent.putExtra("position",position);
        intent.putExtra("jsonHeader",jsonHeader);
        fragment.startActivityForResult(intent,code);

    }

    private String id;

    @Override
    protected void initDatas() {
        super.initDatas();
        id = getIntent().getStringExtra("id")+"";


        if(id.equals(SPUtils.getString(this,Constant.USER_ID))){

            String weiboId = SPUtils.getString(QCaApplication.getContext(), Constant.WEOBO_ID);
            if(!TextUtils.isEmpty(weiboId)){
                iv_right1_title1.setVisibility(View.VISIBLE);
                tv_go_weibo.setVisibility(View.VISIBLE);
            }else{
                iv_right1_title1.setVisibility(View.GONE);
                tv_go_weibo.setVisibility(View.GONE);
            }

        }else{
            iv_right1_title1.setVisibility(View.GONE);
            tv_go_weibo.setVisibility(View.GONE);
        }



        String json = getIntent().getStringExtra("jsonHeader");
        if(!TextUtils.isEmpty(json)){

            try {
                DynamicBean.DataBean.UserBean userBean = JsonParseUtils.parseJsonClass(json, DynamicBean.DataBean.UserBean.class);
                dynamicBean = new DynamicBean();
                dynamicBean.data = new DynamicBean.DataBean();
                dynamicBean.data.user = userBean;
                updateHeader(false);
            }catch (Exception e){

            }
        }else{

        }

        onClick(tv_groom_personal_detial);
    }



    /**
     * 请求推荐数据
     */
    private PersoanalGroomBean persoanalGroomBean;
    private DynamicBean dynamicBean;
    private int pre_page = 1;
    private boolean isNetNotify;
    private void requestLeftData(final int pageLeft) {

        //htt://20180109.dldemo.applinzi.com/php/api.php?action=followers&type=friends_feed_list&start=0&user_id=198159
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        params.put("type","friends_feed_list");
        params.put("start",page);
        params.put("user_id",id+"");
        Log.e("personal_url",params.getUrlParams().toString());
        if(page == 0) {
            tv_count_persoanal_detail.setVisibility(View.GONE);
        }
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                if(xfv_personal_detail != null) {
                    xfv_personal_detail.stopRefresh();
                    xfv_personal_detail.stopLoadMore();
                }

                isNetNotify = true;
                if(page == 0) {
                    dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                    DynamicBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                    delData(dynamicBean,dynamicBean1);


                    if(dynamicBean != null && dynamicBean.list != null){
                        if(dynamicBean.list.size() == 0){
                            xfv_personal_detail.setPullLoadEnable(false);
                          //  ll_no_topic.setVisibility(View.VISIBLE);
                        }else{
//                            xfv_dynamic_fg.setPullLoadEnable(true);
                            ll_no_topic.setVisibility(View.GONE);
                        }
                    }else{
                        xfv_personal_detail.setPullLoadEnable(false);
                       // ll_no_topic.setVisibility(View.VISIBLE);
                    }
                    lv_personal_detail.setAdapter(myLvAdapter);
                    updateHeader(true);
                    pre_page = 1;

                }else{
                    try {
                        DynamicBean newBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                        DynamicBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                        delData(newBean,dynamicBean1);


                        dynamicBean.list.addAll(newBean.list);
                    }catch (Exception e){
//                        ToastUtils.toast("没有更多了。。。");
                        pre_page = 0;
                    }
                    myLvAdapter.notifyDataSetChanged();
                }

                isNetNotify = false;

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                xfv_personal_detail.stopLoadMore();
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                xfv_personal_detail.setTouchDel(true);
                ievg.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ievg.setIntercept(false);
                    }
                },200);

                if(page == 0){
                    if(NetworkUtils.isConnected(QCaApplication.getContext())){
                        ll_net_error.setVisibility(View.GONE);
                        rl_real_title.setVisibility(View.GONE);
                    }else{
                        ll_net_error.setVisibility(View.VISIBLE);
                        rl_real_title.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    /**
     * 处理数据  type 1 显示
     * @param homeDataBean
     * @param newBean
     */
    private void delData(DynamicBean homeDataBean, DynamicBean newBean) {
        if(homeDataBean != null && newBean != null) {
            if (homeDataBean.list != null && newBean.list != null) {
                homeDataBean.list.clear();
                for (int i = 0; i < newBean.list.size(); i++) {

                    DynamicBean.ListBean beanX = newBean.list.get(i);
                    if (beanX.type == 1 || beanX.type == 3 || beanX.type == 6  || beanX.type1 == 8 || beanX.type == 7 ||beanX.type == 4) {//1推荐单  3 话题 6话题  5可能感兴趣的人   8头条   4评论&转发类型
                            if (!(beanX.type == 3 && isHeadline(beanX))) {
                            homeDataBean.list.add(beanX);
                        } else if (beanX.comment != null) {
                            homeDataBean.list.add(beanX);
                        }
                    }
                }
            }
        }
    }

    private boolean isSelf;
    /**
     * 刷新头部数据
     *   大数据推荐id 53719
     */
    private void updateHeader(final boolean isShowNoData) {


            if(dynamicBean  == null || dynamicBean.data == null)return;
            final DynamicBean.DataBean.UserBean user = dynamicBean.data.user;
            if(user == null)return;

        if(user.all_feed_count>0) {
            tv_count_persoanal_detail.setVisibility(View.VISIBLE);
            tv_count_persoanal_detail.setText("全部动态 " + user.all_feed_count + "");
        }else{
            tv_count_persoanal_detail.setVisibility(View.GONE);
        }
//            tv_count_persoanal_detail.setText("共推荐" + user.all_bet_count + "场");
            PictureUtils.showCircle(user.headurl+"",iv_header_personal);










//            if(!TextUtils.isEmpty(user.verify_type)&&Integer.valueOf(user.verify_type)>0){
//                iv_vip_header.setVisibility(View.VISIBLE);
//            }else{
//                iv_vip_header.setVisibility(View.GONE);
//            }

        iv_vip_header.setVisibility(View.GONE);


//            ViewStatusUtils.addTagsPersonal(ll_tag_container_personal_detail,user.tag,user.tag1);
        ll_tag_container_personal_detail.setVisibility(View.GONE);
            if(TextUtils.isEmpty(user.nickname)){
                user.nickname = "";


                if(id.equals(SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID))){
                    user.nickname = SPUtils.getString(QCaApplication.getContext(),Constant.NICK_NAME);
                }
            }


            tv_name_personal.setText(user.nickname+"");
//        tv_fenxishi_personal.setText(user.verify_type);
            if(!id.equals(SPUtils.getString(this,Constant.USER_ID))){

                isSelf = false;
                if(user.follow_status == 1){
                    tv_follow_personal.setTag(1);
//            cb.setChecked(true);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_white_cricle));
                    tv_follow_personal.setTextColor(Color.parseColor("#33ffffff"));
                    tv_follow_personal.setText("已关注");
                }else{
                    tv_follow_personal.setTag(0);
//            cb.setChecked(false);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_yellow_cricel));
                    tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                    tv_follow_personal.setText(" 关注 ");
                }
                tv_follow_personal.setVisibility(View.VISIBLE);
            }else {
                tv_follow_personal.setVisibility(View.GONE);
                isSelf = true;
            }




            if(!"1".equals(user.type)){

                if(user.follow_status == 1){
                    tv_follow_personal.setTag(1);
//            cb.setChecked(true);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_white_cricle));
                    tv_follow_personal.setTextColor(Color.parseColor("#33ffffff"));
                    tv_follow_personal.setText("已关注");
                }else{
                    tv_follow_personal.setTag(0);
//            cb.setChecked(false);
                    tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_yellow_cricel));
                    tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                    tv_follow_personal.setText(" 关注 ");
                }
            }else {
                tv_follow_personal.setVisibility(View.GONE);
//                user.headurl = "http://dldemo-img.stor.sinaapp.com/model_head.png";
                PictureUtils.show(R.drawable.big_data_header,iv_header_personal);
            }








            if(!TextUtils.isEmpty(user.verify_reason)){
                tv_jieshao_personal.setVisibility(View.VISIBLE);
                if(!TextUtils.isEmpty(user.verify_type)&&Integer.valueOf(user.verify_type)>0){


//                    Drawable drawable= getResources().getDrawable(R.drawable.authentication_icon);
//                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

                    SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

                    SpannableString spannedString = new SpannableString("1  "+user.verify_reason);
                    MyImageSpan imageSpan = new MyImageSpan(this,R.drawable.authentication_icon);
                    spannedString.setSpan(imageSpan,0,1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

                    tv_jieshao_personal.setText(spannedString);

                 //   tv_jieshao_personal.setCompoundDrawables(drawable,null,null,null);
                    int dp = DisplayUtil.dip2px(QCaApplication.getContext(), 10);
                  //  tv_jieshao_personal.setCompoundDrawablePadding(dp);
                }else{
                    tv_jieshao_personal.setText(""+user.verify_reason);
                    tv_jieshao_personal.setCompoundDrawables(null,null,null,null);
                }

            }else{
                tv_jieshao_personal.setText("");
                tv_jieshao_personal.setVisibility(View.GONE);
            }

            tv_follow_personal.setOnClickListener(this);



        if(!TextUtils.isEmpty(user.weibo_id)){
            iv_right1_title1.setVisibility(View.VISIBLE);
            iv_right1_title1.setOnClickListener(this);
            tv_go_weibo.setVisibility(View.VISIBLE);
            tv_go_weibo.setOnClickListener(this);
        }else{
            iv_right1_title1.setVisibility(View.GONE);
            iv_right1_title1.setOnClickListener(null);
            tv_go_weibo.setVisibility(View.GONE);
            tv_go_weibo.setOnClickListener(null);
        }


        if(TextUtils.isEmpty(user.followers_count))user.followers_count = "0";
        tv_follower_count.setText(user.followers_count+"");
        tv_praise_count.setText(user.praise_count+"");


        tv_name_title.setText(user.nickname+"");


//        PictureUtils.show(user.headurl+"",v_bg);


                tv_count_persoanal_detail.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        int[] position = new int[2];

                        tv_count_persoanal_detail.getLocationOnScreen(position);
                        v_bg.getLayoutParams().height = position[1];
//                v_bg.setLayoutParams(v_bg.getLayoutParams());
                        v_cover.getLayoutParams().height = v_bg.getLayoutParams().height;

                        v_cover.setLayoutParams(v_cover.getLayoutParams());

                        PictureUtils.showImgNoErrMoHu(user.headurl+"",v_bg);

                        if(dynamicBean != null && dynamicBean.list != null){
                            if(dynamicBean.list.size() == 0){
                                xfv_personal_detail.setPullLoadEnable(false);
                                if(isShowNoData) ll_no_topic.setVisibility(View.VISIBLE);
                            }else{
//                            xfv_dynamic_fg.setPullLoadEnable(true);
                                ll_no_topic.setVisibility(View.GONE);
                            }
                        }else{
                            xfv_personal_detail.setPullLoadEnable(false);
                            if(isShowNoData) ll_no_topic.setVisibility(View.VISIBLE);

                        }
                    }
                },1);




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

    private View ll_top_;
    private ImageView v_bg;

    private ImageView iv_back;
    private ImageView iv_dot_more;

    private TextView tv_follower_count;
    private TextView tv_praise_count;

    private RelativeLayout rl_head_root;

    private View v_cover;

    private void addHeaderForListView() {
        headerView = View.inflate(getContext(),R.layout.personal_detail_header_fg,null);


        rl_head_root = headerView.findViewById(R.id.rl_head_root);

        ll_top_ = headerView.findViewById(R.id.ll_top_);
        v_bg = headerView.findViewById(R.id.v_bg);

        v_cover = headerView.findViewById(R.id.v_cover);


        tv_follower_count = headerView.findViewById(R.id.tv_follower_count);
        tv_praise_count = headerView.findViewById(R.id.tv_praise_count);


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
        iv_left_title1.setOnClickListener(this);



        tv_go_weibo = headerView.findViewById(R.id.tv_go_weibo);
        tv_go_weibo.setOnClickListener(this);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);

        iv_dot_more = findViewById(R.id.iv_dot_more);
        iv_dot_more.setOnClickListener(this);
    }

    /**
     * 1，推荐单,2，话题
     */
    private int type;
    private int page;
    private int pageLeft;
    private int clickType;

    private  ToastPricePopuWindow followPw;
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
                page = 0;
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
            case R.id.iv_left_title1:
            case R.id.iv_back:
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
                    DynamicBean.ListBean bean = dynamicBean.list.get(clickPosition-1);
                    bean.recommendation.buy = 1;
                    myLvAdapter.notifyDataSetChanged();
                    goDetail(bean);
                }else{//充值

                    Intent intent = new Intent(this, CoinDetailActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.iv_right_title1:
            case R.id.iv_dot_more:
//                if(LoginUtils.isUnLogin()){
//                LoginUtils.goLoginActivity(this,ACTION);
//                return;
//                }
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
                if(b == 1) {//取消关注

                    followPw = new ToastPricePopuWindow(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (v.getId() == R.id.tv_cancle_toast_price) {
                                int b = (int) tv_follow_personal.getTag();
                                if (b == 1) {//取消关注
                                    requestFollows("delete");
                                }
                                followPw.dismiss();
                            } else {
                                followPw.dismiss();
                            }

                        }
                    }, "确定不再关注此人？", "确定", "考虑一下");
                    followPw.showAtLocation(lv_personal_detail, Gravity.CENTER,0,0);
                }else{
                    requestFollows("add");
                }
                break;
            case R.id.iv_header_personal:
               if(dynamicBean != null){
//                   Intent intent = new Intent(PersonalDetailActivity.this,PicShowActivity.class);
//
//                   intent.putExtra("urls",dynamicBean.data.user.headurl,true);
                   PicShowActivity.start(PersonalDetailActivity.this,dynamicBean.data.user.headurl,0,true);
//                    startActivity(intent);
                }

                break;

            case R.id.iv_right1_title1://跳转到奥微博
            case R.id.tv_go_weibo:
//                String uid = SPUtils.getString(QCaApplication.getContext(), Constant.WEOBO_ID);
                if(dynamicBean != null && dynamicBean.data != null && dynamicBean.data.user != null){

                if(!TextUtils.isEmpty(dynamicBean.data.user.weibo_id)){

                        String uid = dynamicBean.data.user.weibo_id;

                        try {

                            Intent intent = new Intent();
                            ComponentName cmp = new ComponentName("com.sina.weibo", "com.sina.weibo.page.ProfileInfoActivity");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);

                            intent.putExtra("uid", uid+"");
                            startActivity(intent);
                        }catch (Exception e){
                            ToastUtils.toast("未安装微博，为您跳转网页版");
                           String url = "https://m.weibo.cn/u/"+uid;
                           WebViewActivtiy.start(this,url,"球咖",true);

                        }

                    }
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
        try {
            String headurl = dynamicBean.data.user.headurl;
            ShareUtils.shareMsgForPersonalDetails(type, id, tv_name_personal.getText().toString(),headurl, this);
        }catch (Exception e){

        }
    }

    private  int clickPosition;
    private void goDetail(DynamicBean.ListBean bean){

        Intent intent = new Intent(this, HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
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
                        updateHeader(true);
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
        if(dynamicBean == null || dynamicBean.list == null || i == dynamicBean.list.size()+1)return;
        DynamicBean.ListBean bean = dynamicBean.list.get(i-1);

        if(bean.type == 1){//推荐单
            delType2Click(bean,i);
        }else if(bean.type == 3){//话题
            if(bean.type == 3 && isShootVideo(bean)){
                toShootVideoPlayer(bean,i-1,false);
            }else {
                toJsonCommentBean(bean);
                ComentsDetailsActivity.startForResult(2, PersonalDetailActivity.this, bean.comment.comment_id, i-1);
            }
        }else if(bean.type == 4){//评论类型 或者转发
            if(bean == null || bean.comment == null)return;
                ShareComentsDetailsActivity.startForResult(2, PersonalDetailActivity.this, ComentsDetailsActivity.SHARE_TYPE, bean.comment.comment_id, bean, i - 1);
        }


    }

    private void toJsonCommentBean(DynamicBean.ListBean listBean){
        listBean.comment.user = listBean.user;
        String s = new Gson().toJson(listBean.comment);
        Constant.commentBeanJson = s;
    }

    /**
     * 去短视频页
     * @param bean
     * @param position
     */
    private void toShootVideoPlayer(DynamicBean.ListBean bean, int position,boolean isSelectFirst) {

        DynamicBean dynamicBean = new DynamicBean();
        dynamicBean.list = new ArrayList<>();
        dynamicBean.list.add(bean);
        Constant.dynamicBean = dynamicBean;
        VideoPlayerActivity.start(this,-1,0,bean.subtype,22,isSelectFirst);


    }

    private boolean isShootVideo(DynamicBean.ListBean bean){
        boolean b = !TextUtils.isEmpty(bean.subtype) && Integer.valueOf(bean.subtype) == 7;
        return b;
    }

    private void delType2Click(DynamicBean.ListBean bean,int i) {
        String price = bean.recommendation.price;
        String nickname = bean.user.nickname;
        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,ACTION);
            return;
        }

//        bean.count = "0";
        myLvAdapter.notifyDataSetChanged();
        if(bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0) || "COMPLETE".equals(bean.match.status)|| SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID).equals(bean.user.user_id)){//已购买 //或者免费
            Intent intent = new Intent(this, HomeItemDetailActivity.class);
            intent.putExtra("userId",bean.user.user_id);
            intent.putExtra("betId",bean.match.betId);
            startActivity(intent);
        }else{

//            new ToastPricePopuWindow(this.getActivity(),this,)
            clickPosition = i;
            requestToast(bean.user.user_id,bean.match.betId);
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
                toastPricePopuWindow =   new ToastPricePopuWindow(PersonalDetailActivity.this,PersonalDetailActivity.this,title,type_);
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

                    int follo = 0;
                    if("1".equals(followBean.status.result)){
                        tv_follow_personal.setTag(1);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_white_cricle));
                        tv_follow_personal.setTextColor(Color.parseColor("#33ffffff"));
                        tv_follow_personal.setText("已关注");
//                        Constant.followStates.put(dynamicBean.data.user.user_id,true);
                        NotificationsUtils.checkNotificationAndStartSetting(PersonalDetailActivity.this,lv_personal_detail);

                        follo = 1;
                    }else{
                        tv_follow_personal.setTag(0);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_yellow_cricel));
                        tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                        tv_follow_personal.setText(" 关注 ");
//                        Constant.followStates.remove(dynamicBean.data.user.user_id);
                        follo = 0;
                    }


                    DynamicFragment.sendFoucsData();


                    Intent intent1 = new Intent(DynamicFragment.ACTION);
                    intent1.putExtra(RESULT, 100);//发送到 球咖 更新数据
                    intent1.putExtra("follow_status",follo);
                    intent1.putExtra("user_id",id);
                    LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent1);
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
    class MyLvAdapter extends BaseAdapter {

        @Override
        public int getCount() {


            if(dynamicBean != null && dynamicBean.list!= null){
                return dynamicBean.list.size()+1;
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

            if(i == dynamicBean.list.size()){
                view = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = view.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                if(getCount()<=2){
                    view.setVisibility(View.GONE);
                }
                return view;
            }

            final MyHolder myHolder;
            if(view == null || view.getTag() == null){
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
                myHolder.iv_content_bg_item = view.findViewById(R.id.iv_content_bg_item);
                myHolder.tv_content_bg_dynamic_item = view.findViewById(R.id.tv_content_bg_dynamic_item);
                myHolder.gv_dynamic_item = view.findViewById(R.id.gv_dynamic_item);
                myHolder.rl_bg_long_content_item = view.findViewById(R.id.rl_bg_long_content_item);
                myHolder.tv_content_long_content = view.findViewById(R.id.tv_content_long_content);
                myHolder.tv_title_long_content = view.findViewById(R.id.tv_title_long_content);
                myHolder.iv_result_personal = view.findViewById(R.id.iv_result_personal);
                myHolder.tv_share_topic_item = view.findViewById(R.id.tv_share_topic_item);
                myHolder.tv_content_bottom = view.findViewById(R.id.tv_content_bottom);

                myHolder.v_bottome_line = view.findViewById(R.id.v_bottome_line);

                myHolder.tv_detail_title = view.findViewById(R.id.tv_detail_title);


                myHolder.ll_bottom_item = view.findViewById(R.id.ll_bottom_item);

                myHolder.ll_comment_type = view.findViewById(R.id.ll_comment_type);
                myHolder.tv_content_comment_type_item = view.findViewById(R.id.tv_content_comment_type_item);
                myHolder.gv_comment_type_item = view.findViewById(R.id.gv_comment_type_item);


                myHolder.rl_video_hot_reply_item = view.findViewById(R.id.rl_video_hot_reply_item);
                myHolder.iv_video_hot_reply_item = view.findViewById(R.id.iv_video_hot_reply_item);

                setHeadLineHolder(myHolder,view);


                view.setTag(myHolder);
            }else{
                myHolder = (MyHolder) view.getTag();
            }


            if(i < getCount()-1){
                myHolder.v_bottome_line.setVisibility(View.VISIBLE);
            }else{
                myHolder.v_bottome_line.setVisibility(View.GONE);
            }

            final DynamicBean.ListBean bean = dynamicBean.list.get(i);



//            myHolder.ll_video_dy_item.setVisibility(View.GONE);
            myHolder.ll_headline_dy_item.setVisibility(View.GONE);
            myHolder.ll_bottom_item.setVisibility(View.GONE);
            myHolder.ll_comment_type.setVisibility(View.GONE);
//            myHolder.fl_topic_img_item.setVisibility(View.GONE);

            myHolder.tv_content_dynamic_item.setMaxLines(5);
//            myHolder.tv_content_dynamic_item.setOnClickListener(null);


            myHolder.tv_content_dynamic_item_copy.setVisibility(View.GONE);

            myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });


            if(bean.type == 3 && isHeadline(bean)){//头条
                myHolder.ll_headline_dy_item.setVisibility(View.VISIBLE);
                setHeadLineData(myHolder, bean);
            } else if(bean.type == 1){//推荐单

                /**
                 * 设置头部
                 */
                setComenHead(i,bean,myHolder);


                myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                myHolder.ll_bottom_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                bindType2(i,bean,myHolder);

            }else if(bean.type == 3){//话题

                /**
                 * 设置头部
                 */
                setComenHead(i,bean,myHolder);


                myHolder.ll_bottom_item.setVisibility(View.VISIBLE);
                myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);


                bindType1(i,bean,myHolder);
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

            if(i == getCount()-3 ||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                   requestLeftData(pageLeft);
                }
            }


            return view;
        }
    }

    private void setComenHead(int i, DynamicBean.ListBean bean, MyHolder myHolder){
        PictureUtils.showCircle(bean.user.headurl+"",myHolder.iv_head_dynamic_item);


        myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
        myHolder.iv_red_point_header.setVisibility(View.GONE);

        if(!TextUtils.isEmpty(bean.user.verify_type)&&Integer.valueOf(bean.user.verify_type)>0){
            myHolder.iv_vip_header.setVisibility(View.VISIBLE);
        }else {
            myHolder.iv_vip_header.setVisibility(View.GONE);
        }

        if(false && bean.sys_recommendation == 1 && !isSelf){//系统推荐，显示关注
//            if(bean.follow != 1){

            myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
            myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                }
            });


            if(bean.follow == 1){
//                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                myHolder.tv_follow_dynamic_item.setText("已关注");
                myHolder.tv_follow_dynamic_item.setOnClickListener(null);
            }else{
//                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
//                    myHolder.tv_follow_dynamic_item.setText(" 关注 ");
                myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black));
                myHolder.tv_follow_dynamic_item.setText("+关注");
//                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
            }


        }else{
            myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_detail_title.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(bean.create_time)) {
                String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                myHolder.tv_time_dynamic_item.setText(createTime);
                myHolder.tv_detail_title.setText(createTime);
            }else{
                myHolder.tv_time_dynamic_item.setText("---");
                myHolder.tv_detail_title.setText("---");
            }
        }
    }

    private boolean isHeadline(DynamicBean.ListBean bean){
        boolean b = !TextUtils.isEmpty(bean.subtype) && Integer.valueOf(bean.subtype) == 8;
        return b;
    }


    /**
     * 填充头条数据
     * @param myHolder
     * @param bean
     */
    private void setHeadLineData(MyHolder myHolder, DynamicBean.ListBean bean) {
        if(bean.comment == null)return;
        myHolder.ll_one_img_headline.setVisibility(View.GONE);
        myHolder.ll_more_img_headline.setVisibility(View.GONE);
        myHolder.ll_headline_img_container.setVisibility(View.GONE);

        String comments = "";
        if(!TextUtils.isEmpty(bean.comment.reply_count)&&Integer.valueOf(bean.comment.reply_count)>0){
            String s = DataBindUtils.parseIntToK(Integer.valueOf(bean.comment.reply_count));
            comments = s + "评";
        }

        String bottom_text = bean.user.nickname + "   " + comments;

        if(bean.comment.backgroundList == null && TextUtils.isEmpty(bean.comment.background_img)){
            myHolder.ll_more_img_headline.setVisibility(View.VISIBLE);
            myHolder.tv_title_more_img.setText(bean.comment.title);
            myHolder.tv_bottom_more_img.setText(bottom_text);
        }else if(bean.comment.backgroundList != null){
            if(bean.comment.backgroundList.size()>2){
                //多图
                myHolder.ll_more_img_headline.setVisibility(View.VISIBLE);
                myHolder.ll_headline_img_container.setVisibility(View.VISIBLE);
                myHolder.tv_title_more_img.setText(bean.comment.title);
                myHolder.tv_bottom_more_img.setText(bottom_text);

                for(int i = 0;i<3;i++){
                    String url = bean.comment.backgroundList.get(i).url;
                    ImageView iv = (ImageView) myHolder.ll_headline_img_container.getChildAt(i);
                    PictureUtils.showImg(url,iv);
                }


            }else {//单图
                myHolder.ll_one_img_headline.setVisibility(View.VISIBLE);
                myHolder.tv_title_one_img.setText(bean.comment.title);
                myHolder.tv_bottom_one_img.setText(bottom_text);
                if(TextUtils.isEmpty(bean.comment.background_img)) {
                    PictureUtils.showImg(bean.comment.backgroundList.get(0).url, myHolder.iv_right_one_img);
                }else{
                    PictureUtils.showImg(bean.comment.background_img, myHolder.iv_right_one_img);
                }
            }

        }else {//单图
            PictureUtils.showImg(bean.comment.background_img, myHolder.iv_right_one_img);
            myHolder.ll_one_img_headline.setVisibility(View.VISIBLE);
            myHolder.tv_title_one_img.setText(bean.comment.title);
            myHolder.tv_bottom_one_img.setText(bottom_text);
        }


        if(bean.top == 1){
            myHolder.tv_top_tag.setVisibility(View.VISIBLE);
            myHolder.tv_top_tag_1img.setVisibility(View.VISIBLE);
        }else{
            myHolder.tv_top_tag.setVisibility(View.GONE);
            myHolder.tv_top_tag_1img.setVisibility(View.GONE);
        }

    }



    /**
     * 评论类型
     * @param i
     * @param bean
     * @param myHolder
     */
    private void bindtype4(final int i, final DynamicBean.ListBean bean, MyHolder myHolder) {


        if(bean.reply == null){
            return;
        }
        if(bean.comment == null){

        }else {
            myHolder.ll_comment_type.setVisibility(View.VISIBLE);
        }

        myHolder.tv_content_dynamic_item.setVisibility(View.GONE);

         HomeDataNewBean.DataBeanX.RecommendationBeanX.UserBean user;

         if(!bean.covert) {

             String time = "";
             time = bean.create_time;
             bean.create_time = bean.reply.create_time;
             bean.reply.create_time = time;

             user = bean.user;
             bean.user = bean.reply.user;
             bean.reply.user = user;

             bean.covert = true;
         }

         setComenHead(i,bean,myHolder);

        myHolder.tv_content_dynamic_item_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareComentsDetailsActivity.startForResult(2, PersonalDetailActivity.this, ComentsDetailsActivity.SHARE_TYPE, bean.comment.comment_id, bean, i);

            }
        });
         if(bean.reply != null) {


             myHolder.tv_content_dynamic_item_copy.setVisibility(View.VISIBLE);
             ShareReplyUtils.bindReplyLink(PersonalDetailActivity.this,myHolder.tv_content_dynamic_item_copy,bean.reply,bean.reply.parentList);


             ShareReplyUtils.bindShareOriginal(PersonalDetailActivity.this,myHolder.ll_comment_type,myHolder.tv_content_comment_type_item,bean.reply,bean.comment,i);

         }
        if(bean.comment != null) {
            if (bean.comment.imgListEx != null && bean.comment.imgListEx.size() > 0) {


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
                            ComentsDetailsActivity.startForResult(true, 2, PersonalDetailActivity.this, bean.comment.comment_id, i);

                        }
                    });

                } else {
                    MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_comment_type_item.getAdapter();

                    myHolder.rl_video_hot_reply_item.setVisibility(View.GONE);
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
                            Intent intent = new Intent(PersonalDetailActivity.this, PicShowActivity.class);
                            intent.putExtra("urls", substring);
                            intent.putExtra("position", positi);
                            startActivity(intent);
                        }
                    });

                   }
                } else{
                    myHolder.gv_comment_type_item.setVisibility(View.GONE);
                    myHolder.rl_video_hot_reply_item.setVisibility(View.GONE);
                }




            if (!TextUtils.isEmpty(bean.reply.view_count)) {
                String s = Utils.parseIntToK(Integer.valueOf(bean.reply.view_count));
                myHolder.tv_view_count_topic_item.setText("阅读 " + s);
            }else{
                myHolder.tv_view_count_topic_item.setText("阅读 "+"1");
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
                        LoginUtils.goLoginActivity(PersonalDetailActivity.this, ACTION);
                        return;
                    }
                    ShareComentsDetailsActivity.startForResult(2,PersonalDetailActivity.this,ComentsDetailsActivity.SHARE_TYPE,bean.comment.comment_id,bean,i);


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
     * 话题
     * @param bean
     * @param myHolder
     */
    private void bindType1(final int position,final DynamicBean.ListBean bean, MyHolder myHolder) {

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
                myHolder.tv_content_bg_dynamic_item.setText(bean.comment.title);
                myHolder.tv_content_bottom.setText(bean.comment.comment+"");
                PictureUtils.show(bean.comment.background_img,myHolder.iv_content_bg_item);
            }
        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
            myHolder.rl_bg_content_item.setVisibility(View.GONE);
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
//                            ComentsDetailsActivity.start(PersonalDetailActivity.this,bean.comment.comment_id);
                            if(bean.type == 3 && isShootVideo(bean)){
                                toShootVideoPlayer(bean,position,false);
                            }else {
                                toJsonCommentBean(bean);
                                ComentsDetailsActivity.startForResult(true, 2, PersonalDetailActivity.this, bean.comment.comment_id, position);
                            }
                        }
                    });
                    int heightVideo = (int) (DisplayUtil.getScreenWidth(PersonalDetailActivity.this) / 16.0 * 8.7);
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
                                ComentsDetailsActivity.startForResult(2, PersonalDetailActivity.this, bean.comment.comment_id, position);
                                return;
                            }



                            String urls = "";
                            for(int i = 0;i<bean.comment.imgListEx.size();i++){
                                String url = bean.comment.imgListEx.get(i).url;
                                String delUrl = Utils.delUrl(url);
                                urls +=  delUrl+ ",";
                            }

                            String substring = urls.substring(0, urls.length() - 1);
                            Intent intent = new Intent(PersonalDetailActivity.this,PicShowActivity.class);
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
                    TopicDetailActivity.start(PersonalDetailActivity.this,bean.topic.topic_id);
                }
            });
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
                    LoginUtils.goLoginActivity(PersonalDetailActivity.this,ACTION);
                    return;
                }
//                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
//                WebViewActivtiy.start(PersonalDetailActivity.this,url,"球咖");
                if(bean.type == 3 && isShootVideo(bean)){
                    toShootVideoPlayer(bean,position,true);
                }else {
                    toJsonCommentBean(bean);
                    ComentsDetailsActivity.startForResult(2, PersonalDetailActivity.this, bean.comment.comment_id, position, true);
                }
//                if(LoginUtils.isUnLogin()){
//                    LoginUtils.goLoginActivity(PersonalDetailActivity.this,"");
//                    return;
//                }
//                ReplayCommentsActivity.start(PersonalDetailActivity.this,bean.comment.comment_id);

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
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.comment.reply_count);
//        myHolder.tv_comments_topic_item.setText(bean.comment.reply_count+"");

    }

    /**
     * 分享
     * @param position
     */
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
                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname,urls,typeOfBean,beanX.comment.title, PersonalDetailActivity.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname,PersonalDetailActivity.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_personal_detail, Gravity.CENTER, 0, 0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == 2) {//话题详情
            int position = data.getIntExtra("position", -1);
            String praise_count = data.getStringExtra("praise_count");

            if (position != -1) {
                if(dynamicBean == null || dynamicBean.list == null)return;
                DynamicBean.ListBean bean = dynamicBean.list.get(position);


                int follow_status = Integer.valueOf(data.getStringExtra("follow_status"));

                if (follow_status != dynamicBean.data.user.follow_status) {
                    dynamicBean.data.user.follow_status = follow_status;
                    if(dynamicBean.data.user.follow_status == 1){
                        tv_follow_personal.setTag(1);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_white_cricle));
                        tv_follow_personal.setTextColor(Color.parseColor("#33ffffff"));
                        tv_follow_personal.setText("已关注");
//                        Constant.followStates.put(dynamicBean.data.user.user_id,true);


                    }else{
                        tv_follow_personal.setTag(0);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_yellow_cricel));
                        tv_follow_personal.setTextColor(Color.parseColor("#282828"));
                        tv_follow_personal.setText(" 关注 ");
//                        Constant.followStates.remove(dynamicBean.data.user.user_id);
                    }
                }

                boolean delete = data.getBooleanExtra("delete", false);
                if(delete){
                    dynamicBean.list.remove(position);
                }
//                if (bean.comment.praise_count.equals(praise_count)&&!delete) return;

                if(bean.type == 4){
                    int praise_status = data.getIntExtra("praise_status", 0);
                    bean.reply.praise_count = praise_count;
                    bean.reply.praise_status = praise_status;
                }else {
                    int praise_status = data.getIntExtra("praise_status", 0);
                    bean.comment.praise_count = praise_count;
                    bean.comment.praise_status = praise_status;
                }
                myLvAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == 22 && resultCode == 22){//短视频页返回
            myLvAdapter.notifyDataSetChanged();
        }
    }

    class MyGvAdapter extends BaseAdapter{


        private List<DynamicBean.ListBean.ImageList> imgListEx;



        public MyGvAdapter(List<DynamicBean.ListBean.ImageList> imgListEx) {
             this.imgListEx = imgListEx;
        }

        @Override
        public int getCount() {
            if(imgListEx.size() == 7){
                return  9;
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
                    Utils.scaleImg(PersonalDetailActivity.this, view, imageView, imageList.size);
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

    /**
     * 推荐单
     * @param bean
     * @param myHolder
     */
    private void bindType2(final int position,final DynamicBean.ListBean bean, MyHolder myHolder) {

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
            myHolder.iv_result_personal.setVisibility(View.VISIBLE);
            ViewStatusUtils.parseStatusForImg(bean.recommendation.revenueStr,myHolder.iv_result_personal);
            myHolder.tv_buy.setVisibility(View.GONE);
            myHolder.tv_right_item.setVisibility(View.GONE);
            spannableStringBuilder.append(" · "+bean.recommendation.type);
        }else{
            myHolder.iv_result_personal.setVisibility(View.GONE);
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
                myHolder.tv_price_dynamic_item.setText(price);
            }else{
                myHolder.tv_price_dynamic_item.setText("免费");
            }
        }else{
        }

        myHolder.tv_bottom_dynamic_item.setText(spannableStringBuilder);
        spannableStringBuilder.clear();
    }


    /**
     * 绑定头条View
     * @param myHolder
     * @param view
     */
    private void setHeadLineHolder(MyHolder myHolder, View view) {
        myHolder.ll_headline_dy_item = view.findViewById(R.id.ll_headline_dy_item);
        myHolder.ll_one_img_headline = view.findViewById(R.id.ll_one_img_headline);
        myHolder.tv_title_one_img = view.findViewById(R.id.tv_title_one_img);
        myHolder.tv_bottom_one_img = view.findViewById(R.id.tv_bottom_one_img);
        myHolder.iv_right_one_img = view.findViewById(R.id.iv_right_one_img);
        myHolder.ll_more_img_headline = view.findViewById(R.id.ll_more_img_headline);
        myHolder.tv_title_more_img = view.findViewById(R.id.tv_title_more_img);
        myHolder.ll_headline_img_container = view.findViewById(R.id.ll_headline_img_container);
        myHolder.tv_bottom_more_img = view.findViewById(R.id.tv_bottom_more_img);
        myHolder.tv_top_tag = view.findViewById(R.id.tv_top_tag);
        myHolder.tv_top_tag_1img = view.findViewById(R.id.tv_top_tag_1img);
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
        public TextView tv_title_long_content;
        public TextView tv_content_long_content;
        public ImageView iv_result_personal;
        public TextView tv_share_topic_item;

        public TextView tv_content_bottom;


        public View v_bottome_line;
        public TextView tv_detail_title;




        /**
         * 头条样式
         */
        public FrameLayout ll_headline_dy_item;

        public LinearLayout ll_one_img_headline;
        public TextView tv_title_one_img;
        public TextView tv_bottom_one_img;
        public ImageView iv_right_one_img;

        public LinearLayout ll_more_img_headline;
        public TextView tv_title_more_img;
        public LinearLayout ll_headline_img_container;
        public TextView tv_bottom_more_img;

        public TextView tv_top_tag;
        public TextView tv_top_tag_1img;


        public LinearLayout ll_bottom_item;
        public LinearLayout ll_comment_type;
        public TextView tv_content_comment_type_item;
        public GridView gv_comment_type_item;
        public RelativeLayout rl_video_hot_reply_item;
        public ImageView iv_video_hot_reply_item;
        public TextView tv_content_dynamic_item_copy;
    }
    /**
     * 请求点赞
     * @param listBean
     */
    private void requestThumbup(final DynamicBean.ListBean listBean) {

        if(LoginUtils.isUnLogin()){
           LoginUtils.goLoginActivity(this,ACTION);
            return;
        }

        rl_load_layout = findViewById(R.id.rl_load_layout);
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","topic");

        if(listBean.type == 4){//转发评论点赞
            params.put("type","praise_reply");
            params.put("id",listBean.reply.id);
        }else {
            params.put("type", "praise");
            params.put("id", listBean.comment.comment_id);
        }

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    if(listBean.type == 4){
                        listBean.reply.praise_count = praiseBean.data.praise_count;
                        listBean.reply.praise_status = praiseBean.data.praise_status;
                    }else {
                        listBean.comment.praise_count = praiseBean.data.praise_count;
                        listBean.comment.praise_status = praiseBean.data.praise_status;
                    }
                    myLvAdapter.notifyDataSetChanged();
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


}
