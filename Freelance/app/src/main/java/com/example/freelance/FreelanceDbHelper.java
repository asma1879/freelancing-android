package com.example.freelance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FreelanceDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "freelance.db";
    private static final int VERSION = 8; // Updated version

    public FreelanceDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT UNIQUE, " +
                "password TEXT, " +
                "role TEXT, " +
                "bio TEXT, " +
                "skills TEXT, " +
                "profile_image TEXT, " +
                "wallet_balance REAL DEFAULT 0.0)");

        db.execSQL("CREATE TABLE jobs (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "client_id INTEGER, " +
                "title TEXT, " +
                "description TEXT, " +
                "budget REAL, " +
                "deadline TEXT, " +
                "skills TEXT)");

        db.execSQL("CREATE TABLE bids (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "job_id INTEGER, " +
                "freelancer_id INTEGER, " +
                "amount REAL, " +
                "message TEXT, " +
                "status TEXT DEFAULT 'Pending')");

        db.execSQL("CREATE TABLE job_deliveries (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "job_id INTEGER, " +
                "freelancer_id INTEGER, " +
                "client_id INTEGER, " +
                "delivery_message TEXT, " +
                "timestamp TEXT)");

        db.execSQL("CREATE TABLE payments (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "job_id INTEGER, " +
                "freelancer_id INTEGER, " +
                "client_id INTEGER, " +
                "amount REAL, " +
                "payment_date TEXT, " +
                "status TEXT DEFAULT 'Pending')");

        db.execSQL("CREATE TABLE withdrawals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "freelancer_id INTEGER, " +
                "amount REAL, " +
                "withdraw_date TEXT, " +
                "payment_method TEXT, " +
                "payment_code TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE users ADD COLUMN wallet_balance REAL DEFAULT 0.0");
        }

        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE job_deliveries (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "job_id INTEGER, " +
                    "freelancer_id INTEGER, " +
                    "client_id INTEGER, " +
                    "delivery_message TEXT, " +
                    "timestamp TEXT)");
        }

        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE payments (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "job_id INTEGER, " +
                    "freelancer_id INTEGER, " +
                    "client_id INTEGER, " +
                    "amount REAL, " +
                    "payment_date TEXT, " +
                    "status TEXT DEFAULT 'Pending')");
        }

        if (oldVersion < 5) {
            db.execSQL("CREATE TABLE withdrawals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "freelancer_id INTEGER, " +
                    "amount REAL, " +
                    "withdraw_date TEXT)");
        }

        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE withdrawals ADD COLUMN payment_method TEXT");
            db.execSQL("ALTER TABLE withdrawals ADD COLUMN payment_code TEXT");
        }
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE job_deliveries ADD COLUMN submission_link TEXT");
        }
        if (oldVersion < 8) {
            db.execSQL("CREATE TABLE wishlist (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "freelancer_id INTEGER, " +
                    "job_id INTEGER, " +
                    "UNIQUE(freelancer_id, job_id))"); // Prevent duplicates
        }

    }

    public double getWalletBalance(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT wallet_balance FROM users WHERE id = ?", new String[]{String.valueOf(userId)});
        double balance = 0.0;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(0);
        }
        cursor.close();
        return balance;
    }

    public void updateWalletBalance(int userId, double newBalance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("wallet_balance", newBalance);
        db.update("users", values, "id = ?", new String[]{String.valueOf(userId)});
    }

    public boolean makePayment(int jobId, int clientId, int freelancerId, double amount, String paymentDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        double clientBalance = getWalletBalance(clientId);
        if (clientBalance < amount) return false;

        db.beginTransaction();
        try {
            updateWalletBalance(clientId, clientBalance - amount);
            updateWalletBalance(freelancerId, getWalletBalance(freelancerId) + amount);

            ContentValues values = new ContentValues();
            values.put("job_id", jobId);
            values.put("client_id", clientId);
            values.put("freelancer_id", freelancerId);
            values.put("amount", amount);
            values.put("payment_date", paymentDate);
            values.put("status", "Completed");
            db.insert("payments", null, values);

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    // ✅ Updated method to handle method + code
    public boolean withdrawAmount(int freelancerId, double amount, String withdrawDate, String paymentMethod, String paymentCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        double currentBalance = getWalletBalance(freelancerId);
        if (currentBalance < amount) return false;

        db.beginTransaction();
        try {
            updateWalletBalance(freelancerId, currentBalance - amount);

            ContentValues values = new ContentValues();
            values.put("freelancer_id", freelancerId);
            values.put("amount", amount);
            values.put("withdraw_date", withdrawDate);
            values.put("payment_method", paymentMethod);
            values.put("payment_code", paymentCode);

            db.insert("withdrawals", null, values);

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public Cursor getWithdrawalsForFreelancer(int freelancerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM withdrawals WHERE freelancer_id = ? ORDER BY withdraw_date DESC", new String[]{String.valueOf(freelancerId)});
    }
    public boolean addToWishlist(int freelancerId, int jobId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("freelancer_id", freelancerId);
        values.put("job_id", jobId);

        long result = db.insertWithOnConflict("wishlist", null, values, SQLiteDatabase.CONFLICT_IGNORE);
        return result != -1;
    }

    public boolean removeFromWishlist(int freelancerId, int jobId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("wishlist", "freelancer_id=? AND job_id=?", new String[]{String.valueOf(freelancerId), String.valueOf(jobId)});
        return rows > 0;
    }

    public Cursor getWishlistForFreelancer(int freelancerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT jobs.* FROM jobs " +
                "JOIN wishlist ON jobs.id = wishlist.job_id " +
                "WHERE wishlist.freelancer_id = ?", new String[]{String.valueOf(freelancerId)});
    }

}
