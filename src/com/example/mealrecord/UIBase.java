package com.example.mealrecord;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * UIの基本クラス
 */ 
public class UIBase extends BaseActivity implements OnClickListener,OnMenuItemClickListener{

	protected SQLiteDatabase db;
	protected DbHelper mHelper;
	//protected MyApplication mMyApp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		return false;
	}

	@Override
	public void onClick(View v) {
		
	}
	
	/**
	 * テキストビューの取得
	 * @param rID
	 * @return
	 */
	public TextView getTextView(int rID){
		return (TextView)findViewById(rID);
	}
	
	/**
	 * ビューにあるテキストビューの取得
	 * @param view
	 * @param rID
	 * @return
	 */
	public TextView getTextView(View view, int rID){
		return (TextView)view.findViewById(rID);
	}
	
	/**
	 * ボタンの取得
	 * @param rID
	 * @return
	 */
	public Button getButton(int rID){
		return (Button)findViewById(rID);
	}
	
	/**
	 * ビューにあるボタンの取得
	 * @param view
	 * @param rID
	 * @return
	 */
	public Button getButton(View view, int rID){
		return (Button)view.findViewById(rID);
	}
	
	/**
	 * ImageButton
	 * @param rID
	 * @return
	 */
	public ImageButton getImageButton(int rID){
		return (ImageButton)findViewById(rID);
	}
	
	/**
	 * 
	 * @param rID
	 * @return
	 */
	public ImageView getImageView(int rID){
		return (ImageView)findViewById(rID);
	}

	public CheckBox getCheckBox(int rID){
		return (CheckBox)findViewById(rID);
	}
}
