package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.fragment.MineFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 设置偏好
 */
public class SetPrefrencesActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,SetPrefrencesActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_set_prefrences;
    }

    private CheckBox cb_football_set_prefrences;
    private CheckBox cb_lottery_set_prefrences;
    private TextView tv_submit_set_prefrences;
    private RelativeLayout rl_load_layout;

    private ImageView iv_back;

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);

        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_title.setText("请选择您的偏好");
        tv_ritht_title.setVisibility(View.INVISIBLE);

        cb_football_set_prefrences = findViewById(R.id.cb_football_set_prefrences);
        cb_lottery_set_prefrences = findViewById(R.id.cb_lottery_set_prefrences);
        tv_submit_set_prefrences = findViewById(R.id.tv_submit_set_prefrences);
        tv_submit_set_prefrences.setOnClickListener(this);

        rl_load_layout = findViewById(R.id.rl_load_layout);


        boolean football = SPUtils.getBoolean(QCaApplication.getContext(), Constant.FOOTBALL);
        boolean lottery = SPUtils.getBoolean(QCaApplication.getContext(),Constant.LOTTERY);
        if(!football && !lottery)return;

        cb_football_set_prefrences.setChecked(football);
        cb_lottery_set_prefrences.setChecked(lottery);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_submit_set_prefrences:
                submitDatas();
                break;
        }
    }

    private void submitDatas() {
        if(!cb_football_set_prefrences.isChecked() && !cb_lottery_set_prefrences.isChecked()){
            ToastUtils.toast("至少选择一项偏好");
            return;
        }
        //http://app.mayisports.com/php/api.php?action=user&type=sports_category&value=1,3
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","sports_category");
        String value = "";

        if(cb_football_set_prefrences.isChecked()){
            value += "1";
            SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,true);
        }else{
            SPUtils.putBoolean(QCaApplication.getContext(),Constant.FOOTBALL,false);
        }
        if(cb_lottery_set_prefrences.isChecked()){
            if(cb_football_set_prefrences.isChecked()) {
                value += ","+"2";
            }else{
                value += "2";
            }
            SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,true);
        }else{
            SPUtils.putBoolean(QCaApplication.getContext(),Constant.LOTTERY,false);
        }

        params.put("value",value);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                Intent intent1 = new Intent(MineFragment.MINE_FG_ACTION);
                intent1.putExtra(RESULT, 4);
                LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent1);
                //关闭设置页
                Intent intent2 = new Intent(SettingActivity.MINE_FG_ACTION);
                intent2.putExtra(RESULT, 1);
                LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent2);

                finish();
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast("提交失败");
            }
        });

    }

    @Override
    public void onReload() {

    }
}
