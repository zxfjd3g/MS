package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atguigu.ms_git.R;
/**
 * 设置向导第一步
 * @author xfzhang
 *
 */
public class SetupGuide1Activity extends Activity {

	private TextView tv_guide_title;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_guide1);
		
		init();
	}

	private void init() {
		tv_guide_title = (TextView) findViewById(R.id.tv_guide_title);
		tv_guide_title.setText(R.string.guide1);
	}
	
	public void nextGuide(View v) {
		startActivity(new Intent(this, SetupGuide2Activity.class));
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
}
