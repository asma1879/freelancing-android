package com.example.freelance.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ApplyBidActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_apply_bid);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
EditText etAmount, etMessage;
    Button btnSubmit;
    FreelanceDbHelper dbHelper;
    int jobId, freelancerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_bid);

        etAmount = findViewById(R.id.etAmount);
        etMessage = findViewById(R.id.etMessage);
        btnSubmit = findViewById(R.id.btnSubmit);
        dbHelper = new FreelanceDbHelper(this);

        jobId = getIntent().getIntExtra("jobId", -1);
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        freelancerId = prefs.getInt("userId", -1);

        btnSubmit.setOnClickListener(v -> {
            double amount = Double.parseDouble(etAmount.getText().toString());
            String message = etMessage.getText().toString();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("job_id", jobId);
            cv.put("freelancer_id", freelancerId);
            cv.put("amount", amount);
            cv.put("message", message);
            cv.put("status", "Pending");

            long result = db.insert("bids", null, cv);
            if (result != -1) {
                Toast.makeText(this, "Bid submitted!", Toast.LENGTH_SHORT).show();
                finish(); // go back to previous screen
            } else {
                Toast.makeText(this, "Error submitting bid", Toast.LENGTH_SHORT).show();
            }
        });
    }
}