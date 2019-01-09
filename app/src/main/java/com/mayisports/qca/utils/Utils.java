package com.mayisports.qca.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 常用工具类
 */
public class Utils {


    public static boolean isActive;

    /**
     * 是否在前台
     * @param context
     * @return
     */
    public static boolean isForeground(Context context)
    {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查手机上是否安装了指定的软件
     * @param packageName：应用包名
     * @return
     */
    public static boolean isAvilible(String packageName){
        //获取packagemanager
        final PackageManager packageManager = QCaApplication.getContext().getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<String>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                packageNames.add(packName);
            }
        }
        //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
        return packageNames.contains(packageName);
    }


    public static String md5(String passwordStr) throws NoSuchAlgorithmException {
        String saltStr = passwordStr;
        int hashIterations = 1024;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        byte[] hashed = md5.digest((passwordStr).getBytes());
        int iterations = hashIterations - 1; //already hashed once above
        for (int i = 0; i < iterations; i++) {
            md5.reset();
            hashed = md5.digest(hashed);
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) {
            sb.append(String.format("%02X", b)); // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
        }
        return sb.toString().toLowerCase();
    }


    public static String MD5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(s.getBytes("utf-8"));
            return toHex(bytes).toLowerCase();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String toHex(byte[] bytes) {

        final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
        StringBuilder ret = new StringBuilder(bytes.length * 2);
        for (int i=0; i<bytes.length; i++) {
            ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
            ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
        }
        return ret.toString();
    }

    public static String md5ForParams(String content,String sign) throws NoSuchAlgorithmException {
        String saltStr = sign;
        int hashIterations = 10;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.reset();
        byte[] hashed = md5.digest((content+saltStr).getBytes());
        int iterations = hashIterations - 1; //already hashed once above
        for (int i = 0; i < iterations; i++) {
            md5.reset();
            hashed = md5.digest(hashed);
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : hashed) {
            sb.append(String.format("%02X", b)); // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 判断应用是否安装通过包名
     * @param pkgName
     * @param context
     * @return
     */
    public static boolean isPkgInstalled(String pkgName, @Nullable Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }

    }


    /**
     * 保存到本地图片
     * @param context
     * @param bmp
     * @param url
     */
    public static boolean saveImageToGallery(Context context, Bitmap bmp, String url) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qca");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        int indexOf = url.lastIndexOf("/");
        String substring = url.substring(indexOf);
        String fileName = substring.trim();
        File file = new File(appDir, fileName);
        Log.e("file",file.getPath());
        Log.e("file_boolean",file.exists()+"");
        if(file.exists()){
            return true;
        }

        FileOutputStream fos = null;
        try {
             fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
//            ToastUtils.toast("save error");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
//            ToastUtils.toast("save error");
            return false;
        }finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
        file.delete();
        return true;
    }

    public  static File getStoreFilePath(String url){
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qca");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        int indexOf = url.lastIndexOf("/");
        String substring = url.substring(indexOf);
        String fileName = substring.trim();
        File file = new File(appDir, fileName);
        Log.e("file",file.getPath());
        Log.e("file_boolean",file.exists()+"");
        if(file.exists()){
            return file;
        }
        return file;
    }

    public  static boolean isStoreFilePath(String url){
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "qca");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        int indexOf = url.lastIndexOf("/");
        String substring = url.substring(indexOf);
        String fileName = substring.trim();
        File file = new File(appDir, fileName);
        Log.e("file",file.getPath());
        Log.e("file_boolean",file.exists()+"");
        return  file.exists();
    }

    public static Uri getImageContentUri(Context context, String imageFilePath) {
        String filePath = imageFilePath;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            File imageFile = new File(imageFilePath);
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public static boolean isLogin(){
      if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID))) {
          return false;
      }
        return true;
    }
    
    public static String parseOdd(String odds){
        String text = "";
        switch (Math.abs(Integer.valueOf(odds))) {
            case 0: text = "0"; break;
            case 2500: text = "0/0.5"; break;
            case 5000: text = "0.5"; break;
            case 7500: text = "0.5/1"; break;
            case 10000: text = "1"; break;
            case 12500: text = "1/1.5"; break;
            case 15000: text = "1.5"; break;
            case 17500: text = "1.5/2"; break;
            case 20000: text = "2"; break;
            case 22500: text = "2/2.5"; break;
            case 25000: text = "2.5"; break;
            case 27500: text = "2.5/3"; break;
            case 30000: text = "3"; break;
            case 32500: text = "3/3.5"; break;
            case 35000: text = "3.5"; break;
            case 37500: text = "3.5/4"; break;
            case 40000: text = "4"; break;
            case 42500: text = "4/4.5"; break;
            case 45000: text = "4.5"; break;
            case 47500: text = "4.5/5"; break;
            case 50000: text = "5"; break;
            case 52500: text = "5/5.5"; break;
            case 55000: text = "5.5"; break;
            case 57500: text = "5.5/6"; break;
            case 60000: text = "6"; break;
            case 62500: text = "6/6.5"; break;
            case 65000: text = "6.5"; break;
            case 67500: text = "6.5/7"; break;
            case 70000: text = "7"; break;
            case 72500: text = "7/7.5"; break;
            case 75000: text = "7.5"; break;
            case 77500: text = "7.5/8"; break;
            case 80000: text = "8"; break;
            case 82500: text = "8/8.5"; break;
            case 85000: text = "8.5"; break;
            case 87500: text = "8.5/9"; break;
            case 90000: text = "9"; break;
            default: text = odds; break;
        }
        if ((Integer.valueOf(odds)) < 0) {
            text = "-" + text;
        }
        return text;
    }

    public static String parseOddOfHandicap(String odds){
        String text = "";
        switch (Math.abs(Integer.valueOf(odds))) {
            case 0: text = "平手"; break;
            case 2500: text = "平半"; break;
            case 5000: text = "半球"; break;
            case 7500: text = "半一球"; break;
            case 10000: text = "一球"; break;
            case 12500: text = "一球球半"; break;
            case 15000: text = "球半"; break;
            case 17500: text = "球半两球"; break;
            case 20000: text = "两球"; break;
            case 22500: text = "两球两球半"; break;
            case 25000: text = "两球半"; break;
            case 27500: text = "两球半三球"; break;
            case 30000: text = "三球"; break;
            case 32500: text = "三球三球半"; break;
            case 35000: text = "三球半"; break;
            case 37500: text = "三球半四球"; break;
            case 40000: text = "四球"; break;
            case 42500: text = "四球四球半"; break;
            case 45000: text = "四球半"; break;
            case 47500: text = "四球半五球"; break;
            case 50000: text = "五球"; break;
            case 52500: text = "五球五球半"; break;
            case 55000: text = "五球半"; break;
            case 57500: text = "五球半六球"; break;
            case 60000: text = "六球"; break;
            case 62500: text = "六球六球半"; break;
            case 65000: text = "六球半"; break;
            case 67500: text = "六球半七球"; break;
            case 70000: text = "七球"; break;
            case 72500: text = "七球七球半"; break;
            case 75000: text = "七球半"; break;
            case 77500: text = "七球半八球"; break;
            case 80000: text = "八球"; break;
            case 82500: text = "八球八球半"; break;
            case 85000: text = "八球半"; break;
            case 87500: text = "八球半九球"; break;
            case 90000: text = "九球"; break;
            default: text = odds; break;
        }
        if ((Integer.valueOf(odds)) < 0) {
            text = "受" + text;
        }
        return text;
    }

    /**
     * 获取当前周几
     * @param dt
     * @return
     */
    public static  String getWeekOfDate(Date dt) {
        List<String> weekList = new ArrayList<>();
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) w = 0;

