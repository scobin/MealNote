package com.example.mealrecord;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context context;
    private Activity mCurrentActivity = null;

    
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        MyApplication.context = getApplicationContext();
    }
    
    
    public Activity getCurrentActivity(){
          return mCurrentActivity;
    }
    
    public void setCurrentActivity(Activity mCurrentActivity){
          this.mCurrentActivity = mCurrentActivity;
    }
    
    
    public static MyApplication getInstance() {
        return instance;
    }
    
    public synchronized static Context getAppContext() {
        return context;
    }
    

}
