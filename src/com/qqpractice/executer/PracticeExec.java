package com.qqpractice.executer;

import com.qqpractice.core.ToolsUtils;
import com.qqpractice.user.PracticeInfo;
import com.qqpractice.user.UserIdentity;


public class PracticeExec extends Executer {
	private PracticeInfo mPi = null;

	public PracticeExec(String sid) {
		super(sid);
		mPi = new PracticeInfo();
	}
	
	public PracticeInfo getPracticeInfo() {
		String nickname = getNickName();
		mPi.setNickname(nickname);
		int place = getPlace();
		mPi.setPlace(place);
		int state = getState();
		mPi.setState(state);
		int rank = getUserRank();
		mPi.setRank(rank);
		int power = getfreePower();
		mPi.setPower(power);
		int[] exp = getExperience();
		mPi.setExpCurrent(exp[PracticeInfo.PRACTICE_EXPERIENCE_CURRENT]);
		mPi.setExpTotal(exp[PracticeInfo.PRACTICE_EXPERIENCE_TOTAL]);
		int[] remainTime = getRemainTime(place);
		mPi.setRemainHour(remainTime[PracticeInfo.PRACTICE_REMAIN_TIME_HOUR]);
		mPi.setRemainMin(remainTime[PracticeInfo.PRACTICE_REMAIN_TIME_MINUTE]);
		return mPi;
	}

	/** 获取修炼状态 */
	public int getState() {
		if(mData.indexOf("开始修炼:")!=-1) {
			return PracticeInfo.PRACTICE_STATE_IDLE;
		}else if(mData.indexOf("停止</a>")!=-1) {
			return PracticeInfo.PRACTICE_STATE_PRACTICING;
		}else if(mData.indexOf("疗伤</a>")!=-1) {
			return PracticeInfo.PRACTICE_STATE_HURT;
		}
		return ToolsUtils.GENERIC_UNKNOW;
		
	}
	
	/** 获取昵称 **/
	public String getNickName() {
		int offset = 0;
		/*offset = mData.indexOf(Tools.exchange(new char[]{'\u6b22', '\u8fce', '\u60a8'}) + ",<a href=\"/myInfo.jsp?sid="+sid+"\">");
		//假如有头衔称号
		if(offset==-1) {
			offset = mData.indexOf("</a>,<a href=\"/myInfo.jsp?sid="+sid+"\">") + 56;
			Tools.mvip = true;
			
		}else {
			offset += 55;
			Tools.mvip = false;
		}*/
		offset = mData.indexOf(",<a href=\"/myInfo.jsp?sid=" + sid + "\">") + 52;
		//假如有头衔称号
		if(mData.indexOf("欢迎您,<a href=\"/myInfo.jsp?sid="+sid+"\">")==-1) {
			String ahead = "/vip/index.jsp?sid=" + sid + "\">";
			if(mData.indexOf(ahead + "宗师</a>")!=-1||
				mData.indexOf(ahead + "霸者/a>")!=-1) {
				UserIdentity.SIMPLE_VIP = true;
				UserIdentity.SUPER_VIP = false;
			}
			if(mData.indexOf(ahead + "圣者</a>")!=-1||
				mData.indexOf(ahead + "武尊</a>")!=-1||
				mData.indexOf(ahead + "武王</a>")!=-1) {
				UserIdentity.SUPER_VIP = true;
			}
		}else {
			UserIdentity.SIMPLE_VIP = false;
			UserIdentity.SUPER_VIP = false;
		}
		
		int eoffset = mData.indexOf("</a>(");
		return mData.substring(offset, eoffset);
	}
	
	/** 获取等级  **/
	public int getUserRank() {
		int offset = mData.indexOf("</a>(") + 5;
		int eoffset = mData.indexOf("级)<");
		
		return Integer.parseInt(mData.substring(offset, eoffset));
	}
	
	/** 获取当前体力 **/
	public int getfreePower() {		
		int offset = mData.indexOf("体力:") + 3;
		int eoffset = 0;
		String tmp = null;
		if(mData.indexOf(sid+"\">提升</a>")!=-1) {
			eoffset = mData.indexOf(".3g.qq.com/vip/upVit.jsp?sid="+sid) - 21;
		}else {
			eoffset = mData.indexOf("<br></br><br></br>【");
		}
		
		tmp = mData.substring(offset, eoffset);
		return Integer.parseInt(tmp.substring(0, tmp.indexOf("/")));
	}
	
	/** 获取当前修炼地点 */
	public int getPlace() {
		if(mData.indexOf("战神宫</a>:")!=-1) {
			return PracticeInfo.PRACTICE_PLACE_PALACE;
		}else {
			return PracticeInfo.PRACTICE_PLACE_HOUSE;
		}
		
	}
	
	public int[] getExperience() {
		int[] Ret = new int[]{0, 0};
		//"<br>经验:".length == 7
		int offset = mData.indexOf("</br>经验:") + 8;
		int eoffset = mData.indexOf("&#160;<br></br>体力:");
		String container = null;
		if(eoffset == -1) {
			//如果有“可升级”会阻碍字符查找，先截断字符再获取
			String tmp = mData.substring(offset);
			eoffset = tmp.indexOf("&#160;");
			container = tmp.substring(0, eoffset);
			tmp = null;
		}else {
			container = mData.substring(offset, eoffset);
		}
		//获取经验进行分割，如“2100/3000”
		int slip = container.indexOf("/");
		Ret[PracticeInfo.PRACTICE_EXPERIENCE_CURRENT] = Integer.valueOf(container.substring(0, slip));
		Ret[PracticeInfo.PRACTICE_EXPERIENCE_TOTAL] = Integer.valueOf(container.substring(++slip));
		return Ret;
	}
	
	/** 获取修炼时间 **/
	public int[] getRemainTime(int place) {
		int[] ret = { 0, 0 };
		int offset = 0;
		if(place == PracticeInfo.PRACTICE_PLACE_PALACE) {
			offset = mData.indexOf("战神宫</a>:") + 12;
		}else {
			offset = mData.indexOf("练功房</a>:") + 13;
		}
		
		int indexmin = mData.indexOf("分钟)&#160;");
		int indexhour = mData.indexOf("小时)&#160;");
		//如果 "分钟" 存在
		if(indexmin!=-1) {
			String tmp = mData.substring(offset, indexmin);
			//如果 "小时" 存在
			if(tmp.indexOf("小时")!=-1) {
				  ret[0] = Integer.parseInt(tmp.substring(0, tmp.indexOf("小时")));
				  ret[1] = Integer.parseInt(tmp.substring(tmp.indexOf("小时")+2, tmp.length()));
			}else {
				//只提取分钟
				ret[1] = Integer.parseInt(tmp);
			}
		}else {
			//只提取小时
			ret[0] = Integer.parseInt(mData.substring(offset, indexhour));
		}
		
		return ret;
	}
}
