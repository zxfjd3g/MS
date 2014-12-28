package com.atguigu.ms_git.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.atguigu.ms_git.bean.AppInfo;
import com.atguigu.ms_git.receiver.MyAdminReceiver;
import com.atguigu.ms_git.service.NumberAddressService;

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

	/**
	 * 判断某个service是否已经启动
	 * 
	 * @param context
	 * @param c
	 * @return
	 */
	public static boolean isServiceWorked(Context context, Class<NumberAddressService> c) {
		String className = c.getName();
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = activityManager.getRunningServices(30);
		for (RunningServiceInfo info : list) {
			String name = info.service.getClassName();
			if (className.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 得到所有应用信息的列表
	 * 
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public static Map<Boolean, List<AppInfo>> getAllAppInfos(Context context) throws Exception {

		Map<Boolean, List<AppInfo>> map = new HashMap<Boolean, List<AppInfo>>();
		List<AppInfo> allInfos = new ArrayList<AppInfo>();
		map.put(true, allInfos);
		List<AppInfo> customerInfos = new ArrayList<AppInfo>();// 只存储非系统的第三方应用
		map.put(false, customerInfos);

		PackageManager packageManager = context.getPackageManager();

		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> ResolveInfos = packageManager.queryIntentActivities(intent, 0);
		for (ResolveInfo ri : ResolveInfos) {
			String packageName = ri.activityInfo.packageName;
			Drawable icon = ri.loadIcon(packageManager);
			String appName = ri.loadLabel(packageManager).toString();
			boolean isSystemApp = isSystemApp(packageManager, packageName);
			AppInfo appInfo = new AppInfo(icon, appName, packageName, isSystemApp);
			allInfos.add(appInfo);
			if (!appInfo.isSystemApp()) {
				customerInfos.add(appInfo);
			}
		}
		return map;
	}

	/**
	 * 判断当前包所对应的应用是否是系统应用
	 * 
	 * @param packageName
	 * @return
	 * @throws NameNotFoundException
	 */
	private static boolean isSystemApp(PackageManager pm, String packageName) throws Exception {

		PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);

		return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
	}

	/**
	 * 将以内存大小值格式的友好的字符串
	 * 
	 * @param size
	 * @return
	 */
	public static String formatMemorySize(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if (size < 1024) {
			return size + "byte";
		} else if (size < (1 << 20)) // 左移20位，相当于1024 * 1024
		{
			float kSize = size >> 10; // 右移10位，相当于除以1024
			return formater.format(kSize) + "KB";
		} else if (size < (1 << 30)) // 左移30位，相当于1024 * 1024 * 1024
		{
			float mSize = size >> 20; // 右移20位，相当于除以1024再除以1024
			return formater.format(mSize) + "MB";
		} else if (size < (1 << 40)) {
			float gSize = size >> 30;
			return formater.format(gSize) + "GB";
		} else {
			return "size : error";
		}
	}
	
	public static void killAllProcess(Context context) {
		// 拿到这个包管理器
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 拿到所有正在运行的进程信息
		List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
		// 进行遍历，然后杀死它们
		for (RunningAppProcessInfo runningAppProcessInfo : list) {
			activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
		}
	}

	public static String getProcessCount(Context context) {
		// 拿到这个包管理器
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// 拿到所有正在运行的进程信息
		List<RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
		return list.size() + "";
	}

	public static String getAvailMemory(Context context) {
		// 拿到这个包管理器
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		// new一个内存的对象
		MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		// 拿到现在系统里面的内存信息
		activityManager.getMemoryInfo(memoryInfo);
		return MSUtils.formatMemorySize(memoryInfo.availMem);
	}
}
