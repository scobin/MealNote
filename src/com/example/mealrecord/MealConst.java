package com.example.mealrecord;

import java.io.File;

import android.os.Environment;

/**
 * 定数を格納するクラス
 * @author scobin
 *
 */
public class MealConst {

	//common
	public static final String COMMON_ON = "1"; 
	public static final String COMMON_OFF = "0"; 
	
	//file
	public static final String PHOTO_DIR = Environment.getExternalStorageDirectory().getPath()+"/MealApp/Pic";
	public static final String PHOTO_FILENAME = "/tmp.jpg";
	public static final File PHOTO_FILE = new File(PHOTO_DIR);
	
	//uim002
	public static final int LIST_M020_COUNT_MAX=100; 
	
	//DB
	public static final String TABLEMEAL = "MealRecord";
	 //column_tableName_columnName
	public static final String COL_MEAL_FOOD = "food";
	public static final String COL_MEAL_DATE = "date";
	public static final String COL_MEAL_SCORE = "score";
	public static final String COL_MEAL_PRICE = "price";
	public static final String COL_MEAL_ID = "_id";
	public static final String COL_MEAL_PEOPLE = "people";
	public static final String COL_MEAL_MEMO = "memo";
	public static final String COL_MEAL_MARK = "mark";
	public static final String COL_MEAL_PLACE = "place";

}
