package com.example.freelance.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.Bid;
import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;
import com.example.freelance.dao.BidDao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class PaymentActivity extends AppCompatActivity {

    Button payButton;
    FreelanceDbHelper dbHelper;

    int clientId = 2; // Replace with actual logged-in client ID
    int jobId;
    int freelancerId;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        payButton = findViewById(R.id.payButton);
        dbHelper = new FreelanceDbHelper(this);

        // Get the job ID from intent (from delivery click)
        jobId = getIntent().getIntExtra("jobId", -1);
        if (jobId == -1) {
            Toast.makeText(this, "Job ID missing!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch the best bid (e.g., lowest bid) for the job
        BidDao bidDao = new BidDao(this);
        Bid bestBid = bidDao.getBestBidForJob(jobId);

        if (bestBid == null) {
            Toast.makeText(this, "No bid found for this job!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        freelancerId = bestBid.freelancerId;
        amount = bestBid.amount;

        payButton.setOnClickListener(v -> {
            String paymentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

            boolean success = dbHelper.makePayment(jobId, clientId, freelancerId, amount, paymentDate);

            if (success) {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Payment failed or insufficient balance!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
