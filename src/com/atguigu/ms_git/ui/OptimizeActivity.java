package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.atguigu.ms_git.R;

/**
 * 系统优化界面
 * 
 * @author Administrator
 * 
 */
public class OptimizeActivity extends Activity implements OnClickListener {

	private TextView tv_optimize_cache_clear;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_optimize);

		tv_optimize_cache_clear = (TextView) findViewById(R.id.tv_optimize_cache_clear);
		tv_optimize_cache_clear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_optimize_cache_clear:
			Intent cacheClearIntent = new Intent(this, CacheClearActivity.class);
			startActivity(cacheClearIntent);
			break;
		default:
			break;
		}
	}
}
