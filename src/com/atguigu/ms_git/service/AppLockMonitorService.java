package com.atguigu.ms_git.service;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.atguigu.ms_git.ui.LockScreenActivity;
import com.atguigu.security.dao.AppLockDao;

/**
 * 监听启动锁定应用的Service
 * 
 * @author Administrator
 * 
 */
public class AppLockMonitorService extends Service {

	private AppLockDao appLockDao;
	private List<String> appLockNames;
	private boolean flag = true;
	private ActivityManager am;
	private List<String> tempUnlockNames;

	private static AppLockMonitorService instance;

	public static AppLockMonitorService getInstance() {
		return instance;
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;
		appLockDao = new AppLockDao(this);
		appLockNames = appLockDao.getAll();
		tempUnlockNames = new ArrayList<String>();
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		Uri uri = Uri.parse("content://com.atguigu.security.provider.applockprovider");
		getContentResolver().registerContentObserver(uri, true, observer);

		startMonitor();
	}

	protected ContentObserver observer = new ContentObserver(new Handler()) {
		public void onChange(boolean selfChange) {
			appLockNames = appLockDao.getAll();
			Log.e("TAG", "ContentObserver onChange()..");
		}
	};

	/**
	 * 开启一直运行的子线程监听
	 */
	private void startMonitor() {
		new Thread(new Runnable() {
			@Override
			public void run() {

				while (flag) {
					// 1. 得到当前最近启动的应用packageName
					String packageName = getCurrentPackageName();
					Log.e("TAG", "start pn=" + packageName);
					try {
						// 判断是否在临时不锁定的列表中, 如果是不用显示解锁界面
						// 2. 判断是否在需要锁定的应用列表中?
						// 如果在, 启动一个锁屏界面(LockScreenActivity)
						if (!tempUnlockNames.contains(packageName) && appLockNames.contains(packageName)) {
							startLockScreenActivity(packageName);
						}
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	private void startLockScreenActivity(String packageName) {
		Intent intent = new Intent(this, LockScreenActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("packagename", packageName);
		startActivity(intent);
	}

	/**
	 * 获得当前运行的应用的包名
	 * 
	 * @return
	 */
	private String getCurrentPackageName() {
		return am.getRunningTasks(1).get(0).topActivity.getPackageName();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		getContentResolver().unregisterContentObserver(observer);// 解注册观察者
	}
	
	public void addToTempUnlockNames(String packageName) {
		tempUnlockNames.add(packageName);
	}
}
