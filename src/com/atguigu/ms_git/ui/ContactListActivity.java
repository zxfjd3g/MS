package com.atguigu.ms_git.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.atguigu.ms_git.R;
/**
 * 联系人列表
 * @author xfzhang
 *
 */
public class ContactListActivity extends Activity implements OnItemClickListener {

	private ListView lv_person_list;
	private List<Map<String, String>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_person_list);

		lv_person_list = (ListView) findViewById(R.id.lv_person_list);
		data = getData();
		String[] from = { Phone.DISPLAY_NAME, Phone.DATA1 };
		int[] to = { R.id.tv_item_name, R.id.tv_item_number };

		lv_person_list.setAdapter(new SimpleAdapter(this, data, R.layout.person_item, from, to));

		lv_person_list.setOnItemClickListener(this);
	}

	private List<Map<String, String>> getData() {

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		ContentResolver resolver = getContentResolver();
		String[] projection = { Phone.DISPLAY_NAME, Phone.DATA1 };
		Cursor cursor = resolver.query(Phone.CONTENT_URI, projection, null, null, null);
		while (cursor.moveToNext()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put(Phone.DISPLAY_NAME, cursor.getString(0));
			map.put(Phone.DATA1, cursor.getString(1));
			list.add(map);
		}
		cursor.close();
		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String number = data.get(position).get(Phone.DATA1);
		Intent data = getIntent();
		data.putExtra("number", number);
		setResult(2, data);
		finish();
	}
}
