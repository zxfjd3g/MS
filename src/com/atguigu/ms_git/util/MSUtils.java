package com.atguigu.ms_git.util;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

/**
 * 工具帮助类
 * 
 * @author xfzhang
 * 
 */
@SuppressLint("ServiceCast")
public final class MSUtils {

	/**
	 * 得到手机中的当前应用的版本号 使用: PackageManager
	 * 
	 * @param context
	 * @return
	 */
	public static String getVersion(Context context) {
		String version = null;
		PackageManager pm = (PackageManager) context.getPackageManager();
		try {
			PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			version = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			version = "未知";
		}
		return version;
	}

	/**
	 * 判断网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetConnected(Context context) {
		boolean connected = false;
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null) {
			connected = networkInfo.isConnected();
		}
		return connected;
	}

	/**
	 * 显示提示
	 * 
	 * @param context
	 * @param msg
	 */
	public static void showMsg(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 安装APK
	 * 
	 * @param context
	 * @param apkFile
	 */
	public static void installAPK(Context context, File apkFile) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
