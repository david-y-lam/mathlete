package com.dylam.mathlete;

import java.util.Random;

import android.os.Bundle;
import android.webkit.WebView;

public class Chapter1_4_ThreeDigitSubtraction extends BaseExerciseFragment {
	// variables
	public int num1, num2;
	public int num_min, num1_max, num2_max;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		title = "Three-digit addition";
		hint = "Add these two numbers together.";
		rand = new Random();
		num1_max = 10000;
		num_min = 100;
		num2_max = 1000;
		
		maxTimeInSecs = 10;

		super.onCreate(savedInstanceState);
	}

	public void generateQuestion(WebView w) {
		// Generate our two numbers

		// first number can be four digits
		num1 = rand.nextInt(num1_max - num_min) + num_min;
		// second number should be three digits, but less than
		// first number.
		if (num1 >= 1000) {
			num2 = rand.nextInt(num2_max - num_min) + num_min;
		} else {
			num2 = rand.nextInt(num1_max - num_min) + num_min;
		}
		
		// Now display it on the WebView
		problem = "$$" + Integer.toString(num1) + " - " + Integer.toString(num2) + "$$";
		
		// Save solution.
		solution = Integer.toString(num1 - num2);
		
		super.generateQuestion(w);
	}
}
