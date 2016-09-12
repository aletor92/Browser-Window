package com.threemdev.popupbrowser;

import wei.mark.standout.StandOutWindow;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.Process;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class HeadsService extends Service {

	private WindowManager windowManager;
	private ImageView head;
//	private ImageView background;
	private boolean isEnabledToRestore = false;
	private boolean isEnabledToClose = false;
	private ImageView close;

	@Override
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		// Toast.makeText(getApplicationContext(),
		// "Per ripristinare tieni premuto l'icona", Toast.LENGTH_LONG).show();
		head = new ImageView(this);
		head.setImageResource(R.drawable.icona);
//
//		 background = new ImageView(this);
//		 background.setImageResource(R.drawable.black2);
//		 background.setVisibility(ImageView.GONE);

		close = new ImageView(this);
		close.setImageResource(R.drawable.black_close);
		close.setVisibility(ImageView.GONE);

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				((int) (Blank.getInstance().getScreenWidth() / 5)),
				((int) (Blank.getInstance().getScreenHeight() / 5)),
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.CENTER | Gravity.LEFT;
		params.x = 0;
		params.y = 100;
		head.bringToFront();
		if(!Blank.getInstance().isPlaying()){
			windowManager.addView(head, params);
		}

		// final WindowManager.LayoutParams paramsOpen = new
		// WindowManager.LayoutParams(
		// WindowManager.LayoutParams.WRAP_CONTENT,
		// WindowManager.LayoutParams.WRAP_CONTENT,
		// WindowManager.LayoutParams.TYPE_PHONE,
		// WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
		// PixelFormat.TRANSLUCENT);

		// paramsOpen.gravity = Gravity.BOTTOM;
		//
		// windowManager.addView(open, paramsOpen);

		final WindowManager.LayoutParams paramsClose = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		paramsClose.gravity = Gravity.BOTTOM;
		

		windowManager.addView(close, paramsClose);

		

		// chatHead.setOnLongClickListener(new OnLongClickListener() {
		//
		// @Override
		// public boolean onLongClick(View v) {
		// stopService(new Intent(ChatHeadsService.this, Browser.class));
		//
		// Blank.getInstance().setId(Blank.getInstance().getId() + 1);
		// StandOutWindow.show(ChatHeadsService.this,
		// Browser.class,Blank.getInstance().getId());
		// ChatHeadsService.this.stopSelf();
		// return true;
		// }
		//
		//
		// });
		// final int[] openCoords = new int[2];
		final int[] closeCords = new int[2];

		head.setOnTouchListener(new View.OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					 Vibrator vb = (Vibrator)   getSystemService(Context.VIBRATOR_SERVICE);
			          vb.vibrate(100);
					head.setImageResource(R.drawable.icona_touched);
					initialX = params.x;
					initialY = params.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					// open.setVisibility(ImageView.VISIBLE);
//					background.setVisibility(ImageView.VISIBLE);
					close.setVisibility(ImageView.VISIBLE);
					return false;
				case MotionEvent.ACTION_UP:
					head.setImageResource(R.drawable.icona);
					close.setVisibility(ImageView.GONE);
//					background.setVisibility(ImageView.GONE);

					if (isEnabledToClose) {
						Process.killProcess(Process.myPid());
					}
						if ((Math.abs(event.getRawX() - initialTouchX) < 3) && (Math.abs(event.getRawY() - initialTouchY) < 3)) {
							stopService(new Intent(HeadsService.this,
									Browser.class));
							Blank.getInstance().setId(Blank.getInstance().getId() + 1);
							StandOutWindow.show(HeadsService.this,
									Browser.class, Blank.getInstance().getId());
							HeadsService.this.stopSelf();
						}
//					} 
					
					return false;
				case MotionEvent.ACTION_MOVE:
					
					params.x = initialX
							+ (int) (event.getRawX() - initialTouchX);
					params.y = initialY
							+ (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(head, params);
					int[] headCoords = new int[2];
					head.getLocationOnScreen(headCoords);
					// open.getLocationOnScreen(openCoords);
					close.getLocationOnScreen(closeCords);
					// Log.d("cords", headCoords[0] + " " + headCoords[1] +
					// " - " + closeCords[0] + " " + closeCords[1]);
					Log.d("move", "event x : " + event.getRawX() +  "my x " + initialTouchX);
					paramsClose.x = params.x / 20 ;
					windowManager.updateViewLayout(close, paramsClose);
					if (headCoords[1] > (closeCords[1] - 70)) {// +40 )){
						Log.e("2", closeCords[0] + " 2 " + closeCords[1] + " "
								+ headCoords[1]);
//						windowManager.updateViewLayout(head, paramsClose);
						close.setVisibility(ImageView.INVISIBLE);
						isEnabledToClose = true;
					}else{
						windowManager.updateViewLayout(head, params);
						close.setVisibility(ImageView.VISIBLE);
						isEnabledToClose = false;
					}
					return false;
				}
				return false;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (head != null)
			windowManager.removeView(head);
	}
}
