package com.mayisports.qca.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.ComentsDetailsActivity;
import com.mayi.mayisports.activity.PersonalDetailActivity;
import com.mayi.mayisports.activity.ReportActivity;
import com.mayi.mayisports.activity.VideoPlayerActivity;
import com.mayisports.qca.bean.ComentsDetailBean;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.JieCaoPlayer;
import com.mayisports.qca.view.MyProgressBar;
import com.mayisports.qca.view.SelectPicPopupWindow;
import com.mayisports.qca.view.SharePopuwind;

import org.kymjs.kjframe.http.HttpParams;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * Created by Zpj on 2018/3/15.
 */

public class VideoPlayerFragment extends Fragment implements View.OnClickListener, PlatformActionListener, RequestHttpCallBack.ReLoadListener {

//

    public static final String ACTION = "VideoPlayerFragment";

    private LocalBroadcastManager localBroadcastManager;
    private Rec rec;

    private void initReceiver() {
        localBroadcastManager = LocalBroadcastManager.getInstance(this.getContext());
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(ACTION);
        localBroadcastManager.registerReceiver(rec, intentFilter);
    }

    private void destroyReceiver() {
        localBroadcastManager.unregisterReceiver(rec);
    }

    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            int intExtra = intent.getIntExtra(RESULT, 0);
            if (intExtra == 11) {//侧滑fragment发来
                if (bean != null) {
                    bean.comment.praise_status = intent.getIntExtra("praise_status", 0);
                    bean.comment.praise_count = intent.getStringExtra("praise_count");
                    bean.comment.collection_status = intent.getStringExtra("collection_status");
                    bean.comment.reply_count = intent.getStringExtra("reply_count");
                    upDatePraise();
                }
            }
        }
    }


    private boolean isInit;
    private boolean isShow;

    public  long currentTime;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        if (isVisibleToUser) {
       //     Log.e("isVisibleToUser",true+"");
//            currentTime = 0;
//            startPlay();
        } else {
         //   Log.e("isVisibleToUser",false+"");
            // JCVideoPlayer.releaseAllVideos();
            if (jc_player_fg != null) {
                jc_player_fg.release();
//               currentTime = 0;
//              mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }
        }


    }


    public static String VideoPlayerFragment = "VideoPlayerFragment";
    private TextView txt;
    private int position;
    private DynamicBean.ListBean bean;

    public static VideoPlayerFragment newInstance(int position, DynamicBean.ListBean bean) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoPlayerFragment, position);
        bundle.putSerializable("bean", bean);
        fragment.setArguments(bundle);
        return fragment;
    }


    private View viewRoot;
    private LinearLayout ll_no_data;

//    private FrameLayout fl_right_video_player_ac;
//    private DrawerLayout mDrawerLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (viewRoot == null) {
            viewRoot = View.inflate(this.getActivity(), R.layout.video_player_fg, null);
        }
        initReceiver();
        initView();
        initData();
        return viewRoot;
    }


    @Override
    public void onStart() {
        super.onStart();
//        Log.e("onStart","onStart----");
//        if(url == null) {
//            initData();
//        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = (Integer) getArguments().getSerializable(VideoPlayerFragment);
            bean = (DynamicBean.ListBean) getArguments().getSerializable("bean");
//            VideoPlayerActivity videoPlayerActivity = (VideoPlayerActivity) getActivity();
//            bean =  videoPlayerActivity.dynamicBean.list.get(position);
        }

    }

    private boolean isPause;
    @Override
    public void onPause() {
        super.onPause();
        if (jc_player_fg == null) return;

        if(jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING){
            isBackPause = true;
        }

        jc_player_fg.release();

//        if (jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING  ) {
//            isPause = true;
//            jc_player_fg.startButton.performClick();
//        }else if(jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START || jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING){
//            jc_player_fg.release();
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();

    }

    public JieCaoPlayer jc_player_fg;
    private LinearLayout ll_top_click;
    private ImageView iv_head_dynamic_item;
    private ImageView iv_vip_header;
    private TextView tv_name_item;
    private TextView tv_create_time_item;
    private ImageView iv_dot_more_item;
    private ImageView iv_back_item;
    private TextView tv_content_item;
    private TextView tv_thumbup_topic_item;
    private TextView tv_comments_topic_item;
    private TextView tv_share_topic_item;





    private MyProgressBar progress_video_player_fg;

    private ProgressBar pb_video_player_fg;

    public boolean isAutoPause;

    private View v_video_click_cover;

    private int preX;

    private float DownX;
    private float DownY;
    private float moveX;
    private float moveY;


    private void initView() {
//
//        mDrawerLayout = (DrawerLayout) viewRoot.findViewById(R.id.drawer);
//        fl_right_video_player_ac = (FrameLayout) viewRoot.findViewById(R.id.fl_right_video_player_ac);
//        fl_right_video_player_ac.getLayoutParams().width = DisplayUtil.getScreenWidth(this.getActivity());


        v_video_click_cover = viewRoot.findViewById(R.id.v_video_click_cover);
        v_video_click_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(jc_player_fg != null){
                    jc_player_fg.startButton.performClick();
                }
            }
        });


        /**
         * 移动到某个时间
         */
