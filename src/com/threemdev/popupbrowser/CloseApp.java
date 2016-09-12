package com.threemdev.popupbrowser;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Process;

public class CloseApp extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		Process.killProcess(Process.myPid());
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Process.killProcess(Process.myPid());
	}

}
