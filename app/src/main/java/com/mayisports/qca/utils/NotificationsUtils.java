package com.mayisports.qca.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.mayi.mayisports.QCaApplication;
import com.mayisports.qca.view.ToastNotificationPopuWindow;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import static com.tencent.open.utils.Global.getPackageName;

/**
 * 通知栏管理类
 * Created by zhangpengju on 2018/4/13.
 */

public class NotificationsUtils {

    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

    @SuppressLint("NewApi")
    public static boolean isNotificationEnabled(Context context) {

        AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo appInfo = context.getApplicationInfo();
        String pkg = context.getApplicationContext().getPackageName();
        int uid = appInfo.uid;

        Class appOpsClass = null;
      /* Context.APP_OPS_MANAGER */
        try {
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod(CHECK_OP_NO_THROW, Integer.TYPE, Integer.TYPE,
                    String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 跳转到应用设置详情
     * @param activity
     */
    public static void toAppDetailSettingActivity(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent();
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", activity.getPackageName());
            intent.putExtra("app_uid", activity.getApplicationInfo().uid);
            activity.startActivity(intent);
        } else if (android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivity(intent);
        }
    }


    /**
     * 判断是否开启通知权限，并自动跳转
     */
    public static ToastNotificationPopuWindow toastNotificationPopuWindow = null;
    public static void checkNotificationAndStartSetting(final Activity activity, View view){


        boolean notificationEnabled = isNotificationEnabled(activity.getApplicationContext());
        if(!notificationEnabled){


            String string = SPUtils.getString(QCaApplication.getContext(), Constant.PRE_NOTIFICATION_TIME);

            if(!TextUtils.isEmpty(string)) {
                int dayCount = Utils.getDayString(new Date(Long.valueOf(string)));
                if(dayCount<8){
                    return;
                }
            }

            SPUtils.putString(QCaApplication.getContext(),Constant.PRE_NOTIFICATION_TIME,System.currentTimeMillis()+"");

            toastNotificationPopuWindow = new ToastNotificationPopuWindow(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toAppDetailSettingActivity(activity);
                    toastNotificationPopuWindow.dismiss();
                    toastNotificationPopuWindow = null;
                }
            },"开启推送");
            toastNotificationPopuWindow.showAtLocation(view, Gravity.CENTER,0,0);
        }


    }


}
