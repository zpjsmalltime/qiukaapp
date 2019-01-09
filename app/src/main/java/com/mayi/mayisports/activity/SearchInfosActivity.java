package com.mayi.mayisports.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.opengl.Visibility;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.bean.DynamicBean;
import com.mayisports.qca.bean.FollowBean;
import com.mayisports.qca.bean.PraiseBean;
import com.mayisports.qca.bean.SearchInfosBean;
import com.mayisports.qca.bean.ToastPriceBean;
import com.mayisports.qca.fragment.HomeNewFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.DataBindUtils;
import com.mayisports.qca.utils.DisplayUtil;
import com.mayisports.qca.utils.GridViewUtils;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.KeyBoardUtils;
import com.mayisports.qca.utils.LoginUtils;
import com.mayisports.qca.utils.NotificationsUtils;
import com.mayisports.qca.utils.PictureUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ShareUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.ViewStatusUtils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.SharePopuwind;
import com.mayisports.qca.view.ToastPricePopuWindow;

import org.kymjs.kjframe.http.HttpParams;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.mayi.mayisports.QCaApplication.getContext;
import static com.mayi.mayisports.activity.LoginActivity.RESULT;

/**
 * 搜索球咖等相关信息界面
 */
public class SearchInfosActivity extends BaseActivity implements AdapterView.OnItemClickListener, RequestHttpCallBack.ReLoadListener, PlatformActionListener {



    public static void start(Activity activity,String hint){
        Intent intent = new Intent(activity,SearchInfosActivity.class);
        intent.putExtra("hint",hint);
        activity.startActivity(intent);
    }

    @Override
    protected int setViewForContent() {
        return R.layout.activity_search_infos;
    }

    private ImageView iv_back_title;
    private EditText et_search_title;
    private ImageView tv_clear_et_content;

    private TextView  tv_search_title;
    private View ll_top_title_search;
    private ListView lv_search_ac;
    private MyAdapter myAdapter = new MyAdapter();

    private TextView tv_groom_match_detail;
    private TextView tv_analyes_match_detail;
    private TextView tv_price_match_detail;
    private TextView tv_outs_match_detail;
    private View iv_groom_match_detail;
    private View iv_analyes_match_detail;
    private View iv_price_match_detail;
    private View iv_outs_match_detail;

    private RelativeLayout rl_load_layout;
    private LinearLayout ll_no_data;



