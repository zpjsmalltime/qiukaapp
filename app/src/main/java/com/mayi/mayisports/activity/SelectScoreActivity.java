package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.HandicapBean;
import com.mayisports.qca.bean.SelectParseBean;
import com.mayisports.qca.bean.SelectProcessBean;
import com.mayisports.qca.bean.SelectResultBean;
import com.mayisports.qca.bean.SelectScoreMatchBean;
import com.mayisports.qca.bean.SelectScoreSubmitBean;
import com.mayisports.qca.fragment.ScoreFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 即时比分 筛选模块
 */
public class SelectScoreActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, RequestHttpCallBack.ReLoadListener {


    private RadioGroup rg_select_score;

    private ListView lv_select_score_ac;
    private GridView gv_select_score_ac;

    private TextView tv_num_select_score;
    private TextView tv_all_select_score;
    private TextView tv_no_select_score;
    private TextView tv_one_select_score;
    private TextView tv_title_select_score;
    private ImageView iv_left_title_select_score;
    private TextView tv_ritht_title_select_score;
    private View sv;

    private MyLvAdapter myLvAdapter = new MyLvAdapter();
    private MyGvAdapter myGvAdapter = new MyGvAdapter();
    private String json;
    private RelativeLayout rl_load_layout;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        QCaApplication.idSet.clear();
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_select_score;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);

        rl_load_layout = findViewById(R.id.rl_load_layout);
        rg_select_score = findViewById(R.id.rg_select_score);
        rg_select_score.setOnCheckedChangeListener(this);

        lv_select_score_ac = findViewById(R.id.lv_select_score_ac);
        gv_select_score_ac = findViewById(R.id.gv_select_score_ac);

        tv_num_select_score = findViewById(R.id.tv_num_select_score);
        tv_all_select_score = findViewById(R.id.tv_all_select_score);
        tv_no_select_score = findViewById(R.id.tv_no_select_score);
        tv_one_select_score = findViewById(R.id.tv_one_select_score);
        tv_title_select_score = findViewById(R.id.tv_title_select_score);
        iv_left_title_select_score = findViewById(R.id.iv_left_title_select_score);
        tv_ritht_title_select_score = findViewById(R.id.tv_ritht_title_select_score);
        sv = findViewById(R.id.sv);

        tv_all_select_score.setOnClickListener(this);
        tv_no_select_score.setOnClickListener(this);
        tv_one_select_score.setOnClickListener(this);
        iv_left_title_select_score.setOnClickListener(this);
        tv_ritht_title_select_score.setOnClickListener(this);




        lv_select_score_ac.setAdapter(myLvAdapter);
        gv_select_score_ac.setAdapter(myGvAdapter);
    }


    private int flag;//0 即时比分，1，赛果，2赛程，
    private String date = "";
    private boolean first = true;

    @Override
    protected void initDatas() {
        super.initDatas();
        flag = getIntent().getIntExtra("flag",0);

        if(flag == 0){//即时比分
            rg_select_score.setVisibility(View.VISIBLE);
            rg_select_score.check(R.id.rb_left);
        }else if(flag == 1){//赛果
            tv_title_select_score.setVisibility(View.VISIBLE);
            date = getIntent().getStringExtra("date");
            sv.setVisibility(View.VISIBLE);
            lv_select_score_ac.setVisibility(View.VISIBLE);
            tv_one_select_score.setVisibility(View.VISIBLE);
            gv_select_score_ac.setVisibility(View.GONE);
            type = 1;
            isAll = false;
            isNo = false;
            isOne = false;
            myLvAdapter.notifyDataSetChanged();
            requestResultSelect();
        }else {//赛程
            tv_title_select_score.setVisibility(View.VISIBLE);
            date = getIntent().getStringExtra("date");
            sv.setVisibility(View.VISIBLE);
            lv_select_score_ac.setVisibility(View.VISIBLE);
            tv_one_select_score.setVisibility(View.VISIBLE);
            gv_select_score_ac.setVisibility(View.GONE);
            type = 1;
            isAll = false;
            isNo = false;
            isOne = false;
            myLvAdapter.notifyDataSetChanged();
            requestProcessSelect();
        }


    }

    /**
     * 请求赛程筛选结果
     */
    private SelectProcessBean selectProcessBean;
    private void requestProcessSelect() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListFilter");
        params.put("type","leagueFilter");
        params.put("date",date+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                selectProcessBean = JsonParseUtils.parseJsonClass(string,SelectProcessBean.class);
                if(selectProcessBean != null && selectProcessBean.data != null && selectProcessBean.data.leagueFilter != null){
                    parseProcessBean();
                    myLvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {

                ToastUtils.toast(strMsg);
            }
        });
    }


    private void parseProcessBean() {
        matchBean.clear();
        LinkedHashSet linkedHashSet = new LinkedHashSet();
        for(int i = 0;i<selectProcessBean.data.leagueFilter.size();i++){
            String s = selectProcessBean.data.leagueFilter.get(i).get(5);
            if(s == null)continue;
            total ++;
            String title = parseTitle(s);
            linkedHashSet.add(title);
        }


        Iterator iterator = linkedHashSet.iterator();
        int index = 0;
        while (iterator.hasNext()){
            String next = (String) iterator.next();
            SelectParseBean selectParseBean = new SelectParseBean();
            selectParseBean.title = next;
            selectParseBean.detail = new ArrayList<>();

            for(int j = 0;j<selectProcessBean.data.leagueFilter.size();j++){
                List<String> strings = selectProcessBean.data.leagueFilter.get(j);
                String s = strings.get(5);
                if(s == null)continue;

                String select = strings.get(2);
                if("1".equals(select)){
//                    QCaApplication.idSet.add(strings.get(0));
                }
                String title = parseTitle(s);
                if(next.equals(title)){
                    selectParseBean.detail.add(strings);
                }
            }

            matchBean.add(selectParseBean);
        }
    }

    /**
     * 请求赛果筛选
     */
    private SelectResultBean selectResultBean;
    private void requestResultSelect() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListFilter");
        params.put("type","leagueCompFilter");
        params.put("date",date+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                selectResultBean = JsonParseUtils.parseJsonClass(string,SelectResultBean.class);
                if(selectResultBean != null && selectResultBean.data != null && selectResultBean.data.leagueCompFilter != null){
                    parseResultBean();
                    myLvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                  ToastUtils.toast(strMsg);
            }
        });


    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_all_select_score:
                delAll();
                break;
            case R.id.tv_no_select_score:
                delNo();
                break;
            case R.id.tv_one_select_score:
                delOne();
                break;
            case R.id.iv_left_title_select_score:
                finish();
                break;
            case R.id.tv_ritht_title_select_score:
                String string = SPUtils.getString(this.getBaseContext(), Constant.TYPE + flag + type + date);
                int length = string.split(",").length;
                if(!TextUtils.isEmpty(string)&&length == total){
                    ToastUtils.toast("至少选择一项");
                    SPUtils.putString(this.getBaseContext(),Constant.TYPE+flag+type+date,"");
                    return;
                }
                ToastUtils.toast("完成");
                Intent intent = new Intent(ScoreFragment.ACTION);
                intent.putExtra("select",1);
                if(flag == 0) {
                    if (type == 1) {//赛事  2盘口
                        SPUtils.putString(getBaseContext(), Constant.TYPE + 0 + 2 + date, "");
                    } else if(type == 2){
                        SPUtils.putString(getBaseContext(), Constant.TYPE + 0 + 1 + date, "");
                    }
                }

                intent.putExtra("sp",Constant.TYPE+flag+type+date+"-"+date+"-"+flag);
                LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent);
                finish();

                break;
        }
    }


    @Override
    public void finish() {
        String string = SPUtils.getString(this.getBaseContext(), Constant.TYPE + flag + type + date);
        int length = string.split(",").length;
        if(length == total){
            SPUtils.putString(this.getBaseContext(),Constant.TYPE+flag+type+date,"");
        }
        super.finish();
    }

    /**
     * 提交筛选
     */
    private void submit() {
        String url = Constant.BASE_URL + "/php/api.php";

        HttpParams params = new HttpParams();

        if (flag == 0) {//即时比分
            if (type == 1) {//赛事
                params.put("action", "setting");
                params.put("type", "set");

                if(isNo){
                    ToastUtils.toast("至少选择一个");
//                    addAllIds(selectScoreMatchBean.data.leagueFilter);
                    return;
                }
                String json = getJson("leagueList");
                Log.e("json",json+"");
                params.put("value", json);
            } else {//盘口
                if(isNo_Right){
                    ToastUtils.toast("至少选择一个");
//                    addAllIdsRight();
                    return;
                }
                params.put("action", "setting");
                params.put("type", "set");
                String json = getJson("asiaTapeList");
                Log.e("json",json+"");
                params.put("value", json);
            }

        } else if(flag == 1) {//赛果

            params.put("action", "setting");
            params.put("type", "set");
            if(isNo){
                ToastUtils.toast("至少选择一个");
//                addAllIds(selectResultBean.data.leagueCompFilter);
                return;
            }

            String json = getJson("leagueCompList");
            Log.e("json",json+"");
            params.put("value", json);

        }else if(flag == 2){//赛程
            params.put("action", "setting");
            params.put("type", "set");
            if(isNo){
                ToastUtils.toast("至少选择一个");
//                addAllIds(selectProcessBean.data.leagueFilter);
                return;
            }
            String json = getJson("leagueList");
            Log.e("json",json+"");
            params.put("value", json);
        }


        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                  try {
                      SelectScoreSubmitBean selectScoreSubmitBean = JsonParseUtils.parseJsonClass(string, SelectScoreSubmitBean.class);
                      ToastUtils.toast("成功");
                      finish();
                  }catch (Exception e){
                      ToastUtils.toast(string);
                  }

            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(strMsg);
            }
        });
    }

    private void addAllIdsRight() {
        try {
            for (int i = 0; i < handicapBean.data.asiaTapeFilter.size(); i++) {
                List<String> strings = selectScoreMatchBean.data.leagueFilter.get(i);
                String id = strings.get(0);
                QCaApplication.idSet.add(id);
            }
        }catch (Exception e){

        }
    }

    private void addAllIds(List<List<String>> lists) {
        try {
            for (int i = 0; i < lists.size(); i++) {
                List<String> strings = lists.get(i);
                String s = strings.get(5);
                if(s == null)continue;
                String id = strings.get(0);
                QCaApplication.idSet.add(id);

            }
        }catch (Exception e){

        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i){
            case R.id.rb_left:
                delLeft();
                break;
            case R.id.rb_right:
                delRight();
                break;

        }
    }

    private boolean isOne;
    private void delOne() {
        isAll = false;
        isOne = true;
        isNo = false;
        if(type == 1){
            myLvAdapter.notifyDataSetChanged();
        }else if(type == 2){
            myGvAdapter.notifyDataSetChanged();
        }
//        tv_num_select_score.setText(idSet.size()+"");
    }

    private boolean isNo;
    private boolean isNo_Right;
    private void delNo() {
        SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,"");
        if(type == 1){
            isAll = false;
            isOne = false;
            isNo = true;
            myLvAdapter.notifyDataSetChanged();
        }else if(type == 2){
            isAll_Right =false;
            isNo_Right = true;
            myGvAdapter.notifyDataSetChanged();
        }
    }

    private boolean isAll;
    private boolean isAll_Right;
    private void delAll() {

        if(type == 1){
            isAll = true;
            isOne = false;
            isNo = false;
          myLvAdapter.notifyDataSetChanged();
        }else if(type == 2){
             isAll_Right =true;
             isNo_Right = false;
             myGvAdapter.notifyDataSetChanged();
        }

    }

    private int type;//1 赛事 ，2 盘口

    /**
     * 处理盘口
     */
    private void delRight() {
        if(type == 2)return;

        QCaApplication.idSet.clear();
        sv.setVisibility(View.GONE);
        lv_select_score_ac.setVisibility(View.GONE);
        gv_select_score_ac.setVisibility(View.VISIBLE);
        tv_one_select_score.setVisibility(View.GONE);
        type = 2;

        SPUtils.putString(getBaseContext(), Constant.TYPE + 0 + 1 + date, "");

        isAll_Right =false;
        isNo_Right = false;
        myLvAdapter.notifyDataSetChanged();
        requestNetDataRight();
    }

    /**
     * 请求盘口
     */
    private HandicapBean handicapBean;
    private void requestNetDataRight() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListFilter");
        params.put("type","asiaTapeFilter");
        params.put("value", Utils.getCurrentYYMMDD()+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                handicapBean = JsonParseUtils.parseJsonClass(string,HandicapBean.class);
                if(handicapBean != null && handicapBean.data != null && handicapBean.data.asiaTapeFilter != null) {
                    initSelect();
                    total  = handicapBean.data.asiaTapeFilter.size();
                    myGvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(strMsg);
            }
        });
    }

    /**
     * 初始化盘口
     */
    private void initSelect() {
        for(int i = 0;i<handicapBean.data.asiaTapeFilter.size();i++) {
            final List<String> strings = handicapBean.data.asiaTapeFilter.get(i);
            String s = strings.get(2);
            if("1".equals(s)){
//                QCaApplication.idSet.add(strings.get(0));
            }
        }
    }

    /**
     * 处理赛事
     */
    private void delLeft() {
        if(type == 1)return;
        total = 0;
        sv.setVisibility(View.VISIBLE);
       lv_select_score_ac.setVisibility(View.VISIBLE);
       gv_select_score_ac.setVisibility(View.GONE);
       tv_one_select_score.setVisibility(View.VISIBLE);
       type = 1;
       SPUtils.putString(getBaseContext(), Constant.TYPE + 0 + 2 + date, "");
       isAll = false;
       isNo = false;
       isOne = false;
       myLvAdapter.notifyDataSetChanged();
       requestNetDataLeft();
    }

    /**
     * 加载筛选赛事
     */
    private SelectScoreMatchBean selectScoreMatchBean;
    private void requestNetDataLeft() {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","matchListFilter");
        params.put("type","leagueFilter");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                selectScoreMatchBean = JsonParseUtils.parseJsonClass(string,SelectScoreMatchBean.class);
                if(selectScoreMatchBean != null && selectScoreMatchBean.data != null && selectScoreMatchBean.data.leagueFilter != null) {
                    parseBean();
                    myLvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(strMsg);
            }
        });

    }

    private List<SelectParseBean> matchBean = new ArrayList<>();

    /**
     * 赛事
     */
    private int total;
    private void parseBean(){
        matchBean.clear();
        LinkedHashSet linkedHashSet = new LinkedHashSet();

        for(int i = 0;i<selectScoreMatchBean.data.leagueFilter.size();i++){
                String s = selectScoreMatchBean.data.leagueFilter.get(i).get(5);
                if(s == null)continue;
                total ++;
                String title = parseTitle(s);
                linkedHashSet.add(title);
        }


        Iterator iterator = linkedHashSet.iterator();
        int index = 0;
        while (iterator.hasNext()){
            String next = (String) iterator.next();
            SelectParseBean selectParseBean = new SelectParseBean();
            selectParseBean.title = next;
            selectParseBean.detail = new ArrayList<>();

            for(int j = 0;j<selectScoreMatchBean.data.leagueFilter.size();j++){
                List<String> strings = selectScoreMatchBean.data.leagueFilter.get(j);
                String s = strings.get(5);
                if(s == null)continue;

                String select = strings.get(2);
                if("1".equals(select)){
//                    QCaApplication.idSet.add(strings.get(0));
                }

                String title = parseTitle(s);
                if(next.equals(title)){
                    selectParseBean.detail.add(strings);
                }
            }

            matchBean.add(selectParseBean);
        }
}

    private void parseResultBean(){
        matchBean.clear();
        LinkedHashSet linkedHashSet = new LinkedHashSet();

        for(int i = 0;i<selectResultBean.data.leagueCompFilter.size();i++){
            String s = selectResultBean.data.leagueCompFilter.get(i).get(5);
            if(s == null)continue;
            total ++;
            String title = parseTitle(s);
            linkedHashSet.add(title);
        }


        Iterator iterator = linkedHashSet.iterator();
        int index = 0;
        while (iterator.hasNext()){
            String next = (String) iterator.next();
            SelectParseBean selectParseBean = new SelectParseBean();
            selectParseBean.title = next;
            selectParseBean.detail = new ArrayList<>();

            for(int j = 0;j<selectResultBean.data.leagueCompFilter.size();j++){
                List<String> strings = selectResultBean.data.leagueCompFilter.get(j);
                try {
                    String s = strings.get(5);
                    if(s == null)continue;

                    String select = strings.get(2);
                    if("1".equals(select)){
//                        QCaApplication.idSet.add(strings.get(0));
                    }

                    String title = parseTitle(s);
                    if (next.equals(title)) {
                        selectParseBean.detail.add(strings);
                    }
                }catch (Exception e){

                }
            }

            matchBean.add(selectParseBean);
        }
    }

    public String getJson(String type_) {
        String json = "{\"type\":\"SettingSubmit\",\"subtype\":\""+type_+"\",\"list\":{";
        Iterator<String> iterator = QCaApplication.idSet.iterator();


        while (iterator.hasNext()){
            String next = iterator.next();
            if(isNo || isNo_Right) {
                json += "\"" + next + "\":0,";
            }else{
                json += "\"" + next + "\":1,";
            }
        }
        int i = json.lastIndexOf(",");
        String substring = json.substring(0, i);

        json = substring + "}}";

        return json;
    }

    @Override
    public void onReload() {

    }

    class MyLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            if(flag == 0){
                if(selectScoreMatchBean != null && selectScoreMatchBean.data != null && selectScoreMatchBean.data.leagueFilter != null){
                       return matchBean.size();
                }
            }else if(flag == 1){
                if(selectResultBean != null && selectResultBean.data != null && selectResultBean.data.leagueCompFilter != null){
                    return matchBean.size();
                }
            }else if(flag == 2){
                if(selectProcessBean != null && selectProcessBean.data != null && selectProcessBean.data.leagueFilter != null){
                    return matchBean.size();

                }
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @SuppressLint("WrongViewCast")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            MyHolder myHolder;

            if(view == null){
                myHolder = new MyHolder();
                view = View.inflate(getApplicationContext(),R.layout.select_score_item,null);
                myHolder.tv_select_score_item = view.findViewById(R.id.tv_select_score_item);
                myHolder.gv_select_score_item = view.findViewById(R.id.gv_select_score_item);
                view.setTag(myHolder);
            }else{
                myHolder = (MyHolder) view.getTag();
            }
//            if(flag == 0){
                SelectParseBean selectParseBean = matchBean.get(i);
//                List<String> strings =selectParseBeanleagueFilter.get(i);
                myHolder.tv_select_score_item.setText(selectParseBean.title);
                myHolder.gv_select_score_item.setAdapter(new InnerGvAdapter(i));
//            }else {
//
//
//            }

            return view;
        }
    }

    //1: 亚洲赛事 2: 欧洲其他国 3: 非洲赛事 4: 美洲赛事 5: 大洋洲赛事 6: 国际赛事 7: 欧洲五大国
    private String parseTitle(String code){
        switch (code){
            case "1":
                return "亚洲赛事";
            case "2":
                return  " 欧洲其他国";
            case "3":
                return "非洲赛事";
            case "4":
                return "美洲赛事";
            case "5":
                return "大洋洲赛事";
            case "6":
                return "国际赛事";
            case "7":
                return "欧洲五大国";

        }
        return "";
    }

    class InnerGvAdapter extends  BaseAdapter{

        int j;
        public InnerGvAdapter(int i){
            this.j =i;
        }
        @Override
        public int getCount() {
            return matchBean.get(j).detail.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
           view = View.inflate(getBaseContext(),R.layout.checkbox_layout,null);
            final TextView name = view.findViewById(R.id.tv_name_checkbox);
            final TextView count = view.findViewById(R.id.tv_count_checkbox);
            final List<String> strings = matchBean.get(j).detail.get(i);

            CheckBox checkBox = view.findViewById(R.id.cb);
//           checkBox.setText(strings.get(1));
            name.setText(strings.get(1));
            if(flag == 0) {
                count.setText(strings.get(3) + "场");
            }else{
                count.setText("");
            }




           if(isAll){
              QCaApplication.idSet.add(strings.get(0));

//               String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag);
               SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,"");

           }else if(isNo){
               QCaApplication.idSet.clear();

               String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
               if(!string.contains(strings.get(0))) {
                   string += strings.get(0) + ",";
               }
               SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);

           }else if(isOne){
               if("1".equals(strings.get(6))){//一级
                   QCaApplication.idSet.add(strings.get(0));

                   String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                   string = string.replace(strings.get(0)+",","");
                   SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);

               }else{
//                   QCaApplication.idSet.remove(strings.get(0));

                   String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                   if(!string.contains(strings.get(0))) {
                       string += strings.get(0) + ",";
                   }
                   SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);
               }
           }

