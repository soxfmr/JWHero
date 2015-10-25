package com.qqpractice.databases;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.qqpractice.core.DecImageUtils;
import com.qqpractice.core.GlobalUtils;
import com.qqpractice.core.SecurityUtils;
import com.qqpractice.user.UserList;
import com.qqpractice.user.Userinfo;

/**
 * 数据库管理
 * @version 2014-06-18 16:55
 * 
 * */

public class DataMan {
	public static final String RECORD_NO_FOUND = "nofound";
	
	private static final String DB_NAME = "_userinfo";
	/**数据库版本控制*/
	private static final int DB_VERSION = 8;
	
	private SQLiteHelper sqlhelper;
	private SQLiteDatabase db;
	
	public DataMan(Context context) {
		sqlhelper = new SQLiteHelper(context, DB_NAME, null, DB_VERSION);
		db = sqlhelper.getWritableDatabase();
		
	}
	
	public void close() {
		sqlhelper.close();
		db.close();
	}
	
	/************************************获取数据***************************************/
	
	/**
	 * 获取指定用户信息
	 * */
	public Userinfo get(String username) {
		Userinfo ui = null;
		Cursor cursor = null;
		try{
			cursor = db.query(SQLiteHelper.TB_USERINFO, null, SQLiteHelper.TB_USERINFO_USERNAME + "=?", 
					new String[]{username}, null, null, null);
			if(cursor.moveToFirst()) {
				ui = new Userinfo();
				//恢复密码
				String depwd = SecurityUtils.decoding(cursor.getString(SQLiteHelper.INDEX_USERINFO_PASSWORD));
				
				ui.setUsername(cursor.getString(SQLiteHelper.INDEX_USERINFO_USERNAME));
				ui.setPassword(depwd);
				ui.setNickname(cursor.getString(SQLiteHelper.INDEX_USERINFO_NICKNAME));
				
				ui.setPlace(cursor.getInt(SQLiteHelper.INDEX_USERINFO_PRACTICE_PLACE));
				ui.setPerHouse(cursor.getInt(SQLiteHelper.INDEX_USERINFO_PRACTICE_PERIOD_HOUSE));
				ui.setPerPalace(cursor.getInt(SQLiteHelper.INDEX_USERINFO_PRACTICE_PERIOD_PALACE));
				ui.setCountFight(cursor.getInt(SQLiteHelper.INDEX_USERINFO_FIGHT_COUNT));
				ui.setPlaceQuench(cursor.getInt(SQLiteHelper.INDEX_USERINFO_QUENCH_PLACE));
				ui.setCountQuench(cursor.getInt(SQLiteHelper.INDEX_USERINFO_QUENCH_COUNT));
				
				ui.setMemSwitch(inttoBool(cursor.getInt(SQLiteHelper.INDEX_USERINFO_MEMBER_SWITCH)));
				ui.setMemHeader(inttoBool(cursor.getInt(SQLiteHelper.INDEX_USERINFO_MEMBER_HEADER)));
				ui.setMemFire(inttoBool(cursor.getInt(SQLiteHelper.INDEX_USERINFO_MEMBER_FIRE)));
				ui.setMemTree(inttoBool(cursor.getInt(SQLiteHelper.INDEX_USERINFO_MEMBER_TREE)));
				ui.setMemWater(inttoBool(cursor.getInt(SQLiteHelper.INDEX_USERINFO_MEMBER_WATER)));
				
				ui.setAutoUpdRank(inttoBool(cursor.getInt(SQLiteHelper.INDEX_USERINFO_AUTO_UPDATE_RANK)));
				
				ui.setDateSpend(cursor.getString(SQLiteHelper.INDEX_USERINFO_DATE_SPEND));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;
			}
		}	
		return ui;
	}
	
	/**
	 * 从数据库移除指定用户
	 * */
	public void remove(String username) {
		db.delete(SQLiteHelper.TB_USERINFO, SQLiteHelper.TB_USERINFO_USERNAME + "=?", new String[]{username});
	}
	
