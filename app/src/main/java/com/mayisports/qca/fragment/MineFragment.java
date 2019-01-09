package com.mayisports.qca.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.google.gson.Gson;
import com.mayi.mayisports.MainActivity;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.BindPhoneNumActivity;
import com.mayi.mayisports.activity.CoinDetailActivity;
import com.mayi.mayisports.activity.CollectionActivity;
import com.mayi.mayisports.activity.FansListActivity;
import com.mayi.mayisports.activity.GuessingCompetitionActivity;
import com.mayi.mayisports.activity.LoginActivity;
import com.mayi.mayisports.activity.MyFollowerListActivity;
import com.mayi.mayisports.activity.MyInfoActivity;
import com.mayi.mayisports.activity.PersonalDetailActivity;
import com.mayi.mayisports.activity.PhoneLoginActivity;
import com.mayi.mayisports.activity.SetPrefrencesActivity;
import com.mayi.mayisports.activity.SettingActivity;
import com.mayi.mayisports.activity.SystemMsgActivity;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.bean.MainMetaBean;
import com.mayisports.qca.bean.MineUserInfoBean;
import com.mayisports.qca.bean.RewardBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mob.tools.utils.UIHandler;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 我的 模块
 * Created by Zpj on 2017/12/5.
 */

public class MineFragment extends Fragment implements View.OnClickListener, PlatformActionListener, Handler.Callback, RequestHttpCallBack.ReLoadListener {


    private LocalBroadcastManager localBroadcastManager;
    private Rec rec;
    public static final String MINE_FG_ACTION = "mine_fg";
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(MINE_FG_ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }

    private void destroyReceiver(){
        if(localBroadcastManager != null && rec != null) {
            localBroadcastManager.unregisterReceiver(rec);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }


    @Override
    public void onResume() {
        super.onResume();


        if(!Utils.isLogin()) {
            updateViewUnLogin();
        }


        initData();
    }

    @Override
    public void onReload() {

    }

    private class Rec extends BroadcastReceiver{

            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getIntExtra(RESULT,0) == 1){

                    try {
                        MainActivity activity = (MainActivity) MineFragment.this.getActivity();
                        activity.setSelectFirst();
                    }catch (Exception e){

                    }
                }else if(intent.getIntExtra(RESULT,0) == 3){//支付回调
//                    ToastUtils.toast("支付回调");
//                    initView();
//                    initData();
                }else if(intent.getIntExtra(RESULT,0) == 4){//偏好设置  退出登录
                    try {
                        Intent intent5 = new Intent(MainQcaFragment.ACTION);
                        intent5.putExtra(RESULT, 22);
                        LocalBroadcastManager.getInstance(MineFragment.this.getContext()).sendBroadcast(intent5);

                        MainActivity activity = (MainActivity) MineFragment.this.getActivity();
                        activity.setSelectHome();

                    }catch (Exception e){

                    }

                }else if(intent.getIntExtra(RESULT,0) == 66){//控制消息红点
                    int count = intent.getIntExtra("count", 0);
                    if(tv_msg_count_mine != null) {
                        Utils.setMsgCount(tv_msg_count_mine, count, false);
                    }
                }else if(intent.getIntExtra(RESULT,0) == 99){//刷新数据
                    initData();

                } else{//未登录返回，到首页
                    try {
                        MainActivity activity = (MainActivity) MineFragment.this.getActivity();
                        activity.setSelectHomeWithStutas();
                    }catch (Exception e){

                    }
                }


            }
        }

        private  View viewRoot;

        private TextView tv_title;
        private TextView tv_name_mine;
        private ImageView iv_header_mine;
        private TextView tv_topic_mine;
        private TextView tv_subsri_mine;
        private TextView tv_followers_mine;
        private TextView tv_view_mine;
        private TextView tv_coin_mine;

        private View ll_myinfo_mine_fg;
        private LinearLayout ll_setting_mine;
        private View ll_0_mine_fg;
        private View ll_1_mine_fg;
        private View ll_2_mine_fg;
        private View ll_3_mine_fg;
        private View ll_coin_ming_fg;
        private View ll_mypage_mine_fg;
        private View ll_groom_mine_fg;
        private View ll_buy_mine_fg;
        private View iv_vip_header;

        private XRefreshView xfv_mine_fg;

        private LinearLayout ll_apply_vip_mine;
        private LinearLayout ll_prefrencens_mine;
        private LinearLayout ll_qca_talent_mine;
        private LinearLayout ll_collection_mine;
        private View v_qca_talent_mine;

        private FrameLayout fl_login_mine;
        private LinearLayout ll_msg_mine;

        public TextView tv_msg_count_mine;

        private ImageView iv_bottom_mine;

        private RelativeLayout rl_load_layout;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if(viewRoot == null){
                viewRoot = View.inflate(getContext(), R.layout.mine_fg,null);
            }
            return viewRoot;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            initView();
            delCache();
