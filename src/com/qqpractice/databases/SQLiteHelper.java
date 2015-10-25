package com.qqpractice.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.qqpractice.core.GlobalUtils;

public class SQLiteHelper extends SQLiteOpenHelper {
	/** Tables list */
	public static final String TB_USERINFO = "userinfo";
	public static final String TB_FINAL_USER = "final";
	public static final String TB_GLOBAL_SETTINGS = "globalSetting";
	public static final String TB_FEEDBACK_MESSAGE = "feedbackMessage";
	/** Index keyword of all tables */
	public static final String INDEX = "_id";
	/** Table rows list */
	//TB_USERINFO
	public static final String TB_USERINFO_USERNAME = "username";
	public static final String TB_USERINFO_PASSWORD = "password";
	public static final String TB_USERINFO_NICKNAME = "nickname";
	public static final String TB_USERINFO_HEADICON = "headicon";
	public static final String TB_USERINFO_PRACTICE_PLACE = "practice_place";
	public static final String TB_USERINFO_PRACTICE_PERIOD_HOUSE = "period_house";
	public static final String TB_USERINFO_PRACTICE_PERIOD_PALACE = "period_palace";
	public static final String TB_USERINFO_FIGHT_COUNT = "fight_count";
	public static final String TB_USERINFO_QUENCH_PLACE = "quench_place";
	public static final String TB_USERINFO_QUENCH_COUNT = "quench_count";
	public static final String TB_USERINFO_MEMBER_SWITCH = "member";
	public static final String TB_USERINFO_MEMBER_HEADER = "member_header";
	public static final String TB_USERINFO_MEMBER_FIRE = "member_fire";
	public static final String TB_USERINFO_MEMBER_TREE = "member_tree";
	public static final String TB_USERINFO_MEMBER_WATER = "member_water";
	public static final String TB_USERINFO_AUTOUPDATE_RANK = "autoupd_rank";
	public static final String TB_USERINFO_DATE_SPEND = "date_spend";
	//Extra
	public static final String TB_USERINFO_SID = "sid";
	//TB_FINAL_USER
	public static final String TB_FINAL_USER_USERNAME = "user";
	//TB_GLOBAL_SETTINGS
	public static final String TB_GLOBAL_SETTINGS_NIGHT_SWITCH = "night";
	public static final String TB_GLOBAL_SETTINGS_AUTOUPDATE = "autoupd";
	public static final String TB_GLOBAL_SETTINGS_SHOW_STATUSBAR_ICON = "showico";
	//TB_FEEDBACK_CHAT
	public static final String TB_FEEDBACK_MESSAGE_WHO = "who";
	public static final String TB_FEEDBAKC_MESSAGE_TIME = "time"; 
	public static final String TB_FEEDBACK_MESSAGE_CONTENT = "content";
	/** Keywords index */
	//TB_USERINFO
	public static final int INDEX_USERINFO_USERNAME = 1;
	public static final int INDEX_USERINFO_PASSWORD = 2;
	public static final int INDEX_USERINFO_NICKNAME = 3;
	public static final int INDEX_USERINFO_HEADICON = 4;
	public static final int INDEX_USERINFO_PRACTICE_PLACE = 5;
	public static final int INDEX_USERINFO_PRACTICE_PERIOD_HOUSE = 6;
	public static final int INDEX_USERINFO_PRACTICE_PERIOD_PALACE = 7;
	public static final int INDEX_USERINFO_FIGHT_COUNT = 8;
	public static final int INDEX_USERINFO_QUENCH_PLACE = 9;
	public static final int INDEX_USERINFO_QUENCH_COUNT = 10;
	public static final int INDEX_USERINFO_MEMBER_SWITCH = 11;
	public static final int INDEX_USERINFO_MEMBER_HEADER = 12;
	public static final int INDEX_USERINFO_MEMBER_FIRE = 13;
	public static final int INDEX_USERINFO_MEMBER_TREE = 14;
	public static final int INDEX_USERINFO_MEMBER_WATER = 15;
	public static final int INDEX_USERINFO_AUTO_UPDATE_RANK = 16;
	public static final int INDEX_USERINFO_DATE_SPEND = 17;
	//TB_FINAL_USER
	public static final int INDEX_FINAL_USER_USERNAME = 1;
	//TB_GLOBAL_SETTINGS
	public static final int INDEX_GLOBAL_SETTINGS_NIGHT_SWITCH = 1;
	public static final int INDEX_GLOBAL_SETTINGS_AUTOUPDATE = 2;
	public static final int INDEX_GLOBAL_SETTINGS_SHOW_STATUSBAR_ICON = 3;
	
