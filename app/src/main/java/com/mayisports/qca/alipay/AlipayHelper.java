package com.mayisports.qca.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;

/**
 * @Module :
 * @Comments : 支付宝支付辅助类
 * @Modified :
 */
public class AlipayHelper {
    public static final int SDK_PAY_FLAG = 1;
    private static AlipayHelper alipayHelper;

    public static AlipayHelper getInstance() {
        if (alipayHelper == null) {
            alipayHelper = new AlipayHelper();
        }
        return alipayHelper;
    }
    /**
     * 调起支付宝支付
     *
     * @param payInfo
     */
    public void onAliPay(final String payInfo, final Handler handler, final Activity activity) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(activity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.obj = result;
                msg.what = SDK_PAY_FLAG;
                handler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
