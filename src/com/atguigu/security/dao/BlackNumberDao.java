package com.atguigu.security.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 黑名单Dao
 * 
 * @author xfzhang
 * 
 */
public class BlackNumberDao {

	private DBHelper dbHelper;

	public BlackNumberDao(Context context) {
		dbHelper = new DBHelper(context);
	}

	public void saveNumber(String number) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		database.insert(DBHelper.BLACK_NAME, null, values);
		database.close();
	}

	public void deleteNumber(String number) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		database.delete(DBHelper.BLACK_NAME, "number=?", new String[] { number });
		database.close();
	}

	public void updateNumber(String oldNumber, String newNumber) {
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", newNumber);
		database.update(DBHelper.BLACK_NAME, values, "number=?", new String[] { oldNumber });
		database.close();
	}

	public List<String> getAllNumbers() {
		List<String> list = new ArrayList<String>();
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "select number from " + DBHelper.BLACK_NAME;
		Cursor cursor = database.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			String number = cursor.getString(0);
			list.add(number);
		}
		cursor.close();
		database.close();
		return list;
	}

	public boolean isBlack(String number) {
		boolean isBlack = false;
		SQLiteDatabase database = dbHelper.getWritableDatabase();
		String sql = "select count(*) from " + DBHelper.BLACK_NAME + " where number=?";
		Cursor cursor = database.rawQuery(sql, new String[] { number });
		if (cursor.moveToNext()) {
			isBlack = cursor.getInt(0) > 0;
		}
		cursor.close();
		database.close();
		return isBlack;
	}

}
