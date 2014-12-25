package com.atguigu.ms_git.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.api.APIClient;
import com.atguigu.ms_git.service.NumberAddressService;
import com.atguigu.ms_git.util.Constant;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 高级工具
 * 
 * @author xfzhang
 * 
 */
public class AToolActivity extends Activity implements OnClickListener {

	protected static final int DOWNLOAD_DB_SUCCESS = 1;
	protected static final int DOWNLOAD_DB_ERROR = 2;
	private TextView tv_atool_query;
	private ProgressDialog pd;

	private RelativeLayout rl_atool_number_service_state;
	private TextView tv_atool_number_service_state;
	private CheckBox cb_atool_state;

	private TextView tv_atool_select_bg;

	private TextView tv_atool_change_location;

	private Intent serviceIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);

		init();
	}

	private void init() {
		// 第一行
		tv_atool_query = (TextView) findViewById(R.id.tv_atool_query);
		tv_atool_query.setOnClickListener(this);

		// 第二行
		rl_atool_number_service_state = (RelativeLayout) findViewById(R.id.rl_atool_number_service_state);
		rl_atool_number_service_state.setOnClickListener(this);
		tv_atool_number_service_state = (TextView) findViewById(R.id.tv_atool_number_service_state);
		cb_atool_state = (CheckBox) findViewById(R.id.cb_atool_state);

		boolean worked = MSUtils.isServiceWorked(this, NumberAddressService.class);
		if (worked) {
			cb_atool_state.setChecked(true);
			// 更新界面
			tv_atool_number_service_state.setText(R.string.number_service_state_yes);
			tv_atool_number_service_state.setTextColor(Color.BLACK);
		}

		// 第三行
		tv_atool_select_bg = (TextView) findViewById(R.id.tv_atool_select_bg);
		tv_atool_select_bg.setOnClickListener(this);

		// 第四行
		tv_atool_change_location = (TextView) findViewById(R.id.tv_atool_change_location);
		tv_atool_change_location.setOnClickListener(this);

		serviceIntent = new Intent(this, NumberAddressService.class);
	}

	@Override
	public void onClick(View v) {
		if (v == tv_atool_query) {
			toAddressQuery();
		} else if (v == rl_atool_number_service_state) {
			updateServiceState();
		} else if (v == tv_atool_select_bg) {
			setStyle();
		} else if (v == tv_atool_change_location) {
			startActivity(new Intent(this, DragViewActivity.class));
		}
	}

	/**
	 * 设置归属地提示视图的背景颜色样式
	 */
	private void setStyle() {
		String[] items = { "半透明", "活力橙", "苹果绿", "孔雀蓝", "金属灰" };
		new AlertDialog.Builder(this).setTitle("选择颜色")
				.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						SpUtils.getInstance(AToolActivity.this).putInt(SpUtils.BG_COLOR_INDEX, which);
					}
				}).setPositiveButton(android.R.string.ok, null).show();
	}

	/**
	 * 更新号码归属地服务的状态(更新界面,启动或停止服务)
	 */
	private void updateServiceState() {
		// 改变checkbox的勾选状态
		cb_atool_state.setChecked(!cb_atool_state.isChecked());
		if (cb_atool_state.isChecked()) {
			// 更新界面
			tv_atool_number_service_state.setText(R.string.number_service_state_yes);
			tv_atool_number_service_state.setTextColor(Color.BLACK);
			// 启动服务
			startService(serviceIntent);
		} else {
			// 更新界面
			tv_atool_number_service_state.setText(R.string.number_service_state_no);
			tv_atool_number_service_state.setTextColor(Color.RED);
			// 停止服务
			stopService(serviceIntent);
		}
	}

	/**
	 * 去归属地查询界面 判断Sd卡中是否有归属地数据库文件(address.db), 如果没有需要从服务器上异步下载到sd卡中
	 */
	private void toAddressQuery() {
		if (isDBExist()) {
			startActivity(new Intent(this, QueryAddressActivity.class));
		} else {
			new AsyncTask<Void, Void, Integer>() {
				protected void onPreExecute() {
					pd = new ProgressDialog(AToolActivity.this);
					pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pd.setTitle("下载更新");
					pd.setCancelable(false);
					pd.show();
				}

				@Override
				protected Integer doInBackground(Void... params) {

					// 准备数据库File对象
					File dirFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
							+ "/Security/db");
					if (!dirFile.exists()) {
						dirFile.mkdirs();
					}
					File file = new File(dirFile, "address.db");

					// 数据库文件的url
					String url = Constant.URL_ADDRESS_DB;

					// 开始下载, 下载过程中进度条会及时更新
					try {
						APIClient.downloadFile(url, file, pd);
						return DOWNLOAD_DB_SUCCESS;
					} catch (Exception e) {
						e.printStackTrace();
						return DOWNLOAD_DB_ERROR;
					}
				}

				protected void onPostExecute(Integer result) {

					pd.dismiss();
					if (result == DOWNLOAD_DB_SUCCESS) {
						MSUtils.showMsg(AToolActivity.this, "下载数据库成功!");
					} else if (result == DOWNLOAD_DB_ERROR) {
						MSUtils.showMsg(AToolActivity.this, "下载数据库失败!");
					}
				}
			}.execute();
		}
	}

	/**
	 * 判断Sd卡中是否有归属地数据库文件
	 * 
	 * @return
	 */
	private boolean isDBExist() {
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()
					+ "/Security/db/address.db");
			return file.exists();
		}
		return false;
	}
}
