package com.example.freelance;

public class User {
    public int id;
    public String name, email, password, role, bio, skills, profileImage;
    public double walletBalance;


    public User(int id, String name, String email, String password, String role,
                String bio, String skills, String profileImage, double walletBalance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.bio = bio;
        this.skills = skills;
        this.profileImage = profileImage;
        this.walletBalance = walletBalance;
    }
}
