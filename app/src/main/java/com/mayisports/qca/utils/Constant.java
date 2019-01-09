package com.mayisports.qca.utils;

import android.content.Context;
import android.os.Build;
import android.os.Environment;

import com.mayi.mayisports.BuildConfig;
import com.mayi.mayisports.QCaApplication;
import com.mayisports.qca.bean.ComentsDetailBean;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.ImmediateScoreBean;
import com.mayisports.qca.bean.MainMetaBean;

import org.android.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一些配置信息  常量
 * Created by Zpj on 2017/12/5.
 */

public class Constant {






    //竞猜大赛入口，动态数据
    public static final String BANNER_IMG = "banner_img";


    //推送设备号
    public static final String DEV_PUSH_ID = "dev_push_id";
    public static final String BANNER_URL = "banner_url";
    public static final String MSG_TOKEN = "msg_token";

    //主界面中心活动图入口url
    public static final String MID_URL = "mid_url";
    public static final String PRE_TOAST_TIME = "pre_toast_time";
    public static final String MAIN_PAGE = "main_page";

    //加载更多提示
    public static final String  LOAD_BOTTOM_TOAST = "已经到底了";
    public static final String REPLY_SHARE = "reply_share";


    public static boolean isSee = false;

    public static final String WEIBO_TOKEN = "weibo_token";
    public static final String WEOBO_ID = "weibo_id";
    public static final String WEIBO_NAME = "weibo_name";
    public static final String PRE_NOTIFICATION_TIME = "pre_notification_time";
    public static final String WEIXIN_ID = "weixin_id";
    public static final String WEIXIN_TOKEN = "weixin_token";
    public static final String WEIXIN_NAME = "weixin_name";
    public static final String QQ_ID = "qq_id";
    public static final String QQ_TOKEN = "qq_token";
    public static final String QQ_NAME = "qq_name";
    public static final String MOBLIE = "moblie";
    /**
     * 短视频共享数据
     */
    public static DynamicBean dynamicBean = null;



    public static final String IMG_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()+"/qiuca/";

    public static final String NET_FAIL_TOAST = "获取数据失败";

    public static final String Match_Thread_Interval = "match_thread_interval";
    public static final String Banner_Interval = "banner_interval";
    public static final String Sys_Msg_Interval = "sys_msg_interval;";
    public static final String HOME_PAGE_CACHE = "HOME_PAGE_CACHE";
    public static final String SCORE_PAGE_CACHE = "SCORE_PAGE_CACHE";
    public static final String MINE_PAGE_CACHE = "MINE_PAGE_CACHE";
    public static final String DYNAMIC_PAGE_CACHE = "DYNAMIC_PAGE_CACHE";
    public static final String MAIN_QCA_CACHE = "MAIN_QCA_CACHE";


    public static final String APP_ID_WEPAY ="wx44083f44d242cceb";//wxd930ea5d5a258f4f


    public static final String IS_FIRST = "isFirst";
    public static final String COIN = "COIN";
    public static final String WITHDRAW_COIN = "withdraw_coin";
    public static final String TYPE = "type_select_ac";
    public static final String SESSID = "sessid";
    public static final String COOKIE_ID = "cookie_id";
    public static final String HEADER = "set_cookie";
    /**
     * 比赛状态
     */
    public static final String MATCH_NO_START = "NO_START";
    public static final String MATCH_FIRST_HALF = "FIRST_HALF";
    public static final String MATCH_HALF_TIME = "HALF_TIME";
    public static final String MATCH_SECOND_HALF = "SECOND_HALF";
    public static final String MATCH_COMPLETE = "COMPLETE";
    public static final String MATCH_ABORTION = "ABORTION";
    public static final String MATCH_DELAY = "DELAY";
    public static final String MATCH_INTERRUPT = "INTERRUPT";
    public static final String SEARCH_HISTORY = "SEARCH_HISTORY";
    public static final String FOOTBALL = "FOOTBALL";
    public static final String LOTTERY = "LOTTERY";
    public static final String V_TYPE = "verify_type";
    public static final String HOME_SELECT = "home_select";
    public static final String NICK_NAME = "nick_name";


    public static ImmediateScoreBean.DataBean.MatchlistBean matchlistBean;//比赛详情界面数据传递

    public static final String VERSION = "/v1.0/";
    public static final String BASE_URL = BuildConfig.URL_HOST;


    public static final String APP_ID_BUGLY = "03c30b2581";

    public static final String saltStr = "";//md5 加密秘钥
    public static final String USER_ID = "userId";
    public static final String TOKEN = "token";



    public static String platform = BuildConfig.APPSTORE;
    public static String HEADER_URl = "HEADER_URl";
    public static String commentBeanJson;



    public static String getUrl() {
//        return "http://app.mayisports.com/?source='android'&version='4.0'&network="
//                + PhoneInfo.getInstance(this).getNetworkType()
//                + "&uuid=" + PhoneInfo.getInstance(this).getMyUUID()
//                + "&sysversion=" + Build.VERSION.RELEASE;



        return  "?source=android&version="+BuildConfig.VERSION_NAME+"&network="
                + PhoneInfo.getInstance(QCaApplication.getContext()).getNetworkType()
                + "&uuid=" + PhoneInfo.getInstance(QCaApplication.getContext()).getMyUUID()
                + "&sysversion=" + Build.VERSION.RELEASE
                + "&platform=MMY&appstore="+platform;

    }

    public static String getParams(){
        return "&source=android&version="+ BuildConfig.VERSION_NAME+"&network="
                + PhoneInfo.getInstance(QCaApplication.getContext()).getNetworkType()
                + "&uuid=" + PhoneInfo.getInstance(QCaApplication.getContext()).getMyUUID()
                + "&sysversion=" + Build.VERSION.RELEASE
                + "&platform=MMY&appstore="+platform;
    }


}
