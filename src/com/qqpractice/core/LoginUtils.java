package com.qqpractice.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import com.qqpractice.activity.R;
import com.qqpractice.network.HttpLib;
import com.qqpractice.ui.VeriftyDialog;
import com.qqpractice.ui.bases.BaseActivity;

/**
 * 登录模块
 * 使用 onLogin 进行登录<br />
 * 实现 OnLoginResultListener 接口监听登录状态
 * @version 2014-06-18 16:52 rewrite
 * */

public class LoginUtils {
	private static final String STR_LOGIN_SUCCESS = "登录成功";
	private static final String STR_LOGIN_FAILED = "登录失败";
	private static final String STR_LOGIN_PASSWORD_ERROR1 = "密码不正确";
	private static final String STR_LOGIN_PASSWORD_ERROR2 = "登录密码错误";
	private static final String STR_LOGIN_USERNAME_NULL = "账号不能为空";
	private static final String STR_LOGIN_NEED_SAFE_CODE = "请输入验证码";
	
	private static final int VERIGY_IMGURL = 0;
	private static final int VERIGY_RVALUE = 1;
	private static final int VERIGY_HIDEPW = 2;
	private static final int VERIGY_RANDOM = 3;
	private static final int VERIGY_USERIP = 4;
	
	private static final int TYPE_LOGIN_SUCCESS = 1;
	private static final int TYPE_LOGIN_FAILED = 2; //账号密码错误
	private static final int TYPE_LOGIN_VERIFY = 3; //需要验证码
	private static final int TYPE_LOGIN_UNKNOW = 0;
	
	private static final int ACTION_LOGIN = 1;
	private static final int ACTION_VERIFY = 2;

	private String tmp = null;
	
	private String user = null;
	private String pwd = null;
	
	private Context mContext = null;

	private String initSid = null;
	private String initVdata = null;
	private String[] verifyData = null;
	private String RealSid = null;
	/** 数据分离对象 */
	private Extractor extractor = null;
	
	private AsyncTask<Void, Void, Integer> asynctask = null;
	
	private OnLoginResultListener mLoginResultListener = null;
	
	
	public interface OnLoginResultListener {
		public void onSuccess();
		public void onFailed();
		public void onFinish();
	}
	
	public void setOnLoginResultListener(OnLoginResultListener l) {
		mLoginResultListener = l;
	}
	
	
	public LoginUtils(Context context) {
		mContext = context;
		
		extractor = new Extractor();
	}
	
	public String getRealSid() {
		return RealSid;
	}
	
	/**
	 * 取消登陆，关闭登陆对话框
	 * */
	public void onCancel() {
		asynctask.cancel(true);
		asynctask = null;
	}
	
	/**
	 * 是否已经完成登陆操作
	 * */
	public boolean isLogined() {
		if(asynctask!=null && (asynctask.getStatus()==AsyncTask.Status.RUNNING
				|| asynctask.getStatus()==AsyncTask.Status.PENDING) ) {
			return false;
		}
		return true;
	}
	
	/**
	 * 登陆函数，验证码验证操作已封装入该类中
	 * */
	public void onLogin(String user, String pwd) {
		this.user = user;
		this.pwd = pwd;
		onLogin(null);
	}
	
	/**
	 * 登陆、验证操作
	 * @param code 如果进行验证操作则传入验证码，否则为null
	 * */
	private void onLogin(final String code) {
		if(!ToolsUtils.isNetworkConnected(mContext)) {
			((BaseActivity)mContext).OnNotice(R.string.common_error_unknow);
			return ;
		}
		
		asynctask = null;
		
		System.gc();
		
		asynctask = new AsyncTask<Void, Void, Integer>(){
			ProgressDialog dialog = null;
			int action;
			
			@Override
			protected void onPreExecute() {
				dialog = new ProgressDialog(mContext);
				dialog.setMessage(mContext.getString(R.string.login_loading));
				dialog.setCancelable(true);
				dialog.show();
				super.onPreExecute();
			}

			@Override
			protected Integer doInBackground(Void... params) {
				int mRet = 0;
				action = code == null ? ACTION_LOGIN : ACTION_VERIFY ;
				//登录类型
				if(action == ACTION_LOGIN) {
					mRet = dologin();
				}else {
					mRet = doverify(code);
				}
				return mRet;
			}
			
			@Override
			protected void onPostExecute(Integer result) {
				dialog.dismiss();
				//登陆类型判断
				switch(result) {
				case TYPE_LOGIN_SUCCESS:
					mLoginResultListener.onSuccess();
					break;
				case TYPE_LOGIN_VERIFY:
					//验证码错误提示
					if(action == ACTION_VERIFY)
						((BaseActivity)mContext).OnNotice(R.string.login_error_safe_code);
					//显示安全验证dialog
					showVerifyDialog();
					break;
				case LoginUtils.TYPE_LOGIN_FAILED:
					mLoginResultListener.onFailed();
					break;
				case LoginUtils.TYPE_LOGIN_UNKNOW:
					((BaseActivity)mContext).OnNotice(R.string.common_error_unknow);
					break;
				}
				mLoginResultListener.onFinish();
				super.onPostExecute(result);
			}

			/* 用户取消登录 */
			@Override
			protected void onCancelled() {
				dialog.dismiss();
				super.onCancelled();
			}
			
		}.execute();
		
	}
	
