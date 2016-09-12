package com.threemdev.popupbrowser;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.ui.Window;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.ErrorReason;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.PlaylistEventListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeGeneratorActivity extends YouTubeFailureRecoveryActivity
implements YouTubePlayer.OnInitializedListener
{

	private YoutubeGeneratorActivity.MyPlaybackEventListener playbackEventListener;
	  private YoutubeGeneratorActivity.MyPlayerStateChangeListener playerStateChangeListener;
	  YouTubePlayerView youTubePlayerView;
	  String youtubeId;
	  private YouTubePlayer player;
	  private Bundle state;
	private int screenWidth;
	private int screenHeight;

	  private String formatTime(int millis) {
		    int seconds = millis / 1000;
		    int minutes = seconds / 60;
		    int hours = minutes / 60;

		    return (hours == 0 ? "" : hours + ":")
		        + String.format("%02d:%02d", minutes % 60, seconds % 60);
		  }
	  
protected YouTubePlayer.Provider getYouTubePlayerProvider()
{
  return Blank.getInstance().getYouTubePlayerProvider();
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

public void onConfigurationChanged(Configuration paramConfiguration)
{
  super.onConfigurationChanged(paramConfiguration);
  getScreenSize();
  Video video = new Video();
  int id = Blank.getInstance().getYoutubeId();
  Window window = video.getWindow(id);
  video.getParams(id, window);
}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onSaveInstanceState(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(arg0);
		state = arg0;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void onCreate(Bundle paramBundle)
	{
	  super.onCreate(paramBundle);
	  
	}
//	@Override
//	protected void onNewIntent(Intent intent) {
//		// TODO Auto-generated method stub
//		super.onNewIntent(intent);
//		if(getIntent().getBooleanExtra("endTask",false)){
//			  this.finish();
//		  }
//	}
	public void onInitializationSuccess(YouTubePlayer.Provider paramProvider, YouTubePlayer paramYouTubePlayer, boolean paramBoolean)
	{
	  Blank.getInstance().setYouTubePlayer(paramYouTubePlayer);
	  paramYouTubePlayer.setPlayerStateChangeListener(this.playerStateChangeListener);
	  paramYouTubePlayer.setPlaybackEventListener(this.playbackEventListener);
	  paramYouTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
	  paramYouTubePlayer.setManageAudioFocus(true);
	  paramYouTubePlayer.setShowFullscreenButton(false);
	  paramYouTubePlayer.cueVideo(getIntent().getStringExtra("ID"));
	  

}

private final BroadcastReceiver kill = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
              finish();                                   
        }
};
public void onStart()
{
  super.onStart();
  registerReceiver(kill, new IntentFilter("kill"));
  if(getIntent().getStringExtra("ID") != null && !getIntent().getStringExtra("ID").equals("")){
	  if(!Blank.getInstance().isPlaying())
	  	{
		  StandOutWindow.close(this, Browser.class, Blank.getInstance().getId());
	  	}
	  this.youTubePlayerView = new YouTubePlayerView(this);
	    this.youTubePlayerView.initialize("AIzaSyDgvHU9uWi8WMKm4EvhSUqnnjfl-Uhdq4c", this);
	    Blank.getInstance().setYouTubePlayerView(this.youTubePlayerView);
	    this.playerStateChangeListener = new MyPlayerStateChangeListener();
	    this.playbackEventListener = new MyPlaybackEventListener();
	    if(!Blank.getInstance().isPlaying())
	  	{
	    	
	    Blank.getInstance().setYoutubeId(Blank.getInstance().getYoutubeId() + 1);
	    StandOutWindow.show(this, Video.class, Blank.getInstance().getYoutubeId());
	    Blank.getInstance().setPlaying(true);
	  	}
	    else{
	  		stopService(new Intent(YoutubeGeneratorActivity.this,Video.class));
	  		Blank.getInstance().setYoutubeId(Blank.getInstance().getYoutubeId() + 1);
	 	    StandOutWindow.show(this, Video.class, Blank.getInstance().getYoutubeId());
	  	}
	  }else{
		  this.finish();
	  }
	  if(getIntent().getBooleanExtra("endTask",false)){
		  this.finish();
	  }
  }


public void onStop()
{
  super.onStop();
//  dpLogger.d("PopUpPlayerFloatingWindowActivity: OnStop Called");
//  EasyTracker.getInstance().activityStop(this);
}

//public void playSelectedVideo(VideoVO paramVideoVO)
//{
//  SessionVO.userVO.currentlyPlayingPlayListVO = SessionVO.userVO.currentSearchPlayListVO;
//  SessionVO.userVO.currentlyPlayingPlayListID = "search_playlist";
//  SessionVO.userVO.currentSearchPlayListVO.setCurrentPlayListIndexByObject(paramVideoVO);
//  SessionVO.userVO.currentlyPlayingPlayListVO.setCurrentPlayingVideoVO(paramVideoVO);
//  SessionVO.userVO.currentlyPlayingPlayListVO.setCurrentPlayListIndexByObject(paramVideoVO);
//  SessionVO.userVO.currentlyShowingPlayListVO = SessionVO.userVO.currentlyPlayingPlayListVO;
//  UserVO.searchVideoListAdapter.notifyDataSetChanged();
//  UserVO.searchVideoListView.setSelection(SessionVO.userVO.currentlyPlayingPlayListVO.getCurrentPlayListIndex());
//  dpLogger.d("GOT REQUEST TO PLAY VIDEO...." + paramVideoVO.getVideoID());
//  Uri localUri = Uri.parse("popupplayer://" + MultiWindowService.class + '/' + 1000);
//  Intent localIntent = new Intent(getApplicationContext(), MultiWindowService.class).putExtra("id", 1000).setAction("RESTORE").setData(localUri);
//  localIntent.setFlags(268435456);
//  localIntent.putExtra("subIntent", "playVideo");
//  getApplicationContext().startService(localIntent);
//  UserVO.playVideo(paramVideoVO.getVideoID());
//  SessionVO.userVO.updateSearchVideoListAdapter();
//  if (UserVO._window != null)
//    UserVO._window.setWindowTitle("PopUp Player: " + paramVideoVO.getVideoTitle());
//  EasyTracker.getTracker().sendEvent("popupplayeryoutube", "FlowatingWindowActivity_playVideo=", paramVideoVO.getVideoID() + " title=" + paramVideoVO.getVideoTitle(), Long.valueOf(1L));
//}

@Override
public void onInitializationFailure(Provider arg0,
		YouTubeInitializationResult arg1) {
	
	// TODO Auto-generated method stub
}
	 private final class MyPlaylistEventListener implements PlaylistEventListener {
		    @Override
		    public void onNext() {
		      
		    }

		    @Override
		    public void onPrevious() {
		      
		    }

		    @Override
		    public void onPlaylistEnded() {
		      
		    }
		  }

		  private final class MyPlaybackEventListener implements PlaybackEventListener {
		    String playbackState = "NOT_PLAYING";
		    String bufferingState = "";
		    @Override
		    public void onPlaying() {
		      playbackState = "PLAYING";
		      
		      
		    }

		    @Override
		    public void onBuffering(boolean isBuffering) {
		      bufferingState = isBuffering ? "(BUFFERING)" : "";
		      
		      
		    }

		    @Override
		    public void onStopped() {
		      playbackState = "STOPPED";
		      
		      
		    }

		    @Override
		    public void onPaused() {
		      playbackState = "PAUSED";
		      
		      
		    }

		    @Override
		    public void onSeekTo(int endPositionMillis) {
		      
		         
		    }
		  }

		  private final class MyPlayerStateChangeListener implements PlayerStateChangeListener {
		    String playerState = "UNINITIALIZED";

		    @Override
		    public void onLoading() {
		      playerState = "LOADING";
		      
		      
		    }

		    @Override
		    public void onLoaded(String videoId) {
		      playerState = String.format("LOADED %s", videoId);
		      
		      
		    }

		    @Override
		    public void onAdStarted() {
		      playerState = "AD_STARTED";
		      
		      
		    }

		    @Override
		    public void onVideoStarted() {
		      playerState = "VIDEO_STARTED";
		      
		      
		    }

		    @Override
		    public void onVideoEnded() {
		      playerState = "VIDEO_ENDED";
		      
		      
		    }

		    @Override
		    public void onError(ErrorReason reason) {
		      playerState = "ERROR (" + reason + ")";
		      Log.e("error", playerState);
		      if (reason == ErrorReason.UNEXPECTED_SERVICE_DISCONNECTION) {
		        // When this error occurs the player is released and can no longer be used.
		        player = null;
//		        setControlsEnabled(false);
		      }
		      
		      
		    }

		  }

		  private static final class ListEntry {

		    public final String title;
		    public final String id;
		    public final boolean isPlaylist;

		    public ListEntry(String title, String videoId, boolean isPlaylist) {
		      this.title = title;
		      this.id = videoId;
		      this.isPlaylist = isPlaylist;
		    }

		    @Override
		    public String toString() {
		      return title;
		    }

		  }
		  @Override
		protected void onDestroy() {
			super.onDestroy();
			unregisterReceiver(kill);
		}
}