    @Override
    protected void initView() {
        super.initView();
        setTitleShow(false);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        ll_no_data = findViewById(R.id.ll_no_data);
        iv_back_title =  findViewById(R.id.iv_back_title);
        et_search_title = findViewById(R.id.et_search_title);
        tv_clear_et_content = findViewById(R.id.tv_clear_et_content);
        tv_search_title = findViewById(R.id.tv_search_title);
        ll_top_title_search = findViewById(R.id.top_title_search);
        lv_search_ac = findViewById(R.id.lv_search_ac);

        tv_groom_match_detail =  findViewById(R.id.tv_groom_match_detail);
        tv_analyes_match_detail =  findViewById(R.id.tv_analyes_match_detail);
        tv_price_match_detail =  findViewById(R.id.tv_price_match_detail);
        tv_outs_match_detail =  findViewById(R.id.tv_outs_match_detail);
        iv_groom_match_detail =  findViewById(R.id.iv_groom_match_detail);
        iv_analyes_match_detail =  findViewById(R.id.iv_analyes_match_detail);
        iv_price_match_detail =  findViewById(R.id.iv_price_match_detail);
        iv_outs_match_detail =  findViewById(R.id.iv_outs_match_detail);
        tv_groom_match_detail.setOnClickListener(this);
        tv_analyes_match_detail.setOnClickListener(this);
        tv_price_match_detail.setOnClickListener(this);
        tv_outs_match_detail.setOnClickListener(this);


        iv_back_title.setOnClickListener(this);
        et_search_title.addTextChangedListener(new MyWatcher());
        et_search_title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String trim = et_search_title.getText().toString().trim();
                    if(TextUtils.isEmpty(trim)){
                       trim = et_search_title.getHint().toString().trim();
                       if(TextUtils.isEmpty(trim)){
                           return false;
                       }
                       et_search_title.setText(trim);
                    }
                    if(typeTop == -1 || typeTop == 5)typeTop = 0;
                    keyTitle = trim;
                    saveHistory(keyTitle);
                    KeyBoardUtils.closeKeybord(et_search_title,SearchInfosActivity.this);
                    rl_load_layout = findViewById(R.id.rl_load_layout);
                    requestData();
                }
                return false;
            }
        });


        et_search_title.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_search_title.setCursorVisible(true);
                /**
                 * 切换
                 */
                typeTop = -1;
                ll_top_title_search.setVisibility(View.GONE);
                lv_search_ac.removeHeaderView(headerView);
                requestSearchHot();
                return false;
            }
        });

        tv_clear_et_content.setOnClickListener(this);

        tv_search_title.setOnClickListener(this);

        addHeader();
     //   lv_search_ac.setAdapter(myAdapter);
        lv_search_ac.setOnItemClickListener(this);






    }


    private TextView tv_personal_titel_header;
    private RecyclerView rv_personal_header;
    private TextView tv_topic_title_header;
    private ListView lv_topic_header;
    private View headerView;
    private TextView tv_content_title_header;
    private void addHeader() {
        headerView = View.inflate(getContext(),R.layout.search_header_layout,null);
        tv_personal_titel_header = headerView.findViewById(R.id.tv_personal_titel_header);
        rv_personal_header = headerView.findViewById(R.id.rv_personal_header);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_personal_header.setLayoutManager(linearLayoutManager);
        rv_personal_header.setHasFixedSize(true);
        rv_personal_header.setAdapter(myRvType5Adapter);
        tv_topic_title_header = headerView.findViewById(R.id.tv_topic_title_header);
        lv_topic_header = headerView.findViewById(R.id.lv_topic_header);
        lv_topic_header.setOnItemClickListener(new HeaderItemListener());

        tv_content_title_header = headerView.findViewById(R.id.tv_content_title_header);
        lv_search_ac.addHeaderView(headerView);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        ToastUtils.toastNoStates("分享成功");
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        ToastUtils.toastNoStates("分享失败");
    }

    @Override
    public void onCancel(Platform platform, int i) {
        ToastUtils.toastNoStates("分享取消");

    }

    private class HeaderItemListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {

                SearchInfosBean.DataBean.TopicBean topicBean = searchInfosBean.data.topic.get(position);
                TopicDetailActivity.start(SearchInfosActivity.this, topicBean.id);
            }catch (Exception e){

            }
        }
    }

    @Override
    protected void initDatas() {
        super.initDatas();

        String hint = getIntent().getStringExtra("hint");
        et_search_title.setHint(hint);

        page = 0;
        typeTop = 0;
        keyTitle = "巴萨";
        typeTop = -1;
        requestSearchHot();
    }


    /**
     * 请求热门搜索标签
     */
    private void requestSearchHot() {
        lv_search_ac.removeHeaderView(headerView);
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","search");
        params.put("value","");
        params.put("type",0);
        params.put("start",0);

        RequestNetWorkUtils.kjHttp.cancleAll();
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {


                    ll_top_title_search.setVisibility(View.GONE);
                    searchInfosBean = JsonParseUtils.parseJsonClass(string, SearchInfosBean.class);
                    delHotSearchDatas();
                    lv_search_ac.setDividerHeight(dp05);
                    lv_search_ac.setAdapter(myHostSearchAdapter);

            }


            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });

    }



    @Override
    public void finish() {
        KeyBoardUtils.closeKeybord(et_search_title,this);

        if(typeTop != -1) {

            String trim = et_search_title.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                et_search_title.setText("");
                return;
            }
        }

        super.finish();
    }

    private int page;
    /**
     * 0 综合  1  2  3
     */
    private int typeTop;
    private String keyTitle = "";
    public void requestData() {

        page = 0;
        requestNetDataForSearch(typeTop,keyTitle);
    }


    /**
     * 0  1  2  3  标签 顶部切换 按顺序
     * @param type
     */
    private SearchInfosBean searchInfosBean;
    private int pre_page;

    private int dp05 = (int) (DisplayUtil.dip2px(QCaApplication.getContext(), 1) / 3.0 *2)<=0 ? 1 : (int) (DisplayUtil.dip2px(QCaApplication.getContext(), 1) / 3.0 *2);
    private int dp5 = DisplayUtil.dip2px(QCaApplication.getContext(), 5);
    private void requestNetDataForSearch(final int searchType, String value) {

        selectOne(typeTop);
        if(page == 0){
            lv_search_ac.removeHeaderView(headerView);
        }

        //http://app.mayisports.com/php/api.php?action=search&value=%E5%B7%B4%E8%90%A8&type=0&start=0
        String url = Constant.BASE_URL + "php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","search");
        params.put("value",value);
        params.put("type",searchType);
        params.put("start",page);
        Log.e("url",params.getUrlParams().toString());
        RequestNetWorkUtils.kjHttp.cancleAll();
        if(typeTop != 5) {
            et_search_title.setCursorVisible(false);
        }
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {

                    if(typeTop != searchType)return;

                    if(page == 0) {
                        ll_top_title_search.setVisibility(View.VISIBLE);
                        searchInfosBean = JsonParseUtils.parseJsonClass(string, SearchInfosBean.class);
                        pre_page = 1;

                        switch (searchType){
                            case 0:
                                lv_search_ac.setDividerHeight(dp5);
                                lv_search_ac.removeHeaderView(headerView);
                                lv_search_ac.addHeaderView(headerView);
                                lv_search_ac.setAdapter(myAdapter);
                                updateHeader();
                                myAdapter.notifyDataSetChanged();
                                break;
                            case 1:
                                lv_search_ac.setDividerHeight(dp05);
                                lv_search_ac.removeHeaderView(headerView);
                                lv_search_ac.setAdapter(myPersonalAdapter);
//                                myAdapter.notifyDataSetChanged();
                                break;
                            case 2:
                                lv_search_ac.setDividerHeight(dp05);
                                lv_search_ac.removeHeaderView(headerView);
                                lv_search_ac.setAdapter(myTopicAdapter);
//                                myAdapter.notifyDataSetChanged();
                                myTopicAdapter.notifyDataSetChanged();
                                break;
                            case 3:
                                lv_search_ac.setDividerHeight(dp5);
                                lv_search_ac.removeHeaderView(headerView);
                                lv_search_ac.setAdapter(myAdapter);
                                myAdapter.notifyDataSetChanged();
                                break;
                            case 5:
                                lv_search_ac.setDividerHeight(dp05);
                                ll_top_title_search.setVisibility(View.GONE);
                                lv_search_ac.removeHeaderView(headerView);
                                lv_search_ac.setAdapter(mySearchAdapter);
//                                myAdapter.notifyDataSetChanged();
                                mySearchAdapter.notifyDataSetChanged();
                                break;
                        }

                    }else{
                        try {
                            SearchInfosBean newBean = JsonParseUtils.parseJsonClass(string, SearchInfosBean.class);
                            switch (searchType){
                                case 0:
                                    searchInfosBean.data.comment.addAll(newBean.data.comment);
                                    break;
                                case 1:
                                    searchInfosBean.data.user.addAll(newBean.data.user);
                                    break;
                                case 2:
                                    searchInfosBean.data.topic.addAll(newBean.data.topic);
                                    break;
                                case 3:
                                    searchInfosBean.data.comment.addAll(newBean.data.comment);
                                    break;
                            }

                        }catch (Exception e){
                            pre_page = 0;
                        }finally {
                            switch (searchType){
                                case 0:
                                    myAdapter.notifyDataSetChanged();
                                    break;
                                case 1:
                                    myPersonalAdapter.notifyDataSetChanged();
                                    break;
                                case 2:
                                    myTopicAdapter.notifyDataSetChanged();
                                    break;
                                case 3:
                                    myAdapter.notifyDataSetChanged();
                                    break;
                            }
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
                rl_load_layout = findViewById(R.id.rl_load_layout);
            }
        });



    }

    /**
     * 更新头部 相关人 相关话题
     */
    private void updateHeader() {
        if(searchInfosBean.data != null && searchInfosBean.data.topic != null){
            tv_topic_title_header.setVisibility(View.VISIBLE);
            lv_topic_header.setVisibility(View.VISIBLE);
            lv_topic_header.setAdapter(myTopicAdapter);
        }else{
            tv_topic_title_header.setVisibility(View.GONE);
            lv_topic_header.setVisibility(View.GONE);
        }

        if(searchInfosBean.data != null && searchInfosBean.data.user != null){
            tv_personal_titel_header .setVisibility(View.VISIBLE);
            rv_personal_header.setVisibility(View.VISIBLE);
            myRvType5Adapter.notifyDataSetChanged();
        }else{
            tv_personal_titel_header.setVisibility(View.GONE);
            rv_personal_header.setVisibility(View.GONE);
        }


    }

    /**
     * 处理热搜标签，加入本地搜索记录
     */
    private String splitTag = Utils.MD5("split").substring(0,3);
    private void delHotSearchDatas() {
        String searchHistory = SPUtils.getString(getContext(), Constant.SEARCH_HISTORY);
        List<String> search = new ArrayList<>();
        if(!TextUtils.isEmpty(searchHistory)){
            String[] split = searchHistory.split(splitTag);
            for(int i = 0;i<split.length;i++){
                String s = split[i];
                search.add(s+splitTag+0);
            }
        }
        if(searchInfosBean.data.hotSearchList != null){
            for(int i = 0;i<searchInfosBean.data.hotSearchList.size();i++){
                String s = searchInfosBean.data.hotSearchList.get(i) + splitTag + 1;
                search.add(s);
            }
        }
        searchInfosBean.data.hotSearchList = search;

    }

    private void saveHistory(String keyTitle){
        String string = SPUtils.getString(QCaApplication.getContext(), Constant.SEARCH_HISTORY);
        String[] split = string.split(splitTag);
        if(split.length>2){
           string = string.replace(split[2]+splitTag,"");
        }
        if(string.contains(keyTitle))return;
        string = keyTitle+splitTag +string;
        SPUtils.putString(QCaApplication.getContext(),Constant.SEARCH_HISTORY,string);
    }
    private void deletHistory(String keyTitle) {
        String string = SPUtils.getString(QCaApplication.getContext(), Constant.SEARCH_HISTORY);
        String replace = string.replace(keyTitle + splitTag, "");
        SPUtils.putString(QCaApplication.getContext(),Constant.SEARCH_HISTORY,replace);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
         if(typeTop == -1){//推荐，记录点击
             String[] split = searchInfosBean.data.hotSearchList.get(position).split(splitTag);
             keyTitle = split[0];
             et_search_title.setText(keyTitle);
             et_search_title.setSelection(keyTitle.length());
             page = 0;
             typeTop = 0;
             saveHistory(keyTitle);
             KeyBoardUtils.closeKeybord(et_search_title,this);
             rl_load_layout = findViewById(R.id.rl_load_layout);
             requestNetDataForSearch(typeTop,keyTitle);
         }else if(typeTop == 5){//搜索模糊匹配点击
             if(searchInfosBean.data.wordlist == null) return;
             SearchInfosBean.DataBean.Word word = searchInfosBean.data.wordlist.get(position);
             if(word == null)return;
             keyTitle = word.tag;
             et_search_title.setText(keyTitle);
             et_search_title.setSelection(keyTitle.length());
             page = 0;
             typeTop = 0;
             saveHistory(keyTitle);
             KeyBoardUtils.closeKeybord(et_search_title,this);
             rl_load_layout = findViewById(R.id.rl_load_layout);
             requestNetDataForSearch(typeTop,keyTitle);
         }else if(typeTop == 0 || typeTop == 3){
             SearchInfosBean.DataBean.CommentBeanX bean;
             if(searchInfosBean == null || searchInfosBean.data == null || searchInfosBean.data.comment == null)return;
             if(typeTop == 0){
                 bean = searchInfosBean.data.comment.get(position-1);
             }else{
                 bean = searchInfosBean.data.comment.get(position);
             }
             KeyBoardUtils.closeKeybord(et_search_title,this);
             if(bean.type == 1){//推荐单
                 delType2Click(bean,position);
             }else if(bean.type == 3){//话题
                 if(typeTop == 0) {
                     ComentsDetailsActivity.startForResult(2, SearchInfosActivity.this, bean.comment.comment_id, position-1);
                 }else{
                     ComentsDetailsActivity.startForResult(2, SearchInfosActivity.this, bean.comment.comment_id, position);

                 }
             }
         }else if(typeTop == 1){//个人

             KeyBoardUtils.closeKeybord(et_search_title,this);
             String user_id = searchInfosBean.data.user.get(position).user_id;
             String json = new Gson().toJson(searchInfosBean.data.user.get(position));
             PersonalDetailActivity.start(SearchInfosActivity.this,user_id,position,44,json);
         }else if(typeTop == 2){//话题
             KeyBoardUtils.closeKeybord(et_search_title,this);
             String id1 = searchInfosBean.data.topic.get(position).id;
             TopicDetailActivity.start(this,id1);
         }
    }


    private int clickPosition;
    private void delType2Click(SearchInfosBean.DataBean.CommentBeanX bean, int i) {
        String price = bean.recommendation.price;
        String nickname = bean.user.nickname;
        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }
        myAdapter.notifyDataSetChanged();
        if(bean.recommendation.buy == 1 || (!TextUtils.isEmpty(price) && Integer.valueOf(price)<=0) || "COMPLETE".equals(bean.match.status)|| SPUtils.getString(this,Constant.USER_ID).equals(bean.user.user_id)){//已购买 //或者免费
            Intent intent = new Intent(this, HomeItemDetailActivity.class);
            intent.putExtra("userId",bean.user.user_id);
            intent.putExtra("betId",bean.recommendation.betId);
            startActivity(intent);
        }else{
            clickPosition = i;
            requestToast(bean.user.user_id,bean.recommendation.betId);
        }
    }

    private void goDetail( SearchInfosBean.DataBean.CommentBeanX bean){
        Intent intent = new Intent(this, HomeItemDetailActivity.class);
        intent.putExtra("userId",bean.user.user_id);
        intent.putExtra("betId",bean.recommendation.betId);
        startActivity(intent);
    }

    /**
     * 请求是否购买
     */
    private int type;//1确定，2充值
    private ToastPricePopuWindow toastPricePopuWindow;
    private void requestToast(String userId,String betId) {
        String url = Constant.BASE_URL + "/php/api.php";
        HttpParams params = new HttpParams();
        params.put("action","recommendation_Detail");
        params.put("type","req");
        params.put("betId",betId);
        params.put("user_id",userId);
        params.put("from",3);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
                ToastPriceBean toastPriceBean = JsonParseUtils.parseJsonClass(string,ToastPriceBean.class);
                String title = "";
                title = toastPriceBean.text;
                if("req".equals(toastPriceBean.type)){
                    type = 1;
                }else if("nomoney".equals(toastPriceBean.type)){
                    type = 2;
                }
                toastPricePopuWindow =   new ToastPricePopuWindow(SearchInfosActivity.this,SearchInfosActivity.this,title,type);
                toastPricePopuWindow.showAtLocation(lv_search_ac, Gravity.CENTER,0,0);
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


    private class MyWatcher implements TextWatcher{

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
            String string = s.toString().trim();
            if(TextUtils.isEmpty(string)){
                typeTop = -1;
                ll_top_title_search.setVisibility(View.GONE);
                lv_search_ac.removeHeaderView(headerView);
                requestSearchHot();
            }else {
                ll_top_title_search.setVisibility(View.GONE);
                rl_load_layout = null;
                keyTitle = string;
                typeTop = 5;
                requestData();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){

            case R.id.tv_clear_et_content://清空
                et_search_title.setText("");
                break;

            case R.id.iv_back_title:
                    typeTop = -1;
                    finish();
                break;
            case R.id.tv_search_title://返回键
                if(typeTop == 0 || typeTop == 1 || typeTop == 2 || typeTop == 3){

                }else{
                    typeTop = -1;
                }

                finish();
                break;
            case R.id.tv_groom_match_detail:
                typeTop = 0;
                selectOne(typeTop);
                rl_load_layout = findViewById(R.id.rl_load_layout);
                requestData();
                break;
            case R.id.tv_analyes_match_detail:
                typeTop = 1;
                selectOne(typeTop);
                rl_load_layout = findViewById(R.id.rl_load_layout);
                requestData();
                break;
            case R.id.tv_price_match_detail:
                typeTop = 2;
                selectOne(typeTop);
                rl_load_layout = findViewById(R.id.rl_load_layout);
                requestData();
                break;
            case R.id.tv_outs_match_detail://
                typeTop = 3;
                selectOne(typeTop);
                rl_load_layout = findViewById(R.id.rl_load_layout);
                requestData();
                break;
            case R.id.tv_cancle_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                break;
            case R.id.tv_go_toast_price:
                if(toastPricePopuWindow !=null){
                    toastPricePopuWindow.dismiss();
                }
                if(type == 1){//确定
                    SearchInfosBean.DataBean.CommentBeanX beanX = searchInfosBean.data.comment.get(clickPosition - 1);
                    beanX.recommendation.buy = 1;
                    myAdapter.notifyDataSetChanged();
                    goDetail(beanX);
                }else{//充值
                    Intent intent = new Intent(SearchInfosActivity.this, CoinDetailActivity.class);
                    startActivity(intent);
                }
                break;
        }
    }


    private void showNoData(){
        if(ll_no_data != null)ll_no_data.setVisibility(View.VISIBLE);
    }
    private void hidenNoData(){
        if(ll_no_data != null)ll_no_data.setVisibility(View.GONE);
    }


    /**
     * 综合adapter  和相关内容adapter
     */
    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            if(searchInfosBean != null && searchInfosBean.data != null) {
                if ((searchInfosBean.data.comment == null || searchInfosBean.data.comment.size() == 0) && (searchInfosBean.data.user == null || searchInfosBean.data.user.size() == 0) && (searchInfosBean.data.topic == null || searchInfosBean.data.topic.size() == 0)) {
                    showNoData();
                } else {
                    hidenNoData();
                }

                if (searchInfosBean.data.comment != null) {
                    int size = searchInfosBean.data.comment.size();
                    if(size == 0){
                        tv_content_title_header.setVisibility(View.GONE);
                    }else{
                        tv_content_title_header.setVisibility(View.VISIBLE);
                        size  ++;
                    }
                    return size;
                }else{
                    tv_content_title_header.setVisibility(View.GONE);
                    return 0;
                }
            }
            tv_content_title_header.setVisibility(View.GONE);
            showNoData();
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
        public View getView(final int position, View view, ViewGroup parent) {

            if(position == searchInfosBean.data.comment.size()){
                view = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = view.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                return view;
            }

            final MyHolder myHolder;
            if(view == null || view.getTag() == null){
                myHolder = new MyHolder();
                view = View.inflate(getContext(),R.layout.dynamic_item,null);
                myHolder.ll_top_click = view.findViewById(R.id.ll_top_click);
                myHolder.tv_buy = view.findViewById(R.id.tv_buy);
                myHolder.iv_head_dynamic_item = view.findViewById(R.id.iv_head_dynamic_item);
                myHolder.tv_name_dynamic_item = view.findViewById(R.id.tv_name_dynamic_item);
                myHolder.iv_red_point_header = view.findViewById(R.id.iv_red_point_header);
                myHolder.ll_top_tag_container = view.findViewById(R.id.ll_top_tag_container);
                myHolder.tv_time_dynamic_item = view.findViewById(R.id.tv_time_dynamic_item);
                myHolder.tv_content_dynamic_item = view.findViewById(R.id.tv_content_dynamic_item);
                myHolder.ll_groom_dynamic_item = view.findViewById(R.id.ll_groom_dynamic_item);
                myHolder.tv_team_name_dynamic_item = view.findViewById(R.id.tv_team_name_dynamic_item);
                myHolder.tv_bottom_dynamic_item = view.findViewById(R.id.tv_bottom_dynamic_item);
                myHolder.tv_type_dynamic_item = view.findViewById(R.id.tv_type_dynamic_item);
                myHolder.tv_price_dynamic_item = view.findViewById(R.id.tv_price_dynamic_item);
                myHolder.ll_img_container_dynamic_item = view.findViewById(R.id.ll_img_container_dynamic_item);
                myHolder.rl_topic_dynamic_item = view.findViewById(R.id.rl_topic_dynamic_item);
                myHolder.iv_img_left = view.findViewById(R.id.iv_img_left);
                myHolder.tv_bottom_title_topic_item = view.findViewById(R.id.tv_bottom_title_topic_item);
                myHolder.tv_view_count_topic_item = view.findViewById(R.id.tv_view_count_topic_item);
                myHolder.ll_bottom_dynamic_item = view.findViewById(R.id.ll_bottom_dynamic_item);
                myHolder.tv_thumbup_topic_item = view.findViewById(R.id.tv_thumbup_topic_item);
                myHolder.tv_comments_topic_item = view.findViewById(R.id.tv_comments_topic_item);
                myHolder.ll_price = view.findViewById(R.id.ll_price);
                myHolder.tv_follow_dynamic_item = view.findViewById(R.id.tv_follow_dynamic_item);
                myHolder.iv_vip_header = view.findViewById(R.id.iv_vip_header);
                myHolder.tv_right_item = view.findViewById(R.id.tv_right_item);
                myHolder.rl_video_item = view.findViewById(R.id.rl_video_item);
                myHolder.iv_video_item = view.findViewById(R.id.iv_video_item);
                myHolder.rl_bg_content_item = view.findViewById(R.id.rl_bg_content_item);
                myHolder.tv_content_bg_dynamic_item = view.findViewById(R.id.tv_content_bg_dynamic_item);
                myHolder.iv_content_bg_item = view.findViewById(R.id.iv_content_bg_item);
                myHolder.gv_dynamic_item = view.findViewById(R.id.gv_dynamic_item);
                myHolder.rl_bg_long_content_item = view.findViewById(R.id.rl_bg_long_content_item);
                myHolder.tv_content_long_content = view.findViewById(R.id.tv_content_long_content);
                myHolder.tv_title_long_content = view.findViewById(R.id.tv_title_long_content);
                myHolder.tv_share_topic_item = view.findViewById(R.id.tv_share_topic_item);
                myHolder.tv_content_bottom= view.findViewById(R.id.tv_content_bottom);
                view.setTag(myHolder);
            }else{
                myHolder = (MyHolder) view.getTag();
            }

//            final DynamicBean.ListBean bean = dynamicBean.list.get(i);
            final SearchInfosBean.DataBean.CommentBeanX bean = searchInfosBean.data.comment.get(position);

            myHolder.ll_top_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SearchInfosActivity.this, PersonalDetailActivity.class);
                    intent.putExtra("id", bean.user.user_id + "");
                    intent.putExtra("position", position);
                    String json = new Gson().toJson(bean.user);
                    PersonalDetailActivity.start(SearchInfosActivity.this,bean.user.user_id,position,1,json);
//                        startActivityForResult(intent, 1);
                }
            });


            PictureUtils.showCircle(bean.user.headurl + "", myHolder.iv_head_dynamic_item);
            myHolder.tv_name_dynamic_item.setText(bean.user.nickname);
            ViewStatusUtils.addTags(myHolder.ll_top_tag_container, bean.user.tag, bean.user.tag1);

            myHolder.iv_red_point_header.setVisibility(View.GONE);


            if (!TextUtils.isEmpty(bean.user.verify_type) && Integer.valueOf(bean.user.verify_type) > 0) {
                myHolder.iv_vip_header.setVisibility(View.VISIBLE);
            } else {
                myHolder.iv_vip_header.setVisibility(View.GONE);
            }

            if (false&&bean.user.follow_status == 0 && !(SPUtils.getString(getContext(), Constant.USER_ID).equals(bean.user.user_id))) {//系统推荐，显示关注

                myHolder.tv_follow_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.tv_time_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_follow_dynamic_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        requestFollows(position,typeTop);
                    }
                });


                if (bean.user.follow_status == 1) {
//                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
                    myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                    myHolder.tv_follow_dynamic_item.setText("已关注");
                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                } else {
//                    myHolder.tv_follow_dynamic_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke));
//                    myHolder.tv_follow_dynamic_item.setText(" 关注 ");
                    myHolder.tv_follow_dynamic_item.setTextColor(getResources().getColor(R.color.coment_black_99));
                    myHolder.tv_follow_dynamic_item.setText("+关注");
//                    myHolder.tv_follow_dynamic_item.setOnClickListener(null);
                }


            } else {
                myHolder.tv_follow_dynamic_item.setVisibility(View.GONE);
                myHolder.tv_time_dynamic_item.setVisibility(View.VISIBLE);

                if (bean.comment != null && !TextUtils.isEmpty(bean.create_time)) {
                    String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                    myHolder.tv_time_dynamic_item.setText(createTime);
                } else if (!TextUtils.isEmpty(bean.create_time)) {
                    String createTime = Utils.getCreateTime(Long.valueOf(bean.create_time + "000"));
                    myHolder.tv_time_dynamic_item.setText(createTime);
                } else {
                    myHolder.tv_time_dynamic_item.setText("");
                }


            }


            if (bean.type == 1) {//推荐单
                myHolder.ll_groom_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                myHolder.ll_bottom_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);

                bindType2(position,bean, myHolder);

            } else if (bean.type == 3) {//话题
                myHolder.ll_bottom_dynamic_item.setVisibility(View.VISIBLE);
                myHolder.ll_groom_dynamic_item.setVisibility(View.GONE);

                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);


                bindType1(position,bean, myHolder);
            }

            if(position == getCount()-3||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestNetDataForSearch(typeTop,keyTitle);
                }
            }
            return view;
        }
    }


    private MyRvType5Adapter myRvType5Adapter = new MyRvType5Adapter();
    class MyRvType5Adapter extends RecyclerView.Adapter<RvHolder> {


        @Override
        public RvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RvHolder rvHolder = new RvHolder(LayoutInflater.from(
                    getContext()).inflate(R.layout.type5_cy_item_layout_new, parent,
                    false));
            return rvHolder;
        }

        @Override
        public void onBindViewHolder(final RvHolder holder, final int position) {
            final SearchInfosBean.DataBean.UserBean listBean = searchInfosBean.data.user.get(position);

            PictureUtils.showCircle(listBean.headurl,holder.iv_header_item);
            holder.tv_name_item.setText(listBean.nickname);
            holder.tv_tag_item.setVisibility(View.GONE);
            if(!TextUtils.isEmpty(listBean.verify_type)&&Integer.valueOf(listBean.verify_type)>0){
                holder.iv_vip_item.setVisibility(View.VISIBLE);
            }else{
                holder.iv_vip_item.setVisibility(View.GONE);
            }

            holder.tv_follow_item.setVisibility(View.GONE);

            holder.rl_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String json = new Gson().toJson(listBean);
                    PersonalDetailActivity.start(SearchInfosActivity.this,listBean.user_id,position,2,json);
//                    PersonalDetailActivity.start(HomeNewFragment.this.getActivity(),listBean.user_id);
                }
            });

        }

        @Override
        public int getItemCount() {
            if(searchInfosBean != null && searchInfosBean.data != null && searchInfosBean.data.user != null){
                int size = searchInfosBean.data.user.size();
                return size;
            }
            return 0;
        }
    }
    static class RvHolder extends RecyclerView.ViewHolder{

        public ImageView iv_img_rv_item;
        public TextView tv_title_rv_item;

        public LinearLayout rl_root;
        public ImageView iv_header_item;
        public ImageView iv_vip_item;
        public TextView tv_name_item;
        public TextView tv_tag_item;
        public TextView tv_follow_item;

        public RvHolder(View itemView) {
            super(itemView);
            iv_img_rv_item = itemView.findViewById(R.id.iv_img_rv_item);
            tv_title_rv_item = itemView.findViewById(R.id.tv_title_rv_item);
            iv_header_item = itemView.findViewById(R.id.iv_header_item);
            iv_vip_item = itemView.findViewById(R.id.iv_vip_item);
            tv_name_item = itemView.findViewById(R.id.tv_name_item);
            tv_tag_item = itemView.findViewById(R.id.tv_tag_item);
            tv_follow_item = itemView.findViewById(R.id.tv_follow_item);
            rl_root = itemView.findViewById(R.id.rl_root);
        }
    }

    private void sendHome(String userId,int follow_status){
        Intent intent1 = new Intent(HomeNewFragment.ACTION);
        intent1.putExtra(RESULT, 99);
        intent1.putExtra("userId",userId);
        intent1.putExtra("follow_status",follow_status);
        LocalBroadcastManager.getInstance(this.getBaseContext()).sendBroadcast(intent1);
    }


    public void requestFollows(final int i, final int typeTop){

        if(typeTop == 0 || typeTop ==3 ||typeTop == 1) {
            if (LoginUtils.isUnLogin()) {
                LoginUtils.goLoginActivity(this, HomeNewFragment.ACTION);
                return;
            }


            String url = Constant.BASE_URL + "/php/api.php";
            HttpParams params = new HttpParams();
            params.put("action", "followers");
            String type = "";

            if(typeTop == 1){
                SearchInfosBean.DataBean.UserBean userBean = searchInfosBean.data.user.get(i);
                params.put("user_id", userBean.user_id + "");
                if (userBean.follow_status == 1) {
                    type = "delete";
                } else {
                    type = "add";
                }
                params.put("type", type);
            }else {
                final SearchInfosBean.DataBean.CommentBeanX bean = searchInfosBean.data.comment.get(i);
                params.put("user_id", bean.user.user_id + "");
                if (bean.user.follow_status == 1) {
                    type = "delete";
                } else {
                    type = "add";
                }
                params.put("type", type);
            }
            RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
                @Override
                public void onSucces(String string) {
                    FollowBean followBean = JsonParseUtils.parseJsonClass(string, FollowBean.class);
                    if (followBean != null) {
                        if ("1".equals(followBean.status.result)) {
                            if(typeTop == 1) {
                                SearchInfosBean.DataBean.UserBean userBean = searchInfosBean.data.user.get(i);
                                userBean.follow_status = 1;
                                delFollow(1,userBean.user_id);
                                myPersonalAdapter.notifyDataSetChanged();
                                sendHome(userBean.user_id,1);
                            }else{
                                final SearchInfosBean.DataBean.CommentBeanX bean = searchInfosBean.data.comment.get(i);
                                bean.user.follow_status = 1;
                                delFollow(1,bean.user.user_id);
                                myAdapter.notifyDataSetChanged();
                                sendHome(bean.user.user_id,1);
                            }

                            NotificationsUtils.checkNotificationAndStartSetting(SearchInfosActivity.this,lv_search_ac);
                        } else {
                            if(typeTop == 1) {
                                SearchInfosBean.DataBean.UserBean userBean = searchInfosBean.data.user.get(i);
                                userBean.follow_status = 0;
                                delFollow(0,userBean.user_id);
                                myPersonalAdapter.notifyDataSetChanged();
                                sendHome(userBean.user_id,0);
                            }else{
                                final SearchInfosBean.DataBean.CommentBeanX bean = searchInfosBean.data.comment.get(i);
                                bean.user.follow_status = 0;
                                delFollow(0,bean.user.user_id);
                                myAdapter.notifyDataSetChanged();
                                sendHome(bean.user.user_id,0);
                            }
                        }

                    }
                }

                @Override
                public void onfailure(int errorNo, String strMsg) {
                    ToastUtils.toast(Constant.NET_FAIL_TOAST);
                }
            });
        }
    }

    /**
     * 话题
     *
     * @param bean
     * @param myHolder
     */
    private void bindType1(final int position, final SearchInfosBean.DataBean.CommentBeanX bean, MyHolder myHolder) {

        if (!TextUtils.isEmpty(bean.comment.comment)) {
            bean.comment.comment =  bean.comment.comment.replaceAll("&nbsp;"," ").replaceAll("<br/>"," ").replaceAll("\n"," ").replaceAll("<[^>]*>|\\n","");

            if(TextUtils.isEmpty(bean.comment.background_img) && bean.comment.backgroundList != null){
                bean.comment.background_img = bean.comment.backgroundList.get(0).url;
            }

            if (TextUtils.isEmpty(bean.comment.background_img)) {//文章或者观点

                if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                    myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                    myHolder.tv_title_long_content.setText(bean.comment.title);
                    myHolder.tv_content_long_content.setText(bean.comment.comment);

                }else {//普通评论
                    myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
                    myHolder.rl_bg_content_item.setVisibility(View.GONE);
                    myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                    myHolder.tv_content_dynamic_item.setText(bean.comment.comment);
                }

            } else {
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.VISIBLE);
                myHolder.rl_bg_long_content_item.setVisibility(View.GONE);
                myHolder.tv_content_bg_dynamic_item.setText(bean.comment.title);
                myHolder.tv_content_bottom.setText(bean.comment.comment+"");
                PictureUtils.show(bean.comment.background_img, myHolder.iv_content_bg_item);
            }

        } else {
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
            myHolder.rl_bg_content_item.setVisibility(View.GONE);
            myHolder.rl_bg_content_item.setVisibility(View.GONE);
            myHolder.rl_bg_long_content_item.setVisibility(View.GONE);

            if(!TextUtils.isEmpty(bean.comment.title)){//长文章
                myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_bg_content_item.setVisibility(View.GONE);
                myHolder.rl_bg_long_content_item.setVisibility(View.VISIBLE);
                myHolder.tv_title_long_content.setText(bean.comment.title);
                myHolder.tv_content_long_content.setText(bean.comment.comment);

            }
        }

        if (!TextUtils.isEmpty(bean.comment.view_count)) {
            String s = Utils.parseIntToK(Integer.valueOf(bean.comment.view_count));
            myHolder.tv_view_count_topic_item.setText("阅读 " + s);
        }

