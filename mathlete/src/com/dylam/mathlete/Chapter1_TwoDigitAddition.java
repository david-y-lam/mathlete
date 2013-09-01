package com.dylam.mathlete;

import android.os.Bundle;
import android.util.Log;

public class Chapter1_TwoDigitAddition extends BaseExerciseActivity {
	public int num1, num2;
	public int maxNum, minNum;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		maxNum = 100;
		minNum = 10;
		
		maxTimeInSecs = 5;
		countDownInterval = 1000;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	void generateQuestion() {
		// Get two two-digit numbers
		num1 = rand.nextInt(maxNum - minNum) + minNum;
		num2 = rand.nextInt(maxNum - minNum) + minNum;
		
		// Set the display
		mNumber1View.setText(Integer.toString(num1));
		mNumber2View.setText(Integer.toString(num2));
	}

	@Override
	Boolean checkAnswer(String input) {
		int answer = Integer.parseInt(input);
		
		if (num1 + num2 == answer) {
			return true;
		} else {
			return false;
		}
	}
}