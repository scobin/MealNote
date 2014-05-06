package com.example.mealrecord;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class RankAct extends UIBase {

	ListView listScore;
	TextView txtListTitle;
	ImageButton imBtnScoreExit;
	ImageButton imBtnScoreOptions;
	SQLiteDatabase db;
	DbHelper mHelper;
	ScoreAdapter adapter;
	int rankMode=0;
	AlertDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_rank);
		mHelper=new DbHelper(this,getResources().getString(R.string.db_name_meal));
		db=mHelper.getWritableDatabase();
		findViewFunc();
		showList(rankMode);
		imBtnScoreExit.setOnClickListener(btnClickListener);
		imBtnScoreOptions.setOnClickListener(btnClickListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_score_rank, menu);
		return true;
	}

	private void findViewFunc(){
		listScore=(ListView)findViewById(R.id.listScore);
		imBtnScoreExit=(ImageButton)findViewById(R.id.imBtnScoreExit);
		imBtnScoreOptions=(ImageButton)findViewById(R.id.btnScoreOptions);
		txtListTitle=(TextView)findViewById(R.id.txtListTitle);
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.imBtnScoreExit:
			finish();
			break;
		case R.id.btnScoreOptions:
			LayoutInflater li = LayoutInflater.from(getBaseContext());
			View promptsView = li.inflate(R.layout.score_options_dialog, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					RankAct.this);
			alertDialogBuilder.setView(promptsView);
			alertDialogBuilder.setCancelable(false);
			
			dialog=alertDialogBuilder.create();
			dialog.show();
			Button btnDialogScore=(Button)promptsView.findViewById(R.id.btnDialogScore);
			Button btnDialogRound=(Button)promptsView.findViewById(R.id.btnDialogRound);
			Button btnDialogClose=(Button)promptsView.findViewById(R.id.btnDialogClose);
			btnDialogScore.setOnClickListener(btnClickListener);
			btnDialogRound.setOnClickListener(btnClickListener);
			btnDialogClose.setOnClickListener(btnClickListener);
			break;
		case R.id.btnDialogScore:
			dialog.dismiss();
			rankMode=0;
			txtListTitle.setText("総評価順位");
			showList(0);
			break;
		case R.id.btnDialogRound:
			dialog.dismiss();
			rankMode=1;
			txtListTitle.setText("総回数順位");
			showList(1);
			break;
		case R.id.btnDialogClose:
			dialog.dismiss();
		default:
			super.onClick(v);
		}
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	private OnClickListener btnClickListener=new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.imBtnScoreExit:
				finish();
				break;
			case R.id.btnScoreOptions:
				LayoutInflater li = LayoutInflater.from(getBaseContext());
				View promptsView = li.inflate(R.layout.score_options_dialog, null);
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						RankAct.this);
				alertDialogBuilder.setView(promptsView);
				alertDialogBuilder.setCancelable(false);
				
				dialog=alertDialogBuilder.create();
				dialog.show();
				Button btnDialogScore=(Button)promptsView.findViewById(R.id.btnDialogScore);
				Button btnDialogRound=(Button)promptsView.findViewById(R.id.btnDialogRound);
				Button btnDialogClose=(Button)promptsView.findViewById(R.id.btnDialogClose);
				btnDialogScore.setOnClickListener(btnClickListener);
				btnDialogRound.setOnClickListener(btnClickListener);
				btnDialogClose.setOnClickListener(btnClickListener);
				break;
			case R.id.btnDialogScore:
				dialog.dismiss();
				rankMode=0;
				txtListTitle.setText("総評価順位");
				showList(0);
				break;
			case R.id.btnDialogRound:
				dialog.dismiss();
				rankMode=1;
				txtListTitle.setText("総回数順位");
				showList(1);
				break;
			case R.id.btnDialogClose:
				dialog.dismiss();
			}
		}
		
	};
	
	private void showList(int rankMode){
		
		Cursor cursor=null;
		switch(rankMode){
		case 0://Score rankingを取得する。　
			cursor = db.rawQuery("select * from MealRecord group by date order by score DESC", null);
			break;
		case 1://Eat round rankingを取得する。
			cursor = db.rawQuery("select *,count(distinct date) as count from MealRecord"+
					" group by food order by count DESC;", null);
			break;
		case 2://週間評価順位
			
			break;
		case 3://週間回数順位
			
			break;
		default:
			//default mode:Score rankingを取得する。
			cursor = db.rawQuery("select * from MealRecord group by date order by score DESC", null);
			break;
		}
		
		ArrayList<HashMap<String,String>> mList =new ArrayList<HashMap<String,String>>();
		HashMap<String,String> mMap;
		int count=0;//表示の個数。
		if(cursor.moveToFirst()){
			while(!cursor.isAfterLast()&&count<10){//10番目まで表示する。
				mMap = new HashMap<String,String>();
				mMap.put("date", cursor.getString(1));
				mMap.put("food", cursor.getString(2));
				mMap.put("place", cursor.getString(3));
				mMap.put("score", cursor.getString(4));
				mMap.put("price", cursor.getString(6));
				if(rankMode==1){
					mMap.put("round", cursor.getString(8));
				}
				
				mList.add(mMap);
				count++;
				cursor.moveToNext();
			}
		}
		
		adapter = new ScoreAdapter(this,mList);
		listScore.setAdapter(adapter);
		//listScore.setOnItemClickListener(new ItemClickEvent());
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(db!=null){
			db.close();
		}
	}
	/**
	 * Scoreリストのアダプター
	 * @author kawachi
	 *
	 */
	private class ScoreAdapter extends BaseAdapter{

		private RankAct scoreRankAct;
		private LayoutInflater mInflater;
		private ArrayList<HashMap<String,String>> list=null;
		TextView txtNum,txtInfo0,txtInfo1,txtInfo2,txtInfo3,txtInfo4;
		ImageView imgSRPic;
		
		public ScoreAdapter(RankAct context, ArrayList<HashMap<String,String>> list){
			this.scoreRankAct = context;
			this.mInflater = LayoutInflater.from(context);
			this.list = list;
		} 
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view=null;
			if(convertView == null){
				view = mInflater.inflate(R.layout.score_rank_layout, null);	
			}
			else{
				view=convertView;
			}
			imgSRPic=(ImageView)view.findViewById(R.id.imgSRPic);
			txtNum=(TextView)view.findViewById(R.id.txtSRNum);
			txtInfo0=(TextView)view.findViewById(R.id.txtRInfo0);
			txtInfo1=(TextView)view.findViewById(R.id.txtRInfo1);
			txtInfo2=(TextView)view.findViewById(R.id.txtRInfo2);
			txtInfo3=(TextView)view.findViewById(R.id.txtRInfo3);
			txtInfo4=(TextView)view.findViewById(R.id.txtRInfo4);
			//番号設定
			txtNum.setBackgroundColor(Color.TRANSPARENT);
			txtNum.setText(String.valueOf(position+1));
			txtNum.setTextColor(Color.BLACK);
			
			if(rankMode==0){
				//Score Mode
				txtInfo0.setText(list.get(position).get("score")+"点");
				txtInfo2.setText("場所："+list.get(position).get("place"));
				txtInfo3.setVisibility(View.GONE);
			}
			else if(rankMode==1){
				//Round Mode
				txtInfo0.setText(list.get(position).get("round")+"回");
				txtInfo2.setVisibility(View.GONE);
				txtInfo3.setVisibility(View.GONE);

			}
			txtInfo1.setText(list.get(position).get("food"));
			txtInfo4.setText("値段："+list.get(position).get("price"));
			
			//料理画像の表示設定
			
			String fileName=Utility.makeMealImageFileName(list.get(position).get("date")+list.get(position).get("food"));

			File imgFile= new File(MealConst.PHOTO_DIR,fileName);
			if(imgFile.exists())
			{ 	//ファイル存在している
				Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				
				//写真ファイルの検索
				
				String[] proj = { MediaStore.Images.Media._ID };
				@SuppressWarnings("deprecation")
				Cursor imgcr = scoreRankAct.managedQuery(uri, proj, MediaStore.Images.Media.DISPLAY_NAME+"=?", new String[]{fileName}, null);
				if(imgcr!=null&&imgcr.moveToFirst()){
					//画像がある
					//サブナイル写真の取得
					Long imgID=imgcr.getLong(imgcr.getColumnIndex(MediaStore.Images.Media._ID));
					Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), imgID, Images.Thumbnails.MICRO_KIND, null);
					
					//写真方向の判定
					bmp = Utility.checkImageDirection(imgFile.getPath(), bmp);
					
					imgcr.close();
					
					imgSRPic.setImageBitmap(bmp);
					
				}
				else{
					//画像がなし
					imgSRPic.setImageResource(R.drawable.noimg96);
				}
				
			}
			else{
				//画像がなし
				imgSRPic.setImageResource(R.drawable.noimg96);
			}
			
			
			view.setBackgroundResource(R.drawable.backgradient);
			return view;
		}
		
	}
	
	
}
