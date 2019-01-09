package com.mayisports.qca.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayi.mayisports.activity.WebViewActivtiy;
import com.mayisports.qca.bean.LiveURlBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 直播选项提示框
 * Created by Zpj on 2017/12/14.
 */

public class LiveSelectPopuWindow extends PopupWindow implements View.OnClickListener {


    private TextView tv_content_toast_price;
    private TextView tv_cancle_toast_price;
    private LinearLayout top;

    private List<String> datas ;

    private Activity activity;
    public LiveSelectPopuWindow(Activity paramActivity, List<String> datas){
        this.activity = paramActivity;
        this.datas = datas;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_live, null);
        tv_content_toast_price = view.findViewById(R.id.tv_content_toast_price);
        tv_cancle_toast_price = view.findViewById(R.id.tv_cancle_toast_price);
        top = view.findViewById(R.id.top);





        if(datas != null){
            delDatas(datas);

            int j = 0;

            for(int i = 0;i<datas.size();i++){
                String s = datas.get(i);
                String[] split = s.split("-");
                if(!"0".equals(split[1])) {
                    ViewGroup childAt = (ViewGroup) top.getChildAt(i + 2);
                    childAt.setVisibility(View.VISIBLE);
                    j ++;
                    TextView textView = (TextView) childAt.getChildAt(0);
                    textView.setText(split[0]);
                    textView.setTag(split[1]);
                    textView.setOnClickListener(this);
                }


            }



            for(int i = 0;i<datas.size();i++){
                String s = datas.get(i);
                String[] split = s.split("-");




                if("0".equals(split[1])){

                    j++;
                    ViewGroup childAt = (ViewGroup) top.getChildAt(j + 2);
                    childAt.setVisibility(View.VISIBLE);




                    TextView textView = (TextView) childAt.getChildAt(0);
                    textView.setText(split[0]);
                    textView.setTag(split[1]);
                    textView.setOnClickListener(this);
                    textView.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.coment_black_99));
                    textView.setOnClickListener(null);

                    String str = "以下平台同时直播本场比赛\n"+split[0];
                    SpannableString spannableString = new SpannableString(str);
                    RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.8f);
                    spannableString.setSpan(sizeSpan,0,12, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    textView.setText(spannableString);
                    textView.setTextSize(13);

                }

            }
        }


//        tv_content_toast_price.setText(title);
        setFocusable(true);
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_cancle_toast_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    /**
     * 处理直播link 为空情况
     * @param datas
     */
    private void delDatas(List<String> datas) {
        List<Integer> positionList = new ArrayList<>();
        for(int i = 0;i<datas.size();i++){
            String[] split = datas.get(i).split("-");
            if("0".equals(split[1])){
                positionList.add(i);
            }
        }



        for(int j = 0;j<positionList.size();j++){
            String remove = datas.remove(j);
            datas.add(remove);
        }

    }


    @Override
    public void onClick(View v) {
       String id = (String) v.getTag();

        WebViewActivtiy.start(activity,id,"直播",true);
        dismiss();

//        String url = Constant.BASE_URL + "php/api.php";//htcom/php/api.php?action=match&type=gettvlink_byid&id=2149
//        HttpParams params = new HttpParams();
//        params.put("action","match");
//        params.put("type","gettvlink_byid");
//        params.put("id",id);
//        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
//            @Override
//            public void onSucces(String string) {
//                LiveURlBean liveURlBean = JsonParseUtils.parseJsonClass(string,LiveURlBean.class);
//                String url = liveURlBean.data.tvlink;
////                Intent intent= new Intent();
////                intent.setAction("android.intent.action.VIEW");
////                Uri content_url = Uri.parse(url);
////                intent.setData(content_url);
////                activity.startActivity(intent);
//                WebViewActivtiy.start(activity,url,"直播",true);
//                dismiss();
//            }
//
//            @Override
//            public void onfailure(int errorNo, String strMsg) {
//                ToastUtils.toast(strMsg);
//            }
//        });



    }
}
