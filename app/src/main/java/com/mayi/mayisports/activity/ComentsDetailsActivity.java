package com.mayi.mayisports.activity;

import android.app.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.google.gson.reflect.TypeToken;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.ComentsDetailBean;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.PublishPointBean;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.TopicDetailBean;
import com.mayisports.qca.fragment.ComentsDetailsFragment;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.GridViewUtils;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NetWorkUtil;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.SelectPicPopupWindow;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;

import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class ComentsDetailsActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {

    //http://app.mayisports.com/php/api.php?action=topic&type=get_reply_list&id=38784&start=0

    public static void start(Activity activity, String commentId) {
        Intent intent = new Intent(activity, ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        activity.startActivity(intent);
    }

    public static void startForResult(int requestCode, Activity activity, String commentId, int position) {
        Intent intent = new Intent(activity, ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("position", position);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(boolean isAutoStart, int requestCode, Activity activity, String commentId, int position) {
        Intent intent = new Intent(activity, ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("position", position);
        intent.putExtra("isAutoStart", isAutoStart);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(int requestCode, Activity activity, String commentId, int position, boolean isSelectFirst) {
        Intent intent = new Intent(activity, ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("position", position);
        intent.putExtra("isSelectFirst", isSelectFirst);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(int requestCode, Fragment fragment, String commentId, int position, boolean isSelectFirst) {
        Intent intent = new Intent(fragment.getActivity(), ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("position", position);
        intent.putExtra("isSelectFirst", isSelectFirst);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(int requestCode, Fragment fragment, String commentId, int position) {
        Intent intent = new Intent(fragment.getActivity(), ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("position", position);
        fragment.startActivityForResult(intent, requestCode);
    }

    public static void startForResult(boolean isAutoStart, int requestCode, Fragment fragment, String commentId, int position) {
        Intent intent = new Intent(fragment.getActivity(), ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("position", position);
        intent.putExtra("isAutoStart", isAutoStart);
        fragment.startActivityForResult(intent, requestCode);
    }


    public static void startForResult(int requestCode, Activity activity, int contentHeadType, String commentId, DynamicBean.ListBean.Reply reply) {
        Intent intent = new Intent(activity, ComentsDetailsActivity.class);
        intent.putExtra("commentId", commentId);
        intent.putExtra("replyBean", reply);
        intent.putExtra("contentHeadType", contentHeadType);
        activity.startActivityForResult(intent, requestCode);
    }


    private int contentHeadType;

    public static final int SHARE_TYPE = 2;//转发，评论类型


    @Override
    protected int setViewForContent() {
        return R.layout.activity_coments_details;
    }

    private XRefreshView xfv_coments_detail;
    private ListView lv_coments_detail;
    private MyAdapter myAdapter = new MyAdapter();
    private FrameLayout fl_thumbup_home_detail;
    private FrameLayout fl_price_home_detail;
    private TextView tv_thumbup_comments_detail;

    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;

    private FrameLayout fl_collect_home_detail;
    private TextView tv_collect_comments_detail;


    private LinearLayout ll_content_type_bottom;
    private TextView tv_comments_content_type;
    private RelativeLayout rl_com_count_content_type;
    private TextView tv_com_count;
    private ImageView iv_collect_content_type;

    private LinearLayout ll_normal_type_bottom;


    private TextView tv_comments_comments_detail;


    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);


        tv_comments_comments_detail = findViewById(R.id.tv_comments_comments_detail);

        ll_normal_type_bottom = findViewById(R.id.ll_normal_type_bottom);

        ll_content_type_bottom = findViewById(R.id.ll_content_type_bottom);
        tv_comments_content_type = findViewById(R.id.tv_comments_content_type);
        rl_com_count_content_type = findViewById(R.id.rl_com_count_content_type);
        rl_com_count_content_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lv_coments_detail == null) return;
                lv_coments_detail.setSelection(1);
            }
        });


        tv_com_count = findViewById(R.id.tv_com_count);
        iv_collect_content_type = findViewById(R.id.iv_collect_content_type);
        tv_comments_content_type.setOnClickListener(this);
        iv_collect_content_type.setOnClickListener(this);


        fl_collect_home_detail = findViewById(R.id.fl_collect_home_detail);
        tv_collect_comments_detail = findViewById(R.id.tv_collect_comments_detail);
        fl_collect_home_detail.setOnClickListener(this);


        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_title.setText("球咖");
        tv_ritht_title.setVisibility(View.INVISIBLE);
        iv_right_title.setVisibility(View.VISIBLE);
        iv_right_title.setImageDrawable(getResources().getDrawable(R.drawable.dot_more));
        iv_right_title.setOnClickListener(this);

        fl_thumbup_home_detail = findViewById(R.id.fl_thumbup_home_detail);
        fl_price_home_detail = findViewById(R.id.fl_price_home_detail);
        tv_thumbup_comments_detail = findViewById(R.id.tv_thumbup_comments_detail);
        fl_thumbup_home_detail.setOnClickListener(this);
        fl_price_home_detail.setOnClickListener(this);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        xfv_coments_detail = findViewById(R.id.xfv_coments_detail);
        xfv_coments_detail.setPullRefreshEnable(true);
        xfv_coments_detail.setXRefreshViewListener(new MyXListener());
        lv_coments_detail = findViewById(R.id.lv_coments_detail);

        addHeader();
        lv_coments_detail.setAdapter(myAdapter);
        lv_coments_detail.setOnItemClickListener(this);
        tv_follow_personal.setOnClickListener(this);


    }

    private String commentId;
    private boolean isSelectFirst;
    private DynamicBean.ListBean.Reply replyBean;

    @Override
    protected void initDatas() {
        super.initDatas();
        commentId = getIntent().getStringExtra("commentId");
        isSelectFirst = getIntent().getBooleanExtra("isSelectFirst", false);
        contentHeadType = getIntent().getIntExtra("contentHeadType", 0);
        if (contentHeadType == SHARE_TYPE) {
            try {
                replyBean = (DynamicBean.ListBean.Reply) getIntent().getSerializableExtra("replyBean");
            } catch (Exception e) {

            }
        }

        if (replyBean != null) {
            ll_comment_type.setVisibility(View.VISIBLE);
        }


        initData();
    }

    private void initData() {
        page = 0;
        requestNetDatas();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isPause) {
            player_video.startButton.performClick();
        }
    }

    /**
     * 分享成功
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action", "share");
        //1 2 3
        params.put("type", 3);
        params.put("id", commentId);
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
        ToastUtils.toastNoStates("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.toastNoStates("分享取消");
    }


    @Override
    public void finish() {
        Intent intent = new Intent();
        if (comentsDetailBean != null && comentsDetailBean.data != null) {
            String praise_count = comentsDetailBean.data.comment.praise_count;
            int praise_status = comentsDetailBean.data.comment.praise_status;
            intent.putExtra("position", getIntent().getIntExtra("position", -1));
            intent.putExtra("praise_count", praise_count);
            intent.putExtra("praise_status", praise_status);
            String follow_status = comentsDetailBean.data.comment.user.follow_status;
            String collection_status = comentsDetailBean.data.comment.collection_status;
            intent.putExtra("follow_status", follow_status);
            intent.putExtra("collection_status", collection_status + "");
            if (TextUtils.isEmpty(comentsDetailBean.data.comment.reply_count)) {
                comentsDetailBean.data.comment.reply_count = "0";
            }
            String reply_count = comentsDetailBean.data.comment.reply_count;
            intent.putExtra("reply_count", reply_count);
            Object tag = iv_delect_header.getTag();
            if (tag != null) {
                intent.putExtra("delete", true);
            } else {
                intent.putExtra("delete", false);
            }


            intent.putExtra("collection_status", comentsDetailBean.data.comment.collection_status);
            setResult(2, intent);
        }
        super.finish();
    }

    class MyXListener extends XRefreshView.SimpleXRefreshListener {

        @Override
        public void onRefresh(boolean isPullDown) {
            page = 0;
            clickPosition = -1;
            rl_load_layout = null;
            requestNetDatas();
        }
    }

    private int page;
    private ComentsDetailBean comentsDetailBean;

    private Handler netErrorHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (rl_load_layout != null && ll_net_error != null) {
                rl_load_layout.setVisibility(View.GONE);
                RequestNetWorkUtils.kjHttp.cancelAll();
                ll_net_error.setVisibility(View.VISIBLE);
            }
        }
    };

    private synchronized void requestNetDatas() {
        //http://app.mayisports.com/php/api.php?action=topic&type=get_reply_list&id=39180&start=0
        final String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action", "topic");
        params.put("type", "get_reply_list");
        params.put("id", commentId + "");
        params.put("start", page);
        ll_net_error.setVisibility(View.GONE);
        if (page == 0) {
            setTitleShow(true);
        }


        if (replyBean != null) {
            params.put("reply_id", replyBean.id);
        }


        netErrorHandler.removeCallbacksAndMessages(null);
        netErrorHandler.sendEmptyMessageDelayed(0, 1000 * 10);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout, this) {
            @Override
            public void onSucces(String string) {

                if (page == 0) {
                    comentsDetailBean = JsonParseUtils.parseJsonClass(string, ComentsDetailBean.class);

                    if (comentsDetailBean != null && comentsDetailBean.data != null) {
                        if (rl_load_layout != null) rl_load_layout.setVisibility(View.VISIBLE);

                        pre_page = 1;
                        lv_coments_detail.setAdapter(myAdapter);
                        updateHeader();
                        if (isSelectFirst) {
                            lv_coments_detail.setSelection(1);
                            isSelectFirst = false;
                            if (rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                        }
                    } else {
                        ToastUtils.toast(Constant.NET_FAIL_TOAST);
                        if (rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                    }
                } else {
                    if (rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
                    ComentsDetailBean newBean = JsonParseUtils.parseJsonClass(string, ComentsDetailBean.class);
                    try {
                        if (newBean.data.list.size() > 0) {
                            comentsDetailBean.data.list.addAll(newBean.data.list);
                        } else {
                            pre_page = 0;
                        }
                    } catch (Exception e) {
                        pre_page = 0;
                    }
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                if (page == 0) {
                    ll_net_error.setVisibility(View.VISIBLE);
                } else {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();

                netErrorHandler.removeCallbacksAndMessages(null);


                rl_load_layout = findViewById(R.id.rl_load_layout);
                if (xfv_coments_detail != null) {
                    xfv_coments_detail.stopRefresh();
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private boolean isPause;

    @Override
    protected void onPause() {
        super.onPause();
        if (player_video.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING) {
            isPause = true;
            player_video.startButton.performClick();
        } else if (player_video.currentState == JCVideoPlayer.CURRENT_STATE_PLAYING_BUFFERING_START || player_video.currentState == JCVideoPlayer.CURRENT_STATE_PREPARING) {
            player_video.release();
        }


    }


    private void setCollect(boolean isCollect) {
        if (isCollect) {
//            tv_collect_comments_detail.setText("取消收藏");
            Drawable drawable = getResources().getDrawable(R.drawable.article_collection_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            //       tv_collect_comments_detail.setCompoundDrawables(drawable,null,null,null);
            iv_collect_content_type.setImageDrawable(drawable);
        } else {
//            tv_collect_comments_detail.setText("收藏");
            Drawable drawable = getResources().getDrawable(R.drawable.article_collection_icon_uncheck);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//            tv_collect_comments_detail.setCompoundDrawables(drawable,null,null,null);
            iv_collect_content_type.setImageDrawable(drawable);

        }
    }


    private boolean isDelete;
    private boolean setUp;
    private ComentsDetailBean.DataBean.CommentBean comment;
    private boolean isLoadUrl;

    private void updateHeader() {


        if (comentsDetailBean.data.comment == null) {
            if (rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
            return;
        }
        comment = comentsDetailBean.data.comment;
        Log.e("网络数据头部", "updateHeader");


        /**
         * 更新头部
         */

        PictureUtils.showCircle(comment.user.headurl, iv_head_dynamic_item);

        if (!TextUtils.isEmpty(comment.user.verify_type) && Integer.valueOf(comment.user.verify_type) > 0) {
            iv_vip_header.setVisibility(View.VISIBLE);
        } else {
            iv_vip_header.setVisibility(View.GONE);
        }

        tv_name_item.setText(comment.user.nickname);
        tv_name_item_1.setText(comment.user.nickname);
        if (!TextUtils.isEmpty(comment.create_time)) {
            Long aLong = Long.valueOf(comment.create_time + "000");
            String createTime = Utils.getCreateTime(aLong);
            tv_create_time_topic_item.setText(createTime);
            tv_create_time_topic_item_1.setText(createTime);
        } else {
            tv_create_time_topic_item.setText("");
            tv_create_time_topic_item_1.setText("");
        }

        if ("1".equals(comment.user.follow_status)) {
            tv_follow_personal.setTag(1);
            tv_follow_personal.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
            tv_follow_personal.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
            tv_follow_personal.setText("已关注");
            tv_follow_personal.setVisibility(View.GONE);
        } else {
            tv_follow_personal.setTag(0);
            tv_follow_personal.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.shape_blue_storke_cricle));
            tv_follow_personal.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
            tv_follow_personal.setText(" 关注 ");
            tv_follow_personal.setVisibility(View.VISIBLE);
        }


        /**
         * 底部收藏状态
         */
        if ("1".equals(comentsDetailBean.data.comment.collection_status)) {
            setCollect(true);
        } else {
            setCollect(false);
        }


        if (contentHeadType == SHARE_TYPE) {

        } else {

            /**
             * 视频播放
             */
            if (comment.imgListEx != null && comment.imgListEx.size() > 0) {
                DynamicBean.ListBean.ImageList imageList = comment.imgListEx.get(0);
                if ("video".equals(imageList.type)) {
                    setTitleShow(false);
                    xfv_coments_detail.setPullRefreshEnable(false);
                    player_video.setVisibility(View.VISIBLE);
                    iv_back.setVisibility(View.VISIBLE);
                    iv_dot_more.setVisibility(View.VISIBLE);
                    gv_publish_point.setVisibility(View.GONE);
                    tv_title_coments_detail.setVisibility(View.GONE);
                    String videoUrl = imageList.url;
                    if (!setUp) {


                        /**
                         * 打开硬件加速
                         */

//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);


                        setUp = player_video.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");

                        if (setUp) {
                            String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                            PictureUtils.show(imgUrl, player_video.thumbImageView);
                        }
                        //模拟用户点击开始按钮，NORMAL状态下点击开始播放视频，播放中点击暂停视频
                        boolean isAutoStart = getIntent().getBooleanExtra("isAutoStart", false);
                        if (isAutoStart) {
                            player_video.startButton.performClick();
                        }
                    } else {
//                    player_video.startButton.performClick();
                        if (isPause && player_video.currentState == JCVideoPlayer.CURRENT_STATE_PAUSE) {
                            isPause = false;
                            player_video.startButton.performClick();
                        }
                    }

                } else {
                    setTitleShow(true);
                    xfv_coments_detail.setPullRefreshEnable(true);
                    player_video.setVisibility(View.GONE);
                    iv_back.setVisibility(View.GONE);
                    iv_dot_more.setVisibility(View.GONE);
                    gv_publish_point.setVisibility(View.VISIBLE);
                    tv_title_coments_detail.setVisibility(View.VISIBLE);
                    gv_publish_point.setAdapter(myGVAdatper);
                }


            } else {
                setTitleShow(true);
                xfv_coments_detail.setPullRefreshEnable(true);
                player_video.setVisibility(View.GONE);
                iv_back.setVisibility(View.GONE);
                iv_dot_more.setVisibility(View.GONE);
                gv_publish_point.setVisibility(View.VISIBLE);
                gv_publish_point.setAdapter(myGVAdatper);
                tv_title_coments_detail.setVisibility(View.VISIBLE);
            }

            /**
             * 顶部图片
             */
            if (TextUtils.isEmpty(comment.background_img)) {
                iv_top_img_comments_header.setVisibility(View.GONE);
            } else {
                iv_top_img_comments_header.setVisibility(View.VISIBLE);
                PictureUtils.show(comment.background_img, iv_top_img_comments_header);
            }


            if (TextUtils.isEmpty(comment.topic_title)) {
                tv_title_coments_detail.setVisibility(View.GONE);
            } else {
                tv_title_coments_detail.setVisibility(View.VISIBLE);
                tv_title_coments_detail.setText(comment.topic_title);
            }


//        tv_content_detail.setText(comment.comment);


            /**
             * 显示类型判断
             */
            if (TextUtils.isEmpty(comment.title)) {//显示普通动态
                tv_title_header.setVisibility(View.GONE);
                ll_content_type_bottom.setVisibility(View.GONE);
                ll_normal_type_bottom.setVisibility(View.VISIBLE);
                ll_coment_type_user_name.setVisibility(View.GONE);
                ll_top_user_header.setVisibility(View.VISIBLE);
                ll_content_type_share.setVisibility(View.GONE);
            } else {//显示文章
                tv_title_header.setVisibility(View.VISIBLE);
                tv_title_header.setText(comment.title);
                ll_content_type_bottom.setVisibility(View.VISIBLE);
                ll_normal_type_bottom.setVisibility(View.GONE);
                ll_coment_type_user_name.setVisibility(View.VISIBLE);
                ll_top_user_header.setVisibility(View.GONE);
                ll_content_type_share.setVisibility(View.VISIBLE);

            }

            tv_content_detail.setVisibility(View.GONE);


            String imgPath = "file:///android_asset/";

            if (TextUtils.isEmpty(comment.title)) {
                tv_content_detail.setVisibility(View.VISIBLE);

                tv_content_detail.setText(comment.comment);
                wv_content_comments_detail.setVisibility(View.GONE);
                if (rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);
            } else {
                comment.comment = comment.comment.replaceAll("\\n", "<br/>");
                comment.comment = comment.comment.replaceAll("<img ", "<img onerror='this.src=\"file:///android_asset/pic_dauflt_img.png\"' ");


                /**
                 * 处理样式  <div class=\"description topic-description\">
                 */

                String css = ".topic-description .git-wrap {" +
                        "  position: relative;" +
                        "}" +
                        ".topic-description .git-wrap:after {" +
                        "  content: '';" +
                        "  position: absolute;" +
                        "  top: 0;" +
                        "  left: 0;" +
                        "  bottom: 0;" +
                        "  right: 0;" +
                        "  width: 106px;" +
                        "  height: 39px;" +
                        "  margin: auto;" +
                        "  background: no-repeat center center;" +
                        "  background-size: 106px 39px;" +
                        "}" +
                        ".topic-description .git-wrap.status0:after {" +
                        "  background-image: url('" + imgPath + "gifStatus0.png');" +
                        "}" +
                        ".topic-description .git-wrap.status1:after {" +
                        "  background-image: url('" + imgPath + "gifStatus1.gif');" +
                        "}.topic-description tr, .topic-description td {" +
                        "  border: none;" +
                        "}";

                String js = "";
                String jsNew = "<script type=\"text/javascript\" src=\"file:///android_asset/comment.js\" /></script>";
                String jsNewWifi = "<script type=\"text/javascript\" src=\"file:///android_asset/comment_wifi.js\" /></script>";

                if (NetWorkUtil.isWifi(QCaApplication.getContext())) {
                    js = jsNewWifi;
                } else {
                    js = jsNew;
                }


                String jsImg = "<script type=\"text/javascript\" src=\"file:///android_asset/img_native.js\" /></script>";

                String cssUrl = "<style>" + css + "br {line-height: 2px;}div, p, span, ul, li, img {-webkit-touch-callout: none;-webkit-user-select: none; -khtml-user-select: none;-moz-user-select: none;-ms-user-select: none;user-select: none;-webkit-overflow-scrolling: touch;}.description {width: 100%;overflow: hidden;}.description {color: #282828;}.description, .description * {font-size: 16px; line-height: 19px;letter-spacing: 1px!important;margin-top: 5px!important;margin-bottom: 5px!important;}.description table {width: 100%%!important;}.description table tr:first-child {width: 100%%!important;}.description table tr:nth-child(2) td {width: 50%%!important;}.topic-description, .topic-description * {font-size: 17px;line-height: 1.41;letter-spacing: 0.34px;}.topic-description p{line-height: 1.41;margin: 25px 0!important;font-weight: 400; }.topic-description img{display: block;visibility: visible!important;width:100%!important;height: auto!important;margin: 25px 0!important; background: transparent!important; }</style><div class=\"description topic-description\">" + comment.comment + "</div>" + js + jsImg;

                String local = "file:///android_asset";
                wv_content_comments_detail.loadDataWithBaseURL(local, cssUrl, "text/html", "utf-8", null);


            }


            //selectionList
            if (comment.selectionList != null) {
                ll_select_container.setVisibility(View.VISIBLE);
                ll_select_container.removeAllViews();
                for (int i = 0; i < comment.selectionList.size(); i++) {
                    ComentsDetailBean.DataBean.CommentBean.SelectionList selectionList = comment.selectionList.get(i);
                    View view = View.inflate(QCaApplication.getContext(), R.layout.item_select_comments, null);
                    TextView tv_title = view.findViewById(R.id.tv_title);
                    TextView tv_value = view.findViewById(R.id.tv_value);
                    tv_title.setText(selectionList.title);
                    tv_value.setText(selectionList.value);
                    ll_select_container.addView(view);
                }
            } else {
                ll_select_container.setVisibility(View.GONE);
            }


            ll_thumb_header.setVisibility(View.GONE);
            tv_toast_thumbup_header.setVisibility(View.GONE);


            if (TextUtils.isEmpty(comment.view_count)) {
                tv_view_count_topic_item.setText("阅读\t0");
            } else {
                Integer integer = Integer.valueOf(comment.view_count);
                String countW = Utils.parseIntToK(integer);
                tv_view_count_topic_item.setText("阅读\t" + countW);
            }

            tv_view_count_topic_item.setVisibility(View.GONE);

            if (comentsDetailBean != null && comentsDetailBean.data != null && comentsDetailBean.data.list != null && comentsDetailBean.data.list.size() > 0) {

            } else {
                tv_count_header.setVisibility(View.GONE);
                v_line.setVisibility(View.GONE);
            }


            tv_count_header.setVisibility(View.GONE);


            if (TextUtils.isEmpty(comment.reply_count) || "0".equals(comment.reply_count)) {
                tv_count_header.setText("全部评论");
                tv_com_count.setVisibility(View.GONE);
//            tv_no_data.setVisibility(View.VISIBLE);
//            ll_thumb_header.setVisibility(View.GONE);
            } else {
                tv_count_header.setText("全部评论 " + comment.reply_count + "");
//            tv_no_data.setVisibility(View.GONE);
//            ll_thumb_header.setVisibility(View.VISIBLE);
                tv_com_count.setVisibility(View.VISIBLE);
                if (Integer.valueOf(comment.reply_count) > 999) {
                    tv_com_count.setText("999+");
                } else {
                    tv_com_count.setText(comment.reply_count);
                }
            }

            if (comment.praise_status == 1) {//点赞
                Drawable drawable = getResources().getDrawable(R.drawable.thumb_checked_small);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_thumbup_comments_detail.setCompoundDrawables(drawable, null, null, null);
                tv_thumbup_comments_detail_2.setCompoundDrawables(drawable, null, null, null);
//            tv_toast_thumbup_header.setVisibility(View.GONE);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.thumb_small);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                tv_thumbup_comments_detail.setCompoundDrawables(drawable, null, null, null);
                tv_thumbup_comments_detail_2.setCompoundDrawables(drawable, null, null, null);
//            tv_toast_thumbup_header.setVisibility(View.VISIBLE);
            }

            DataBindUtils.setBottomPraise(tv_thumbup_comments_detail, comment.praise_count);
            DataBindUtils.setBottomPraise(tv_thumbup_comments_detail_2, comment.praise_count);
//        tv_thumbup_comments_detail.setText("点赞·"+comment.praise_count+"");


            DataBindUtils.setComentCount(tv_comments_comments_detail, comment.reply_count);

            if (SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(comment.user.user_id)) {
                tv_follow_personal.setVisibility(View.GONE);
//            iv_delect_header.setVisibility(View.VISIBLE);
                iv_delect_header.setVisibility(View.GONE);
                isDelete = true;
            } else if (!"1".equals(comment.user.follow_status)) {
                tv_follow_personal.setVisibility(View.VISIBLE);
                iv_delect_header.setVisibility(View.GONE);
                isDelete = false;
            } else {
                tv_follow_personal.setVisibility(View.GONE);
                iv_delect_header.setVisibility(View.GONE);
                isDelete = false;
            }


        }

    }

    /**
     * 处理点赞
     *
     * @param bean
     */
    private void requestThumbupComents(final ComentsDetailBean.DataBean.CommentBean bean) {

        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action", "topic");
        params.put("type", "praise");
        params.put("id", bean.comment_id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null, this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string, PraiseBean.class);
                if (praiseBean.data != null) {
                    bean.praise_count = praiseBean.data.praise_count;
                    bean.praise_status = praiseBean.data.praise_status;

                    /**
                     * 更新点赞人数
                     */
                    if (comentsDetailBean.data.praise_list == null) {
                        comentsDetailBean.data.praise_list = new ArrayList<>();
                    }

                    if (bean.praise_status == 1) {//点赞
                        Drawable drawable = getResources().getDrawable(R.drawable.thumb_checked_small);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_thumbup_comments_detail.setCompoundDrawables(drawable, null, null, null);
                        tv_thumbup_comments_detail_2.setCompoundDrawables(drawable, null, null, null);
                        delPraisePersonalData(comentsDetailBean.data.praise_list, 1);
//                        tv_toast_thumbup_header.setVisibility(View.GONE);

                    } else {
                        Drawable drawable = getResources().getDrawable(R.drawable.thumb_small);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        tv_thumbup_comments_detail.setCompoundDrawables(drawable, null, null, null);
                        tv_thumbup_comments_detail_2.setCompoundDrawables(drawable, null, null, null);
                        delPraisePersonalData(comentsDetailBean.data.praise_list, 0);
//                        tv_toast_thumbup_header.setVisibility(View.VISIBLE);
                    }
                    DataBindUtils.setBottomPraise(tv_thumbup_comments_detail, bean.praise_count);
                    DataBindUtils.setBottomPraise(tv_thumbup_comments_detail_2, bean.praise_count);
//                    tv_thumbup_comments_detail.setText("点赞·"+bean.praise_count+"");
//                    updateHeader();


                    if (comentsDetailBean.data.praise_list != null && comentsDetailBean.data.praise_list.size() > 0) {
                        ll_thumb_header.setVisibility(View.GONE);
                        // rv_thumb_header.setAdapter(new MyRvAdapter());
                        tv_toast_thumbup_header.setVisibility(View.GONE);
                    } else {
                        ll_thumb_header.setVisibility(View.GONE);
                        tv_toast_thumbup_header.setVisibility(View.VISIBLE);
                    }

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

    private void delPraisePersonalData(List<ComentsDetailBean.DataBean.PraiseListBean> praise_list, int i) {
        if (i == 1) {//点赞

            delPraisePersonalData(praise_list, 0);


            ComentsDetailBean.DataBean.PraiseListBean praiseListBean = new ComentsDetailBean.DataBean.PraiseListBean();
            praiseListBean.user_id = SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID);
            praiseListBean.headurl = SPUtils.getString(QCaApplication.getContext(), Constant.HEADER_URl);
            praise_list.add(0, praiseListBean);

        } else {//取消
            int j = 0;
            for (j = 0; j < praise_list.size(); j++) {
                String user_id = praise_list.get(j).user_id;
                if (user_id.equals(SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID))) {
                    break;
                }
            }

            if (j != praise_list.size()) {
                praise_list.remove(j);
            }

        }
    }

    private RelativeLayout rl_load_layout;
    private ImageView iv_head_dynamic_item;
    private ImageView iv_vip_header;
    private TextView tv_name_item;
    private TextView tv_create_time_topic_item;
    private TextView tv_follow_personal;
    private TextView tv_content_detail;
    private GridView gv_publish_point;
    private MyGVAdatper myGVAdatper = new MyGVAdatper();
    private LinearLayout ll_thumb_header;
    private RecyclerView rv_thumb_header;
    private TextView tv_title_coments_detail;
    private TextView tv_count_header;
    private LinearLayout ll_select_container;
    private TextView tv_no_data;
    private ImageView iv_delect_header;
    private WebView wv_content_comments_detail;

    private JCVideoPlayerStandard player_video;
    private TextView tv_view_count_topic_item;
    private ImageView iv_top_img_comments_header;

    private RelativeLayout rl_top_header;

    private TextView tv_title_header;
    private TextView tv_toast_thumbup_header;
    private ImageView iv_back;
    private ImageView iv_dot_more;


    private RelativeLayout rl_video_container;

    private LinearLayout ll_coment_type_user_name;
    private TextView tv_name_item_1;
    private TextView tv_create_time_topic_item_1;


    private LinearLayout ll_top_user_header;
    private LinearLayout ll_content_type_share;

    private ViewGroup view;

    private TextView tv_thumbup_comments_detail_2;

    private ImageView iv_weixin_share;
    private ImageView iv_pengyouquan_share;
    private ImageView iv_weibo_share;


    private View v_line;


    private LinearLayout ll_comment_type;

    private void addHeader() {
        view = (ViewGroup) View.inflate(this, R.layout.coments_detail_header, null);
        tv_title_header = view.findViewById(R.id.tv_title_header);


        v_line = view.findViewById(R.id.v_line);

        iv_weixin_share = view.findViewById(R.id.iv_weixin_share);
        iv_weixin_share.setOnClickListener(this);
        iv_pengyouquan_share = view.findViewById(R.id.iv_pengyouquan_share);
        iv_pengyouquan_share.setOnClickListener(this);
        iv_weibo_share = view.findViewById(R.id.iv_weibo_share);
        iv_weibo_share.setOnClickListener(this);


        ll_comment_type = view.findViewById(R.id.ll_comment_type);//转发评论


        ll_top_user_header = view.findViewById(R.id.ll_top_user_header);

        ll_content_type_share = view.findViewById(R.id.ll_content_type_share);
        tv_thumbup_comments_detail_2 = view.findViewById(R.id.tv_thumbup_comments_detail_2);
        tv_thumbup_comments_detail_2.setOnClickListener(this);

        ll_coment_type_user_name = view.findViewById(R.id.ll_coment_type_user_name);
        ll_coment_type_user_name.setOnClickListener(this);
        tv_name_item_1 = view.findViewById(R.id.tv_name_item_1);
        tv_create_time_topic_item_1 = view.findViewById(R.id.tv_create_time_topic_item_1);


        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_dot_more = findViewById(R.id.iv_dot_more);
        iv_dot_more.setOnClickListener(this);
        tv_toast_thumbup_header = view.findViewById(R.id.tv_toast_thumbup_header);
        player_video = findViewById(R.id.player_video);


        int heightVideo = (int) (DisplayUtil.getScreenWidth(this) / 16.0 * 9);
        player_video.getLayoutParams().height = heightVideo;

        iv_top_img_comments_header = view.findViewById(R.id.iv_top_img_comments_header);
        int height = (int) (DisplayUtil.getScreenWidth(this) / 100.0 * 32);
        iv_top_img_comments_header.getLayoutParams().height = height;

        tv_view_count_topic_item = view.findViewById(R.id.tv_view_count_topic_item);
        tv_title_coments_detail = view.findViewById(R.id.tv_title_coments_detail);
        ll_select_container = view.findViewById(R.id.ll_select_container);
        tv_title_coments_detail.setOnClickListener(this);
        rl_top_header = view.findViewById(R.id.rl_top);
        rl_top_header.setOnClickListener(this);
        iv_head_dynamic_item = view.findViewById(R.id.iv_head_dynamic_item);
        iv_vip_header = view.findViewById(R.id.iv_vip_header);
        tv_name_item = view.findViewById(R.id.tv_name_item);
        tv_create_time_topic_item = view.findViewById(R.id.tv_create_time_topic_item);
        tv_follow_personal = view.findViewById(R.id.tv_follow_personal);
        tv_content_detail = view.findViewById(R.id.tv_content_detail);
        gv_publish_point = view.findViewById(R.id.gv_publish_point);
        ll_thumb_header = view.findViewById(R.id.ll_thumb_header);
        tv_count_header = view.findViewById(R.id.tv_count_header);
        rv_thumb_header = view.findViewById(R.id.rv_thumb_header);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        iv_delect_header = view.findViewById(R.id.iv_delect_header);

        wv_content_comments_detail = view.findViewById(R.id.wv_content_comments_detail);


//        wv_content_comments_detail.setClickable(false);
//        wv_content_comments_detail.setEnabled(false);
        wv_content_comments_detail.setLayerType(View.LAYER_TYPE_NONE, null);

        wv_content_comments_detail.setWebChromeClient(new WebChromeClient());
        WebSettings settings = wv_content_comments_detail.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadsImagesAutomatically(true);

        settings.setBlockNetworkImage(false);
        wv_content_comments_detail.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(final WebView view, String url) {
                //   view.loadUrl("javascript:window.qk.resize(document.body.getBoundingClientRect().height)");
                super.onPageFinished(view, url);

                //description
                view.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.loadUrl("javascript:window.qk.resize(document.getElementsByClassName('description')[0].offsetHeight)");
                        //    if(rl_load_layout != null)rl_load_layout.setVisibility(View.GONE);
                    }
                }, 300);
                //   if(rl_load_layout != null)rl_load_layout.setVisibility(View.GONE);
//                view.measure(0, 0);
//                final int measuredHeight = view.getMeasuredHeight();
//
//                final int contentHeight = view.getContentHeight();
//
//                Log.i("zzz", "measuredHeight=" + measuredHeight+"----"+contentHeight);
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        wv_content_comments_detail.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, measuredHeight));
//                    }
//                });

            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);


            }


        });

        wv_content_comments_detail.addJavascriptInterface(new JavascriptHandler(), "qk");
        iv_delect_header.setOnClickListener(this);
        rv_thumb_header.setHasFixedSize(true);
        rv_thumb_header.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        gv_publish_point.setAdapter(myGVAdatper);
        gv_publish_point.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = "";
                for (int i = 0; i < comentsDetailBean.data.comment.imgListEx.size(); i++) {
                    String url = comentsDetailBean.data.comment.imgListEx.get(i).url;
                    String delUrl = Utils.delUrl(url);
                    str += delUrl + ",";
                }
                str = str.substring(0, str.length() - 1);
                PicShowActivity.start(ComentsDetailsActivity.this, str, position);
            }
        });
        lv_coments_detail.addHeaderView(view);
    }

    @Override
    public void onReload() {

    }

    private class JavascriptHandler {
        @JavascriptInterface
        public void resize(final float height) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams layoutParams = wv_content_comments_detail.getLayoutParams();
                    layoutParams.height = (int) (height * getResources().getDisplayMetrics().density) + 10;
//                    if(layoutParams.height > 5000){
//                        layoutParams.height = 20;
//                        ToastUtils.toastNoStates("加载失败");
//                    }
                    // wv_content_comments_detail.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)+10));
                    wv_content_comments_detail.setLayoutParams(layoutParams);
                    if (rl_load_layout != null) rl_load_layout.setVisibility(View.GONE);

                }
            });
        }


        @JavascriptInterface
        public void nativePicture(String jsonArray, String index) {
//            ToastUtils.toast(jsonArray+index);
//            Log.e("urls",jsonArray);

            PicShowActivity.start(ComentsDetailsActivity.this, jsonArray, Integer.valueOf(index));
        }
    }

    class MyRvAdapter extends RecyclerView.Adapter<MyRvAdapter.MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(QCaApplication.getContext(), R.layout.img_rv_layout, null);

            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ComentsDetailBean.DataBean.PraiseListBean bean = comentsDetailBean.data.praise_list.get(position);

            PictureUtils.showCircle(bean.headurl, holder.iv);
            holder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(bean);
                    PersonalDetailActivity.start(ComentsDetailsActivity.this, bean.user_id, json);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (comentsDetailBean != null && comentsDetailBean.data != null && comentsDetailBean.data.praise_list != null) {
                return comentsDetailBean.data.praise_list.size();
            }
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            ImageView iv;

            public MyViewHolder(View view) {
                super(view);
                iv = view.findViewById(R.id.iv);
            }
        }
    }

    class MyGVAdatper extends BaseAdapter {

        @Override
        public int getCount() {
            if (comment != null && comment.imglist != null) {
                List<DynamicBean.ListBean.ImageList> imgListEx = comment.imgListEx;
                return imgListEx.size();
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

            DynamicBean.ListBean.ImageList imageList = comment.imgListEx.get(position);
            View view = View.inflate(QCaApplication.getContext(), R.layout.img_gif_layout, null);
            ImageView imageView = view.findViewById(R.id.iv);
            TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
            Utils.showType(imageList.url, imageView, tv_gif_icon, imageList.size);

            if (getCount() == 1) {
                Utils.scaleImg(ComentsDetailsActivity.this, view, imageView, imageList.size);
            }


            return view;
        }
    }

    private int clickPosition = -1;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) return;
        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }
        clickPosition = position - 1;
        if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.list == null || position == comentsDetailBean.data.list.size() + 1)
            return;
        ComentsDetailBean.DataBean.ListBean listBean = comentsDetailBean.data.list.get(position - 1);
        toReplay(listBean.reply_id, listBean.nickname);
    }

    private ToastPricePopuWindow toastPricePopuWindow;
    private SelectPicPopupWindow selectPicPopupWindow;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_title_coments_detail:
                if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.comment == null)
                    return;
                TopicDetailActivity.start(this, comentsDetailBean.data.comment.topic_id);
                break;
            case R.id.tv_follow_personal:
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
                    return;
                }

                if (tv_follow_personal.getTag() == null) break;
                int b = (int) tv_follow_personal.getTag();
                if (b == 1) {//取消关注
                    ToastUtils.toast("您已关注该用户");
                    return;
//                    requestFollows("delete");
                } else {//关注
                    requestFollows("add");
                }
                break;
            case R.id.fl_thumbup_home_detail:
            case R.id.tv_thumbup_comments_detail_2:
                if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.comment == null)
                    return;
                ComentsDetailBean.DataBean.CommentBean comment = comentsDetailBean.data.comment;
                requestThumbupComents(comment);
                break;
            case R.id.fl_price_home_detail://评论
            case R.id.tv_comments_content_type:
                if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.comment == null)
                    return;
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
                    return;
                }
                //      ReplayCommentsActivity.start(this, commentId,44);
                PublishPointActivity.start(this, "-1", commentId, false, 44);
                break;
            case R.id.iv_delect_header:
                String str = "确定要删此内容吗?";
                toastPricePopuWindow = new ToastPricePopuWindow(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.tv_go_toast_price) {//左边
                            toastPricePopuWindow.dismiss();
                        } else if (v.getId() == R.id.tv_cancle_toast_price) {//右边
                            delComments(commentId, 1, 0);
                        }


                    }
                }, str, "确定", "考虑一下");
                toastPricePopuWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);


                break;
            case R.id.iv_right_title:
            case R.id.iv_dot_more:


                if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.comment == null) {
                    return;
                }
