package com.example.freelance.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.freelance.Bid;
import com.example.freelance.FreelanceDbHelper;

import java.util.ArrayList;

public class BidDao {
    private SQLiteDatabase db;

    public BidDao(Context context) {
        db = new FreelanceDbHelper(context).getWritableDatabase();
    }

    // Place a bid
    public long placeBid(Bid bid) {
        ContentValues cv = new ContentValues();
        cv.put("job_id", bid.jobId);
        cv.put("freelancer_id", bid.freelancerId);
        cv.put("amount", bid.amount);
        cv.put("message", bid.message);
        cv.put("status", bid.status);
        return db.insert("bids", null, cv);
    }

    // Get all bids for a job
    public ArrayList<Bid> getBidsForJob(int jobId) {
        ArrayList<Bid> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM bids WHERE job_id=?",
                new String[]{String.valueOf(jobId)});
        while (c.moveToNext()) {
            list.add(new Bid(
                    c.getInt(0),
                    c.getInt(1),
                    c.getInt(2),
                    c.getDouble(3),
                    c.getString(4),
                    c.getString(5)
            ));
        }
        return list;
    }

    // Get all bids by a freelancer
    public ArrayList<Bid> getMyBids(int freelancerId) {
        ArrayList<Bid> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM bids WHERE freelancer_id=?",
                new String[]{String.valueOf(freelancerId)});
        while (c.moveToNext()) {
            list.add(new Bid(
                    c.getInt(0),
                    c.getInt(1),
                    c.getInt(2),
                    c.getDouble(3),
                    c.getString(4),
                    c.getString(5)
            ));
        }
        return list;
    }
    // Get the best bid (e.g., lowest) for a given job
    public Bid getBestBidForJob(int jobId) {
        Cursor c = db.rawQuery(
                "SELECT * FROM bids WHERE job_id = ? ORDER BY amount ASC LIMIT 1",
                new String[]{String.valueOf(jobId)}
        );

        if (c.moveToFirst()) {
            return new Bid(
                    c.getInt(0),  // id
                    c.getInt(1),  // jobId
                    c.getInt(2),  // freelancerId
                    c.getDouble(3),  // amount
                    c.getString(4),  // message
                    c.getString(5)   // status
            );
        }

        return null; // No bid found
    }

}
