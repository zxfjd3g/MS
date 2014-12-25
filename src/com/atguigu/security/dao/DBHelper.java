package com.atguigu.security.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String BLACK_NAME = "blacknumber";
	private static int version = 1;
	public DBHelper(Context context) {
		super(context, "security.db", null, version);
	}
	
	public DBHelper(Context context, int version) {
		super(context, "security.db", null, version);
		DBHelper.version = version;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacknumber (_id integer primary key autoincrement, number varchar(20))");
		db.execSQL("create table applock (_id integer primary key autoincrement, packagename varchar(30))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
