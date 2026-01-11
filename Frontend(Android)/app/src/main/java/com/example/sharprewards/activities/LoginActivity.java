package com.example.sharprewards.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharprewards.MainActivity;
import com.example.sharprewards.R;
import com.example.sharprewards.api.ApiService;
import com.example.sharprewards.api.RetrofitClient;
import com.example.sharprewards.models.LoginRequest;
import com.example.sharprewards.models.LoginResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText emailInput, passwordInput;
    Button loginBtn;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.loginBtn);
        loader = findViewById(R.id.loader);

        TextView goToSignup = findViewById(R.id.goToSignup);
        goToSignup.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class))
        );

        loginBtn.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        loader.setVisibility(View.VISIBLE);

        ApiService api = RetrofitClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(email, password);

        api.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loader.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse result = response.body();

                    Toast.makeText(LoginActivity.this, result.msg, Toast.LENGTH_SHORT).show();

                    // SAVE USER DATA (FIXED)
                    SharedPreferences prefs = getSharedPreferences("SharpRewards", MODE_PRIVATE);
                    prefs.edit()
                            .putString("token", result.token)
                            .putString("userId", result.user._id)
                            .putString("name", result.user.name)

                            .putString("area", result.user.area)        // ADD
                            .putString("city", result.user.city)        // ADD
                            .putString("country", result.user.country)  // ADD

                            .putInt("tokens", result.user.tokens)
                            .putInt("streak", result.user.streak)
                            .apply();



                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                    Log.d("LOGIN_DEBUG", "USER ID = " + result.user._id);
                    Log.d("LOGIN_DEBUG", "FULL RESPONSE = " + new Gson().toJson(result));


                } else {
                    Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loader.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
