package com.atguigu.ms_git.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.atguigu.ms_git.R;
import com.atguigu.security.dao.BlackNumberDao;

/**
 * 电话黑名单界面
 * 
 * @author xfzhang
 */
public class NumberSecurityActivity extends ListActivity {

	private List<String> numbers = new ArrayList<String>();
	private BlackNumberDao dao;
	private ArrayAdapter<String> adapter;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_number_security);
		
		init();
	}

	private void init() {
		
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText(R.string.numberSecurity);
		dao = new BlackNumberDao(this);
		numbers = dao.getAllNumbers();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				android.R.id.text1, numbers);
		setListAdapter(adapter);

		getListView().setOnCreateContextMenuListener(this);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		menu.add(1, 1, 1, R.string.update_number);
		menu.add(1, 2, 1, R.string.delete_number);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo();
		int position = (int) menuInfo.id;
		switch (item.getItemId()) {
		case 1:
			showDeleteDialog(position);
			break;
		case 2:
			dao.deleteNumber(numbers.get(position));
			numbers.remove(position);
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	private void showDeleteDialog(final int position) {
		final EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_PHONE);
		
		new AlertDialog.Builder(this)
			.setTitle("更新黑名单")
			.setView(editText)
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					String oldNumber = numbers.get(position);
					String newNumber = editText.getText().toString();
					dao.updateNumber(oldNumber, newNumber);
					numbers.set(position, newNumber);
					TextView tv = (TextView) getListView().getChildAt(position);
					tv.setText(newNumber);
				}
			})
			.setNegativeButton("取消", null)
			.show();
	}
	
	public void onClickAdd(View v) {
		
		final EditText editText = new EditText(this);
		editText.setInputType(InputType.TYPE_CLASS_PHONE);
		new AlertDialog.Builder(this)
		.setTitle("添加黑名单")
		.setView(editText)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String number = editText.getText().toString();
				dao.saveNumber(number);
				numbers.add(number);
				adapter.notifyDataSetChanged();
			}
		})
		.setNegativeButton("取消", null)
		.show();
	}
}
