package com.atguigu.ms_git.ui;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.bean.AppInfo;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 程序管理
 * 
 * @author xfzhang
 * 
 */
public class AppManagerActivity extends Activity implements OnClickListener, OnItemClickListener {

	private TextView tv_title;
	private ListView lv_app_manager;
	private LinearLayout ll_app_manager_progress;

	private boolean showSystemApp = true; // 是否显示系统应用
	private Map<Boolean, List<AppInfo>> appInfoMap;
	private AppManagerAdapter adapter;

	private PopupWindow popupWindow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_manager);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.app_manager_all);
		tv_title.setOnClickListener(this);
		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_app_manager_progress = (LinearLayout) findViewById(R.id.ll_app_manager_progress);
		lv_app_manager.setOnItemClickListener(this);
		lv_app_manager.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				removePopupWindow();
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});
		new AsyncTask<Void, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				ll_app_manager_progress.setVisibility(View.VISIBLE);// 提示正在加载中...
			}

			@Override
			protected Integer doInBackground(Void... params) {

				try {
					appInfoMap = MSUtils.getAllAppInfos(AppManagerActivity.this);
					adapter = new AppManagerAdapter(appInfoMap);
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
					return 2;
				}

			}

			@Override
			protected void onPostExecute(Integer resultCode) {
				ll_app_manager_progress.setVisibility(View.GONE);
				if (resultCode == 1) {
					lv_app_manager.setAdapter(adapter);
				} else {
					Toast.makeText(AppManagerActivity.this, "读取应用信息出错", 0).show();
				}
			}
		}.execute();

	}

	/**
	 * 初始化PopupWindow
	 */
	private View popupView;
	private LinearLayout ll_app_uninstall;
	private LinearLayout ll_app_start;
	private LinearLayout ll_app_share;

	private class AppManagerAdapter extends BaseAdapter {

		Map<Boolean, List<AppInfo>> map;

		public void setMap(Map<Boolean, List<AppInfo>> map) {
			this.map = map;
		}

		public AppManagerAdapter(Map<Boolean, List<AppInfo>> map) {
			this.map = map;
		}

		@Override
		public int getCount() {
			return map.get(showSystemApp).size();
		}

		@Override
		public Object getItem(int position) {
			return map.get(showSystemApp).get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 处理视图
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(AppManagerActivity.this, R.layout.app_manager_item, null);
				viewHolder = new ViewHolder();
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_app_manager_icon);
				viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_app_manager_name);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			// 处理数据
			AppInfo appInfo = map.get(showSystemApp).get(position);
			viewHolder.imageView.setImageDrawable(appInfo.getIcon());
			viewHolder.textView.setText(appInfo.getAppName());

			return convertView;
		}

		private class ViewHolder {
			public ImageView imageView;
			public TextView textView;
		}
	}

	@Override
	public void onClick(View v) {
		AppInfo appInfo = (AppInfo) v.getTag();
		if (v == tv_title) {
			switchList();
		} else if (v == ll_app_share) {
			shareApp(appInfo.getAppName());
		} else if (v == ll_app_start) {
			startApp(appInfo.getPackageName());
		} else if (v == ll_app_uninstall) {
			uninStallApp(appInfo);
		}
	}

	/**
	 * 卸载指定的应用
	 * 
	 * @param appInfo
	 */
	private void uninStallApp(AppInfo appInfo) {
		removePopupWindow();
		if (appInfo.isSystemApp()) {
			Toast.makeText(this, "系统应用不能卸载!", 0).show();
		} else if (getPackageName().equals(appInfo.getPackageName())) {
			Toast.makeText(this, "当前应用不能卸载!", 0).show();
		} else {
			Intent intent = new Intent(Intent.ACTION_DELETE);
			intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
			startActivityForResult(intent, 1);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			appInfoMap = MSUtils.getAllAppInfos(this);
			adapter.setMap(appInfoMap);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据包名启动对应的应用
	 * 
	 * @param packageName
	 */
	private void startApp(String packageName) {
		removePopupWindow();
		PackageManager packageManager = getPackageManager();
		Intent intent = packageManager.getLaunchIntentForPackage(packageName);
		if (intent == null) {
			MSUtils.showMsg(this, "此应用无法启动");
		} else {
			startActivity(intent);
		}
	}

	/**
	 * 分享指定的应用
	 * 
	 * @param appName
	 */
	private void shareApp(String appName) {
		removePopupWindow();
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");// 纯文件
		intent.putExtra(Intent.EXTRA_SUBJECT, "应用分享");
		intent.putExtra(Intent.EXTRA_TEXT, "分享一个不错的应用: " + appName); // 内容
		startActivity(intent);
	}

	/**
	 * 在全部应用与用户应用间切换
	 */
	private void switchList() {
		showSystemApp = !showSystemApp;
		if (showSystemApp) {
			tv_title.setText(R.string.app_manager_all);
		} else {
			tv_title.setText(R.string.app_manager_customer);
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		removePopupWindow();

		popupView = View.inflate(this, R.layout.popup_view, null);
		ll_app_uninstall = (LinearLayout) popupView.findViewById(R.id.ll_app_uninstall);
		ll_app_uninstall.setOnClickListener(this);
		ll_app_start = (LinearLayout) popupView.findViewById(R.id.ll_app_start);
		ll_app_start.setOnClickListener(this);
		ll_app_share = (LinearLayout) popupView.findViewById(R.id.ll_app_share);
		ll_app_share.setOnClickListener(this);
		popupWindow = new PopupWindow(popupView, 240, view.getHeight());
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		AppInfo appInfo = appInfoMap.get(showSystemApp).get(position);
		ll_app_share.setTag(appInfo);
		ll_app_start.setTag(appInfo);
		ll_app_uninstall.setTag(appInfo);

		popupWindow.showAsDropDown(view, 50, 0 - view.getHeight());
		Animation animation = new ScaleAnimation(0f, 1f, 0f, 1f);
		animation.setDuration(500);
		popupView.startAnimation(animation);
	}

	private void removePopupWindow() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
}
