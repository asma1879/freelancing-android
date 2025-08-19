package com.example.freelance.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.freelance.R;
import com.example.freelance.User;
import com.example.freelance.dao.UserDao;

import java.io.InputStream;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 101;
    private ImageView imgProfile;
    private EditText etName, etBio, etSkills;
    private EditText etEmail;

    private Button btnSave, btnChangeImage;

    private TextView tvWallet;
    private EditText etAddMoney;
    private Button btnAddMoney;

    private Uri selectedImageUri = null;
    private UserDao userDao;
    private User currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        imgProfile = findViewById(R.id.imgProfile);
        etName = findViewById(R.id.etName);
        etBio = findViewById(R.id.etBio);
        etSkills = findViewById(R.id.etSkills);
        etEmail = findViewById(R.id.etEmail);

        btnSave = findViewById(R.id.btnSave);
        btnChangeImage = findViewById(R.id.btnChangeImage);

        // Wallet UI
        tvWallet = findViewById(R.id.tvWallet);
        //etAddMoney = findViewById(R.id.etAddMoney);
        //btnAddMoney = findViewById(R.id.btnAddMoney);

        userDao = new UserDao(this);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        if (userId > 0) {
            currentUser = userDao.getUserById(userId);
            if (currentUser != null) {
                etName.setText(currentUser.name);
                etBio.setText(currentUser.bio);
                etSkills.setText(currentUser.skills);
                etEmail.setText(currentUser.email);

                tvWallet.setText("Wallet: $ " + currentUser.walletBalance);

                if (currentUser.profileImage != null) {
                    try {
//                        InputStream stream = getContentResolver().openInputStream(Uri.parse(currentUser.profileImage));
//                        imgProfile.setImageBitmap(BitmapFactory.decodeStream(stream));
                        imgProfile.setImageBitmap(BitmapFactory.decodeFile(currentUser.profileImage));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        btnChangeImage.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType("image/*");
            startActivityForResult(i, PICK_IMAGE);
        });

        btnSave.setOnClickListener(v -> {
            currentUser.name = etName.getText().toString().trim();
            currentUser.bio = etBio.getText().toString().trim();
            currentUser.skills = etSkills.getText().toString().trim();
            currentUser.email = etEmail.getText().toString().trim();


//            if (selectedImageUri != null) {
//                currentUser.profileImage = selectedImageUri.toString();
//            }
            if (selectedImageUri != null) {
                String imagePath = saveImageToInternalStorage(selectedImageUri);
                if (imagePath != null) {
                    currentUser.profileImage = imagePath;
                }
            }


            int rows = userDao.updateUser(currentUser);
            if (rows > 0) {
                Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
            }
        });


//        btnAddMoney.setOnClickListener(v -> {
//            String amountStr = etAddMoney.getText().toString().trim();
//            if (!amountStr.isEmpty()) {
//                double addAmount = Double.parseDouble(amountStr);
//                currentUser.walletBalance += addAmount;
//
//                int rows = userDao.updateUser(currentUser);
//                if (rows > 0) {
//                    tvWallet.setText("Wallet: $ " + currentUser.walletBalance);
//                    Toast.makeText(this, "Money added!", Toast.LENGTH_SHORT).show();
//                    etAddMoney.setText("");
//                } else {
//                    Toast.makeText(this, "Failed to add money.", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(this, "Enter amount", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imgProfile.setImageURI(selectedImageUri);
        }
    }
    private String saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();

            // Save to internal storage
            String filename = "profile_" + userId + ".jpg";
            openFileOutput(filename, MODE_PRIVATE).write(buffer);

            // Return the path
            return getFilesDir() + "/" + filename;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
