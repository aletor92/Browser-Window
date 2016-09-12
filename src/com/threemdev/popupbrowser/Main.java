package com.threemdev.popupbrowser;

import java.util.ArrayList;

import wei.mark.standout.StandOutWindow;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

public class Main extends Activity
{
  private int screenHeight;
  private int screenWidth;

  public void getBrowserList()
  {
	  ArrayList localArrayList = new ArrayList();
	  Cursor mCur = managedQuery(android.provider.Browser.BOOKMARKS_URI, android.provider.Browser.HISTORY_PROJECTION, 
              null, null, null);
		mCur.moveToFirst();
		if (mCur.moveToFirst() && mCur.getCount() > 0) {
		while (mCur.isAfterLast() == false) {
			String str = mCur.getString(android.provider.Browser.HISTORY_PROJECTION_URL_INDEX);
			if(str!=null && !str.equals("")){
				if (str.toLowerCase().contains("http://www.")){
				      str = str.substring(11, str.length());
				}else if (str.toLowerCase().contains("https://www.")){
					str = str.substring(12, str.length());
				} else if (str.toLowerCase().indexOf("www.") == 0){
			        str = str.substring(4, str.length());
				}
				Log.d("URL", str);
				localArrayList.add(str);
			}
		    mCur.moveToNext();
		}
		Blank.getInstance().setURL__HISTORY(localArrayList);
		}
	}
    

  public void getScreenSize()
  {
	  if (Build.VERSION.SDK_INT >= 11) {
	        Point size = new Point();
	        try {
	            this.getWindowManager().getDefaultDisplay().getRealSize(size);
	            screenWidth = size.x;
	            screenHeight = size.y;
	        } catch (NoSuchMethodError e) {
	            Log.i("error", "it can't work");
	        }

	    } else {
	        DisplayMetrics metrics = new DisplayMetrics();
	        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	        screenWidth = metrics.widthPixels;
	        screenHeight = metrics.heightPixels;
	    }
	  if(getResources().getConfiguration().orientation == getResources().getConfiguration().ORIENTATION_PORTRAIT){
		  Blank.getInstance().setOrientation(getResources().getConfiguration().ORIENTATION_PORTRAIT);
		  Blank.getInstance().setScreenWidth((int)(this.screenWidth / 1.1D));
		  Blank.getInstance().setScreenHeight(this.screenHeight / 2);
	  }else{
		  Blank.getInstance().setOrientation(getResources().getConfiguration().ORIENTATION_LANDSCAPE);
		  Blank.getInstance().setScreenWidth((int)(this.screenWidth / 2D));
		  Blank.getInstance().setScreenHeight((int) (this.screenHeight / 1.5));
	  }
  }

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    Blank.getInstance().setCLICK_TO_CLOSE(getResources().getString(R.string.close));
    Blank.getInstance().setCLICK_TO_RESTORE(getResources().getString(R.string.click_to_restore));
    getBrowserList();
//    StandOutWindow.scloseAll(this, Browser.class);
    stopService(new Intent(Main.this, Browser.class));
    if (getIntent() != null)
    {
      String str = getIntent().getDataString();
      if ((str != null) && (!str.equals("")))
      {
        stopService(new Intent(this, Browser.class));
        Blank.getInstance().setURL(str);
      }else if(getIntent().getStringExtra("URL")!= null && !getIntent().getStringExtra("URL").equals("")){
    	  stopService(new Intent(this, Browser.class));
          Blank.getInstance().setURL(str);
    	  str = getIntent().getStringExtra("URL");
      }
      try{
    	  getScreenSize();
      }catch(Exception e ){
    	  e.printStackTrace();
      }
      if(isHeadRunning()){
  		stopService(new Intent(Main.this,HeadsService.class));
  	}
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
        	
        	StandOutWindow.show(Main.this, Browser.class, Blank.getInstance().getId());
	        Main.this.finish();
        }
      }
      , 500);
    }
  }
  private boolean isBrowserRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (Browser.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
  private boolean isHeadRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (HeadsService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
}

/* Location:           C:\Users\Administrator\Desktop\APKTool\
 * Qualified Name:     com.threemdev.popupbrowser.Main
 * JD-Core Version:    0.6.2
 */