package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.PublishPointBean;
import com.mayisports.qca.bean.PublishPointSelectItemBean;
import com.mayisports.qca.fragment.DynamicFragment;
import com.mayisports.qca.fragment.MainQcaFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.kymjs.kjframe.http.HttpParams;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 发布观点界面
 */
public class PublishPointActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener {



    public static void start(Activity activity,String topcId){
        Intent intent = new Intent(activity,PublishPointActivity.class);
        intent.putExtra("topcId",topcId);
        activity.startActivity(intent);
    }
    public static void start(Activity activity,String topcId,boolean isShow){
        Intent intent = new Intent(activity,PublishPointActivity.class);
        intent.putExtra("topcId",topcId);
        intent.putExtra("isShow",isShow);
        activity.startActivity(intent);
    }

    public static void start(Activity activity,String topcId,String commentId,boolean isShow,int code){
        Intent intent = new Intent(activity,PublishPointActivity.class);
        intent.putExtra("topcId",topcId);
        intent.putExtra("isShow",isShow);
        intent.putExtra("commentId",commentId);
        activity.startActivityForResult(intent,code);
    }


    @Override
    public void finish() {

        KeyBoardUtils.closeKeybord(et_publish_point,this);
        super.finish();
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_publish_point;
    }

    private EditText et_publish_point;
    private GridView gv_publish_point;
    private MyAdapter myAdapter = new MyAdapter();
    private RelativeLayout rl_load;
    private ListView lv_publish_point;

    private ImageView iv_link_topic;

    private TextView tv_text_limit_count;
    @Override
    protected void initView() {
        super.initView();
        iv_left_title.setVisibility(View.INVISIBLE);
        iv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_left_title.setVisibility(View.VISIBLE);
        tv_left_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_title.setText("发布观点");
        tv_ritht_title.setText("发布");
        tv_ritht_title.setOnClickListener(this);

        rl_load = findViewById(R.id.rl_load_layout);
        lv_publish_point = findViewById(R.id.lv_publish_point);

        lv_publish_point.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);


        topicId = getIntent().getStringExtra("topcId");


        iv_link_topic = findViewById(R.id.iv_link_topic);
        iv_link_topic.setOnClickListener(this);

        tv_text_limit_count = findViewById(R.id.tv_text_limit_count);


        addHeader();

