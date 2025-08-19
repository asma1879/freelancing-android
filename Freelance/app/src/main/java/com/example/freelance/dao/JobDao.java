package com.example.freelance.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.Job;

import java.util.*;


public class JobDao {
    private SQLiteDatabase db;

    public JobDao(Context context) {
        db = new FreelanceDbHelper(context).getWritableDatabase();
    }

    // Post a job
    public long addJob(Job job) {
        ContentValues cv = new ContentValues();
        cv.put("client_id", job.clientId);
        cv.put("title", job.title);
        cv.put("description", job.description);
        cv.put("budget", job.budget);
        cv.put("deadline", job.deadline);
        cv.put("skills", job.skills);
        return db.insert("jobs", null, cv);
    }

    // Get all jobs
    public ArrayList<Job> getAllJobs() {
        ArrayList<Job> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM jobs", null);
        while (c.moveToNext()) {
            list.add(new Job(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getDouble(4),
                    c.getString(5),
                    c.getString(6)
            ));
        }
        return list;
    }

    // Get job by ID
    public Job getJobById(int jobId) {
        Cursor c = db.rawQuery("SELECT * FROM jobs WHERE id=?",
                new String[]{String.valueOf(jobId)});
        if (c.moveToFirst()) {
            return new Job(
                    c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getDouble(4),
                    c.getString(5),
                    c.getString(6)
            );
        }
        return null;
    }
    // Fetch jobs posted by a specific client
    // Fetch jobs posted by a specific client
    public List<Job> getJobsByClientId(int clientId) {
        List<Job> jobList = new ArrayList<>();

        // Use existing db object
        Cursor cursor = db.rawQuery("SELECT * FROM jobs WHERE client_id = ?", new String[]{String.valueOf(clientId)});

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("client_id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("description")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("budget")),
                        cursor.getString(cursor.getColumnIndexOrThrow("deadline")),
                        cursor.getString(cursor.getColumnIndexOrThrow("skills"))
                );
                jobList.add(job);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return jobList;
    }

}
