package com.mayisports.qca.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mayi.mayisports.R;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 打赏提示框
 * Created by Zpj on 2017/12/15.
 */

public class RewardPopuwind extends PopupWindow implements AdapterView.OnItemClickListener {

    private TextView tv_balance_reward;
    private TextView tv_ok_reward;
    private GridView gv_reward;
    private MyAdapter myAdapter;

    public RewardPopuwind(Activity paramActivity, final CoinCheckListener coinCheckListener, String coin){
        super(paramActivity);
        View view = View.inflate(paramActivity,R.layout.reward_toast_layout, null);
        gv_reward = view.findViewById(R.id.gv_reward);
        tv_balance_reward = view.findViewById(R.id.tv_balance_reward);
        tv_balance_reward.setText(coin+"金币");
        tv_ok_reward = view.findViewById(R.id.tv_ok_reward);
        tv_ok_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(coinCheckListener != null){
                    Integer count = Integer.valueOf(coins[checkPosition].replace("金币", ""));
                    coinCheckListener.onCoinCheck(count);
                }
            }
        });
        myAdapter = new MyAdapter();
        gv_reward.setAdapter(myAdapter);
        gv_reward.setOnItemClickListener(this);



        setFocusable(true);
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(checkPosition == i){
            return;
        }
        checkPosition = i;
        myAdapter.notifyDataSetChanged();
    }


    public interface  CoinCheckListener{
        void onCoinCheck(int count);
    }

    private int checkPosition;
    private String[] coins = {"1金币","8金币","18金币","28金币","88金币","188金币"};
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return coins.length;
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
            String coin = coins[i];
            View layout = View.inflate(getContext(),R.layout.coin_layout,null);
            TextView tv = layout.findViewById(R.id.tv);
            tv.setText(coin);
            if(checkPosition == i){
                tv.setBackground(getContext().getResources().getDrawable(R.drawable.reward_check));
            }else{
                tv.setBackground(getContext().getResources().getDrawable(R.drawable.reward_bg));
            }
            return layout;
        }
    }
}
