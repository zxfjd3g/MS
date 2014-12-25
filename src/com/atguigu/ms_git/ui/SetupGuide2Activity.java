package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.ms_git.util.SpUtils;
/**
 * 设置向导第二步
 * @author xfzhang
 *
 */
public class SetupGuide2Activity extends Activity {

	private TextView tv_guide_title;
	private CheckBox cb_guide2_bound;
	private String simNumber;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_guide2);
		
		init();
	}

	private void init() {
		tv_guide_title = (TextView) findViewById(R.id.tv_guide_title);
		tv_guide_title.setText(R.string.guide2);
		
		cb_guide2_bound = (CheckBox) findViewById(R.id.cb_guide2_bound);
		simNumber = SpUtils.getInstance(this).getString(SpUtils.SIM, null);
		if(simNumber!=null) {
			cb_guide2_bound.setText(R.string.guide2_bounded);
			cb_guide2_bound.setTextColor(Color.WHITE);
			cb_guide2_bound.setChecked(true);
		} else {
			simNumber = MSUtils.getSimNumber(this);
		}
		cb_guide2_bound.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean checked = cb_guide2_bound.isChecked();
				if(checked) {
					cb_guide2_bound.setText(R.string.guide2_bounded);
					cb_guide2_bound.setTextColor(Color.WHITE);
					SpUtils.getInstance(SetupGuide2Activity.this).putString(SpUtils.SIM, simNumber);
				} else {
					SpUtils.getInstance(SetupGuide2Activity.this).remove(SpUtils.SIM);
					cb_guide2_bound.setText(R.string.guide2_unbound);
					cb_guide2_bound.setTextColor(Color.RED);
				}
			}
		});
	}
	
	public void preGuide(View v) {
		startActivity(new Intent(this, SetupGuide1Activity.class));
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}
	
	public void nextGuide(View v) {
		startActivity(new Intent(this, SetupGuide3Activity.class));
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
}
