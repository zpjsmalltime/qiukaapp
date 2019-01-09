package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.progress.ProgressImageView;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.JCaoStrandPlayer;
import com.mayisports.qca.view.LongImageView;
import com.mayisports.qca.view.PinchImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.kymjs.kjframe.http.HttpCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;


/**
 * 查看图片界面
 */
public class PicShowActivity extends BaseActivity {


    private GalleryViewPager gvp_pic_show;
    private ViewPager vp_pic_show;
    private ProgressBar pb_save_img;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_pic_show;
    }

    private int clickPosition;
    @Override
    protected void initView() {
        super.initView();


        pb_save_img = findViewById(R.id.pb_save_img);
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("查看原图");
        iv_left_title.setOnClickListener(this);

        iv_download_pic_show_acitivity = findViewById(R.id.iv_download_pic_show_acitivity);
        iv_download_pic_show_acitivity.setOnClickListener(this);

        gvp_pic_show = findViewById(R.id.gvp_pic_show);
        gvp_pic_show.setOffscreenPageLimit(3);

        vp_pic_show = findViewById(R.id.vp_pic_show);
        vp_pic_show.setOffscreenPageLimit(3);


    }

    public static void start(Activity activity,String urls,int clickPosition){
        Intent intent = new Intent(activity,PicShowActivity.class);
        intent.putExtra("urls",urls);
        intent.putExtra("position",clickPosition);
        activity.startActivity(intent);
    }

    public static void start(Activity activity,String urls,int clickPosition,boolean isHeader){
        Intent intent = new Intent(activity,PicShowActivity.class);
        intent.putExtra("urls",urls);
        intent.putExtra("position",clickPosition);
        intent.putExtra("isHeader",isHeader);
        activity.startActivity(intent);
    }

    private String urls = "";
    private boolean isHeader;
    private ArrayList<String> urlList;

    @Override
    protected void initDatas() {
        super.initDatas();
        clickPosition = getIntent().getIntExtra("position",0);
        urls = getIntent().getStringExtra("urls");
        if(urls == null) urls = "";
        String[] split = urls.split(",");
        urlList = new ArrayList<>();
        for(int i = 0;i<split.length;i++){
            String s = split[i];
            boolean sqkt_ = s.contains("sqkt_");
            if(sqkt_){
                s = s.replace("sqkt_","qkt_");
            }

            urlList.add(s);
        }

        final MyAdapter myAdapter = new MyAdapter(this,urlList);
        vp_pic_show.setAdapter(myAdapter);
        vp_pic_show.setCurrentItem(clickPosition);

        tv_title.setText((clickPosition + 1) + "/" + urlList.size());

        isHeader = getIntent().getBooleanExtra("isHeader", false);
        if(isHeader){
            setTitleShow(false);
        }

        vp_pic_show.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {


                tv_title.setText((position + 1) + "/" + urlList.size());
                String url = urlList.get(position);
                if(urlList.get(position).endsWith(".gif")) {
                    ViewGroup viewWithTag = vp_pic_show.findViewWithTag(position);
                    ProgressBar progress_imageview = viewWithTag.findViewById(R.id.progress_imageview);

                    FrameLayout fl_content = viewWithTag.findViewById(R.id.fl_content);
                    PinchImageView pinchImageView = (PinchImageView) fl_content.getChildAt(0);

                    PictureUtils.showProgress(url, pinchImageView, PicShowActivity.this, progress_imageview, R.drawable.pic_dauflt);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

    }

    private ImageView iv_download_pic_show_acitivity;

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_left_title:
                finish();
                break;
            case R.id.iv_download_pic_show_acitivity:
                //保存到本地照片
                iv_download_pic_show_acitivity.setEnabled(false);

                final int currentItem = vp_pic_show.getCurrentItem();
                final String url = urlList.get(currentItem);

                if(Utils.isStoreFilePath(url)){
                    ToastUtils.toast("图片已保存");
                    iv_download_pic_show_acitivity.setEnabled(true);
                    return;
                }
                ToastUtils.toast("保存中 ...");

                final File file = Utils.getStoreFilePath(url);
                String storeFilePath = file.getPath();


                if(!TextUtils.isEmpty(storeFilePath)){
                    RequestNetWorkUtils.downloadFile(storeFilePath, url, new HttpCallBack() {
                        @Override
                        public void onLoading(final long count, final long current) {
                            super.onLoading(count, current);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb_save_img.setVisibility(View.VISIBLE);
                                    int percent = (int) (current*100/count);
                                    pb_save_img.setProgress(percent);
                                    if(percent>=100){
                                        pb_save_img.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }

                        @Override
                        public void onSuccess(byte[] t) {
                            super.onSuccess(t);

                            handler.sendEmptyMessage(1);
                            // 其次把文件插入到系统图库
//                            int indexOf = url.lastIndexOf("/");
//                            String substring = url.substring(indexOf);
//                            String fileName = substring.trim();
//                            try {
//                                MediaStore.Images.Media.insertImage(QCaApplication.getContext().getContentResolver(),
//                                        file.getPath(), fileName, null);
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            }

                            // 最后通知图库更新
                            QCaApplication.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
//                            file.delete();
                        }

                        @Override
                        public void onFinish() {
                            super.onFinish();
                            iv_download_pic_show_acitivity.setEnabled(true);
                        }

                        @Override
                        public void onFailure(int errorNo, String strMsg) {
                            super.onFailure(errorNo, strMsg);
                            handler.sendEmptyMessage(0);
                            file.delete();
                        }
                    });
                }



                break;
        }
    }

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                ToastUtils.toast("保存成功...");
            }else{
                ToastUtils.toast("保存失败...");

            }
            iv_download_pic_show_acitivity.setEnabled(true);
        }
    };



    class  MyAdapter extends PagerAdapter {

        private Context context;
        private List<String> list;
        public MyAdapter(Context context,List<String> urls) {
            this.context = context;
            this.list = urls;
        }

        @Override
        public int getCount() {
            if(list == null)return 0;
            return list.size();
//            return 3;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final String url = list.get(position);

            View view = View.inflate(PicShowActivity.this,R.layout.pic_show_item,null);
            FrameLayout fl_content = view.findViewById(R.id.fl_content);


                ProgressBar progress_imageview = view.findViewById(R.id.progress_imageview);

                PinchImageView pinchImageView = new PinchImageView(context);
                pinchImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PicShowActivity.this.finish();
//                        WebViewActivtiy.start(PicShowActivity.this,url,"tu");
                    }
                });

                if(!url.startsWith("http")){
                    progress_imageview.setVisibility(View.GONE);

                    iv_download_pic_show_acitivity.setVisibility(View.GONE);
                    setTitleShow(false);

                    if(url.endsWith(".mp4") || url.endsWith(".avi") || url.endsWith(".mpeg")){
                        JCVideoPlayerStandard jc_play = view.findViewById(R.id.jc_play);
                        jc_play.setVisibility(View.VISIBLE);


                        String videoUrl =   url;
                        boolean setUp = jc_play.setUp(videoUrl, JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
                        if (setUp) {

                            PictureUtils.showImg(url, jc_play.thumbImageView);
                            //           myHolder.jc_type_item.startButton.performClick();
                        }

                    }else{
                        PictureUtils.show(url,pinchImageView);
                        JCVideoPlayerStandard jc_play = view.findViewById(R.id.jc_play);
                        jc_play.setVisibility(View.GONE);

                    }


                }else if (isHeader) {
                    PictureUtils.showProgress(url, pinchImageView, PicShowActivity.this, progress_imageview, R.drawable.defluat_header_icon);
                } else {
                    PictureUtils.showProgress(url, pinchImageView, PicShowActivity.this, progress_imageview, R.drawable.pic_dauflt);
                }

                fl_content.addView(pinchImageView);

            view.setTag(position);
            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }


        private View mCurrentView;

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container,position,object);
            mCurrentView = (View)object;
            View view = (View) object;
            view.setTag(position);
        }

        public View getPrimaryItem() {
            return mCurrentView;
        }


    }




    public static class ProgressHandler extends Handler {

        private final WeakReference<Activity> mActivity;
        private final ProgressBar mProgressImageView;

        public ProgressHandler(Activity activity, ProgressBar progressImageView) {
            super(Looper.getMainLooper());
            mActivity = new WeakReference<>(activity);
            mProgressImageView = progressImageView;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Activity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        mProgressImageView.setVisibility(View.VISIBLE);
                        int percent = msg.arg1*100/msg.arg2;
                        mProgressImageView.setProgress(percent);
                        if(percent>=100){
                            mProgressImageView.setVisibility(View.GONE);
                        }
                        Log.e("progress",percent+"");
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }
}

