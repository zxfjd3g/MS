package com.atguigu.security.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.atguigu.security.dao.AppLockDao;
/**
 * 
 * @author Administrator
 *
 */
public class ApplockProvider extends ContentProvider {

	public static final String AUTHORITY = "com.atguigu.security.provider.applockprovider";
	private static final int DELETE = 1;
	private static final int INSERT = 2;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static final Uri URI = Uri.parse(AUTHORITY);
	static {
		matcher.addURI(AUTHORITY, "delete", DELETE);
		matcher.addURI(AUTHORITY, "insert", INSERT);
	}

	private AppLockDao appLockDao;

	@Override
	public boolean onCreate() {
		appLockDao = new AppLockDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {

		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {

		int match = matcher.match(uri);
		if (match == INSERT) {
			String packageName = values.getAsString("packagename");
			appLockDao.add(packageName);
			//通知所有监听在此URI上的obsever
			getContext().getContentResolver().notifyChange(URI, null);
		} else {
			throw new RuntimeException("格式不正确");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int match = matcher.match(uri);
		if (match == DELETE) {
			appLockDao.delete(selectionArgs[0]);
			//通知所有监听在此URI上的obsever
			getContext().getContentResolver().notifyChange(URI, null); 
		} else {
			throw new RuntimeException("格式不正确");
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		
		return 0;
	}
}
