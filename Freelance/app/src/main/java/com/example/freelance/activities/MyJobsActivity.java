package com.example.freelance.activities;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyJobsActivity extends AppCompatActivity {

    LinearLayout layoutJobs;
    SQLiteDatabase db;
    FreelanceDbHelper dbHelper;
    int freelancerId = 1; // TODO: Replace with actual logged-in freelancer ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);

        layoutJobs = findViewById(R.id.layoutJobs);
        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getWritableDatabase();

        loadAcceptedJobs();
    }

    private void loadAcceptedJobs() {
        Cursor cursor = db.rawQuery(
                "SELECT bids.id AS bid_id, jobs.id AS job_id, jobs.title, jobs.client_id " +
                        "FROM bids INNER JOIN jobs ON bids.job_id = jobs.id " +
                        "WHERE bids.freelancer_id=? AND bids.status='Accepted'",
                new String[]{String.valueOf(freelancerId)}
        );
        layoutJobs.removeAllViews();

        if (cursor.getCount() == 0) {
            TextView noJobsText = new TextView(this);
            noJobsText.setText("No accepted jobs to show.");
            noJobsText.setPadding(16,16,16,16);
            layoutJobs.addView(noJobsText);
        }

        while (cursor.moveToNext()) {
            int bidId = cursor.getInt(cursor.getColumnIndexOrThrow("bid_id"));
            int jobId = cursor.getInt(cursor.getColumnIndexOrThrow("job_id"));
            int clientId = cursor.getInt(cursor.getColumnIndexOrThrow("client_id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));

            View jobCard = LayoutInflater.from(this).inflate(R.layout.job_card_with_submit, null);

            TextView txtTitle = jobCard.findViewById(R.id.txtTitle);
            Button btnSubmit = jobCard.findViewById(R.id.btnSubmit);

            txtTitle.setText(title);
            btnSubmit.setOnClickListener(v -> showSubmitDialog(jobId, freelancerId, clientId));

            layoutJobs.addView(jobCard);
        }
        cursor.close();
    }

    private void showSubmitDialog(int jobId, int freelancerId, int clientId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_submit_work, null);
        EditText editMessage = dialogView.findViewById(R.id.editMessage);
        //Button btnSend = dialogView.findViewById(R.id.btnSend);
        EditText editLink = dialogView.findViewById(R.id.editLink);
        Button btnSend = dialogView.findViewById(R.id.btnSend);


        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString().trim();
            String link = editLink.getText().toString().trim();
            if (message.isEmpty()) {
                Toast.makeText(this, "Enter a message.", Toast.LENGTH_SHORT).show();
                return;
            }

            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            ContentValues values = new ContentValues();
            values.put("job_id", jobId);
            values.put("freelancer_id", freelancerId);
            values.put("client_id", clientId);
            values.put("delivery_message", message);
            values.put("submission_link", link);

            values.put("timestamp", timestamp);

            long result = db.insert("job_deliveries", null, values);
            if (result != -1) {
                // Update bid status to Submitted to hide from My Jobs
                ContentValues updateValues = new ContentValues();
                updateValues.put("status", "Submitted");
                int rowsUpdated = db.update("bids", updateValues, "job_id=? AND freelancer_id=?", new String[]{String.valueOf(jobId), String.valueOf(freelancerId)});

                if (rowsUpdated > 0) {
                    Toast.makeText(this, "Work submitted successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    loadAcceptedJobs(); // Refresh list, submitted job disappears
                } else {
                    Toast.makeText(this, "Failed to update job status.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Submission failed.", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}
