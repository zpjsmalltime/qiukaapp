package com.mayisports.qca.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import com.mayi.mayisports.QCaApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 获取关于app 版本之类 工具类
 */
public class UpdataAppUtils {



    public static void downLoadApkWebView(Activity activity,String apkUrl){
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.setData(Uri.parse(apkUrl));
        activity.startActivity(i);
        activity = null;
    }


    // 获取当前程序的版本号
    public static int getVersionCode() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = QCaApplication.getContext().getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(QCaApplication.getContext().getPackageName(), 0);
        return packInfo.versionCode;
    }
    public static int getVersionCode2()  {
        int versionCode = 0;
        PackageManager packageManager = QCaApplication.getContext().getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(QCaApplication.getContext().getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionCode;
    }
    // 获取当前程序的版本name
    public static String getVersionName() throws Exception {
        //获取packagemanager的实例
        PackageManager packageManager = QCaApplication.getContext().getPackageManager();
        //getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(QCaApplication.getContext().getPackageName(), 0);
        return packInfo.versionName;
    }


//    // 用pull解析器解析服务器返回的xml文件 (xml封装了版本号)
//    public static UpdataInfo getUpdataInfo(InputStream is) throws Exception {
//        XmlPullParser parser = Xml.newPullParser();
//        parser.setInput(is, "utf-8");//设置解析的数据源
//        int type = parser.getEventType();
//        UpdataInfo info = new UpdataInfo();//实体
//        while (type != XmlPullParser.END_DOCUMENT) {
//            switch (type) {
//                case XmlPullParser.START_TAG:
//                    if ("versioncode".equals(parser.getName())) {
//                        info.setVersionCode(parser.nextText()); //获取版本号
//                    } else if ("url".equals(parser.getName())) {
//                        info.setUrl(parser.nextText()); //获取要升级的APK文件
//                    } else if ("description".equals(parser.getName())) {
//                        info.setDescription(parser.nextText()); //获取该文件的信息
//                    } else if ("versionname".equals(parser.getName())) {
//                        info.setVersionname(parser.nextText()); //获取该文件的信息
//                    } else if ("title".equals(parser.getName())) {
//                        info.setTitle(parser.nextText()); //获取该文件的信息
//                    } else if ("ismust".equals(parser.getName())) {
//                        info.setIsmust(parser.nextText()); //获取该文件的信息
//                    }
//                    break;
//            }
//            type = parser.next();
//        }
//        return info;
//    }


    //从服务器下载apk
    public static File getFileFromServer(String path, ProgressDialog pd) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "youzitang.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                pd.setProgress(total);
            }


            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }


    public static InputStream downloadXML(String urlStr) {
        InputStream inStream = null;
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conn.setConnectTimeout(5 * 1000);
        try {
            conn.setRequestMethod("GET");
            conn.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            inStream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inStream;
    }

    public static String convertStreamToString(InputStream is) throws UnsupportedEncodingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "/n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


    /*
 * 从服务器中下载APK
 */
//    public  void downLoadApk(UpdataInfo updataInfo) {
//        final ProgressDialog pd;    //进度条对话框
//        pd = new ProgressDialog(splashActivity);
//        pd.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
//        pd.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount() == 0) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
////        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        pd.setProgressNumberFormat("%1d kb/%2d kb");
//        pd.setMessage("正在下载更新");
//        pd.show();
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    File file = getFileFromServer(updataInfo.getUrl(), pd);
////                    splashActivity.installApk(file);
//                    pd.dismiss(); //结束掉进度条对话框
//                } catch (Exception e) {
////                    Message msg = new Message();
////                    msg.what = DOWN_ERROR;
////                    handler.sendMessage(msg);
//                    e.printStackTrace();
//                }
//            }}.start();
//    }
//
//
//    public static Observable<UpdataInfo> downloadXml(String path){
//        return Observable.create(subscriber -> {
//            if(!subscriber.isUnsubscribed()){
//                //访问网络
//                Request request = new Request.Builder().url(path).build();
//                new OkHttpClient().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if(response.isSuccessful()){
//                            InputStream inputStream = response.body().byteStream();
//                            if(inputStream != null){
//                                try {
//                                    subscriber.onNext(getUpdataInfo(inputStream));
//                                } catch (Exception e) {
//                                    subscriber.onError(e);
//                                    e.printStackTrace();
//                                }
//                            }
//                        }else {
//                            subscriber.onError(new Exception());
//                        }
//                        subscriber.onCompleted();
//                    }
//                });
//            }
//        });
//    }
//    public static Observable<String> downloadAudioFile(String path, String fileName){
//        return Observable.create(subscriber -> {
//            if(!subscriber.isUnsubscribed()){
//                //访问网络
//                Request request = new Request.Builder().url(path).build();
//                new OkHttpClient().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if(response.isSuccessful()){
//                            InputStream inputStream = response.body().byteStream();
//                            int length = response.body().bytes().length;
//                            if(inputStream != null){
//
//                                FileOutputStream fileOutputStream = null;
//                                try {
//                                    String path = SDUtils.youzitangPath()+fileName+".mp3";
//                                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/youzitang");
//                                    if(!file.exists()){
//                                        file.mkdirs();
//                                    }
//                                    fileOutputStream = new FileOutputStream(new File(path));
//                                    byte[] buffer = new byte[2048];
//                                    int len = 0;
//                                    while ((len = inputStream.read(buffer)) != -1) {
//                                        fileOutputStream.write(buffer, 0, len);
//                                    }
//                                    fileOutputStream.flush();
//                                } catch (IOException e) {
//                                    subscriber.onError(e);
//                                    e.printStackTrace();
//                                }
//                                subscriber.onNext("");
//                            }
//                        }else {
//                            subscriber.onError(new Exception());
//                        }
//                        subscriber.onCompleted();
//                    }
//                });
//            }
//        });
//    }
//    public Observable<String> downloadAudioFileGetProgress(String path, String fileName){
//        return Observable.create(subscriber -> {
//            if(!subscriber.isUnsubscribed()){
//                //访问网络
//                Request request = new Request.Builder().url(path).build();
//                new OkHttpClient().newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        subscriber.onError(e);
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        if(response.isSuccessful()){
//                            InputStream inputStream = response.body().byteStream();
//                            if(downloadProgress != null){
//                                downloadProgress.downloadLength((int)response.body().contentLength());
//                            }
//                            if(inputStream != null){
//                                FileOutputStream fileOutputStream = null;
//                                try {
//                                    String path = SDUtils.youzitangPath()+fileName+".mp3";
//                                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/youzitang");
//                                    if(!file.exists()){
//                                        file.mkdirs();
//                                    }
//                                    fileOutputStream = new FileOutputStream(new File(path));
//                                    byte[] buffer = new byte[2048];
//                                    int progress = 0;
//                                    int len = 0;
//                                    while ((len = inputStream.read(buffer)) != -1) {
//                                        progress += 2048;
//                                        if(downloadProgress != null){
//                                            downloadProgress.downloadProgress(progress);
//                                        }
//                                        fileOutputStream.write(buffer, 0, len);
//                                    }
//                                    fileOutputStream.flush();
//                                } catch (IOException e) {
//                                    subscriber.onError(e);
//                                    e.printStackTrace();
//                                }
//                                subscriber.onNext("");
//                            }
//                        }else {
//                            subscriber.onError(new Exception());
//                        }
//                        subscriber.onCompleted();
//                    }
//                });
//            }
//        });
//    }
//
//    /**
//     * 下载文件
//     * @param path
//     * @return
//     */
//    public static Observable<File> downloadFile(String path, String apkName){
//        return Observable.create(new Observable.OnSubscribe<File>() {
//            @Override
//            public void call(Subscriber<? super File> subscriber) {
//                if(!subscriber.isUnsubscribed()){
//                    try {
//                        File file = getFileFromServer(path, apkName);
//                        subscriber.onNext(file);
//                        subscriber.onCompleted();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        subscriber.onError(e);
//                    }
//                }
//            }
//        });
//    }


    //从服务器下载apk:
    private static File getFileFromServer(String path, String apkName) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), apkName+".apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }


    //安装apk
    public void installApk(File file) {
//        Intent intent = new Intent();
//        //执行动作
//        intent.setAction(Intent.ACTION_VIEW);
//        //执行的数据类型
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivityForResult(intent, 1);
//        finish();
    }


    private DownloadProgress downloadProgress;
    public void setDownloadProgress(DownloadProgress downloadProgress){
        this.downloadProgress = downloadProgress;
    }
    public interface DownloadProgress{
        void downloadLength(int length);
        void downloadProgress(int progress);
    }


}
