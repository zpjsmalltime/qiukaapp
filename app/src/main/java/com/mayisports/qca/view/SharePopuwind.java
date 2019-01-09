package com.mayisports.qca.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.utils.DisplayUtil;

import java.util.List;

/**
 * 打赏提示框
 * Created by Zpj on 2017/12/15.
 */

public class SharePopuwind extends PopupWindow implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_share_fg;
    private TextView tv_cancle;


    /**
     * jsApiList: ['onMenuShareTimeline',
     * 'onMenuShareAppMessage',
     * 'onMenuShareQQ',
     * 'onMenuShareWeibo',
     * 'onMenuShareQZone']
     * @param jsApiList
     */
    public void setShowType(List<String> jsApiList){
        if(jsApiList == null || view == null)return;

        view.findViewById(R.id.rb_4_share_fg).setVisibility(View.GONE);
        view.findViewById(R.id.rb_3_share_fg).setVisibility(View.GONE);
        view.findViewById(R.id.rb_1_share_fg).setVisibility(View.GONE);
        view.findViewById(R.id.rb_0_share_fg).setVisibility(View.GONE);
        view.findViewById(R.id.rb_2_share_fg).setVisibility(View.GONE);

        for(int i = 0;i<jsApiList.size();i++){
            String s = jsApiList.get(i);
            switch (s){
                case "onMenuShareTimeline":
                    view.findViewById(R.id.rb_4_share_fg).setVisibility(View.VISIBLE);
                    break;
                case "onMenuShareAppMessage":
                    view.findViewById(R.id.rb_3_share_fg).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.rb_3_share_fg).setTag("onMenuShareAppMessage");
                    break;
                case "onMenuShareQQ":
                    view.findViewById(R.id.rb_1_share_fg).setVisibility(View.VISIBLE);
                    break;
                case "onMenuShareWeibo":
                    view.findViewById(R.id.rb_0_share_fg).setVisibility(View.VISIBLE);
                    break;
                case "onMenuShareQZone":
                    view.findViewById(R.id.rb_2_share_fg).setVisibility(View.VISIBLE);
                    break;
                case "miniProgram":
                    view.findViewById(R.id.rb_3_share_fg).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.rb_3_share_fg).setTag("miniProgram");
                    break;
            }
        }
    }

    public static class ShareInfoBean{
        public String title;
        public String text;
        public String url;
        public String imageUrl;
    }

    private ShareInfoBean shareInfoBean;
    private ShareTypeClickListener shareTypeClickListener;

    private View view;
    public SharePopuwind(Activity paramActivity, final ShareTypeClickListener shareTypeClickListener, ShareInfoBean shareInfoBean){
        super(paramActivity);
        this.shareInfoBean = shareInfoBean;
        this.shareTypeClickListener = shareTypeClickListener;
        view = View.inflate(paramActivity,R.layout.share_layout, null);
        rg_share_fg = view.findViewById(R.id.rg_share_fg);
        rg_share_fg.setOnCheckedChangeListener(this);
        tv_cancle = view.findViewById(R.id.tv_cancle);
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int type = -1;
        switch (i){
            case R.id.rb_0_share_fg:
                 type = 1;
                break;
            case R.id.rb_1_share_fg:
                type = 2;
                break;
            case R.id.rb_2_share_fg:
                type = 3;
                break;
            case R.id.rb_3_share_fg:
               Object tag =  view.findViewById(R.id.rb_3_share_fg).getTag();
               if(tag == null){
                   type = 4;
                   break;
               }
               String ta = (String) tag;
                if(ta.equals("miniProgram")){
                    type = 6; //小程序
                }else{
                    type = 4;
                }


                break;
            case R.id.rb_4_share_fg:
                type = 5;
                break;
        }

        if(shareTypeClickListener != null){
            shareTypeClickListener.onTypeClick(type);
        }
        dismiss();
    }

    public interface  ShareTypeClickListener{
        void onTypeClick(int count);
    }


}
