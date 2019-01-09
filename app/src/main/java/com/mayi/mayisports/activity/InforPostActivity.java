package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.InforPostBean;
import com.mayisports.qca.utils.BindViewUtils;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.List;

/**
 * 情报站 界面
 */
public class InforPostActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener, AdapterView.OnItemClickListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,InforPostActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_infor_post;
    }

    private ListView lv_infor_post_activity;
    private ImageView iv_header;
    private MyAdapter myAdapter = new MyAdapter();
    private ViewGroup rl_load_layout;

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
        tv_title.setText("情报站");


        rl_load_layout = findViewById(R.id.rl_load_layout);
        lv_infor_post_activity = findViewById(R.id.lv_infor_post_activity);


        View view = View.inflate(this,R.layout.img_header_layout,null);
        iv_header = view.findViewById(R.id.iv_header);
        lv_infor_post_activity.addHeaderView(view);
        lv_infor_post_activity.setAdapter(myAdapter);

        lv_infor_post_activity.setOnItemClickListener(this);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        requestNetDatas();
    }

    private InforPostBean inforPostBean;
    private void requestNetDatas() {
        //htt://app.mayisports.com/php/api.php?action=match&type=get_match_information_list
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match");
        params.put("type","get_match_information_list");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                inforPostBean = JsonParseUtils.parseJsonClass(string,InforPostBean.class);
//                String imgUrl = "http://app.mayisports.com/static/img/banner.12fe00d.png";
//                PictureUtils.show(imgUrl,iv_header);
                InforPostBean beanNew = JsonParseUtils.parseJsonClass(string, InforPostBean.class);
                delData(InforPostActivity.this.inforPostBean,beanNew);
                lv_infor_post_activity.setAdapter(myAdapter);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    /**
     * 拆分
     * @param inforPostBean
     * @param beanNew
     */
    private int oldIndex;
    private void delData(InforPostBean inforPostBean, InforPostBean beanNew) {
         if(inforPostBean == null || inforPostBean.data == null || inforPostBean.data.matchInfoList == null)return;
        List<InforPostBean.DataBean.MatchInfoListBean> infoList = inforPostBean.data.matchInfoList;
        infoList.clear();

        for(int i = 0;i<beanNew.data.matchInfoList.size();i++){

            InforPostBean.DataBean.MatchInfoListBean bean = beanNew.data.matchInfoList.get(i);
            if("NO_START".equals(bean.match.status) || "FIRST_HALF".equals(bean.match.status) || "HALF_TIME".equals(bean.match.status) || "SECOND_HALF".equals(bean.match.status)){
                infoList.add(bean);
            }
        }
         oldIndex = infoList.size();
        for(int i = 0;i<beanNew.data.matchInfoList.size();i++){

            InforPostBean.DataBean.MatchInfoListBean bean = beanNew.data.matchInfoList.get(i);
            if("NO_START".equals(bean.match.status) || "FIRST_HALF".equals(bean.match.status) || "HALF_TIME".equals(bean.match.status) || "SECOND_HALF".equals(bean.match.status)){

            }else{
                infoList.add(bean);
            }
        }


    }

    @Override
    public void onReload() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)return;
        InforPostBean.DataBean.MatchInfoListBean bean = inforPostBean.data.matchInfoList.get(position-1);
        Intent intent = new Intent(this, MatchDetailActivity.class);
        intent.putExtra("betId",bean.match.betId);
        intent.putExtra("type",2);
        startActivity(intent);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(inforPostBean != null && inforPostBean.data != null && inforPostBean.data.matchInfoList != null){
                int size = inforPostBean.data.matchInfoList.size();
                return size;
            }
            return 0;
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
            Holder holder ;
            if(convertView == null){
                holder = new Holder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.infor_post_item,null);
                holder.tv_vs_item_home = convertView.findViewById(R.id.tv_vs_item_home);
                holder.tv_tag_1 = convertView.findViewById(R.id.tv_tag_1);
                holder.tv_tag_2 = convertView.findViewById(R.id.tv_tag_2);
                holder.tv_view_count_home = convertView.findViewById(R.id.tv_view_count_home);
                holder.ll_old_title = convertView.findViewById(R.id.ll_old_title);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            InforPostBean.DataBean.MatchInfoListBean bean = inforPostBean.data.matchInfoList.get(position);

            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            SpannableString vs = new SpannableString(bean.match.hostTeamName+" vs "+bean.match.awayTeamName+" ");
            spannableStringBuilder.append(vs);
            String time = BindViewUtils.simpleDateFormat.format(Long.valueOf(bean.match.match_time+"000"));
            SpannableString leaguName = new SpannableString(bean.match.leagueName + " · " +time);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#BFBFBF"));
            leaguName.setSpan(foregroundColorSpan,0,leaguName.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(0.7f);
            leaguName.setSpan(relativeSizeSpan,0,leaguName.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(leaguName);

            holder.tv_vs_item_home.setText(spannableStringBuilder);


            holder.tv_tag_1.setVisibility(View.GONE);
            holder.tv_tag_2.setVisibility(View.GONE);
            if(bean.infoList != null){
                        try {
                            for (int j = 0; j < bean.infoList.size(); j++) {
                                if (j > 1) break;
                                String title = bean.infoList.get(j).title;
                                int i1 = title.indexOf("【");
                                int i2 = title.indexOf("】");
                                String substring = title.substring(i1 + 1, i2);
                                if(j == 0) {
                                    holder.tv_tag_1.setText(substring);
                                    holder.tv_tag_1.setVisibility(View.VISIBLE);
                                }

                                if(j == 1) {
                                    holder.tv_tag_2.setText(substring);
                                    holder.tv_tag_2.setVisibility(View.VISIBLE);
                                }
                            }
                        }catch (Exception e){

                        }
                    }

                  String count =   Utils.parseIntToK(bean.view_count);
                  holder.tv_view_count_home.setText(count);


                  if(oldIndex == position || oldIndex == -1){
                      holder.ll_old_title.setVisibility(View.VISIBLE);
                  }else{
                      holder.ll_old_title.setVisibility(View.GONE);
                  }

            return convertView;
        }
    }

    static class Holder {
        public TextView tv_vs_item_home;
        public TextView tv_tag_1;
        public TextView tv_tag_2;
        public TextView tv_view_count_home;
        public LinearLayout ll_old_title;
    }
}
