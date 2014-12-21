package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 手机防盗界面
 * 
 * @author xfzhang
 * 
 */
public class LostProtectActivity extends Activity implements OnClickListener {

	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lost_protect);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		sp = getSharedPreferences("security", Context.MODE_PRIVATE);

		String lostPwd = sp.getString("protect_pwd", null);
		if (lostPwd == null) {// 还没有进行密码设置
			showSetDialog();
		} else {// 设置过密码
			showLoginDialog();
		}
	}

	/**
	 * 显示登陆dialog
	 */
	private Dialog pwdLoginDialog;
	private EditText et_pwd_login_password;
	private Button btn_pwd_login_yes;
	private Button btn_pwd_login_no;

	private void showLoginDialog() {
		View view = View.inflate(this, R.layout.pwd_login_dialog, null);
		et_pwd_login_password = (EditText) view.findViewById(R.id.et_pwd_login_password);
		btn_pwd_login_yes = (Button) view.findViewById(R.id.btn_pwd_login_yes);
		btn_pwd_login_yes.setOnClickListener(this);
		btn_pwd_login_no = (Button) view.findViewById(R.id.btn_pwd_login_no);
		btn_pwd_login_no.setOnClickListener(this);
		
		pwdLoginDialog = new Dialog(this, R.style.myDialog);
		pwdLoginDialog.setContentView(view);
		pwdLoginDialog.setCancelable(false);
		pwdLoginDialog.show();
	}

	/**
	 * 显示密码设置dialog
	 */
	private Dialog pwdSetDialog;
	private EditText et_pwd_set_pwd;
	private EditText et_pwd_set_confirm_pwd;
	private Button btn_pwd_set_yes;
	private Button btn_pwd_set_no;

	private void showSetDialog() {
		View view = View.inflate(this, R.layout.pwd_set_dialog, null);
		et_pwd_set_pwd = (EditText) view.findViewById(R.id.et_pwd_set_pwd);
		et_pwd_set_confirm_pwd = (EditText) view.findViewById(R.id.et_pwd_set_confirm_pwd);
		btn_pwd_set_yes = (Button) view.findViewById(R.id.btn_pwd_set_yes);
		btn_pwd_set_yes.setOnClickListener(this);
		btn_pwd_set_no = (Button) view.findViewById(R.id.btn_pwd_set_no);
		btn_pwd_set_no.setOnClickListener(this);

		pwdSetDialog = new Dialog(this, R.style.myDialog);
		pwdSetDialog.setContentView(view);
		pwdSetDialog.setCancelable(false);
		pwdSetDialog.show();
	}

	@Override
	public void onClick(View v) {
		if (v == btn_pwd_set_yes) {
			String pwd = et_pwd_set_pwd.getText().toString();
			String confrmPwd = et_pwd_set_confirm_pwd.getText().toString();
			if (TextUtils.isEmpty(pwd)) {
				MSUtils.showMsg(this, "密码不能为空!");
				return;
			}
			if (!pwd.equals(confrmPwd)) {
				MSUtils.showMsg(this, "确认密码必须与密码相同!");
				return;
			}
			sp.edit().putString("protect_pwd", pwd).commit();
			MSUtils.showMsg(this, "进入手机防盗设置流程!");
			finish();
		} else if (v == btn_pwd_set_no) {
			finish();
		} else if (v == btn_pwd_login_yes) {
			String protectPwd = sp.getString("protect_pwd", null);
			String pwd = et_pwd_login_password.getText().toString();
			if(!protectPwd.equals(pwd)) {
				MSUtils.showMsg(this, "密码不正确!");
				return;
			}
			MSUtils.showMsg(this, "进入手机防盗设置流程!");
			finish();
		} else if (v == btn_pwd_login_no) {
			finish();
		}
	}
}
