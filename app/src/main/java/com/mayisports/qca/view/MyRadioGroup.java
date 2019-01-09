package com.mayisports.qca.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

/**
 * Created by Zpj on 2018/2/6.
 */

public class MyRadioGroup extends RadioGroup {

    private OnClickButton onClickButton;
    public void setOnClickButton(OnClickButton onClickButton){
        this.onClickButton = onClickButton;
    }
    public MyRadioGroup(Context context) {
        super(context);
    }

    public MyRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void check(int id) {
        if(onClickButton != null){
            onClickButton.onClickButton(id);
        }
        super.check(id);
    }
    public interface OnClickButton{
        void onClickButton(int id);
    }
}
