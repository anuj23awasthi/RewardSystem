package com.example.sharprewards.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharprewards.R;
import com.example.sharprewards.api.ApiService;
import com.example.sharprewards.api.RetrofitClient;
import com.example.sharprewards.models.ChallengeSubmitRequest;
import com.example.sharprewards.models.ChallengeSubmitResponse;
import com.example.sharprewards.models.TodayChallengeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChallengeActivity extends AppCompatActivity {

    TextView questionText;
    EditText answerInput;
    Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        questionText = findViewById(R.id.questionText);
        answerInput = findViewById(R.id.answerInput);
        submitBtn = findViewById(R.id.submitBtn);

        loadQuestion();

        submitBtn.setOnClickListener(v -> submitAnswer());
    }

    private void loadQuestion() {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.getChallenge().enqueue(new Callback<TodayChallengeResponse>() {
            @Override
            public void onResponse(Call<TodayChallengeResponse> call, Response<TodayChallengeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionText.setText(response.body().question);
                } else {
                    questionText.setText("Failed to load question");
                }
            }

            @Override
            public void onFailure(Call<TodayChallengeResponse> call, Throwable t) {
                questionText.setText("Error: " + t.getMessage());
            }
        });
    }

    private void submitAnswer() {
        String ans = answerInput.getText().toString();

        String userId = getSharedPreferences("SharpRewards", MODE_PRIVATE)
                .getString("userId", "");

        ChallengeSubmitRequest req = new ChallengeSubmitRequest(userId, Integer.parseInt(ans));

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.submitChallenge(req)
                .enqueue(new Callback<ChallengeSubmitResponse>() {
                    @Override
                    public void onResponse(Call<ChallengeSubmitResponse> call, Response<ChallengeSubmitResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            ChallengeSubmitResponse resp = response.body();

                            // SHOW ALREADY SUBMITTED MESSAGE
                            if (resp.msg != null && resp.msg.contains("Already")) {
                                Toast.makeText(ChallengeActivity.this,
                                        "Already submitted today",
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }

                            // NORMAL FLOW
                            Toast.makeText(ChallengeActivity.this,
                                    resp.correct ? "Correct!" : "Wrong!",
                                    Toast.LENGTH_SHORT).show();

                            Log.d("SUBMIT_DEBUG", "userId sending = " + userId);

                        } else {
                            Toast.makeText(ChallengeActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ChallengeSubmitResponse> call, Throwable t) {
                        Toast.makeText(ChallengeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
