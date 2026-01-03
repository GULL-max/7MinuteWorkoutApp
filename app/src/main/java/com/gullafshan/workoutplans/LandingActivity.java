package com.gullafshan.workoutplans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LandingActivity extends AppCompatActivity {

    public static final String PREFS = "auth_prefs";
    public static final String KEY_SAVED_USERNAME = "saved_username";
    public static final String KEY_IS_LOGGED_IN = "is_logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        // Check login state
        SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
        boolean isLoggedIn = sp.getBoolean(KEY_IS_LOGGED_IN, false);
        if (!isLoggedIn) {
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return;
        }

        // Initialize views
        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvLandingInstructions = findViewById(R.id.tvLandingInstructions);
        Button btnStartWorkout = findViewById(R.id.btnStartWorkout);
        Button btnApiData = findViewById(R.id.btnApiData);
        Button btnNotes = findViewById(R.id.btnNotes);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Retrieve username
        String username = sp.getString(KEY_SAVED_USERNAME, "Guest");

        // Set welcome message
        tvWelcome.setText(getString(R.string.welcome_with_name, username));

        // Set instructions text
        tvLandingInstructions.setText(getString(R.string.landing_instructions));

        // Open Workout Timer
        btnStartWorkout.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, WorkoutTimerActivity.class));
        });

        // Open API Data placeholder
        btnApiData.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, ApiDataActivity.class));
        });

        // Open Notes / Quiz placeholder
        btnNotes.setOnClickListener(v -> {
            startActivity(new Intent(LandingActivity.this, NotesActivity.class));
        });

        // Logout → clear task stack & return to Login
        btnLogout.setOnClickListener(v -> {
            sp.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply();
            Intent i = new Intent(LandingActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });
    }
}