//            initData();
        }

        private void delCache(){
            String json = SPUtils.getString(this.getActivity(), Constant.MINE_PAGE_CACHE);
            if(!TextUtils.isEmpty(json)){

                if(Utils.isLogin()) {
                    try {
                        mineUserInfoBean = JsonParseUtils.parseJsonClass(json, MineUserInfoBean.class);
                        updateView();
                    }catch (Exception e){

                    }
                }
            }
        }

        private MineUserInfoBean mineUserInfoBean;
        private void initData() {
            String url = Constant.BASE_URL + "/php/api.php";
            HttpParams params = new HttpParams();
            params.put("action","user");
            params.put("type","userinfo");
            RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
                @Override
                public void onSucces(String string) {
                    if(xfv_mine_fg != null) {
                        xfv_mine_fg.stopRefresh();
                    }
                    if(Utils.isLogin()) {
                        try {
                            mineUserInfoBean = JsonParseUtils.parseJsonClass(string, MineUserInfoBean.class);
                            SPUtils.putString(QCaApplication.getContext(), Constant.MINE_PAGE_CACHE,string);
                            updateView();
                        }catch (Exception e){

                        }
                    }else{
                        updateViewUnLogin();
                    }
                }

                @Override
                public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
                    if(xfv_mine_fg != null) {
                        xfv_mine_fg.stopRefresh();
                    }
                }
            });
        }

    /**
     * 未登录状态
     */
    private void updateViewUnLogin() {
        ll_qca_talent_mine.setVisibility(View.GONE);
        v_qca_talent_mine.setVisibility(View.GONE);
        tv_followers_mine.setText("-");
        tv_topic_mine.setText("-");
        tv_subsri_mine.setText("-");

        ll_myinfo_mine_fg.setVisibility(View.INVISIBLE);
        fl_login_mine.setVisibility(View.VISIBLE);
        tv_coin_mine.setText("");
        tv_msg_count_mine.setVisibility(View.GONE);

    }

    private void updateView() {

            SPUtils.putString(getContext(),Constant.COIN,mineUserInfoBean.user.coin+"");
            SPUtils.putString(getContext(),Constant.WITHDRAW_COIN,mineUserInfoBean.user.coin2cash+"");
            SPUtils.putString(getContext(),Constant.HEADER_URl,mineUserInfoBean.user.weibo_headurl);

            try {
                if (TextUtils.isEmpty(mineUserInfoBean.user.nickname)) {
                    String mobile = mineUserInfoBean.user.mobile;
                    String mo = mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
                    mineUserInfoBean.user.nickname = mo;
                }
            }catch (Exception e){

            }

            SPUtils.putString(getContext(),Constant.NICK_NAME,mineUserInfoBean.user.nickname+"");

            if(!TextUtils.isEmpty(mineUserInfoBean.user.mobile)){
                Utils.setPhoneNum(mineUserInfoBean.user.mobile);
            }else{
                Utils.setPhoneNum("");
            }



                MineUserInfoBean.WeiboBean weibo = mineUserInfoBean.user.weibo;
                if(mineUserInfoBean.user.weibo != null && !TextUtils.isEmpty(weibo.user_id)){
                    Utils.setWeiboData(weibo.user_id,weibo.token,weibo.nickname);
                }else{
                    Utils.setWeiboData("","","");
                }


            MineUserInfoBean.WeiboBean weixin = mineUserInfoBean.user.weixin;
            if(weixin != null && !TextUtils.isEmpty(weixin.user_id)){
                Utils.setWeiXinData(weixin.user_id,weixin.token,weixin.nickname);
            }else{
                Utils.setWeiXinData("","","");
            }

            MineUserInfoBean.WeiboBean qq = mineUserInfoBean.user.qq;
            if(qq != null && !TextUtils.isEmpty(qq.user_id)){
                Utils.setQQData(qq.user_id,qq.token,qq.nickname);
            }else{
                Utils.setQQData("","","");
            }





            if(!TextUtils.isEmpty(mineUserInfoBean.user.sports_category)){
                SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,false);
                SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,false);
                String[] split = mineUserInfoBean.user.sports_category.split(",");
                if(split.length == 1){
                    if("1".equals(split[0])){
                        SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,true);
                    }else if("2".equals(split[0])){
                        SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,true);
                    }
                }else if(split.length == 2){
                    if("1".equals(split[0])){
                        SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,true);
                    }else if("2".equals(split[0])){
                        SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,true);
                    }

                    if("1".equals(split[1])){
                        SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,true);
                    }else if("2".equals(split[1])){
                        SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,true);
                    }
                }


            }


            PictureUtils.showCircle(mineUserInfoBean.user.weibo_headurl,iv_header_mine);

            if(!TextUtils.isEmpty(mineUserInfoBean.user.verify_type)&&Integer.valueOf(mineUserInfoBean.user.verify_type)>0){
                iv_vip_header.setVisibility(View.VISIBLE);
                ll_apply_vip_mine.setVisibility(View.GONE);
                SPUtils.putString(getContext(),Constant.V_TYPE,mineUserInfoBean.user.verify_type);
            }else{
                iv_vip_header.setVisibility(View.GONE);
                ll_apply_vip_mine.setVisibility(View.GONE);
                SPUtils.putString(getContext(),Constant.V_TYPE,"");
            }

            ll_myinfo_mine_fg.setVisibility(View.VISIBLE);
            fl_login_mine.setVisibility(View.GONE);

            Utils.setMsgCount(tv_msg_count_mine,mineUserInfoBean.user.msg_count,false);


