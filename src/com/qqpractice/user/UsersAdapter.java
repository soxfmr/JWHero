package com.qqpractice.user;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.qqpractice.activity.R;

public class UsersAdapter extends BaseAdapter implements OnCheckedChangeListener {
	private List<UserList> mUserlists = null;
	private List<Integer> mPreDelList = null;

	private LayoutInflater mInflate = null;
	//接口相关
	private OnCheckedStateListener onCheckedStateListener = null;
	
	public UsersAdapter(Context context, List<UserList> mUserlists) {
		this.mUserlists = mUserlists;
		mPreDelList = new ArrayList<Integer>();
		
		mInflate = LayoutInflater.from(context);
	}
	
	/**
	 * 列表有内容被反选时调用
	 * */
	public interface OnCheckedStateListener {
		public void onChange();
	}
	
	public void setOnCheckedStateListener(OnCheckedStateListener listener) {
		this.onCheckedStateListener = listener;
	}
	
	public final class ListItem {
		public CheckBox cbSelect;
		public ImageView ivHead;
		public TextView tvNickName;
		public TextView tvUsername;
	}
	
	public void reset(int[] positions) {
		for(int i = 0, len = positions.length; i < len; i++) {
			mUserlists.remove(positions[i]);
		}
		mPreDelList.clear();
	}
	
	public UserList getUserlist(int position) {
		return mUserlists.get(position);
	}
	
	public List<Integer> getmPreDelList() {
		return mPreDelList;
	}

	public int getCheckedCount() {
		return mPreDelList.size();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUserlists.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItem item = null;
		if(convertView == null) {
			convertView = mInflate.inflate(R.layout.userlist_item, null);
			item = new ListItem();
			item.cbSelect = (CheckBox) convertView.findViewById(R.id.userlist_item_cb_select);
			item.ivHead = (ImageView) convertView.findViewById(R.id.userlist_item_iv_headicon);
			item.tvNickName = (TextView) convertView.findViewById(R.id.userlist_item_tv_nickname);
			item.tvUsername = (TextView) convertView.findViewById(R.id.userlist_item_tv_username);
			item.cbSelect.setOnCheckedChangeListener(this);
			convertView.setTag(item);
		}else {
			item = (ListItem) convertView.getTag();
		}
		//将字节数组转为图片,数组是否为空在decImage内判断
		Bitmap bitmap = mUserlists.get(position).getHeadicon();
		if(bitmap != null) {
			item.ivHead.setImageBitmap(bitmap);
			bitmap.recycle();
			bitmap = null;
		}
		item.tvNickName.setText(mUserlists.get(position).getNickname());
		item.tvUsername.setText(mUserlists.get(position).getUsername());
		
		item.cbSelect.setId(position);
		item.cbSelect.setChecked(false);
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton cb, boolean state) {
		int position = cb.getId();
		if(state) {
			mPreDelList.add(position);
		}else {
			if(getCheckedCount() > 0) {
				int index = mPreDelList.indexOf(position);
				if(index != -1) {
					mPreDelList.remove(index);	
				}
			}
		}
		if(onCheckedStateListener != null) {
			onCheckedStateListener.onChange();
		}
	}

}