package com.example.freelance.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.Context;
import android.view.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.freelance.R;

import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.ViewHolder>{
    private final List<String> bidList;
    private final List<Integer> bidIds;
    private final Context context;
    private final OnBidActionListener listener;

    public interface OnBidActionListener {
        void onAccept(int bidId);
        void onDelete(int bidId);
    }

    public BidAdapter(Context context, List<String> bidList, List<Integer> bidIds, OnBidActionListener listener) {
        this.context = context;
        this.bidList = bidList;
        this.bidIds = bidIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bid_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bidText.setText(bidList.get(position));

        holder.itemView.setOnClickListener(v -> {
            int bidId = bidIds.get(position);
            new AlertDialog.Builder(context)
                    .setTitle("Bid Action")
                    .setItems(new CharSequence[]{"✅ Accept Bid", "❌ Delete Bid"}, (dialog, which) -> {
                        if (which == 0) listener.onAccept(bidId);
                        else if (which == 1) listener.onDelete(bidId);
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bidText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bidText = itemView.findViewById(R.id.bidText);
        }
    }
}
