package com.otitan.util;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.otitan.adapter.ContactsManagerDbAdapter;
import com.otitan.entity.Page;
import com.otitan.entity.Record;

import java.util.ArrayList;
import java.util.List;

public class PageDao {

	SQLiteDatabase mSQLiteDatabaseWritable;

	public Page<Record> getRecordPage(Page<Record> page, String loginName,
			String contactName) {
		int pageSize = page.getPageSize();
		int pageNo = page.getPageNo();
		String sql = "select * from "
				+ ContactsManagerDbAdapter.TABLENAME_RECORD
				+ " where (fromUser = ? and toUser = ?) or (fromUser = ? and toUser = ?)"
				+ " Limit " + String.valueOf(pageSize) + " Offset "
				+ String.valueOf(pageNo * pageSize);
		String[] selectionArgs = { loginName, contactName, contactName,
				loginName };
		Cursor cursor = mSQLiteDatabaseWritable.rawQuery(sql, selectionArgs);
		List<Record> result = new ArrayList<Record>();
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			Record record = new Record();
			record.setrId(cursor.getInt(0));
			record.setFromUser(cursor.getString(1));
			record.setToUser(cursor.getString(2));
			record.setContent(cursor.getString(3));
			record.setrTime(cursor.getString(4));
			record.setIsReaded(cursor.getInt(5));
			result.add(record);
		}
		page.setTotalCount(getRowCount(loginName, contactName));
		page.setResult(result);
		cursor.close();
		return page;
	}

	/**
	 *
	 */
	public int getRowCount(String loginName, String contactName) {
		String sql = "select * from "
				+ ContactsManagerDbAdapter.TABLENAME_RECORD
				+ " where (fromUser = ? and toUser = ?) or (fromUser = ? and toUser = ?)";
		String[] selectionArgs = { loginName, contactName, contactName,
				loginName };
		Cursor cursor = mSQLiteDatabaseWritable.rawQuery(sql, selectionArgs);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}
}