        lv_publish_point.setAdapter(myLvAdapter);
        delImg();
    }

    private MyLvAdapter myLvAdapter = new MyLvAdapter();
    private TextView tv_link_topic_publish;
    private ImageView iv_delete_topic;





    private CheckBox cb_share_center;
    private void addHeader() {
        View view = View.inflate(QCaApplication.getContext(),R.layout.publish_point_header,null);

        et_publish_point = view.findViewById(R.id.et_publish_point);
        gv_publish_point = view.findViewById(R.id.gv_publish_point);
        gv_publish_point.setAdapter(myAdapter);
        gv_publish_point.setOnItemClickListener(this);
        tv_link_topic_publish = view.findViewById(R.id.tv_link_topic_publish);

        cb_share_center = view.findViewById(R.id.cb_share_center);

        boolean aBoolean = SPUtils.getBoolean(QCaApplication.getContext(), Constant.REPLY_SHARE);
        cb_share_center.setChecked(aBoolean);


        iv_delete_topic = view.findViewById(R.id.iv_delete_topic);
        if("0".equals(topicId)){
           tv_link_topic_publish.setVisibility(View.VISIBLE);
            tv_link_topic_publish.setVisibility(View.GONE);
           tv_link_topic_publish.setOnClickListener(this);
           iv_delete_topic.setOnClickListener(this);
           tv_title.setText("发布动态");
           et_publish_point.setHint("分享你的足球新鲜事");
        }else if("-1".equals(topicId)){
            tv_title.setText("发表评论");
            et_publish_point.setHint("写下你的评论");
            tv_link_topic_publish.setVisibility(View.GONE);
            iv_delete_topic.setVisibility(View.GONE);
            iv_link_topic.setVisibility(View.GONE);
            cb_share_center.setVisibility(View.VISIBLE);




        } else{
            tv_link_topic_publish.setVisibility(View.GONE);
            iv_delete_topic.setVisibility(View.GONE);
            iv_link_topic.setVisibility(View.GONE);
        }


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




        lv_publish_point.addHeaderView(view);
    }

    private void delImg() {
      File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/qiuca/");
        if(file.exists()){
            File[] files = file.listFiles();
            for(File file1:files){
                file1.delete();
            }
        }else{
            file.mkdirs();
        }

    }

    /**
     * 0 发布动态
     */

    /**
     * -1 评论动态
     */
    private String topicId;
    @Override
    protected void initDatas() {
        super.initDatas();
        if(!topicId.equals("0") && !topicId.equals("-1")) {
            requestNetDatas();
        }

        et_publish_point.requestFocus();
        KeyBoardUtils.openKeybord(et_publish_point,this);
        lv_publish_point.setSelection(0);

        lv_publish_point.postDelayed(new Runnable() {
            @Override
            public void run() {
                lv_publish_point.setSelection(0);
            }
        },200);
    }


    private PublishPointSelectItemBean pointSelectItemBean;
    private void requestNetDatas() {
        //http://app.mayisports.com/php/api.php?action=topic&type=selection_item&id=1203
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","selection_item");
        params.put("id",topicId);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load,this) {
            @Override
            public void onSucces(String string) {
                try {
                    pointSelectItemBean = JsonParseUtils.parseJsonClass(string, PublishPointSelectItemBean.class);
                    if (pointSelectItemBean != null && pointSelectItemBean.data != null && pointSelectItemBean.data.topic != null && pointSelectItemBean.data.topic.selection != null) {
                        myLvAdapter.notifyDataSetChanged();
                    } else {
//                        lv_publish_point.setVisibility(View.GONE);
                    }
                }catch (Exception e){
//                    lv_publish_point.setVisibility(View.GONE);
                    myLvAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                lv_publish_point.setSelection(0);
            }
        });
    }

    class  MyLvAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(pointSelectItemBean != null &&pointSelectItemBean.data != null &&pointSelectItemBean.data.topic != null && pointSelectItemBean.data.topic.selection != null){
                boolean isShow = getIntent().getBooleanExtra("isShow", false);
                if(!isShow){
                    return 0;
                }
                return pointSelectItemBean.data.topic.selection.size();
            }
            return 0;
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
            MyLvHolder myLvHolder;
            if(convertView == null){
                myLvHolder = new MyLvHolder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.publish_item_layout,null);
                myLvHolder.title = convertView.findViewById(R.id.tv_title);
                myLvHolder.content = convertView.findViewById(R.id.lv_content);
                convertView.setTag(myLvHolder);
            }else{
                myLvHolder = (MyLvHolder) convertView.getTag();
            }

            PublishPointSelectItemBean.DataBean.TopicBean.SelectionBean bean = pointSelectItemBean.data.topic.selection.get(position);
            myLvHolder.title.setText((position+1)+"."+bean.title);
            myLvHolder.content.setAdapter(new MyContentAdapter(bean));

            return convertView;
        }
    }

    static class MyLvHolder{
        public TextView title;
        public ListView content;
    }

    private HashMap<String,String> selectMap = new HashMap<>();
    class MyContentAdapter extends BaseAdapter{

        private List<PublishPointSelectItemBean.DataBean.TopicBean.SelectionBean.ItemsBean> items;
        private String id;
        public MyContentAdapter(PublishPointSelectItemBean.DataBean.TopicBean.SelectionBean bean){
            items = bean.items;
            id = bean.id;
        }
        @Override
        public int getCount() {
            if(items != null)return items.size();
            return 0;
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

            convertView = View.inflate(getBaseContext(),R.layout.checkbox_layout_publish_point,null);
            final TextView name = convertView.findViewById(R.id.tv_name_checkbox);
            final TextView count = convertView.findViewById(R.id.tv_count_checkbox);

            CheckBox checkBox = convertView.findViewById(R.id.cb);
            final PublishPointSelectItemBean.DataBean.TopicBean.SelectionBean.ItemsBean itemsBean = items.get(position);

            name.setText(itemsBean.title);

            if(itemsBean.id.equals(selectMap.get(id))){
                checkBox.setChecked(true);
                name.setTextColor(Color.parseColor("#282828"));
                count.setTextColor(Color.parseColor("#282828"));
            }else{
                checkBox.setChecked(false);
                name.setTextColor(Color.parseColor("#999999"));
                count.setTextColor(Color.parseColor("#999999"));
            }



            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        selectMap.put(id,itemsBean.id);
                        name.setTextColor(Color.parseColor("#282828"));
                        count.setTextColor(Color.parseColor("#282828"));
                    }
                     notifyDataSetChanged();

                }
            });

            return convertView;
        }
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_ritht_title:
                publish();
                break;
            case R.id.tv_link_topic_publish://关联话题
            case R.id.iv_link_topic:
               SearchTopicActivity.start(this,11);
                break;
            case R.id.iv_delete_topic:
                setLinkTopic(null,"");
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            if(msg.what == 200){
                String url = (String) msg.obj;
                url = url.replace("[\"","").replace("\"]","");
                urlServer.add(url);
                if(imgPath.size()>0){
             //       String urlserver = Constant.BASE_URL+"php/object/upload/images.php";
                    String urlserver = Constant.BASE_URL+"php/object/upload/imgUpload.php";
                    PictureUtils.uploadFileImage(urlserver, imgPath.remove(0), handler);
                }else{
                    uploadOur();
                }
            }else{
                Object obj = msg.obj;
                if(obj != null){
                    String str = (String) obj;
                    imgPath.add(0, str);
                }
                rl_load.setVisibility(View.GONE);
                tv_ritht_title.setEnabled(true);
                myAdapter.notifyDataSetChanged();
            }
        }
    };
    /**
     * 发布观点，并校验
     */
    private List<String> urlServer = new ArrayList<>();
    private void publish() {
        if(et_publish_point.getText().toString().trim().length()<=0){
            ToastUtils.toast("请填写内容");
            return;
        }

        KeyBoardUtils.closeKeybord(et_publish_point,this);

        tv_ritht_title.setEnabled(false);
        rl_load.setVisibility(View.VISIBLE);
        String url = Constant.BASE_URL+"php/object/upload/imgUpload.php";
        if(imgPath.size()>0) {
            PictureUtils.uploadFileImage(url, imgPath.remove(0), handler);
        }else{
            uploadOur();
        }
    }


    /**
     * 提交到自己服务器
     */
    private void uploadOur(){
        //http://app.mayisports.com/php/api.php

        /**
         *
         * action:topic
         type:comment
         id:1197
         value:123456789
         imglist:http://sinacloud.net/topicimg/qkt_200327_1516358422330152.png,http://sinacloud.net/topicimg/qkt_200327_1516358431635098.png
         score:
         answer:{}
         *
         */
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();


        if(topicId.equals("-1")){//评论动态
            params.put("action","topic");
            params.put("type","reply");
            String commentId = getIntent().getStringExtra("commentId") + "";
            params.put("id",commentId);
            params.put("reply_id","0");
            //0 不同步  1 同步
            boolean checked = cb_share_center.isChecked();
            int shareInt = checked ? 1:0;
            params.put("share",shareInt+"");

        }else {

            params.put("action", "topic");
            params.put("type", "comment");
            if (topicId.equals("0")) {
                String topicIdNew = "";
                if (tv_link_topic_publish.isShown()) {
                    Object tag = tv_link_topic_publish.getTag();
                    if (tag != null) {
                        topicIdNew = (String) tag;
                    }
                }
                if (TextUtils.isEmpty(topicIdNew)) {
                    topicIdNew = "0";
                }
                params.put("id", topicIdNew);


            } else {
                params.put("id", topicId);

                int count = myLvAdapter.getCount();
                if (selectMap.size() != count) {
                    ToastUtils.toast("请回答所有题目");
                    rl_load.setVisibility(View.GONE);
                    tv_ritht_title.setEnabled(true);
                    return;
                }
                String json = new Gson().toJson(selectMap);
                params.put("answer", json);

            }

        }



        String value = et_publish_point.getText().toString();
        String encode = URLEncoder.encode(value);
        params.put("value",encode);
        String str = "";
        for(int i = 0;i<urlServer.size();i++){
            str += urlServer.get(i)+",";
        }
        if(str.length()>1){
           str =  str.substring(0,str.length()-1);
        }
        params.put("imglist",str);



        if(topicId.equals("-1")){

            RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
                @Override
                public void onSucces(String string) {



                        PublishPointBean bean = JsonParseUtils.parseJsonClass(string, PublishPointBean.class);

                        if (bean != null && bean.status != null) {
                            if (bean.status.errno == 0) {
                                ToastUtils.toast("发布成功");
                                setResult(99);
                                finish();
                            } else {
                                ToastUtils.toast(bean.status.errstr);
                            }
                        }else{
                            ToastUtils.toast("发布成功");
                            setResult(99);
                            finish();
                        }

                }

                @Override
                public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    rl_load.setVisibility(View.GONE);
                    tv_ritht_title.setEnabled(true);
                }
            });
        }else {


            RequestNetWorkUtils.postRequest(url, params, new RequestHttpCallBack() {
                @Override
                public void onSucces(String string) {



                        PublishPointBean publishPointBean = JsonParseUtils.parseJsonClass(string, PublishPointBean.class);
                        if (publishPointBean != null && publishPointBean.status != null) {
                            if (publishPointBean.status.errno == 0) {
                                ToastUtils.toastNoStates("提交成功");


                                if ("0".equals(topicId)) {//发布动态
//                            Intent intent1 = new Intent(DynamicFragment.ACTION);
//                            LocalBroadcastManager.getInstance(PublishPointActivity.this).sendBroadcast(intent1);
                                    Intent intent1 = new Intent(MainQcaFragment.ACTION);
                                    intent1.putExtra(RESULT, 11);
                                    LocalBroadcastManager.getInstance(PublishPointActivity.this).sendBroadcast(intent1);

                                } else {//参与话题讨论
                                    Intent intent1 = new Intent(TopicDetailActivity.ACTION);
                                    intent1.putExtra(RESULT, 66);
                                    LocalBroadcastManager.getInstance(PublishPointActivity.this).sendBroadcast(intent1);

                                }


                                finish();

                            } else {
                                ToastUtils.toast(publishPointBean.status.errstr + "");
                            }
                        } else {
                            ToastUtils.toast("失败，请重试");
                        }

                }

                @Override
                public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
                }

                @Override
                public void onFinish() {
                    super.onFinish();
                    rl_load.setVisibility(View.GONE);
                    tv_ritht_title.setEnabled(true);
                }
            });

        }
    }


    private List<String> imgPath = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == imgPath.size()){
//            ToastUtils.toast("上传照片哦");
//            selectPicFromPhone();
//            selectPicFromLocal();
            KeyBoardUtils.closeKeybord(et_publish_point,this);
            if(topicId.equals("-1")){
                startMatissPhoto(true);
            }else {

                if(cb_share_center.isShown()){
                    startMatissPhoto(true);
                }else if (imgPath.size() == 0) {
                    startMatissPhoto(false);
                } else {
                    startMatissPhoto(true);
                }
            }

        }else{

            String s = imgPath.get(position);

            PicShowActivity.start(this,s,0);

        }
    }

    private static final int REQUEST_CODE_CHOOSE = 23;

    //JPEG, PNG, GIF    MPEG, MP4
    private void startMatissPhoto(boolean isImg){
      //  int max = 9 - imgPath.size();
        int max = 1;
        if(isImg){
            Matisse.from(this)
                    .choose(MimeType.ofImage()) // 1、获取 SelectionCreator
                    .countable(false)
                    .showSingleMediaType(true)
                    .maxSelectable(max)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine()) // 2、配置各种各样的参数
                    .forResult(REQUEST_CODE_CHOOSE); // 3、打开 MatisseActivity
        }else {


            Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF, MimeType.MPEG, MimeType.MP4, MimeType.AVI)) // 1、获取 SelectionCreator
                    .countable(false)
                    .showSingleMediaType(true)
                    .maxSelectable(max)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(new GlideEngine()) // 2、配置各种各样的参数
                    .forResult(REQUEST_CODE_CHOOSE); // 3、打开 MatisseActivity

        }
    }

    public static final int VALUE_PICK_PICTURE = 2;
    private Uri chooseUri;
    private void selectPicFromLocal() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, VALUE_PICK_PICTURE);
    }




    private ArrayList<String> mSelectPath = new ArrayList<String>();;//选中的图片的存储路径
    private ArrayList<String> mUpLoadPicPath = new ArrayList<>();//压缩后的图片存储位置
    private static final int REQUEST_IMAGE = 100;
    private int index;
    private int countPic;
    private ArrayList<String> adapterList = new ArrayList<>();//当前的图片


    /**
     * 裁剪图片方法实现
     *
     */
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
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
//         outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 300);

        // 是否返回uri
        intent.putExtra("return-data", true);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f1));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());

        startActivityForResult(intent, 8);

    }


    private Bitmap bitmap;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 11&&resultCode == 11){
            String title = data.getStringExtra("title");
            String id = data.getStringExtra("id");
            setLinkTopic(id,title);
        }



        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
