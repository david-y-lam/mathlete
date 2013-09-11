package com.dylam.mathlete;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.dylam.mathlete.UserAnswerContract.UserAnswer;

public class UserLogGraphActivity extends ListActivity {
	public SQLiteDatabase mDb;
	public Cursor mCursor;
	public int mSessionDatetimeIndex, 
			mExerciseIndex, 
			mProblemIndex, 
			mSolutionIndex, 
			mProblemStartDatetimeIndex, 
			mSubmissionStartDatetimeIndex, 
			mCorrectIndex;

	public ArrayList<String> mSessionDatetimeIndexList,
    		mExerciseIndexList,
    		mProblemIndexList,
    		mSolutionIndexList,
    		mProblemStartDatetimeIndexList,
    		mSubmissionStartDatetimeIndexList,
    		mCorrectIndexList;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set up cursor
		UserAnswersLogDbHelper mDbHelper = new UserAnswersLogDbHelper(this);
		mDb = mDbHelper.getReadableDatabase();
		
		Cursor mCursor =  mDb.rawQuery( "select rowid _id,* from userAnswersLog", null);

		startManagingCursor(mCursor);
		
		mSessionDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SESSION_DATETIME);
		mExerciseIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_EXERCISE);
		mProblemIndex  = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_PROBLEM);
		mSolutionIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SOLUTION);
		mProblemStartDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_PROBLEM_START_DATETIME);
		mSubmissionStartDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SUBMISSION_START_DATETIME);
		mCorrectIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_CORRECT);
		

		@SuppressWarnings("deprecation")
		ListAdapter adapter = new SimpleCursorAdapter(this,
										android.R.layout.two_line_list_item,
										mCursor,
										new String[] { UserAnswer._ID,
														UserAnswer.COLUMN_NAME_PROBLEM,
														UserAnswer.COLUMN_NAME_SOLUTION },
										new int[] { android.R.id.text1, android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
		
	}
	
	private class MyCursorAdapter extends CursorAdapter {
		
		private LayoutInflater mInflater;
		
		@SuppressWarnings("deprecation")
		public MyCursorAdapter(Context context, Cursor c) {
			super(context, c);
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			// Create the custom view.
			TextView titleView = (TextView) view.findViewById(android.R.id.text1);
			TextView summaryView = (TextView) view.findViewById(android.R.id.text2);
			
			// Pull data.
			String sessionDate = mCursor.getString(mSessionDatetimeIndex);
			String exercise= mCursor.getString(mExerciseIndex);
			String problem = mCursor.getString(mProblemIndex);
			String startDate = mCursor.getString(mProblemStartDatetimeIndex);
			String solution = mCursor.getString(mSolutionIndex);
			String submissionDate = mCursor.getString(mSubmissionStartDatetimeIndex);
			String correct = mCursor.getString(mCorrectIndex);
			
			titleView.setText(startDate + ":" + problem + " Solution: " + solution);
			summaryView.setText("Submission time: " + submissionDate + " Your answer was: " + correct);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			return mInflater.inflate(android.R.layout.two_line_list_item, null);

		}
		
	}
	
}
