package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.service.AppLockMonitorService;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 针对锁定应用的锁屏界面
 * 
 * @author Administrator
 * 
 */
public class LockScreenActivity extends Activity {

	private ImageView iv_lock_app_icon;
	private TextView tv_lock_app_name;
	private EditText et_lock_pwd;
	private String password;
	private String packageName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock_screen);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		// 初始化视图
		iv_lock_app_icon = (ImageView) findViewById(R.id.iv_lock_app_icon);
		tv_lock_app_name = (TextView) findViewById(R.id.tv_lock_app_name);
		et_lock_pwd = (EditText) findViewById(R.id.et_lock_pwd);

		// 设置视图显示数据
		packageName = getIntent().getStringExtra("packagename");
		try {
			PackageManager pm = getPackageManager();
			ApplicationInfo applicationInfo = getPackageManager().getApplicationInfo(packageName, 0);
			Drawable icon = applicationInfo.loadIcon(pm);
			iv_lock_app_icon.setImageDrawable(icon);
			String appName = applicationInfo.loadLabel(pm).toString();
			tv_lock_app_name.setText(appName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 获取保存的保护密码
		password = SpUtils.getInstance(this).getString("protect_pwd", null);
	}

	public void confirm(View v) {
		String inputPwd = et_lock_pwd.getText().toString();
		if (TextUtils.isEmpty(password)) {
			MSUtils.showMsg(this, "还未设置保护密码, 请进行手机防盗设置!");
		} else if (TextUtils.isEmpty(inputPwd)) {
			MSUtils.showMsg(this, "密码不能为空!");
		} else if (!inputPwd.equals(password)) {
			MSUtils.showMsg(this, "密码不正确!");
		} else {
			finish();
			AppLockMonitorService.getInstance().addToTempUnlockNames(packageName);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
}
