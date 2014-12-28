package com.atguigu.ms_git.bean;

import android.graphics.drawable.Drawable;

public class TrafficInfo {

	private String name;
	private Drawable icon;
	private int uid;

	private long inSize; // 下载流量大小
	private long outSize; // 上传流量大小

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public long getInSize() {
		return inSize;
	}

	public void setInSize(long inSize) {
		if(inSize==-1)
			inSize = 0;
		this.inSize = inSize;
	}

	public long getOutSize() {
		return outSize;
	}

	public void setOutSize(long outSize) {
		if(outSize==-1)
			outSize = 0;
		this.outSize = outSize;
	}

	@Override
	public String toString() {
		return "TrafficInfo [name=" + name + ", icon=" + icon + ", uid=" + uid + ", inSize=" + inSize + ", outSize="
				+ outSize + "]";
	}

}
