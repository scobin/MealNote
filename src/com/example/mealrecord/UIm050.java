/**
 * お気に入りの詳細内容画面
 */ 
package com.example.mealrecord;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;


public class UIm050 extends FavoriteUIBase {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uim050);
	}

	@Override
	protected void onStart() {
		//写真の設定
		String imgPath=MealConst.PHOTO_DIR + "/" + Utility.makeMealImageFileName(mList.get(position).get(MealConst.COL_MEAL_DATE)+mList.get(position).get(MealConst.COL_MEAL_FOOD));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;
		Bitmap bitmap = BitmapFactory.decodeFile(imgPath, options);
		if(bitmap != null) {
			bitmap = Utility.checkImageDirection(imgPath, bitmap);
			getImageView(R.id.img_m050_pic).setImageBitmap(bitmap);
			getImageView(R.id.img_m050_pic).setScaleType(ImageView.ScaleType.CENTER_CROP);
		}
		else {
			getImageView(R.id.img_m050_pic).setImageResource(R.drawable.noimg96);
			getImageView(R.id.img_m050_pic).setScaleType(ImageView.ScaleType.CENTER);
		}
		//詳細内容の設定
		getTextView(R.id.txt_m050_time).setText(Utility.getMessage(R.string.str_m050_time, mList.get(position).get(MealConst.COL_MEAL_DATE)));
		getTextView(R.id.txt_m050_price).setText(Utility.getMessage(R.string.str_m050_price,mList.get(position).get(MealConst.COL_MEAL_PRICE)));
		getTextView(R.id.txt_m050_score).setText(Utility.getMessage(R.string.str_m050_score,mList.get(position).get(MealConst.COL_MEAL_SCORE)));
		getTextView(R.id.txt_m050_food).setText(Utility.getMessage(R.string.str_m050_food,mList.get(position).get(MealConst.COL_MEAL_FOOD)));
		getTextView(R.id.txt_m050_place).setText(Utility.getMessage(R.string.str_m050_place,mList.get(position).get(MealConst.COL_MEAL_PLACE)));
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	

}
