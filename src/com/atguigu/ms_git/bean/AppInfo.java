package com.atguigu.ms_git.bean;

import android.graphics.drawable.Drawable;
/**
 * 应用信息对象
 * @author xfzhang
 *
 */
public class AppInfo {

	//图标
	private Drawable icon;
	//名称
	private String appName;
	//包名(标识)
	private String packageName;
	//是否是系统内置应用
	private boolean isSystemApp;

	public AppInfo(Drawable icon, String appName, String packageName, boolean isSystemApp) {
		super();
		this.icon = icon;
		this.appName = appName;
		this.packageName = packageName;
		this.isSystemApp = isSystemApp;
	}

	public AppInfo() {
		super();
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	@Override
	public String toString() {
		return "AppInfo [icon=" + icon + ", appName=" + appName + ", packageName=" + packageName
				+ ", isSystemApp=" + isSystemApp + "]";
	}

}
