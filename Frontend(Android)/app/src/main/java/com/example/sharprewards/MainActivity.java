package com.example.sharprewards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.sharprewards.activities.ChallengeActivity;
import com.example.sharprewards.activities.LeaderboardActivity;
import com.example.sharprewards.activities.RewardsActivity;

public class MainActivity extends AppCompatActivity {

    TextView welcomeText, tokensText, streakText;
    Button challengeBtn, leaderboardBtn, rewardsBtn;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcomeText = findViewById(R.id.welcomeText);
        tokensText = findViewById(R.id.tokensText);
        streakText = findViewById(R.id.streakText);

        challengeBtn = findViewById(R.id.challengeBtn);
        leaderboardBtn = findViewById(R.id.leaderboardBtn);
        rewardsBtn = findViewById(R.id.rewardsBtn);

        prefs = getSharedPreferences("SharpRewards", MODE_PRIVATE);

        loadUserData(); // initial load

        challengeBtn.setOnClickListener(v ->
                startActivity(new Intent(this, ChallengeActivity.class)));

        leaderboardBtn.setOnClickListener(v ->
                startActivity(new Intent(this, LeaderboardActivity.class)));

        rewardsBtn.setOnClickListener(v ->
                startActivity(new Intent(this, RewardsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();   // refresh values when coming back
    }

    private void loadUserData() {
        String name = prefs.getString("name", "User");
        int tokens = prefs.getInt("tokens", 0);
        int streak = prefs.getInt("streak", 0);

        welcomeText.setText("Welcome, " + name);
        tokensText.setText("Tokens: " + tokens);
        streakText.setText("Streak: " + streak);
    }
}
