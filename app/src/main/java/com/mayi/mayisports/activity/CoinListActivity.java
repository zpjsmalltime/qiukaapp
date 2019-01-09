package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.CoinListBean;
import com.mayisports.qca.bean.SysMsgBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.ta.utdid2.android.utils.NetworkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.Date;

/**
 * 金币明细界面
 */
public class CoinListActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,CoinListActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_coin_list;
    }

    private XRefreshView xfv_sys_msg;
    private ListView lv_sys_msg;
    private RelativeLayout rl_load_layout;
    private MyAdapter myAdapter = new MyAdapter();
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

        tv_title.setText("金币明细");
        tv_ritht_title.setVisibility(View.INVISIBLE);

        xfv_sys_msg = findViewById(R.id.xfv_sys_msg);
        xfv_sys_msg.setPullRefreshEnable(true);
        xfv_sys_msg.setXRefreshViewListener(new MyXListener());
        lv_sys_msg = findViewById(R.id.lv_sys_msg);
        rl_load_layout = findViewById(R.id.rl_load_layout);

        ll_no_data = findViewById(R.id.ll_no_data);

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        });

        addHeader();


    }

    private void addHeader() {
        View view = View.inflate(QCaApplication.getContext(),R.layout.coin_list_head,null);
        lv_sys_msg.addHeaderView(view);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        requestNetDatas();
    }

    private int page;

    @Override
    public void onReload() {

    }


    class MyXListener extends XRefreshView.SimpleXRefreshListener{

        @Override
        public void onRefresh(boolean isPullDown) {
            page = 0;
            rl_load_layout = null;
            requestNetDatas();
        }
    }

    private CoinListBean sysMsgBean;
    private void requestNetDatas() {
        //https://app.mayisports.com/php/api.php?action=user&type=user_capital_detail&start=0
        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action", "user");
        params.put("type", "user_capital_detail");
        params.put("start", page);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout, this) {
            @Override
            public void onSucces(String string) {
                if (page == 0) {
                    sysMsgBean = JsonParseUtils.parseJsonClass(string, CoinListBean.class);
                    if (sysMsgBean != null) {
                        pre_page = 1;
                        lv_sys_msg.setAdapter(myAdapter);
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                    }
                } else {
                    CoinListBean newBean = JsonParseUtils.parseJsonClass(string, CoinListBean.class);
                    try {
                        if (newBean.data.size() > 0) {
                            sysMsgBean.data.addAll(newBean.data);
                        } else {
                            pre_page = 0;
                        }
                    } catch (Exception e) {
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
                if (xfv_sys_msg != null) {
                    xfv_sys_msg.stopRefresh();
                }

                if(page == 0){
                    if(NetworkUtils.isConnected(QCaApplication.getContext())){
                        ll_net_error.setVisibility(View.GONE);
                    }else{
                        ll_net_error.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


    private int pre_page;
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
           if(sysMsgBean != null && sysMsgBean.data != null){
               int size = sysMsgBean.data.size();
               if(size == 0){
                   ll_no_data.setVisibility(View.VISIBLE);
                   return 0;
               }
               return sysMsgBean.data.size()+1;
           }
           ll_no_data.setVisibility(View.VISIBLE);
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

            if(position == sysMsgBean.data.size()){
                convertView = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = convertView.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                return convertView;
            }

            MyHolder myHolder;
            if(convertView == null || convertView.getTag() == null){
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(), R.layout.coin_list_item,null);
                myHolder.tv_title = convertView.findViewById(R.id.tv_title);
                myHolder.tv_date = convertView.findViewById(R.id.tv_date);
                myHolder.tv_count = convertView.findViewById(R.id.tv_count);
                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }

            CoinListBean.DataBean dataBean = sysMsgBean.data.get(position);
            myHolder.tv_title.setText(dataBean.title);
            String date = Utils.simpleDateFormatYYMMDD.format(new Date(Long.valueOf(dataBean.create_time + "000")));
            myHolder.tv_date.setText(date);

            if(dataBean.coin.contains("-")){
                myHolder.tv_count.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.black_28));
                myHolder.tv_count.setText(dataBean.coin);
            }else{
                myHolder.tv_count.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.red_));
                myHolder.tv_count.setText("+"+dataBean.coin);

            }

            if(position == getCount()-3 || getCount()<3){
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

        public TextView tv_title;
        public TextView tv_date;
        public TextView tv_count;

    }

}
