package com.example.freelance.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.Job;
import com.example.freelance.R;
import com.example.freelance.dao.JobDao;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;


public class JobDetailsActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_job_details);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
private EditText etTitle, etDescription, etBudget, etDeadline, etSkills;
    private Button btnPostJob;
    private JobDao jobDao;
    private int clientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etBudget = findViewById(R.id.etBudget);
        etDeadline = findViewById(R.id.etDeadline);
        etSkills = findViewById(R.id.etSkills);
        btnPostJob = findViewById(R.id.btnPostJob);

        jobDao = new JobDao(this);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        clientId = prefs.getInt("userId", -1);

        btnPostJob.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            String budgetStr = etBudget.getText().toString().trim();
            String deadline = etDeadline.getText().toString().trim();
            String skills = etSkills.getText().toString().trim();

            if (title.isEmpty() || desc.isEmpty() || budgetStr.isEmpty() || deadline.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double budget;
            try {
                budget = Double.parseDouble(budgetStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid budget", Toast.LENGTH_SHORT).show();
                return;
            }

            Job job = new Job(0, clientId, title, desc, budget, deadline, skills);
            long result = jobDao.addJob(job);

            if (result > 0) {
                Toast.makeText(this, "Job posted successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Go back to dashboard
            } else {
                Toast.makeText(this, "Failed to post job.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}