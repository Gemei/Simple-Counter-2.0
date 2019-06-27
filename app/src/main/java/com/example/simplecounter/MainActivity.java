package com.example.simplecounter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener{
	private int screenFlag;

    GestureDetector gestureDetector;

	private boolean isTimerEnabled, isTimerRunning;
	private long MillisecondTime, StartTime, TimeBuff, UpdateTime = 0L ;
	private Handler timerHandler;
	private int Seconds, Minutes, MilliSeconds ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        if(getCheckBoxValue("darkMoodPrefName","darkMode")){
			setTheme(R.style.DarkTheme);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
		}
		else {
			setTheme(R.style.AppTheme);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
		}

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//No Title Bar
		setContentView(R.layout.activity_main);
        timerHandler = new Handler();
		gestureDetector = new GestureDetector(MainActivity.this, MainActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.menu_stayAwake).setChecked(getCheckBoxValue("screenLockPrefName","screenLock"));
		menu.findItem(R.id.menu_darkMode).setChecked(getCheckBoxValue("darkMoodPrefName","darkMode"));
		//menu.findItem(R.id.menu_switchToTimer).setChecked(getCheckBoxValue("switchMoodPrefName","switchMood"));

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			//If reset count item selected do that
			case R.id.menu_resetButton:
				//go to resetCounter method
				savePreResetValue(getCounterValue());//Get the last counter value right before reseting it
				resetCounter();
				playResetClick();
				return true;
			//If reset count item selected do that
			case R.id.menu_lastValue:
				//go to send the value of last count to sandValue method
				showValue(getPreResetValue());
				return true;
			//If help item selected do that
			case R.id.menu_settings:
				//Start the help activity
				showHelp();
				return true;
			case R.id.menu_switchToTimer:
				//Screen Wake Lock CheckBox
				if (item.isChecked()) {
					item.setChecked(false);
					//saveCheckBoxValue("switchMoodPrefName",false, "switchMood");
					switchToCounterMood();

				} else {
					item.setChecked(true);
					//saveCheckBoxValue("switchMoodPrefName",true, "switchMood");
					switchToTimerMood();
				}
				return true;
			case R.id.menu_stayAwake:
				//Screen Wake Lock CheckBox
				if (item.isChecked()) {
					item.setChecked(false);
					saveCheckBoxValue("screenLockPrefName",false, "screenLock");
					if(checkScreenLock()){
						setScreenLock();
					}
				} else {
					item.setChecked(true);
					saveCheckBoxValue("screenLockPrefName",true, "screenLock");
					if(!checkScreenLock()){
						clearScreenLock();
					}
				}
				return true;
			case R.id.menu_darkMode:
				//Screen Wake Lock CheckBox
				if (item.isChecked()) {
					item.setChecked(false);
					saveCheckBoxValue("darkMoodPrefName", false, "darkMode");
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
					restartApp();
				} else {
					item.setChecked(true);
					saveCheckBoxValue("darkMoodPrefName",true, "darkMode");
					AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
					restartApp();
				}
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void restartApp(){
		Intent currentIntent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(currentIntent);
		finish();
	}
	private void setScreenLock(){
		//Keep Screen from turning off
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		screenFlag = getWindow().getAttributes().FLAG_KEEP_SCREEN_ON;
	}
	private void clearScreenLock(){
		//Keep Screen from turning off
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		screenFlag = 0;
	}
	private boolean checkScreenLock() {
		if ((screenFlag & WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON) != 0) {
			return true;
		}
		return false;
	}

	private void showHelp() {
		/*Make A New Alert Dialog*/
		AlertDialog.Builder helpDialog = new AlertDialog.Builder(this);
		helpDialog.setTitle("Help!");
		TextView myMsg = new TextView(this);
		myMsg.setTextSize(18);
		if(!isTimerEnabled){
			myMsg.setText("Use the rocker buttons to increase and decrease the count.");
		}
		else{
			myMsg.setText("Single tap to start timer, double tap to pause and long press to clear timer.");
		}
		myMsg.setPadding(10, 8, 10, 8);
		//myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

		helpDialog.setView(myMsg).setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// User cancelled the dialog
			}
		});

		helpDialog.show();
	}

	//send value of last count to activity LastValue
	private void showValue(int value) {
        /*Make A New Alert Dialog*/
		String toString = String.valueOf(value);
		AlertDialog.Builder lastValueDialog = new AlertDialog.Builder(this);
		lastValueDialog.setTitle("Last Counter Value");
		TextView myMsg = new TextView(this);
		myMsg.setTextSize(30);
		myMsg.setText(toString);
		myMsg.setPadding(10, 10, 10, 10);
		myMsg.setGravity(Gravity.CENTER_HORIZONTAL);

		lastValueDialog.setView(myMsg).setNegativeButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// User cancelled the dialog
			}
		});

		lastValueDialog.show();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event){
		this.gestureDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		if(!isTimerRunning && isTimerEnabled){
			StartTime = SystemClock.uptimeMillis();
            timerHandler.postDelayed(runnable, 0);
			isTimerRunning=true;
		}
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent) {
		if(!isTimerRunning && isTimerEnabled){
			switchToTimerMood();
			Toast.makeText(getApplicationContext(),"Timer restarted",Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		if(isTimerRunning && isTimerEnabled) {
			TimeBuff += MillisecondTime;
            timerHandler.removeCallbacks(runnable);
			isTimerRunning=false;
			Toast.makeText(getApplicationContext(),"Paused",Toast.LENGTH_SHORT).show();
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent motionEvent) {
		return false;
	}


	//Trace the volume buttons actions
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		int action = event.getAction();
		int keyCode = event.getKeyCode();

		switch (keyCode) {
			//When clicking the upper volume button
			case KeyEvent.KEYCODE_VOLUME_UP:
				if (action == KeyEvent.ACTION_UP && !isTimerEnabled) {

					//play up click sound
					playUpClick();

					//Get text view from the xml file
					TextView countView = findViewById(R.id.text_count);

					//Get the text from the text view file in the xml and convert it to string
					String toString = countView.getText().toString();
					//Converting the String value to int value
					int toInt = Integer.parseInt(toString);
					toInt++;
					//Converting int value to String value to add to the text view
					toString = String.valueOf(toInt);
					countView.setText(toString);//text views only take string values

					saveCounterValue(toInt);//call saveCounter method and give the value toInt
				}
				return true;

			//When clicking the down volume button
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				if (action == KeyEvent.ACTION_DOWN && !isTimerEnabled) {
					//play down click sound
					playDownClick();

					//Get text view from the xml file
					TextView countView = findViewById(R.id.text_count);

					//Get the text from the text view file in the xml and convert it to string
					String toString = countView.getText().toString();
					//Converting the String value to int value
					int toInt = Integer.parseInt(toString);
					toInt--;
					//Converting int value to String value to add to the text view
					toString = String.valueOf(toInt);
					countView.setText(toString);//text views only take string values

					saveCounterValue(toInt);//call saveCounter method and give the value toInt
				}
				return true;

			default:
				return super.dispatchKeyEvent(event);//Releasing volume buttons
		}
	}


	//Switch to clock timer mood
	private void switchToTimerMood(){
		TextView timer = findViewById(R.id.text_count);

		MillisecondTime = 0L ;
		StartTime = 0L ;
		TimeBuff = 0L ;
		UpdateTime = 0L ;
		Seconds = 0 ;
		Minutes = 0 ;
		MilliSeconds = 0 ;

		timer.setText("00:00:00");

		isTimerEnabled=true;
	}

	private void switchToCounterMood(){

		timerHandler.removeCallbacks(runnable);
		TextView counter = findViewById(R.id.text_count);

		counter.setText(String.valueOf(getCounterValue()));

		isTimerEnabled=false;
	}

	public Runnable runnable = new Runnable() {

		public void run() {
			TextView timer = findViewById(R.id.text_count);

			MillisecondTime = SystemClock.uptimeMillis() - StartTime;
			UpdateTime = TimeBuff + MillisecondTime;
			Seconds = (int) (UpdateTime / 1000);
			Minutes = Seconds / 60;
			Seconds = Seconds % 60;
			MilliSeconds = (int) (UpdateTime % 100);

			timer.setText("" + String.format("%02d", Minutes) + ":"
					+ String.format("%02d", Seconds) + ":"
					+ String.format("%02d", MilliSeconds));

            timerHandler.postDelayed(this, 0);
		}

	};


	//rest the counter value on click
	private void resetCounter() {
		TextView countView = findViewById(R.id.text_count);
		if(!isTimerEnabled){
			//Set counter value to 0
			countView.setText("0");
			saveCounterValue(0);//call saveCounter method and give the value 0
		} else {
			switchToTimerMood();
		}
	}

	//up click sound method
	private void playUpClick() {
		final MediaPlayer upClickSound = MediaPlayer.create(this, R.raw.upclick);
		upClickSound.start();
    }

	//down click sound method
	private void playDownClick() {
		final MediaPlayer downClickSound = MediaPlayer.create(this, R.raw.downclick);
		downClickSound.start();
    }

	//reset click sound method
	private void playResetClick() {
		final MediaPlayer resetCounterSound = MediaPlayer.create(this, R.raw.reset);
		resetCounterSound.start();
	}

	//method to save counter value
	private void saveCounterValue(int value) {
		//Saving the counter value from the shared preferences
		SharedPreferences myPrefs = getSharedPreferences("counterValue", MODE_PRIVATE);
		//Make editor object to write to the shared preferences
		SharedPreferences.Editor editor = myPrefs.edit();
		//Saving the counter value under the name of "savedCounterValue"
		editor.putInt("savedCounterValue", value);
		//Commit the action and save the value
		editor.commit();
	}

	//method to get counter saved value
	private int getCounterValue() {
		//Retrieving the counter value from the shared preferences
		SharedPreferences myPrefs = getSharedPreferences("counterValue", MODE_PRIVATE);
		int intCountrValue = myPrefs.getInt("savedCounterValue", 0);
		return intCountrValue;
	}

	//Method to save the last counter value before reset
	private void savePreResetValue(int value) {
		//Saving the counter value from the shared preferences
		SharedPreferences myPrefs = getSharedPreferences("preResetValue", MODE_PRIVATE);
		//Make editor object to write to the shared preferences
		SharedPreferences.Editor editor = myPrefs.edit();
		//Saving the counter value under the name of "preResetValue"
		editor.putInt("preResetValue", value);
		//Commit the action and save the value
		editor.commit();
	}

	//Method to get the last counter value before reset
	private int getPreResetValue() {
		//Retrieving the counter value from the shared preferences
		SharedPreferences myPrefs = getSharedPreferences("preResetValue", MODE_PRIVATE);
		int intCountrValue = myPrefs.getInt("preResetValue", 0);
		return intCountrValue;
	}


	//Method to save the last CheckBox status
	private void saveCheckBoxValue(String PrefName, boolean value, String name) {
		//Saving the CheckBox value from the shared preferences
		SharedPreferences myPrefs = getSharedPreferences(name, MODE_PRIVATE);
		//Make editor object to write to the shared preferences
		SharedPreferences.Editor editor = myPrefs.edit();
		//Saving the CheckBox value under the name of "preResetValue"
		editor.putBoolean(PrefName + name, value);
		//Commit the action and save the value
		editor.commit();
	}

	//Method to get the last CheckBox status before reset
	private boolean getCheckBoxValue(String PrefName, String name) {
		//Retrieving the CheckBox value from the shared preferences
		SharedPreferences myPrefs = getSharedPreferences(name, MODE_PRIVATE);
		boolean CheckBoxstatus = myPrefs.getBoolean(PrefName + name, false);
		return CheckBoxstatus;
	}

	@Override
	public void onResume() {
		super.onResume();//Always call the super.onResume(); when overriding onResume() method

		if(!isTimerEnabled){
			//Retrieving the count value from the getCounterValue() method and convert it to string
			String toString = String.valueOf(getCounterValue());
			TextView countView = findViewById(R.id.text_count);
			countView.setText(toString);
		} else {
            Toast.makeText(getApplicationContext(),"Paused",Toast.LENGTH_SHORT).show();
        }
	}

	@Override()
	public void onPause() {
		super.onPause();
        if(isTimerRunning && isTimerEnabled) {
            TimeBuff += MillisecondTime;
            timerHandler.removeCallbacks(runnable);
            isTimerRunning=false;
        }
	}
}
