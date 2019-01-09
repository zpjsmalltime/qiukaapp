package com.mayisports.qca.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.mayi.mayisports.activity.CoinDetailActivity;
import com.mayisports.qca.utils.Constant;
import com.mayisports.qca.utils.ToastUtils;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @Module :
 * @Comments : 微信支付
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		api = WXAPIFactory.createWXAPI(getApplicationContext(),  Constant.APP_ID_WEPAY);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		Log.i("first", "req--" + req.toString());
	//	ToastUtils.toastNoStates("微信来了"+req.toString());
	}

	@Override
	public void onResp(final BaseResp resp) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				String jsFunc = "getWechatPayResult("+resp.errCode+")";
//				ToastUtils.toast("微信支付状态："+resp.errCode+resp.errStr);
				if(resp.errCode == 0) {
					CoinDetailActivity.toRefreshCoin();
				}else{

				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
					//系统版本大于等于4.4
//					MainActivity.webView.evaluateJavascript(jsFunc, null);
				} else {
					//系统版本小于4.4
					String call = "javascript:" + jsFunc;
//					MainActivity.webView.loadUrl(call);
				}
				finish();
				Log.d("WXPayEntryActivity", "result:" + resp.errCode);
			}
		},1000);
	}
}




















