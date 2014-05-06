package com.example.mealrecord;

import android.os.Bundle;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class UIm010 extends UIBase {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uim010);
	}
	
	@Override
	protected void onStart() {
		
		super.onStart();
		//リスナー登録
		findViewById(R.id.btn_m010_record).setOnClickListener(this);
		findViewById(R.id.btn_m010_rank).setOnClickListener(this);
		findViewById(R.id.btn_m010_favorite).setOnClickListener(this);
	}

	@Override
	protected void onStop() {
		
		super.onStop();
		//リスナー解放
		findViewById(R.id.btn_m010_record).setOnClickListener(null);
		findViewById(R.id.btn_m010_rank).setOnClickListener(null);
		findViewById(R.id.btn_m010_favorite).setOnClickListener(null);
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		
		return super.onMenuItemClick(item);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
		case R.id.btn_m010_rank:
			actClearAndTop(RankAct.class);
			break;
		case R.id.btn_m010_record:
			actClearAndTop(UIm020.class);
			break;
		case R.id.btn_m010_favorite:
			actClearAndTop(UIm040.class);
		default:
			break;
		}
		
	}

}
