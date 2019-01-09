package com.mayisports.qca.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.GuessToastItemBean;

import java.util.List;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 竞猜大赛选项
 * Created by Zpj on 2017/12/14.
 */

public class GuessCompetPopuWindow extends PopupWindow implements AdapterView.OnItemClickListener {


    private TextView tv_content_toast_price;
    private TextView tv_cancle_toast_price;
    private LinearLayout top;

    private List<GuessToastItemBean.DataBean> datas;

    private Activity activity;
    private ListView lv_guess_toast;


    public interface  OnItemCheck{
        void onItemCheck(GuessToastItemBean.DataBean bean);
    }

    private OnItemCheck onItemCheck;
    public void setOnItemCheck(OnItemCheck onItemCheck){
        this.onItemCheck = onItemCheck;
    }


    public GuessCompetPopuWindow(Activity paramActivity, List<GuessToastItemBean.DataBean> data,OnItemCheck onItemCheck){
        this.activity = paramActivity;
        this.onItemCheck = onItemCheck;
        this.datas = data;
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.toast_guess_compet, null);
        lv_guess_toast = view.findViewById(R.id.lv_guess_toast);
        lv_guess_toast.setOnItemClickListener(this);


        tv_content_toast_price = view.findViewById(R.id.tv_content_toast_price);
        tv_cancle_toast_price = view.findViewById(R.id.tv_cancle_toast_price);
        top = view.findViewById(R.id.top);




        if(datas != null){

            lv_guess_toast.setAdapter(new MyAdapter());
        }



//        tv_content_toast_price.setText(title);
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
        tv_cancle_toast_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


    private int select;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          select = position;
          if(onItemCheck != null){
              onItemCheck.onItemCheck(datas.get(position));
          }

          dismiss();
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return datas.size();
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
            convertView = View.inflate(QCaApplication.getContext(),R.layout.guess_toast_item,null);
            TextView tv_content = convertView.findViewById(R.id.tv_content);
            GuessToastItemBean.DataBean dataBean = datas.get(position);
            String str = "第"+dataBean.match_index+"期("+dataBean.date+")";
            tv_content.setText(str);

            if(position == select){
                tv_content.setBackground(QCaApplication.getContext().getResources().getDrawable(R.drawable.guess_toast_shape));
            }

            return convertView;
        }
    }
}
