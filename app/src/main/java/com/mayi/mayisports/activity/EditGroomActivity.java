package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.R;
import com.mayisports.qca.bean.EditGroomBean;
import com.mayisports.qca.bean.SubmitGroomBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastPricePopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.text.DecimalFormat;

/**
 * 编辑推荐界面
 */
public class EditGroomActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener, AdapterView.OnItemClickListener, View.OnLayoutChangeListener {


    public static void start(Activity activity,String betId){
        Intent intent = new Intent(activity,EditGroomActivity.class);
        intent.putExtra("betId",betId);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_edit_groom;
    }


    private FrameLayout fl_view_root;
    private TextView tv_match_edit_groom;
    private TextView tv_host_name_edit_groom;
    private TextView tv_away_name_edit_groom;
    private TextView tv_time_edit_groom;

    private RelativeLayout rl_europe_edit_groom;
    private LinearLayout ll_europe_edit_groom;
    private TextView tv_left_europe_edit_groom;
    private TextView tv_m_europe_edit_groom;
    private TextView tv_right_europe_edit_groom;

    private RelativeLayout rl_asia_edit_groom;
    private LinearLayout ll_asia_edit_groom;
    private TextView tv_left_asia_edit_groom;
    private TextView tv_right_asia_edit_groom;


    private RelativeLayout rl_scroe_edit_groom;
    private LinearLayout ll_scroe_edit_groom;
    private TextView tv_left_scroe_edit_groom;
    private TextView tv_right_scroe_edit_groom;
    private LinearLayout ll_plus_edit_groom;
    private ImageView iv_icon_edit_groom;
    private GridView gv_edit_groom;
    private CheckBox cb_edit_groom;
    private EditText et_edit_groom;
    private EditText et_content_edit_groom;
    private TextView tv_submit_edit_groom;

    private RelativeLayout rl_load_layout;
    private LinearLayout ll_coin_edit_groom;

    private CheckBox cb_share_groom;

    private TextView tv_no_pan;
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
        tv_title.setText("编辑推荐");

        fl_view_root = findViewById(R.id.fl_view_root);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        tv_match_edit_groom = findViewById(R.id.tv_match_edit_groom);
        tv_host_name_edit_groom = findViewById(R.id.tv_host_name_edit_groom);
        tv_away_name_edit_groom = findViewById(R.id.tv_away_name_edit_groom);
        tv_time_edit_groom = findViewById(R.id.tv_time_edit_groom);

        tv_no_pan = findViewById(R.id.tv_no_pan);

        rl_europe_edit_groom = findViewById(R.id.rl_europe_edit_groom);
        ll_europe_edit_groom = findViewById(R.id.ll_europe_edit_groom);
        tv_left_europe_edit_groom = findViewById(R.id.tv_left_europe_edit_groom);
        tv_m_europe_edit_groom = findViewById(R.id.tv_m_europe_edit_groom);
        tv_right_europe_edit_groom = findViewById(R.id.tv_right_europe_edit_groom);
        rl_asia_edit_groom = findViewById(R.id.rl_asia_edit_groom);
        ll_asia_edit_groom = findViewById(R.id.ll_asia_edit_groom);
        tv_left_asia_edit_groom =findViewById(R.id.tv_left_asia_edit_groom);
        tv_right_asia_edit_groom = findViewById(R.id.tv_right_asia_edit_groom);
        rl_scroe_edit_groom = findViewById(R.id.rl_scroe_edit_groom);
        ll_scroe_edit_groom = findViewById(R.id.ll_scroe_edit_groom);
        tv_left_scroe_edit_groom = findViewById(R.id.tv_left_scroe_edit_groom);
        tv_right_scroe_edit_groom = findViewById(R.id.tv_right_scroe_edit_groom);
        ll_plus_edit_groom = findViewById(R.id.ll_plus_edit_groom);
        iv_icon_edit_groom = findViewById(R.id.iv_icon_edit_groom);
        gv_edit_groom = findViewById(R.id.gv_edit_groom);
        cb_edit_groom = findViewById(R.id.cb_edit_groom);
        et_edit_groom = findViewById(R.id.et_edit_groom);
        et_content_edit_groom = findViewById(R.id.et_content_edit_groom);
        tv_submit_edit_groom = findViewById(R.id.tv_submit_edit_groom);
        ll_coin_edit_groom = findViewById(R.id.ll_coin_edit_groom);
        cb_share_groom = findViewById(R.id.cb_share_groom);


