package com.dylam.mathlete;

import android.provider.BaseColumns;

public final class UserAnswerContract {
	public UserAnswerContract() {};
	
	public static abstract class UserAnswer implements BaseColumns {
        public static final String TABLE_NAME = "userAnswersLog";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_SESSION_DATETIME = "sessionDateTime";
        public static final String COLUMN_NAME_LEVEL = "level";
        public static final String COLUMN_NAME_EXERCISE = "exercise";
        public static final String COLUMN_NAME_PROBLEM = "problem";
        public static final String COLUMN_NAME_SOLUTION = "solution";
        public static final String COLUMN_NAME_USER_ANSWER = "userAnswer";
        public static final String COLUMN_NAME_PROBLEM_START_DATETIME= "startTime";
        public static final String COLUMN_NAME_SUBMISSION_START_DATETIME = "submissioStartDateTime";
        public static final String COLUMN_NAME_CORRECT = "isCorrect";
	}
}
