package com.dylam.mathlete;

import java.util.Random;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.dylam.mathlete.UserAnswerContract.UserAnswer;


abstract public class BaseExerciseFragment extends Fragment{
	// UI elements
	public WebView mWebView;
	public ProgressBar mCountdownBar;
	public EditText mUserInput;
	public Button mButton;
	public Vibrator mVibrator;
	
	// Timer that drives the progress bar
	private CountDownTimer mTimer;
	public int maxTimeInSecs;
	
	// Backend elements
	public int num_correct, num_total;
	public int timeRemainingInSecs;
	
	// Database elements
	public SQLiteDatabase mDb;
	public Time sessionStartDatetime, problemStartDatetime, submissionDatetime;
	
	public String TAG = "BaseExerciseFragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.base_exercise_activity, container, false);

		// Initialize question generating elements
		rand = new Random();
		
		// Bind UI elements. In fragments, onCreateView sets up the UI and is
		// called before onCreate().
		mWebView = (WebView)v.findViewById(R.id.webView1);
		mCountdownBar = (ProgressBar)v.findViewById(R.id.countdownBar);		
		mButton = (Button)v.findViewById(R.id.button1);
		mUserInput = (EditText)v.findViewById(R.id.user_answer_input);
		mCountdownBar.setMax(maxTimeInSecs * 1000);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mUserInput
			.setOnEditorActionListener(new OnEditorActionListener(){
				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					boolean handled = false;
					
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						onSubmit();
						handled = true;
					}
					
					return handled;
				}
			});

		mButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				onSubmit();
			}
		});
		
		// Pop up the input keyboard
		// TODO: add a listener to switch focus and display
		// keyboard after focus has shifted and returned?
		mUserInput.requestFocus();
		Config.context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		mVibrator = (Vibrator)Config.context.getSystemService(Context.VIBRATOR_SERVICE);
		
		// Database elements
		mDb = Config.dbHelper.getWritableDatabase();
		sessionStartDatetime = new Time();
		sessionStartDatetime.setToNow();
		submissionDatetime = new Time();
		problemStartDatetime = new Time();
		
		//Restore old state if applicable
		if (savedInstanceState != null) {
			problem = savedInstanceState.getString("problem");
			timeRemainingInSecs = savedInstanceState.getInt("timeRemaining");
		 	solution = savedInstanceState.getString("solution");
		 	
		 	showProblem(problem, mWebView);
		} else {
			generateQuestion(mWebView);
			startCountdown(maxTimeInSecs);
		}
		
		return v;
	}

	@Override
	public void onPause() {
		if (mTimer != null) {
			mTimer.cancel();
		}

		super.onPause();
	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		startCountdown(timeRemainingInSecs);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		//Question, answer, current time, stats
		outState.putString("problem", problem);
		outState.putInt("timeRemaining", timeRemainingInSecs);
		outState.putString("solution", solution);
	}

	public void onSubmit() {
		// Get answer from user
		String input = mUserInput.getText().toString();
		String result;
		if(input.length() == 0) {
			return;
		}
		
		submissionDatetime.setToNow();

		// Check answer.
		if (checkAnswer(input)) {
			if (mTimer != null) {
				mTimer.cancel();
			}

			generateQuestion(mWebView);
			result = "Correct!";
			
			startCountdown(maxTimeInSecs);
		} else {
			result = "Incorrect!";
		}
		
		mUserInput.setText("");
		
		// Insert into Database
		ContentValues values = new ContentValues();
		values.put(UserAnswer.COLUMN_NAME_SESSION_DATETIME, sessionStartDatetime.toString());
		//values.put(UserAnswer.COLUMN_NAME_LEVEL, null);
		values.put(UserAnswer.COLUMN_NAME_EXERCISE, title);
		values.put(UserAnswer.COLUMN_NAME_PROBLEM, problem);
		values.put(UserAnswer.COLUMN_NAME_SOLUTION,solution);
		values.put(UserAnswer.COLUMN_NAME_PROBLEM_START_DATETIME, problemStartDatetime.toString());
		values.put(UserAnswer.COLUMN_NAME_SUBMISSION_START_DATETIME, submissionDatetime.toString());
		values.put(UserAnswer.COLUMN_NAME_CORRECT, result);
		
		long rowId = mDb.insert(UserAnswer.TABLE_NAME, null, values);
		
		// Display results.
		Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
	}
	
	// Takes time in seconds, but the CountDownTimer class uses millis
	// as units, so we need to convert seconds into milliseconds (x 1000)
	public void startCountdown(int timeInSecs) {
		final int timeInMillis = timeInSecs * 1000;   
		
		mCountdownBar.setProgress(timeInMillis);
		
		mTimer = new CountDownTimer(timeInMillis, 1000) {
			@Override
			public void onFinish() {
				timeRemainingInSecs = 0;
				mCountdownBar.setProgress(0);
				if (mVibrator.hasVibrator()) {
					mVibrator.vibrate(100);
				}
				Toast.makeText(Config.context, "Time's up!", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onTick(long millisUntilFinished) {
				timeRemainingInSecs =(int) (millisUntilFinished/1000);
				mCountdownBar.setProgress((int) millisUntilFinished);
				if (mVibrator.hasVibrator() && millisUntilFinished <= timeInMillis / 3) {
					mVibrator.vibrate(100);
				}
				
			}
		}.start();
	}
	

	//Logic for generating exercises. Should this be a nested class?
	public String title, hint, problem, inputType, solution;
	public Random rand;
	public int max, min;
	public static String open_html = 
			"<!DOCTYPE html><html lang=\"en\" xmlns:m=\"http://www.w3.org/1998/Math/MathML\"><head><meta charset=\"utf-8\"><link rel=\"stylesheet\" href=\"file:///android_asset/jqmath-0.4.0.css\"><script src=\"file:///android_asset/jquery-1.4.3.min.js\"></script><script src=\"file:///android_asset/jqmath-etc-0.4.0.min.js\"></script></head><html>";
	public static String close_html = "</html>";
	
	public void generateQuestion(WebView w) {
		showProblem(problem, w);
		problemStartDatetime.setToNow();
	}
	
	public boolean checkAnswer(String userAnswer) {
		return solution.equals(userAnswer);
	}
	
	public void showProblem(String problem, WebView w) {
		String html = new StringBuilder().append(open_html).append(problem).append(close_html).toString();

		w.loadDataWithBaseURL("file:///android_asset", html, "text/html", "utf-8", "");
	}
}