	public static final int INDEX_FEEDBACK_MESSAGE_WHO = 1;
	public static final int INDEX_FEEDBACK_MESSAGE_TIME = 2;
	public static final int INDEX_FEEDBACK_MESSAGE_CONTENT = 3;
	
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建数据表
		TableManager tbmgr = new TableManager(db);
		//tbmgr.onPreInsertData();
		//TB_USERINFO，用户信息表
		tbmgr.addrow(TB_USERINFO_USERNAME, TableManager.TYPE_VARCHAR);
		tbmgr.addrow(TB_USERINFO_PASSWORD, TableManager.TYPE_VARCHAR);
		tbmgr.addrow(TB_USERINFO_NICKNAME, TableManager.TYPE_VARCHAR);
		tbmgr.addrow(TB_USERINFO_HEADICON, "blob");
		tbmgr.addrow(TB_USERINFO_PRACTICE_PLACE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_PRACTICE_PERIOD_HOUSE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_PRACTICE_PERIOD_PALACE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_FIGHT_COUNT, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_QUENCH_PLACE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_QUENCH_COUNT, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_MEMBER_SWITCH, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_MEMBER_HEADER, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_MEMBER_FIRE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_MEMBER_TREE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_MEMBER_WATER, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_AUTOUPDATE_RANK, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_USERINFO_DATE_SPEND, TableManager.TYPE_VARCHAR);
		tbmgr.create(TB_USERINFO);
		//TB_FINAL_USER，记录最后有效登录用户
		tbmgr.addrow(TB_FINAL_USER_USERNAME, TableManager.TYPE_VARCHAR);
		tbmgr.create(TB_FINAL_USER);
		//TB_GLOBAL_SETTINGS，全局设置表
		tbmgr.addrow(TB_GLOBAL_SETTINGS_NIGHT_SWITCH, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_GLOBAL_SETTINGS_AUTOUPDATE, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_GLOBAL_SETTINGS_SHOW_STATUSBAR_ICON, TableManager.TYPE_INTEGER);
		tbmgr.create(TB_GLOBAL_SETTINGS);
		//TB_FEEDBACK_MESSAGE，用户同开发者交互信息表
		tbmgr.addrow(TB_FEEDBACK_MESSAGE_WHO, TableManager.TYPE_INTEGER);
		tbmgr.addrow(TB_FEEDBAKC_MESSAGE_TIME, TableManager.TYPE_VARCHAR);
		tbmgr.addrow(TB_FEEDBACK_MESSAGE_CONTENT, TableManager.TYPE_VARCHAR);
		tbmgr.create(TB_FEEDBACK_MESSAGE);
		
		//初始化数据
		tbmgr.addvalue(TB_FINAL_USER_USERNAME, DataMan.RECORD_NO_FOUND);
		tbmgr.insert(TB_FINAL_USER);
		
		//tbmgr.addrow(GLOBAL_SKIN_TYPE, type);
		tbmgr.addvalue(TB_GLOBAL_SETTINGS_NIGHT_SWITCH, GlobalUtils.NIGHT_SWITCH_CLOSE+"");
		tbmgr.addvalue(TB_GLOBAL_SETTINGS_AUTOUPDATE, "1");
		tbmgr.addvalue(TB_GLOBAL_SETTINGS_SHOW_STATUSBAR_ICON, "0");
		tbmgr.insert(TB_GLOBAL_SETTINGS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldver, int newver) {
		TableManager tbmgr = new TableManager(db);
		tbmgr.drop(TB_USERINFO);
		tbmgr.drop(TB_FINAL_USER);
		tbmgr.drop(TB_GLOBAL_SETTINGS);
		
		//Droping the table of the old version
		tbmgr.drop("tip");
		
		onCreate(db);
	}
	
	class TableManager {
		public static final String TYPE_VARCHAR = "varchar";
		public static final String TYPE_INTEGER = "integer";
		
		private SQLiteDatabase db = null;
		
		private StringBuilder sb = null;
		private StringBuilder sbvalue = null;
		
		public TableManager(SQLiteDatabase db) {
			this.db = db;
		}
		
		public void addrow(String key, String type) {
			if(sb == null) {
				sb = new StringBuilder();
				sb.append(key);
				sb.append(" ");
				sb.append(type);
			}else {
				sb.append(",");
				sb.append(key);
				sb.append(" ");
				sb.append(type);
			}
		}
		
		public void addvalue(String key, String value) {
			if(sb == null) {
				sb = new StringBuilder();
				sbvalue = new StringBuilder();
				//添加
				sb.append(key);
				sbvalue.append(") values(");
				sbvalue.append("'");
				sbvalue.append(value);
				sbvalue.append("'");
			}else {
				sb.append(",");
				sb.append(key);
				sbvalue.append(",");
				sbvalue.append("'");
				sbvalue.append(value);
				sbvalue.append("'");
			}
		}
		
		/**
		 * 初始化数据
		 * @param bclear 插入数据后是否清除数据存储对象
		 * */
		public void insert(String tablename) {
			StringBuilder sql = new StringBuilder();
			sql.append("insert into ");
			sql.append(tablename);
			sql.append(" (");
			
			sql.append(sb.toString());
			sql.append(sbvalue.toString());
			
			sql.append(")");
			
			Log.i("keyname.toString()", sql.toString());
			db.execSQL(sql.toString());
			
			sb = null;
			sbvalue = null;
		}
		
		/**
		 * 创建表
		 * */
		public void create(String tablename) {
			StringBuilder sql = new StringBuilder();
			sql.append("create table if not exists ");
			sql.append(tablename);
			sql.append(" (");
			sql.append(INDEX);
			sql.append(" integer primary key,");
			
			sql.append(sb.toString());
			
			sql.append(") ");
			
			Log.i("sql.toString()", sql.toString());
			db.execSQL(sql.toString());
			
			sb = null;
		}
		
		public void drop(String tablename) {
			db.execSQL("drop table if exists " + tablename);
		}
		
	}

}