//        JCMediaManager.instance().mediaPlayer.seekTo((long)time);

        jc_player_fg = viewRoot.findViewById(R.id.jc_player_fg);


        ll_top_click = viewRoot.findViewById(R.id.ll_top_click);
        iv_head_dynamic_item = viewRoot.findViewById(R.id.iv_head_dynamic_item);
        iv_vip_header = viewRoot.findViewById(R.id.iv_vip_header);
        tv_name_item = viewRoot.findViewById(R.id.tv_name_item);
        tv_create_time_item = viewRoot.findViewById(R.id.tv_create_time_item);
        iv_dot_more_item = viewRoot.findViewById(R.id.iv_dot_more_item);
        iv_back_item = viewRoot.findViewById(R.id.iv_back_item);
        tv_content_item = viewRoot.findViewById(R.id.tv_content_item);
        tv_thumbup_topic_item = viewRoot.findViewById(R.id.tv_thumbup_topic_item);
        tv_comments_topic_item = viewRoot.findViewById(R.id.tv_comments_topic_item);
        tv_share_topic_item = viewRoot.findViewById(R.id.tv_share_topic_item);

        progress_video_player_fg = viewRoot.findViewById(R.id.progress_video_player_fg);
        pb_video_player_fg = viewRoot.findViewById(R.id.pb_video_player_fg);
        jc_player_fg.setProgress(progress_video_player_fg);
        progress_video_player_fg.setOnValueChangeListener(new MyProgressBar.OnValueChangeListener() {
            @Override
            public void onProgressChange(int progress) {
                pb_video_player_fg.setProgress(progress);
            }

            @Override
            public void onSecondaryProgressChange(int secondaryProgress) {
               pb_video_player_fg.setSecondaryProgress(secondaryProgress);
            }
        });

        iv_dot_more_item.setOnClickListener(this);
        iv_back_item.setOnClickListener(this);

    }


    public void openDrawer() {
        if(jc_player_fg == null)return;
        if (jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
            try {
                jc_player_fg.startButton.performClick();
            } catch (Exception e) {
                ToastUtils.toast("网络有点问题。。。退出重试哦");
            }
//                    jc_player_fg.startButton.performClick();
            isAutoPause = true;
        }

        /**
         * 更新侧滑框
         */

    }

    public boolean isBackPause;

    public void closeDrawer() {
        if(jc_player_fg == null)return;
        if ((jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PAUSE && isAutoPause) || isBackPause) {
            try {
                jc_player_fg.startButton.performClick();
            } catch (Exception e) {
                ToastUtils.toast("网络有点问题。。。退出重试哦");
            }
//                    jc_player_fg.startButton.performClick();
            isAutoPause = false;
            isBackPause = false;
        }
//        upDateMainContent();
    }

    private boolean setUp;
    private boolean isReturn;

    private boolean isJcNull;
    public void startPlay() {
        if (jc_player_fg == null) {
            isReturn = true;
            isJcNull = true;
            return;
        }
        isJcNull = false;
        jc_player_fg.post(new Runnable() {
            @Override
            public void run() {


        isReturn = false;

        try {
//            if(currentTime != 0) {
//                jc_player_fg.seekToTime =  currentTime;
//                jc_player_fg.release();
//                currentTime = 0;
//            }
                jc_player_fg.startButton.performClick();


        } catch (Exception e) {
            ToastUtils.toast("网络有点问题。。。退出重试哦");
        }



            }
        });

    }


    public void initData() {

        //
        if (jc_player_fg == null || bean == null || bean.comment == null || bean.comment.imgListEx == null) return;

//        if(isPause) {
//            jc_player_fg.startButton.performClick();
//            return;
//        }

        String  url = bean.comment.imgListEx.get(0).url;
        if (!TextUtils.isEmpty(url)) {

            String videoUrl = url;
            jc_player_fg.release();
            if (!setUp) {

                setUp = jc_player_fg.setUp(videoUrl, JCVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");

//                jc_player_fg.seekToTime =  currentTime;
                currentTime = 0;
                Log.e("currentTime",currentTime+"");
                if (setUp) {
                    String imgUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".png";
                    PictureUtils.show(imgUrl, jc_player_fg.thumbImageView);
                    if (isReturn) {
                        try {
                            if(isJcNull) {
                                Log.e("video", "initData");
                                FragmentActivity activity = getActivity();
                                if(activity != null){
                                        jc_player_fg.startButton.performClick();

                                }

                            }

                        } catch (Exception e) {
                            ToastUtils.toast("网络有点问题。。。退出重试哦");
                        }
                    }


                }
            } else {
                if (isReturn) {
                    try {
//                        jc_player_fg.seekToInAdvance = (int) currentTime;
//                        jc_player_fg.startButton.performClick();

                    } catch (Exception e) {
                        ToastUtils.toast("网络有点问题。。。退出重试哦");
                    }
                }

            }
        }

        if (bean.user != null) {
            PictureUtils.showCircle(bean.user.headurl + "", iv_head_dynamic_item);
            tv_name_item.setText(bean.user.nickname);

            if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                iv_vip_header.setVisibility(View.VISIBLE);
            } else {
                iv_vip_header.setVisibility(View.GONE);
            }
        }

        String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
        tv_create_time_item.setText(createTime);

        try {
            if (bean.comment.comment.length() > 80) {
                bean.comment.comment = bean.comment.comment.substring(0, 79) + "...";
            }
            tv_content_item.setText(bean.comment.comment);
        }catch (Exception e){

        }


        ll_top_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopVideo();
                PersonalDetailActivity.start(VideoPlayerFragment.this, bean.user.user_id, 0, 2, "{}");
            }
        });

        tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestThumbup(bean);
            }
        });


        tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (LoginUtils.isUnLogin()) {
//                    LoginUtils.goLoginActivity(VideoPlayerFragment.this.getActivity(), "");
//                    return;
//                }
                String url = Constant.BASE_URL + "#/topicViewDetail/" + bean.comment.comment_id;
//                stopVideo();
//                ComentsDetailsActivity.startForResult(2, VideoPlayerFragment.this, bean.comment.comment_id, position, true);
                VideoPlayerActivity videoPlayerActivity = (VideoPlayerActivity) getActivity();
                videoPlayerActivity.openDrawer();

            }
        });


        tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