//            mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            Log.e("uri",Matisse.obtainPathResult(data).toString());
            if(Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size() >0) {
                String s = Matisse.obtainPathResult(data).get(0);
//            if(s.endsWith(".gif")|| s.endsWith()){
//
//            }
//            startPhotoZoom(s);
                imgPath.add(s);
                gv_publish_point.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myAdapter.notifyDataSetChanged();
                    }
                }, 300);
            }
        }


        if (requestCode == 8) {
                mUpLoadPicPath.clear();
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        bitmap = extras.getParcelable("data");
                        if (bitmap != null) {
                            PictureUtils.saveBitmap(bitmap, imgPath.size() + "");
                            imgPath.add(Constant.IMG_PATH + imgPath.size() + ".png");
                            gv_publish_point.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    myAdapter.notifyDataSetChanged();
                                }
                            }, 500);

                        }

                    }
                }
            }
        }

    /**
     * 设置关联的话题
     * @param id
     * @param title
     */
    private void setLinkTopic(String id, String title) {
        if(id == null){//删除，恢复关联状态
            tv_link_topic_publish.setTag(null);
            tv_link_topic_publish.setText("关联话题");

            tv_link_topic_publish.setVisibility(View.GONE);

            iv_delete_topic.setVisibility(View.GONE);

            Drawable drawable= getResources().getDrawable(R.drawable.right_gray_triangle);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_link_topic_publish.setCompoundDrawables(null,null,drawable,null);
            int dp = DisplayUtil.dip2px(QCaApplication.getContext(), 2);
            tv_link_topic_publish.setCompoundDrawablePadding(dp);
        }else{//设置数据，显示删除键

            tv_link_topic_publish.setVisibility(View.VISIBLE);

            tv_link_topic_publish.setTag(id);
           tv_link_topic_publish.setText(title);
           iv_delete_topic.setVisibility(View.VISIBLE);

            Drawable drawable= getResources().getDrawable(R.drawable.topicbubble);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tv_link_topic_publish.setCompoundDrawables(drawable,null,null,null);
            int dp = DisplayUtil.dip2px(QCaApplication.getContext(), 2);
            tv_link_topic_publish.setCompoundDrawablePadding(dp);
            tv_link_topic_publish.setTextColor(QCaApplication.getContext().getResources().getColor(R.color.blue_5593E6));
        }

    }

    @Override
    public void onReload() {

    }

    private int addCount = 1;
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(imgPath.size()>8)return imgPath.size();

            if(imgPath.size() >0){
                String url = imgPath.get(0);
                if(url.endsWith(".mp4") || url.endsWith(".avi") || url.endsWith(".mpeg") || cb_share_center.isShown()){
                   addCount = 0;
                }else{
                    addCount = 1;
                }
            }else{
                addCount = 1;
            }



            return imgPath.size() +addCount;
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            View view = View.inflate(QCaApplication.getContext(),R.layout.img_layout,null);
            final ImageView imageView = view.findViewById(R.id.iv_);

            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            int height = (int) ((DisplayUtil.getScreenWidth(PublishPointActivity.this) - 10) *0.3);
            layoutParams.height = height;
            imageView.setLayoutParams(layoutParams);


            /**
             * else if(position == imgPath.size()-1){
             if(bitmap != null){
             PictureUtils.show(bitmap,imageView);
             }
             }
             */
            ImageView iv_delect = view.findViewById(R.id.iv_delect);
            TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);


            if(position == imgPath.size()){
                PictureUtils.show(R.drawable.plus_,imageView);
                iv_delect.setVisibility(View.GONE);
                tv_gif_icon.setVisibility(View.GONE);

            }else{
                iv_delect.setVisibility(View.VISIBLE);
                String path = imgPath.get(position);
                File file = new File(path);
                PictureUtils.show(file,imageView);
                try {
                    Drawable fromPath = BitmapDrawable.createFromPath(path);
                    String size = fromPath.getIntrinsicWidth() + "," + fromPath.getIntrinsicHeight();
                    Utils.showType(path, imageView, tv_gif_icon, size);
                }catch (Exception e){
                    String size = "1,1";
                    Utils.showType(path, imageView, tv_gif_icon, size);
                }

            }


            iv_delect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        imgPath.remove(position);
                        myAdapter.notifyDataSetChanged();
                    }catch (Exception e){

                    }
                }
            });



            return view;
        }
    }
}
