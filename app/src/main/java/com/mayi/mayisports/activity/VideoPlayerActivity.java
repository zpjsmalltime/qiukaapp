package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.fragment.ComentsDetailsFragment;
import com.mayisports.qca.fragment.VideoPlayerFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import static fm.jiecao.jcvideoplayer_lib.JCVideoPlayer.CLICK_QUIT_FULLSCREEN_TIME;


/**
 * 短视频播放界面
 */
public class VideoPlayerActivity extends BaseActivity implements SwipeBackActivityBase, RequestHttpCallBack.ReLoadListener {


    private SwipeBackActivityHelper mHelper;
    private SwipeBackLayout mSwipeBackLayout;

    public static void start(Activity activity, int position, DynamicBean dynamicBean,int page,String subType) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("dynamicBean", dynamicBean);
        intent.putExtra("page",page);
        intent.putExtra("subType",subType);
        activity.startActivity(intent);
    }

    public static void start(Fragment activity, int position,int page,String subType,int requestCode) {
        Intent intent = new Intent(activity.getActivity(), VideoPlayerActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("page",page);
        intent.putExtra("subType",subType);
        activity.startActivityForResult(intent,requestCode);

    }

    public static void start(Activity activity, int position,int page,String subType,int requestCode) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("page",page);
        intent.putExtra("subType",subType);
        activity.startActivityForResult(intent,requestCode);

    }

    public static void start(Activity activity, int position,int page,String subType,int requestCode,boolean isSelectFirst) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("page",page);
        intent.putExtra("subType",subType);
        intent.putExtra("isSelectFirst",isSelectFirst);
        activity.startActivityForResult(intent,requestCode);

    }


    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

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

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_video_player;
    }


    private ViewPager vp_video_player_ac;

    private int stateP;
    private boolean isOver;
    private int currentPage;

    private FrameLayout fl_right_video_player_ac;
    private DrawerLayout mDrawerLayout;
    private ComentsDetailsFragment comentsDetailsFragment;

    private ProgressBar progress_volume_ac;
    private int prePage;
    private int currentT;

    private boolean isDragPage;
    private long preTime;


    private LinkedHashMap<String,Fragment> mapCacheFragment = new LinkedHashMap<>();


    float y1 = 0;
    float y2 = 0;
    float x1 = 0;
    float x2 = 0;


    /**
     * 处理滑动方向
     */
    private boolean isPullDown;
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {


        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            //当手指按下的时候

            y1 = event.getY();
          //  isDragPage = false;
            isPullDown = false;
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            //当手指离开的时候
            y2 = event.getY();
//            if(y1 - y2 > 70) {
//            //    ToastUtils.toastNoStates( "向上滑");
//            } else

            if(y2 - y1 > 150) {
                isPullDown = true;

                long duration = System.currentTimeMillis() - preTime;
//                &&
                if(duration>2500  && currentPage == 0 && !isDragPage){   //当前页是第一页，并且是拖动状态，并且像素偏移量为0
                    preTime = System.currentTimeMillis();
                    boolean openDrawer = isOpenDrawer();
                    if(!openDrawer) {
                        ToastUtils.toast("当前为第一个视频");
                    }
                }
            }
//            else if(x1 - x2 > 50) {
//              //  ToastUtils.toastNoStates( "向左滑");
//            } else if(x2 - x1 > 50) {
//              //  ToastUtils.toastNoStates( "向右滑");
//            }
        }



        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void initView() {
        super.initView();
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mSwipeBackLayout = getSwipeBackLayout();
        double v = DisplayUtil.getScreenWidth(this) / 2.3;
        mSwipeBackLayout.setSwipeMode(SwipeBackLayout.FULL_SCREEN_LEFT, (int) v);
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
//        mSwipeBackLayout.seten


        isSelectFirst = getIntent().getBooleanExtra("isSelectFirst",false);

        progress_volume_ac = (ProgressBar) findViewById(R.id.progress_volume_ac);

        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                Log.e("onScrollStateChange", state + "---" + scrollPercent);
                stateP = state;
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Log.e("onEdgeTouch", edgeFlag + "");

            }

            @Override
            public void onScrollOverThreshold() {
                Log.e("onScrollOverThreshold", "true");
                isOver = true;
            }
        });


        setTitleShow(false);
        vp_video_player_ac = (ViewPager) findViewById(R.id.vp_video_player_ac);

        vp_video_player_ac.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                long duration = System.currentTimeMillis() - preTime;
