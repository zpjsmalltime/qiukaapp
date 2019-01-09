package com.mayisports.qca.view;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.danikula.videocache.HttpProxyCacheServer;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCMediaPlayerListener;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Zpj on 2018/3/15.
 */

public class JieCaoPlayer extends JCVideoPlayerStandard {
    public JieCaoPlayer(Context context) {
        super(context);
    }

    public JieCaoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * this.bottomProgressBar = (ProgressBar)this.findViewById(id.bottom_progressbar);
     this.titleTextView = (TextView)this.findViewById(id.title);
     this.backButton = (ImageView)this.findViewById(id.back);
     this.thumbImageView = (ImageView)this.findViewById(id.thumb);
     this.coverImageView = (ImageView)this.findViewById(id.cover);
     this.loadingProgressBar = (ProgressBar)this.findViewById(id.loading);
     this.tinyBackImageView = (ImageView)this.findViewById(id.back_tiny);
     this.thumbImageView.setOnClickListener(this);
     this.backButton.setOnClickListener(this);
     this.tinyBackImageView.setOnClickListener(this);
     * @param context
     */
    @Override
    public void init(Context context) {
        super.init(context);
        titleTextView.setVisibility(GONE);
        backButton.setVisibility(GONE);
        tinyBackImageView.setVisibility(GONE);
        bottomProgressBar.setVisibility(GONE);
        fullscreenButton.setVisibility(GONE);
        bottomContainer.setVisibility(GONE);
        progressBar.setVisibility(GONE);
        currentTimeTextView.setVisibility(GONE);
        totalTimeTextView.setVisibility(GONE);
        backButton.setVisibility(View.GONE);
        tinyBackImageView.setVisibility(View.GONE);

        this.looping = true;


//        HttpProxyCacheServer proxy = getProxy();
//        //注意不能传入本地路径，本地的你还传进来干嘛。
//        String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
//        videoView.setVideoPath(proxyUrl);




    }


    public long seekToTime = -1;

//    @Override
//    public void onPrepared() {
//        Log.i("JieCaoVideoPlayer", "onPrepared  [" + this.hashCode() + "] ");
//        if(this.currentState == 1) {
//            JCMediaManager.instance().mediaPlayer.start();
//            if(this.seekToTime != -1) {
//                JCMediaManager.instance().mediaPlayer.seekTo((long)this.seekToTime);
//                this.seekToTime = -1;
//            }
//
//            this.startProgressTimer();
//            this.setUiWitStateAndScreen(2);
//        }
//    }


//    public long getCurrentPositionWhenPlaying1() {
//        long position = 0;
//        if(this.currentState == 2 || this.currentState == 5 || this.currentState == 3) {
//            try {
//                position = JCMediaManager.instance().mediaPlayer.getCurrentPosition();
//            } catch (IllegalStateException var3) {
//                var3.printStackTrace();
//                return position;
//            }
//        }
//
//        return position;
//    }

    @Override
    public boolean setUp(String url, int screen, Object... objects) {
        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(url, true);
        url = proxyUrl;
        return super.setUp(url, screen, objects);
    }

    private HttpProxyCacheServer getProxy() {
        return QCaApplication.getProxy(QCaApplication.getContext());
    }

    public void setProgress(ProgressBar progress){
        bottomProgressBar = progress;
    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                startButton.setVisibility(VISIBLE);
                startDismissControlViewTimer();
                break;
        }

        return false;
    }


    /**
     * 更新播放按钮
     */
    @Override
    public void updateStartImage() {
//        super.updateStartImage();
        if(this.currentState == 2) {
            this.startButton.setImageResource(R.drawable.empty);

        } else if(this.currentState == 7) {//播放出错
            this.startButton.setImageResource(R.drawable.empty);
        } else {
            this.startButton.setImageResource(R.drawable.video_pause);
        }

    }


//
    public void changeUiToPlayingBufferingShow() {
        switch(this.currentScreen) {
            case 0:
            case 1:
                this.setAllControlsVisible(0, 0, 4, 0, 0, 4, 4);
                break;
            case 2:
                this.setAllControlsVisible(0, 0, 4, 0, 0, 4, 4);
            case 3:
        }

    }

    public void setUiWitStateAndScreen(int state) {
        super.setUiWitStateAndScreen(state);
        switch(this.currentState) {
            case 0:
                this.changeUiToNormal();
                break;
            case 1:
                this.changeUiToPreparingShow();
                this.startDismissControlViewTimer();
                break;
            case 2:
                this.changeUiToPlayingShow();
                this.startDismissControlViewTimer();
                break;
            case 3:
                this.changeUiToPlayingBufferingShow();
            case 4:
            default:
                break;
            case 5:
                this.changeUiToPauseShow();
                this.cancelDismissControlViewTimer();
                break;
            case 6:
                this.changeUiToCompleteShow();
                this.cancelDismissControlViewTimer();
                this.bottomProgressBar.setProgress(100);
                break;
            case 7:
                this.changeUiToError();
        }

    }

    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int coverImg, int bottomPro) {
        super.setAllControlsVisible(topCon,bottomCon,startBtn,loadingPro,thumbImg,coverImg,bottomPro);
        this.topContainer.setVisibility(GONE);
        this.bottomContainer.setVisibility(GONE);
    }
    public void startDismissControlViewTimer() {
        this.cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        this.mDismissControlViewTimerTask = new JieCaoPlayer.DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(this.mDismissControlViewTimerTask, 2500L);
    }
    public class DismissControlViewTimerTask extends JCVideoPlayerStandard.DismissControlViewTimerTask {
        public DismissControlViewTimerTask() {
        }

        public void run() {
            if(currentState != 0 && currentState != 7 && currentState != 6 && getContext() != null && getContext() instanceof Activity) {
                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    public void run() {
                        bottomContainer.setVisibility(GONE);
                        topContainer.setVisibility(GONE);
                       startButton.setVisibility(GONE);
                        if(currentScreen != 3) {
                            bottomProgressBar.setVisibility(0);
                        }

                    }
                });
            }

        }
    }
}