	private void showVerifyDialog() {
		final VeriftyDialog dialog = new VeriftyDialog(mContext, verifyData[VERIGY_IMGURL]);
		dialog.setOnActionListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String code = dialog.getCode();
				if(code==null || code.equals("")) {
					((BaseActivity)mContext).OnNotice(R.string.common_error_input);
					return ;
				}
				onLogin(code);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	private int dologin() {
		int mRet = TYPE_LOGIN_UNKNOW;
		
		tmp = HttpLib.connect("http://pt.3g.qq.com/", new String[] {"aid", "nLogin"});
		
		if(tmp!=null && !tmp.equals("")) {
			try{
				//初始化登陆数据
				initSid = extractor.getInitsid();
				initVdata = extractor.getvdata();
				if( initSid == null || initVdata == null ) {
					return mRet;
				}
				//登陆
				tmp = null;
				tmp = HttpLib.connect("http://pt.3g.qq.com/handleLogin?vdata=" + initVdata,
						new String[] {"sid", initSid, 
						"qq", user,
						"pwd", pwd,
						"sidtype", "1",
						"q_from", "",
						"bid", "0",
						"modifySKey", "0",
						"loginType", "3",
						"aid", "nLoginHandle",
						"login_url", "http://pt.3g.qq.com/s?aid=nLogin"});
				mRet = getType();
			}catch(IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
			
		}
		tmp = null;
		initSid = null;
		initVdata = null;
		return mRet;
	}
	
	private int doverify(String code) {
		int mRet = TYPE_LOGIN_UNKNOW;
		
		tmp = null;
		tmp = HttpLib.connect("http://pt.3g.qq.com/handleLogin?sid=" + initSid +
				"&vdata=" + initVdata,
				new String[] {
				"qq", user,
				"u_token", user,
				"hexpwd", verifyData[VERIGY_HIDEPW],
				"sid", initSid,
				"hexp", "true",
				"auto", "0",
				"loginTitle", "手机腾讯网",
				"q_from", "",
				"modifySKey", "0",
				"q_status", "20",
				"r", verifyData[VERIGY_RVALUE],
				"loginType", "3",
				"bid_code", "qqchatLogin",
				"imgType", "gif",
				"extend", verifyData[VERIGY_IMGURL].substring(0, verifyData[VERIGY_IMGURL].length()-4),
				"r_sid", verifyData[VERIGY_RANDOM],
				"bid", "0",
				"login_url", "http://pt.3g.qq.com/s?aid=nLogin",
				"rip", verifyData[VERIGY_USERIP],
				"verify", code});
		try{ mRet = getType(); }catch(IndexOutOfBoundsException e){e.printStackTrace();};
		
		tmp = null;
		initSid = null;
		initVdata = null;
		return mRet;
	}
	 
	private int getType() {
		int mRet = TYPE_LOGIN_UNKNOW;
		if(tmp != null && !tmp.equals("")) {
			// 1.登录成功
			if(tmp.indexOf(STR_LOGIN_SUCCESS) != -1) {
				RealSid = extractor.getRealsid();
				mRet = TYPE_LOGIN_SUCCESS;
				
			// 2.需要进行安全验证
			}else if(tmp.indexOf(STR_LOGIN_NEED_SAFE_CODE) != -1) {
				if(verifyData == null) {
					//VERIGY_USERIP + 1 = 5，指定验证数据数量，使用该方式赋值方便后续增添新验证数据
					verifyData = new String[VERIGY_USERIP + 1];
					verifyData[VERIGY_IMGURL] = extractor.getimageURL();
					verifyData[VERIGY_RVALUE] = extractor.getRvalue();
					verifyData[VERIGY_HIDEPW] = extractor.getHidepw();
					verifyData[VERIGY_RANDOM] = extractor.getRandom();
					verifyData[VERIGY_USERIP] = extractor.getAddress();
				}
				mRet = TYPE_LOGIN_VERIFY;
			// 3.账号密码有误
			}else if(tmp.indexOf(STR_LOGIN_USERNAME_NULL) != -1||
					tmp.indexOf(STR_LOGIN_PASSWORD_ERROR1)!= -1||
					tmp.indexOf(STR_LOGIN_PASSWORD_ERROR2)!= -1||
					tmp.indexOf(STR_LOGIN_FAILED)!= -1) {
				mRet = TYPE_LOGIN_FAILED;
			}
		}
		return mRet;
	}
	
	
	class Extractor  {
		private int offset = 0, eoffset = 0;
		
		public final String getInitsid() {
			offset = tmp.indexOf("sid\" value=\"") + 12;
			return tmp.substring(offset, offset + 24);
		}
		
		public final String getvdata() {
			offset = tmp.indexOf("vdata=") + 6;
			return tmp.substring(offset, offset + 32);
		}
		
		public final String getRealsid() {
			offset  = tmp.indexOf("sid=") + 4;
			return tmp.substring(offset, offset + 24);
		}
		
		public final String getimageURL() {
			offset = tmp.indexOf("<img src=\"") + 10;
			eoffset = tmp.indexOf("?r=");
			return tmp.substring(offset, eoffset);
		}
		
		public final String getRvalue() {
			offset = tmp.indexOf("?r=") + 3;
			eoffset = tmp.indexOf("\" alt=");
			return tmp.substring(offset, eoffset);
		}
		
		public final String getHidepw() {
			offset = tmp.indexOf("\"hexpwd\" value=\"") + 16;
			eoffset = tmp.indexOf("<postfield name=\"sidtype\"") - 4;
			return tmp.substring(offset, eoffset);
		}
		
		public final String getRandom() {
			offset = tmp.indexOf("\"r_sid\" value=\"") + 15;
			eoffset = tmp.indexOf("<postfield name=\"bid\"") - 4;
			return tmp.substring(offset, eoffset);
		}
		
		public final String getAddress() {
			offset = tmp.indexOf("\"rip\" value=\"") + 13;
			eoffset = tmp.indexOf("</go>") - 5;
			return tmp.substring(offset, eoffset);
		}
	}
	
}