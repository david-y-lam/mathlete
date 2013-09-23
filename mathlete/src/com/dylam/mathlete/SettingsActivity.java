package com.dylam.mathlete;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences_activity);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}