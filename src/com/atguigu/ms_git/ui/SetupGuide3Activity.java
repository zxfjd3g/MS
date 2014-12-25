package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 设置向导第三步
 * 
 * @author xfzhang
 * 
 */
public class SetupGuide3Activity extends Activity {

	private TextView tv_guide_title;
	private EditText et_guide3_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup_guide3);

		init();
	}

	private void init() {
		tv_guide_title = (TextView) findViewById(R.id.tv_guide_title);
		tv_guide_title.setText(R.string.guide3);
		
		et_guide3_number = (EditText) findViewById(R.id.et_guide3_number);
		String number = SpUtils.getInstance(this).getString(SpUtils.NUMBER, "");
		et_guide3_number.setText(number);
		
		findViewById(R.id.btn_guide3_contact).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(SetupGuide3Activity.this, ContactListActivity.class), 1);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode==1 && resultCode==2) {
			String number = data.getStringExtra("number");
			et_guide3_number.setText(number);
		}
	}

	public void preGuide(View v) {
		startActivity(new Intent(this, SetupGuide2Activity.class));
		finish();
		overridePendingTransition(R.anim.left_in, R.anim.right_out);
	}

	public void nextGuide(View v) {
		String number = et_guide3_number.getText().toString();
		if(TextUtils.isEmpty(number)) {
			MSUtils.showMsg(this, "必须指定安全号码!");
			return;
		}
		SpUtils.getInstance(this).putString(SpUtils.NUMBER, number);
		startActivity(new Intent(this, SetupGuide4Activity.class));
		finish();
		overridePendingTransition(R.anim.right_in, R.anim.left_out);
	}
}
