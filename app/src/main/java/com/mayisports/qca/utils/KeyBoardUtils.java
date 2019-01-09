package com.mayisports.qca.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘相关工具类
 */
public class KeyBoardUtils {

	
	 /** 
     * 打开软键盘 
     *  
     * @param mEditText 
     *            输入框 
     * @param mContext 
     *            上下文 
     */  
    public static void openKeybord(EditText mEditText, Context mContext)
    {  
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }  
  
    /** 
     * 关闭软键盘 
     *  
     * @param mEditText 
     *            输入框 
     * @param mContext 
     *            上下文 
     */  
    public static void closeKeybord(View mEditText, Context mContext)
    {  
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);  
    } 
}
