package com.mayisports.qca.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.mayi.mayisports.R;
import com.mayisports.qca.utils.PictureUtils;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 提示绑定手机号弹窗
 * Created by Zpj on 2018/3/23.
 */

public class BindPhonePopuWindow extends PopupWindow {

    private TextView tv_content_toast_msg;
    private TextView tv_ok_toast_msg;

    private ImageView iv_ad;


    private TextView tv_to_bind_phone;
    private TextView tv_dismiss;


    public BindPhonePopuWindow(View.OnClickListener paramOnClickListener, String title, String imgUrl){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.bind_num_layout, null);
        tv_content_toast_msg = view.findViewById(R.id.tv_content_toast_msg);
//        tv_content_toast_msg.setOnClickListener(paramOnClickListener);
        tv_ok_toast_msg = view.findViewById(R.id.tv_ok_toast_msg);
       // tv_ok_toast_msg.setOnClickListener(paramOnClickListener);


        tv_to_bind_phone = view.findViewById(R.id.tv_to_bind_phone);
        tv_to_bind_phone.setOnClickListener(paramOnClickListener);

        tv_dismiss = view.findViewById(R.id.tv_dismiss);

        iv_ad = view.findViewById(R.id.iv_ad);
        iv_ad.setOnClickListener(paramOnClickListener);

        PictureUtils.showRounded(imgUrl,iv_ad);

//        tv_content_toast_msg.setText(title);
        setFocusable(true);
        setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        setBackgroundDrawable(new BitmapDrawable());

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(view);

        tv_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }


}
