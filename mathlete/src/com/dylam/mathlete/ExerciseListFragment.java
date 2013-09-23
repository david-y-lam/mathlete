package com.dylam.mathlete;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class ExerciseListFragment extends ListFragment {
    public static String[] exercises = new String[] {
		"Chapter1_1_TwoDigitAddition",
		"Chapter1_2_ThreeDigitAddition",
		"Chapter1_3_TwoDigitSubtraction",
		"Chapter1_4_ThreeDigitSubtraction"
    };
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		

		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, exercises);
		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		BaseExerciseFragment frag = null;
		
		switch(position) {
		case 0:
			frag = new Chapter1_1_TwoDigitAddition();			
			break;
		case 1:
			frag = new Chapter1_2_ThreeDigitAddition();
			break;
		case 2:
			frag = new Chapter1_3_TwoDigitSubtraction();
			break;
		case 3:
			frag = new Chapter1_4_ThreeDigitSubtraction();
			break;
		default:
			break;
		}
		getFragmentManager()
			.beginTransaction()
			.replace(R.id.content, frag, exercises[position])
			.addToBackStack(exercises[position])
			.commit();
	}
}