//        for(int i = 0;i<7;i++){
//            int i1 = Math.abs(w - 6 + i) % 7;
//            weekList.add(weekDays[i]);
//        }

        return weekDays[w];
    }

    /**
     * 获取当前日期倒叙七天
     * @param dt
     * @return
     */
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
    public static SimpleDateFormat simpleDateFormatYY = new SimpleDateFormat("yyyyMMdd");
    public static SimpleDateFormat simpleDateFormatSplitLine = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat simpleDateFormatMMDD = new SimpleDateFormat("MM-dd HH:mm");
    public static SimpleDateFormat simpleDateFormatYYMMDD = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static SimpleDateFormat simpleDateFormatHHMM = new SimpleDateFormat("HH:mm");
    public static SimpleDateFormat simpleDateFormatYear = new  SimpleDateFormat("yyyy");

    public static SimpleDateFormat simpleDateSplit = new  SimpleDateFormat("yy/MM/dd");
    /**
     *
     * @return
     */
    public static List<String> getWeeksDaysBefore(boolean isToday){
        List<String> list = new ArrayList<>();
        int count = 8; //显示今天以前
        if(isToday)count = 7;//包含今天
        for(int i = 1;i<=8;i++){
            Date getdate = getdate(i-count);
            String weekOfDate = getWeekOfDate(getdate);
            String str = weekOfDate+","+simpleDateFormat.format(getdate)+","+simpleDateFormatYY.format(getdate);
            list.add(str);
        }


        return  list;
    }
    /**
     *
     * @return
     */
    public static List<String> getWeeksDaysAfter(){
        List<String> list = new ArrayList<>();
        for(int i = 1;i<=8;i++){
            Date getdate = getdate(i);
            String weekOfDate = getWeekOfDate(getdate);
            String str = weekOfDate+","+simpleDateFormat.format(getdate)+","+simpleDateFormatYY.format(getdate);
            list.add(str);
        }


        return  list;
    }


    public static  Date getdate(int i) // //获取前后日期 i为正数 向后推迟i天，负数时向前提前i天
    {
        Date dat = null;
        Calendar cd = Calendar.getInstance();
        cd.add(Calendar.DATE, i);
        dat = cd.getTime();
        return dat;
    }

    public static String getCurrentYYMMDD(){
        String substring = simpleDateFormatYY.format(new Date()).substring(2);
        return substring;
    }


    public static String parseIntToK(int view_count) {
        try {
            if(view_count <10000)return view_count+"";
            int k = view_count / 10000;
            int b = view_count % 10000 / 1000;
            return k+"."+b+"万";
        }catch ( Exception e){
            return "0.0";
        }

    }

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }

    public static String getMinStart(long time){
        long from = new Date(time).getTime();
        long to = new Date(System.currentTimeMillis()).getTime();
        return  (int) ((to - from)/(1000 * 60))+"";
    }

    public static long getDayLongTime(){
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time=simpleDateFormatSplitLine.format(System.currentTimeMillis())+" 13:00";
        Date date = null;
        try {
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
   public static String getCreateTime(long time){

        return getMatchStartTime(time);

//        String str = "";
//       long from = new Date(time).getTime();
//       long to = new Date(System.currentTimeMillis()).getTime();
////       int days = (int) ((to - from)/(1000 * 60 * 60 * 24));
//       int days = getDayString(new Date(time));
//
//       if(days == -1){
//           str = "昨 "+Utils.simpleDateFormatHHMM.format(time);
//       }else if(days == 0){
//
//           int hourStart = Utils.getHourStart(time);
//           if(hourStart<1){
//               str = Utils.getMinStart(time)+"分钟前";
//           }else if(hourStart<=3){
//               str = hourStart+"小时前";
//           }else {
//               str = Utils.simpleDateFormatHHMM.format(time);
//           }
//
//
//       }else if(days == 1) {
//          str = "明 "+Utils.simpleDateFormatHHMM.format(time);
//       }else {
//           str = Utils.simpleDateFormatMMDD.format(time);
//       }
//
//       return  str;
   }

    public static String getMatchStartTime(String time){
        long lon =  Long.valueOf(time+ "000");
        return getMatchStartTime(lon);

    }
    public static String getMatchStartTime(long time){
        String str = "";
        long from = new Date(time).getTime();
        long to = new Date(System.currentTimeMillis()).getTime();
//       int days = (int) ((to - from)/(1000 * 60 * 60 * 24));
        int days = getDayString(new Date(time));

        if(days == -1){
            str = "昨"+Utils.simpleDateFormatHHMM.format(time);
        }else if(days == 0){

            int hourStart = Utils.getHourStart(time);
//            if(hourStart<1){
//                str = Utils.getMinStart(time)+"分钟前";
//            }else if(hourStart<=3){
//                str = hourStart+"小时前";
//            }else {
                str =Utils.simpleDateFormatHHMM.format(time);
//            }


        }else if(days == 1) {
            str = "明"+Utils.simpleDateFormatHHMM.format(time);
        }else {
            str = Utils.simpleDateFormatMMDD.format(time);
        }

        return  str;
    }

    public static String getDayTommowYesterdy(long time){
        String str = "";
        long from = new Date(time).getTime();
        long to = new Date(System.currentTimeMillis()).getTime();
//       int days = (int) ((to - from)/(1000 * 60 * 60 * 24));
        int days = getDayString(new Date(time));

        if(days == -1){
            str = "昨天";
        }else if(days == 0){

            str = "今天";



        }else if(days == 1) {
            str = "明天";
        }else {
            str = "";
        }

        return  str;
    }


    public static  int  getDayString(Date startDate){
        int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        long today = (System.currentTimeMillis()+offSet)/86400000;
        long start = (startDate.getTime()+offSet)/86400000;
        return (int) (start-today);
    }

    public static  int  getDayString(Date startDate,Date endDate){
       // int offSet = Calendar.getInstance().getTimeZone().getRawOffset();
        int offSet = 0;
        long today = (startDate.getTime()+offSet)/86400000;
        long start = (endDate.getTime()+offSet)/86400000;
        return (int) (start-today);
    }

    public static  int getHourStart(long time){
       long from = new Date(time).getTime();
       long to = new Date(System.currentTimeMillis()).getTime();
       int hours = (int) ((to - from)/(1000 * 60 * 60));
       return hours;
   }

    /*
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String ymd) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(ymd);
        long ts = date.getTime();

        return ts;
    }

    /**
     * 显示是否动图
     * @param url
     * @param imageView
     * @param tv_gif_icon
     * @param size
     */
    public static String showType(String url, ImageView imageView, TextView tv_gif_icon, String size) {
        if(url.endsWith(".gif")){
            tv_gif_icon.setText("动图");
            tv_gif_icon.setVisibility(View.VISIBLE);
        }else{
            tv_gif_icon.setVisibility(View.GONE);
        }

        String[] split = size.split(",");
        if(!url.endsWith(".gif") && split.length>1 ){
            double sizeDouble = Integer.valueOf(split[0])*1.0/Integer.valueOf(split[1]);
            if(sizeDouble<0.4 || sizeDouble>2.5){
                tv_gif_icon.setText("长图");
                tv_gif_icon.setVisibility(View.VISIBLE);
            }else{
                tv_gif_icon.setVisibility(View.GONE);
            }

        }

        //MPEG, MP4
        if(url.endsWith(".mp4") || url.endsWith(".avi") || url.endsWith(".mpeg")){
            tv_gif_icon.setText("视频");
            tv_gif_icon.setVisibility(View.VISIBLE);
        }

        PictureUtils.showImg(url,imageView);
        return url;
    }

    /**
     * 处理变为 高清图
     * @param url
     */
    public static String delUrl(String url) {
            int index = url.lastIndexOf("/");
            if(index != -1){
                String baseUrl = url.substring(0,index+1);
                String substring = url.substring(index + 1);
                boolean s = substring.startsWith("s");
                if(s){
                    String imgUrl = substring.substring(1);
                    url = baseUrl + imgUrl;
                    return url;
                }
            }
        return url;
    }

    public static void scaleImg(FragmentActivity activity, View view, ImageView imageView, String sizeStr) {
        int screenWidth = DisplayUtil.getScreenWidth(activity);
        String[] split = sizeStr.split(",");
        if(split.length>1){
            int width;
            int height;
            double size = Integer.valueOf(split[0])*1.0/Integer.valueOf(split[1]);
            if(size>1){//横图
                height = (int) (screenWidth*0.46);
                width = (int) (height *size);
                double maxWidth = screenWidth * 1.0 * 0.618;
                if(width > maxWidth){
                    width = (int) maxWidth;
                }
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if(layoutParams == null){
                    layoutParams = new AbsListView.LayoutParams(width,height);
                }else{
                    layoutParams.height = height;
                    layoutParams.width = width;
                }

                view.setLayoutParams(layoutParams);
            }else if(size == 1){
//                        width =
                height = (int) (screenWidth*0.46);
                width = (int) height;
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if(layoutParams == null){
                    layoutParams = new AbsListView.LayoutParams(width,height);
                }else{
                    layoutParams.height = height;
                    layoutParams.width = width;
                }
                view.setLayoutParams(layoutParams);
            }else{//长图
                width = (int) (screenWidth*0.46);
                height = (int) (width/1.0 /size);

                double maxHeight = screenWidth * 1.0 * 0.618;
                if(height > maxHeight){
                    height = (int) maxHeight;
                }
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if(layoutParams == null){
                    layoutParams = new AbsListView.LayoutParams(width,height);
                }else{
                    layoutParams.height = height;
                    layoutParams.width = width;
                }
                view.setLayoutParams(layoutParams);
            }

        }else{
           int  height = (int) (screenWidth*0.46);
           int  width = (int) height;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if(layoutParams == null){
                layoutParams = new AbsListView.LayoutParams(width,height);
            }else{
                layoutParams.height = height;
                layoutParams.width = width;
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public static void scaleImgTwo(FragmentActivity activity, View view) {
        int screenWidth = DisplayUtil.getScreenWidth(activity);

            int  height = (int) (screenWidth*0.32);
            int  width = (int) height;
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if(layoutParams == null){
                layoutParams = new AbsListView.LayoutParams(width,height);
            }else{
                layoutParams.height = height;
                layoutParams.width = width;
            }
            view.setLayoutParams(layoutParams);

    }


    /**
     * 计算百分比
     * @param numerator 分子
     * @param denominator 分母
     * @param formatCount 保留个数
     */
    public static String parsePercent(double numerator, double denominator, int formatCount) {

        if(denominator==0){
            return "---";
        }
        double v = numerator / denominator*100;

        return String.format("%."+formatCount+"f",v)+"%";
    }

    /**
     * 计算百分比  默认保留一位
     * @param numerator 分子
     * @param denominator 分母
     */
    public static String parsePercent(double numerator, double denominator) {
        return  parsePercent(numerator,denominator,1);
    }


    /**
     * 解析消息数
     * @param tv
     * @param count
     * @param isPoint
     */
    public static void setMsgCount(TextView tv,int count,boolean isPoint){

            tv.setVisibility(View.VISIBLE);

            if(isPoint && count>1){
                tv.setText("");
                return;
            }

            if(count<1){
                tv.setVisibility(View.GONE);
            }else if(count <2){
                tv.setText("1");
            }else if(count<100){
                tv.setText(count+"");
            }else{
                tv.setText("99+");
            }

    }

    public static void dispatchData(int platform,String id,String token,String name){
        switch (platform){
            case 1://微博
                setWeiboData(id,token,name);
                break;
            case 4://微信
                setWeiXinData(id,token,name);
                break;
            case 7://QQ
                setQQData(id,token,name);
                break;
        }
    }


    public static void setWeiboData(String id,String token,String weiboName){
        SPUtils.putString(QCaApplication.getContext(),Constant.WEOBO_ID,id);
        SPUtils.putString(QCaApplication.getContext(),Constant.WEIBO_TOKEN,token);
        SPUtils.putString(QCaApplication.getContext(),Constant.WEIBO_NAME,weiboName);
    }
    public static void setWeiXinData(String id,String token,String weixinName){
        SPUtils.putString(QCaApplication.getContext(),Constant.WEIXIN_ID,id);
        SPUtils.putString(QCaApplication.getContext(),Constant.WEIXIN_TOKEN,token);
        SPUtils.putString(QCaApplication.getContext(),Constant.WEIXIN_NAME,weixinName);
    }
    public static void setQQData(String id,String token,String qqName){
        SPUtils.putString(QCaApplication.getContext(),Constant.QQ_ID,id);
        SPUtils.putString(QCaApplication.getContext(),Constant.QQ_TOKEN,token);
        SPUtils.putString(QCaApplication.getContext(),Constant.QQ_NAME,qqName);
    }

    public static String getPlatformNameBySortId(int sortId){

        String platformName = "";
        switch (sortId){
            case 1://微博
                platformName = "weibo";
                break;
            case 4://微信
                platformName = "weixin";
                break;
            case 7://QQ
                platformName = "qq";
                break;
        }
        return platformName;
    }


    public static void setPhoneNum(String num) {
        SPUtils.putString(QCaApplication.getContext(),Constant.MOBLIE,num);
    }

    public static boolean isHavePhoneNum(){
        String string = SPUtils.getString(QCaApplication.getContext(), Constant.MOBLIE);
        if(!TextUtils.isEmpty(string)){
            return true;
        }

        return false;
    }



    /**
     * 获取倒计时
     *
     * @param totalSeconds
     */
    public static String getRevertCount(long totalSeconds) {
        long day = 0;
        long hour = 0;
        long minute = 0;
        long second = 0;


        if (totalSeconds > 0) {
            second = totalSeconds;
            if (second >= 60) {
                minute = second / 60;
                second = second % 60;
                if (minute >= 60) {
                    hour = minute / 60;
                    minute = minute % 60;
                    if (hour > 24) {
                        day = hour / 24;
                        hour = hour % 24;
                    }
                }
            }
        }

        System.out.println("初始格式化后——>" + day + "天" + hour + "小时" + minute
                + "分钟" + second + "秒");
        return  day + "-" + hour + "-" + minute;
    }


    public interface OnQQUIdListener{
        public void onOk(String uid,String data);
        public void onError(String msg);
    }
    public static void getQQUID(String token, final OnQQUIdListener qquIdListener) {
        String url = "https://graph.qq.com/oauth2.0/me";
        HttpParams params = new HttpParams();
        params.put("access_token",token);
        params.put("unionid",1);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {


            @Override
            public void onSucces(String string) {
                if(qquIdListener != null){
                    String s = string;
                    String[] split = s.split(":");
                    s = split[split.length - 1];
                    split = s.split("\"");
                    s = split[1];
                    String  unionid = s;
                    qquIdListener.onOk(unionid,string);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                 if(qquIdListener != null){
                     qquIdListener.onError(strMsg);
                 }
            }
        });
    }


    /**
     * 获取两位
     * @param d
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static double getW2(double d){

        double v = d / 10000.0;
        String format = String.format("%.2f", v);

        Double aDouble = Double.valueOf(format);

        return aDouble;
    }


    public static void setOddsEur(TextView top,TextView bottom,double first ,double current){
        double w2 = Utils.getW2(first);
        top.setText(w2+"");

        double w21 = Utils.getW2(current);

        bottom.setText(w21+"");
        if(w21 >w2){
            bottom.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
        }else if(w21<w2){
            bottom.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.green));

        }
    }
}


