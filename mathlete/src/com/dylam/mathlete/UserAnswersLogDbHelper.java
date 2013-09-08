package com.dylam.mathlete;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dylam.mathlete.UserAnswerContract.UserAnswer;

public class UserAnswersLogDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserAnswers.db";
    private static final String DB_CREATE = 
			"create table " + UserAnswer.TABLE_NAME + " (" +
			UserAnswer.COLUMN_NAME_ENTRY_ID + " integer primary key autoincrement," +
			UserAnswer.COLUMN_NAME_SESSION_DATETIME + " datetime," +
			UserAnswer.COLUMN_NAME_LEVEL + " text," +
			UserAnswer.COLUMN_NAME_EXERCISE + " text," +
			UserAnswer.COLUMN_NAME_PROBLEM + " text," +
			UserAnswer.COLUMN_NAME_SOLUTION + " text," +
			UserAnswer.COLUMN_NAME_PROBLEM_START_DATETIME + " text," +
			UserAnswer.COLUMN_NAME_SUBMISSION_START_DATETIME + " text," + 
			UserAnswer.COLUMN_NAME_CORRECT + " boolean );";
    private static final String SQL_DELETE_ENTRIES = 
    		"DROP TABLE IF EXISTS " + UserAnswer.TABLE_NAME;
    
	public UserAnswersLogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DB_CREATE);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		super.onOpen(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
	}
}