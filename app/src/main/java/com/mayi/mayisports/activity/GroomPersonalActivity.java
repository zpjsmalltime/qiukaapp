package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.GroomPersonalBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 可能感兴趣的人
 */
public class GroomPersonalActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener, AdapterView.OnItemClickListener {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,GroomPersonalActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_groom_personal;
    }

    private ListView lv_groom_personal;
    private MyAdapter myAdapter = new MyAdapter();
    private RelativeLayout rl_load_layout;
    @Override
    protected void initView() {
        super.initView();
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setTitleShow(true);
        tv_title.setText("可能感兴趣的人");
        tv_ritht_title.setVisibility(View.INVISIBLE);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        lv_groom_personal = findViewById(R.id.lv_groom_personal);
        lv_groom_personal.setAdapter(myAdapter);
        lv_groom_personal.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        requestNetDatas();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        requestNetDatas();
    }

    private GroomPersonalBean groomPersonalBean;
    private boolean isNetNotify;
    private void requestNetDatas() {
        //http://20180123.dldemo.applinzi.com/php/api.php?action=user_recommendation_homepage&type=mylist
        //http://app.mayisports.com/php/api.php?action=followers&type=sys_recommendation
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        params.put("type","sys_recommendation");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                groomPersonalBean = JsonParseUtils.parseJsonClass(string,GroomPersonalBean.class);
                if(groomPersonalBean != null && groomPersonalBean.data != null){
//                    isNetNotify = true;
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    @Override
    public void onReload() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GroomPersonalBean.DataBean dataBean = groomPersonalBean.data.get(position);
        String json = new Gson().toJson(dataBean);
        PersonalDetailActivity.start(this,dataBean.user_id,position,99,json);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99){
            int position = data.getIntExtra("position", -1);
            if(position != -1){
                int follow_status = data.getIntExtra("follow_status",0);
                GroomPersonalBean.DataBean dataBean = groomPersonalBean.data.get(position);
                dataBean.follow_status = follow_status;
//                isNetNotify = false;
                sendHome(dataBean.user_id,follow_status);
                myAdapter.notifyDataSetChanged();
            }
        }

    }

    private void sendHome(String userId,int follow_status){
        Intent intent1 = new Intent(HomeNewFragment.ACTION);
        intent1.putExtra(RESULT, 99);
        intent1.putExtra("userId",userId);
        intent1.putExtra("follow_status",follow_status);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(groomPersonalBean != null && groomPersonalBean.data != null){
                return groomPersonalBean.data.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            MyHolder myHolder;
            if(convertView == null){
                myHolder = new MyHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.groom_personal_item,null);
                myHolder.iv_header_item = convertView.findViewById(R.id.iv_header_item);
                myHolder.iv_vip_item = convertView.findViewById(R.id.iv_vip_item);
                myHolder.tv_name_item = convertView.findViewById(R.id.tv_name_item);
                myHolder.tv_reason_item = convertView.findViewById(R.id.tv_reason_item);
                myHolder.tv_follow_item = convertView.findViewById(R.id.tv_follow_item);
                convertView.setTag(myHolder);
            }else{
                myHolder = (MyHolder) convertView.getTag();
            }

            GroomPersonalBean.DataBean dataBean = groomPersonalBean.data.get(position);

            PictureUtils.showCircle(dataBean.headurl,myHolder.iv_header_item);

            try {
                if (!TextUtils.isEmpty(dataBean.verify_type) && Integer.valueOf(dataBean.verify_type) > 0) {
                    myHolder.iv_vip_item.setVisibility(View.VISIBLE);
                } else {
                    myHolder.iv_vip_item.setVisibility(View.GONE);
                }
            }catch (Exception e){
                myHolder.iv_vip_item.setVisibility(View.GONE);
            }

//            if(isNetNotify){
//                if(dataBean.follow_status == 1){
//                    Constant.followStates.put(dataBean.user_id,true);
//                }else{
//                    Constant.followStates.remove(dataBean.user_id);
//                }
//            }
//            Boolean aBoolean = Constant.followStates.get(dataBean.user_id);
//            if(aBoolean != null){
//                dataBean.follow_status = 1;
//            }else{
//                dataBean.follow_status = 0;
//            }

            if (dataBean.follow_status == 1) {//关注
                myHolder.tv_follow_item.setTag(1);
                myHolder.tv_follow_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                myHolder.tv_follow_item.setTextColor(Color.parseColor("#e8e8e8"));
                myHolder.tv_follow_item.setText("已关注");
            } else {
                myHolder.tv_follow_item.setTag(0);
                myHolder.tv_follow_item.setBackground(getResources().getDrawable(R.drawable.select_score_bottom));
                myHolder.tv_follow_item.setTextColor(Color.parseColor("#282828"));
                myHolder.tv_follow_item.setText(" 关注 ");
            }

            myHolder.tv_name_item.setText(dataBean.nickname);
            if(TextUtils.isEmpty(dataBean.verify_reason)){
                myHolder.tv_reason_item.setText("");
            }else {
                myHolder.tv_reason_item.setText(dataBean.verify_reason);
            }

            myHolder.tv_follow_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(LoginUtils.isUnLogin()){
                        LoginUtils.goLoginActivity(GroomPersonalActivity.this,HomeNewFragment.ACTION);
                        return;
                    }
                   requestFollows(position);
                }
            });

//            if(position == getCount()-1)isNetNotify = false;


            return convertView;
        }
    }

    static class MyHolder {

        public ImageView iv_header_item;
        public ImageView iv_vip_item;
        public TextView tv_name_item;
        public TextView tv_reason_item;
        public TextView tv_follow_item;

    }

    public void requestFollows(int i){

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
            return;
        }

        final GroomPersonalBean.DataBean bean = groomPersonalBean.data.get(i);
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","followers");
        String type = "";
        if(bean.follow_status == 1){
            type = "delete";
        }else{
            type = "add";
        }
        params.put("type",type);
        params.put("user_id",bean.user_id+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                FollowBean followBean = JsonParseUtils.parseJsonClass(string,FollowBean.class);
                if(followBean != null){
                    if("1".equals(followBean.status.result)){
                        bean.follow_status = 1;
                        sendHome(bean.user_id,1);
                        NotificationsUtils.checkNotificationAndStartSetting(GroomPersonalActivity.this,lv_groom_personal);
                    }else{
                        bean.follow_status = 0;
                        sendHome(bean.user_id,0);
                    }

                    myAdapter.notifyDataSetChanged();

                    DynamicFragment.sendFoucsData();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }
}
