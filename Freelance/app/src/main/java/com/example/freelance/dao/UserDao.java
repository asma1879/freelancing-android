package com.example.freelance.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.freelance.FreelanceDbHelper;
import com.example.freelance.User;

public class UserDao {
    private SQLiteDatabase db;

    public UserDao(Context context) {
        db = new FreelanceDbHelper(context).getWritableDatabase();
    }

    // Register new user
    public long registerUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put("name", user.name);
        cv.put("email", user.email);
        cv.put("password", user.password);
        cv.put("role", user.role);
        cv.put("bio", user.bio);
        cv.put("skills", user.skills);
        cv.put("profile_image", user.profileImage);
        cv.put("wallet_balance", user.walletBalance); // ✅ include wallet_balance

        return db.insert("users", null, cv);
    }

    // Login
    public User login(String email, String password) {
        Cursor c = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?",
                new String[]{email, password});

        if (c.moveToFirst()) {
            User user = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("role")),
                    c.getString(c.getColumnIndexOrThrow("bio")),
                    c.getString(c.getColumnIndexOrThrow("skills")),
                    c.getString(c.getColumnIndexOrThrow("profile_image")),
                    c.getDouble(c.getColumnIndexOrThrow("wallet_balance")) // ✅ include wallet
            );
            c.close();
            return user;
        }
        c.close();
        return null;
    }

    // Get user by ID
    public User getUserById(int id) {
        Cursor c = db.rawQuery("SELECT * FROM users WHERE id=?",
                new String[]{String.valueOf(id)});

        if (c.moveToFirst()) {
            User user = new User(
                    c.getInt(c.getColumnIndexOrThrow("id")),
                    c.getString(c.getColumnIndexOrThrow("name")),
                    c.getString(c.getColumnIndexOrThrow("email")),
                    c.getString(c.getColumnIndexOrThrow("password")),
                    c.getString(c.getColumnIndexOrThrow("role")),
                    c.getString(c.getColumnIndexOrThrow("bio")),
                    c.getString(c.getColumnIndexOrThrow("skills")),
                    c.getString(c.getColumnIndexOrThrow("profile_image")),
                    c.getDouble(c.getColumnIndexOrThrow("wallet_balance"))
            );
            c.close();
            return user;
        }
        c.close();
        return null;
    }

    // Update profile
    public int updateUser(User user) {
        ContentValues cv = new ContentValues();
        cv.put("name", user.name);
        cv.put("email", user.email);
        cv.put("bio", user.bio);
        cv.put("skills", user.skills);
        cv.put("profile_image", user.profileImage);
        return db.update("users", cv, "id=?", new String[]{String.valueOf(user.id)});
    }

    // Optional: Update wallet only
    public int updateWallet(int userId, double newBalance) {
        ContentValues cv = new ContentValues();
        cv.put("wallet_balance", newBalance);
        return db.update("users", cv, "id=?", new String[]{String.valueOf(userId)});
    }
}
