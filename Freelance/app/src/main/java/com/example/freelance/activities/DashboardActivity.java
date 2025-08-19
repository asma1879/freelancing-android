package com.example.freelance.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class DashboardActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_dashboard);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
private TextView welcomeText;
    private Button btnAction1, btnAction2, btnProfile,btnAction3,btnAction4,btnLogout,btnAddMoney;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        welcomeText = findViewById(R.id.tvWelcome);
        btnAction1 = findViewById(R.id.btnAction1);
        btnAction2 = findViewById(R.id.btnAction2);
        btnAction3 = findViewById(R.id.btnAction3);
        btnAction4 = findViewById(R.id.btnAction4);
        btnProfile = findViewById(R.id.btnProfile);
        btnAddMoney = findViewById(R.id.btnAddMoney); // Find the button
        btnAddMoney.setVisibility(View.GONE);
        btnLogout = findViewById(R.id.btnLogout);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String role = prefs.getString("userRole", "Unknown");

        welcomeText.setText("Welcome " + role);

        if (role.equals("Client")) {
            btnAction1.setText("Post Job");
            btnAction2.setText("My Posted Jobs");
           // btnAction3.setText("Browse Jobs");
            btnAction4.setText("My Bids");
            btnAddMoney.setVisibility(View.VISIBLE);
//            Button btnAddMoney = findViewById(R.id.btnAddMoney);
//            btnAddMoney.setOnClickListener(v -> {
//                startActivity(new Intent(this, AddFundsActivity.class));
//            });
            btnAddMoney.setOnClickListener(v -> {
                startActivity(new Intent(this, AddFundsActivity.class));
            });


            btnAction1.setOnClickListener(v -> {
                // Go to post job page
                startActivity(new Intent(this, JobDetailsActivity.class));
            });

            btnAction2.setOnClickListener(v -> {
                startActivity(new Intent(this, JobListActivity.class));
            });
//            btnAction3.setOnClickListener(v -> {
//                startActivity(new Intent(this, BrowseJobsActivity.class));
//            });
//
            btnAction4.setOnClickListener(v -> {
                startActivity(new Intent(this, ClientBidsActivity.class));
            });

        } else if (role.equals("Freelancer")) {
            btnAction3.setText("Browse Jobs");
            btnAction4.setText("My Bids");

            btnAction3.setOnClickListener(v -> {
                startActivity(new Intent(this, BrowseJobsActivity.class));
            });

            btnAction4.setOnClickListener(v -> {
                startActivity(new Intent(this, MyBidsActivity.class));
            });

        } else if (role.equals("Admin")) {
            btnAction1.setText("Manage Users");
            btnAction2.setText("View Reports");

//            btnAction1.setOnClickListener(v -> {
//                startActivity(new Intent(this, AdminUserListActivity.class));
//            });

//            btnAction2.setOnClickListener(v -> {
//                startActivity(new Intent(this, AdminReportActivity.class));
//            });
        }

        btnProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        btnLogout.setOnClickListener(v -> {
            // Clear session
           // SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear(); // Removes userId and userRole
            editor.apply();

            // Redirect to login screen
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear back stack
            startActivity(intent);
            finish(); // Close dashboard
        });
    }

}