//        myHolder.tv_content_dynamic_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {  //#/topicViewDetail/36979
////                String url = Constant.BASE_URL + "#/topicViewDetail/"+bean.comment.comment_id;
////                WebViewActivtiy.start(HomeNewFragment.this.getActivity(),url,"球咖");
////                ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
//                ComentsDetailsActivity.startForResult(2,SearchInfosActivity.this,bean.comment.comment_id,position);
//            }
//        });
//        myHolder.iv_content_bg_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
//                ComentsDetailsActivity.startForResult(2,SearchInfosActivity.this, bean.comment.comment_id,position);
//            }
//        });
//        myHolder.rl_bg_long_content_item.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
//                ComentsDetailsActivity.startForResult(2,SearchInfosActivity.this, bean.comment.comment_id,position);
//            }
//        });


        if (bean.comment.imgListEx != null) {//http://sinacloud.net/topicimg/qkt_0_0_1514886017660840.gif

            if (bean.comment.imgListEx.size() > 0) {
                if ("video".equals(bean.comment.imgListEx.get(0).type)) {
                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.GONE);
                    myHolder.rl_video_item.setVisibility(View.VISIBLE);
                    SearchInfosBean.DataBean.CommentBeanX.ImageList imageList = bean.comment.imgListEx.get(0);
                    String imgUrl = imageList.url.substring(0, imageList.url.lastIndexOf(".")) + ".png";
                    PictureUtils.show(imgUrl, myHolder.iv_video_item);
                    myHolder.rl_video_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            ComentsDetailsActivity.start(HomeNewFragment.this.getActivity(), bean.comment.comment_id);
                            ComentsDetailsActivity.startForResult(true,2,SearchInfosActivity.this, bean.comment.comment_id,position);
                        }
                    });

                    int heightVideo = (int) (DisplayUtil.getScreenWidth(SearchInfosActivity.this) / 16.0 * 8.7);
                    myHolder.iv_video_item.getLayoutParams().height = heightVideo;
                } else {
                    myHolder.rl_video_item.setVisibility(View.GONE);
                    myHolder.gv_dynamic_item.setVisibility(View.VISIBLE);

                    MyGvAdapter adapter = (MyGvAdapter) myHolder.gv_dynamic_item.getAdapter();
                    if (adapter != null) {
                        adapter.setData(bean.comment.imgListEx);
                        adapter.notifyDataSetChanged();
                    }else {
                        myHolder.gv_dynamic_item.setAdapter(new MyGvAdapter(bean.comment.imgListEx));
                    }

                    if(bean.comment.imgListEx.size() == 4 || bean.comment.imgListEx.size() == 2){
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,2);
                    }else if(bean.comment.imgListEx.size() == 1){
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,1);
                    }else{
                        GridViewUtils.updateGridViewLayoutParams(myHolder.gv_dynamic_item,3);
                    }

                    myHolder.gv_dynamic_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positi, long id) {

                            if(bean.comment.imgListEx.size() == 7 && positi>=7){
                                ComentsDetailsActivity.startForResult(2, SearchInfosActivity.this, bean.comment.comment_id, position);
                                return;
                            }

                            String urls = "";
                            for (int i = 0; i < bean.comment.imgListEx.size(); i++) {
                                String url = bean.comment.imgListEx.get(i).url;
                                String delUrl = Utils.delUrl(url);
                                urls += delUrl + ",";
                            }

                            String substring = urls.substring(0, urls.length() - 1);
                            Intent intent = new Intent(SearchInfosActivity.this, PicShowActivity.class);
                            intent.putExtra("urls", substring);
                            intent.putExtra("position", positi);
                            startActivity(intent);
                        }
                    });

                    myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);

                }
            } else {
                myHolder.ll_img_container_dynamic_item.setVisibility(View.GONE);
                myHolder.rl_video_item.setVisibility(View.GONE);
                myHolder.gv_dynamic_item.setVisibility(View.GONE);
            }

        }

        if (bean.topic != null) {
            myHolder.rl_topic_dynamic_item.setVisibility(View.VISIBLE);
//            PictureUtils.show(bean.topic.background_img, myHolder.iv_img_left);
            myHolder.tv_bottom_title_topic_item.setText(bean.topic.title);
//            String s = Utils.parseIntToK(Integer.valueOf(bean.topic.view_count));
//            myHolder.tv_view_count_topic_item.setText(s+" 阅读");
            myHolder.rl_topic_dynamic_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TopicDetailActivity.start(SearchInfosActivity.this,bean.topic.topic_id);
                }
            });
        }else{
            myHolder.rl_topic_dynamic_item.setVisibility(View.GONE);
        }

        myHolder.tv_thumbup_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestThumbup(bean);
            }
        });

        myHolder.tv_comments_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LoginUtils.isUnLogin()) {
                    LoginUtils.goLoginActivity(SearchInfosActivity.this, HomeNewFragment.ACTION);
                    return;
                }
                ComentsDetailsActivity.startForResult(2,SearchInfosActivity.this, bean.comment.comment_id,position);
            }
        });

        myHolder.tv_share_topic_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.toast("分享");
                showShare(position);
            }
        });
        if (bean.comment.praise_status == 1) {//点赞
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_checked_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.drawable.thumb_small);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            myHolder.tv_thumbup_topic_item.setCompoundDrawables(drawable, null, null, null);
        }

        DataBindUtils.setPraiseCount(myHolder.tv_thumbup_topic_item,bean.comment.praise_count);
        DataBindUtils.setComentCount(myHolder.tv_comments_topic_item,bean.comment.reply_count);
