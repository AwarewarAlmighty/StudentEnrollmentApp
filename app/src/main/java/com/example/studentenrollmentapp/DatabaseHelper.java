package com.example.studentenrollmentapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Info
    private static final String DATABASE_NAME = "StudentEnrollment.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_STUDENTS = "Students";
    public static final String TABLE_SUBJECTS = "Subjects";
    public static final String TABLE_ENROLLMENTS = "Enrollments";

    // Students Table Columns
    public static final String COLUMN_STUDENT_ID = "id";
    public static final String COLUMN_STUDENT_NAME = "name";
    public static final String COLUMN_STUDENT_EMAIL = "email";
    public static final String COLUMN_STUDENT_PASSWORD = "password";

    // Subjects Table Columns
    public static final String COLUMN_SUBJECT_ID = "id";
    public static final String COLUMN_SUBJECT_NAME = "name";
    public static final String COLUMN_SUBJECT_CREDITS = "credits";

    // Enrollments Table Columns
    public static final String COLUMN_ENROLLMENT_ID = "id";
    public static final String COLUMN_ENROLLMENT_STUDENT_ID = "student_id";
    public static final String COLUMN_ENROLLMENT_SUBJECT_ID = "subject_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Students Table
        String createStudentsTable = "CREATE TABLE " + TABLE_STUDENTS + " (" +
                COLUMN_STUDENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STUDENT_NAME + " TEXT, " +
                COLUMN_STUDENT_EMAIL + " TEXT UNIQUE, " +
                COLUMN_STUDENT_PASSWORD + " TEXT)";
        db.execSQL(createStudentsTable);

        // Create Subjects Table
        String createSubjectsTable = "CREATE TABLE " + TABLE_SUBJECTS + " (" +
                COLUMN_SUBJECT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SUBJECT_NAME + " TEXT, " +
                COLUMN_SUBJECT_CREDITS + " INTEGER)";
        db.execSQL(createSubjectsTable);

        // Create Enrollments Table
        String createEnrollmentsTable = "CREATE TABLE " + TABLE_ENROLLMENTS + " (" +
                COLUMN_ENROLLMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ENROLLMENT_STUDENT_ID + " INTEGER, " +
                COLUMN_ENROLLMENT_SUBJECT_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_ENROLLMENT_STUDENT_ID + ") REFERENCES " + TABLE_STUDENTS + "(" + COLUMN_STUDENT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_ENROLLMENT_SUBJECT_ID + ") REFERENCES " + TABLE_SUBJECTS + "(" + COLUMN_SUBJECT_ID + "))";
        db.execSQL(createEnrollmentsTable);

        // Insert Default Subjects
        insertDefaultSubjects(db);
    }

    private void insertDefaultSubjects(SQLiteDatabase db) {
        String insertSubjects = "INSERT INTO " + TABLE_SUBJECTS + " (" +
                COLUMN_SUBJECT_NAME + ", " +
                COLUMN_SUBJECT_CREDITS + ") VALUES " +
                "('Math', 4), " +
                "('Computer Science', 3), " +
                "('Physics', 3), " +
                "('English', 2), " +
                "('Chemistry', 3)";
        db.execSQL(insertSubjects);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENROLLMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }
}
