package com.atguigu.security.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {

	private DBHelper dbHelper;

	public AppLockDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public List<String> getAll() {
		List<String> list = new ArrayList<String>();

		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select packagename from applock", null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		database.close();

		return list;
	}

	public void add(String packageName) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("insert into applock(packagename) values(?)", new String[] { packageName });
		database.close();
	}

	public void delete(String packageName) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.execSQL("delete from applock where packagename=?", new String[] { packageName });
		database.close();
	}

	public boolean exists(String packagename) {
		boolean exist = false;
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		Cursor cursor = database.rawQuery("select count(*) from applock where packagename=?",
				new String[] { packagename });
		if (cursor.moveToNext()) {
			exist = cursor.getLong(0) > 0;
		}
		cursor.close();
		database.close();
		return exist;
	}
}
