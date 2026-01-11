package com.example.sharprewards.api;

import com.example.sharprewards.models.BasicResponse;
import com.example.sharprewards.models.ChallengeSubmitRequest;
import com.example.sharprewards.models.ChallengeSubmitResponse;
import com.example.sharprewards.models.GlobalLeaderboardEntry;
import com.example.sharprewards.models.LeaderboardEntry;
import com.example.sharprewards.models.LoginRequest;
import com.example.sharprewards.models.LoginResponse;
import com.example.sharprewards.models.RegisterRequest;
import com.example.sharprewards.models.Reward;
import com.example.sharprewards.models.TodayChallengeResponse;
import com.example.sharprewards.models.RedeemRequest;
import com.example.sharprewards.models.RedeemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest req);

    @POST("/auth/register")
    Call<BasicResponse> register(@Body RegisterRequest req);

    @GET("/challenge/today")
    Call<TodayChallengeResponse> getChallenge();

    @POST("/challenge/submit")
    Call<ChallengeSubmitResponse> submitChallenge(@Body ChallengeSubmitRequest req);


    // -------- LEADERBOARD ---------

    // -------- LEADERBOARD ---------

    @GET("/leaderboard/today")
    Call<List<LeaderboardEntry>> getTodayLeaderboard();

    @GET("/leaderboard/global")
    Call<List<GlobalLeaderboardEntry>> getGlobalLeaderboard();


    @GET("/leaderboard/area/{area}")
    Call<List<LeaderboardEntry>> getAreaLeaderboard(@Path("area") String area);

    @GET("/leaderboard/city/{city}")
    Call<List<LeaderboardEntry>> getCityLeaderboard(@Path("city") String city);

    @GET("/leaderboard/country/{country}")
    Call<List<LeaderboardEntry>> getCountryLeaderboard(@Path("country") String country);





    // -------- REWARDS ---------

    @GET("/rewards")
    Call<List<Reward>> getRewards();

    @POST("/rewards/redeem")
    Call<RedeemResponse> redeemReward(@Body RedeemRequest req);
}
