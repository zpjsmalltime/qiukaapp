package com.mayisports.qca.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.PicShowActivity;
import com.mayisports.qca.utils.progress.ProgressImageView;
import com.mayisports.qca.utils.progress.ProgressModelLoader;
import com.mayisports.qca.view.CropCircleTransformation;
import com.mayisports.qca.view.GlideRoundTransform;
import com.mayisports.qca.view.RoundedCornersTransformation;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 *图片显示工具类
 */
public class PictureUtils {

    /**
     * 暂停加载
     */
    public static void pauseLoad(){
        Glide.with(QCaApplication.getContext()).pauseRequests();
    }

    /**
     * 重新加载
     */
    public static void resumeLoad(){
        Glide.with(QCaApplication.getContext()).resumeRequests();
    }

    public static void showImg(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                into(imageView);//显示到目标View中
    }

    public static void showImgNoErr(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
               asBitmap(). //强制处理为bitmap
//                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
//                error(R.drawable.pic_dauflt).//加载失败时显示的图片
//                 .bitmapTransform(new BlurTransformation(QCaApplication.getContext(), 14, 3)).
                into(imageView);//显示到目标View中
    }

    public static void showImgNoErrMoHu(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
               // asBitmap(). //强制处理为bitmap
//                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
//                error(R.drawable.pic_dauflt).//加载失败时显示的图片
              bitmapTransform(new BlurTransformation(QCaApplication.getContext(), 20, 1)).
        into(imageView);//显示到目标View中
    }

    public static void show(String url, ImageView imageView){
        if(TextUtils.isEmpty(url))url = "";
        if(url.endsWith("gif")){
            showGIF(url,imageView);
        }else{
            Glide.with(QCaApplication.getContext()).
                    load(url).
                    asBitmap(). //强制处理为bitmap
                    placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                    error(R.drawable.pic_dauflt).//加载失败时显示的图片
                    into(imageView);//显示到目标View中
        }
    }

    public static void showProgress(String url, final ImageView imageView, Activity activity, ProgressBar progressImageView, int erroImgRes){

        progressImageView.setVisibility(View.GONE);
        if(url.endsWith("gif")){
            Glide.with(QCaApplication.getContext()).
                    using(new ProgressModelLoader(new PicShowActivity.ProgressHandler(activity, progressImageView))).
                    load(url)
                     .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    . //强制处理为bitmap
//                    placeholder(erroImgRes).//加载中显示的图片
                    error(erroImgRes)//加载失败时显示的图片
                    .into(imageView);//显示到目标View中
        }else{
            Glide.with(QCaApplication.getContext()).
                    using(new ProgressModelLoader(new PicShowActivity.ProgressHandler(activity, progressImageView))).
                    load(url)
                    .asBitmap()//强制处理为bitmap
                    .
//                    placeholder(erroImgRes).//加载中显示的图片
                  error(erroImgRes).//加载失败时显示的图片
                    into(imageView);//显示到目标View中
        }


    }


    public static void show(String url, ImageView imageView,int erroImgRes){



            Glide.with(QCaApplication.getContext()).
                    load(url).
                    asBitmap(). //强制处理为bitmap
                    placeholder(erroImgRes).//加载中显示的图片
                    error(erroImgRes).//加载失败时显示的图片
                    into(imageView);//显示到目标View中
    }


    public static void test(String url, ImageView imageView,int erroImgRes){



        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(erroImgRes).//加载中显示的图片
                error(erroImgRes).//加载失败时显示的图片
                into(new ImageViewTarget<Bitmap>(imageView) {
            @Override
            protected void setResource(Bitmap bitmap) {




            }
        });//显示到目标View中
    }





    public static void showGIF(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asGif()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                . //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                into(imageView);//显示到目标View中
    }