//           iv_bottom_mine.setVisibility(View.VISIBLE);
//           fl_bottom_mine.setVisibility(View.VISIBLE);


            tv_name_mine.setText(mineUserInfoBean.user.nickname+"");
            tv_topic_mine.setText(mineUserInfoBean.user.topic_count+"");
            tv_subsri_mine.setText(mineUserInfoBean.user.my_friends_count+"");
            tv_followers_mine.setText(mineUserInfoBean.user.followers_count+"");
            tv_view_mine.setText(mineUserInfoBean.user.view_count+"");
            if(mineUserInfoBean.user.coin == 0.0){
                tv_coin_mine.setText("余额: 0" +"\t\t");
            }else if(mineUserInfoBean.user.coin%1 >0){
                tv_coin_mine.setText("余额: "+ mineUserInfoBean.user.coin+"\t\t");
            }else{
                tv_coin_mine.setText("余额: "+((int)mineUserInfoBean.user.coin)+"\t\t");
            }

            if(false&&"6".equals(mineUserInfoBean.user.subtype)){
                ll_qca_talent_mine.setVisibility(View.VISIBLE);
                v_qca_talent_mine.setVisibility(View.VISIBLE);
            }else{
                ll_qca_talent_mine.setVisibility(View.GONE);
                v_qca_talent_mine.setVisibility(View.GONE);
            }

        }


    private LinearLayout ll_bottom_mine;

    private ImageView iv_phone_login;
    private ImageView iv_weixin_login;
    private ImageView iv_weibo_login;
    private ImageView iv_qq_login;
    private void initView() {
            initReceiver();


            rl_load_layout = viewRoot.findViewById(R.id.rl_load_layout);

            iv_phone_login = viewRoot.findViewById(R.id.iv_phone_login);
            iv_weixin_login = viewRoot.findViewById(R.id.iv_weixin_login);
            iv_weibo_login = viewRoot.findViewById(R.id.iv_weibo_login);
            iv_qq_login = viewRoot.findViewById(R.id.iv_qq_login);
            iv_phone_login.setOnClickListener(this);
            iv_weixin_login.setOnClickListener(this);
            iv_weibo_login.setOnClickListener(this);
            iv_qq_login.setOnClickListener(this);


            ll_qca_talent_mine = viewRoot.findViewById(R.id.ll_qca_talent_mine);
            ll_collection_mine = viewRoot.findViewById(R.id.ll_collection_mine);
            v_qca_talent_mine = viewRoot.findViewById(R.id.v_qca_talent_mine);
            ll_qca_talent_mine.setOnClickListener(this);
            viewRoot.findViewById(R.id.iv_left_title).setVisibility(View.INVISIBLE);
            tv_title = viewRoot.findViewById(R.id.tv_title);
            tv_title.setText("我");
            viewRoot.findViewById(R.id.tv_ritht_title).setVisibility(View.INVISIBLE);

            iv_vip_header = viewRoot.findViewById(R.id.iv_vip_header);
            tv_name_mine = viewRoot.findViewById(R.id.tv_name_mine);
            iv_header_mine = viewRoot.findViewById(R.id.iv_header_mine);
            tv_topic_mine = viewRoot.findViewById(R.id.tv_topic_mine);
            tv_subsri_mine = viewRoot.findViewById(R.id.tv_subsri_mine);
            tv_followers_mine = viewRoot.findViewById(R.id.tv_followers_mine);
            tv_view_mine = viewRoot.findViewById(R.id.tv_view_mine);
            tv_coin_mine = viewRoot.findViewById(R.id.tv_coin_mine);

            ll_setting_mine = viewRoot.findViewById(R.id.ll_setting_mine);
            ll_setting_mine.setOnClickListener(this);

            ll_0_mine_fg = viewRoot.findViewById(R.id.ll_0_mine_fg);
            ll_0_mine_fg.setOnClickListener(this);
            ll_1_mine_fg = viewRoot.findViewById(R.id.ll_1_mine_fg);
            ll_1_mine_fg.setOnClickListener(this);
            ll_2_mine_fg = viewRoot.findViewById(R.id.ll_2_mine_fg);
            ll_2_mine_fg.setOnClickListener(this);
            ll_3_mine_fg = viewRoot.findViewById(R.id.ll_3_mine_fg);
            ll_3_mine_fg.setOnClickListener(this);
            ll_coin_ming_fg = viewRoot.findViewById(R.id.ll_coin_ming_fg);
            ll_coin_ming_fg.setOnClickListener(this);
            ll_mypage_mine_fg = viewRoot.findViewById(R.id.ll_mypage_mine_fg);
            ll_mypage_mine_fg.setOnClickListener(this);
            ll_groom_mine_fg = viewRoot.findViewById(R.id.ll_groom_mine_fg);
            ll_groom_mine_fg.setOnClickListener(this);
            ll_buy_mine_fg = viewRoot.findViewById(R.id.ll_buy_mine_fg);
            ll_buy_mine_fg.setOnClickListener(this);
            ll_myinfo_mine_fg = viewRoot.findViewById(R.id.ll_myinfo_mine_fg);
            ll_myinfo_mine_fg.setOnClickListener(this);

//            xfv_mine_fg = viewRoot.findViewById(R.id.xfv_mine_fg);
//            xfv_mine_fg.setPullRefreshEnable(false);//设置允许下拉刷新
//            xfv_mine_fg.setPullLoadEnable(false);//设置允许上拉加载
//            //刷新动画，需要自定义CustomGifHeader，不需要修改动画的会默认头布局
////        CustomGifHeader header = new CustomGifHeader(ct);
////        xfv_home_fg.setCustomHeaderView(header);
//            xfv_mine_fg.setMoveForHorizontal(true);
//
//            xfv_mine_fg.setXRefreshViewListener(new MyListener());

            ll_apply_vip_mine = viewRoot.findViewById(R.id.ll_apply_vip_mine);
            ll_apply_vip_mine.setOnClickListener(this);

            ll_prefrencens_mine = viewRoot.findViewById(R.id.ll_prefrencens_mine);
            ll_prefrencens_mine.setOnClickListener(this);

            fl_login_mine = viewRoot.findViewById(R.id.fl_login_mine);
            fl_login_mine.setOnClickListener(this);

            ll_msg_mine = viewRoot.findViewById(R.id.ll_msg_mine);
            ll_msg_mine.setOnClickListener(this);

            tv_msg_count_mine = viewRoot.findViewById(R.id.tv_msg_count_mine);

            ll_collection_mine.setOnClickListener(this);

            iv_bottom_mine = viewRoot.findViewById(R.id.iv_bottom_mine);//底部新增图片入口
            ViewGroup.LayoutParams layoutParams = iv_bottom_mine.getLayoutParams();
            layoutParams.height = (int) (DisplayUtil.getScreenWidth(this.getActivity())*0.32);
            iv_bottom_mine.setLayoutParams(layoutParams);

            iv_bottom_mine.setOnClickListener(this);

            ll_bottom_mine = viewRoot.findViewById(R.id.ll_bottom_mine);

//            PictureUtils.showRounded(R.drawable.mine_bottom_icon,iv_bottom_mine);
          requestMeta();

        }

    /**
     * 请求初始化数据
     */
    private MainMetaBean mainMetaBean;
    private void requestMeta() {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","meta");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                mainMetaBean = JsonParseUtils.parseJsonClass(string,MainMetaBean.class);
                if(mainMetaBean != null  ){

                    if(mainMetaBean.bannerList != null && mainMetaBean.bannerList.size()>0){
                        MainMetaBean.BannerBean bannerBean = mainMetaBean.bannerList.get(0);
                        SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_IMG,bannerBean.img);
                        SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_URL,bannerBean.url);
//                        iv_bottom_mine.setVisibility(View.VISIBLE);
                        ll_bottom_mine.setVisibility(View.VISIBLE);
//                        PictureUtils.showRounded(bannerBean.img,iv_bottom_mine);

                        addBottomBanner(mainMetaBean);
                    } else{
                        SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_IMG,"");
                        SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_URL,"");
                        iv_bottom_mine.setVisibility(View.GONE);
                        ll_bottom_mine.setVisibility(View.GONE);
                    }

                }else{
                    SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_IMG,"");
                    SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_URL,"");
                    iv_bottom_mine.setVisibility(View.GONE);
                    ll_bottom_mine.setVisibility(View.GONE);
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

            }
        });

    }


    /**
     * 添加底部广告位图片
     * @param mainMetaBean
     */
    private void addBottomBanner(MainMetaBean mainMetaBean) {

        if(mainMetaBean == null && mainMetaBean.bannerList == null)return;
        ll_bottom_mine.removeAllViews();
        List<MainMetaBean.BannerBean> bannerList = mainMetaBean.bannerList;
        for(int i = 0;i<bannerList.size();i++){
            final MainMetaBean.BannerBean bannerBean = bannerList.get(i);
            View view = View.inflate(QCaApplication.getContext(),R.layout.img_bottom_layout,null);
            ImageView iv_bottom_mine = view.findViewById(R.id.iv_bottom_mine);
            PictureUtils.showRounded(bannerBean.img,iv_bottom_mine);

            iv_bottom_mine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(LoginUtils.isUnLogin()){
                        LoginUtils.goLoginActivity(MineFragment.this.getActivity(), MineFragment.MINE_FG_ACTION);
                        return;
                    }
                    WebViewActivtiy.start(getActivity(),bannerBean.url,"球咖",true);
//
                }
            });
            ll_bottom_mine.addView(view);
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_myinfo_mine_fg:
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/person?toMyInfo=1","我的资料");
                MyInfoActivity.start(this.getActivity());
                break;
            case R.id.ll_setting_mine:
                Intent intent = new Intent(this.getActivity(), SettingActivity.class);
                startActivity(intent);
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/mySet","设置");
                break;
            case R.id.ll_0_mine_fg:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }

                Intent intent0 = new Intent(this.getActivity(),PersonalDetailActivity.class);
                intent0.putExtra("id",SPUtils.getString(this.getContext(),Constant.USER_ID)+"");
                intent0.putExtra("type",1);
                String json = new Gson().toJson(mineUserInfoBean);
                PersonalDetailActivity.start(this,SPUtils.getString(this.getContext(),Constant.USER_ID)+"",0,0,json);
