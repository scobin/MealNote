/**
 * 新規記録の追加や編集画面
 */ 
package com.example.mealrecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker.OnTimeChangedListener;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class UIm030 extends RecordUIBase {

	EditText eTFood,eTPlace,eTScore,eTPrice,eTPeople[]=new EditText[10],eTMemo;
	ImageView imgRecMenuMeal;
	TextView textDate;
	AlertDialog dialog;
	private int peopleNum=1;
	private int year;  
    private int month;  
    private int day;  
    private int hour;  
    private int minute;   
    private ProgressDialog progdialog=null;
    /** 写真取得有無フラグ */
    private Boolean isGotPhoto=false;
    //private Handler handler=new Handler();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.uim030);
		
		findViewFunction();
		//Initial of Date  
		Date d = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		CharSequence s =sdf.format(d.getTime());
		textDate.setText(s);
		CheckCmdFunc();
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//Button Event
		getImageButton(R.id.imgBtn_m030_AddMenuPeopleAdd).setOnClickListener(this);
		getImageButton(R.id.imgBtn_m030_AddMenuDate).setOnClickListener(this);		
		getButton(R.id.btn_m030_AddMenuOK).setOnClickListener(this);
		getButton(R.id.btn_m030_AddMenuCancel).setOnClickListener(this);
		getImageButton(R.id.imgBtn_m030_UseCamera).setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		super.onStop();

		//Button Event
		getImageButton(R.id.imgBtn_m030_AddMenuPeopleAdd).setOnClickListener(null);
		getImageButton(R.id.imgBtn_m030_AddMenuDate).setOnClickListener(null);		
		getButton(R.id.btn_m030_AddMenuOK).setOnClickListener(null);
		getButton(R.id.btn_m030_AddMenuCancel).setOnClickListener(null);
		getImageButton(R.id.imgBtn_m030_UseCamera).setOnClickListener(null);
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(progdialog!=null){
			progdialog.dismiss();
			progdialog=null;
		}
				
	}

	@Override
	public void onClick(View v) {
		
		super.onClick(v);
		switch(v.getId()){
		case R.id.imgBtn_m030_AddMenuDate:
			dateSelectFunc();
			break;
		case R.id.imgBtn_m030_AddMenuPeopleAdd:
			peopleAddFunc();
			break;
		case R.id.btn_m030_AddMenuOK:
			addOkFunc(); //After typing the data, send back the data to SQL Activity
			break;
		case R.id.btn_m030_AddMenuCancel:
			finish();
			break;
		case R.id.imgBtn_m030_UseCamera:
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				//ＳＤカードの確認OK
				if(!MealConst.PHOTO_FILE.exists()){
					if(!MealConst.PHOTO_FILE.mkdirs()){
						//ファイルの作成
						Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_m030_error_message1), Toast.LENGTH_SHORT).show();
					}
				}
				useIntentCamera();
			}
			else{
				
				Toast.makeText(getApplicationContext(), getResources().getString(R.string.str_m030_error_message2), Toast.LENGTH_SHORT).show();
			}
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==RESULT_OK){
			File tmp= new File(MealConst.PHOTO_FILE,MealConst.PHOTO_FILENAME);
			 
			if(tmp.exists()){
				//画像がある
				isGotPhoto = true;
				BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inSampleSize=4;
				Bitmap bmp=BitmapFactory.decodeFile(tmp.getAbsolutePath(), opts);
                //写真の方向を取得して処理する。
				bmp = Utility.checkImageDirection(tmp.getAbsolutePath(), bmp);
				imgRecMenuMeal.setImageBitmap(bmp);
				imgRecMenuMeal.setScaleType(ScaleType.CENTER_CROP);//ScaleTypeの設定
				
			}
			else{
				//画像がなし
				imgRecMenuMeal.setImageResource(R.drawable.noimg96);
			}
			
		}
		
	}

	private void findViewFunction(){
	    
	    eTFood=(EditText)findViewById(R.id.etxt_m030_AddMenuFood);
	    eTPlace=(EditText)findViewById(R.id.etxt_m030_AddMenuPlace);
	    eTPeople[0]=(EditText)findViewById(R.id.etxt_m030_AddMenuPeople);
	    eTPrice=(EditText)findViewById(R.id.etxt_m030_AddMenuPrice);
	    eTScore=(EditText)findViewById(R.id.etxt_m030_AddMenuScore);
	    eTMemo=(EditText)findViewById(R.id.etxt_m030_AddMenuMemo);
	    textDate=(TextView)findViewById(R.id.txt_m030_AddMenuDate);
	    imgRecMenuMeal=(ImageView)findViewById(R.id.img_m030_RecMenuMeal);
	}
	    
	private void CheckCmdFunc(){
		
		switch(command){
		case RECORD_UPDATE:
			peopleNum = people.length;
			eTPeople[0].setText(people[0]);
			for(int i=1;i<peopleNum;i++){
				//Add the EditText view, total is the value of peopleNum.
				LinearLayout lL=(LinearLayout) findViewById(R.id.lLPeople);
				eTPeople[i]=new EditText(this);
				lL.addView(eTPeople[i]);
				eTPeople[i].setText(people[i]);
			}
			textDate.setText(date);
			eTFood.setText(food);
			eTPlace.setText(place);
			eTPrice.setText(String.valueOf(price));
			eTScore.setText(String.valueOf(score));
			eTMemo.setText(memo);
			//お気に入りチェックボックスの設定
			if(mark == 1){
				getCheckBox(R.id.cb_m030_favorite).setChecked(true);
			}
			else{
				getCheckBox(R.id.cb_m030_favorite).setChecked(false);
			}
			//画像の表示設定
			String fileName = Utility.makeMealImageFileName(date+food);
			File tmp= new File(MealConst.PHOTO_DIR,fileName);
					 
			if(tmp.exists()){
				//画像がある
				BitmapFactory.Options opts=new BitmapFactory.Options();
				opts.inSampleSize=4;
				Bitmap bmp=BitmapFactory.decodeFile(tmp.getAbsolutePath(), opts);
                //写真の方向を取得して処理する。
				bmp = Utility.checkImageDirection(tmp.getAbsolutePath(), bmp);
				imgRecMenuMeal.setImageBitmap(bmp);
				imgRecMenuMeal.setScaleType(ScaleType.CENTER_CROP);//ScaleTypeの設定
				
			}
			else{
				//画像がなし
				imgRecMenuMeal.setImageResource(R.drawable.noimg96);
			}
			break;
	    default:
			break;
		}
		
		
	}
	
	
	private void addOkFunc(){
		mHelper=new DbHelper(this,getResources().getString(R.string.db_name_meal));
		db=mHelper.getWritableDatabase();
		
		ContentValues values=new ContentValues();
		String[] peopleData= new String[peopleNum];
		for(int i=0;i<peopleNum;i++){
			peopleData[i] = eTPeople[i].getText().toString();
		}
		values.put("date", textDate.getText().toString());
		values.put("food", eTFood.getText().toString());
		values.put("place", eTPlace.getText().toString());
		values.put("people", Utility.createPeopleData(peopleData));
		values.put("price", eTPrice.getText().toString());
		values.put("score", eTScore.getText().toString());
		values.put("memo", eTMemo.getText().toString());
		if(getCheckBox(R.id.cb_m030_favorite).isChecked()){
			values.put("mark", MealConst.COMMON_ON);
		}
		else{
			values.put("mark", MealConst.COMMON_OFF);
		}
		
		if(command!=INSERT){
			//update record
			db.update(MealConst.TABLEMEAL, values, "_id=?", new String[]{String.valueOf(updateId)});
		}
		else{
			//Add new record
			db.insertOrThrow(MealConst.TABLEMEAL, null, values);
		}
		db.close();
		//写真ファイルのコピーして名前を変更する。
		if(isGotPhoto){
			String newFileName=Utility.makeMealImageFileName(textDate.getText().toString()+eTFood.getText().toString());
			
			if(!copyFile(MealConst.PHOTO_FILE.getPath()+"/tmp.jpg",MealConst.PHOTO_DIR+"/"+newFileName)){
				Toast.makeText(this,getResources().getString(R.string.msg_m030_save_photo), Toast.LENGTH_SHORT).show();
			}
			
			//media scan
			scanFileAsync(getBaseContext(),MealConst.PHOTO_DIR+"/"+newFileName);
		}

		finish();//
	}
	
	/**
	 * 写真の情報をMediaStoreデータベースに追加する。
	 * @param ctx
	 * @param filePath
	 */
	public void scanFileAsync(Context ctx, String filePath) {
		 Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		 scanIntent.setData(Uri.fromFile(new File(filePath)));
		 ctx.sendBroadcast(scanIntent);
    } 
	
	/**
	 * ファイルをコピーする。
	 * @param oldPath コピー対象ファイルのパス
	 * @param newPath 新しいファイルのパス
	 * @return　コピー成功はtrueを返す。失敗はfalseを返す。
	 */
	private boolean copyFile(String oldPath,String newPath){
		boolean result=true;
		try{

			int byteRead=0;
			File oldFile=new File(oldPath);
			if(oldFile.exists()){
				FileInputStream inStream = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer =new byte[1024];
				//int length;
				while((byteRead=inStream.read(buffer))!=-1){
					//byteSum+=byteRead;
					fs.write(buffer,0,byteRead);
				}
				fs.flush();
				fs.close();
				inStream.close();
			}
			else{
				result=false;
			}
		}
		catch(Exception e){
			result=false;
		}
		return result;
	}
	
	@SuppressLint("HandlerLeak")
	private void peopleAddFunc(){
		LinearLayout lL=(LinearLayout) findViewById(R.id.lLPeople);
		eTPeople[peopleNum]=new EditText(this);
		lL.addView(eTPeople[peopleNum]);
		peopleNum++;
		
	}
	
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(MealConst.PHOTO_FILE,MealConst.PHOTO_FILENAME)));
				startActivityForResult(intent,0);
				
				break;
			}

		}

	};
	
	private void useIntentCamera(){
		//カメラ起動Dialogの表示
		progdialog=new ProgressDialog(UIm030.this);
		progdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progdialog.setMessage(getResources().getString(R.string.msg_m030_start_camera));
		progdialog.setCancelable(false);
		progdialog.show();
		//start the camera.
		new Thread(){
			@Override
			public void run(){
				super.run();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what=1;
				mHandler.sendMessage(msg);
			}
		}.start();

	}
	
	private void dateSelectFunc(){
		LayoutInflater li = LayoutInflater.from(getBaseContext());
		View dateSelectView = li.inflate(R.layout.date_select_pop, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				UIm030.this);
		alertDialogBuilder.setView(dateSelectView);
		DatePicker datePicker = (DatePicker) dateSelectView.findViewById(R.id.datePicker1);  
        TimePicker timePicker = (TimePicker) dateSelectView.findViewById(R.id.timePicker1);
         
        Calendar calendar = Calendar.getInstance(); 
        year = calendar.get(Calendar.YEAR);  
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);  
        hour = calendar.get(Calendar.HOUR);  
        minute = calendar.get(Calendar.MINUTE);  
        datePicker.init(year, month, day, new OnDateChangedListener(){

			@Override
			public void onDateChanged(DatePicker view, int year,
					int monthOfYear, int dayOfMonth) {
				UIm030.this.year = year;
				UIm030.this.month=monthOfYear;
				UIm030.this.day=dayOfMonth;
				
			}
        	
        });
        timePicker.setOnTimeChangedListener(new OnTimeChangedListener(){

			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				UIm030.this.hour=hourOfDay;
				UIm030.this.minute=minute;
			}
        	
        });
        timePicker.setIs24HourView(true);
        // Setup the Dialog's settings and show it.
        alertDialogBuilder
    	.setCancelable(false)
    	.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DecimalFormat df = new DecimalFormat();
				df.applyPattern("00");
				String text=String.valueOf(year)+"-"+
				            String.valueOf(df.format(month+1))+"-"+
						    String.valueOf(df.format(day))+" "+
				            String.valueOf(df.format(hour))+":"+
						    String.valueOf(df.format(minute));
				textDate.setText(text);
			}
		})
		.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create().show();
        
	}
   
	

}
