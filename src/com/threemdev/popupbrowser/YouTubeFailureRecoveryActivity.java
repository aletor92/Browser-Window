package com.threemdev.popupbrowser;

import android.app.Dialog;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;

public abstract class YouTubeFailureRecoveryActivity extends YouTubeBaseActivity
  implements YouTubePlayer.OnInitializedListener
{
  private static final int RECOVERY_DIALOG_REQUEST = 1;

  protected abstract YouTubePlayer.Provider getYouTubePlayerProvider();

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    if (paramInt1 == 1)
      getYouTubePlayerProvider().initialize("AIzaSyCYFlVV6hvlRbR19O0MdDN2qPPkjk51CKo", this);
  }

  public void onInitializationFailure(YouTubePlayer.Provider paramProvider, YouTubeInitializationResult paramYouTubeInitializationResult)
  {
    if (paramYouTubeInitializationResult.isUserRecoverableError())
    {
      paramYouTubeInitializationResult.getErrorDialog(this, 1).show();
      return;
    }
    String str = getString(2131296302);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramYouTubeInitializationResult.toString();
    Toast.makeText(this, String.format(str, arrayOfObject), 1).show();
  }
}

/* Location:           C:\Users\Administrator\Desktop\APKTool\
 * Qualified Name:     com.digitalportal.popupplayeryoutube.YouTubeFailureRecoveryActivity
 * JD-Core Version:    0.6.2
 */