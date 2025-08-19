package com.example.freelance;
import android.view.*;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{
    private List<Job> jobList;

    public JobAdapter(List<Job> jobList) {
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        Job job = jobList.get(position);
        holder.tvTitle.setText(job.title);
        holder.tvBudget.setText("Budget: $" + job.budget);
        holder.tvDeadline.setText("Deadline: " + job.deadline);
        holder.tvSkills.setText("Skills: " + job.skills);
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvBudget, tvDeadline, tvSkills;

        public JobViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvJobTitle);
            tvBudget = itemView.findViewById(R.id.tvJobBudget);
            tvDeadline = itemView.findViewById(R.id.tvJobDeadline);
            tvSkills = itemView.findViewById(R.id.tvJobSkills);
        }
    }
}
