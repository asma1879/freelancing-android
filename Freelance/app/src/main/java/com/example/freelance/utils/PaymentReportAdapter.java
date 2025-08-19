package com.example.freelance.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelance.R;
import com.example.freelance.activities.PaymentReport;

import java.util.List;

public class PaymentReportAdapter extends RecyclerView.Adapter<PaymentReportAdapter.ViewHolder>{
    private List<PaymentReport> reports;

    public PaymentReportAdapter(List<PaymentReport> reports) {
        this.reports = reports;
    }

    @NonNull
    @Override
    public PaymentReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentReportAdapter.ViewHolder holder, int position) {
        PaymentReport report = reports.get(position);
        holder.tvJobTitle.setText(report.jobTitle);
        holder.tvFreelancerName.setText(report.freelancerName);
        holder.tvAmount.setText(String.format("$%.2f", report.amount));
        holder.tvPaymentDate.setText(report.paymentDate);
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobTitle, tvFreelancerName, tvAmount, tvPaymentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobTitle = itemView.findViewById(R.id.tvJobTitle);
            tvFreelancerName = itemView.findViewById(R.id.tvFreelancerName);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvPaymentDate = itemView.findViewById(R.id.tvPaymentDate);
        }
    }
}
