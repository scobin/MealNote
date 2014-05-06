package com.example.mealrecord;


import java.io.IOException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

/**
 * tool
 * @author kawachi
 *
 */
public class Utility {
	
	//DB 
	public static void createMealRecordTable(SQLiteDatabase db){
		String DATABASE_CREATE_TABLE =
			     "create table "+MealConst.TABLEMEAL+"("
			       + "_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,"
			             + "date DATETIME,"
			             + "food TEXT,"
			             + "place TEXT,"
			             + "score INT,"
			             + "people TEXT,"
			             + "price INT," 
			             + "memo TEXT,"
			             + "mark INT"
			         + ")";
	    db.execSQL(DATABASE_CREATE_TABLE);
	}
	
		
	public static void deleteMealRecordTable(SQLiteDatabase db){
		String cmd = "DROP TABLE IF EXISTS " + MealConst.TABLEMEAL;
		db.execSQL(cmd); 
	}
		
	public static String createPeopleData(String[] data){
		String people="";
		for(int i=0;i<data.length;i++){
			if(i>0){
				people += ",";
			}
			people += data[i];
		}
		return people;
	}
	
	public static String[] prasePeopleData(String people ){
		String[] data = people.split(",");
		return data;
	}
	
	//String
	
	public static String getMessage(int strID, String replacement){
		String str=MyApplication.getInstance().getApplicationContext().getString(strID);
		try{
			str=str.replace("{0}", replacement);
		}catch(Exception e){
			str=null;
		}
		return str;
	}

	public static String getMessage(int strID, String[] replacement){
		String str=MyApplication.getInstance().getApplicationContext().getString(strID);
		try{
			for(int i=0;i<replacement.length;i++) {
				str=str.replace("{"+String.valueOf(i)+"}", replacement[i]);
			}
		}catch(Exception e){
			str=null;
		}
		return str;
	}
	//image
	/**
	 * ファイルシステムから取得した画像の方向をチェックして調整する。
	 * @param filePath 画像ファイルのパス
	 * @param bmp 取得した画像
	 * @return　調整した画像
	 */
	public static Bitmap checkImageDirection(String filePath,Bitmap bmp){
		Bitmap newbmp=null;
		try {
			ExifInterface exif = new ExifInterface(filePath); 
			int direction =Integer.parseInt(exif.getAttribute(ExifInterface.TAG_ORIENTATION));
			Matrix matrix = new Matrix(); 
			switch(direction){
			case 6:
				matrix.postRotate(90); 
			    newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			    break;
			case 3: 
			    matrix.postRotate(180); 
			    newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			    break;
			case 8: 
			    matrix.postRotate(270); 
			    newbmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
			    break;
			default:
				newbmp = bmp;
			    break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newbmp;
	}
	
	//file
	/**
	 * 写真ファイルの名前生成
	 * @param fileNameStr
	 * @return
	 */	
	public static String makeMealImageFileName(String fileNameStr){
		String fileName = fileNameStr + ".jpg";
		fileName=fileName.replace("-", "");
		fileName=fileName.replace(":", "");
		fileName=fileName.replace(" ", "");
		return fileName;
	}
	
}
