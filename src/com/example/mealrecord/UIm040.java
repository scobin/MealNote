/**
 * お気に入り画面
 */ 
package com.example.mealrecord;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;



public class UIm040 extends FavoriteUIBase {

	/** record the total number of the list */
	private int mCount;
	/** record the index*/
	private int index = 0;

	private float startX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uim040);
		findViewById(R.id.ll_m040_content).setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					startX = event.getX();
					break;
				case MotionEvent.ACTION_UP:
					if(event.getX()>(startX+50)){
						//previous index
						if((index-4) >= 0){
							index -= 4;
							updateListView();
						}
					} 
					else if(event.getX()<(startX-50)){
						//next index
						if(mCount > (index+4)){
							index += 4;
							updateListView();
						}
						
					}
					break;
				default:
					break;
				}
				return false;
			}
			
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
		String dbCmd = "select * from " + MealConst.TABLEMEAL +" where mark=1 order by date DESC";
		mHelper = new DbHelper(this,getResources().getString(R.string.db_name_meal));
		db = mHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(dbCmd, null);
		mCount = cursor.getCount();
		getTextView(R.id.txt_m040_data_total_number).setText(Utility.getMessage(R.string.str_m000_data_total_number, String.valueOf(mCount)));
		mList = new ArrayList<HashMap<String,String>>();
		HashMap<String,String> mMap;
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()){
				mMap = new HashMap<String,String>();
				mMap.put("_id", cursor.getString(0));
				mMap.put("date", cursor.getString(1));
				mMap.put("food", cursor.getString(2));
				mMap.put("place", cursor.getString(3));
				mMap.put("score", cursor.getString(4));
				mMap.put("price", cursor.getString(6));
				mList.add(mMap);
				cursor.moveToNext();
			}
		}
		//db.close();
		findViewById(R.id.txt_m050_more_1).setOnClickListener(this);
		findViewById(R.id.txt_m050_more_2).setOnClickListener(this);
		findViewById(R.id.txt_m050_more_3).setOnClickListener(this);
		findViewById(R.id.txt_m050_more_4).setOnClickListener(this);
		updateListView();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(db!=null){
			db.close();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		
		case R.id.txt_m050_more_1:
			position = (Integer) findViewById(R.id.ll_m040_row1).getTag();
			actClearAndTop(UIm050.class);
			break;
		case R.id.txt_m050_more_2:
			position = (Integer) findViewById(R.id.ll_m040_row2).getTag();
			actClearAndTop(UIm050.class);
			break;
		case R.id.txt_m050_more_3:
			position = (Integer) findViewById(R.id.ll_m040_row3).getTag();
			actClearAndTop(UIm050.class);
			break;
		case R.id.txt_m050_more_4:
			position = (Integer) findViewById(R.id.ll_m040_row4).getTag();
			actClearAndTop(UIm050.class);
			break;
		default:
			super.onClick(v);
			break;
		}
		
	}
	
	private void updateListView(){
		
		int i = index;
		int j;
		//ページナンバー
		getTextView(R.id.txt_m040_page).setText(Utility.getMessage(R.string.str_m040_page, new String[]{String.valueOf(index+1),String.valueOf(index+4)}));
		//内容の表示
		TextView txtNum;
		TextView txtInfo1;
		TextView txtInfo2;
		TextView txtInfo3;
		ImageView imgPic;
		findViewById(R.id.ll_m040_row1).setVisibility(View.GONE);
		findViewById(R.id.ll_m040_row2).setVisibility(View.GONE);
		findViewById(R.id.ll_m040_row3).setVisibility(View.GONE);
		findViewById(R.id.ll_m040_row4).setVisibility(View.GONE);
		while(i<index+4 && i<mCount){
			j=i%4;
			switch(j){
			    case 0:
			    	findViewById(R.id.ll_m040_row1).setVisibility(View.VISIBLE);
			    	findViewById(R.id.ll_m040_row1).setTag(i);
			    	txtNum=(TextView)findViewById(R.id.txt_m040_SRNum_1);
			    	txtInfo1=(TextView)findViewById(R.id.txt_m040_RInfo1_1);
			    	txtInfo2=(TextView)findViewById(R.id.txt_m040_RInfo2_1);
			    	txtInfo3=(TextView)findViewById(R.id.txt_m040_RInfo3_1);
			    	imgPic=(ImageView)findViewById(R.id.img_m040_SRPic_1);
				    break;
			    case 1:
			    	findViewById(R.id.ll_m040_row2).setVisibility(View.VISIBLE);
			    	findViewById(R.id.ll_m040_row2).setTag(i);
			    	txtNum=(TextView)findViewById(R.id.txt_m040_SRNum_2);
			    	txtInfo1=(TextView)findViewById(R.id.txt_m040_RInfo1_2);
			    	txtInfo2=(TextView)findViewById(R.id.txt_m040_RInfo2_2);
			    	txtInfo3=(TextView)findViewById(R.id.txt_m040_RInfo3_2);
			    	imgPic=(ImageView)findViewById(R.id.img_m040_SRPic_2);
			    	break;
			    case 2:
			    	findViewById(R.id.ll_m040_row3).setVisibility(View.VISIBLE);
			    	findViewById(R.id.ll_m040_row3).setTag(i);
			    	txtNum=(TextView)findViewById(R.id.txt_m040_SRNum_3);
			    	txtInfo1=(TextView)findViewById(R.id.txt_m040_RInfo1_3);
			    	txtInfo2=(TextView)findViewById(R.id.txt_m040_RInfo2_3);
			    	txtInfo3=(TextView)findViewById(R.id.txt_m040_RInfo3_3);
			    	imgPic=(ImageView)findViewById(R.id.img_m040_SRPic_3);
			    	break;
			    case 3:
			    	findViewById(R.id.ll_m040_row4).setVisibility(View.VISIBLE);
			    	findViewById(R.id.ll_m040_row4).setTag(i);
			    	txtNum=(TextView)findViewById(R.id.txt_m040_SRNum_4);
			    	txtInfo1=(TextView)findViewById(R.id.txt_m040_RInfo1_4);
			    	txtInfo2=(TextView)findViewById(R.id.txt_m040_RInfo2_4);
			    	txtInfo3=(TextView)findViewById(R.id.txt_m040_RInfo3_4);
			    	imgPic=(ImageView)findViewById(R.id.img_m040_SRPic_4);
			    	break;
			    default:
			    	return;	
			}
			//番号
			txtNum.setText(String.valueOf(i + 1));
			//時間
			txtInfo1.setText(mList.get(i).get(MealConst.COL_MEAL_DATE));
			//料理名
			txtInfo2.setText(mList.get(i).get(MealConst.COL_MEAL_FOOD));
			//評価
			txtInfo3.setText(getResources().getString(R.string.str_m000_score)
					+ ": " + mList.get(i).get(MealConst.COL_MEAL_SCORE));
			//料理画像の表示設定
			String fileName=Utility.makeMealImageFileName(mList.get(i).get(MealConst.COL_MEAL_DATE)+mList.get(i).get(MealConst.COL_MEAL_FOOD));
			File imgFile= new File(MealConst.PHOTO_DIR,fileName);
			if(imgFile.exists()) { 	
				//ファイル存在している
				Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				//写真ファイルの検索
				String[] proj = { MediaStore.Images.Media._ID };
				@SuppressWarnings("deprecation")
				Cursor imgcr = this.managedQuery(uri, proj, MediaStore.Images.Media.DISPLAY_NAME+"=?", new String[]{fileName}, null);
				//Cursor imgcr = (Cursor) new CursorLoader(mMyApp.getApplicationContext(),uri, proj, MediaStore.Images.Media.DISPLAY_NAME+"=?", new String[]{fileName}, null);
						if(imgcr!=null&&imgcr.moveToFirst()){
							//画像がある
							//サブナイル写真の取得
							Long imgID=imgcr.getLong(imgcr.getColumnIndex(MediaStore.Images.Media._ID));
							Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(mMyApp.getApplicationContext().getContentResolver(), imgID, Images.Thumbnails.MICRO_KIND, null);
							//写真方向の判定
							bmp = Utility.checkImageDirection(imgFile.getPath(), bmp);
							
							//imgcr.close();
							imgPic.setImageBitmap(bmp);
							
						}
						else{
							//画像がなし
							imgPic.setImageResource(R.drawable.noimg96);
						}
						
			}
			else{
				//ファイル存在してない
				imgPic.setImageResource(R.drawable.noimg96);
			}
			i++;
		}
	}

}
