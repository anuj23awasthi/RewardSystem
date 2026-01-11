package com.example.sharprewards.models;

public class ChallengeSubmitRequest {
    public String userId;
    public int answer;

    public ChallengeSubmitRequest(String userId, int answer) {
        this.userId = userId;
        this.answer = answer;
    }
}

