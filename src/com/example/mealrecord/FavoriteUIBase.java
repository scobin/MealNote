package com.example.mealrecord;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * お気に入りの基本クラス
 */ 
public class FavoriteUIBase extends UIBase{
    /** 情報リスト*/
	protected static ArrayList<HashMap<String,String>> mList;
	/** 選択した位置*/
	protected static int position;
}