//                showShare();
                VideoPlayerFragment.this.onClick(iv_dot_more_item);
            }
        });

        upDatePraise();

    }

    private void stopVideo() {
        if (jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
            try {
                jc_player_fg.startButton.performClick();
            } catch (Exception e) {
                ToastUtils.toast("网络有点问题。。。退出重试哦");
            }
        }
    }

    private void upDatePraise() {
        if (bean.comment.praise_status == 1) {//点赞
            Drawable drawable = QCaApplication.getContext().getResources().getDrawable(R.drawable.thumbup_ok_video_player);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_thumbup_topic_item.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = QCaApplication.getContext().getResources().getDrawable(R.drawable.thumbup_no_video_player);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_thumbup_topic_item.setCompoundDrawables(null, drawable, null, null);
        }

        DataBindUtils.setPraiseCount(tv_thumbup_topic_item, bean.comment.praise_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count+"");
        DataBindUtils.setComentCount(tv_comments_topic_item, bean.comment.reply_count + "");
//

    }

    /**
     * 通知主界面侧滑栏 更新相关数据
     */
    private void upDateMainContent() {
//        VideoPlayerActivity videoPlayerActivity = (VideoPlayerActivity) getActivity();
//        videoPlayerActivity.updateContent(bean);
        Intent intent = new Intent(ComentsDetailsFragment.ACTION);
        intent.putExtra(RESULT, 11);
        intent.putExtra("praise_status", bean.comment.praise_status);
        intent.putExtra("praise_count", bean.comment.praise_count);
        intent.putExtra("collection_status", bean.comment.collection_status);
        LocalBroadcastManager.getInstance(this.getActivity().getBaseContext()).sendBroadcast(intent);

    }

    private void showShare() {
        SharePopuwind sharePopuwind = new SharePopuwind(this.getActivity(), new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                DynamicBean.ListBean beanX = bean;
                if (beanX.topic == null) {
//                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id, beanX.comment.comment, beanX.user.nickname, VideoPlayerFragment.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname, VideoPlayerFragment.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(tv_comments_topic_item, Gravity.CENTER, 0, 0);

    }

    /**
     * 处理点赞
     *
     * @param bean
     */
    private void requestThumbup(final DynamicBean.ListBean bean) {

        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this.getActivity(), "");
            return;
        }

        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this.getActivity(), "");
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action", "topic");
        params.put("type", "praise");
        params.put("id", bean.comment.comment_id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null, this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string, PraiseBean.class);
                if (praiseBean.data != null) {
                    bean.comment.praise_count = praiseBean.data.praise_count;
                    bean.comment.praise_status = praiseBean.data.praise_status;
                    upDateMainContent();
                    upDatePraise();
                } else {
                    ToastUtils.toast(praiseBean.status.errstr);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });

    }


    private SelectPicPopupWindow selectPicPopupWindow;
    private boolean isDelete;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_dot_more_item:


