package com.atguigu.ms_git.ui;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.bean.AppInfo;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.security.dao.AppLockDao;

/**
 * 应用锁
 * 
 * @author Administrator
 * 
 */
public class AppLockActivity extends Activity implements OnItemClickListener {

	private ListView lv_app_lock;
	private LinearLayout ll_app_lock_progress;
	private List<AppInfo> appInfoList;
	private AppLockAdapter adapter;
	private List<String> lockedApps;
	private AppLockDao appLockDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {

		appLockDao = new AppLockDao(this);
		lv_app_lock = (ListView) findViewById(R.id.lv_app_lock);
		ll_app_lock_progress = (LinearLayout) findViewById(R.id.ll_app_lock_progress);
		lv_app_lock.setOnItemClickListener(this);
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				ll_app_lock_progress.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {
					appInfoList = MSUtils.getAllAppInfos(AppLockActivity.this).get(true);
					adapter = new AppLockAdapter();
					lockedApps = appLockDao.getAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {
				ll_app_lock_progress.setVisibility(View.GONE);
				lv_app_lock.setAdapter(adapter);
			}
		}.execute();
	}

	private class AppLockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return appInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return appInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(AppLockActivity.this, R.layout.app_lock_item, null);
				viewHolder = new ViewHolder();
				viewHolder.iconIV = (ImageView) convertView.findViewById(R.id.iv_app_lock_icon);
				viewHolder.nameTV = (TextView) convertView.findViewById(R.id.tv_app_lock_name);
				viewHolder.lockIV = (ImageView) convertView.findViewById(R.id.iv_app_lock);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			AppInfo appInfo = appInfoList.get(position);
			viewHolder.iconIV.setImageDrawable(appInfo.getIcon());
			viewHolder.nameTV.setText(appInfo.getAppName());
			if (lockedApps.contains(appInfo.getPackageName())) {
				viewHolder.lockIV.setImageResource(R.drawable.lock);
				viewHolder.lockIV.setTag(true);
			} else {
				viewHolder.lockIV.setImageResource(R.drawable.unlock);
				viewHolder.lockIV.setTag(false);
			}

			return convertView;
		}
	}

	private class ViewHolder {
		public ImageView iconIV;
		public TextView nameTV;
		public ImageView lockIV;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		// 添加动画效果，动画结束后，就把锁的图片改变
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f);
		animation.setDuration(500);
		view.startAnimation(animation);// 向右水平快速移动后复原

		AppInfo appInfo = appInfoList.get(position);
		ViewHolder viewHolder = (ViewHolder) view.getTag();
		boolean lock = (Boolean) viewHolder.lockIV.getTag();
		if (lock) {
			viewHolder.lockIV.setImageResource(R.drawable.unlock);
			viewHolder.lockIV.setTag(false);
			lockedApps.remove(appInfo.getPackageName());
			Uri uri = Uri.parse("content://com.atguigu.security.provider.applockprovider/delete");
			getContentResolver().delete(uri, null, new String[] { appInfo.getPackageName() });
		} else {
			viewHolder.lockIV.setImageResource(R.drawable.lock);
			viewHolder.lockIV.setTag(true);
			lockedApps.add(appInfo.getPackageName());
			ContentValues values = new ContentValues();
			values.put("packagename", appInfo.getPackageName());
			Uri uri = Uri.parse("content://com.atguigu.security.provider.applockprovider/insert");
			getContentResolver().insert(uri, values);
		}
	}
}