        ll_plus_edit_groom.setOnClickListener(this);
        gv_edit_groom.setOnItemClickListener(this);
        tv_submit_edit_groom.setOnClickListener(this);

        screenHeight = this.getWindowManager().getDefaultDisplay().getHeight();
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        fl_view_root.addOnLayoutChangeListener(this);

        tv_right_scroe_edit_groom.setOnClickListener(this);
        tv_left_scroe_edit_groom.setOnClickListener(this);
        tv_right_asia_edit_groom.setOnClickListener(this);
        tv_left_asia_edit_groom.setOnClickListener(this);
        tv_left_europe_edit_groom.setOnClickListener(this);
        tv_m_europe_edit_groom.setOnClickListener(this);
        tv_right_europe_edit_groom.setOnClickListener(this);



        et_edit_groom.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(editGroomBean == null || editGroomBean.data == null || editGroomBean.data.match == null)return;
                EditText et = (EditText) v;
                if(hasFocus){
                    String string = et.getText().toString();
                    if(TextUtils.isEmpty(string)){
                        String host = URLDecoder.decode(editGroomBean.data.match.hostTeamName);
                        String away = URLDecoder.decode(editGroomBean.data.match.awayTeamName);
                        String s = host + "vs" + away + ":";
                        et.setText(s);
                    }
                }else{

                }
            }
        });

    }



    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ll_plus_edit_groom://金币选项
                boolean shown = ll_coin_edit_groom.isShown();
                if(shown){
                    ll_coin_edit_groom.setVisibility(View.GONE);
                    iv_icon_edit_groom.setImageResource(R.drawable.arri_top);
                }else{
                    ll_coin_edit_groom.setVisibility(View.VISIBLE);
                    iv_icon_edit_groom.setImageResource(R.drawable.arri_down);
                }
                break;
            case R.id.tv_left_asia_edit_groom:
            case R.id.tv_right_asia_edit_groom:
            case R.id.tv_left_scroe_edit_groom:
            case R.id.tv_right_scroe_edit_groom:
            case R.id.tv_left_europe_edit_groom:
            case R.id.tv_m_europe_edit_groom:
            case R.id.tv_right_europe_edit_groom:
                updatePan(view.getId());
                break;
            case R.id.tv_submit_edit_groom:
                requestSubmitDatas();
                break;
        }
    }

    /**
     * error_sql_insert into `user_recommendation`(match_time,betDetail,user_id,betId,price,return_if_wrong,create_time,title, reason)values(1517082300,'{"hostOdds":"0.9","tape":"2500"}',71640,1424817,,0,1516967198,'','')
     * 提交数据
     */
    private void requestSubmitDatas() {

        if(editGroomBean == null || editGroomBean.data ==null)return;

        if(!checkDatas()){
            if(editGroomBean.data.odds != null){
                ToastUtils.toast("请至少选择推荐内容");
            }else{
                ToastUtils.toast("该比赛暂无可用盘口，无法发布");
            }

            return;
        }

        final String url =Constant.BASE_URL + "php/api.php";

        final HttpParams params = new HttpParams();
        params.put("action","user_recommendation");
        params.put("type","submit");
        params.put("betId",betId);
        params.put("oddsType",oddsType);
        params.put("value",value);
        params.put("asiaTape",asiaTape);
        String reason = et_content_edit_groom.getText().toString().trim();
        params.put("reason",reason);
        String title = et_edit_groom.getText().toString();

        String host = URLDecoder.decode(editGroomBean.data.match.hostTeamName);
        String away = URLDecoder.decode(editGroomBean.data.match.awayTeamName);
        String s = host + "vs" + away + ":";
        if(title.equals(s)){
            title = "";
        }
        params.put("title",title);

        int ifReturn = cb_edit_groom.isChecked()?1:0;
        params.put("return_if_wrong",ifReturn);
        if(currentCheck.equals("-1")){
            String text = et_coin.getText().toString();
            if(TextUtils.isEmpty(text)){
                ToastUtils.toast("请填写金币");
                return;
            }else{
                Integer integer = Integer.valueOf(text);
                if(integer>500){
                    ToastUtils.toast("金币最多500哦");
                    return;
                }

                currentCheck = text;
            }
        }
        params.put("price",currentCheck);

        final int share = cb_share_groom.isChecked() ? 1 : 0;

        params.put("share",share+"");

        if((!TextUtils.isEmpty(reason) || !TextUtils.isEmpty(title)) && share == 0){//提示是否同步到动态
            String str = "你编辑了推荐理由,是否同步\n本条推荐至动态?";
            ToastPricePopuWindow toastPricePopuWindow = new ToastPricePopuWindow(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  if(v.getId() == R.id.tv_cancle_toast_price){//左边
                      //不同步
                      params.put("share",0+"");
                  }else if(v.getId() == R.id.tv_go_toast_price){//右边
                      //同步
                      params.put("share",1+"");
                  }

                  String urlParams = params.getUrlParams().toString();
                  Log.e("urlParams",urlParams);
                  RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,EditGroomActivity.this) {
                      @Override
                      public void onSucces(String string) {
                          SubmitGroomBean bean = JsonParseUtils.parseJsonClass(string,SubmitGroomBean.class);
                          if(bean != null &bean.data!=null){
                              if(bean.data.result == 1){
                                  ToastUtils.toast(bean.data.text);
                                  PublishSuccessGroomActivity.start(EditGroomActivity.this,betId);
                                  finish();
                              }else{
                                  ToastUtils.toast(bean.data.text);
                              }

                          }
                      }

                      @Override
                      public void onfailure(int errorNo, String strMsg) {
                          ToastUtils.toast(Constant.NET_FAIL_TOAST);
                      }
                  });


              }
          }, str, "不同步", "同步");
            toastPricePopuWindow.showAtLocation(tv_title, Gravity.CENTER, 0, 0);
            return;
        }

