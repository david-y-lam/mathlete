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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

abstract public class BaseExerciseFragment extends Fragment{
	// UI elements
	public TextView mNumber1View, mNumber2View;
	public ProgressBar mCountdownBar;
	public EditText mUserInput;
	public Button mButton;
	
	// Timer that drives the progress bar
	private CountDownTimer mTimer;
	public int maxTimeInSecs;
	
	// Backend elements
	public Random rand;
	public int num_correct, num_total;
	public int num1, num2;
	public int maxNum, minNum;
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
		mCountdownBar = (ProgressBar)v.findViewById(R.id.countdownBar);		
		mNumber1View = (TextView)v.findViewById(R.id.number_1);
		mNumber2View = (TextView)v.findViewById(R.id.number_2);
		mButton = (Button)v.findViewById(R.id.button1);
		mUserInput = (EditText)v.findViewById(R.id.user_answer_input);
		mCountdownBar.setMax(maxTimeInSecs * 1000);

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

		
		// Restore old state if applicable
		if (savedInstanceState != null) {
			num1 = savedInstanceState.getInt("num1");
			num2 = savedInstanceState.getInt("num2");
			timeRemainingInSecs = savedInstanceState.getInt("timeRemaining");
			mNumber1View.setText(Integer.toString(num1));
			mNumber2View.setText(Integer.toString(num2));
		} else {
			generateQuestion();
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
		
		// Question, answer, current time, stats
		outState.putInt("num1", num1);
		outState.putInt("num2", num2);
		outState.putString("answer", mUserInput.getText().toString());
		outState.putInt("timeRemaining", timeRemainingInSecs);
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
			
			generateQuestion();
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
	
	// Methods for handling questions and answers
	
	// Logic for generating two numbers. maxNum and minNum 
	// are ranges for numbers to be generated. This method 
	// can be overridden in subclasses for exercises that don't
	// fit this model.
	public void generateQuestion() {
		// Get two two-digit numbers
		num1 = rand.nextInt(maxNum - minNum) + minNum;
		num2 = rand.nextInt(maxNum - minNum) + minNum;
		
		// Set the display
		mNumber1View.setText(Integer.toString(num1));
		mNumber2View.setText(Integer.toString(num2));
	}
	
	// Logic for checking answer goes here.
	abstract Boolean checkAnswer(String input);	
		
}
