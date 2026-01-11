package com.example.sharprewards.models;

public class RedeemRequest {
    public String userId;
    public String rewardId;

    public RedeemRequest(String userId, String rewardId) {
        this.userId = userId;
        this.rewardId = rewardId;
    }
}
