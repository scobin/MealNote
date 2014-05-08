package com.example.mealrecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * データベースのヘルパークラス
 */ 
public class DbHelper extends SQLiteOpenHelper{

	private static final int VERSION = 1; 
	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}
	public DbHelper(Context context,String name) {
		   this(context, name, null, VERSION);
	}
		   
	public DbHelper(Context context, String name, int version) { 
		    this(context, name, null, version); 
	}  
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		Utility.createMealRecordTable(db);
		
	    String DATABASE_CREATE_TABLE =
			   			     "create table Food("
			   			       + "_ID INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
			   			             + "name TEXT,"
			   			             + "eat_time INT,"
			   			             + "price_avg INT,"
			   			             + "type TEXT,"
			   			             + "category TEXT"
			   			         + ")";
	    db.execSQL(DATABASE_CREATE_TABLE);	    
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Utility.deleteMealRecordTable(db);
		
		Utility.createMealRecordTable(db);
	}

	@Override   
	public void onOpen(SQLiteDatabase db) {    
	           super.onOpen(db);  
	}
	 
	@Override
	public synchronized void close() {
	            super.close();
	}
}
