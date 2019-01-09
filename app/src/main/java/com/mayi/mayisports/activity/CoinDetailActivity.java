package com.mayi.mayisports.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.icu.util.ULocale;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.mayi.mayisports.QCaApplication;
import com.mayi.mayisports.R;
import com.mayisports.qca.alipay.AlipayHelper;
import com.mayisports.qca.alipay.PayResult;
import com.mayisports.qca.bean.WePayBean;
import com.mayisports.qca.fragment.MineFragment;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.JsonParseUtils;
import com.mayisports.qca.utils.SPUtils;
import com.mayisports.qca.utils.ToastUtils;
import com.mayisports.qca.utils.Utils;
import com.mayisports.qca.utils.request_net_utils.RequestHttpCallBack;
import com.mayisports.qca.utils.request_net_utils.RequestNetWorkUtils;
import com.mayisports.qca.view.ToastPricePopuWindow;
import com.mayi.mayisports.wxapi.PayWechatBean;
import com.mayi.mayisports.wxapi.WechatPayHelper;

import org.kymjs.kjframe.http.HttpParams;

import static com.mayi.mayisports.QCaApplication.getContext;

/**
 * 金币详情页
 */
public class CoinDetailActivity extends BaseActivity implements AdapterView.OnItemClickListener {


    public static void start(Activity activity){
        Intent intent = new Intent(activity,CoinDetailActivity.class);
        activity.startActivity(intent);
    }


    public static final String COIN_ACTION = "coin_action";

    private LocalBroadcastManager localBroadcastManager;
    private Rec rec;
    private void initReceiver(){
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        rec = new Rec();
        IntentFilter intentFilter = new IntentFilter(COIN_ACTION);
        localBroadcastManager.registerReceiver(rec,intentFilter);
    }
    private void destroyReceiver(){
        localBroadcastManager.unregisterReceiver(rec);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyReceiver();
    }

    private class Rec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

             if(tv_balance_coin == null)return;

            ToastUtils.toastNoStates("充值成功");
           tv_balance_coin.postDelayed(new Runnable() {
               @Override
               public void run() {
                   initView();
               }
           },1000);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }

    private TextView tv_toast_coin;
    private GridView gv_coin;
    private TextView tv_balance_coin;
    private TextView tv_withdraw_coin;
    private TextView tv_withdraw_btn_coin;
    private ImageView iv_wepay_coin;
    private ImageView iv_alipay_coin;
    private TextView tv_topup_coin;

    private MyAdapter myAdapter;

    @Override
    protected int setViewForContent() {
        return R.layout.activity_coin_detail;
    }

    @Override
    protected void initView() {
        super.initView();
        initReceiver();
        tv_ritht_title.setText("明细");
        tv_title.setText("金币");
        iv_left_title.setOnClickListener(this);
        tv_ritht_title.setOnClickListener(this);

        tv_toast_coin = findViewById(R.id.tv_toast_coin);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString s1 = new SpannableString("充值金币仅用于球咖内部消费，不可提现；\n如充值遇到问题，可联系客服微信:");
        spannableStringBuilder.append(s1);
        SpannableString s2 = new SpannableString("mayisports");
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        s2.setSpan(styleSpan,0,s2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        ForegroundColorSpan span = new ForegroundColorSpan(getResources().getColor(R.color.coment_black));
        s2.setSpan(span,0,s2.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(s2);
        SpannableString s3 = new SpannableString("");
        spannableStringBuilder.append(s3);

        tv_toast_coin.setText(spannableStringBuilder);


        gv_coin = findViewById(R.id.gv_coin);
        myAdapter = new MyAdapter();
        gv_coin.setAdapter(myAdapter);
        gv_coin.setOnItemClickListener(this);

        tv_balance_coin = findViewById(R.id.tv_balance_coin);
        tv_withdraw_coin = findViewById(R.id.tv_withdraw_coin);
        tv_withdraw_btn_coin = findViewById(R.id.tv_withdraw_btn_coin);
        iv_wepay_coin = findViewById(R.id.iv_wepay_coin);
        iv_wepay_coin.setOnClickListener(this);
        iv_alipay_coin = findViewById(R.id.iv_alipay_coin);
        iv_alipay_coin.setOnClickListener(this);
        tv_topup_coin = findViewById(R.id.tv_topup_coin);
        tv_topup_coin.setOnClickListener(this);

        /**
         * 初始化支付方式
         */
        updatePay(payType);
        String balance = SPUtils.getString(this, Constant.COIN);
        if(!TextUtils.isEmpty(balance)) {
            double aDouble = Double.valueOf(balance);
            if (aDouble == 0.0) {
                tv_balance_coin.setText("0");
            } else if (aDouble % 1 > 0) {
                tv_balance_coin.setText(aDouble+"");
            } else {
                tv_balance_coin.setText(((int) aDouble)+"");
            }
        }else{
            tv_balance_coin.setText("0");
        }

        String withdraw = SPUtils.getString(this, Constant.WITHDRAW_COIN);
        if(!TextUtils.isEmpty(withdraw)) {
            double aDouble = Double.valueOf(withdraw);
            if (aDouble == 0.0) {
                tv_withdraw_coin.setText("0");
            } else if (aDouble % 1 > 0) {
                tv_withdraw_coin.setText("" + aDouble);
            } else {
                tv_withdraw_coin.setText("" + ((int) aDouble));
            }
        }else{
            tv_withdraw_coin.setText("0");
        }

        tv_withdraw_btn_coin.setOnClickListener(this);

    }




    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tv_ritht_title:
//                WebViewActivtiy.start(this, Constant.BASE_URL+"#/coinDetail","明细");
                CoinListActivity.start(this);
                break;
            case R.id.iv_left_title:
                finish();
                break;
            case R.id.iv_wepay_coin:
                payType = 1;
                updatePay(payType);
                break;
            case R.id.iv_alipay_coin:
                payType = 2;
                updatePay(payType);
                break;
            case R.id.tv_topup_coin:
                Integer count = Integer.valueOf(coins[checkPosition].replace("金币", ""));
                tv_topup_coin.setEnabled(false);
                tv_topup_coin.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_topup_coin.setEnabled(true);
                    }
                },2500);

                if(payType == 1){//微信
                    requestWeChatDatas(count);
                }else{//支付宝
                    requestAlipayDatas(count);
                }
                break;
            case R.id.tv_withdraw_btn_coin:
