package com.qqpractice.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.qqpractice.activity.R;
import com.qqpractice.network.NetImage;

public class VeriftyDialog extends Dialog implements View.OnClickListener {
	private Button btnConfirm;
	private ImageView imageCode;
	private EditText etCode;
	
	//加载验证码对象
	private NetImage netimage = null;
	
	public String getCode() {
		return etCode.getText().toString();
	}
	
	public VeriftyDialog(Context context, String imgurl) {
		super(context, R.style.VerifyDialogTheme);
		setContentView(R.layout.dialog_verify);
		
		imageCode = (ImageView) findViewById(R.id.Verify_ImageCode);
		etCode = (EditText) findViewById(R.id.Verify_SafeCode);
		btnConfirm = (Button) findViewById(R.id.Verify_btnConfirm);
		
		imageCode.setOnClickListener(this);
		//加载验证码图片
		netimage = new NetImage(imgurl, imageCode);
		netimage.load();
	}

	public void setOnActionListener(View.OnClickListener listener) {
		btnConfirm.setOnClickListener(listener);
	}

	@Override
	public void onClick(View v) {
		netimage.load();
		
	}

}
