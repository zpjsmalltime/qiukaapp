package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.FansListBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.kymjs.kjframe.http.HttpParams;


/**
 * 粉丝列表页
 */
public class FansListActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,FansListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_fans_list;
    }

    private ListView lv_fans_list_ac;
    private RelativeLayout rl_load_layout;
    private LinearLayout ll_no_data;



    private LinearLayout ll_net_error;
    private TextView tv_refresh_net;


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
        tv_title.setText("我的粉丝");
        tv_ritht_title.setVisibility(View.INVISIBLE);

        lv_fans_list_ac = findViewById(R.id.lv_fans_list_ac);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        ll_no_data = findViewById(R.id.ll_no_data);

        lv_fans_list_ac.setOnItemClickListener(this);

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        });

    }


    @Override
    protected void initDatas() {
        super.initDatas();
        page = 0;
        requestNetDatas();
    }

    private int page;
    private int pre_page;
    private FansListBean fansListBean;
    private void requestNetDatas() {
        //http://app.mayisports.com/php/api.php?action=user&type=followers_list&user_id=71640&start=0
        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","followers_list");
        params.put("user_id", SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID));
        params.put("start",page);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                if (page == 0){
                    fansListBean = JsonParseUtils.parseJsonClass(string, FansListBean.class);
                    pre_page = 1;
                    lv_fans_list_ac.setAdapter(myAdapter);
                }else{
                    try{
                        FansListBean newBean = JsonParseUtils.parseJsonClass(string, FansListBean.class);
                        fansListBean.data.list.addAll(newBean.data.list);
                    }catch (Exception e){
                        pre_page = 0;
                    }
                    myAdapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);

                if(page == 0){
                    if(NetworkUtils.isConnected(QCaApplication.getContext())){
                        if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                    }else{
                        if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
                    }
                }

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FansListBean.DataBean.ListBean listBean = fansListBean.data.list.get(position);
        String json = new Gson().toJson(listBean);
        PersonalDetailActivity.start(this,listBean.user_id,json);
    }

    @Override
    public void onReload() {

    }


    private MyAdapter myAdapter = new MyAdapter();
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(fansListBean != null && fansListBean.data != null && fansListBean.data.list != null){
                int size = fansListBean.data.list.size();
                if(size == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                }else {
                    ll_no_data.setVisibility(View.GONE);
                }
                return size;
            }
            ll_no_data.setVisibility(View.VISIBLE);
            return 0;
        }

        @Override
        public FansListBean.DataBean.ListBean getItem(int position) {
           return  fansListBean.data.list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if(convertView == null){
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.fans_list_item,null);
                myHolder.iv_head_dynamic_item = convertView.findViewById(R.id.iv_head_dynamic_item);
                myHolder.iv_vip_header = convertView.findViewById(R.id.iv_vip_header);
                myHolder.tv_name_dynamic_item = convertView.findViewById(R.id.tv_name_dynamic_item);
                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }

            FansListBean.DataBean.ListBean item = getItem(position);

            PictureUtils.showCircle(item.headurl,myHolder.iv_head_dynamic_item);

            if(!TextUtils.isEmpty(item.verify_type)&&Integer.valueOf(item.verify_type)>0){
                myHolder.iv_vip_header.setVisibility(View.VISIBLE);
            }else {
                myHolder.iv_vip_header.setVisibility(View.GONE);
            }

            myHolder.tv_name_dynamic_item.setText(item.nickname);

            if(position == getCount()-3||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestNetDatas();
                }
            }
            return convertView;
        }
    }
    static class MyHolder {
        public ImageView iv_head_dynamic_item;
        public ImageView iv_vip_header;
        public TextView tv_name_dynamic_item;
    }
}
