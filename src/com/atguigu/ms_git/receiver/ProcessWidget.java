package com.atguigu.ms_git.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.atguigu.ms_git.service.UpdateWidgetService;

/**
 * 
 * @author Administrator
 *
 */
public class ProcessWidget extends AppWidgetProvider {

	private Intent intent;

	@Override
	public void onEnabled(Context context) {
		intent = new Intent(context, UpdateWidgetService.class);
		context.startService(intent);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		if (intent != null) {
			context.stopService(intent);
			intent = null;
		}
	}
}
