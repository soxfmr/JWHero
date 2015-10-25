package com.qqpractice.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

/**
 * 网络连接模块
 * 
 * @author soxfmr
 * 
 * **/

public class HttpLib {
	
	public static String connect(final String URL, final String[] DATA) {
		String mRet = null;
		
		List<NameValuePair> Params = new ArrayList<NameValuePair>();
		//获取数组数据
		for(int i=0, index = 0, count = DATA.length / 2;
				i<count; i++) {
			Params.add(new BasicNameValuePair(DATA[index], 
					DATA[index+1]));
			index += 2;
		}
		
		try {
			HttpPost post = new HttpPost(URL);
			//转换编码并发送数据
			post.setEntity(new UrlEncodedFormEntity(Params, HTTP.UTF_8));
			
			HttpClient client = new DefaultHttpClient();
			//client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20 * 1000);
			//client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20 * 1000);
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
			//取回响应数据
			HttpResponse httpResponse = client.execute(post);
			if(httpResponse.getStatusLine().getStatusCode()==200) {
				mRet = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
			}
			//System.out.println(httpResponse.getStatusLine().getStatusCode());
			//Log.i("TAG", mRet);
			
		} catch (Exception e) {
			return mRet;
		}
		
		return mRet;

	}
	
}
