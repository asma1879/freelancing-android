package com.example.freelance;

public class Bid {
    public int id, jobId, freelancerId;
    public double amount;
    public String message, status;

    public Bid(int id, int jobId, int freelancerId,
               double amount, String message, String status) {
        this.id = id;
        this.jobId = jobId;
        this.freelancerId = freelancerId;
        this.amount = amount;
        this.message = message;
        this.status = status;
    }
}