//
//                showShare();
                //弹窗
                if (true || selectPicPopupWindow == null) {
                    selectPicPopupWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
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
                                        LoginUtils.goLoginActivity(ComentsDetailsActivity.this, HomeNewFragment.ACTION);
                                        return;
                                    }
                                    requestCollect();
                                    break;
                                case R.id.btn_pick_photo://举报
                                    selectPicPopupWindow.dismiss();
                                    if (isDelete) {
                                        ComentsDetailsActivity.this.onClick(iv_delect_header);
                                    } else {
                                        ReportActivity.start(ComentsDetailsActivity.this, comentsDetailBean.data.comment.user.user_id, comentsDetailBean.data.comment.user.nickname, comentsDetailBean.data.comment.create_time);
                                    }
                                    break;
                            }
                        }
                    }, new SharePopuwind.ShareTypeClickListener() {
                        @Override
                        public void onTypeClick(int type) {
                            ComentsDetailBean.DataBean.CommentBean comment = comentsDetailBean.data.comment;
                            if (TextUtils.isEmpty(comentsDetailBean.data.comment.topic_id) || "0".equals(comment.topic_id)) {

//                                ShareUtils.shareMsgForPointNoTopic(type,comment.comment_id,comment.comment,comment.user.nickname,ComentsDetailsActivity.this);
                                String videoImag = "";
                                if (player_video.isShown()) {
                                    videoImag = comment.imgListEx.get(0).url;
                                    ShareUtils.shareMsgForPointNoTopic(type, comment.comment_id, comment.comment, comment.user.nickname, videoImag, ComentsDetailsActivity.this);
                                } else {


                                    List<String> urls = new ArrayList<>();


                                    int typeOfBean;
                                    if (TextUtils.isEmpty(comment.title)) {
                                        typeOfBean = 1;
                                        List<DynamicBean.ListBean.ImageList> imgListEx = comment.imgListEx;
                                        if (imgListEx != null) {
                                            for (int i = 0; i < imgListEx.size(); i++) {
                                                urls.add(imgListEx.get(i).url);
                                            }
                                        }
                                    } else {
                                        typeOfBean = 2;
                                        if (!TextUtils.isEmpty(comment.background_img)) {
                                            urls.add(comment.background_img);
                                        } else if (comment.backgroundList != null) {
                                            List<DynamicBean.ListBean.ImageList> backgroundList = comment.backgroundList;
                                            for (int i = 0; i < backgroundList.size(); i++) {
                                                urls.add(backgroundList.get(i).url);
                                            }
                                        }

                                    }
                                    ShareUtils.shareMsgForPointNoTopic(type, comment.comment_id, comment.comment, comment.user.nickname, urls, typeOfBean, comment.title, ComentsDetailsActivity.this);

                                }


                            } else {
                                ShareUtils.shareMsgForPointHasTopic(type, comment.comment_id, comment.topic_title, comment.user.nickname, ComentsDetailsActivity.this);
                            }
                        }
                    });
                }

                selectPicPopupWindow.btn_share_photo.setVisibility(View.GONE);
                ComentsDetailBean.DataBean.CommentBean comment1 = comentsDetailBean.data.comment;

                if (TextUtils.isEmpty(comment1.title)) {
                    selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                    selectPicPopupWindow.tv_share_title.setVisibility(View.VISIBLE);


                } else {
                    selectPicPopupWindow.rg_share_fg.setVisibility(View.VISIBLE);
                    selectPicPopupWindow.tv_share_title.setVisibility(View.VISIBLE);
                    selectPicPopupWindow.btn_take_photo.setVisibility(View.VISIBLE);

                }


                //      selectPicPopupWindow.btn_take_photo.setVisibility(View.GONE);

                if (isDelete) {//删除键
                    selectPicPopupWindow.btn_pick_photo.setText("删除");
                } else {//举报键
                    selectPicPopupWindow.btn_pick_photo.setText("举报");
                }

                if ("1".equals(comentsDetailBean.data.comment.collection_status)) {
                    selectPicPopupWindow.btn_take_photo.setText("取消收藏");
                } else {
                    selectPicPopupWindow.btn_take_photo.setText("收藏");
                }

                selectPicPopupWindow.showAtLocation(lv_coments_detail, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                break;
            case R.id.rl_top://顶部点击
            case R.id.ll_coment_type_user_name:
                if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.comment == null || comentsDetailBean.data.comment.user == null)
                    return;
                String user_id = comentsDetailBean.data.comment.user.user_id;
                String json = new Gson().toJson(comentsDetailBean.data.comment.user);
                PersonalDetailActivity.start(this, user_id, 0, 1, json);
                break;

            case R.id.fl_collect_home_detail:
                onClick(iv_dot_more);

                break;
            case R.id.iv_collect_content_type:
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(ComentsDetailsActivity.this, HomeNewFragment.ACTION);
                    return;
                }
                requestCollect();
                //   ToastUtils.toast("转发");
                break;
            case R.id.iv_weixin_share: //bo 1 w 4 c5
                setClickOutTime(iv_weixin_share, 4000);
                contentTypeShare(4);
                break;
            case R.id.iv_pengyouquan_share:
                setClickOutTime(iv_pengyouquan_share, 4000);
                contentTypeShare(5);
                break;
            case R.id.iv_weibo_share:
                setClickOutTime(iv_weibo_share, 6000);
                contentTypeShare(1);
                break;

        }
    }

    public void setClickOutTime(final View view, long time) {
        view.setEnabled(false);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, time);
    }


    private void contentTypeShare(int typeShare) {

        ComentsDetailBean.DataBean.CommentBean comment = comentsDetailBean.data.comment;

        List<String> urls = new ArrayList<>();


        int typeOfBean;
        if (TextUtils.isEmpty(comment.title)) {
            typeOfBean = 1;
            List<DynamicBean.ListBean.ImageList> imgListEx = comment.imgListEx;
            if (imgListEx != null) {
                for (int i = 0; i < imgListEx.size(); i++) {
                    urls.add(imgListEx.get(i).url);
                }
            }
        } else {
            typeOfBean = 2;
            if (!TextUtils.isEmpty(comment.background_img)) {
                urls.add(comment.background_img);
            } else if (comment.backgroundList != null) {
                List<DynamicBean.ListBean.ImageList> backgroundList = comment.backgroundList;
                for (int i = 0; i < backgroundList.size(); i++) {
                    urls.add(backgroundList.get(i).url);
                }
            }

        }
        ShareUtils.shareMsgForPointNoTopic(typeShare, comment.comment_id, comment.comment, comment.user.nickname, urls, typeOfBean, comment.title, ComentsDetailsActivity.this);

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
        params.put("id", commentId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string, ReportSubmitBean.class);
                if (reportSubmitBean.status.result == 1) {
                    ToastUtils.toast("收藏成功");
                    comentsDetailBean.data.comment.collection_status = "1";
                    setCollect(true);
                } else if (reportSubmitBean.status.result == 0) {
                    ToastUtils.toast("取消收藏");
                    comentsDetailBean.data.comment.collection_status = "0";
                    setCollect(false);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private void showShare() {
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                ComentsDetailBean.DataBean.CommentBean comment = comentsDetailBean.data.comment;
                if (TextUtils.isEmpty(comentsDetailBean.data.comment.topic_id) || "0".equals(comment.topic_id)) {

//                    ShareUtils.shareMsgForPointNoTopic(type,comment.comment_id,comment.comment,comment.user.nickname,ComentsDetailsActivity.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, comment.comment_id, comment.topic_title, comment.user.nickname, ComentsDetailsActivity.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());


        sharePopuwind.showAtLocation(lv_coments_detail, Gravity.CENTER, 0, 0);
    }

    public void requestFollows(String type) {
        if (comentsDetailBean == null || comentsDetailBean.data == null || comentsDetailBean.data.comment == null)
            return;
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action", "followers");
        params.put("type", type);
        params.put("user_id", comentsDetailBean.data.comment.user.user_id);
        rl_load_layout = null;
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout, this) {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string, FollowBean.class);
                if (followBean != null) {
                    if ("1".equals(followBean.status.result)) {
                        tv_follow_personal.setTag(1);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
                        tv_follow_personal.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_B3B4B9));
                        tv_follow_personal.setText("已关注");
                        comentsDetailBean.data.comment.user.follow_status = "1";
                        NotificationsUtils.checkNotificationAndStartSetting(ComentsDetailsActivity.this, lv_coments_detail);
                    } else {
                        tv_follow_personal.setTag(0);
                        tv_follow_personal.setBackground(getResources().getDrawable(R.drawable.shape_blue_storke_cricle));
                        tv_follow_personal.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
                        tv_follow_personal.setText(" 关注 ");
                        comentsDetailBean.data.comment.user.follow_status = "0";
                    }

                    DynamicFragment.sendFoucsData();
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

    private int pre_page;
    private boolean isNullDatas;

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (comentsDetailBean != null && comentsDetailBean.data != null && comentsDetailBean.data.list != null) {
                int size = comentsDetailBean.data.list.size();
                if (size == 0) {
                    size = 1;
                    isNullDatas = true;
                } else {
                    isNullDatas = false;
                    size++;
                }
                return size;
            }
            tv_count_header.setVisibility(View.VISIBLE);
            v_line.setVisibility(View.VISIBLE);
            isNullDatas = true;
            return 1;
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (isNullDatas) {
                convertView = View.inflate(QCaApplication.getContext(), R.layout.no_topic_item, null);
                convertView.setTag(null);
                return convertView;
            }


            if (position == comentsDetailBean.data.list.size()) {
                convertView = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = convertView.findViewById(R.id.tv_content);
                if (pre_page == 1) {
                    tv_content.setText("加载中...");
                } else {
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                return convertView;
            }


            MyHolder myHolder;
            if (convertView == null || convertView.getTag() == null) {
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(), R.layout.comments_detail_item, null);
                myHolder.iv_head_dynamic_item = convertView.findViewById(R.id.iv_head_dynamic_item);
                myHolder.vip_coin = convertView.findViewById(R.id.iv_vip_header);
                myHolder.iv_red_point_header = convertView.findViewById(R.id.iv_red_point_header);
                myHolder.tv_name_item = convertView.findViewById(R.id.tv_name_item);
                myHolder.tv_create_time_topic_item = convertView.findViewById(R.id.tv_create_time_topic_item);
                myHolder.iv_jinghua_item = convertView.findViewById(R.id.iv_jinghua_item);
                myHolder.tv_content_topic_detail_item = convertView.findViewById(R.id.tv_content_topic_detail_item);
                myHolder.ll_img_container_dynamic_item = convertView.findViewById(R.id.ll_img_container_dynamic_item);
                myHolder.ll_tag_container_item = convertView.findViewById(R.id.ll_tag_container_item);
                myHolder.tv_thumbup_topic_item = convertView.findViewById(R.id.tv_thumbup_topic_item);
                myHolder.tv_comments_topic_item = convertView.findViewById(R.id.tv_comments_topic_item);
                myHolder.ll_replay_layout = convertView.findViewById(R.id.ll_replay_layout);
                myHolder.tv_name = convertView.findViewById(R.id.tv_name);
                myHolder.tv_replay = convertView.findViewById(R.id.tv_replay);
                myHolder.rl_top = convertView.findViewById(R.id.rl_top);
                myHolder.iv_delect = convertView.findViewById(R.id.iv_delect);

                myHolder.ll_tv_count_container = convertView.findViewById(R.id.ll_tv_count_container);

                View view = convertView.findViewById(R.id.ll_bottom_dynamic_item);
                view.setVisibility(View.GONE);

                myHolder.gv_dynamic_item = convertView.findViewById(R.id.gv_dynamic_item);


                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }

            final ComentsDetailBean.DataBean.ListBean bean = comentsDetailBean.data.list.get(position);


            tv_count_header.setVisibility(View.GONE);
            v_line.setVisibility(View.GONE);


            if (position == 0) {
                myHolder.ll_tv_count_container.setVisibility(View.VISIBLE);
                TextView textView = convertView.findViewById(R.id.tv_count_item);
                textView.setText(tv_count_header.getText().toString());

            } else {
                myHolder.ll_tv_count_container.setVisibility(View.GONE);
            }


            myHolder.rl_top.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(bean);
                    PersonalDetailActivity.start(ComentsDetailsActivity.this, bean.user_id, json);
                }
            });

            PictureUtils.showCircle(bean.headurl, myHolder.iv_head_dynamic_item);

            if (!TextUtils.isEmpty(bean.verify_type) && Integer.valueOf(bean.verify_type) > 0) {
                myHolder.vip_coin.setVisibility(View.VISIBLE);
            } else {
                myHolder.vip_coin.setVisibility(View.GONE);
            }

            myHolder.tv_name_item.setText(bean.nickname);
            Long aLong = Long.valueOf(bean.create_time + "000");
            String createTime = Utils.getCreateTime(aLong);
            myHolder.tv_create_time_topic_item.setText(createTime);

            /**
             * 精华未知字段
             */
            myHolder.iv_jinghua_item.setVisibility(View.GONE);


            if (bean.parent != null) {
                myHolder.ll_replay_layout.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(bean.parent.nickname)) {
                    myHolder.tv_name.setText("匿名用户");
                } else {
                    myHolder.tv_name.setText(bean.parent.nickname);
                }

                myHolder.tv_replay.setText(bean.parent.reply);
            } else {
                myHolder.ll_replay_layout.setVisibility(View.GONE);
            }


            bean.reply = bean.reply.replaceAll("<br>", "").replaceAll("<br", "").replaceAll("<b", "");
            myHolder.tv_content_topic_detail_item.setText(bean.reply + "");

