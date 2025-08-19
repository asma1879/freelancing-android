package com.example.freelance.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.Job;
import com.example.freelance.JobAdapter;
import com.example.freelance.R;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WishlistActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_wishlist);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
RecyclerView recyclerWishlist;
    TextView tvNoWishlist;
    FreelanceDbHelper dbHelper;
    List<Job> wishlistJobs;
    JobAdapter adapter;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        recyclerWishlist = findViewById(R.id.recyclerWishlist);
        tvNoWishlist = findViewById(R.id.tvNoWishlist);
        dbHelper = new FreelanceDbHelper(this);

        int freelancerId = 123; // Replace with real logged-in freelancer ID

        wishlistJobs = new ArrayList<>();
        Cursor cursor = dbHelper.getWishlistForFreelancer(freelancerId);

        if (cursor.moveToFirst()) {
            do {
                Job job = new Job();  // Make sure you have default constructor in Job class
                job.id = cursor.getInt(cursor.getColumnIndex("id"));
                job.title = cursor.getString(cursor.getColumnIndex("title"));
                job.budget = cursor.getDouble(cursor.getColumnIndex("budget"));  // use getDouble here
                job.deadline = cursor.getString(cursor.getColumnIndex("deadline"));
                job.skills = cursor.getString(cursor.getColumnIndex("skills"));
                wishlistJobs.add(job);

            } while (cursor.moveToNext());
        }

        if (wishlistJobs.isEmpty()) {
            tvNoWishlist.setVisibility(View.VISIBLE);
        } else {
            adapter = new JobAdapter(wishlistJobs);
            recyclerWishlist.setLayoutManager(new LinearLayoutManager(this));
            recyclerWishlist.setAdapter(adapter);
        }

        cursor.close();
    }
}