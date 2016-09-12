package com.threemdev.popupbrowser;

import java.util.ArrayList;
import java.util.List;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Browser extends StandOutWindow
  implements View.OnClickListener
{
  private final int HEIGHT = Blank.getInstance().getScreenHeight();
  private FrameLayout mFrame;
  private String HOMEPAGE = "http://www.google.it/";
  private final int WIDTH = Blank.getInstance().getScreenWidth();
  private ImageView btnBack;
  private ImageView btnNext;
  private int mId;
  private View mView;
  private String currentUrl;
  private ProgressBar progress;
  private ImageView refresh;
  private boolean switchUserAgent = false;
  private String titleString = "";
  private CustomEditText urlSearch;
  private WebView webView;
  private FrameLayout mMainContent ;
  
  private View mCustomView;
  private CustomViewCallback mCustomViewCallback;
  private FrameLayout mCustomViewContainer;
  private FrameLayout mContentView;

  
  @Override
	public void onCreate() {
			super.onCreate();
			Log.w("init", "init");
		    try{
		    	getWindow(Blank.getInstance().getId()).setVisibility(LinearLayout.VISIBLE);
		    	createAndAttachView(mId, mFrame);
		    }catch (Exception e) {
		    	e.printStackTrace();
			}
			
	}
  private void init(View paramView, final int id)
  {
    ArrayAdapter localArrayAdapter = new ArrayAdapter(this,  android.R.layout.simple_dropdown_item_1line, Blank.getInstance().getURL__HISTORY());
    if (Blank.getInstance().getURL() != null)
      this.HOMEPAGE = Blank.getInstance().getURL();
    mContentView = (FrameLayout) paramView.findViewById(R.id.main_content);
    this.refresh = ((ImageView)paramView.findViewById(R.id.refresh));
    this.webView = ((WebView)paramView.findViewById(R.id.webView1));
    this.progress = ((ProgressBar)paramView.findViewById(R.id.progressBar1));
    this.btnBack = ((ImageView)paramView.findViewById(R.id.back));
    this.btnNext = ((ImageView)paramView.findViewById(R.id.next));
    this.progress.setVisibility(8);
    this.progress.setProgress(10);
    mMainContent = (FrameLayout)paramView.findViewById(R.id.main_content);
    this.urlSearch = ((CustomEditText)paramView.findViewById(R.id.editText1));
    this.urlSearch.setAdapter(localArrayAdapter);
    this.webView.setWebChromeClient(new MyWebChromeClient());
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setLoadsImagesAutomatically(true);
    webView.getSettings().setAppCacheEnabled(true);
    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
    webView.setHorizontalScrollBarEnabled(false);
    webView.setVerticalScrollBarEnabled(false);
    webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
    webView.getSettings().setUseWideViewPort(true);
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.getSettings().setJavaScriptEnabled(true);
    if (Build.VERSION.SDK_INT < 8) {
        webView.getSettings().setPluginsEnabled(true);
    } else {
        webView.getSettings().setPluginState(PluginState.ON);
    }
//    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
      this.webView.getSettings().setAppCacheEnabled(true);
      this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
      this.webView.getSettings().setSaveFormData(false);
      this.webView.getSettings().setSavePassword(false);
      this.webView.getSettings().setSupportMultipleWindows(false);
      // enable navigator.geolocation 
      this.webView.getSettings().setGeolocationEnabled(true);
      this.webView.getSettings().setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
	    
	  // enable Web Storage: localStorage, sessionStorage
      this.webView.getSettings().setDomStorageEnabled(true);
	    
      if(switchUserAgent){
    	  webView.getSettings().setUserAgentString("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/534.36 (KHTML, like Gecko) Chrome/13.0.766.0 Safari/534.36");
    	  webView.reload();
      }
      else{  
    		  webView.getSettings().setUserAgentString("Mozilla/5.0 (Linux; U; Android 2.1; Nexus One Build/ERD62) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Mobile Safari/530.17");
    		  this.webView.reload();
      }
      this.webView.loadUrl(this.HOMEPAGE);
      this.urlSearch.setText(this.HOMEPAGE);
      this.webView.setDownloadListener(new DownloadListener()
      {
        public void onDownloadStart(String paramAnonymousString1, String paramAnonymousString2, String paramAnonymousString3, String paramAnonymousString4, long paramAnonymousLong)
        {
          Browser.closeAll(Browser.this, Browser.class);
          Uri.parse(paramAnonymousString1);
          Intent localIntent = new Intent("android.intent.action.VIEW");
          localIntent.setData(Uri.parse(paramAnonymousString1));
          localIntent.setFlags(268435456);
          Browser.this.startActivity(localIntent);
        }
      });
      this.webView.setWebViewClient(new WebViewClient()
      {
    	  
    	@Override
    	public WebResourceResponse shouldInterceptRequest(WebView view,
    			String url) {
    		
    		Log.e("request", url);
    		return super.shouldInterceptRequest(view, url);
    		
    	}
        public void onPageFinished(WebView paramAnonymousWebView, String paramAnonymousString)
        {
        if(!paramAnonymousString.contains("vnd.youtube")){
          Browser.this.progress.setProgress(paramAnonymousWebView.getProgress());
          Browser.this.titleString = Browser.this.webView.getTitle();
          if (!Browser.this.webView.canGoBack())
          {
            Browser.this.btnBack.setEnabled(false);
          }else{
        	  Browser.this.btnBack.setEnabled(true);
          }
          if (Browser.this.webView.canGoForward()){
        	  Browser.this.btnNext.setEnabled(true);
          }else{
        	  Browser.this.btnNext.setEnabled(false);
          }
            
            currentUrl = webView.getUrl();
            Browser.this.setTitle(id, Browser.this.titleString);
            new Handler().postDelayed(new Runnable()
            {
              public void run()
              {
                Browser.this.progress.setVisibility(8);
              }
            }
            , 1500L);
//            if (paramAnonymousString.contains("data:text/html,<html><head><title>ABOUT</title></head><body style=\"background-color:#F0F0F0\"><img src=\"data:image/png;base64,iVBORw0KGgoAAAANSUhEU"))
//              paramAnonymousString = "BROWSER WINDOW ABOUT";
            if (paramAnonymousString.length() >= 200){
            	Browser.this.urlSearch.setText(paramAnonymousString.substring(0, 200));
            }else{
            	Browser.this.urlSearch.setText(paramAnonymousString);
            }
        }
            Log.d("url", ""+webView.getUrl());
        }

        public boolean shouldOverrideUrlLoading(WebView paramAnonymousWebView, String paramAnonymousString)
        {
//        	if(paramAnonymousWebView.getUrl().contains("youtube.com") && !paramAnonymousWebView.getUrl().contains("nomobile=1")){
//        		Browser.this.webView.loadUrl("http://www.youtube.com/?nomobile=1");
//        	}
        	if(paramAnonymousString.contains("vnd.youtube")){
//        		startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(paramAnonymousString)).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
//        		startActivity(new Intent(Browser.this,YoutubeGeneratorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("endTask", true));
        		String youtubeId = paramAnonymousString.substring(12,paramAnonymousString.indexOf("?"));
        		Log.d("id", youtubeId);
        		Blank.getInstance().setURL("http://m.youtube.com");
        		startActivity(new Intent(Browser.this,YoutubeGeneratorActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("ID", youtubeId));
        		btnBack.performClick();
//        		stopSelf();
//        		hide(id);
        		
        		Log.e("youtube", paramAnonymousWebView.getOriginalUrl());
        	}else{
	        	Browser.this.urlSearch.setText(paramAnonymousString);
	        	paramAnonymousWebView.loadUrl(paramAnonymousString);
	        	Browser.this.setTitle(id, Browser.this.webView.getTitle());
        	}
         return false;
         
          
        }
      });
      this.webView.setHapticFeedbackEnabled(true);
      this.urlSearch.setDrawableClickListener(new CustomEditText.DrawableClickListener()
      {
        public void onClick(CustomEditText.DrawableClickListener.DrawablePosition paramAnonymousDrawablePosition)
        {
          Browser.this.urlSearch.setText("");
        }
      });
      this.urlSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
        {
          String str = null;
//          if (paramAnonymousInt == 2)
//          {
            str = Browser.this.urlSearch.getText().toString();
            if ((str.contains(".")) && ((!str.toLowerCase().contains("http://")) && (!str.toLowerCase().contains("https://")))){
            Browser.this.webView.loadUrl("http://" + str);
            
          	}else if (!str.contains(".")){
              Browser.this.webView.loadUrl("http://www.google.it/search?q=" + str);
          	}else{
              Browser.this.webView.loadUrl(str);
          	}
          Log.d("TEXT", str);
          	Browser.this.progress.setVisibility(0);
          	return true;
        }
      });
      
      
      
      this.btnBack.setOnClickListener(this);
      this.btnNext.setOnClickListener(this);
      this.refresh.setOnClickListener(this);
      
    
  }

  public void createAndAttachView(final int paramInt, FrameLayout paramFrameLayout)
  {
//	if(Blank.getInstance().isPlaying()){
		
//	}else{
    final View localView = ((LayoutInflater)getSystemService("layout_inflater")).inflate(R.layout.main, paramFrameLayout, true);
    this.mView = localView;
    this.mId = paramInt;
    this.mFrame = paramFrameLayout;
    Blank.getInstance().setId(paramInt);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        Browser.this.init(localView, paramInt);
      }
    }
    , 1500L);
