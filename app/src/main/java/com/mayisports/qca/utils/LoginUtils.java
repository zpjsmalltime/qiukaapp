package com.mayisports.qca.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.activity.LoginActivity;
import com.tencent.open.utils.Util;

import org.android.agoo.ut.UT;

import java.util.UUID;

/**
 * Created by Zpj on 2017/12/26.
 */

public class LoginUtils {

    public static void goLoginActivity(Activity activity,String action){
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.putExtra(LoginActivity.ACTION,action);
        activity.startActivity(intent);
    }


    public static void saveLoginTags(String id,String token){
        SPUtils.putString(QCaApplication.getContext(),Constant.USER_ID,id);
        SPUtils.putString(QCaApplication.getContext(),Constant.TOKEN,token);
    }

    public static void removeLoginTags(){
        SPUtils.putString(QCaApplication.getContext(),Constant.USER_ID,"");
        SPUtils.putString(QCaApplication.getContext(),Constant.TOKEN,"");
        SPUtils.putString(QCaApplication.getContext(),Constant.SEARCH_HISTORY,"");
        SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,false);
        SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,false);
        SPUtils.putInt(QCaApplication.getContext(),Constant.HOME_SELECT,0);
        SPUtils.putString(QCaApplication.getContext(),Constant.NICK_NAME,"");
        SPUtils.putString(QCaApplication.getContext(),Constant.V_TYPE,"");

        Utils.setQQData("","","");
        Utils.setWeiXinData("","","");
        Utils.setWeiboData("","","");
        Utils.setPhoneNum("");


        SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_IMG,"");
        SPUtils.putString(QCaApplication.getContext(),Constant.BANNER_URL,"");
    }

    public static boolean isLogin(){
        if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID))) {
            return false;
        }
        return true;
    }

    public static boolean isUnLogin(){
        if(TextUtils.isEmpty(SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID))) {
            return true;
        }
        return false;
    }


    /**
     * 当前用户id是否为自己
     * @param userId
     * @return
     */
    public static boolean isUserSelf(String userId){
        if(userId.equals(SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID))) {
            return true;
        }
        return false;
    }
}
