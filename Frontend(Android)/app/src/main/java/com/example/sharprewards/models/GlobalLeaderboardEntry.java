package com.example.sharprewards.models;

public class GlobalLeaderboardEntry {
    public User _id;          // because _id contains full USER object from aggregation
    public int totalTokens;   // not tokensEarned
}
