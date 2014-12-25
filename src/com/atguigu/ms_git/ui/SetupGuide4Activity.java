package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 设置向导第四步
 * 
 * @author xfzhang
 * 
 */
public class SetupGuide4Activity extends Activity {

	private TextView tv_guide_title;
	private CheckBox cb_guide4_setup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_guide4);

		init();
	}

	private void init() {
		tv_guide_title = (TextView) findViewById(R.id.tv_guide_title);
		tv_guide_title.setText(R.string.guide4);

		cb_guide4_setup = (CheckBox) findViewById(R.id.cb_guide4_setup);
		boolean isProtected = SpUtils.getInstance(SetupGuide4Activity.this).getBoolean(SpUtils.IS_PROTECTED, false);
		if (isProtected) {
			cb_guide4_setup.setChecked(true);
			cb_guide4_setup.setText(R.string.guide4_item2);
			cb_guide4_setup.setTextColor(Color.WHITE);
		}
		cb_guide4_setup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					cb_guide4_setup.setText(R.string.guide4_item2);
					cb_guide4_setup.setTextColor(Color.WHITE);
					SpUtils.getInstance(SetupGuide4Activity.this).putBoolean(SpUtils.IS_PROTECTED, true);
				} else {
					SpUtils.getInstance(SetupGuide4Activity.this).remove(SpUtils.IS_PROTECTED);
					cb_guide4_setup.setText(R.string.guide4_item1);
					cb_guide4_setup.setTextColor(Color.RED);
				}
			}
		});
	}

	public void preGuide(View v) {
		startActivity(new Intent(this, SetupGuide3Activity.class));
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	public void confirm(View v) {
		startActivity(new Intent(this, LostProtectActivity.class));
		boolean checked = cb_guide4_setup.isChecked();
		if (!checked) {
			new AlertDialog.Builder(this).setMessage(R.string.guide4_item3)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SpUtils.getInstance(SetupGuide4Activity.this).putBoolean(SpUtils.SET_UP, true);
							finish();
							startActivity(new Intent(SetupGuide4Activity.this, LostProtectActivity.class));
							MSUtils.activeDevince(SetupGuide4Activity.this);
						}
					}).setNegativeButton("取消", null).show();
		} else {
			SpUtils.getInstance(SetupGuide4Activity.this).putBoolean(SpUtils.SET_UP, true);
			MSUtils.activeDevince(SetupGuide4Activity.this);
		}

		finish();
	}
}
