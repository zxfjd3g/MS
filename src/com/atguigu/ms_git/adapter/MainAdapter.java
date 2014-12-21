package com.atguigu.ms_git.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.atguigu.ms_git.R;

public class MainAdapter extends BaseAdapter {

	private static final String[] NAMES = new String[] { "手机防盗", "通讯卫士", "软件管理", "流量管理", "任务管理", "手机杀毒", "系统优化",
			"高级工具", "设置中心" };

	private static final int[] ICONS = new int[] { R.drawable.widget01, R.drawable.widget02, R.drawable.widget03,
			R.drawable.widget04, R.drawable.widget05, R.drawable.widget06, R.drawable.widget07, R.drawable.widget08,
			R.drawable.widget09 };

	private Context context;
	private SharedPreferences sp;

	public MainAdapter(Context context) {
		this.context = context;
		sp = context.getSharedPreferences("security", Context.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return NAMES.length;
	}

	@Override
	public Object getItem(int position) {
		return NAMES[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.main_item, null);

			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_main_icon);
			holder.textView = (TextView) convertView.findViewById(R.id.tv_item_main_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.imageView.setImageResource(ICONS[position]);
		holder.textView.setText(NAMES[position]);
		
		if(position==0) {
			String lostName = sp.getString("lost_name", null);
			if(lostName!=null) {
				holder.textView.setText(lostName);
			}
		}

		return convertView;
	}

	class ViewHolder {
		public ImageView imageView;
		public TextView textView;
	}
}
