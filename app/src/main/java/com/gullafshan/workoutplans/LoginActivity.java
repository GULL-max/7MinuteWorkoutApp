package com.gullafshan.workoutplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    TextView tvForgot, tvRegister;

    private static final String DEFAULT_USER = "testuser";
    private static final String DEFAULT_PASS = "testpass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgot = findViewById(R.id.tvForgotPassword);
        tvRegister = findViewById(R.id.tvRegisterLink);

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);

        // If already logged in, skip login
        boolean loggedIn = prefs.getBoolean("isLoggedIn", false);
        if (loggedIn) {
            startActivity(new Intent(this, LandingActivity.class));
            finish();
        }

        btnLogin.setOnClickListener(v -> attemptLogin());
        tvForgot.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class))
        );
        tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class))
        );
    }

    private void attemptLogin() {
        String u = etUsername.getText().toString().trim();
        String p = etPassword.getText().toString().trim();

        if (u.isEmpty() || p.isEmpty()) {
            Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String savedUser = prefs.getString("saved_username", null);
        String savedPass = prefs.getString("saved_password", null);

        boolean valid = false;

        // Check registered user
        if (savedUser != null && savedPass != null && savedUser.equals(u) && savedPass.equals(p)) {
            valid = true;
        }
        // Check default login
        else if (DEFAULT_USER.equals(u) && DEFAULT_PASS.equals(p)) {
            valid = true;
        }

        if (valid) {
            prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("loggedInUser", u)
                    .apply();

            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }
}

