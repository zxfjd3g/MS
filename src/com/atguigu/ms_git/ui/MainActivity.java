package com.atguigu.ms_git.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.adapter.MainAdapter;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 主界面
 * 
 * @author xfzhang
 * 
 */
public class MainActivity extends Activity implements OnItemClickListener, OnItemLongClickListener {

	private GridView gv_main;
	private MainAdapter adapter;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		sp = getSharedPreferences("security", Context.MODE_PRIVATE);
		gv_main = (GridView) findViewById(R.id.gv_main);
		adapter = new MainAdapter(this);
		gv_main.setAdapter(adapter);
		// 设置item的长按事件监听
		gv_main.setOnItemLongClickListener(this);
		// 设置
		gv_main.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0: // 手机防盗
			startActivity(new Intent(this, LostProtectActivity.class));
			break;
		case 1: // 通讯卫士
			
			break;
		case 2: // 软件管理

			break;
		case 3: // 流量管理

			break;
		case 4:// 任务管理

			break;
		case 5: // 手机杀毒

			break;
		case 6: // 系统优化

			break;
		case 7:// 高级工具
			startActivity(new Intent(this, AToolActivity.class));
			break;
		case 8: // 设置中心

			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

		if (position == 0) {// 点击第一个item(手机防盗)
			final EditText editText = new EditText(this);
			new AlertDialog.Builder(this).setTitle("名称修改").setView(editText)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 得到输入的新名称
							String name = editText.getText().toString();
							// 过滤不输入的情况
							if (TextUtils.isEmpty(name)) {
								MSUtils.showMsg(MainActivity.this, "名称不能为空");
								return;
							}
							// 保存到SP中
							sp.edit().putString("lost_name", name).commit();
							// 更新界面
							adapter.notifyDataSetChanged();
						}
					}).setNegativeButton("取消", null).show();
		}
		return false;
	}
}
