package com.example.freelance.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;
import com.example.freelance.WithdrawModel;
import com.example.freelance.utils.WithdrawAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class WithdrawActivity extends AppCompatActivity {

    private TextView balanceText, statusText;
    private EditText amountInput, paymentCodeInput;
    private Spinner paymentMethodSpinner;
    private Button withdrawButton;
    private ListView reportList;

    private FreelanceDbHelper dbHelper;
    private int freelancerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        amountInput = findViewById(R.id.editTextWithdrawAmount);
        paymentCodeInput = findViewById(R.id.editTextPaymentCode);
        paymentMethodSpinner = findViewById(R.id.spinnerPaymentMethod);
        balanceText = findViewById(R.id.textViewBalance);
        statusText = findViewById(R.id.textViewStatus);
        withdrawButton = findViewById(R.id.buttonWithdraw);
        reportList = findViewById(R.id.withdrawReportList);

        dbHelper = new FreelanceDbHelper(this);


        freelancerId = getIntent().getIntExtra("freelancerId", -1);
        if (freelancerId == -1) {
            Toast.makeText(this, "Invalid freelancer ID", Toast.LENGTH_LONG).show();
            finish();
        }

        loadWalletBalance();
        loadWithdrawalHistory();

        withdrawButton.setOnClickListener(v -> processWithdrawal());
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.payment_methods,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paymentMethodSpinner.setAdapter(adapter);

    }

    private void loadWalletBalance() {
        double balance = dbHelper.getWalletBalance(freelancerId);
        balanceText.setText(String.format(Locale.getDefault(), "Balance: $%.2f", balance));
    }

    private void processWithdrawal() {
        String amountStr = amountInput.getText().toString().trim();
        String paymentCode = paymentCodeInput.getText().toString().trim();
        //String paymentMethod = paymentMethodSpinner.getSelectedItem().toString();
        Object selectedItem = paymentMethodSpinner.getSelectedItem();
        if (selectedItem == null) {
            statusText.setText("Please select a payment method");
            return;
        }
        String paymentMethod = selectedItem.toString();


        if (amountStr.isEmpty()) {
            statusText.setText("Please enter amount");
            return;
        }

        if (paymentCode.isEmpty()) {
            statusText.setText("Please enter payment number/code");
            return;
        }

        double amount = Double.parseDouble(amountStr);
        double currentBalance = dbHelper.getWalletBalance(freelancerId);

        if (amount <= 0) {
            statusText.setText("Amount must be greater than 0");
        } else if (amount > currentBalance) {
            statusText.setText("Insufficient wallet balance");
        } else {
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
            boolean success = dbHelper.withdrawAmount(freelancerId, amount, date, paymentMethod, paymentCode);

            if (success) {
                statusText.setText("Withdrawal successful");
                amountInput.setText("");
                paymentCodeInput.setText("");
                loadWalletBalance();
                loadWithdrawalHistory();
            } else {
                statusText.setText("Withdrawal failed");
            }
        }
    }


    private void loadWithdrawalHistory() {
        Cursor cursor = dbHelper.getWithdrawalsForFreelancer(freelancerId);
        ArrayList<WithdrawModel> historyList = new ArrayList<>();

        while (cursor.moveToNext()) {
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("withdraw_date"));
            String method = cursor.getString(cursor.getColumnIndexOrThrow("payment_method"));
            String code = cursor.getString(cursor.getColumnIndexOrThrow("payment_code"));
            historyList.add(new WithdrawModel(amount, date, method, code));
        }

        WithdrawAdapter adapter = new WithdrawAdapter(this, historyList);
        reportList.setAdapter(adapter);
        cursor.close();
    }
}
