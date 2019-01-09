package com.mayisports.qca.utils.request_net_utils;

/**
 * Created by Zpj on 2017/12/5.
 */

public interface HttpCallBack<T> {


        void onSuccess(T o);

        void onFailure(int errorNo, String strMsg);

}
