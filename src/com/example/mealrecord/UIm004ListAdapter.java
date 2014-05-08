package com.example.mealrecord;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 記録閲覧リストのアダプター
 */ 
public class UIm004ListAdapter extends BaseAdapter{

	private ArrayList<HashMap<String,String>> list=null;
	private LayoutInflater mInflater;
	private Context context;
	
	public UIm004ListAdapter(Context context, ArrayList<HashMap<String,String>> list){
		this.context = context;
		this.mInflater = LayoutInflater.from(context);
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view=null;
		ViewTag viewTag;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.favorite_list_layout, null);
			viewTag = new ViewTag(
                    (ImageView) convertView.findViewById(R.id.img_SRPic),
                    (TextView) convertView.findViewById(R.id.txt_SRNum),
                    (TextView) convertView.findViewById(R.id.txt_RInfo1),
                    (TextView) convertView.findViewById(R.id.txt_RInfo2),
                    (TextView) convertView.findViewById(R.id.txt_RInfo3)                    
                    );
			convertView.setTag(viewTag);
			view = convertView;
		}
		else{
			view=convertView;
			viewTag = (ViewTag)convertView.getTag();
		}
		//番号設定
		//viewTag.txtNum.setBackgroundColor(Color.TRANSPARENT);
		viewTag.txtNum.setText(String.valueOf(position+1));
		viewTag.db_id = String.valueOf(list.get(position).get(MealConst.COL_MEAL_ID));
		viewTag.txtNum.setTextColor(Color.BLACK);
		//date
		viewTag.txtInfo1.setText(list.get(position).get(MealConst.COL_MEAL_DATE));
		//food name
		viewTag.txtInfo2.setText(list.get(position).get(MealConst.COL_MEAL_FOOD));
		//score
		viewTag.txtInfo3.setText(MyApplication.getAppContext().getResources().getString(R.string.str_m000_score)
				+ ": " + list.get(position).get(MealConst.COL_MEAL_SCORE));
		
		
		//料理画像の表示設定
		
		String fileName=Utility.makeMealImageFileName(list.get(position).get(MealConst.COL_MEAL_DATE)+list.get(position).get(MealConst.COL_MEAL_FOOD));

		File imgFile= new File(MealConst.PHOTO_DIR,fileName);
		if(imgFile.exists())
		{ 	//ファイル存在している
			Uri uri=MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			
			//写真ファイルの検索
			
			String[] proj = { MediaStore.Images.Media._ID };
			@SuppressWarnings("deprecation")
			Cursor imgcr = ((Activity) context).managedQuery(uri, proj, MediaStore.Images.Media.DISPLAY_NAME+"=?", new String[]{fileName}, null);
			if(imgcr!=null&&imgcr.moveToFirst()){
				//画像がある
				//サブナイル写真の取得
				Long imgID=imgcr.getLong(imgcr.getColumnIndex(MediaStore.Images.Media._ID));
				Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), imgID, Images.Thumbnails.MICRO_KIND, null);
				
				//写真方向の判定
				bmp = Utility.checkImageDirection(imgFile.getPath(), bmp);
				
				imgcr.close();
				
				viewTag.imgSRPic.setImageBitmap(bmp);
				
			}
			else{
				//画像がなし
				viewTag.imgSRPic.setImageResource(R.drawable.noimg96);
			}
			
		}
		else{
			//ファイル存在してない
			viewTag.imgSRPic.setImageResource(R.drawable.noimg96);
		}
		
		
		//view.setBackgroundResource(R.drawable.backgradient);
		return view;
	}

	class ViewTag{
		ImageView imgSRPic;
		TextView txtNum;
		TextView txtInfo1;
		TextView txtInfo2;
		TextView txtInfo3;
		String db_id;
    
        public ViewTag(ImageView imgSRPic, TextView txtNum, TextView txtInfo1,
        		TextView txtInfo2, TextView txtInfo3){
            this.imgSRPic = imgSRPic;
            this.txtNum = txtNum;
            this.txtInfo1 = txtInfo1;
            this.txtInfo2 = txtInfo2;
            this.txtInfo3 = txtInfo3;
        }
    }
	
}
