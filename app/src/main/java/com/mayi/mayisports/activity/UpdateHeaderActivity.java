package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.RewardBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastMsgPopuWindow;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.net.URLEncoder;

/**
 * 更新头像  Url
 */
public class UpdateHeaderActivity extends BaseActivity {



    public static void start(Activity activity){
        Intent intent = new Intent(activity,UpdateHeaderActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_update_header;
    }


    private ImageView iv_header;
    private TextView tv_upload;

    private RelativeLayout rl_load_webview_layout;
    @Override
    protected void initView() {
        super.initView();
        tv_ritht_title.setVisibility(View.INVISIBLE);
        tv_title.setText("修改头像");
        iv_left_title.setOnClickListener(this);


        iv_header = findViewById(R.id.iv_header);
        tv_upload = findViewById(R.id.tv_upload);
        tv_upload.setOnClickListener(this);

        rl_load_webview_layout = findViewById(R.id.rl_load_webview_layout);

        String string = SPUtils.getString(QCaApplication.getContext(), Constant.HEADER_URl);
        if(TextUtils.isEmpty(string)){

        }else {
            PictureUtils.showRounded(string,iv_header,R.drawable.defluat_header_icon,3);
        }
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
            case R.id.tv_upload:
                ToastUtils.toast("上传图片");
                startMatissPhoto();
                break;
        }
    }

    private static final int REQUEST_CODE_CHOOSE = 23;
    private void startMatissPhoto(){
        Matisse.from(this)
                .choose(MimeType.ofImage()) // 1、获取 SelectionCreator
                .countable(false)
                .showSingleMediaType(true)
                .maxSelectable(1)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine()) // 2、配置各种各样的参数
                .forResult(REQUEST_CODE_CHOOSE); // 3、打开 MatisseActivity

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            Log.e("uri",Matisse.obtainPathResult(data).toString());
            if(Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() >0) {
                String s = Matisse.obtainPathResult(data).get(0);

                startPhotoZoom(s);

            }
        }else if (requestCode == 8) {//裁剪成功
            tv_upload.setEnabled(false);

            //'http://admin.qiuka.tv/getajax1/'+{{$id}};

            //{'data':imgurl},

            String urlserver = "http://admin.qiuka.tv/getajax1/"+SPUtils.getString(QCaApplication.getContext(),Constant.USER_ID);
            rl_load_webview_layout.setVisibility(View.VISIBLE);
            PictureUtils.uploadFileImageHeader(urlserver, headFile, handler);
        }




    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 200){
                String url = (String) msg.obj;
                rl_load_webview_layout.setVisibility(View.GONE);
                 ToastUtils.toastNoStates("修改成功");
                 finish();

            }else{
//                tv_ritht_title.setEnabled(true);
                rl_load_webview_layout.setVisibility(View.GONE);
                tv_upload.setEnabled(true);
                ToastUtils.toast("修改失败,请重试");
            }
        }
    };



    /**
     * 裁剪图片方法实现
     *
     */
    private String headFile;
    public void startPhotoZoom(String  path) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= 7) {
            intent.setDataAndType(Utils.getImageContentUri(getBaseContext(), path), "image/*");
        } else {
            File file = new File(path);
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        }
        intent.putExtra("crop", "true");


//         aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
//         outputX outputY 是裁剪图片宽高
//        intent.putExtra("outputX", 800);
//        intent.putExtra("outputY", 800);

        // 是否返回uri
        intent.putExtra("return-data", false);
        headFile = Environment.getExternalStorageDirectory().getAbsolutePath().toString() + File.separator+"qiuca"+ File.separator+"head.png";
        File f1 = new File(headFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f1));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

        startActivityForResult(intent, 8);



    }

    /**
     * 提交数据
     */
    private void submit() {



        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","user");
        params.put("type","feedback");

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
//        toastMsgPopuWindow.showAtLocation(et_content_replay, Gravity.CENTER,0,0);
    }
}
