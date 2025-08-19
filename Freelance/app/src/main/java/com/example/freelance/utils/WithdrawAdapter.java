package com.example.freelance.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.freelance.R;
import com.example.freelance.WithdrawModel;

import java.util.List;

public class WithdrawAdapter extends ArrayAdapter<WithdrawModel> {
    private Context context;
    private List<WithdrawModel> withdrawals;

    public WithdrawAdapter(Context context, List<WithdrawModel> withdrawals) {
        super(context, 0, withdrawals);
        this.context = context;
        this.withdrawals = withdrawals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        WithdrawModel withdraw = withdrawals.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.withdraw_item, parent, false);
        }

        TextView amountView = convertView.findViewById(R.id.withdrawAmount);
        TextView dateView = convertView.findViewById(R.id.withdrawDate);
        TextView methodView = convertView.findViewById(R.id.withdrawMethod);
        TextView codeView = convertView.findViewById(R.id.withdrawCode);

        amountView.setText(String.format("Amount: $%.2f", withdraw.getAmount()));
        dateView.setText("Date: " + withdraw.getDate());
        methodView.setText("Method: " + withdraw.getPaymentMethod());
        codeView.setText("Code: " + withdraw.getPaymentCode());

        return convertView;
    }
}