//http://app.mayisports.com/php/api.php?action=user_recommendation&type=submit&betId=1406823&oddsType=hostOdds&value=11200.000000000002&asiaTape=2500&reason=&price=0&return_if_wrong=0&title=%E6%96%AF%E5%9B%BE%E5%8A%A0%E7%89%B9vs%E6%B3%95%E5%85%B0%E5%85%8B%E7%A6%8F%EF%BC%9A123&share=0
        String urlParams = params.getUrlParams().toString();
        Log.e("urlParams",urlParams);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                SubmitGroomBean bean = JsonParseUtils.parseJsonClass(string,SubmitGroomBean.class);
                if(bean != null &bean.data!=null){
                    if(bean.data.result == 1){
                        ToastUtils.toast(bean.data.text);
                        PublishSuccessGroomActivity.start(EditGroomActivity.this,betId);
                        finish();
                    }else{
                        ToastUtils.toast(bean.data.text);
                    }

                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });


    }

    private boolean checkDatas(){
        if(!TextUtils.isEmpty(oddsType))return true;
        return false;
    }

    /**
     * 刷新盘口
     * /php/api.php?action=user_recommendation&type=submit&betId=1424808&oddsType=drowOdds&value=33000&asiaTape=&reason=&price=18&return_if_wrong=0&title=
     * 1亚盘
     * 'hostOdds' 'awayOdds' 'winOdds' 'drowOdds' 'loseOdds' 'smallScoreOdds' 'bigScoreOdds'
     */
    private String oddsType;
    private String value;
    private String asiaTape;
    private void updatePan(int resID){
        if(editGroomBean == null || editGroomBean.data == null)return;

        ll_asia_edit_groom.setBackgroundResource(R.drawable.tuijiandan_empty_2);
        ll_europe_edit_groom.setBackgroundResource(R.drawable.tuijiandan_empty_3);
        ll_scroe_edit_groom.setBackgroundResource(R.drawable.tuijiandan_empty_2);


        EditGroomBean.DataBean.OddsBean odds = editGroomBean.data.odds;
        switch (resID){
            case R.id.tv_left_asia_edit_groom:
                ll_asia_edit_groom.setBackgroundResource(R.drawable.fangandan_2_left);
                oddsType = "hostOdds";
                value = odds.asia.hostOdds*10000+"";
                asiaTape = odds.asia.tape;
                break;
            case R.id.tv_right_asia_edit_groom:
                ll_asia_edit_groom.setBackgroundResource(R.drawable.fangandan_2_rig);
                oddsType = "awayOdds";
                value = odds.asia.awayOdds*10000+"";
                asiaTape = odds.asia.tape;
                break;
            case R.id.tv_left_scroe_edit_groom:
                ll_scroe_edit_groom.setBackgroundResource(R.drawable.fangandan_2_left);
                oddsType = "bigScoreOdds";
                value = odds.score.hostOdds;  //awayOdds
                asiaTape = odds.score.tape;
                break;
            case R.id.tv_right_scroe_edit_groom:
                ll_scroe_edit_groom.setBackgroundResource(R.drawable.fangandan_2_rig);
                oddsType = "smallScoreOdds";
                value = odds.score.awayOdds;
                asiaTape = odds.score.tape;

                break;
            case R.id.tv_left_europe_edit_groom:
                ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_left);
                oddsType = "winOdds";
                value = odds.europeOdds.winOdds+"";
                asiaTape = "";
                break;
            case R.id.tv_m_europe_edit_groom:
                ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_m);
                oddsType = "drowOdds";
                value = odds.europeOdds.drowOdds+"";
                asiaTape = "";
                break;
            case R.id.tv_right_europe_edit_groom:
                ll_europe_edit_groom.setBackgroundResource(R.drawable.fangandan_3_righ);
                oddsType = "loseOdds";
                value = odds.europeOdds.loseOdds+"";
                asiaTape = "";
                break;
        }

    }

    private String betId;
    @Override
    protected void initDatas() {
        super.initDatas();
        betId = getIntent().getStringExtra("betId");
        requestNetDatas();
    }

    private EditGroomBean editGroomBean;
    private void requestNetDatas() {
        //om/php/api.php?action=user_recommendation&type=matchDetail&betId=1432784&from=1
        String url = Constant.BASE_URL +"php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user_recommendation");
        params.put("type","matchDetail");
        params.put("betId",betId);
        params.put("from",1);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                editGroomBean = JsonParseUtils.parseJsonClass(string,EditGroomBean.class);
                updateView();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
               ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }


    /**
     * 刷新View
     */
    private DecimalFormat df = new DecimalFormat("#.00");
    private void updateView() {
        if(editGroomBean != null && editGroomBean.data != null){
            EditGroomBean.DataBean data = editGroomBean.data;
            tv_match_edit_groom.setText(data.match.leagueName);

            String host = URLDecoder.decode(data.match.hostTeamName);
            tv_host_name_edit_groom.setText(host);
            String away = URLDecoder.decode(data.match.awayTeamName);
            tv_away_name_edit_groom.setText(away);

            Long aLong = Long.valueOf(data.match.timezoneoffset + "000");
            String matchStartTime = Utils.getMatchStartTime(aLong);
            tv_time_edit_groom.setText(matchStartTime);

            if(editGroomBean.data.odds != null) {

                tv_no_pan.setVisibility(View.GONE);

                if (editGroomBean.data.odds.europeOdds != null) {
                    rl_europe_edit_groom.setVisibility(View.VISIBLE);
//                    ll_result_pan.setBackgroundDrawable(getResources().getDrawable(R.drawable.fangandan_3_m));
                    double v = editGroomBean.data.odds.europeOdds.winOdds / 10000.0;
                    tv_left_europe_edit_groom.setText("主胜   " + df.format(v));
                    double v1 = editGroomBean.data.odds.europeOdds.drowOdds / 10000.0;
                    tv_m_europe_edit_groom.setText("平局   " + df.format(v1));
                    double v2 = Integer.valueOf(editGroomBean.data.odds.europeOdds.loseOdds) / 10000.0;
                    tv_right_europe_edit_groom.setText("客胜   " + df.format(v2));
                } else {
                    rl_europe_edit_groom.setVisibility(View.GONE);
                }


                if (editGroomBean.data.odds.asia != null) {
                    rl_asia_edit_groom.setVisibility(View.VISIBLE);
                    EditGroomBean.DataBean.OddsBean odds = editGroomBean.data.odds;

                    String format = df.format(odds.asia.hostOdds + 1);
                    String s0 = Utils.parseOddOfHandicap(odds.asia.tape) + "   " + format;
                    tv_left_asia_edit_groom.setText(s0);
                    String a = "";
                    if (odds.asia.tape.contains("-")) {
                        a = odds.asia.tape;
                        a = a.replace("-", "");
                    } else {
                        a = "-" + odds.asia.tape;
                    }
                    String s1 = "" + Utils.parseOddOfHandicap(a) + "   " + df.format(Double.valueOf(odds.asia.awayOdds) + 1);
                    tv_right_asia_edit_groom.setText(s1);
                } else {
                    rl_asia_edit_groom.setVisibility(View.GONE);
                }


                if (editGroomBean.data.odds.score != null) {
                    rl_scroe_edit_groom.setVisibility(View.VISIBLE);
                    EditGroomBean.DataBean.OddsBean odds = editGroomBean.data.odds;
                    String s0 = "大于" + Utils.parseOddOfHandicap(odds.score.tape) + "   " + df.format(Double.valueOf(odds.score.hostOdds) / 10000 + 1);
                    tv_left_scroe_edit_groom.setText(s0);
                    String s1 = "小于" + Utils.parseOddOfHandicap(odds.score.tape) + "   " + df.format(Double.valueOf(odds.score.awayOdds) / 10000 + 1);
                    tv_right_scroe_edit_groom.setText(s1);
                } else {
                    rl_scroe_edit_groom.setVisibility(View.GONE);
                }

            }else{//
                rl_europe_edit_groom.setVisibility(View.GONE);
                rl_asia_edit_groom.setVisibility(View.GONE);
                rl_scroe_edit_groom.setVisibility(View.GONE);
                tv_no_pan.setVisibility(View.VISIBLE);
            }
                //填充金币

            currentCheck = editGroomBean.data.standard_price+"";
            gv_edit_groom.setAdapter(myAdapter);


        }
    }

    private String[] coins = {"免费,0","1金币,1","5金币,5","10金币,10","18金币,18","28金币,28","其他金额,-1"};
    private MyAdapter myAdapter = new MyAdapter();
    private String currentCheck = "";

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private View editView;
    private EditText   et_coin;
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return coins.length;
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

            final String[] split = coins[position].split(",");
            if(position == getCount()-1) {
                if (editView == null){
                    editView = View.inflate(getBaseContext(), R.layout.edit_layout_publish_point, null);
                    convertView = editView;
                  }else {
                    convertView = editView;
                 }
                et_coin = convertView.findViewById(R.id.et_coin);
                if(split[1].equals(currentCheck)){
                    et_coin.setBackgroundResource(R.drawable.shape_yellow_stroke_small);
//                    et_coin.requestFocus();
                }else {
                    et_coin.setBackgroundResource(R.drawable.shape_gray_storke);
                    et_coin.clearFocus();
                    et_coin.setText("");
                }
                et_coin.setHint(split[0]);
                et_coin.setOnFocusChangeListener(new android.view.View.
                        OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            // 此处为得到焦点时的处理内容
                            currentCheck = split[1];
                            notifyDataSetChanged();

                        }

                    }
                });
            }else {
                convertView = View.inflate(getBaseContext(), R.layout.checkbox_layout_publish_point, null);
                final TextView name = convertView.findViewById(R.id.tv_name_checkbox);
                final TextView count = convertView.findViewById(R.id.tv_count_checkbox);
                name.setGravity(Gravity.CENTER);
                CheckBox checkBox = convertView.findViewById(R.id.cb);
                name.setText(split[0]);


                if (split[1].equals(currentCheck)) {
                    checkBox.setChecked(true);
                    name.setTextColor(Color.parseColor("#282828"));
                    count.setTextColor(Color.parseColor("#282828"));
                } else {
                    checkBox.setChecked(false);
                    name.setTextColor(Color.parseColor("#999999"));
                    count.setTextColor(Color.parseColor("#999999"));
                }


                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            currentCheck = split[1];
                            name.setTextColor(Color.parseColor("#282828"));
                            count.setTextColor(Color.parseColor("#282828"));
                        }
//                       EditGroomActivity.this.gv_edit_groom.setAdapter(myAdapter);
                        KeyBoardUtils.closeKeybord(EditGroomActivity.this.tv_m_europe_edit_groom,EditGroomActivity.this);
                        notifyDataSetChanged();
                    }
                });

            }
            return convertView;
        }
    }

    @Override
    public void onReload() {

    }


    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;


    /**
     * 监听键盘
     */
    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){
////          ToastUtils.toast("键盘弹起");
//            if(currentCheck.equals("-1")) {
//                String coin = coins[coins.length - 1];
//                currentCheck = coin.split(",")[1];
//                myAdapter.notifyDataSetChanged();
//            }
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){
//            ToastUtils.toast("键盘关闭");
        }
    }
}
