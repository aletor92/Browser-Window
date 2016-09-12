package com.threemdev.popupbrowser;

import android.util.Log;
import java.util.ArrayList;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class Blank
{
  private static Blank istance = null;
  private int id;
  private String CLICK_TO_CLOSE;
  private String CLICK_TO_RESTORE;
  private String URL = null;
  private ArrayList<String> URL__HISTORY = new ArrayList();
  private int screenHeight;
  private int screenWidth;
  private YouTubePlayer.Provider youTubePlayerProvider;
  private YouTubePlayer youTubePlayer;
  private YouTubePlayerView youTubePlayerView;
  private int youtubeId = 2000;
  private boolean isPlaying;
  private int orientation;
  
  
  public static Blank getInstance()
  {
    if (istance == null);
    try
    {
      if (istance == null)
        istance = new Blank();
      return istance;
    }
    finally
    {
    }
  }

  public String getCLICK_TO_CLOSE()
  {
    return this.CLICK_TO_CLOSE;
  }

  public String getCLICK_TO_RESTORE()
  {
    return this.CLICK_TO_RESTORE;
  }

  public int getScreenHeight()
  {
    return this.screenHeight;
  }

  public int getScreenWidth()
  {
    return this.screenWidth;
  }

  public String getURL()
  {
    return this.URL;
  }

  public ArrayList<String> getURL__HISTORY()
  {
    return this.URL__HISTORY;
  }

  public void setCLICK_TO_CLOSE(String paramString)
  {
    this.CLICK_TO_CLOSE = paramString;
  }

  public void setCLICK_TO_RESTORE(String paramString)
  {
    this.CLICK_TO_RESTORE = paramString;
  }

  public void setScreenHeight(int paramInt)
  {
    this.screenHeight = paramInt;
  }

  public void setScreenWidth(int paramInt)
  {
    this.screenWidth = paramInt;
  }

  public void setURL(String paramString)
  {
    this.URL = paramString;
  }

  public void setURL__HISTORY(ArrayList<String> paramArrayList)
  {
    this.URL__HISTORY = paramArrayList;
  }

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public YouTubePlayer.Provider getYouTubePlayerProvider() {
	return youTubePlayerProvider;
}

public void setYouTubePlayerProvider(YouTubePlayer.Provider youTubePlayerProvider) {
	this.youTubePlayerProvider = youTubePlayerProvider;
}

public YouTubePlayer getYouTubePlayer() {
	return youTubePlayer;
}

public void setYouTubePlayer(YouTubePlayer youTubePlayer) {
	this.youTubePlayer = youTubePlayer;
}

public YouTubePlayerView getYouTubePlayerView() {
	return youTubePlayerView;
}

public void setYouTubePlayerView(YouTubePlayerView youTubePlayerView) {
	this.youTubePlayerView = youTubePlayerView;
}

public int getYoutubeId() {
	return youtubeId;
}

public void setYoutubeId(int youtubeId) {
	this.youtubeId = youtubeId;
}

public boolean isPlaying() {
	return isPlaying;
}

public void setPlaying(boolean isPlaying) {
	this.isPlaying = isPlaying;
}

public int getOrientation() {
	return orientation;
}

public void setOrientation(int orientation) {
	this.orientation = orientation;
}
}

/* Location:           C:\Users\Administrator\Desktop\APKTool\
 * Qualified Name:     com.threemdev.popupbrowser.Blank
 * JD-Core Version:    0.6.2
 */