package com.gullafshan.workoutplans;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // --- FIX STARTS HERE ---
                // 1. Get the text from the EditText fields
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                // --- FIX ENDS HERE ---

                // Check if fields are empty
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check for the correct credentials
                // As you can see, the correct credentials are "user" and "password"
                if (username.equals("user") && password.equals("password")) {
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    // Navigate to the LandingActivity
                    Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish(); // Finish LoginActivity so the user can't go back to it
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
