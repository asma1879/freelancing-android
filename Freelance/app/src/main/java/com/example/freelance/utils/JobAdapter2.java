package com.example.freelance.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freelance.Job;
import com.example.freelance.R;
import com.example.freelance.activities.ApplyBidActivity;

import java.util.List;

public class JobAdapter2 extends RecyclerView.Adapter<JobAdapter2.JobViewHolder> {

    public interface WishlistCallback {
        void onSaveClicked(int freelancerId, int jobId);
    }

    private final List<Job> jobList;
    private final Context context;
    private final int freelancerId;
    private final WishlistCallback wishlistCallback;

    public JobAdapter2(Context context, List<Job> jobList, int freelancerId, WishlistCallback wishlistCallback) {
        this.context = context;
        this.jobList = jobList;
        this.freelancerId = freelancerId;
        this.wishlistCallback = wishlistCallback;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_jobs, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.tvTitle.setText(job.title);
        holder.tvBudget.setText("Budget: $" + job.budget);
        holder.tvDeadline.setText("Deadline: " + job.deadline);
        holder.tvSkills.setText("Skills: " + job.skills);

        holder.btnSave.setOnClickListener(v -> {
            if (wishlistCallback != null) {
                wishlistCallback.onSaveClicked(freelancerId, job.id);
            }
        });

        holder.btnApply.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplyBidActivity.class);
            intent.putExtra("jobId", job.id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBudget, tvDeadline, tvSkills;
        Button btnSave, btnApply;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvBudget = itemView.findViewById(R.id.tvJobBudget);
            tvDeadline = itemView.findViewById(R.id.tvJobDeadline);
            tvSkills = itemView.findViewById(R.id.tvJobSkills);
            btnSave = itemView.findViewById(R.id.btnSave);
            btnApply = itemView.findViewById(R.id.btnApply);
        }
    }
}
