package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.engin.AddressService;

public class QueryAddressActivity extends Activity {

	private EditText et_query_number;
	private TextView tv_query_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);

		et_query_number = (EditText) findViewById(R.id.et_query_number);
		tv_query_result = (TextView) findViewById(R.id.tv_query_result);
	}

	// 点击查询
	public void onClickQuery(View v) {
		String number = et_query_number.getText().toString();
		if (TextUtils.isEmpty(number)) {
			et_query_number.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
		} else {
			String address = new AddressService().getAddress(number);
			tv_query_result.setText(number + ": " + address);
		}
	}
}
