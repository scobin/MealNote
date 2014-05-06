package com.example.mealrecord;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class CursorAdapter extends SimpleCursorAdapter{

	private Cursor cr;
	private String[] col;

	@SuppressWarnings("deprecation")
	public CursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.cr = c;
		this.col = from;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		View view =null;
		
		if(convertView !=null){
			view=convertView;
			
		}
		else{
			view = super.getView(position, convertView, parent);
			
		}
		view.setBackgroundResource(R.drawable.backgradient);
		return super.getView(position, view, parent);
		
	}
	
	
}