//                startActivity(intent0);
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/personHomepage/"+SPUtils.getString(this,Constant.USER_ID)+",15","话题");
                break;
            case R.id.ll_1_mine_fg:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/mySubcription","订阅");
                MyFollowerListActivity.start(getActivity());
                break;
            case R.id.ll_2_mine_fg:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/fansAndView/"+SPUtils.getString(this.getContext(),Constant.USER_ID)+",fans","粉丝");
                FansListActivity.start(getActivity());
                break;
            case R.id.ll_3_mine_fg:
                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/fansAndView/"+SPUtils.getString(this.getContext(),Constant.USER_ID)+",view","访客");
                break;
            case R.id.ll_coin_ming_fg:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                Intent intentCoin = new Intent(this.getActivity(), CoinDetailActivity.class);
                startActivity(intentCoin);
//                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/myCoin","我的金币");
                break;
            case R.id.ll_mypage_mine_fg:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                Intent intent1 = new Intent(this.getActivity(), PersonalDetailActivity.class);
                intent1.putExtra("id",SPUtils.getString(this.getContext(),Constant.USER_ID));
                startActivity(intent1);
//                WebViewActivtiy.start(getActivity(), Constant.BASE_URL+"#/personHomepage/"+SPUtils.getString(this.getContext(),Constant.USER_ID)+",6 (userId, from)","我的主页");
                break;
            case R.id.ll_groom_mine_fg:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/myRecommendation","我的推荐");
                break;
            case R.id.ll_buy_mine_fg:
                WebViewActivtiy.start(getActivity(),Constant.BASE_URL+"#/consumeList","已购推荐");
