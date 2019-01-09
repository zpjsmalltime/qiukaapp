package com.mayi.mayisports.activity;

import android.view.View;

import com.mayi.mayisports.R;

/**
 * 关于我们界面
 */
public class AboutMeActivity extends BaseActivity {



    @Override
    protected int setViewForContent() {
        return R.layout.activity_about_me;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("关于我们");
        iv_left_title.setOnClickListener(this);
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
