package com.example.sharprewards.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sharprewards.R;
import com.example.sharprewards.adapters.LeaderboardAdapter;
import com.example.sharprewards.api.ApiService;
import com.example.sharprewards.api.RetrofitClient;
import com.example.sharprewards.models.LeaderboardEntry;
import com.example.sharprewards.models.GlobalLeaderboardEntry;
import com.example.sharprewards.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeaderboardActivity extends AppCompatActivity {

    Spinner filterSpinner;
    RecyclerView leaderboardList;

    String area, city, country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        filterSpinner = findViewById(R.id.filterSpinner);
        leaderboardList = findViewById(R.id.leaderboardList);

        leaderboardList.setLayoutManager(new LinearLayoutManager(this));

        SharedPreferences prefs = getSharedPreferences("SharpRewards", MODE_PRIVATE);
        area = prefs.getString("area", "Unknown");
        city = prefs.getString("city", "Unknown");
        country = prefs.getString("country", "Unknown");

        setupSpinner();
    }

    private void setupSpinner() {
        String[] filters = {"Global", "Area", "City", "Country"};

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, filters);

        filterSpinner.setAdapter(adapter);

        filterSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int pos, long id) {
                switch (pos) {
                    case 0: loadGlobal(); break;
                    case 1: loadArea(); break;
                    case 2: loadCity(); break;
                    case 3: loadCountry(); break;
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });
    }

    // ---------------- GLOBAL -------------------

    private void loadGlobal() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getGlobalLeaderboard().enqueue(new Callback<List<GlobalLeaderboardEntry>>() {
            @Override
            public void onResponse(Call<List<GlobalLeaderboardEntry>> call,
                                   Response<List<GlobalLeaderboardEntry>> response) {

                Log.d("GLOBAL_DEBUG", "Response: " + new Gson().toJson(response.body()));

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(LeaderboardActivity.this, "Failed to load Global leaderboard", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Convert global → normal model
                List<LeaderboardEntry> converted = new ArrayList<>();

                for (GlobalLeaderboardEntry g : response.body()) {
                    LeaderboardEntry e = new LeaderboardEntry();
                    e.user = g._id;          // user object
                    e.tokensEarned = g.totalTokens;
                    converted.add(e);
                }

                leaderboardList.setAdapter(new LeaderboardAdapter(converted));
            }

            @Override
            public void onFailure(Call<List<GlobalLeaderboardEntry>> call, Throwable t) {
                Log.e("GLOBAL_ERROR", "FAILED", t);
                Toast.makeText(LeaderboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ---------------- AREA -------------------

    private void loadArea() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getAreaLeaderboard(area).enqueue(defaultCallback);
    }

    private void loadCity() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getCityLeaderboard(city).enqueue(defaultCallback);
    }

    private void loadCountry() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        api.getCountryLeaderboard(country).enqueue(defaultCallback);
    }

    // ---------------- DEFAULT CALLBACK -------------------

    Callback<List<LeaderboardEntry>> defaultCallback = new Callback<List<LeaderboardEntry>>() {
        @Override
        public void onResponse(Call<List<LeaderboardEntry>> call, Response<List<LeaderboardEntry>> response) {

            Log.d("LEADERBOARD_DEBUG", "Response code: " + response.code());
            Log.d("LEADERBOARD_DEBUG", "Raw: " + new Gson().toJson(response.body()));

            if (response.isSuccessful() && response.body() != null) {
                leaderboardList.setAdapter(new LeaderboardAdapter(response.body()));
            } else {
                Toast.makeText(LeaderboardActivity.this, "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<List<LeaderboardEntry>> call, Throwable t) {
            Log.e("LEADERBOARD_ERROR", "FAILED", t);
            Toast.makeText(LeaderboardActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    };
}
