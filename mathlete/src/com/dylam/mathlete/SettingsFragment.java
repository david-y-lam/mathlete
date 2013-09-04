package com.dylam.mathlete;

import android.app.AlertDialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.util.Log;

public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		
		if (preference.getTitle()
				.equals(getActivity().getResources().getString(R.string.pref_reset_stats_title))) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
			// TODO: add listener for positive button
			dialog.setMessage(R.string.pref_reset_stats_dialog_message)
				.setPositiveButton(R.string.pref_reset_stats_positive_button_msg, null)
				.setNegativeButton(R.string.pref_reset_stats_negative_button_msg, null)
				.show();
		}

		return super.onPreferenceTreeClick(preferenceScreen, preference);
	}
}