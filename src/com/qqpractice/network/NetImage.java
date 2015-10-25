package com.qqpractice.network;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

/**
 * 获取网络图片模块
 * 
 * @author soxfmr
 * 
 * **/

@SuppressLint("HandlerLeak")
public class NetImage implements Runnable {
	/** Thread running tag, which limit only one of thread. */
	private boolean bRun = false;
	
	private String imgurl = null;
	private ImageView imgview = null;
	
	public NetImage(String imgurl, ImageView imgview) {
		this.imgurl = imgurl;
		this.imgview = imgview;
		
	}
	
	public void load() {
		if(!bRun) new Thread(this).start();
	}

	@Override
	public void run() {
		try{
			bRun = true;
			
			URL url = new URL(imgurl);
			URLConnection con = url.openConnection();
			con.connect();
			
			InputStream is = con.getInputStream();
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			
			Message msg = updateHandler.obtainMessage();
			msg.obj = bitmap;
			msg.sendToTarget();
			
			url = null;
			con = null;
			is = null;
			bitmap = null;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			bRun = false;
		}
		
	}
	
	private Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			imgview.setImageBitmap((Bitmap)msg.obj);
			super.handleMessage(msg);
			
		}
		
		
	};

}