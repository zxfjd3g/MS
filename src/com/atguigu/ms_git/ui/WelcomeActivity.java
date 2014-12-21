package com.atguigu.ms_git.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.api.APIClient;
import com.atguigu.ms_git.bean.UpdateInfo;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 欢迎界面
 * 
 * @author xfzhang 
 * 
 */
/**
 * 1. 整个界面显示一个透明度动画(持续3s, 从完全不透明到完全透明) 
 * 2. 显示手机中的当前应用的版本号 
 * 3.应用版本更新检查
 */
public class WelcomeActivity extends Activity {

	public static final int REQUEST_UPDATE_ERROR = 1; // 代表请求更新信息出错
	public static final int REQUEST_UDPATE_SUCCESS = 2; // 代表请求更新信息成功
	protected static final int DOWNLOAD_APK_ERROR = 3; //代表下载最新版本APK出错
	protected static final int DOWNLOAD_APK_SUCCESS = 4;//代表下载最新版本APK成功

	private LinearLayout ll_welcome_root;
	private TextView tv_welcome_version;
	
	private UpdateInfo updateInfo; //服务器端最新版本的信息对象
	private String version;//当前应用的版本号
	private ProgressDialog pd; //显示下载进度
	private File apkFile; //用来保存apk的file
	

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REQUEST_UPDATE_ERROR:
				MSUtils.showMsg(WelcomeActivity.this, "请求最新apk网络异常!");
				toMain();
				break;
			case REQUEST_UDPATE_SUCCESS:
				if(updateInfo.getVersion().equals(version)) {//当前就为最新版本
					MSUtils.showMsg(WelcomeActivity.this, "当前是最新版本!");
					toMain();
				} else {
					showDowloadDialog();//显示提示下载最新版本的dialog
				}
				break;
			case DOWNLOAD_APK_ERROR:
				MSUtils.showMsg(WelcomeActivity.this, "下载最新apk网络异常!");
				toMain();
				break;
			case DOWNLOAD_APK_SUCCESS:
				MSUtils.installAPK(WelcomeActivity.this, apkFile);
				finish();
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);

		init();
	}

	/**
	 * 显示提示下载最新版本的dialog
	 */
	private void showDowloadDialog() {
		new AlertDialog.Builder(this).setTitle("版本更新").setMessage(updateInfo.getDesc())
				.setPositiveButton("下载", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//开始下载apk
						startDownloadApk();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						toMain();
					}
				})
				.setCancelable(false)
				.show();
	}

	/**
	 * 开始下载apk(开启分线程)
	 */
	private void startDownloadApk() {
		//下载的url
		final String apkUrl = updateInfo.getApkUrl();
		
		//准备用来保存apk的File对象
		apkFile = createApkFile();
		
		//显示一个dialog提示下载
		showDownloadProgress();
		
		//启动分线程下载
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					APIClient.downloadAPK(apkUrl, apkFile, pd);
					handler.sendEmptyMessage(DOWNLOAD_APK_SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(DOWNLOAD_APK_ERROR);
				}
			}
		}).start();
	}

	/**
	 * 显示一个dialog提示下载
	 */
	private void showDownloadProgress() {
		pd = new ProgressDialog(this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setTitle("下载更新");
		pd.setCancelable(false);
		pd.show();
	}

	/**
	 * 准备用来保存apk的File对象
	 * @return
	 */
	private File createApkFile() {
		File file = null;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File dir = getExternalFilesDir(null);
			file = new File(dir, "Security.apk");
		} else {
			File dir = getFilesDir();
			file = new File(dir, "Security.apk");
		}
		return file;
	}

	/*
	 * 初始化
	 */
	private void init() {
		ll_welcome_root = (LinearLayout) findViewById(R.id.ll_welcome_root);
		tv_welcome_version = (TextView) findViewById(R.id.tv_welcome_version);

		// 1. 显示透明度动画
		showAnimation();

		// 2. 显示手机中的当前应用的版本号
		showVersion();

		// 3. 应用版本更新检查
		checkUpdate();
	}

	/**
	 * 检查应用版本更新
	 */

	private void checkUpdate() {
		// 检查手机是否联网?
		boolean isConnected = MSUtils.isNetConnected(this);
		if (!isConnected) {// 如果没有联网, 显示提示
			MSUtils.showMsg(this, "手机没有连接网络!");
			toMain();
			return;
		}
		// 联网了, 开启分线程获取服务器端的最新版本信息数据
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					updateInfo = APIClient.getUpdateInfo();
					handler.sendEmptyMessage(REQUEST_UDPATE_SUCCESS);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(REQUEST_UPDATE_ERROR);
				}
			}
		}).start();
	}

	/**
	 * 进入主界面
	 */
	private void toMain() {
		finish();
		startActivity(new Intent(this, MainActivity.class));
	}

	/**
	 * 显示手机中的当前应用的版本号
	 */

	private void showVersion() {
		version = MSUtils.getVersion(this);
		tv_welcome_version.setText("当前版本: " + version);
	}

	/**
	 * 整个界面显示一个透明度动画(持续3s, 从完全不透明到完全透明)
	 */
	private void showAnimation() {
		AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(3000);
		ll_welcome_root.startAnimation(animation);
	}
}
