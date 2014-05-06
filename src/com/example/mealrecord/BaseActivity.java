package com.example.mealrecord;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class BaseActivity extends Activity{
	protected MyApplication mMyApp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyApp = (MyApplication)this.getApplicationContext();
    }
    protected void onResume() {
        super.onResume();
        mMyApp.setCurrentActivity(this);
    }
    protected void onPause() {
        clearReferences();
        super.onPause();
    }
    protected void onDestroy() {        
        clearReferences();
        super.onDestroy();
    }
    
    /**
     * Intent.FLAG_ACTIVITY_CLEAR_TOPでActivityの遷移順番を管理する。
     * @param cls
     */
    public void actClearAndTop(Class<?> cls) {
    	Intent intent = new Intent(mMyApp.getApplicationContext(),cls);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
    }
    
    private void clearReferences(){
        Activity currActivity = mMyApp.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this))
        	mMyApp.setCurrentActivity(null);
    }

}