//           if(QCaApplication.idSet.contains(strings.get(0))){
            String string1 = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
            if(!SPUtils.getString(QCaApplication.getContext(),Constant.TYPE+flag+type+date).contains(strings.get(0))){
               checkBox.setChecked(true);
               name.setTextColor(Color.parseColor("#282828"));
               count.setTextColor(Color.parseColor("#282828"));
           }else{
               checkBox.setChecked(false);
               name.setTextColor(Color.parseColor("#999999"));
               count.setTextColor(Color.parseColor("#999999"));
           }



           checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                     if(b){
                         isNo = false;

                         String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                         string = string.replace(strings.get(0)+",","");
                         SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);

                         QCaApplication.idSet.add(strings.get(0));
                         name.setTextColor(Color.parseColor("#282828"));
                         count.setTextColor(Color.parseColor("#282828"));
                     }else{

                         String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                         if(!string.contains(strings.get(0))) {
                             string += strings.get(0) + ",";
                         }
                         SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);


                         QCaApplication.idSet.remove(strings.get(0));
                         name.setTextColor(Color.parseColor("#999999"));
                         count.setTextColor(Color.parseColor("#999999"));
                     }

                   String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                   String[] split = string.split(",");

                   if(split.length>0&&string.length()>2){
                       tv_num_select_score.setText(total - split.length+"");
                   }else{
                       tv_num_select_score.setText(total+"");
                   }
