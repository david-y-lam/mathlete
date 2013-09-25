package com.dylam.mathlete;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewSeries;

import java.util.ArrayList;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    // Log.d(TAG, "In UserLogGraphActivity onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_log_graph);

		// Read in the data into Arrays.
		mDb = new UserAnswersLogDbHelper(this).getReadableDatabase();
        // TOOD: Why can't I just user UserAnswer.????
		mCursor = mDb.query(UserAnswerContract.UserAnswer.TABLE_NAME,
				null, 
				null,
				null,
				null,
				null, 
				null);
			
		mSessionDatetimeIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_SESSION_DATETIME);
		mExerciseIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_EXERCISE);
		mProblemIndex  = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_PROBLEM);
		mSolutionIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_SOLUTION);
		mUserAnswerIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_USER_ANSWER);
		mProblemStartDatetimeIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_PROBLEM_START_DATETIME);
		mSubmissionStartDatetimeIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_SUBMISSION_START_DATETIME);
		mCorrectIndex = mCursor.getColumnIndex(UserAnswerContract.UserAnswer.COLUMN_NAME_CORRECT);
		    
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

        // init example series data
        GraphViewData[] gv = new GraphViewData[mCursor.getCount()];

        for (int i = 0; i < mCursor.getCount(); i++) {
            gv[i] = new GraphViewData(i, mTimeElapsedList.get(i));
        }

        GraphViewSeries exampleSeries = new GraphViewSeries(gv);

        GraphView graphView = new BarGraphView(
                this // context
                , "GraphViewDemo" // heading
        );
        graphView.addSeries(exampleSeries); // data
        graphView.setViewPort(0, mCursor.getColumnCount() / 2);
        graphView.setScalable(true);
        graphView.setScrollable(true);
        graphView.getGraphViewStyle().setVerticalLabelsColor(Color.BLACK);
        graphView.getGraphViewStyle().setHorizontalLabelsColor(Color.BLACK);

        LinearLayout layout = (LinearLayout) findViewById(R.id.graph);
        layout.addView(graphView);

    }
	
        // if (selection != null) {
        // 	// Update the textviews with the relevant info.
        // 	Time sessionStartTime = new Time();
        // 	sessionStartTime.set(Long.parseLong(mSessionDatetimeIndexList.get(selection.first)));
        // 	((TextView)findViewById(R.id.session_date_tv)).setText("Session Datetime:" + sessionStartTime.format3339(false));
        // 	((TextView)findViewById(R.id.exercise_tv)).setText("Exercise:" + mExerciseIndexList.get(selection.first));
        // 	((TextView)findViewById(R.id.problem_tv)).setText("Problem:" + mProblemIndexList.get(selection.first));
        // 	((TextView)findViewById(R.id.solution_tv)).setText("Solution:" + mSolutionIndexList.get(selection.first));
        // 	Time problemStartTime = new Time();
        // 	problemStartTime.set(Long.parseLong(mProblemStartDatetimeIndexList.get(selection.first)));
        // 	((TextView)findViewById(R.id.problem_start_datetime_tv)).setText("Problem Start datetime:" + problemStartTime.format3339(false)); 
        // 	((TextView)findViewById(R.id.user_answer_tv)).setText("User Answer:" + mUserAnswerList.get(selection.first));
        // 	Time submissionStartTime = new Time();
        // 	submissionStartTime.set(Long.parseLong(mSubmissionStartDatetimeIndexList.get(selection.first)));
        // 	((TextView)findViewById(R.id.submission_start_datetime_tv)).setText("Submission datetime:" + submissionStartTime.format3339(false));
        // 	((TextView)findViewById(R.id.correct_tv)).setText("Result:" + mCorrectIndexList.get(selection.first));
        // }

}