//                showShare();
                //弹窗
                if (selectPicPopupWindow == null) {
                    selectPicPopupWindow = new SelectPicPopupWindow(VideoPlayerFragment.this.getActivity(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_share_photo://分享
//                                    selectPicPopupWindow.dismiss();
//                                    showShare();
                                    if (selectPicPopupWindow.rg_share_fg.isShown()) {
                                        selectPicPopupWindow.rg_share_fg.setVisibility(View.GONE);
                                        selectPicPopupWindow.btn_share_photo.setText("分享");
                                    } else {
                                        selectPicPopupWindow.btn_share_photo.setText("取消分享");
                                        selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                                    }
                                    break;
                                case R.id.btn_take_photo://收藏

                                    selectPicPopupWindow.dismiss();
                                    if (LoginUtils.isUnLogin()) {
                                        LoginUtils.goLoginActivity(VideoPlayerFragment.this.getActivity(), HomeNewFragment.ACTION);
                                        return;
                                    }
                                    requestCollect();
                                    break;
                                case R.id.btn_pick_photo://举报
                                    selectPicPopupWindow.dismiss();
                                    if (isDelete) {
//                                        ComentsDetailsActivity.this.onClick(iv_delect_header);
                                    } else {
                                        ReportActivity.start(VideoPlayerFragment.this.getActivity(), bean.user.user_id, bean.user.nickname, bean.create_time + "");
                                    }
                                    break;
                            }
                        }
                    }, new SharePopuwind.ShareTypeClickListener() {
                        @Override
                        public void onTypeClick(int type) {
                            DynamicBean.ListBean.CommentBean comment = bean.comment;
//                            if (TextUtils.isEmpty(bean.topic.topic_id) || "0".equals(bean.topic.topic_id)) {
//
//                                ShareUtils.shareMsgForPointNoTopic(type, comment.comment_id, comment.comment, bean.user.nickname, VideoPlayerFragment.this);
//                            } else {
//                                ShareUtils.shareMsgForPointHasTopic(type, comment.comment_id, bean.topic.title, bean.user.nickname, VideoPlayerFragment.this);
//                            }
                            String videoUrl = comment.imgListEx.get(0).url;
                            String imgUrl = videoUrl.substring(0, videoUrl.lastIndexOf(".")) + ".png";
                            ShareUtils.shareMsgForShootVideo(type,bean.user.user_id,comment.comment_id,comment.comment,imgUrl,bean.user.headurl,bean.user.nickname,VideoPlayerFragment.this);
                        }
                    });
                }
                selectPicPopupWindow.btn_share_photo.setVisibility(View.GONE);
                selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                selectPicPopupWindow.tv_share_title.setVisibility(View.VISIBLE);
                if (isDelete) {//删除键
//                    selectPicPopupWindow.btn_pick_photo.setText("删除");
                    selectPicPopupWindow.btn_pick_photo.setVisibility(View.GONE);
                } else {//举报键
                    selectPicPopupWindow.btn_pick_photo.setText("举报");
                    selectPicPopupWindow.btn_pick_photo.setVisibility(View.VISIBLE);
                }

                if ("1".equals(bean.comment.collection_status)) {
                    selectPicPopupWindow.btn_take_photo.setText("取消收藏");
                } else {
                    selectPicPopupWindow.btn_take_photo.setText("收藏");
                }

                selectPicPopupWindow.showAtLocation(tv_comments_topic_item, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.iv_back_item:
                getActivity().finish();
                break;
        }
    }


    /**
     * 收藏功能
     */
    private void requestCollect() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action", "user");
        params.put("type", "collection");
        /**
         * match:1 comment:2 recommendation:5 topic:6
         */
        params.put("subtype", 2);
        params.put("id", bean.comment.comment_id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string, ReportSubmitBean.class);
                if (reportSubmitBean.status.result == 1) {
                    ToastUtils.toast("收藏成功");
                    bean.comment.collection_status = "1";
                } else if (reportSubmitBean.status.result == 0) {
                    ToastUtils.toast("取消收藏");
                    bean.comment.collection_status = "0";
                }
                upDateMainContent();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
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

    @Override
    public void onReload() {

    }


