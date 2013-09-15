package com.dylam.mathlete;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Time;

import com.androidplot.ui.SeriesRenderer;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.dylam.mathlete.UserAnswerContract.UserAnswer;

public class UserLogGraphActivity extends Activity {
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
	public ArrayList<Float> mTimeElapsedList;
	
	public Cursor mCursor;
	public SQLiteDatabase mDb;
	
	public XYPlot mPlot;
	public XYSeries mTimeElapsedSeries;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_log_graph);
		
		// Read in the data into Arrays.
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
		mTimeElapsedList = new ArrayList<Float>();
		
		mCursor.moveToFirst();
		for(int i = 0; i < mCursor.getCount(); i++, mCursor.moveToNext()) {
			mSessionDatetimeIndexList.add(mCursor.getString(mSessionDatetimeIndex));
			mExerciseIndexList.add(mCursor.getString(mExerciseIndex));
			mProblemIndexList.add(mCursor.getString(mProblemIndex));
			mSolutionIndexList.add(mCursor.getString(mSolutionIndex));
			mProblemStartDatetimeIndexList.add(mCursor.getString(mProblemStartDatetimeIndex));
			mSubmissionStartDatetimeIndexList.add(mCursor.getString(mSubmissionStartDatetimeIndex));
			mCorrectIndexList.add(mCursor.getString(mCorrectIndex));
			Time start = new Time();
			Time end = new Time();
			start.parse3339(mProblemStartDatetimeIndexList.get(i));
			end.parse3339(mSubmissionStartDatetimeIndexList.get(i));
			mTimeElapsedList.add((float)(end.toMillis(false) - start.toMillis(false)) / 1000);
		} 
		
		// Plot it.
		mPlot = (XYPlot)findViewById(R.id.user_log_graph);
		mPlot.setTicksPerRangeLabel(3);
		mPlot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
		mPlot.setTicksPerRangeLabel(2);
		
		mTimeElapsedSeries = new SimpleXYSeries(mTimeElapsedList, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Time");
		mPlot.addSeries(mTimeElapsedSeries, new MyBarFormatter(Color.BLUE, Color.BLACK));
	}
	
    class MyBarFormatter extends BarFormatter {
        public MyBarFormatter(int fillColor, int borderColor) {
            super(fillColor, borderColor);
        }

        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
            return MyBarRenderer.class;
        }

        @Override
        public SeriesRenderer getRendererInstance(XYPlot plot) {
            return new MyBarRenderer(plot);
        }
    }
	
    class MyBarRenderer extends BarRenderer<MyBarFormatter> {

        public MyBarRenderer(XYPlot plot) {
            super(plot);
        }

		/**
         * Implementing this method to allow us to inject our
         * special selection formatter.
         * @param index index of the point being rendered.
         * @param series XYSeries to which the point being rendered belongs.
         * @return
         */
        @Override
        // TODO: figure out why using @Override screws up the Maven builds
        protected MyBarFormatter getFormatter(int index, XYSeries series) { 
        	return getFormatter(series);
        }
    }
}