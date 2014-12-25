package com.atguigu.ms_git.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.util.Log;

import com.atguigu.ms_git.bean.UpdateInfo;
import com.atguigu.ms_git.util.Constant;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 与服务器交互的客户端工具类
 * 
 * @author xfzhang
 * 
 */
@SuppressLint("NewApi")
public class APIClient {

	/**
	 * 得到最新版本的相关信息对象
	 * 
	 * @param urlPath
	 * @return
	 * @throws Exception
	 */
	public static UpdateInfo getUpdateInfo() throws Exception {

		// URL url = new URL(Constant.URL_CHECK_APK_UPDATE_XML);
		URL url = new URL(Constant.URL_CHECK_APK_UPDATE_JSON);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setConnectTimeout(5000);
		InputStream is = connection.getInputStream();
		// UpdateInfo info = parseXmlToInfo(is);
		UpdateInfo info = parseJsonToInfo(is);
		is.close();
		connection.disconnect();

		return info;
	}

	/**
	 * 解析Json数据注封装为inof对象
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	private static UpdateInfo parseJsonToInfo(InputStream is) throws Exception {
		String jsonString = MSUtils.readString(is);
		JSONObject jsonObject = new JSONObject(jsonString);
		String version = jsonObject.getString("version");
		String apkUrl = jsonObject.getString("apkUrl");
		String desc = jsonObject.getString("desc");
		return new UpdateInfo(version, apkUrl, desc);
	}

	/**
	 * 解析包含xml数据的流
	 * 
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
	 * 下载apk, 保存到指定文件中, 并显示下载进度
	 * 
	 * @param apkUrl
	 * @param file
	 * @param pd
	 * @throws Exception
	 */
	public static void downloadFile(String apkUrl, File file, ProgressDialog pd) throws Exception {
		Log.i("TAG", "apkUrl=" + apkUrl);
		URL url = new URL(apkUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setConnectTimeout(5000);
		int responseCode = connection.getResponseCode();
		if(responseCode==200) {
			InputStream is = connection.getInputStream();
			int total = connection.getContentLength();
			Log.e("TAG", "total="+total);
			pd.setMax(total);
			OutputStream os = new FileOutputStream(file);
			byte[] buffer = new byte[2048];
			int len = -1;
			while ((len = is.read(buffer)) > 0) {
				os.write(buffer, 0, len);
				pd.incrementProgressBy(len);
				//模拟网速慢
				//Thread.sleep(100);
			}
			os.close();
			is.close();
		}
		pd.dismiss();
		connection.disconnect();
	}
}