//                &&
                if(duration>2500  && position == 0 && isDragPage && positionOffsetPixels == 0){   //当前页是第一页，并且是拖动状态，并且像素偏移量为0
                    preTime = System.currentTimeMillis();
                  //  ToastUtils.toast("暂无更多视频");
                }

            }

            @Override
            public void onPageSelected(int position) {

                currentPage = position;
                VideoPlayerActivity.this.position = position;
//                if (position == 0) ToastUtils.toast("当前为第一个视频");
                if (dynamicBean.list.size() <3 || position == dynamicBean.list.size() - 2){
//                    ToastUtils.toast("当前为最后一个视频");
                    if (pre_page == 1) {
                        page++;
                        requestNetData();
                    }

                }

                try {
                    String comment_id = dynamicBean.list.get(currentPage).comment.comment_id;

                    comentsDetailsFragment.initDatas(comment_id,isSelectFirst);

                    if(isSelectFirst == true){
                 //       isSelectFirst = false;
                    }
                    VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(currentPage);

                    if (!isOpenDrawer()) videoPlayerFragment.startPlay();
                }catch (Exception e){

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isDragPage = state == 1;

            }
        });


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        setDrawerLeftEdgeSize(this, mDrawerLayout, 0.05f);
        fl_right_video_player_ac = (FrameLayout) findViewById(R.id.fl_right_video_player_ac);
        fl_right_video_player_ac.getLayoutParams().width = DisplayUtil.getScreenWidth(this);

        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            /**
             * 当抽屉滑动状态改变的时候被调用
             * 状态值是STATE_IDLE（闲置--0）, STATE_DRAGGING（拖拽的--1）, STATE_SETTLING（固定--2）中之一。
             * 抽屉打开的时候，点击抽屉，drawer的状态就会变成STATE_DRAGGING，然后变成STATE_IDLE
             */
            @Override
            public void onDrawerStateChanged(int arg0) {
                Log.i("drawer", "drawer的状态：" + arg0);
            }

            /**
             * 当抽屉被滑动的时候调用此方法
             * arg1 表示 滑动的幅度（0-1）
             */
            @Override
            public void onDrawerSlide(View arg0, float arg1) {
                Log.i("drawer", arg1 + "");
                if (arg1 > 0.3) {
//                    String comment_id = dynamicBean.list.get(currentPage).comment.comment_id;
//                    comentsDetailsFragment.initDatas(comment_id);
                }
            }

            /**
             * 当一个抽屉被完全打开的时候被调用
             */
            @Override
            public void onDrawerOpened(View arg0) {
                Log.i("drawer", "抽屉被完全打开了！");
                String comment_id = dynamicBean.list.get(currentPage).comment.comment_id;

                VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(currentPage);
//                VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) mapCacheFragment.get(currentPage + "");
                videoPlayerFragment.openDrawer();

            }

            /**
             * 当一个抽屉完全关闭的时候调用此方法
             */
            @Override
            public void onDrawerClosed(View arg0) {
                Log.i("drawer", "抽屉被完全关闭了！");
                VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(currentPage);
//                VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) mapCacheFragment.get(currentPage + "");
                videoPlayerFragment.closeDrawer();

            }
        });

        /**
         * 动态添加fragment
         */

//          int childCount = fl_right_video_player_ac.getChildCount();
//        if(childCount == 0) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        comentsDetailsFragment = ComentsDetailsFragment.newInstance("");
        transaction.add(R.id.fl_right_video_player_ac, comentsDetailsFragment);
        transaction.commit();
//        }



        if(isSelectFirst){


            openDrawer();
            vp_video_player_ac.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isSelectFirst = false;
                }
            },500);


