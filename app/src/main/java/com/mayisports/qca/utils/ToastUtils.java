package com.mayisports.qca.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.mayi.mayisports.QCaApplication;

/**
 * 提示框 工具类
 */
public class ToastUtils {

    private static Toast toastObj;
    private static boolean isEnable;
    public static void toast(String toast){
        if(toastObj != null){
           toastObj.cancel();
        }
        toastObj = Toast.makeText(QCaApplication.getContext(), toast, Toast.LENGTH_SHORT);
        if(isEnable) {
            toastObj.show();
        }
    }

    public static void cancleToast(){
        if(toastObj != null)toastObj.cancel();
        isEnable = false;
    }
    public static void enableToast(){
        isEnable = true;
    }


    /**
     * 无状态 提示 不会根据提示消息
     * @param toast
     */
    public static void toastNoStates(String toast){

       Toast.makeText(QCaApplication.getContext(), toast, Toast.LENGTH_SHORT).show();

    }
}
