package com.atguigu.ms_git.bean;

import android.graphics.drawable.Drawable;

/**
 * 应用任务信息
 * 
 * @author Administrator
 * 
 */
public class TaskInfo {

	private int id;
	private String name;
	private String packageName;
	private Drawable icon;
	private int memory;
	private boolean checked;

	public TaskInfo() {
		super();
	}

	public TaskInfo(int id, String name, String packageName, Drawable icon, int memory, boolean checked) {
		super();
		this.id = id;
		this.name = name;
		this.packageName = packageName;
		this.icon = icon;
		this.memory = memory;
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	@Override
	public String toString() {
		return "TaskInfo [id=" + id + ", name=" + name + ", packageName=" + packageName + ", icon=" + icon
				+ ", memory=" + memory + ", checked=" + checked + "]";
	}

}
