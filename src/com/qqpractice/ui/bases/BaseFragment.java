package com.qqpractice.ui.bases;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
	
	public abstract void initView(LayoutInflater inflater, ViewGroup container);
	
	public abstract void onRefresh();
	
}