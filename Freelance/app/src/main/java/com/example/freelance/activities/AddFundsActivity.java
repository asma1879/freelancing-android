package com.example.freelance.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.freelance.R;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.R;

import android.widget.Spinner;
import android.widget.ArrayAdapter;

public class AddFundsActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_add_funds);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
TextView tvCurrentBalance;
    EditText etAddAmount;
    Button btnAddFunds;
    FreelanceDbHelper dbHelper;
    SQLiteDatabase db;
    int clientId;
    Spinner spinnerPaymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_funds);

        tvCurrentBalance = findViewById(R.id.tvCurrentBalance);
        etAddAmount = findViewById(R.id.etAddAmount);
        btnAddFunds = findViewById(R.id.btnAddFunds);
        spinnerPaymentMethod = findViewById(R.id.spinnerPaymentMethod);

        dbHelper = new FreelanceDbHelper(this);
        db = dbHelper.getWritableDatabase();

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        clientId = prefs.getInt("userId", -1);

        showCurrentBalance();
        setupPaymentMethodSpinner();

        btnAddFunds.setOnClickListener(v -> {
            String amountStr = etAddAmount.getText().toString();
            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
                return;
            }

            double amountToAdd = Double.parseDouble(amountStr);

            String selectedPaymentMethod = spinnerPaymentMethod.getSelectedItem().toString();

            // For now just show payment method chosen:
            Toast.makeText(this, "Adding funds via " + selectedPaymentMethod, Toast.LENGTH_SHORT).show();

            Cursor c = db.rawQuery("SELECT wallet_balance FROM users WHERE id = ?", new String[]{String.valueOf(clientId)});
            double current = 0;
            if (c.moveToFirst()) {
                current = c.getDouble(0);
            }
            c.close();

            double newBalance = current + amountToAdd;

            ContentValues cv = new ContentValues();
            cv.put("wallet_balance", newBalance);
            db.update("users", cv, "id = ?", new String[]{String.valueOf(clientId)});

            Toast.makeText(this, "Funds added!", Toast.LENGTH_SHORT).show();
            etAddAmount.setText("");
            showCurrentBalance();
        });
    }

    private void setupPaymentMethodSpinner() {
        String[] paymentMethods = {"Credit Card", "PayPal", "Bank Transfer"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paymentMethods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(adapter);
    }

    private void showCurrentBalance() {
        Cursor c = db.rawQuery("SELECT wallet_balance FROM users WHERE id = ?", new String[]{String.valueOf(clientId)});
        if (c.moveToFirst()) {
            double balance = c.getDouble(0);
            tvCurrentBalance.setText("Current Balance: $" + String.format("%.2f", balance));
        }
        c.close();
    }
}