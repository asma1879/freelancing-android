package com.example.freelance.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;

import java.util.ArrayList;

public class ClientDeliveryActivity extends AppCompatActivity {

    ListView deliveryListView;
    Button btnMakePayment;
    ArrayList<String> deliveries = new ArrayList<>();
    ArrayAdapter<String> adapter;
    FreelanceDbHelper dbHelper;
    SQLiteDatabase db;

    int clientId = 2; // Replace with actual logged-in client ID



    // Keep track of the selected delivery's job_id or delivery_id to pass it later
    int selectedDeliveryJobId = -1;
    ArrayList<String> deliveryLinks = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_delivery);

        deliveryListView = findViewById(R.id.deliveryListView);
        btnMakePayment = findViewById(R.id.btnMakePayment);
        btnMakePayment.setVisibility(View.GONE); // Hide button initially

        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getReadableDatabase();

        // Modify your query to also select the delivery ID or job_id to track it
        Cursor cursor = db.rawQuery(
                "SELECT d.id, j.title, u.name, d.delivery_message, d.timestamp, d.job_id,d.submission_link " +
                        "FROM job_deliveries d " +
                        "INNER JOIN jobs j ON d.job_id = j.id " +
                        "INNER JOIN users u ON d.freelancer_id = u.id " +
                        "WHERE d.client_id = ?", new String[]{String.valueOf(clientId)}
        );

        ArrayList<Integer> deliveryJobIds = new ArrayList<>(); // Keep track of job_ids per delivery

        while (cursor.moveToNext()) {
            int deliveryId = cursor.getInt(0);
            String jobTitle = cursor.getString(1);
            String freelancerName = cursor.getString(2);
            String deliveryText = cursor.getString(3);
            String date = cursor.getString(4);
            int jobId = cursor.getInt(5);
            String submission_link = cursor.getString(6); // index 6 because it’s the 7th column


            deliveryJobIds.add(jobId);
            deliveryLinks.add(submission_link);


            deliveries.add("📌 Job Title : " + jobTitle +
                    "\n👤 Freelancer Name : " + freelancerName +
                    "\n📦 Delivery Message : " + deliveryText +
                    "\n📅 Date & Time : " + date+
                    "\n🔗 Delivery Link: " + submission_link);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deliveries);
        deliveryListView.setAdapter(adapter);

        // Show payment button when a delivery is selected
//        deliveryListView.setOnItemClickListener((parent, view, position, id) -> {
//            selectedDeliveryJobId = deliveryJobIds.get(position);
//            btnMakePayment.setVisibility(View.VISIBLE);
//            String selectedLink = deliveryLinks.get(position);
//            if (selectedLink != null && !selectedLink.isEmpty()) {
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(android.net.Uri.parse(selectedLink));
//                startActivity(intent); // Open the link in browser or file viewer
//            }
//
//        });
//        deliveryListView.setOnItemClickListener((parent, view, position, id) -> {
//            selectedDeliveryJobId = deliveryJobIds.get(position);
//            btnMakePayment.setVisibility(View.VISIBLE);
//        });
        deliveryListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedDeliveryJobId = deliveryJobIds.get(position);
            btnMakePayment.setVisibility(View.VISIBLE);
            // Optionally, show the link in a Toast or Dialog instead of opening it automatically
            String selectedLink = deliveryLinks.get(position);
            if (selectedLink != null && !selectedLink.isEmpty()) {
                // For example, show a dialog with options
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Delivery Link")
                        .setMessage(selectedLink)
                        .setPositiveButton("Open Link", (dialog, which) -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(android.net.Uri.parse(selectedLink));
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });


        btnMakePayment.setOnClickListener(v -> {
            if (selectedDeliveryJobId != -1) {
                Intent intent = new Intent(ClientDeliveryActivity.this, PaymentActivity.class);
                intent.putExtra("jobId", selectedDeliveryJobId); // pass selected job id
                startActivity(intent);
            }
        });
    }
}
