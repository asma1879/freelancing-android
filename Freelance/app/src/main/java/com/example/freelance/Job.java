package com.example.freelance;

public class Job {
    public int id, clientId;
    public String title, description, deadline, skills;
    public double budget;
    public Job() {}

    public Job(int id, int clientId, String title, String desc,
               double budget, String deadline, String skills) {
        this.id = id;
        this.clientId = clientId;
        this.title = title;
        this.description = desc;
        this.budget = budget;
        this.deadline = deadline;
        this.skills = skills;
    }
}
