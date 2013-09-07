package com.dylam.mathlete;

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
	public String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		
		mFragManager = getFragmentManager();
		
		// If we're restoring state, then we don't need to 
		// do anything else.
		if (savedInstanceState != null) { 
			return;
		}

		// NOTE: clicking back button takes us to empty framelayout.
		// How do I implement exiting the activity/app if there is no
		// else?
		mFragManager.beginTransaction()
			.replace(R.id.content, new ExerciseListFragment(), "ExerciseListFragment()")
			.addToBackStack("ExerciseListFragment")
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