package com.junyangwang.easycall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database {
	
	public static final String KEY_ROWID = "id";
	public static final String KEY_NAME = "name";
	public static final String KEY_NUMBERS = "numbers";
	
	private static final String DATABASE_NAME = "EasyCall";
	private static final String DATABASE_TABLE_NAME = "EasyCallTable";
	private static final int DATABASE_VERSION = 1;
	
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE_NAME + " (" +
						KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						KEY_NAME + " TEXT NOT NULL, " +
						KEY_NUMBERS + " TEXT NOT NULL);"		
			);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_NAME);
			onCreate(db);
		}
		
	}
	
	public Database(Context c){
		ourContext = c;
	}
	
	public Database open(){
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		//ourHelper.onUpgrade(ourDatabase, 1, 1);
		if (!isTableExists(DATABASE_TABLE_NAME)){
			ourHelper.onCreate(ourDatabase);
		}
		return this;
	}
	
	public void close(){
		ourHelper.close();
	}
	
	public boolean isTableExists(String tableName) {
	    Cursor c = ourDatabase.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
	    if(c!=null) {
	        if(c.getCount()>0) {
	            return true;
	        }
	    }
	    return false;
	}
	
	public void createEntry(String name, String numbers){
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_NUMBERS, numbers);
			
		ourDatabase.insert(DATABASE_TABLE_NAME, null, cv);
	}
	
	public void updateEntry(int rowid, String name, String numbers){
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_NAME, name);
		cv.put(KEY_NUMBERS, numbers);
			
		ourDatabase.update(DATABASE_TABLE_NAME, cv, KEY_ROWID + "=" + rowid, null);
	}
	
	public int getHighestRow(){
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_NUMBERS};
		Cursor c = ourDatabase.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
		
		int max = -1;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if (c.getInt(0) > max){
				max = c.getInt(0);
			}
		}
		
		return max;
	}
	
	public String getName(int rowid){
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_NUMBERS};
		Cursor c = ourDatabase.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
		
		String name = null;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if (c.getInt(0) == rowid){
				name = c.getString(1);
			}
		}
		
		return name;	
	}
	
	public String getNumbers(int rowid){
		String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_NUMBERS};
		Cursor c = ourDatabase.query(DATABASE_TABLE_NAME, columns, null, null, null, null, null);
		
		String numbers = null;
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			if (c.getInt(0) == rowid){
				numbers = c.getString(2);
			}
		}
		
		return numbers;	
	}
	
	public void deleteUser(int rowid){
		ourDatabase.delete(DATABASE_TABLE_NAME, KEY_ROWID + "=" + rowid, null);		
	}
	
}