//            openDrawer();
        }
    }


    public void openDrawer() {
        if (mDrawerLayout == null) return;
        mDrawerLayout.openDrawer(Gravity.RIGHT);
    }

    public void closeDrawer() {
        if (mDrawerLayout == null) return;
        mDrawerLayout.closeDrawer(Gravity.RIGHT);
    }

    public boolean isOpenDrawer(){
        if(mDrawerLayout == null)return false;

        return  mDrawerLayout.isDrawerOpen(Gravity.RIGHT);
    }


    @Override
    public void finish() {
        setResult(22,getIntent());
        JCVideoPlayer.releaseAllVideos();
        super.finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
            JCVideoPlayer.releaseAllVideos();
            vp_video_player_ac.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initDatass();
                }
            }, 5);
    }



    @Override
    public void onBackPressed() {

        if(mDrawerLayout.isDrawerOpen(Gravity.RIGHT)){
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
            return;
        }

        if (JCVideoPlayer.backPress()) {
//            return;
        }
        super.onBackPressed();
    }


    /**
     * 放开pause 释放
     */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(currentPage);
//            VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) mapCacheFragment.get(currentPage + "");
//            videoPlayerFragment.currentTime = videoPlayerFragment.jc_player_fg.getCurrentPositionWhenPlaying1();
            if(videoPlayerFragment.jc_player_fg.currentState == JCVideoPlayer.CURRENT_STATE_PAUSE){
                videoPlayerFragment.isBackPause = true;
            }
        }catch (Exception e){

        }
        JCVideoPlayer.releaseAllVideos();
    }

    private List<Fragment> fragmentList = new ArrayList<>();

    private int position = -1;
    public DynamicBean dynamicBean;
    private int page;
    private String subType;

    private MyPagerAdapter myPagerAdapter;


    private boolean isSelectFirst;
    protected void initDatass() {

       try {
           subType = getIntent().getStringExtra("subType");

           if (position == -1) {
               position = getIntent().getIntExtra("position", 0);
               currentPage = position;
               dynamicBean = (DynamicBean) getIntent().getSerializableExtra("dynamicBean");
               if (Constant.dynamicBean != null) {
                   dynamicBean = Constant.dynamicBean;
//                Constant.dynamicBean = null;
               }
               page = getIntent().getIntExtra("page", 0);


               subType = getIntent().getStringExtra("subType");


               fragmentList.clear();
               for (int i = 0; i < dynamicBean.list.size(); i++) {
                   DynamicBean.ListBean bean = dynamicBean.list.get(i);

                   fragmentList.add(VideoPlayerFragment.newInstance(i, bean));
               }
           }
           myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());

           vp_video_player_ac.setAdapter(myPagerAdapter);

           if (position == 0) {
               String comment_id = dynamicBean.list.get(position).comment.comment_id;
               comentsDetailsFragment.initDatas(comment_id,isSelectFirst);
               VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(position);
//            VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) mapCacheFragment.get(position + "");

               if (!isOpenDrawer() && !isSelectFirst) videoPlayerFragment.startPlay();



           } else if (position == -1) {//单个视频跳转
               /**
                * 请求数据
                */

               String comment_id = dynamicBean.list.get(0).comment.comment_id;
               comentsDetailsFragment.initDatas(comment_id,isSelectFirst);
               VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(0);
//            VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) mapCacheFragment.get(position + "");
               if (!isOpenDrawer() && !isSelectFirst) videoPlayerFragment.startPlay();
               page++;
               requestNetData();
               currentPage = 0;
           }


           vp_video_player_ac.setCurrentItem(currentPage);

       }catch (Exception e){

       }
