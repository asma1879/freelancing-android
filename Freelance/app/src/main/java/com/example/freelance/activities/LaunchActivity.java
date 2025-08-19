package com.example.freelance.activities;

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

import androidx.appcompat.app.AppCompatActivity;


public class LaunchActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_launch);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//    }
//@Override
//protected void onCreate(Bundle savedInstanceState) {
//    super.onCreate(savedInstanceState);
//
//    SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
//    int userId = prefs.getInt("userId", -1);
//    String role = prefs.getString("userRole", null);
//
//    if (userId > 0 && role != null) {
//        // User is logged in
//        startActivity(new Intent(this, JobListActivity.class));
//    } else {
//        // User not logged in
//        startActivity(new Intent(this, LoginActivity.class));
//    }
//
//    finish(); // Don't return to this activity
//}
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
    int userId = prefs.getInt("userId", -1);
    String role = prefs.getString("userRole", null);

    Intent intent;

    if (userId > 0 && role != null) {
        // User is logged in → go to Dashboard
        intent = new Intent(this, HomeActivity.class);
    } else {
        // User not logged in → go to Login
        intent = new Intent(this, LoginActivity.class);
    }

    startActivity(intent);
    finish(); // Prevent returning here
}

}