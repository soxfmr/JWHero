package com.qqpractice.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.qqpractice.databases.DataMan;
import com.qqpractice.databases.SQLiteHelper;
import com.qqpractice.user.UserList;
import com.qqpractice.user.UsersAdapter;

public class AccountActivity extends Activity implements UsersAdapter.OnCheckedStateListener {
	public static final String ACCOUNT_CHANGED = "account_changed";
	public static final String ACCOUNT_TEMP_USER = "account_tempuser";
	
	private ListView mListview = null;
	private Button btnDelete = null;
	
	private UsersAdapter mAdapter = null;
	private DataMan dataMan = null;
	
	private boolean bDeleted = false;
	private boolean bTempUserClear = false;
	
	private String tempUser = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account);

		initview();
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			tempUser = bundle.getString(ACCOUNT_TEMP_USER);
		}
		
		dataMan = new DataMan(this);
		loadData();
	}
	
	public void initview() {
		mListview = (ListView) findViewById(R.id.account_lv_userlist); 
		btnDelete = (Button) findViewById(R.id.account_btndelete);
		btnDelete.setOnClickListener(new onAcountDeleteListener());
	}
	
	public void loadData() {
		List<UserList> list = dataMan.getUserList();
		if(list != null) {
			mAdapter = new UsersAdapter(this, list);
			mAdapter.setOnCheckedStateListener(this);
			mListview.setAdapter(mAdapter);
			mListview.setOnItemClickListener(new onAccountClickListener());
		}else {
			dataMan.close();
			dataMan = null;	
		}
	}

	@Override
	public void finish() {
		if(dataMan != null) {
			dataMan.close();
			dataMan = null;
		}
		btnDelete = null;
		mListview = null;
		mAdapter = null;
		super.finish();
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			back(null);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void back(Bundle bundle) {
		Intent intent = new Intent();
		if(bundle == null) {
			bundle = new Bundle();
		}
		bundle.putBoolean(ACCOUNT_CHANGED, bDeleted);
		bundle.putBoolean(ACCOUNT_TEMP_USER, bTempUserClear);
		intent.putExtras(bundle);
		setResult(0, intent);
		finish();
	}
	
	class onAccountClickListener implements OnItemClickListener {
		
		@Override
		public void onItemClick(AdapterView<?> adpter, View view,
				int position, long id) {
			select(position);
			
		}
		
		public void select(int position) {
			Bundle bundle = new Bundle();
			UserList list = mAdapter.getUserlist(position);
			bundle.putString(SQLiteHelper.TB_USERINFO_USERNAME, list.getUsername());
			bundle.putString(SQLiteHelper.TB_USERINFO_PASSWORD, list.getPassword());
			back(bundle);
		}
	}
	
	class onAcountDeleteListener implements View.OnClickListener {

		@Override
		public void onClick(View view) {
			UserList ul = null;
			String username = null;
			String finaluser = dataMan.getFinaluser();
			//当前删除项目索引
			int position = 0;
			final int size = mAdapter.getCheckedCount();
			//存储id类标
			int[] positions = new int[size];
			List<Integer> delList = mAdapter.getmPreDelList();
			for(int i = 0;  i < size; i++) {
				//获取用户
				position = delList.get(i);
				ul = mAdapter.getUserlist(position);
				username = ul.getUsername();
				//清除用户
				dataMan.remove(ul.getUsername());
				//匹配当前登录界面用户
				if(tempUser != null && username.equals(tempUser)) {
					bTempUserClear = true;
				}
				//匹配最后登录用户
				if(finaluser.equals(username)) {
					dataMan.setFinalusr(DataMan.RECORD_NO_FOUND);
				}
				ul = null;
			}
			//更新adapter
			mAdapter.reset(positions);
			mAdapter.notifyDataSetChanged();
			//重置禁用状态
			btnDelete.setEnabled(false);
			
			bDeleted = true;
		}
	}

	@Override
	public void onChange() {
		if(mAdapter != null) {
			btnDelete.setEnabled(mAdapter.getCheckedCount() > 0 ? true : false);
		}
		
	}
}