//        VideoPlayerFragment videoPlayerFragment = (VideoPlayerFragment) fragmentList.get(position);
//        videoPlayerFragment.initData();
    }

    private int pre_page = 1;

    private  void requestNetData() {

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();

        params.put("action","sys_feed");
        //-5
        params.put("type","-5");
        params.put("subtype",subType);

        params.put("start",page);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(null,this) {
            @Override
            public void onSucces(String string) {
                if(page == 0) {

                    dynamicBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
                    DynamicBean dynamicBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                    if(dynamicBean!= null&&dynamicBean.data != null&&dynamicBean.list == null){
                        if(dynamicBean.data.list != null){
                            dynamicBean.list = dynamicBean.data.list;
                            dynamicBean1.list = dynamicBean1.data.list;
                        }
                    }
                    delData(dynamicBean,dynamicBean1);

                    pre_page = 1;
//                    lv_home_fg.setAdapter(myAdapter);
//                    myAdapter.notifyDataSetChanged();

                }else{
                    try {
                        DynamicBean newBean = JsonParseUtils.parseJsonClass(string, DynamicBean.class);
                        DynamicBean newBean1 = JsonParseUtils.parseJsonClass(string, DynamicBean.class);

                        if(newBean!= null&&newBean.data != null&&newBean.list == null){
                            if(newBean.data.list != null){
                                newBean.list = newBean.data.list;
                                newBean1.list = newBean1.data.list;
                            }
                        }
                        delData(newBean,newBean1);


                        if(newBean.list.size()>0) {
                            dynamicBean.list.addAll(newBean.list);
                            for(int i = 0;i<newBean.list.size();i++){
                                DynamicBean.ListBean bean = newBean.list.get(i);

                                fragmentList.add(VideoPlayerFragment.newInstance(i,bean));
                            }
                        }else{
                            pre_page = 0;
                        }
                    }catch (Exception e){
//                        ToastUtils.toast("没有更多了。。。");
                        pre_page = 0;
                    }
                    myPagerAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);

            }

            @Override
            public void onFinish() {
                super.onFinish();


            }
        });
    }

    /**
     * 处理数据  type 1 显示
     * @param homeDataBean
     * @param newBean
     */
    private void delData(DynamicBean homeDataBean, DynamicBean newBean) {
        if(homeDataBean.list != null&&newBean.list != null){
            homeDataBean.list.clear();
            for(int i = 0;i<newBean.list.size();i++){

                DynamicBean.ListBean beanX = newBean.list.get(i);
                if(beanX.type == 1 || beanX.type == 3  ||beanX.type == 6 ||beanX.type == 5) {//1推荐单  3 话题 6话题  5可能感兴趣的人
                    homeDataBean.list.add(beanX);
                }
            }
        }
    }


    @Override
    public void onReload() {

    }


    private class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

//            VideoPlayerFragment videoPlayerFragment = VideoPlayerFragment.newInstance(position, dynamicBean.data.list.get(position));
//            mapCacheFragment.put(position+"",videoPlayerFragment);
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
//            if(dynamicBean != null){
//                return  dynamicBean.data.list.size();
//            }
            return fragmentList.size();
        }

//        @Override
//        public int getItemPosition(Object object) {
//
//            return POSITION_NONE;
//        }

    }


    private void setDrawerLeftEdgeSize (Activity activity, DrawerLayout drawerLayout, float displayWidthPercentage) {
        if (activity == null || drawerLayout == null) return;
        try {
            // 找到 ViewDragHelper 并设置 Accessible 为true
            Field leftDraggerField =
                    drawerLayout.getClass().getDeclaredField("mRightDragger");//Right
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            // 找到 edgeSizeField 并设置 Accessible 为true
            Field edgeSizeField = leftDragger.getClass().getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);
            int edgeSize = edgeSizeField.getInt(leftDragger);

            // 设置新的边缘大小
            Point displaySize = new Point();
            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
            edgeSizeField.setInt(leftDragger, Math.max(edgeSize, (int) (displaySize.x *
                    displayWidthPercentage)));
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        }
    }



    AudioManager audioManager = (AudioManager) QCaApplication.getContext().getSystemService(Context.AUDIO_SERVICE);

    int maxVolum = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);


    /**
     * 屏蔽系统音量键
     * @param event
     * @return
     */
    @Override
    public boolean dispatchKeyEvent( KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                int currVolum = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                currVolum--;
                if (currVolum <= 0) {
                    currVolum = 0;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currVolum,
                        0);
                int i = (int) (currVolum*1.0 / maxVolum*100);
                progress_volume_ac.setProgress(i);

                volumeBarHandler.removeCallbacksAndMessages(null);
                progress_volume_ac.setVisibility(View.VISIBLE);

                volumeBarHandler.sendEmptyMessageDelayed(1,1500);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                int currVolum2 = audioManager
                        .getStreamVolume(AudioManager.STREAM_MUSIC);
                currVolum2++;
                if (currVolum2 >= maxVolum) {
                    currVolum2 = maxVolum;
                }
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currVolum2,
                        0);
                int i2 =  (int) (currVolum2*1.0 / maxVolum*100);
                progress_volume_ac.setProgress(i2);
                volumeBarHandler.removeCallbacksAndMessages(null);
                progress_volume_ac.setVisibility(View.VISIBLE);

                volumeBarHandler.sendEmptyMessageDelayed(1,1500);
                return true;
        }
        return super.dispatchKeyEvent(event);
    }

    private Handler volumeBarHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progress_volume_ac.setVisibility(View.GONE);

        }
    };


}