	/**
	 * 获取登陆用户列表
	 * */
	public List<UserList> getUserList() {
		List<UserList> users = null;
		Cursor cursor = null;
		DecImageUtils decImage = null;
		Bitmap bitmap = null;
		try{
			cursor = db.query(SQLiteHelper.TB_USERINFO, null, null, null, null, null, null);
			final int count = cursor.getCount();
			if(count>=1) {
				decImage = new DecImageUtils();
				users = new ArrayList<UserList>();
				cursor.moveToFirst();
				while(!cursor.isAfterLast()) {
					UserList user = new UserList();
					user.setUsername(cursor.getString(SQLiteHelper.INDEX_USERINFO_USERNAME));
					user.setNickname(cursor.getString(SQLiteHelper.INDEX_USERINFO_NICKNAME));
					user.setPassword(SecurityUtils.decoding(
							cursor.getString(SQLiteHelper.INDEX_USERINFO_PASSWORD)));
					//获取头像
					bitmap = decImage.getImage(cursor.getBlob(SQLiteHelper.INDEX_USERINFO_HEADICON));
					if(bitmap != null) {
						user.setHeadicon(bitmap);
						bitmap.recycle();
						bitmap = null;
					}
					//添加用户
					users.add(user);
					user = null;
					cursor.moveToNext();
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;
			}			
		}	
		return users;
	}
	
	public String[] getAllAccount() {
		String accounts[] = null;
		
		Cursor cursor = null;
		try{
			cursor = db.query(SQLiteHelper.TB_USERINFO, null, null, null, null, null, null);
			final int count = cursor.getCount();
			if(count>=1) {
				accounts = new String[count];
				cursor.moveToFirst();
				for(int i=0;!cursor.isAfterLast()&&!(cursor.getString(1)==null);i++) {
					accounts[i] = cursor.getString(SQLiteHelper.INDEX_USERINFO_USERNAME);
					cursor.moveToNext();
				}
				
			}
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;	
			}
		}
		
		return accounts;
	}

	/**
	 * 获取用户密码
	 * */
	public String getpwd(String username) {
		String ret = null;
		Cursor cursor = null;
		
		try{
			cursor = db.query(SQLiteHelper.TB_USERINFO, null, SQLiteHelper.TB_USERINFO_USERNAME + "=?", 
					new String[]{username}, null, null, null);
			if(cursor.moveToFirst()) {
				//恢复密码
				ret = SecurityUtils.decoding(cursor.getString(SQLiteHelper.INDEX_USERINFO_PASSWORD));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;
			}
		}
		return ret;
	}
	
	/**
	 * 获取用户密码
	 * */
	public Bitmap getUserIcon(String username) {
		Bitmap ret = null;
		Cursor cursor = null;
		DecImageUtils decImage = null;
		
		try{
			cursor = db.query(SQLiteHelper.TB_USERINFO, null, SQLiteHelper.TB_USERINFO_USERNAME + "=?", 
					new String[]{username}, null, null, null);
			if(cursor.moveToFirst()) {
				decImage = new DecImageUtils();
				ret = decImage.getImage(cursor.getBlob(SQLiteHelper.INDEX_USERINFO_HEADICON));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;
			}
		}
		return ret;
	}
	
	/**
	 * 获取最后登陆用户
	 * @return 成功返回用户名，否则为<b>RECORD_NO_FOUND</b>
	 * */
	public String getFinaluser() {
		String ret = RECORD_NO_FOUND;
		Cursor cursor = null;
		
		try{
			cursor = db.query(SQLiteHelper.TB_FINAL_USER, null, SQLiteHelper.INDEX + "=?",
					new String[]{"1"}, null, null, null);
			if(cursor.moveToFirst()) {
				ret = cursor.getString(SQLiteHelper.INDEX_FINAL_USER_USERNAME);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;	
			}
		}
		return ret;
	}
	
