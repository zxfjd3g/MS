package com.atguigu.ms_git.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.atguigu.ms_git.receiver.MyAdminReceiver;

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

	/**
	 * 将一个inputStream读取成一个字符串
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String readString(InputStream is) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = is.read(buffer)) > 0) {
			bos.write(buffer, 0, len);
		}
		bos.close();

		return bos.toString();
	}

	/**
	 * 得到手机卡的唯一序列号
	 * 
	 * @return
	 */
	public static String getSimNumber(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getSimSerialNumber();
	}

	/**
	 * 发送短信
	 * 
	 * @param phoneNumber
	 * @param message
	 */
	public static void sendMesage(String phoneNumber, String message) {
		SmsManager smsManager = SmsManager.getDefault();
		smsManager.sendTextMessage(phoneNumber, null, message, null, null);
	}

	/**
	 * 锁屏
	 */
	public static void lockScreen(Context context) {
		DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		manager.resetPassword("23456", 0);// 重新设置密码
		manager.lockNow();// 锁屏
	}

	/**
	 * 恢复出厂设置(数据会全部清理) This will cause the device to reboot
	 * 
	 * @param context
	 */
	public static void resetPhone(Context context) {
		DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		manager.wipeData(0);
	}

	/**
	 * 播放警报音乐
	 * 
	 * @param context
	 * @param jxmzf
	 */
	public static void runAlarm(Context context, int resourceId) {
		MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceId);
		mediaPlayer.setVolume(1.0f, 1.0f);
		mediaPlayer.start();
	}

	/**
	 * 激活设备: 使当前应用能获取root权限
	 */
	public static void activeDevince(Context context) {
		DevicePolicyManager manager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName componentName = new ComponentName(context, MyAdminReceiver.class);
		if (!manager.isAdminActive(componentName)) {
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
			context.startActivity(intent);
		}
	}
}
