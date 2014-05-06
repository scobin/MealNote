package com.example.mealrecord;

import android.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * 記録UIの基本クラス
 * @author kawachi
 *
 */
public class RecordUIBase extends UIBase implements AdapterView.OnItemClickListener{
	
	protected static int command;
	
	protected static final int INSERT = 0; 
	protected static final int RECORD_UPDATE = 1;
	
	protected static String date;
	protected static String food;
	protected static String place;
	protected static int score;
	protected static int price;
	protected static String memo;
	protected static String[] people;
	protected static int mark;
	/** For the update command,and to record the id of the data which to be updated later.*/
	protected static int updateId; 

	/** クリックされたリストの項目に格納するデータのID */
	protected int onClickItemID;
	protected AlertDialog dialog;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	
	}
	
	
}
