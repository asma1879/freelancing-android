package com.example.freelance.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.Job;
import com.example.freelance.JobAdapter;
import com.example.freelance.R;
import com.example.freelance.dao.JobDao;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class JobListActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_job_list);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
private RecyclerView recyclerMyJobs;
    private JobDao jobDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        recyclerMyJobs = findViewById(R.id.recyclerMyJobs);
        recyclerMyJobs.setLayoutManager(new LinearLayoutManager(this));

        jobDao = new JobDao(this);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        int clientId = prefs.getInt("userId", -1);

        List<Job> jobs = jobDao.getJobsByClientId(clientId);

        JobAdapter adapter = new JobAdapter(jobs);
        recyclerMyJobs.setAdapter(adapter);
    }
}