//                startActivity(new Intent(this.getActivity(), GIFActivity.class));
                break;
            case R.id.ll_apply_vip_mine://申请球咖认证
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"applyV/applyV.html?hideDownloadBar=1",true,"申请认证");
                break;
            case R.id.ll_prefrencens_mine:
                SetPrefrencesActivity.start(this.getActivity());
                break;
            case R.id.ll_qca_talent_mine:
                WebViewActivtiy.start(this.getActivity(),Constant.BASE_URL+"#/myActionStatics","球咖达人");
                break;
            case R.id.fl_login_mine:
//                LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                break;
            case R.id.ll_msg_mine:
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                SystemMsgActivity.start(this.getActivity());
                tv_msg_count_mine.setVisibility(View.GONE);
                break;
            case R.id.ll_collection_mine://收藏界面
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                CollectionActivity.start(this.getActivity());
                break;
            case R.id.iv_bottom_mine://底部图片入口
                if(LoginUtils.isUnLogin()){
                    LoginUtils.goLoginActivity(this.getActivity(), MineFragment.MINE_FG_ACTION);
                    return;
                }
                WebViewActivtiy.start(getActivity(),SPUtils.getString(QCaApplication.getContext(),Constant.BANNER_URL),"球咖",true);
//                GuessingCompetitionActivity.start(this.getActivity());

