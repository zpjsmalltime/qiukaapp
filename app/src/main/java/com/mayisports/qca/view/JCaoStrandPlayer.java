package com.mayisports.qca.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.danikula.videocache.HttpProxyCacheServer;
import com.mayi.mayisports.QCaApplication;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jcvideoplayer_lib.R.string;

/**
 * Created by zhangpengju on 2018/3/30.
 */

public class JCaoStrandPlayer extends JCVideoPlayerStandard {
    public JCaoStrandPlayer(Context context) {
        super(context);
    }

    public JCaoStrandPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

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



    //静音模式  默认为false
    private boolean isSilencePattern = false;


    /**
     * 设置静音模式
     *
     * @param isSilencePattern
     */
    public void setSilencePattern(boolean isSilencePattern) {
        this.isSilencePattern = isSilencePattern;
    }

    /**
     * 设置音量
     *
     * @param isSilence
     */
    public void setVolume(boolean isSilence) {
        if (isSilence) {//静音
            JCMediaManager.instance().mediaPlayer.setVolume(0f, 0f);
        } else {
            JCMediaManager.instance().mediaPlayer.setVolume(1f, 1f);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(delTouch){
            return true;
        }else {
            return super.onTouchEvent(event);
        }

    }

    /**
     * 请求或者释放焦点
     *
     * @param focus
     */
    public void setAudioFocus(boolean focus) {
        AudioManager mAudioManager = (AudioManager)
                getContext().getSystemService(Context.AUDIO_SERVICE);
        if (focus) {//请求音频焦点
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener,
                    AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        } else {//释放
            mAudioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }




    public boolean delTouch;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(onClick != null){
            if(event.getAction() == MotionEvent.ACTION_UP) {
                onClick.onClick();
            }
        }

        if(delTouch){
            return true;
        }else {
           return super.onTouch(v, event);
        }
    }

    private OnClick onClick;

    public void setOnClick(OnClick onClick){
        this.onClick = onClick;
    }

    public interface OnClick{
        void onClick();
    }


    @Override
    public void setUiWitStateAndScreen(int state) {

            super.setUiWitStateAndScreen(state);

        if(state == 2){
       //     setAudioFocus(true);

            if (isSilencePattern ) {
                setAudioFocus(false);
                setVolume(true);
            }
        }


    }

    public static boolean isAllow4G;
    public static  boolean isShow = false;


    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro, int thumbImg, int coverImg, int bottomPro) {
        super.setAllControlsVisible(topCon,bottomCon,startBtn,loadingPro,thumbImg,coverImg,bottomPro);
        if(delTouch) {
            this.topContainer.setVisibility(GONE);
            this.bottomContainer.setVisibility(GONE);
        }
    }


    @Override
    public void showWifiDialog() {
      //  super.showWifiDialog();

        if(!isShow){
            isShow = true;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setMessage(this.getResources().getString(string.tips_not_wifi));
        builder.setPositiveButton(this.getResources().getString(string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                JCaoStrandPlayer.this.startPlayLogic();
                JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED = true;
                isAllow4G = true;
            }
        });
        builder.setNegativeButton(this.getResources().getString(string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            //    JCVideoPlayer.WIFI_TIP_DIALOG_SHOWED = true;
                isAllow4G = false;
            }
        });
        builder.create().show();
    }
}
