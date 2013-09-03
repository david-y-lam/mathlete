package com.dylam.mathlete;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

abstract public class BaseExerciseActivity extends Activity{
	// UI elements
	public TextView mNumber1View, mNumber2View;
	public ProgressBar mCountdownBar;
	public EditText mUserInput;
	
	// Timer that drives the progress bar
	private CountDownTimer mTimer;
	public int maxTimeInSecs;
	public int countDownInterval;
	public int maxTimeInMillis;
	
	// Backend elements
	public Random rand;
	public int num_correct;
	public int num_total;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_exercise_activity);
		
		// Initialize UI elements
		maxTimeInMillis = maxTimeInSecs * 1000;
		mCountdownBar = (ProgressBar)findViewById(R.id.countdownBar);
		mCountdownBar.setMax(maxTimeInMillis);
		
		mNumber1View = (TextView)findViewById(R.id.number_1);
		mNumber2View = (TextView)findViewById(R.id.number_2);

		// Set up user input.
		mUserInput = (EditText)findViewById(R.id.user_answer_input);
		mUserInput
			.setOnEditorActionListener(new OnEditorActionListener(){

				@Override
				public boolean onEditorAction(TextView v, int actionId,
						KeyEvent event) {
					boolean handled = false;
					
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						onSubmit(null);
						handled = true;
					}
					
					return handled;
				}
				
			});

		// Pop up the input keyboard
		// TODO: add a listener to switch focus and display
		// keyboard after focus has shifted and returned?
		mUserInput.requestFocus();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		
		
		// Initialize question generating elements
		rand = new Random();
		
		// Start first question
		generateQuestion();
		startCountdown();
	}
	
	public void onSubmit(View v) {
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
			
			generateQuestion();
			result = "Correct!";
			
			startCountdown();
		} else {
			result = "Incorrect!";
		}
		
		// Display results.
		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
	}
	
	public void startCountdown() {
		mCountdownBar.setProgress(maxTimeInMillis);
		
		mTimer = new CountDownTimer(maxTimeInMillis, 1000) {
			@Override
			public void onFinish() {
				mCountdownBar.setProgress(0);
			}

			@Override
			public void onTick(long millisUntilFinished) {
				mCountdownBar.setProgress((int) millisUntilFinished);
			}
		}.start();
	}
	
	
	// Methods for handling questions and answers
	abstract void generateQuestion();
	
	abstract Boolean checkAnswer(String input);
		
}