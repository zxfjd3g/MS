package com.atguigu.ms_git.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.TrafficStats;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.ms_git.bean.TrafficInfo;
import com.atguigu.ms_git.engin.TrafficInfoProvider;
import com.atguigu.ms_git.util.MSUtils;

/**
 * 流量管理
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("deprecation")
public class TrafficManagerActivity extends Activity {

	private TextView tv_title;
	private TextView tv_traffic_2g_3g;
	private TextView tv_traffic_wifi;

	private SlidingDrawer drawer;
	private ListView lv_traffic_content;
	private List<TrafficInfo> trafficInfoList;
	private TrafficAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_traffic_manager);

		init();
	}

	private void init() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.traffic_manager);
		tv_traffic_2g_3g = (TextView) findViewById(R.id.tv_traffic_2g_3g);
		tv_traffic_wifi = (TextView) findViewById(R.id.tv_traffic_wifi);
		lv_traffic_content = (ListView) findViewById(R.id.lv_traffic_content);
		drawer = (SlidingDrawer) findViewById(R.id.sd_traffic);
		loadData();
	}

	private ProgressDialog pd;

	private void loadData() {
		new AsyncTask<Void, Void, Void>() {

			protected void onPreExecute() {
				pd = ProgressDialog.show(TrafficManagerActivity.this, null, "初始化中...");
			}

			@Override
			protected Void doInBackground(Void... params) {
				trafficInfoList = new TrafficInfoProvider(TrafficManagerActivity.this).getAll();
				return null;
			}

			protected void onPostExecute(Void result) {
				pd.dismiss();
				adapter = new TrafficAdapter();
				lv_traffic_content.setAdapter(adapter);
				drawer.animateOpen();
				showTotalTraffic();
			}
		}.execute();
	}

	/**
	 * 显示总流量
	 */
	private void showTotalTraffic() {
		long totalRxBytes = TrafficStats.getTotalRxBytes();
		long totalTxBytes = TrafficStats.getTotalTxBytes();
		long totalBytes = totalRxBytes + totalTxBytes;

		long mobileRxBytes = TrafficStats.getMobileRxBytes();
		long mobileTxBytes = TrafficStats.getMobileTxBytes();
		long totalMobileBytes = mobileRxBytes + mobileTxBytes;

		long totalWifiBytes = totalBytes - totalMobileBytes;

		tv_traffic_2g_3g.setText(MSUtils.formatMemorySize(totalMobileBytes));
		tv_traffic_wifi.setText(MSUtils.formatMemorySize(totalWifiBytes));
	}

	private class TrafficAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return trafficInfoList.size();
		}

		@Override
		public Object getItem(int position) {
			return trafficInfoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = View.inflate(TrafficManagerActivity.this, R.layout.traffic_manager_item, null);
				viewHolder = new ViewHolder();
				viewHolder.iv_traffic_icon = (ImageView) convertView.findViewById(R.id.iv_traffic_icon);
				viewHolder.tv_traffic_name = (TextView) convertView.findViewById(R.id.tv_traffic_name);
				viewHolder.tv_traffic_transmitted = (TextView) convertView.findViewById(R.id.tv_traffic_transmitted);
				viewHolder.tv_traffic_received = (TextView) convertView.findViewById(R.id.tv_traffic_received);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			TrafficInfo trafficInfo = trafficInfoList.get(position);
			viewHolder.iv_traffic_icon.setImageDrawable(trafficInfo.getIcon());
			viewHolder.tv_traffic_name.setText(trafficInfo.getName());
			viewHolder.tv_traffic_received.setText(MSUtils.formatMemorySize(trafficInfo.getInSize()));
			viewHolder.tv_traffic_transmitted.setText(MSUtils.formatMemorySize(trafficInfo.getOutSize()));

			return convertView;
		}
	}

	private class ViewHolder {
		public ImageView iv_traffic_icon;
		public TextView tv_traffic_name;
		public TextView tv_traffic_transmitted;
		public TextView tv_traffic_received;
	}
}