//            if(bean.selectionList != null){
//                myHolder.ll_tag_container_item.setVisibility(View.VISIBLE);
//                addTags(myHolder.ll_tag_container_item,bean.selectionList);
//            }else{
            myHolder.ll_tag_container_item.setVisibility(View.GONE);
//            }


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
                        LoginUtils.goLoginActivity(ComentsDetailsActivity.this, HomeNewFragment.ACTION);
                        return;
                    }
//                    String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment_id;
//                    WebViewActivtiy.start(TopicDetailActivity.this,url,"球咖");
//                    clickPosition = position;
//                    toReplay(bean.reply_id,bean.nickname);
                    //  ToastUtils.toast("查看回复");
                    ReplyDetailsActivity.start(ComentsDetailsActivity.this, commentId, bean.reply_id, bean);
                }
            });

            if (bean.praise_status == 1) {//点赞
                Drawable drawable = getResources().getDrawable(R.drawable.comment_praise_icon_checked);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                myHolder.tv_thumbup_topic_item.setCompoundDrawables(null, null, drawable, null);
            } else {
                Drawable drawable = getResources().getDrawable(R.drawable.comment_praise_icon_uncheck);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                myHolder.tv_thumbup_topic_item.setCompoundDrawables(null, null, drawable, null);
            }
            DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item, bean.praise_count, false);
