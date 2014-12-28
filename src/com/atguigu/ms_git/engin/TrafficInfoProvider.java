package com.atguigu.ms_git.engin;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.util.Log;

import com.atguigu.ms_git.bean.TrafficInfo;

public class TrafficInfoProvider {

	private Context context;

	public TrafficInfoProvider(Context context) {
		this.context = context;
	}

	public List<TrafficInfo> getAll() {

		List<TrafficInfo> list = new ArrayList<TrafficInfo>();
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> applications = pm.getInstalledApplications(0);
		for(ApplicationInfo info : applications) {
			String appName = (String) info.loadLabel(pm);
			Drawable icon = info.loadIcon(pm);
			int uid = info.uid;
			long uidRxBytes = TrafficStats.getUidRxBytes(uid);
			long uidTxBytes = TrafficStats.getUidTxBytes(uid);
			TrafficInfo trafficInfo = new TrafficInfo();
			trafficInfo.setIcon(icon);
			trafficInfo.setInSize(uidRxBytes);
			trafficInfo.setOutSize(uidTxBytes);
			trafficInfo.setUid(uid);
			trafficInfo.setName(appName);
			list.add(trafficInfo);
		}
		
		
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> ResolveInfos = pm.queryIntentActivities(intent, 0);
		Log.e("TAG", applications.size()+"_"+ResolveInfos.size());
		/*
		for (ResolveInfo ri : ResolveInfos) {
			TrafficInfo info = new TrafficInfo();
			String packageName = ri.activityInfo.packageName;
			Drawable icon = ri.loadIcon(pm);
			info.setIcon(icon);
			String appName = ri.loadLabel(pm).toString();
			info.setName(appName);
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				int uid = packageInfo.applicationInfo.uid;
				info.setUid(uid);

				long inSize = TrafficStats.getUidRxBytes(uid);
				info.setInSize(inSize);

				long outSize = TrafficStats.getUidTxBytes(uid);
				info.setOutSize(outSize);

				list.add(info);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}*/
		return list;
	}
	
	public List<TrafficInfo> getAll2() {

		List<TrafficInfo> list = new ArrayList<TrafficInfo>();
		PackageManager pm = context.getPackageManager();
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> ResolveInfos = pm.queryIntentActivities(intent, 0);

		for (ResolveInfo ri : ResolveInfos) {
			TrafficInfo info = new TrafficInfo();
			String packageName = ri.activityInfo.packageName;
			Drawable icon = ri.loadIcon(pm);
			info.setIcon(icon);
			String appName = ri.loadLabel(pm).toString();
			info.setName(appName);
			try {
				PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
				int uid = packageInfo.applicationInfo.uid;
				info.setUid(uid);

				long inSize = TrafficStats.getUidRxBytes(uid);
				info.setInSize(inSize);

				long outSize = TrafficStats.getUidTxBytes(uid);
				info.setOutSize(outSize);

				list.add(info);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
}
