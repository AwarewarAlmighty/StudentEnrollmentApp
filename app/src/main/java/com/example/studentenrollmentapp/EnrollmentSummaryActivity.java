package com.example.studentenrollmentapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    private TextView tvSummary;
    private Button btnLogout;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        tvSummary = findViewById(R.id.tv_summary);
        btnLogout = findViewById(R.id.btn_logout);
        databaseHelper = new DatabaseHelper(this);

        loadEnrollmentSummary();

        // Set up the logout button click listener
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void loadEnrollmentSummary() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Get logged-in student_id from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("StudentSession", Context.MODE_PRIVATE);
        int studentId = preferences.getInt("student_id", -1);

        if (studentId == -1) {
            tvSummary.setText("Error: No logged-in user");
            return;
        }

        // Query to get the subjects enrolled by the logged-in student
        String query = "SELECT s.name, s.credits FROM " + DatabaseHelper.TABLE_ENROLLMENTS + " e " +
                "INNER JOIN " + DatabaseHelper.TABLE_SUBJECTS + " s " +
                "ON e.subject_id = s.id WHERE e.student_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(studentId)});

        StringBuilder summary = new StringBuilder();
        int totalCredits = 0;

        while (cursor.moveToNext()) {
            String subjectName = cursor.getString(0);
            int credits = cursor.getInt(1);
            totalCredits += credits;

            summary.append(subjectName).append(" (").append(credits).append(" credits)\n");
        }

        if (summary.length() == 0) {
            summary.append("No subjects enrolled.");
        }

        summary.append("\n\nTotal Credits: ").append(totalCredits);
        tvSummary.setText(summary.toString());

        cursor.close();
        db.close();
    }

    private void logoutUser() {
        // Clear the student session (student_id) from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("StudentSession", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("student_id");  // Remove the saved student_id
        editor.apply();  // Commit the changes

        // Show a Toast message
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(EnrollmentSummaryActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();  // Finish the current activity to prevent back navigation to the summary
    }
}
