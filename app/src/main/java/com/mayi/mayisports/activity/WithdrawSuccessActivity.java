package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.mayi.mayisports.R;

/**
 * 关于我们界面
 */
public class WithdrawSuccessActivity extends BaseActivity {


    public static void start(Activity activity,String count,String account){
        Intent intent = new Intent(activity,WithdrawSuccessActivity.class);
        intent.putExtra("count",count);
        intent.putExtra("account",account);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_withdraw_success;
    }


    private TextView tv_count;
    private TextView tv_account;

    @Override
    protected void initView() {
        super.initView();
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("提现");
        iv_left_title.setOnClickListener(this);

        tv_count = findViewById(R.id.tv_count);
        tv_account = findViewById(R.id.tv_account);
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        Intent intent = getIntent();
        tv_count.setText( intent.getStringExtra("count"));
        tv_account.setText(intent.getStringExtra("account"));


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
}
