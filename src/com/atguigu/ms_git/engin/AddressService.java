package com.atguigu.ms_git.engin;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class AddressService {
	public String getAddress(String number) {

		String pattern = "^1[3458]\\d{9}$";
		String address = number;
		if (number.matches(pattern)) {// 手机号码
			address = query("select cardtype from info where mobileprefix = ? ",
					new String[] { number.substring(0, 7) });
			if (address.equals("")) {
				address = number;
			}
		} else {// 固定电话
			int len = number.length();
			switch (len) {
			case 4: // 模拟器
				address = "模拟器";
				break;

			case 7: // 本地号码
				address = "本地号码";
				break;

			case 8: // 本地号码
				address = "本地号码";
				break;

			case 10: // 3位区号+7位号码
				address = query("select city from info where area = ? limit 1", new String[] { number.substring(0, 3) });
				if (address.equals("")) {
					address = number;
				}
				break;

			case 11: // 3位区号+8位号码 或 4位区号+7位号码
				address = query("select city from info where area = ? limit 1", new String[] { number.substring(0, 3) });
				if (address.equals("")) {
					address = query("select city from info where area = ? limit 1",
							new String[] { number.substring(0, 4) });
					if (address.equals("")) {
						address = number;
					}
				}
				break;

			case 12: // 4位区号+8位号码
				address = query("select city from info where area = ? limit 1", new String[] { number.substring(0, 4) });
				if (address.equals("")) {
					address = number;
				}
				break;

			default:
				break;
			}
		}
		return address;
	}

	private String query(String sql, String[] selectionArgs) {
		String result = "";
		String path = Environment.getExternalStorageDirectory() + "/security/db/address.db";
		Log.e("TAG", "dbPath=" + path);
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(path, null);
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(sql, selectionArgs);
			if (cursor.moveToNext()) {
				result = cursor.getString(0);
			}
			cursor.close();
			db.close();
		}
		return result;
	}
}
