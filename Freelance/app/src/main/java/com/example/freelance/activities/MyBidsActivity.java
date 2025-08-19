package com.example.freelance.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class MyBidsActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_my_bids);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
ListView listBids;
    ArrayList<String> bids = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FreelanceDbHelper dbHelper;
    SQLiteDatabase db;
    int freelancerId = 1; // Replace with actual session user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bids);

        listBids = findViewById(R.id.listBids);
        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT j.title, b.amount, b.status FROM bids b INNER JOIN jobs j ON b.job_id = j.id WHERE b.freelancer_id = ?", new String[]{String.valueOf(freelancerId)});
        while (c.moveToNext()) {
            String jobTitle = c.getString(0);
            double amount = c.getDouble(1);
            String status = c.getString(2);
            bids.add("🔹 " + jobTitle + "\n💰 $" + amount + " | 📌 Status: " + status);
        }
        c.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bids);
        listBids.setAdapter(adapter);
    }
}