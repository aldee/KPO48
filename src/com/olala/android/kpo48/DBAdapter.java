package com.olala.android.kpo48;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;

public class DBAdapter {
	private static final String DATABASE_NAME = "kepo";
	private static final String DATABASE_TABLE = "data";
	private static final int DATABASE_VERSION = 1;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAMA = "nama";
	public static final String KEY_NIU = "niu";
	public static final String KEY_URL = "url";
	public static final String KEY_ANGKATAN = "angkatan";
	public static final String KEY_PRODI = "prodi";
	
	private static final String TAG = "DBAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String CREATE_DATABASE = "CREATE TABLE data(_id integer primary key autoincrement, nama varchar(20) not null, niu integer not null, angkatan integer not null, url varchar(200) not null, prodi varchar(20) not null)";
	
	private Context ctx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		
		public DatabaseHelper(Context ctx) {
			super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DATABASE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int id, int id2) {
			
		}
	}
	
	public DBAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	public DBAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(ctx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public long createData(String nama, int niu, int angkatan, String url, String prodi) {
		ContentValues values = new ContentValues();
		values.put(KEY_NAMA, nama);
		values.put(KEY_ANGKATAN, angkatan);
		values.put(KEY_NIU, niu);
		values.put(KEY_URL, url);
		values.put(KEY_PRODI, prodi);
		return mDb.insert(DATABASE_TABLE, null, values);
	}
	
	public Cursor getDataByDetails(String angkatan, String prodi) {
		Cursor c = mDb.rawQuery("SELECT * FROM data WHERE angkatan = " +angkatan+ " AND prodi = " +prodi, null);
		return c;
	}
}
