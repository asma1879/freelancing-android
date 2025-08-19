package com.example.freelance.activities;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;
import com.example.freelance.utils.BidAdapter;

import java.util.ArrayList;

public class ClientBidsActivity extends AppCompatActivity {

    Spinner jobSpinner;

    ArrayList<String> bidList = new ArrayList<>();
    ArrayList<Integer> bidIds = new ArrayList<>();
    ArrayList<String> jobTitles = new ArrayList<>();
    ArrayList<Integer> jobIds = new ArrayList<>();

    ArrayAdapter<String> spinnerAdapter;

    FreelanceDbHelper dbHelper;
    SQLiteDatabase db;
    int clientId;
    RecyclerView bidRecyclerView;
    BidAdapter bidAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_bids);

        jobSpinner = findViewById(R.id.jobSpinner);
        bidRecyclerView = findViewById(R.id.bidRecyclerView);
        bidRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        bidAdapter = new BidAdapter(this, bidList, bidIds, new BidAdapter.OnBidActionListener() {
            @Override
            public void onAccept(int bidId) {
                acceptBid(bidId);
            }

            @Override
            public void onDelete(int bidId) {
                deleteBid(bidId);
            }
        });

        bidRecyclerView.setAdapter(bidAdapter);

        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getReadableDatabase();

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        clientId = prefs.getInt("userId", -1);

        loadJobTitles(); // Load jobs into dropdown

        jobSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedJobId = jobIds.get(position);
                loadBidsForJob(selectedJobId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void loadJobTitles() {
        jobTitles.clear();
        jobIds.clear();

        jobTitles.add("All Jobs");
        jobIds.add(-1); // -1 means "all jobs"

        Cursor jobCursor = db.rawQuery("SELECT id, title FROM jobs WHERE client_id = ?", new String[]{String.valueOf(clientId)});
        while (jobCursor.moveToNext()) {
            jobIds.add(jobCursor.getInt(0));
            jobTitles.add(jobCursor.getString(1));
        }
        jobCursor.close();

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, jobTitles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobSpinner.setAdapter(spinnerAdapter);
    }

    private void loadBidsForJob(int jobId) {
        bidList.clear();
        bidIds.clear();

        Cursor bidCursor;
        if (jobId == -1) {
            bidCursor = db.rawQuery(
                    "SELECT bids.id, bids.freelancer_id, bids.amount, bids.message, bids.status, jobs.title " +
                            "FROM bids INNER JOIN jobs ON bids.job_id = jobs.id WHERE jobs.client_id = ?",
                    new String[]{String.valueOf(clientId)}
            );
        } else {
            bidCursor = db.rawQuery(
                    "SELECT bids.id, bids.freelancer_id, bids.amount, bids.message, bids.status, jobs.title " +
                            "FROM bids INNER JOIN jobs ON bids.job_id = jobs.id WHERE jobs.client_id = ? AND jobs.id = ?",
                    new String[]{String.valueOf(clientId), String.valueOf(jobId)}
            );
        }

        while (bidCursor.moveToNext()) {
            int bidId = bidCursor.getInt(0);
            int freelancerId = bidCursor.getInt(1);
            double amount = bidCursor.getDouble(2);
            String message = bidCursor.getString(3);
            String status = bidCursor.getString(4);
            String title = bidCursor.getString(5);

            String item = "📌 Job: " + title +
                    "\n👤 Freelancer ID: " + freelancerId +
                    "\n💵 Bid: $" + amount +
                    "\n📄 Message: " + message +
                    "\n📋 Status: " + status;

            bidList.add(item);
            bidIds.add(bidId);
        }
        bidCursor.close();

        bidAdapter.notifyDataSetChanged();
    }

    private void showBidOptionsDialog(int bidId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Bid Action")
                .setItems(new String[]{"✅ Accept Bid", "❌ Delete Bid"}, (dialog, which) -> {
                    if (which == 0) {
                        acceptBid(bidId);
                    } else if (which == 1) {
                        deleteBid(bidId);
                    }
                })
                .show();
    }

    private void acceptBid(int bidId) {
        SQLiteDatabase writableDb = dbHelper.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("status", "Accepted");
        int rows = writableDb.update("bids", cv, "id = ?", new String[]{String.valueOf(bidId)});

        Cursor cursor = writableDb.rawQuery("SELECT job_id, freelancer_id FROM bids WHERE id = ?", new String[]{String.valueOf(bidId)});
        if (cursor.moveToFirst()) {
            int jobId = cursor.getInt(0);
            int freelancerId = cursor.getInt(1);

            Cursor check = writableDb.rawQuery("SELECT id FROM job_deliveries WHERE job_id = ? AND freelancer_id = ?", new String[]{String.valueOf(jobId), String.valueOf(freelancerId)});
            if (!check.moveToFirst()) {
                ContentValues deliveryValues = new ContentValues();
                deliveryValues.put("job_id", jobId);
                deliveryValues.put("freelancer_id", freelancerId);
                deliveryValues.put("status", "Ongoing");
                deliveryValues.put("message", "");
                writableDb.insert("job_deliveries", null, deliveryValues);
            }
            check.close();
        }
        cursor.close();

        if (rows > 0) {
            Toast.makeText(this, "Bid accepted and job assigned!", Toast.LENGTH_SHORT).show();
            int selectedJobId = jobIds.get(jobSpinner.getSelectedItemPosition());
            loadBidsForJob(selectedJobId); // refresh only selected job
        }
    }

    private void deleteBid(int bidId) {
        SQLiteDatabase writableDb = dbHelper.getWritableDatabase();
        int rows = writableDb.delete("bids", "id = ?", new String[]{String.valueOf(bidId)});
        if (rows > 0) {
            Toast.makeText(this, "Bid deleted!", Toast.LENGTH_SHORT).show();
            int selectedJobId = jobIds.get(jobSpinner.getSelectedItemPosition());
            loadBidsForJob(selectedJobId); // refresh only selected job
        }
    }
}
