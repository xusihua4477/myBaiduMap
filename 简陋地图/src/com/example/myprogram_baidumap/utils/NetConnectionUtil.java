package com.example.myprogram_baidumap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetConnectionUtil {
	public static final int NET_NO_CONNECTION = 0;
	public static final int NET_WIFE_CONNECTION = 1;
	public static final int NET_GPRS_CONNECTION = 2;
	public static final int NET_OK_NOT_KNOWN_TYPE = 3;

	public static int getNetStatus(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	
		if (connectivityManager==null) {
			System.out.println("connectivityManager是空");
			return NET_NO_CONNECTION;
		}
		
		
	 NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	if (activeNetworkInfo==null || !activeNetworkInfo.isAvailable()) {
		System.out.println("connectivityManager是空");
		return NET_NO_CONNECTION;
		
	}
	if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_MOBILE) {
		System.out.println("移动网络");
		return NET_GPRS_CONNECTION;
	}
	
	if (activeNetworkInfo.getType()==ConnectivityManager.TYPE_WIFI) {
		System.out.println("wifi网络");
		return NET_WIFE_CONNECTION;
	}
	
	if (activeNetworkInfo.isAvailable()) {
		System.out.println("已经连接,网络类型未知");
		return NET_OK_NOT_KNOWN_TYPE;
	}
	
	return 0;
	
	}
}
