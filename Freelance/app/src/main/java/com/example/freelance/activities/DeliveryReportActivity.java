package com.example.freelance.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.R;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;

import java.util.Locale;

public class DeliveryReportActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_delivery_report);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
ListView listViewDeliveries;
    FreelanceDbHelper dbHelper;
    SQLiteDatabase db;
    int freelancerId = 1; // TODO: Replace with logged-in user ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_report);

        listViewDeliveries = findViewById(R.id.listViewDeliveries);
        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getReadableDatabase();

        loadDeliveryHistory();
    }

    private void loadDeliveryHistory() {
        String query = "SELECT jd.id, j.title, jd.delivery_message, jd.timestamp " +
                "FROM job_deliveries jd INNER JOIN jobs j ON jd.job_id = j.id " +
                "WHERE jd.freelancer_id = ? ORDER BY jd.timestamp DESC";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(freelancerId)});

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No deliveries found.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Simple ArrayAdapter with concatenated strings (can be replaced with custom adapter)
        String[] deliveriesArray = new String[cursor.getCount()];
        int index = 0;

        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String message = cursor.getString(cursor.getColumnIndexOrThrow("delivery_message"));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));

            deliveriesArray[index++] = String.format(Locale.getDefault(),
                    "Job: %s\nMessage: %s\nDate: %s", title, message, timestamp);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, deliveriesArray);
        listViewDeliveries.setAdapter(adapter);
    }
}