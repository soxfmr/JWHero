package com.qqpractice.core;

import com.qqpractice.user.PracticeInfo;

import android.content.Context;
import android.content.SharedPreferences;

public class RestoreUtils {
	private static final String FN_RESTORE = "restore";
	
	private static final String FIELD_USER = "r_user";
	private static final String FIELD_SID = "r_sid";
	private static final String FIELD_PLACE = "r_place";
	private static final String FIELD_TIME_UPPER = "r_time_upper";
	
	private SharedPreferences sp;
	private SharedPreferences.Editor editor;
	
	public RestoreUtils(Context context) {
		sp = context.getSharedPreferences(FN_RESTORE, Context.MODE_PRIVATE);
		editor = sp.edit();
	}
	
	public void save(String user, String sid) {
		editor.putString(FIELD_USER, user);
		editor.putString(FIELD_SID, sid);
		editor.commit();
	}
	
	public void setTimeUpper(int upper) {
		editor.putInt(FIELD_TIME_UPPER, upper);
		editor.commit();
	}
	
	public int getTimeUpper() {
		return sp.getInt(FIELD_TIME_UPPER, PracticeInfo.PRACTICE_PERIOD_PALACE_NORMAL);
	}
	
	
	public void setPlace(int place) {
		editor.putInt(FIELD_PLACE, place);
		editor.commit();
	}
	
	public int getPlace() {
		return sp.getInt(FIELD_PLACE, PracticeInfo.PRACTICE_PLACE_SMART);
	}
	
	public void setUser(String user) {
		editor.putString(FIELD_USER, user);
		editor.commit();
	}
	
	public String getUser() {
		return sp.getString(FIELD_USER, null);
	}
	
	public void setSid(String sid) {
		editor.putString(FIELD_SID, sid);
		editor.commit();
	}
	
	public String getSid() {
		return sp.getString(FIELD_SID, null);
	}
	
}
