package com.qqpractice.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qqpractice.core.LoginUtils;
import com.qqpractice.core.LoginUtils.OnLoginResultListener;
import com.qqpractice.databases.DataMan;
import com.qqpractice.databases.SQLiteHelper;
import com.qqpractice.ui.LayoutButton;
import com.qqpractice.ui.bases.BaseActivity;

public class LoginActivity extends BaseActivity implements OnClickListener, OnLoginResultListener {
	public static final int FLAG = 2;
	
	private static final int LOGIN_USER = 0;
	private static final int LOGIN_KEY = 1;
	
	private Button btnlogin = null;
	private ImageView imgUserIcon = null;
	private LayoutButton btnAccountMgr = null;
	
	private AutoCompleteTextView edUser = null;
	private EditText edPass = null;
	private TextView[] textviews;
	private ImageView[] btnClears;
	private int[] bitmaps, lightBitmaps;
	
	private LoginUtils loginUtil = null;
	private DataMan dataMan = null;
	
	private String user, pwd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		initview();
		
		dataMan = new DataMan(this);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle != null) {
			receive(bundle);
		}
		//账号密码自动完成
		loadAdapte();
	}
	
	private void initview() {
		btnlogin = (Button) findViewById(R.id.Login_btnLogin);
		imgUserIcon = (ImageView) findViewById(R.id.Login_UserIcon);
		btnAccountMgr = (LayoutButton) findViewById(R.id.Login_btnAccountmgr);
		
		edUser = (AutoCompleteTextView) findViewById(R.id.Login_UserName);
		edPass = (EditText) findViewById(R.id.Login_Password);
		
		textviews = new TextView[2];
		textviews[LOGIN_USER] = (TextView) findViewById(R.id.Login_UserNameIcon);
		textviews[LOGIN_KEY] = (TextView) findViewById(R.id.Login_PasswordIcon);
		
		btnClears = new ImageView[2];
		btnClears[LOGIN_USER] = (ImageView) findViewById(R.id.Login_UserName_Clear);
		btnClears[LOGIN_KEY] = (ImageView) findViewById(R.id.Login_Password_Clear);
		
		bitmaps = new int[2];
		bitmaps[LOGIN_USER] = R.drawable.login_user;
		bitmaps[LOGIN_KEY] = R.drawable.login_key;
		
		lightBitmaps = new int[2];
		lightBitmaps[LOGIN_USER] = R.drawable.login_user_light;
		lightBitmaps[LOGIN_KEY] = R.drawable.login_key_light;
		
		edUser.setOnItemClickListener(new AutoCompleteAccountClickListener());
		edUser.addTextChangedListener(new textWatcher(LOGIN_USER));
		edPass.addTextChangedListener(new textWatcher(LOGIN_KEY));
		edUser.setOnFocusChangeListener(new focusChangeListener(LOGIN_USER));
		edPass.setOnFocusChangeListener(new focusChangeListener(LOGIN_KEY));
		btnClears[LOGIN_USER].setOnClickListener(this);
		btnClears[LOGIN_KEY].setOnClickListener(this);
		btnlogin.setOnClickListener(this);
		btnAccountMgr.setOnClickListener(this);
	}
	
	/**
	 * 监听编辑框文本变化，切换图标
	 * **/
	class textWatcher implements TextWatcher {
		int index = 0;
		
		public textWatcher(int index) {
			this.index = index;
		}

		@Override
		public void afterTextChanged(Editable s) {
			int length = s.toString().length();
			textviews[index].setBackgroundResource(length==0?bitmaps[index]:lightBitmaps[index]);
			btnClears[index].setVisibility(length==0?View.GONE:View.VISIBLE);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {}
		
	}
	
	/**
	 * 监听编辑框焦点变化，显示隐藏清空按钮
	 * **/
	class focusChangeListener implements View.OnFocusChangeListener {
		private int index = 0;
		
		public focusChangeListener(int index) {
			this.index = index;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			EditText etTemp = (EditText) v;
			if(hasFocus) {
				if(etTemp.getText().toString().length() > 0) {
					btnClears[index].setVisibility(View.VISIBLE);
				}
			}else {
				btnClears[index].setVisibility(View.GONE);
			}
			
		}
		
	}
	
	class AutoCompleteAccountClickListener implements OnItemClickListener {
		String autoUser = null, autoPass = null;

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			//What the fuck with this fucking verify
			if(dataMan != null) {
				autoUser = ((TextView) view).getText().toString();
				if(autoUser != null && !autoUser.equals("")) {
					//获取密码
					autoPass = dataMan.getpwd(autoUser);
					if(autoPass != null) {
						showPassowrd(autoPass);
					}
					showUserIcon(autoUser);
				}
			}
		}
		
	}
	
	private void receive(Bundle bundle) {
		String user = null, tip = null;
		
		user = bundle.getString(SQLiteHelper.TB_USERINFO_USERNAME);
		tip = bundle.getString(ACTIVITY_TIP);
		//显示错误信息
		if(tip != null) {
			Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
		}
		
		if(user != null) {
			showUsername(user);
		}else {
			//读取最后登录用户
			user = dataMan.getFinaluser();
			if(user.equals(DataMan.RECORD_NO_FOUND)) {
				return ;
			}
		}
		//获取密码
		showPassowrd(dataMan.getpwd(user));
	}
	
	private void clearAll() {
		showUsername("");
		showPassowrd("");
		showUserIcon(null);
	}
	
	private void showUsername(String user) {
		edUser.setText(user);
	}
	
	private void showPassowrd(String pass) {
		edPass.setText(pass);
		//I have no idea that the delete icon will show on the password widget :(
		btnClears[LOGIN_KEY].setVisibility(View.GONE);
	}
	
	private void showUserIcon(String username) {
		//获取头像
		if(username != null && dataMan != null) {
			Bitmap bm = dataMan.getUserIcon(username);
			if(bm != null) {
				imgUserIcon.setImageBitmap(bm);
				bm.recycle();
				bm = null;
				return ;
			}
		}
		imgUserIcon.setImageResource(R.drawable.icon);
	}
	
	private void showGuide(boolean bshow) {
		btnAccountMgr.setCorner(bshow);
	}
	
	private void loadAdapte() {
		boolean bRet = true;
		String[] accounts = dataMan.getAllAccount();
		//Close the connection of the database when the user list is null.
		if(accounts == null) {
			dataMan.close();
			dataMan = null;
			accounts = new String[]{};
			bRet = false;
		}
		
		edUser.setAdapter(new ArrayAdapter<String>(this, 
				R.drawable.dropdown_item_layout, accounts));
		
		showGuide(bRet);
	}
	
	@Override
	public void finish() {
		if(dataMan != null) {
			dataMan.close();
			dataMan = null;
		}
		imgUserIcon = null;
		btnAccountMgr = null;
		btnlogin = null;
		edUser = null;
		edPass = null;
		textviews = null;
		btnClears = null;
		bitmaps = null;
		lightBitmaps = null;
		loginUtil = null;
		user = null;
		pwd = null;
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null) {
			Bundle bundle = data.getExtras();
			if(bundle != null) {
				if(bundle.getBoolean(AccountActivity.ACCOUNT_CHANGED)) {
					//Clear current user which on the editText
					if(bundle.getBoolean(AccountActivity.ACCOUNT_TEMP_USER)) {
						clearAll();
					}
					loadAdapte();
				}
				String username = bundle.getString(SQLiteHelper.TB_USERINFO_USERNAME);
				if(username!= null && !username.equals("")) {
					showUsername(username);
					showPassowrd(bundle.getString(SQLiteHelper.TB_USERINFO_PASSWORD));
					showUserIcon(username);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(loginUtil!= null && !loginUtil.isLogined()) {
				loginUtil.onCancel();
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.Login_btnLogin:
			user = edUser.getText().toString().trim();
			pwd = edPass.getText().toString().trim();
			if(user==null||user.equals("")||pwd==null||pwd.equals("")) {
				OnNotice(R.string.common_error_input);
				return ;
			}
			//loginUtil对象是否为空
			if(loginUtil == null) {
				loginUtil = new LoginUtils(this);
				loginUtil.setOnLoginResultListener(this);
			}
			loginUtil.onLogin(user, pwd);
			break;
		case R.id.Login_btnAccountmgr:
			//如果当前用户不为空则传入用户名
			Bundle bundle = null;
			String usr = edUser.getText().toString().trim();
			if(usr != null && usr.length() > 0) {
				bundle = new Bundle();
				bundle.putString(AccountActivity.ACCOUNT_TEMP_USER, usr);
			}
			openActivityWait(AccountActivity.class, bundle, 0);
			break;
		case R.id.Login_UserName_Clear:
			showUsername("");
			break;
		case R.id.Login_Password_Clear:
			showPassowrd("");
			break;
		}
		
	}

	@Override
	public void onSuccess() {
		Bundle bundle = new Bundle();
		//登陆页面来源标识
		bundle.putInt(ACTIVITY_TAG, FLAG);
		bundle.putString(SQLiteHelper.TB_USERINFO_USERNAME, user);
		bundle.putString(SQLiteHelper.TB_USERINFO_PASSWORD, pwd);
		bundle.putString(SQLiteHelper.TB_USERINFO_SID, loginUtil.getRealSid());
		openActivity(HomeActivity.class, bundle);
		finish();
	}

	@Override
	public void onFailed() {
		OnNotice(R.string.login_error_account);
		
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		
	}

}
