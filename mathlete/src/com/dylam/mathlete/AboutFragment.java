package com.dylam.mathlete;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;


public class AboutFragment extends DialogFragment {

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage(R.string.dialog_about_body)
			.setTitle(R.string.dialog_about_title)
			.setPositiveButton("Return", null);
		
		return builder.create();
	}
}
