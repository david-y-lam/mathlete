package com.dylam.mathlete;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.dylam.mathlete.UserAnswerContract.UserAnswer;

public class UserLogGraphActivity extends Activity {
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Pull the data.
		mDb = Config.dbHelper.getReadableDatabase();
		mCursor = mDb.query(UserAnswer.TABLE_NAME, 
							null, 
							null,
							null,
							null,
							null, 
							null);

		mSessionDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SESSION_DATETIME);
		mExerciseIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_EXERCISE);
		mProblemIndex  = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_PROBLEM);
		mSolutionIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SOLUTION);
		mProblemStartDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_PROBLEM_START_DATETIME);
		mSubmissionStartDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SUBMISSION_START_DATETIME);
		mCorrectIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_CORRECT);
		
		mSessionDatetimeIndexList = new ArrayList<String>();
		mExerciseIndexList = new ArrayList<String>();
		mProblemIndexList = new ArrayList<String>();
   		mSolutionIndexList = new ArrayList<String>();
   		mProblemStartDatetimeIndexList = new ArrayList<String>();
   		mSubmissionStartDatetimeIndexList = new ArrayList<String>();
   		mCorrectIndexList = new ArrayList<String>();


   		for(mCursor.moveToFirst(); mCursor.moveToNext(); ) {
		    mSessionDatetimeIndexList.add(mCursor.getString(mSessionDatetimeIndex));
		    mExerciseIndexList.add(mCursor.getString(mExerciseIndex));
		    mProblemIndexList.add(mCursor.getString(mProblemIndex));
		    mSolutionIndexList.add(mCursor.getString(mSolutionIndex));
		    mProblemStartDatetimeIndexList.add(mCursor.getString(mProblemStartDatetimeIndex));
		    mSubmissionStartDatetimeIndexList.add(mCursor.getString(mSubmissionStartDatetimeIndex));
		    mCorrectIndexList.add(mCursor.getString(mCorrectIndex));
   		}
   		
   		int size = mSessionDatetimeIndexList.size();
		// Set up charts.
   		
		// Draw it.
	}
}
