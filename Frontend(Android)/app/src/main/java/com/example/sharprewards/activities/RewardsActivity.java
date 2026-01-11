package com.example.sharprewards.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sharprewards.R;
import com.example.sharprewards.adapters.RewardsAdapter;
import com.example.sharprewards.api.ApiService;
import com.example.sharprewards.api.RetrofitClient;
import com.example.sharprewards.models.RedeemRequest;
import com.example.sharprewards.models.RedeemResponse;
import com.example.sharprewards.models.Reward;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RewardsActivity extends AppCompatActivity {

    RecyclerView rewardList;
    RewardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        rewardList = findViewById(R.id.rewardList);
        rewardList.setLayoutManager(new LinearLayoutManager(this));

        loadRewards();
    }

    private void loadRewards() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getRewards().enqueue(new Callback<List<Reward>>() {
            @Override
            public void onResponse(Call<List<Reward>> call, Response<List<Reward>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    adapter = new RewardsAdapter(response.body());

                    adapter.setOnRedeemClickListener(reward -> redeemReward(reward));

                    rewardList.setAdapter(adapter);

                } else {
                    Toast.makeText(RewardsActivity.this,
                            "Failed to load rewards",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Reward>> call, Throwable t) {
                Toast.makeText(RewardsActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void redeemReward(Reward reward) {

        SharedPreferences prefs = getSharedPreferences("SharpRewards", MODE_PRIVATE);
        String userId = prefs.getString("userId", "");

        if (userId.isEmpty()) {
            Toast.makeText(this, "User ID missing! Login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        RedeemRequest req = new RedeemRequest(userId, reward._id);
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.redeemReward(req).enqueue(new Callback<RedeemResponse>() {
            @Override
            public void onResponse(Call<RedeemResponse> call, Response<RedeemResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    RedeemResponse res = response.body();

                    Toast.makeText(RewardsActivity.this,
                            "Redeemed! Code: " + res.couponCode,
                            Toast.LENGTH_LONG).show();

                } else {

                    try {
                        String error = response.errorBody().string();

                        if (error.contains("Not enough tokens"))
                            Toast.makeText(RewardsActivity.this, "Not enough tokens!", Toast.LENGTH_SHORT).show();

                        else if (error.contains("Invalid user or reward"))
                            Toast.makeText(RewardsActivity.this, "Reward unavailable!", Toast.LENGTH_SHORT).show();

                        else
                            Toast.makeText(RewardsActivity.this, "Redeem failed!", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(RewardsActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RedeemResponse> call, Throwable t) {
                Toast.makeText(RewardsActivity.this,
                        "Network Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
