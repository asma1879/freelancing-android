package com.example.freelance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.Job;
import com.example.freelance.JobAdapter;
import com.example.freelance.R;
import com.example.freelance.utils.JobAdapter2;
import com.example.freelance.utils.RecyclerItemClickListener;

import java.util.ArrayList;
import androidx.appcompat.app.AlertDialog;


public class BrowseJobsActivity extends AppCompatActivity {

    RecyclerView jobRecyclerView;
    FreelanceDbHelper dbHelper;
    SQLiteDatabase db;
    ArrayList<Job> jobList = new ArrayList<>();
    JobAdapter2 jobAdapter;

    int freelancerId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_jobs);

        // Get logged in freelancerId from SharedPreferences (adjust key as per your app)
        freelancerId = getSharedPreferences("session", MODE_PRIVATE).getInt("userId", -1);

        jobRecyclerView = findViewById(R.id.jobRecyclerView);
        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getReadableDatabase();

        // Load jobs from DB
        Cursor c = db.rawQuery("SELECT id, client_id, title, description, budget, deadline, skills FROM jobs", null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            int clientId = c.getInt(1);
            String title = c.getString(2);
            String desc = c.getString(3);
            double budget = c.getDouble(4);
            String deadline = c.getString(5);
            String skills = c.getString(6);

            jobList.add(new Job(id, clientId, title, desc, budget, deadline, skills));
        }
        c.close();

        // Set layout manager and adapter, pass freelancerId and a callback method reference
        jobRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        jobAdapter = new JobAdapter2(this, jobList, freelancerId, this::saveJobToWishlist);
        jobRecyclerView.setAdapter(jobAdapter);

        // Optional: handle clicks on job items (opens ApplyBidActivity)
        jobRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, jobRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(BrowseJobsActivity.this, ApplyBidActivity.class);
                        i.putExtra("jobId", jobList.get(position).id);
                        startActivity(i);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        // Optional: handle long clicks
                    }
                })
        );
    }

    // This method is called by the adapter when user taps "Save" button
    public void saveJobToWishlist(int freelancerId, int jobId) {
        boolean success = dbHelper.addToWishlist(freelancerId, jobId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Wishlist");

        if (success) {
            builder.setMessage("Job added to wishlist!");
        } else {
            builder.setMessage("Job already in wishlist or error occurred");
        }

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}
