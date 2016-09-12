package com.threemdev.popupbrowser;

import java.util.ArrayList;
import java.util.List;

import wei.mark.standout.*;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class Video extends StandOutWindow {

	private final int HEIGHT = Blank.getInstance().getScreenHeight();
	private final int WIDTH = Blank.getInstance().getScreenWidth();
	 
	@Override
	public String getAppName() {
		return "Youtube Windows";
	}

	@Override
	public int getAppIcon() {
		return R.drawable.ic_menu;
	}

	// move the window by dragging the view
	@Override
	public int getFlags(int id) {
		return super.getFlags(id) | StandOutFlags.FLAG_BODY_MOVE_ENABLE|StandOutFlags.FLAG_DECORATION_CLOSE_DISABLE|
				StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE|
				StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE;
	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return "Click To Close";
	}

	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return new Intent(Video.this,CloseApp.class);
	}

	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		Log.d("flags", "flags");
		 if(WIDTH !=0 && HEIGHT !=0){
		    	return new StandOutWindow.StandOutLayoutParams(id,WIDTH, HEIGHT, -2147483647, -2147483647);
		    }else{
		    	return new StandOutWindow.StandOutLayoutParams(id,150, 100, -2147483647, -2147483647);
		    }
	}
	
	 @Override
		public Notification getPersistentNotification(int id) {
			
		  	int icon = R.drawable.ic_launcher;
			long when = System.currentTimeMillis();
			Context c = getApplicationContext();
			String contentTitle = getPersistentNotificationTitle(id);
			String contentText = getPersistentNotificationMessage(id);
			String tickerText = String.format("%s: %s", contentTitle, contentText);

			// getPersistentNotification() is called for every new window
			// so we replace the old notification with a new one that has
			// a bigger id
			Intent notificationIntent = getPersistentNotificationIntent(id);

			PendingIntent contentIntent = null;

			if (notificationIntent != null) {
				contentIntent = PendingIntent.getService(this, 0,
						notificationIntent,
						// flag updates existing persistent notification
						PendingIntent.FLAG_UPDATE_CURRENT);
			}

			Notification notification = new Notification(icon, tickerText, when);
			notification.setLatestEventInfo(c, contentTitle, contentText,
					contentIntent);
			return notification;
		}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		final View localViewVideo = ((LayoutInflater)getSystemService("layout_inflater")).inflate(R.layout.video_loading_progress, frame, true);
		FrameLayout frameVideo = (FrameLayout)localViewVideo.findViewById(R.id.videoLayout);
		if(frameVideo.getChildCount()>1){
			frameVideo.removeAllViews();
		}
		frameVideo.addView(Blank.getInstance().getYouTubePlayerView());
		Log.d("create", "create");
//		sendBroadcast(new Intent("kill"));
	}
	
	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		  ArrayList localArrayList = new ArrayList();
		  localArrayList.add(new DropDownListItem(
				android.R.drawable.ic_menu_close_clear_cancel, "Quit "
						+ getAppName(), new Runnable() {
					@Override
					public void run() {
						
						sendBroadcast(new Intent("kill"));
						Blank.getInstance().setId(Blank.getInstance().getId() + 1);
						Blank.getInstance().setPlaying(false);
						StandOutWindow.show(Video.this,Browser.class, Blank.getInstance().getId());
						stopSelf();
					}
				}));
		return localArrayList;
	}

}
