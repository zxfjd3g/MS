package com.atguigu.ms_git.engin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.RemoteException;

import com.atguigu.ms_git.bean.CacheInfo;
import com.atguigu.ms_git.ui.CacheClearActivity;
import com.atguigu.ms_git.util.MSUtils;

public class CacheInfoProvider {

	private PackageManager pm;
	private List<CacheInfo> cacheInfos = new ArrayList<CacheInfo>();

	public CacheInfoProvider(Context context) {
		pm = context.getPackageManager();
	}

	public List<CacheInfo> getCacheInfos() {
		return cacheInfos;
	}

	public void loadData(final Handler handler) {

		Collections.synchronizedCollection(cacheInfos);
		final List<ApplicationInfo> applications = pm.getInstalledApplications(0);
		for (int i = 0; i < applications.size(); i++) {
			ApplicationInfo info = applications.get(i);

			final CacheInfo cacheInfo = new CacheInfo();

			String packageName = info.packageName;
			cacheInfo.setPackageName(packageName);
			Drawable icon = info.loadIcon(pm);
			cacheInfo.setIcon(icon);
			String appName = (String) info.loadLabel(pm);
			cacheInfo.setName(appName);
			try {
				Method method = PackageManager.class.getMethod("getPackageSizeInfo", String.class,
						IPackageStatsObserver.class);
				method.invoke(pm, packageName, new IPackageStatsObserver.Stub() {
					@Override
					public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
						cacheInfo.setCodeSize(MSUtils.formatMemorySize(pStats.codeSize));
						cacheInfo.setDataSize(MSUtils.formatMemorySize(pStats.dataSize));
						cacheInfo.setCacheSize(MSUtils.formatMemorySize(pStats.cacheSize));

						cacheInfos.add(cacheInfo);
						if (cacheInfos.size() == applications.size()) {
							handler.sendEmptyMessage(CacheClearActivity.FINISH);
						}
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
