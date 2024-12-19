package com.example.studentenrollmentapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentSummaryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment_summary);

        tvSummary = findViewById(R.id.tv_summary);
        databaseHelper = new DatabaseHelper(this);

        loadEnrollmentSummary();
    }

    private void loadEnrollmentSummary() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT s.name, s.credits FROM " + DatabaseHelper.TABLE_ENROLLMENTS + " e " +
                "INNER JOIN " + DatabaseHelper.TABLE_SUBJECTS + " s " +
                "ON e.subject_id = s.id WHERE e.student_id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{"1"}); // Replace "1" with dynamic student ID

        StringBuilder summary = new StringBuilder();
        int totalCredits = 0;

        while (cursor.moveToNext()) {
            String subjectName = cursor.getString(0);
            int credits = cursor.getInt(1);
            totalCredits += credits;

            summary.append(subjectName).append(" (").append(credits).append(" credits)\n");
        }

        summary.append("\nTotal Credits: ").append(totalCredits);
        tvSummary.setText(summary.toString());

        cursor.close();
        db.close();
    }
}