	/**
	 * 获取全局设置表
	 * */
	public GlobalUtils getGlobalSetting() {
		GlobalUtils gbUtils = null;
		Cursor cursor = null;
		
		try{
			cursor = db.query(SQLiteHelper.TB_GLOBAL_SETTINGS, null, SQLiteHelper.INDEX + "=?",
					new String[]{"1"}, null, null, null);
			if(cursor.moveToFirst()) {
				gbUtils = new GlobalUtils();
				gbUtils.setNightSwitch(cursor.getInt(SQLiteHelper.INDEX_GLOBAL_SETTINGS_NIGHT_SWITCH));
				gbUtils.setAutoUpdate( inttoBool(cursor.getInt(SQLiteHelper.INDEX_GLOBAL_SETTINGS_AUTOUPDATE)) );
				gbUtils.setShowBarIcon( inttoBool(cursor.getInt(SQLiteHelper.INDEX_GLOBAL_SETTINGS_SHOW_STATUSBAR_ICON)) );
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally {
			if(cursor!=null) {
				cursor.close();
				cursor = null;	
			}
		}
		return gbUtils;
	}
	
	/*************************************End 获取数据**********************************************/
	
	/**********************************Begin 写入数据*****************************************/
	
	/**
	 * 添加新用户
	 * */
	public void add(Userinfo userinfo) {
		db.insert(SQLiteHelper.TB_USERINFO, SQLiteHelper.INDEX, create(userinfo));
	}
	
	/**
	 * 更新用户信息
	 * */
	public void update(Userinfo userinfo) {
		db.update(SQLiteHelper.TB_USERINFO,  create(userinfo)  , SQLiteHelper.TB_USERINFO_USERNAME + "=?", 
				new String[]{userinfo.getUsername()});
	}
	
	/**
	 * 更新用户密码，一般用于重新登陆时输入的有效新密码
	 * */
	public void updatePassword(String username, String password) {
		ContentValues value = new ContentValues();
		value.put(SQLiteHelper.TB_USERINFO_PASSWORD, SecurityUtils.encoding(password));
		db.update(SQLiteHelper.TB_USERINFO,  value, SQLiteHelper.TB_USERINFO_USERNAME + "=?", 
				new String[]{username});
		
		value = null;
		password = null;
		username = null;
	}
	
	/**
	 * 设置用户头像
	 * */
	public void setheadIcon(String username, Bitmap bitmap) {
		ContentValues values = new ContentValues();
		//将图片储存到输出流
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, bos);
		//储存
		values.put(SQLiteHelper.TB_USERINFO_HEADICON, bos.toByteArray());
		db.update(SQLiteHelper.TB_USERINFO, values, SQLiteHelper.TB_USERINFO_USERNAME + "=?", new String[]{username});
		
		values = null;
		bitmap = null;
		username = null;
		try {
			bos.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	/**
	 * 设置最后登陆用户
	 * @param username 最后登陆用户，为空时设置为 <b>NULL</b>
	 * */
	public void setFinalusr(String username) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.TB_FINAL_USER_USERNAME, username == null? RECORD_NO_FOUND : username);
		db.update(SQLiteHelper.TB_FINAL_USER, values, SQLiteHelper.INDEX + "=?", new String[]{"1"});
		
		values = null;
		username = null;
	}
	
	/**
	 * 更新全局设置表
	 * */
	public void updateGlobalSetting(GlobalUtils gbUtils) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.TB_GLOBAL_SETTINGS_NIGHT_SWITCH, gbUtils.getNightSwitch());
		values.put(SQLiteHelper.TB_GLOBAL_SETTINGS_AUTOUPDATE,  booltoInt(gbUtils.isAutoUpdate()) );
		values.put(SQLiteHelper.TB_GLOBAL_SETTINGS_SHOW_STATUSBAR_ICON, booltoInt(gbUtils.isShowBarIcon()) );
		db.update(SQLiteHelper.TB_GLOBAL_SETTINGS, values, SQLiteHelper.INDEX + "=?", new String[]{"1"});
		
		values = null;
		gbUtils = null;
	}
	
	/************************************End 写入数据***************************************/
	
	/**
	 * 构建UserInfo对象
	 * @return 返回ContentValues对象，直接插入数据库
	 * */
	private ContentValues create(Userinfo userinfo) {
		ContentValues values = new ContentValues();
		//混淆密码
		String enpwd = SecurityUtils.encoding(userinfo.getPassword());
		
		values.put(SQLiteHelper.TB_USERINFO_USERNAME, userinfo.getUsername());
		values.put(SQLiteHelper.TB_USERINFO_PASSWORD, enpwd);
		values.put(SQLiteHelper.TB_USERINFO_NICKNAME, userinfo.getNickname());
		
		values.put(SQLiteHelper.TB_USERINFO_PRACTICE_PLACE, userinfo.getPlace());
		values.put(SQLiteHelper.TB_USERINFO_PRACTICE_PERIOD_HOUSE, userinfo.getPerHouse());
		values.put(SQLiteHelper.TB_USERINFO_PRACTICE_PERIOD_PALACE, userinfo.getPerHouse());
		values.put(SQLiteHelper.TB_USERINFO_FIGHT_COUNT, userinfo.getCountFight());
		values.put(SQLiteHelper.TB_USERINFO_QUENCH_PLACE, userinfo.getPlaceQuench());
		values.put(SQLiteHelper.TB_USERINFO_QUENCH_COUNT, userinfo.getCountQuench());
		
		values.put(SQLiteHelper.TB_USERINFO_MEMBER_SWITCH, booltoInt(userinfo.isMemSwitch()));
		values.put(SQLiteHelper.TB_USERINFO_MEMBER_HEADER, booltoInt(userinfo.isMemHeader()));
		values.put(SQLiteHelper.TB_USERINFO_MEMBER_FIRE, booltoInt(userinfo.isMemFire()));
		values.put(SQLiteHelper.TB_USERINFO_MEMBER_TREE, booltoInt(userinfo.isMemTree()));
		values.put(SQLiteHelper.TB_USERINFO_MEMBER_WATER, booltoInt(userinfo.isMemWater()));
		
		values.put(SQLiteHelper.TB_USERINFO_AUTOUPDATE_RANK, booltoInt(userinfo.isAutoUpdRank()));
		
		values.put(SQLiteHelper.TB_USERINFO_DATE_SPEND, userinfo.getDateSpend());
		
		userinfo = null;
		return values;
	}
	
	
	private boolean inttoBool(int i) {
		return i==1?true:false;
	}
	
	private int booltoInt(boolean b) {
		return b?1:0;
	}

}
