package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.WithdrawCoinBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

/**
 * 提现界面
 */
public class WithdrawCoinActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {

    public static void start(Activity activity){
        Intent intent = new Intent(activity,WithdrawCoinActivity.class);
        activity.startActivity(intent);

    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_withdraw_coin;
    }

    private TextView tv_balance_coin;
    private EditText et_withdraw_count;
    private EditText et_number_withdraw;
    private EditText et_name_withdraw;
    private TextView tv_withdraw_coin;

    private RelativeLayout rl_load_layout;

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_title.setText("提现");
        iv_left_title.setOnClickListener(this);
        tv_ritht_title.setVisibility(View.INVISIBLE);

        tv_balance_coin = findViewById(R.id.tv_balance_coin);
        et_withdraw_count = findViewById(R.id.et_withdraw_count);
        et_number_withdraw = findViewById(R.id.et_number_withdraw);
        et_name_withdraw = findViewById(R.id.et_name_withdraw);
        tv_withdraw_coin = findViewById(R.id.tv_withdraw_coin);
        tv_withdraw_coin.setOnClickListener(this);

        rl_load_layout = findViewById(R.id.rl_load_layout);
    }


    @Override
    protected void initDatas() {
        super.initDatas();
        String string = SPUtils.getString(QCaApplication.getContext(), Constant.WITHDRAW_COIN);
        tv_balance_coin.setText(string);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_left_title:
                finish();
                break;
            case R.id.tv_withdraw_coin:
                withdrawSubmit();
                break;
        }
    }


    private void withdrawSubmit() {
        String count = et_withdraw_count.getText().toString();
        if(TextUtils.isEmpty(count)){
            ToastUtils.toast("请填写提现金额");
            return;
        }

        final Integer countInt = Integer.valueOf(count);
        if(countInt<20){
            ToastUtils.toast("至少20金币");
            return;
        }

        final double aDouble = Double.valueOf(SPUtils.getString(QCaApplication.getContext(), Constant.WITHDRAW_COIN));
        if(countInt>aDouble){
            ToastUtils.toast("金币不足");
            return;
        }

        final String number = et_number_withdraw.getText().toString();
        if(TextUtils.isEmpty(number)){
            ToastUtils.toast("请填写账号");
            return;
        }
        String name = et_name_withdraw.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.toast("请填写姓名");
            return;
        }


        /**
         * key value 一致，缩写
         */
        //{action: 'user', type: 'user_enchashment', count: cashValue, value: account, accountName}
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","user_enchashment");
        params.put("count",countInt);
        params.put("value",number);
        params.put("accountName",name);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                WithdrawCoinBean withdrawCoinBean = JsonParseUtils.parseJsonClass(string,WithdrawCoinBean.class);
                if(withdrawCoinBean != null){
                    if(withdrawCoinBean.status.errno == 0){
                        try {
                            double v = aDouble - countInt;
                            SPUtils.putString(QCaApplication.getContext(), Constant.WITHDRAW_COIN, v + "");
                            String coin = SPUtils.getString(QCaApplication.getContext(), Constant.COIN);
                            double v1 = Double.valueOf(coin) - countInt;
                            SPUtils.putString(QCaApplication.getContext(), Constant.COIN, v1 + "");
                        }catch (Exception e){

                        }
                        WithdrawSuccessActivity.start(WithdrawCoinActivity.this,countInt+"元",number);
                        finish();
                    }else{
                        ToastUtils.toast(withdrawCoinBean.status.errstr);
                    }
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
}
