package com.example.freelance.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.R;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;
import com.example.freelance.utils.PaymentReportAdapter;

import java.util.ArrayList;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentReportActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_payment_report);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
RecyclerView paymentRecyclerView;
    FreelanceDbHelper dbHelper;
    int userId;
    ArrayList<PaymentReport> reportList = new ArrayList<>();
    PaymentReportAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_report);

        paymentRecyclerView = findViewById(R.id.paymentRecyclerView);
        dbHelper = new FreelanceDbHelper(this);
        userId = getSharedPreferences("session", MODE_PRIVATE).getInt("userId", -1);

        loadPaymentReport();

        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PaymentReportAdapter(reportList);
        paymentRecyclerView.setAdapter(adapter);
    }

    private void loadPaymentReport() {
        reportList.clear();

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT j.title, u.name, p.amount, p.payment_date " +
                        "FROM payments p " +
                        "JOIN jobs j ON p.job_id = j.id " +
                        "JOIN users u ON p.freelancer_id = u.id " +
                        "WHERE p.client_id = ? ORDER BY p.payment_date DESC",
                new String[]{String.valueOf(userId)}
        );

        while (cursor.moveToNext()) {
            String jobTitle = cursor.getString(0);
            String freelancerName = cursor.getString(1);
            double amount = cursor.getDouble(2);
            String date = cursor.getString(3);

            reportList.add(new PaymentReport(jobTitle, freelancerName, amount, date));
        }
        cursor.close();

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}