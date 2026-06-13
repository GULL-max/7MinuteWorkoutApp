package com.gullafshan.workoutplans;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
// The unused import statement 'import android.view.View;' is removed.
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Hard-coded credentials for login check (Requirement 1)
    // FIX: Changing to dictionary words to clear the Typo warnings
    private static final String HARDCODED_USERNAME = "TRAINER";
    private static final String HARDCODED_PASSWORD = "PASSWORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link the Java logic to the XML layout file
        setContentView(R.layout.activity_main);

        // 2. Initialize UI components (finding them by ID from the XML)
        EditText usernameEditText = findViewById(R.id.et_username);
        EditText passwordEditText = findViewById(R.id.et_password);
        Button loginButton = findViewById(R.id.btn_login);
        TextView registerTextView = findViewById(R.id.tv_register);
        TextView forgotPasswordTextView = findViewById(R.id.tv_forgot_password);

        // 3. Login Button Click Listener (Handles authentication and success)
        // Lambda syntax fixes the anonymous OnClickListener warnings
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Check against hard-coded credentials (using the corrected constant)
            if (username.equals(HARDCODED_USERNAME) && password.equals(HARDCODED_PASSWORD)) {
                // Success Path (Requirement 9)
                Toast.makeText(MainActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Navigate to Landing Screen (Requirement 8)
                Intent intent = new Intent(MainActivity.this, LandingActivity.class);
                startActivity(intent);

                // finish() removes this activity from the stack (Requirement 7)
                finish();
            } else {
                // Failure Path
                Toast.makeText(MainActivity.this, "Invalid Username or Password.", Toast.LENGTH_LONG).show();
            }
        });

        // 4. Register Link Listener (Requirement 10)
        registerTextView.setOnClickListener(v -> {
            // Navigate to Registration Activity (Requirement 8)
            Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // 5. Forgot Password Link Listener (Requirement 10)
        forgotPasswordTextView.setOnClickListener(v -> {
            // Navigate to Forgot Password Activity (Requirement 8)
            Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }
}

