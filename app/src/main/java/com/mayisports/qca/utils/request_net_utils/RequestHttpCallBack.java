package com.mayisports.qca.utils.request_net_utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import org.kymjs.kjframe.http.HttpCallBack;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public abstract class RequestHttpCallBack extends HttpCallBack {



    private boolean openDialog;
    private Activity activity;
    public RequestHttpCallBack(Activity activity){
            openDialog = true;

            this.activity = activity;
    }

    public RequestHttpCallBack(){
        openDialog = false;
    }

    private ViewGroup viewGroup;
    private ReLoadListener reLoadListener;
    public RequestHttpCallBack(ViewGroup v,ReLoadListener reLoadListener){
        this.viewGroup = v;
        this.reLoadListener = reLoadListener;
    }

    public interface ReLoadListener{
        void onReload();
    }
    @Override
    public void onPreStart() {
        super.onPreStart();
        if(openDialog){

        }
        if(viewGroup != null){
            viewGroup.setVisibility(View.VISIBLE);
        }


    }


   public static HashMap<String,String> header = new HashMap<>();
    @Override
    public void onSuccess(Map<String, String> headers, byte[] t) {
        super.onSuccess(headers, t);
//        String string = headers.toString();
//        String phpsessid = headers.get("Set-Cookie");
        // PHPSESSID=c276f5551405f35b505c28a0e6bef6fc; path=/; HttpOnly; cookie_id=1515724085;
//        Log.e("sessid",phpsessid+"");
//
//        if(phpsessid != null) {
//            SPUtils.putString(QCaApplication.getContext(), Constant.HEADER,phpsessid);
//
//
//            String[] split = phpsessid.split(";");
//
//
//            for(int i = 0;i<split.length;i++){
//                String[] split1 = split[i].split("=");
//                if(split1.length>1) {
//                    header.put(split1[0].trim(), split1[1].trim());
////                    Log.e("PHPSESSID",header.get("PHPSESSID")+"");
////                    Log.e("cookie_id",header.get("cookie_id")+"");
//
//                    SPUtils.putString(QCaApplication.getContext(), Constant.SESSID,header.get("PHPSESSID")+"");
//                    SPUtils.putString(QCaApplication.getContext(),Constant.COOKIE_ID,header.get("cookie_id")+"");
//                }
//
//
//            }


//            SPUtils.putString(QCaApplication.getContext(), Constant.SESSID,s);
//            SPUtils.putString(QCaApplication.getContext(),Constant.COOKIE_ID,s);
        }

    @Override
    public void onSuccess(String t){
        if(viewGroup != null) {
            viewGroup.setVisibility(View.GONE);
        }
        onSucces(t);
    }

    public abstract void onSucces(String string);

    @Override
    public void onFinish() {
        super.onFinish();
        if(openDialog && activity != null && !activity.isFinishing()){
        }

        activity = null;
    }

    @Override
    public  void onFailure(int errorNo, String strMsg){
//        Toast.makeText(TalentApplication.getContext(),errorNo+strMsg,Toast.LENGTH_LONG).show();
        if(viewGroup != null) {
            viewGroup.setVisibility(View.GONE);
//            viewGroup.getChildAt(0).setVisibility(View.GONE);
//            viewGroup.getChildAt(1).setVisibility(View.VISIBLE);
            if(reLoadListener != null) {
                reLoadListener.onReload();
            }
        }
        onfailure(errorNo,strMsg);
    }

    public abstract void onfailure(int errorNo,String strMsg);

}
