package com.atguigu.ms_git.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import com.atguigu.ms_git.service.AppLockMonitorService;
import com.atguigu.ms_git.util.MSUtils;
import com.atguigu.ms_git.util.SpUtils;

/**
 * 监听开机完成的Receiver 用来检查SIM卡是否已经变更, 如果已经变了, 发信息给保存的安全号手机
 * 
 * @author Administrator
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i("TAG", "BootCompleteReceiver onReceive()");
		
		boolean isAppLockStart = SpUtils.getInstance(context).getBoolean(SpUtils.APP_LOCK, false);
		if (isAppLockStart) {
			Intent service =  new Intent(context, AppLockMonitorService.class);
			service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startService(service );
		}
		// 条件1: 启动了防盗监听
		boolean isProtected = SpUtils.getInstance(context).getBoolean(SpUtils.IS_PROTECTED, false);
		Log.i("TAG", "BootCompleteReceiver isProtected "+isProtected );
		if (!isProtected)
			return;
		// 条件2: 当前手机SIM号与保存的SIM号不一致
		String currSimNumber = MSUtils.getSimNumber(context);
		String simNumber = SpUtils.getInstance(context).getString(SpUtils.SIM, null);
		Log.e("TAG", "currSimNumber="+currSimNumber+" simNumber="+currSimNumber );
		
		if (simNumber != null && simNumber.equals(currSimNumber))
			return;
		// 发送一个提示短信给保存的安全号
		String number = SpUtils.getInstance(context).getString(SpUtils.NUMBER, null);
		Log.e("TAG", "number="+number);
		if (!TextUtils.isEmpty(number)) {
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(number, null, "SIM卡有变更, 手机可能被盗了!", null, null);
		}
	}
}
