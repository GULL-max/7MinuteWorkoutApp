package com.gullafshan.workoutplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    public static final String PREFS_NAME = "auth_prefs";
    public static final String KEY_SAVED_USERNAME = "saved_username";
    public static final String KEY_SAVED_PASSWORD = "saved_password";

    // Hard-coded fallback username (same as LoginActivity)
    private static final String DEFAULT_USER = "testuser";

    private EditText etNewPassword, etConfirmPassword;
    private Button btnReset;
    private String usernameFromIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnReset = findViewById(R.id.btnResetPassword);

        // Get username from the previous screen (ForgotPasswordActivity)
        Intent intent = getIntent();
        usernameFromIntent = intent.getStringExtra("username");

        btnReset.setOnClickListener(v -> doReset());
    }

    private void doReset() {
        String newPass = etNewPassword.getText().toString().trim();
        String confirm = etConfirmPassword.getText().toString().trim();

        if (newPass.isEmpty() || confirm.isEmpty()) {
            Toast.makeText(this, "Please enter and confirm the new password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirm)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedUsername = sp.getString(KEY_SAVED_USERNAME, null);

        // Allow reset if the username exists or matches the default username
        if ((savedUsername != null && savedUsername.equals(usernameFromIntent)) ||
                DEFAULT_USER.equals(usernameFromIntent)) {

            sp.edit()
                    .putString(KEY_SAVED_USERNAME, usernameFromIntent)
                    .putString(KEY_SAVED_PASSWORD, newPass)
                    .apply();

            Toast.makeText(this, "Password reset successful. Please login with the new password.", Toast.LENGTH_SHORT).show();

            // Redirect to LoginActivity
            Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();

        } else {
            Toast.makeText(this, "Username not found. Cannot reset password.", Toast.LENGTH_SHORT).show();
        }
    }
}
