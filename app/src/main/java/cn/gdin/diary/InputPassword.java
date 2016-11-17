package cn.gdin.diary;

import cn.gdin.diary.util.MD5Util;
import cn.gdin.diary.util.MyApplication;
import cn.gdin.diary.util.Util;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputPassword extends Activity {
	private Button mBtnPwdOk;
	private Button mBtnCancel;
	private EditText mPassword;
	private String pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_pwd);
		
		MyApplication.getInstance().addActivity(this);
		
		mPassword = (EditText) findViewById(R.id.et_pwd); // 填写密码文本框
		mBtnPwdOk = (Button) findViewById(R.id.btn_pwd_ok);// 确认密码
		mBtnCancel = (Button) findViewById(R.id.btn_pwd_cancel);// 取消
		// 解密
		pwd = MD5Util.JM(MD5Util.KL(Util.loadData(this)));
		
		//判断读取的密码是否为空
		if (!TextUtils.isEmpty(pwd)) {
			pwdBtnOnclick();
		} else {
			//跳转到ItemActivity，处理相关操作（设置密码）
			Intent intent = new Intent();
			intent.setClass(InputPassword.this, ItemActivity.class);
			startActivity(intent);
			finish();
		}

	}

	// 输入密码
	private void pwdBtnOnclick() {

		mBtnPwdOk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String password = mPassword.getText().toString();
				//对输入的密码进行MD5加密后去匹配正确的密码
				String passwd = MD5Util.MD5(password);
				
				if (passwd.equals(pwd)) {
					Intent intent = new Intent();
					intent.setClass(InputPassword.this, ItemActivity.class);
					startActivity(intent);
					finish();
				} else {
					// 弹出提示，密码错误
					Toast.makeText(InputPassword.this, "密码错误！", 0).show();
				}

			}
		});
		//用户点击了取消，退出
		mBtnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
