package com.dylam.mathlete;

import android.os.Bundle;
import android.webkit.WebView;

import java.util.Random;

public class Chapter1_3_TwoDigitSubtraction extends BaseExerciseFragment {
	// variables
	public int num1, num2;
	public int max, min;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		title = "Two-digit addition";
		hint = "Add these two numbers together.";
		rand = new Random();
		max = 100;
		min = 10;
		maxTimeInSecs = 10;

		super.onCreate(savedInstanceState);
	}

	public void generateQuestion(WebView w) {
		// Generate our two numbers
		num1 = rand.nextInt(max - min) + min;
		
		// second number must be less than first number
		num2 = rand.nextInt(num1 - min) + min;
		
		// Now display it on the WebView
		problem = "$$" + Integer.toString(num1) + " - " + Integer.toString(num2) + "$$";
		
		// Save solution.
		solution = Integer.toString(num1 - num2);
		
		super.generateQuestion(w);
	}
}
