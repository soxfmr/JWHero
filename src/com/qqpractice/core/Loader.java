package com.qqpractice.core;

import android.content.Context;
import android.os.Bundle;

import com.qqpractice.activity.LoginActivity;
import com.qqpractice.activity.SplashActivity;
import com.qqpractice.databases.DataMan;
import com.qqpractice.databases.SQLiteHelper;
import com.qqpractice.ui.bases.BaseActivity;
import com.qqpractice.user.Userinfo;

public class Loader {
	private Context context = null;
	private Bundle bundle = null;
	
	private String user, passwd;
	private String sid;
	private boolean loadLater = false;
	
	private SessionManager mSessionMgr = null;
	private Userinfo mUserinfo = null;
	private DataMan dataMan = null;
	
	public Loader(Context context, Bundle bundle) {
		this.context = context;
		this.bundle = bundle;
		
		mSessionMgr = new SessionManager(context);
		dataMan = new DataMan(context);
	}
	
	public boolean isLoadLater() {
		return loadLater;
	}
	
	public void load() {
		if(bundle != null) {
			user = bundle.getString(SQLiteHelper.TB_USERINFO_USERNAME);
			passwd = bundle.getString(SQLiteHelper.TB_USERINFO_PASSWORD);
			sid = bundle.getString(SQLiteHelper.TB_USERINFO_SID);
			// 用户数据库信息
			mUserinfo = dataMan.get(user);
			// 登录来源标识
			int flag = bundle.getInt(BaseActivity.ACTIVITY_TAG);
			
			switch(flag) {
			case SplashActivity.FLAG: onFromSplash();
				break;
			case LoginActivity.FLAG:  onFromLogin();
				break;
			default: ;
			}
		}else {
			onFromBackground();
		}
	}
	
	public void loadSettings() {
		GlobalSettings settings = null;
		GlobalUtils globalUtils = null;
		if( (globalUtils = dataMan.getGlobalSetting()) != null ) {
			settings = new GlobalSettings();
			//加载皮肤
			int nightSwitch = globalUtils.getNightSwitch();
			settings.skin(nightSwitch);
			//状态栏图标
			boolean bshow = globalUtils.isShowBarIcon();
			settings.showStatusBarIcon(bshow);
			//自动升级检测
			boolean bUpdate = globalUtils.isAutoUpdate();
			settings.update(bUpdate);
		}
	}
	
	/**
	 * 通过用户名获取用户信息，如果用户信息为null返回false<br />
	 * 登录密码解码错误时返回false
	 * */
	private boolean onFromSplash() {
		boolean bRet = false;
		
		if(mUserinfo != null) {
			passwd = mUserinfo.getPassword();
			if( passwd != null && !passwd.equals("") ) {
				// 登陆
				mSessionMgr.onLogin(user, passwd);
				bRet = true;
			}
		
		}
		
		return bRet;
	}
	
	/**
	 * 从登录界面获取用户帐号密码和sid，结果肯定为true
	 * */
	private void onFromLogin() {
		if(mUserinfo != null) {
			// 更新密码
			if(!mUserinfo.getPassword().equals(passwd)) {
				mUserinfo.setPassword(user);
				dataMan.updatePassword(user, passwd);
			}
		}else {
			mUserinfo = new Userinfo();
			mUserinfo.init(user, passwd);
			// 添加用户
			dataMan.add(mUserinfo);
		}
		dataMan.setFinalusr(user);
		
		mUserinfo.setSid(sid);
	}
	
	/**
	 * 后台数据恢复，读取用户名和sid<br />
	 * 如果用户名为null，提示会话过期并返回登录界面<br />
	 * sid为null，进行登录，否则直接加载修炼模块
	 * */
	private boolean onFromBackground() {
		boolean bRet = false;
		
		// 恢复工作
		RestoreUtils Restore = new RestoreUtils(context);
		user = Restore.getUser();
		sid = Restore.getSid();
		
		if(user != null && !user.equals("")) {
			mUserinfo = dataMan.get(user);
			if(mUserinfo != null) {
				passwd = mUserinfo.getPassword();
				// sid 不存在则自动登录
				if(sid == null || sid.equals("")) {
					// 登陆
					mSessionMgr.onLogin(user, passwd);
				}else {
					mUserinfo.setSid(sid);
				}
				bRet = true;
			}
		}else {
			// 登录信息过期
			mSessionMgr.onTimeout();
		}
		
		return bRet;
	}

}
