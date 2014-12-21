package com.atguigu.ms_git.bean;

/*
 {
 version:"1.1", 
 apkUrl:"http://192.168.173.1:8080/SecurityServer/Security.apk", 
 desc:"解决了上一个版本中的不少bug, 优化了网络请求逻辑"
 }

 */
public class UpdateInfo {

	private String version;
	private String apkUrl;
	private String desc;

	public UpdateInfo() {
		super();
	}

	public UpdateInfo(String version, String apkUrl, String desc) {
		super();
		this.version = version;
		this.apkUrl = apkUrl;
		this.desc = desc;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "UpdateInfo [version=" + version + ", apkUrl=" + apkUrl + ", desc=" + desc + "]";
	}
}
