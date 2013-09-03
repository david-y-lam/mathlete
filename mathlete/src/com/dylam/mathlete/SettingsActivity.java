package com.dylam.mathlete;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_activity);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}