//                NotificationsUtils.toAppDetailSettingActivity(this.getActivity());
//                NotificationsUtils.checkNotificationAndStartSetting(this.getActivity(),iv_bottom_mine);

                break;

            case R.id.iv_phone_login:
                loginByPhone();
                break;
            case R.id.iv_weixin_login:
                iv_weixin_login.setEnabled(false);
                iv_weixin_login.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_weixin_login.setEnabled(true);
                    }
                },2000);
                loginByWeixin();
                break;
            case R.id.iv_weibo_login:
                iv_weibo_login.setEnabled(false);
                iv_weibo_login.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_weibo_login.setEnabled(true);
                    }
                },2000);
                loginByWeibo();
                break;
            case R.id.iv_qq_login:
                iv_qq_login.setEnabled(false);
                iv_qq_login.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        iv_qq_login.setEnabled(true);
                    }
                },2000);
                loginByQQ();
                break;
        }
    }


    /**
     * 手机号登录
     */
    private void loginByPhone(){
        PhoneLoginActivity.start(this.getActivity(),MineFragment.MINE_FG_ACTION);
    }


    /**
     * 微博登陆
     */
    private void loginByWeibo() {
        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        sinaWeibo.setPlatformActionListener(this);
        sinaWeibo.SSOSetting(false);
        authorize(sinaWeibo);
    }

    /**
     * 微信登陆
     */
    private void loginByWeixin() {
        Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
        wechat.setPlatformActionListener(this);
        wechat.SSOSetting(false);
        authorize(wechat);
    }

    /**
     * qq登陆
     */
    private void loginByQQ() {
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(this);
        qq.SSOSetting(false);
        authorize(qq);
    }


    /**
     * 授权
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {  //如果授权就删除授权资料
            platform.removeAccount(true);
        }

        if(rl_load_layout != null) rl_load_layout.setVisibility(View.VISIBLE);
        platform.showUser(null); //授权并获取用户信息
    }
    private static final int MSG_ACTION_CCALLBACK = 0;


    /**
     * 授权
     * @param platform
     * @param i
     */
    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    //登陆发送的handle消息在这里处理
    @Override
    public boolean handleMessage(Message message) {
        switch (message.arg1) {
            case 1:  // 成功


                //获取用户资料
                Platform platform = (Platform) message.obj;
                String userId = platform.getDb().getUserId();//获取用户账号
                final String token = platform.getDb().getToken();
                final String userName = platform.getDb().getUserName();
                final String userIcon = platform.getDb().getUserIcon();
                final int sortId = platform.getSortId();

//                submitWeiboDatas(sortId,userId,token,userName,userIcon);
                if(sortId == 4){//微信
                    userId = platform.getDb().get("unionid");
                    getAuthStatus(sortId, userId, token, userName, userIcon);
                }else if(sortId == 7){//qq
                    Utils.getQQUID(token, new Utils.OnQQUIdListener() {
                        @Override
                        public void onOk(String uid, String data) {
                            String id = uid;
                            getAuthStatus(sortId,uid,token,userName,userIcon);
                        }

                        @Override
                        public void onError(String msg) {
                            ToastUtils.toast("请重试");
                            if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                        }
                    });
                    break;
                }else {
                    getAuthStatus(sortId, userId, token, userName, userIcon);
                }

                break;
            case 2:  // 失败
                ToastUtils.toast("登录失败");
                if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                break;
            case 3:  // 取消
                ToastUtils.toast("登录取消");
//                submitWeiboDatas("fdsfadsf","fdsfsdfdsfd");
                if(rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                break;
        }
        return false;
    }


    /**
     * 获取绑定信息
     * @param sortId
     * @param id
     * @param token
     * @param weiboName
     * @param weiboIcon
     */
    private void getAuthStatus(final int sortId,final String id, final String token, final String weiboName,final String weiboIcon){
        String url = Constant.BASE_URL + "/php/api.php";

        HttpParams params = new HttpParams();

        params.put("action","user");
        params.put("type", "get_auth_bind_status");
        params.put("id",id);
        String platformName = Utils.getPlatformNameBySortId(sortId);
        params.put("platform",platformName);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
//                ToastUtils.toast(string);
                //{"status":{"login":1,"errno":0,"id":"200327"}}
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string, RewardBean.class);
                //&&rewardBean.status.first_auth == 1
                if(rewardBean.status.errno == 0){
                    if(rewardBean.status.first_auth == 1){
                        BindPhoneNumActivity.start(MineFragment.this.getActivity(),sortId,id,token,weiboName,weiboIcon);
//                        finish();
                        rl_load_layout.setVisibility(View.GONE);
                    }else{
                        submitWeiboDatas(sortId,id,token,weiboName,weiboIcon);
//                        LoginUtils.saveLoginTags(rewardBean.status.user_id,"");
//                        LoginActivity.loginOk(MineFragment.MINE_FG_ACTION);
//                        initData();
                    }
                }else{
                    ToastUtils.toast(rewardBean.status.errstr+"");
                    rl_load_layout.setVisibility(View.GONE);
                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                rl_load_layout.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 第三方登录提交
     * @param sortId
     * @param id
     * @param token
     * @param weiboName
     * @param weiboIcon
     */
    private void submitWeiboDatas(final int sortId,final String id, final String token, final String weiboName,final String weiboIcon){
        //api.php?action=user&type=authinfo&id=&value=  //api.php?action=user&type=authinfo&id={user_id}&value={token}
        String url = Constant.BASE_URL + "/php/api.php";
        //action=user&type=auth_unlogin&platform={weixin,qq,weibo}
        //api.php?action=user&type=auth_login&platform=(weixin,weibo,qq)&nickname=&headurl=
        HttpParams params = new HttpParams();
        params.put("action","user");
//        params.put("type","authinfo");
        if(TextUtils.isEmpty(token) && TextUtils.isEmpty(id)){
            params.put("type", "auth_unlogin");
        }else {
            params.put("type", "auth_login");
            params.put("id",id);
            params.put("value",token);
            String encode = URLEncoder.encode(weiboName);
            params.put("nickname",encode);
            params.put("headurl",weiboIcon);
        }

        String platformName = Utils.getPlatformNameBySortId(sortId);

        params.put("platform",platformName);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
//                ToastUtils.toast(string);
                //{"status":{"login":1,"errno":0,"id":"200327"}}
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string, RewardBean.class);
                //&&rewardBean.status.first_auth == 1
                if(rewardBean.status.errno == 0 ){
                    ToastUtils.toast("登录成功");
                     LoginUtils.saveLoginTags(rewardBean.status.user_id,"");
                     LoginActivity.loginOk(MineFragment.MINE_FG_ACTION);

                     initData();
                }else{
                    ToastUtils.toast(rewardBean.status.errstr+"");
                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }
}
