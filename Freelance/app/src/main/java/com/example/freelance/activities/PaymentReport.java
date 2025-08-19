package com.example.freelance.activities;

public class PaymentReport {
    public String jobTitle;
    public String freelancerName;
    public double amount;
    public String paymentDate;

    public PaymentReport(String jobTitle, String freelancerName, double amount, String paymentDate) {
        this.jobTitle = jobTitle;
        this.freelancerName = freelancerName;
        this.amount = amount;
        this.paymentDate = paymentDate;
    }
}
