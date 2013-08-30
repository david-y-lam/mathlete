package com.dylam.mathlete;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	int min = 0;
	int max = 100;
	int correct_count = 0;
	String TAG = "MentalMath";
	int a;
	int b;
	TextView number1;
	TextView number2;
	Random r;
	EditText user_input;
	ProgressBar mProgressBar; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Generate numbers
		r = new Random();
		number1 = (TextView)findViewById(R.id.number_1);
		number2 = (TextView)findViewById(R.id.number_2);
		
		// Set up key listeners.
		user_input = (EditText)findViewById(R.id.user_answer);
		user_input.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch(keyCode) {
					case KeyEvent.KEYCODE_ENTER:	
						on_submit(null);
						return true;
					}
					
				}
				
				return false;
			}} );
		generateNumbers();
		
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void generateNumbers() {
		a = r.nextInt(this.max - this.min);
		b = r.nextInt(this.max - this.min);
		
		number1.setText(Integer.toString(a));
		number2.setText(Integer.toString(b));
	}
	
	public void on_submit(View v) {
		// Get input from user
		int answer = Integer.parseInt(((EditText) findViewById(R.id.user_answer)).getText().toString());
		Log.d(TAG, "User entered: " + Integer.toString(answer));
		
		// Check results
		String result = "";
		if (a + b == answer) {
			result = "Correct!";
			generateNumbers();
		} else {
			result = "Incorrect!";
		}

		Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
		
		// Clear input
		user_input.setText("");
	}


}
