package com.example.sharprewards.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sharprewards.MainActivity;
import com.example.sharprewards.R;
import com.example.sharprewards.api.ApiService;
import com.example.sharprewards.api.RetrofitClient;
import com.example.sharprewards.models.BasicResponse;
import com.example.sharprewards.models.LoginRequest;
import com.example.sharprewards.models.LoginResponse;
import com.example.sharprewards.models.RegisterRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    EditText nameInput, emailInput, passwordInput, areaInput, cityInput, countryInput;
    Button registerBtn;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        areaInput = findViewById(R.id.areaInput);
        cityInput = findViewById(R.id.cityInput);
        countryInput = findViewById(R.id.countryInput);
        registerBtn = findViewById(R.id.registerBtn);
        loader = findViewById(R.id.loader);

        findViewById(R.id.goToLoginBtn).setOnClickListener(v -> finish());

        registerBtn.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        loader.setVisibility(View.VISIBLE);

        RegisterRequest req = new RegisterRequest(
                nameInput.getText().toString(),
                emailInput.getText().toString(),
                passwordInput.getText().toString(),
                areaInput.getText().toString(),
                cityInput.getText().toString(),
                countryInput.getText().toString()
        );

        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.register(req).enqueue(new Callback<BasicResponse>() {
            @Override
            public void onResponse(Call<BasicResponse> call, Response<BasicResponse> response) {
                loader.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Signup successful!", Toast.LENGTH_SHORT).show();

                    // 🔥 Auto-login immediately after signup
                    autoLogin(req.email, req.password);

                } else {
                    Toast.makeText(SignUpActivity.this, "Signup failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(SignUpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void autoLogin(String email, String password) {
        ApiService api = RetrofitClient.getClient().create(ApiService.class);

        api.login(new LoginRequest(email, password)).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse result = response.body();

                    SharedPreferences prefs = getSharedPreferences("SharpRewards", MODE_PRIVATE);
                    prefs.edit()
                            .putString("token", result.token)
                            .putString("userId", result.user._id)
                            .putString("name", result.user.name)
                            .putInt("tokens", result.user.tokens)
                            .putInt("streak", result.user.streak)
                            .putString("area", result.user.area)
                            .putString("city", result.user.city)
                            .putString("country", result.user.country)
                            .apply();


                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "Auto-login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
