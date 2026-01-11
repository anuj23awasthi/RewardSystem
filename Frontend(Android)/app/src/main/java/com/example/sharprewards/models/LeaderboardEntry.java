package com.example.sharprewards.models;

public class LeaderboardEntry {

    // Global leaderboard returns string
    public String _id;

    // For both global + daily
    public User user;

    // Daily leaderboard fields
    public Integer tokensEarned;
    public Boolean correct;
    public Boolean submitted;
    public String date;

    // Global leaderboard field
    public Integer totalTokens;
}
