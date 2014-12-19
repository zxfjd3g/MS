package com.atguigu.ms_git.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;

import com.atguigu.ms_git.bean.UpdateInfo;
import com.atguigu.ms_git.util.Constant;

public class APIClient {

	/**
	 * 得到最新版本的相关信息对象
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	public static UpdateInfo getUpdateInfo() throws Exception {

		URL url = new URL(Constant.URL_CHECK_APK_UPDATE_XML);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setConnectTimeout(5000);
		InputStream is = connection.getInputStream();
		UpdateInfo info = parseXmlToInfo(is);

		is.close();
		connection.disconnect();

		return info;
	}

	/**
	 * 解析包含xml数据的流
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private static UpdateInfo parseXmlToInfo(InputStream is) throws Exception {
		UpdateInfo info = new UpdateInfo();
		XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
		pullParser.setInput(is, "utf-8");
		int eventType = pullParser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			if (eventType == XmlPullParser.START_TAG) {
				String tagName = pullParser.getName();
				if ("version".equals(tagName)) {
					info.setVersion(pullParser.nextText());
				} else if ("apkUrl".equals(tagName)) {
					info.setApkUrl(pullParser.nextText());
				} else if ("desc".equals(tagName)) {
					info.setDesc(pullParser.nextText());
				}
			}
			eventType = pullParser.next();
		}
		return info;
	}

	/**
	 * 下载apk, 保存到指定文件中,  并显示下载进度
	 * @param apkUrl
	 * @param apkFile
	 * @param pd
	 * @throws Exception 
	 */
	public static void downloadAPK(String apkUrl, File apkFile, ProgressDialog pd) throws Exception {
		URL url = new URL(apkUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setConnectTimeout(5000);
		InputStream is = connection.getInputStream();
		pd.setMax(is.available());
		OutputStream os = new FileOutputStream(apkFile);
		byte[] buffer = new byte[1024];
		int len = -1;
		while((len=is.read(buffer))>0) {
			os.write(buffer, 0, len);
			pd.incrementProgressBy(len);
		}
		os.close();
		is.close();
		connection.disconnect();
	}
}