//                WebViewActivtiy.start(this,Constant.BASE_URL+"#/drawCash","提现");

                String phone = SPUtils.getString(this, Constant.MOBLIE);
                String withdraw = SPUtils.getString(this, Constant.WITHDRAW_COIN);
                if(TextUtils.isEmpty(withdraw))withdraw = "0";
                if(TextUtils.isEmpty(phone)){//提示绑定手机号
                    showToast();
                }else if(Double.valueOf(withdraw)<20){//金币不小于20
                    showPriceToast();
                } else {
                    WithdrawCoinActivity.start(this);
                }
                break;
        }
    }


    /**
     * 提示金币不足
     */
    private ToastPricePopuWindow priceToast;
    private void showPriceToast() {

        priceToast = new ToastPricePopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tv_cancle_toast_price){
                    priceToast.dismiss();
                }else if(v.getId() == R.id.tv_go_toast_price){
                    String url = SPUtils.getString(QCaApplication.getContext(), Constant.MID_URL);
                    if(TextUtils.isEmpty(url)){
                        GuessingCompetitionActivity.start(CoinDetailActivity.this);
                    }else{
                        try {
                            WebViewActivtiy.start(CoinDetailActivity.this, url, "球咖",true);
                        }catch (Exception e){

                        }
                    }
                    priceToast.dismiss();
                }

            }
        },"最低提现金额20元,快去参加\n更多有奖活动吧！","算了","前往");

        priceToast.showAtLocation(tv_balance_coin, Gravity.CENTER,0,0);
    }


    /**
     * 提示是否绑定手机号
     */
    private ToastPricePopuWindow toastPricePopuWindow;
    private void showToast() {
         toastPricePopuWindow = new ToastPricePopuWindow(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.tv_cancle_toast_price){
                    toastPricePopuWindow.dismiss();
                }else if(v.getId() == R.id.tv_go_toast_price){
                    BindPhoneNumActivity.start(CoinDetailActivity.this);
                    toastPricePopuWindow.dismiss();
                }

            }
        },"提现需要绑定手机号","算了","绑定");

         toastPricePopuWindow.showAtLocation(tv_balance_coin, Gravity.CENTER,0,0);

    }

    private void requestWeChatDatas(double count) {
//        getWechatPay();  /php/wxpay/wxpay_api.php?total_fee=6
        String url = Constant.BASE_URL + "/php/wxpay/wxpay_api.php";
        HttpParams params = new HttpParams();
        params.put("total_fee",count+"");
        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {
            @Override
            public void onSucces(String string) {
               getWechatPay(string);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    private void requestAlipayDatas(double count) {
        String url = "http://adminqiuka.applinzi.com/ZfbPay";
        HttpParams params = new HttpParams();
        params.put("total_fee",count+"");
        params.put("user_id",SPUtils.getString(this,Constant.USER_ID));

        //支付秘钥
        //qi!1@u#2$k%3^a*8(q)i_9*u0kfbxswdklmaa

        String str = "qi!1@u#2$k%3^a*8(q)i_9*u0kfbxswdklmaa";
        String content = SPUtils.getString(this,Constant.USER_ID)+count+str;
        String s = "";
        try {
           s = Utils.MD5(Utils.MD5(content));
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("sign",s);

        RequestNetWorkUtils.getRequest(url, params, new RequestHttpCallBack() {

            //alipay_sdk=alipay-sdk-php-20161101&amp;app_id=2016051601408000&amp;biz_content=%7B%22subject%22%3A%22%5Cu652f%5Cu4ed8%5Cu5b9dAPP%5Cu652f%5Cu4ed8%22%2C%22out_trade_no%22%3A%221496053052E%2B12%22%2C%22total_amount%22%3A%226%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%7D&amp;charset=UTF-8&amp;format=json&amp;method=alipay.trade.app.pay&amp;notify_url=adminqiuka.applinzi.com%2FZfbAttestation&amp;sign_type=RSA2&amp;timestamp=2018-01-03+14%3A22%3A10&amp;version=1.0&amp;sign=ocCbJkhrB3GLuZj7foY%2FhOBb7%2F7h3CcK1Gn%2F5K7poqRe%2F4FP2XBEgpANLlhq69BMiN05BQkpFKho%2FadByrZTU3N7G8qnPUeaHJpOS2%2Bioo%2FP5HWA1i8OigY0XBijqJNET%2BySpQCx17uaNSJL7ttNM2ZBh1U4yAVOpcLwvzBYPEEpksishV6FpT2o8Iyd6FiVSGOwbLTcALq%2BgzhcacM5hNmgCUbJ1BUZvZbx6KvBNfaBrDyW1GI8msV1wJGkgw4KUntD2FzwR%2FkLPGAZoDoMANLYEAgnBV4kQQK62zH%2F0r4NgUhy%2B68i91hoKwFWTCweJNWnv%2FBXx78YRWUGLBcj7w%3D%3D
            @Override
            public void onSucces(String string) {
                getAlipay(string);
            }

            @Override
            public void onfailure(int errorNo, String strMsg) {
                ToastUtils.toast(Constant.NET_FAIL_TOAST);
            }
        });
    }

    /**
     * 1 微信  2 支付宝
     * @param type
     */
    private int payType = 1;
    private void updatePay(int type){
        switch (type){
            case 1:
                iv_wepay_coin.setImageDrawable(getResources().getDrawable(R.drawable.choose_check));
                iv_alipay_coin.setImageDrawable(getResources().getDrawable(R.drawable.choose_uncheck));
                break;
            case 2:
                iv_alipay_coin.setImageDrawable(getResources().getDrawable(R.drawable.choose_check));
                iv_wepay_coin.setImageDrawable(getResources().getDrawable(R.drawable.choose_uncheck));
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(checkPosition == i){
            return;
        }
        checkPosition = i;
        myAdapter.notifyDataSetChanged();
    }


    private void getWechatPay(String payInfo) {
        if (!WechatPayHelper.getInstance().isWXAppInstalled()) {
            ToastUtils.toast("您尚未安装微信");
            return;
        }

        WePayBean wePayBean = JsonParseUtils.parseJsonClass(payInfo, WePayBean.class);
        if(wePayBean.result != null) {
            PayWechatBean payWechatBean = wePayBean.result;
            WechatPayHelper.getInstance().onWechatPay(payWechatBean);
        }
    }

    private void getAlipay(String payInfo) {
        payInfo =  payInfo.replace("&amp;","&");
        AlipayHelper alipayHelper = AlipayHelper.getInstance();
        alipayHelper.onAliPay(payInfo, mHandler,this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AlipayHelper.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        ToastUtils.toast("支付成功");
                        toRefreshCoin();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            ToastUtils.toast("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            ToastUtils.toast("支付失败");
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public static void toRefreshCoin() {
        Intent intent = new Intent(MineFragment.MINE_FG_ACTION);
        intent.putExtra(LoginActivity.RESULT,3);
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent);

        Intent intent1 = new Intent(CoinDetailActivity.COIN_ACTION);
        LocalBroadcastManager.getInstance(QCaApplication.getContext()).sendBroadcast(intent1);
    }


    private int checkPosition;
    private String[] coins = {"6金币","30金币","50金币","98金币","208金币","488金币"};
    private String[] coinsRMB = {"￥ 6元","￥ 30元","￥ 50元","￥ 98元","￥ 208元","￥ 488元"};
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return coins.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View layout = View.inflate(getContext(),R.layout.coin_detail_layout,null);
            TextView tv1 = layout.findViewById(R.id.tv1);
            tv1.setText(coins[i]);
            TextView tv2 = layout.findViewById(R.id.tv2);
            tv2.setText(coinsRMB[i]);

            if(checkPosition == i){
                layout.setBackground(getContext().getResources().getDrawable(R.drawable.topup_check));
            }else{
                layout.setBackground(getContext().getResources().getDrawable(R.drawable.topup_uncheck));
            }
            return layout;
        }
    }
}
