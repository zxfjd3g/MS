package com.atguigu.ms_git.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.bean.TaskInfo;
import com.atguigu.ms_git.engin.TaskInfoProvider;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 任务管理
 * 
 * @author Administrator
 * 
 */
public class AppTaskManagerActivity extends Activity implements OnItemClickListener {

	private TextView tv_process_count;
	private TextView tv_process_memory;
	private ListView lv_process_list;
	private Map<Boolean, List<TaskInfo>> map;
	private AppTaskAdapter adapter;
	private LinearLayout ll_process_load;
	private TaskInfoProvider provider;
	private ActivityManager am;
	private long availMemory;
	private long appMemory;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_manager);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		tv_process_count = (TextView) findViewById(R.id.tv_process_count);
		tv_process_memory = (TextView) findViewById(R.id.tv_process_memory);
		lv_process_list = (ListView) findViewById(R.id.lv_process_list);
		lv_process_list.setOnItemClickListener(this);
		ll_process_load = (LinearLayout) findViewById(R.id.ll_process_load);
		provider = new TaskInfoProvider(this);
		adapter = new AppTaskAdapter();
		am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		loadData();
	}

	private void loadData() {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				ll_process_load.setVisibility(View.VISIBLE);
			}

			@Override
			protected Void doInBackground(Void... params) {
				map = provider.getAll();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				ll_process_load.setVisibility(View.GONE);
				lv_process_list.setAdapter(adapter);
				availMemory = getAvailMemory();
				appMemory = getAppMemory();
				
				updateTitle();
			}
		}.execute();
	}

	/**
	 * 得到应用占用的总内存
	 * 
	 * @return
	 */
	private long getAppMemory() {
		long total = 0;
		List<TaskInfo> list = map.get(false);
		for (TaskInfo info : list) {
			total += info.getMemory();
		}
		list = map.get(true);
		for (TaskInfo info : list) {
			total += info.getMemory();
		}
		return total;
	}

	/**
	 * 得到可用的内存大小
	 * 
	 * @return
	 */
	protected long getAvailMemory() {

		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availMem = outInfo.availMem;
		return availMem;
	}

	private class AppTaskAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return map.get(false).size() + map.get(true).size() + 2;
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return "customer"; // 标识为用户应用进程头
			} else if (position > 0 && position <= map.get(false).size()) {
				return map.get(false).get(position - 1);
			} else if (position == map.get(false).size() + 1) {
				return "system";// 标识为系统应用进程头
			} else {
				return map.get(true).get(position - map.get(false).size() - 2);
			}
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Object data = getItem(position);
			if (data.equals("customer")) {
				return createTextView("用户应用进程数: " + map.get(false).size());
			} else if (data.equals("system")) {
				return createTextView("系统应用进程数: " + map.get(true).size());
			} else if (data instanceof TaskInfo) {
				// 准备视图对象
				ViewHolder viewHolder = null;
				if (convertView == null || convertView instanceof TextView) {// 注意要加上后面的条件
					convertView = View.inflate(AppTaskManagerActivity.this, R.layout.task_manager_item, null);
					viewHolder = new ViewHolder();
					viewHolder.iconIV = (ImageView) convertView.findViewById(R.id.iv_process_manager_icon);
					viewHolder.nameTV = (TextView) convertView.findViewById(R.id.tv_process_manager_name);
					viewHolder.memoryTV = (TextView) convertView.findViewById(R.id.tv_process_manager_memory);
					viewHolder.clearCB = (CheckBox) convertView.findViewById(R.id.cb_process_manager_state);
					convertView.setTag(viewHolder);
				} else {
					viewHolder = (ViewHolder) convertView.getTag();
				}
				// 设置显示数据
				TaskInfo info = (TaskInfo) data;
				viewHolder.iconIV.setImageDrawable(info.getIcon());
				viewHolder.nameTV.setText(info.getName());
				viewHolder.memoryTV.setText("占用内存: " + MSUtils.formatMemorySize(info.getMemory()));

				// 隐藏当前应用和系统一此进程的checkbox
				String packageName = info.getPackageName();
				if (getPackageName().equals(packageName) || "system".equals(packageName)) {
					viewHolder.clearCB.setChecked(false);
					viewHolder.clearCB.setVisibility(View.GONE);
				} else {
					viewHolder.clearCB.setChecked(info.isChecked());
				}

				return convertView;
			} else {
				Log.e("TAG", "出现......");
				return null;
			}

		}

		private TextView createTextView(String content) {
			TextView textView = new TextView(AppTaskManagerActivity.this);
			textView.setText(content);
			textView.setTextSize(20);
			textView.setTextColor(Color.BLACK);
			textView.setBackgroundColor(Color.GRAY);
			return textView;
		}
	}

	private class ViewHolder {
		public ImageView iconIV;
		public TextView nameTV;
		public TextView memoryTV;
		public CheckBox clearCB;

	}

	/**
	 * 点击清理按钮
	 * 
	 * @param view
	 */
	public void onClickClear(View view) {

		int killedCount = 0;
		int killedMemory = 0;
		List<Integer> positions = new ArrayList<Integer>();
		
		List<TaskInfo> list = map.get(false);
		for (int i=0;i<list.size();i++) {
			TaskInfo info = list.get(i);
			if (info.isChecked()) {
				positions.add(i);
				am.killBackgroundProcesses(info.getPackageName());
				killedCount++;
				killedMemory += info.getMemory();
			}
		}
		for(int position : positions) {
			list.remove(position);
		}
		
		positions.clear();
		
		list = map.get(true);
		for (int i=0;i<list.size();i++) {
			TaskInfo info = list.get(i);
			if (info.isChecked()) {
				positions.add(i);
				am.killBackgroundProcesses(info.getPackageName());
				killedCount++;
				killedMemory += info.getMemory();
			}
		}
		for(int position : positions) {
			list.remove(position);
		}
		
		adapter.notifyDataSetChanged();
		MSUtils.showMsg(this, "清除"+killedCount+"个进程, 释放"+MSUtils.formatMemorySize(killedMemory)+"内存");
		availMemory += killedMemory;
		appMemory -= killedMemory;
		updateTitle();
	}

	

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Object item = adapter.getItem(position);
		if (item instanceof TaskInfo) {
			TaskInfo info = (TaskInfo) item;
			ViewHolder viewHolder = (ViewHolder) view.getTag();
			viewHolder.clearCB.toggle();// 切换checkbox的选中状态
			info.setChecked(viewHolder.clearCB.isChecked());// 同步更新对应数据对象中的checked属性值
		}
	}

	/**
	 * 更新标题显示
	 */
	private void updateTitle() {
		tv_process_memory.setText(MSUtils.formatMemorySize(availMemory) + " / " + MSUtils.formatMemorySize(appMemory));
		tv_process_count.setText((map.get(false).size() + map.get(true).size()) + "个进程");
	}
}
