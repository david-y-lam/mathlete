package com.dylam.mathlete;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {
	public FragmentManager mFragManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		// Add the fragment.
		mFragManager = getFragmentManager();

		// NOTE: clicking back button takes us to empty framelayout.
		// How do I implement exiting the activity/app if there is no
		// else?
		mFragManager.beginTransaction()
			.replace(R.id.content, new Chapter1_1_TwoDigitAddition(), "1.1")
			.addToBackStack("Exercise")
			.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case R.id.action_help:
				// TODO: implement help for specific exercises.
				Toast.makeText(getApplicationContext(), "help", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_about:
				AboutFragment aboutFragment = new AboutFragment();
				aboutFragment.show(getFragmentManager(), "About Fragment");
				return true;
			case R.id.action_settings:
				/*mFragManager.beginTransaction()
					.replace(R.id.content, new SettingsFragment(), "Settings Fragment")
					.addToBackStack("Settings")
					.commit();
				
				for(int entry = 0; entry < mFragManager.getBackStackEntryCount(); entry++){
					   Log.i("MainActivity:onOptionsItemSelected", "Found fragment: " + mFragManager.getBackStackEntryAt(entry).getName());
				}*/
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
			default:
				return false;
		}
	}

	@Override
	public void onBackPressed() {
		// Should this be 1 or 0? 
		if (mFragManager.getBackStackEntryCount() > 1) {
			mFragManager.popBackStackImmediate();
		} else {
			super.onBackPressed();
		}
	}
}