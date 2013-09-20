package com.dylam.mathlete;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.text.format.Time;
import android.util.Log;
import android.util.Pair;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.androidplot.LineRegion;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.dylam.mathlete.UserAnswerContract.UserAnswer;

public class UserLogGraphActivity extends Activity {
	public static final String TAG = "user log graph";
	public int mSessionDatetimeIndex, 
		mExerciseIndex, 
		mProblemIndex, 
		mSolutionIndex, 
		mUserAnswerIndex,
		mProblemStartDatetimeIndex, 
		mSubmissionStartDatetimeIndex, 
		mCorrectIndex; 
	
	public ArrayList<String> mSessionDatetimeIndexList,
		mExerciseIndexList,
		mProblemIndexList,
		mSolutionIndexList,
		mProblemStartDatetimeIndexList,
		mUserAnswerList,
		mSubmissionStartDatetimeIndexList,
		mCorrectIndexList;
	public ArrayList<Float> mTimeElapsedList;
	
	public Cursor mCursor;
	public SQLiteDatabase mDb;
	
	public XYPlot mPlot;
	public XYSeries mTimeElapsedSeries;
    private Pair<Integer, XYSeries> selection;
    
    public MyBarFormatter mDefaultFormatter, mSelectedFormatter;
    
    private GestureDetectorCompat mDetector;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        // Log.d(TAG, "In UserLogGraphActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_log_graph);

		// Read in the data into Arrays.
		mDb = new UserAnswersLogDbHelper(this).getReadableDatabase();
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
		mUserAnswerIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_USER_ANSWER);
		mProblemStartDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_PROBLEM_START_DATETIME);
		mSubmissionStartDatetimeIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_SUBMISSION_START_DATETIME);
		mCorrectIndex = mCursor.getColumnIndex(UserAnswer.COLUMN_NAME_CORRECT);
		    
		mSessionDatetimeIndexList = new ArrayList<String>();
		mExerciseIndexList = new ArrayList<String>();
		mProblemIndexList = new ArrayList<String>();
		mSolutionIndexList = new ArrayList<String>();
		mProblemStartDatetimeIndexList = new ArrayList<String>();
		mUserAnswerList = new ArrayList<String>();
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
			mUserAnswerList.add(mCursor.getString(mUserAnswerIndex));
			mSubmissionStartDatetimeIndexList.add(mCursor.getString(mSubmissionStartDatetimeIndex));
			mCorrectIndexList.add(mCursor.getString(mCorrectIndex));
			Long start = Long.parseLong(mProblemStartDatetimeIndexList.get(i));
			Long end = Long.parseLong(mSubmissionStartDatetimeIndexList.get(i));
			mTimeElapsedList.add((float)(end - start) / 1000);
		}
        mDb.close();
		
		// Plot it.
		mDefaultFormatter = new MyBarFormatter(Color.BLACK, Color.BLUE);
		mSelectedFormatter = new MyBarFormatter(Color.BLACK, Color.GREEN);
		mPlot = (XYPlot)findViewById(R.id.user_log_graph);
		mPlot.setTicksPerRangeLabel(3);
		mPlot.setRangeLowerBoundary(0, BoundaryMode.FIXED);
		mPlot.setTicksPerRangeLabel(2);
		
		mTimeElapsedSeries = new SimpleXYSeries(mTimeElapsedList, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Time");
		mPlot.addSeries(mTimeElapsedSeries, mDefaultFormatter);

        mPlot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    onPlotClicked(new PointF(motionEvent.getX(), motionEvent.getY()));
                }
                return true;
            }
        });
        
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
	}
	
	
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	mDetector.onTouchEvent(event);
    	return super.onTouchEvent(event);
	}


	private void onPlotClicked(PointF point) {

        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well. 
        if (mPlot.getGraphWidget().getGridRect().contains(point.x, point.y)) {
            Number x = mPlot.getXVal(point);
            Number y = mPlot.getYVal(point);


            selection = null;
            double xDistance = 0;
            double yDistance = 0;

            // find the closest value to the selection:
            for (XYSeries series : mPlot.getSeriesSet()) {
                for (int i = 0; i < series.size(); i++) {
                    Number thisX = series.getX(i);
                    Number thisY = series.getY(i);
                    if (thisX != null && thisY != null) {
                        double thisXDistance =
                                LineRegion.measure(x, thisX).doubleValue();
                        double thisYDistance =
                                LineRegion.measure(y, thisY).doubleValue();
                        if (selection == null) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance < xDistance) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance == xDistance &&
                                thisYDistance < yDistance &&
                                thisY.doubleValue() >= y.doubleValue()) {
                            selection = new Pair<Integer, XYSeries>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        }
                    }
                }
            }
            
        } else {
            // if the press was outside the graph area, deselect:
            selection = null;
        }
        
        if (selection != null) {
        	// Update the textviews with the relevant info.
        	Time sessionStartTime = new Time();
        	sessionStartTime.set(Long.parseLong(mSessionDatetimeIndexList.get(selection.first)));
        	((TextView)findViewById(R.id.session_date_tv)).setText("Session Datetime:" + sessionStartTime.format3339(false));
        	((TextView)findViewById(R.id.exercise_tv)).setText("Exercise:" + mExerciseIndexList.get(selection.first));
        	((TextView)findViewById(R.id.problem_tv)).setText("Problem:" + mProblemIndexList.get(selection.first));
        	((TextView)findViewById(R.id.solution_tv)).setText("Solution:" + mSolutionIndexList.get(selection.first));
        	Time problemStartTime = new Time();
        	problemStartTime.set(Long.parseLong(mProblemStartDatetimeIndexList.get(selection.first)));
        	((TextView)findViewById(R.id.problem_start_datetime_tv)).setText("Problem Start datetime:" + problemStartTime.format3339(false)); 
        	((TextView)findViewById(R.id.user_answer_tv)).setText("User Answer:" + mUserAnswerList.get(selection.first));
        	Time submissionStartTime = new Time();
        	submissionStartTime.set(Long.parseLong(mSubmissionStartDatetimeIndexList.get(selection.first)));
        	((TextView)findViewById(R.id.submission_start_datetime_tv)).setText("Submission datetime:" + submissionStartTime.format3339(false));
        	((TextView)findViewById(R.id.correct_tv)).setText("Result:" + mCorrectIndexList.get(selection.first));
        }
        
        mPlot.redraw();
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
        	if (selection != null && 
        		selection.second == series && 
        		selection.first == index) {
        		return mSelectedFormatter;
        	} else {
        		return getFormatter(series);
        	}
        }
    }
    
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			Log.d(TAG, "in onscroll");
			Log.d(TAG, e1.toString());
			Log.d(TAG, e2.toString());
			// TODO Auto-generated method stub
			return super.onScroll(e1, e2, distanceX, distanceY);
		}
    	
    }
}