//                   tv_num_select_score.setText(total - split.length+"");
//                   tv_num_select_score.setText(QCaApplication.idSet.size()+"");
               }
           });

           if(i == getCount()-1){
               String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
               String[] split = string.split(",");
               if(split.length>0&&string.length()>2){
                   tv_num_select_score.setText(total - split.length+"");
               }else{
                   tv_num_select_score.setText(total+"");
               }

//               tv_num_select_score.setText(QCaApplication.idSet.size()+"");
           }

            return view;
        }
    }
//    private HashSet<String> idSet = new HashSet<>();


    static class MyHolder{

        public TextView tv_select_score_item;
        public GridView gv_select_score_item;
    }


    class MyGvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(handicapBean != null && handicapBean.data != null && handicapBean.data.asiaTapeFilter != null)
            return handicapBean.data.asiaTapeFilter.size();
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = View.inflate(getBaseContext(),R.layout.checkbox_layout,null);
            final TextView name = view.findViewById(R.id.tv_name_checkbox);
            final TextView count = view.findViewById(R.id.tv_count_checkbox);
            final List<String> strings = handicapBean.data.asiaTapeFilter.get(i);
            Log.e("list",handicapBean.data.asiaTapeFilter.toString());
            CheckBox checkBox = view.findViewById(R.id.cb);
            name.setText(Utils.parseOddOfHandicap(strings.get(1)));
            count.setText(strings.get(3)+"场");

            if(flag == 0) {
                Log.e("item",strings.toString());
                if (isAll_Right) {
                    QCaApplication.idSet.add(strings.get(0));
                    SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,"");
                } else if (isNo_Right) {
                    Log.e("item_no",strings.toString());
                    QCaApplication.idSet.clear();

                    String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                    String s = strings.get(0);

                    String s1 = new String(s);
                    if(!string.contains(s1)  || "5000".equals(s) || "7500".equals(s)) {
//                        Log.e("item_string",string);
                        Log.e("item_true",strings.toString());
                        string += (strings.get(0) + ",");
                    }
                    SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);
                }
            }

            if(!SPUtils.getString(QCaApplication.getContext(),Constant.TYPE+flag+type+date).contains(strings.get(0))){
                checkBox.setChecked(true);
                name.setTextColor(Color.parseColor("#282828"));
                count.setTextColor(Color.parseColor("#282828"));
            }else{
                checkBox.setChecked(false);
                name.setTextColor(Color.parseColor("#999999"));
                count.setTextColor(Color.parseColor("#999999"));
            }


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        isNo_Right = false;
                        String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                        string = string.replace(strings.get(0)+",","");
                        SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);


                        QCaApplication.idSet.add(strings.get(0));
                        name.setTextColor(Color.parseColor("#282828"));
                        count.setTextColor(Color.parseColor("#282828"));
                    }else{
                        String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                        if(!string.contains(strings.get(0))) {
                            string += strings.get(0) + ",";
                        }
                        SPUtils.putString(QCaApplication.getContext(),Constant.TYPE+flag+type+date,string);


                        QCaApplication.idSet.remove(strings.get(0));
                        name.setTextColor(Color.parseColor("#999999"));
                        count.setTextColor(Color.parseColor("#999999"));
                    }

                    String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                    String[] split = string.split(",");
                    if(split.length>0&&string.length()>1){
                        tv_num_select_score.setText(total - split.length+"");
                    }else{
                        tv_num_select_score.setText(total+"");
                    }
                }
            });

            if(i == getCount()-1){

                String string = SPUtils.getString(QCaApplication.getContext(), Constant.TYPE + flag+type+date);
                Log.e("split--last",string);
                String[] split = string.split(",");
                if(split.length>0&&string.length()>1){
                    tv_num_select_score.setText(total - split.length+"");
                    Log.e("split--top",string);
                }else{
                    tv_num_select_score.setText(total+"");
                    Log.e("split--bottom",string);
                }
            }

            return view;
        }



    }

}
