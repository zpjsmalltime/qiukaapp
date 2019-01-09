package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.ReportSubmitBean;
import com.mayisports.qca.bean.SubmitGroomBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 举报界面
 */
public class ReportActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener {

   public static void start(Activity activity,String reportUserId,String reportUserName,String publishTime){
       Intent intent = new Intent(activity,ReportActivity.class);
       intent.putExtra("reportUserId",reportUserId);
       intent.putExtra("reportUserName",reportUserName);
       intent.putExtra("publishTime",publishTime);
       activity.startActivity(intent);
   }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_report;
    }


    private RelativeLayout rl_load_layout;
    private TextView tv_name_report_activity;
    private GridView gv_content_report_activity;
    private TextView tv_submit_report_activity;
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("举报");

        rl_load_layout = findViewById(R.id.rl_load_layout);
        tv_name_report_activity = findViewById(R.id.tv_name_report_activity);
        gv_content_report_activity = findViewById(R.id.gv_content_report_activity);
        tv_submit_report_activity = findViewById(R.id.tv_submit_report_activity);
        tv_submit_report_activity.setOnClickListener(this);
    }


    private List<String> itemTitles;
    private String reportUserId;
    private String publishTime;
    private String reportUserName;
    @Override
    protected void initDatas() {
        super.initDatas();

        itemTitles = new ArrayList<>();
        itemTitles.add("垃圾广告营销");
        itemTitles.add("不实信息");
        itemTitles.add("违法有害信息");
        itemTitles.add("抄袭内容");
        itemTitles.add("淫秽色情内容");
        itemTitles.add("人身攻击");

        gv_content_report_activity.setAdapter(myGvAdapter);
        gv_content_report_activity.setOnItemClickListener(this);


        reportUserId = getIntent().getStringExtra("reportUserId");
        reportUserName =   getIntent().getStringExtra("reportUserName");
        try {
            publishTime = getIntent().getStringExtra("publishTime");

            Long aLong = Long.valueOf(publishTime + "000");
            publishTime =   Utils.simpleDateFormatYYMMDD.format(new Date(aLong));

        }catch (Exception e){

        }


//        tv_name_report_activity.setText("举报"+reportUserName+"发布的内容");

        SpannableString spannableString = new SpannableString("举报 "+reportUserName+" 发布的内容");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#4F698D"));
        spannableString.setSpan(foregroundColorSpan,3,3+reportUserName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv_name_report_activity.setText(spannableString);



    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId() == R.id.tv_submit_report_activity){
            submitDatas();
        }
    }


    private void submitDatas() {
        //php/api.php?action=user&type=feedback&value=用户: 202369 举报 用户: 200327 发布时间: 1519867275的内容,举报理由: 不实信息
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","feedback");
        String value = "用户:"+ SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID)+" 举报了 用户: "+reportUserId+" 发布时间: "+publishTime+"的内容,举报理由: "+itemTitles.get(selectPosition);
        value = URLEncoder.encode(value);
        params.put("value",value);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                ReportSubmitBean reportSubmitBean = JsonParseUtils.parseJsonClass(string,ReportSubmitBean.class);
                if(reportSubmitBean.status.result == 1){
                    ToastUtils.toast("举报成功");
                    finish();
                }else{
                    ToastUtils.toast("举报失败，请重试");
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST+errorNo);
            }
        });
    }

    private MyGvAdapter myGvAdapter = new MyGvAdapter();

    private int selectPosition;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == selectPosition)return;
        selectPosition = position;
        myGvAdapter.notifyDataSetChanged();
    }

    @Override
    public void onReload() {

    }

    class MyGvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemTitles.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(QCaApplication.getContext(),R.layout.report_item,null);
            TextView tv_title_item = convertView.findViewById(R.id.tv_title_item);
            ImageView iv_select_coin = convertView.findViewById(R.id.iv_select_coin);

            if(position == selectPosition){
                iv_select_coin.setImageDrawable(getResources().getDrawable(R.drawable.choose_check));
            }else {
                iv_select_coin.setImageDrawable(getResources().getDrawable(R.drawable.choose_uncheck));
            }

            String text = itemTitles.get(position);
            tv_title_item.setText(text);


            return convertView;
        }
    }



}
