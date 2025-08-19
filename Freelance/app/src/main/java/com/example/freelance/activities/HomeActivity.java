package com.example.freelance.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;
//import com.example.freelance.WithdrawActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome, tvWalletBalance;
    private BottomNavigationView bottomNav;
    private LinearLayout clientSection, freelancerSection;
    private FreelanceDbHelper dbHelper;
    private int userId;
    ImageView imageProfileHome;


    // Card references for click listeners
    private MaterialCardView cardPostJob, cardMyJobs, cardBrowseJobs,
            cardMyBids, cardProfile, cardAddFunds,
            cardClientDeliveries, cardFreelancerMyJobs,
            cardClientsBids, cardPaymentReport,cardWithdraw,cardFreelancerPaymentReport,cardWishlist;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI components
        initializeViews();

        // Get session data
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        String role = prefs.getString("userRole", "Unknown");
        userId = prefs.getInt("userId", -1);

//        SharedPreferences profilePrefs = getSharedPreferences("profile", MODE_PRIVATE);
//        String imageUriString = profilePrefs.getString("imageUri", null);
//
//        if (imageUriString != null) {
//            Uri imageUri = Uri.parse(imageUriString);
//            imageProfileHome.setImageURI(imageUri);
//        }


        // Initialize database helper
        dbHelper = new FreelanceDbHelper(this);

        // Update UI based on role
        updateUI(role);

        // Setup bottom navigation
        setupBottomNavigation();

        // Setup card click listeners
        setupCardClickListeners();
    }

    private void initializeViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        bottomNav = findViewById(R.id.bottomNavigation);

        // Section containers
        clientSection = findViewById(R.id.clientSection);
        freelancerSection = findViewById(R.id.freelancerSection);

        // Card views
        cardPostJob = findViewById(R.id.cardPostJob);
        cardMyJobs = findViewById(R.id.cardMyJobs);
        cardMyBids = findViewById(R.id.cardMyBids);
        cardBrowseJobs = findViewById(R.id.cardBrowseJobs);
        cardProfile = findViewById(R.id.cardProfile);
        cardAddFunds = findViewById(R.id.cardAddFunds);
        cardFreelancerMyJobs = findViewById(R.id.cardFreelancerMyJobs);
        cardClientsBids = findViewById(R.id.cardClientBids);
        cardClientDeliveries = findViewById(R.id.cardClientDeliveries);
        cardPaymentReport = findViewById(R.id.cardPaymentReport);
        cardWithdraw = findViewById(R.id.cardWithdraw);
        cardFreelancerPaymentReport = findViewById(R.id.cardFreelancerPaymentReport);
        cardWishlist = findViewById(R.id.cardWishlist);

        //imageProfileHome = findViewById(R.id.imageProfileHome);


    }

    private void updateUI(String role) {
        tvWelcome.setText("Welcome, " + role);
        setupRoleBasedUI(role);
        displayWalletBalance();
    }

    private void setupRoleBasedUI(String role) {
        // Hide both sections initially
        clientSection.setVisibility(View.GONE);
        freelancerSection.setVisibility(View.GONE);

        // Show the appropriate section based on role
        if (role.equals("Client")) {
            clientSection.setVisibility(View.VISIBLE);
        } else if (role.equals("Freelancer")) {
            freelancerSection.setVisibility(View.VISIBLE);
        }
    }


    private void displayWalletBalance() {
        if (userId != -1) {
            double balance = dbHelper.getWalletBalance(userId);
            tvWalletBalance.setText("Wallet Balance: $" + String.format("%.2f", balance));
        } else {
            tvWalletBalance.setText("Wallet Balance: $0.00");
        }
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            Class<?> activityToOpen = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_profile) {
                activityToOpen = ProfileActivity.class;
            } else if (itemId == R.id.nav_jobs) {
                activityToOpen = JobListActivity.class;
            } else if (itemId == R.id.nav_bids) {
                activityToOpen = MyBidsActivity.class;
            } else if (itemId == R.id.nav_logout) {
                // Handle logout
                SharedPreferences.Editor editor = getSharedPreferences("session", MODE_PRIVATE).edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }

            if (activityToOpen != null) {
                startActivity(new Intent(this, activityToOpen));
                return true;
            }

            return false;
        });
    }

    private void setupCardClickListeners() {
        cardPostJob.setOnClickListener(v -> startActivity(new Intent(this, JobDetailsActivity.class)));
        cardMyJobs.setOnClickListener(v -> startActivity(new Intent(this, JobListActivity.class)));
        cardMyBids.setOnClickListener(v -> startActivity(new Intent(this, MyBidsActivity.class)));
        cardBrowseJobs.setOnClickListener(v -> startActivity(new Intent(this, BrowseJobsActivity.class)));
        cardProfile.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        cardAddFunds.setOnClickListener(v -> startActivity(new Intent(this, AddFundsActivity.class)));
        cardClientDeliveries.setOnClickListener(v -> startActivity(new Intent(this, ClientDeliveryActivity.class)));
        cardFreelancerMyJobs.setOnClickListener(v -> startActivity(new Intent(this, MyJobsActivity.class)));
        cardClientsBids.setOnClickListener(v -> startActivity(new Intent(this, ClientBidsActivity.class)));
        cardPaymentReport.setOnClickListener(v -> startActivity(new Intent(this, PaymentReportActivity.class)));
       // cardWithdraw.setOnClickListener(v -> startActivity(new Intent(this, WithdrawActivity.class)));
        cardFreelancerPaymentReport.setOnClickListener(v -> startActivity(new Intent(this, DeliveryReportActivity.class)));
        cardWithdraw.setOnClickListener(v -> {
            Intent intent = new Intent(this, WithdrawActivity.class);
            intent.putExtra("freelancerId", userId);
            startActivity(intent);
        });
//        imageProfileHome.setOnClickListener(v -> {
//            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
//        });
        cardWishlist.setOnClickListener(v -> {
            Intent intent = new Intent(this, WishlistActivity.class);
            intent.putExtra("userId", userId); // Pass user id if needed
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        displayWalletBalance(); // Refresh balance when returning to this activity
    }
}