package com.atguigu.ms_git.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.util.GpsUtils;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 监听接收到短信广播的receiver
 * 
 * @author Administrator
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		
		Log.i("TAG", "SmsReceiver onReceive()");
		Bundle bundle = intent.getExtras();
		Object[] pdus = (Object[]) bundle.get("pdus");
		byte[] pdu = (byte[]) pdus[0];

		SmsMessage smsMessage = SmsMessage.createFromPdu(pdu);

		String phoneNumber = smsMessage.getOriginatingAddress();
		String message = smsMessage.getMessageBody();
		if ("#location#".equals(message)) {// gps追踪
			abortBroadcast();
			Log.e("TAG", "回送位置信息!");
			GpsUtils.getInstance().sendLocation(context, phoneNumber);
		} else if ("#reset#".equals(message)) {// 远程销毁数据(恢复出厂模式)
			abortBroadcast();
			Log.e("TAG", "远程销毁数据!");
			MSUtils.resetPhone(context);
		} else if ("#lock#".equals(message)) {// 远程锁屏
			abortBroadcast();
			Log.e("TAG", "远程锁屏!");
			MSUtils.lockScreen(context);
		} else if ("#alarm#".equals(message)) {// 报警音乐
			abortBroadcast();
			Log.e("TAG", "报警音乐!");
			MSUtils.runAlarm(context, R.raw.alarm);
		}
	}
}
