package com.atguigu.ms_git.ui;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.bean.CacheInfo;
import com.atguigu.ms_git.engin.CacheInfoProvider;

public class CacheClearActivity extends ListActivity implements OnItemClickListener {

	private TextView tv_title;
	public static final int FINISH = 1;
	private LinearLayout ll_cache_clear_load;
	private CacheInfoProvider provider;
	private List<CacheInfo> cacheInfos;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == FINISH) {
				ll_cache_clear_load.setVisibility(View.GONE);
				cacheInfos = provider.getCacheInfos();
				setListAdapter(new CacheClearAdater());
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cache_clear);

		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.cache_clear);
		ll_cache_clear_load = (LinearLayout) findViewById(R.id.ll_cache_clear_load);
		ll_cache_clear_load.setVisibility(View.VISIBLE);
		provider = new CacheInfoProvider(this);
		provider.loadData(handler);
		getListView().setOnItemClickListener(this);
	}

	private class CacheClearAdater extends BaseAdapter {

		@Override
		public int getCount() {
			return cacheInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return cacheInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(CacheClearActivity.this, R.layout.cache_clear_item, null);
				viewHolder = new ViewHolder();
				viewHolder.iconIV = (ImageView) convertView.findViewById(R.id.iv_item_cache_icon);
				viewHolder.nameTV = (TextView) convertView.findViewById(R.id.tv_item_cache_name);
				viewHolder.codeSizeTV = (TextView) convertView.findViewById(R.id.tv_item_cache_code);
				viewHolder.dataSizeTV = (TextView) convertView.findViewById(R.id.tv_item_cache_data);
				viewHolder.cacheSizeTV = (TextView) convertView.findViewById(R.id.tv_item_cache_cache);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			CacheInfo cacheInfo = cacheInfos.get(position);
			viewHolder.iconIV.setImageDrawable(cacheInfo.getIcon());
			viewHolder.nameTV.setText(cacheInfo.getName());
			viewHolder.codeSizeTV.setText(cacheInfo.getCodeSize());
			viewHolder.dataSizeTV.setText(cacheInfo.getDataSize());
			viewHolder.cacheSizeTV.setText(cacheInfo.getCacheSize());

			return convertView;
		}
	}

	private class ViewHolder {
		public ImageView iconIV;
		public TextView nameTV;
		public TextView codeSizeTV;
		public TextView dataSizeTV;
		public TextView cacheSizeTV;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent();
		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + cacheInfos.get(position).getPackageName()));
		startActivity(intent);
	}
}
