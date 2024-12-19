package com.example.studentenrollmentapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class EnrollmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrollment);

        // Navigate to SubjectSelectionActivity
        findViewById(R.id.btn_select_subjects).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrollmentActivity.this, SubjectSelectionActivity.class);
                startActivity(intent);
            }
        });

        // Navigate to EnrollmentSummaryActivity
        findViewById(R.id.btn_view_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnrollmentActivity.this, EnrollmentSummaryActivity.class);
                startActivity(intent);
            }
        });
    }
}