//    private void initData() {
//        page = 0;
//        requestNetData();
//    }
//
//    /**
//     * 短视频
//     */
//    private String tagId = "-4";
//    private DynamicBean dynamicBean;
//    private int page;
//    private int pre_page = 1;
//    private void requestNetData() {
//
//        String url = Constant.BASE_URL + "php/api.php";
//        HttpParams params = new HttpParams();
//
//        params.put("action","sys_feed");
//        params.put("type",tagId);
//        params.put("start",page);
//
//        if(page>0){
//            params.put("loadMore",0);
//        }else{
//            params.put("loadMore",1);
//        }
//
//        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_clone,this) {
//            @Override
//            public void onSucces(String string) {
//                if(xfv_home_fg != null) {
//                    xfv_home_fg.stopRefresh();
//                    xfv_home_fg.stopLoadMore();
//                }
//
//                if(page == 0) {
//
//                    dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
//                    DynamicBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
//
//                    if(dynamicBean!= null&&dynamicBean.data != null&&dynamicBean.list == null){
//                        if(dynamicBean.data.list != null){
//                            dynamicBean.list = dynamicBean.data.list;
//                            dynamicBean1.list = dynamicBean1.data.list;
//                        }
//                    }
//                    delData(dynamicBean,dynamicBean1);
//
//
//
//                    if(dynamicBean != null && dynamicBean.list != null){
//                        if(dynamicBean.list.size() == 0){
//                            if(ll_no_data != null) xfv_home_fg.setPullLoadEnable(false);
//                            if(ll_no_data != null) ll_no_data.setVisibility(View.VISIBLE);
////                            preCreateTime = "";
//                        }else{
////                            xfv_dynamic_fg.setPullLoadEnable(true);
//                            if(ll_no_data != null)ll_no_data.setVisibility(View.GONE);
//                        }
//                    }else{
//                        if(ll_no_data != null)xfv_home_fg.setPullLoadEnable(false);
//                        if(ll_no_data != null)ll_no_data.setVisibility(View.VISIBLE);
////                        preCreateTime = "";
//                    }
//                    pre_page = 1;
////                    setTopTitleShow();
//                    lv_home_fg.setAdapter(myAdapter);
//                }else{
//                    try {
//                        DynamicBean newBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
//                        DynamicBean newBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
//
//                        if(newBean!= null&&newBean.data != null&&newBean.list == null){
//                            if(newBean.data.list != null){
//                                newBean.list = newBean.data.list;
//                                newBean1.list = newBean1.data.list;
//                            }
//                        }
//                        delData(newBean,newBean1);
//                        dynamicBean.list.addAll(newBean.list);
//                    }catch (Exception e){
////                        ToastUtils.toast("没有更多了。。。");
//                        pre_page = 0;
//                    }
//                    myAdapter.notifyDataSetChanged();
//                }
//
//            }
//
//            @Override
//            public void onfailure(int errorNo, String strMsg) {
//                ToastUtils.toast(Constant.NET_FAIL_TOAST);
//
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                rl_load_clone = rl_load_layout;
//                if(xfv_home_fg != null) {
//                    xfv_home_fg.stopRefresh();
//                    xfv_home_fg.stopLoadMore();
//                }
//
//            }
//        });
//    }
//
//
//
//    /**
//     * 处理数据  type 1 显示
//     * @param homeDataBean
//     * @param newBean
//     */
//    private void delData(DynamicBean homeDataBean, DynamicBean newBean) {
//        if(homeDataBean.list != null&&newBean.list != null){
//            homeDataBean.list.clear();
//            for(int i = 0;i<newBean.list.size();i++){
//
//                DynamicBean.ListBean beanX = newBean.list.get(i);
//                if(beanX.type == 1 || beanX.type == 3  ||beanX.type == 6 ||beanX.type == 5) {//1推荐单  3 话题 6话题  5可能感兴趣的人
//                    homeDataBean.list.add(beanX);
//                }
//            }
//        }
//    }beanX
}
