package com.gullafshan.workoutplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    EditText etName, etUsername, etPassword, etConfirm;
    Button btnRegister;
    TextView tvGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        etName = findViewById(R.id.et_name);
        etUsername = findViewById(R.id.et_username_register);
        etPassword = findViewById(R.id.et_password_register);
        etConfirm = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        tvGoToLogin = findViewById(R.id.tv_go_to_login);

        btnRegister.setOnClickListener(v -> registerUser());
        tvGoToLogin.setOnClickListener(v -> {
            // open LoginActivity in front of user
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
        });
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String username = etUsername.getText().toString().trim();
        String pw = etPassword.getText().toString().trim();
        String cpw = etConfirm.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || pw.isEmpty() || cpw.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pw.equals(cpw)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pw.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save to SharedPreferences (simple local storage)
        SharedPreferences sp = getSharedPreferences("auth_prefs", MODE_PRIVATE);
        sp.edit()
                .putString("saved_name", name)
                .putString("saved_username", username)
                .putString("saved_password", pw)
                .apply();

        Toast.makeText(this, "Registration successful. Please login.", Toast.LENGTH_SHORT).show();

        // open LoginActivity
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(i);
        // finish() optional if you don't want user to go back to registration with back button
        finish();
    }
}
