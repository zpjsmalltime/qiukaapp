package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyboardShortcutGroup;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.EditNickNameBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastMsgPopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLEncoder;


/**
 * 修改昵称界面
 *
 * 提交前本地校验
 * 不大于8位
 * 不为空  不与当前name 相同
 */
public class EditNicknameActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {

    public static void start(Activity activity,String nickName){
        Intent intent = new Intent(activity,EditNicknameActivity.class);
        intent.putExtra("nickName",nickName);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_edit_nickname;
    }

    private EditText et_edit_nickname;
    private RelativeLayout rl_load_layout;
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        tv_ritht_title.setText("完成");
        tv_ritht_title.setOnClickListener(this);

        tv_title.setText("修改昵称");
        iv_left_title.setOnClickListener(this);

        et_edit_nickname = findViewById(R.id.et_edit_nickname);
        rl_load_layout = findViewById(R.id.rl_load_layout);
    }

    private String nickName;
    @Override
    protected void initDatas() {
        super.initDatas();
        nickName = getIntent().getStringExtra("nickName");
        if(TextUtils.isEmpty(nickName))nickName = "";
        if(nickName.length()>10)nickName = nickName.substring(0,10);
        et_edit_nickname.setText(nickName);
        et_edit_nickname.setSelection(nickName.length());
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
            case R.id.tv_ok_toast_msg:
                if(toastMsgPopuWindow != null){
                    toastMsgPopuWindow.dismiss();
                    finish();
                }
                break;
        }
    }

    private EditNickNameBean editNickNameBean;
    private void submitData() {

        KeyBoardUtils.closeKeybord(et_edit_nickname,this);

        //http://app.mayisports.com/php/api.php?action=user&date=&type=modify&value=[%22name%22,%22%E8%A7%84%E5%BE%8B%22]
        String et = et_edit_nickname.getText().toString().trim();
        if(TextUtils.isEmpty(et)){
            ToastUtils.toast("不能为空");
            return;
        }

        if(et.equals(nickName)){
            ToastUtils.toast("昵称未改变");
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("date","");
        params.put("type","modify");
        String encode = URLEncoder.encode(et);
        params.put("value","[\"name\",\""+encode+"\"]");
        String s = params.getUrlParams().toString();
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                 editNickNameBean = JsonParseUtils.parseJsonClass(string,EditNickNameBean.class);
                 if(editNickNameBean != null && editNickNameBean.status != null){
                    if(editNickNameBean.status.errno == 0){
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
        toastMsgPopuWindow.showAtLocation(et_edit_nickname, Gravity.CENTER,0,0);
    }
}
