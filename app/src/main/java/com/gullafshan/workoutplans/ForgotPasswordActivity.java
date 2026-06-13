package com.gullafshan.workoutplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etForgotUsername;
    private Button btnForgotContinue;

    public static final String PREFS = "auth_prefs";
    public static final String KEY_SAVED_USERNAME = "saved_username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etForgotUsername = findViewById(R.id.etForgotUsername);
        btnForgotContinue = findViewById(R.id.btnForgotContinue);

        btnForgotContinue.setOnClickListener(v -> {
            String username = etForgotUsername.getText().toString().trim();
            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter your username/email", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
            String savedUser = sp.getString(KEY_SAVED_USERNAME, null);

            // allow reset if saved user exists or if user enters default hard-coded username
            if ((savedUser != null && savedUser.equals(username)) ||
                    "testuser".equals(username)) {
                Intent i = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                i.putExtra("username", username);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(this, "Username not found", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