    public static void show(Bitmap bitmap, ImageView imageView){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();

            Glide.with(QCaApplication.getContext()).
                    load(bytes). //强制处理为bitmap
                    placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                    error(R.drawable.pic_dauflt).//加载失败时显示的图片
                    into(imageView);
            baos.close();
        }catch (Exception e){

        }

    }

    public static void show(int resourceId,ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(resourceId).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                into(imageView);//显示到目标View中
    }
    public static void show(Uri uri, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(uri).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                into(imageView);//显示到目标View中
    }
    public static void show(File file, ImageView imageView){//new File(Environment.getExternalStorageDirectory()
        Glide.with(QCaApplication.getContext()).
                load(file).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                into(imageView);//显示到目标View中
    }

    /**
     * 显示头像
     */
    public static void showCircle(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.defluat_header_icon).//加载中显示的图片
                error(R.drawable.defluat_header_icon).//加载失败时显示的图片
                transform(new CropCircleTransformation(QCaApplication.getContext())).
                into(imageView);//显示到目标View中

    }


    /**
     * 显示圆形图片
     */
    public static void showCircle(String url, ImageView imageView,int res){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(res).//加载中显示的图片
                error(res).//加载失败时显示的图片
                transform(new CropCircleTransformation(QCaApplication.getContext())).
                into(imageView);//显示到目标View中
    }

    public static void showCircleRounded(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.defluat_header_icon).//加载中显示的图片
                error(R.drawable.defluat_header_icon).//加载失败时显示的图片
                transform(new RoundedCornersTransformation(QCaApplication.getContext(),20,0)).
                into(imageView);//显示到目标View中
    }


    public static void showRounded(String url, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                transform(new CenterCrop(QCaApplication.getContext()),new GlideRoundTransform(QCaApplication.getContext(),5)).
                into(imageView);//显示到目标View中
    }

    public static void showRounded(String url, ImageView imageView,int res,int radiusDp){
        Glide.with(QCaApplication.getContext()).
                load(url).
                asBitmap(). //强制处理为bitmap
                placeholder(res).//加载中显示的图片
                error(res).//加载失败时显示的图片
                transform(new CenterCrop(QCaApplication.getContext()),new GlideRoundTransform(QCaApplication.getContext(),radiusDp)).
                into(imageView);//显示到目标View中
    }
    public static void showRounded(int res, ImageView imageView){
        Glide.with(QCaApplication.getContext()).
                load(res).
                asBitmap(). //强制处理为bitmap
                placeholder(R.drawable.pic_dauflt).//加载中显示的图片
                error(R.drawable.pic_dauflt).//加载失败时显示的图片
                transform(new CenterCrop(QCaApplication.getContext()),new GlideRoundTransform(QCaApplication.getContext(),5)).
                into(imageView);//显示到目标View中
    }


    //根据ImageView的大小自动缩放图片
    public static Bitmap autoResizeFromLocalFile(String picturePath) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        options.inSampleSize = calculateInSampleSize(options, 500, 500);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }


    //计算图片缩放比例
    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void saveBitmap(Bitmap bm, String name) {
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/qiuca/", name + ".png");
        if (f.exists()) {
            f.delete();
        }
        f = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/qiuca/",name+".png");

        try {
            f.createNewFile();
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 50, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @category 上传文件至Server的方法
     * @param uploadUrl 上传路径参数
     * @param uploadFilePath 文件路径
     * @author ylbf_dev
     */
    public static void uploadFileImage(final String uploadUrl, final String uploadFilePath, final Handler handler) {

        File file = new File(uploadFilePath);
        if(file.exists()){
            if(1024*1024*300 < file.length()){
                ToastUtils.toastNoStates("文件超过300M!");

                Message obtain = Message.obtain();
                obtain.what = 500;
                obtain.obj = uploadFilePath;
                handler.sendMessage(obtain);



                return;
            }
        }


        new Thread(new Runnable() {
            @Override
            public void run() {


                String end = "\r\n";
                String twoHyphens = "--";
//        String boundary = "******";
                String boundary = "----WebKitFormBoundary32C0ovB2kkh4d7WA";
                InputStream is = null;
                DataOutputStream dos = null;
                try {
                    URL url = new URL(uploadUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);



                    String userId = SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID);
                    String token = SPUtils.getString(QCaApplication.getContext(), Constant.TOKEN);

                    httpURLConnection.setRequestProperty("Cookie", "token="+token+";id="+userId);
                    httpURLConnection.setRequestProperty("user_id",userId);

                    dos = new DataOutputStream(httpURLConnection.getOutputStream());
                    dos.writeBytes(twoHyphens + boundary + end);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"file[]\"; filename=\"cai.png\"" + end);
                    dos.writeBytes("Content-Disposition: form-data; name=\"file[]\"; filename=\""+uploadFilePath+"\"" + end);
//          dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
                    dos.writeBytes(end);
                    // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
                    FileInputStream fis = new FileInputStream(uploadFilePath);
                    byte[] buffer = new byte[8192]; // 8k
                    int count = 0;
                    while ((count = fis.read(buffer)) != -1) {
                        dos.write(buffer, 0, count);

                    }
                    fis.close();
                    dos.writeBytes(end);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();

                    // 读取服务器返回结果
                    String responseMessage = httpURLConnection.getResponseMessage();
                    int responseCode = httpURLConnection.getResponseCode();
                    is = httpURLConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String result = br.readLine();
                    if(handler != null){
                        Message obtain = Message.obtain();
                        obtain.what = responseCode;
                        obtain.obj = result;
                        handler.sendMessage(obtain);
                    }
                    Log.e("response",result+""+responseMessage+responseCode);
                    dos.close();
                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("exception",e.getMessage());
//            setTitle(e.getMessage());
                }finally {
                    if(dos != null){
                        try {
                            dos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();
    }



    /**
     * @category 上传文件至Server的方法
     * @param uploadUrl 上传路径参数
     * @param uploadFilePath 文件路径
     * @author ylbf_dev
     */
    public static void uploadFileImageHeader(final String uploadUrl, final String uploadFilePath, final Handler handler) {
        new Thread(new Runnable() {
            @Override
            public void run() {


                String end = "\r\n";
                String twoHyphens = "--";
//        String boundary = "******";
                String boundary = "----WebKitFormBoundary32C0ovB2kkh4d7WA";
                InputStream is = null;
                DataOutputStream dos = null;
                try {
                    URL url = new URL(uploadUrl);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");



                    String userId = SPUtils.getString(QCaApplication.getContext(), Constant.USER_ID);
                    String token = SPUtils.getString(QCaApplication.getContext(), Constant.TOKEN);

                    httpURLConnection.setRequestProperty("Cookie", "laravel_session="+token+";id="+userId);
                    httpURLConnection.setRequestProperty("user_id",userId);


                    dos = new DataOutputStream(httpURLConnection.getOutputStream());
//                    dos.writeBytes(twoHyphens + boundary + end);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"file[]\"; filename=\"cai.png\"" + end);
//                    dos.writeBytes("Content-Disposition: form-data; name=\"data\"; filename=\"head.png\"" + end);
//                  dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\""
//                  + uploadFilePath.substring(uploadFilePath.lastIndexOf("/") + 1) + "\"" + end);
//                    dos.writeBytes(end);
                    // 文件通过输入流读到Java代码中-++++++++++++++++++++++++++++++`````````````````````````
                    FileInputStream fis = new FileInputStream(uploadFilePath);


                    String string = new String("data:image/jpeg;base64,");
                    string += GetImageStr(uploadFilePath);
                    String encode1 = "data="+URLEncoder.encode(string);
                    dos.writeBytes(encode1);



//                    byte[] buffer = new byte[8192]; // 8k

//                    int count = 0;
//                    while ((count = fis.read(buffer)) != -1) {
////                        dos.write(buffer, 0, count);
//
//                    }
                    fis.close();
//                    dos.writeBytes(end);
//                    dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
                    dos.flush();

                    // 读取服务器返回结果
                    String responseMessage = httpURLConnection.getResponseMessage();
                    int responseCode = httpURLConnection.getResponseCode();
                    is = httpURLConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is, "utf-8");
                    BufferedReader br = new BufferedReader(isr);
                    String result = br.readLine();
                    if(handler != null){
                        Message obtain = Message.obtain();
                        obtain.what = responseCode;
                        obtain.obj = result;
                        handler.sendMessage(obtain);
                    }
                    Log.e("response",result+""+responseMessage+responseCode);
                    dos.close();
                    is.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("exception",e.getMessage());
//            setTitle(e.getMessage());

                    if(handler != null){
                        Message obtain = Message.obtain();
                        obtain.what = -1;
                        obtain.obj = "";
                        handler.sendMessage(obtain);
                    }
                }finally {
                    if(dos != null){
                        try {
                            dos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if(is != null){
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();
    }


    public static String GetImageStr(String imgFilePath) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        byte[] data = null;

        // 读取图片字节数组
        try {
            InputStream in = new FileInputStream(imgFilePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
        String string = android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
        return string;// 返回Base64编码过的字节数组字符串
    }
}
