package com.qqpractice.core;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ToolsUtils {
	public static final int GENERIC_UNKNOW = 0x0;
	
	/**
	 * @return 网络连接状态
	 * */
	public static final boolean isNetworkConnected(Context context) {
		boolean mRet = false;
    	ConnectivityManager connMgr = (ConnectivityManager) 
    			context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo ni = connMgr.getActiveNetworkInfo();
    	if(ni != null) 
    	{
    		if(ni.isConnected()) {
    			mRet = true;
        	}
    	}
    	ni = null;
		connMgr = null;
    	return mRet;
	}
	
	/** 分钟转换为毫秒 **/
	public static final long MtoMS(int period) {
		return period * 60 * 1000;
		
	}
	
	/** 小时转换为毫秒 **/
	public static final long HtoMS(int period) {
		return period * 3600 * 1000;
		
	}
}