//        myHolder.tv_thumbup_topic_item.setText(bean.comment.praise_count + "");
//        myHolder.tv_comments_topic_item.setText(bean.comment.reply_count + "");

    }

    private void showShare(final int position) {
        SharePopuwind sharePopuwind = new SharePopuwind(this, new SharePopuwind.ShareTypeClickListener() {
            @Override
            public void onTypeClick(int type) {
                SearchInfosBean.DataBean.CommentBeanX beanX = searchInfosBean.data.comment.get(position);
                if (beanX.topic == null) {
                    List<String> urls = new ArrayList<>();


                    int typeOfBean;
                    if(TextUtils.isEmpty(beanX.comment.title)){
                        typeOfBean = 1;
                        List<SearchInfosBean.DataBean.CommentBeanX.ImageList> imgListEx = beanX.comment.imgListEx;
                        if(imgListEx != null) {
                            for (int i = 0;i<imgListEx.size();i++) {
                                urls.add(imgListEx.get(i).url);
                            }
                        }
                    }else{
                        typeOfBean = 2;
                        if(!TextUtils.isEmpty(beanX.comment.background_img)){
                            urls.add(beanX.comment.background_img);
                        }else if(beanX.comment.backgroundList != null){
                            List<SearchInfosBean.DataBean.CommentBeanX.ImageList> backgroundList = beanX.comment.backgroundList;
                            for(int i =0;i<backgroundList.size();i++){
                                urls.add(backgroundList.get(i).url);
                            }
                        }

                    }
                    ShareUtils.shareMsgForPointNoTopic(type, beanX.comment.comment_id,beanX.comment.comment, beanX.user.nickname, urls,typeOfBean,beanX.comment.title,SearchInfosActivity.this);
                } else {
                    ShareUtils.shareMsgForPointHasTopic(type, beanX.comment.comment_id, beanX.topic.title, beanX.user.nickname,SearchInfosActivity.this);
                }
            }
        }, new SharePopuwind.ShareInfoBean());

        sharePopuwind.showAtLocation(lv_search_ac, Gravity.CENTER, 0, 0);

    }
    /**
     * 处理点赞
     * @param bean
     */
    private void requestThumbup(final SearchInfosBean.DataBean.CommentBeanX  bean) {

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        if(LoginUtils.isUnLogin()){
            LoginUtils.goLoginActivity(this,HomeNewFragment.ACTION);
            return;
        }

        String url = Constant.BASE_URL + "php/api.php";
        final HttpParams params = new HttpParams();
        params.put("action","topic");
        params.put("type","praise");
        params.put("id",bean.comment.comment_id);
        rl_load_layout = findViewById(R.id.rl_load_layout);
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack(rl_load_layout,this) {
            @Override
            public void onSucces(String string) {
                PraiseBean praiseBean = JsonParseUtils.parseJsonClass(string,PraiseBean.class);
                if(praiseBean.data != null){
                    bean.comment.praise_count = praiseBean.data.praise_count;
                    bean.comment.praise_status = praiseBean.data.praise_status;
                    myAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.toast("点赞失败");
                }
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });

    }


    class MyGvAdapter extends BaseAdapter{


        private List<SearchInfosBean.DataBean.CommentBeanX.ImageList> imgListEx;

        public MyGvAdapter(List<SearchInfosBean.DataBean.CommentBeanX.ImageList> imgListEx) {
            this.imgListEx = imgListEx;
        }

        @Override
        public int getCount() {
            if(imgListEx.size() == 7){
                return 9;
            }
            return imgListEx.size();
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
            View view;
            try{
            SearchInfosBean.DataBean.CommentBeanX.ImageList imageList = imgListEx.get(position);

             view = View.inflate(getContext(),R.layout.img_gif_layout,null);
            ImageView imageView = view.findViewById(R.id.iv);
            TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
            Utils.showType(imageList.url,imageView,tv_gif_icon, imageList.size);


            if(getCount() == 1){
                Utils.scaleImg(SearchInfosActivity.this,view,imageView,imageList.size);
            }}catch (Exception e){
                SearchInfosBean.DataBean.CommentBeanX.ImageList imageList = imgListEx.get(0);

                view = View.inflate(getContext(),R.layout.img_gif_layout,null);
                ImageView imageView = view.findViewById(R.id.iv);
                TextView tv_gif_icon = view.findViewById(R.id.tv_gif_icon);
                Utils.showType(imageList.url,imageView,tv_gif_icon, imageList.size);
                imageView.setVisibility(View.INVISIBLE);
                tv_gif_icon.setVisibility(View.INVISIBLE);
            }
            return view;
        }

        public void setData(List<SearchInfosBean.DataBean.CommentBeanX.ImageList> data) {
            imgListEx = data;
        }
    }

    /**
     * 推荐单
     * @param bean
     * @param myHolder
     */
    private void bindType2(final  int position,final SearchInfosBean.DataBean.CommentBeanX bean, MyHolder myHolder) {

        if(!TextUtils.isEmpty(bean.recommendation.title)){
            myHolder.tv_content_dynamic_item.setVisibility(View.VISIBLE);
            myHolder.tv_content_dynamic_item.setText(bean.recommendation.title);
        }else{
            myHolder.tv_content_dynamic_item.setVisibility(View.GONE);
            myHolder.tv_content_dynamic_item.setText("");
        }


        myHolder.tv_team_name_dynamic_item.setText(bean.match.hostTeamName + "\t\tvs\t\t" + bean.match.awayTeamName);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        spannableStringBuilder.append(bean.match.leagueName);
        myHolder.tv_type_dynamic_item.setText(bean.recommendation.type+"");
        if (!TextUtils.isEmpty(bean.match.match_time)) {
            String matchStartTime = Utils.getMatchStartTime(Long.valueOf(bean.match.match_time + "000"));
            spannableStringBuilder.append(" · "+matchStartTime);
        }

        if(bean.recommendation.reason_len >0){
            spannableStringBuilder.append(" · "+bean.recommendation.reason_len+"字");
        }

        /**
         * 不中退款
         */
        if("1".equals(bean.recommendation.return_if_wrong)){
            spannableStringBuilder.append(" · "+"不中退款");
        }


        if(bean.match.status.equals("COMPLETE")){//一结束     1
            myHolder.ll_price.setVisibility(View.GONE);
            myHolder.tv_right_item.setVisibility(View.VISIBLE);
//            myHolder.iv_result_personal.setVisibility(View.VISIBLE);
            myHolder.tv_buy.setVisibility(View.GONE);
            spannableStringBuilder.append(" · "+bean.recommendation.type);
        }else{
            if(bean.recommendation.buy == 1){
                spannableStringBuilder.append(" · "+bean.recommendation.type);
                myHolder.tv_buy.setVisibility(View.VISIBLE);
                myHolder.ll_price.setVisibility(View.GONE);
                myHolder.tv_right_item.setVisibility(View.VISIBLE);
            }else{
                myHolder.tv_buy.setVisibility(View.GONE);
                myHolder.ll_price.setVisibility(View.VISIBLE);
                myHolder.tv_right_item.setVisibility(View.GONE);
            }
        }

        if(!bean.match.status.equals("COMPLETE")) {
            if (!TextUtils.isEmpty(bean.recommendation.price)&&!"0".equals(bean.recommendation.price) ) {
                Integer integer = Integer.valueOf(bean.recommendation.price);
                String price = integer + "金币";
                myHolder.tv_price_dynamic_item.setText(price);
            }else{
                myHolder.tv_price_dynamic_item.setText("免费");
            }
        }else{
        }


        myHolder.tv_bottom_dynamic_item.setText(spannableStringBuilder);
        spannableStringBuilder.clear();
    }

    static class MyHolder{
        public LinearLayout ll_top_click;
        public ImageView iv_head_dynamic_item;
        public TextView tv_name_dynamic_item;
        public LinearLayout ll_top_tag_container;
        public TextView tv_time_dynamic_item;
        public TextView tv_content_dynamic_item;
        public LinearLayout ll_groom_dynamic_item;
        public TextView tv_team_name_dynamic_item;
        public TextView tv_bottom_dynamic_item;
        public TextView tv_type_dynamic_item;
        public TextView tv_price_dynamic_item;
        public LinearLayout ll_img_container_dynamic_item;
        public RelativeLayout rl_topic_dynamic_item;
        public ImageView iv_img_left;
        public TextView tv_bottom_title_topic_item;
        public TextView tv_view_count_topic_item;
        public LinearLayout ll_bottom_dynamic_item;
        public TextView tv_thumbup_topic_item;
        public TextView tv_comments_topic_item;
        public LinearLayout ll_price;
        public TextView tv_follow_dynamic_item;
        public ImageView iv_vip_header;
        public TextView iv_red_point_header;
        public TextView tv_buy;
        public TextView tv_right_item;

        public RelativeLayout rl_video_item;
        public ImageView iv_video_item;

        public RelativeLayout rl_bg_content_item;
        public TextView tv_content_bg_dynamic_item;
        public ImageView iv_content_bg_item;
        public GridView gv_dynamic_item;
        public RelativeLayout rl_bg_long_content_item;
        public TextView tv_content_long_content;
        public TextView tv_title_long_content;
        public TextView tv_share_topic_item;
        public TextView tv_content_bottom;
    }

    /**
     * 刷新顶部tab状态
     * @param postion
     */
    private void selectOne(int postion){

        tv_groom_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_groom_match_detail.setVisibility(View.INVISIBLE);

        tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_analyes_match_detail.setVisibility(View.INVISIBLE);

        tv_price_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_price_match_detail.setVisibility(View.INVISIBLE);

        tv_outs_match_detail.setTextColor(getResources().getColor(R.color.score_gray_top_title));
        iv_outs_match_detail.setVisibility(View.INVISIBLE);

        switch (postion){
            case 0:
                tv_groom_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_groom_match_detail.setVisibility(View.VISIBLE);
                break;
            case 1:
                tv_analyes_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_analyes_match_detail.setVisibility(View.VISIBLE);
                break;
            case 2:
                tv_price_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_price_match_detail.setVisibility(View.VISIBLE);
                break;
            case 3:
                tv_outs_match_detail.setTextColor(getResources().getColor(R.color.black_28));
                iv_outs_match_detail.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void delFollow(int type,String userId) {
        if(searchInfosBean == null || searchInfosBean.data == null || searchInfosBean.data.comment == null)return;
        for(int i = 0;i<searchInfosBean.data.comment.size();i++){
            SearchInfosBean.DataBean.CommentBeanX.UserBeanX user = searchInfosBean.data.comment.get(i).user;
            String user_id = user.user_id;
            if(user_id.equals(userId)){
                user.follow_status = type;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(searchInfosBean == null || searchInfosBean.data == null)return;
        if(requestCode == 1&&resultCode == 1){//个人详情

            int follow_status = data.getIntExtra("follow_status", 0);
            int position = data.getIntExtra("position", -1);
            if(position != -1) {
                SearchInfosBean.DataBean.CommentBeanX.UserBeanX user = searchInfosBean.data.comment.get(position).user;
                if(user.follow_status == follow_status)return;
                user.follow_status = follow_status;
                delFollow(follow_status, user.user_id);
                myAdapter.notifyDataSetChanged();
                sendHome(user.user_id,follow_status);
            }

        }else if(requestCode == 2&&resultCode == 1){//个人详情

            int follow_status = data.getIntExtra("follow_status", 0);
            int position = data.getIntExtra("position", -1);
            if(position != -1) {
                SearchInfosBean.DataBean.UserBean user = searchInfosBean.data.user.get(position);
                if(user.follow_status == follow_status)return;
                user.follow_status = follow_status;
                delFollow(follow_status, user.user_id);
                myAdapter.notifyDataSetChanged();
                sendHome(user.user_id,follow_status);
            }

        } else if(requestCode == 2 && resultCode == 2){
            int position = data.getIntExtra("position", -1);
            String praise_count = data.getStringExtra("praise_count");

            if(position != -1) {

                    SearchInfosBean.DataBean.CommentBeanX beanX = searchInfosBean.data.comment.get(position);
                    int  follow_status  = Integer.valueOf(data.getStringExtra("follow_status"));
                    if(beanX.comment.praise_count.equals(praise_count) && (beanX.user.follow_status == follow_status))return;
                    int praise_status = data.getIntExtra("praise_status", 0);
                    beanX.comment.praise_count = praise_count;
                    beanX.comment.praise_status = praise_status;
                    beanX.user.follow_status = follow_status;
                    delFollow(follow_status,beanX.user.user_id);
                    myAdapter.notifyDataSetChanged();
                    sendHome(beanX.user.user_id,follow_status);
                }

        }else if(requestCode == 44 && resultCode == 1){
            int position = data.getIntExtra("position", -1);

            if(position != -1) {

                SearchInfosBean.DataBean.UserBean beanX = searchInfosBean.data.user.get(position);
                int  follow_status  = data.getIntExtra("follow_status",0);
                beanX.follow_status = follow_status;
                delFollow(follow_status,beanX.user_id);
                myPersonalAdapter.notifyDataSetChanged();
                sendHome(beanX.user_id,follow_status);
            }

        }

    }

    /**
     * 搜索适配器，填充搜索自动补全内容
     */
    private MySearchAdapter mySearchAdapter = new MySearchAdapter();
    private class MySearchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            hidenNoData();
            if(searchInfosBean != null && searchInfosBean.data != null && searchInfosBean.data.wordlist != null){
                return searchInfosBean.data.wordlist.size();
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

            Holder holder;
            if(convertView == null || convertView.getTag() == null){
                holder = new Holder();
                convertView = View.inflate(getContext(),R.layout.search_item_layout,null);
                holder.tv_top_item = convertView.findViewById(R.id.tv_top_item);
                holder.iv_left_item = convertView.findViewById(R.id.iv_left_item);
                holder.tv_value_item = convertView.findViewById(R.id.tv_value_item);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            SearchInfosBean.DataBean.Word s = searchInfosBean.data.wordlist.get(position);

            holder.tv_top_item.setVisibility(View.GONE);
            PictureUtils.show(R.drawable.search_home,holder.iv_left_item);


            holder.tv_value_item.setText(s.tag);

            return convertView;
        }
    }

    /**
     * 热搜adapter
     */
    private MyHostSearchAdapter myHostSearchAdapter = new MyHostSearchAdapter();
    private class MyHostSearchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            hidenNoData();
            if(searchInfosBean != null && searchInfosBean.data != null && searchInfosBean.data.hotSearchList != null){
                return searchInfosBean.data.hotSearchList.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if(convertView == null){
                holder = new Holder();
                convertView = View.inflate(getContext(),R.layout.search_item_layout,null);
                holder.tv_top_item = convertView.findViewById(R.id.tv_top_item);
                holder.iv_left_item = convertView.findViewById(R.id.iv_left_item);
                holder.tv_value_item = convertView.findViewById(R.id.tv_value_item);
                holder.iv_rigth_item = convertView.findViewById(R.id.iv_rigth_item);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            String s = searchInfosBean.data.hotSearchList.get(position);
            final String[] split = s.split(splitTag);

            if(position>0){
                String s1 = searchInfosBean.data.hotSearchList.get(position - 1).split(splitTag)[1];
                if(!split[1].equals(s1)){
                    holder.tv_top_item.setVisibility(View.VISIBLE);
                    holder.tv_top_item.setText("为你推荐");
                }else{
                    holder.tv_top_item.setVisibility(View.GONE);
                }
            }else{
                if(split[1].equals("0")){
                    holder.tv_top_item.setText("搜索记录");
                }else{
                    holder.tv_top_item.setText("为你推荐");
                }
                holder.tv_top_item.setVisibility(View.VISIBLE);
            }
            if("0".equals(split[1])){
                PictureUtils.show(R.drawable.search_history,holder.iv_left_item);
                holder.iv_rigth_item.setVisibility(View.VISIBLE);
                holder.iv_rigth_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletHistory(split[0]);
                        searchInfosBean.data.hotSearchList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                holder.tv_value_item.setTypeface(Typeface.DEFAULT);
                holder.tv_value_item.setTextColor(getResources().getColor(R.color.black_66));
            }else{
                PictureUtils.show(R.drawable.search_hot,holder.iv_left_item);
                holder.iv_rigth_item.setVisibility(View.GONE);
                holder.tv_value_item.setTypeface(Typeface.DEFAULT);
                holder.tv_value_item.setTextColor(getResources().getColor(R.color.black_28));
            }
            holder.tv_value_item.setText(split[0]);

            return convertView;
        }
    }


    static class Holder{
        public TextView tv_top_item;
        public ImageView iv_left_item;
        public TextView tv_value_item;
        public ImageView iv_rigth_item;
    }

    /**
     * 相关用户adapter
     */
    private MyPersonalAdapter myPersonalAdapter = new MyPersonalAdapter();
    private class MyPersonalAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            if(searchInfosBean != null && searchInfosBean.data != null && searchInfosBean.data.user != null){
                int size = searchInfosBean.data.user.size();
                if(size == 0){
                    showNoData();
                }else{
                    hidenNoData();
                    size ++;
                }
                return size;
            }
            showNoData();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(position == searchInfosBean.data.user.size()){
                convertView = View.inflate(QCaApplication.getContext(), R.layout.loading_bottom, null);
                TextView tv_content = convertView.findViewById(R.id.tv_content);
                if(pre_page == 1){
                    tv_content.setText("加载中...");
                }else{
                    tv_content.setText(Constant.LOAD_BOTTOM_TOAST);
                }
                return convertView;
            }

            PersonalHolder myHolder;
            if(convertView == null || convertView.getTag() == null){
                myHolder = new PersonalHolder();
                convertView = View.inflate(getContext(),R.layout.groom_personal_item,null);
                myHolder.iv_header_item = convertView.findViewById(R.id.iv_header_item);
                myHolder.iv_vip_item = convertView.findViewById(R.id.iv_vip_item);
                myHolder.tv_name_item = convertView.findViewById(R.id.tv_name_item);
                myHolder.tv_reason_item = convertView.findViewById(R.id.tv_reason_item);
                myHolder.tv_follow_item = convertView.findViewById(R.id.tv_follow_item);
                convertView.setTag(myHolder);
            }else{
                myHolder = (PersonalHolder) convertView.getTag();
            }

            SearchInfosBean.DataBean.UserBean dataBean = searchInfosBean.data.user.get(position);
            PictureUtils.showCircle(dataBean.headurl,myHolder.iv_header_item);

            if(!TextUtils.isEmpty(dataBean.verify_type)&&Integer.valueOf(dataBean.verify_type)>0){
                myHolder.iv_vip_item.setVisibility(View.VISIBLE);
            }else{
                myHolder.iv_vip_item.setVisibility(View.GONE);
            }


            if(!(SPUtils.getString(getContext(), Constant.USER_ID).equals(dataBean.user_id))){
                myHolder.tv_follow_item.setVisibility(View.VISIBLE);
                if (dataBean.follow_status == 1) {//关注
                    myHolder.tv_follow_item.setTag(1);
                    myHolder.tv_follow_item.setBackground(getResources().getDrawable(R.drawable.shape_gray_storke_cricle));
                    myHolder.tv_follow_item.setTextColor(Color.parseColor("#e8e8e8"));
                    myHolder.tv_follow_item.setText("已关注");
                } else {
                    myHolder.tv_follow_item.setTag(0);
                    myHolder.tv_follow_item.setBackground(getResources().getDrawable(R.drawable.shape_yellow_cricel));
                    myHolder.tv_follow_item.setTextColor(Color.parseColor("#282828"));
                    myHolder.tv_follow_item.setText(" 关注 ");
                }

            }else{
                myHolder.tv_follow_item.setVisibility(View.GONE);
            }
            myHolder.tv_name_item.setText(dataBean.nickname);
            if(TextUtils.isEmpty(dataBean.verify_reason)){
                myHolder.tv_reason_item.setText("");
            }else {
                myHolder.tv_reason_item.setText(dataBean.verify_reason);
            }

            myHolder.tv_follow_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestFollows(position,typeTop);
                }
            });


            if(position == getCount()-3||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestNetDataForSearch(typeTop,keyTitle);
                }
            }
            return convertView;
        }
    }

    static class PersonalHolder{
        public ImageView iv_header_item;
        public ImageView iv_vip_item;
        public TextView tv_name_item;
        public TextView tv_reason_item;
        public TextView tv_follow_item;
    }


    /**
     * 相关话题adapter
     */
    private MyTopicAdapter myTopicAdapter = new MyTopicAdapter();
    private class MyTopicAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if(searchInfosBean != null && searchInfosBean.data != null && searchInfosBean.data.topic != null){
                int size = searchInfosBean.data.topic.size();
                if(size == 0){
                    showNoData();
                }else{
                    hidenNoData();
//                    size ++;
                }
                return size;
            }
            showNoData();
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


            Holder holder;
            if(convertView == null || convertView.getTag() == null){
                holder = new Holder();
                convertView = View.inflate(getContext(),R.layout.search_item_layout,null);
                holder.tv_top_item = convertView.findViewById(R.id.tv_top_item);
                holder.iv_left_item = convertView.findViewById(R.id.iv_left_item);
                holder.tv_value_item = convertView.findViewById(R.id.tv_value_item);
                convertView.setTag(holder);
            }else{
                holder = (Holder) convertView.getTag();
            }
            SearchInfosBean.DataBean.TopicBean topicBean = searchInfosBean.data.topic.get(position);
            holder.tv_top_item.setVisibility(View.GONE);

            PictureUtils.show(R.drawable.jinghao_search,holder.iv_left_item);
            holder.tv_value_item.setText(topicBean.title);
            holder.tv_value_item.setTypeface(Typeface.DEFAULT_BOLD);



            if(position == getCount()-3||(getCount()<3)){
                if(pre_page == 1) {
                    rl_load_layout = null;
                    page++;
                    requestNetDataForSearch(typeTop,keyTitle);
                }
            }
            return convertView;
        }
    }
}
