package com.example.freelance;

public class WithdrawModel {
    private double amount;
    private String date;
    private String paymentMethod;
    private String paymentCode;

    public WithdrawModel(double amount, String date, String paymentMethod, String paymentCode) {
        this.amount = amount;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.paymentCode = paymentCode;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentCode() {
        return paymentCode;
    }
}
