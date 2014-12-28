package com.atguigu.ms_git.service;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 显示并更新widget的service
 * 
 * @author Administrator
 * 
 */
public class UpdateWidgetService extends Service {

	private AppWidgetManager manager;
	private RemoteViews remoteViews;

	private Timer timer;
	private TimerTask timerTask;

	private Intent intent;
	private PendingIntent pendingIntent;
	private ComponentName componentName;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("TAG", "UpdateWidgetService onCreate()");
		
		manager = AppWidgetManager.getInstance(this);
		remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
		componentName = new ComponentName(getPackageName(), "com.atguigu.ms_git.receiver.ProcessWidget");
		intent = new Intent(this, UpdateWidgetService.class);
		intent.putExtra("clear", true);
		pendingIntent = PendingIntent.getService(this, 1, intent, 0);
		remoteViews.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				Log.i("TAG", "UpdateWidgetService timerTask()");
				remoteViews.setTextViewText(R.id.tv_process_count,
						"进程数为  " + MSUtils.getProcessCount(getApplicationContext()));
				remoteViews.setTextViewText(R.id.tv_process_memory,
						"可用内存为 " + MSUtils.getAvailMemory(getApplicationContext()));
				manager.updateAppWidget(componentName, remoteViews);
			}
		};
		timer.schedule(timerTask, 1000, 3000);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		boolean clear = intent.getBooleanExtra("clear", false);
		if (clear) {
			Log.e("TAG", "点击了清理!");
			MSUtils.killAllProcess(this);
			remoteViews.setTextViewText(R.id.tv_process_count,
					"进程数为:  " + MSUtils.getProcessCount(getApplicationContext()));
			remoteViews.setTextViewText(R.id.tv_process_memory,
					"可用内存为: " + MSUtils.getAvailMemory(getApplicationContext()));
			manager.updateAppWidget(componentName, remoteViews);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();
		timer = null;
		timerTask = null;
	}
}
