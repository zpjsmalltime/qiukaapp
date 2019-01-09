package com.mayi.mayisports.activity;

import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.mayi.mayisports.R;


/**
 * 多类型，流式布局
 */
public class MutiTypeHomeActivity extends BaseActivity {



    @Override
    protected int setViewForContent() {
        return R.layout.activity_muti_type_home;
    }

    private ListView lv_home_fg;
    private XRefreshView xfv_home_fg;

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_title.setText("Demo首页");
        lv_home_fg = findViewById(R.id.lv_home_fg);
        xfv_home_fg = findViewById(R.id.xfv_home_fg);
        xfv_home_fg.setXRefreshViewListener(new MyRefreshListener());

    }

    class MyRefreshListener extends XRefreshView.SimpleXRefreshListener{
        @Override
        public void onRefresh(boolean isPullDown) {
            super.onRefresh(isPullDown);


        }
    }
}
