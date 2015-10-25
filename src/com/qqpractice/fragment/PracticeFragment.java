package com.qqpractice.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qqpractice.activity.R;
import com.qqpractice.receiver.BaseUiUpdateReceiver;
import com.qqpractice.ui.ExpProgressBar;
import com.qqpractice.ui.bases.BaseFragment;

public class PracticeFragment extends BaseFragment {
	private TextView tvUserRank, tvUserPower,
		tvExpCurrent, tvExpTotal;
	private ExpProgressBar expProgressbar;
	
	private UiUpdateReceiver uiReceiver = null;
	private IntentFilter mFilter = null;
	
	
	public PracticeFragment() {
		uiReceiver = new UiUpdateReceiver();
		mFilter = new IntentFilter(UiUpdateReceiver.ACTION_PRACTICE_UI_UPDATE);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActivity().registerReceiver(uiReceiver, mFilter);
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		initView(inflater, container);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(uiReceiver);
		super.onDestroyView();
	}
	
	@Override
	public void initView(LayoutInflater inflater, ViewGroup container) {
		View view = inflater.inflate(R.layout.fragment_practice, container, false);
		tvUserRank = (TextView) view.findViewById(R.id.practice_userinfo_basic_rank);
		tvUserPower = (TextView) view.findViewById(R.id.practice_userinfo_basic_power);
		tvExpCurrent = (TextView) view.findViewById(R.id.practice_userinfo_experience_current);
		tvExpTotal = (TextView) view.findViewById(R.id.practice_userinfo_experience_total);
		
		expProgressbar = (ExpProgressBar) view.findViewById(R.id.practice_userinfo_experience_progress);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	
	public class UiUpdateReceiver extends BaseUiUpdateReceiver {
		public static final int ACTION_UPDATE_RANK = 0x1;
		public static final int ACTION_UPDATE_POWER =  0x2;
		public static final int ACTION_UPDATE_EXPERIENCE = 0x3;

		public static final String KEY_EXP_CURRENT = "key_exp_current";
		public static final String KEY_EXP_TOTAL = "key_exp_total";
		
		public static final String ACTION_PRACTICE_UI_UPDATE = "com.qqpractice.fragment.ACTION_PRACTICE_UI_UPDATE";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			super.onReceive(context, intent);
			switch(action) {
			case ACTION_UPDATE_RANK:
				tvUserRank.setText(dispatchChars(KEY_GENERIC));
				break;
			case ACTION_UPDATE_POWER:
				tvUserPower.setText(dispatchChars(KEY_GENERIC));
				break;
			case ACTION_UPDATE_EXPERIENCE:
				int expCurrent = dispatchInt(KEY_EXP_CURRENT);
				int expTotal = dispatchInt(KEY_EXP_TOTAL);
				// 显示
				tvExpCurrent.setText(expCurrent+"");
				tvExpTotal.setText(expTotal+"");
				// 调整进度条
				expProgressbar.setCurrentExp(expCurrent);
				expProgressbar.setTotalExp(expTotal);
				break;
			default:;
			}
		}
		
	}

}
