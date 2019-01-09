package com.mayi.mayisports.activity;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mayi.mayisports.R;
import com.mayisports.qca.bean.RewardBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastMsgPopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;

/**
 * 意见反馈界面
 */
public class ReplayActivity extends BaseActivity {


    private EditText et_content_replay;
    private TextView tv_submit_replay;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_replay;
    }

    @Override
    protected void initView() {
        super.initView();
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("意见反馈");
        iv_left_title.setOnClickListener(this);
        et_content_replay = findViewById(R.id.et_content_replay);
        tv_submit_replay = findViewById(R.id.tv_submit_replay);
        tv_submit_replay.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.iv_left_title:
                finish();
                break;
            case R.id.tv_submit_replay:
                submit();
                break;
        }
    }

    private void submit() {
        String trim = et_content_replay.getText().toString().trim();
        if(trim.length()<5){
            showToast("反馈的内容字数不得少于5个",0);
            return;
        }


        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","feedback");
        String value = URLEncoder.encode(trim);
        params.put("value",value);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                RewardBean rewardBean = JsonParseUtils.parseJsonClass(string,RewardBean.class);
                if(rewardBean != null && rewardBean.status != null  && rewardBean.status.result == 1){
                    showToast("提交成功,感谢您的参与！",1);
                }else{
                    showToast("提交失败",0);
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private ToastMsgPopuWindow toastMsgPopuWindow;
    private void showToast(String msg, final int finishType){
        toastMsgPopuWindow = new ToastMsgPopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(finishType == 1){
                    finish();
                }else{
                    toastMsgPopuWindow.dismiss();
                }
            }
        }, msg + "");
        toastMsgPopuWindow.showAtLocation(et_content_replay, Gravity.CENTER,0,0);
    }
}
