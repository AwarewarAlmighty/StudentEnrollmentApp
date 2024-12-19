package com.example.studentenrollmentapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SubjectSelectionActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout subjectsContainer;
    private Button btnSubmit;
    private static final int MAX_CREDITS = 24;
    private int totalCredits = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_selection);

        databaseHelper = new DatabaseHelper(this);
        subjectsContainer = findViewById(R.id.subjects_container);
        btnSubmit = findViewById(R.id.btn_submit);

        loadSubjects();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enrollSelectedSubjects();
            }
        });
    }

    private void loadSubjects() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_SUBJECTS,
                new String[]{DatabaseHelper.COLUMN_SUBJECT_ID, DatabaseHelper.COLUMN_SUBJECT_NAME, DatabaseHelper.COLUMN_SUBJECT_CREDITS},
                null, null, null, null, null);

        subjectsContainer.removeAllViews();

        while (cursor.moveToNext()) {
            int subjectId = cursor.getInt(0);
            String subjectName = cursor.getString(1);
            int credits = cursor.getInt(2);

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(subjectName + " (" + credits + " credits)");
            checkBox.setTag(new Subject(subjectId, subjectName, credits));

            subjectsContainer.addView(checkBox);
        }

        cursor.close();
        db.close();
    }

    private void enrollSelectedSubjects() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ArrayList<Subject> selectedSubjects = new ArrayList<>();
        totalCredits = 0;

        // Get logged-in student_id from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("StudentSession", Context.MODE_PRIVATE);
        int studentId = preferences.getInt("student_id", -1);

        if (studentId == -1) {
            Toast.makeText(this, "Error: No logged-in user", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < subjectsContainer.getChildCount(); i++) {
            View view = subjectsContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {
                    Subject subject = (Subject) checkBox.getTag();
                    totalCredits += subject.credits;
                    selectedSubjects.add(subject);
                }
            }
        }

        if (totalCredits > MAX_CREDITS) {
            Toast.makeText(this, "Cannot enroll. Total credits exceed " + MAX_CREDITS, Toast.LENGTH_SHORT).show();
            return;
        }

        for (Subject subject : selectedSubjects) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ENROLLMENT_STUDENT_ID, studentId);
            values.put(DatabaseHelper.COLUMN_ENROLLMENT_SUBJECT_ID, subject.id);
            db.insert(DatabaseHelper.TABLE_ENROLLMENTS, null, values);
        }

        Toast.makeText(this, "Subjects enrolled successfully!", Toast.LENGTH_SHORT).show();
        db.close();
    }

    private static class Subject {
        int id;
        String name;
        int credits;

        Subject(int id, String name, int credits) {
            this.id = id;
            this.name = name;
            this.credits = credits;
        }
    }
}
