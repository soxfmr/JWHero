package com.qqpractice.core;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.qqpractice.activity.LoginActivity;
import com.qqpractice.activity.R;
import com.qqpractice.databases.SQLiteHelper;
import com.qqpractice.user.Userinfo;

public class SessionManager implements LoginUtils.OnLoginResultListener {
	private Context context;
	private LoginUtils loginUtils = null;
	
	public SessionManager(Context context) {
		this.context = context;
		
		loginUtils = new LoginUtils(context);
		loginUtils.setOnLoginResultListener(this);
		
	}
	
	public void addSession(Userinfo ui) {
		if(ui == null) return;
		RestoreUtils restore = new RestoreUtils(context);
		restore.save(ui.getUsername(), ui.getSid());
 	}
	
	public void onLogin(String user, String passwd) {
		loginUtils.onLogin(user, passwd);
	}
	
	public void onLogout(String user, String passwd) {
		Bundle bundle = new Bundle();
		bundle.putString(SQLiteHelper.TB_USERINFO_USERNAME, user);
		bundle.putString(SQLiteHelper.TB_USERINFO_PASSWORD, passwd);
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
	
	public void onTimeout() {
		new AlertDialog.Builder(context)
		.setMessage(R.string.session_timeout) // 会话信息过期，请重新登录
		.setNegativeButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface di, int id) {
				// 会话过期没有任何用户信息，直接返回
				Intent intent = new Intent(context, LoginActivity.class);
				context.startActivity(intent);
				
			}
		})
		.setCancelable(false)
		.show();
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}
}