//            myHolder.tv_thumbup_topic_item.setText(bean.praise_count+"");
            //   bean.reply_count = "10";
            if (!TextUtils.isEmpty(bean.reply_count) && Integer.valueOf(bean.reply_count) != 0) {
                myHolder.tv_comments_topic_item.setText("共" + bean.reply_count + "条回复 >");
                myHolder.tv_comments_topic_item.setVisibility(View.VISIBLE);
            } else {
                myHolder.tv_comments_topic_item.setVisibility(View.GONE);
            }


            if (SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID).equals(bean.user_id)) {
                myHolder.iv_delect.setVisibility(View.VISIBLE);
                myHolder.iv_delect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String str = "确定删除当前评论?";
                        toastPricePopuWindow = new ToastPricePopuWindow(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (v.getId() == R.id.tv_cancle_toast_price) {//左边
                                    delComments(bean.reply_id, 2, position);
                                    toastPricePopuWindow.dismiss();

                                } else if (v.getId() == R.id.tv_go_toast_price) {//右边
//                                    delComments(commentId, 1);


                                    toastPricePopuWindow.dismiss();
                                }


                            }
                        }, str, "确认", "考虑一下");
                        toastPricePopuWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);


//                        delComments(bean.reply_id,2);
                    }
                });
                myHolder.tv_thumbup_topic_item.setVisibility(View.VISIBLE);
            } else {
                myHolder.iv_delect.setVisibility(View.GONE);
                myHolder.tv_thumbup_topic_item.setVisibility(View.VISIBLE);
            }


            if (bean.imgListEx != null && bean.imgListEx.size() > 0) {

                myHolder.gv_dynamic_item.setVisibility(View.VISIBLE);

                MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_dynamic_item.getAdapter();
                if (adapter != null) {
                    adapter.setData(bean.imgListEx);
                    adapter.notifyDataSetChanged();
                } else {
                    List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx = bean.imgListEx;
                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.imgListEx));
                }


                if (bean.imgListEx.size() == 4 || bean.imgListEx.size() == 2) {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item, 2);
                } else if (bean.imgListEx.size() == 1) {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item, 1);
                } else {
                    GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item, 3);
                }

