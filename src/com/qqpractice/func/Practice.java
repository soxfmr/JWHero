package com.qqpractice.func;

import com.qqpractice.executer.Executer;
import com.qqpractice.executer.PracticeExec;
import com.qqpractice.network.HttpLib;
import com.qqpractice.network.HttpUrl;
import com.qqpractice.user.PracticeInfo;

public class Practice extends Controller {
	private static final int SID = 1;
	private static String[] DATA_INDEX = new String[]{"sid", null, "g_f", "1661"};
	private static String[] DATA_HOUSE = new String[]{"sid", null };
	private static String[] DATA_PALACE = new String[]{"sid", null, "palace", "1"};
	private static String[] DATA_UPDATE =  new String[]{"sid", null, "action", "lvlup" };
	
	private static String[] DATA_START = null;
	
	private PracticeExec mPe = null;
	
	public Practice(String sid) {
		//初始化对象内的sid
		DATA_INDEX[SID] = sid;
		DATA_HOUSE[SID] = sid;
		DATA_PALACE[SID] = sid;
		DATA_UPDATE[SID] = sid;
		
		mPe = new PracticeExec(sid);
	}
	
	@Override
	public void addExecuter(Executer exec) {
		mPe = (PracticeExec) exec;
	}
	
	public void setPlace(int place) {
		DATA_START = place == PracticeInfo.PRACTICE_PLACE_HOUSE ? DATA_HOUSE : DATA_PALACE;
	}
	
	public PracticeInfo getPracticeInfo() {
		PracticeInfo pi = null;
		if(request(HttpUrl.PRACTICE_INDEX, DATA_INDEX, null)) {
			pi = mPe.getPracticeInfo();
		}
		return pi;
	}
	
	public boolean onStart() {
		return request(HttpUrl.PRACTICE_START, DATA_START, "已");
	}
	
	public boolean onDone() {
		return request(HttpUrl.PRACTICE_FINISH, DATA_START, "已经修炼");
	}
	
	public boolean onCure() {
		return request(HttpUrl.PRACTICE_CURE, DATA_HOUSE, "恢复正常");
	}
	
	public boolean onUpdate() {
		return request(HttpUrl.PRACTICE_UPDATE, DATA_UPDATE, "成功升到");
	}
	
	public boolean isLowRank(int rank) {
		return rank < PracticeInfo.PRACTICE_RANK_LIMIT_PALACE;
	}
	
	public boolean isLowMiniPower(int power) {
		return power < PracticeInfo.PRACTICE_POWER_LIMIT_HOUSE;
	}
	
	public boolean isLowPower(int power) {
		return power < PracticeInfo.PRACTICE_POWER_LIMIT_PALACE;
	}

	private boolean request(String url, String[] data, String keyword) {
		mPe.requestData(HttpLib.connect(url, data));
		if(mPe.check(keyword)) {
			return true;
		}
		return false;
	}
	
}
