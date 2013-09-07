package com.dylam.mathlete;

import java.util.Random;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

public class Chapter1_1_TwoDigitAddition extends BaseExerciseFragment {
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
		num2 = rand.nextInt(max - min) + min;
		
		// Now display it on the WebView
		problem = "$$" + Integer.toString(num1) + " + " + Integer.toString(num2) + "$$";
		
		// Save solution.
		solution = Integer.toString(num1 + num2);
		
		super.generateQuestion(w);
	}
}
