package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.OddsDetailBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 赔率详情界面
 */
public class OddsDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    public static void start(Activity activity ,int type,String betId,String  companyId){
        Intent intent = new Intent(activity,OddsDetailActivity.class);
        intent.putExtra("type",type);
        intent.putExtra("betId",betId);
        intent.putExtra("companyId",companyId);
        activity.startActivity(intent);

    }


    @Override
    protected int setViewForContent() {
        return R.layout.activity_odds_detail;
    }


    private ListView lv_left_odds;
    private ListView lv_right_odds;

    private TextView tv_title_0;
    private TextView tv_title_1;
    private TextView tv_title_2;
    private TextView tv_title_3;


    private RelativeLayout rl_load_layout;
    @Override
    protected void initView() {
        super.initView();
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("赔率详情");
        iv_left_title.setOnClickListener(this);

        lv_left_odds = findViewById(R.id.lv_left_odds);
        lv_right_odds = findViewById(R.id.lv_right_odds);

        lv_left_odds.setOnItemClickListener(this);

        tv_title_0 = findViewById(R.id.tv_title_0);
        tv_title_1 = findViewById(R.id.tv_title_1);
        tv_title_2 = findViewById(R.id.tv_title_2);
        tv_title_3 = findViewById(R.id.tv_title_3);

        rl_load_layout = findViewById(R.id.rl_load_layout);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_left_title:
                finish();
                break;
        }
    }


    private int type;
    private String betId;
    private String companyId;

    @Override
    protected void initDatas() {
        super.initDatas();

        type = getIntent().getIntExtra("type",0);
        betId = getIntent().getStringExtra("betId");
        companyId = getIntent().getStringExtra("companyId");


        requestLeftData();
    }


    /**
     * 初始化请求标题
     */
    private OddsDetailBean oddsDetailBean;
    private void requestLeftData() {


        //https://app.mayisports.com/php/api.php?action=showoddschanges&type=euro&betId=1543816&companyId=23
        String typeStr = "";

        switch (type){
            case 0:
                typeStr = "euro";

                tv_title_0.setText("胜");
                tv_title_1.setText("平");
                tv_title_2.setText("负");
                tv_title_3.setText("更新");

                break;
            case 1:
                typeStr = "asia";


                tv_title_0.setText("主赔");
                tv_title_1.setText("让球");
                tv_title_2.setText("客赔");
                tv_title_3.setText("更新");
                break;
            case 2:
                typeStr = "score";


                tv_title_0.setText("大球");
                tv_title_1.setText("盘口");
                tv_title_2.setText("小球");
                tv_title_3.setText("更新");
                break;
        }

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","showoddschanges");
        params.put("type",typeStr);
        params.put("betId",betId+"");
        params.put("companyId",companyId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
                    oddsDetailBean = JsonParseUtils.parseJsonClass(string,OddsDetailBean.class);

                    lv_left_odds.setAdapter(myLeftAdapter);

                if(oddsDetailBean == null || oddsDetailBean.data == null || oddsDetailBean.data.companyList == null){
                    return;
                }

                List<OddsDetailBean.DataBean.CompanyListBean> companyList = oddsDetailBean.data.companyList;
                for(int i = 0;i<companyList.size();i++){
                    String company = companyList.get(i).companyId+"";
                    if(company.equals(companyId)){
                        clickPostion = i;
                        lv_left_odds.setSelection(i);
                        lv_right_odds.setAdapter(new MyRightAdapter(oddsDetailBean));
                        break;
                    }
                }


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    /**
     * 请求右侧内容
     * @param companyId
     */

    private void requestRightData(int companyId) {
       //https://app.mayisports.com/php/api.php?action=showoddschanges&type=euro&betId=1543816&companyId=23
        String typeStr = "";

        switch (type){
            case 0:
                typeStr = "euro";
                break;
            case 1:
                typeStr = "asia";
                break;
            case 2:
                typeStr = "score";
                break;
        }

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","showoddschanges");
        params.put("type",typeStr);
        params.put("betId",betId+"");
        params.put("companyId",companyId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,null) {
            @Override
            public void onSucces(String string) {
                OddsDetailBean  oddsDetailBean = JsonParseUtils.parseJsonClass(string,OddsDetailBean.class);

                lv_right_odds.setAdapter(new MyRightAdapter(oddsDetailBean));


            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    private int clickPostion;
    private MyLeftAdapter myLeftAdapter = new MyLeftAdapter();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         if(clickPostion != position){
             clickPostion = position;
             OddsDetailBean.DataBean.CompanyListBean companyListBean = oddsDetailBean.data.companyList.get(position);
             requestRightData(companyListBean.companyId);
             myLeftAdapter.notifyDataSetChanged();
         }
    }



    class MyLeftAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(oddsDetailBean == null || oddsDetailBean.data == null || oddsDetailBean.data.companyList == null){
                return 0;
            }
            return oddsDetailBean.data.companyList.size();
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
            ComonHolder comonHolder;
            if(convertView == null){
                comonHolder  = new ComonHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.left_title_odds_item,null);

                comonHolder.tv_name = convertView.findViewById(R.id.tv_name);

                convertView.setTag(comonHolder);
            }else{
                comonHolder = (ComonHolder) convertView.getTag();
            }

            OddsDetailBean.DataBean.CompanyListBean companyListBean = oddsDetailBean.data.companyList.get(position);
            comonHolder.tv_name.setText(companyListBean.companyName);

            if(position == clickPostion){
                convertView.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.white_));
            }else{
                convertView.setBackgroundColor(QCaApplication.getContext().getResources().getColor(R.color.coment_gray_bg));

            }

            return convertView;
        }
    }

    static class ComonHolder{

        public TextView tv_name;

        public TextView tv_title_0;
        public TextView tv_title_1;
        public TextView tv_title_2;
        public TextView tv_title_3;

    }


    class MyRightAdapter extends BaseAdapter{

        private OddsDetailBean oddsDetailBean;

        public MyRightAdapter(OddsDetailBean oddsDetailBean){
            this.oddsDetailBean = oddsDetailBean;
        }

        @Override
        public int getCount() {
            if(oddsDetailBean == null || oddsDetailBean.data == null || oddsDetailBean.data.oddsList == null){
                return 0;
            }
            Collections.reverse(oddsDetailBean.data.oddsList);
            return oddsDetailBean.data.oddsList.size();
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
            ComonHolder comonHolder;
            if(convertView == null){
                comonHolder  = new ComonHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.right_odds_item,null);

                comonHolder.tv_title_0 = convertView.findViewById(R.id.tv_title_0);
                comonHolder.tv_title_1 = convertView.findViewById(R.id.tv_title_1);
                comonHolder.tv_title_2 = convertView.findViewById(R.id.tv_title_2);
                comonHolder.tv_title_3 = convertView.findViewById(R.id.tv_title_3);


                convertView.setTag(comonHolder);
            }else{
                comonHolder = (ComonHolder) convertView.getTag();
            }

            OddsDetailBean.DataBean.OddsListBean oddsListBean = oddsDetailBean.data.oddsList.get(position);

            switch (type){
                case 0:

                    double title0 = Utils.getW2(oddsListBean.winOdds);
                    comonHolder.tv_title_0.setText(title0+"");

                    double title1 = Utils.getW2(oddsListBean.drowOdds);
                    comonHolder.tv_title_1.setText(title1+"");

                    double title2 = Utils.getW2(oddsListBean.loseOdds);
                    comonHolder.tv_title_2.setText(title2+"");

                    String createTime = Utils.getCreateTime(oddsListBean.createTime.time);
                    comonHolder.tv_title_3.setText(createTime);


                    break;
                case 1:
                    double v = Double.valueOf(oddsListBean.hostOdds)/10000 + 1.00;
                    String format = String.format("%.2f", v);
                    comonHolder.tv_title_0.setText(format);


                    String tape = Utils.parseOddOfHandicap(oddsListBean.tape+"");
                    comonHolder.tv_title_1.setText(tape);


                    double v2 = Double.valueOf(oddsListBean.awayOdds)/10000 + 1.00;
                    String format2 = String.format("%.2f", v2);
                    comonHolder.tv_title_2.setText(format2);

                    String createTime1 = Utils.getCreateTime(oddsListBean.createTime.time);
                    comonHolder.tv_title_3.setText(createTime1);
                    break;
                case 2:
                    double vv = Double.valueOf(oddsListBean.hostOdds)/10000 + 1.00;
                    String formatv = String.format("%.2f", vv);
                    comonHolder.tv_title_0.setText(formatv);



                    String tape1 = Utils.parseOddOfHandicap(oddsListBean.tape+"");
                    comonHolder.tv_title_1.setText(tape1);


                    double vv1 = Double.valueOf(oddsListBean.awayOdds)/10000 + 1.00;
                    String formatv1 = String.format("%.2f", vv1);
                    comonHolder.tv_title_2.setText(formatv1);


                    String createTime2 = Utils.getCreateTime(oddsListBean.createTime.time);
                    comonHolder.tv_title_3.setText(createTime2);
                    break;
            }


            return convertView;
        }
    }


}
