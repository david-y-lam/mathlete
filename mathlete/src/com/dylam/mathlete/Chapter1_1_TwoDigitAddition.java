package com.dylam.mathlete;

import android.os.Bundle;
import android.util.Log;

public class Chapter1_1_TwoDigitAddition extends BaseExerciseFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		maxNum = 100;
		minNum = 10;
		
		maxTimeInSecs = 5;
		countDownInterval = 1000;
		super.onCreate(savedInstanceState);
	}

	@Override
	Boolean checkAnswer(String input) {
		int answer = Integer.parseInt(input);
		
		return num1 + num2 == answer;
	}
}