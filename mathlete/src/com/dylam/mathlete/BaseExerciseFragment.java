package com.dylam.mathlete;

import java.util.Random;

import android.app.Fragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

abstract public class BaseExerciseFragment extends Fragment{
	// UI elements
	public WebView mWebView;
	public ProgressBar mCountdownBar;
	public EditText mUserInput;
	public Button mButton;
	
	// Timer that drives the progress bar
	private CountDownTimer mTimer;
	public int maxTimeInSecs;
	
	// Backend elements
	public int num_correct, num_total;
	public int timeRemainingInSecs;
	
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
		//mUserInput.requestFocus();
		//getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

		
		//Restore old state if applicable
		if (savedInstanceState != null) {
			problem = savedInstanceState.getString("problem");
			timeRemainingInSecs = savedInstanceState.getInt("timeRemaining");
		 	solution = savedInstanceState.getString("solution");
		 	
		 	showProblem(problem, mWebView);
		} else {
			generateAndDisplayQuestion(mWebView);
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
		
		// Check answer.
		if (checkAnswer(input)) {
			if (mTimer != null) {
				mTimer.cancel();
			}
			
			mUserInput.setText("");
			
			generateAndDisplayQuestion(mWebView);
			result = "Correct!";
			
			startCountdown(maxTimeInSecs);
		} else {
			result = "Incorrect!";
		}
		
		// Display results.
		Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
	}
	
	// Takes time in seconds, but the CountDownTimer class uses millis
	// as units, so we need to convert seconds into milliseconds (x 1000)
	public void startCountdown(int timeInSecs) {
		int timeInMillis = timeInSecs * 1000;   
		
		mCountdownBar.setProgress(timeInMillis);
		
		mTimer = new CountDownTimer(timeInMillis, 1000) {
			@Override
			public void onFinish() {
				timeRemainingInSecs = 0;
				mCountdownBar.setProgress(0);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				timeRemainingInSecs =(int) (millisUntilFinished/1000);
				mCountdownBar.setProgress((int) millisUntilFinished);
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
	}
	
	public boolean checkAnswer(String userAnswer) {
		return solution.equals(userAnswer);
	}
	
	public void showProblem(String problem, WebView w) {
		String html = new StringBuilder().append(open_html).append(problem).append(close_html).toString();

		w.loadDataWithBaseURL("file:///android_asset", html, "text/html", "utf-8", "");
	}
}
