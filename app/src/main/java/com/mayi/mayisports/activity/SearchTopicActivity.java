package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.SearchTopicBean;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;

import org.kymjs.kjframe.http.HttpParams;

import java.util.ArrayList;
import java.util.List;

/**
 * 搜索话题界面，关联话界面
 */
public class SearchTopicActivity extends BaseActivity implements RequestHttpCallBack.ReLoadListener, AdapterView.OnItemClickListener {



    public static void start(Activity activity,int requestCode){
        Intent intent = new Intent(activity,SearchTopicActivity.class);
        activity.startActivityForResult(intent,requestCode);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_search_topic;
    }

    private ImageView iv_back_title;
    private EditText et_search_title;
    private ImageView tv_clear_et_content;
    private TextView  tv_search_title;
    private View ll_top_title_search;
    private ListView lv_search_ac;
    private RelativeLayout rl_load_layout;
    private LinearLayout ll_no_data;

    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);

        rl_load_layout = findViewById(R.id.rl_load_layout);
        ll_no_data = findViewById(R.id.ll_no_data);
        iv_back_title =  findViewById(R.id.iv_back_title);
        iv_back_title.setOnClickListener(this);
        et_search_title = findViewById(R.id.et_search_title);
        tv_clear_et_content = findViewById(R.id.tv_clear_et_content);
        tv_clear_et_content.setOnClickListener(this);
        tv_search_title = findViewById(R.id.tv_search_title);
        ll_top_title_search = findViewById(R.id.top_title_search);
        lv_search_ac = findViewById(R.id.lv_search_ac);

        tv_search_title.setOnClickListener(this);
        lv_search_ac.setOnItemClickListener(this);

        et_search_title.setHint("事件,赛事,球队,球员");

        et_search_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TextUtils.isEmpty(s.toString())){
                    tv_clear_et_content.setVisibility(View.GONE);
                }else{
                    tv_clear_et_content.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String trim = s.toString().trim();
                if(TextUtils.isEmpty(trim)){
                    isEditSearch = false;
                    requestNetDataForSearch("");
                }
            }
        });

        et_search_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String trim = et_search_title.getText().toString().trim();
                    if(TextUtils.isEmpty(trim)){
                        return false;
                    }
                    isEditSearch = true;
                    requestNetDataForSearch(trim);
                }
                return false;
            }
        });

        et_search_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isEditSearch = false;
                requestNetDataForSearch("");
                return false;
            }
        });

    }

    @Override
    protected void initDatas() {
        super.initDatas();
        requestNetDataForSearch("");
    }


    private boolean isEditSearch;
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_clear_et_content:
                et_search_title.setText("");
                break;
            case R.id.tv_search_title:
                String trim = et_search_title.getText().toString().trim();
                if(!TextUtils.isEmpty(trim)){
                    et_search_title.setText("");
                }else{
                    finish();
                }
                break;
            case R.id.iv_back_title:
                finish();
                break;
        }
    }

    private SearchTopicBean searchTopicBean;
    private void requestNetDataForSearch(final String value) {


        //http://app.mayisports.com/php/api.php?action=search&value=%E5%B7%B4%E8%90%A8&type=0&start=0
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","search");
        params.put("value",value);
        params.put("type",2);
        RequestNetWorkUtils.kjHttp.cancleAll();
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onPreStart() {
                super.onPreStart();
                KeyBoardUtils.closeKeybord(et_search_title,SearchTopicActivity.this);
            }

            @Override
            public void onSucces(String string) {

                searchTopicBean = JsonParseUtils.parseJsonClass(string, SearchTopicBean.class);

                if(isEditSearch){
                    myTopicListBeanList.clear();
                    if(searchTopicBean != null && searchTopicBean.data != null && searchTopicBean.data.topic != null){
                        myTopicListBeanList.addAll(searchTopicBean.data.topic);
                    }
                    lv_search_ac.setAdapter(myHostSearchAdapter);
                }else {
                    delData(searchTopicBean);
                }


            }


            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
                isEditSearch = false;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });



    }

    private List<SearchTopicBean.DataBean.MyTopicListBean> myTopicListBeanList = new ArrayList<>();

    private void delData(SearchTopicBean searchTopicBean) {
        myTopicListBeanList.clear();
        if(searchTopicBean == null || searchTopicBean.data == null){
            ll_no_data.setVisibility(View.VISIBLE);
            return;
        }

       if(searchTopicBean.data.myTopicList != null){
            myTopicListBeanList.addAll(searchTopicBean.data.myTopicList);
       }else{
           searchTopicBean.data.myTopicList = new ArrayList<>();
       }
       if(searchTopicBean.data.hotTopicList != null){
           myTopicListBeanList.addAll(searchTopicBean.data.hotTopicList);
       }else{
           searchTopicBean.data.hotTopicList = new ArrayList<>();
       }

        lv_search_ac.setAdapter(myHostSearchAdapter);

    }



    /**
     * 热搜adapter
     */
    private MyHostSearchAdapter myHostSearchAdapter = new MyHostSearchAdapter();

    @Override
    public void onReload() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SearchTopicBean.DataBean.MyTopicListBean myTopicListBean = myTopicListBeanList.get(position);
        Intent intent = new Intent();
        String title = myTopicListBean.title;
        intent.putExtra("title",title);
        String id1 = myTopicListBean.id;
        intent.putExtra("id",id1);

        setResult(11,intent);
        finish();
    }

    private class MyHostSearchAdapter extends BaseAdapter {

        @Override
        public int getCount() {


                int size = myTopicListBeanList.size();
                if(size == 0){
                    ll_no_data.setVisibility(View.VISIBLE);
                }else{
                    ll_no_data.setVisibility(View.GONE);
                }
                return size;
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
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                convertView = View.inflate(QCaApplication.getContext(),R.layout.search_item_layout,null);
                holder.tv_top_item = convertView.findViewById(R.id.tv_top_item);
                holder.iv_left_item = convertView.findViewById(R.id.iv_left_item);
                holder.tv_value_item = convertView.findViewById(R.id.tv_value_item);
                holder.iv_rigth_item = convertView.findViewById(R.id.iv_rigth_item);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            SearchTopicBean.DataBean.MyTopicListBean s =myTopicListBeanList.get(position);

            if(!isEditSearch) {
                if (position == 0) {
                    if (searchTopicBean.data.myTopicList.size() != 0) {
                        holder.tv_top_item.setVisibility(View.VISIBLE);
                        holder.tv_top_item.setText("最近使用");
                    } else {
                        holder.tv_top_item.setVisibility(View.VISIBLE);
                        holder.tv_top_item.setText("热门话题");
                    }
                } else if (position == searchTopicBean.data.myTopicList.size() && searchTopicBean.data.myTopicList.size() != 0) {

                    holder.tv_top_item.setVisibility(View.VISIBLE);
                    holder.tv_top_item.setText("热门话题");
                } else {
                    holder.tv_top_item.setVisibility(View.GONE);
                }

            }else{
                holder.tv_top_item.setVisibility(View.GONE);
            }

            holder.iv_rigth_item.setVisibility(View.GONE);
            PictureUtils.show(R.drawable.topicbubble,holder.iv_left_item);
            holder.tv_value_item.setText(s.title);

            return convertView;
        }
    }

    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(et_search_title,this);

        String trim = et_search_title.getText().toString().trim();
        if(!TextUtils.isEmpty(trim)){
            et_search_title.setText("");
            return;
        }

        super.finish();
    }

    static class Holder{
        public TextView tv_top_item;
        public ImageView iv_left_item;
        public TextView tv_value_item;
        public ImageView iv_rigth_item;
    }
}
