package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.service.AppLockMonitorService;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 设置界面
 * 
 * @author Administrator
 * 
 */
public class SettingActivity extends Activity {
	private TextView tv_lock_tips;
	private CheckBox cb_lock_state;
	private Intent appLockIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);

		appLockIntent = new Intent(this, AppLockMonitorService.class);

		tv_lock_tips = (TextView) findViewById(R.id.tv_lock_tips);
		cb_lock_state = (CheckBox) findViewById(R.id.cb_lock_state);

		boolean isAppLockStart = SpUtils.getInstance(this).getBoolean(SpUtils.APP_LOCK, false);
		if (isAppLockStart) {
			tv_lock_tips.setText("服务已经开启");
			cb_lock_state.setChecked(true);
		} else {
			tv_lock_tips.setText("服务没有开启");
			cb_lock_state.setChecked(false);
		}

		cb_lock_state.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					startService(appLockIntent);
					tv_lock_tips.setText("服务已经开启");
				} else {
					stopService(appLockIntent);
					tv_lock_tips.setText("服务没有开启");
				}
				SpUtils.getInstance(SettingActivity.this).putBoolean(SpUtils.APP_LOCK, isChecked);
			}
		});
	}
}