//                    myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                myHolder.gv_dynamic_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String urls = "";
                        for (int i = 0; i < bean.imgListEx.size(); i++) {
                            String url = bean.imgListEx.get(i).url;
                            String delUrl = Utils.delUrl(url);
                            urls += delUrl + ",";
                        }

                        String substring = urls.substring(0, urls.length() - 1);
                        Intent intent = new Intent(ComentsDetailsActivity.this, PicShowActivity.class);
                        intent.putExtra("urls", substring);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                });


            } else {
                myHolder.gv_dynamic_item.setVisibility(View.GONE);

            }


            if (position == getCount() - 3 || getCount() < 3) {
                if (pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestNetDatas();
                }
            }

            return convertView;
        }
    }


    class MyGvAdapter extends BaseAdapter {


        private List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx;


        public MyGvAdapter(List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }

        @Override
        public int getCount() {
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

            TopicDetailBean.DataBean.ListBean.ImageList imageList = imgListEx.get(position);

            View view = View.inflate(QCaApplication.getContext(), R.layout.img_gif_layout, null);
            ImageView imageView = view.findViewById(R.id.iv);
            TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
            Utils.showType(imageList.url, imageView, tv_gif_icon, imageList.size);

            if (getCount() == 1) {
                Utils.scaleImg(ComentsDetailsActivity.this, view, imageView, imageList.size);
            }
            return view;
        }

        public void setData(List<TopicDetailBean.DataBean.ListBean.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }
    }


    /**
     * 删除评论
     */
    private void delComments(String replayId, final int subtype, final int position) {
        //http/php/api.php?action=topic&type=del&subtype=2&id=4123
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action", "topic");
        params.put("type", "del");
        params.put("subtype", subtype);
        params.put("id", replayId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout, this) {
            @Override
            public void onSucces(String string) {
                PublishPointBean publishPointBean = JsonParseUtils.parseJsonClass(string, PublishPointBean.class);
                if (publishPointBean != null && publishPointBean.status != null) {
                    if (publishPointBean.status.errno == 0) {
                        ToastUtils.toast("删除成功");
                        if (subtype == 1) {
                            iv_delect_header.setTag(true);
                            finish();
                        } else {
//                            page = 0;
//                            requestNetDatas();
                            comentsDetailBean.data.list.remove(position);
                            myAdapter.notifyDataSetChanged();
                        }
                    } else {
                        ToastUtils.toast("删除失败");
                    }
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private void toReplay(String replayId, String name) {
        ReplayCommentsActivity.start(this, replayId, replayId, name, commentId, 44);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 44 && resultCode == 99) {
            isSelectFirst = true;
            initData();
        }
    }

    /**
     * 处理点赞
     *
     * @param bean
     */
    private void requestThumbup(final ComentsDetailBean.DataBean.ListBean bean) {

        //http://app.mayisports.com/php/api.php?action=topic&type=praise_reply&id=4091
        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        if (LoginUtils.isUnLogin()) {
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action", "topic");
        params.put("type", "praise_reply");
        params.put("id", bean.reply_id);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null, this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string, PraiseBean.class);
                if (praiseBean.data != null) {
                    bean.praise_count = praiseBean.data.praise_count;
                    bean.praise_status = praiseBean.data.praise_status;
                    myAdapter.notifyDataSetChanged();
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

    static class MyHolder {
        public ImageView iv_head_dynamic_item;
        public ImageView vip_coin;
        public TextView iv_red_point_header;
        public TextView tv_name_item;
        public TextView tv_create_time_topic_item;
        public ImageView iv_jinghua_item;
        public TextView tv_content_topic_detail_item;
        public LinearLayout ll_tag_container_item;
        public LinearLayout ll_img_container_dynamic_item;
        public TextView tv_thumbup_topic_item;
        public TextView tv_comments_topic_item;
        public RelativeLayout rl_top;
        public LinearLayout ll_replay_layout;
        public TextView tv_name;
        public TextView tv_replay;
        public ImageView iv_delect;

        public GridView gv_dynamic_item;
        public LinearLayout ll_tv_count_container;
    }
}
