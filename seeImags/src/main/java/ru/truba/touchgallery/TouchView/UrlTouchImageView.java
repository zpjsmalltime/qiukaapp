/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ru.truba.touchgallery.TouchView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import ru.truba.touchgallery.R;
import ru.truba.touchgallery.TouchView.InputStreamWrapper.InputStreamProgressListener;

public class UrlTouchImageView extends RelativeLayout {
    public ProgressBar mProgressBar;
    protected TouchImageView mImageView;

    protected Context mContext;

    public UrlTouchImageView(Context ctx)
    {
        super(ctx);
        mContext = ctx;
        init();

    }
    public UrlTouchImageView(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
        mContext = ctx;
        init();
    }
    public TouchImageView getImageView() { return mImageView; }

    @SuppressWarnings("deprecation")
    protected void init() {
        initImageLoader();
        mImageView = new TouchImageView(mContext);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mImageView.setLayoutParams(params);
        this.addView(mImageView);
        mImageView.setVisibility(GONE);

        mProgressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleHorizontal);
        params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        params.setMargins(30, 0, 30, 0);
        mProgressBar.setLayoutParams(params);
        mProgressBar.setIndeterminate(false);
        mProgressBar.setMax(100);
        this.addView(mProgressBar);
    }


    /**
     * 初始化第三方图片加载器
     */
    private ImageLoader imageLoader;
    private DisplayImageOptions normalOptions;
    private void initImageLoader() {

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mContext).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
//        com.nostra13.universalimageloader.utils.L.disableLogging();

        initOptions();


    }

    private void initOptions() {
        imageLoader = ImageLoader.getInstance();

        normalOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.pic_default_loading)
                .showImageForEmptyUri(R.drawable.pic_default_loading)
                .showImageOnFail(R.drawable.pic_default_loading).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)// 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
                .build();


    }

    public void setUrl(String imageUrl)
    {
//        new ImageLoadTask().execute(imageUrl);
        imageLoader.displayImage(imageUrl,mImageView,normalOptions);
        mImageView.setScaleType(ScaleType.MATRIX);
        mImageView.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
    }

    public void setScaleType(ScaleType scaleType) {
        mImageView.setScaleType(scaleType);
    }

    //No caching load
    public class ImageLoadTask extends AsyncTask<String, Integer, Bitmap>
    {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bm = null;
            try {
                URL aURL = new URL(url);
                URLConnection conn = aURL.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                int totalLen = conn.getContentLength();
                InputStreamWrapper bis = new InputStreamWrapper(is, 8192, totalLen);
                bis.setProgressListener(new InputStreamProgressListener()
                {
                    @Override
                    public void onProgress(float progressValue, long bytesLoaded,
                                           long bytesTotal)
                    {
                        publishProgress((int)(progressValue * 100));
                    }
                });
                bm = BitmapFactory.decodeStream(bis);
//                bm = BitmapFactory.decodeStream(bis,new Rect(),opt);
                bis.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap == null)
            {
                mImageView.setScaleType(ScaleType.CENTER);
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_photo);
                mImageView.setImageBitmap(bitmap);
            }
            else
            {
                mImageView.setScaleType(ScaleType.MATRIX);
                mImageView.setImageBitmap(bitmap);
            }
            mImageView.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            mProgressBar.setProgress(values[0]);
        }
    }
}
