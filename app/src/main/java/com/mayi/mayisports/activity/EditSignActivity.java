package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.mayi.mayisports.R;
import com.mayisports.qca.bean.EditNickNameBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastMsgPopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;

/**
 * 编辑提交个性签名界面
 */
public class EditSignActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {

    public static void start(Activity activity,String sign){
        Intent intent = new Intent(activity,EditSignActivity.class);
        intent.putExtra("sign",sign);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_edit_sign;
    }

    private EditText et_edit_sign;
    private RelativeLayout rl_load_layout;
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_ritht_title.setText("完成");
        tv_ritht_title.setOnClickListener(this);

        tv_title.setText("个性签名");
        iv_left_title.setOnClickListener(this);

        et_edit_sign = findViewById(R.id.et_edit_sign);
        rl_load_layout = findViewById(R.id.rl_load_layout);
    }

    private String sign;

    @Override
    protected void initDatas() {
        super.initDatas();
        sign = getIntent().getStringExtra("sign");
        if(sign == null)sign = "";
        et_edit_sign.setText(sign);
        et_edit_sign.setSelection(sign.length());

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_left_title:
                finish();
                break;
            case R.id.tv_ritht_title:
                submitData();
                break;
        }
    }


    private EditNickNameBean editNickNameBean;
    private void submitData() {

        KeyBoardUtils.closeKeybord(et_edit_sign,this);

        String et = et_edit_sign.getText().toString().trim();
        if(TextUtils.isEmpty(et)){
            ToastUtils.toast("不能为空");
            return;
        }

        if(et.equals(sign)){
            ToastUtils.toast("签名未改变");
            return;
        }
        //http://app.mayisports.com/php/api.php?action=user&type=modify_intro&value=干干脆脆
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","modify_intro");
        String encode = URLEncoder.encode(et);
        params.put("value",encode);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                editNickNameBean = JsonParseUtils.parseJsonClass(string,EditNickNameBean.class);
                if(editNickNameBean != null && editNickNameBean.status != null){
                    if(editNickNameBean.status.result == 1){
                        ToastUtils.toast("修改成功");
                        finish();
                    }else{
                        showToast();
                    }
                }else{
                    ToastUtils.toast("错误，请重试");
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

    private ToastMsgPopuWindow toastMsgPopuWindow;
    private void showToast(){
        toastMsgPopuWindow = new ToastMsgPopuWindow(this,editNickNameBean.status.errstr);
        toastMsgPopuWindow.showAtLocation(et_edit_sign, Gravity.CENTER,0,0);
    }


}
