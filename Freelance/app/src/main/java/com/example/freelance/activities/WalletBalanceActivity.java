package com.example.freelance.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.R;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;

public class WalletBalanceActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_wallet_balance);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
TextView tvWalletBalance;
    FreelanceDbHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);

        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        dbHelper = new FreelanceDbHelper(this);

        // Get userId from SharedPreferences session (same as AddFundsActivity)
        userId = getSharedPreferences("session", MODE_PRIVATE).getInt("userId", -1);

        showWalletBalance();
    }

    private void showWalletBalance() {
        double balance = dbHelper.getWalletBalance(userId);
        tvWalletBalance.setText("Wallet Balance: $" + String.format("%.2f", balance));
    }
}