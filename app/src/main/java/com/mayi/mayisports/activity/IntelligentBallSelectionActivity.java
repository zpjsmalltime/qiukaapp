package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.IntelBallSelectBean;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.util.List;

import static com.mayisports.qca.fragment.HomeNewFragment.ACTION;


/**
 * 足球数据
 */
public class IntelligentBallSelectionActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,IntelligentBallSelectionActivity.class);
        activity.startActivity(intent);
    }

    private ListView lv_ball_activity;
    private MyAdapter myAdapter = new MyAdapter();
    private ViewGroup rl_load_layout;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_intelligent_ball_selection;
    }

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
        tv_title.setText("足球数据");

        lv_ball_activity = findViewById(R.id.lv_ball_activity);
        rl_load_layout = findViewById(R.id.rl_load_layout);

        addHeader();

        lv_ball_activity.setOnItemClickListener(this);

    }


    @Override
    protected void initDatas() {
        super.initDatas();
        requestNestDatas();
    }

    private IntelBallSelectBean intelBallSelectBean;
    private void requestNestDatas() {
        //htt://app.mayisports.com/php/api.php?action=match_leida
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","match_leida");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                intelBallSelectBean = JsonParseUtils.parseJsonClass(string,IntelBallSelectBean.class);
                updateHeader();
                IntelBallSelectBean newBean = JsonParseUtils.parseJsonClass(string, IntelBallSelectBean.class);
                delData(intelBallSelectBean,newBean);
                lv_ball_activity.setAdapter(myAdapter);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(strMsg);
            }
        });
    }

    /**
     * 根据 h  g  处理数据
     * @param intelBallSelectBean
     * @param newBean
     */
    private void delData(IntelBallSelectBean intelBallSelectBean, IntelBallSelectBean newBean) {
        if(intelBallSelectBean == null || intelBallSelectBean.data == null)return;
        intelBallSelectBean.data.clear();
        for(int i = 0;i<newBean.data.size();i++){
            IntelBallSelectBean.DataBean dataBean = newBean.data.get(i);

            if(dataBean.statics.h != null){
                dataBean.statics.type = 1;
                IntelBallSelectBean.DataBean clone = dataBean.clone();
                intelBallSelectBean.data.add(clone);
                Log.e("type",dataBean.statics.type+""+ URLDecoder.decode(dataBean.match.hostTeamName));
            }

            if(dataBean.statics.g != null){
                dataBean.statics.type = 2;
                IntelBallSelectBean.DataBean clone = dataBean.clone();
                intelBallSelectBean.data.add(clone);
                Log.e("type",dataBean.statics.type+""+ URLDecoder.decode(dataBean.match.hostTeamName));
            }


        }
    }

    /**
     * 更新头部信息
     */
    private void updateHeader() {

        if(intelBallSelectBean.packlist  == null || intelBallSelectBean.packlist.size() == 0)return;
        IntelBallSelectBean.PacklistBean bean = intelBallSelectBean.packlist.get(0);

        tv_value_header.setText(bean.desc_value);
        tv_text_header.setText(bean.desc_text);
        tv_title_header.setText(bean.title);
        tv_count_header.setText("周均"+bean.cpw+"场");



//        tv_price_header.setText(bean.price+"金币/"+bean.newX+"天");
        tv_price_header.setText(bean.price+"金币/7天");

    }

    private TextView tv_value_header;
    private TextView tv_text_header;
    private TextView tv_title_header;
    private TextView tv_count_header;
    private TextView tv_price_header;
    private void addHeader() {
        View header = View.inflate(QCaApplication.getContext(),R.layout.ball_select_header,null);
        tv_value_header = header.findViewById(R.id.tv_value_header);
        tv_text_header = header.findViewById(R.id.tv_text_header);
        tv_title_header = header.findViewById(R.id.tv_title_header);
        tv_count_header = header.findViewById(R.id.tv_count_header);
        tv_price_header = header.findViewById(R.id.tv_price_header);
        lv_ball_activity.addHeaderView(header);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(IntelligentBallSelectionActivity.this, HomeNewFragment.ACTION);
                    return;
                }
                WebViewActivtiy.start(IntelligentBallSelectionActivity.this,Constant.BASE_URL+"#/packageIndex","智推荐");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)return;
        IntelBallSelectBean.DataBean bean = intelBallSelectBean.data.get(position-1);
        Intent intent = new Intent(this,MatchDetailActivity.class);
        intent.putExtra("betId",bean.match.betId);
        startActivity(intent);
    }

    @Override
    public void onReload() {

    }

    class MyAdapter extends  BaseAdapter{

        @Override
        public int getCount() {
            if(intelBallSelectBean != null&&intelBallSelectBean.data != null){
                int size = intelBallSelectBean.data.size();
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
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.intel_ball_select_item,null);
                holder.tv_title_ball_item = convertView.findViewById(R.id.tv_title_ball_item);
                holder.tv_tag_ball_item = convertView.findViewById(R.id.tv_tag_ball_item);
                holder.tv_time_ball_item = convertView.findViewById(R.id.tv_time_ball_item);
                holder.tv_team_ball_item = convertView.findViewById(R.id.tv_team_ball_item);
                holder.ll_0_ball_item = convertView.findViewById(R.id.ll_0_ball_item);
                holder.tv_0_top_ball_item = convertView.findViewById(R.id.tv_0_top_ball_item);
                holder.tv_0_bottom_ball_item = convertView.findViewById(R.id.tv_0_bottom_ball_item);
                holder.ll_1_ball_item = convertView.findViewById(R.id.ll_1_ball_item);
                holder.tv_1_top_ball_item = convertView.findViewById(R.id.tv_1_top_ball_item);
                holder.tv_1_bottom_ball_item = convertView.findViewById(R.id.tv_1_bottom_ball_item);
                holder.v_line_ball_item = convertView.findViewById(R.id.v_line_ball_item);
                convertView.setTag(holder);

            }else{
                holder = (Holder) convertView.getTag();
            }

            IntelBallSelectBean.DataBean bean = intelBallSelectBean.data.get(position);

            String hostName = URLDecoder.decode(bean.match.hostTeamName);
            String awayName = URLDecoder.decode(bean.match.awayTeamName);
            if(bean.statics.type == 1){//主队
                holder.tv_title_ball_item.setText(hostName);
            }else if(bean.statics.type == 2){//客队
                holder.tv_title_ball_item.setText(awayName);
            }

            holder.tv_tag_ball_item.setVisibility(View.GONE);

            String time = "";
            time += URLDecoder.decode(bean.match.leagueName);
            time += " ";
            Long aLong = Long.valueOf(bean.match.timezoneoffset + "000");
            time += Utils.getMatchStartTime(aLong);
            holder.tv_time_ball_item.setText(time);

            String teamName = hostName;
            teamName += " ";
            if(!TextUtils.isEmpty(bean.match.asiaTape)) {
                teamName += Utils.parseOdd(bean.match.asiaTape + "");
            }else{
                teamName += "vs";
            }

            teamName += " ";
            teamName += awayName;
            holder.tv_team_ball_item.setText(teamName);



            holder.ll_0_ball_item.setVisibility(View.INVISIBLE);
            holder.ll_1_ball_item.setVisibility(View.INVISIBLE);
            holder.v_line_ball_item.setVisibility(View.INVISIBLE);
            if(bean.statics.type == 1){//主队
                for(int j = 0;j<bean.statics.h.size();j++){
                    if(j>1)break;
                    List<String> strings = bean.statics.h.get(bean.statics.h.size()-j-1);
                    if(j == 1){
                        holder.ll_0_ball_item.setVisibility(View.VISIBLE);
                        holder.v_line_ball_item.setVisibility(View.VISIBLE);
                        String s = strings.get(0);
                        String s1 = strings.get(1);


                        holder.tv_0_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,29);

                        if("盘口变化".equals(s)){
                            String str = "";
                            s1 =  s1.replace(".",",");
                            String[] split = s1.split(",");
                            double v = Double.valueOf(split[0])/10000.0;
                            str = v + "";
                            if("升".equals(split[2])){
                                str +="↑";
                            }else{
                                str +="↓";
                            }
                            double v1 = Double.valueOf(split[1])/10000.0;
                            str +=v1;

                            s1 = str;
                            holder.tv_0_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        }


                        holder.tv_0_top_ball_item.setText(s1);

                        holder.tv_0_bottom_ball_item.setText(s);

                    }

                    if(j == 0){
                        holder.ll_1_ball_item.setVisibility(View.VISIBLE);

                        holder.tv_1_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,29);
                        String s1 = strings.get(1);
                        String s = strings.get(0);
                        if("盘口变化".equals(s)){
                            String str = "";
                            s1 =  s1.replace(".",",");
                            String[] split = s1.split(",");
                            double v = Double.valueOf(split[0])/10000.0;
                            str = v + "";
                            if("升".equals(split[2])){
                                str +="↑";
                            }else{
                                str +="↓";
                            }
                            double v1 = Double.valueOf(split[1])/10000.0;
                            str +=v1;
                            s1 = str;
                            holder.tv_1_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);

                        }

                        holder.tv_1_top_ball_item.setText(s1);

                        holder.tv_1_bottom_ball_item.setText(s);
                    }
                }

            }else if(bean.statics.type == 2){//客队
                for(int j = 0;j<bean.statics.g.size();j++){
                    if(j>1)break;
                    List<String> strings = bean.statics.g.get(bean.statics.g.size()-j-1);
                    if(j == 1){
                        holder.ll_0_ball_item.setVisibility(View.VISIBLE);
                        holder.v_line_ball_item.setVisibility(View.VISIBLE);
                        holder.tv_0_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,29);
                        String s1 = strings.get(1);
                        String s = strings.get(0);
                        if("盘口变化".equals(s)){
                            String str = "";
                            s1 =  s1.replace(".",",");
                            String[] split = s1.split(",");
                            double v = Double.valueOf(split[0])/10000.0;
                            if(v == 0)v = 0;
                            str = v + "";
                            if("升".equals(split[2])){
                                str +="↑";
                            }else{
                                str +="↓";
                            }
                            double v1 = Double.valueOf(split[1])/10000.0;
                            if(v1 == 0)v1 = 0;
                            str +=v1;
                            s1 = str;
                            holder.tv_0_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        }
                        holder.tv_0_top_ball_item.setText(s1);

                        holder.tv_0_bottom_ball_item.setText(s);

                    }

                    if(j == 0){
                        holder.ll_1_ball_item.setVisibility(View.VISIBLE);


                        holder.tv_1_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,29);
                        String s1 = strings.get(1);
                        String s = strings.get(0);

                        if("盘口变化".equals(s)){
                            String str = "";
                            s1 =  s1.replace(".",",");
                            String[] split = s1.split(",");
                            double v = Double.valueOf(split[0])/10000.0;
                            if(v == 0)v = 0;
                            str = v + "";
                            if("升".equals(split[2])){
                                str +="↑";
                            }else{
                                str +="↓";
                            }
                            double v1 = Double.valueOf(split[1])/10000.0;
                            if(v1 == 0)v1 = 0;
                            str +=v1;
                            s1 = str;
                            holder.tv_1_top_ball_item.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        }
                        holder.tv_1_top_ball_item.setText(s1);

                        holder.tv_1_bottom_ball_item.setText(s);
                    }


                }

            }




            return convertView;
        }
    }

    static class Holder{

        public TextView tv_title_ball_item;
        public TextView tv_tag_ball_item;
        public TextView tv_time_ball_item;
        public TextView tv_team_ball_item;
        public LinearLayout ll_0_ball_item;
        public TextView tv_0_top_ball_item;
        public TextView tv_0_bottom_ball_item;
        public View v_line_ball_item;
        public LinearLayout ll_1_ball_item;
        public TextView tv_1_top_ball_item;
        public TextView tv_1_bottom_ball_item;
    }
}
