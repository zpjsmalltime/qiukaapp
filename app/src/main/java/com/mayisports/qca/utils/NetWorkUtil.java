package com.mayisports.qca.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetWorkUtil {

	private NetWorkUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 判断网络是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (null != connectivity) {

			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (null != info && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 判断是否是wifi连接
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (cm == null)
			return false;
		try {
			return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
		}catch (Exception e){
			return false;
		}

	}
	
	/**
	 * 打开wifi
	 */
	public static void openWifi(Context context, boolean isOpen) {
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(isOpen);
	}

	/**
	 * 打开网络设置界面
	 */
	public static void openSetting(Activity activity) {
		Intent intent = new Intent("/");
		ComponentName cm = new ComponentName("com.android.settings",
				"com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}
	/**
	 * 获取ip v4 地址
	 * @return 失败返回 -1
	 *
	 */
	public static String getLocalIpV4Address() {

			try {
				for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
					NetworkInterface intf = en.nextElement();
					for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
						InetAddress inetAddress = enumIpAddr.nextElement();
						if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
							//if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet6Address) {

							return inetAddress.getHostAddress().toString();
						}
					}
				}
			} catch (Exception e) {
			}
			return "-1";
	}

	public static boolean byIsConnectToOpen(Context context){
		boolean connected = isConnected(context);
		if(!connected){
			ToastUtils.toast("当前网络连接异常，请稍后再试");
		}
		return connected;
	}
	public static boolean byIsConnectToOpen(Context context, String msg){
		boolean connected = isConnected(context);
		if(!connected){
			ToastUtils.toast(msg);
		}
		return connected;
	}
}

