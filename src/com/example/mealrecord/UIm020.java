
package com.example.mealrecord;


/**
 * 記録の閲覧画面
 */ 
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author kawachi
 *
 */
public class UIm020 extends RecordUIBase {

	private ListView lv;

	/** record the total number of the list */
	private int listCount;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.uim020);
		mHelper=new DbHelper(this,getResources().getString(R.string.db_name_meal));
		db = mHelper.getWritableDatabase();
		lv = (ListView) findViewById(R.id.lv_m020_information);
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		//リスナー登録
		findViewById(R.id.btn_m020_add).setOnClickListener(this);
		findViewById(R.id.btn_m020_all_delete).setOnClickListener(this);
		findViewById(R.id.btn_m020_exit).setOnClickListener(this);
		
		updateListView();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//リスナー解放
	    findViewById(R.id.btn_m020_add).setOnClickListener(null);
		findViewById(R.id.btn_m020_all_delete).setOnClickListener(null);
		findViewById(R.id.btn_m020_exit).setOnClickListener(null);
	}

	@Override
	public void onClick(View v) {
		
		super.onClick(v);
		switch(v.getId()){
		case R.id.btn_m020_add:
			//check the total number
			if(listCount<MealConst.LIST_M020_COUNT_MAX){
				Intent intent =new Intent(UIm020.this,UIm030.class);
				command = INSERT;
				
				startActivityForResult(intent,0);
			}
			else{
				Toast.makeText(v.getContext(), getResources().getString(R.string.toast_m020_list_count_is_max), Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.btn_m020_all_delete:
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					UIm020.this)
			.setCancelable(false)
			.setTitle(getResources().getString(R.string.msg_m020_title_delete))
			.setMessage(getResources().getString(R.string.msg_m020_delete_qury))
			.setNegativeButton(getResources().getString(R.string.btn_no), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.setPositiveButton(getResources().getString(R.string.btn_yes), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Utility.deleteMealRecordTable(db);
					Utility.createMealRecordTable(db);
					updateListView();
				}
			});
			AlertDialog dialogYesNo=alertDialogBuilder.create();
			dialogYesNo.show(); 
			break;
		case R.id.btn_m020_exit:
			finish();
			break;
			
		case R.id.btnDialogDelete:
			db.delete(MealConst.TABLEMEAL, "_id="+String.valueOf(onClickItemID),null);
			updateListView();
			dialog.dismiss();
			break;
		case R.id.btnDialogEdit:
			ItemEditFunc(onClickItemID);
			dialog.dismiss();
			break;
		case R.id.btnDialogCancel:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}

	
	private void updateListView(){
		Cursor cursor = db.rawQuery("select * from MealRecord order by date DESC", null);
		listCount = cursor.getCount();
		getTextView(R.id.txt_m020_data_total_number).setText(Utility.getMessage(R.string.str_m000_data_total_number, String.valueOf(listCount)));
		String id = cursor.getColumnName(0);
		String date=cursor.getColumnName(1);
		String food=cursor.getColumnName(2);
		String place=cursor.getColumnName(3);
		String score=cursor.getColumnName(4);
		String people=cursor.getColumnName(5);
		String price=cursor.getColumnName(6);
		String memo=cursor.getColumnName(7);
		String[] ColumnNames ={id,date,food,place,score,people,price,memo};
		ListAdapter adapter = new CursorAdapter(this,
				R.layout.listviewlayout,cursor,ColumnNames,new int[]{R.id.id,
				R.id.date,R.id.food,R.id.place,R.id.score,R.id.people,
				R.id.price,R.id.memo});
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(this);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		TextView tVid=(TextView)view.findViewById(R.id.id);
		TextView tVfood=(TextView)view.findViewById(R.id.food);//Dialogに表示するため、予め取得する。
		onClickItemID=Integer.parseInt(tVid.getText().toString());
		//Dialog
		LayoutInflater li = LayoutInflater.from(getBaseContext());
		View promptsView = li.inflate(R.layout.item_click_dialog, null);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				mMyApp.getCurrentActivity());
		alertDialogBuilder.setView(promptsView);
		alertDialogBuilder.setCancelable(false);
		dialog=alertDialogBuilder.create();
		dialog.show();
		
		TextView txtClickNum=(TextView)promptsView.findViewById(R.id.txtClickNum);
		txtClickNum.setText(getResources().getString(R.string.str_m000_number)+tVid.getText()+"  "+tVfood.getText());//選択された項目のIDをDialogに表示する。
		//リスナー登録
		getButton(promptsView,R.id.btnDialogEdit).setOnClickListener((OnClickListener) this);
		getButton(promptsView,R.id.btnDialogDelete).setOnClickListener((OnClickListener) this);
		getButton(promptsView,R.id.btnDialogCancel).setOnClickListener((OnClickListener) this);
	
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==-1){
			
	        updateListView();
		}
	}

	private void ItemEditFunc(int Itemid){
		updateId = Itemid;
		String cmdHeader = "select * from " + MealConst.TABLEMEAL + " ";
		Cursor cursor = db.rawQuery(cmdHeader + "where _id=?", new String[]{String.valueOf(Itemid)});
		if(cursor.moveToFirst()){
			//Intent intent =new Intent(UIm020.this,UIm003.class);
			command = RECORD_UPDATE;
			
			date = cursor.getString(cursor.getColumnIndex(MealConst.COL_MEAL_DATE));
			food = cursor.getString(cursor.getColumnIndex(MealConst.COL_MEAL_FOOD));
			place = cursor.getString(cursor.getColumnIndex(MealConst.COL_MEAL_PLACE));
			score = cursor.getInt(cursor.getColumnIndex(MealConst.COL_MEAL_SCORE));
			price = cursor.getInt(cursor.getColumnIndex(MealConst.COL_MEAL_PRICE));
			memo = cursor.getString(cursor.getColumnIndex(MealConst.COL_MEAL_MEMO));
			people = Utility.prasePeopleData(cursor.getString(cursor.getColumnIndex(MealConst.COL_MEAL_PEOPLE)));
			mark = cursor.getInt(cursor.getColumnIndex(MealConst.COL_MEAL_MARK));

			actClearAndTop(UIm030.class);
			//Intent intent =new Intent(UIm020.this,UIm003.class);
			//startActivity(intent);
		}
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if(db!=null){
			db.close();
		}
	}
}

