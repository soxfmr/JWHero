package com.qqpractice.executer;

import org.apache.commons.lang3.StringEscapeUtils;

public abstract class Executer {
	public String mData = null;
	public String sid = null;
	
	public Executer(String sid) {
		this.sid = sid;
	}
	
	public void requestData(String data) {
		data = StringEscapeUtils.unescapeHtml3(data);
		this.mData = data;
	}

	/** 检测返回结果 **/
	public boolean check(String tag) {
		
		if(mData!=null && !mData.equals("")) {
			return tag==null || mData.indexOf(tag)!=-1;
			
		}
		return false;
	}
	
}
