package com.atguigu.ms_git.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.engin.AddressService;
import com.atguigu.ms_git.util.SpUtils;

public class NumberAddressService extends Service {

	private WindowManager windowManager;
	private TelephonyManager telephonyManager;
	private View addressView;

	private PhoneStateListener listener = new PhoneStateListener() {
		public void onCallStateChanged(int state, String incomingNumber) {
			/*
			 * @see TelephonyManager#CALL_STATE_IDLE
			 * 
			 * @see TelephonyManager#CALL_STATE_RINGING
			 * 
			 * @see TelephonyManager#CALL_STATE_OFFHOOK
			 */
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 来电前或挂断电话后
				removeAdressView();
				break;
			case TelephonyManager.CALL_STATE_RINGING: // 响铃中
				showAddress(incomingNumber);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: // 通话中
				removeAdressView();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 显示来电归属地
	 * 
	 * @param incomingNumber
	 */
	private void showAddress(String incomingNumber) {

		String address = new AddressService().getAddress(incomingNumber);

		// 移除地址视图
		removeAdressView();

		// 创建显示的视图对象
		addressView = LayoutInflater.from(this).inflate(R.layout.show_location, null);
		setBgColor();

		TextView textView = (TextView) addressView.findViewById(R.id.tv_show_location);
		textView.setText(address);

		// 设置布局参数

		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		params.width = WindowManager.LayoutParams.WRAP_CONTENT; // 宽度自适应
		params.height = WindowManager.LayoutParams.WRAP_CONTENT; // 高度自适应
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;// 设置成半透明的
		params.type = WindowManager.LayoutParams.TYPE_TOAST; // toast类型

		// 主要是确定坐标系是从左上角开始的，不然呆会设置位置的时候有些麻烦
		params.gravity = Gravity.LEFT | Gravity.TOP;

		int x = SpUtils.getInstance(this).getInt(SpUtils.LOCATION_X, -1);
		int y = SpUtils.getInstance(this).getInt(SpUtils.LOCATION_Y, -1);
		if (x != -1) {
			params.x = x;
			params.y = y;
		}

		windowManager.addView(addressView, params);
	}

	/**
	 * 设置归属地视图的背景颜色
	 */
	private void setBgColor() {

		int index = SpUtils.getInstance(this).getInt(SpUtils.BG_COLOR_INDEX, -1);
		switch (index) {
		case 0:
			addressView.setBackgroundResource(R.drawable.call_locate_white);
			break;
		case 1:
			addressView.setBackgroundResource(R.drawable.call_locate_orange);
			break;

		case 2:
			addressView.setBackgroundResource(R.drawable.call_locate_green);
			break;

		case 3:
			addressView.setBackgroundResource(R.drawable.call_locate_blue);
			break;

		case 4:
			addressView.setBackgroundResource(R.drawable.call_locate_gray);
			break;
		default:
			break;
		}
	}

	/**
	 * 移除归属地显示视图
	 */
	private void removeAdressView() {
		if (addressView != null) {
			windowManager.removeView(addressView);
			addressView = null;
		}
	}

	@Override
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// Listen for changes to the device call state. 监听电话状态
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// Stop listening for updates.
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);
		removeAdressView();
	}

}
