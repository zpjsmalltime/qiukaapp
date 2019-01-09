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

/**
 * 系统消息界面
 */
public class SystemMsgActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener, AdapterView.OnItemClickListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,SystemMsgActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_system_msg;
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

        ll_net_error = findViewById(R.id.ll_net_error);
        tv_refresh_net = findViewById(R.id.tv_refresh_net);
        tv_refresh_net.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDatas();
            }
        });


        tv_title.setText("系统消息");
        tv_ritht_title.setVisibility(View.INVISIBLE);

        xfv_sys_msg = findViewById(R.id.xfv_sys_msg);
        xfv_sys_msg.setPullRefreshEnable(true);
        xfv_sys_msg.setXRefreshViewListener(new MyXListener());
        lv_sys_msg = findViewById(R.id.lv_sys_msg);
        rl_load_layout = findViewById(R.id.rl_load_layout);

//        lv_sys_msg.setAdapter(myAdapter);
        lv_sys_msg.setOnItemClickListener(this);

        ll_no_data = findViewById(R.id.ll_no_data);

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SysMsgBean.DataBean bean = sysMsgBean.data.get(position);
        switch (bean.type){
            case "2":///packageDetail/' + itemId
                WebViewActivtiy.start(this,Constant.BASE_URL+"#/packageDetail/"+ bean.itemId,"球咖");
                break;
            case "3":// /quizContest
                WebViewActivtiy.start(this,Constant.BASE_URL+"#/quizContest","球咖");
                break;
            case "5"://比赛详情页
                MatchDetailActivity.start(this,bean.itemId);
                break;
            case "6"://个人主页
            case "7":
                String json = null;
                PersonalDetailActivity.start(this,bean.itemId,json);
                break;
            case "10"://话题评论详情
                ComentsDetailsActivity.start(this,bean.itemId);
                break;
            case "11"://话题页
                TopicDetailActivity.start(this,bean.itemId);
                break;
            case "12"://粉丝页 网址
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/fansAndView/"+ SPUtils.getString(this,Constant.USER_ID)+",fans","粉丝");
                FansListActivity.start(this);
                break;
            case "13"://
                WebViewActivtiy.start(this,Constant.BASE_URL+"#/topicList","话题广场");
                break;
            case "15"://来访页 网址
                WebViewActivtiy.start(this,Constant.BASE_URL+"#/fansAndView/"+SPUtils.getString(this,Constant.USER_ID)+",view","访客");
                break;

        }
    }

    class MyXListener extends XRefreshView.SimpleXRefreshListener{

        @Override
        public void onRefresh(boolean isPullDown) {
            page = 0;
            rl_load_layout = null;
            requestNetDatas();
        }
    }

    private SysMsgBean sysMsgBean;
    private void requestNetDatas() {
        //http://php/api.php?action=user&type=msg&start=0&v=1516760342
        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action", "user");
        params.put("type", "msg");
        params.put("start", page);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout, this) {
            @Override
            public void onSucces(String string) {
                if (page == 0) {
                    sysMsgBean = JsonParseUtils.parseJsonClass(string, SysMsgBean.class);
//
                    if (sysMsgBean != null) {
                        pre_page = 1;
//                        myAdapter.notifyDataSetChanged();
                        lv_sys_msg.setAdapter(myAdapter);
                    }else{
                        ll_no_data.setVisibility(View.VISIBLE);
                    }
                } else {
                    SysMsgBean newBean = JsonParseUtils.parseJsonClass(string, SysMsgBean.class);
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
                        if(ll_net_error != null)ll_net_error.setVisibility(View.GONE);
                    }else{
                        if(ll_net_error != null)ll_net_error.setVisibility(View.VISIBLE);
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
                convertView = View.inflate(QCaApplication.getContext(), R.layout.sys_msg_item,null);
                myHolder.tv_content_sys_msg = convertView.findViewById(R.id.tv_content_sys_msg);
                myHolder.tv_type_sys_msg = convertView.findViewById(R.id.tv_type_sys_msg);
                myHolder.tv_create_item_sys_msg = convertView.findViewById(R.id.tv_create_item_sys_msg);
                myHolder.ll_click_sys_msg = convertView.findViewById(R.id.ll_click_sys_msg);
                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }
            SysMsgBean.DataBean bean = sysMsgBean.data.get(position);


            Long aLong = Long.valueOf(bean.create_time + "000");
            String createTime = Utils.getCreateTime(aLong);
            if(position >0){
                SysMsgBean.DataBean dataBean = sysMsgBean.data.get(position - 1);
                Long preLong = Long.valueOf(dataBean.create_time + "000");
                String perCreateTime = Utils.getCreateTime(preLong);
                if(createTime.equals(perCreateTime)){
                    myHolder.tv_create_item_sys_msg.setVisibility(View.GONE);
                }else{
                    myHolder.tv_create_item_sys_msg.setVisibility(View.VISIBLE);
                }
            }else{
                myHolder.tv_create_item_sys_msg.setVisibility(View.VISIBLE);
            }

            myHolder.tv_create_item_sys_msg.setText(createTime);

            myHolder.tv_type_sys_msg.setText(bean.title);

            myHolder.tv_content_sys_msg.setText(bean.message);

            if(bean.type.equals("0")){
                myHolder.ll_click_sys_msg.setVisibility(View.GONE);
            }else{
                myHolder.ll_click_sys_msg.setVisibility(View.VISIBLE);
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
        public TextView tv_create_item_sys_msg;
        public TextView tv_type_sys_msg;
        public TextView tv_content_sys_msg;
        public LinearLayout ll_click_sys_msg;

    }

}
