package com.dylam.mathlete;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		
		// Add the fragment.
		getFragmentManager().beginTransaction()
			.add(R.id.content, new Chapter1_1_TwoDigitAddition(), "1.1")
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
}