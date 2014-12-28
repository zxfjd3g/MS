package com.atguigu.ms_git.engin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.util.Log;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.bean.TaskInfo;

@SuppressLint("NewApi")
public class TaskInfoProvider {

	private PackageManager pm;
	private ActivityManager am;
	private Drawable defaultDrawable;
	private List<RunningAppProcessInfo> runningAppProcesses;

	public TaskInfoProvider(Context context) {
		defaultDrawable = context.getResources().getDrawable(R.drawable.logo);
		pm = context.getPackageManager();
		am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		runningAppProcesses = am.getRunningAppProcesses();
	}

	public Map<Boolean, List<TaskInfo>> getAll() {
		Map<Boolean, List<TaskInfo>> map = new HashMap<Boolean, List<TaskInfo>>();
		List<TaskInfo> customerTaskList = new ArrayList<TaskInfo>();
		map.put(false, customerTaskList);
		List<TaskInfo> systemTaskList = new ArrayList<TaskInfo>();
		map.put(true, systemTaskList);

		for (RunningAppProcessInfo info : runningAppProcesses) {

			TaskInfo taskInfo = new TaskInfo();
			taskInfo.setId(info.pid);
			String packageName = info.processName;
			taskInfo.setPackageName(packageName);

			boolean systemApp = true;
			try {
				ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
				String name = applicationInfo.loadLabel(pm).toString();
				taskInfo.setName(name);
				Drawable icon = applicationInfo.loadIcon(pm);//不是loadLogo()
				taskInfo.setIcon(icon);
				systemApp = isSystemApp(pm, packageName);

			} catch (Exception e) {
				// e.printStackTrace();
				Log.i("TAG", "读到一个没有名称的应用 " + packageName);
				taskInfo.setName(packageName);
				taskInfo.setIcon(defaultDrawable);
			}

			int memory = getAppMemory(taskInfo.getId());
			taskInfo.setMemory(memory);
			if (systemApp) {
				systemTaskList.add(taskInfo);
			} else {
				customerTaskList.add(taskInfo);
			}
		}
		return map;
	}

	/**
	 * 根据进程id得到进程占用的内存
	 * 
	 * @param id
	 * @return
	 */
	private int getAppMemory(int id) {
		MemoryInfo[] infos = am.getProcessMemoryInfo(new int[] { id });
		int memory = infos[0].getTotalPrivateDirty();
		return memory;
	}

	/**
	 * 判断当前包所对应的应用是否是系统应用
	 * 
	 * @param packageName
	 * @return
	 * @throws NameNotFoundException
	 */
	private boolean isSystemApp(PackageManager pm, String packageName) throws Exception {

		PackageInfo packageInfo = pm.getPackageInfo(packageName, 0);
		return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0;
	}
}