//	}
  }

  public int getAppIcon()
  {
    return R.drawable.ic_menu;
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

  public String getAppName()
  {
    return "Browser Window ";
  }

  public Animation getCloseAnimation(int paramInt)
  {
    return super.getCloseAnimation(paramInt);
  }

  public List<StandOutWindow.DropDownListItem> getDropDownItems(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    if (this.switchUserAgent){
      localArrayList.add(new StandOutWindow.DropDownListItem(android.R.drawable.ic_menu_preferences, "Mobile Mode", new Runnable()
      {
        public void run()
        {
        	switchUserAgent = false;
        	Blank.getInstance().setURL(webView.getUrl());
        	Browser.this.init(Browser.this.mView, Browser.this.mId);
        }
      }));
    }else{
    	localArrayList.add(new StandOutWindow.DropDownListItem( android.R.drawable.ic_menu_preferences, "Desktop Mode", new Runnable()
    	 {
            public void run()
            {
            	switchUserAgent = true;
            	Blank.getInstance().setURL(webView.getUrl());
            	Browser.this.init(Browser.this.mView, Browser.this.mId);
              
            }
          }));
    }
     localArrayList.add(new StandOutWindow.DropDownListItem( android.R.drawable.ic_menu_help, "About", new Runnable()
      {
        public void run()
        {
        	Browser.this.webView.loadUrl("file:///android_asset/www/about/index.html");
//          sBrowser.this.webView.loadData("<html><head><title>ABOUT</title></head><body style=\"background-color:#F0F0F0\"><img src=\"data:image/png;base64,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\" style=\"float: left;\"> <h1 style=\"font-family: cursive;color: #0F3551;\">Browser Window </h1><br><br><h1 style=\"font-family:cursive;color:#0f3551;\">Created By 3M-Dev</h1><h1 style=\"color: #0F3551;font-family: cursive;\">Developer</h1>  <h3 style=\"color: #0F3551;font-family: cursive;\">Alexandro Torregrossa</h3> <br>\t\t<h1 style=\"color: #0F3551;font-family: cursive;\">Graphic designer</h1> <h3 style=\"color: #0F3551;font-family: cursive;\">Andrea Di Giacomo</h3><br></body></html>", "text/html", "Unicode");
        }
      }));
      return localArrayList;
  }

  public int getFlags(int paramInt)
  {
	if(Blank.getInstance().isPlaying()){
		return super.getFlags(paramInt) | StandOutFlags.FLAG_BODY_MOVE_ENABLE |  StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE | StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE ;
	}else{
		return super.getFlags(paramInt) | StandOutFlags.FLAG_BODY_MOVE_ENABLE | StandOutFlags.FLAG_DECORATION_SYSTEM | StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP | StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE | StandOutFlags.FLAG_WINDOW_HIDE_ENABLE;
	}
  }

  public int getHiddenIcon()
  {
    return 17301569;
  }

  @Override
	public synchronized void hide(int id) {
		// TODO Auto-generated method stub
	  	getWindow(id).setVisibility(LinearLayout.GONE);
		startService(new Intent(this,HeadsService.class));
		Blank.getInstance().setURL(currentUrl);
		this.closeAll();
		this.onDestroy();
		
	}
  
  public Intent getHiddenNotificationIntent(int paramInt)
  {
	 
//	  return StandOutWindow.getShowIntent(Browser.this, Browser.class, paramInt);
	 return null;
	 
  }

  public String getHiddenNotificationMessage(int paramInt)
  {
    return Blank.getInstance().getCLICK_TO_RESTORE() + " " + this.titleString;
  }

  public String getHiddenNotificationTitle(int paramInt)
  {
    return getAppName() + " minimize";
  }

  public Animation getHideAnimation(int paramInt)
  {
    return AnimationUtils.loadAnimation(this, 17432579);
  }

  public StandOutWindow.StandOutLayoutParams getParams(int paramInt, Window paramWindow)
  {
    Log.e("CR7", "width" + this.WIDTH + "height" + this.HEIGHT);
    Log.e("id", "id " + paramInt);
    
    if(this.WIDTH !=0 && this.HEIGHT !=0){
    	return new StandOutWindow.StandOutLayoutParams(paramInt,this.WIDTH, this.HEIGHT, -2147483647, -2147483647);
    }else{
    	return new StandOutWindow.StandOutLayoutParams(paramInt,150, 100, -2147483647, -2147483647);
    }
  }

//  @Override
//	public synchronized void close(int id) {
//	  	startService(new Intent(this,CloseApp.class));
//	  	Blank.getInstance().setURL(currentUrl);
//		this.closeAll();
//		this.onDestroy();
//
//	}
//  
  public Intent getPersistentNotificationIntent(int paramInt)
  {
    return new Intent(Browser.this,CloseApp.class);
  }

  public String getPersistentNotificationMessage(int paramInt)
  {
    return "Click to Close";
  }

  public String getPersistentNotificationTitle(int paramInt)
  {
    return getAppName();
  }

  public Animation getShowAnimation(int paramInt)
  {
    if (isExistingId(paramInt))
      return AnimationUtils.loadAnimation(this, 17432578);
    return super.getShowAnimation(paramInt);
  }

  public void onClick(View paramView)
  {
    switch (paramView.getId())
    {
    case R.id.back:
    	this.webView.goBack();
    	break;
    case R.id.next:
    	this.webView.goForward();
        break;
    case R.id.refresh:
    	this.webView.reload();
    	break;
    default:
    	break;
     }
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    try{
    	hide(mId);
    }catch (Exception e) {
	}
  }
  
  private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (Browser.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
  
  
  private class MyWebChromeClient extends WebChromeClient {
		private View videoView ;
		private View mVideoProgressView;
		private VideoView video;
		
		
	public void onSelectionStart(WebView view) {
	    // Parent class aborts the selection, which seems like a terrible default.
	    //Log.i("DroidGap", "onSelectionStart called");
	}
  	
  	@Override
		public void onShowCustomView(View view,WebChromeClient.CustomViewCallback callback)
		{
			Log.i("show", "here in on ShowCustomView");
			
////			mCustomView = view;
//			//vedere view
//			if (view instanceof FrameLayout) {
//				videoView = view;
////				Log.d("",""+ webView.getWindowToken());
//	            FrameLayout frame = (FrameLayout) videoView;
////	            webView.setVisibility(WebView.GONE);
////	            if(frame.getFocusedChild() instanceof VideoView){
////	            	video = (VideoView)frame.getFocusedChild();
//	            
//	            webView.addView(frame);
//	            mCustomViewCallback = callback;
////	            }
////	            mFrame.setLayoutParams(new LayoutParams(frame.getWidth(),frame.getHeight()));
//			}
		}
//		
		@Override
		public void onHideCustomView() {
			
//		if (videoView == null)
//			return;	       
//			
//		// Hide the custom view.
////		mCustomView.setVisibility(View.GONE);
//		
//		// Remove the custom view from its container.
//		webView.removeView(videoView);
//		videoView = null;
//		webView.setVisibility(WebView.VISIBLE);
//		mCustomViewCallback.onCustomViewHidden();
		}

		@Override
		public View getVideoLoadingProgressView() {
			//Log.i(LOGTAG, "here in on getVideoLoadingPregressView");
			
	        if (mVideoProgressView == null) {
	            LayoutInflater inflater = LayoutInflater.from(Browser.this);
	            mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
	        }
	        return mVideoProgressView; 
		}
//  	
		public void onProgressChanged(WebView view, int progress)   
        {
         //Make the bar disappear after URL is loaded, and changes string to Loading...
         Browser.this.progress.setProgress(progress ); //Make the bar disappear after URL is loaded
         Browser.this.progress.setVisibility(0);
        }
		@Override
		public void onReceivedTouchIconUrl(WebView view, String url,
			boolean precomposed) {
			Log.d("urlTouch", "url " + url );
		super.onReceivedTouchIconUrl(view, url, precomposed);
		}
  }
}