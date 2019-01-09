package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.PublishPointBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 评论&回复评论界面
 */
public class ReplayCommentsActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener {

    public static void start(Activity activity,String replayId,String userName,String commentsId){
        Intent intent = new Intent(activity,ReplayCommentsActivity.class);
        intent.putExtra("replayId",replayId);
        intent.putExtra("commentsId",commentsId);
        intent.putExtra("userName",userName);
        intent.putExtra("type",2);
        activity.startActivity(intent);
    }

    public static void start(Activity activity,String rootReplyId,String replayId,String userName,String commentsId,int reqeustCode){
        Intent intent = new Intent(activity,ReplayCommentsActivity.class);
        intent.putExtra("rootReplyId",rootReplyId);
        intent.putExtra("replayId",replayId);
        intent.putExtra("commentsId",commentsId);
        intent.putExtra("userName",userName);
        intent.putExtra("type",2);
        activity.startActivityForResult(intent,reqeustCode);
    }

    public static void start(Activity activity,String replayId,String userName,String commentsId,int reqeustCode){
        Intent intent = new Intent(activity,ReplayCommentsActivity.class);
        intent.putExtra("replayId",replayId);
        intent.putExtra("commentsId",commentsId);
        intent.putExtra("userName",userName);
        intent.putExtra("type",2);
        activity.startActivityForResult(intent,reqeustCode);
    }

    public static void start(Activity activity,String commentsId){
        Intent intent = new Intent(activity,ReplayCommentsActivity.class);
        intent.putExtra("commentsId",commentsId);
        intent.putExtra("type",1);
        activity.startActivity(intent);
    }

    public static void start(Activity activity,String commentsId,int requestCode){
        Intent intent = new Intent(activity,ReplayCommentsActivity.class);
        intent.putExtra("commentsId",commentsId);
        intent.putExtra("type",1);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_replay_comments;
    }

    private EditText et_publish_point;
    private RelativeLayout rl_load_layout;

    private CheckBox cb_share_center;

    private TextView tv_text_limit_count;
    @Override
    protected void initView() {
        super.initView();
        setTitleShow(true);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_left_title.setVisibility(View.INVISIBLE);
        tv_left_title.setVisibility(View.VISIBLE);
        tv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tv_text_limit_count = findViewById(R.id.tv_text_limit_count);
        cb_share_center = findViewById(R.id.cb_share_center);
        boolean aBoolean = SPUtils.getBoolean(QCaApplication.getContext(), Constant.REPLY_SHARE);
        cb_share_center.setChecked(aBoolean);

        tv_title.setText("发表评论");
        tv_ritht_title.setText("发布");
        tv_ritht_title.setOnClickListener(this);

        et_publish_point = findViewById(R.id.et_publish_point);
        rl_load_layout = findViewById(R.id.rl_load_layout);

        tv_ritht_title.setTextColor(Color.parseColor("#55FCDB45"));

        et_publish_point.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    tv_ritht_title.setTextColor(Color.parseColor("#FCDB45"));
                }else{
                    tv_ritht_title.setTextColor(Color.parseColor("#55FCDB45"));
                }

                int count1 = 140 - s.length();
                tv_text_limit_count.setText(count1+"");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_ritht_title:
                if(et_publish_point.getText().toString().trim().length()<=0){
                    ToastUtils.toast("内容不能为空");
                    return;
                }
                if(type == 1) {
                    publishComments();
                }else{
                    publishReplay();
                }
                break;
        }
    }

    private void publishReplay() {

        ///php/api.php?action=topic&type=reply&id=39180&value=123456&reply_id=4091
        String url = Constant.BASE_URL + "php/api.php";

        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","reply");
        params.put("id",commentsId+"");
        String value = et_publish_point.getText().toString();
        String encode = URLEncoder.encode(value);
        params.put("value",encode);
        params.put("reply_id",replayId+"");
        String rootReplyId = getIntent().getStringExtra("rootReplyId");
        if(!TextUtils.isEmpty(rootReplyId)){
            params.put("root_reply_id",rootReplyId);
        }


        //0 不同步  1 同步
        boolean checked = cb_share_center.isChecked();
        int shareInt = checked ? 1:0;
        params.put("share",shareInt+"");


        String s = params.getUrlParams().toString();
        tv_ritht_title.setEnabled(false);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                PublishPointBean bean = JsonParseUtils.parseJsonClass(string,PublishPointBean.class);
                if(bean.status != null){
                    if(bean.status.errno == 0){
                        ToastUtils.toast("发布成功");
                        setResult(99);
                        finish();
                    }else{
                        ToastUtils.toast(bean.status.errstr);
                    }
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tv_ritht_title.setEnabled(true);
            }
        });
    }

    /**
     * 发布评论
     */
    private void publishComments() {
        ////.com/php/api.php?action=topic&type=reply&id=39180&value=123456&reply_id=0
        String url = Constant.BASE_URL + "php/api.php";

        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","reply");
        params.put("id",commentsId+"");
        String value = et_publish_point.getText().toString();
        String encode = URLEncoder.encode(value);
        params.put("value",encode);
        params.put("reply_id","0");
        tv_ritht_title.setEnabled(false);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                PublishPointBean bean = JsonParseUtils.parseJsonClass(string,PublishPointBean.class);
                if(bean.status != null){
                    if(bean.status.errno == 0){
                        ToastUtils.toast("发布成功");
                        setResult(99);
                        finish();
                    }else{
                        ToastUtils.toast(bean.status.errstr);
                    }
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                tv_ritht_title.setEnabled(true);
            }
        });



    }

    private int type;
    private String replayId;
    private String userName;
    private String commentsId;

    @Override
    protected void initDatas() {
        super.initDatas();
        type = getIntent().getIntExtra("type",0);
        if(type == 1){//话题观点评论
            commentsId = getIntent().getStringExtra("commentsId");
            et_publish_point.setHint("发布您的评论吧");
        }else if(type == 2){//回复评论
            replayId = getIntent().getStringExtra("replayId");
            userName = getIntent().getStringExtra("userName");
            commentsId = getIntent().getStringExtra("commentsId");
            et_publish_point.setHint("回复@"+userName+"的评论");
        }
    }

    @Override
    public void onReload() {

    }
}
